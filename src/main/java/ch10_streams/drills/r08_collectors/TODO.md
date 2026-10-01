# Drill de rappel 8 — Les collecteurs simples

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall08`** et ton record pour `Data.BOOKS`.
- Écris une ligne `Dxx : ` par défi.

## Défis

- ☐ **D01.** Collecte les auteurs de trois façons :
  - en liste : affiche la taille ;
  - en ensemble : affiche la taille ;
  - en `TreeSet` : affiche le premier et le dernier, séparés par `..`.
  → `D01 : 8 6 Asimov..Zola`
- ☐ **D02.** La taille de l'ensemble **non modifiable** des mots. Puis collecte `"a"` et `null` dans une liste **non modifiable** et affiche l'exception.
  → `D02 : 7 NullPointerException`
- ☐ **D03.** Les trois formes de jointure sur `a`, `b` : sans séparateur, avec `-`, puis `", "` avec crochets. Enfin, cette dernière forme sur un flux **vide**.
  → `D03 : ab a-b [a, b] []`
- ☐ **D04.** Avec des collecteurs uniquement, stockés dans des variables dont **tu écris le type exact** :
  - le nombre de livres ;
  - la moyenne des pages ;
  - le total des pages ;
  - le total des prix (2 décimales).
  → `D04 : 8 399.875 3199 71.10`
- ☐ **D05.** Trois collecteurs de statistiques :
  - les années : affiche `min-max` ;
  - les prix : affiche le max ;
  - les pages **en `long`** : affiche la somme.
  → `D05 : 1877-1989 12.0 3199`
- ☐ **D06.** Le titre le plus ancien, puis le titre le plus cher, avec les collecteurs « min » et « max ».
  → `D06 : L'Assommoir Le Silmarillion`
- ☐ **D07.** Trois `toMap` :
  - auteur → titre **sans** fusion (échoue) ;
  - auteur → titres fusionnés par `+`, dans une `TreeMap` ;
  - titre → pages.

  Affiche dans cet ordre : l'exception, les titres de Zola, les pages de Dune, puis les clés de la `TreeMap`.
  → `D07 : IllegalStateException Germinal+L'Assommoir 412 [Asimov, Gibson, Herbert, Simmons, Tolkien, Zola]`
- ☐ **D08.** La moyenne des prix (3 décimales), puis la moyenne des années par le collecteur de moyenne sur des **`long`**.
  → `D08 : 8.888 1945.625`

## Sortie attendue complète

```
D01 : 8 6 Asimov..Zola
D02 : 7 NullPointerException
D03 : ab a-b [a, b] []
D04 : 8 399.875 3199 71.10
D05 : 1877-1989 12.0 3199
D06 : L'Assommoir Le Silmarillion
D07 : IllegalStateException Germinal+L'Assommoir 412 [Asimov, Gibson, Herbert, Simmons, Tolkien, Zola]
D08 : 8.888 1945.625
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Collecteur | Type du résultat | Piège |
|---|---|---|
| `toList()`, `toSet()` | `List`, `Set` | modifiables, mais rien n'est **garanti** |
| `toCollection(Supplier)` | la collection fournie | `TreeSet::new`, `ArrayDeque::new`… |
| `toUnmodifiableList/Set/Map` | non modifiables | `null` → **NPE** |
| `joining()`, `joining(sep)`, `joining(sep, prefix, suffix)` | `String` | flux vide → `prefix + suffix` |
| `counting()` | **`Long`** | |
| `averagingInt/Long/Double` | **`Double`** (toujours) | |
| `summingInt` / `summingLong` / `summingDouble` | `Integer` / `Long` / `Double` | |
| `summarizingInt/Long/Double` | `XxxSummaryStatistics` | |
| `minBy(cmp)` / `maxBy(cmp)` | `Optional<T>` | |
| `toMap(k, v)` | `Map` | clé en double → **`IllegalStateException`** |
| `toMap(k, v, merge)` | `Map` | |
| `toMap(k, v, merge, mapSupplier)` | la `Map` fournie | |

</details>
