# Chapitre 7 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et la règle du crescendo) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Tous les types, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_payments` — moyens de paiement | interfaces, `default`/`static`/`private`, héritage multiple d'interfaces, losange et `X.super` | `PaymentsApp` | Luhn, IBAN modulo 97 sans débordement, redéfinir une `default` |
| ☐ | `p02_poker` — poker | enums (champs, constructeur, corps par constante, interface), records (compact, `this(...)`) | `Poker` | évaluateur de mains, départage, Fisher-Yates, meilleure main de 5 parmi 7 |
| ☐ | `p03_turtle` — tortue graphique | `sealed` / `permits` / `non-sealed`, records vides et récursifs | `TurtleApp` | analyseur de `REPEAT [ … ]` imbriqués, interpréteur, dessin |
| ☐ | `p04_routes` — tournée de livraison | records en profondeur, interface avec `default`, record imbriqué | `RoutesApp` | Haversine, plus proche voisin, 2-opt, optimum par force brute |
| ☐ | `p05_tree` — arbre de recherche | les 4 classes imbriquées, interface imbriquée | `TreeApp` | suppression à 3 cas, parcours itératifs, ancêtre commun, rééquilibrage |
| ☐ | `p06_fleet` — flotte multimodale | polymorphisme, upcast et downcast, cast vers une interface, tableaux d'interfaces | `FleetApp` | Dijkstra selon les capacités de chaque véhicule |
| ☐ | `p07_sheet` — **capstone** tableur | tout le chapitre | `SheetApp` | formules, graphe de dépendances, tri topologique, cycles |
