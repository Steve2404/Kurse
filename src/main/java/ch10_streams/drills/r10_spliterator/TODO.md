# Drill de rappel 10 — `Spliterator`

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall10`** et **ta propre classe qui implémente `Spliterator<String>`**.
- Utilise `Data.WORDS` (9 mots).
- Écris une ligne `Dxx : ` par défi.

## Défis

- ☐ **D01.** Sur le spliterator de la liste `WORDS`, affiche :
  - la taille estimée ;
  - la taille exacte ;
  - s'il est `ORDERED` ;
  - s'il est `SIZED`.
  → `D01 : 9 9 true true`
- ☐ **D02.** Coupe-le **une** fois. Affiche la taille de la partie **rendue**, puis celle de la partie **gardée**.
  → `D02 : 4 5`
- ☐ **D03.** Sur la partie rendue :
  - lis **un** élément ;
  - lis **tout le reste** dans une liste ;
  - tente une dernière lecture.
  
  Affiche les trois résultats.
  → `D03 : stream [lambda, optional, java] false`
- ☐ **D04.** Écris ton spliterator « paires ». Il rend les mots deux par deux, collés par `+` ; le dernier peut être seul. Il ne sait **pas** se couper. Il est ordonné, de taille connue et sans `null`.
  Affiche sa taille estimée, puis la liste obtenue en le transformant en stream séquentiel.
  → `D04 : 5 [stream+lambda, optional+java, stream+collector, map+java, filter]`
- ☐ **D05.** Transforme un spliterator « paires » neuf en stream et compte-le. Puis vérifie qu'un autre spliterator « paires » refuse de se couper.
  → `D05 : 5 true`
- ☐ **D06.** Le spliterator d'un `Stream.iterate` infini : affiche sa taille exacte, puis s'il est `SIZED`.
  → `D06 : -1 false`

## Sortie attendue complète

```
D01 : 9 9 true true
D02 : 4 5
D03 : stream [lambda, optional, java] false
D04 : 5 [stream+lambda, optional+java, stream+collector, map+java, filter]
D05 : 5 true
D06 : -1 false
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Méthode | Rôle |
|---|---|
| `boolean tryAdvance(Consumer)` | traite **au plus un** élément ; `false` quand c'est fini |
| `void forEachRemaining(Consumer)` | traite tout le reste |
| `Spliterator<T> trySplit()` | rend un **préfixe** et garde le reste ; `null` s'il refuse de se couper |
| `long estimateSize()` | une estimation (`Long.MAX_VALUE` si inconnue) |
| `long getExactSizeIfKnown()` | la taille si `SIZED`, sinon **-1** |
| `int characteristics()` | une combinaison de bits (`ORDERED`, `DISTINCT`, `SORTED`, `SIZED`, `NONNULL`, `IMMUTABLE`, `CONCURRENT`, `SUBSIZED`) |
| `boolean hasCharacteristics(int)` | |
| `StreamSupport.stream(spliterator, false)` | `Stream<T>` (le `true`, parallèle, est au chapitre 13) |

**Ce qu'il faut retenir :**
- `ArrayList` est `ORDERED | SIZED | SUBSIZED` et coupe au milieu : la moitié basse est rendue.
- Un spliterator, comme un stream, est **à usage unique**.

</details>
