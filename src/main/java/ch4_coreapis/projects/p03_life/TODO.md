# Projet 3 — Le jeu de la vie

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 4) :**
- les **tableaux à deux dimensions** : création, `.length` des lignes et des colonnes, parcours, for-each sur les lignes ;
- les valeurs par défaut ;
- `Arrays.toString`, `Arrays.equals` et `Arrays.deepEquals` ;
- `equals` d'un tableau ;
- un algorithme de **simulation** sur une grille.

**Ce qui est donné :** `Data.java` (deux grilles en texte) et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch4_coreapis.projects.p03_life`. La classe du `main` s'appelle **`Life`**.

**Règle du crescendo :** chapitres 1 à 4. Pas de collection.

---

## Le problème

Le **jeu de la vie** de Conway. Une grille de cellules vivantes (`#`) et mortes (`.`) évolue par générations, selon 2 règles :
- **une cellule vivante survit** si elle a 2 ou 3 voisines vivantes, parmi ses 8 voisines ;
- **une cellule morte naît** si elle a exactement 3 voisines vivantes.

Les bords sont « morts » : une case hors de la grille ne compte pas comme voisine.

Le programme fait évoluer la grille `Data.WORLD` (un **planeur**, qui se déplace, et un **clignotant**) pendant 4 générations. Il prouve ensuite qu'un clignotant revient à son état initial toutes les **2** générations.

---

## Tableau de bord

### ☐ Étape 1 — Du texte à la grille

- Transforme les lignes de texte en `boolean[][]`. Il y a autant de lignes que de chaînes, et autant de colonnes que de caractères.
- **Questions :**
  - que contient un `new boolean[3][4]` juste après sa création ?
  - dans `grid[r][c]`, quel indice désigne la ligne ?
  - `grid.length` et `grid[0].length` désignent quoi ?

### ☐ Étape 2 — Compter les voisines

- **Parcours des 8 voisines :** deux boucles de décalage, de −1 à +1.
- **À ignorer :** la cellule elle-même et toutes les cases **hors de la grille**. Un `continue` suffit.
  - **Expérience :** enlève le test des bords. Quelle exception obtiens-tu ?

### ☐ Étape 3 — Une génération

```
generation 4 (vivantes : 8)
..........
..........
...#...###
....#.....
..###.....
```
- **Le piège classique :** si tu modifies la grille **pendant** que tu la parcours, les voisines des cellules suivantes sont faussées. Il faut calculer la génération suivante dans un **nouveau** tableau.
- **Le nombre de vivantes :** for-each sur les lignes, puis for-each sur les cellules.
- **À la main :** dessine la génération 1 du planeur avant d'exécuter.

### ☐ Étape 4 — Comparer des tableaux

```
clignotant : 1 etape identique false, 2 etapes identique true, Arrays.equals(lignes) false, equals false
ligne 2 apres 1 etape : [false, true, true, true, false], dimensions 5x5
```
- **Trois comparaisons, trois réponses.** Explique en commentaire pourquoi :
  - `Arrays.deepEquals` voit l'égalité ;
  - `Arrays.equals` ne la voit **pas** : il compare des tableaux de tableaux, donc des **références** de lignes ;
  - `equals` ne la voit pas non plus.
- **Question :** un tableau a-t-il une méthode `toString` utile ? Qu'afficherait `System.out.println(once[2])` ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `new boolean[…][…]` | 1, 3 | ☐ |
| `grid[r][c]`, `.length` | 1 à 3 | ☐ |
| `continue;` | 2 | ☐ |
| for-each sur les lignes | 3 | ☐ |
| `Arrays.deepEquals`, `Arrays.equals`, `Arrays.toString` | 4 | ☐ |

---

## Sortie attendue complète

```
generation 0 (vivantes : 8)
..........
..#.......
...#...###
.###......
..........
..........
..........
generation 1 (vivantes : 8)
..........
........#.
.#.#....#.
..##....#.
..#.......
..........
..........
generation 2 (vivantes : 8)
..........
..........
...#...###
.#.#......
..##......
..........
..........
generation 3 (vivantes : 8)
..........
........#.
..#.....#.
...##...#.
..##......
..........
..........
generation 4 (vivantes : 8)
..........
..........
...#...###
....#.....
..###.....
..........
..........
clignotant : 1 etape identique false, 2 etapes identique true, Arrays.equals(lignes) false, equals false
ligne 2 apres 1 etape : [false, true, true, true, false], dimensions 5x5
```
