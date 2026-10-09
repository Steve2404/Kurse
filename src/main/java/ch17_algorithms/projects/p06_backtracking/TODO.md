# Projet 6 — Le planning et le sudoku (récursivité et retour arrière)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **diviser pour régner** par récursivité : la puissance rapide, O(log n) ;
- **le retour arrière** (*backtracking*) et son gabarit : **choisir**, **explorer**, **défaire** ;
- **énumérer** : permutations, sous-ensembles, combinaisons, parenthèses bien formées ;
- **élaguer** : refuser un choix impossible **avant** de descendre plus loin (les N reines, le sudoku) ;
- **les pièges** : la copie oubliée, le choix pas défait, la pile d'appels qui déborde.

**Ce que TU crées :** dans `ch17_algorithms.projects.p06_backtracking` :
- **`Backtracking`** (signatures imposées) ;
- **`BacktrackingTest`**, tes tests.

**Règle du crescendo :** tout Java 17, JUnit et Mockito ; pas de `Math.pow` dans ton code (tu écris la puissance). Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : un cadenas à code, et un labyrinthe.

> **🧰 Tes outils pour ce projet**
>
> - **Voir la récursivité :** pose un point d'arrêt au début d'une méthode récursive et lance un petit test en mode **Debug** (chapitre 16, projet 7). Le panneau **Frames** montre la pile des appels : un niveau par appel en cours.
> - **Lancer tes tests :** **Ctrl+Maj+F10**. **Lancer `Check` :** flèche verte à côté de `Check.main`.

---

## Tableau de bord

### ☐ Étape 1 — Diviser pour régner : la puissance rapide

**📖 La leçon : couper le problème en deux.** Pour calculer 3¹⁶, multiplier 16 fois 3 marche, mais on peut faire mieux : 3¹⁶ = (3⁸)², 3⁸ = (3⁴)², 3⁴ = (3²)², 3² = 3 × 3. **4** multiplications au lieu de 16. Pour un exposant impair : 3⁹ = (3⁴)² × 3. Chaque appel **divise l'exposant par deux** : **O(log n)** appels.

Une méthode récursive a toujours un **cas de base** (qui s'arrête sans s'appeler) et un **cas récursif** (qui s'appelle sur un problème **plus petit**). Sans cas de base, ou si le problème ne rapetisse pas, la pile d'appels déborde (chapitre 5).

**👉 À toi :** `public final class Backtracking` (constructeur `private`) avec **`public static long power(long base, int exp)`** : `base` à la puissance `exp`, en O(log exp). Un exposant négatif lance `IllegalArgumentException("exposant negatif : " + exp)`.

**Tes tests :** quelques puissances (dont un exposant 0, une base négative, 2⁶²), et **1 à la puissance un milliard** en moins d'une seconde (avec `assertTimeoutPreemptively`) ; l'exposant négatif.

**🧪 Expérience :** dans `Mesure`, écris la version naïve récursive (`return e == 0 ? 1 : b * naive(b, e - 1);`) et appelle-la avec la base 1 et l'exposant 5 000, puis 1 000 000. Que se passe-t-il ?

### ☐ Étape 2 — Le gabarit du retour arrière : les permutations

**📖 La leçon : choisir, explorer, défaire.** Pour essayer tous les codes d'un cadenas à 3 molettes, on tourne la 1re molette sur un chiffre, puis on essaie tous les codes des 2 molettes suivantes, puis on **remet** la molette et on passe au chiffre suivant. En code, le gabarit est toujours le même :

```java
void explorer(etatEnCours) {
    if (solution complète) { resultats.add(COPIE de l'état); return; }
    for (chaque choix possible) {
        faire le choix;            // choisir
        explorer(etatEnCours);     // explorer la suite
        défaire le choix;          // défaire : l'état redevient comme avant
    }
}
```

Deux pièges classiques :
- **la copie** : `resultats.add(etat)` ajoute **la même liste** à chaque fois ; elle sera vidée par les « défaire » suivants. Il faut `resultats.add(new ArrayList<>(etat))` ;
- **défaire tout ce qu'on a fait**, dans l'ordre inverse.

**👉 À toi :** **`public static List<List<Integer>> permutations(List<Integer> items)`** : toutes les façons d'ordonner les éléments (tous différents), dans l'ordre où l'on choisit les éléments de gauche à droite (pour `[1, 2, 3]` : `[1, 2, 3]`, `[1, 3, 2]`, `[2, 1, 3]`…). Un tableau `boolean[] used` dit quels éléments sont déjà placés.

**Tes tests :** `[1, 2, 3]` (la liste exacte), la liste vide (une seule permutation : la liste vide), 7 éléments (5040 permutations).

**🧪 Expérience :** remplace `new ArrayList<>(current)` par `current`. Que contient le résultat pour `[1, 2, 3]` ?

**❓ Question :** combien de permutations pour n éléments ? Quelle est donc la complexité de ta méthode ?

### ☐ Étape 3 — Énumérer : sous-ensembles, combinaisons, parenthèses

**📖 La leçon : un arbre de décisions.** Chaque problème d'énumération est un **arbre** : à chaque nœud, un choix ; à chaque feuille, une solution. Pour les sous-ensembles de `{1, 2, 3}`, le choix de chaque élément est binaire (« sans lui » ou « avec lui ») : 2 × 2 × 2 = **8** feuilles.

**👉 À toi :**
- **`public static List<List<Integer>> subsets(List<Integer> items)`** : tous les sous-ensembles, en décidant **d'abord sans** l'élément, **puis avec** (pour `[1, 2, 3]` : `[]`, `[3]`, `[2]`, `[2, 3]`, `[1]`…) ;
- **`public static List<List<Integer>> combinationSum(int[] candidates, int target)`** : les combinaisons de candidats (tous différents et positifs, chacun **réutilisable** autant qu'on veut) dont la somme vaut `target`. Chaque combinaison est en ordre croissant, et la liste est dans l'ordre lexicographique. Pour éviter les doublons (`[2, 3]` et `[3, 2]`), on ne revient **jamais** à un candidat plus petit : trie les candidats, et passe l'indice de départ à l'appel récursif. Coupe dès qu'un candidat dépasse ce qui reste ;
- **`public static List<String> parentheses(int n)`** : toutes les chaînes de `n` paires de parenthèses bien formées, `(` essayée avant `)`. On peut ouvrir tant qu'on a ouvert moins de `n` fois ; on peut fermer seulement s'il reste une ouverte non fermée.

**Tes tests :** les sous-ensembles de `[1, 2, 3]` (la liste exacte), de la liste vide, et de 10 éléments (1024, tous différents : compte-les dans un `HashSet`) ; les combinaisons pour `{2, 3, 6, 7}` → 7 et `{5, 3, 2}` → 8, sans solution, cible 0 ; les parenthèses pour 3, 1, 4 (14) et 0.

**❓ Question :** pourquoi `combinationSum` passe-t-il `i` (et pas `i + 1`) à l'appel récursif ?

### ☐ Étape 4 — Élaguer : les N reines

**📖 La leçon : couper les branches mortes.** Dans un labyrinthe, dès qu'un couloir est un cul-de-sac, on fait demi-tour **tout de suite** : on n'explore pas toutes ses suites. C'est l'**élagage**. Pour placer 8 reines sur un échiquier sans qu'elles se menacent, essayer les 8⁸ = 16 millions de placements puis vérifier serait lent. On place **une reine par ligne**, et on refuse **tout de suite** une case attaquée (même colonne, même diagonale) : on ne descend jamais dans une branche déjà perdue.

Pour savoir en O(1) si une case est attaquée, trois tableaux de `boolean` : les colonnes prises, les diagonales `\` prises (indice `ligne - colonne + n`, constant sur une diagonale), les diagonales `/` prises (indice `ligne + colonne`).

**👉 À toi :** **`public static int nQueens(int n)`** : le nombre de façons de placer `n` reines.

**Tes tests :** n = 1 à 10 (1 → 1, 2 → 0, 3 → 0, 4 → 2, 6 → 4, 8 → 92, 10 → 724), et **13 reines** (73 712 solutions) en moins de 3 secondes.

**🧪 Expérience :** retire la vérification des diagonales `/`. Combien de « solutions » trouves-tu pour 8 reines ?

### ☐ Étape 5 — Le sudoku

**👉 À toi :** **`public static boolean solveSudoku(int[][] grid)`** : remplit la grille 9 × 9 (0 = case vide) et rend `true`, ou rend `false` si elle n'a pas de solution.
- **D'abord**, vérifie les chiffres de départ : une grille qui contient déjà deux fois le même chiffre sur une ligne, une colonne ou un carré 3 × 3 rend `false` ;
- puis le retour arrière, case par case : pour une case vide, essaie 1 à 9 ; un chiffre déjà présent sur la ligne, la colonne ou le carré est refusé tout de suite (élagage) ; si aucun chiffre ne marche, **remets la case à 0** (défaire) et rends `false`.

Le carré 3 × 3 de la case `(r, c)` commence en `(r / 3 * 3, c / 3 * 3)`.

**Tes tests :** une grille classique (sa solution exacte) ; la grille d'Arto Inkala, présentée comme « la plus difficile du monde », en moins de 3 secondes ; trois grilles de départ fausses, dont une **presque vide** avec deux 5 sur la première ligne (pourquoi celle-là ? c'est une question).

**❓ Question :** pourquoi la grille presque vide est-elle le meilleur test de la vérification de départ ?

### ☐ Étape 6 — La vitesse, et les mutants

**👉 À toi :** lance `Check`, et tue les **13** mutants.

### Expériences (hors sortie attendue)

1. Dans ton sudoku, retire la remise à 0 (`grid[r][c] = 0`) avant le `return false` final. Quelle grille ne se résout plus correctement ?
2. Pose un point d'arrêt dans `parentheses` pour `n = 2`, et regarde la pile des appels (*Frames*) au moment où la 1re solution est ajoutée. Combien de niveaux ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class Backtracking` et les huit signatures exactes, `new ArrayList<>(current)` ; jamais `Math.pow`.
- **Tes tests :** au moins **20** tests, `@Test`, `@ParameterizedTest`, `assertEquals(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 13 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p06_backtracking ===
[PASS] tes tests sur TON code : 22 tests, 22 reussis
[PASS] tes tests sur le code de REFERENCE : 22 tests, 22 reussis
[PASS] les tests de REFERENCE sur TON code : 22 tests, 22 reussis
   mutant 1 : tue (par fastPower [1^1000000000 = 1])
   …
[PASS] mutants : 13/13 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
