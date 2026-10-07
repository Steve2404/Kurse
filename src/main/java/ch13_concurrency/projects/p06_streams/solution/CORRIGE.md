# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur une machine à plusieurs cœurs.

---

## Étape 1 — Des calculs parallèles exacts

**Le code :** `ParallelLab.java`. `.parallel()` coupe le flux en morceaux traités par plusieurs threads (ceux du pool commun Fork/Join), puis recombine les résultats.

**Question — pourquoi une somme de `double` peut-elle différer, mais pas une somme de `long` ?** En parallèle, les nombres sont additionnés **dans un autre ordre** (par morceaux). Pour des `long`, l'addition est exacte et **associative** : `(a + b) + c == a + (b + c)`, quel que soit l'ordre. Pour des `double`, chaque addition **arrondit** : l'ordre change le résultat. Vérifié sur la somme de 0.1, 0.2, … (un million de termes) : séquentiel `5.0000049999961174E10`, parallèle `5.0000049999961075E10`, différents.

---

## Étape 2 — `reduce` et `collect`

**L'identité 10 :** en séquentiel, elle est utilisée une fois : 5050 + 10 = 5060. En parallèle, elle est utilisée **une fois par morceau**, donc ajoutée plusieurs fois : le résultat dépend du nombre de morceaux. Une identité doit être **neutre** (0 pour une somme).

**Le `collect` à 3 arguments** reste dans l'ordre, même en parallèle : chaque morceau a sa propre `ArrayList`, et `addAll` les recolle dans l'ordre des morceaux.

---

## Étape 3 — L'ordre

- `forEachOrdered` donne `[1, 2, …, 12]` dans l'ordre ; `forEach` donne les mêmes éléments dans un ordre **quelconque**, d'où la comparaison après tri ;
- `findFirst()` rend 3 (le 1er élément qui convient), même en parallèle ; `findAny()` rend **un** élément qui convient, pas forcément le même à chaque fois ;
- `unordered().limit(10)` donne bien 10 éléments, mais pas forcément les 10 premiers ;
- `isParallel()` : `false`, puis `true` après `.parallel()` ; `.sequential()` le remet à `false`.

---

## Étape 4 — Collecteurs concurrents et tableaux

**Expérience 1 — `(a, b) -> a - b` dans un `reduce` parallèle :** la soustraction n'est **pas associative** (`(5 - 3) - 1 ≠ 5 - (3 - 1)`). Vérifié sur les entiers de 1 à 1000 : le séquentiel donne `-500500`, et le parallèle donne un **autre** résultat (`0`, à chacun des 5 lancements sur cette machine). Le résultat dépend du découpage : il pourrait changer sur une machine avec un autre nombre de cœurs. Un opérateur de `reduce` doit être **associatif**.

**Expérience 2 — une `ArrayList` ordinaire remplie par un `forEach` parallèle :** vérifié, 5 lancements de 100 000 ajouts : 4 fois une `ArrayIndexOutOfBoundsException`, et une fois une liste de **16 854** éléments au lieu de 100 000. Une `ArrayList` n'est pas faite pour être modifiée par plusieurs threads : des ajouts sont perdus, ou son tableau interne est corrompu. Il faut `collect`, une collection concurrente, ou une liste synchronisée.
