# Chapitre 7 (Beyond Classes) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres 1 à 6 et 10 :

> **Comment sont faits les énoncés.** Chaque étape d'un projet suit le même schéma :
> - **📖 La leçon** : la notion expliquée simplement, avec un exemple sur **un autre sujet** que le projet ;
> - **👉 À toi** : ce que tu construis ;
> - **🧪 Expériences** et **❓ Questions** : tu essaies, tu observes, tu réponds en commentaire.
>
> Les gestes de base sont expliqués une fois pour toutes dans le **projet 0 du chapitre 1** (`ch1_buildingblocks/projects/p00_bonjour`) : créer une classe, lancer, `Check`, arguments, terminal, lire une erreur. Relis-le si l'un d'eux te manque. Chaque projet rappelle aussi ses commandes exactes.
- des **projets** à construire de A à Z, pour **comprendre** ;
- des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 7 | 9 |
| But | concevoir avec interfaces, enums, records, types scellés et classes imbriquées | retrouver vite et sans aide |
| Durée | 2 à 4 h chacun | 10 à 18 min chacun |
| Combien de fois | une fois ; p02 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 7

**Tu as droit à :**
- les **chapitres 1 à 6** : tout le langage de base, les API de base, les méthodes, les classes, l'héritage, les constructeurs, les classes abstraites, les objets immuables ;
- tout le **chapitre 7** :
  - **les interfaces** :
    - les modificateurs implicites ;
    - l'héritage multiple d'interfaces ;
    - les méthodes `default`, `static`, `private` et `private static` ;
    - le conflit en losange et `X.super.m()` ;
  - **les enums** :
    - `values`, `valueOf`, `ordinal`, `name`, `compareTo` ;
    - le `switch` sur un enum ;
    - les champs, constructeurs et méthodes ;
    - les méthodes abstraites avec un **corps par constante** ;
    - l'implémentation d'une interface ;
  - **les types scellés** : `sealed`, `permits`, `non-sealed`, `final`, `permits` omis dans un même fichier ;
  - **les records** :
    - les accesseurs, `equals`, `hashCode` et `toString` générés ;
    - les constructeurs compact, canonique et supplémentaire ;
    - les membres `static` ;
    - les records imbriqués et locaux ;
  - **les classes imbriquées** :
    - interne (`outer.new Inner()`, `Outer.this`) ;
    - `static` ;
    - locale ;
    - anonyme ;
    - et la règle *effectively final* ;
  - **le polymorphisme** : type de la référence ou type de l'objet, upcast, downcast, cast vers une interface, règles de compilation de `instanceof` et des casts.

**Le grand changement :** tu modélises avec des **contrats** (interfaces), des **valeurs** (records), des **ensembles fermés** (enums, types scellés), et tu caches les détails dans des **classes imbriquées**.

**Ce qui reste exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| lambdas, `::`, interfaces fonctionnelles du JDK | 8 | une **classe anonyme** qui implémente ta propre interface (`new Visitor() { … }`) |
| `List`, `Map`, `Set`, `Comparable`, `Comparator`, les génériques que tu déclares | 9 | des tableaux qui grandissent ; ta propre méthode `compareTo(Hand)` |
| streams, `Optional` | 10 | des boucles |
| `try/catch`, `throw` | 11 | tester avant d'agir (`instanceof` avant un cast) ; une valeur spéciale (`null`, `NaN`) |
| `switch` avec pattern (`case Circle c ->`) | preview en Java 17 | une chaîne de `if (x instanceof Circle c)` |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_payments/TODO.md` en aperçu Markdown.
3. Suis la section 5.

**L'ordre complet :**

```
p01 → r01 r02
p02 → r03 r04
p03 → r05
p04 → r06
p05 → r07
p06 → r08
p07 → r09 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch7_beyondclasses/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p02_poker/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction : à la fin seulement
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       └── (tes types)      ← Suit.java, Rank.java, Card.java… : un type public par fichier
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi
    └── r01_interfaces/
        ├── TODO.md          ← les défis + la carte mémoire repliée
        ├── Check.java
        ├── solution/
        └── (ton Recall01.java, avec ses petits types sous la classe publique)
```

**Spécificités du chapitre 7 :**
- **Un type public par fichier** dans les projets (interface, enum, record ou classe). Les types imbriqués restent **dans** leur englobante.
- **Les types scellés exigent le même paquet** : tous les sous-types autorisés sont dans le paquet du projet.
- **Les records** :
  - leurs accesseurs s'appellent `x()`, pas `getX()` ;
  - leur `toString` généré a la forme `Point[x=3, y=4]` : la sortie attendue en dépend ;
  - un composant tableau impose des **copies défensives**.
- **Pas de lambdas encore :** là où le chapitre 8 écrira `v -> …`, tu écris une **classe anonyme**. C'est plus long, et c'est exactement ce que la lambda remplacera.
- **Les expériences sont essentielles.** Ce chapitre est plein de règles jugées par `javac` : `X.super`, `permits`, `non-sealed`, constructeur compact, *effectively final*, casts sans lien.

---

## 5. Comment faire un projet

### 5.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, les types à créer, la règle du crescendo |
| **Tableau de bord** (étapes ☐) | chaque étape contient les types et leurs membres, les **lignes exactes**, les **appels exacts** du `main`, les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact, au caractère près |

### 5.2 Travailler, étape par étape

1. **Dessine d'abord** les types : interface, enum, record, `sealed`. Note qui implémente ou étend qui, et ce qui est imbriqué dans quoi.
2. **Crée la classe du `main` tout de suite**, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences** et **réponds aux questions par écrit**, en commentaire.
5. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | ton `main` a planté | `ClassCastException` : un cast sans `instanceof` ; `NullPointerException` : un `valueOf` ou un `of(...)` qui a rendu `null` |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` (attention au `toString` des records et des enums) |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit où ils servent |
| `[FAIL] API : interdit ici …` | une lambda, une collection, un générique, `Comparable`… | remplace-le (voir la section 2) |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | redessine les types ; relis la règle dans l'étape ; ajoute des `println` temporaires |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou la partie concernée de `solution/`), ferme, réécris de mémoire, note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ou `Data.java` ;
- taper en dur un résultat que Java doit calculer.

### 5.5 Quand c'est réussi

1. Compare tes types avec `solution/` et lis les commentaires.
2. Coche le projet dans `projects/README.md`, puis fais ses drills.

---

## 6. Comment faire un drill

1. Note l'heure. Le chrono cible est en haut du `TODO.md`.
2. Crée `RecallNN.java`, avec ses petits types sous la classe publique, dans le dossier du drill.
3. Fais les défis. La ligne attendue est sous chaque défi.
4. **Rien d'autre que ta mémoire.** Plus de 3 minutes bloqué : ✗, et défi suivant.
5. Lance `Check`.
6. **Après seulement :** ouvre la carte mémoire, relis tes ✗, puis fais les expériences.
7. Note date, temps et ✗ dans `drills/README.md`.
8. **Avant chaque répétition, supprime ton `RecallNN.java`.**

---

## 7. Comment savoir que le chapitre 7 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 9 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r09 passe en moins de 18 minutes, sans carte.
- [ ] Tu sais dire, sans hésiter :
  - les modificateurs implicites d'un membre d'interface ;
  - les 3 règles de conflit entre `default` ;
  - qui peut étendre un type `sealed` ;
  - ce qu'un record génère, et ce qui y est interdit ;
  - les 4 sortes de classes imbriquées et ce que chacune voit ;
  - quand un cast compile, et quand il échoue à l'exécution.
- [ ] Tu sais écrire sans aide :
  - un enum avec constructeur et corps par constante ;
  - un record avec constructeur compact ;
  - une interface scellée et ses records ;
  - une classe interne qui utilise `Outer.this` ;
  - une classe anonyme.
- [ ] p02 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.
