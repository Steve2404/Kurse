# Chapitre 17 (Algorithmes et structures de données) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer.

> **Comment sont faits les énoncés.** Chaque étape d'un projet suit le même schéma :
> - **📖 La leçon** : la notion expliquée simplement, avec un exemple sur **un autre sujet** que le projet ;
> - **👉 À toi** : ce que tu construis ;
> - **🧪 Expériences** et **❓ Questions** : tu essaies, tu mesures, tu réponds en commentaire.
>
> Les gestes de base sont expliqués une fois pour toutes dans le **projet 0 du chapitre 1** (`ch1_buildingblocks/projects/p00_bonjour`). Les tests, le débogueur et les mutants viennent du **chapitre 16** : fais-le avant celui-ci.

**Pourquoi ce chapitre :** un programme juste mais lent ne sert à rien dès que les données grandissent : une page qui met une minute à s'afficher, un traitement de nuit qui ne finit plus. Un développeur senior **reconnaît** au premier coup d'œil la famille d'un problème (dichotomie, fenêtre, hachage, graphe, programmation dynamique…), sait dire **combien de temps** prendra une solution avant de la lancer, et écrit les gabarits classiques sans hésiter. Ce sont aussi les questions des entretiens d'embauche.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 11 | 7 (le dernier est un entretien chronométré) |
| But | une famille d'algorithmes par projet, dans une application, puis un GPS de livreur qui les assemble | réécrire les gabarits de mémoire, vite, sans erreur d'indice |
| Durée | 3 à 5 h chacun | 20 à 25 min (45 min pour l'entretien) |
| Combien de fois | une fois ; p09, p10 et p11 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : tout Java, plus le chapitre 16

**Tu as droit à tout Java 17, JUnit et Mockito.** Mais dans chaque projet, **tu écris l'algorithme étudié toi-même** : la version toute faite est interdite dans ton code (tu peux t'en servir dans tes tests pour comparer). Par exemple :
- p01 : pas d'`Arrays.binarySearch` ; p02 et p07 : pas de tri tout fait, ni de `TreeMap` ou `TreeSet` ;
- p04 : pas de `LinkedHashMap` (le cache LRU s'écrit à la main) ; p05 : pas de `java.util.Stack` (on utilise `ArrayDeque`) ;
- p08 : le tas s'écrit à la main (`siftUp`, `siftDown`), mais `PriorityQueue` sert ailleurs.

Chaque `TODO.md` dit ses interdits ; `Check` les vérifie. Comme au chapitre 16 : pas de `System.out` ni de `Thread.sleep` dans les tests.

---

## 3. Comment `Check` vérifie : la justesse, les mutants, **la vitesse**

Les projets sont vérifiés comme au chapitre 16 : tes tests sur ton code, sur le code de référence, les tests de référence sur ton code, et tes tests sur des **mutants**. Une nouveauté : les tests de référence contiennent des **tests de vitesse**, sur de grosses entrées (un million de nombres, des graphes de 90 000 sommets) avec `assertTimeoutPreemptively`. Un algorithme en O(n²) là où O(n log n) est attendu y échoue avec `execution timed out after 3000 ms`.

Deux conseils qui en découlent :
- **mets un délai** (`assertTimeoutPreemptively`) autour de tes tests qui pourraient boucler : une dichotomie mal écrite ne s'arrête jamais. Sans délai, `Check` attend 20 secondes avant de conclure à une boucle infinie ;
- **les tests de référence sont des exemples** de bons tests d'algorithmes : les cas limites (vide, une case, doublons, débordements) **plus** la vitesse **plus**, au capstone, la comparaison avec un **oracle**.

**Les drills** sont vérifiés par les tests de référence seuls (tu n'écris que le code) : une ligne par défi, `d01 : 6 executions, 6 reussies`, puis les premiers échecs.

Lance `Check` **depuis la racine du dépôt** (`Kurse`).

---

## 4. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_search/TODO.md` : sa première leçon (la complexité) sert à tout le chapitre.
3. Suis la section 6.

**L'ordre complet :**

```
p01 → p02 → r01
p03 → p04 → r02
p05 → p06 → r04
p07 → p08 → r03
p09 → r05
p10 → r06
p11 → r07 (l'entretien, test final)
```

---

## 5. La disposition des dossiers

```
ch17_algorithms/
├── PARCOURS.md              ← ce fichier
├── PALAIS.md                ← ton palais mental pour ce chapitre (24 stations)
├── projects/
│   ├── README.md            ← la liste des 11 projets, à cocher
│   └── p01_search/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← le code, les tests de référence et CORRIGE.md : à la fin
│       └── (ton code)       ← Search.java et SearchTest.java : c'est TOI qui les crées
└── drills/
    ├── README.md            ← quand faire quel drill, tableau de suivi J0 → R5
    └── r01_search_sort/     ← TODO.md (défis, carte mémoire), Check.java, solution/
```

---

## 6. Comment faire un projet

### 6.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les algorithmes visés, ce que tu crées, les interdits |
| **Tableau de bord** (étapes ☐) | la leçon, les signatures et les règles **exactes**, les tests à écrire, les questions, les expériences |
| **Checklist** | ce que `Check` cherchera |

### 6.2 Travailler, étape par étape

1. **Sur papier d'abord** : dessine les données, déroule l'algorithme à la main sur un petit exemple. La plupart des bugs d'algorithmes sont des erreurs d'indice d'un cran : le papier les montre.
2. **Calcule la complexité** avant de coder : si elle ne passera pas sur un million d'éléments, cherche mieux.
3. **Écris le code, puis les tests** (cas limites, puis un test de vitesse). Lance tes tests, puis `Check`.
4. **Fais les expériences de mesure** dans une petite classe à part avec un `main` et `System.nanoTime()` : voir un O(n²) exploser vaut mieux que mille explications.
5. **Vérifie l'étape** dans la section correspondante de `solution/CORRIGE.md`.

### 6.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `execution timed out after 3000 ms` | trop lent | calcule la complexité de ton code ; cherche la boucle en trop |
| `les tests ne se terminent pas en 20 s` | boucle infinie (ou très lent) | une dichotomie dont `lo` ou `hi` n'avance plus ? un parcours qui revient en arrière ? |
| `StackOverflowError` | récursion trop profonde | une pile explicite au lieu de la récursion, ou un cas de base manquant |
| `[FAIL] les tests de REFERENCE sur TON code` | ton code a un bug | le nom du test qui échoue dit lequel |
| `mutant N : SURVIT` | un cas que tes tests ne regardent pas | palier 2 de `INDICES.md` |
| `*** PROJET REUSSI ***` | tout est juste, et assez rapide | compare avec la solution |

### 6.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | déroule à la main sur un exemple de 4 ou 5 éléments ; lance un seul test en mode Debug |
| 2 | 20 min de plus | `INDICES.md` (indice 1, puis 2) ; ou demande-moi un **indice** |
| 3 | en dernier recours | la section de l'étape dans `solution/CORRIGE.md`, ferme, réécris |

---

## 7. Comment faire un drill

1. Note l'heure. Crée `RecallNN.java`, de mémoire.
2. Lance `Check`.
3. **Après seulement :** la carte mémoire.
4. Note date, temps et ✗ dans `drills/README.md`. Avant chaque répétition, supprime ton `RecallNN.java`.

---

## 8. Comment savoir que le chapitre 17 est acquis

- [ ] Les 11 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 7 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] Tu sais, sans hésiter :
  - donner la complexité d'un algorithme, et dire si elle passera sur un million d'éléments ;
  - écrire une dichotomie juste du premier coup (et ses bornes) ;
  - reconnaître une fenêtre glissante, un problème de hachage, de pile monotone, de graphe, de programmation dynamique ;
  - écrire de mémoire : tri fusion, tri rapide, BFS, Dijkstra, union-find, et une table de programmation dynamique ;
  - expliquer comment marche une `HashMap`, une `PriorityQueue`, un `TreeMap`, et ce qu'ils coûtent ;
  - tester un algorithme rapide contre un oracle lent.
- [ ] r07 (l'entretien) passe en moins de 30 minutes.
- [ ] p09, p10 et p11 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.

---

## 9. 🏠 Ton palais mental (pour ne pas oublier dans 6 mois)

Les gabarits et les pièges de ce chapitre sont rangés au **plafond de la cuisine** (stations 1 à 12) et au **plafond de la chambre 1** (stations 1 à 12) : [`PALAIS.md`](PALAIS.md).

- **Quand :** une fois le capstone réussi, pose les images (30 minutes, en deux fois), puis fais une balade le soir même.
- **À chaque répétition des drills** : la balade des deux plafonds **avant** le drill.
- **Chaque dimanche :** la grande balade, du salon jusqu'à la terrasse, puis le circuit des plafonds.
- Le palais range les signaux et les pièges ; il ne remplace ni les projets ni les drills.
