# Chapitre 14 (I/O) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres précédents :

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
| Combien | 7 | 6 (+ 1 bonus) |
| But | de vrais outils sur des fichiers : liens d'un site, sauvegarde incrémentale, compresseur, sauvegarde de partie, tri externe, doublons, gestionnaire de versions | retrouver vite et sans aide l'API et ses pièges |
| Durée | 2 à 4 h chacun | 10 à 12 min chacun |
| Combien de fois | une fois ; p02 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 14

**Tu as droit à :** tous les chapitres 1 à 13, et tout le **chapitre 14** :
- **`Path`/`Paths`/`File`** : construire, décomposer, `resolve`, `relativize`, `normalize` ;
- **`Files`** :
  - les opérations : créer, copier, déplacer, supprimer, tester, `mismatch` ;
  - la lecture et l'écriture de texte et d'octets ;
  - les parcours : `list`, `walk`, `find`, `walkFileTree` ;
  - les attributs ;
- **les flux `java.io`** : octets et caractères, tampons, passerelles avec `Charset`, `PrintStream`/`PrintWriter`, `mark`/`reset`, `Data*Stream` ;
- **la sérialisation** : `Serializable`, `transient`, `serialVersionUID`, `ObjectInputStream`/`ObjectOutputStream` ;
- **`System.in`/`out`/`err`**, `Console`, `Scanner`.

**Ce qui reste exclu :** JDBC (chapitre 15), `System.exit`, `printStackTrace` et `now()`. `Check` les refuse avec le message `[FAIL] API : interdit ici`.

---

## 3. Les règles d'un programme qui touche au disque

`Check` compare ta sortie, ligne par ligne, et elle doit être la même sur toutes les machines :
- **un bac à sable par projet** : `build/ch14/<projet>/`, relatif à la racine du dépôt (`Data.SANDBOX`). Le programme le **supprime puis le recrée** au début : il repart toujours de zéro ;
- **des chemins relatifs, affichés avec `/`** : sous Windows, `Path.toString()` utilise `\`, d'où la méthode `show(Path)` du projet 1, réutilisée partout ;
- **un ordre fixe** : l'ordre d'un parcours (`walk`, `list`, `walkFileTree`) dépend du système de fichiers. **Trie** avant d'afficher ;
- **des dates fixes** : jamais l'heure réelle. Si une date compte, fixe-la (`setLastModifiedTime`) ;
- **toujours fermer** : les flux `java.io` comme les streams de `Files` (`lines`, `walk`, `list`, `find`) vont dans un try-with-resources.

Lance `Check` **depuis la racine du dépôt** (`Kurse`) : c'est le répertoire de travail par défaut dans IntelliJ.

---

## 4. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_paths/TODO.md` en aperçu Markdown.
3. Suis la section 6.

**L'ordre complet :**

```
p01 → r01
p02 → r02
p03 → r03
p04 → r04
p05 → r05
p06 → (révision r05)
p07 → r06 (test final)
puis r07 (bonus : liens symboliques, POSIX, Console ; une partie se lance à la main dans un vrai terminal)
```

---

## 5. La disposition des dossiers

```
ch14_io/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p01_paths/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction : à la fin seulement
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       └── (tes types)      ← PathLab.java... : c'est TOI qui les crées
└── drills/ …

build/ch14/…                 ← les bacs à sable (ignorés par git)
```

---

## 6. Comment faire un projet

### 6.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, l'algorithme, les types à créer |
| **Tableau de bord** (étapes ☐) | les types et leurs méthodes, les **lignes exactes**, les **appels exacts**, les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact |

### 6.2 Travailler, étape par étape

1. **Ouvre le bac à sable** dans l'explorateur pendant que tu travailles : regarder les fichiers créés aide énormément.
2. **Commence par la suppression et la recréation du bac à sable** : sans elles, un 2e lancement trouve les fichiers du 1er.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences** : provoque chaque exception au moins une fois.
5. **Réponds aux questions par écrit**, en commentaire dans ton code.
6. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 6.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] ton programme a lance … NoSuchFileException` | un chemin faux, ou un dossier non créé | vérifie `createDirectories`, et le répertoire de travail (la racine du dépôt) |
| `[ERREUR] … FileAlreadyExistsException` | le bac à sable n'a pas été vidé | supprime-le au début |
| `[FAIL] sortie : …` | une ligne diffère | souvent `\` au lieu de `/`, ou un ordre non trié |
| `[FAIL] API : …` | un élément manque, ou est interdit | la checklist |
| `*** PROJET REUSSI ***` | tout est juste | compare avec la solution |

### 6.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | affiche les chemins avec `toAbsolutePath()`, et regarde le bac à sable |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la carte mémoire du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou la partie concernée de `solution/`), ferme, réécris |

---

## 7. Comment faire un drill

1. Note l'heure. Crée `RecallNN.java`, de mémoire.
2. Lance `Check`.
3. **Après seulement :** la carte mémoire, puis les expériences.
4. Note date, temps et ✗ dans `drills/README.md`. Avant chaque répétition, supprime ton `RecallNN.java`.

---

## 8. Comment savoir que le chapitre 14 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 6 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] Tu sais, sans hésiter :
  - prévoir le résultat de `resolve`, `relativize`, `normalize`, `subpath`, `startsWith` ;
  - dire quelle exception lève chaque opération de `Files`, et quand `REPLACE_EXISTING` ou `deleteIfExists` l'évitent ;
  - choisir entre flux d'octets et de caractères, et les envelopper correctement ;
  - dire ce que rendent `read()` et `readLine()` à la fin ;
  - dire ce qui est sérialisé, et quels constructeurs s'exécutent à la relecture ;
  - choisir entre `list`, `walk`, `find` et `walkFileTree`, et savoir pourquoi les fermer ;
  - lire et modifier un attribut de fichier.
- [ ] Tu sais écrire sans aide : supprimer ou copier un arbre, comparer deux arbres, une copie profonde par sérialisation, un tri externe, un visiteur de fichiers.
- [ ] p02 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.

---

## 9. 🏠 Ton palais mental (pour ne pas oublier dans 6 mois)

Les règles et les pièges de ce chapitre sont rangés dans **la terrasse**, stations 1 à 6 : [`PALAIS.md`](PALAIS.md).

- **Quand :** une fois le capstone réussi, pose les images (15 minutes), puis fais une balade le soir même.
- **À chaque répétition des drills** (J+1, J+3, J+7, J+14, J+30) : la balade de la pièce **avant** le drill (2 minutes, à voix haute, sans regarder, puis vérifie).
- **Chaque dimanche :** la grande balade, du salon jusqu'à la dernière pièce installée.
- Le palais range les règles ; il ne remplace ni les projets ni les drills.
