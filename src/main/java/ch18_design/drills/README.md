# Chapitre 18 — Drills de rappel (mémorisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../PARCOURS.md).

Les **projets** te font **comprendre** les principes et les patrons. Les **drills** te les font **réécrire de mémoire**, vite et proprement : en entretien et au travail, on attend qu'un développeur senior écrive un builder, un décorateur ou une machine à états sans hésiter. Ici, tu écris seulement le code ; ce sont les **tests de référence** qui le vérifient, avec les règles de forme de `Check`.

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
| p01 `Garage` | r01 |
| p02 `ShippingCalculator` | (attends p04) |
| p03 `Storage` | (le test de contrat se refait en refaisant p03) |
| p04 `OrderService` | r02 |
| p05 `Email` | r03 |
| p06 `Weather` | r04 |
| p07 `History` | r05 |
| p08 `Order` | r06 |
| p09 `CheeseShop` | refais r01 en 10 minutes : c'est le test final |

## Tableau de suivi

| Drill | Thème | J0 | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|---|---|
| r01 | Refactorer sans changer le résultat | | | | | | |
| r02 | Stratégies injectées | | | | | | |
| r03 | Builder et objet immuable | | | | | | |
| r04 | Adaptateur, proxy, décorateurs, composite | | | | | | |
| r05 | Commandes et observateurs | | | | | | |
| r06 | État et méthode modèle | | | | | | |
