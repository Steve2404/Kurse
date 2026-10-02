# Chapitre 4 (Core APIs) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres 1 à 3 et 10 :
- des **projets** à construire de A à Z, pour **comprendre** ;
- des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 6 | 10 |
| But | comprendre, concevoir, produire une sortie exacte | retrouver vite et sans aide |
| Durée | 2 à 4 h chacun | 12 à 20 min chacun |
| Combien de fois | une fois ; p06 refait 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 4

**Tu as droit à :**
- les **chapitres 1 à 3** : classes, `main`, primitifs, enveloppes, text blocks, opérateurs, `if`, `switch`, boucles, étiquettes, pattern matching ;
- tout le **chapitre 4** :
  - **`String`** : toute l'API, la concaténation, l'immutabilité, le **pool** et `intern` ;
  - **`StringBuilder`** : les trois constructeurs, le chaînage, la mutabilité ;
  - les **tableaux** : 1D, 2D, irréguliers, et la classe **`Arrays`** (`sort`, `binarySearch`, `equals`, `compare`, `mismatch`, `fill`, `copyOf`, `toString`, `deepToString`) ;
  - **`Math`** : `min`, `max`, `round`, `ceil`, `floor`, `pow`, `sqrt`, `abs`, `random` ;
  - les **dates** : `LocalDate`, `LocalTime`, `LocalDateTime`, `ZonedDateTime`, `Instant`, `Period`, `Duration`, `ChronoUnit`, le changement d'heure.

**Le grand changement :** les **tableaux** te donnent enfin une mémoire. Place aux algorithmes classiques :
- tri, recherche dichotomique, fusion ;
- grilles 2D et simulation ;
- fréquences et planning.

Tu peux écrire des méthodes `static` simples pour découper ton `main`. Le chapitre 5 les étudiera en détail.

**Ce qui reste exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| `this(...)`, bloc `static { }`, héritage (`extends`) | 6 | une classe simple avec des méthodes `static` |
| `record`, `enum` (les tiens), `interface` | 7 | des **tableaux parallèles** (un tableau par champ) et des constantes |
| lambdas, `::`, `Comparator` | 8 | des boucles et des comparaisons écrites à la main |
| `List`, `Map`, `Set`, `ArrayList` | 9 | des tableaux, redimensionnés avec `Arrays.copyOf` ou `System.arraycopy` |
| streams, `Optional`, `chars()`, `lines()` | 10 | `split`, `charAt` et des boucles |
| `try/catch`, `throw`, `Locale`, `DateTimeFormatter`, `NumberFormat` | 11 | les erreurs s'observent dans les « expériences » ; dates affichées avec `toString()` |
| `now()` | — | des dates **fixes**, sinon la sortie attendue changerait chaque jour |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

**Attention au formatage :** n'utilise **jamais `%f`**. Ta JVM est réglée en allemand, donc `%.2f` écrit `3,14` au lieu de `3.14`. Pour des montants, calcule en **centimes** (`long`) et affiche `euros + "." + centimes`, ou utilise `Math.round(x * 100) / 100.0`. `%s`, `%d` et `%-8s` sont sûrs.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_textstats/TODO.md` en **aperçu Markdown** (icône « Preview » d'IntelliJ).
3. Suis la section 5, « Comment faire un projet ».

**L'ordre complet :**

```
p01 → r01 r02
p02 → r03 r04
p03 → r05
p04 → r06 r07
p05 → r08 r09
p06 → r10 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch4_coreapis/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 6 projets, à cocher
│   └── p01_textstats/
│       ├── TODO.md          ← L'ÉNONCÉ : tu le lis
│       ├── Data.java        ← les données d'entrée (texte, tableaux) : tu les LIS, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES, tu ne le modifies pas
│       ├── solution/        ← la correction : tu ne l'ouvres qu'à la fin
│       └── (tes fichiers)   ← TOUT le reste, c'est TOI qui le crées ici
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi des répétitions
    └── r01_string/
        ├── TODO.md          ← les défis + la carte mémoire repliée en bas
        ├── Check.java
        ├── solution/
        └── (ton RecallNN.java)
```

**Spécificités du chapitre 4 :**
- **`Data.java`.** La plupart des projets lisent leurs données dans `Data.java` : `Data.TEXT`, `Data.ROOMS`… Ne recopie pas les valeurs dans ton code ; `Check` vérifie que tu passes par `Data`.
- **Prédis avant de lancer.** Le chapitre 4 est plein de valeurs « surprenantes » :
  - `round(-2.5)` vaut `-2` ;
  - `binarySearch` d'un absent est négatif ;
  - le 31 janvier plus un mois donne le 28 février ;
  - `==` sur deux `String`…
  
  Écris ta prédiction **en commentaire**, puis compare. C'est l'écart qui t'apprend.
- **Dessine les tableaux.** Pour un tableau 2D ou un `StringBuilder` modifié plusieurs fois, dessine les cases avec leurs indices sur papier, à chaque étape.
- **L'immutabilité.** `s.toUpperCase();` seul ne fait **rien** : il faut `s = s.toUpperCase();`. Même chose pour `LocalDate` : `date.plusDays(1);` seul est perdu. Par contre, `StringBuilder` est modifié sur place.

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

1. **Conçois d'abord sur papier** :
   - quels tableaux (et de quel type) ;
   - quelles méthodes `static` ;
   - quels indices.
2. **Crée la classe du `main` tout de suite**, même vide, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences.** Écris la ligne fautive, lis l'erreur exacte :
   - soit l'erreur de `javac` ;
   - soit l'exception à l'exécution (`StringIndexOutOfBoundsException`, `DateTimeException`…).
   
   Puis retire la ligne.
5. **Réponds aux questions par écrit**, en commentaire dans ton code.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | ton `main` a planté (souvent un indice hors limites) | lis l'exception et la ligne indiquée |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` caractère par caractère ; les espaces de tête s'affichent `·` |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit à quelle étape ils servent |
| `[FAIL] API : interdit ici …` | une notion d'un chapitre suivant | trouve une solution avec les chapitres 1 à 4 (un tableau au lieu d'une `List`…) |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` (Run → Edit Configurations → Program arguments) vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | relis l'étape ; dessine le tableau ou la chaîne avec ses indices ; ajoute des `println` temporaires |
| 2 | 20 min de plus | relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** sur ce point précis |
| 3 | en dernier recours | lis **uniquement** la partie concernée de `solution/`, ferme, réécris de mémoire, et note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ou `Data.java` ;
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

## 7. Comment savoir que le chapitre 4 est acquis

- [ ] Les 6 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 10 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r10 passe en moins de 20 minutes, sans carte.
- [ ] Tu sais dire, sans hésiter :
  - quelles méthodes **modifient** l'objet (`StringBuilder`) et lesquelles en rendent un **nouveau** (`String`, dates) ;
  - quand deux `String` sont `==` (le pool, les constantes de compilation) ;
  - ce que rendent `binarySearch`, `compare` et `mismatch` ;
  - le type de retour de `Math.round` ;
  - ce que fait `plusMonths` un 31 ;
  - la différence entre `Period` et `Duration`.
- [ ] p06 a été refait **depuis un dossier vide**, 2 à 3 semaines plus tard.
