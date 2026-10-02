# Drill de rappel 7 — Kata mixte chronométré (tout le chapitre 3)

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min, **sans carte**. C'est le test final de chaque cycle.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`**.
- `Check` lance ton `main` avec `30 un 7 deux 12`. Le 1er argument est la limite.

## Défis

- ☐ **D01.** La somme des entiers de 1 à la limite qui sont multiples de 3 **ou** de 5.
  → `D01 : 225`
- ☐ **D02.** Le PGCD de 48 et 180 (Euclide, avec `while`).
  → `D02 : 12`
- ☐ **D03.** Pour chaque argument (for-each) :
  1. un `switch` expression le transforme en `Object` : `un` et `deux` restent du texte, le reste devient un nombre ;
  2. une méthode classe ensuite cet `Object` : `pair` (pattern + `&&`), `impair` ou `texte`.
  → `D03 : pair texte impair texte pair`
- ☐ **D04.** Le nombre de nombres premiers jusqu'à la limite (étiquette et `continue outer`).
  → `D04 : 10`
- ☐ **D05.** Le nombre d'étapes de Collatz depuis 27.
  → `D05 : 111`
- ☐ **D06.** Les chiffres romains de 1 à 4 : un `switch` expression dans une boucle, avec un `default` en bloc et `yield`.
  → `D06 : I II III IV`
- ☐ **D07.** Le nombre de chiffres de la limite (`do/while`).
  → `D07 : 2`
- ☐ **D08.** Un escalier de 3 marches : 1, 2 puis 3 dièses, chaque marche suivie de `|` (boucles imbriquées).
  → `D08 : #|##|###|`

## Sortie attendue complète

```
D01 : 225
D02 : 12
D03 : pair texte impair texte pair
D04 : 10
D05 : 111
D06 : I II III IV
D07 : 2
D08 : #|##|###|
```

## Après le kata

- **Pour chaque défi raté ou trop lent :** refais le drill de son thème le lendemain.
  - r01 : `if` et patterns ;
  - r02 et r03 : `switch` ;
  - r04 et r05 : boucles ;
  - r06 : sauts.
- **Note ton temps** dans `drills/README.md`.
