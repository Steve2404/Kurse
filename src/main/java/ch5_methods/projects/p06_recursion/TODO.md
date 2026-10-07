# Projet 6 — Le laboratoire de récursivité

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 5) :** des méthodes qui **s'appellent elles-mêmes**. Pour chacune, demande-toi :
- quel est le **cas de base**, celui qui s'arrête ?
- en quoi l'appel récursif porte-t-il sur un problème **plus petit** ?
- qu'est-ce qui est **partagé** entre les appels : un compteur `static`, un tableau passé en paramètre, la mémoïsation ?

C'est **le** projet d'algorithmique du chapitre :
- exponentiation rapide ;
- Fibonacci naïf contre mémoïsé ;
- recherche dichotomique, **tri fusion**, **tri rapide** ;
- **tours de Hanoï** ;
- sous-ensembles et combinaisons ;
- **N reines** (retour arrière) ;
- **remplissage de zones** (*flood fill*) ;
- **rendu de monnaie** (programmation dynamique).

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch5_methods.projects.p06_recursion`, la classe **`RecursionLab`**.

**Règle du crescendo :** chapitres 1 à 5. `Arrays.sort` est **interdit** ici : tu tries à la main. Pas de collection.

**Ce projet est un laboratoire de récursion.** Relis d'abord la leçon de la récursion (projet 4, étape 3). Pour chaque fonction :
1. écris le **cas de base** : quand s'arrête-t-on ?
2. écris l'**appel au problème plus petit** ;
3. **déroule à la main** sur une petite valeur, un appel par ligne, en décalant chaque appel.

**Tes outils pour ce projet** (pas d'arguments) :

```
javac -d build/ch5-p06 -sourcepath src/main/java src/main/java/ch5_methods/projects/p06_recursion/RecursionLab.java
java "-Duser.language=fr" -cp build/ch5-p06 ch5_methods.projects.p06_recursion.RecursionLab
```

---

## Tableau de bord

### ☐ Étape 1 — Les bases

```
bases : 20! = 2432902008176640000, somme des chiffres de 98765 = 35, pgcd(1071, 462) = 21, 37 en binaire = 100101, kayak true, kayaks false
puissance : 3^20 = 3486784401 en 7 multiplications (au lieu de 19)
```

**📖 La leçon : une récursion qui fait quelque chose avant de se rappeler.**

```java
static void compteARebours(int n) {
    if (n == 0) {
        System.out.println("partez !");
        return;
    }
    System.out.print(n + " ");
    compteARebours(n - 1);
}
compteARebours(3);     // 3 2 1 partez !
```

Sans cas de base, la méthode s'appellerait sans fin : chaque appel occupe un peu de mémoire (la « pile »), et le programme finit par s'arrêter avec une erreur. L'expérience te la fait voir.

**👉 À toi :**

- `long factorial(int)`, `int digitSum(int)`, `int gcd(int, int)` (Euclide), `String binary(int)`, `boolean palindrome(String)` : chacune tient en une ou deux lignes.
- **`long power(long x, int n)`** : calcule `half = power(x, n / 2)`, puis `half * half`, et une multiplication de plus par x si n est impair.
  - Compte les multiplications dans un champ **`static`**.
  - **Question :** pourquoi 7 et pas 19 ? Quelle est la complexité ?
- **Expérience :** `factorial(21)` déborde un `long`. Et une récursion sans cas de base ? (`StackOverflowError`)

### ☐ Étape 2 — Fibonacci : le coût des appels répétés

```
fibonacci(25) = 75025 en 242785 appels ; memoise 75025 en 49 appels ; fibonacci(90) = 2880067194370816120
```

**📖 La leçon : se souvenir des réponses (mémoïsation).** Si une récursion recalcule souvent la même chose, on range chaque résultat dans un tableau la 1re fois, et on le relit ensuite au lieu de recalculer. Le tableau est passé en paramètre : tous les appels partagent **le même** tableau (projet 4, étape 1).

**👉 À toi :**

- `fibNaive(n)` incrémente un compteur `static calls` à chaque appel.
- `fibMemo(int n, long[] memo)` : le tableau est **partagé** par tous les appels, car c'est la même référence. Si `memo[n]` est déjà calculé, rends-le.
- Remets le compteur à 0 entre les deux mesures.
- **Question :** pourquoi 49 appels pour n = 25 ?

### ☐ Étape 3 — Diviser pour régner

```
tri fusion [3, 3, 9, 10, 27, 38, 43, 82] (17 comparaisons), tri rapide true (18 comparaisons), original [38, 27, 43, 3, 9, 82, 10, 3]
recherche recursive : 43 -> 6, 11 -> -5
```

**📖 La leçon : diviser pour régner.** Couper le problème en deux moitiés, résoudre chaque moitié **récursivement**, puis combiner les deux résultats. Trier un tableau de 1 case est le cas de base : il est déjà trié.

**👉 À toi :**

- **`void mergeSort(int[] a, int low, int high)`** :
  1. trie la moitié gauche, puis la droite ;
  2. fusionne dans un tableau temporaire (`<=` pour la stabilité) ;
  3. recopie avec `System.arraycopy`.
  - Compte les comparaisons de la fusion.
- **`void quickSort(int[] a, int low, int high)`** : partition de Lomuto (le pivot est le dernier élément), puis récursion à gauche et à droite du pivot.
- Trie **des copies** (`clone()`) de `Data.UNSORTED`. Les deux résultats sont égaux (`Arrays.equals`).
- **`int search(int[] a, int key, int low, int high)`** : la dichotomie récursive, avec la convention `-(low + 1)` et `>>> 1`.

### ☐ Étape 4 — Hanoï, sous-ensembles, combinaisons

```
hanoi 4 disques : 15 deplacements (2^4 - 1), debut : 1:A->B 2:A->C 1:B->C 3:A->B 1:C->A
sous-ensembles de {1,2,3} : {} {3} {2} {2,3} {1} {1,3} {1,2} {1,2,3}
combinaisons 3 parmi 5 (10) : 123 124 125 134 135 145 234 235 245 345
```

**📖 Conseil :** pour Hanoï, joue d'abord avec 3 pièces de monnaie de tailles différentes sur 3 assiettes. Pour les sous-ensembles, dessine un arbre : à chaque niveau, une branche « je laisse l'élément », une branche « je le prends ».

**👉 À toi :**

- **`hanoi(int disks, char from, char to, char via, StringBuilder first)`** :
  1. déplace n − 1 disques sur le piquet intermédiaire ;
  2. déplace le grand disque ;
  3. remets les n − 1 disques par-dessus.
  - Ne note que les 5 premiers déplacements.
- **`subsets`** : chaque élément est d'abord **laissé**, puis **pris**. Respecte cet ordre pour obtenir la même sortie.
- **`combinations(int start, int n, int k, String current, StringBuilder out)`** rend le nombre de combinaisons.
  - **L'élagage :** i ne dépasse pas `n - k + 1`.

### ☐ Étape 5 — Les N reines

```
reines : 6x6 -> 4 solutions, 8x8 -> 92 solutions ; premiere 6x6 :
  .Q....
  ...Q..
  .....Q
  Q.....
  ..Q...
  ....Q.
```

**📖 Conseil :** fais les 4 reines à la main sur une grille 4 × 4. Pose une reine par ligne ; quand aucune case n'est sûre, reviens à la ligne précédente et déplace sa reine. C'est le retour arrière.

**👉 À toi :**

- `int queens(int row, int[] cols, int[] firstSolution)` : `cols[r]` est la colonne de la reine de la ligne r.
  - Pour chaque colonne **sûre** (pas la même colonne, pas la même diagonale : `|Δcol| == Δligne`), pose la reine et passe à la ligne suivante.
  - Recopie la **première** solution trouvée (`firstSolution[0] == -1` au départ, grâce à `Arrays.fill`).
- Affiche chaque ligne avec `".".repeat(col) + "Q" + ".".repeat(5 - col)`.

### ☐ Étape 6 — Les îles et la monnaie

```
iles : 6 (tailles 3,3,4,5,5,1), la plus grande 5
monnaie : 4562 facons de faire 100, minimum 2 pieces, pour 63 : 4
```

**📖 Rappel :** `toCharArray()` fait une copie modifiable d'une ligne (chapitre 4, projet 7). La mémoïsation de l'étape 2.

**👉 À toi :**

- **`int fill(char[][] g, int r, int c)`** rend la taille de l'île.
  - Hors grille ou pas `#` : rends 0.
  - Sinon, **marque** la case (`~`) **avant** de recurser dans les 4 directions.
  - **Question :** que se passe-t-il si on marque après ?
  - La grille vient de `Data.MAP[r].toCharArray()`. Les îles sont trouvées dans l'ordre de lecture.
- **`long ways(int[] coins, int i, int amount, long[][] memo)`** : soit on utilise la pièce i (et on peut la reprendre), soit on passe à la suivante.
  - `memo` est initialisé à −1 avec `Arrays.fill` sur chaque ligne.
- **`int fewest(int[] coins, int amount, int[] memo)`** : le minimum de pièces.

---

## Checklist (vérifiée par `Check`)

- `Data.UNSORTED`, `Data.MAP` et `Data.COINS` ;
- `memo` ;
- `>>> 1`, `System.arraycopy`, `toCharArray()`, `repeat` ;
- `new long[` ;
- **pas** d'`Arrays.sort`.

---

## Sortie attendue complète

```
bases : 20! = 2432902008176640000, somme des chiffres de 98765 = 35, pgcd(1071, 462) = 21, 37 en binaire = 100101, kayak true, kayaks false
puissance : 3^20 = 3486784401 en 7 multiplications (au lieu de 19)
fibonacci(25) = 75025 en 242785 appels ; memoise 75025 en 49 appels ; fibonacci(90) = 2880067194370816120
tri fusion [3, 3, 9, 10, 27, 38, 43, 82] (17 comparaisons), tri rapide true (18 comparaisons), original [38, 27, 43, 3, 9, 82, 10, 3]
recherche recursive : 43 -> 6, 11 -> -5
hanoi 4 disques : 15 deplacements (2^4 - 1), debut : 1:A->B 2:A->C 1:B->C 3:A->B 1:C->A
sous-ensembles de {1,2,3} : {} {3} {2} {2,3} {1} {1,3} {1,2} {1,2,3}
combinaisons 3 parmi 5 (10) : 123 124 125 134 135 145 234 235 245 345
reines : 6x6 -> 4 solutions, 8x8 -> 92 solutions ; premiere 6x6 :
  .Q....
  ...Q..
  .....Q
  Q.....
  ..Q...
  ....Q.
iles : 6 (tailles 3,3,4,5,5,1), la plus grande 5
monnaie : 4562 facons de faire 100, minimum 2 pieces, pour 63 : 4
```
