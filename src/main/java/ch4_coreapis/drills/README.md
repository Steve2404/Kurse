# Chapitre 4 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **comprendre**. Les **drills** te font **retenir** : retrouver vite, de mémoire, ce qui fait la difficulté du chapitre 4 :
- ce que rend une méthode, et si elle modifie l'objet ;
- la valeur exacte de `binarySearch`, `compare` ou `round(-2.5)` ;
- le résultat de `==` sur deux `String` ;
- la date obtenue un 31 plus un mois.

C'est ce que l'examen mesure.

## Les règles d'un drill

1. **Chronomètre-toi.** Le chrono cible est en haut du `TODO.md`.
2. **Ni carte, ni Javadoc, ni solution pendant le drill.** C'est l'effort de rappel qui fixe la mémoire.
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
| p01 `TextStats` | r01, r02 |
| p02 `Editor` | r03, r04 |
| p03 `Life` | r05 |
| p04 `Scores` | r06, r07 |
| p05 `Calendar` | r08, r09 |
| p06 `Hotel` | r10 (test final) |

## Tableau de suivi

Note la date et le temps (par exemple `03/10 · 12 min · 1✗`).

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | `String` : méthodes de base, 4 formes d'`indexOf` | | | | | | |
| r02 | `String` : concaténation, immutabilité, `indent`, formats | | | | | | |
| r03 | `StringBuilder` | | | | | | |
| r04 | Pool de chaînes et égalité | | | | | | |
| r05 | Tableaux : déclaration, valeurs par défaut, 2D | | | | | | |
| r06 | La classe `Arrays` | | | | | | |
| r07 | La classe `Math` (dont `random`) | | | | | | |
| r08 | `LocalDate`, `LocalTime`, `Period` | | | | | | |
| r09 | `Duration`, `Instant`, fuseaux, changement d'heure | | | | | | |
| r10 | Kata mixte (test final) | | | | | | |
