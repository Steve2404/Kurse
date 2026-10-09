# Chapitre 6 (Class Design) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres 1 à 5 et 10 :

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
| But | concevoir des hiérarchies, produire une sortie exacte | retrouver vite et sans aide |
| Durée | 2 à 4 h chacun | 12 à 15 min chacun |
| Combien de fois | une fois ; p06 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 6

**Tu as droit à :**
- les **chapitres 1 à 5** : bases, opérateurs, décisions, API de base, méthodes, accès, `static`, surcharge, récursivité ;
- tout le **chapitre 6** :
  - **l'héritage** : `extends`, l'héritage simple, `Object`, les classes `final` ;
  - **les constructeurs** : le constructeur par défaut, `this(...)`, `super(...)`, `private`, `protected`, de copie ;
  - **l'initialisation** : classe puis objet, parent puis enfant, champs `final` ;
  - **les membres hérités** : redéfinir (`@Override`), `super.m()`, le retour covariant, les méthodes `final`, masquer les méthodes `static` et les champs, redéclarer une méthode `private` ;
  - **les classes abstraites** : méthodes abstraites, abstraites intermédiaires, constructeurs ;
  - **les objets immuables** : les 5 règles, les copies défensives ;
  - redéfinir `toString`, `equals` et `hashCode`.

**Le grand changement :** tu écris enfin tes **constructeurs**, et tu conçois des **hiérarchies** : une racine abstraite, des niveaux intermédiaires, des classes concrètes. Une même ligne (`s.describe()`) exécute la version de chaque objet réel.

**Ce qui reste exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| `interface`, `implements`, `sealed`, `permits` | 7 | une classe **abstraite** commune |
| `record`, `enum` (les tiens) | 7 | une classe immuable écrite à la main ; des constantes `static final` |
| classes imbriquées, locales, anonymes | 7 | une classe de premier niveau (plusieurs classes non publiques par fichier, c'est permis) |
| **cast d'objet** `(Manager) e` et ses règles | 7 | `if (e instanceof Manager m)` (pattern matching, chapitre 3) |
| lambdas, `Comparator` | 8 | une comparaison écrite dans le tri |
| collections | 9 | des tableaux |
| `try/catch`, `throw` | 11 | une valeur spéciale (`null`, `INVALIDE`) ; `throws` n'apparaît que dans les expériences |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

**Le formatage :** pas de `%f`. Utilise `Math.round(x * 100) / 100.0`, ou des centimes en `long`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_shapes/TODO.md` en aperçu Markdown.
3. Suis la section 5.

**L'ordre complet :**

```
p01 → r01 r02
p02 → r03
p03 → r04 r05
p04 → r07 r08
p05 → r06
p06 → (révision r04 r06)
p07 → r09 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch6_classdesign/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p01_shapes/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction : à la fin seulement
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       └── (tes classes)    ← Shape.java, Circle.java…, ShapesApp.java : c'est TOI qui les crées
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi
    └── r01_inherit/
        ├── TODO.md          ← les défis + la carte mémoire repliée
        ├── Check.java
        ├── solution/
        └── (ton Recall01.java, avec ses petites classes)
```

**Spécificités du chapitre 6 :**
- **Une classe par fichier dans les projets** (une hiérarchie se lit mieux ainsi). Dans les drills, mets toutes les petites classes non publiques **sous** `RecallNN`, dans le même fichier.
- **Dessine la hiérarchie** avant de coder : les boîtes, les flèches `extends`, et pour chaque méthode, où elle est déclarée, redéfinie ou héritée.
- **Pour l'initialisation**, écris l'ordre attendu sur papier **avant** de lancer. C'est le sujet le plus piégé de l'examen.
- **Mets `@Override` partout** : javac t'avertit si tu as surchargé au lieu de redéfinir.
- **Les expériences** sont essentielles. Les règles de redéfinition (accès, retour, `static`, `final`, `throws`) se jugent par `javac` : écris la ligne, lis l'erreur, retire-la.

---

## 5. Comment faire un projet

### 5.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, les classes à créer, la règle du crescendo |
| **Tableau de bord** (étapes ☐) | chaque étape contient les classes et leurs membres, les **lignes exactes** à afficher, les **appels exacts** du `main`, les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact, au caractère près |

### 5.2 Travailler, étape par étape

1. **Conçois d'abord sur papier :**
   - l'arbre des classes ;
   - ce qui est abstrait ou concret ;
   - les constructeurs, et qui appelle qui ;
   - ce qui est `final`, `private` ou `protected`.
2. **Crée la classe du `main` tout de suite**, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences** et **réponds aux questions par écrit**, en commentaire.
5. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | ton `main` a planté | lis l'exception (`NullPointerException` : un champ utilisé avant son initialisation ?) |
| `[FAIL] sortie : 3/14 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit où ils servent |
| `[FAIL] API : interdit ici …` | un cast d'objet, une interface, une classe imbriquée… | remplace-le (voir le tableau de la section 2) |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | redessine la hiérarchie ; trace l'ordre des constructeurs ; ajoute des `println` temporaires |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou la partie concernée de `solution/`), ferme, réécris de mémoire, note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ou `Data.java` ;
- taper en dur un résultat que Java doit calculer.

### 5.5 Quand c'est réussi

1. Compare ta hiérarchie avec `solution/` (et ses commentaires) : ce qui est abstrait, `final` ou `protected`.
2. Coche le projet dans `projects/README.md`, puis fais ses drills.

---

## 6. Comment faire un drill

1. Note l'heure. Le chrono cible est en haut du `TODO.md`.
2. Crée `RecallNN.java`, avec ses petites classes, dans le dossier du drill.
3. Fais les défis D01, D02… La ligne attendue est sous chaque défi.
4. **Rien d'autre que ta mémoire.** Plus de 3 minutes bloqué : ✗, et défi suivant.
5. Lance `Check`.
6. **Après seulement :** ouvre la carte mémoire, relis tes ✗, puis fais les expériences.
7. Note date, temps et ✗ dans `drills/README.md`.
8. **Avant chaque répétition, supprime ton `RecallNN.java`.**

---

## 7. Comment savoir que le chapitre 6 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 9 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r09 passe en moins de 15 minutes, sans carte.
- [ ] Tu sais dire, sans hésiter :
  - l'ordre exact d'initialisation d'un `new Enfant()` sur 3 niveaux ;
  - ce que javac insère quand un constructeur n'appelle ni `this` ni `super` ;
  - les 5 règles d'une redéfinition valide ;
  - la différence entre redéfinir et masquer ;
  - les 5 règles d'une classe immuable ;
  - le contrat `equals` / `hashCode`.
- [ ] Tu sais écrire sans aide : une hiérarchie abstraite avec une méthode modèle, un constructeur de copie, une classe immuable avec copies défensives, un `equals` correct.
- [ ] p06 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.

---

## 8. 🏠 Ton palais mental (pour ne pas oublier dans 6 mois)

Les règles et les pièges de ce chapitre sont rangés dans **la chambre 1**, stations 7 à 12 : [`PALAIS.md`](PALAIS.md).

- **Quand :** une fois le capstone réussi, pose les images (15 minutes), puis fais une balade le soir même.
- **À chaque répétition des drills** (J+1, J+3, J+7, J+14, J+30) : la balade de la pièce **avant** le drill (2 minutes, à voix haute, sans regarder, puis vérifie).
- **Chaque dimanche :** la grande balade, du salon jusqu'à la dernière pièce installée.
- Le palais range les règles ; il ne remplace ni les projets ni les drills.
