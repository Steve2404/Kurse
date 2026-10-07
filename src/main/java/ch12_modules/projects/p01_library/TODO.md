# Projet 1 — La bibliothèque en trois modules

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 12) :**
- **`module-info.java`** : `module`, `exports`, `requires`, `requires transitive`, l'**export qualifié** `exports … to …` ;
- `java.base`, requis **implicitement** (`mandated`) ;
- **compiler plusieurs modules :** `javac -d … --module-source-path … -m …` ;
- **lancer :** `java -p … -m module/classe`, puis `java -p … -m module` depuis un jar qui a une classe principale ;
- **`jar --create`**, avec `--main-class` ;
- **décrire :** `java --describe-module`, `jar --describe-module` ;
- **une erreur d'accès** à la compilation (paquet non exporté au module) ;
- **à l'exécution :** `Class.getModule()`, `Module.getName()`, `isNamed()`, `isExported(…)`, `canRead(…)`.

Côté algorithmes :
- un **index trié** (`TreeMap` et `subMap`) pour la recherche par préfixe ;
- des **regroupements** par genre et par décennie.

**Ce qui est donné :** `Check.java` (le correcteur).

**Ce que TU crées :**
1. **Tes modules**, hors de Maven, dans `ch12_modules/p01_library/` (à la racine du dépôt) :
   - `src/` contient **un dossier par module** : `src/library.model/`, `src/library.service/`, `src/library.app/` ;
   - `intruder/library.intruder/` contient un module qui **doit échouer**.
2. **Ton script** `build.sh`, dans **ce** dossier (`src/main/java/ch12_modules/projects/p01_library/build.sh`).

**Règle du crescendo :** chapitres 1 à 12. Pas de threads, de fichiers ni de JDBC dans le code Java.

**Ce que le chapitre 12 t'apprend :** les **modules**. Un module est un groupe de paquets qui porte un **nom** et un **contrat**, écrit dans un fichier `module-info.java` :
- ce qu'il **montre** aux autres (`exports`) ;
- ce dont il **a besoin** (`requires`).

Ce qui n'est pas exporté est **invisible** de l'extérieur, même `public`.

**Où ranger tes fichiers.** Ce chapitre travaille **hors de Maven**, avec les vrais outils `javac`, `java` et `jar` :
- tes modules vont dans `ch12_modules/p01_library/src/`, à la **racine** du dépôt (à côté de `src/` et de `pom.xml`) ;
- ton script `build.sh` va dans le dossier de ce `TODO.md`.

**Créer les dossiers et fichiers dans IntelliJ :**
1. Clic droit sur le dossier racine `Kurse` → **New** → **Directory**, puis tape le chemin entier avec des `/` : `ch12_modules/p01_library/src/library.model/library/model`. IntelliJ crée tous les dossiers d'un coup.
2. Clic droit sur `library.model` (le dossier du module) → **New** → **File** → `module-info.java`.
3. Pour une classe : clic droit sur le dossier du paquet → **New** → **File** → `Book.java`. Écris toi-même la ligne `package library.model;` en haut.

IntelliJ ne connaît pas ces dossiers comme du code Java : il peut souligner en rouge ou ne rien colorer. **Ce n'est pas grave** : c'est ton script qui compile, avec `javac`.

**Lancer ton script** (dans le terminal PowerShell, depuis `Kurse`, comme au chapitre 1, projet 4) :

```
& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch12_modules/projects/p01_library/build.sh
```

Puis lance `Check.java` avec la flèche verte : il exécute ton script de la même façon.

---

## Tableau de bord

### ☐ Étape 1 — Les trois modules

**📖 La leçon : le `module-info.java`.** Exemple sur une cuisine, avec deux modules :

```java
// fichier src/cuisine.recettes/module-info.java
module cuisine.recettes {
    exports cuisine.recettes;              // ce paquet est visible des autres modules
}

// fichier src/cuisine.app/module-info.java
module cuisine.app {
    requires cuisine.recettes;             // j'ai besoin de ce module
}
```

Les classes vont **sous** le dossier du module, rangées par paquet : `src/cuisine.recettes/cuisine/recettes/Recette.java`. Le nom d'un module ressemble à un nom de paquet ; par habitude, on lui donne le nom de son paquet principal.

**Les directives à connaître :**

| Directive | Sens |
|---|---|
| `exports p;` | le paquet `p` est visible de **tous** les modules qui me lisent |
| `exports p to m1, m2;` | **export qualifié** : visible **seulement** de `m1` et `m2` |
| `requires m;` | je lis le module `m` |
| `requires transitive m;` | je lis `m`, **et** ceux qui me lisent le lisent aussi |

`java.base` (qui contient `String`, `List`…) est requis **automatiquement** par tous les modules.

Pour qu'un module utilise une classe d'un autre, il faut **deux** choses : qu'il le **lise** (`requires`), **et** que le paquet soit **exporté** vers lui.

**👉 À toi :**

| Module | `module-info.java` | Contenu |
|---|---|---|
| `library.model` | exporte `library.model` | `record Book(String isbn, String title, String author, int year, Genre genre)` avec `static Book parse(String)` (champs séparés par `;`), et `enum Genre { ROMAN, SF, POLAR, ESSAI }` |
| `library.service` | `requires transitive library.model`, exporte `library.service`, et `exports library.service.internal to library.app` | `library.service.internal.Normalizer` et `library.service.Catalog` |
| `library.app` | `requires library.service` **seulement** | `library.app.Main` |

- **`Normalizer.sortKey(String title)`** : met en minuscules (`Locale.ROOT`) et retire un article de tête (`le `, `la `, `les `, `l'`).
- **`Catalog(List<Book>)`** remplit une `TreeMap` : clé de tri → livre. Méthodes :
  - `size()` ;
  - `List<String> titlesStartingWith(String prefix)` : `subMap(prefix, true, prefix + Character.MAX_VALUE, false)` ;
  - `Map<Genre, Long> countByGenre()` : une `TreeMap`, donc dans l'ordre de l'`enum` ;
  - `Map<Integer, List<String>> byDecade()` : les livres triés par année, groupés par `year / 10 * 10` dans une `TreeMap` ;
  - `List<Book> byAuthor(String)`.
- **Question :** pourquoi `library.service` doit-il dire `requires transitive` (indice : quel type rend `byAuthor`) ?

### ☐ Étape 2 — Le programme

```
catalogue : 9 livres, par genre {ROMAN=3, SF=4, POLAR=1, ESSAI=1}
...
library.service.internal exporte a library.app true, a library.model false, a tous false
library.app lit library.model true (par transitivite), library.model lit library.app false
```

**📖 La leçon : interroger les modules pendant l'exécution.** Chaque classe sait à quel module elle appartient :

```java
Module m = Main.class.getModule();
m.getName()                              // "cuisine.app"
m.isNamed()                              // true : un module avec un module-info
m.canRead(Recette.class.getModule())     // true : cuisine.app lit cuisine.recettes
recettes.isExported("cuisine.recettes")              // exporté à tous ?
recettes.isExported("cuisine.recettes", autreModule) // exporté vers ce module-là ?
```

**📖 Rappel :** `subMap` d'une `TreeMap` (chapitre 9, projet 5), `groupingBy` (chapitre 10, projet 5).

**👉 À toi :**

- **Les données**, à recopier dans `Main` :
  ```java
  static final String[] BOOKS = {
          "978-1;Le Petit Prince;Saint-Exupery;1943;ROMAN", "978-2;Dune;Herbert;1965;SF", "978-3;La Peste;Camus;1947;ROMAN",
          "978-4;Les Robots;Asimov;1950;SF", "978-5;Fondation;Asimov;1951;SF", "978-6;L'Etranger;Camus;1942;ROMAN",
          "978-7;Le Mythe de Sisyphe;Camus;1942;ESSAI", "978-8;La Nuit des temps;Barjavel;1968;SF", "978-9;Maigret;Simenon;1931;POLAR"};
  ```
- **Les 7 lignes**, dans l'ordre :
  1. le nombre de livres et `countByGenre()` ;
  2. `byDecade()` ;
  3. `titlesStartingWith("p")` et `("n")` ;
  4. les titres de `byAuthor("Camus")`, puis `Normalizer.sortKey("L'Etranger")` ;
  5. les noms des modules de `Main`, `Catalog` et `Book`, puis `isNamed()` ;
  6. `isExported("library.service.internal", m)` vers `library.app`, puis vers `library.model`, puis `isExported(…)` sans cible ;
  7. `canRead` de `library.app` vers `library.model`, puis l'inverse.

### ☐ Étape 3 — L'intrus

**📖 Rappel :** un paquet non exporté vers un module lui est invisible (étape 1). Le message de `javac` dit **quelle** directive manque : lis-le jusqu'au bout.

**👉 À toi :**

- `intruder/library.intruder/module-info.java` : `requires library.service`.
- `library.intruder.Spy` appelle `Normalizer.sortKey("Le Secret")`. Ce module **ne doit pas compiler**.

### ☐ Étape 4 — Le script `build.sh`

```
--- describe-module library.service
exports library.service
library.service
qualified exports library.service.internal to library.app
...
--- intrus
error: package library.service.internal is not visible
```

**📖 La leçon : compiler et lancer des modules.** Sur l'exemple de la cuisine :

```bash
# Compiler : --module-source-path = le dossier qui contient UN dossier par module ;
#            -m = les modules à compiler ; -d = où ranger (un dossier par module).
javac -d out/mods --module-source-path src -m cuisine.app

# Lancer : -p (ou --module-path) = où chercher les modules ; -m module/classe.
java -p out/mods -m cuisine.app/cuisine.app.Main

# Décrire un module, tel que la JVM le voit.
java -p out/mods --describe-module cuisine.app

# Un jar par module ; --main-class inscrit la classe principale dans le jar.
jar --create --file out/jars/cuisine.recettes.jar -C out/mods/cuisine.recettes .
jar --create --file out/jars/cuisine.app.jar --main-class cuisine.app.Main -C out/mods/cuisine.app .
java -p out/jars -m cuisine.app          # plus besoin du nom de la classe
jar --describe-module --file out/jars/cuisine.app.jar
```

`--describe-module` affiche des lignes comme `requires java.base mandated` (« requis d'office »).

**📖 Rappel :** les scripts bash, `|`, `set -e` (chapitre 1, projet 4, étape 5). `sed 's/ file:.*//'` efface, sur chaque ligne, tout ce qui suit ` file:`. `sort` trie les lignes. `head -1` garde la première.

**👉 À toi :**

- **En tête :**
  - `#!/bin/bash` et `set -e` ;
  - `P=ch12_modules/p01_library` et `OUT=build/ch12/p01_library` ;
  - `rm -rf "$OUT"`.
- **Les commandes, dans l'ordre :**
  1. `javac -d "$OUT/mods" --module-source-path "$P/src" -m library.app,library.service,library.model` ;
  2. `java -p "$OUT/mods" -m library.app/library.app.Main` ;
  3. `echo "--- describe-module library.service"`, puis `java -p … --describe-module library.service`, filtré par `| sed 's/ file:.*//' | sort` ;
  4. un jar par module dans `$OUT/jars/<module>.jar` (`jar --create --file … -C "$OUT/mods/<module>" .`), avec `--main-class library.app.Main` pour l'application ;
  5. `echo "--- describe-module du jar library.app"`, puis `jar --describe-module --file …`, filtré par `| sed 's/ jar:.*//' | sort` ;
  6. `echo "--- depuis les jars"`, puis `java -p "$OUT/jars" -m library.app | head -1` ;
  7. `echo "--- intrus"`, puis la compilation de l'intrus : `javac -d "$OUT/intruder" -p "$OUT/mods" --module-source-path "$P/intruder" -m library.intruder`. Garde seulement les messages, sans chemin :
     ```bash
     2>&1 | grep -E "error:|declared in" | sed 's/.*error:/error:/; s/^ *//' || true
     ```
- **Pourquoi ces filtres ?**
  - `sed` retire les chemins, propres à chaque machine ;
  - `sort` est nécessaire car l'**ordre des lignes** de `--describe-module` n'est **pas garanti** ;
  - `|| true` évite que `set -e` arrête le script sur l'échec voulu.
- **Expériences** (puis remets en état) :
  - retire `transitive` : que dit `javac` pour `Main` ?
  - retire `library.model` de `-m` à l'étape 1 : est-il quand même compilé ?
  - `java -p "$OUT/mods" -m library.app` (sans classe), avant les jars : que se passe-t-il ?

---

## Checklist (vérifiée par `Check`)

- **Dans les `module-info`** : `module library.model`, `exports library.model;`, `requires transitive library.model;`, `exports library.service.internal to library.app;`, `requires library.service;`, `module library.intruder`.
- **Dans le Java** : `record Book(`, `enum Genre`, `.subMap(`, `Collectors.groupingBy(`, `.getModule()`, `.isExported(`, `.canRead(`, `.isNamed()`.
- **Dans le script** : `javac -d`, `--module-source-path`, `-m library.app,library.service,library.model`, `java -p`, `-m library.app/library.app.Main`, `--describe-module library.service`, `jar --create`, `--main-class library.app.Main`, `jar --describe-module`, `-m library.app |`.

---

## Sortie attendue complète

```
catalogue : 9 livres, par genre {ROMAN=3, SF=4, POLAR=1, ESSAI=1}
par decennie : {1930=[Maigret], 1940=[L'Etranger, Le Mythe de Sisyphe, Le Petit Prince, La Peste], 1950=[Les Robots, Fondation], 1960=[Dune, La Nuit des temps]}
titres en 'p' : [La Peste, Le Petit Prince], en 'n' : [La Nuit des temps]
Camus : [La Peste, L'Etranger, Le Mythe de Sisyphe], cle de 'L'Etranger' : etranger
modules : library.app, library.service, library.model ; nomme true
library.service.internal exporte a library.app true, a library.model false, a tous false
library.app lit library.model true (par transitivite), library.model lit library.app false
--- describe-module library.service
exports library.service
library.service
qualified exports library.service.internal to library.app
requires java.base mandated
requires library.model transitive
--- describe-module du jar library.app

contains library.app
library.app
main-class library.app.Main
requires java.base mandated
requires library.service
--- depuis les jars
catalogue : 9 livres, par genre {ROMAN=3, SF=4, POLAR=1, ESSAI=1}
--- intrus
error: package library.service.internal is not visible
(package library.service.internal is declared in module library.service, which does not export it to module library.intruder)
```
