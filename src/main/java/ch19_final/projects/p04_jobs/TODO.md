# Projet 4 — Les rappels en arrière-plan : la concurrence en conditions réelles

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 19) :**
- parler à un **service externe** lent et capricieux, à travers un adaptateur ;
- **réessayer** ce qui peut passer, avec un **recul exponentiel**, et pas ce qui ne passera jamais ;
- l'**interruption** d'un fil : la respecter et la transmettre ;
- un **pool borné** avec une **file bornée** : refuser tout de suite plutôt que s'effondrer (la contre-pression) ;
- `CompletableFuture` : travail en arrière-plan, **délai maximal**, erreurs emballées ;
- l'**idempotence** : le même rappel demandé deux fois n'est envoyé qu'une fois ;
- l'**arrêt propre** d'un programme qui travaille ;
- des **tests de concurrence déterministes** : des verrous (`CountDownLatch`), jamais des `sleep`.

**Ce qui est FOURNI :** `Data.java` contient `REMINDERS` (les 12 rappels du jour : tâche, téléphone, texte) et `Operator`, l'opérateur SMS : 50 ms par message, « occupé » de temps en temps, et qui refuse les numéros inconnus. Lis son commentaire : ses caprices dépendent du numéro, pour que la démonstration donne toujours le même résultat. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch19_final.projects.p04_jobs` : `Notification`, `GatewayException`, `SmsGateway`, `OperatorGateway`, `Sleeper`, `RetryPolicy`, `Retrier`, `Metrics`, `SendResult`, `ReminderService`, `ReminderDemo`, et tes tests (par exemple `RetrierTest` et `ReminderServiceTest`).

**Règle du crescendo :** tout Java 17 (le chapitre 13 pour les fils), JUnit et Mockito. Aucune méthode de plus de **15 lignes**. Ni `Executors.newCachedThreadPool`, ni `LinkedBlockingQueue` (sans borne), ni `printStackTrace`. `Retrier` et `ReminderService` ne contiennent jamais `Thread.sleep` ; `ReminderService` ne connaît pas `Data`. Dans tes tests : ni `System.out`, ni `Thread.sleep`, ni `Sleeper.REAL`.

---

## Tableau de bord

### ☐ Étape 1 — L'opérateur, derrière un port

**📖 La leçon : deux sortes d'échecs.** Un service externe échoue de deux façons très différentes :
- **passagère** : « occupé », « délai dépassé », « réessayez plus tard ». Dans une seconde, ça passera peut-être : **réessayer** a un sens ;
- **définitive** : « numéro inconnu », « refusé », « mal formé ». Réessayer ne fera que charger le service pour rien, et retarder l'erreur.

L'adaptateur (chapitre 18) traduit les erreurs du service en **une** exception à nous, qui dit dans quelle catégorie on est.

**📖 La leçon : l'interruption.** Quand on veut arrêter un fil (à l'arrêt du programme, par exemple), on l'**interrompt** : `thread.interrupt()` lève un **drapeau**, et les méthodes qui attendent (`Thread.sleep`, `await`, `join`…) lancent aussitôt une `InterruptedException`, **en baissant le drapeau**. Celui qui attrape cette exception sans la relancer doit **relever** le drapeau (`Thread.currentThread().interrupt()`) : sinon la demande d'arrêt est perdue, et le code plus haut continue comme si de rien n'était.

**👉 À toi :**
- `public record Notification(long taskId, String phone, String text)` ;
- `public class GatewayException extends RuntimeException`, constructeur `(String message, boolean retryable)`, et `boolean retryable()` ;
- `@FunctionalInterface public interface SmsGateway` : `String send(String phone, String text)` (rend l'identifiant du message) ;
- `public final class OperatorGateway implements SmsGateway`, constructeur `(Data.Operator operator)` : `IllegalStateException` (occupé) → `GatewayException` **réessayable** avec le même message ; `IllegalArgumentException` (inconnu) → **non** réessayable ; `InterruptedException` → relève le drapeau, et `GatewayException("interrompu", false)`.

**🧪 Les tests :** avec un vrai `Data.Operator` : `0612345670` → `SMS-1` ; `0612345671` est occupé une fois (réessayable), puis passe (`SMS-2`) ; `0512345674` : `numero inconnu : 0512345674`, non réessayable. Puis l'interruption : appelle `Thread.currentThread().interrupt()` **avant** `send` (le `sleep` de l'opérateur lancera alors tout de suite `InterruptedException`) : `interrompu`, et `Thread.currentThread().isInterrupted()` est vrai. Baisse le drapeau à la fin (`Thread.interrupted()`), sinon il gênerait les tests suivants.

**❓ Question :** si l'adaptateur attrapait l'`InterruptedException` sans relever le drapeau, que se passerait-il au moment d'arrêter le programme (étape 8) ?

### ☐ Étape 2 — Combien attendre : le recul exponentiel

**📖 La leçon : laisser respirer un service saturé.** Si 1 000 clients réessaient **aussitôt** un service qui sature, ils l'achèvent. On attend donc **de plus en plus** entre deux essais : 100 ms, puis 200, 400, 800… (le **recul exponentiel**, *exponential backoff*), avec un **plafond** (sinon on attendrait des heures). En vrai, on ajoute aussi un peu de **hasard** (*jitter*) à chaque attente : sinon, 1 000 clients tombés en panne au même instant réessaient tous **au même instant**, encore et encore.

**📖 La leçon : attendre est une dépendance.** Comme l'heure (chapitre 18), le temps qui passe s'**injecte**. Un test qui dort vraiment 100 + 200 + 400 ms est lent, et ne vérifie pas les durées. Une interface `Sleeper` : en production, elle dort ; dans les tests, elle **note** les durées demandées, sans attendre.

**👉 À toi :**
- `@FunctionalInterface public interface Sleeper` : `void sleep(Duration duration) throws InterruptedException`, et une constante `Sleeper REAL = duration -> Thread.sleep(duration.toMillis());` ;
- `public record RetryPolicy(int maxAttempts, Duration firstDelay, double multiplier, Duration maxDelay)` : `maxAttempts < 1` → `IllegalArgumentException("au moins 1 essai : 0")`, `multiplier < 1` → `"multiplicateur inferieur a 1 : 0.5"` ; `Duration delayAfter(int failures)` : l'attente après le n-ième échec, `firstDelay × multiplier^(n-1)` arrondi à la milliseconde (`Math.round`), sans dépasser `maxDelay`.

**🧪 Les tests :** un `@ParameterizedTest` pour 100 ms × 2 plafonné à 1 s (échecs 1, 2, 3, 4, 5 et 10) ; un multiplicateur de 1,5 (100, 150, 225, 338) et de 1 ; les deux refus.

### ☐ Étape 3 — Réessayer

**👉 À toi :** `public final class Retrier`, constructeur `(RetryPolicy policy, Sleeper sleeper, Metrics metrics)` (crée `Metrics` à l'étape 4, ou commence par une version vide), et `<T> T call(Supplier<T> action)` :
- rend le résultat dès qu'un essai réussit ;
- une `GatewayException` **non** réessayable, ou l'échec du **dernier** essai permis : on la relance ;
- sinon : on compte un nouvel essai (`metrics`), on attend `delayAfter(nombre d'échecs)` avec le `Sleeper`, et on recommence ;
- l'exception relancée porte les échecs **précédents** en `suppressed` (`addSuppressed`), dans l'ordre : rien ne se perd pour celui qui enquête ;
- une interruption pendant l'attente : relève le drapeau, et lance `GatewayException("interrompu pendant l'attente", false)`, avec les échecs précédents en `suppressed` ;
- toute autre exception (un bug, une `NullPointerException`) n'est **pas** réessayée.

**🧪 Les tests (`RetrierTest`) :** le `Sleeper` est `sleeps::add` (une `List<Duration>`) ; un appel qui échoue « occupe 1 », « occupe 2 »… n fois puis réussit : 0 échec (aucune attente), 2 échecs (attentes `[100, 200]`, 2 essais de plus), 3 échecs (le dernier essai réussit) ; 4 échecs sur 4 essais (le message `occupe 4`, les `suppressed` `[occupe 1, occupe 2, occupe 3]`, les attentes `[100, 200, 400]`) ; non réessayable (1 appel, aucune attente) ; réessayable puis non réessayable ; `maxAttempts = 1` ; une `NullPointerException` ; un `Sleeper` qui lance `InterruptedException`.

**❓ Question :** pourquoi ne pas réessayer **toutes** les exceptions, `NullPointerException` comprise ?

### ☐ Étape 4 — Compter sans se bloquer

**📖 La leçon : des compteurs partagés.** Plusieurs fils comptent les envois en même temps. `count++` sur un `long` perd des additions (chapitre 13). `AtomicLong` est juste, mais sous forte concurrence, tous les fils se battent pour **la même** case mémoire. `LongAdder` donne à chaque fil sa propre case, et additionne seulement quand on **lit** : parfait pour des statistiques, beaucoup écrites et peu lues.

**👉 À toi :** `public final class Metrics` : cinq `LongAdder` (envoyés, essais en plus, échecs, refus, délais dépassés), des méthodes (accès paquet) `sent()`, `retry()`, `failed()`, `rejected()`, `timedOut()`, et `Snapshot snapshot()`, où `public record Snapshot(long sent, long retries, long failed, long rejected, long timedOut)` est imbriqué dans `Metrics`.

### ☐ Étape 5 — Le pool borné : refuser plutôt que s'effondrer

**📖 La leçon : la contre-pression.** Un `Executors.newFixedThreadPool(3)` a 3 fils… et une file d'attente **sans limite**. Si les rappels arrivent plus vite qu'on ne les envoie, la file grandit, la mémoire aussi, les délais deviennent absurdes, puis le programme tombe. Un système solide **borne** sa file, et quand elle est pleine, **refuse tout de suite** : l'appelant sait qu'il doit ralentir ou réessayer plus tard (c'est la **contre-pression**, *backpressure* ; en HTTP, un 503 ou un 429).

```java
new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
        new ArrayBlockingQueue<>(queueCapacity), new ThreadPoolExecutor.AbortPolicy());
```

Avec `AbortPolicy`, un travail qui ne trouve ni fil libre ni place dans la file fait lancer `RejectedExecutionException` par `execute` (et donc par `CompletableFuture.supplyAsync(…, executor)`).

**👉 À toi :** `public record SendResult(long taskId, String status, String detail)` ; `public final class ReminderService implements AutoCloseable`, constructeur `(SmsGateway gateway, Retrier retrier, int threads, int queueCapacity, Duration timeout, Metrics metrics)` :
- `CompletableFuture<SendResult> send(Notification n)` : l'envoi (avec le `Retrier`) part en arrière-plan sur le pool (`supplyAsync`) ; réussi : `"envoye"`, avec l'identifiant du message en détail ; une `GatewayException` : `"echec"`, avec son message ; pool et file pleins : un futur **déjà terminé** `("refuse", "file pleine")` ;
- `List<SendResult> sendAll(List<Notification> notifications)` : envoie tout, attend tout (`CompletableFuture.allOf(...).join()`), et rend les résultats **triés par tâche** ;
- `close()` : `shutdownNow()`.

**📖 La leçon : tester la concurrence sans dormir.** « J'attends 100 ms, le fil aura sûrement démarré » : faux sur une machine chargée, et lent partout. On **contrôle** les fils avec des verrous : une passerelle de test qui attend un feu vert (`CountDownLatch release`) et signale qu'elle a commencé (`CountDownLatch started`). Le test sait alors **exactement** où en est chaque fil.

**🧪 Les tests (`ReminderServiceTest`) :** une passerelle **rapide** écrite à la main (« 06…7 » toujours occupé, « 05… » inconnu) : `sendAll` sur 5 rappels dans le désordre (les résultats triés, les métriques) ; une passerelle **bloquante** : 1 fil et une file de 2 places, le premier envoi bloqué (`started.await`), deux en attente, le quatrième **refusé** tout de suite (`isDone()` vrai) ; puis le feu vert, et les trois envoyés. Le `@AfterEach` donne le feu vert et ferme le service.

**❓ Question :** avec 1 fil et une file de 2 places, pourquoi faut-il attendre `started` **avant** d'envoyer les trois suivants ? Que pourrait-il se passer sinon ?

### ☐ Étape 6 — Le délai maximal, et les erreurs emballées

**📖 La leçon : `orTimeout`.** Un opérateur bloqué ne doit pas bloquer l'appelant pour toujours. `future.orTimeout(500, TimeUnit.MILLISECONDS)` termine le futur en erreur (`TimeoutException`) si le résultat n'est pas arrivé à temps. **Attention :** cela abandonne **l'attente**, pas le **travail**. Le fil qui envoie continue, et le SMS partira peut-être quand même, plus tard.

**📖 La leçon : les erreurs emballées.** Une exception lancée **dans** le travail d'un `CompletableFuture` arrive dans `exceptionally` **emballée** dans une `CompletionException` (la vraie est sa `getCause()`). Une `TimeoutException` d'`orTimeout`, elle, arrive telle quelle. On **déballe** donc avant de décider.

**👉 À toi :** dans `send`, `.orTimeout(timeout)` puis `.exceptionally(…)` : une `TimeoutException` donne `("delai depasse", "100 ms")` (le délai en millisecondes) et compte un délai dépassé ; une autre erreur, `("echec", message de la vraie cause)`.

**🧪 Les tests :** un délai de 100 ms avec la passerelle bloquante : `delai depasse` ; **puis** le feu vert : l'envoi s'est fait quand même (un `CountDownLatch finished` dans la passerelle).

**❓ Question :** l'expérience montre qu'un délai dépassé n'empêche pas l'envoi. Quel problème cela pose-t-il si l'appelant, voyant « délai dépassé », redemande le rappel ? Quelle étape de ce projet y répond (en partie) ?

### ☐ Étape 7 — L'idempotence : une seule fois, même demandé deux fois

**📖 La leçon : demander deux fois arrive tout le temps.** Un utilisateur clique deux fois, un client réessaie après un délai dépassé, deux serveurs traitent la même file. Une opération **idempotente** donne le même résultat, qu'on la demande une fois ou dix. Pour un envoi, on retient chaque rappel **accepté** par sa clé (ici `taskId`) : la deuxième demande reçoit **le même futur**, et rien n'est renvoyé.

**📖 Le piège :** « si la clé n'est pas dans la map, l'ajouter » en deux temps (`containsKey` puis `put`) laisse deux fils passer ensemble entre les deux. `ConcurrentHashMap.computeIfAbsent(clé, fonction)` fait les deux d'**un seul geste** : la fonction n'est appelée qu'une fois par clé, même avec 16 fils.

**👉 À toi :** une `ConcurrentHashMap<Long, CompletableFuture<SendResult>>` et `computeIfAbsent` dans `send`. Un **refus** n'est pas retenu (le rappel pourra être redemandé quand la file aura de la place) : `remove(clé, futur)` si le futur rendu est un refus.

**🧪 Les tests :** le même rappel deux fois : `assertSame` sur les deux futurs, et une seule passerelle appelée ; **16 fils** démarrés ensemble par un `CountDownLatch gate` qui demandent tous le même rappel : un seul appel ; un rappel refusé, puis redemandé quand la file s'est vidée : envoyé.

### ☐ Étape 8 — L'arrêt propre, la démonstration, et les mutants

**📖 La leçon : arrêter un programme qui travaille.** Couper net perd les envois en cours. L'arrêt **propre** :
1. `shutdown()` : plus aucun nouveau travail, mais ceux en cours **et** en file continuent ;
2. `awaitTermination(délai de grâce)` : on attend qu'ils finissent ;
3. passé ce délai, `shutdownNow()` **interrompt** les fils (ton étape 1 sert ici) et rend la liste des travaux jamais commencés.

**👉 À toi :**
- `int shutdown(Duration grace) throws InterruptedException` : l'arrêt propre ; rend le nombre de rappels **jamais commencés** (0 si tout a fini à temps) ;
- `public final class ReminderDemo` avec `main` : `Sleeper.REAL`, `RetryPolicy(4, 100 ms, 2, 1 s)`, 3 fils, une file de 20, un délai de 2 s, l'adaptateur sur un `new Data.Operator()` ; envoie les 12 rappels de `REMINDERS` et affiche `tache 5 : echec (numero inconnu : 0512345674)` (le détail seulement si ce n'est pas `envoye`), puis la durée totale en ms, les métriques, et `jamais commences : 0`.

**🧪 Les tests :** un arrêt avec une grâce suffisante (tous envoyés, 0) ; un arrêt avec la passerelle bloquante, 1 fil et 2 rappels en file, grâce de 100 ms : `2` jamais commencés, celui en cours `("echec", "interrompu")`, et les deux autres finissent en `delai depasse` ; un envoi **après** l'arrêt : refusé ; enfin, de bout en bout, les 12 rappels de `Data` avec le vrai opérateur (et un `Sleeper` qui ne dort pas).

**🧪 Expériences :**
- lance `ReminderDemo`. Puis change le nombre de fils : 1, puis 12. Note les durées. Pourquoi 12 fils ne vont-ils pas 12 fois plus vite que 1 ?
- avec 3 fils, remplace la politique par `RetryPolicy(4, Duration.ZERO, 1, Duration.ZERO)` (aucune attente) : la durée, et les résultats ?

**👉 Puis :** lance `Check`. Les 20 mutants touchent le recul, les règles de nouvel essai, les `suppressed`, l'interruption, l'idempotence, la file bornée, le délai, le tri, l'arrêt, l'adaptateur et les métriques. Le `Check` prend environ 40 secondes : les tests attendent de vrais délais (100 à 500 ms) et la démonstration de bout en bout.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Notification(`, `class GatewayException extends RuntimeException`, `interface SmsGateway`, `interface Sleeper`, `record RetryPolicy(`, `final class Metrics`, `LongAdder`, `final class Retrier`, `addSuppressed`, `Thread.currentThread().interrupt()`, `record SendResult(`, `final class ReminderService implements AutoCloseable`, `ThreadPoolExecutor`, `ArrayBlockingQueue`, `RejectedExecutionException`, `CompletableFuture.supplyAsync(`, `orTimeout(`, `exceptionally(`, `computeIfAbsent(`, `ConcurrentHashMap`, `awaitTermination(`, `shutdownNow()`, `final class OperatorGateway implements SmsGateway`, `final class ReminderDemo`, `Data.REMINDERS` ; ni `Executors.newCachedThreadPool`, ni `LinkedBlockingQueue`, ni `printStackTrace`.
- **La conception :** aucune méthode de plus de **15 lignes** ; ni `Retrier.java` ni `ReminderService.java` ne contiennent `Thread.sleep` ; `Retrier.java` ne contient pas `catch (RuntimeException` ; `ReminderService.java` ne contient pas `Data.`.
- **Tes tests :** au moins **25** tests, `CountDownLatch`, `assertSame(`, `getSuppressed()`, `isInterrupted()`, `sendAll(`, `shutdown(Duration`, `assertTimeoutPreemptively(`, `@AfterEach` ; ni `System.out`, ni `Thread.sleep`, ni `Sleeper.REAL`.
- **Les 20 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch19_final.projects.p04_jobs ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 29 tests, 29 reussis
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
