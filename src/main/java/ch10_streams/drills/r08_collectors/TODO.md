# Drill de rappel 8 — Les collecteurs simples

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall08`** et ton record pour `Data.BOOKS`.
- Écris une ligne `Dxx : ` par défi.

**Les notions de ce drill ont été apprises dans :** projet 5 (étapes 2, 3, 7 et 8). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r08_collectors` → **New** → **Java Class** → `Recall08`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall08`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall08`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Collecte les auteurs de trois façons :
  - en liste : affiche la taille ;
  - en ensemble : affiche la taille ;
  - en `TreeSet` : affiche le premier et le dernier, séparés par `..`.
  → `D01 : 8 6 Asimov..Zola`
- ☐ **D02.** La taille de l'ensemble **non modifiable** des mots, puis celle de la liste **non modifiable** des mots.
  → `D02 : 7 9`

**Expérience** (hors sortie attendue) : collecte `"a"` et `null` dans une liste non modifiable, lance, et note l'exception.
- ☐ **D03.** Les trois formes de jointure sur `a`, `b` : sans séparateur, avec `-`, puis `", "` avec crochets. Enfin, cette dernière forme sur un flux **vide**.
  → `D03 : ab a-b [a, b] []`
- ☐ **D04.** Avec des collecteurs uniquement, stockés dans des variables dont **tu écris le type exact** :
  - le nombre de livres ;
  - la moyenne des pages ;
  - le total des pages ;
  - le total des prix, arrondi à 2 décimales avec `Math.round`.
  → `D04 : 8 399.875 3199 71.1`
- ☐ **D05.** Trois collecteurs de statistiques :
  - les années : affiche `min-max` ;
  - les prix : affiche le max ;
  - les pages **en `long`** : affiche la somme.
  → `D05 : 1877-1989 12.0 3199`
- ☐ **D06.** Le titre le plus ancien, puis le titre le plus cher, avec les collecteurs « min » et « max ».
  → `D06 : L'Assommoir Le Silmarillion`
- ☐ **D07.** Deux `toMap` :
  - auteur → titres fusionnés par `+`, dans une `TreeMap` ;
  - titre → pages.

  Affiche dans cet ordre : les titres de Zola, les pages de Dune, puis les clés de la `TreeMap`.
  → `D07 : Germinal+L'Assommoir 412 [Asimov, Gibson, Herbert, Simmons, Tolkien, Zola]`

**Expérience** (hors sortie attendue) : fais un `toMap` auteur → titre **sans** fonction de fusion, lance, et note l'exception. Pourquoi l'auteur pose-t-il problème, et pas le titre ?
- ☐ **D08.** La moyenne des prix (arrondie à 3 décimales avec `Math.round`), puis la moyenne des années par le collecteur de moyenne sur des **`long`**.
  → `D08 : 8.888 1945.625`

## Sortie attendue complète

```
D01 : 8 6 Asimov..Zola
D02 : 7 9
D03 : ab a-b [a, b] []
D04 : 8 399.875 3199 71.1
D05 : 1877-1989 12.0 3199
D06 : L'Assommoir Le Silmarillion
D07 : Germinal+L'Assommoir 412 [Asimov, Gibson, Herbert, Simmons, Tolkien, Zola]
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
