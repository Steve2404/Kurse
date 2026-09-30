# Chapitre 8 (Lambdas and Functional Interfaces) — parcours, drills et plan de révision

Les **exercices** (`ch8_lambdas/exercises`, 01 → 20) t'apprennent les notions.
Les **drills** (`ch8_lambdas/drills/exercises`, 01 → 05) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : `drills/Words.java`
(6 mots, 5 nombres, un préfixe). Lis ce fichier une fois et garde-le ouvert à côté.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | Syntaxe et interfaces fonctionnelles | 01 → 02 → 03 | Drill01 |
| 2 | Références de méthode | 04 → 05 | Drill03 |
| 3 | Interfaces intégrées et leurs méthodes pratiques | 06 → 07 → 08 → 09 → 10 | Drill02 |
| 4 | Interfaces primitives | 11 → 12 | Drill04 (TODO 1 à 7) |
| 5 | Variables dans les lambdas | 13 → 14 | Drill04 (TODO 8 à 10) |
| 6 | Patrons avancés | 15 → 16 → 17 → 18 → 19 | refaire Drill02 et Drill03 |
| 7 | Synthèse | 20 (capstone : annuler / refaire) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : le tableau des 9 interfaces de base (entrées, sortie,
nom de la méthode), les 4 sortes de références de méthode et les formes permises des paramètres.

**Conseil propre à ce chapitre :** devant une lambda, demande-toi toujours **quelle est sa
cible** (l'interface fonctionnelle attendue). C'est elle qui décide du nombre de paramètres,
de leur type et de ce que le corps doit rendre.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `LambdaSyntax` | toutes les formes de paramètres (rien, un, deux, types, `var`), corps expression ou bloc, capture | 10 |
| 02 `BuiltInInterfaces` | `Supplier`, `Consumer.andThen`, `Predicate.and/or/negate/not`, `BiPredicate`, `Function.andThen/compose/identity`, `UnaryOperator`, `BinaryOperator`, `BiConsumer` | 13 |
| 03 `MethodReferences` | static, liée, non liée (1 ou 2 paramètres), constructeur, constructeur de tableau | 11 |
| 04 `PrimitivesAndScope` | `IntPredicate`, `IntUnaryOperator`, `IntBinaryOperator`, `ToIntFunction`, `IntSupplier`, `DoubleSupplier`, tableau d'une case, copie par tour | 10 |
| 05 `MixedKata` | 10 questions sur les mots, **sans indiquer la forme** | 10 |

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
- **Lecture de code** : les Javadoc des exercices 02, 03, 05 et 14 contiennent les
  **verdicts réels de `javac`** (`invalid lambda parameter declaration`,
  `bad return type in lambda expression`, `Unexpected @FunctionalInterface annotation`,
  `is not a functional interface`, `invalid method reference`,
  `variable x is already defined in method m()`…) : relis-les avant l'examen, puis fais les
  questions de révision du livre.
- Le chapitre 10 (streams) réutilise tout ce chapitre : chaque `filter`, `map`, `reduce` prend
  une de ces interfaces.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch8_lambdas/drills/exercises/Drill01_LambdaSyntax.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Syntaxe | | | | | | | |
| 02 Interfaces intégrées | | | | | | | |
| 03 Références de méthode | | | | | | | |
| 04 Primitives et portée | | | | | | | |
| 05 Kata mélangé | | | | | | | |
