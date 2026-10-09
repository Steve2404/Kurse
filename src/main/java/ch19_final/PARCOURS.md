# Chapitre 19 (Le projet final : une vraie application, de bout en bout) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer.

> **Comment sont faits les énoncés.** Chaque étape d'un projet suit le même schéma :
> - **📖 La leçon** : la notion expliquée simplement, avec un exemple ou une mesure ;
> - **👉 À toi** : ce que tu construis ;
> - **🧪 Expériences** et **❓ Questions** : tu essaies, tu mesures, tu réponds en commentaire.
>
> Les gestes de base sont expliqués une fois pour toutes dans le **projet 0 du chapitre 1** (`ch1_buildingblocks/projects/p00_bonjour`). Ce chapitre s'appuie sur **tous** les précédents : JDBC (15), les fils (13), les fichiers (14), les tests et Mockito (16), la complexité (17), la conception (18). Fais-les avant.

**Pourquoi ce chapitre :** savoir écrire des classes justes ne suffit pas pour tenir une application en production. Un développeur senior sait faire voyager des données (un format, une base qui évolue, une API HTTP), et sait faire tenir le programme quand tout va mal. Cela veut dire des requêtes simultanées, un service externe en panne, un client trop pressé, un rapport qui rame. Il sait **relire** le code des autres et **assembler** le tout en une architecture propre. Il sait aussi **mesurer** avant de corriger. Ici, tu construis chaque brique toi-même, sans bibliothèque web ni JSON, et tu les assembles dans le projet 8 : **l'atelier en ligne**, une application complète. Après ce chapitre, Spring Boot ne sera plus une boîte noire : tu sauras ce que chaque annotation fait à ta place.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 8 | 6 |
| But | une brique de l'application par projet, puis le capstone qui assemble tout | réécrire chaque brique de mémoire, proprement |
| Durée | 4 à 8 h chacun (le capstone : un week-end) | 25 à 35 min |
| Combien de fois | une fois ; p01, p03 et p08 refaits 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

Le fil rouge : **l'atelier de vélos**. Ses tâches (« Changer la chaîne », « Régler les freins »…) passent d'un projet à l'autre : exportées en JSON (p01), rangées en base (p02), servies par une API (p03), rappelées par SMS (p04), protégées des pannes (p05), résumées dans un rapport (p06), puis réunies dans l'application finale (p08). Le p07, lui, t'apprend à relire le code d'un collègue.

---

## 2. La règle du crescendo : tout Java, sans bibliothèque de plus

**Tu as droit à tout Java 17, JDBC et H2, JUnit et Mockito.** Pas de bibliothèque JSON, pas de Spring, pas de serveur web autre que celui du JDK (`com.sun.net.httpserver`). Chaque `TODO.md` dit ses interdits (pas d'expression régulière dans le parseur, pas de `double` pour l'argent, pas de `Thread.sleep` dans le code…) et une **longueur maximale** de méthode (15 ou 18 lignes). `Check` les vérifie. Comme aux chapitres 16 à 18 : pas de `System.out` ni de `Thread.sleep` dans les tests.

---

## 3. Comment `Check` vérifie

Comme au chapitre 18 : tes tests sur ton code, sur le code de référence, les tests de référence sur ton code, tes tests sur des **mutants**, et les règles de **forme**. Trois nouveautés :

- **des tests d'intégration** : une vraie base H2 (une base neuve par test, en mémoire), un vrai serveur HTTP (sur le port `0`), un vrai client (`java.net.http.HttpClient`) ;
- **des tests de concurrence déterministes** : des verrous (`CountDownLatch`) décident quand chaque fil avance ; jamais de `sleep` « pour laisser le temps » ;
- **des mutants de performance** : le mutant donne le **bon** résultat, mais en O(n²) ; seul un test qui chronomètre un gros volume peut le tuer (projet 6).

Certains `Check` prennent 20 à 40 secondes : chaque mutant relance tous tes tests, avec de vraies bases et de vrais serveurs.

**Les drills** sont vérifiés par les tests de référence seuls (tu n'écris que le code) : une ligne par défi, `d01 : 1 executions, 1 reussies`, plus les règles de forme.

Lance `Check` **depuis la racine du dépôt** (`Kurse`).

---

## 4. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_json/TODO.md`.
3. Suis la section 6.

**L'ordre complet :**

```
p01 → r01
p02 → r02
p03 → r03
p04 → r04
p05 → r05
p06 → p07 → r06
p08 (capstone) → r03 en 15 minutes
```

Les projets 3 et 8 **réutilisent** ton code des projets précédents (tu le copies dans leur paquet) : garde tes solutions propres.

---

## 5. La disposition des dossiers

```
ch19_final/
├── PARCOURS.md              ← ce fichier
├── PALAIS.md                ← ton palais mental pour ce chapitre (36 stations)
├── projects/
│   ├── README.md            ← la liste des 8 projets, à cocher
│   └── p01_json/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code
│       ├── Data.java        ← les données FOURNIES : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← le code, les tests de référence et CORRIGE.md : à la fin
│       └── (ton code)       ← tes classes et tes tests : c'est TOI qui les crées
└── drills/
    ├── README.md            ← quand faire quel drill, tableau de suivi J0 → R5
    └── r01_json/            ← TODO.md (défis, carte mémoire), Check.java, solution/
```

Les bases H2 dans un fichier et les fichiers des expériences vont dans `build/ch19/` (ignoré par Git) : supprime ce dossier quand tu veux repartir de zéro.

---

## 6. Comment faire un projet

### 6.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, ce qui est fourni, ce que tu réutilises, ce que tu crées, les interdits |
| **Tableau de bord** (étapes ☐) | la leçon, les types et les règles **exacts**, les tests à écrire, les questions, les expériences |
| **Checklist** | ce que `Check` cherchera |

### 6.2 Travailler, étape par étape

1. **Lis la leçon**, puis écris d'abord les **tests** de l'étape (ils disent ce que tu dois obtenir).
2. **Des petits pas, tous verts** : une méthode, ses tests, la suivante.
3. **Les expériences sont des mesures** : fais-les vraiment, et note le résultat. C'est elles qui apprennent le métier (une injection qui supprime une table, une file qui grandit, un rapport qui rame, une requête à 100 ms).
4. **Vérifie l'étape** dans la section correspondante de `solution/CORRIGE.md`.

### 6.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[FAIL] conception : … fait N lignes` | une méthode trop longue | extraire des méthodes (Ctrl+Alt+M), une par idée |
| `[FAIL] conception : X.java ne doit pas contenir : …` | une dépendance ou un geste interdit à cet endroit | relire la leçon de l'étape |
| `[FAIL] les tests de REFERENCE sur TON code` | ton code ne fait pas ce que dit l'énoncé | le nom du test qui échoue dit lequel |
| `les tests ne se terminent pas en 20 s` | une boucle infinie, un fil bloqué, un `join` sans délai | un verrou jamais ouvert ? un serveur jamais fermé ? |
| `mutant N : SURVIT` | une règle que tes tests ne regardent pas | palier 2 de `INDICES.md` |
| `*** PROJET REUSSI ***` | juste, testé, et bien rangé | compare avec la solution |

### 6.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | relis la leçon de l'étape ; dessine sur papier (les classes, les fils, les flèches de dépendance) |
| 2 | 20 min de plus | `INDICES.md` (indice 1, puis 2) ; ou demande-moi un **indice** |
| 3 | en dernier recours | la section de l'étape dans `solution/CORRIGE.md`, ferme, réécris |

---

## 7. Comment faire un drill

1. Note l'heure. Crée les fichiers du drill, de mémoire.
2. Lance `Check`.
3. **Après seulement :** la carte mémoire.
4. Note date, temps et ✗ dans `drills/README.md`. Avant chaque répétition, supprime tes fichiers.

---

## 8. Comment savoir que le chapitre 19 est acquis

- [ ] Les 8 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 6 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] Tu sais, sans hésiter :
  - écrire un parseur par descente récursive, avec des erreurs qui disent où et quoi, et une profondeur maximale ;
  - expliquer l'injection SQL et l'éviter, faire évoluer un schéma par migrations, écrire une transaction et un verrou optimiste ;
  - écrire un routeur HTTP, choisir le bon code de statut, et ne jamais montrer un bug au client ;
  - écrire un pool borné, un délai, des nouveaux essais avec recul, une opération idempotente et un arrêt propre ;
  - écrire un seau à jetons, un disjoncteur et des percentiles, et dire pourquoi la moyenne ment ;
  - mesurer avant d'optimiser, lire un profileur, écrire un test de vitesse ;
  - relire le code d'un collègue avec une liste de contrôle, et prouver chaque défaut par un test ;
  - dessiner l'architecture hexagonale de ton application, et dire ce qui la remplace dans Spring Boot.
- [ ] r03 passe en moins de 15 minutes après le capstone.
- [ ] p01, p03 et p08 ont été refaits **depuis un dossier vide**, 3 semaines plus tard.

---

## 9. 🏠 Ton palais mental (pour ne pas oublier dans 6 mois)

Le dernier circuit des plafonds : la **douche 1** (les données qui voyagent), la **douche 2** (le programme sous pression), et le **dessus de la terrasse** (le regard du senior), 12 stations chacun : [`PALAIS.md`](PALAIS.md).

- **Quand :** une fois le capstone réussi, pose les images (une pièce par jour), puis fais une balade le soir même.
- **À chaque répétition des drills** : la balade des trois plafonds **avant** le drill.
- **Chaque dimanche :** la grande balade, du salon jusqu'à la terrasse, puis tout le circuit des plafonds, du salon au ciel.
- Le palais range les règles et les pièges ; il ne remplace ni les projets ni les drills.

---

## 10. Et après ?

Tu as fini le parcours. Pour continuer à progresser comme un senior :

- **Spring Boot** : refais l'atelier en ligne avec Spring (le tableau de correspondance est à la fin du corrigé du projet 8). Tu verras chaque annotation remplacer une classe que tu connais.
- **Lire du code** : choisis un projet open source en Java (par exemple une bibliothèque que tu utilises), lis une classe par jour, et note ce que tu y apprends.
- **Relire et être relu** : chaque modification relue par un autre, avec la liste de contrôle du projet 7.
- **Mesurer** : garde le réflexe du projet 6. Avant d'optimiser, un chiffre ; après, un autre.
- **Garder la main** : les drills des chapitres 16 à 19, une fois par mois, et la grande balade du palais.
