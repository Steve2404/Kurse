# Chapitre 4 (Core APIs) — parcours, drills et plan de révision

Les **exercices** (`ch4_coreapis/exercises`, 01 → 32) t'apprennent les API et l'algorithmique.
Les **drills** (`ch4_coreapis/drills/exercises`, 01 → 08) te font répéter chaque méthode jusqu'à ce
qu'elle sorte toute seule. Tous les drills utilisent les mêmes données : le journal de bord de
`drills/Journal.java` (une ligne de journal, un titre, des mots, des scores, des dates et des heures).
Lis ce fichier une fois et garde-le ouvert à côté.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi cette méthode d'API et quel piège elle évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser les méthodes).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | String | 01 → 02 → 03 → 04 → 05 → 06 | Drill01 |
| 2 | StringBuilder | 07 → 08 → 09 → 10 | Drill02 |
| 3 | Tableaux et `Arrays` | 11 → 12 → 13 → 14 → 15 → 16 | Drill03 |
| 4 | `Math` | 17 → 18 → 19 → 20 | Drill04 |
| 5 | Dates et heures (`java.time`), dont `Instant` | 21 → 22 → 23 → 24 → 25 → 26 → 27 | Drill05, puis Drill06 (méthodes qu'on oublie) |
| 6 | Algorithmique avec les Core APIs | 28 → 29 → 30 → 31 | Drill07 (schémas d'algorithme) |
| 7 | Synthèse | 32 (capstone : le journal de bord) | Drill08 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : écrire de mémoire toutes les méthodes d'une classe
(String, StringBuilder, Arrays, Math, LocalDate…) **avec leur type de retour**.

**Conseil propre à ce chapitre :** pour chaque méthode, retiens trois choses : ce qu'elle rend
(type), si elle **modifie** l'objet (StringBuilder, `Arrays.sort`, `Arrays.fill`) ou en rend un
**nouveau** (String, java.time), et ce qui se passe aux bornes (fin EXCLUE, -1, exception).

| Drill | Contenu | TODO |
|---|---|---|
| 01 `StringApi` | `length`, `charAt`, `substring`, `indexOf`, `lastIndexOf`, `contains`, `startsWith`, `toUpperCase`, `equalsIgnoreCase`, `repeat`, `replace`, `strip`, `isBlank`, `join`, `split`, `compareTo`, `'a' + 1 + ""` | 19 |
| 02 `StringBuilderApi` | `append`, `insert`, `delete`, `deleteCharAt`, `replace`, `reverse`, `setCharAt`, `setLength`, `indexOf`, `substring`, `contentEquals`, chaînage | 13 |
| 03 `ArraysApi` | `toString`, `copyOf`, `copyOfRange`, `sort`, `binarySearch`, `fill`, `equals`, `compare`, `mismatch`, `deepToString`, `asList`, tableaux en escalier, valeurs par défaut | 14 |
| 04 `MathApi` | `round` (long ou int), `ceil`, `floor`, `pow`, `sqrt`, `max` mixte, `min`, `abs(MIN_VALUE)`, `random`, `addExact` | 13 |
| 05 `DateTimeApi` | `of`, `plusMonths`, `getDayOfWeek`, `isLeapYear`, `withMonth`, `atTime`, `Duration`, `Period`, `ChronoUnit`, `parse`, `truncatedTo`, `ZonedDateTime` et heure d'été | 18 |
| 06 `ApiExtras` | `indent`, `stripIndent`, `translateEscapes`, pool (`trim`, `toUpperCase`, `intern`), `chars`, `Arrays.compare`/`mismatch` avec null, `Instant` (`parse`, `ofEpochSecond`, `atZone`, `plus`, `until`, `toInstant`) | 18 |
| 07 `AlgorithmPatterns` | swap, deux pointeurs, meilleur, `int[26]`, dichotomie, fenêtre glissante, sommes préfixes, Euclide, premier, chiffres, FizzBuzz, insertion triée, distincts | 14 |
| 08 `MixedKata` | 12 questions sur le journal, **sans indiquer la méthode** | 12 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris de mémoire**.
   Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` jusqu'à 100 %.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois.
- **Mélange** : le Drill08 chaque semaine pendant la révision de l'examen.
- **Algorithmique « au bout des doigts »** : refais le Drill07 **chaque jour** jusqu'à le finir en
  moins de 20 minutes sans la carte, puis passe au calendrier normal. Ensuite, reprends un exercice
  28 à 31 **sur une page blanche** (sans relire l'énoncé détaillé, seulement le titre du TODO).
- **Lecture de code** : les Javadoc des exercices 04, 05, 09, 15, 19, 23, 25, 26, 27 et 31 commencent par
  des « Rappels vérifiés » (résultats et messages d'erreur réels de Java 17) : relis-les avant
  l'examen, puis fais les questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch4_coreapis/drills/exercises/Drill01_StringApi.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 9 min – 17/19`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 String | | | | | | | |
| 02 StringBuilder | | | | | | | |
| 03 Arrays | | | | | | | |
| 04 Math | | | | | | | |
| 05 java.time | | | | | | | |
| 06 Méthodes qu'on oublie | | | | | | | |
| 07 Schémas d'algorithme | | | | | | | |
| 08 Kata mélangé | | | | | | | |
