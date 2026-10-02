# Drill de rappel 6 — Autoboxing et unboxing

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall06`** dans le paquet `ch5_methods.drills.r06_boxing`.

## Défis

- ☐ **D01.** `Integer a = 127, b = 127, c = 1000, d = 1000;`. Affiche :
  - `a == b` ;
  - `c == d` ;
  - `c.equals(d)` ;
  - `c.intValue() == d`.
  → `D01 : true false true true`
- ☐ **D02.** Cinq variables : `Integer boxed = 5`, `int unboxed = boxed`, `Double ratio = 2.5`, `Character letter = 'x'`, `Boolean flag = true`. Affiche :
  - `boxed + unboxed` ;
  - `ratio * 2` ;
  - `letter` ;
  - `!flag`.
  → `D02 : 10 5.0 x false`
- ☐ **D03.** Trois méthodes :
  - `int twice(Integer n)` (unboxing) ;
  - `Integer half(int n)` (boxing au retour) ;
  - `boolean isNull(Integer n)`.
  
  Appelle `twice(21)`, `half(9)`, `isNull(null)` et `isNull(0)`.
  → `D03 : 42 4 true false`
- ☐ **D04.** `Long big = 5L;`. Affiche :
  - `big.equals(5)` ;
  - `big.equals(5L)` ;
  - `big == 5` ;
  - `Integer.valueOf(5).equals(5)`.
  → `D04 : false true true true`
- ☐ **D05.** Les conversions :
  - `Integer.parseInt("12")` et `Integer.valueOf("12")` ;
  - `Double.parseDouble("1.5")` ;
  - `Boolean.parseBoolean("TRUE")` et `Boolean.parseBoolean("oui")`.
  → `D05 : 12 12 1.5 true false`
- ☐ **D06.** `Integer counter = 10;`, puis `counter++;`, puis `Integer same = counter;`, puis `counter += 5;`. Affiche `counter` et `same`.
  → `D06 : 16 11`
- ☐ **D07.** `Integer[] scores = {3, null, 7}` : la somme des non-nuls, puis le nombre de `null`.
  → `D07 : 10 1`
- ☐ **D08.** Les utilitaires des enveloppes :
  - `Integer.MAX_VALUE` et `Integer.MIN_VALUE` ;
  - `Integer.compare(3, 7)` ;
  - `Character.getNumericValue('8')` ;
  - `Integer.toBinaryString(10)`.
  → `D08 : 2147483647 -2147483648 -1 8 1010`

## Expériences (hors sortie attendue)

1. `Long l = 5;`, `Double d = 5;` et `long x = Integer.valueOf(5);` : lesquelles compilent ?
2. `Integer n = null; int m = n;` : à quel moment ça échoue ?
3. `Integer.parseInt("1.5")` et `Integer.parseInt("")` : quelle exception ?
4. `Character c = 65;` compile-t-il ? Et `Character c = 'A' + 1;` ?

## Sortie attendue complète

```
D01 : true false true true
D02 : 10 5.0 x false
D03 : 42 4 true false
D04 : false true true true
D05 : 12 12 1.5 true false
D06 : 16 11
D07 : 10 1
D08 : 2147483647 -2147483648 -1 8 1010
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les conversions automatiques :**
- **boxing** : primitif → son enveloppe **exacte** (`int` → `Integer`, jamais `int` → `Long`) ;
- **unboxing** : enveloppe → primitif. Sur `null`, c'est une `NullPointerException`.

**L'égalité :**
- `==` entre deux enveloppes compare les **références**. Grâce au cache (−128 à 127), deux `Integer` égaux de cette plage sont le même objet ;
- `==` entre une enveloppe et un primitif fait un unboxing, donc une comparaison numérique ;
- `equals` compare le contenu **et le type** : `Long.equals(Integer)` vaut toujours `false`.

**Les conversions de texte :**
- `parseXxx(String)` rend un **primitif** ; `valueOf(String)` rend une **enveloppe** ;
- `Boolean.parseBoolean` vaut `true` seulement pour « true », sans tenir compte de la casse.

**L'immuabilité :** `counter++` sur un `Integer` crée un nouvel objet ; les autres références gardent l'ancien.

</details>
