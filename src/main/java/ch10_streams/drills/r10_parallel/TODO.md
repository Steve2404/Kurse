# Drill de rappel 10 — Streams parallèles

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall10`**.
- Utilise `Data.WORDS` et `Data.NUMBERS`.
- Écris une ligne `Dxx : ` par défi.

## Défis

- ☐ **D01.** `isParallel()` pour quatre streams :
  - `stream()` ;
  - `parallelStream()` ;
  - `stream().parallel()` ;
  - `stream().parallel().map(...).sequential()`.
  → `D01 : false true true false`
- ☐ **D02.** Affiche `NUMBERS` dans l'ordre **depuis un flux parallèle**, séparés par des espaces.
  → `D02 : 5 3 8 1 9 2 8 7`
- ☐ **D03.** Réduis 1..100 par **soustraction** (identité 0), en séquentiel puis en parallèle. Affiche le séquentiel, puis si le parallèle est différent. Explique pourquoi en commentaire.
  → `D03 : sequentiel -5050, parallele different : true`
- ☐ **D04.** Sur un flux parallèle des mots de 4 lettres :
  - le **premier** (déterministe) ;
  - le fait qu'**un** tel mot existe.
  → `D04 : java true`
- ☐ **D05.** Le nombre de mots distincts, en parallèle, en **renonçant à l'ordre**.
  → `D05 : 7`
- ☐ **D06.** La liste des longueurs en séquentiel est-elle égale à la même liste collectée en parallèle ?
  → `D06 : true`
- ☐ **D07.** Le nombre de mots par longueur, en parallèle, dans une `Map` **concurrente**. Affiche les comptes des longueurs 6 et 4.
  → `D07 : 4 2`
- ☐ **D08.** Le total des lettres avec un `reduce` à 3 arguments correct. Affiche-le, puis indique si le résultat parallèle est identique.
  → `D08 : 52 true`
- ☐ **D09.** Le nombre d'occurrences de `java`, avec le collecteur qui produit **directement** une `Map` concurrente (clé, valeur, fusion).
  → `D09 : 2`

## Sortie attendue complète

```
D01 : false true true false
D02 : 5 3 8 1 9 2 8 7
D03 : sequentiel -5050, parallele different : true
D04 : java true
D05 : 7
D06 : true
D07 : 4 2
D08 : 52 true
D09 : 2
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Mettre un stream en parallèle :**
- `collection.parallelStream()` ou `stream.parallel()` ;
- `stream.sequential()` ramène en séquentiel ;
- `isParallel()` dit ce qu'il en est ;
- le **dernier** appel s'applique à **tout** le pipeline.

**Ordre :**
- `forEach` ne garantit aucun ordre en parallèle ;
- `forEachOrdered` respecte l'ordre de rencontre ;
- `findAny` peut rendre n'importe quel élément ; `findFirst` reste déterministe (mais il est plus coûteux) ;
- `unordered()` lève la contrainte d'ordre : `distinct`, `limit`… deviennent moins coûteux.

**Réductions :**
- `reduce` et `collect` en parallèle exigent une identité neutre, des opérations **associatives** et **sans état**, et un combiner cohérent ;
- `groupingByConcurrent` et `toConcurrentMap` utilisent **une** `ConcurrentMap` partagée, sans fusion de maps partielles. Ils sont idéaux sur un flux parallèle **non ordonné**.

**À éviter :** les effets de bord (ajouter à une `ArrayList` partagée dans `forEach`). En parallèle, c'est une **course aux données**.

</details>
