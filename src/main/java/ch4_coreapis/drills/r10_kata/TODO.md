# Drill de rappel 10 — Kata mixte chronométré (tout le chapitre 4)

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min, **sans carte**. C'est le test final de chaque cycle.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall10`** dans le paquet `ch4_coreapis.drills.r10_kata`.

## Défis

- ☐ **D01.** Sur `"  le Java est un Langage  "` :
  1. `strip` puis `split(" ")` ;
  2. avec un for-each, construis dans un `StringBuilder` les initiales en majuscules ;
  3. affiche le nombre de mots, les initiales, puis les initiales inversées.
  
  Attention : le builder est **modifié** par `reverse()`, même au milieu de la concaténation.
  → `D01 : 5 LJEUL LUEJL`
- ☐ **D02.** Inverse `"Kayak"` avec un `StringBuilder`. Affiche :
  - le résultat ;
  - le résultat comparé à `"Kayak"` avec `equalsIgnoreCase` ;
  - puis avec `equals`.
  → `D02 : kayaK true false`
- ☐ **D03.** Sur `values = {5, 3, 9, 1, 7}` :
  1. copie le tableau avec `Arrays.copyOf`, puis trie la **copie** ;
  2. affiche la copie triée ;
  3. affiche `binarySearch` de 7, puis de 4 ;
  4. affiche `values[0]`, qui ne doit pas avoir changé.
  → `D03 : [1, 3, 5, 7, 9] 3 -3 5`
- ☐ **D04.** Une table de multiplication 3×3 dans un `int[3][3]` (boucles imbriquées). Affiche-la avec `deepToString`, puis `table[2][1]`.
  → `D04 : [[1, 2, 3], [2, 4, 6], [3, 6, 9]] 6`
- ☐ **D05.** Cinq appels :
  - `round(2.5)` ;
  - `round(-2.5)` ;
  - `ceil(-0.5)` ;
  - `pow(3, 2)` ;
  - `max(1, 2L)`.
  → `D05 : 3 -2 -0.0 9.0 2`
- ☐ **D06.** À partir du 31/01/2026, quatre calculs :
  - `plusMonths(1)` ;
  - `plusMonths(1).plusMonths(1)` ;
  - `plusMonths(2)` ;
  - `Period.between` jusqu'au 01/03/2026.
  → `D06 : 2026-02-28 2026-03-28 2026-03-31 P1M1D`
- ☐ **D07.** Les jours entre le 31/01/2026 et Noël 2026, puis le jour de la semaine de Noël.
  → `D07 : 328 FRIDAY`
- ☐ **D08.** Trois variables :
  - `a = "Java"` ;
  - `b = "Ja" + "va"` ;
  - `c = new String("Java")`.
  
  Affiche `a == b`, `a == c`, `a.equals(c)`, puis `"%s-%03d".formatted(a, 7)`.
  → `D08 : true false true Java-007`

## Sortie attendue complète

```
D01 : 5 LJEUL LUEJL
D02 : kayaK true false
D03 : [1, 3, 5, 7, 9] 3 -3 5
D04 : [[1, 2, 3], [2, 4, 6], [3, 6, 9]] 6
D05 : 3 -2 -0.0 9.0 2
D06 : 2026-02-28 2026-03-28 2026-03-31 P1M1D
D07 : 328 FRIDAY
D08 : true false true Java-007
```

## Après le kata

Pour chaque ✗, refais **le drill thématique** correspondant :

| Défi | Drill |
|---|---|
| D01, D02 | r01, r03 |
| D03, D04 | r05, r06 |
| D05 | r07 |
| D06, D07 | r08 |
| D08 | r02, r04 |
