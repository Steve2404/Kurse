# Chapitre 10 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

Les **projets** (`../projects`) te font **comprendre** l'API. Les **drills de rappel** te la font **retenir** : retrouver vite, de mémoire, la bonne méthode, son type de retour et son piège. C'est ce que l'examen mesure.

## Le format

- **Dans chaque dossier `rNN_…`, il y a :**
  - `TODO.md` : les défis D01, D02… Chaque défi donne sa consigne et **la ligne exacte attendue juste en dessous**. La **carte mémoire** est repliée en bas ;
  - `Check.java` : le correcteur. Il vérifie la sortie, puis les méthodes de l'API que tu dois avoir utilisées ;
  - `solution/` : la solution commentée.
- **Tu crées toi-même** la classe `RecallNN`, son `main` et ton record, à partir de `ch10_streams.drills.Data`, partagé par tous les drills.
- Lance `Check.java`. Avec l'argument `solution`, il vérifie la solution.

## Les règles d'un drill (c'est là que la mémoire se construit)

1. **Chronomètre-toi.** Vise le temps indiqué en haut du `TODO.md`.
2. **Ni carte, ni Javadoc, ni solution pendant le drill.** L'effort de rappel est **précisément** ce qui fixe la mémoire. Relire ne la fixe presque pas.
3. **Si tu bloques sur un défi,** passe au suivant et marque-le ✗.
4. **À la fin,** ouvre la carte mémoire. Relis **seulement** ce qui concerne tes ✗, puis termine ces défis.
5. **Remise à zéro :** supprime ton `RecallNN.java` avant chaque répétition. On repart toujours d'un fichier vide.

## Le plan de répétition espacée

Après le premier passage (J0), refais **le même drill, depuis un fichier vide**, aux intervalles suivants :

| Répétition | Quand | Objectif |
|---|---|---|
| R1 | J+1 | tout juste, même lentement |
| R2 | J+3 | sous le chrono cible |
| R3 | J+7 | sous le chrono, **sans ouvrir la carte** |
| R4 | J+14 | moitié du chrono cible |
| R5 | J+30 | d'une traite ; ensuite, le drill est **acquis** |

**Ajuster le rythme :**
- Un drill raté (plus de 2 ✗) revient au palier précédent.
- Un drill réussi sans faute **et** sous le chrono peut sauter un palier.

## Quand faire quel drill (lien avec les projets)

| Après le projet… | Fais les drills |
|---|---|
| p01 `LoanDesk` | r01 |
| p02 `BusNetwork` | r02, r03, r04 |
| p03 `WeatherStation` | r05 |
| p04 `League` | r07 |
| p05 `MusicStats` | r08, r09 |
| p06 `CashJournal` | r11 |
| p07 `Warehouse` | r12 (premier passage) |
| p08 `Telemetry` | r06, r10, puis r12 en test final |

## Tableau de suivi

Note la date et le temps (par exemple `03/10 · 14 min · 1✗`).

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | Optional | | | | | | |
| r02 | Sources, paresse | | | | | | |
| r03 | Intermédiaires, Comparator | | | | | | |
| r04 | Terminales | | | | | | |
| r05 | Streams primitifs | | | | | | |
| r06 | Interfaces fonctionnelles primitives | | | | | | |
| r07 | reduce / collect | | | | | | |
| r08 | Collecteurs simples | | | | | | |
| r09 | groupingBy / partitioningBy / teeing | | | | | | |
| r10 | Parallèle | | | | | | |
| r11 | Spliterator | | | | | | |
| r12 | Kata mixte (test final) | | | | | | |

**Critère de fin du chapitre :**
- les 12 drills ont passé R3 ;
- r12 passe en moins de 25 minutes, sans carte ;
- p07 et p08 sont refaits **depuis zéro** 2 à 3 semaines plus tard.
