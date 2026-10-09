# Projet 9 — La cave à fromages (capstone : un refactoring complet, puis une nouveauté)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Le capstone du chapitre.** Ici, l'énoncé te dit **ce que** le programme doit faire et **quels types** créer, mais beaucoup moins **comment** : c'est à toi de choisir les gestes, dans le bon ordre, en t'appuyant sur les huit projets précédents :
- le **filet** du legacy (projets 1 et 2 : maître étalon, graines, cas limites) ;
- l'**objet valeur** immuable (projets 1 et 5) ;
- les **stratégies** et la **fabrique** (projet 2), une stratégie en **lambda** ;
- le **décorateur** (projet 6) ;
- les **petites méthodes** (au plus **8 lignes** ici) et **aucune** chaîne de `if … else if` sur les noms.

**Ce qui est FOURNI :** `Data.java` contient `LegacyCheeseShop.updateDay`, le programme qui fait vieillir le stock de la cave chaque nuit : des `if` dans des `if`, sur le nom de chaque produit. Il marche depuis des années ; sa sortie **est** la spécification. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch18_design.projects.p09_cheese` : `Cheese`, `AgingRule`, `DecayRule`, `AgedRule`, `TicketRule`, `BioRule`, `RuleBook`, `CheeseShop`, et tes tests (par exemple `CheeseShopTest`).

**Règle du crescendo :** chapitres 1 à 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests. Dans ton code : ni `switch`, ni `instanceof`, ni `else if`.

---

## Tableau de bord

### ☐ Étape 1 — Lire, et écrire les règles en français

**👉 À toi :** lance `Data` (trois jours d'un stock d'exemple) et lis `updateDay` **en entier**, crayon en main.

**❓ Questions :**
- Écris **toutes** les règles, produit par produit (combien de qualité par jour, avant et après la date, les limites 0 et 50, le billet de dégustation et ses paliers, le sel). Attention : `sellIn` est diminué **avant** les comparaisons.
- Un `Comte` de qualité 55 (au-dessus du maximum) : que devient-il le lendemain ? Et une `Tomme` de qualité 60 ? Est-ce cohérent ? Faut-il le « corriger » pendant le refactoring ?
- Dans le legacy, le nom `"Sel de Guerande"` apparaît deux fois. Que se passe-t-il si l'on se trompe d'orthographe dans l'un des deux ?

### ☐ Étape 2 — Le filet

**👉 À toi :** dans `CheeseShopTest` (pour l'instant, ton `CheeseShop` peut appeler le legacy en convertissant les articles, ou tu compares le legacy à lui-même) :
- une méthode d'aide `sameAsLegacy(List<Cheese> stock, int days)` qui fait vieillir **les deux** versions du même stock **jour après jour** et compare, à chaque jour, les textes `nom sellIn qualite` ;
- le **maître étalon** : `@RepeatedTest(300)`, `new SplittableRandom(numéro)`, de 1 à 6 articles tirés parmi `"Tomme"`, `"Brie"`, `"Comte"`, `"Billet degustation"`, `"Sel de Guerande"`, `"Chevre"`, un `sellIn` de −3 à 15 (`nextInt(-3, 16)`), une qualité de 0 à 50 (80 pour le sel), et de 1 à 8 jours ;
- les **bords**, à la main : chaque palier du billet (11, 10, 6, 5, 1 et 0 jours restants), les limites 0 et 50 de chaque sorte de produit, un article **au-dessus** de 50, le sel avec une date passée ;
- l'exemple de `Data` après 3 jours (avec une méthode `afterDays`, étape 4).

**🧪 Expérience :** compte, dans une petite classe à part, combien des 300 stocks tirés contiennent, **au départ**, un billet **pile** à 10 jours, ou un article (autre que le sel) au-dessus de 50. Que conclus-tu ?

### ☐ Étape 3 — L'article et les règles

**👉 À toi :**
- `public record Cheese(String name, int sellIn, int quality)` : refuse une qualité négative (`"qualite negative : " + quality`) ; `Cheese next(int quality)` rend l'article du lendemain (un jour de moins, la nouvelle qualité) ;
- `@FunctionalInterface public interface AgingRule` avec `Cheese age(Cheese cheese)`, une constante `MAX_QUALITY = 50`, et une méthode `static int raise(int quality, int gain)` qui ajoute `gain` sans dépasser 50… **sans jamais baisser** un article déjà au-dessus (la réponse à la question de l'étape 1) ;
- une règle par **famille** de produits, chacune en quelques lignes :
  - `DecayRule`, construite avec la perte de base : elle perd cette quantité par jour, le **double** une fois la date passée, jamais sous 0 (la `Tomme`, la `Chevre`, tout produit inconnu, et le `Brie` qui perd 2) ;
  - `AgedRule` : le `Comte` ;
  - `TicketRule` : le billet de dégustation ;
  - le sel ne change **jamais** : une **lambda** suffit, pas besoin de classe.

Teste chaque règle **seule** si tu le souhaites, puis passe par le filet.

**❓ Question :** pourquoi `DecayRule` est-elle **paramétrée** par sa perte, au lieu d'avoir une classe `TommeRule` et une classe `BrieRule` ?

### ☐ Étape 4 — La fabrique des règles et la cave

**👉 À toi :**
- `public final class RuleBook` (constructeur privé) avec `public static AgingRule ruleFor(String name)` : une `Map<String, AgingRule>` des produits spéciaux (`Brie`, `Comte`, `Billet degustation`, `Sel de Guerande`), et la règle standard pour tous les autres (`getOrDefault`). C'est le **seul** endroit du programme qui connaît des noms de produits ;
- `public final class CheeseShop` (constructeur privé) :
  - `public static List<Cheese> nextDay(List<Cheese> stock)` : chaque article vieillit selon **sa** règle ; la liste reçue n'est pas modifiée ;
  - `public static List<Cheese> afterDays(List<Cheese> stock, int days)`.
- Plus aucune trace du legacy dans ton code ; aucune méthode de plus de **8 lignes** ; `CheeseShop.java` ne contient **aucune** chaîne de caractères. Relance tout le filet.

**❓ Questions :**
- Le patron veut vendre un nouveau produit, le `Mimolette`, qui vieillit comme le `Comte`. Que modifies-tu ? Et dans le legacy ?
- `nextDay` rend une **nouvelle** liste d'articles **nouveaux**, alors que le legacy modifiait les articles. Cite deux avantages.

### ☐ Étape 5 — La nouveauté : les produits bio

**La demande du patron :** un produit dont le nom commence par `"Bio "` (`"Bio Brie"`, `"Bio Tomme"`) vieillit selon la règle du produit qui suit (`Brie`, `Tomme`), **mais** il perd sa qualité **deux fois plus vite**. S'il en **gagne** (un `"Bio Comte"`, un `"Bio Billet degustation"`), rien ne change. Jamais sous 0.

**👉 À toi :**
- `public final class BioRule implements AgingRule` : un **décorateur** (projet 6) construit avec la règle qu'il enveloppe ; il calcule ce que la règle enveloppée ferait perdre, et le double ;
- dans `RuleBook.ruleFor` : un nom qui commence par `"Bio "` donne un `BioRule` autour de la règle du **reste** du nom ;
- les tests : `"Bio Brie"` (5 jours, qualité 10), `"Bio Tomme"` avant et après la date, `"Bio Brie"` qui tomberait sous 0, `"Bio Comte"`, `"Bio Billet degustation"` (avant l'événement et le jour où il passe), `"Bio Sel de Guerande"`. Calcule chaque résultat **sur papier** d'abord.

**❓ Questions :**
- Combien de fichiers as-tu touchés pour les produits bio ? Lesquels n'as-tu pas ouverts ?
- Pourquoi le maître étalon ne tire-t-il **jamais** de produit bio ? Que ferait le legacy d'un `"Bio Brie"` ?

### ☐ Étape 6 — Les mutants, et le bilan

**👉 À toi :** lance `Check`.

**🧪 Expérience :** garde seulement le maître étalon et relance `Check`. Quels mutants survivent, et pourquoi ceux-là ?

**❓ Le bilan du chapitre :** pour chacun des cinq principes **SOLID**, montre **où** il apparaît dans ton code final (une classe, une ligne). Puis cite **un** patron de conception que tu as utilisé dans ce projet et qui vient d'un projet précédent.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Cheese(`, `interface AgingRule`, `final class DecayRule implements AgingRule`, `final class AgedRule implements AgingRule`, `final class TicketRule implements AgingRule`, `final class BioRule implements AgingRule`, `final class RuleBook`, `final class CheeseShop`, `Map<String, AgingRule>`, `static AgingRule ruleFor(String`, une lambda (`->`) ; ni `switch`, ni `instanceof`, ni `else if`, ni rien de `Legacy`.
- **La conception :** aucune méthode de plus de **8 lignes** ; aucune chaîne de caractères dans `CheeseShop.java` ; `DecayRule`, `AgedRule` et `TicketRule` ne contiennent pas `equals(` ; `BioRule.java` contient `private final AgingRule inner`.
- **Tes tests :** au moins **100** tests, `Data.LegacyCheeseShop`, `Data.LegacyItem`, `@RepeatedTest`, `new SplittableRandom(`, `@ParameterizedTest`, `CheeseShop.afterDays(`, un produit `"Bio …"` ; ni `System.out` ni `Thread.sleep`.
- **Les 15 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p09_cheese ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 326 tests, 326 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 15/15 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
