# Drill de rappel 1 — Opérateurs unaires et incrémentations

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall01`**.
- **Calcule chaque ligne à la main AVANT d'exécuter.**

## Défis

- ☐ **D01.** Avec `a = 5`, affiche dans **un seul** `println` : `a++`, `a`, `++a`, `a`.
  → `D01 : 5 6 7 7`
- ☐ **D02.** Avec `b = 10` : `b--`, `--b`, `b`.
  → `D02 : 10 8 8`
- ☐ **D03.** Avec `c = 3`, calcule `d = c++ * 2 + c`, puis affiche `d` et `c`.
  → `D03 : 10 4`
- ☐ **D04.** Avec `e = 1`, écris exactement `e = e++;`, puis affiche `e`.
  → `D04 : 1`
- ☐ **D05.** Avec `f = 4` : `-f`, `-(-f)`, `~f`, `~-1`.
  → `D05 : -4 4 -5 0`
- ☐ **D06.** Avec `g = true` : `!g`, `!!g`, puis la négation de `f > 3`.
  → `D06 : false true false`
- ☐ **D07.** Un `char` `'a'`, incrémenté avec `++` : affiche-le, puis son code (cast), puis `++` encore une fois **dans** le `println`.
  → `D07 : b 98 c`
- ☐ **D08.** Un `long` 2, incrémenté ; un `double` 1.5, décrémenté.
  → `D08 : 3 0.5`
- ☐ **D09.** Un `byte` 7 rangé dans un `int` via le `+` **unaire**. Puis, avec `letter = 'A'` : `+letter`, `-letter`, `+(-3)`.
  → `D09 : 7 65 -65 -3`

## Expériences (hors sortie attendue)

1. `int x = 5; x++++;` : lis l'erreur de `javac`. Pourquoi `x++` n'est-il pas une variable ?
2. `boolean b = !5;` puis `int i = !true;` : lis les deux erreurs.
3. `final int k = 1; k++;` : que dit `javac` ?
4. `byte b = 5; byte c = +b;` : pourquoi ce `+` « qui ne fait rien » empêche-t-il la compilation ?

## Sortie attendue complète

```
D01 : 5 6 7 7
D02 : 10 8 8
D03 : 10 4
D04 : 1
D05 : -4 4 -5 0
D06 : false true false
D07 : b 98 c
D08 : 3 0.5
D09 : 7 65 -65 -3
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Forme | Effet sur la variable | Valeur **rendue** |
|---|---|---|
| `++x` | `x + 1` | la **nouvelle** valeur |
| `x++` | `x + 1` | l'**ancienne** valeur |
| `--x` / `x--` | `x - 1` | nouvelle / ancienne |

**Les incréments :**
- dans une expression, chaque `x++` modifie `x` **immédiatement**, et la suite de l'expression voit la nouvelle valeur ;
- **le piège `x = x++` :** l'ancienne valeur est rendue, `x` est incrémenté, **puis** l'affectation remet l'ancienne valeur ;
- `++` et `--` marchent sur tous les types numériques, y compris `char`, avec un cast implicite. Ils ne s'appliquent qu'à une **variable**.

**Les autres unaires :**
- `-x` donne l'opposé ; `+x` ne change rien (mais promeut `byte`, `short` et `char` en `int`) ;
- `~x` est le complément bit à bit, et vaut `-x - 1` ;
- `!` ne s'applique qu'à un `boolean`, et un `boolean` n'est **jamais** un nombre en Java.

</details>
