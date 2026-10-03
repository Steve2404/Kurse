# Drill de rappel 2 — Les opérations de `Files`

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall02`** dans le paquet `ch14_io.drills.r02_files`. Le `main` déclare `throws IOException`.
- Travaille dans `box = Path.of("build/ch14/r02_files")`, supprimé au départ (un `walk` trié à l'envers, puis `delete`).

## Défis

- ☐ **D01.** `dir = Files.createDirectories(box.resolve("a/b"))` et `f = Files.writeString(dir.resolve("f.txt"), "bonjour")`. Affiche `exists(f)`, `isRegularFile(f)`, `isDirectory(dir)`, `size(f)`, puis `readString(f)`.
  → `D01 : true true true 7 bonjour`
- ☐ **D02.** Dans cet ordre :
  1. `copy = Files.copy(f, box.resolve("copie.txt"))` ;
  2. recopie dessus **sans option** (attrape l'exception) ;
  3. réécris `f` avec `"bonsoir"` ;
  4. copie avec `REPLACE_EXISTING`.
  
  Affiche l'exception, `readString(copy)`, `mismatch(f, copy)`, puis `mismatch(f, box.resolve("a/b/f.txt"))`.
  → `D02 : FileAlreadyExistsException bonsoir -1 -1`
- ☐ **D03.** `moved = Files.move(copy, box.resolve("a/deplace.txt"))`. Affiche `exists(copy)`, `exists(moved)`, puis `isSameFile(moved, box.resolve("a/b/../deplace.txt"))`.
  → `D03 : false true true`
- ☐ **D04.** Affiche :
  - `delete(box.resolve("a"))` → exception ;
  - `delete(box.resolve("absent"))` → exception ;
  - `deleteIfExists(absent)`, puis `deleteIfExists(moved)`.
  → `D04 : DirectoryNotEmptyException NoSuchFileException false true`
- ☐ **D05.** Affiche :
  - `createDirectory(box.resolve("x/y"))` → exception ;
  - `createDirectory(dir)` → exception.
  
  Puis `createDirectories(dir)` sans erreur, et `notExists(box.resolve("x"))`.
  → `D05 : NoSuchFileException FileAlreadyExistsException true`
- ☐ **D06.** Trois parties, séparées par ` | ` :
  1. **`toRealPath()`** de `box.resolve("a/b/../b/f.txt")` : `isAbsolute()`, puis `endsWith(Path.of("a", "b", "f.txt"))`. Ensuite `toRealPath()` d'un fichier absent → nom de l'exception ;
  2. **`Files.copy` avec des flux** : d'un `ByteArrayInputStream` de `"abc"` vers `flux.txt`, puis de `flux.txt` vers un `ByteArrayOutputStream`. Affiche les deux nombres d'octets rendus, puis le contenu du flux de sortie ;
  3. **`java.io.File`** : dans le dossier `a/b`, `renameTo` de `f.txt` en `g.txt`, `list().length`, le nom du premier `listFiles()`, puis `delete()` de `g.txt` deux fois.
  → `D06 : true true NoSuchFileException | 3 3 abc | true 1 g.txt true false`

## Expériences (hors sortie attendue)

1. `Files.move` d'un dossier non vide vers un autre disque : que peut-il se passer ?
2. `Files.copy` d'un dossier : son contenu est-il copié ?
3. `Files.mismatch` sur deux fichiers de tailles différentes, avec le même début : que rend-il ?

## Sortie attendue complète

```
D01 : true true true 7 bonjour
D02 : FileAlreadyExistsException bonsoir -1 -1
D03 : false true true
D04 : DirectoryNotEmptyException NoSuchFileException false true
D05 : NoSuchFileException FileAlreadyExistsException true
D06 : true true NoSuchFileException | 3 3 abc | true 1 g.txt true false
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Méthode | Si la cible existe / manque |
|---|---|
| `createDirectory(d)` | existe → `FileAlreadyExistsException` ; parent absent → `NoSuchFileException` |
| `createDirectories(d)` | crée les parents ; sans erreur si `d` existe déjà |
| `copy(a, b[, options])` | b existe → `FileAlreadyExistsException`, sauf avec `REPLACE_EXISTING` ; pour un dossier, ne copie **pas** son contenu |
| `move(a, b[, options])` | idem ; `ATOMIC_MOVE` en une seule opération |
| `delete(p)` | absent → `NoSuchFileException` ; dossier non vide → `DirectoryNotEmptyException` |
| `deleteIfExists(p)` | rend `false` si absent |

- `exists`, `notExists`, `isDirectory`, `isRegularFile`, `isSymbolicLink`, `isReadable`, `isWritable`, `isHidden`, `size` ;
- `isSameFile(a, b)` regarde le **vrai** fichier (et `isSameFile(p, p)` vaut `true`, même si le fichier n'existe pas) ;
- `mismatch(a, b)` rend la position du premier octet différent, ou -1.
- **`toRealPath()`** : un chemin absolu et normalisé, avec les liens résolus. Le fichier doit **exister** (sinon `NoSuchFileException`) ; c'est la seule méthode de `Path` qui touche au disque.
- **`Files.copy(InputStream, Path[, options])`** et **`Files.copy(Path, OutputStream)`** rendent le nombre d'octets copiés.
- **`java.io.File`** (l'ancienne API) répond par des **booléens** : `exists`, `delete`, `renameTo`, `mkdir`/`mkdirs`. Elle a aussi `list()` (des noms), `listFiles()` (des `File`), `length()`, `lastModified()`, `getName()` et `getParent()`.

</details>
