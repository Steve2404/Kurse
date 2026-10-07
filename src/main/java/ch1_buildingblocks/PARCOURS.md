# Chapitre 1 (Building Blocks) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. C'est le même fonctionnement qu'au chapitre 10 : des **projets** à construire de A à Z pour **comprendre**, et des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 5 | 7 |
| But | comprendre, concevoir, produire une sortie exacte | retrouver vite et sans aide |
| Durée | 1 à 3 h chacun | 10 à 20 min chacun |
| Combien de fois | une fois ; p05 refait 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : seulement le chapitre 1

On est au **premier** chapitre. Tu n'as donc droit qu'à ce qu'il enseigne :
- la structure d'une classe : champs, méthodes, commentaires, plusieurs classes dans un fichier ;
- `main` et ses arguments ;
- `javac`, `java`, `jar` ;
- les paquets et les imports ;
- les constructeurs, les blocs d'initialisation et l'ordre d'initialisation ;
- les 8 primitifs, les littéraux et les classes enveloppes ;
- les text blocks ;
- les variables, les identificateurs, `var`, `final`, la portée et les valeurs par défaut ;
- le ramasse-miettes.

**Ce qui est admis en plus**, car utilisé dans tous les exemples du chapitre 1 : les opérateurs **`+ - * / %`**, la concaténation de chaînes avec `+`, et l'affectation `=`.

**Ce qui est exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| `++`, `+=`, ternaire `?:`, casts `(int)`, règles de priorité et de promotion | 2 | `x = x + 1` ; affecter un `char` à un `int` pour voir son code |
| `if`, `switch`, boucles | 3 | aucun choix ni répétition : chaque projet est **un enchaînement exact** d'étapes ; c'est la précision (ordre d'initialisation, espaces des text blocks, littéraux) qui fait la difficulté |
| méthodes de `String` (`length`, `substring`…), `StringBuilder`, `String.format`, `Math`, tableaux créés par toi | 4 | concaténation, et calculs en centimes avec `/` et `%` |
| `this(...)` entre constructeurs, bloc `static { }`, héritage | 6 | un constructeur par usage |
| `record`, `enum`, `interface` | 7 | des classes ordinaires |
| lambdas, collections | 8, 9 | des variables et des objets |
| `try/catch`, `Locale` | 11 | les erreurs s'**observent** dans des « expériences » |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_receipt/TODO.md` en **aperçu Markdown** (icône « Preview » d'IntelliJ).
3. Suis la section 5, « Comment faire un projet ».

**L'ordre complet :**

```
p01 → r01 r03 r04
p02 → r02
p03 → r05 r06
p04 → (refais la partie script de r01)
p05 → r07 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch1_buildingblocks/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 5 projets, à cocher
│   └── p01_receipt/
│       ├── TODO.md          ← L'ÉNONCÉ : tu le lis
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Check.java       ← le correcteur : tu le LANCES, tu ne le modifies pas
│       ├── solution/        ← la correction : tu ne l'ouvres qu'à la fin
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       └── (tes fichiers)   ← TOUT le reste, c'est TOI qui le crées ici
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi des répétitions
    └── r01_main/
        ├── TODO.md          ← les défis + la carte mémoire repliée en bas
        ├── Check.java
        ├── solution/
        └── (ton RecallNN.java)
```

**Spécificités du chapitre 1 :**
- **Les arguments.** Certains `Check` lancent ton `main` **avec des arguments** (p01, p05, r01, r07). Ils sont écrits dans le `TODO.md` et en haut du `Check.java` (`ARGS`). Pour les essayer toi-même : Run → Edit Configurations → **Program arguments**.
- **Les sous-paquets.** Le projet **p04** et le capstone **p05** se répartissent en sous-paquets (`app`, `model`…). Crée les dossiers correspondants dans le dossier du projet. Le correcteur lit tous les sous-dossiers, sauf `solution/`.
- **Les scripts.** Le projet **p04** et le drill **r01** te demandent un script **`commandes.sh`** (`javac`, `java`, `jar`). `Check` l'exécute avec **bash** (Git Bash sous Windows), **depuis la racine du dépôt**. Écris tes dossiers de travail sous `build/`, qui est ignoré par git.

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

1. **Calcule la sortie à la main** d'abord : les montants, l'ordre du journal, les valeurs des littéraux.
2. **Crée la classe du `main` tout de suite**, même vide, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences.** Écris la ligne fautive, **lis l'erreur exacte** de `javac` ou de `java`, puis retire la ligne. Au chapitre 1, l'examen pose énormément de questions « est-ce que ça compile ? ».
5. **Réponds aux questions par écrit**, en commentaire dans ton code.
6. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | ton `main` a planté (souvent un argument manquant) | lis l'exception |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` caractère par caractère ; les espaces de tête s'affichent `·` |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit à quelle étape ils servent |
| `[FAIL] API : interdit ici …` | une notion d'un chapitre suivant | trouve une solution du chapitre 1 |
| `[FAIL] script …` | ton `commandes.sh` ne produit pas la bonne sortie | lance-le toi-même : `bash chemin/commandes.sh` depuis la racine |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` (Run → Edit Configurations → Program arguments) vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | relis l'étape, recalcule à la main, ouvre la Javadoc (`Integer`, `Character`…) |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la carte mémoire du drill du même thème, ou demande-moi un indice sur ce point précis |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md`, ferme, réécris de mémoire, et note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ;
- taper en dur une valeur que Java doit calculer (`127`, `0`, `null`…).

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

## 7. Comment savoir que le chapitre 1 est acquis

- [ ] Les 5 projets affichent `PROJET REUSSI` (et le script de p04 `SCRIPT REUSSI`). Toutes les questions ont une réponse écrite.
- [ ] Les 7 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r07 passe en moins de 20 minutes, sans carte.
- [ ] Tu sais écrire **de mémoire**, dans un terminal, les commandes `javac -d`, `java -cp`, `jar` et `java -jar` d'un projet en plusieurs paquets.
- [ ] p05 a été refait **depuis un dossier vide**, 2 à 3 semaines plus tard.
