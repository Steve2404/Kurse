# Projet 6 — Le calcul formel

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 6) :**
- une hiérarchie **abstraite sur 3 niveaux** : `Expr` → `BinaryOp` (abstraite **intermédiaire**, qui implémente une partie des méthodes abstraites) → `Add`, `Sub`, `Mul`, `Div` ;
- une méthode par défaut (`simplify()`) que certaines sous-classes redéfinissent ;
- **`toString()` redéfini et `final`** ;
- des champs `protected final` et les constructeurs chaînés ;
- une classe avec un constructeur et de l'état d'instance : le `Parser`.

Côté algorithmes :
- un **analyseur récursif** (une méthode par niveau de priorité) ;
- l'évaluation **récursive** d'un arbre ;
- la **dérivée symbolique** ;
- la **simplification** ;
- la **méthode de Newton**.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch6_classdesign.projects.p06_algebra` :
- `Expr` (abstraite), `Num`, `Var`, `BinaryOp` (abstraite) ;
- `Add`, `Sub`, `Mul`, `Div`, `Pow` ;
- `Parser` ;
- **`Algebra`** (le `main`).

**Règle du crescendo :** chapitres 1 à 6.
- Pas d'interface ni de `record`.
- Pas de cast d'objet : le pattern `e instanceof Num n` suffit.

---

## Tableau de bord

### ☐ Étape 1 — L'arbre

- **`Expr`** (`abstract`) :
  - `public abstract double eval(double x)`, `public abstract Expr derive()`, `public abstract String show()`, `public abstract int size()` ;
  - `public Expr simplify()`, qui rend `this` par défaut ;
  - **`public final String toString()`**, qui rend `show()` ;
  - `protected static boolean is(Expr e, double v)` : vrai si e est un `Num` qui vaut v.
- **`Num(double)`** :
  - `derive()` rend `Num(0)` ;
  - `show()` écrit `3`, et non `3.0`, quand la valeur est entière (`value == Math.rint(value)`).
- **`Var`** (la variable x) : `derive()` rend `Num(1)`.
- **`BinaryOp`** (`abstract`, `extends Expr`) :
  - `protected final Expr left, right` et un constructeur `protected` ;
  - `protected abstract char symbol()` ;
  - elle **implémente** `show()` (`(gauche op droite)`) et `size()` (1 + les deux côtés).
- **`Add`, `Sub`, `Mul`, `Div` `extends BinaryOp`** : `eval`, `derive` et `simplify`.
  - Dérivées : (u·v)′ = u′v + uv′ et (u/v)′ = (u′v − uv′) / v².
- **`Pow(Expr base, int exponent)`** `extends Expr` : ce n'est pas un `BinaryOp`, car l'exposant est un `int`.
  - Sa dérivée est (uⁿ)′ = n · uⁿ⁻¹ · u′, construite comme `Mul(Mul(Num(n), Pow(u, n-1)), u')`.
  - `show()` = `(base ^ n)`.

### ☐ Étape 2 — L'analyseur

- **`Parser`** :
  - un champ `private final String text` (sans les espaces) et un `private int pos` ;
  - le constructeur `public Parser(String text)` ;
  - `public static Expr parse(String)`, qui fait `new Parser(text).expr()`.
- **Une méthode par niveau de priorité :**
  - `expr` lit des `term` séparés par `+` ou `-`, associatifs à **gauche** ;
  - `term` lit des `factor` séparés par `*` ou `/` ;
  - `factor` = `atom`, éventuellement suivi de `^` et d'un entier ;
  - `atom` = un nombre, `x`, ou `( expr )`.
- **Question :** pourquoi `2-3-4` doit-il donner `((2 - 3) - 4)` et non `(2 - (3 - 4))` ?

### ☐ Étape 3 — Dériver et simplifier

```
f = (((3 * (x ^ 2)) + (2 * x)) - 5) | simplifiee (((3 * (x ^ 2)) + (2 * x)) - 5) | f(2.0) = 11.0
  f' = ((3 * (2 * x)) + 2) | f'(2.0) = 14.0 | noeuds 23 -> 7
```
- **Les règles de `simplify()`** (simplifier d'abord les enfants, puis appliquer) :
  - `Num op Num` → un `Num` (sauf pour `Div`) ;
  - `0 + e`, `e + 0` et `e - 0` → `e` ;
  - `0 * e` et `e * 0` → `0` ; `1 * e` et `e * 1` → `e` ;
  - `0 / e` → `0` ; `e / 1` → `e` ;
  - `e ^ 0` → `1` ; `e ^ 1` → `e` ; `Num ^ n` → un `Num`.
- **Pour chaque expression** de `Data.EXPRESSIONS`, deux lignes :
  1. `f = …`, puis `| simplifiee …`, puis `| f(2.0) = …` ;
  2. deux espaces, `f' = ` suivi de la dérivée **simplifiée**, sa valeur en `Data.X`, puis `noeuds <taille de la dérivée brute> -> <taille simplifiée>`.
- Les valeurs sont arrondies à 4 décimales : `Math.round(v * 10_000) / 10_000.0`.

### ☐ Étape 4 — Newton et polymorphisme

```
newton (((x ^ 3) - (2 * x)) - 5) : 2.1 2.0946 2.0946 2.0946 2.0946 2.0946 ; f(x) = 0.0
polymorphisme : Add (1 + (x * 1)) -> (1 + x) (Add)
```
- **Newton :** x ← x − f(x)/f′(x), avec 6 itérations depuis `Data.START`, sur `Data.NEWTON`. f′ est la dérivée simplifiée. Affiche chaque x arrondi, puis f(x) final.
- **La dernière ligne :** construis à la main `new Add(new Num(1), new Mul(new Var(), new Num(1)))`. Affiche :
  - son nom de classe, puis l'expression ;
  - `->` et l'expression simplifiée ;
  - le nom de classe du résultat, entre parenthèses.
- **Expériences :**
  - retire `simplify()` de `Sub` : quelle version s'exécute ?
  - essaie de redéfinir `toString()` dans `Num` ;
  - `new BinaryOp(a, b)` : quelle erreur ?

---

## Checklist (vérifiée par `Check`)

- `Data.EXPRESSIONS`, `Data.NEWTON` et `Data.START` ;
- `abstract class Expr` et `abstract class BinaryOp extends Expr` ;
- 4 `extends BinaryOp` ;
- `public abstract Expr derive()`, `public final String toString()`, `protected abstract char symbol()`, `public Expr simplify()` ;
- `new Parser(` et `private final String text`.

---

## Sortie attendue complète

```
f = (((3 * (x ^ 2)) + (2 * x)) - 5) | simplifiee (((3 * (x ^ 2)) + (2 * x)) - 5) | f(2.0) = 11.0
  f' = ((3 * (2 * x)) + 2) | f'(2.0) = 14.0 | noeuds 23 -> 7
f = ((x + 1) * (x - 1)) | simplifiee ((x + 1) * (x - 1)) | f(2.0) = 3.0
  f' = ((x - 1) + (x + 1)) | f'(2.0) = 4.0 | noeuds 15 -> 7
f = (((x ^ 3) - (2 * x)) - 5) | simplifiee (((x ^ 3) - (2 * x)) - 5) | f(2.0) = -1.0
  f' = ((3 * (x ^ 2)) - 2) | f'(2.0) = 10.0 | noeuds 16 -> 6
f = (1 / x) | simplifiee (1 / x) | f(2.0) = 0.5
  f' = (-1 / (x ^ 2)) | f'(2.0) = -0.25 | noeuds 10 -> 4
f = ((2 * (x + 0)) * 1) | simplifiee (2 * x) | f(2.0) = 4.0
  f' = 2 | f'(2.0) = 2.0 | noeuds 21 -> 1
newton (((x ^ 3) - (2 * x)) - 5) : 2.1 2.0946 2.0946 2.0946 2.0946 2.0946 ; f(x) = 0.0
polymorphisme : Add (1 + (x * 1)) -> (1 + x) (Add)
```
