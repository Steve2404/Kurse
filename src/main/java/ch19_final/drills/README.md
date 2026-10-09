# Chapitre 19 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **construire** chaque brique, avec ses leçons et ses mesures. Les **drills** te les font **réécrire de mémoire**, vite et proprement : un parseur, une transaction, un routeur, un pool borné, un disjoncteur, une revue express. Ici, tu écris seulement le code ; ce sont les **tests de référence** qui le vérifient, avec les règles de forme de `Check`.

## Les règles d'un drill

1. **Chronomètre-toi.** Le chrono cible est en haut du `TODO.md`.
2. **Ni carte, ni Javadoc, ni solution pendant le drill.**
3. **Bloqué plus de 3 min ?** Marque ✗ et passe au défi suivant.
4. **À la fin,** ouvre la carte mémoire pour tes ✗ seulement.
5. **Avant chaque répétition,** supprime tes fichiers du drill.

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
| p01 `JsonParser` | r01 |
| p02 `JdbcTaskRepository` | r02 |
| p03 `WebServer` | r03 |
| p04 `ReminderService` | r04 |
| p05 `ResilientStock` | r05 |
| p06 et p07 | r06 |
| p08 (capstone) | refais r03 en 15 minutes : c'est le test final |

## Tableau de suivi

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | Un mini JSON | | | | | | |
| r02 | JDBC solide | | | | | | |
| r03 | Routeur et serveur HTTP | | | | | | |
| r04 | La concurrence en conditions réelles | | | | | | |
| r05 | Limiteur, disjoncteur, percentiles | | | | | | |
| r06 | La revue express | | | | | | |
