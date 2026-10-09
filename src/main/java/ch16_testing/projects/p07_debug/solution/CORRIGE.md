# Projet 7 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code corrigé est dans [`Inventory.java`](Inventory.java) (chaque correction porte son numéro), et les tests de non-régression dans [`InventoryTest.java`](InventoryTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4** : en lançant `Data`, et sur des copies de l'inventaire avec un seul bug à la fois.

---

## Étape 1 — Reproduire

La sortie de `Data` (vérifiée) :

```
PEN en stock : 200
prix moyen : 14606
expedier 200 PEN : false
PEN en stock : 200
valeur du stock : 706375879
INK et CUP au meme niveau : false
stock bas (< 100) : [GLUE, PAD, PEN]
```

**Question — les lignes :** cinq lignes diffèrent (le prix moyen, l'expédition, le stock de `PEN` après, la valeur du stock, la comparaison INK/CUP). Deux sont justes : la 1re (`PEN en stock : 200`) et la dernière (`stock bas`). **Non**, une ligne juste ne prouve pas un code juste : la dernière est juste **par hasard**, deux bugs s'y annulent (étape 7). Un seul exemple qui marche ne prouve rien.

---

## Étape 2 — Le premier point d'arrêt (BUG 1)

**Question — `==` et `equals` :** `item.sku == sku` vaut **`false`**, `item.sku.equals(sku)` vaut **`true`**. Les deux chaînes contiennent `PEN`, mais ce sont **deux objets différents** : chacune a été fabriquée par un `split` différent. `==` compare les références (les numéros `{String@…}` du débogueur diffèrent), `equals` compare le contenu. Résultat : chaque livraison de `PEN` crée un **nouvel** article, et la liste contient deux `PEN` (120 et 80).

**Question — pourquoi `quantity` rendait 200 :** parce que `quantity` **additionne** tous les articles du même code (`total += item.qty`) et compare avec `equals` : 120 + 80 = 200. Le doublon est invisible dans cette méthode-là, mais pas ailleurs (la moyenne, l'expédition).

**Question — la même ligne deux fois :** oui, le bug apparaît quand même. `"PEN;10;100".split(";")[0] == "PEN;10;100".split(";")[0]` vaut **`false`** (vérifié) : `split` fabrique une **nouvelle** chaîne à chaque appel. Même avec la même ligne, les deux codes sont deux objets.

**Le test :** `sameSkuTwiceMakesOneItem` (avec le bug, le prix moyen compterait `PEN` deux fois, et `ship("PEN", 15)` échouerait car le premier `PEN` n'en a que 10).

---

## Étape 3 — Le point d'arrêt conditionnel (BUG 2)

Au point d'arrêt : `item.qty` = 200, `qty` = 200, et `item.qty > qty` est **faux**. On ne peut pas expédier **exactement** tout le stock : il faut `>=`.

**Question — l'autre raison :** avec le BUG 1, `ship` s'arrête au **premier** article `PEN` trouvé, qui n'en a que **120** : 120 > 200 est faux, donc `false`, même si le stock total (200) suffisait.

---

## Étape 4 — Évaluer une expression (BUG 3)

Au point d'arrêt sur `BOX` (vérifié) : `item.qty * item.priceCents` vaut **704 982 704**, et `(long) item.qty * item.priceCents` vaut **4 999 950 000**.

**Question — pourquoi ça déborde :** Java calcule d'abord `item.qty * item.priceCents` avec deux `int` (le `Integer` est déballé en `int`). Le résultat exact, 4 999 950 000, dépasse `Integer.MAX_VALUE` (2 147 483 647) : il « fait le tour » (chapitre 2) et donne 704 982 704. C'est seulement **ensuite** qu'il est ajouté au `long`. La conversion doit avoir lieu **avant** la multiplication, sur l'un des deux opérandes : `(long) item.qty * item.priceCents`.

---

## Étape 5 — Surveiller des variables (BUG 4)

**Question — les deux écarts** (vérifiés) :
- `LegacyInventory` : `sum` = 102 243 et `n` = **7**, car `PEN` est compté deux fois (BUG 1) ; 102 243 / 7 = **14 606** ;
- ton `Inventory` corrigé du BUG 1 : `sum` = 102 093 et `n` = 6 ; 102 093 / 6 = 17 015,5, **tronqué** à **17 015** (BUG 4) ;
- avec l'arrondi : (102 093 + 3) / 6 = **17 016**.

---

## Étape 6 — Le cache des `Integer` (BUG 5)

Au point d'arrêt (vérifié) : `find(a).qty` et `find(b).qty` affichent tous deux 1000, mais `==` vaut **`false`** et `equals` vaut **`true`**. `(Integer) 127 == (Integer) 127` vaut **`true`**, `(Integer) 128 == (Integer) 128` vaut **`false`**.

**Question — pourquoi pas 100 :** 100 est dans le **cache** (−128 à 127) : `Integer.valueOf(100)` rend toujours le **même** objet, donc `==` est vrai. Un test avec des petites quantités passerait avec le bug. Il faut une valeur **hors** du cache, comme 1000.

---

## Étape 7 — Un bug en cache un autre (BUG 6)

Avec les bugs 1 à 5 corrigés et le BUG 6 encore là, la dernière ligne devient **`[GLUE, PAD]`** (vérifié) : `PEN` a disparu. Au premier tour, `i` vaut **1**.

**Question — pourquoi c'était juste avant :** dans `LegacyInventory`, le BUG 1 avait créé **deux** articles `PEN` : le premier à l'indice 0 (sauté par le BUG 6) et le second à l'indice 3, avec 80 pièces, sous le seuil de 100. C'est ce **doublon** qui mettait `PEN` dans la liste. Corriger le BUG 1 a **révélé** le BUG 6. C'est pour cela qu'on corrige un bug à la fois, et qu'on relance tout après chaque correction.

---

## Étape 8 — Valider les entrées

**Le code :** le `try`/`catch` de `receive`, le contrôle de `ship`, et les tests `malformedDeliveriesAreRejectedWithoutChangingTheStock`, `shippingZeroIsRejected`, `averageOfNothingIsZero` et `dataScenario`.

**L'expérience :** le débogueur s'arrête **dans le code de Java**, à l'endroit où l'exception est lancée : dans `Integer.parseInt` (la pile vérifiée commence par `NumberFormatException.forInputString`, puis `Integer.parseInt`). Le message de l'exception est `For input string: "cinq"`. Dans *Frames*, un clic sur la ligne en dessous ramène à **ta** méthode `receive`, avec ses variables (`line`, `parts`).

---

## Étape 9 — Les mutants

Tous les mutants sont tués par les tests de référence (vérifié). Les mutants 1 à 6 sont les six bugs du programme d'origine : chaque test « BUG n » est un **verrou**. Si quelqu'un réintroduit le bug dans six mois, le test le dira tout de suite.
