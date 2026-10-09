# Projet 8 — La vie d'une commande (le patron État, la méthode modèle)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 18) :**
- la **machine à états** : un tableau des transitions, écrit **avant** le code ;
- le patron **État** (*State*) : chaque état est un objet qui sait ce qu'il accepte ; le contexte **délègue** au lieu de tester ;
- des **méthodes par défaut** qui refusent, pour n'écrire que les transitions permises ;
- tester une machine à états **case par case** (un test paramétré sur tout le tableau) ;
- la **méthode modèle** (*Template Method*) : un squelette `final`, des étapes `abstract`, un **crochet** ;
- quand l'héritage est le bon outil (la méthode modèle), et quand la composition est meilleure (la stratégie).

**Ce qui est FOURNI :** `Data.java` contient `LegacyOrder`, l'ancienne gestion des commandes : l'état est une `String`, et chaque méthode refait sa série de `if`. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch18_design.projects.p08_workflow` : `Transition`, `OrderState`, `NewState`, `PaidState`, `ShippedState`, `DeliveredState`, `ClosedState`, `Order`, `ReceiptExporter`, `TextReceipt`, `CsvReceipt`, et tes tests (par exemple `WorkflowTest`).

**Règle du crescendo :** chapitres 1 à 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests. Dans ton code : ni `switch`, ni `instanceof`, et aucune méthode de plus de **10 lignes**.

---

## Le tableau des transitions (la spécification)

Une commande a **six** états et accepte **cinq** actions. Une case vide est un **refus**.

<table>
<tr><th>État \ action</th><th>payer</th><th>expédier</th><th>livrer</th><th>annuler</th><th>rembourser</th></tr>
<tr><td><b>nouvelle</b></td><td>→ payée</td><td></td><td></td><td>→ annulée (rien à rembourser)</td><td></td></tr>
<tr><td><b>payée</b></td><td></td><td>→ expédiée</td><td></td><td>→ annulée, <b>tout</b> est remboursé</td><td></td></tr>
<tr><td><b>expédiée</b></td><td></td><td></td><td>→ livrée (on note la date)</td><td></td><td></td></tr>
<tr><td><b>livrée</b></td><td></td><td></td><td></td><td></td><td>→ remboursée, <b>tout</b> est remboursé, jusqu'au 14<sup>e</sup> jour après la livraison <b>compris</b></td></tr>
<tr><td><b>annulée</b></td><td></td><td></td><td></td><td></td><td></td></tr>
<tr><td><b>remboursée</b></td><td></td><td></td><td></td><td></td><td></td></tr>
</table>

Les libellés des états dans le code, **sans accent** : `nouvelle`, `payee`, `expediee`, `livree`, `annulee`, `remboursee`. Ceux des actions : `payer`, `expedier`, `livrer`, `annuler`, `rembourser`.

---

## Tableau de bord

### ☐ Étape 1 — Comparer le legacy au tableau

**👉 À toi :** lance `Data` et lis `LegacyOrder`.

**❓ Questions :**
- Pour chacune des trois lignes de la sortie, dis quelle case du tableau le legacy ne respecte pas.
- Le legacy refuse-t-il une action interdite ? Comment l'appelant sait-il qu'elle a été ignorée ?
- Pour ajouter un état « en préparation » entre `payee` et `expediee`, combien de méthodes du legacy faut-il relire et modifier ?

### ☐ Étape 2 — Les tests d'abord : tout le tableau

**📖 La leçon : tester une machine à états.** Une machine à états se teste **case par case** : 6 états × 5 actions = **30 cas**, ni plus ni moins. Un test paramétré (chapitre 16) les décrit en une table qui **ressemble** à la spécification. Pour chaque cas, on amène une commande neuve dans l'état de départ **par le chemin normal**, on tente l'action, et l'on vérifie l'état d'arrivée, ou le refus (le message, **et** l'état qui n'a pas bougé).

**👉 À toi :** écris le test **avant** le code (il ne compilera qu'à l'étape 3) :
- une méthode d'aide `orderIn(String state)` qui fabrique `new Order("CMD-1", 3400, clock)` et l'amène dans l'état demandé (ici, dans le **test**, un `switch` est permis) ;
- une méthode d'aide qui transforme le nom d'une action en `Consumer<Order>` (`Order::pay`…) ;
- `@ParameterizedTest` avec un `@CsvSource` de **30** lignes `état, action, résultat`, où le résultat est l'état d'arrivée ou `refus`. Un refus est une `IllegalStateException` avec le message `action refusee : expedier (commande nouvelle)`.

**❓ Question :** pourquoi amener la commande dans l'état de départ par le **chemin normal** (payer, puis expédier…), plutôt que de lui donner directement l'état voulu ?

### ☐ Étape 3 — Le patron État

**📖 La leçon : l'objet état.** Au lieu d'une `String` et de `if` partout, chaque état devient une **classe** qui implémente une interface `OrderState`. Chaque action y est une méthode qui **rend l'état suivant**. Par défaut (méthodes `default`), une action est **refusée** : chaque classe n'écrit que **ses** transitions permises, exactement les cases remplies de **sa** ligne du tableau. Le **contexte** (`Order`) garde l'état courant et lui **délègue** : `state = state.pay(this)`. Il ne contient plus aucun `if` sur l'état.

**Exemple sur un autre sujet :** un distributeur de boissons : `SansPiece`, `AvecPiece`, `EnDistribution`, `Vide`. Appuyer sur le bouton ne fait pas la même chose selon l'état ; chaque état le sait, et la machine se contente de lui transmettre le bouton.

**👉 À toi :**
- `public record Transition(String from, String to)`, dont `toString()` rend `payee -> expediee` ;
- `public interface OrderState` : `String label()` ; les cinq actions `OrderState pay(Order order)`, `ship`, `deliver`, `cancel`, `refund`, chacune `default` et **refusée** (`throw order.refused("payer")`…) ; `default boolean isClosed()` qui rend `false` ;
- une classe par état, qui n'écrit **que** ses cases : `NewState`, `PaidState`, `ShippedState`, `DeliveredState` ; et une seule classe `ClosedState` pour les deux états finaux, avec un constructeur privé et deux fabriques `cancelled()` et `refunded()` (elle n'accepte rien et répond `true` à `isClosed`) ;
- `public final class Order`, construite avec `(String id, long amountCents, Clock clock)` :
  - les cinq actions publiques `pay()`, `ship()`, `deliver()`, `cancel()`, `refund()`, chacune d'une ligne : `change(state.pay(this))` ;
  - `change` note la transition (`Transition(ancien libellé, nouveau libellé)`) puis change l'état. Un refus lance l'exception **avant**, donc rien ne change ;
  - pour les états (visibles dans le paquet seulement) : `IllegalStateException refused(String action)` (le message du tableau), `void recordRefund(long cents)`, `void markDelivered()` (note la date du jour), `LocalDate today()` (avec l'horloge) et `LocalDate deliveredOn()` ;
  - pour tout le monde : `id()`, `amountCents()`, `status()` (le libellé de l'état), `isClosed()`, `refundedCents()`, `transitions()` (une copie non modifiable).
- Le remboursement d'une commande livrée vérifie le délai : après le 14e jour, `IllegalStateException("delai de remboursement depasse")`.

**🧪 Les tests de plus :** un refus ne change ni les transitions ni le remboursement ; une commande payée puis annulée est remboursée de 3 400, une nouvelle annulée de 0 ; le remboursement le 14e jour (avec une horloge qu'on avance, comme au projet 6), le refus le 15e ; le délai compte depuis la **livraison** (expédiée, 10 jours d'attente, livrée, 14 jours plus tard : encore remboursable) ; la liste des transitions et sa protection.

**❓ Questions :**
- Pour ajouter l'état « en préparation » entre `payee` et `expediee`, quels fichiers changent ? `Order` change-t-il ?
- `Order` contient des méthodes visibles **dans le paquet seulement** (`refused`, `recordRefund`…). Pourquoi ne pas les rendre publiques ?

### ☐ Étape 4 — Les reçus : la méthode modèle

**📖 La leçon : la méthode modèle.** Le reçu lisible et l'export pour le tableur ont **le même squelette** : un en-tête, une ligne par transition, un pied. Seul le contenu de chaque partie change. La **méthode modèle** écrit le squelette **une fois**, dans une classe abstraite, dans une méthode `final` (personne ne peut le changer), et laisse aux sous-classes les **étapes** : des méthodes `abstract` qu'elles doivent écrire, et des **crochets** (*hooks*) : des méthodes déjà écrites, souvent vides, qu'elles **peuvent** redéfinir.

C'est un des rares cas où l'**héritage** est le bon outil : les variantes partagent un algorithme fixe et ne diffèrent que par de petites étapes. Si les étapes devaient se **combiner** librement ou changer à l'exécution, on préférerait des stratégies (projet 2).

**👉 À toi :**
- `public abstract class ReceiptExporter` avec `public final String export(Order order)` : l'en-tête, puis une ligne par transition, puis le pied ; les étapes `protected abstract String header(Order order)` et `protected abstract String line(Order order, Transition transition)` ; le crochet `protected String footer(Order order)`, qui rend `""` ;
- `public final class TextReceipt extends ReceiptExporter` : en-tête `Commande CMD-1`, une ligne `  nouvelle -> payee` (deux espaces devant) par transition, pied `Etat final : annulee, rembourse : 3400 centimes` ; chaque partie finit par un saut de ligne ;
- `public final class CsvReceipt extends ReceiptExporter` : en-tête `commande;de;vers`, lignes `CMD-1;nouvelle;payee`, **pas** de pied (il garde le crochet vide).

**🧪 Les tests :** les deux reçus complets d'une commande payée puis annulée (des blocs de texte `"""`), et l'export d'une commande neuve (l'en-tête seul).

**❓ Question :** pourquoi `export` est-il `final` ? Que pourrait faire une sous-classe sinon ?

### ☐ Étape 5 — Les mutants

**👉 À toi :** lance `Check`. Les 17 mutants changent une case du tableau, oublient un remboursement ou la date de livraison, déplacent la limite des 14 jours, ou cassent un reçu.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Transition(`, `interface OrderState`, `default OrderState pay(Order`, les cinq classes d'état `final class … implements OrderState` (`NewState`, `PaidState`, `ShippedState`, `DeliveredState`, `ClosedState`), `final class Order`, `abstract class ReceiptExporter`, `public final String export(Order`, `protected abstract String`, `final class TextReceipt extends ReceiptExporter`, `final class CsvReceipt extends ReceiptExporter`, `LocalDate.now(clock)` ; ni `switch`, ni `instanceof`, ni rien de `Legacy`.
- **La conception :** aucune méthode de plus de **10 lignes** ; `Order.java` ne contient ni `if (` ni `equals(` ; `TextReceipt` et `CsvReceipt` ne réécrivent pas `export`.
- **Tes tests :** au moins **30** tests, `@ParameterizedTest`, `@CsvSource`, `extends Clock`, `new TextReceipt()`, `new CsvReceipt()`, `assertThrows(` ; ni `System.out` ni `Thread.sleep`.
- **Les 17 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p08_workflow ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 38 tests, 38 reussis
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
