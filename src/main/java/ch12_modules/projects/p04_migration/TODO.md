# Projet 4 — La migration (modules nommés, automatiques, sans nom)

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 12) :**
- **les trois sortes de modules** :
  - **nommé** (un `module-info`) ;
  - **automatique** (un jar **sans** `module-info` placé sur le **module path**) ;
  - **sans nom** (tout le code du **classpath**) ;
- **le nom d'un module automatique** : soit `Automatic-Module-Name` dans le manifeste, soit un nom déduit du fichier (`acme-text-2.1.jar` → `acme.text`, version 2.1) ;
- **ce qu'un module automatique fait :** il exporte tous ses paquets et lit tous les modules, y compris le module sans nom. Un module **nommé** ne lit **pas** le module sans nom ;
- **la migration *top-down*** : l'application devient un module nommé avant ses dépendances ;
- **les erreurs :** `module not found`, et le **cycle** de `requires` (interdit) ;
- `jar --manifest`, `java -cp`, `jdeps -s`.

Côté algorithme : des **slugs d'URL uniques** (`l-ete-a-paris`, puis `l-ete-a-paris-2`…). On retire les accents avec `java.text.Normalizer` (forme NFD, puis `\p{M}`).

**Ce que TU crées :** dans `ch12_modules/p04_migration/` :
- `legacy/`, le vieux code **sans** module ;
- `manifest.txt` ;
- `src/blog.app/`, le module nommé ;
- `cycles/`, deux modules en cycle.

Et ton script `build.sh`, dans ce dossier.

**Règle du crescendo :** chapitres 1 à 12.

---

## Tableau de bord

### ☐ Étape 1 — Le vieux code (sans `module-info`)

- **`legacy/com/acme/text/Slugify.java`** : `static String slug(String title)` :
  1. `Normalizer.normalize(title, Normalizer.Form.NFD).replaceAll("\\p{M}", "")` ;
  2. en minuscules (`Locale.ROOT`) ;
  3. `[^a-z0-9]+` devient `-` ;
  4. retire les `-` au début et à la fin.
- **`legacy/com/acme/utils/Strings.java`** : `static String shorten(String text, int max)`. Le texte reste tel quel s'il tient, sinon on garde les `max - 1` premiers caractères, suivis de `~`.
- **`legacy/com/acme/demo/Demo.java`** (un `main`) affiche `classpath : <slug("Bonjour le Monde")> ; module nomme <isNamed()>, nom <getName()>`.
- **`manifest.txt`** : une ligne `Automatic-Module-Name: com.acme.utils`.

### ☐ Étape 2 — Le module nommé et le cycle

- **`src/blog.app/module-info.java`** : `requires acme.text;` et `requires com.acme.utils;`.
- **`blog.app.Main`**, avec les titres :
  ```java
  static final String[] TITLES = {"L'Ete a Paris !", "Java 17 : les modules", "L'ete a Paris", "Ete a Paris", "Les modules, enfin..."};
  ```
  - **`uniqueSlugs(String[])`** : une `HashMap` de comptes (`merge`). La 1re fois, le slug tel quel ; la n-ième fois, `slug-n`.
  - **Les lignes :**
    - `slugs : <liste>` ;
    - `courts : <shorten("Les modules de Java 17", 12)> | <shorten("JPMS", 12)>` ;
    - pour `Main.class`, `Slugify.class` et `Strings.class` : `<nom simple> -> module <nom>, automatique <getDescriptor().isAutomatic()>, lit le module sans nom <canRead(ClassLoader.getSystemClassLoader().getUnnamedModule())>`.
- **`cycles/cycle.a/module-info.java`** (`requires cycle.b;`) et **`cycles/cycle.b/module-info.java`** (`requires cycle.a;`).

### ☐ Étape 3 — Le script `build.sh`

```
--- describe-module des jars


No module descriptor found. Derived automatic module.
acme.text@2.1 automatic
...
--- cycle
error: cyclic dependence involving cycle.b
```
- **En tête :** `P=ch12_modules/p04_migration` et `OUT=build/ch12/p04_migration`.
- **Les commandes :**
  1. `javac -d "$OUT/legacy" $(find "$P/legacy" -name "*.java")` ; puis, dans `$OUT/jars` :
     - `acme-text-2.1.jar`, avec seulement `com/acme/text` (`-C "$OUT/legacy" com/acme/text`) ;
     - `old_utils.jar`, avec `--manifest "$P/manifest.txt"` et seulement `com/acme/utils` ;
  2. `echo "--- describe-module des jars"`, puis pour chaque jar : `jar -J-Duser.language=en --describe-module --file … | sed 's/ jar:.*//' | sort`.
     - `-J` passe une option à la JVM de l'outil. Sans elle, `jar` répond dans la langue de la machine (en allemand ici) ;
  3. `echo "--- classpath"`, puis `java -cp "$OUT/legacy" com.acme.demo.Demo` ;
  4. `echo "--- javac sans module path"`, puis `javac -d "$OUT/mods" --module-source-path "$P/src" -m blog.app`, filtré par `2>&1 | grep "error:" | sed 's/.*error:/error:/' || true` ;
  5. la vraie compilation, avec `-p "$OUT/jars"` ; puis `$OUT/jars/blog.app.jar` avec `--main-class blog.app.Main` ; puis `echo "--- module path"` et `java -p "$OUT/jars" -m blog.app` ;
  6. `echo "--- cycle"`, puis `javac -d "$OUT/cycles" --module-source-path "$P/cycles" -m cycle.a,cycle.b`, filtré comme en 4, avec un `| sort` en plus (l'ordre des deux erreurs varie) ;
  7. `echo "--- jdeps"`, puis :
     - `jdeps -s "$OUT/jars/acme-text-2.1.jar"` ;
     - `jdeps -s --module-path "$OUT/jars" -m blog.app`.
     
     **Attention :** pour `jdeps`, `-p` veut dire `--package`, et non `--module-path`.
- **Questions :**
  - Pourquoi le nom du 2e jar n'est-il pas `old.utils` ?
  - Dans quel module vit `Demo` ? Pourquoi `blog.app` ne pourrait-il pas faire `requires` vers lui ?
- **Expériences :**
  - renomme le jar en `acme_text.jar` : quel nom de module ?
  - mets `acme-text-2.1.jar` sur le **classpath** au lieu du module path pour compiler `blog.app` : quelle erreur ?

---

## Checklist (vérifiée par `Check`)

- **Dans les `module-info` et le manifeste** : `module blog.app`, `requires acme.text;`, `requires com.acme.utils;`, `Automatic-Module-Name`, `module cycle.a`, `requires cycle.b;`, `requires cycle.a;`.
- **Dans le Java** : `.isAutomatic()`, `.getUnnamedModule()`, `.merge(`.
- **Dans le script** : `acme-text-2.1.jar`, `old_utils.jar`, `--manifest`, `jar -J-Duser.language=en --describe-module`, `java -cp`, `com.acme.demo.Demo`, `-p "$OUT/jars" --module-source-path`, `-m cycle.a,cycle.b`, `jdeps -s`, `jdeps -s --module-path`.

---

## Sortie attendue complète

```
--- describe-module des jars


No module descriptor found. Derived automatic module.
acme.text@2.1 automatic
contains com.acme.text
requires java.base mandated


No module descriptor found. Derived automatic module.
com.acme.utils automatic
contains com.acme.utils
requires java.base mandated
--- classpath
classpath : bonjour-le-monde ; module nomme false, nom null
--- javac sans module path
error: module not found: acme.text
error: module not found: com.acme.utils
--- module path
slugs : [l-ete-a-paris, java-17-les-modules, l-ete-a-paris-2, ete-a-paris, les-modules-enfin]
courts : Les modules~ | JPMS
Main -> module blog.app, automatique false, lit le module sans nom false
Slugify -> module acme.text, automatique true, lit le module sans nom true
Strings -> module com.acme.utils, automatique true, lit le module sans nom true
--- cycle
error: cyclic dependence involving cycle.a
error: cyclic dependence involving cycle.b
--- jdeps
acme-text-2.1.jar -> java.base
blog.app -> acme.text
blog.app -> com.acme.utils
blog.app -> java.base
```
