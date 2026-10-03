# Drill de rappel 6 — Kata : les problèmes de concurrence

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall06`** dans le paquet `ch13_concurrency.drills.r06_kata`, avec un champ `static int unsafe`.
- Ajoute `static boolean grab(ReentrantLock mine, ReentrantLock theirs, CyclicBarrier both, long waitMs) throws Exception` :
  1. `mine.lock()` ;
  2. `both.await()` ;
  3. `theirs.tryLock(waitMs, MILLISECONDS)` : si l'essai réussit, `theirs.unlock()` aussitôt ;
  4. `mine.unlock()` dans un `finally` ;
  5. rend le résultat de l'essai.

## Défis

- ☐ **D01. La course.** 4 tâches qui, 50 000 fois chacune, font `unsafe++` et `safe.incrementAndGet()` (un `AtomicInteger`). Affiche `safe`, puis `unsafe <= 200000`.
  → `D01 : atomique 200000, sans protection <= 200000 true`
- ☐ **D02. L'interblocage évité.** Deux verrous `left` et `right`, et une `CyclicBarrier(2)` :
  - la tâche 1 fait `grab(left, right, …, 30)` ;
  - la tâche 2 fait `grab(right, left, …, 3_000)`.
  → `D02 : premier false, second true`
- ☐ **D03. L'ordre global.** 8 tâches, dont la moitié prend les verrous « à l'envers ». Chaque tâche, 1 000 fois :
  1. range ses deux verrous par `System.identityHashCode` ;
  2. verrouille le plus petit, **puis** l'autre ;
  3. incrémente un `AtomicInteger` ;
  4. déverrouille dans des `finally`.
  
  Affiche le total, puis le résultat de `awaitTermination`.
  → `D03 : 8000 deplacements, termine true`

## Expériences (hors sortie attendue)

1. Dans D02, donne le même délai aux deux (`30`) : le résultat est-il toujours le même ? Pourquoi ? (C'est le risque d'un **livelock** si on réessaie en boucle.)
2. Dans D03, supprime le tri des verrous : lance plusieurs fois, que risques-tu ?
3. Lance `unsafe++` plusieurs fois et note les valeurs : pourquoi sont-elles différentes ?

## Sortie attendue complète

```
D01 : atomique 200000, sans protection <= 200000 true
D02 : premier false, second true
D03 : 8000 deplacements, termine true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Problème | Définition | Remède |
|---|---|---|
| **Course** (*race condition*) | le résultat dépend de l'entrelacement des threads (`x++` non atomique) | `synchronized`, `Lock`, atomiques, structures concurrentes |
| **Interblocage** (*deadlock*) | chaque thread attend un verrou tenu par un autre, pour toujours | un ordre global des verrous ; `tryLock` avec délai |
| **Famine** (*starvation*) | un thread n'obtient jamais la ressource : d'autres passent toujours avant | verrous équitables, `new ReentrantLock(true)` |
| **Livelock** | les threads réagissent l'un à l'autre (relâchent, réessaient) sans jamais avancer | délais aléatoires ou différents, ordre global |

- **Une course ne produit jamais un résultat « trop grand »** avec `x++` : elle **perd** des incréments.
- **Les 4 conditions d'un interblocage :**
  1. exclusion mutuelle ;
  2. détention et attente ;
  3. pas de préemption ;
  4. attente circulaire.
  
  L'ordre global casse la 4e.

</details>
