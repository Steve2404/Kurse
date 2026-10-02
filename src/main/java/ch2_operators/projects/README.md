# Chapitre 2 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et la règle du crescendo) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que `TODO.md` (l'énoncé), `Check.java` (le correcteur), parfois `Data.java`, et `solution/` (à n'ouvrir qu'à la fin). **Tous les autres fichiers, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_parking` — tarification d'un parking | ternaires imbriqués, `/` et `%`, `+=` / `-=`, `++` | `Parking` | toute une grille tarifaire **sans un seul `if`** |
| ☐ | `p02_permissions` — droits Unix en bits | `& \| ^ ~`, `<< >> >>>`, masques, octal | `Permissions` | `chmod` et `umask` simulés bit à bit ; priorité `&` contre `!=` |
| ☐ | `p03_cipherclock` — César, horloge, débordements | arithmétique de `char`, casts, modulo négatif, débordements | `CipherClock` | un `%` toujours positif et une division « plancher » sans `Math` |
| ☐ | `p04_sensors` — trames de capteurs | `&&` contre `&`, `^`, effets de bord, `x++ + ++x`, `==` sur des références, `instanceof` | `Sensors` | prouver le court-circuit en comptant les contrôles |
| ☐ | `p05_reportcard` — **capstone** bulletin | promotion, portée d'un cast, arrondi, bits, ternaires, priorité `&&` / `\|\|` | `ReportCard` | peu d'indices ; le piège `(double) (a / b)` |
