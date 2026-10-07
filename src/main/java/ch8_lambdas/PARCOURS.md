# Chapitre 8 (Lambdas and Functional Interfaces) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres 1 à 7 et 10 :
- des **projets** à construire de A à Z, pour **comprendre** ;
- des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 7 | 9 |
| But | programmer **avec des fonctions** : les passer, les rendre, les composer | retrouver vite et sans aide |
| Durée | 2 à 4 h chacun | 10 à 12 min chacun |
| Combien de fois | une fois ; p05 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 8

**Tu as droit à :**
- les **chapitres 1 à 7** : tout le langage de base, les classes, l'héritage, les interfaces, les enums, les records, les types scellés, les classes imbriquées ;
- tout le **chapitre 8** :
  - **la syntaxe des lambdas** : paramètres sans type, typés ou `var`, `final` ; corps en expression ou en bloc ;
  - **les interfaces fonctionnelles** : une seule méthode abstraite, `@FunctionalInterface`, les méthodes d'`Object` qui ne comptent pas ;
  - **les quatre sortes de références de méthode**, plus les constructeurs et les constructeurs de tableau ;
  - **les interfaces du JDK** : `Supplier`, `Consumer`, `BiConsumer`, `Predicate`, `BiPredicate`, `Function`, `BiFunction`, `UnaryOperator`, `BinaryOperator` ;
  - **leurs combinaisons** : `andThen`, `compose`, `identity`, `and`, `or`, `negate`, `not`, `isEqual`, `minBy`, `maxBy` ;
  - **les versions primitives** : `IntPredicate`, `ToIntFunction`, `DoubleUnaryOperator`, `ObjIntConsumer`, `BooleanSupplier`… ;
  - **les variables dans les lambdas** : *effectively final*, les champs, `this`.

**Les génériques :** tu **utilises** ceux du JDK (`Function<String, Integer>`), mais tu n'en **déclares** pas (`interface X<T>` : chapitre 9). Pour un tableau de fonctions, déclare une interface non générique (`interface Rule extends Predicate<String> {}`), car `new Predicate<String>[n]` est interdit.

**Ce qui reste exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| `List`, `Map`, `Set`, `Collections`, `PriorityQueue` | 9 | des tableaux ; un tas binaire écrit à la main (p03) |
| `Comparable`, `Comparator` et ses `comparing` / `thenComparing` | 9 | ta propre interface `Order`, avec ses combinateurs (p06) |
| déclarer un type générique | 9 | une interface non générique qui étend une interface du JDK |
| streams, `Optional`, `IntStream` | 10 | des boucles qui appellent des fonctions |
| `try/catch`, `throw` | 11 | une valeur spéciale (`NaN`, `null`, `REJETEE`) |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

**Le formatage :** pas de `%f`. Arrondis avec `Math.round`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_pipeline/TODO.md` en aperçu Markdown.
3. Suis la section 5.

**L'ordre complet :**

```
p01 → r01 r02
p02 → r03 r04
p03 → r07
p04 → r05
p05 → r06
p06 → r08
p07 → r09 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch8_lambdas/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p01_pipeline/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction : à la fin seulement
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       └── (tes types)      ← Step.java, Memo.java, PipelineApp.java : c'est TOI qui les crées
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi
    └── r01_syntax/
        ├── TODO.md          ← les défis + la carte mémoire repliée
        ├── Check.java
        ├── solution/
        └── (ton Recall01.java)
```

**Spécificités du chapitre 8 :**
- **Les imports.** Toutes les interfaces du JDK sont dans `java.util.function`. Écris les imports à la main dans les drills : c'est un bon rappel des noms.
- **Lis une lambda par sa FORME.** Combien de paramètres, de quels types, et que rend-elle ? Cette forme désigne l'interface (voir la carte du drill r08).
- **Le type d'une lambda vient du contexte** : la variable, le paramètre, le retour ou le cast. Sans contexte (`var f = x -> x;`), pas de lambda.
- **Pour compter dans une lambda :** un **champ**, ou un tableau `int[] n = {0}`. Une variable locale `int` ne peut pas être modifiée.
- **Écris les deux formes.** Pour chaque référence de méthode, écris au moins une fois sa lambda équivalente, en commentaire.

---

## 5. Comment faire un projet

### 5.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, les types à créer, la règle du crescendo |
| **Tableau de bord** (étapes ☐) | les types et leurs membres, les **lignes exactes**, les **appels exacts** du `main`, les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact, au caractère près |

### 5.2 Travailler, étape par étape

1. **Sur papier**, pour chaque fonction : sa forme (entrée → sortie), l'interface choisie, et ce qu'elle capture.
2. **Crée la classe du `main` tout de suite**, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences** et **réponds aux questions par écrit**, en commentaire.
5. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | ton `main` a planté | `NullPointerException` : une fonction pas encore affectée (case de tableau `null`) |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` ; souvent un ordre `andThen` / `compose` inversé |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit où ils servent |
| `[FAIL] API : interdit ici …` | une collection, `Comparator`, un générique déclaré, un stream… | remplace-le (voir la section 2) |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | réécris la lambda en classe anonyme, pour voir ce qu'elle implémente ; ajoute des `println` temporaires |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou la partie concernée de `solution/`), ferme, réécris de mémoire, note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ou `Data.java` ;
- taper en dur un résultat que Java doit calculer.

### 5.5 Quand c'est réussi

1. Compare avec `solution/` : les choix d'interfaces et les références de méthode. Lis les commentaires.
2. Coche le projet dans `projects/README.md`, puis fais ses drills.

---

## 6. Comment faire un drill

1. Note l'heure. Le chrono cible est en haut du `TODO.md`.
2. Crée `RecallNN.java` dans le dossier du drill.
3. Fais les défis. La ligne attendue est sous chaque défi.
4. **Rien d'autre que ta mémoire.** Plus de 3 minutes bloqué : ✗, et défi suivant.
5. Lance `Check`.
6. **Après seulement :** ouvre la carte mémoire, relis tes ✗, puis fais les expériences.
7. Note date, temps et ✗ dans `drills/README.md`.
8. **Avant chaque répétition, supprime ton `RecallNN.java`.**

---

## 7. Comment savoir que le chapitre 8 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 9 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r09 passe en moins de 12 minutes, sans carte.
- [ ] Tu sais, sans hésiter :
  - dire si une lambda compile, rien qu'à sa syntaxe ;
  - dire si une interface est fonctionnelle ;
  - nommer l'interface du JDK et sa méthode pour n'importe quelle forme (dont les primitives) ;
  - donner l'ordre de `andThen` et de `compose` ;
  - classer une référence parmi les 4 sortes et écrire sa lambda ;
  - dire ce qu'une lambda peut lire et modifier, et ce que vaut `this` dedans.
- [ ] Tu sais écrire sans aide : un combinateur (`default … then(…)`), une fonction qui rend une fonction (currying, dérivée), un compilateur de règles texte en `Predicate`.
- [ ] p05 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.
