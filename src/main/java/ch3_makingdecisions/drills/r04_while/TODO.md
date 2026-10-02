# Drill de rappel 4 — `while` et `do/while`

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`**.
- **Compte les tours à la main** avant d'exécuter.

## Défis

- ☐ **D01.** Avec `n = 5` : tant que `n > 0`, ajoute `n` à `sum`, puis décrémente. Affiche `sum`, puis `n`.
  → `D01 : 15 0`
- ☐ **D02.** Un `while` dont la condition est fausse **dès le départ** (compteur `never`), et un `do/while` dont la condition est fausse (compteur `once`). Affiche les deux compteurs.
  → `D02 : 0 1`
- ☐ **D03.** Pars de 1 et double tant que la valeur est < 1000. Affiche la valeur, puis le nombre de doublements.
  → `D03 : 1024 10`
- ☐ **D04.** Avec `x = 10`, et `sum` qui vaut encore 15 (D01) : `while (x-- > 7) { sum = sum + x; }`. Affiche `x`, puis `sum`.
  → `D04 : 6 39`
- ☐ **D05.** Avec un `do/while` sur 9045 : le nombre de chiffres et leur somme.
  → `D05 : 4 18`
- ☐ **D06.** La même boucle de comptage de chiffres, sur **0**. Combien de chiffres ? (C'est pour ce cas que le `do/while` est le bon choix.)
  → `D06 : 1`
- ☐ **D07.** Un capital de 1000 placé à 10 % par an (intérêts en division entière). Combien d'années pour atteindre au moins 2000, et quel capital final ?
  → `D07 : 8 2142`
- ☐ **D08.** Deux `while` imbriqués : la boucle extérieure va de 1 à 3 (variable `a`), et la boucle intérieure tourne `a` fois en incrémentant `b`. Affiche `a`, puis `b`.
  → `D08 : 3 6`

## Expériences (hors sortie attendue)

1. `while (false) { x++; }` : que dit `javac` ? Et `do { x++; } while (false);` ?
2. `while (true) { } System.out.println("fin");` : pourquoi la ligne suivante ne compile-t-elle pas ?
3. Oublie le point-virgule après `} while (cond)` : quelle erreur ?

## Sortie attendue complète

```
D01 : 15 0
D02 : 0 1
D03 : 1024 10
D04 : 6 39
D05 : 4 18
D06 : 1
D07 : 8 2142
D08 : 3 6
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Boucle | Condition testée | Nombre minimal de tours |
|---|---|---|
| `while (c) { … }` | **avant** chaque tour | 0 |
| `do { … } while (c);` | **après** chaque tour (attention au `;` final) | **1** |

**Les règles :**
- la condition **doit** être un `boolean` ;
- `while (x-- > 7)` : la comparaison utilise l'**ancienne** valeur, et la décrémentation a lieu même au tour qui sort.

**Le code inaccessible :**
- `while (false) { … }` : le corps est inaccessible, c'est une **erreur de compilation** ;
- `do { … } while (false);` est légal ;
- après `while (true) { … }` **sans `break`**, le code qui suit est inaccessible : erreur aussi.

**Le choix :** `do/while` quand le corps doit s'exécuter au moins une fois (une saisie, le comptage des chiffres d'un nombre qui peut valoir 0).

</details>
