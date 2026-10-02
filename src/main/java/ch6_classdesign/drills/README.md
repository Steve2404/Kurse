# Chapitre 6 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **comprendre**. Les **drills** te font **retenir** : trancher vite, de mémoire, ce qui fait la difficulté du chapitre 6 :
- l'ordre d'initialisation ;
- quel constructeur appelle lequel ;
- si une méthode est redéfinie, masquée ou surchargée ;
- ce que voit une référence de type parent ;
- si une classe est vraiment immuable.

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
| p01 `ShapesApp` | r01, r02 |
| p02 `TracerApp` | r03 |
| p03 `Payroll` | r04, r05 |
| p04 `ImmutableLab` | r07, r08 |
| p05 `Arena` | r06 |
| p07 `MediaApp` | r09 (test final) |

## Tableau de suivi

Note la date et le temps (par exemple `03/10 · 12 min · 1✗`).

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | Héritage, `Object`, `final` | | | | | | |
| r02 | Constructeurs, `this()`, `super()` | | | | | | |
| r03 | Ordre d'initialisation | | | | | | |
| r04 | Redéfinir ou surcharger | | | | | | |
| r05 | Masquer ou redéfinir | | | | | | |
| r06 | Classes abstraites | | | | | | |
| r07 | Objets immuables | | | | | | |
| r08 | `toString`, `equals`, `hashCode` | | | | | | |
| r09 | Kata mixte (test final) | | | | | | |
