# Chapitre 3 (Making Decisions) — parcours, drills et plan de révision

Les **exercices** (`ch3_makingdecisions/exercises`, 01 → 17) t'apprennent les notions.
Les **drills** (`ch3_makingdecisions/drills/exercises`, 01 → 05) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : la météo de la
semaine de `drills/Week.java` (un enum `Day`, 7 températures, des objets variés, une grille,
des commandes). Lis ce fichier une fois et garde-le ouvert à côté.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi cette structure de contrôle et quel piège elle évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | if/else et pattern matching | 01 → 02 → 03 → 04 | Drill01 |
| 2 | switch statement et expression | 05 → 06 → 07 → 08 → 09 | Drill02 |
| 3 | Boucles | 10 → 11 → 12 | Drill03 |
| 4 | break, continue, étiquettes, return | 13 → 14 → 15 → 16 | Drill04 |
| 5 | Synthèse | 17 (capstone : le robot) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : réécrire les types acceptés par `switch`, les règles
d'un switch expression et la portée d'une variable de pattern.

**Conseil propre à ce chapitre :** avant de lancer, **trace à la main** (valeur de chaque
variable à chaque tour, quelle branche du switch, où va le `break`). C'est ce que l'examen demande.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `IfAndPatterns` | `if` / `else if`, `==` sur enum, `instanceof String s`, `&&` après le pattern, sortie anticipée, `null instanceof` | 10 |
| 02 `SwitchForms` | enum sans `default`, `case A, B`, case empilés, `String`, `char`, `byte`, `yield`, fall-through, forme `:` avec `yield`, `throw`, `null` | 12 |
| 03 `Loops` | for-each, for avec index, `while`, `do/while`, 2D, à l'envers, `i += 2`, deux variables, `Day.values()` | 12 |
| 04 `BreakContinueLabels` | `break`, `continue`, `continue rows`, `break search`, `break` dans un switch dans une boucle, `return` | 10 |
| 05 `MixedKata` | 12 questions sur la semaine, **sans indiquer la structure** | 12 |

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
- **Mélange** : le Drill05 chaque semaine pendant la révision de l'examen.
- **Lecture de code** : les Javadoc des exercices 03, 06, 08, 12 et 16 contiennent les
  **verdicts réels de `javac`** (messages d'erreur exacts : `cannot find symbol`,
  `constant expression required`, `the switch expression does not cover all possible input values`,
  `undefined label`, `unreachable statement`…) : relis-les avant l'examen,
  puis fais les questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch3_makingdecisions/drills/exercises/Drill01_IfAndPatterns.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 if et patterns | | | | | | | |
| 02 Formes de switch | | | | | | | |
| 03 Boucles | | | | | | | |
| 04 break, continue, étiquettes | | | | | | | |
| 05 Kata mélangé | | | | | | | |
