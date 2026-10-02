# Chapitre 4 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et la règle du crescendo) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données, s'il y en a ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Tous les autres fichiers, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_textstats` — analyseur de texte | presque toute l'API `String` : recherche, découpe, casse, nettoyage, `indent`, `stripIndent`, `translateEscapes`, formatage | `TextStats` | fréquences des mots **sans `Map`** (tri puis séries) ; toutes les positions d'un mot ; palindromes ; censure |
| ☐ | `p02_editor` — éditeur de texte | `StringBuilder` (`insert`, `delete`, `replace`, `reverse`, `setLength`…), pool de chaînes, `intern`, `==` contre `equals` | `Editor` | une pile d'annulation dans un `String[]` de taille fixe ; couper/coller avec contrôle des bornes |
| ☐ | `p03_life` — jeu de la vie | tableaux 2D `boolean[][]`, `Arrays.deepEquals`, `deepToString`, `equals` | `Life` | compter les voisins aux bords ; une nouvelle grille à chaque génération ; prouver la période 2 du clignotant |
| ☐ | `p04_scores` — statistiques d'une classe | `Arrays` (`sort`, `binarySearch`, `compare`, `mismatch`, `fill`, `copyOf`…), `Math`, tableau irrégulier | `Scores` | recherche dichotomique écrite à la main ; fusion de deux tableaux triés ; rotation par trois inversions ; médiane, écart type |
| ☐ | `p05_calendar` — planificateur | `LocalDate`, `LocalTime`, `ZonedDateTime`, `Instant`, `Period`, `Duration`, `ChronoUnit` | `Calendar` | un vol pendant le changement d'heure ; les pièges de `Period` ; les jours ouvrés ; la grille d'un mois |
| ☐ | `p06_hotel` — **capstone** hôtel | tout le chapitre | `Hotel` | tableaux parallèles ; prix majorés le week-end en centimes ; détection des chevauchements ; planning `int[][]` |
