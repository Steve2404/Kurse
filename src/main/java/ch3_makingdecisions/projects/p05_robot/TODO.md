# Projet 5 (CAPSTONE) — Le robot sur une grille

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées :** tout le chapitre 3, avec les chapitres 1 et 2.
- les boucles **imbriquées** ;
- **`continue` et `break` étiquetés** ;
- `continue` simple ;
- `while` ;
- `if` / `else if` ;
- le `switch` expression ;
- une simulation à **état**.

**Ce qui est donné :** `Check.java`. Arguments : `10 8 25` (largeur, hauteur, batterie), puis des couples « direction pas » : `E 3 N 2 X 1 E 4 S 6 O 2 N 9 E 1 N 7`.

**Ce que TU crées :** tout le programme, dans le paquet `ch3_makingdecisions.projects.p05_robot`. La classe du `main` s'appelle **`Robot`**.

**Règle du crescendo :** chapitres 1 à 3. Pas de tableau (donc pas de carte stockée), pas de méthode de `String`.

**Indication :** peu d'indices ici. Rejoue la simulation **à la main**, case par case, avant de coder.

**C'est le projet-bilan du chapitre 3.** Il n'y a pas de leçon nouvelle. Quand tu bloques, relis la leçon d'origine :

| Tu dois… | Leçon à relire |
|---|---|
| un `switch` qui rend une valeur | projet 1, étape 3 |
| parcourir `args` deux par deux | projet 1, étape 1 (la boucle `for`) |
| sauter une commande avec `continue` | projet 3, étape 3 |
| une boucle `while` | projet 1, étape 5 |
| sortir de **plusieurs** boucles ou passer au tour suivant d'une boucle extérieure (étiquettes) | projet 2, étapes 1 et 5 |
| dessiner une grille avec deux boucles | projet 3, étape 2 |
| une chaîne `if` / `else if` | projet 1, étapes 1 et 4 |

**Tes outils pour ce projet :**
- **Arguments dans IntelliJ :** Run → Edit Configurations… → **Robot** → Program arguments :

```
10 8 25 E 3 N 2 X 1 E 4 S 6 O 2 N 9 E 1 N 7
```

- **Terminal** (depuis `Kurse`) :

```
javac -d build/ch3-p05 src/main/java/ch3_makingdecisions/projects/p05_robot/Robot.java
java "-Duser.language=fr" -cp build/ch3-p05 ch3_makingdecisions.projects.p05_robot.Robot 10 8 25 E 3 N 2
```

**Conseil :** dessine la grille (10 × 8) sur papier quadrillé, place les obstacles, puis suis le robot à la main, commande par commande.

---

## Le problème

Un robot part de la case **(0, 0)**, en bas à gauche, sur une grille `largeur × hauteur`. Il exécute des commandes « direction + nombre de pas » :
- les directions sont `N` (haut), `S` (bas), `E` (droite) et `O` (gauche) ;
- toute autre direction est **ignorée**.

**Les règles :**
1. Il avance **pas à pas**. Chaque pas coûte 1 point de batterie.
2. **Un mur** (le bord de la grille) arrête la commande en cours. Le robot passe à la **commande suivante**.
3. **Un obstacle** arrête aussi la commande en cours. Il n'y a pas de carte en mémoire : la case (x, y) est bloquée si `(3x + 5y) mod 11 = 0`, sauf la case de départ.
4. **Batterie vide** : la simulation s'arrête **complètement**, même s'il reste des commandes.

**À la fin :**
- un **bilan** : pas parcourus, position, distance de Manhattan au départ, batterie ;
- la **carte** :
  - `R` pour le robot ;
  - `S` pour le départ ;
  - `#` pour les obstacles ;
  - `.` ailleurs ;
  - la ligne du haut en premier, chaque ligne précédée de son numéro.

---

## Tableau de bord

### ☐ Étape 1 — Les directions et les obstacles

- **Le déplacement :** deux `switch` expressions donnent le déplacement horizontal et vertical d'une direction (0 si elle est inconnue).
- **Les obstacles :** une méthode qui dit si une case est un obstacle, selon la formule ci-dessus.

### ☐ Étape 2 — La boucle des commandes

```
1. E3 : arrive en (3,0), batterie 22
3. X1 : direction inconnue, ignoree
```
- Les commandes commencent à `args[3]` et vont **par deux**. Quel pas d'incrément ?
- **Une direction inconnue :** un `continue` **simple**.

### ☐ Étape 3 — Les pas : trois sorties différentes

```
4. E4 : obstacle en (4,2), arret en (3,2) apres 0 pas
5. S6 : mur atteint en (3,0) apres 2 pas
```
- **Les pas :** une boucle `while` (« tant qu'il reste des pas »).
- **À chaque pas, dans cet ordre :**
  1. **batterie vide** → arrêt **de tout le programme de commandes** : un `break` **étiqueté** ;
  2. **mur** (la case suivante sort de la grille) → **commande suivante** : un `continue` **étiqueté** ;
  3. **obstacle** sur la case suivante → commande suivante ;
  4. sinon, on avance.
- **Question :** pourquoi un `break` **simple** dans le `while` ne suffirait-il pas pour le mur ? Que se passerait-il avec la ligne « arrive en … » ?

### ☐ Étape 4 — Le bilan et la carte

```
BILAN : 17 pas parcourus, position (2,7), distance au depart 9, batterie 8
7 ..R#......
...
0 S.........
```
- **La carte :** deux boucles imbriquées. Les lignes vont de haut en bas (indice **décroissant**), les colonnes de gauche à droite.
- **La priorité entre les symboles** quand plusieurs s'appliquent : `R`, puis `S`, puis `#`, puis `.`. Une chaîne `if` / `else if`.

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| étiquette de boucle | 2, 3 | ☐ |
| `continue;` (simple) | 2 | ☐ |
| `i += 2` | 2 | ☐ |
| `continue étiquette;`, `break étiquette;` | 3 | ☐ |
| `while (`, `\|\|` | 3 | ☐ |
| 2 `switch (` | 1 | ☐ |
| `else if` | 4 | ☐ |

---

## Sortie attendue complète

```
1. E3 : arrive en (3,0), batterie 22
2. N2 : arrive en (3,2), batterie 20
3. X1 : direction inconnue, ignoree
4. E4 : obstacle en (4,2), arret en (3,2) apres 0 pas
5. S6 : mur atteint en (3,0) apres 2 pas
6. O2 : arrive en (1,0), batterie 16
7. N9 : obstacle en (1,6), arret en (1,5) apres 5 pas
8. E1 : arrive en (2,5), batterie 10
9. N7 : mur atteint en (2,7) apres 2 pas
BILAN : 17 pas parcourus, position (2,7), distance au depart 9, batterie 8
7 ..R#......
6 .#........
5 ..........
4 ........#.
3 ......#...
2 ....#.....
1 ..#.......
0 S.........
```
