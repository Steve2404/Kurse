# Projet 10 — La caisse et le correcteur orthographique (la programmation dynamique)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **la programmation dynamique** : des sous-problèmes **qui se répètent**, calculés **une seule fois** et rangés dans un tableau ;
- **la méthode** : définir ce que vaut une case (`best[a] = …`), la **relation** avec les cases plus petites, les **cas de départ**, l'**ordre** de remplissage ;
- **les classiques** : Fibonacci, le rendu de monnaie (le moins de pièces, le nombre de façons), la plus longue sous-suite commune, la distance d'édition, le sac à dos, la plus longue sous-suite croissante, les chemins dans une grille ;
- **pourquoi le glouton échoue** parfois, et la programmation dynamique non ;
- **retrouver la solution**, pas seulement sa valeur, en remontant la table.

**Ce que TU crées :** dans `ch17_algorithms.projects.p10_dynamic` :
- **`Dynamic`** (signatures imposées) ;
- **`DynamicTest`**, tes tests.

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : un escalier.

> **🧰 Tes outils pour ce projet**
>
> - **Remplis la table à la main** sur papier pour un petit exemple, avant de coder : c'est la meilleure façon de trouver la relation, et de l'écrire sans erreur d'indice.
> - **Lancer tes tests :** **Ctrl+Maj+F10**. **Lancer `Check` :** flèche verte à côté de `Check.main`.

---

## Tableau de bord

### ☐ Étape 1 — Ne jamais recalculer : Fibonacci

**📖 La leçon : l'escalier.** On monte un escalier une ou deux marches à la fois. De combien de façons peut-on monter 10 marches ? Pour arriver à la marche 10, on vient de la 9 **ou** de la 8 : `facons(10) = facons(9) + facons(8)`. Écrit en récursion directe, `facons(9)` recalcule `facons(8)`, qui est **aussi** recalculé par `facons(10)`… Les mêmes sous-problèmes reviennent des milliards de fois : O(2ⁿ).

La programmation dynamique calcule chaque sous-problème **une seule fois**, du plus petit au plus grand, et garde les résultats : O(n). Quand chaque case ne dépend que des deux précédentes, deux variables suffisent.

**👉 À toi :** `public final class Dynamic` (constructeur `private`) avec **`public static long fibonacci(int n)`** : F(0) = 0, F(1) = 1, F(n) = F(n−1) + F(n−2), en O(n). `n < 0` lance `IllegalArgumentException("n negatif : " + n)`.

**Tes tests :** F(0), F(1), F(2), F(10) = 55, F(50) et F(92) (le plus grand qui tienne dans un `long` : 7 540 113 804 746 346 429), chacun en moins d'une seconde ; le négatif.

**🧪 Expérience :** dans `Mesure`, écris la version récursive naïve, avec un compteur d'appels (`static long appels`), et lance-la pour n = 30, 35 et 40.

### ☐ Étape 2 — Le rendu de monnaie

**📖 La leçon : la case, la relation, le départ.** Pour rendre une somme avec le moins de pièces possible :
- **la case** : `best[a]` = le moins de pièces pour faire la somme `a` ;
- **la relation** : la dernière pièce rendue est l'une des pièces `c` ; il restait `a − c` à faire : `best[a] = 1 + min sur c de best[a − c]` ;
- **le départ** : `best[0] = 0` (rien à rendre) ;
- **l'ordre** : de `a = 1` jusqu'à la somme voulue.

Le **glouton** (« toujours la plus grosse pièce qui rentre ») marche avec nos euros, mais pas avec des pièces de 1, 3 et 4 pour faire 6 : il prend 4 + 1 + 1 (3 pièces), alors que 3 + 3 suffit.

**Compter les façons** est un autre problème : `ways[a]` = le nombre de **combinaisons** (l'ordre ne compte pas : 1 + 2 et 2 + 1 sont la même façon). L'astuce : la boucle sur les pièces est à l'**extérieur**, celle sur les sommes à l'intérieur. Ainsi, chaque combinaison est construite une seule fois, dans l'ordre des pièces.

**👉 À toi :**
- **`public static int minCoins(int[] coins, int amount)`** : le moins de pièces, ou **−1** si c'est impossible ;
- **`public static long countWays(int[] coins, int amount)`** : le nombre de combinaisons (1 pour la somme 0).

**Tes tests :** `{1, 3, 4}` pour 6 (le glouton se trompe) ; `{1, 2, 5}` pour 11 ; `{2}` pour 3 (impossible) ; la somme 0 ; les façons de faire 5 avec `{1, 2, 5}` (écris-les toutes à la main), de faire 3 avec `{2}`, de faire 0 ; et les façons de faire 2 livres avec les 8 pièces anglaises (`{1, 2, 5, 10, 20, 50, 100, 200}` pour 200 : **73 682**).

**❓ Question :** que compterait `countWays` si l'on mettait la boucle des **sommes** à l'extérieur ? Essaie avec `{1, 2}` pour 3.

### ☐ Étape 3 — Deux mots : sous-suite commune et distance d'édition

**📖 La leçon : une table à deux dimensions.** Quand le problème porte sur **deux** chaînes, la case dépend de deux longueurs : `dp[i][j]` = la réponse pour les `i` premières lettres de `a` et les `j` premières de `b`. On ajoute une ligne et une colonne 0 (le mot vide) pour les cas de départ.

- **La plus longue sous-suite commune** (*LCS*, des lettres dans le même ordre, pas forcément voisines) : si les lettres `a[i−1]` et `b[j−1]` sont égales, `dp[i][j] = dp[i−1][j−1] + 1` ; sinon, on jette la dernière lettre de l'un **ou** de l'autre : `max(dp[i−1][j], dp[i][j−1])`.
- **La distance d'édition** (Levenshtein, celle d'un correcteur orthographique) : le moins d'insertions, de suppressions ou de remplacements pour passer de `a` à `b`. `dp[i][0] = i` et `dp[0][j] = j` (tout effacer, tout écrire) ; puis le minimum entre remplacer (gratuit si les lettres sont égales), supprimer et insérer.

Pour **retrouver** la sous-suite elle-même, on **remonte** la table depuis la fin : lettres égales, on la garde et on recule en diagonale ; sinon, on recule vers la plus grande des deux cases voisines.

**👉 À toi :**
- **`public static int lcsLength(String a, String b)`** et **`public static String lcs(String a, String b)`** : en remontant, à égalité entre « en haut » (`i − 1`) et « à gauche » (`j − 1`), choisis **en haut** (pour que le résultat soit unique) ;
- **`public static int editDistance(String a, String b)`**.

**Tes tests :** `"ABCBDAB"` et `"BDCABA"` (longueur 4, sous-suite `"BCBA"`), `"chien"` et `"niche"`, deux mots sans lettre commune, un mot vide ; les distances `chien`/`chine`, `kitten`/`sitting`, vide/`abc` et l'inverse, deux mots égaux, `sunday`/`saturday`.

### ☐ Étape 4 — Le sac à dos

**📖 La leçon : prendre ou laisser.** Un randonneur peut porter 7 kg. Chaque objet a un poids et une valeur, et il n'y en a **qu'un** de chaque. `best[w]` = la meilleure valeur avec au plus `w` kg. Pour chaque objet, et pour chaque capacité, on **prend** l'objet (`best[w − poids] + valeur`) ou on le **laisse** (`best[w]`).

**Piège :** avec un seul tableau, il faut parcourir les capacités **de la plus grande à la plus petite**. Dans l'autre sens, `best[w − poids]` aurait déjà été mis à jour **avec** cet objet : on le prendrait plusieurs fois.

**👉 À toi :** **`public static int knapsack(int[] weights, int[] values, int capacity)`** : la meilleure valeur, chaque objet au plus une fois.

**Tes tests :** quatre objets dans 7 kg (calcule la réponse à la main) ; un objet trop lourd ; deux objets qui ne rentrent pas ensemble ; un objet pile à la capacité ; **un seul objet de 2 kg dans un sac de 4 kg** (il ne doit compter qu'une fois).

### ☐ Étape 5 — Croissant, et la grille

**👉 À toi :**
- **`public static int longestIncreasing(int[] a)`** : la longueur de la plus longue sous-suite **strictement** croissante, en **O(n log n)**. L'astuce : `tails[k]` = la plus **petite** fin possible d'une sous-suite croissante de longueur `k + 1`. Pour chaque nombre, une **dichotomie** (projet 1) cherche la première fin `>= lui` et la remplace ; s'il n'y en a pas, la plus longue sous-suite s'allonge ;
- **`public static long gridPaths(boolean[][] blocked)`** : le nombre de chemins du coin haut-gauche au coin bas-droit, en allant seulement à droite ou en bas, sans passer par une case bloquée (`true`). Chaque case reçoit la somme de la case du dessus et de celle de gauche ; une ligne de tableau suffit.

**Tes tests :** `{10, 9, 2, 5, 3, 7, 101, 18}` (4), trois nombres égaux (1 : strictement !), vide, croissant, décroissant ; une grille 3 × 3 libre (6), avec le centre bloqué (2), une seule case, le départ bloqué (0), une grille 18 × 18 (2 333 606 220) ; un test de vitesse (le rendu de 200 000, une distance d'édition et une LCS entre deux mots de 3000 lettres, une sous-suite croissante d'un million de nombres, un sac à dos de 200 objets et 50 000 kg) en moins de 4 secondes.

**❓ Question :** pourquoi la plus longue sous-suite croissante « bête » (comparer chaque nombre à tous les précédents) ne passerait-elle pas le test de vitesse ?

### ☐ Étape 6 — La vitesse, et les mutants

**👉 À toi :** lance `Check`, et tue les **13** mutants.

### Expériences (hors sortie attendue)

1. Dans ton sac à dos, parcours les capacités dans l'ordre **croissant**. Que rend-il pour un objet de 2 kg (valeur 3) dans un sac de 4 kg ?
2. Dans ta `lcs`, oublie le `reverse()` final. Que rend `lcs("ABCBDAB", "BDCABA")` ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class Dynamic` et ses neuf signatures exactes.
- **Tes tests :** au moins **20** tests, `@Test`, `@ParameterizedTest`, `assertEquals(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 13 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p10_dynamic ===
[PASS] tes tests sur TON code : 20 tests, 20 reussis
[PASS] tes tests sur le code de REFERENCE : 20 tests, 20 reussis
[PASS] les tests de REFERENCE sur TON code : 20 tests, 20 reussis
   mutant 1 : tue (par fibonacci)
   …
[PASS] mutants : 13/13 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
