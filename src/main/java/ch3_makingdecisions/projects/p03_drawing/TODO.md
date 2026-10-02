# Projet 3 — L'atelier de dessin en boucles

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 3) :**
- les **boucles imbriquées** ;
- calculer des bornes de boucle ;
- `continue` ;
- `while` ;
- le ternaire dans une boucle ;
- l'alignement de texte **sans** `String.format` ni `String.repeat`.

**Ce qui est donné :** `Check.java`. Argument : `5` (la taille).

**Ce que TU crées :** tout le programme, dans le paquet `ch3_makingdecisions.projects.p03_drawing`. La classe du `main` s'appelle **`Drawing`**.

**Règle du crescendo :** chapitres 1 à 3. Pas de `String.repeat`, pas de `String.format`, pas de tableau.

**À savoir :** `Check` ignore les espaces en **fin** de ligne. Seuls ceux de **tête** et du **milieu** comptent. Quand une ligne diffère, il les affiche sous forme de `·`.

---

## Le problème

Six dessins ASCII de taille n = 5. Chacun est un exercice de **géométrie des indices**. Pour chaque ligne, il faut trouver combien d'espaces et combien de caractères écrire, et où.

**La méthode qui marche :** pour chaque figure, dessine sur papier un tableau « numéro de ligne → nombre d'espaces, nombre de symboles ». Trouve la formule, puis seulement ensuite écris la boucle.

---

## Tableau de bord

### ☐ Étape 1 — Les outils

- Une méthode qui **répète** un caractère n fois, avec une boucle. `String.repeat` est au chapitre 4.
- Une méthode qui compte les **chiffres** d'un nombre, avec un `while`.
- Une méthode qui **aligne un nombre à droite** sur une largeur donnée, en le précédant d'espaces.

### ☐ Étape 2 — Pyramide et losange creux

```
    *            *
   ***          * *
  *****        *   *
 *******      *     *
*********    *       *
              *     *
               ...
```
- **La pyramide :** pour la ligne r (de 1 à n), combien d'espaces ? Combien d'étoiles ?
- **Le losange creux :** 2n − 1 lignes. **Astuce :** fais varier un indice de −(n − 1) à n − 1. La « demi-largeur » de chaque ligne dépend alors de sa **valeur absolue**, sans `Math.abs` (un ternaire suffit).
  - La pointe du haut et celle du bas n'ont qu'**une** étoile : un `if` gère ce cas.

### ☐ Étape 3 — Damier et croix

```
#.#.#.#.#.     \   /
.#.#.#.#.#      \ /
#.#.#.#.#.       +
                / \
               /   \
```
- **Le damier :** n lignes de 2n cases. La couleur d'une case dépend de la parité de `ligne + colonne`.
- **La croix :** sur une grille n × n :
  - `\` sur la diagonale principale ;
  - `/` sur l'autre diagonale ;
  - `+` au croisement ;
  - un espace ailleurs.
  - **Contrainte :** quand la case n'est sur aucune diagonale, ajoute l'espace puis **`continue`**, sans `else`.
  - **Piège :** comment écrire le caractère `\` dans un littéral `char` ?

### ☐ Étape 4 — La table de multiplication alignée

```
   |   1   2   3   4   5
---+--------------------
 1 |   1   2   3   4   5
 5 |   5  10  15  20  25
```
- Chaque produit est aligné **à droite** sur 4 caractères, et chaque numéro de ligne sur 2.

### ☐ Étape 5 — Le triangle de Pascal

```
             1
           1   1
         1   2   1
       1   3   3   1
     1   4   6   4   1
   1   5  10  10   5   1
```
- n + 1 lignes. Chaque nombre est aligné sur 4 caractères, et chaque ligne est décalée pour centrer le triangle.
- **L'algorithme sans tableau :** dans une ligne r, chaque coefficient se déduit du **précédent** :
  - `C(r, 0) = 1` ;
  - `C(r, k+1) = C(r, k) × (r − k) / (k + 1)`.
  - Vérifie-le sur la ligne 4.
- **Question :** pourquoi multiplier **avant** de diviser ? Que donnerait l'inverse ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| au moins 3 `for (` et des boucles imbriquées | 2 à 5 | ☐ |
| `while (` | 1 | ☐ |
| `continue;` | 3 | ☐ |
| ternaire `? :`, `% 2` | 2, 3 | ☐ |

---

## Sortie attendue complète

```
-- pyramide --
    *
   ***
  *****
 *******
*********
-- losange creux --
    *
   * *
  *   *
 *     *
*       *
 *     *
  *   *
   * *
    *
-- damier --
#.#.#.#.#.
.#.#.#.#.#
#.#.#.#.#.
.#.#.#.#.#
#.#.#.#.#.
-- croix --
\   /
 \ /
  +
 / \
/   \
-- table --
   |   1   2   3   4   5
---+--------------------
 1 |   1   2   3   4   5
 2 |   2   4   6   8  10
 3 |   3   6   9  12  15
 4 |   4   8  12  16  20
 5 |   5  10  15  20  25
-- pascal --
             1
           1   1
         1   2   1
       1   3   3   1
     1   4   6   4   1
   1   5  10  10   5   1
```
