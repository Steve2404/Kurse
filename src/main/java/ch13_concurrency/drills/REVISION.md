# Chapitre 13 (Concurrency) — parcours, drills et plan de révision

Les **exercices** (`ch13_concurrency/exercises`, 01 → 16) t'apprennent les notions.
Les **drills** (`ch13_concurrency/drills/exercises`, 01 → 05) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : `drills/Loans.java`
(les 12 emprunts de la bibliothèque du chapitre 10, 134 jours au total). Lis ce fichier une fois.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

⚠️ **Lance chaque `main()` plusieurs fois.** Un code concurrent faux peut réussir une fois par
chance. Les tests sont écrits pour être déterministes (`join`, `awaitTermination`, latchs, jamais de
`sleep` « au hasard ») : une version juste passe à chaque fois. Aucun test ne lance de vrai
deadlock (l'Exercise10 utilise des threads daemon et un délai maximal).

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | Threads | 01 → 02 | Drill01 (TODO 1 et 2) |
| 2 | ExecutorService, Callable, Future | 03 → 04 → 05 | Drill01 (TODO 3 à 10) |
| 3 | Synchronisation : synchronized, Lock, atomiques, barrière | 06 → 07 → 08 → 09 | Drill02 |
| 4 | Problèmes de concurrence | 10 | refaire Drill02 |
| 5 | Collections concurrentes | 11 → 12 | Drill03 |
| 6 | Streams parallèles | 13 → 14 → 15 | Drill04 |
| 7 | Synthèse | 16 (capstone : producteur / consommateur) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : les méthodes d'`ExecutorService` (`execute`, `submit`,
`invokeAll`, `invokeAny`, `shutdown`, `shutdownNow`, `awaitTermination`), les 6 états d'un Thread,
les 4 problèmes (deadlock, livelock, famine, race condition) avec un remède chacun.

**Conseil propre à ce chapitre :** devant chaque donnée partagée, demande-toi *qui la lit, qui
l'écrit, et en même temps ?* Si deux threads écrivent : opération atomique (`merge`,
`incrementAndGet`), verrou, ou une case par thread.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `ThreadsAndExecutors` | `start` + `join`, deux threads, `submit` + `get`, `invokeAll`, `invokeAny`, `execute`, `shutdown` + `awaitTermination`, `schedule`, `ExecutionException`, pool fixe | 10 |
| 02 `SynchronizationTools` | `synchronized`, `addAndGet`, `getAndIncrement` contre `incrementAndGet`, `compareAndSet`, `accumulateAndGet`, `lock`/`unlock`, `tryLock`, réentrance, `CyclicBarrier`, `IllegalMonitorStateException` | 10 |
| 03 `ConcurrentCollections` | `ConcurrentHashMap.merge`, `newKeySet`, `putIfAbsent`, `CopyOnWriteArrayList`, `ConcurrentLinkedQueue`, `poll(délai)`, `ConcurrentSkipListSet`, `synchronizedList` | 8 |
| 04 `ParallelStreams` | `parallelStream`, `isParallel`, `findFirst` / `findAny`, `forEachOrdered`, `reduce` à 3 arguments, `groupingByConcurrent`, `toConcurrentMap` | 8 |
| 05 `MixedKata` | 8 questions sur les emprunts, **sans indiquer la forme** | 8 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris de mémoire**.
   Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` **plusieurs fois** jusqu'à 100 % à chaque fois.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois.
- **Mélange** : le Drill05 chaque semaine pendant la révision de l'examen.
- **Lecture de code** : l'Exercise02 compare ta règle aux **vrais états** des threads
  (`getState()`), l'Exercise12 au vrai comportement des collections modifiées pendant un for-each
  (`ConcurrentModificationException` même pour `synchronizedList`). Relis leurs Javadoc avant l'examen,
  puis fais les questions de révision du livre.
- Le chapitre 10 (streams) prolonge les exercices 13 à 15 : `reduce` à 3 arguments (Exercise17 et
  Drill08 du chapitre 10).

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch13_concurrency/drills/exercises/Drill01_ThreadsAndExecutors.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Threads et executors | | | | | | | |
| 02 Synchronisation | | | | | | | |
| 03 Collections concurrentes | | | | | | | |
| 04 Streams parallèles | | | | | | | |
| 05 Kata mélangé | | | | | | | |
