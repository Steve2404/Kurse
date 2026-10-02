# Drill de rappel 3 — `StringBuilder`

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall03`** dans le paquet `ch4_coreapis.drills.r03_builder`.
- Les **trois** constructeurs sont exigés : `()`, `("texte")` et `(capacité)`.

## Défis

- ☐ **D01.** Un `StringBuilder` vide auquel tu chaînes cinq `append` : `"ab"`, `1`, `'c'`, `true`, `2.5`. Affiche le contenu puis sa longueur.
  → `D01 : ab1ctrue2.5 11`
- ☐ **D02.** Sur `"animals"`, chaîne trois `insert` :
  1. `"-"` à l'indice 7 ;
  2. puis `"-"` à l'indice 0 ;
  3. puis `"+"` à l'indice 4.
  
  Attention : chaque `insert` décale les indices des suivants.
  → `D02 : -ani+mals-`
- ☐ **D03.** Sur `"abcdef"`, chaîne `delete(1, 3)` puis `deleteCharAt(0)`.
  → `D03 : def`
- ☐ **D04.** Trois résultats :
  - `delete(4, 100)` sur `"abcdef"` ;
  - `replace(3, 6, "sty")` sur `"pigeon dirty"` ;
  - `replace(2, 100, "X")` sur `"12345"`.
  → `D04 : abcd pigsty dirty 12X`
- ☐ **D05.** Sur `"stressed"` :
  1. garde `substring(0, 3)` dans un `String` ;
  2. inverse le builder ;
  3. affiche le builder, le `String` gardé, `indexOf("s")` et `charAt(1)`, le tout **après** l'inversion.
  → `D05 : desserts str 2 e`
- ☐ **D06.** `one = new StringBuilder("x")`, puis `two = one.append("y")`, puis `two.append("z")`. Affiche `one` et `one == two`.
  → `D06 : xyz true`
- ☐ **D07.** Trois résultats :
  - la longueur d'un `new StringBuilder(100)` ;
  - `"hello"` réduit à 2 caractères avec `setLength`, entre crochets ;
  - `equals` entre deux `StringBuilder("ab")`.
  → `D07 : 0 [he] false`
- ☐ **D08.** Trois résultats :
  - `"level"` inversé puis converti en `String` ;
  - ce `String` est-il `equals` à `"level"` ?
  - `"level".contentEquals(…)` avec un `StringBuilder("level")`.
  → `D08 : level true true`

## Expériences (hors sortie attendue)

1. `new StringBuilder("abc").insert(5, "x")` : quelle exception ? Et `delete(2, 1)` ?
2. `String s = new StringBuilder("a");` : que dit `javac` ?
3. `sb.substring(1)` modifie-t-il `sb` ? Vérifie en affichant `sb` après.
4. `new StringBuilder('a')` : que contient-il ? (Indice : `'a'` vaut 97…)

## Sortie attendue complète

```
D01 : ab1ctrue2.5 11
D02 : -ani+mals-
D03 : def
D04 : abcd pigsty dirty 12X
D05 : desserts str 2 e
D06 : xyz true
D07 : 0 [he] false
D08 : level true true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Un objet mutable :**
- `append`, `insert`, `delete`, `deleteCharAt`, `replace` et `reverse` modifient l'objet **et** le rendent (`this`), d'où le chaînage ;
- deux variables peuvent donc pointer vers le même builder.

**Les méthodes qui ne modifient pas :**
- `substring`, `indexOf`, `charAt` et `length` ;
- `substring` rend un `String`.

**Les bornes :**
- `delete(a, b)` et `replace(a, b, x)` acceptent `b > length()` : ils s'arrêtent au bout ;
- `insert` et `charAt` hors limites lèvent une exception.

**Les constructeurs :**
- `()` : vide ;
- `("texte")` : avec un contenu ;
- `(int capacité)` : vide, la capacité n'est pas la longueur ;
- `setLength(n)` tronque (ou complète avec `'\0'`).

**L'égalité :**
- `StringBuilder` ne redéfinit **pas** `equals` : c'est une comparaison de références ;
- pour comparer le contenu : `toString().equals(…)` ou `contentEquals`.

</details>
