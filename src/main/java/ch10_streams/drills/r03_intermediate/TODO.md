# Drill de rappel 3 — Opérations intermédiaires et `Comparator`

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall03`** et ton record pour `Data.BOOKS`.
- Écris une ligne `Dxx : ` par défi.

## Défis

- ☐ **D01.** Les titres des livres de genre SF parus après 1960.
  → `D01 : [Dune, Hyperion, Neuromancien]`
- ☐ **D02.** Les mots de `WORDS`, sans doublon, dans l'ordre naturel.
  → `D02 : [collector, filter, java, lambda, map, optional, stream]`
- ☐ **D03.** Les 3 premiers mots distincts dans l'ordre **inverse**.
  → `D03 : [stream, optional, map]`
- ☐ **D04.** Les 2 titres qui ont le plus de pages. Le comparateur est construit sur un `int`, puis inversé.
  → `D04 : [Germinal, L'Assommoir]`
- ☐ **D05.** Les livres triés par auteur croissant puis, pour un même auteur, par année **décroissante**. N'inverse **que** le second critère. Affiche `auteur:annee`, séparés par `, `.
  → `D05 : Asimov:1951, Gibson:1984, Herbert:1965, Simmons:1989, Tolkien:1977, Tolkien:1937, Zola:1885, Zola:1877`
- ☐ **D06.** Les 3 moins chers. À égalité de prix, trie par titre dans l'ordre naturel, en le **nommant** explicitement.
  → `D06 : [Germinal, L'Assommoir, Le Hobbit]`
- ☐ **D07.** Trie la liste `b`, `null`, `a` deux fois :
  - `null` en tête, puis l'ordre naturel ;
  - `null` en fin, après l'ordre inverse.
  → `D07 : [null, a, b] [b, a, null]`
- ☐ **D08.** Tous les mots des titres (découpés sur les espaces), avec leur nombre total, puis le nombre de mots distincts.
  → `D08 : 10 mots, 9 distincts`
- ☐ **D09.** La page 2, de taille 3, des titres triés.
  → `D09 : [Hyperion, L'Assommoir, Le Hobbit]`
- ☐ **D10.** Sur `NUMBERS`, en `List<Integer>` :
  - les éléments **tant que** la valeur est inférieure à 9 ;
  - les éléments **à partir du premier** qui ne l'est plus.
  → `D10 : [5, 3, 8, 1] [9, 2, 8, 7]`
- ☐ **D11.** Compte `WORDS` avec un `peek` qui remplit une liste, puis refais-le avec un `filter(w -> true)` ajouté. Combien de fois `peek` a-t-il tourné chaque fois ? Explique pourquoi en commentaire.
  → `D11 : count 9 -> peek 0 fois ; avec filter count 9 -> peek 9 fois`
- ☐ **D12.** Trie `WORDS` par longueur **seulement**. À longueur égale, dans quel ordre restent les mots ? (Le tri d'un flux ordonné est *stable*.)
  → `D12 : [map, java, java, stream, lambda, stream, filter, optional, collector]`

**Expérience** (hors sortie attendue) : trie, sans comparateur, un stream de deux `new Object()`. Ça compile ? Que se passe-t-il à l'exécution, et pourquoi ?
- ☐ **D13.** Sur `[[a, b], [], [c]]`, affiche les tailles (`map`), puis tous les éléments à plat.
  → `D13 : map [2, 0, 1], flatMap [a, b, c]`

## Sortie attendue complète

```
D01 : [Dune, Hyperion, Neuromancien]
D02 : [collector, filter, java, lambda, map, optional, stream]
D03 : [stream, optional, map]
D04 : [Germinal, L'Assommoir]
D05 : Asimov:1951, Gibson:1984, Herbert:1965, Simmons:1989, Tolkien:1977, Tolkien:1937, Zola:1885, Zola:1877
D06 : [Germinal, L'Assommoir, Le Hobbit]
D07 : [null, a, b] [b, a, null]
D08 : 10 mots, 9 distincts
D09 : [Hyperion, L'Assommoir, Le Hobbit]
D10 : [5, 3, 8, 1] [9, 2, 8, 7]
D11 : count 9 -> peek 0 fois ; avec filter count 9 -> peek 9 fois
D12 : [map, java, java, stream, lambda, stream, filter, optional, collector]
D13 : map [2, 0, 1], flatMap [a, b, c]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Opération | Remarque |
|---|---|
| `filter`, `map`, `flatMap(Function<T, Stream<R>>)` | `flatMap` doit rendre un `Stream` |
| `distinct()` | utilise `equals`/`hashCode` ; garde la 1re occurrence |
| `sorted()` | exige `Comparable` (sinon `ClassCastException` **à l'exécution**) |
| `sorted(Comparator)` | |
| `skip(n)`, `limit(n)` | `limit` court-circuite |
| `takeWhile`, `dropWhile` | Java 9 ; s'arrêtent au **premier** échec (préfixe) |
| `peek(Consumer)` | pour le débogage ; peut être **sauté** par `count()` si la taille est connue (Java 9+) |

**`Comparator` :**
- `comparing(f)`, `comparing(f, cmp)`, `comparingInt`, `comparingLong`, `comparingDouble` ;
- `thenComparing(f)`, `thenComparing(f, cmp)`, `thenComparingInt` ;
- `reversed()` inverse **tout** ce qui précède ;
- `Comparator.naturalOrder()`, `Comparator.reverseOrder()` ;
- `nullsFirst(cmp)`, `nullsLast(cmp)`.

**Piège de typage :** dans `comparing(b -> b.pages()).reversed()`, le type de `b` n'est pas inféré. Écris `(Book b) -> …` ou une référence de méthode.

</details>
