# Chapitre 9 (Collections and Generics) — parcours, drills et plan de révision

Les **exercices** (`ch9_collections/exercises`, 01 → 21) t'apprennent les notions.
Les **drills** (`ch9_collections/drills/exercises`, 01 → 06) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : `drills/Pantry.java`
(7 fruits, 6 nombres, 5 prix). Lis ce fichier une fois et garde-le ouvert à côté.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | List et Set | 01 → 02 → 03 | Drill01 |
| 2 | Queue, Deque, PriorityQueue | 04 → 05 → 06 → 07 | Drill02 |
| 3 | Map | 08 → 09 → 10 → 11 | Drill03 |
| 4 | Trier et chercher | 12 → 13 → 14 → 15 | Drill04 |
| 5 | Iterable / Iterator | 16 | refaire Drill01 et Drill02 |
| 6 | Génériques | 17 → 18 → 19 → 20 | Drill05 |
| 7 | Synthèse | 21 (capstone : graphe générique) | Drill06 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : le tableau des méthodes de Deque (exception / valeur
spéciale), ce que rendent `put` / `merge` / `computeIfAbsent`, et la phrase PECS.

**Conseil propre à ce chapitre :** pour chaque collection, pose-toi trois questions —
*ordre ?* (aucun, ajout, trié), *doublons ?* (List oui, Set non), *null ?* (`List.of`,
`TreeSet`, `ArrayDeque` le refusent). Pour chaque générique : *est-ce que je lis
(extends) ou j'écris (super) ?*

| Drill | Contenu | TODO |
|---|---|---|
| 01 `ListAndSet` | `subList`, `add(i, e)`, `remove(int)` / `remove(Object)`, `removeIf`, `replaceAll`, `LinkedHashSet`, `TreeSet`, `retainAll`, `headSet`, `List.of` immuable | 12 |
| 02 `QueueAndDeque` | file (`offer`/`poll`), pile (`push`/`pop`), `peek` et `pop` sur vide, `PriorityQueue` (naturel, inversé, Comparator), `descendingIterator` | 10 |
| 03 `MapApi` | `get`, `getOrDefault`, `put` (ancienne valeur), `putIfAbsent`, `merge`, `computeIfAbsent`, `replaceAll`, `computeIfPresent` → null, `entrySet`, `ceilingEntry`, `firstKey` | 12 |
| 04 `SortingAndSearching` | `Collections.sort`, `reverseOrder`, `comparingInt`, `reversed`, `thenComparing`, `nullsFirst`, `binarySearch` (trouvé / absent), `max`, `min` | 10 |
| 05 `Generics` | méthode générique, `<T extends Comparable<? super T>>`, `? extends`, `? super`, `<K, V>`, `List<?>`, méthode générique dans une classe générique, record générique | 10 |
| 06 `MixedKata` | 10 questions sur le marché, **sans indiquer la forme** | 10 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris de mémoire**.
   Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` jusqu'à 100 %.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois.
- **Mélange** : le Drill06 chaque semaine pendant la révision de l'examen.
- **Lecture de code** : les exercices 02, 04 et 08 comparent ta règle à la **JVM**
  (exceptions de `List.of`, `Arrays.asList`, méthodes de Deque sur vide, retours de
  `put` / `merge`…) ; les exercices 18 et 19 la comparent aux **verdicts réels de `javac`**
  (`generic array creation`, `name clash: ... have the same erasure`,
  `non-static type variable T cannot be referenced from a static context`,
  `Object cannot be safely cast to List<String>`, `interface expected here`…).
  Relis leurs Javadoc avant l'examen, puis fais les questions de révision du livre.
- Le chapitre 10 (streams) s'appuie sur tout ce chapitre : `Collectors.groupingBy` est le
  `computeIfAbsent` de la Map, `sorted(Comparator)` réutilise les Comparator de l'exercice 13.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch9_collections/drills/exercises/Drill01_ListAndSet.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 List et Set | | | | | | | |
| 02 Queue et Deque | | | | | | | |
| 03 API de Map | | | | | | | |
| 04 Trier et chercher | | | | | | | |
| 05 Génériques | | | | | | | |
| 06 Kata mélangé | | | | | | | |
