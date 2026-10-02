# Chapitre 2 (Operators) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. C'est le même fonctionnement qu'aux chapitres 1 et 10 : des **projets** à construire de A à Z pour **comprendre**, et des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

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

## 2. La règle du crescendo : chapitres 1 et 2 seulement

**Tu as droit à :**
- tout le **chapitre 1** : classes, `main` et arguments, paquets, constructeurs, primitifs, littéraux, classes enveloppes, text blocks, `var`, portée ;
- tout le **chapitre 2** :
  - les unaires `++ -- - ! ~` ;
  - l'arithmétique, la **promotion** et les **casts** ;
  - les affectations composées ;
  - les comparaisons et `instanceof` ;
  - `& | ^` sur des booléens et sur des entiers ;
  - `&&` et `||` ;
  - les **décalages** ;
  - le **ternaire** `? :` ;
  - les règles de **priorité**.

**Le grand changement :** le **ternaire** permet enfin de faire des **choix**, et les opérateurs permettent de vrais **calculs** (modulo, bits, arrondis). La difficulté vient de la précision des règles : promotion, troncature, court-circuit, priorité.

**Ce qui reste exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| `if`, `switch`, boucles, `instanceof` avec variable (pattern matching) | 3 | le **ternaire**, simple ou imbriqué ; pas de répétition : chaque cas est écrit |
| méthodes de `String`, `StringBuilder`, `String.format`, `Math`, tableaux créés par toi | 4 | la concaténation, les centimes, l'arrondi par cast `(int) (x * 10 + 0.5) / 10.0` |
| `this(...)`, bloc `static { }`, héritage | 6 | une classe simple |
| `record`, `enum`, `interface` | 7 | des classes et des constantes `static final` |
| lambdas, collections | 8, 9 | des variables et des objets |
| `try/catch`, `Locale` | 11 | les erreurs s'observent dans les « expériences » |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_parking/TODO.md` en **aperçu Markdown** (icône « Preview » d'IntelliJ).
3. Suis la section 5, « Comment faire un projet ».

**L'ordre complet :**

```
p01 → r01 r06
p02 → r05
p03 → r02 r03
p04 → r04
p05 → r07 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch2_operators/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 5 projets, à cocher
│   └── p01_parking/
│       ├── TODO.md          ← L'ÉNONCÉ : tu le lis
│       ├── Check.java       ← le correcteur : tu le LANCES, tu ne le modifies pas
│       ├── solution/        ← la correction : tu ne l'ouvres qu'à la fin
│       └── (tes fichiers)   ← TOUT le reste, c'est TOI qui le crées ici
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi des répétitions
    └── r01_unary/
        ├── TODO.md          ← les défis + la carte mémoire repliée en bas
        ├── Check.java
        ├── solution/
        └── (ton RecallNN.java)
```

**Spécificités du chapitre 2 :**
- **Les arguments.** Plusieurs `Check` lancent ton `main` avec des arguments (p01 à p05, r07). Ils sont écrits dans le `TODO.md` et en haut du `Check.java` (`ARGS`). Pour les essayer toi-même : Run → Edit Configurations → **Program arguments**.
- **Les données.** Le projet p03 reçoit un `Data.java` (le message lettre par lettre, car les méthodes de `String` arrivent au chapitre 4).
- **Calcule à la main.** Au chapitre 2, presque chaque ligne de sortie est un **piège de calcul** : promotion, troncature, débordement, ordre d'évaluation. **Pose le calcul sur papier avant d'exécuter**, sinon le projet ne t'apprend rien.

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

1. **Calcule la sortie à la main** d'abord : chaque division, chaque cast, chaque incrément, chaque court-circuit.
2. **Crée la classe du `main` tout de suite**, même vide, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences.** Écris la ligne fautive, **lis l'erreur exacte** de `javac` (perte de précision, type incompatible…), puis retire la ligne.
5. **Réponds aux questions par écrit**, en commentaire dans ton code.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | ton `main` a planté (souvent un argument manquant) | lis l'exception |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` caractère par caractère ; les espaces de tête s'affichent `·` |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit à quelle étape ils servent |
| `[FAIL] API : interdit ici …` | une notion d'un chapitre suivant | trouve une solution avec les chapitres 1 et 2 (un ternaire au lieu d'un if…) |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` (Run → Edit Configurations → Program arguments) vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | relis l'étape, recalcule à la main **en binaire si besoin**, relis la table des priorités |
| 2 | 20 min de plus | relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** sur ce point précis |
| 3 | en dernier recours | lis **uniquement** la partie concernée de `solution/`, ferme, réécris de mémoire, et note `// AIDE : solution consultée` |

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

## 7. Comment savoir que le chapitre 2 est acquis

- [ ] Les 5 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 7 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r07 passe en moins de 20 minutes, sans carte.
- [ ] Tu sais réciter la **table des priorités** et les **règles de promotion** sans regarder.
- [ ] p05 a été refait **depuis un dossier vide**, 2 à 3 semaines plus tard.
