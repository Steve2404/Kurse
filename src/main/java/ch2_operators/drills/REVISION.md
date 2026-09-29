# Chapitre 2 (Operators) — parcours, drills et plan de révision

Les **exercices** (`ch2_operators/exercises`, 01 → 17) t'apprennent les notions.
Les **drills** (`ch2_operators/drills/exercises`, 01 → 05) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : le bulletin
de `drills/Grades.java` (des notes, un `byte` à 120, une lettre, des options en bits).
Lis ce fichier une fois et garde-le ouvert à côté.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi cet opérateur et quel piège il évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | Unaires et incréments | 01 → 02 → 03 | Drill01 (partie `++`) |
| 2 | Promotion et casts | 04 → 05 → 06 → 07 | Drill01, Drill02 |
| 3 | Relationnels, logiques, bits | 08 → 09 → 10 → 11 → 12 | Drill03 |
| 4 | Ternaire et précédence | 13 → 14 → 15 → 16 | Drill04 |
| 5 | Synthèse | 17 (capstone : le bulletin) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : réécrire la table de précédence et les règles de promotion.

**Conseil propre à ce chapitre :** pour chaque expression, écris le résultat **sur papier
avant** de lancer. C'est exactement ce que l'examen demande.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `ArithmeticAndPromotion` | `/` entière, `%` et son signe, promotion, `char`, débordement, `/ 0`, NaN, `x++ + ++x` | 13 |
| 02 `CastsAndCompound` | `(byte)`, `(short)`, `(int)` vers zéro, élargissement, cast caché de `+=` `*=` `/=` | 12 |
| 03 `LogicAndBits` | `&&` `\|\|` `!` `^`, court-circuit, masques `&` `\|` `^` `~`, `<<` `>>` `>>>` | 15 |
| 04 `TernaryAndPrecedence` | ternaire et son type, précédence, gauche à droite, parenthèses, `a = b = c` | 12 |
| 05 `MixedKata` | 13 questions sur le bulletin, **sans indiquer l'opérateur** | 13 |

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
- **Lecture de code** : les Javadoc des exercices 02, 07, 11 et 12 contiennent les
  **verdicts réels de `javac`** (types et messages d'erreur) : relis-les avant l'examen,
  puis fais les questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch2_operators/drills/exercises/Drill01_ArithmeticAndPromotion.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 11/13`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Arithmétique | | | | | | | |
| 02 Casts | | | | | | | |
| 03 Logique et bits | | | | | | | |
| 04 Ternaire et précédence | | | | | | | |
| 05 Kata mélangé | | | | | | | |
