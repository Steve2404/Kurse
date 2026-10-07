# Projet 1 — Le plan du site (`Path` sans toucher au disque)

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 14) :**
- **créer un chemin :** `Path.of(…)`, `Paths.get(…)`, plusieurs morceaux ou une chaîne avec `/` ;
- **décomposer :** `getFileName`, `getParent`, `getRoot` (`null` pour un chemin relatif), `getNameCount`, `getName(i)`, `subpath(début, fin)` ;
- **comparer :**
  - `startsWith` et `endsWith` comparent des **noms entiers**, pas des caractères ;
  - `equals` ne normalise pas ;
  - `isAbsolute`, `toAbsolutePath` ;
- **combiner :** `resolve`, `resolveSibling`, `normalize` (retire `.`, résout `..`), `relativize` (le chemin pour aller de A à B) ;
- **`java.io.File`** ↔ `Path` (`toFile`, `toPath`) ;
- **les erreurs :** `IllegalArgumentException` et `InvalidPathException`.

Côté algorithmes :
- les **liens relatifs** d'un site (`relativize` depuis le dossier de la page source), avec un aller-retour de vérification et la détection des liens cassés ;
- l'**ancêtre commun** de deux chemins ;
- un **mini-shell** `cd`, qui refuse de sortir de la racine ;
- l'**arborescence** du site.

**Ce que TU crées :** dans `ch14_io.projects.p01_paths` : **`PathLab`** (le `main`).

**À retenir pour TOUT le chapitre :** sous Windows, un `Path` s'affiche avec `\`. Écris `static String show(Path p)`, qui rend `p.toString().replace('\\', '/')`, et affiche toujours tes chemins avec.

**Règle du crescendo :** chapitres 1 à 14. Pas de JDBC, de `System.exit` ni de `printStackTrace`.

**Ce que le chapitre 14 t'apprend :** lire et écrire des **fichiers**, parcourir des **dossiers**, et sauvegarder des **objets** sur le disque. Deux familles d'outils :
- `java.nio.file` (`Path`, `Files`) : la façon moderne ;
- `java.io` (`File`, les flux `InputStream`, `Reader`…) : l'ancienne, toujours très utilisée.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : un carnet de recettes.

**Tes programmes travaillent dans un « bac à sable »**, `build/ch14/<projet>/`, qu'ils effacent et recréent au début : tu ne risques rien pour tes vrais fichiers. Relis la section 3 du `PARCOURS.md`.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch14-p01 -sourcepath src/main/java src/main/java/ch14_io/projects/p01_paths/PathLab.java
java "-Duser.language=fr" -cp build/ch14-p01 ch14_io.projects.p01_paths.PathLab
```

Lance-les depuis le dossier `Kurse` : les chemins du programme partent de là.

---

## Tableau de bord

### ☐ Étape 1 — Décomposer, comparer

```
decomposition : docs/api/io/files.html egal true, nom files.html, parent docs/api/io, racine null, 4 noms, getName(1) api, subpath(1, 3) api/io
comparaisons : startsWith("docs") true, startsWith("doc") false, endsWith("io/files.html") true, absolu false, toAbsolutePath absolu true
```

**📖 La leçon : `Path`, un chemin.** Un `Path` décrit **où** est un fichier ou un dossier. Le créer ne touche **pas** au disque : le fichier n'a même pas besoin d'exister.

```java
Path p = Path.of("carnet", "recettes", "tarte.txt");    // ou Path.of("carnet/recettes/tarte.txt")
p.getFileName()       // tarte.txt
p.getParent()         // carnet/recettes
p.getNameCount()      // 3 : les noms, numérotés à partir de 0
p.getName(1)          // recettes
p.subpath(0, 2)       // carnet/recettes (la fin est exclue)
p.isAbsolute()        // false : il part du dossier courant
p.startsWith("carnet")    // true : compare des NOMS entiers
```

**⚠️ Sous Windows**, `p.toString()` affiche `carnet\recettes\tarte.txt`, avec des `\`. Pour un affichage identique partout, remplace-les : `p.toString().replace('\\', '/')`. C'est la méthode `show(Path)` du projet.

**👉 À toi :**

- `p = Path.of("docs", "api", "io", "files.html")`, comparé (`equals`) avec `Paths.get("docs/api/io/files.html")`.
- `endsWith(Path.of("io", "files.html"))`.

### ☐ Étape 2 — Normaliser, relativiser

```
normalisees : docs/guide/./install.html -> docs/guide/install.html blog/2026/../2025/bilan.html -> blog/2025/bilan.html
lien docs/guide/intro.html -> ../api/index.html, aller-retour true, ancetre commun 'docs'
lien docs/api/index.html -> io/paths.html (CASSE), aller-retour true, ancetre commun 'docs/api'
```

**📖 La leçon : combiner des chemins.**

```java
Path.of("carnet").resolve("courses.txt")                          // carnet/courses.txt : ajoute au bout
Path.of("carnet/./recettes/../photos").normalize()                // carnet/photos : retire . et ..
Path.of("carnet/recettes").relativize(Path.of("carnet/photos/gateau.jpg"))   // ../photos/gateau.jpg
```

`relativize` répond à la question : « depuis ce dossier, comment aller là-bas ? ».

**👉 À toi :**

- **Les pages :** normalise chaque page de `Data.PAGES`, et range les formes normalisées (affichées avec `show`) dans un `TreeSet`. Affiche celles qui ont changé : ` <brut> -> <normalisé>`.
- **`static Path commonAncestor(Path a, Path b)`** : part de `Path.of("")`, et ajoute (`resolve`) les noms égaux en tête.
- **Pour chaque `source -> cible` de `Data.LINKS`** :
  - `href` = `source.getParent().relativize(cible normalisée)`, ou la cible elle-même si la source n'a pas de parent ;
  - `(CASSE)` si la cible n'est pas une page connue ;
  - l'aller-retour : `dossier source.resolve(href).normalize()` doit égaler la cible ;
  - l'ancêtre commun.

### ☐ Étape 3 — Le mini-shell et l'arborescence

```
shell | cd docs => /docs | cd guide => /docs/guide | ... | cd .. => refuse (hors racine) | ... | cd /tmp => refuse (absolu) | ...
arborescence :
blog/
  2025/
    bilan.html
```

**📖 Rappel :** `resolve` et `normalize` (étape 2). Un `TreeSet` pour garder les pages triées (chapitre 9).

**👉 À toi :**

- **Le shell :** le répertoire courant part de `Path.of("")`. Pour chaque commande `cd <arg>` de `Data.COMMANDS` :
  - un argument qui commence par `/` est refusé ;
  - sinon `next = cwd.resolve(arg).normalize()`. Si `next.startsWith("..")`, il est refusé (hors racine) ; sinon `cwd = next`, et le résultat est `"/" + show(cwd)`.
  - Chaque commande s'ajoute à la ligne sous la forme ` | <commande> => <résultat>`.
- **L'arborescence :** pour chaque page triée, affiche les dossiers pas encore affichés (avec `subpath(0, profondeur)`), en indentant de 2 espaces par niveau et en terminant par `/`. Puis affiche le nom du fichier.
- **Question :** pourquoi `cd docs//api` fonctionne-t-il ?

### ☐ Étape 4 — `resolve`, `File`, erreurs

```
resolve docs/guide/intro.html, resolveSibling docs/api, resolve(absolu) rend l'argument true ; File : nom intro.html, parent docs/guide, retour toPath true ; erreurs [...]
```

**📖 La leçon : `File`, l'ancienne classe.** Avant `Path`, Java utilisait `java.io.File`. On passe de l'un à l'autre : `path.toFile()` et `file.toPath()`. `file.getName()` et `file.getParent()` ressemblent à `getFileName()` et `getParent()` de `Path`, mais rendent des `String`.

**👉 À toi :**

- `guide = Path.of("docs/guide")` :
  - `resolve("intro.html")`, `resolveSibling("api")` ;
  - `guide.resolve(guide.toAbsolutePath())` égale-t-il l'argument ?
- `guide.resolve("intro.html").toFile()` : `getName()`, `getParent()` (avec `/`), puis `toPath()`, comparé.
- **Trois erreurs**, attrapées et nommées :
  - `guide.subpath(1, 5)` ;
  - `guide.relativize(guide.toAbsolutePath())` ;
  - `Path.of("a\u0000b")`.
- **Expériences :**
  - `Path.of("/a/b").isAbsolute()` sous Windows et sous Linux : même résultat ?
  - `Path.of("a/b").getRoot()` ;
  - `Path.of("").getNameCount()`.

---

## Checklist (vérifiée par `Check`)

- `Data.PAGES`, `Data.LINKS`, `Data.COMMANDS`, `Path.of(`, `Paths.get(` ;
- `.getFileName()`, `.getParent()`, `.getRoot()`, `.getNameCount()`, `.getName(`, `.subpath(` ;
- `.startsWith(`, `.endsWith(`, `.isAbsolute()`, `.toAbsolutePath()` ;
- `.normalize()`, `.relativize(`, `.resolve(`, `.resolveSibling(`, `.toFile()`, `.toPath()` ;
- `catch (IllegalArgumentException`, `catch (InvalidPathException`, `replace('\\', '/')`.

---

## Sortie attendue complète

```
decomposition : docs/api/io/files.html egal true, nom files.html, parent docs/api/io, racine null, 4 noms, getName(1) api, subpath(1, 3) api/io
comparaisons : startsWith("docs") true, startsWith("doc") false, endsWith("io/files.html") true, absolu false, toAbsolutePath absolu true
normalisees : docs/guide/./install.html -> docs/guide/install.html blog/2026/../2025/bilan.html -> blog/2025/bilan.html
lien docs/guide/intro.html -> ../api/index.html, aller-retour true, ancetre commun 'docs'
lien docs/api/io/files.html -> ../../../index.html, aller-retour true, ancetre commun ''
lien blog/2026/janvier.html -> ../2025/bilan.html, aller-retour true, ancetre commun 'blog'
lien index.html -> docs/guide/install.html, aller-retour true, ancetre commun ''
lien docs/api/index.html -> io/paths.html (CASSE), aller-retour true, ancetre commun 'docs/api'
shell | cd docs => /docs | cd guide => /docs/guide | cd ../api/./io => /docs/api/io | cd ../../.. => / | cd .. => refuse (hors racine) | cd blog/2026/../2025 => /blog/2025 | cd /tmp => refuse (absolu) | cd docs//api => /blog/2025/docs/api
arborescence :
blog/
  2025/
    bilan.html
  2026/
    janvier.html
docs/
  api/
    index.html
    io/
      files.html
  guide/
    install.html
    intro.html
index.html
resolve docs/guide/intro.html, resolveSibling docs/api, resolve(absolu) rend l'argument true ; File : nom intro.html, parent docs/guide, retour toPath true ; erreurs [subpath IllegalArgumentException, relativize relatif/absolu IllegalArgumentException, caractere nul InvalidPathException]
```
