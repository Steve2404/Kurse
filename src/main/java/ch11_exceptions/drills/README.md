# Chapitre 11 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **comprendre**. Les **drills** te font **retenir** : répondre vite, de mémoire, à ce qui fait la difficulté du chapitre 11 :
- la famille d'une exception ;
- le chemin dans `finally` ;
- l'ordre de fermeture des ressources ;
- le message exact d'une exception du JDK ;
- le résultat d'un format numérique ou de date ;
- le bundle choisi pour une locale.

C'est ce que l'examen mesure.

## Les règles d'un drill

1. **Chronomètre-toi.** Le chrono cible est en haut du `TODO.md`.
2. **Ni carte, ni Javadoc, ni solution pendant le drill.**
3. **Bloqué plus de 3 min ?** Marque ✗ et passe au défi suivant.
4. **À la fin,** ouvre la carte mémoire pour tes ✗ seulement, puis fais les **expériences**.
5. **Avant chaque répétition,** supprime ton `RecallNN.java` (et les bundles de r09).

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
| p01 `Teller` | r01, r05 |
| p02 `Calculator` | r03, r02 |
| p03 `Resources` | r04 |
| p04 `VmLab` | (révision r02) |
| p05 `Billing` | r06, r07 |
| p06 `Agenda` | r08 |
| p07 `Shop` | r09, r10 (test final) |

## Tableau de suivi

Note la date et le temps (par exemple `03/10 · 9 min · 1✗`).

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | Hiérarchie des exceptions | | | | | | |
| r02 | `try` / `catch` / `finally` | | | | | | |
| r03 | Écrire, chaîner, relancer | | | | | | |
| r04 | try-with-resources | | | | | | |
| r05 | Exceptions du JDK | | | | | | |
| r06 | `NumberFormat` | | | | | | |
| r07 | `DecimalFormat` | | | | | | |
| r08 | `DateTimeFormatter` | | | | | | |
| r09 | `Locale` et `ResourceBundle` | | | | | | |
| r10 | Kata : `MessageFormat`, `Properties` (test final) | | | | | | |
