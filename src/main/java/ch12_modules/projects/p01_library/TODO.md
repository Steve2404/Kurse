# Projet 1 — La bibliothèque en trois modules

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

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

---

## Tableau de bord

### ☐ Étape 1 — Les trois modules

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
