# Chapitre 5 (Methods) — parcours, drills et plan de révision

Les **exercices** (`ch5_methods/exercises`, 01 → 18) t'apprennent les notions.
Les **drills** (`ch5_methods/drills/exercises`, 01 → 05) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : l'équipe de
`drills/Team.java` (4 noms, 4 scores, des bonus `Integer` dont certains `null`, une constante).
Lis ce fichier une fois et garde-le ouvert à côté.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | Déclarer une méthode, varargs | 01 → 02 | Drill01 |
| 2 | Modificateurs d'accès, encapsulation | 03 → 04 | refaire le tableau de l'exercice 03 sur page blanche |
| 3 | static (membres, imports, fabriques) | 05 → 06 → 07 → 08 | Drill02 (1re moitié) |
| 4 | final et effectivement final | 09 → 10 | Drill02 (2e moitié) |
| 5 | Passage par valeur, autoboxing | 11 → 12 → 13 → 14 → 15 | Drill03 |
| 6 | Surcharge | 16 → 17 | Drill04 |
| 7 | Synthèse | 18 (capstone : le tournoi) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : réécrire la recette d'une déclaration de méthode, le
tableau des 4 niveaux d'accès et les 3 tours du choix de surcharge.

**Conseil propre à ce chapitre :** pour chaque appel de méthode, pose-toi deux questions.
1) **Qu'est-ce qui est copié ?** Le nombre ou l'adresse. 2) **Quelle surcharge ?** Exacte,
élargissement, boxing, puis varargs. Ce sont les deux pièges favoris de l'examen.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `DeclarationsAndVarargs` | varargs (vide, un, plusieurs, tableau), paramètre obligatoire + varargs, `String.join`, `String[]` contre `Object...`, ordre des modificateurs | 12 |
| 02 `StaticAndFinal` | compteur static, constante, `import static`, fabrique static, appel static via `null`, `final` sur un tableau, lambdas et capture | 12 |
| 03 `PassByValueAndBoxing` | rendre ou modifier, réaffectation invisible, échange de cases, `null` dans les `Integer`, cache, `remove(int)` contre `remove(Object)`, `Long.equals` | 14 |
| 04 `Overloading` | faire choisir chaque surcharge : exacte, cast, boxing, `Object`, varargs, `short`, `char`, `float`, `Long`, `null` | 12 |
| 05 `MixedKata` | 12 questions sur l'équipe, **sans indiquer la forme** | 12 |

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
- **Lecture de code** : les Javadoc des exercices 01, 03, 06, 10, 15 et 17 contiennent les
  **verdicts réels de `javac`** (messages exacts : `illegal combination of modifiers`,
  `has private access`, `non-static method ... cannot be referenced from a static context`,
  `must be final or effectively final`, `reference to m is ambiguous`…) : relis-les avant
  l'examen, puis fais les questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch5_methods/drills/exercises/Drill01_DeclarationsAndVarargs.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 11/12`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Déclarations et varargs | | | | | | | |
| 02 static et final | | | | | | | |
| 03 Passage par valeur et boxing | | | | | | | |
| 04 Surcharge | | | | | | | |
| 05 Kata mélangé | | | | | | | |
