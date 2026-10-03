# Chapitre 15 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **comprendre**. Les **drills** te font **retenir** : écrire de mémoire l'API JDBC, et prévoir ses pièges (index à partir de 1, curseur avant la 1re ligne, `wasNull`, auto-commit, fermetures). C'est ce que l'examen mesure.

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
| p01 `LibraryApp` | r01 |
| p02 `BankApp` | r02 |
| p03 `ShopApp` | r04 |
| p04 `ImportApp` | (révision r02) |
| p05 `LoyaltyApp` | r03 |
| p06 `ReportApp` | r05 |
| p07 `BikeApp` | r06 (test final) |
| après r06 | r07 (bonus : pilotes, curseurs défilants, isolation ; une partie avec Docker, à la main) |

## Tableau de suivi

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | Connexion, `Statement`, `ResultSet` | | | | | | |
| r02 | `PreparedStatement` | | | | | | |
| r03 | `CallableStatement` | | | | | | |
| r04 | Transactions et `Savepoint` | | | | | | |
| r05 | Lots, clés, métadonnées, exceptions | | | | | | |
| r06 | Kata : le carnet de notes (test final) | | | | | | |
| r07 | Bonus : fournisseurs, curseurs défilants, isolation | | | | | | |
