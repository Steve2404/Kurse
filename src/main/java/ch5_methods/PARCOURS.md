# Chapitre 5 (Methods) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres 1 à 4 et 10 :

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
| But | comprendre, concevoir, produire une sortie exacte | retrouver vite et sans aide |
| Durée | 2 à 4 h chacun | 12 à 15 min chacun |
| Combien de fois | une fois ; p06 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 5

**Tu as droit à :**
- les **chapitres 1 à 4** : bases du langage, opérateurs, décisions et boucles, `String`, `StringBuilder`, tableaux, `Arrays`, `Math`, dates ;
- tout le **chapitre 5** :
  - **concevoir une méthode** : accès, modificateurs optionnels (`static`, `final`), type de retour, nom, paramètres, corps, `return` ;
  - **variables** locales et d'instance, `final`, `var` ;
  - **varargs** ;
  - les **quatre niveaux d'accès** dans des **paquets** que tu crées ;
  - **`static`** : champs, méthodes, blocs `static { }`, import static, ordre d'initialisation ;
  - le **passage par valeur** ;
  - l'**autoboxing** et l'**unboxing** ;
  - la **surcharge** et sa résolution ;
  - la **récursivité**.

**Le grand changement :** tu **découpes** enfin ton programme en méthodes et en classes réparties dans des paquets, avec le niveau d'accès juste. Et la récursivité ouvre les algorithmes « diviser pour régner » et le retour arrière : tri fusion, N reines, rendu de monnaie…

**Ce qui reste exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| écrire un **constructeur**, `this(...)`, `super` | 6 | le constructeur par défaut (`new X()`) puis une **fabrique `static`** qui remplit les champs, ou un bloc `{ }` d'instance |
| héritage (`extends`), `abstract`, `@Override` | 6 | une classe simple. **Exception :** `extends` est permis dans p02 et r03, seulement pour étudier `protected` |
| `record`, `enum` (les tiens), `interface` | 7 | des classes et des constantes `static final` |
| lambdas, `::`, `Comparator` | 8 | des boucles et des comparaisons écrites à la main |
| `List`, `Map`, `Set` | 9 | des tableaux qui doublent de taille (`Arrays.copyOf`) |
| streams, `Optional` | 10 | des boucles |
| `try/catch`, `throw`, `Locale`, `DateTimeFormatter` | 11 | les erreurs s'observent dans les « expériences » |

`Check` refuse ces notions, y compris un constructeur que tu aurais écrit. Le message est `[FAIL] API : interdit ici`.

**Le formatage :** pas de `%f` (ta JVM est en allemand et écrirait `3,14`). Utilise des centimes en `long`, ou `Math.round(x * 100) / 100.0`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_stats/TODO.md` en aperçu Markdown.
3. Suis la section 5.

**L'ordre complet :**

```
p01 → r01 r02
p02 → r03
p03 → r04
p04 → r05 r06
p05 → r07
p06 → r08
p07 → r09 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch5_methods/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p02_bank/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction (avec ses sous-paquets) : à la fin seulement
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       ├── core/            ← TES sous-paquets, que TU crées…
│       ├── premium/
│       └── app/BankApp.java ← … dont celui du main
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi
    └── r01_declare/
        ├── TODO.md          ← les défis + la carte mémoire repliée
        ├── Check.java
        ├── solution/
        └── (ton RecallNN.java)
```

**Spécificités du chapitre 5 :**
- **Les paquets.** Dans p02, p07 et r03, tu crées des **sous-dossiers** (`core/`, `app/`…). La ligne `package` de chaque fichier doit correspondre à son dossier, par exemple `package ch5_methods.projects.p02_bank.core;`. `Check` lance alors `app.BankApp`.
- **Les expériences sont au cœur du chapitre.** Les règles d'accès, de surcharge et d'initialisation se jugent par `javac`. Écris la ligne interdite, lis l'erreur **exacte**, puis retire-la.
- **Dessine la mémoire.** Pour le passage par valeur, dessine deux colonnes (variables de l'appelant / de la méthode) et des flèches vers les objets. Pour la récursivité, dessine la **pile d'appels**.
- **Prédis les surcharges.** Avant chaque appel surchargé, écris en commentaire la version que tu attends et la phase (1, 2 ou 3).

---

## 5. Comment faire un projet

### 5.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, les classes à créer (et leurs paquets), la règle du crescendo |
| **Tableau de bord** (étapes ☐) | chaque étape contient la règle, puis les **lignes exactes** à afficher, puis les **signatures** imposées, les **questions** et les **expériences** |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact, au caractère près |

### 5.2 Travailler, étape par étape

1. **Conçois d'abord sur papier :**
   - les classes et leur paquet ;
   - pour chaque membre, son niveau d'accès et s'il est `static` ;
   - les signatures des méthodes.
2. **Crée la classe du `main` tout de suite**, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences** et **réponds aux questions par écrit**, en commentaire.
5. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le dossier (`app.BankApp` = dossier `app/`) |
| `[ERREUR] ton programme a lance …` | ton `main` a planté | lis l'exception (`StackOverflowError` : un cas de base manque) |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` ; les espaces de tête s'affichent `·` |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit à quelle étape ils servent |
| `[FAIL] API : interdit ici …` | une notion d'un chapitre suivant, ou un constructeur | remplace-le par une fabrique `static` |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | relis l'étape ; dessine la pile d'appels ou les références ; ajoute des `println` temporaires |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou la partie concernée de `solution/`), ferme, réécris de mémoire, note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ou `Data.java` ;
- taper en dur un résultat que Java doit calculer.

### 5.5 Quand c'est réussi

1. Compare ta conception avec `solution/` : les niveaux d'accès, ce qui est `static`, les signatures. Lis les commentaires.
2. Coche le projet dans `projects/README.md`, puis fais ses drills.

---

## 6. Comment faire un drill

1. Note l'heure. Le chrono cible est en haut du `TODO.md`.
2. Crée `RecallNN.java` (et les autres classes demandées) dans le dossier du drill.
3. Fais les défis D01, D02… La ligne attendue est sous chaque défi.
4. **Rien d'autre que ta mémoire.** Plus de 3 minutes bloqué : ✗, et défi suivant.
5. Lance `Check`.
6. **Après seulement :** ouvre la carte mémoire, relis tes ✗, puis fais les expériences.
7. Note date, temps et ✗ dans `drills/README.md`.
8. **Avant chaque répétition, supprime tes fichiers** du drill (pas `Check.java`, `TODO.md` ni `solution/`).

---

## 7. Comment savoir que le chapitre 5 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 9 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r09 passe en moins de 15 minutes, sans carte.
- [ ] Tu sais dire, sans hésiter :
  - qui voit un membre `protected` depuis un autre paquet, et par quelle référence ;
  - l'ordre d'initialisation d'une classe et d'un objet ;
  - pourquoi `swap(int, int)` ne fait rien ;
  - quand `==` est vrai entre deux `Integer` ;
  - la version choisie par `f(5)` entre `long`, `Integer` et `int...`, et pourquoi.
- [ ] Tu sais écrire sans aide : un tri fusion, une exponentiation rapide, un retour arrière (permutations, N reines), un remplissage de zone, une mémoïsation.
- [ ] p06 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.

---

## 8. 🏠 Ton palais mental (pour ne pas oublier dans 6 mois)

Les règles et les pièges de ce chapitre sont rangés dans **la chambre 1**, stations 1 à 6 : [`PALAIS.md`](PALAIS.md).

- **Quand :** une fois le capstone réussi, pose les images (15 minutes), puis fais une balade le soir même.
- **À chaque répétition des drills** (J+1, J+3, J+7, J+14, J+30) : la balade de la pièce **avant** le drill (2 minutes, à voix haute, sans regarder, puis vérifie).
- **Chaque dimanche :** la grande balade, du salon jusqu'à la dernière pièce installée.
- Le palais range les règles ; il ne remplace ni les projets ni les drills.
