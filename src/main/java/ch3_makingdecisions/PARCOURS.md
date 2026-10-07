# Chapitre 3 (Making Decisions) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. C'est le même fonctionnement qu'aux chapitres 1, 2 et 10 : des **projets** à construire de A à Z pour **comprendre**, et des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

> **Comment sont faits les énoncés.** Chaque étape d'un projet suit le même schéma :
> - **📖 La leçon** : la notion expliquée simplement, avec un exemple sur **un autre sujet** que le projet ;
> - **👉 À toi** : ce que tu construis ;
> - **🧪 Expériences** et **❓ Questions** : tu essaies, tu observes, tu réponds en commentaire.
>
> Les gestes de base sont expliqués une fois pour toutes dans le **projet 0 du chapitre 1** (`ch1_buildingblocks/projects/p00_bonjour`) : créer une classe, lancer, `Check`, arguments, terminal, lire une erreur. Relis-le si l'un d'eux te manque. Chaque projet rappelle aussi ses commandes exactes.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 5 | 7 |
| But | comprendre, concevoir, produire une sortie exacte | retrouver vite et sans aide |
| Durée | 1 à 3 h chacun | 12 à 20 min chacun |
| Combien de fois | une fois ; p05 refait 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 3

**Tu as droit à :**
- tout le **chapitre 1** : classes, `main` et arguments, primitifs, classes enveloppes, text blocks, variables ;
- tout le **chapitre 2** : tous les opérateurs, casts, ternaire ;
- tout le **chapitre 3** :
  - `if` / `else` ;
  - le **pattern matching** `instanceof Type variable` et la **portée de flux** ;
  - le **`switch`** instruction et expression : flèche, `yield`, plusieurs valeurs par `case` ;
  - **`while`**, **`do/while`**, **`for`**, **for-each** ;
  - **`break`**, **`continue`**, les **étiquettes**, `return`.

**Le grand changement :** avec les boucles, place aux **vrais algorithmes** : recherche, glouton, nombres premiers, simulation. La seule « collection » disponible est **`args`** (un `String[]` que Java te donne), et le **`switch` sur un `String`** remplace `equals`.

**Ce qui reste exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| méthodes de `String` (`equals`, `length`, `charAt`…), `StringBuilder`, `String.format`, `String.valueOf`, `Math`, **tableaux créés par toi** | 4 | `switch` sur un `String` ; concaténation dans une boucle ; calculs avec `/` et `%` ; pas de mémoire de tableau (formules, variables) |
| pattern dans un `case` (`case Integer i ->`) | *preview* en Java 17 | `if` / `else if` avec `instanceof` |
| `this(...)`, bloc `static { }`, héritage | 6 | une classe simple |
| `record`, `enum`, `interface` | 7 | des classes et des constantes `static final` |
| lambdas, collections | 8, 9 | `args` et des variables |
| `try/catch`, `Locale` | 11 | les erreurs s'observent dans les « expériences » |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_vending/TODO.md` en **aperçu Markdown** (icône « Preview » d'IntelliJ).
3. Suis la section 5, « Comment faire un projet ».

**L'ordre complet :**

```
p01 → r02 r03
p02 → r04 r06
p03 → r05
p04 → r01
p05 → r07 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch3_makingdecisions/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 5 projets, à cocher
│   └── p01_vending/
│       ├── TODO.md          ← L'ÉNONCÉ : tu le lis
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Check.java       ← le correcteur : tu le LANCES, tu ne le modifies pas
│       ├── solution/        ← la correction : tu ne l'ouvres qu'à la fin
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       └── (tes fichiers)   ← TOUT le reste, c'est TOI qui le crées ici
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi des répétitions
    └── r01_if/
        ├── TODO.md          ← les défis + la carte mémoire repliée en bas
        ├── Check.java
        ├── solution/
        └── (ton RecallNN.java)
```

**Spécificités du chapitre 3 :**
- **Les arguments.** La plupart des `Check` lancent ton `main` avec des arguments (lis `ARGS` en haut du `Check.java`). Pour les essayer toi-même : Run → Edit Configurations → **Program arguments**.
- **Le piège Windows :** le lanceur `java` remplace un argument `*` par la liste des fichiers du dossier. C'est pourquoi la calculatrice de p04 utilise `x` pour la multiplication.
- **Trace à la main.** Pour une boucle, écris sur papier un **tableau des variables à chaque tour** (« i | n | sum »). C'est la seule façon fiable de prédire un résultat, et c'est exactement ce que l'examen demande.

---

## 5. Comment faire un projet

### 5.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, le nom de la classe `main`, la règle du crescendo |
| **Le problème** | comprendre l'application ; ne rien coder encore |
| **Tableau de bord** (étapes ☐) | chaque étape contient tout ce qu'il faut : la règle, puis les **lignes exactes** à afficher, puis les **contraintes**, puis les **questions** et les **expériences** |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact, au caractère près, **espaces compris** |

### 5.2 Travailler, étape par étape

1. **Trace à la main** d'abord : le tableau des variables à chaque tour de boucle, et la branche prise à chaque `if` ou `switch`.
2. **Crée la classe du `main` tout de suite**, même vide, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences.** Écris la ligne fautive, **lis l'erreur exacte** de `javac` (code inaccessible, `case` en double, variable de pattern hors de portée…), puis retire la ligne.
5. **Réponds aux questions par écrit**, en commentaire dans ton code.
6. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | ton `main` a planté (souvent un argument manquant) | lis l'exception |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` caractère par caractère ; les espaces de tête s'affichent `·` |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit à quelle étape ils servent |
| `[FAIL] API : interdit ici …` | une notion d'un chapitre suivant | trouve une solution avec les chapitres 1 à 3 (un `switch` au lieu d'`equals`…) |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` (Run → Edit Configurations → Program arguments) vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | relis l'étape ; trace la boucle à la main tour par tour ; ajoute des `println` temporaires pour voir les variables |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** sur ce point précis |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou la partie concernée de `solution/`), ferme, réécris de mémoire, et note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ;
- taper en dur un résultat que Java doit calculer.

### 5.5 Quand c'est réussi

1. Compare ta conception avec `solution/` et lis ses commentaires.
2. Montre-moi ton code si tu veux une relecture.
3. Coche le projet dans `projects/README.md`, puis fais ses drills.

---

## 6. Comment faire un drill

1. Note l'heure. Le chrono cible est en haut du `TODO.md`.
2. Crée `RecallNN.java` dans le dossier du drill.
3. Fais les défis D01, D02… La ligne attendue est juste en dessous de chaque défi.
4. **Rien d'autre que ta mémoire.** Plus de 3 minutes bloqué : ✗, et défi suivant.
5. Lance `Check`.
6. **Après seulement :** ouvre la carte mémoire, relis tes ✗, termine-les, puis fais les expériences.
7. Note date, temps et ✗ dans `drills/README.md`.
8. **Avant chaque répétition, supprime ton `RecallNN.java`.**

---

## 7. Comment savoir que le chapitre 3 est acquis

- [ ] Les 5 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 7 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r07 passe en moins de 20 minutes, sans carte.
- [ ] Tu sais dire, sans hésiter, où saute un `continue` (dans un `for`, un `while`, un `do/while`), ce que fait `break` dans un `switch` placé dans une boucle, et où une variable de pattern est utilisable.
- [ ] p05 a été refait **depuis un dossier vide**, 2 à 3 semaines plus tard.
