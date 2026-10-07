# Projet 3 — La tortue (types scellés et records)

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 7) :**
- une **interface scellée** `sealed interface Command permits …` ;
- chaque sous-type direct est **`final`** (les records le sont), **`sealed`** ou **`non-sealed`** ;
- une classe **`non-sealed`** qui **rouvre** la hiérarchie : `Macro`, étendue librement par `Square` et `Stairs` ;
- des **records** :
  - avec constructeur compact ;
  - **sans composant** (`record PenUp()`) ;
  - **récursif** (`Repeat` contient des `Command[]`) ;
  - avec des **champs `static`** (`Turn.RIGHT`) ;
- un enum `Direction` avec des champs et `ordinal()` ;
- l'interprétation d'une hiérarchie scellée par une chaîne d'`instanceof` avec pattern.

Côté algorithmes :
- un **analyseur récursif** pour les blocs `REPEAT n [ … ]` imbriqués ;
- un **interpréteur** récursif ;
- un dessin sur une grille.

**Ce qui est donné :** `Data.java` (le programme) et `Check.java`.

**Ce que TU crées :** dans le paquet `ch7_beyondclasses.projects.p03_turtle` :
- `Command` ;
- les records `Move`, `Turn`, `PenUp`, `PenDown`, `Repeat` ;
- `Macro`, `Square`, `Stairs` ;
- `Direction`, `Turtle` ;
- **`TurtleApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 7. Pas de collection (tableaux qui grandissent avec `Arrays.copyOf`). Pas de lambda. Le `switch` avec pattern (`case Move m ->`) n'existe pas en Java 17 : on enchaîne les `instanceof`.

---

## Tableau de bord

### ☐ Étape 1 — Les commandes

- **`sealed interface Command permits Move, Turn, Repeat, PenUp, PenDown, Macro`**, avec une seule méthode : `int size()`. C'est le nombre de commandes élémentaires une fois tout déplié.
- **`record Move(int steps)`** :
  - son constructeur compact remplace un nombre négatif par 0 ;
  - `size()` = 1.
- **`record Turn(int quarters)`** :
  - `public static final Turn RIGHT = new Turn(1)` et `LEFT = new Turn(-1)` ;
  - `size()` = 1.
- **`record PenUp()`** et **`record PenDown()`** : aucun composant ; `size()` = 1.
- **`record Repeat(int times, Command[] body)`** :
  - le constructeur compact copie le tableau ;
  - l'accesseur `body()` rend une copie ;
  - `size()` = `times` × la somme des tailles du corps ;
  - `depth()` = 1 + la plus grande profondeur des `Repeat` du corps.
- **`non-sealed abstract class Macro implements Command`** :
  - un nom ;
  - `abstract Command[] expand()` ;
  - `size()` = la somme des tailles de l'expansion.
- **`Square(int side)`** se déplie en `Repeat(4, {Move(side), RIGHT})`.
- **`Stairs(int steps)`** se déplie en `Repeat(steps, {Move(2), RIGHT, Move(1), LEFT})`.
- **Expériences :**
  - écris `class Jump implements Command` (absente de `permits`) : quelle erreur ?
  - retire `non-sealed` de `Macro` : quelle erreur ?
  - `class Spiral extends Square` : pourquoi est-ce permis ?

### ☐ Étape 2 — Analyser le programme

```
programme : 14 commandes SQUARE PenUp Move PenDown Repeat PenUp Turn Move Turn Move Turn Turn PenDown STAIRS
deplie : 57 commandes elementaires, imbrication 2
records : Move[steps=3] Move[steps=0] Turn[quarters=1] PenUp[] egal true ; Repeat.equals compare les tableaux par reference : false
```
- **L'analyseur, dans `TurtleApp` :**
  - des champs `private static String[] tokens` et `int pos` ;
  - `static Command[] parseBlock()` lit des commandes jusqu'à `]` ou la fin, avec un `switch` expression sur le mot.
  - **`REPEAT`** est le `default` : il lit le nombre, saute `[`, rappelle `parseBlock()` (**récursion**), saute `]`, puis fait `yield new Repeat(...)`.
- **Ligne 1 :** pour chaque commande de premier niveau, affiche le nom de la macro (`m.name()`) si c'est une `Macro`, sinon `getClass().getSimpleName()`.
- **Ligne 2 :** la somme des `size()`, et la plus grande `depth()` des `Repeat`.
- **Ligne 3 :**
  - les `toString` générés de `new Move(3)`, `new Move(-2)`, `Turn.RIGHT` et `new PenUp()` ;
  - `new Move(3).equals(new Move(3))` ;
  - l'égalité de deux `Repeat(1, new Command[0])`.
  - **Question :** pourquoi la dernière est-elle fausse ?

### ☐ Étape 3 — Dessiner

```
|
| ##### ###
| #   #   ###
...
position (12,7) cap EAST, distance 73, virages 26
```
- **`enum Direction`** : `NORTH(-1, 0)`, `EAST(0, 1)`, `SOUTH(1, 0)`, `WEST(0, -1)`. `turn(int quarters)` rend `values()[Math.floorMod(ordinal() + quarters, 4)]`.
- **`Turtle(int rows, int cols, int row, int col)`** :
  - une grille de `char` remplie d'espaces ;
  - la tortue regarde vers `EAST`, crayon baissé ;
  - elle marque `#` sur sa case de départ.
- **`run(Command c)`**, une chaîne d'`instanceof` avec pattern :
  - `Move` avance pas à pas (compte la distance et marque si le crayon est baissé) ;
  - `Turn` tourne (compte les virages) ;
  - `Repeat` exécute `times` fois chaque commande du corps (récursion) ;
  - `PenUp` lève le crayon ; `PenDown` le baisse et marque la case ;
  - `Macro` exécute son `expand()`.
- **`picture()`** rend les lignes jusqu'à la dernière non vide, chacune précédée de `|` et sans espaces finaux.
- **`report()`** = `position (r,c) cap X, distance N, virages M`.
- La grille vient de `Data.ROWS` × `Data.COLS`, avec un départ en (1, 1).

---

## Checklist (vérifiée par `Check`)

- `Data.PROGRAM` et `Data.ROWS` ;
- `sealed interface Command permits` et `non-sealed abstract class Macro` ;
- 2 `extends Macro` ;
- `record Move(`, `record Turn(`, `record Repeat(`, `record PenUp()`, `record PenDown()` ;
- `public Move {` ;
- `enum Direction` ;
- `instanceof Repeat r`, `instanceof Macro`, `yield`.

---

## Sortie attendue complète

```
programme : 14 commandes SQUARE PenUp Move PenDown Repeat PenUp Turn Move Turn Move Turn Turn PenDown STAIRS
deplie : 57 commandes elementaires, imbrication 2
records : Move[steps=3] Move[steps=0] Turn[quarters=1] PenUp[] egal true ; Repeat.equals compare les tableaux par reference : false
|
| ##### ###
| #   #   ###
| #   #     ###
| #   #       ####
| #####          ###
|                  ###
|                    ##
|
| ###
|   ###
|     ###
|       #
position (12,7) cap EAST, distance 73, virages 26
```
