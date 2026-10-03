# Chapitre 13 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **comprendre**. Les **drills** te font **retenir** : écrire de mémoire l'API de la concurrence, et prévoir ses pièges (états, exceptions, ordre, atomicité). C'est ce que l'examen mesure.

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

## Quand faire quel drill

| Après le projet… | Drills |
|---|---|
| p01 `Downloader` | r01 |
| p02 `PrimeLab` | r02 |
| p03 `BankLab` | r03 |
| p04 `LogPipeline` | r04 |
| p05 `ParallelLife` | (révision r03) |
| p06 `ParallelLab` | r05 |
| p07 `CrawlerApp` | r06 (test final) |
| p08 `AsyncLab` (bonus) | r07 (bonus) |

## Tableau de suivi

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | Threads | | | | | | |
| r02 | Executors, `Future`, planification | | | | | | |
| r03 | `synchronized`, atomiques, verrous, barrière | | | | | | |
| r04 | Collections concurrentes | | | | | | |
| r05 | Streams parallèles | | | | | | |
| r06 | Kata : problèmes de concurrence (test final) | | | | | | |
| r07 | Bonus : `CompletableFuture`, Fork/Join, `ThreadLocal`, `Semaphore` | | | | | | |
