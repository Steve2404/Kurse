# Chapitre 9 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **comprendre**. Les **drills** te font **retenir** : répondre vite, de mémoire, à ce qui fait la difficulté du chapitre 9 :
- quelle méthode rend `null` et laquelle lève une exception ;
- l'ordre et les `null` de chaque collection ;
- ce qui est modifiable ou figé ;
- la sémantique de `merge` et de `compute` ;
- l'ordre d'un `Comparator` composé ;
- ce qui compile avec les génériques et les jokers.

C'est ce que l'examen mesure.

## Les règles d'un drill

1. **Chronomètre-toi.** Le chrono cible est en haut du `TODO.md`.
2. **Ni carte, ni Javadoc, ni solution pendant le drill.**
3. **Bloqué plus de 3 min ?** Marque ✗ et passe au défi suivant.
4. **À la fin,** ouvre la carte mémoire pour tes ✗ seulement, puis fais les **expériences**.
5. **Avant chaque répétition,** supprime ton `RecallNN.java`.

## Le plan de répétition espacée

| Répétition | Quand | Objectif |
|---|---|---|
| R1 | J+1 | tout juste |
| R2 | J+3 | sous le chrono cible |
| R3 | J+7 | sous le chrono, **sans ouvrir la carte** |
| R4 | J+14 | moitié du chrono |
| R5 | J+30 | d'une traite : drill **acquis** |

**Ajuster le rythme :**
- plus de 2 ✗ → retour au palier précédent ;
- parfait et rapide → un palier sauté.

## Quand faire quel drill

| Après le projet… | Drills |
|---|---|
| p01 `Inventory` | r01, r02, r06 |
| p02 `Words` | r03, r05 |
| p03 `Graphs` | r04 |
| p04 `GenericsLab` | r08, r09 |
| p05 `Ranking` | r07 |
| p06 `Editor` | (révision r03, r04) |
| p07 `Social` | r10 (test final) |

## Tableau de suivi

Note la date et le temps (par exemple `03/10 · 9 min · 1✗`).

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | L'interface `Collection` | | | | | | |
| r02 | `List`, `ListIterator`, `LinkedList` | | | | | | |
| r03 | `Set`, `NavigableSet` | | | | | | |
| r04 | `Queue`, `Deque`, `PriorityQueue` | | | | | | |
| r05 | L'API `Map` | | | | | | |
| r06 | Collections immuables et vues | | | | | | |
| r07 | `Comparable` et `Comparator` | | | | | | |
| r08 | Déclarer des génériques | | | | | | |
| r09 | Les jokers | | | | | | |
| r10 | Kata mixte (test final) | | | | | | |
