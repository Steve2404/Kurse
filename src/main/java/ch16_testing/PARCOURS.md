# Chapitre 16 (Tester et déboguer) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. C'est le **premier chapitre après le livre** : il ne prépare plus l'examen OCP, il prépare au **métier**.

> **Comment sont faits les énoncés.** Chaque étape d'un projet suit le même schéma :
> - **📖 La leçon** : la notion expliquée simplement, avec un exemple sur **un autre sujet** que le projet ;
> - **👉 À toi** : ce que tu construis ;
> - **🧪 Expériences** et **❓ Questions** : tu essaies, tu observes, tu réponds en commentaire.
>
> Les gestes de base sont expliqués une fois pour toutes dans le **projet 0 du chapitre 1** (`ch1_buildingblocks/projects/p00_bonjour`) : créer une classe, lancer, `Check`, arguments, lire une erreur. Relis-le si l'un d'eux te manque.

**Pourquoi ce chapitre :** jusqu'ici, `Check` vérifiait ton travail. Dans une vraie équipe, il n'y a pas de `Check` : chaque développeur écrit **ses propres tests**, et ce sont eux qui disent si le code marche, aujourd'hui et dans deux ans. Un développeur senior se reconnaît d'abord à ses tests : ils attrapent les bugs, ils ne cassent pas pour rien, et ils se lisent comme une documentation. Ce chapitre t'apprend à en écrire de bons, à les écrire **avant** le code (le TDD), et à trouver un bug avec le **débogueur** au lieu de deviner.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 8 | 6 (dont 2 katas) |
| But | une caisse, un panier, un guichet des impôts, des chiffres romains en TDD, des réservations, un paiement, un inventaire bogué à déboguer, une médiathèque | retrouver vite et sans aide les assertions, le cycle de vie, les sources de cas, Mockito ; rendre le TDD automatique |
| Durée | 2 à 4 h chacun | 12 à 15 min (35 min pour un kata) |
| Combien de fois | une fois ; p04 et p08 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 15, plus JUnit et Mockito

**Tu as droit à tout le livre**, plus :
- **JUnit 5** : `@Test`, les assertions, le cycle de vie (`@BeforeEach`…), `@Nested`, `@DisplayName`, les tests paramétrés ;
- **Mockito** (à partir du projet 6) : `@Mock`, `when`, `verify`, `ArgumentCaptor`, `InOrder`…
- **`java.time.Clock`**, pour figer le temps dans les tests.

Les deux bibliothèques sont déclarées dans `pom.xml` : IntelliJ les télécharge tout seul. (Si `org.junit` est souligné en rouge : panneau **Maven** à droite → **Reload All Maven Projects**.)

**Ce qui est interdit, et vérifié par `Check` :**
- dans tes **tests** : `System.out` (un test **vérifie**, il n'affiche pas) et `Thread.sleep` (un test lent ou qui dépend du hasard) ;
- dans ton **code** : `double` et `float` pour de l'argent (projets 1 à 3), l'heure réelle `LocalDateTime.now()` et compagnie (projets 5 et 8) ;
- Mockito au projet 5 (les doublures s'y écrivent à la main).

---

## 3. Comment `Check` vérifie des tests

Ici, tu écris **deux** choses dans le paquet du projet : le **code** (ses classes et ses méthodes sont imposées par le `TODO.md`) et **tes tests** (chaque fichier dont le nom finit par `Test.java`).

`Check` recopie tout dans un paquet temporaire (`checkrun.r1`, `checkrun.r2`…), le compile, puis lance les tests **quatre fois** :

| Ligne de `Check` | Ce qu'elle vérifie | Si elle échoue |
|---|---|---|
| `tes tests sur TON code` | tes tests passent | un test rouge : ton code, ou ton test, a un bug |
| `tes tests sur le code de REFERENCE` | tes tests passent aussi sur le code corrigé | un de tes tests attend une **mauvaise** valeur |
| `les tests de REFERENCE sur TON code` | ton code passe les tests du corrigé | **ton code** a un bug ; le nom du test qui échoue dit lequel |
| `mutant N : tue / SURVIT` | tes tests attrapent des bugs glissés exprès dans le code de référence | un cas que tes tests ne regardent pas |

Puis il vérifie l'API de ton code et de tes tests (la checklist du `TODO.md`).

**Les mutants** sont la grande idée du chapitre : un test vert ne prouve rien s'il resterait vert avec un code faux. Chaque mutant est une copie du code de référence avec **un seul petit bug** (`<` au lieu de `<=`, un `+ 50` oublié…). Un bon jeu de tests les **tue** tous.

**Trois règles qui en découlent :**
- **respecte les signatures et les messages** du `TODO.md` à la lettre : les tests de référence les appellent ;
- **les doublures de test** (projet 5) vont **dans** ta classe de test (des classes `static` imbriquées), pas dans des fichiers à part : seuls les fichiers `…Test.java` accompagnent tes tests chez les mutants ;
- **le nombre de tests** compte chaque cas d'un test paramétré. Le minimum est dans la checklist.

**Les drills r01 à r04** sont vérifiés autrement : `Check` lance tes tests et affiche une ligne par méthode, `d01 : 1 executions, 1 reussies`, comparée à la sortie attendue. Les **katas** r05 et r06 sont vérifiés comme des projets, avec des mutants.

Lance `Check` **depuis la racine du dépôt** (`Kurse`) : c'est le répertoire de travail par défaut dans IntelliJ.

---

## 4. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_vat/TODO.md` en aperçu Markdown.
3. Suis la section 6.

**L'ordre complet :**

```
p01 → r01
p02 → r02
p03 → r03
p04 → r05 (kata)
p05 → (révision r02)
p06 → r04
p07 → (révision r01)
p08 → r06 (kata final, test du chapitre)
```

---

## 5. La disposition des dossiers

```
ch16_testing/
├── PARCOURS.md              ← ce fichier
├── PALAIS.md                ← ton palais mental pour ce chapitre
├── projects/
│   ├── README.md            ← la liste des 8 projets, à cocher
│   └── p01_vat/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction et ses tests : à la fin seulement
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       └── (ton code)       ← VatCalculator.java et VatCalculatorTest.java : c'est TOI qui les crées
└── drills/
    ├── README.md            ← quand faire quel drill, tableau de suivi J0 → R5
    └── r01_assertions/      ← TODO.md (défis, carte mémoire), Check.java, solution/
```

---

## 6. Comment faire un projet

### 6.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, ce que tu crées, les interdits, l'encadré **Tes outils** |
| **Tableau de bord** (étapes ☐) | la leçon, puis les signatures et les règles **exactes** du code, puis les tests à écrire, les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code et dans tes tests |
| **Ce que `Check` affiche** | à quoi ressemble la réussite |

### 6.2 Travailler, étape par étape

1. **Calcule à la main** le résultat attendu de chaque test, **avant** de lancer : un test dont tu ne connais pas la réponse ne teste rien.
2. **Une étape à la fois.** Écris le code, puis ses tests (ou l'inverse, au projet 4). Lance tes tests (**Ctrl+Maj+F10**), puis `Check`. Coche ☐ → ☑.
3. **Fais les expériences** : casse exprès, lis le message, répare.
4. **Réponds aux questions par écrit**, en commentaire dans ton code.
5. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences.

### 6.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] aucun fichier de test` | pas de fichier `…Test.java` | crée ta classe de test, avec ce nom |
| `[FAIL] … ne compile pas` + `cannot find symbol` | une classe ou une méthode imposée manque, ou n'a pas la bonne signature | compare avec le `TODO.md`, lettre à lettre |
| `[FAIL] tes tests sur TON code` | un test rouge | lis l'échec affiché (`expected: <…> but was: <…>`) |
| `[FAIL] tes tests sur le code de REFERENCE` | un de tes tests attend une mauvaise valeur | refais le calcul à la main |
| `[FAIL] les tests de REFERENCE sur TON code` | ton code a un bug | le nom du test qui échoue le dit |
| `mutant N : SURVIT` | un bug que tes tests ne voient pas | cherche le cas manquant ; palier 2 de `INDICES.md` |
| `[FAIL] seulement N tests` | pas assez de tests | la checklist donne le minimum |
| `[FAIL] API : …` | un élément manque, ou est interdit | la checklist |
| `OpenJDK … Sharing is only supported…` | Mockito s'installe dans la JVM | rien : c'est normal |
| `*** PROJET REUSSI ***` | tout est juste | compare avec la solution |

### 6.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | lance **un seul** test, en mode **Debug** (projet 7), et regarde les valeurs |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** ; pour un mutant, la liste repliée de ce qu'il change ; ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou la partie concernée de `solution/`), ferme, réécris |

---

## 7. Comment faire un drill

1. Note l'heure. Crée tes fichiers, de mémoire.
2. Lance tes tests, puis `Check`.
3. **Après seulement :** la carte mémoire (avec les résultats des expériences).
4. Note date, temps et ✗ dans `drills/README.md`. Avant chaque répétition, supprime tes fichiers.

---

## 8. Comment savoir que le chapitre 16 est acquis

- [ ] Les 8 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 6 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] Tu sais, sans hésiter :
  - écrire un test lisible (Arrange, Act, Assert ; un nom qui dit ce qui est vérifié) et lire un message d'échec ;
  - choisir les cas : les valeurs limites, une classe d'équivalence par règle ;
  - écrire un test paramétré avec chaque source, et dire combien de cas elle produit ;
  - faire un cycle de TDD sans tricher ;
  - remplacer une dépendance par un faux, un espion ou un bouchon écrit à la main, ou par un simulacre Mockito, et dire quand **ne pas** le faire ;
  - figer le temps avec `Clock` ;
  - trouver un bug au débogueur (point d'arrêt conditionnel, évaluation, exception), puis le verrouiller par un test ;
  - expliquer ce qu'est un mutant, un mutant équivalent, et pourquoi 100 % de couverture ne suffit pas.
- [ ] r06 (le bowling) passe en moins de 20 minutes, en TDD.
- [ ] p04 et p08 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.

---

## 9. 🏠 Ton palais mental (pour ne pas oublier dans 6 mois)

Les règles et les pièges de ce chapitre sont rangés au **plafond du salon**, stations 1 à 12 : [`PALAIS.md`](PALAIS.md). C'est le **deuxième circuit** de ta maison : même trajet, mais on lève les yeux.

- **Quand :** une fois le capstone réussi, pose les images (15 minutes), puis fais une balade le soir même.
- **À chaque répétition des drills** (J+1, J+3, J+7, J+14, J+30) : la balade du plafond du salon **avant** le drill (2 minutes, à voix haute, sans regarder, puis vérifie).
- **Chaque dimanche :** la grande balade, du salon jusqu'à la terrasse, puis le circuit des plafonds.
- Le palais range les règles ; il ne remplace ni les projets ni les drills.
