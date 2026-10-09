# Projet 9 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`CheeseShopTest.java`](CheeseShopTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** et **JUnit 5.11.4**.

---

## Étape 1 — Lire, et écrire les règles

La sortie de `Data` (vérifiée) :

```
jour 1 : [Tomme 1 9, Brie 0 7, Comte 0 48, Billet degustation 10 31, Billet degustation 0 43, Sel de Guerande 0 80]
jour 2 : [Tomme 0 8, Brie -1 3, Comte -1 50, Billet degustation 9 33, Billet degustation -1 0, Sel de Guerande 0 80]
jour 3 : [Tomme -1 6, Brie -2 0, Comte -2 50, Billet degustation 8 35, Billet degustation -2 0, Sel de Guerande 0 80]
```

**Question — les règles** (avec `sellIn` déjà diminué d'un jour) :
- **tous les produits sauf le sel** : `sellIn` diminue de 1 chaque nuit ;
- **produits ordinaires** (`Tomme`, `Chevre`, tout nom inconnu) : −1 par jour, −2 une fois la date passée (`sellIn < 0`), jamais sous 0 ;
- **`Brie`** : pareil, mais −2, puis −4 ;
- **`Comte`** : +1 par jour, +2 une fois la date passée, sans dépasser 50 ;
- **`Billet degustation`** : +1 ; +2 s'il reste moins de 10 jours (`sellIn < 10`, donc 10 jours ou moins avant la mise à jour) ; +3 s'il en reste moins de 5 ; sans dépasser 50 ; tombe à **0** quand l'événement est passé (`sellIn < 0`) ;
- **`Sel de Guerande`** : ne change jamais, ni la date ni la qualité (80).

**Question — au-dessus de 50 :** un `Comte` à 55 **reste à 55** (le legacy n'ajoute que si `quality < 50`). Une `Tomme` à 60, avant sa date, descend normalement à 59 : rien ne la ramène à 50. Ce n'est pas très cohérent (le maximum n'est qu'un plafond pour les **hausses**), mais pendant un refactoring on **ne corrige pas** : on fige (les tests de bords le vérifient), et l'on proposera la correction **à part**, avec son propre test, après en avoir parlé au patron. Peut-être qu'un article à 55 est voulu (un produit d'exception).

**Question — deux fois le même nom :** les deux tests doivent être identiques. Une faute dans le premier (`"Sel de Guérande"`) et le sel perdrait un jour par nuit tout en gardant sa qualité ; une faute dans le second, et il perdrait de la qualité comme un produit ordinaire. Un nom répété est un **nombre magique** en texte : dans le code final, chaque nom n'apparaît **qu'une fois**, dans la table de `RuleBook`.

---

## Étape 2 — Le filet

Le code : `sameAsLegacy`, `goldenMaster`, `edgesLikeLegacy` et `sampleAfterThreeDays` dans [`CheeseShopTest.java`](CheeseShopTest.java).

**Expérience — ce que tire le hasard** (vérifié, même tirage que le test) : sur 300 stocks, **13** contiennent au départ un billet pile à 10 jours, et **0** un article (autre que le sel) au-dessus de 50, puisque le tirage s'arrête à 50. Le maître étalon ne peut donc **pas** voir la règle « au-dessus de 50, on garde » : il faut le cas à la main. C'est le mutant 4, qui survit au maître étalon seul (étape 6).

---

## Étape 3 — L'article et les règles

Le code : [`Cheese.java`](Cheese.java), [`AgingRule.java`](AgingRule.java), [`DecayRule.java`](DecayRule.java), [`AgedRule.java`](AgedRule.java), [`TicketRule.java`](TicketRule.java). Le sel est la lambda `SALT` de [`RuleBook.java`](RuleBook.java).

**Question — une règle paramétrée :** la `Tomme` et le `Brie` suivent **la même** règle, avec un seul nombre différent. Deux classes recopieraient le même algorithme (le code dupliqué du projet 1) ; la moindre correction devrait être faite deux fois. Une classe paramétrée dit exactement ce qui varie (la perte de base) et ce qui ne varie pas (le doublement, le plancher à 0). On crée une **nouvelle classe** quand l'**algorithme** change, pas quand un **nombre** change.

---

## Étape 4 — La fabrique et la cave

Le code : [`RuleBook.java`](RuleBook.java), [`CheeseShop.java`](CheeseShop.java). Le stock d'exemple après 3 jours (vérifié) est celui du jour 3 de `Data`.

**Question — le `Mimolette` :** **une ligne** dans la table de `RuleBook` : `"Mimolette", new AgedRule()`. Rien d'autre. Dans le legacy, il faudrait modifier la condition du comté (`i.name.equals("Comte") || i.name.equals("Mimolette")`), et vérifier que la branche des « autres produits » ne s'applique plus à lui… dans une méthode de 40 lignes.

**Question — des articles nouveaux :**
1. **pas d'effet de bord surprise** : celui qui garde une référence vers le stock d'hier (un écran, un rapport) n'est pas modifié dans son dos (la surprise 3 du projet 5) ;
2. **comparer avant et après** est trivial (deux listes), ce qui facilite les tests et l'historique ; et un objet immuable peut être partagé entre plusieurs fils d'exécution sans verrou (chapitre 13).

---

## Étape 5 — Les produits bio

Le code : [`BioRule.java`](BioRule.java), et deux lignes dans `RuleBook.ruleFor`. Les valeurs (vérifiées) : `Bio Brie` (5 jours, 10) → **6** ; `Bio Tomme` (5, 10) → **8**, après la date (0, 10) → **6** ; `Bio Brie` (1, 3) → **0** ; `Bio Comte` (5, 10) → **11** ; `Bio Billet degustation` (3, 10) → **13**, et le jour où il passe (0, 10) → **0** ; `Bio Sel de Guerande` → **80**.

**Question — les fichiers touchés :** **deux** : `BioRule` (créé) et `RuleBook` (le préfixe). Sans les ouvrir : `Cheese`, `AgingRule`, les trois règles, `CheeseShop`. Le bio **s'ajoute** à n'importe quelle règle existante, même à celles qu'on écrira demain : c'est la force du décorateur.

**Question — le bio et le maître étalon :** les noms tirés ne commencent jamais par `"Bio "`. Le legacy, lui, ne connaît pas les produits bio : un `"Bio Brie"` n'est égal à aucun de ses noms, il serait traité comme un **produit ordinaire** (−1 par jour). Le comparer au legacy n'aurait aucun sens : la nouveauté se teste avec des cas écrits exprès, à partir de la demande du patron.

---

## Étape 6 — Les mutants, et le bilan

**Expérience — seulement le maître étalon** (vérifié) : 9 mutants sur 15 sont tués ; **6 survivent** : **4** (au-dessus de 50), **11**, **12** et **13** (le bio), **14** (`afterDays`) et **15** (la validation de `Cheese`). Ce sont exactement ce que le maître étalon ne peut pas voir : ce que le hasard ne tire jamais, ce que le legacy ne connaît pas, et ce que le test n'appelle pas.

**Question — le bilan SOLID :**
- **S** (une responsabilité) : `CheeseShop` fait vieillir le stock, `RuleBook` choisit la règle, chaque règle sait vieillir **une** famille de produits ;
- **O** (ouvert/fermé) : un nouveau produit ou une nouvelle famille s'ajoute **sans modifier** `CheeseShop` ni les règles existantes (une ligne dans `RuleBook`, ou une classe) ;
- **L** (Liskov) : `BioRule`, `DecayRule`, la lambda du sel… sont toutes des `AgingRule` interchangeables : `CheeseShop` les utilise sans savoir laquelle il a ;
- **I** (interfaces étroites) : `AgingRule` n'a qu'**une** méthode, ce qui permet même une lambda ;
- **D** (inversion) : `CheeseShop` dépend de l'abstraction `AgingRule`, pas des classes concrètes ; seul `RuleBook` (la racine de composition des règles) les nomme.

**Question — un patron réutilisé :** le **décorateur** du projet 6 (`BioRule` autour d'une autre règle) ; la **stratégie** et la **fabrique** du projet 2 (les règles et `RuleBook`) ; l'**objet valeur** immuable des projets 1 et 5 (`Cheese`) ; le **maître étalon** des projets 1 et 2.
