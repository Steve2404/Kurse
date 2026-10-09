# Projet 1 — La facture du garage (refactorer du code legacy sans rien casser)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 18) :**
- les **odeurs du code** (*code smells*) : méthode trop longue, noms obscurs, code dupliqué, nombres magiques, code de type en `String`, paramètre booléen, plusieurs responsabilités mêlées ;
- le **filet de sécurité** avant de toucher au code : les **tests de caractérisation** et le **maître étalon** (*golden master*) ;
- les **gestes du refactoring** et leurs raccourcis dans IntelliJ : renommer, extraire une variable, une constante, une méthode, une classe ;
- l'**objet valeur** (`Money`) contre l'obsession des types primitifs ;
- **remplacer un `if` sur un code de type par du polymorphisme** (`sealed interface`, `enum`, `default`) ;
- le principe de **responsabilité unique** (le S de SOLID) ; la **façade**.

**Ce qui est FOURNI :** `Data.java` contient `LegacyGarage`, le programme de factures du garage, écrit il y a dix ans. **Il marche** : les clients paient ces montants depuis des années. Sa sortie **est** la spécification. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch18_design.projects.p01_invoice` : `Garage`, `Money`, `Category`, `InvoiceLine`, `Part`, `Labor`, `Fee`, `LineParser`, `Customer`, `InvoiceTotals`, `InvoiceCalculator`, `InvoicePrinter`, et tes tests (des fichiers dont le nom finit par `Test.java`, par exemple `GarageTest` et `MoneyTest`).

**Règle du crescendo :** tout Java 17, JUnit et Mockito (chapitres 1 à 17). Pas de `System.out` ni de `Thread.sleep` dans tes tests. Dans ton code : pas d'`instanceof` (étape 4 : le polymorphisme le remplace), et à la fin, aucune méthode de plus de **12 lignes**.

> **🧰 Tes outils pour ce projet : les refactorings d'IntelliJ** (clavier par défaut sous Windows). Place le curseur sur l'élément, puis :
>
> | Geste | Raccourci |
> |---|---|
> | **renommer** (variable, méthode, classe : tous les usages suivent) | **Maj+F6** |
> | **extraire une variable** (une expression devient une variable nommée) | sélectionne l'expression, **Ctrl+Alt+V** |
> | **extraire une constante** (`1800` devient `PRICE_PER_QUARTER`) | sélectionne le nombre, **Ctrl+Alt+C** |
> | **extraire une méthode** (des lignes deviennent une méthode) | sélectionne les lignes, **Ctrl+Alt+M** |
> | **intégrer** (*inline*, l'inverse : une variable ou une méthode disparaît) | **Ctrl+Alt+N** |
> | **déplacer** (une méthode ou une classe ailleurs) | **F6** |
> | **tous les refactorings** possibles ici | **Ctrl+Alt+Maj+T** |
> | relancer les derniers tests | **Maj+F10** |
>
> IntelliJ fait ces gestes **sans se tromper** (il met à jour chaque usage). Un refactoring fait à la main, en copiant-collant, est la première source de bugs.

---

## Tableau de bord

### ☐ Étape 1 — Lire le legacy et nommer ses odeurs

**📖 La leçon : la conception, c'est rendre le code facile à changer.** Un programme juste mais illisible coûte cher : chaque modification prend une journée et casse autre chose. La **conception** (*design*), c'est l'art de ranger le code pour qu'une modification ne touche **qu'un seul endroit**.

Pour savoir où ranger, on commence par sentir les **odeurs** (*code smells*, le mot de Martin Fowler) : des signes qu'un code sera dur à changer. Les plus courantes :

| Odeur | À quoi on la reconnaît |
|---|---|
| **méthode trop longue** | il faut faire défiler l'écran pour la lire |
| **noms obscurs** | `t`, `t2`, `x`, `f` : il faut lire tout le code pour deviner |
| **code dupliqué** | le même bout de code copié à plusieurs endroits : un bug corrigé à un endroit reste aux autres |
| **nombres magiques** | `1800`, `20000` : que veulent-ils dire ? |
| **code de type** | `if (p[0].equals("PART"))` : chaque nouvelle sorte de ligne oblige à modifier tous les `if` |
| **paramètre booléen** | `invoice("Dupont", true, …)` : que veut dire `true` ? |
| **responsabilités mêlées** | une même méthode lit le texte, calcule les prix **et** met en page |

**Exemple sur un autre sujet :** une méthode `void inscrire(String n, int t, boolean b)` qui vérifie l'âge, calcule le tarif du club de sport, envoie le mail de bienvenue et écrit dans le fichier des membres. Changer le texte du mail oblige à relire le calcul du tarif ; changer de fichier oblige à relire la vérification de l'âge.

**👉 À toi :** lance `Data` (sa sortie est la facture d'exemple). Lis `LegacyGarage.invoice` **en entier**, puis réponds en commentaire, au début de ton futur `GarageTest`.

**❓ Questions :**
- Pour chaque odeur du tableau, donne un exemple précis dans `invoice` (un nom, une ligne).
- Combien de fois le bout de code `< 10 ? "0" : ""` est-il recopié ? Que fait-il ? Si un client demande que les montants s'écrivent avec une espace pour les milliers (`1 234,50`), combien d'endroits faut-il changer ?
- Écris, en français, **toutes les règles de prix** que tu lis dans le code (les quarts d'heure, les remises, leurs seuils, la TVA, les arrondis). Pour chaque seuil, dis s'il est **inclus** (`>=`) ou **exclu** (`>`).
- Que se passe-t-il avec la ligne `"PART;X;deux;100"` ? Et avec `"TAXI;X;1"` ?

### ☐ Étape 2 — Le filet de sécurité : la caractérisation

**📖 La leçon : on ne refactore jamais sans tests.** **Refactorer**, c'est changer la **forme** du code **sans changer ce qu'il fait**. Pour en être sûr, il faut des tests **avant** de toucher à quoi que ce soit. Mais le legacy n'a pas de tests, et personne ne connaît toutes ses règles. On écrit alors des **tests de caractérisation** : ils ne disent pas ce que le code **devrait** faire, ils figent ce qu'il **fait** aujourd'hui, même ses bizarreries.

La technique du **maître étalon** (*golden master*) : on garde l'ancien code à côté, on fabrique **des centaines d'entrées au hasard**, et l'on vérifie que le nouveau code produit **exactement** le même résultat que l'ancien. Le hasard doit être **reproductible** : `new Random(graine)` donne toujours la même suite de nombres pour la même graine, donc un échec se rejoue à l'identique.

**Exemple sur un autre sujet :** pour réécrire le calcul des impôts d'une mairie, on fait tourner l'ancien et le nouveau programme sur 10 000 foyers tirés au hasard (graine fixe), et l'on compare les deux montants foyer par foyer.

**👉 À toi :**
1. Crée `public final class Garage` avec la **même** méthode que le legacy : `public static String invoice(String customerName, boolean loyal, List<String> textLines)`. Pour l'instant, **recopie** le corps de `LegacyGarage.invoice` tel quel (avec un constructeur privé). C'est cette copie que tu vas refactorer.
2. Dans `GarageTest`, écris une méthode d'aide `sameAsLegacy(String customer, boolean loyal, List<String> lines)` qui compare `Data.LegacyGarage.invoice(…)` et `Garage.invoice(…)` avec `assertEquals` (mets les lignes dans le message d'échec : `() -> "lignes : " + lines`).
3. Les tests :
   - **la facture d'exemple** : le texte exact (colle la sortie de `Data` dans un bloc de texte `"""`), avec `Data.SAMPLE` et le client `"Dupont"` fidèle ;
   - **le maître étalon** : `@RepeatedTest(300)` avec un paramètre `RepetitionInfo info`. La graine est `info.getCurrentRepetition()`. Avec `new Random(graine)`, tire de 0 à 6 lignes (`nextInt(7)`) ; chaque ligne est, à pile ou face (`nextBoolean()`), une pièce `"PART;Piece i;" + (1 + nextInt(5)) + ";" + nextInt(15_000)` ou de la main-d'œuvre `"LABOR;Travail i;" + (1 + nextInt(200))` ; le client est fidèle à pile ou face ;
   - **les seuils, à la main** : pièces à exactement 200,00 et à 199,99 (fidèle ou non), main-d'œuvre de 240 minutes et de 226 minutes, une vis à 5 centimes, une facture vide ;
   - **les refus** : un `@ParameterizedTest` sur `"PART;X;0;100"`, `"PART;X;1;-1"`, `"PART;X;1"`, `"PART;X;1;2;3"`, `"LABOR;X;0"`, `"LABOR;X"`, `"TAXI;X;1"`, `""` et `"part;X;1;100"` : le legacy et ta copie lancent une `IllegalArgumentException` avec **le même message** ;
   - **la bizarrerie** : `"PART;X;deux;100"` lance une `NumberFormatException` chez les deux.
4. Lance tes tests : tout doit passer (ta copie est identique).

**🧪 Expériences :**
- Dans ta copie, remplace `1800L` par `1700L` et relance : combien de tests échouent ? Que dit le message ? Remets `1800L`.
- Combien des 300 factures au hasard touchent **vraiment** chaque règle ? Écris une petite classe à part avec un `main` qui refait le même tirage et compte : la remise fidélité, la remise main-d'œuvre, des pièces à **exactement** 200,00, une main-d'œuvre à **exactement** 240 minutes, les factures vides.

**❓ Questions :**
- Pourquoi la graine doit-elle être fixe, et pourquoi la prendre dans le numéro de la répétition plutôt qu'une seule graine pour tout ?
- Vu l'expérience, pourquoi faut-il **aussi** tester les seuils à la main ?
- La `NumberFormatException` brute est-elle un bon comportement ? Alors pourquoi ton test la **fige**-t-il ?

### ☐ Étape 3 — Un objet valeur : `Money`

**📖 La leçon : l'obsession des primitifs.** Le legacy manipule l'argent avec des `long` nus : rien n'empêche d'additionner des centimes et des minutes, ni d'avoir un montant négatif, et le format est recopié dix fois. Un **objet valeur** (*value object*) donne un **type** à une notion : il est **immuable**, il se **compare par sa valeur**, il **refuse** les valeurs impossibles et il **porte ses calculs**. En Java 17, un `record` est fait pour ça.

**Exemple sur un autre sujet :** `record Temperature(double celsius)` refuse moins de −273,15, sait `toFahrenheit()` et `isFreezing()`. Plus personne n'écrit `c * 9 / 5 + 32` à la main dans le code.

**👉 À toi :** crée `public record Money(long cents)` :
- le constructeur compact refuse un montant négatif : `IllegalArgumentException("montant negatif : " + cents)` ;
- `public static final Money ZERO` et `public static Money ofCents(long cents)` ;
- `plus(Money)`, `minus(Money)`, `times(int)` rendent un **nouveau** `Money` ;
- `percent(int percent)` : le pourcentage arrondi au centime **le plus proche**, la moitié monte, **comme le legacy** : `(cents * percent + 50) / 100` ;
- `boolean isZero()` ;
- `String format()` : les euros, une virgule, **deux** chiffres de centimes (`5` → `"0,05"`, `123456` → `"1234,56"`). Une seule ligne avec `String.format` et `%02d`.

Teste `Money` seul (`MoneyTest`) : le format (`0`, `5`, `50`, `123456`), l'arrondi de `percent` (`15` à 10 % donne `2`, `14` à 10 % donne `1`), le refus d'un négatif, et `minus` qui passerait sous zéro.

Puis, dans ta copie de `Garage`, **remplace** chaque bout de format recopié par `Money.ofCents(…).format()`. Relance le maître étalon après **chaque** remplacement.

**❓ Question :** pourquoi `minus` qui passe sous zéro doit-il **échouer** plutôt que rendre un `Money` négatif ? Comment le legacy affiche-t-il la remise, et pourquoi n'a-t-il jamais besoin d'un montant négatif ?

### ☐ Étape 4 — Remplacer le code de type par du polymorphisme

**📖 La leçon : l'objet sait ce qu'il est.** `if (p[0].equals("PART")) … else if (p[0].equals("LABOR")) …` : chaque endroit du programme qui traite une ligne doit **redemander** sa sorte. Ajouter une sorte de ligne oblige à retrouver **tous** ces `if`. Le polymorphisme renverse la question : chaque sorte de ligne est un **type** qui **sait** son libellé et son prix ; le code appelant dit seulement `line.price()`.

En Java 17, une **interface scellée** (`sealed interface … permits A, B`) liste toutes ses implémentations : le compilateur sait qu'il n'y en a pas d'autres. Une méthode **`default`** donne un comportement commun que seules certaines implémentations changent.

**Exemple sur un autre sujet :** un jeu d'échecs avec `if (piece.equals("TOUR")) … else if (piece.equals("FOU")) …` dans chaque méthode, contre `sealed interface Piece permits Tour, Fou, …` avec `boolean peutAller(Case depart, Case arrivee)` dans chaque pièce.

**👉 À toi :**
- `public enum Category { PART, LABOR, FEE }` (`FEE` servira à l'étape 7) ;
- `public sealed interface InvoiceLine permits Part, Labor, Fee` avec `String label()`, `Money price()`, `Category category()`, et `default int billedMinutes()` qui rend `0` ;
- `public record Part(String name, int quantity, Money unitPrice)` : refuse une quantité `< 1` (`"quantite invalide : " + quantity`) ; libellé `"Disque x2"` ; prix = prix unitaire × quantité ;
- `public record Labor(String name, int minutes)` : refuse `< 1` minute (`"minutes invalides : " + minutes`) ; deux constantes `QUARTER = 15` et `PRICE_PER_QUARTER = Money.ofCents(1800)` ; facturé au **quart d'heure commencé** (une méthode privée `quarters()`) ; `billedMinutes()` rend les minutes facturées ; libellé `"Montage (1 h 45)"` avec `String.format("%s (%d h %02d)", …)` ;
- pour compiler dès maintenant, `public record Fee(String name, Money amount)` : libellé `name + " (forfait)"`, prix `amount`, catégorie `FEE` ;
- `public final class LineParser` (constructeur privé) avec `public static InvoiceLine parse(String line)` : un `switch` sur le premier champ (`"PART"`, `"LABOR"`, `"FEE"` pour plus tard, sinon refus) et une méthode privée par sorte. Il garde **exactement** les refus du legacy, dans le **même ordre** : d'abord le nombre de champs, puis la lecture des nombres (la `NumberFormatException` passe telle quelle), puis les valeurs (`"ligne invalide : " + line`). Les refus en double se rangent dans deux méthodes privées `requireLength(line, fields, length)` et `invalid(line)`.

Teste chaque record et le lecteur seuls (`LineTest`, ou dans ton `MoneyTest`), puis branche-les dans `Garage` : la boucle lit chaque ligne avec `LineParser.parse` et utilise `label()` et `price()`. Relance le maître étalon.

**❓ Questions :**
- Le `switch` de `LineParser` est un `if` sur un code de type. Pourquoi est-il acceptable **ici**, et nulle part ailleurs ?
- Pourquoi `billedMinutes()` est-elle une méthode `default` de l'interface, plutôt qu'un test `if (line instanceof Labor)` dans le calcul ?
- `Part` refuse déjà une quantité `< 1`. Pourquoi `LineParser` doit-il **quand même** tester la quantité, avant de construire la `Part` ? (Indice : compare les deux messages.)

### ☐ Étape 5 — Une responsabilité par classe

**📖 La leçon : le S de SOLID.** Les cinq principes **SOLID** (Robert C. Martin) guident la conception objet ; ce chapitre les fait tous. Le premier, la **responsabilité unique** (*Single Responsibility Principle*) : une classe ne doit avoir **qu'une raison de changer**. Le legacy en a trois : le format des lignes de texte change, les règles de prix changent, la mise en page change. Trois raisons, donc trois classes.

Une conséquence heureuse : un calcul séparé de la mise en page se teste **sans comparer de texte**. On lit directement chaque montant.

**Exemple sur un autre sujet :** un bulletin de notes. `Moyennes` calcule (les coefficients, les arrondis), `Bulletin` met en page (le PDF, les couleurs). Le jour où le lycée change de logo, personne ne touche aux moyennes.

**👉 À toi :**
- `public record Customer(String name, boolean loyal)` : le booléen a enfin un nom ;
- `public record InvoiceTotals(Money parts, Money labor, Money fees, Money loyaltyDiscount, Money laborDiscount, Money net, Money vat, Money total)` (`fees` vaut zéro jusqu'à l'étape 7) ;
- `public final class InvoiceCalculator` avec `public InvoiceTotals compute(Customer customer, List<InvoiceLine> lines)` : les sous-totaux par catégorie (un *stream* filtré sur `category()`, puis `reduce(Money.ZERO, Money::plus)`), les deux remises (une méthode privée chacune), le net, la TVA et le total. Les nombres magiques deviennent des constantes : `LOYALTY_THRESHOLD` (200,00), `LOYALTY_PERCENT` (10), `LONG_JOB_MINUTES` (240), `LABOR_DISCOUNT_PERCENT` (5), `VAT_PERCENT` (20). **Aucun texte** dans cette classe ;
- `public final class InvoicePrinter` avec `public String print(Customer customer, List<InvoiceLine> lines, InvoiceTotals totals)` : **aucun calcul de prix** (pas de `percent`), tout vient de `totals`. Les lignes de remise n'apparaissent que si la remise n'est pas nulle ; range ces lignes optionnelles dans une méthode privée.

**🧪 Le test du calcul seul :** pièces 300,00, main-d'œuvre de 300 minutes, client fidèle. Calcule **sur papier** les huit montants de `InvoiceTotals` (avec `fees` à zéro pour l'instant), puis vérifie-les avec **un seul** `assertEquals` sur un `new InvoiceTotals(…)` : un record se compare champ par champ. Ajoute un client **non fidèle** : aucune remise fidélité.

**❓ Question :** cite une modification future qui ne toucherait **que** `InvoicePrinter`, une qui ne toucherait **que** `InvoiceCalculator`, et une qui ne toucherait **que** `LineParser`.

### ☐ Étape 6 — La façade, et le legacy disparaît

**📖 La leçon : la façade et les petits pas.** Une **façade** est une porte d'entrée simple devant plusieurs classes : `Garage.invoice` garde **la même signature** que le legacy, donc les programmes qui l'appellent ne voient rien du changement. À l'intérieur, elle ne fait qu'**assembler** : lire, calculer, imprimer.

La règle d'or du refactoring : **des petits pas, tous verts**. Un geste, les tests, un geste, les tests. Si un test devient rouge, on **annule** le dernier geste (**Ctrl+Z**) au lieu de chercher longtemps : il était petit, on le refait autrement.

**👉 À toi :**
1. Réécris `Garage.invoice` en **quatre lignes** : les lignes de texte deviennent des `InvoiceLine` (`stream().map(LineParser::parse).toList()`), un `Customer`, les totaux, le texte.
2. Ta copie du legacy a disparu : **aucune** méthode de ton code ne dépasse **12 lignes**, et ton code ne mentionne plus `LegacyGarage` (seuls tes tests le font).
3. Relance **tous** tes tests.

**❓ Questions :**
- Fais la liste des refactorings que tu as faits depuis l'étape 2 (renommer, extraire une constante, une méthode, une classe, remplacer un code de type…). Lesquels IntelliJ pouvait-il faire pour toi ?
- Compte les lignes : le legacy faisait une méthode de combien de lignes ? Ton code fait combien de classes ? Est-ce **plus** de code ? Pourquoi est-ce quand même mieux ?

### ☐ Étape 7 — La récompense : une nouvelle fonctionnalité en un quart d'heure

**📖 La leçon : on refactore pour pouvoir changer.** Le patron du garage demande une nouveauté : les **forfaits** (recyclage des pneus, petites fournitures). Dans le legacy, il aurait fallu un troisième `else if`, une troisième variable `t3`, toucher au calcul du net et à l'affichage, en priant de ne rien casser. Maintenant, chaque morceau a sa place.

**👉 À toi :** une ligne `"FEE;nom;centimes"` :
- exactement 3 champs, un montant `>= 0`, sinon `"ligne invalide : " + line` (la `NumberFormatException` passe, comme pour les autres) ;
- son libellé est `"nom (forfait)"`, sa catégorie `FEE` ;
- les forfaits s'additionnent dans `fees` ; ils n'ont **aucune remise** (ni dans la base de la remise fidélité, ni ailleurs) mais **paient la TVA** ;
- la facture affiche `Forfaits : …` juste après la ligne `Main-d'oeuvre`, **seulement** s'il y en a.

**🧪 Les tests :** la facture complète de `"Ba"` (fidèle) avec `"PART;Pneu;4;5000"` et `"FEE;Recyclage;350"` (calcule-la sur papier, puis écris le texte exact attendu), et les refus `"FEE;X;-1"`, `"FEE;X"`, `"FEE;X;1;2"`.

**❓ Questions :**
- Combien de fichiers as-tu modifiés pour ajouter les forfaits ? Lesquels n'as-tu **pas** eu besoin d'ouvrir ?
- Ton maître étalon passe toujours : pourquoi ne peut-il **pas** tester les forfaits ? Qui les teste ?

### ☐ Étape 8 — Les mutants

**👉 À toi :** lance `Check`. Les 12 mutants changent chacun **une** règle de la facture (un arrondi, un seuil, la base de la TVA, un refus…). Un mutant qui survit est une règle que tes tests ne regardent pas : le palier 2 de `INDICES.md` dit ce que change chacun.

**🧪 Expérience :** garde **seulement** le maître étalon (mets les autres tests en commentaire) et relance `Check`. Quels mutants survivent ? Pourquoi exactement ceux-là ? Remets tes tests.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Money(long cents)`, `sealed interface InvoiceLine`, `record Part(`, `record Labor(`, `record Fee(`, `enum Category`, `record Customer(`, `record InvoiceTotals(`, `final class LineParser`, `final class InvoiceCalculator`, `final class InvoicePrinter`, `final class Garage`, `default int billedMinutes()` ; ni `instanceof` ni `LegacyGarage`.
- **La conception :** aucune méthode de plus de **12 lignes** ; pas de `format(` dans `InvoiceCalculator.java`, pas de `split(` non plus ; pas de `percent(` dans `InvoicePrinter.java`.
- **Tes tests :** au moins **100** tests (un test répété compte chaque répétition), `Data.LegacyGarage`, `@RepeatedTest`, `RepetitionInfo`, `new Random(`, `@ParameterizedTest`, `assertThrows(`, `new InvoiceCalculator()` ; ni `System.out` ni `Thread.sleep`.
- **Les 12 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p01_invoice ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 334 tests, 334 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 12/12 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```

Si une méthode est trop longue, `Check` le dit : `[FAIL] conception : InvoiceCalculator.java : compute() fait 13 lignes (au plus 12)`. Les lignes comptées sont celles du corps, sans les lignes vides ni les commentaires.
