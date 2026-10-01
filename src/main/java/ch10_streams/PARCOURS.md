# Chapitre 10 (Streams) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Il répond à cinq questions : par où commencer, comment le dossier est rangé, comment lire une consigne, comment travailler, et comment se comporter quand on bloque.

---

## 1. Ce que tu vas faire, en une phrase

Tu vas **construire 8 applications** (les projets) pour **comprendre** l'API des streams. Ensuite, tu **refais de mémoire, à intervalles espacés**, 12 petits drills chronométrés pour la **retenir**.

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| But | comprendre, concevoir, résoudre un problème | retrouver vite et sans aide |
| Durée | 2 à 6 h chacun, en plusieurs séances | 10 à 30 min chacun |
| Combien de fois | une fois, puis p07 et p08 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, réflexion, papier | **aucune** pendant le drill |

---

## 2. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_loandesk/TODO.md` en **aperçu Markdown** (dans IntelliJ, l'icône « Preview » en haut à droite de l'éditeur).
3. Suis la section 4 ci-dessous, « Comment faire un projet ».
4. Quand p01 affiche `PROJET REUSSI`, fais le drill `drills/r01_optional`, en suivant la section 5.

**L'ordre complet :**

```
p01 → r01
p02 → r02 r03 r04
p03 → r05
p04 → r07
p05 → r08 r09
p06 → r11
p07 → r12 (1er passage)
p08 → r06 r10 → r12 (test final)
```

Pendant tout ce temps, les **répétitions** des drills déjà faits passent **avant** le projet du jour (section 6).

---

## 3. La disposition des dossiers

```
ch10_streams/
├── PARCOURS.md              ← ce fichier (le mode d'emploi)
├── projects/
│   ├── README.md            ← la liste des 8 projets, à cocher
│   └── p01_loandesk/
│       ├── TODO.md          ← L'ÉNONCÉ : tu le lis
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES, tu ne le modifies pas
│       ├── solution/        ← la correction : tu ne l'ouvres qu'à la fin
│       └── (tes fichiers)   ← TOUT le reste, c'est TOI qui le crées ici
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi des répétitions
    ├── Data.java            ← les données communes aux 12 drills
    └── r01_optional/
        ├── TODO.md          ← les défis + la carte mémoire repliée en bas
        ├── Check.java
        ├── solution/
        └── (ton RecallNN.java)
```

**Où créer tes fichiers :**
- Dans le **même dossier** que le `TODO.md`, donc dans le même paquet, par exemple `package ch10_streams.projects.p01_loandesk;`.
- Clic droit sur le dossier → New → Java Class.
- Tu peux créer autant de fichiers que tu veux (un record par fichier, une interface…), ou tout mettre dans un seul fichier avec des types imbriqués. Le correcteur lit tous les `.java` du dossier, sauf `Data.java` et `Check.java`.
- **Un seul nom est imposé :** celui de la classe qui contient le `main`. Il est écrit en haut du `TODO.md` (`LoanDesk`, `BusNetwork`… ; pour les drills, `Recall01`, `Recall02`…).

---

## 4. Comment faire un projet

### 4.1 Lire le `TODO.md` (dans cet ordre)

| Partie du `TODO.md` | Ce que tu en fais |
|---|---|
| **En-tête** (API visée, donné, à créer) | Tu sais quelles méthodes tu vas devoir utiliser et quel nom donner au `main`. |
| **Le problème** | Tu comprends l'application dans son ensemble. Ne code rien encore. |
| **Tableau de bord** (les étapes ☐) | C'est ta feuille de route. **Chaque étape contient tout ce qu'il te faut :** la règle métier, puis les lignes exactes que ton programme doit afficher (dans un bloc gris), puis les **contraintes** (« une seule chaîne », « sans `if »`…), puis les **questions** (en gras ou en `>`). |
| **Checklist API** | Les méthodes que `Check` cherchera dans ton code, avec l'étape où elles ont leur place. |
| **Sortie attendue complète** | Le contrat exact, ligne par ligne. `Check` compare au caractère près. |

### 4.2 Travailler, étape par étape

1. **Calcule à la main** 2 ou 3 lignes de la sortie attendue, sur papier, à partir de `Data.java`. Si tu n'arrives pas à les calculer à la main, tu ne pourras pas les coder.
2. **Conçois sur papier** (étape 1 de chaque projet) : quels records, classes, interfaces ? Qui fait quoi ? C'est **toi** qui décides : l'énoncé donne des pistes, jamais la structure.
3. **Crée la classe du `main`** tout de suite, même vide. Cela te permet de lancer `Check` dès le début.
4. **Fais une étape à la fois.** Code-la, lance `Check`, corrige, puis coche ☐ → ☑ dans le `TODO.md`.
5. **Réponds aux questions de l'étape par écrit**, en commentaire dans ton code, au-dessus de l'endroit concerné. Ce sont exactement les pièges de l'examen OCP. Une étape dont tu n'as pas répondu aux questions n'est pas finie.

### 4.3 Lancer `Check` et lire sa réponse

Clic droit sur `Check.java` → **Run 'Check.main()'**. Voici ce qu'il affiche au tout début de p01 :

```
=== Verification de ch10_streams.projects.p01_loandesk.LoanDesk ===
[ERREUR] classe introuvable : ch10_streams.projects.p01_loandesk.LoanDesk (as-tu cree la classe avec ce nom et ce paquet ?)
[FAIL] sortie : 0/28 lignes justes avant la 1re difference (ligne 1)
       attendu : OK : Lea emprunte Dune (reste 2)
       obtenu  : (rien de plus)
[FAIL] API : encore a placer dans ton code : [Optional.ofNullable(, Optional.of(, ...]

*** Pas encore : la sortie differe. il manque des methodes de l'API. ***
```

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | ta classe `main` n'existe pas, ou elle a un mauvais nom ou un mauvais paquet | crée-la avec le nom exact |
| `[ERREUR] ton programme a lance …` | ton `main` a planté (exception) | lis l'exception, corrige |
| `[FAIL] sortie : 12/28 lignes justes … (ligne 13)` | les 12 premières lignes sont bonnes, la 13e diffère | compare `attendu` et `obtenu` **caractère par caractère** : espaces, virgule ou point, majuscules |
| `[FAIL] API : encore a placer …` | ces méthodes n'apparaissent pas encore dans ton code | normal tant que tu n'as pas fini ; la checklist te dit à quelle étape elles servent |
| `[FAIL] API : appel interdit …` | tu as utilisé une méthode interdite dans ce projet (par exemple `.get()`) | remplace-la |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 4.5 |

**Astuces :**
- Lance `Check` **souvent** : il te donne toujours la **première** ligne fausse, c'est donc ta prochaine tâche.
- Pour voir ta sortie brute, lance directement ta classe `main`.
- `Check` avec l'argument `solution` (Run → Edit Configurations → Program arguments) vérifie la solution. Cela sert à voir à quoi ressemble un projet réussi, sans lire la solution.

### 4.4 Quand tu bloques (la règle des 3 paliers)

| Palier | Combien de temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | relis l'étape, ses contraintes et ses questions ; recalcule à la main ; ouvre la **Javadoc** de la classe concernée |
| 2 | 20 min de plus | relis la **carte mémoire** du drill du même thème (bas du `TODO.md` dans `drills/`) ; ou demande-moi un **indice** sur ce point précis, sans la solution |
| 3 | en dernier recours | ouvre `solution/`, mais lis **uniquement** la méthode qui te bloque, puis **ferme**, et réécris-la de mémoire. Note `// AIDE : solution consultée` dans ton code : cette étape devra être refaite plus tard |

**Ce qu'il ne faut jamais faire :**
- copier-coller depuis `solution/` ;
- modifier `Data.java` ou `Check.java` pour faire passer le test ;
- sauter les questions des étapes ;
- coder sans avoir calculé la sortie à la main.

### 4.5 Quand c'est réussi

1. Ouvre `solution/` et **compare ta conception** : types, responsabilités, choix entre stream et boucle. Les commentaires de la solution expliquent **pourquoi**.
2. Si tu veux, montre-moi ton code : je relis ta **conception**, pas seulement le résultat.
3. Coche le projet dans `projects/README.md`.
4. Fais le ou les drills associés (voir l'ordre en section 2).

---

## 5. Comment faire un drill de rappel

1. Note l'heure de départ et le **chrono cible** (écrit en haut du `TODO.md` du drill).
2. Crée `RecallNN.java` dans le dossier du drill, avec ton record pour `Data.BOOKS` si le drill en a besoin.
3. **Fais les défis D01, D02…** Chaque défi donne sa consigne, et la ligne exacte à afficher est juste en dessous (`→ D01 : …`).
4. **Rien d'autre que ta mémoire :** ni carte mémoire, ni Javadoc, ni solution, ni tes projets. Si un défi bloque plus de 3 minutes, marque-le ✗ et passe au suivant.
5. Lance `Check`.
6. **Après seulement,** ouvre la **carte mémoire** (en bas du `TODO.md`, « Ouvrir la carte »). Relis ce qui concerne tes ✗ et termine ces défis.
7. Note la date, le temps et le nombre de ✗ dans le tableau de `drills/README.md`.
8. **Avant chaque répétition, supprime ton `RecallNN.java`.** On repart toujours d'un fichier vide.

Pourquoi ces règles ? La mémoire se renforce quand on **fait l'effort de retrouver**, pas quand on relit. Regarder la carte avant de chercher supprime précisément cet effort.

---

## 6. Le rythme

**Une séance type (1 h 30) :**

| Durée | Activité |
|---|---|
| 10 à 20 min | les **répétitions dues** aujourd'hui (voir le tableau de `drills/README.md`) : toujours en premier |
| 60 min | le projet en cours, une ou deux étapes |
| 10 à 20 min | si un projet vient d'être fini : le premier passage (J0) de son drill |

**La répétition espacée** (détails dans `drills/README.md`) :
- après le premier passage J0, refais le même drill à J+1, J+3, J+7, J+14 et J+30 ;
- un drill raté (plus de 2 ✗) recule d'un palier ;
- un drill parfait et rapide peut en sauter un.

**Bonus examen :** à partir de la répétition R3, fais les drills avec l'**autocomplétion désactivée**, ou dans un éditeur simple. À l'examen, tu n'as pas d'IDE.

---

## 7. Comment savoir que le chapitre est acquis

- [ ] Les 8 projets affichent `PROJET REUSSI`, et toutes les questions des `TODO.md` ont une réponse écrite.
- [ ] Les 12 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r12 (kata mixte) passe en moins de 25 minutes, sans carte.
- [ ] p07 et p08 ont été refaits **depuis un dossier vide** (déplace tes fichiers ailleurs), 2 à 3 semaines plus tard, sans regarder ton ancien code.

---

## 8. Ce que couvre le parcours

À eux deux, `projects/` et `drills/` pratiquent **toutes** les méthodes du chapitre 10. Chaque `Check` refuse ton code tant que les méthodes visées n'y apparaissent pas :
- `Optional` et ses versions primitives ;
- toutes les sources de streams ;
- la paresse et l'usage unique ;
- les opérations intermédiaires et terminales ;
- `Comparator` ;
- `IntStream`, `LongStream`, `DoubleStream` et la table des conversions ;
- les interfaces fonctionnelles primitives ;
- `reduce`, `collect`, `Collector.of` ;
- tous les `Collectors` ;
- le parallélisme ;
- `Spliterator`.

Les anciens exercices « remplir le corps » ont été retirés. Ils restent consultables dans l'historique git (commit `60ff6c1` et antérieurs).
