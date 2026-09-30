# Chapitre 7 (Beyond Classes) — parcours, drills et plan de révision

Les **exercices** (`ch7_beyondclasses/exercises`, 01 → 24) t'apprennent les notions.
Les **drills** (`ch7_beyondclasses/drills/exercises`, 01 → 05) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Les données partagées sont le catalogue de romans de
`drills/Catalog.java` (titres, années, pages, formats). Chaque drill contient aussi ses petits
types à compléter (interfaces, enums, records, classes imbriquées).

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | Interfaces (abstract, default, static, private) | 01 → 02 → 03 → 04 → 05 | Drill01 |
| 2 | Enums | 06 → 07 → 08 | Drill02 |
| 3 | Types sealed | 09 → 10 → 11 → 12 | Drill03 (TODO 7 et 8) |
| 4 | Records | 13 → 14 → 15 → 16 | Drill03 |
| 5 | Classes imbriquées (interne, static, locale, anonyme) | 17 → 18 → 19 → 20 → 21 → 22 | Drill04 |
| 6 | Polymorphisme et casts | 23 | refaire Drill03 et Drill04 |
| 7 | Synthèse | 24 (capstone : la boutique) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : les 5 sortes de membres d'interface avec leurs modificateurs
implicites, les 3 choix d'un enfant de `sealed`, ce que javac génère pour un record, et les
4 sortes de classes imbriquées.

**Conseil propre à ce chapitre :** pour chaque type, pose-toi la question de ce que le
compilateur ajoute **en cachette** (modificateurs implicites d'interface, `private` du
constructeur d'enum, `final` des records, `this` englobant d'une classe interne). La moitié
des pièges de l'examen vient de là.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `Interfaces` | implémenter en `public`, `default` qui appelle l'abstract, `private`, `static` fabrique, diamant `X.super`, constante, lambda, anonyme | 10 |
| 02 `Enums` | constructeur, méthode par constante, `values`, `valueOf`, `ordinal`, `compareTo`, switch exhaustif, `EnumMap` | 10 |
| 03 `SealedAndRecords` | constructeur compact, constructeur non canonique, méthodes, « wither », `static`, `instanceof` sur un sealed, `equals` et `toString` générés | 10 |
| 04 `NestedClasses` | `static` dans une interne, `Outer.this`, `outer.new Inner()`, `new Outer.Nested()`, classe locale, comparateur anonyme, anonyme avec état | 10 |
| 05 `MixedKata` | une médiathèque complète, **sans indiquer la forme** | 10 |

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
- **Lecture de code** : les Javadoc des exercices 04, 08, 11, 15 et 21 contiennent les
  **verdicts réels de `javac`** (`missing method body, or declare abstract`,
  `modifier protected not allowed here`, `types A and B are incompatible`,
  `enum types may not be instantiated`, `sealed, non-sealed or final modifiers expected`,
  `field declaration must be static`, `invalid accessor method in record R`,
  `non-static variable this cannot be referenced from a static context`…) : relis-les avant
  l'examen, puis fais les questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch7_beyondclasses/drills/exercises/Drill01_Interfaces.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 12 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Interfaces | | | | | | | |
| 02 Enums | | | | | | | |
| 03 Sealed et records | | | | | | | |
| 04 Classes imbriquées | | | | | | | |
| 05 Kata mélangé | | | | | | | |
