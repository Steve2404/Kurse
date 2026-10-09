# Kurse — Java OCP 17, en projets

Exercices pour préparer la certification **Oracle Certified Professional Java SE 17 Developer** (1Z0-829), chapitre par chapitre, d'après le livre *OCP Oracle Certified Professional Java SE 17 Developer Study Guide* (Selikoff, Boyarsky).

Chaque chapitre se travaille en deux temps :
- des **projets** : de vraies applications que tu construis de A à Z, pour **comprendre** ;
- des **drills** : des défis chronométrés, refaits à intervalles espacés, pour **retenir**.

## Les 15 chapitres

| Chap | Dossier | Sujet | Projets | Drills |
|---|---|---|---|---|
| 1 | `ch1_buildingblocks` | Building Blocks | 5 | 7 |
| 2 | `ch2_operators` | Operators | 5 | 7 |
| 3 | `ch3_makingdecisions` | Making Decisions | 5 | 7 |
| 4 | `ch4_coreapis` | Core APIs | 8 | 13 |
| 5 | `ch5_methods` | Methods | 7 | 9 |
| 6 | `ch6_classdesign` | Class Design | 7 | 9 |
| 7 | `ch7_beyondclasses` | Beyond Classes | 7 | 9 |
| 8 | `ch8_lambdas` | Lambdas and Functional Interfaces | 7 | 9 |
| 9 | `ch9_collections` | Collections and Generics | 7 | 10 |
| 10 | `ch10_streams` | Streams | 8 | 11 |
| 11 | `ch11_exceptions` | Exceptions and Localization | 7 | 10 |
| 12 | `ch12_modules` | Modules (JPMS) | 6 | 6 |
| 13 | `ch13_concurrency` | Concurrency | 7 + 1 bonus | 6 + 1 bonus |
| 14 | `ch14_io` | I/O | 7 | 6 + 1 bonus |
| 15 | `ch15_jdbc` | JDBC | 7 | 6 + 1 bonus |

## Après le livre : vers le niveau senior

Le livre prépare l'examen ; ces chapitres préparent au métier. Même format (projets, drills, `Check`, indices, corrigés, palais mental), sans crescendo à respecter du côté du livre : tout Java 17 est permis.

| Chap | Dossier | Sujet | Projets | Drills |
|---|---|---|---|---|
| 16 | `ch16_testing` | Tester et déboguer : JUnit 5, TDD, doublures, Mockito, le débogueur | 8 | 6 (dont 2 katas) |
| 17 | `ch17_algorithms` | Algorithmes et structures de données : complexité, tris, fenêtres, hachage, piles, retour arrière, arbres, tas, graphes, programmation dynamique | 11 | 7 (dont un entretien chronométré) |
| 18 | `ch18_design` | Conception : odeurs et refactoring de legacy, maître étalon, SOLID, patrons de création, de structure et de comportement | 9 | 6 |
| 19 | à venir | Le projet final | | |

À partir du chapitre 16, **tu écris aussi les tests**. `Check` les lance sur ton code, sur le code de référence, et sur des **mutants** (des copies du code avec un bug glissé exprès) : un bon jeu de tests les attrape tous. Au chapitre 17, les tests de référence vérifient aussi la **vitesse** : un algorithme juste mais trop lent échoue.

Les dossiers sont dans `src/main/java/`. Les modules du chapitre 12 vivent hors Maven, dans `ch12_modules/` à la racine. `ch15_jdbc-lab/` contient un Docker facultatif (PostgreSQL et MySQL) pour le drill bonus du chapitre 15.

## Par où commencer

1. Ouvre le dépôt dans IntelliJ (projet Maven, **Java 17**). Tu débutes ? Commence par `src/main/java/ch1_buildingblocks/projects/p00_bonjour`, qui montre chaque clic. Pour retenir à long terme : `PALAIS_MENTAL.md`.
2. Lis `src/main/java/<chapitre>/PARCOURS.md` en entier : c'est le mode d'emploi du chapitre.
3. Ouvre `projects/p01_…/TODO.md` en aperçu Markdown, et suis-le.

## Comment ça marche

```
src/main/java/chN_nom/
├── PARCOURS.md            ← le mode d'emploi du chapitre
├── projects/
│   ├── README.md          ← la liste des projets, à cocher
│   └── pNN_sujet/
│       ├── TODO.md        ← l'énoncé : étapes, appels exacts, sortie attendue
│       ├── Data.java      ← les données (à lire, pas à modifier)
│       ├── Check.java     ← le correcteur : tu le LANCES
│       └── solution/      ← la correction commentée, à ouvrir à la fin
└── drills/
    ├── README.md          ← quand faire quel drill, et le tableau de suivi
    └── rNN_theme/         ← TODO.md (défis + carte mémoire), Check.java, solution/
```

- **Tu crées toi-même tous les types** (classes, records, interfaces, `main`, et à partir du chapitre 16 les tests) dans le paquet du projet.
- **`Check`** lance ton programme, compare sa sortie ligne par ligne, puis vérifie que tu as utilisé l'API visée. Lance-le depuis la racine du dépôt. Avec l'argument `solution`, il vérifie la correction.
- **Le crescendo :** le chapitre N n'utilise que les chapitres 1 à N. `Check` refuse les notions des chapitres suivants (`[FAIL] API : interdit ici`).
- **La révision espacée :** chaque drill se refait à J0, J+1, J+3, J+7, J+14 et J+30. Avant chaque répétition, supprime ton `RecallNN.java`.

## Pour un agent IA

Lis `AI_EXERCISE_AUTHORING_PROMPT.md` : il décrit le format, les préférences de l'apprenant et la vérification obligatoire.
