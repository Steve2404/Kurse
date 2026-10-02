# Chapitre 11 (Exceptions and Localization) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres 1 à 10 :
- des **projets** à construire de A à Z, pour **comprendre** ;
- des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 7 | 10 |
| But | concevoir des **hiérarchies d'exceptions**, gérer les erreurs proprement, et produire une application **multilingue** | retrouver vite et sans aide |
| Durée | 2 à 4 h chacun | 10 à 12 min chacun |
| Combien de fois | une fois ; p03 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 11

**Tu as droit à :**
- les **chapitres 1 à 10** : le langage, les classes, les interfaces, les records, les lambdas, les collections, les génériques, les streams et `Optional` ;
- tout le **chapitre 11** :
  - **les exceptions :**
    - la hiérarchie `Throwable`, `Exception`, `RuntimeException`, `Error` ;
    - `throw` et `throws`, et les règles de redéfinition ;
    - `try` / `catch` / `finally`, le multi-catch ;
    - les exceptions personnalisées, le chaînage, la relance ;
    - **try-with-resources**, `AutoCloseable`, `Closeable`, les exceptions supprimées ;
  - **les formats :** `NumberFormat` (dont compact), `DecimalFormat`, `DateTimeFormatter`, `MessageFormat`, `String.format` avec une `Locale` ;
  - **la localisation :** `Locale` (constructeurs, `Builder`, catégories), `ResourceBundle` et les fichiers `.properties`, `Properties`.

**Ce qui reste exclu :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| `Thread`, `ExecutorService`, `synchronized`, `Atomic…`, streams parallèles | 13 | tout est séquentiel |
| lire ou écrire des fichiers (`Files`, `Path`, flux `java.io`, `Scanner`) | 14 | les données sont dans `Data.java`. Les bundles sont lus par `ResourceBundle` (ce chapitre) |
| JDBC | 15 | — |
| `System.exit`, `printStackTrace` | — | interdits : `Check` ne peut pas les vérifier. Teste-les dans une classe à part (expériences) |
| `now()` | — | des dates fixes, venues de `Data` |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

**Les locales :** ta machine peut être réglée en allemand. Les programmes commencent donc souvent par `Locale.setDefault(…)`, ou passent toujours une `Locale` explicite.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_bank/TODO.md` en aperçu Markdown.
3. Suis la section 5.

**L'ordre complet :**

```
p01 → r01 r05
p02 → r03 r02
p03 → r04
p04 → (révision r02)
p05 → r06 r07
p06 → r08
p07 → r09 r10 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch11_exceptions/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p01_bank/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction : à la fin seulement
│       └── (tes types)      ← BankException.java, Account.java, Teller.java... : c'est TOI qui les crées
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi
    └── r01_hierarchy/
        ├── TODO.md          ← les défis + la carte mémoire repliée
        ├── Check.java
        ├── solution/
        └── (ton Recall01.java)

src/main/resources/ch11_exceptions/   ← les fichiers .properties que TU crées (p07, r09)
```

**Spécificités du chapitre 11 :**
- **Pour chaque exception que tu écris, décide d'abord :** vérifiée (l'appelant **peut** réagir : saisie, service, ressource) ou non vérifiée (erreur de **programmation**) ?
- **Lis les erreurs de compilation.** Beaucoup de règles se voient au compilateur, et non à l'exécution :
  - `catch` inatteignable ;
  - exception vérifiée non déclarée ;
  - `throws` trop large dans une redéfinition.
  
  Les **expériences** des fiches les provoquent exprès.
- **Ne compare jamais un `toString()` d'exception à toi :** il contient le nom **complet** du paquet. Les fiches affichent `getClass().getSimpleName()` et `getMessage()`.
- **Les espaces invisibles.** Plusieurs locales séparent les milliers par U+00A0 ou U+202F. Les projets les remplacent par `_` (méthode `visible`), pour que la sortie se lise.
- **Les bundles :** le **nom de base** est `paquet.nom` (sans `_fr`, ni `.properties`). Écris les `.properties` en ASCII. Dans IntelliJ, vérifie l'encodage des fichiers properties (*Settings > Editor > File Encodings*).

---

## 5. Comment faire un projet

### 5.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, les algorithmes, les types à créer, la règle du crescendo |
| **Tableau de bord** (étapes ☐) | les types et leurs membres, les **messages exacts**, les **appels exacts** du `main`, l'**ordre des `catch`**, les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact, au caractère près |

### 5.2 Travailler, étape par étape

1. **Sur papier :** la hiérarchie de tes exceptions (qui étend qui), et pour chaque méthode, ce qu'elle déclare.
2. **Crée la classe du `main` tout de suite**, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences** et **réponds aux questions par écrit**, en commentaire.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | une exception est sortie de ton `main` | il manque un `catch`, ou il est au mauvais niveau |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | souvent un message d'exception, un ordre de `catch`, ou une locale oubliée |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit où ils servent |
| `[FAIL] API : interdit ici …` | un thread, un fichier, `System.exit`, `printStackTrace`… | remplace-le (voir la section 2) |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` vérifie la solution, pour voir à quoi ressemble un projet réussi. Lance `Check` depuis la racine du dépôt (`Kurse`).

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | affiche `getClass().getSimpleName()`, `getMessage()` et `getCause()` de l'exception, dans un `catch (Exception e)` temporaire |
| 2 | 20 min de plus | relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la partie concernée de `solution/`, ferme, réécris de mémoire, note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ou `Data.java` ;
- taper en dur un résultat que Java doit calculer.

### 5.5 Quand c'est réussi

1. Compare avec `solution/` : le choix vérifiée ou non vérifiée, l'ordre des `catch`, les causes. Lis les commentaires.
2. Coche le projet dans `projects/README.md`, puis fais ses drills.

---

## 6. Comment faire un drill

1. Note l'heure. Le chrono cible est en haut du `TODO.md`.
2. Crée `RecallNN.java` dans le dossier du drill (et, pour r09, les `.properties`).
3. Fais les défis. La ligne attendue est sous chaque défi.
4. **Rien d'autre que ta mémoire.** Plus de 3 minutes bloqué : ✗, et défi suivant.
5. Lance `Check`.
6. **Après seulement :** ouvre la carte mémoire, relis tes ✗, puis fais les expériences.
7. Note date, temps et ✗ dans `drills/README.md`.
8. **Avant chaque répétition, supprime ton `RecallNN.java`** (et les bundles de r09).

---

## 7. Comment savoir que le chapitre 11 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 10 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r10 passe en moins de 10 minutes, sans carte.
- [ ] Tu sais, sans hésiter :
  - classer n'importe quelle exception du JDK (vérifiée, non vérifiée ou erreur) ;
  - dire si une suite de `catch` compile, et ce qu'une redéfinition peut déclarer ;
  - prédire le chemin et la valeur rendue avec `finally` (même avec `return` dans `finally`) ;
  - donner l'ordre de fermeture d'un try-with-resources, et quelle exception est principale ou supprimée ;
  - écrire le résultat de `NumberFormat`, d'un motif `DecimalFormat` ou `DateTimeFormatter`, et de `MessageFormat` ;
  - donner le bundle choisi et la valeur d'une clé, pour une locale et une locale par défaut données.
- [ ] Tu sais écrire sans aide : une hiérarchie d'exceptions avec cause, un adaptateur de lambda qui enveloppe une exception vérifiée, des réessais avec exceptions supprimées, une application à trois bundles.
- [ ] p03 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.
