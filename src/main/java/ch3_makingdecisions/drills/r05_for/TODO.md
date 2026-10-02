# Drill de rappel 5 — `for` et for-each

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall05`**.
- `Check` lance ton `main` avec `4 8 15 16`.

## Défis

- ☐ **D01.** Les entiers de 0 à 4 collés. Puis, de 10 en descendant de 3 tant que > 0, chacun suivi d'une espace.
  → `D01 : 01234 | 10 7 4 1`
- ☐ **D02.** Une **seule** boucle `for` avec **deux** variables : `i` part de 0, `j` de 6, `i++` et `j--` dans la mise à jour. Elle tourne tant que `i < j` et concatène `i` et `j` suivis d'une espace.
  → `D02 : 06 15 24`
- ☐ **D03.** Un `for` **sans initialisation ni mise à jour** (`for (; k < 3; )`), avec `k` déclaré avant et incrémenté dans le corps.
  → `D03 : 3`
- ☐ **D04.** Un **for-each** sur `args` : le nombre d'arguments, puis chacun entre crochets.
  → `D04 : 4 [4][8][15][16]`
- ☐ **D05.** La somme des arguments, avec un for-each.
  → `D05 : 43`
- ☐ **D06.** Les arguments **à l'envers**, avec un `for` à indice décroissant.
  → `D06 : 16 15 8 4`
- ☐ **D07.** 20! dans un `long`, avec `*=`.
  → `D07 : 2432902008176640000`
- ☐ **D08.** Deux `for` imbriqués : `i` va de 0 à 2, et `j` va **de `i`** à 2. Compte les tours.
  → `D08 : 6`

## Expériences (hors sortie attendue)

1. `for (int i = 0, long j = 0; …)` : quelle erreur ?
2. Utilise `i` **après** la boucle `for (int i = 0; …)`. Que dit `javac` ?
3. Dans un for-each sur `args`, modifie la variable de boucle (`arg = "x";`). Est-ce que `args` change ?
4. `for ( ; ; ) { }` suivi d'une instruction : pourquoi l'instruction est-elle inaccessible ?

## Sortie attendue complète

```
D01 : 01234 | 10 7 4 1
D02 : 06 15 24
D03 : 3
D04 : 4 [4][8][15][16]
D05 : 43
D06 : 16 15 8 4
D07 : 2432902008176640000
D08 : 6
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**`for (init ; condition ; mise à jour)` :**
- les trois parties sont **facultatives** ; `for ( ; ; )` est une boucle infinie ;
- **init :** plusieurs variables, mais du **même** type, en une seule déclaration (`int i = 0, j = 6`) ;
- **mise à jour :** plusieurs expressions séparées par des virgules (`i++, j--`) ;
- **la portée :** une variable déclarée dans `init` n'existe que dans la boucle.

**Le for-each `for (Type x : source)` :**
- la source est un **tableau** ou un `Iterable` (une collection, au chapitre 9) ;
- `x` est une **copie** de l'élément : la réaffecter ne change pas la source ;
- il ne donne pas l'indice ; il faut un `for` classique pour l'indice, le sens inverse ou un saut de 2.

</details>
