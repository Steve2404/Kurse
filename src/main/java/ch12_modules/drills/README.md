# Chapitre 12 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **comprendre**. Les **drills** te font **retenir** : écrire de mémoire les directives et les commandes, et prévoir les messages des outils. C'est ce que l'examen mesure.

Chaque drill = des modules dans `ch12_modules/drills/rNN_…/src/`, plus un script `recall.sh` à côté du `TODO.md`.

## Les règles d'un drill

1. **Chronomètre-toi.** Le chrono cible est en haut du `TODO.md`.
2. **Ni carte, ni `--help`, ni solution pendant le drill.**
3. **Bloqué plus de 3 min ?** Marque ✗ et passe au défi suivant.
4. **À la fin,** ouvre la carte mémoire pour tes ✗ seulement, puis fais les **expériences**.
5. **Avant chaque répétition,** supprime tes modules et ton `recall.sh`.

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
| p01 `library` | r01, r02 |
| p02 `pricing` | r03 |
| p03 `config` | r06 |
| p04 `migration` | r04 |
| p05 `runtime` | r05 |

## Tableau de suivi

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | Directives de `module-info` | | | | | | |
| r02 | `javac`, `java`, `jar` | | | | | | |
| r03 | Services | | | | | | |
| r04 | Nommés, automatiques, sans nom | | | | | | |
| r05 | `--describe-module`, `jdeps`, `jlink` | | | | | | |
| r06 | Kata (options courtes) | | | | | | |
