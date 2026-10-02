# Drill de rappel 2 — Les varargs

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall02`** dans le paquet `ch5_methods.drills.r02_varargs`.
- `int[] data = {4, 5, 6};` est déclaré au début de `main`.

## Défis

- ☐ **D01.** `int count(int... v)` rend la longueur, ou −1 si `v` est `null`. Appelle-la :
  - sans argument ;
  - avec `7` ;
  - avec `1, 2, 3` ;
  - avec `data` ;
  - avec `(int[]) null`.
  → `D01 : 0 1 3 3 -1`
- ☐ **D02.** `String label(String prefix, int... v)` : le préfixe suivi des nombres. Appelle-la avec `"x"`, avec `"x", 1`, puis avec `"y", 1, 2, 3`.
  → `D02 : x x1 y123`
- ☐ **D03.** Deux méthodes :
  - `first(int... v)`, qui rend 0 si `v` est vide ; appelle-la sans argument, puis avec `8, 9` ;
  - `last(int... v)` ; appelle-la avec `data`, puis avec `5`.
  → `D03 : 0 8 6 5`
- ☐ **D04.** `String kinds(Object... items)` rend la longueur, ou `null`. Appelle-la :
  - sans argument ;
  - avec `"a", 1` ;
  - avec `(Object) null` ;
  - avec `(Object[]) null` ;
  - avec `(Object) new String[] {"a", "b"}` ;
  - avec `(Object[]) new String[] {"a", "b"}`.
  → `D04 : 0 2 1 null 1 2`
- ☐ **D05.** `int rows(int[]... grid)`. Appelle-la sans argument, avec deux tableaux, puis avec un `int[][]` de 3 lignes.
  → `D05 : 0 2 3`
- ☐ **D06.** `void change(int... v)` fait `v[0] = 99`.
  - appelle-la avec `data` ;
  - puis avec `other[0], other[1]`, où `other = {1, 2}`.
  
  Affiche `data[0]` et `other[0]`.
  → `D06 : 99 1`
- ☐ **D07.** `String joined(char sep, String... words)`, avec `String.join`. Appelle-la avec trois mots, puis sans mot (entre crochets).
  → `D07 : a-b-c []`
- ☐ **D08.** `count(new int[0])`, `count(new int[] {})`, puis `label("z", new int[] {7, 8})`.
  → `D08 : 0 0 z78`

## Expériences (hors sortie attendue)

1. `static void f(int... a, String s)` et `static void g(int... a, int... b)` : quelles erreurs ?
2. `kinds(null)` sans cast : quel **avertissement** ? Et `count(null)` ?
3. `static void h(int[] a)` à côté de `static void h(int... a)` : pourquoi refusé ?
4. Appelle `h(1, 2)` quand seul `h(int[] a)` existe : ça compile ?
5. `public static void main(String... args)` : est-ce un `main` valide ?

## Sortie attendue complète

```
D01 : 0 1 3 3 -1
D02 : x x1 y123
D03 : 0 8 6 5
D04 : 0 2 1 null 1 2
D05 : 0 2 3
D06 : 99 1
D07 : a-b-c []
D08 : 0 0 z78
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**La déclaration :**
- `Type... nom` : un seul varargs, et c'est le **dernier** paramètre ;
- dans la méthode, c'est un **tableau** `Type[]`.

**Les quatre façons d'appeler :**

| Appel | Ce que reçoit la méthode |
|---|---|
| `f()` | un tableau **vide** (longueur 0, jamais `null`) |
| `f(1, 2)` | un nouveau tableau `{1, 2}` |
| `f(arr)` | **le** tableau `arr` lui-même : les modifications se voient chez l'appelant |
| `f((int[]) null)` | `null` |

**`Object...` :**
- `(Object) null` : un tableau d'**un** élément `null` ;
- `(Object[]) null` : le tableau est `null` ;
- un `String[]` passé seul est pris comme le tableau lui-même (un `String[]` est un `Object[]`), sauf avec le cast `(Object)`.

**La surcharge :**
- `f(int[])` et `f(int...)` ont la même signature : c'est interdit ;
- une version varargs n'est choisie qu'en **dernier** recours (phase 3).

</details>
