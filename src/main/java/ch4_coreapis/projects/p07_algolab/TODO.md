# Projet 7 — Le laboratoire d'algorithmes sur tableaux

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 4) :** les **algorithmes classiques** sur tableaux, écrits à la main, puis comparés à `Arrays` :
- tris par **insertion** et par **sélection** ;
- **dédoublonnage** en place ;
- **deux pointeurs** ;
- **borne inférieure** dichotomique ;
- **sommes préfixes** et **fenêtre glissante** ;
- **Kadane** (le meilleur sous-tableau) ;
- **crible d'Ératosthène** ;
- matrices : transposée, rotation, **spirale** ;
- **triangle de Pascal** en tableau irrégulier ;
- **parcours en largeur** (BFS) dans un labyrinthe, avec une file faite d'un simple `int[]`.

C'est le projet qui te rend **à l'aise en algorithmique**. Pour chaque étape, demande-toi la **complexité** : combien de tours de boucle pour n éléments ?

**Ce qui est donné :** `Data.java` (les nombres, la matrice, le labyrinthe…) et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch4_coreapis.projects.p07_algolab`. La classe du `main` s'appelle **`AlgoLab`**.

**Règle du crescendo :** chapitres 1 à 4. Pas de collection (la file du BFS est un tableau), pas de lambda, pas de récursion obligatoire (le chapitre 5 en parlera). Des méthodes `static` simples sont les bienvenues : une par étape.

---

## Tableau de bord

### ☐ Étape 1 — Deux tris à la main, puis dédoublonner

```
insertion : [1, 3, 3, 8, 8, 15, 17, 23, 29, 42] decalages=24
selection : echanges=6 identique a Arrays.sort true true original intact true
uniques : [1, 3, 8, 15, 17, 23, 29, 42] (8 sur 10)
```
- **Tri par insertion :** chaque nouvel élément glisse vers la gauche tant que son voisin est **strictement** plus grand. Compte chaque décalage.
- **Tri par sélection :** à chaque tour, cherche le minimum du reste et échange-le avec la case courante. Ne compte que les échanges **réels** (`min != i`).
- Trie toujours une **copie** de `Data.NUMBERS` (`Arrays.copyOf` pour l'une, `clone()` pour l'autre). Le dernier `true` le prouve.
- Compare avec `Arrays.sort` : `Arrays.equals` pour l'un, `Arrays.mismatch(…) == -1` pour l'autre.
- **Le dédoublonnage en place** sur le tableau trié : un indice `k` compte les valeurs uniques déjà écrites au début du tableau. Affiche ensuite `Arrays.copyOf(a, k)`.
- **Questions :**
  - pourquoi `>` et pas `>=` rend-il le tri par insertion **stable** ?
  - combien de comparaisons fait le tri par sélection pour n éléments, même si le tableau est déjà trié ?

### ☐ Étape 2 — Deux pointeurs et borne inférieure

```
paires de somme 32 : 3+29 15+17 en 7 etapes
borne inferieure : 8->3 9->5 0->0 50->10
```
- **Deux pointeurs** sur la copie triée : `left` part du début, `right` de la fin.
  - Somme trop petite → `left++` ; trop grande → `right--`.
  - Somme trouvée → note la paire, puis **saute les doublons** des deux côtés.
  - Compte les tours de la boucle (`etapes`).
- **La borne inférieure** : le premier indice dont la valeur est `>= q`, sur l'intervalle `[low, high[`.
  - Le milieu se calcule avec `(low + high) >>> 1`.
  - **Question :** pourquoi `(low + high) / 2` peut-il déborder sur un très grand tableau ?
  - Contrairement à `Arrays.binarySearch`, elle est bien définie avec des **doublons** : pour 8, elle rend le **premier** 8.

### ☐ Étape 3 — Sommes préfixes, fenêtre glissante, Kadane

```
sommes d'intervalle : [0,4]=60 [2,2]=17 [5,9]=89 [0,9]=149
fenetre de 3 : max=65 a partir de l'indice 5 [42, 15, 8]
kadane : max=6 jours 3 a 6 [4, -1, 2, 1]
```
- **Les sommes préfixes** sur `Data.NUMBERS` **non trié** : un tableau `prefix` de taille n + 1, avec `prefix[i + 1] = prefix[i] + n[i]`.
  - Chaque requête de `Data.RANGES` (fin **incluse**) se calcule en une soustraction : `prefix[fin + 1] - prefix[debut]`.
- **La fenêtre glissante** de taille `Data.WINDOW` : ajoute l'élément qui entre, retire celui qui sort. Ne re-somme jamais toute la fenêtre.
- **Kadane** sur `Data.PROFITS` : la meilleure somme qui **finit** au jour i est soit le jour i seul, soit le jour i plus la meilleure somme qui finissait la veille. Retiens aussi les bornes.
- Affiche les morceaux avec `Arrays.copyOfRange` (fin **exclue**).
- **Question :** quelle est la complexité de chaque méthode, comparée aux boucles imbriquées naïves ?

### ☐ Étape 4 — Le crible d'Ératosthène

```
premiers <= 60 (17) : 2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59
jumeaux : 6 paires
```
- Un `boolean[]` de taille `Data.SIEVE_LIMIT + 1` : `true` veut dire « barré ». Un tableau neuf vaut `false` partout.
- Pour chaque i non barré avec `i * i <= limite`, barre ses multiples **à partir de `i * i`**.
- **Les jumeaux** : les paires (p, p + 2) toutes deux premières, avec p + 2 ≤ limite.
- **Question :** pourquoi peut-on commencer à `i * i` et s'arrêter quand `i * i` dépasse la limite ?

### ☐ Étape 5 — Matrices

```
transposee : [[1, 5, 9], [2, 6, 10], [3, 7, 11], [4, 8, 12]]
rotation : [[9, 5, 1], [10, 6, 2], [11, 7, 3], [12, 8, 4]]
spirale : 1 2 3 4 8 12 11 10 9 5 6 7
diagonale : 18 transposee de la transposee identique true
```
- `Data.MATRIX` a 3 lignes et 4 colonnes : la transposée et la rotation ont **4 lignes et 3 colonnes**.
- **La rotation horaire** : la case `[r][c]` part en `[c][lignes - 1 - r]`.
- **La spirale** : quatre bornes (`top`, `bottom`, `left`, `right`) qui se resserrent. Les deux derniers côtés ne se parcourent que si les bornes ne se sont pas croisées.
  - **Expérience :** retire ces deux tests et lance avec une matrice d'**une seule ligne**. Que se passe-t-il ?
- **La diagonale** : la somme des `m[i][i]`.
- Écris une méthode `transpose` réutilisable et vérifie avec `Arrays.deepEquals` que la transposée de la transposée redonne la matrice.
  - **Question :** pourquoi `Arrays.equals` ne suffirait-il pas ici ?

### ☐ Étape 6 — Le triangle de Pascal

```
          [1]
         [1, 1]
       [1, 2, 1]
      [1, 3, 3, 1]
    [1, 4, 6, 4, 1]
  [1, 5, 10, 10, 5, 1]
[1, 6, 15, 20, 15, 6, 1]
somme de la derniere ligne : 64 = 2^6 true
```
- Un tableau **irrégulier** de `Data.PASCAL_ROWS` lignes : `new int[n][]`, puis la ligne r reçoit `new int[r + 1]`.
- Chaque case intérieure est la somme des deux cases au-dessus.
- **Le centrage :** la largeur de référence est la longueur du `Arrays.toString` de la dernière ligne. Chaque ligne reçoit `(largeur - longueur) / 2` espaces devant (`repeat`).
- **La vérification :** compare la somme à `(int) Math.pow(2, …)`. Pourquoi ce cast ?

### ☐ Étape 7 — Le plus court chemin dans un labyrinthe (BFS)

```
labyrinthe : plus court chemin = 23 pas, 45 cases explorees
  S.#******.
  *##*####*#
  ****#...*#
  #.#.#.##**
  ..#...#E#*
  .####.#*#*
  ......#***
```
- **La grille :** un `char[][]` dont chaque ligne vient de `Data.MAZE[r].toCharArray()`. C'est une copie **modifiable** : un `String` ne l'est pas.
- **Le codage d'une case** en un seul `int` : `r * colonnes + c`. Pour revenir : `cell / colonnes` et `cell % colonnes`.
- **La file** est un `int[]` de taille `lignes * colonnes`, avec deux indices `head` (lecture) et `tail` (écriture).
- **Trois tableaux de travail :**
  - `distance[]`, rempli de `-1` avec `Arrays.fill` (−1 = pas encore visitée) ;
  - `previous[]`, la case d'où l'on vient ;
  - deux petits tableaux `dr = {-1, 1, 0, 0}` et `dc = {0, 0, -1, 1}` pour les 4 voisins. **Garde cet ordre** (haut, bas, gauche, droite), sinon tu obtiendras un autre chemin de même longueur.
- **La boucle :** défile une case et compte-la (`explorees`). Arrête-toi si c'est la sortie. Sinon, enfile chaque voisin dans la grille, hors mur et non visité.
- **Le tracé :** remonte `previous` depuis la sortie jusqu'au départ et pose une `*` sur chaque case (ni `S` ni `E`). Affiche chaque ligne avec `new String(ligne)`, précédée de deux espaces.
- **Questions :**
  - pourquoi un parcours en **largeur** trouve-t-il forcément le plus **court** chemin ?
  - que se passerait-il si l'on marquait une case comme visitée au moment où on la **défile**, et non quand on l'**enfile** ?

---

## Checklist (vérifiée par `Check`)

- `Data.NUMBERS` et `Data.MAZE` ;
- `Arrays.copyOf`, `sort`, `equals`, `mismatch`, `copyOfRange`, `deepToString`, `deepEquals`, `fill` ;
- `clone()`, `toCharArray()`, `new String(…)`, `repeat` ;
- un crible `new boolean[…]` ;
- un tableau irrégulier `new int[…][]` et une grille `new char[…][]` ;
- une somme préfixe `… + 1] - …[` ;
- `>>> 1`.

---

## Sortie attendue complète

```
insertion : [1, 3, 3, 8, 8, 15, 17, 23, 29, 42] decalages=24
selection : echanges=6 identique a Arrays.sort true true original intact true
uniques : [1, 3, 8, 15, 17, 23, 29, 42] (8 sur 10)
paires de somme 32 : 3+29 15+17 en 7 etapes
borne inferieure : 8->3 9->5 0->0 50->10
sommes d'intervalle : [0,4]=60 [2,2]=17 [5,9]=89 [0,9]=149
fenetre de 3 : max=65 a partir de l'indice 5 [42, 15, 8]
kadane : max=6 jours 3 a 6 [4, -1, 2, 1]
premiers <= 60 (17) : 2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59
jumeaux : 6 paires
transposee : [[1, 5, 9], [2, 6, 10], [3, 7, 11], [4, 8, 12]]
rotation : [[9, 5, 1], [10, 6, 2], [11, 7, 3], [12, 8, 4]]
spirale : 1 2 3 4 8 12 11 10 9 5 6 7
diagonale : 18 transposee de la transposee identique true
          [1]
         [1, 1]
       [1, 2, 1]
      [1, 3, 3, 1]
    [1, 4, 6, 4, 1]
  [1, 5, 10, 10, 5, 1]
[1, 6, 15, 20, 15, 6, 1]
somme de la derniere ligne : 64 = 2^6 true
labyrinthe : plus court chemin = 23 pas, 45 cases explorees
  S.#******.
  *##*####*#
  ****#...*#
  #.#.#.##**
  ..#...#E#*
  .####.#*#*
  ......#***
```
