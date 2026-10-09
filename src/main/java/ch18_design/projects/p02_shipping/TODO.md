# Projet 2 — Les frais de port (ouvert à l'extension, fermé à la modification)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 18) :**
- le principe **ouvert/fermé** (le O de SOLID) : ajouter un comportement **sans modifier** le code qui marche ;
- le patron **Stratégie** : une interface, une classe par façon de faire, choisie à l'exécution ;
- une stratégie d'une seule méthode écrite en **lambda** (`@FunctionalInterface`) ;
- la **composition plutôt que l'héritage** : des surcharges qui s'ajoutent à n'importe quel transporteur ;
- le **registre** de stratégies (une `Map` par code) et la **racine de composition** (le seul endroit qui nomme les classes concrètes) ;
- ne plus se servir des **exceptions pour piloter le programme** ;
- un piège des tests au hasard : **les graines voisines de `Random`**.

**Ce qui est FOURNI :** `Data.java` contient `LegacyShipping`, le calcul des frais de port de la boutique de thé : un `switch` sur le code du transporteur, des `if` pour les surcharges, et un `cheapest` qui attrape des exceptions pour sauter les transporteurs qui refusent. Sa sortie est la spécification. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch18_design.projects.p02_shipping` : `Parcel`, `ShippingRate`, `PostRate`, `ExpressRate`, `PickupRate`, `FreightRate`, `Surcharge`, `Surcharges`, `Quote`, `ShippingCalculator`, `Shop`, et tes tests (par exemple `ShippingTest` et `RatesTest`).

**Règle du crescendo :** chapitres 1 à 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests. Dans ton code : ni `switch`, ni `instanceof`, ni `catch`, et aucune méthode de plus de **10 lignes**.

---

## Tableau de bord

### ☐ Étape 1 — Lire le legacy : où faut-il toucher pour ajouter un transporteur ?

**📖 La leçon : ouvert à l'extension, fermé à la modification.** Le deuxième principe SOLID, **ouvert/fermé** (*Open/Closed Principle*, Bertrand Meyer), dit : un module doit être **ouvert à l'extension** (on peut lui ajouter des comportements) mais **fermé à la modification** (on n'a pas besoin de rouvrir son code pour ça). Chaque fois qu'on rouvre un code qui marche, on risque de le casser, et il faut tout retester.

Le signe qu'un code n'est **pas** fermé : un `switch` ou une chaîne de `if` sur un **code** (`"POST"`, `"EXPRESS"`…), que l'on retrouve à plusieurs endroits, et qu'il faut compléter à chaque nouveauté.

**Exemple sur un autre sujet :** une application de paiement avec `switch (moyen) { case "CARTE" … case "PAYPAL" … }`. Le jour où l'on ajoute « Apple Pay », il faut modifier ce `switch`, celui des remboursements, celui des frais, et retester les trois moyens qui marchaient.

**👉 À toi :** lance `Data` et lis `LegacyShipping` en entier.

**❓ Questions :**
- Écris les règles de chaque transporteur (prix, paliers, pays, poids maximal) et des deux surcharges. Pour chaque limite, inclus ou exclu ?
- Le patron veut ajouter `FREIGHT`, un transporteur pour les colis de plus de 30 kg. Quels endroits du legacy faudrait-il modifier ?
- Dans `cheapest`, à quoi sert le `try`/`catch` ? Pourquoi est-ce une mauvaise idée de se servir d'exceptions pour ça ?

### ☐ Étape 2 — Le filet de sécurité, et le piège des graines

**📖 La leçon : comparer aussi les refus.** Le legacy rend un prix **ou** lance une exception. Le maître étalon doit comparer les deux cas : on transforme chaque résultat en **texte** (le prix, ou `"refus : "` suivi du message), et l'on compare les deux textes.

**👉 À toi :** dans `ShippingTest` :
- une méthode d'aide `private static String outcome(LongSupplier price)` : elle appelle `price.getAsLong()` et rend le prix en texte ; si une `IllegalArgumentException` part, elle rend `"refus : " + e.getMessage()` (le `catch` est permis **dans les tests**) ;
- `goldenMasterPrice` : `@RepeatedTest(400)` ; avec `new SplittableRandom(info.getCurrentRepetition())`, tire un transporteur parmi `"POST"`, `"EXPRESS"`, `"PICKUP"`, `"TRUCK"` (`nextInt(4)`), un poids de 0 à 32 000 g (`nextInt(32_001)`), un pays parmi `"FR"`, `"BE"`, `"DE"`, `"LU"`, `"CH"`, `"US"`, `"fr"`, `"FRA"`, et fragile à pile ou face. Compare `outcome` du legacy et `outcome` de ton calculateur (pour l'instant, ton calculateur n'existe pas : commence par comparer le legacy **à lui-même**, puis branche ton code au fil des étapes) ;
- `goldenMasterCheapest` : `@RepeatedTest(200)`, graine `1000 + numéro`, un poids de 1 à 30 000 g, un des six premiers pays (les valides), fragile à pile ou face. Le legacy rend `"POST:750"` ou `"AUCUN"` ; ton `cheapest` rendra un `Optional<Quote>` : transforme-le avec `map(q -> q.carrier() + ":" + q.priceCents()).orElse("AUCUN")`.

**🧪 Expérience (le piège) :** remplace `SplittableRandom` par `Random` dans `goldenMasterPrice` et compte, dans une petite classe à part, combien de fois chaque transporteur est tiré sur les 400 répétitions (le tirage `nextInt(4)` fait en **premier** avec `new Random(n)`, pour `n` de 1 à 400). Remets `SplittableRandom`.

**❓ Questions :**
- Que montre l'expérience ? Quel aurait été le danger pour tes tests ?
- Le maître étalon des prix et celui du moins cher se recouvrent : pourquoi garder les deux ?

### ☐ Étape 3 — Le colis se valide lui-même

**👉 À toi :** `public record Parcel(int grams, String country, boolean fragile)` :
- refuse un poids `< 1` (`"poids invalide : " + grams`), puis un pays qui n'est pas deux lettres majuscules (`country == null || !country.matches("[A-Z]{2}")` : `"pays invalide : " + country`), **dans cet ordre**, comme le legacy ;
- `boolean domestic()` : le pays est `"FR"` ;
- `int startedKilos()` : le nombre de kilos **commencés** (1 g → 1, 1000 g → 1, 1001 g → 2).

**❓ Question :** le legacy vérifie le poids et le pays **avant** de regarder le transporteur. Avec ton `Parcel`, où ces vérifications ont-elles lieu, et pourquoi l'ordre des messages reste-t-il le même ?

### ☐ Étape 4 — Une stratégie par transporteur

**📖 La leçon : le patron Stratégie.** Un **patron de conception** (*design pattern*, popularisé par le livre du « Gang of Four ») est une solution connue, avec un nom, à un problème qui revient souvent. Le patron **Stratégie** : quand il y a plusieurs façons de faire la même chose, on écrit une **interface** pour « la façon de faire », une **classe par façon**, et le code appelant reçoit la stratégie sans savoir laquelle c'est.

**Exemple sur un autre sujet :** un GPS calcule un itinéraire « le plus court », « le plus rapide » ou « sans péage ». `interface Itineraire { List<Route> calculer(Lieu depart, Lieu arrivee); }`, trois classes, et l'écran de navigation ne connaît que l'interface.

**👉 À toi :**
- `public interface ShippingRate` : `String code()`, `boolean accepts(Parcel parcel)`, `long basePrice(Parcel parcel)` (le prix **avant** les surcharges) ;
- `public final class PostRate` : code `"POST"`, accepte jusqu'à 30 000 g **inclus**, les paliers du legacy (une méthode privée pour le prix en France), le **double** hors de France ;
- `public final class ExpressRate` : code `"EXPRESS"`, jusqu'à 30 000 g inclus, 12,90 + 2,50 par kilo commencé (`startedKilos()`), 15,00 de plus hors de France ;
- `public final class PickupRate` : code `"PICKUP"`, seulement `"FR"` et `"BE"` (un `Set`), jusqu'à 20 000 g inclus, 3,90 fixe.

Teste chaque stratégie **seule** (`RatesTest`), sans calculateur : chaque palier, de part et d'autre de chaque limite.

**❓ Question :** pourquoi `accepts` et `basePrice` sont-elles deux méthodes séparées, au lieu d'un `basePrice` qui lancerait une exception ?

### ☐ Étape 5 — Les surcharges : la composition plutôt que l'héritage

**📖 La leçon : la composition.** Pour ajouter « fragile » aux transporteurs, l'héritage donnerait `PostFragile extends PostRate`, `ExpressFragile extends ExpressRate`… puis, avec la douane, `PostFragileHorsUE`… Chaque nouvelle surcharge **multiplie** le nombre de classes. La **composition** fait l'inverse : une surcharge est un petit objet à part, et le calculateur **ajoute** les surcharges qu'on lui donne à **n'importe quel** transporteur. Trois transporteurs et deux surcharges font cinq classes, pas douze.

Quand une stratégie n'a qu'**une** méthode abstraite, c'est une **interface fonctionnelle** (chapitre 8) : on l'écrit en **lambda**, sans classe. `@FunctionalInterface` demande au compilateur de vérifier qu'il n'y a bien qu'une méthode abstraite.

**👉 À toi :**
- `@FunctionalInterface public interface Surcharge` avec `long amount(Parcel parcel, long basePrice)` : le supplément, calculé sur le prix de **base** ;
- `public final class Surcharges` (constructeur privé), deux fabriques qui rendent des **lambdas** :
  - `static Surcharge fragile()` : 15 % du prix de base, arrondis au centime le plus proche, si le colis est fragile, sinon 0 ;
  - `static Surcharge customs()` : 8,00 hors de l'Union européenne (le `Set` `EU` du legacy), sinon 0.

**❓ Questions :**
- Avec l'héritage, combien de classes faudrait-il pour 4 transporteurs et 3 surcharges qui se combinent librement ? Et avec la composition ?
- Pourquoi une surcharge reçoit-elle le prix **de base**, et pas le prix déjà surchargé ? (Indice : le legacy calcule `extra` sur `p`.)

### ☐ Étape 6 — Le calculateur, fermé pour de bon

**📖 La leçon : le registre et la racine de composition.** Le calculateur range ses stratégies dans une `Map` par code : chercher `"POST"` remplace le `switch`. Il **reçoit** ses stratégies dans son constructeur : il ne nomme **aucune** classe concrète. Un seul endroit du programme les nomme et les assemble : la **racine de composition** (*composition root*), ici `Shop.standard()`.

**👉 À toi :**
- `public record Quote(String carrier, long priceCents)` ;
- `public final class ShippingCalculator` :
  - le constructeur `ShippingCalculator(List<ShippingRate> rates, List<Surcharge> surcharges)` range les transporteurs dans une `LinkedHashMap` ; deux transporteurs avec le **même code** : `IllegalArgumentException("transporteur en double : " + code)` (regarde ce que rend `putIfAbsent`) ;
  - `long price(String carrier, Parcel parcel)` : code inconnu → `"transporteur inconnu : " + carrier` ; colis refusé → `"colis refuse par " + carrier` ; sinon, le prix de base **plus la somme** des surcharges, chacune calculée sur le prix de base (une méthode privée `finalPrice`) ;
  - `List<Quote> quotes(Parcel parcel)` : un devis par transporteur qui **accepte** le colis, trié par prix croissant, puis par code (`Comparator.comparingLong(…).thenComparing(…)`) ;
  - `Optional<Quote> cheapest(Parcel parcel)` : le premier devis, ou vide ;
- `public final class Shop` (constructeur privé) avec `public static ShippingCalculator standard()` : les trois transporteurs, et les surcharges **dans cet ordre** : `customs()`, puis `fragile()`.

Branche ton calculateur dans tes deux maîtres étalons : tout doit passer. Le calculateur ne doit nommer ni `PostRate`, ni `PickupRate`, ni `FreightRate`, ni `Surcharges` : `Check` le vérifie.

**❓ Questions :**
- Le legacy rendait le moins cher sous la forme `"POST:750"` ou `"AUCUN"`. Pourquoi un `Optional<Quote>` est-il meilleur ?
- Ton calculateur est-il fermé à **tout** changement ? Que faudrait-il modifier pour une surcharge qui dépend du **transporteur** (par exemple, « fragile » gratuit chez `EXPRESS`) ?

### ☐ Étape 7 — La preuve : deux transporteurs de plus, sans rouvrir le calculateur

**👉 À toi :**
1. **Le fret :** `public final class FreightRate`, code `"FREIGHT"` : seulement en France et seulement **au-dessus** de 30 000 g (30 000 g pile, c'est encore la poste) ; 49,00 plus 0,90 par kilo commencé au-delà de 30 kg. Ajoute-le à `Shop.standard()`, après `PickupRate`. Tes maîtres étalons passent toujours (pourquoi ? question plus bas).
2. **Le vélo, dans tes tests :** dans `ShippingTest`, crée un transporteur `"BIKE"` avec une **classe anonyme** (`new ShippingRate() { … }`) : en France jusqu'à 3 000 g, 3,00. Construis un `new ShippingCalculator(…)` avec `PostRate`, ton vélo et la surcharge `fragile()`, et vérifie les devis d'un colis fragile de 400 g, puis d'un colis de 4 000 g.
3. **Une surcharge en lambda, dans tes tests :** `Surcharge flat = (parcel, base) -> 100;` et `Surcharge half = (parcel, base) -> base / 2;` sur `PickupRate` seul : le prix vérifie que chaque surcharge se calcule bien sur le prix **de base**.
4. Ajoute les tests du fret (de part et d'autre de 30 000 g, en France et ailleurs, fragile) et du refus d'un code en double.

**❓ Questions :**
- Combien de fichiers as-tu modifiés pour ajouter `FREIGHT` ? Et pour `BIKE` ?
- Pourquoi tes maîtres étalons passent-ils encore, alors que le legacy ne connaît pas `FREIGHT` ?

### ☐ Étape 8 — Les mutants

**👉 À toi :** lance `Check`. Les 16 mutants changent chacun une règle : un palier, une limite, un pays, l'arrondi, le tri, un refus, l'ordre des surcharges…

**🧪 Expérience :** garde **seulement** tes deux maîtres étalons et relance `Check`. Quels mutants survivent ? Qu'ont-ils en commun ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Parcel(`, `interface ShippingRate`, `@FunctionalInterface`, `interface Surcharge`, `record Quote(`, `final class PostRate`, `final class ExpressRate`, `final class PickupRate`, `final class FreightRate`, `final class Surcharges`, `final class ShippingCalculator`, `final class Shop`, `Optional<Quote> cheapest(`, `List<Quote> quotes(`, `Comparator.`, une lambda (`->`) ; ni `switch`, ni `instanceof`, ni `catch (`, ni `LegacyShipping`.
- **La conception :** aucune méthode de plus de **10 lignes** ; `ShippingCalculator.java` ne nomme ni `PostRate`, ni `PickupRate`, ni `FreightRate`, ni `Surcharges.` ; `Shop.java` contient `new FreightRate()`.
- **Tes tests :** au moins **100** tests, `Data.LegacyShipping`, `@RepeatedTest`, `new SplittableRandom(`, `@ParameterizedTest`, `assertThrows(`, `new ShippingRate()`, `new ShippingCalculator(`, une `Surcharge` écrite en lambda (`Surcharge nom = (…`) ; ni `System.out` ni `Thread.sleep`.
- **Les 16 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p02_shipping ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 644 tests, 644 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 16/16 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
