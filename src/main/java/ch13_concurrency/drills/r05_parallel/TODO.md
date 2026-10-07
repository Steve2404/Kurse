# Drill de rappel 5 — Les streams parallèles

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall05`** dans le paquet `ch13_concurrency.drills.r05_parallel`, avec `List<Integer> nums` = 1 à 10.

**Les notions de ce drill ont été apprises dans :** projet 6 (étapes 1 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r05_parallel` → **New** → **Java Class** → `Recall05`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall05`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall05`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Affiche :
  - `nums.stream().isParallel()` ;
  - `nums.parallelStream().isParallel()` ;
  - `nums.stream().parallel().sequential().isParallel()`.
  → `D01 : false true false`
- ☐ **D02.** En parallèle, affiche :
  - `reduce(0, Integer::sum)` ;
  - si `reduce(5, Integer::sum)` diffère de 60 ;
  - `reduce(0, (acc, n) -> acc + n * n, Integer::sum)`.
  → `D02 : 55 true 385`
- ☐ **D03.** `forEachOrdered` en parallèle, vers une liste synchronisée. Puis `nums.parallelStream().map(n -> n * 10).toList()`.
  → `D03 : [1, 2, …, 10] [10, 20, …, 100]`
- ☐ **D04.** En parallèle, avec `filter(n -> n > 4)` : `findFirst()`, puis `findAny().isPresent()`. Ensuite `unordered().skip(2).count()`.
  → `D04 : 5 true 8`
- ☐ **D05.** Deux collecteurs concurrents :
  - `groupingByConcurrent(n -> n % 2 == 0)` : affiche le nom simple de sa classe, puis les tailles par clé (`TreeMap`) ;
  - `toConcurrentMap(n -> n, n -> "n" + n * n)`, pour n ≤ 4, en `TreeMap`.
  → `D05 : ConcurrentHashMap {false=5, true=5} {1=n1, 2=n4, 3=n9, 4=n16}`
- ☐ **D06.** Sur `List.of("b", "a", "d", "c")` en parallèle :
  - `Collectors.joining(",")` ;
  - `sorted().toList()` ;
  - `map(String::toUpperCase).collect(Collectors.toList())`.
  → `D06 : b,a,d,c [a, b, c, d] [B, A, D, C]`

## Expériences (hors sortie attendue)

1. Remplace `forEachOrdered` par `forEach` dans D03, et lance plusieurs fois : que vois-tu ?
2. `reduce(0, (a, b) -> a - b)` en parallèle : le résultat change-t-il d'une exécution à l'autre ?
3. Pourquoi `findAny` peut-il être plus rapide que `findFirst` en parallèle ?
4. Pourquoi `joining` garde-t-il l'ordre, alors que le travail est réparti sur plusieurs threads ?

## Sortie attendue complète

```
D01 : false true false
D02 : 55 true 385
D03 : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
D04 : 5 true 8
D05 : ConcurrentHashMap {false=5, true=5} {1=n1, 2=n4, 3=n9, 4=n16}
D06 : b,a,d,c [a, b, c, d] [B, A, D, C]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Créer :** `collection.parallelStream()` ou `stream.parallel()`. Revenir en séquentiel : `sequential()`. Le **dernier** appel l'emporte pour tout le pipeline.
- **Ce qui reste déterministe** (sur une source ordonnée) :
  - `findFirst`, `limit`, `skip`, `sorted` ;
  - `toList`, `collect` (dont `joining`), `forEachOrdered` ;
  - `reduce` associatif avec une identité neutre.
- **Ce qui ne l'est pas :** `forEach`, `findAny`, et tout ce qui suit `unordered()`.
- **`reduce(identité, accumulateur, combineur)`** :
  - `identité` est neutre : `combineur(identité, x) == x` ;
  - l'accumulateur et le combineur sont associatifs, et compatibles entre eux.
- **`collect(fournisseur, accumulateur, combineur)`** : chaque morceau a son propre conteneur, et les conteneurs sont ensuite fusionnés.
- **Les collecteurs concurrents :** `groupingByConcurrent` et `toConcurrentMap` remplissent **une seule** `ConcurrentMap` partagée. Ils sont efficaces si le stream est parallèle et non ordonné.
- **Les effets de bord** (écrire dans une liste non synchronisée) sont à proscrire.

</details>
