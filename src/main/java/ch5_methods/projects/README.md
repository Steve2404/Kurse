# Chapitre 5 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et la règle du crescendo) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données, s'il y en a ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Toutes les classes, et les sous-paquets, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_stats` — boîte à outils statistique | conception de méthodes, varargs (`int...`, `int[]...`, `Object...`, `null`), `return;`, `private` | `StatsReport` (+ `Stats`) | quickselect, modes, moyenne glissante, histogramme vertical |
| ☐ | `p02_bank` — banque en paquets | les 4 niveaux d'accès, `protected` d'un autre paquet, encapsulation, fabriques `static`, import static | `app.BankApp` | qui peut appeler quoi ; virement tout ou rien ; intérêts en centimes |
| ☐ | `p03_bikes` — vélos en libre-service | `static` contre instance, blocs `static` et `{ }`, ordre d'initialisation, `static final`, `static` via `null` | `BikeApp` | Floyd-Warshall dans un bloc `static` ; station la plus proche ; rééquilibrage glouton |
| ☐ | `p04_tournament` — tournoi | passage par valeur, objets immuables en paramètre, autoboxing, cache `Integer` | `Tournament` | permutations par retour arrière, permutation suivante, rang, calendrier toutes rondes |
| ☐ | `p05_overload` — surcharges et JSON | résolution en 3 phases, `null`, type déclaré contre type réel | `OverloadLab` | prédire 30 appels ; un sérialiseur JSON qui contourne le choix à la compilation |
| ☐ | `p06_recursion` — laboratoire de récursivité | cas de base, pile d'appels, mémoïsation | `RecursionLab` | tri fusion, tri rapide, Hanoï, N reines, flood fill, rendu de monnaie |
| ☐ | `p07_shop` — **capstone** boutique | tout le chapitre | `app.ShopApp` | 4 paquets, surcharges `int`/`long`, copie défensive, meilleur panier par retour arrière |
