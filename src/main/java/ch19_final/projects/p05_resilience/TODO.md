# Projet 5 — Tenir debout quand tout va mal : limiteur, disjoncteur, percentiles

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 19) :**
- une **horloge manuelle** : tester des minutes de panne en une milliseconde ;
- le **seau à jetons** (*token bucket*) : limiter le débit d'un client, en acceptant les rafales ;
- compter **juste** : des entiers plutôt que des `double` ;
- une **fuite de mémoire** discrète, et son remède ;
- le **disjoncteur** (*circuit breaker*) : fermé, ouvert, demi-ouvert ; une machine à états (chapitre 18) avec le temps ;
- distinguer une **panne** d'une **erreur du client** ;
- mesurer les temps de réponse avec des **percentiles**, parce que la moyenne ment ;
- le **repli** (*fallback*) : une réponse un peu ancienne, qui le dit, plutôt qu'une page d'erreur.

**Ce qui est FOURNI :** `Data.java` contient `START` (10 h pile, le 9 octobre 2026), `STOCK` (trois références de pièces et leur stock) et `PartsSupplier`, le service de stock du fournisseur : il lit l'heure sur l'horloge qu'on lui donne, et il est **en panne de 10:00:05 à 10:00:12**. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch19_final.projects.p05_resilience` : `ManualClock`, `TokenBucket`, `RateLimiter`, `CircuitOpenException`, `CircuitBreaker`, `LatencyRecorder`, `StockClient`, `StockAnswer`, `StockUnavailableException`, `ResilientStock`, `ResilienceDemo`, et tes tests (par exemple `RateLimiterTest`, `CircuitBreakerTest`, `ResilientStockTest`).

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Aucune méthode de plus de **15 lignes**. Le temps ne se lit **que** sur une `Clock` injectée : ni `Thread.sleep`, ni `System.currentTimeMillis`, ni `System.nanoTime`, ni `Instant.now()`. Pas de `double` dans `TokenBucket`. Dans tes tests : ni `System.out`, ni `Thread.sleep`, ni `Clock.system…`.

> Ce que tu écris ici existe dans des bibliothèques (Resilience4j, Bucket4j, Micrometer). Après ce projet, tu sauras les **régler**, ce qui est bien plus rare que de savoir les installer.

---

## Tableau de bord

### ☐ Étape 1 — Une horloge qu'on avance à la main

**📖 La leçon : tester le temps.** Un disjoncteur s'ouvre 30 secondes, un seau se remplit en une seconde : tester cela avec de vraies attentes rendrait les tests lents et fragiles. `Clock.fixed` (chapitre 18) est **arrêtée** : impossible de faire passer le temps. On écrit une horloge qu'on **avance** : `clock.advance(Duration.ofMinutes(5))` fait passer 5 minutes en une instruction.

`java.time.Clock` est une classe abstraite : une sous-classe doit écrire `instant()`, `getZone()` et `withZone(ZoneId)`.

**👉 À toi :** `public final class ManualClock extends Clock`, constructeur `ManualClock(Instant start)` : `void advance(Duration duration)`, `instant()` rend l'instant courant, `getZone()` rend `ZoneOffset.UTC`, `withZone` lance `UnsupportedOperationException`. `advance` et `instant` sont `synchronized` (un fil peut lire pendant qu'un autre avance).

**❓ Question :** pourquoi `ManualClock` est-elle dans le code du projet, et pas seulement dans les tests ?

### ☐ Étape 2 — Le seau à jetons

**📖 La leçon : limiter sans brider.** Un client qui envoie 1 000 requêtes par seconde peut écrouler l'API pour tout le monde. Le **seau à jetons** :
- un seau contient au plus `capacity` jetons, et il est **plein** au départ ;
- chaque requête **prend** un jeton ; pas de jeton : refusée ;
- le seau se **remplit** de `perSecond` jetons par seconde, sans déborder.

Un client peut donc faire une **rafale** de `capacity` requêtes (un humain qui ouvre une page déclenche 5 requêtes d'un coup), puis `perSecond` par seconde en moyenne. On ne remplit pas avec un minuteur : à chaque demande, on calcule ce qui a coulé depuis la dernière fois.

**📖 La leçon : compter juste.** Avec des `double`, ajouter dix fois `0.1` jeton donne `0.9999999999999999`, pas 1 : la requête serait refusée alors qu'elle a droit à son jeton. On compte en **millièmes de jeton**, dans un `long` : une milliseconde à `perSecond` jetons par seconde ajoute exactement `perSecond` millièmes.

**👉 À toi :** `public final class TokenBucket`, constructeur `(int capacity, int perSecond, Clock clock)` (une valeur < 1 : `IllegalArgumentException`) :
- `boolean tryAcquire()` ;
- `Duration timeUntilNext()` : zéro s'il y a un jeton, sinon le temps qu'il manque, **arrondi à la milliseconde supérieure** (1 jeton à 3 par seconde : 334 ms, pas 333) ;
- `boolean isFull()` ;
- le remplissage ajoute les **millisecondes entières** écoulées, et garde le reste : après un pas de 1,5 ms, la demi-milliseconde compte pour le pas suivant (avance la date de référence des millisecondes comptées, pas jusqu'à « maintenant ») ;
- les méthodes publiques sont `synchronized`.

**🧪 Les tests (`RateLimiterTest`) :** une rafale (3 oui, puis non) ; le remplissage à 2 jetons/s (rien à 499 ms, un jeton à 500 ms) ; jamais plus que la capacité, même après une heure ; dix pas de 100 ms donnent **exactement** un jeton ; des pas de 1,5 ms avec un essai à chaque pas : le premier jeton revient au pas **667** ; `timeUntilNext` (250, puis 150 après 100 ms, et 334) ; les refus du constructeur.

**🧪 Expérience :** dans un test jetable, additionne dix fois `0.1` dans un `double`. Le résultat est-il `>= 1` ?

### ☐ Étape 3 — Un seau par client, et la fuite de mémoire

**📖 La leçon : la fuite de mémoire en Java.** Le ramasse-miettes libère ce que **plus personne ne référence**. Une `Map` qui garde un seau pour **chaque** client jamais vu ne libère rien : sur un serveur ouvert à Internet, des millions d'adresses passent, et la mémoire grimpe jusqu'à `OutOfMemoryError`, des semaines plus tard. C'est **la** fuite de mémoire typique de Java : une collection qui ne fait que grandir.

Ici, le remède est élégant : un seau **plein** est exactement un seau **neuf**. L'oublier ne change rien pour le client : s'il revient, il recevra un seau neuf… plein.

**👉 À toi :** `public final class RateLimiter`, constructeur `(int capacity, int perSecond, Clock clock)` :
- `Duration acquire(String client)` : `Duration.ZERO` si la requête passe, sinon le temps d'attente (qu'une API enverrait dans l'en-tête `Retry-After` d'un **429 Too Many Requests**) ; un seau par client, créé à la demande (`computeIfAbsent` sur une `ConcurrentHashMap`) ;
- `int evictFull()` : oublie les seaux pleins, rend combien ; `int clients()`.

**🧪 Les tests :** deux clients indépendants ; l'attente de 1 s, puis 600 ms après 400 ms ; l'oubli des seaux pleins (un seul après 1 s, l'autre après 2 s).

**❓ Question :** qui appellerait `evictFull()` dans une vraie application, et à quel rythme ?

### ☐ Étape 4 — Le disjoncteur

**📖 La leçon : ne pas insister auprès d'un malade.** Le fournisseur est en panne, et chaque appel met 2 secondes à échouer. Si l'on continue d'appeler, **chaque** utilisateur attend 2 secondes pour rien, nos fils sont tous bloqués à attendre, et le fournisseur, assailli, n'arrive pas à redémarrer. Le **disjoncteur**, comme celui du tableau électrique, coupe le circuit :

| État | Ce qui se passe | Transition |
|---|---|---|
| **fermé** (`CLOSED`) | les appels passent | `threshold` échecs **de suite** → ouvert |
| **ouvert** (`OPEN`) | tout est refusé **tout de suite**, sans appeler | après `openFor` → demi-ouvert |
| **demi-ouvert** (`HALF_OPEN`) | **un** appel d'essai passe | réussi → fermé ; raté → ouvert, pour un nouveau `openFor` |

Un succès en état fermé remet le compte des échecs à zéro (« de suite »).

**📖 Le piège : une erreur n'est pas forcément une panne.** « Référence inconnue » prouve que le fournisseur **répond** : ouvrir le disjoncteur pour cela couperait le stock de **toutes** les pièces parce qu'un client a fait une faute de frappe. Le disjoncteur reçoit donc une règle, `Predicate<RuntimeException> isFailure` : les autres exceptions comptent comme une **réponse** (un succès pour lui), et sont relancées telles quelles.

**👉 À toi :**
- `public class CircuitOpenException extends RuntimeException` (constructeur `(String message)`) ;
- `public final class CircuitBreaker`, avec `public enum State { CLOSED, OPEN, HALF_OPEN }`, constructeur `(int threshold, Duration openFor, Clock clock, Predicate<RuntimeException> isFailure)` :
  - `<T> T call(Supplier<T> action)` : ouvert et délai pas encore passé → `CircuitOpenException("circuit ouvert : reessayer dans 4 s")` (le temps restant, **arrondi à la seconde supérieure**) ; à l'instant exact de la fin du délai, l'essai est permis ;
  - `State state()` ;
  - les transitions sont `synchronized`, mais **pas** l'appel lui-même : un appel lent ne doit pas bloquer les autres fils pendant qu'il attend.

**🧪 Les tests (`CircuitBreakerTest`) :** fermé ; 2 échecs puis le 3e ouvre ; un succès remet à zéro ; ouvert : refus **sans** appeler, avec 10 s, puis 4 s après 6,5 s, puis 1 s ; l'essai réussi referme ; l'essai raté rouvre pour 10 s pleines ; cinq « référence inconnue » de suite laissent fermé, et une au milieu d'échecs remet le compte à zéro ; une « référence inconnue » pendant l'essai referme.

**❓ Question :** pourquoi le délai restant est-il arrondi **au-dessus** ? Que lirait l'utilisateur avec un arrondi en dessous, 300 ms avant la fin ?

### ☐ Étape 5 — Un seul essai à la fois

**📖 La leçon : le demi-ouvert est fragile.** Le délai passe, et 50 requêtes arrivent en même temps : si toutes passaient « à l'essai », le fournisseur qui redémarre recevrait 50 appels d'un coup, et retomberait. En demi-ouvert, **un seul** essai passe ; les autres sont refusés (`"circuit ouvert : essai en cours"`) jusqu'à ce qu'il réponde.

**👉 À toi :** un booléen `trialRunning`, levé par l'essai, baissé à sa fin (réussite ou échec).

**🧪 Le test :** un essai lancé dans un autre fil (`CompletableFuture.supplyAsync`) qui attend un feu vert (`CountDownLatch`, comme au projet 4) ; pendant ce temps, l'état est `HALF_OPEN` et un second appel est refusé ; le feu vert donné, l'essai réussit et l'état est `CLOSED`.

### ☐ Étape 6 — Mesurer : les percentiles

**📖 La leçon : la moyenne ment.** 99 appels à 10 ms et un à 5 secondes : la moyenne vaut 59,9 ms, une durée que **personne** n'a vécue. Le **percentile** p dit : « p % des appels ont été au moins aussi rapides ». p50 (la médiane) : l'expérience typique ; p95, p99 : les mauvais jours, ceux dont les utilisateurs se plaignent ; max : le pire. Les engagements de service s'écrivent en percentiles (« p99 sous 300 ms »).

**📖 La méthode du rang le plus proche :** on trie les n mesures ; le percentile p est la valeur de rang `ceil(p / 100 × n)` (en comptant à partir de 1). Sur 10 mesures, p95 est la 10e (`ceil(9,5) = 10`), p11 la 2e.

**📖 La fenêtre glissante :** on garde seulement les `size` **dernières** mesures, dans un tableau **circulaire** : un indice `next` qui revient à 0 après la dernière case (`(next + 1) % size`). La mémoire reste fixe, et les vieilles mesures s'effacent d'elles-mêmes.

**👉 À toi :** `public final class LatencyRecorder`, constructeur `(int size)` : `void record(Duration)` (en millisecondes), `long percentile(double p)` (`p` hors de `]0, 100]` → `IllegalArgumentException("percentile hors de ]0, 100] : 0.0")` ; aucune mesure → `IllegalStateException("aucune mesure")`), `int count()`, et `String summary()` : `n=100 p50=10 ms p95=10 ms p99=10 ms max=5000 ms` (ou `aucune mesure`). Tout est `synchronized`.

**🧪 Les tests :** un `@ParameterizedTest` sur les 10 mesures 10, 20… 100 dans le désordre (p1, p10, p11, p50, p90, p95, p99, p100) ; les 99 + 1 de la leçon ; une fenêtre de 3 après 5 mesures ; les erreurs.

### ☐ Étape 7 — Le stock protégé

**📖 La leçon : le repli.** Quand le fournisseur ne répond pas (panne, ou disjoncteur ouvert), on peut souvent répondre **quand même** : avec la dernière valeur connue, en disant **son âge**. « 14 en stock (il y a 12 s) » vaut mieux qu'une page d'erreur, à condition de ne pas mentir sur la fraîcheur. Sans valeur connue, on le dit clairement.

**👉 À toi :**
- `@FunctionalInterface public interface StockClient` : `int stock(String ref)` ;
- `public record StockAnswer(String ref, int quantity, String source)` : `source` vaut `"fournisseur"`, ou `"cache (12 s)"` (l'âge en secondes entières, au moment de **répondre**) ;
- `public class StockUnavailableException extends RuntimeException`, constructeur `(String ref, RuntimeException cause)`, message `"stock inconnu pour BRAKE-V : " + message de la cause` ;
- `public final class ResilientStock`, constructeur `(StockClient supplier, CircuitBreaker breaker, LatencyRecorder latency, Clock clock)`, méthode `StockAnswer stock(String ref)` : l'appel passe par le disjoncteur ; une réussite met à jour la dernière valeur connue ; une `IllegalArgumentException` (référence inconnue) est **relancée** telle quelle (le cache n'y peut rien) ; toute autre erreur (panne, `CircuitOpenException`) → le repli ; et **dans tous les cas** (`finally`), le temps de réponse est enregistré.

**🧪 Les tests (`ResilientStockTest`) :** un fournisseur de test écrit à la main, qui fait avancer l'horloge (30 ms quand il va bien, 2 s pour échouer en panne) ; la réponse fraîche (30 ms mesurées) ; la panne avec l'âge `12 s` ; le cache garde la **dernière** valeur (7 s, pas 17) ; sans valeur connue ; le disjoncteur ouvert : réponse **sans appel** et **sans attente** (0 ms) ; sans cache, le message dit pourquoi (`circuit ouvert : reessayer dans 30 s`) ; une référence inconnue, trois fois : toujours l'erreur, disjoncteur fermé ; le retour à la normale ; enfin le scénario du vrai `PartsSupplier` : la suite des sources, une lettre par seconde, `FFFFCCCCCCCCCCFFFFFF`, et 14 appels.

**🧪 Expérience :** dans un test jetable, le fournisseur de test en panne, fais 10 demandes avec un seuil de 2, puis avec `Integer.MAX_VALUE` (le disjoncteur ne s'ouvre jamais). Compare le nombre d'appels, la durée totale sur l'horloge, et le résumé des latences.

**❓ Question :** dans le scénario de `PartsSupplier`, l'essai de 10:00:11 échoue alors que la panne finit à 10:00:12. Que se passe-t-il ensuite, et pourquoi le circuit reste-t-il ouvert jusqu'à 10:00:15 ?

### ☐ Étape 8 — La démonstration, et les mutants

**👉 À toi :** `public final class ResilienceDemo` avec `main` : une `ManualClock` à `Data.START`, le `PartsSupplier`, un disjoncteur (3 échecs, 4 s ; une `IllegalArgumentException` n'est pas une panne), une fenêtre de 100 mesures. Le client du fournisseur est une lambda qui fait avancer l'horloge : 30 ms quand il répond, 2 s quand il échoue. Vingt fois : avance d'une seconde, et affiche l'heure de la demande (`HH:mm:ss.SSS`, avec un `DateTimeFormatter` en UTC), la réponse (`CHAIN-11 = 14 (fournisseur)`) et l'état du disjoncteur entre crochets. Puis `appels au fournisseur : …` et le résumé des latences. Enfin un `RateLimiter(5, 1, clock)` et 8 requêtes d'un même client au même instant : affiche l'attente de chacune.

**🧪 Expérience :** lance la démo. Explique les heures : pourquoi la demande qui suit 10:00:05.120 a-t-elle lieu à 10:00:08.120 ?

**👉 Puis :** lance `Check`. Les 20 mutants touchent le seau (le plafond, le reste de milliseconde, l'arrondi), le limiteur (l'oubli, un seau par client), le disjoncteur (le seuil, la remise à zéro, l'essai unique, le demi-ouvert, les erreurs du client, l'arrondi), les percentiles, la fenêtre, le repli et l'horloge.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class ManualClock extends Clock`, `final class TokenBucket`, `final class RateLimiter`, `class CircuitOpenException extends RuntimeException`, `final class CircuitBreaker`, `enum State`, `Predicate<RuntimeException>`, `final class LatencyRecorder`, `interface StockClient`, `record StockAnswer(`, `class StockUnavailableException extends RuntimeException`, `final class ResilientStock`, `final class ResilienceDemo`, `synchronized`, `computeIfAbsent(`, `Arrays.sort(`, `Math.ceil(`, `finally`, `Data.PartsSupplier` ; ni `Thread.sleep`, ni `System.currentTimeMillis`, ni `System.nanoTime`, ni `Instant.now()`.
- **La conception :** aucune méthode de plus de **15 lignes** ; `TokenBucket.java` ne contient pas `double` ; `CircuitBreaker.java` ne contient pas `Thread` ; `ResilientStock.java` ne contient pas `Data.`.
- **Tes tests :** au moins **35** tests, `ManualClock`, `advance(`, `@ParameterizedTest`, `CountDownLatch`, `assertThrows(`, `getMessage()` ; ni `System.out`, ni `Thread.sleep`, ni `Clock.system`.
- **Les 20 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch19_final.projects.p05_resilience ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 38 tests, 38 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 20/20 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
