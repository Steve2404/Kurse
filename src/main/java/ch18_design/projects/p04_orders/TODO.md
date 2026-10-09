# Projet 4 — Les commandes de la boulangerie (inversion des dépendances, injection)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 18) :**
- le principe d'**inversion des dépendances** (le D de SOLID) : le métier ne dépend pas des détails techniques, ce sont les détails qui dépendent du métier ;
- les **ports** (des interfaces écrites pour le métier) et les **adaptateurs** (la base, le mail, la console) : l'architecture **hexagonale** en petit ;
- l'**injection de dépendances par le constructeur**, sans bibliothèque ;
- les dépendances **cachées** : `new` d'un service, singleton statique, `LocalDateTime.now()`, `new Random()`, `System.out` ;
- l'**horloge injectée** (`java.time.Clock`) pour tester le temps ;
- la **racine de composition** et le **test d'intégration** qui branche les vrais adaptateurs.

**Ce qui est FOURNI :** `Data.java` contient `LegacyOrderService`, l'ancien service de commandes. Il marche… mais il envoie le mail lui-même, lit l'heure de l'ordinateur, tire le numéro de commande au hasard et range tout dans un singleton statique. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch18_design.projects.p04_orders` : `Order`, `OrderRepository`, `Notifier`, `Catalog`, `IdGenerator`, `OrderService`, `InMemoryOrderRepository`, `ConsoleNotifier`, `SequentialIds`, `BakeryApp`, et tes tests (par exemple `OrderServiceTest` et `AdaptersTest`).

**Règle du crescendo :** chapitres 1 à 17, JUnit et Mockito. Pas de `System.out`, ni de `Thread.sleep`, ni d'horloge système dans tes tests. Aucune méthode de plus de **10 lignes**.

---

## Tableau de bord

### ☐ Étape 1 — Essayer de tester le legacy

**📖 La leçon : les dépendances cachées.** Une classe **dépend** de tout ce qu'elle utilise. Certaines dépendances se voient (les paramètres du constructeur) ; d'autres sont **cachées** à l'intérieur du code : un `new SmtpMailer()`, un singleton `Database.INSTANCE`, un `LocalDateTime.now()`, un `new Random()`, un `System.out.println`. Une dépendance cachée **ne se remplace pas** : impossible de tester sans envoyer de vrai mail, impossible de tester « dimanche » un mardi, impossible de prévoir le numéro de commande.

**👉 À toi :** lance `Data`, deux fois. Puis lis `LegacyOrderService` et essaie, **sur papier**, d'écrire un test qui vérifie : « une commande passée un dimanche est refusée ».

**🧪 Expérience :** relance `Data` en changeant le **fuseau horaire** de la JVM : dans IntelliJ, menu de la flèche verte → **Modify Run Configuration…** → **Modify options** → **Add VM options**, et tape `-Duser.timezone=America/New_York` (puis essaie `Asia/Tokyo`). Que change le résultat ?

**❓ Questions :**
- Les deux lancements donnent-ils la même sortie ? Pourquoi ?
- Fais la liste de **toutes** les dépendances cachées de `placeOrder`. Pour chacune, explique pourquoi elle empêche d'écrire un test fiable.
- Que montre l'expérience du fuseau horaire sur ce que « teste » vraiment un test qui lancerait `placeOrder` ?

### ☐ Étape 2 — Les ports : ce dont le métier a besoin

**📖 La leçon : inverser la dépendance.** Dans le legacy, le **métier** (les règles de la boulangerie) dépend des **détails** (le mailer SMTP, la base). Le principe d'**inversion des dépendances** (*Dependency Inversion Principle*) retourne la flèche :
1. le métier déclare des **interfaces**, écrites **de son point de vue** : « j'ai besoin d'enregistrer une commande », « j'ai besoin de prévenir le client » ;
2. les détails techniques **implémentent** ces interfaces.

Le métier ne connaît plus que ses interfaces : on appelle ces interfaces des **ports**, et leurs implémentations des **adaptateurs** (c'est l'architecture **hexagonale**, ou « ports et adaptateurs », d'Alistair Cockburn). Le métier se teste avec de **fausses** implémentations ; la production branche les vraies.

**Exemple sur un autre sujet :** une prise électrique. L'appareil (le métier) ne dépend pas de la centrale (le détail) : il dépend de la **prise** (le port), et n'importe quelle centrale, ou une batterie, peut la fournir.

**👉 À toi :**
- `public record Order(String id, String customer, List<String> items, long totalCents, LocalDateTime placedAt)` : le constructeur compact **copie** la liste (`List.copyOf`) ;
- `public interface OrderRepository` : `void save(Order order)`, `Optional<Order> find(String id)`, `boolean delete(String id)` (vrai si elle existait), `List<Order> byCustomer(String customer)` (dans l'ordre d'enregistrement) ;
- `public interface Notifier` : `void orderConfirmed(Order order)` et `void orderCancelled(Order order)` ;
- `@FunctionalInterface public interface Catalog` : `Optional<Long> price(String item)` ;
- `@FunctionalInterface public interface IdGenerator` : `String next()`.

**❓ Question :** l'interface s'appelle `Notifier` avec `orderConfirmed(Order)`, et pas `MailSender` avec `send(String to, String text)`. Pourquoi ce choix de nom et de méthodes compte-t-il ?

### ☐ Étape 3 — Le service : tout arrive par le constructeur

**📖 La leçon : l'injection de dépendances.** Le service ne fabrique **rien** : il **reçoit** tout ce dont il a besoin par son constructeur, et le garde dans des champs `final`. C'est l'**injection de dépendances** (*dependency injection*). Pas besoin de bibliothèque : un constructeur suffit (Spring le fait automatiquement dans les grosses applications, mais c'est la même idée).

Le temps aussi est une dépendance : `java.time.Clock` est une horloge qu'on **injecte**. En production, `Clock.systemDefaultZone()` ; dans un test, `Clock.fixed(Instant.parse("2026-10-09T10:15:00Z"), ZoneOffset.UTC)` est une horloge **arrêtée** à l'heure que l'on veut. On lit l'heure avec `LocalDateTime.now(clock)`.

**👉 À toi :** `public final class OrderService`, constructeur `OrderService(OrderRepository repository, Notifier notifier, Catalog catalog, IdGenerator ids, Clock clock)` :
- `Order place(String customer, List<String> items)` :
  - lit l'heure avec l'horloge ; boutique **fermée** le dimanche, avant 7 h et à partir de 19 h (7 h inclus, 19 h exclu) : `IllegalStateException("boutique fermee")` ;
  - une liste vide : `IllegalArgumentException("commande vide")` ; un article inconnu du catalogue : `IllegalArgumentException("article inconnu : " + item)` ;
  - le total est la somme des prix (un article commandé deux fois compte deux fois) ;
  - crée la commande (le numéro vient de `ids.next()`, la date est l'heure lue), **l'enregistre**, **prévient** le client, et la rend ;
  - si le `Notifier` lance une `RuntimeException`, la commande reste prise : on l'attrape et on la rend quand même (la confirmation partira plus tard) ;
  - rien n'est enregistré ni envoyé si la commande est refusée ;
- `boolean cancel(String id)` : annulable pendant **30 minutes** après `placedAt`, la 30e minute **comprise** (une constante `CANCEL_WINDOW = Duration.ofMinutes(30)`) : la commande est effacée, le client prévenu, et l'on rend `true`. Commande inconnue ou trop tard : `false`, et rien ne change ;
- `List<Order> history(String customer)` : les commandes du client.

Dans `OrderService.java` : ni `System.`, ni `now()` sans horloge, ni `Clock.system…`, ni `new` d'un adaptateur. `Check` le vérifie.

**🧪 Les tests, avec des doublures écrites à la main :** dans `OrderServiceTest`, une classe imbriquée `static final class RecordingNotifier implements Notifier` qui **note** chaque appel dans une liste (`"confirmee T-1"`, `"annulee T-1"`). Le catalogue et le générateur de numéros sont des **lambdas** (`item -> Optional.ofNullable(prix.get(item))`, `() -> "M-1"`). Teste : une commande normale (la commande rendue **entière** avec `assertEquals`, son enregistrement, la notification) ; les heures de fermeture **et** d'ouverture (de part et d'autre de 7 h et de 19 h, un dimanche, un samedi) ; les refus ; l'annulation à 30 minutes pile, à 30 minutes et 1 seconde, d'une commande inconnue ; l'historique.

**❓ Questions :**
- Pour tester l'annulation, il faut que le temps **avance** entre la commande et l'annulation, alors que `Clock.fixed` est arrêtée. Comment fais-tu ?
- Pourquoi `cancel` utilise-t-il `isAfter` pour refuser, et pas `!isBefore` ? Quel test le vérifie ?

### ☐ Étape 4 — Les pannes et les vérifications, avec Mockito

**📖 La leçon : le bon outil pour chaque doublure (rappel du chapitre 16).** Une doublure écrite à la main est parfaite pour **noter** ce qui se passe. Mockito est plus court pour **simuler une panne** (`doThrow(…).when(mock).methode(any())`) ou **vérifier** qu'un appel a eu lieu (`verify`) ou n'a **pas** eu lieu (`verifyNoInteractions`).

**👉 À toi :**
- un `Notifier` Mockito qui lance `IllegalStateException("serveur de mail en panne")` : la commande est rendue **et** enregistrée, et `verify` montre que la notification a bien été tentée ;
- une commande refusée (un dimanche) : `verifyNoInteractions` sur le `Notifier`.

**❓ Question :** attraper **toutes** les `RuntimeException` du `Notifier` et ne rien faire, est-ce une bonne pratique ? Que faudrait-il ajouter dans une vraie application ?

### ☐ Étape 5 — Les adaptateurs et la racine de composition

**👉 À toi :**
- `public final class InMemoryOrderRepository implements OrderRepository` : une `LinkedHashMap` (l'ordre d'enregistrement) ;
- `public final class ConsoleNotifier implements Notifier` : il **reçoit** un `PrintStream` dans son constructeur (pas de `System.out` dans cette classe) et écrit `MAIL a ada@example.org : commande CMD-1 confirmee, total 340 centimes` ou `MAIL a ada@example.org : commande CMD-1 annulee` ;
- `public final class SequentialIds implements IdGenerator` : `SequentialIds("CMD-")` rend `CMD-1`, `CMD-2`, `CMD-3`… ;
- `public final class BakeryApp` (constructeur privé) :
  - `public static OrderService create(Clock clock, PrintStream out)` : le **seul** endroit qui fait `new` des adaptateurs ; le catalogue est une lambda sur les prix du legacy (baguette 120, croissant 110, tarte 1850) ;
  - `main` : `create(Clock.systemDefaultZone(), System.out)`, passe la commande d'exemple du legacy et l'affiche (ou affiche `refus : …` si la boutique est fermée).

**🧪 Les tests (`AdaptersTest`) :** chaque adaptateur seul (`ConsoleNotifier` sur un `PrintStream` autour d'un `ByteArrayOutputStream`, chapitre 14), la copie des articles dans `Order`, puis un **test d'intégration** : `BakeryApp.create` avec une horloge fixe un vendredi à 8 h 30 et un flux en mémoire ; une commande, son mail, l'historique, un article inconnu.

**❓ Questions :**
- Combien de classes du projet contiennent un `new` d'adaptateur ? Pourquoi est-ce un bon signe ?
- Demain, les commandes iront dans une base H2 (chapitre 15). Quels fichiers changent ? Lesquels ne bougent pas, et lesquels de tes tests restent valables tels quels ?

### ☐ Étape 6 — Les mutants

**👉 À toi :** lance `Check`. Les 17 mutants changent une règle de la boulangerie (une heure, le dimanche, le total, la panne du mail, la fenêtre d'annulation…) ou un adaptateur.

**🧪 Expérience :** lance `BakeryApp` (la vraie horloge). Puis, comme à l'étape 1, change le fuseau horaire de la JVM. Pourquoi tes **tests**, eux, donnent-ils toujours le même résultat, quel que soit le fuseau et l'heure à laquelle tu les lances ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Order(`, `interface OrderRepository`, `interface Notifier`, `interface Catalog`, `interface IdGenerator`, `final class OrderService`, `final class InMemoryOrderRepository implements OrderRepository`, `final class ConsoleNotifier implements Notifier`, `final class SequentialIds implements IdGenerator`, `final class BakeryApp`, `LocalDateTime.now(clock)`, `Clock.systemDefaultZone()` ; ni `Random`, ni rien de `Legacy`, ni singleton `static OrderService instance`.
- **La conception :** aucune méthode de plus de **10 lignes** ; `OrderService.java` ne contient ni `System.`, ni `now()`, ni `Clock.system`, ni `new InMemoryOrderRepository`, `new ConsoleNotifier`, `new SequentialIds` ; `ConsoleNotifier.java` ne contient pas `System.out` ; `BakeryApp.java` contient `new OrderService(`.
- **Tes tests :** au moins **15** tests, `implements Notifier`, `Clock.fixed(`, `mock(`, `doThrow(`, `verify(`, `verifyNoInteractions(`, `ByteArrayOutputStream`, `BakeryApp.create(`, `@ParameterizedTest`, `assertThrows(` ; ni `System.out`, ni `Thread.sleep`, ni `systemDefaultZone`.
- **Les 17 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p04_orders ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 18 tests, 18 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 17/17 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```

Mockito peut afficher une ligne `OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes…` au premier lancement : c'est un avertissement de la JVM quand Mockito s'installe, sans conséquence.
