# Projet 7 (CAPSTONE) — Le tableur

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées :** tout le chapitre 7 :
- des **records** (`Ref`, et les nœuds de formule) ;
- un **enum** avec un corps par constante (`Op`) ;
- deux **interfaces scellées** dont les implémentations sont des **records imbriqués** :
  - `Expr`, où `permits` est omis car tout est dans le même fichier ;
  - `Content`, où `permits` est explicite ;
- une classe imbriquée **`static`** (`Sheet.Parser`) et une classe **interne** (`Sheet.Evaluator`, avec `Sheet.this`) ;
- une classe **locale** (`Graph`, dans `recalculate`) et une classe **anonyme** (`Sheet.CellVisitor`) ;
- le polymorphisme par `instanceof` et pattern.

Côté algorithmes :
- un **analyseur récursif** de formules (`+ - * /`, parenthèses, `SUM(A1:B2)`) ;
- le **graphe de dépendances** ;
- le **tri topologique de Kahn** ;
- la **détection des cycles** (`#CYCLE`) ;
- le recalcul après modification.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch7_beyondclasses.projects.p07_sheet` :
- `Ref`, `Op`, `Expr`, `Content` ;
- `Sheet` (avec ses classes imbriquées) ;
- **`SheetApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 7. Pas de collection ni de lambda.

---

## Tableau de bord

### ☐ Étape 1 — Les briques

- **`record Ref(int col, int row)`** :
  - `static Ref parse("B3")` donne la colonne 1 et la ligne 2 (à partir de 0) ;
  - `toString()` redonne `B3`.
- **`enum Op`** :
  - `PLUS('+')`, `MINUS('-')`, `TIMES('*')`, `DIVIDE('/')` ;
  - chacun implémente `public abstract double apply(double a, double b)` dans **son corps**. La division par 0 rend `Double.NaN` ;
  - `symbol()` et `static Op of(char)`.
- **`sealed interface Expr`**, **sans** `permits`, contient les records **imbriqués** `Num(double value)`, `Cell(Ref ref)`, `Binary(Op op, Expr left, Expr right)` et `Sum(Ref from, Ref to)`.
  - `static int references(Expr e, Ref[] out, int n)` ajoute à `out` toutes les références (récursif). `Sum` ajoute chaque cellule du rectangle.
- **`sealed interface Content permits Content.Number, Content.Text, Content.Formula`**, avec les records imbriqués `Number(double)`, `Text(String)` et `Formula(String source, Expr expr)`.

### ☐ Étape 2 — La feuille

```
arbre de B3 : Binary[op=PLUS, left=Binary[op=DIVIDE, left=Cell[ref=B2], right=Cell[ref=A1]], right=Cell[ref=B1]]
cellules : 3 nombres, 1 texte, 8 formules ; operateurs 4 ; TIMES.apply(6, 7) = 42.0
```
- **`Sheet`** : `COLS = 4`, `ROWS = 3`, et les tableaux `Content[][] cells`, `double[][] values`, `boolean[][] inCycle` (indexés `[col][row]`).
- **`static class Parser`** (imbriquée `static`) : un constructeur `Parser(String text)` et les méthodes `expr`, `term`, `factor` :
  - `+` et `-` sont moins prioritaires que `*` et `/`, et associatifs à gauche ;
  - `factor` lit une parenthèse, `SUM(X:Y)` (avec `startsWith("SUM(", pos)`), une référence (une lettre puis des chiffres) ou un nombre.
- **`set(String "B3=...")`** :
  - `'Total` → `Text` ;
  - un premier caractère chiffre → `Number` ;
  - sinon → `Formula`, analysée par `new Parser(raw).expr()`.
- **Dans `main`** :
  1. remplis la feuille avec `Data.CELLS` ;
  2. affiche l'arbre de `new Sheet.Parser("B2/A1+B1").expr()` (le `toString` des records imbriqués) ;
  3. compte les cellules par type (en relisant `Data.CELLS`) ;
  4. affiche `Op.values().length`, `Op.of('*')` et `Op.TIMES.apply(6, 7)`.

### ☐ Étape 3 — Recalculer dans le bon ordre

```
ordre de calcul : B1 B2 B3 C3 C2 D3
   A        B        C        D
1  10       20       Total    #CYCLE
2  20       60       -2       #CYCLE
3  30       26       110      -2
```
- **`class Evaluator`** (interne) : `double eval(Expr e)`, une chaîne d'`instanceof`.
  - Une référence hors de la feuille (comme `E9`) vaut 0.
  - Les valeurs se lisent dans `Sheet.this.values`.
- **`recalculate()`** déclare une **classe locale** `Graph` :
  - `n = COLS * ROWS` ;
  - `edge[a][b]` signifie que b dépend de a ; avec `inDegree[]` ;
  - `id(ref) = col * ROWS + row` ;
  - `add(from, to)` ignore les références hors feuille et les doublons.
- **Kahn :**
  1. mets dans une file tous les nœuds de degré entrant nul, par id croissant ;
  2. défile un nœud et calcule sa valeur (nombre, formule évaluée ou 0) ;
  3. décrémente ses successeurs, et enfile ceux qui tombent à 0 (par id croissant).
  - Les nœuds jamais atteints sont **dans un cycle**, ou dépendent d'un cycle.
- **`formulaOrder()`** liste les **formules**, dans l'ordre de calcul.
- **`shown(ref)`** :
  - `#CYCLE` si la cellule est dans un cycle ;
  - le texte pour un `Text` ; `""` pour une case vide ;
  - `#DIV0` pour `NaN` ;
  - sinon la valeur : entière si elle l'est, sinon arrondie à 2 décimales.
- **`visit(CellVisitor)`** parcourt ligne par ligne. Dans `SheetApp.print`, une **classe anonyme** `new Sheet.CellVisitor() { … }` remplit un `StringBuilder` par ligne. Chaque colonne fait 9 caractères, et les espaces finaux sont retirés.
- L'en-tête est `   A        B…`, sans espaces finaux.

### ☐ Étape 4 — Modifier et recalculer

```
apres A1=5 :
...
3  30       21       90       -4.5
```
- `sheet.set(Data.CHANGE)`, puis `recalculate()`, puis affiche `apres A1=5 :` et la grille.
- **Expériences :**
  - ajoute `record Mod(…) implements Expr` **dans un autre fichier** : pourquoi est-ce refusé, alors que `permits` est absent ?
  - `new Sheet.Evaluator()` depuis `SheetApp` : quelle erreur ? Et `sheet.new Evaluator()` ?

---

## Checklist (vérifiée par `Check`)

- `Data.CELLS` et `Data.CHANGE` ;
- `record Ref(` et `enum Op` avec `public abstract double apply(` ;
- `sealed interface Expr`, `record Binary(`, `sealed interface Content permits` ;
- `static class Parser`, `class Evaluator`, `Sheet.this.values`, `class Graph` ;
- `new Sheet.CellVisitor()` et `new Sheet.Parser(`.

---

## Sortie attendue complète

```
arbre de B3 : Binary[op=PLUS, left=Binary[op=DIVIDE, left=Cell[ref=B2], right=Cell[ref=A1]], right=Cell[ref=B1]]
cellules : 3 nombres, 1 texte, 8 formules ; operateurs 4 ; TIMES.apply(6, 7) = 42.0
ordre de calcul : B1 B2 B3 C3 C2 D3
   A        B        C        D
1  10       20       Total    #CYCLE
2  20       60       -2       #CYCLE
3  30       26       110      -2
apres A1=5 :
   A        B        C        D
1  5        10       Total    #CYCLE
2  20       55       -4.5     #CYCLE
3  30       21       90       -4.5
```
