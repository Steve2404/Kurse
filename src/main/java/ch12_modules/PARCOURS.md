# Chapitre 12 (Modules) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement ressemble à celui des chapitres 1 à 11 :

> **Comment sont faits les énoncés.** Chaque étape d'un projet suit le même schéma :
> - **📖 La leçon** : la notion expliquée simplement, avec un exemple sur **un autre sujet** que le projet ;
> - **👉 À toi** : ce que tu construis ;
> - **🧪 Expériences** et **❓ Questions** : tu essaies, tu observes, tu réponds en commentaire.
>
> Les gestes de base sont expliqués une fois pour toutes dans le **projet 0 du chapitre 1** (`ch1_buildingblocks/projects/p00_bonjour`) : créer une classe, lancer, `Check`, arguments, terminal, lire une erreur. Relis-le si l'un d'eux te manque. Chaque projet rappelle aussi ses commandes exactes.
- des **projets** à construire de A à Z, pour **comprendre** ;
- des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

**Ce qui change :** les modules ne vivent **pas** dans `src/main/java`. Chaque module a son `module-info.java`, et Maven compile ce dossier comme **un seul** programme : plusieurs `module-info.java` y casseraient tout. Tu écris donc :
- **tes modules**, dans `ch12_modules/…`, à la **racine** du dépôt ;
- **ton script de commandes** (`build.sh` ou `recall.sh`), à côté du `TODO.md`. C'est lui qui compile, empaquette, lance et inspecte, avec les vrais outils `javac`, `java`, `jar`, `jdeps` et `jlink`.

`Check` lance ton script, compare sa sortie à la sortie attendue, puis vérifie les directives et options de tes `module-info.java` et de ton script.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 6 | 6 |
| But | concevoir des applications **multi-modules** et maîtriser les outils en ligne de commande | retrouver vite et sans aide les directives et les options |
| Durée | 2 à 4 h chacun | 12 à 15 min chacun |
| Combien de fois | une fois ; p02 et p06 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, `javac --help`, `java --help`, papier | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 12

**Tu as droit à :**
- tout le **Java** des chapitres 1 à 11 dans tes classes ;
- tout le **chapitre 12** :
  - `module-info.java` : `exports` (qualifié ou non), `requires` et `requires transitive`, `opens` (qualifié ou non), `open module`, `uses` et `provides … with` ;
  - **les services** : `ServiceLoader` et `provider()` ;
  - **les modules** nommés, automatiques et sans nom ; la migration *top-down* et *bottom-up* ;
  - **les outils** :
    - `javac` (`-d`, `--module-source-path`, `-m`, `-p`, `--add-exports`) ;
    - `java` (`-p`, `-m`, `--describe-module`, `--list-modules`, `--show-module-resolution`, `--limit-modules`, `--add-modules`, `--add-exports`, `--add-opens`) ;
    - `jar`, `jdeps` et `jlink` ;
  - à l'exécution : `Module`, `ModuleLayer`, `ModuleDescriptor`.

**Ce qui reste exclu :** les threads (chapitre 13), les fichiers (chapitre 14), JDBC (chapitre 15), `System.exit` et `now()`. `Check` les refuse avec le message `[FAIL] API : interdit ici`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Vérifie que **Git Bash** est installé : `Check` lance ton script avec. Les outils du JDK (`javac`, `jar`, `jdeps`, `jlink`) sont pris dans le JDK qui exécute `Check`.
3. Ouvre `projects/p01_library/TODO.md` en aperçu Markdown, puis suis la section 5.

**L'ordre complet :**

```
p01 → r01 r02
p02 → r03
p03 → r06
p04 → r04
p05 → r05
p06 (capstone)
```

---

## 4. La disposition des dossiers

```
src/main/java/ch12_modules/
├── PARCOURS.md                  ← ce fichier
├── projects/p01_library/
│   ├── TODO.md                  ← L'ÉNONCÉ
│   ├── INDICES.md               ← 2 indices repliés par étape, sans code (si tu bloques)
│   ├── Check.java               ← le correcteur : tu le LANCES
│   ├── solution/build.sh        ← le script de la correction
│   ├── solution/CORRIGE.md      ← étape par étape : directives, réponses aux questions, messages des expériences
│   └── (ton build.sh)           ← TON script
└── drills/r01_directives/ …     ← même chose, avec recall.sh

ch12_modules/                    ← à la RACINE du dépôt, hors de Maven
├── p01_library/
│   ├── solution/src/…           ← les modules de la correction
│   └── (ton src/…)              ← TES modules : src/<nom.du.module>/module-info.java + paquets
└── drills/r01_directives/ …

build/ch12/…                     ← ce que tes scripts produisent (ignoré par git)
```

**Spécificités du chapitre 12 :**
- **Un dossier par module :** `src/library.model/module-info.java`, puis les paquets en dessous (`src/library.model/library/model/Book.java`). C'est la forme qu'attend `--module-source-path`.
- **Un seul dossier par module path.** Sous Windows, le séparateur des chemins est `;`, ailleurs c'est `:`. Pour rester portable, les fiches rangent tous les jars dans **un** dossier. Attention : deux `-p` ne s'additionnent pas, le dernier gagne.
- **Les filtres.** Les sorties des outils contiennent des chemins propres à ta machine : les fiches les retirent avec `sed`. L'ordre des lignes de `--describe-module` n'est pas garanti : on les trie avec `sort`. Les échecs voulus se terminent par `|| true`, pour que `set -e` n'arrête pas le script.
- **La langue.** Certains outils répondent dans la langue de la machine (`jar` en allemand ici). `-J-Duser.language=en` force l'anglais.
- **Les messages d'erreur sont les vrais**, ceux du compilateur et de la JVM. Apprends à les lire : à l'examen, c'est le message qui dit quelle directive manque.

---

## 5. Comment faire un projet

### 5.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, les algorithmes, les modules à créer |
| **Tableau de bord** (étapes ☐) | les modules et leurs directives, les classes, les données, les **lignes exactes**, puis les **commandes** du script, une à une |
| **Checklist** | ce que `Check` cherchera dans tes `module-info`, ton Java et ton script |
| **Sortie attendue complète** | le contrat exact |

### 5.2 Travailler, étape par étape

1. **Sur papier**, dessine le **graphe des modules** : qui requiert qui (transitif ou non), qui exporte quoi à qui, qui fournit et qui utilise.
2. **Écris les `module-info.java` d'abord**, puis les classes.
3. **Écris le script au fur et à mesure.** Lance-le toi-même depuis le dossier `Kurse`, dans le terminal PowerShell d'IntelliJ : `& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch12_modules/projects/p01_library/build.sh` (le chemin complet de Git Bash, comme au chapitre 1, projet 4). Puis lance `Check`.
4. **Fais les expériences et réponds aux questions** : casser une directive pour lire le message, c'est **le** meilleur entraînement pour l'examen.
5. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] script introuvable` | pas de `build.sh` à côté du `TODO.md` | crée-le |
| `[FAIL] script : 3/25 … (ligne 4)` | la 4e ligne diffère | lance le script à la main ; souvent une compilation ratée (message plus haut) ou un filtre oublié |
| `[ERREUR] dossier introuvable` | pas de dossier `ch12_modules/<projet>` | crée tes modules au bon endroit |
| `[FAIL] API : encore a placer …` | une directive ou une option manque | la checklist dit laquelle |
| `*** PROJET REUSSI ***` | tout est juste | compare avec la solution |

L'argument `solution` vérifie la solution. Lance `Check` depuis la racine du dépôt (`Kurse`).

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | lance chaque commande seule, sans filtre, et lis le **message complet** |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la carte mémoire du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou le `module-info` concerné dans `ch12_modules/<projet>/solution/`), ferme, réécris, et note `# AIDE : solution consultée` |

---

## 6. Comment faire un drill

1. Note l'heure. Le chrono cible est en haut du `TODO.md`.
2. Écris les modules et le script `recall.sh`, **de mémoire**.
3. Lance `Check`.
4. **Après seulement :** la carte mémoire, puis les expériences.
5. Note date, temps et ✗ dans `drills/README.md`.
6. **Avant chaque répétition**, supprime tes modules (`ch12_modules/drills/rNN/src…`) et ton `recall.sh`.

---

## 7. Comment savoir que le chapitre 12 est acquis

- [ ] Les 6 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 6 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] Tu sais, sans hésiter :
  - écrire chaque directive, et dire ce qu'elle permet à la compilation et à l'exécution ;
  - distinguer `exports` et `opens`, et `requires` et `requires transitive` ;
  - nommer les 4 rôles d'un service, et écrire un fournisseur des deux façons ;
  - donner le nom d'un module automatique à partir d'un nom de jar ;
  - dire qui lit qui parmi les modules nommés, automatiques et sans nom ;
  - écrire de mémoire les commandes `javac`, `java`, `jar`, `jdeps` et `jlink` (formes courtes et longues) ;
  - reconnaître les messages : `not visible`, `module not found`, `cyclic dependence`, `InaccessibleObjectException`, `IllegalAccessError`, le paquet partagé, `jlink` et les modules automatiques.
- [ ] p02 et p06 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.
