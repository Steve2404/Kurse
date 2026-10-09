# Chapitre 18 (Conception : SOLID, patrons, refactoring) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer.

> **Comment sont faits les énoncés.** Chaque étape d'un projet suit le même schéma :
> - **📖 La leçon** : la notion expliquée simplement, avec un exemple sur **un autre sujet** que le projet ;
> - **👉 À toi** : ce que tu construis ;
> - **🧪 Expériences** et **❓ Questions** : tu essaies, tu mesures, tu réponds en commentaire.
>
> Les gestes de base sont expliqués une fois pour toutes dans le **projet 0 du chapitre 1** (`ch1_buildingblocks/projects/p00_bonjour`). Les tests, Mockito et les mutants viennent du **chapitre 16**, la complexité et les structures de données du **chapitre 17** : fais-les avant celui-ci.

**Pourquoi ce chapitre :** un programme juste aujourd'hui doit encore être **facile à changer** dans un an, par quelqu'un d'autre. C'est ce qui sépare un développeur qui « fait marcher » d'un développeur senior : il sait **lire** un code difficile, le **refactorer** sans rien casser, reconnaître les principes **SOLID** et les **patrons de conception**, et surtout savoir **quand** s'en servir, et quand ne pas compliquer. La plupart des projets partent d'un **legacy** fourni : un vrai code qui marche mais que personne n'ose toucher, comme on en trouve dans toutes les entreprises.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 9 | 6 |
| But | un principe ou une famille de patrons par projet, sur une application ; un capstone de refactoring complet | réécrire les patrons de mémoire, proprement |
| Durée | 3 à 5 h chacun | 20 à 25 min |
| Combien de fois | une fois ; p01, p06 et p09 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : tout Java, plus les chapitres 16 et 17

**Tu as droit à tout Java 17, JUnit et Mockito.** Chaque `TODO.md` dit ses interdits : souvent `switch` et `instanceof` (le polymorphisme les remplace), `extends` quand on veut de la composition, et toujours une **longueur maximale** de méthode. `Check` les vérifie. Comme au chapitre 16 : pas de `System.out` ni de `Thread.sleep` dans les tests.

---

## 3. Comment `Check` vérifie : les tests, les mutants, **la forme**

Les projets sont vérifiés comme au chapitre 16 : tes tests sur ton code, sur le code de référence, les tests de référence sur ton code, et tes tests sur des **mutants**. Deux nouveautés :

- **la forme du code** : `Check` mesure chaque méthode (les lignes non vides de son corps, sans les commentaires) et vérifie ce que chaque fichier a le droit de connaître. Les messages :
  - `[FAIL] conception : InvoiceCalculator.java : compute() fait 13 lignes (au plus 12)` : découpe la méthode (**Ctrl+Alt+M**) ;
  - `[FAIL] conception : OrderService.java ne doit pas contenir : le service ne lit pas l'heure systeme (now())` : une dépendance est au mauvais endroit ;
- **le maître étalon** : quand un legacy est fourni, tes tests le comparent au nouveau code sur des centaines d'entrées au hasard. Les mutants qui survivent te montrent ce que le hasard ne voit pas : les limites exactes, les refus, les nouveautés.

**Les drills** sont vérifiés par les tests de référence seuls (tu n'écris que le code) : une ligne par défi, `d01 : 1 executions, 1 reussies`, plus les règles de forme.

Lance `Check` **depuis la racine du dépôt** (`Kurse`).

---

## 4. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_invoice/TODO.md` : son étape 1 (les odeurs du code) et sa boîte à outils (les raccourcis de refactoring d'IntelliJ) servent à tout le chapitre.
3. Suis la section 6.

**L'ordre complet :**

```
p01 → r01
p02 → p03 → p04 → r02
p05 → r03
p06 → r04
p07 → r05
p08 → r06
p09 (capstone) → r01 en 10 minutes
```

---

## 5. La disposition des dossiers

```
ch18_design/
├── PARCOURS.md              ← ce fichier
├── PALAIS.md                ← ton palais mental pour ce chapitre (24 stations)
├── projects/
│   ├── README.md            ← la liste des 9 projets, à cocher
│   └── p01_invoice/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code
│       ├── Data.java        ← le legacy FOURNI : tu le lis, tu ne le modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← le code, les tests de référence et CORRIGE.md : à la fin
│       └── (ton code)       ← tes classes et tes tests : c'est TOI qui les crées
└── drills/
    ├── README.md            ← quand faire quel drill, tableau de suivi J0 → R5
    └── r01_refactor/        ← TODO.md (défis, carte mémoire), Check.java, solution/
```

---

## 6. Comment faire un projet

### 6.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, ce qui est fourni, ce que tu crées, les interdits |
| **Tableau de bord** (étapes ☐) | la leçon, les types et les règles **exacts**, les tests à écrire, les questions, les expériences |
| **Checklist** | ce que `Check` cherchera |

### 6.2 Travailler, étape par étape

1. **Lis le legacy en entier**, crayon en main : écris ses règles en français. On ne refactore que ce qu'on a compris.
2. **Le filet d'abord** : les tests de caractérisation, **avant** de toucher au code.
3. **De petits pas, tous verts** : un geste (de préférence fait par IntelliJ), les tests, un geste, les tests. Rouge ? **Ctrl+Z**, et refais plus petit.
4. **Ne change jamais le comportement pendant un refactoring**, même pour corriger une bizarrerie : note-la, et corrige-la dans une étape à part.
5. **Vérifie l'étape** dans la section correspondante de `solution/CORRIGE.md`.

### 6.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[FAIL] conception : … fait N lignes` | une méthode trop longue | extraire des méthodes (Ctrl+Alt+M), une par idée |
| `[FAIL] conception : X.java ne doit pas contenir : …` | une dépendance au mauvais endroit | relire la leçon de l'étape (port, fabrique, racine de composition) |
| `[FAIL] API : interdit ici : [switch]` | un code de type ou un aiguillage | le polymorphisme, une `Map`, un état |
| `[FAIL] les tests de REFERENCE sur TON code` | ton code ne fait pas ce que dit l'énoncé (ou le legacy) | le nom du test qui échoue dit lequel |
| `mutant N : SURVIT` | une règle que tes tests ne regardent pas | palier 2 de `INDICES.md` |
| `*** PROJET REUSSI ***` | juste, testé, et bien rangé | compare avec la solution |

### 6.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | relis la leçon de l'étape et l'exemple sur un autre sujet ; dessine les classes et leurs flèches sur papier |
| 2 | 20 min de plus | `INDICES.md` (indice 1, puis 2) ; ou demande-moi un **indice** |
| 3 | en dernier recours | la section de l'étape dans `solution/CORRIGE.md`, ferme, réécris |

---

## 7. Comment faire un drill

1. Note l'heure. Crée les fichiers du drill, de mémoire.
2. Lance `Check`.
3. **Après seulement :** la carte mémoire.
4. Note date, temps et ✗ dans `drills/README.md`. Avant chaque répétition, supprime tes fichiers.

---

## 8. Comment savoir que le chapitre 18 est acquis

- [ ] Les 9 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 6 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] Tu sais, sans hésiter :
  - nommer les odeurs d'un code et proposer le refactoring de chacune ;
  - écrire le filet d'un legacy (caractérisation, maître étalon, cas limites) **avant** de le toucher ;
  - expliquer chaque principe SOLID avec un exemple de **tes** projets ;
  - écrire de mémoire : une stratégie et son registre, un builder immuable, un décorateur, un proxy, un composite, une commande avec annuler/refaire, un observateur, une machine à états, une méthode modèle ;
  - dire **quand ne pas** utiliser un patron (une seule variante, pas de besoin réel : YAGNI).
- [ ] r01 passe en moins de 10 minutes après le capstone.
- [ ] p01, p06 et p09 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.

---

## 9. 🏠 Ton palais mental (pour ne pas oublier dans 6 mois)

Les principes sont rangés au **plafond de la chambre 2** (stations 1 à 12), les patrons au **plafond de la chambre 3** (stations 1 à 12) : [`PALAIS.md`](PALAIS.md).

- **Quand :** une fois le capstone réussi, pose les images (30 minutes, en deux fois), puis fais une balade le soir même.
- **À chaque répétition des drills** : la balade des deux plafonds **avant** le drill.
- **Chaque dimanche :** la grande balade, du salon jusqu'à la terrasse, puis le circuit des plafonds.
- Le palais range les règles et les pièges ; il ne remplace ni les projets ni les drills.
