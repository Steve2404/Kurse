# Projet 2 — La sauvegarde incrémentale (`Files`)

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 14) :**
- **créer et écrire :** `createDirectories` contre `createDirectory`, `writeString` ;
- **copier, déplacer, supprimer :**
  - `copy` (avec ou sans `StandardCopyOption.REPLACE_EXISTING`) ;
  - `move` ;
  - `delete` contre `deleteIfExists` ;
- **tester :** `exists`, `notExists`, `isDirectory`, `isRegularFile`, `isSameFile`, `size`, `mismatch` (Java 12 : position du 1er octet différent, ou -1) ;
- **parcourir :**
  - `Files.walk` (récursif), `Files.list` (un niveau), `Files.find` (avec un critère) ;
  - ces streams **ouvrent** des dossiers : il faut les **fermer** (try-with-resources) ;
- **les exceptions :** `FileAlreadyExistsException`, `DirectoryNotEmptyException`, `NoSuchFileException` (toutes des `IOException`).

Côté algorithmes :
- supprimer un **arbre** (du plus profond au moins profond) ;
- **copier** un arbre ;
- **comparer** deux arbres : ajouté, modifié (à quel octet), inchangé, supprimé ;
- une **sauvegarde incrémentale**, qui ne copie que ce qui a changé.

**Ce que TU crées :** dans `ch14_io.projects.p02_backup` : `Backup` et **`BackupLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 14.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch14-p02 -sourcepath src/main/java src/main/java/ch14_io/projects/p02_backup/BackupLab.java
java "-Duser.language=fr" -cp build/ch14-p02 ch14_io.projects.p02_backup.BackupLab
```

Tu peux regarder ce que ton programme a créé : dans le panneau Project d'IntelliJ, ouvre `build/ch14/p02_backup` (clic droit → **Reload from Disk** si rien n'apparaît).

---

## Tableau de bord

### ☐ Étape 1 — Les outils sur les arbres

**📖 La leçon : `Files`, agir sur le disque.** La classe `Files` contient des méthodes `static` qui, elles, touchent **vraiment** le disque :

```java
Path recettes = Path.of("carnet/recettes");
Files.createDirectories(recettes);                            // crée le dossier ET ses parents
Path tarte = recettes.resolve("tarte.txt");
Files.writeString(tarte, "farine\nbeurre\npommes");           // écrit un texte (crée ou remplace)
Files.write(recettes.resolve("crepes.txt"), List.of("farine", "lait", "oeufs"));   // écrit des lignes
Files.exists(tarte)              // true
Files.isDirectory(recettes)      // true
Files.size(tarte)                // 20 (octets)
Files.readAllLines(tarte)        // [farine, beurre, pommes]
Files.readString(tarte)          // tout le texte
Files.copy(tarte, autre);        // copie
Files.move(autre, ailleurs);     // déplace ou renomme
Files.delete(ailleurs);          // supprime
```

**📖 La leçon : parcourir un dossier.** `Files.walk(dossier)` donne un `Stream<Path>` du dossier et de **tout** ce qu'il contient, à tous les niveaux. Il garde le dossier ouvert : il faut le fermer, avec un try-with-resources (chapitre 11) :

```java
try (Stream<Path> s = Files.walk(Path.of("carnet"))) {
    s.filter(Files::isRegularFile).forEach(System.out::println);
}
```

Ces méthodes lancent des `IOException` (vérifiées) : déclare `throws IOException` (chapitre 11).

**👉 À toi :**

- **`final class Backup`**, avec `static String show(Path)` (avec `/`) et :
  - `deleteTree(Path root)` : rien si le dossier n'existe pas. Sinon, `Files.walk` dans un try-with-resources, trié par `Comparator.reverseOrder()`, puis `Files.delete` de chaque chemin ;
  - `List<String> files(Path root)` : les fichiers ordinaires, en chemins relatifs (`show(root.relativize(p))`), triés ;
  - `int copyTree(Path from, Path to)` : pour chaque chemin du `walk`, `createDirectories` si c'est un dossier, sinon `copy(…, REPLACE_EXISTING)`. Rend le nombre de fichiers copiés ;
  - `Map<String, String> diff(Path current, Path saved)` (une `TreeMap`) :
    - pour chaque fichier de `current` : `ajoute` s'il est absent de `saved`, sinon `inchange` ou `modifie (octet <mismatch>)` ;
    - puis `supprime` pour ceux de `saved` absents de `current` ;
  - `Map<String, Long> sizes(Path root)` : la taille cumulée par **premier** nom du chemin relatif (`groupingBy` + `summingLong`, dans une `TreeMap`).
- **Question :** pourquoi l'ordre inverse permet-il de supprimer un dossier non vide ?

### ☐ Étape 2 — Sauvegarder, modifier, comparer

```
source : [budget.csv, notes/idees.txt, ...] ; tailles par dossier {budget.csv=25, notes=43, photos=48}
sauvegarde complete : 6 fichiers copies ; diff [inchange]
apres modifications :
  budget.csv : supprime
  notes/budget.csv : ajoute
  notes/todo.txt : modifie (octet 26)
increment : 3 fichiers [notes/budget.csv, notes/courses.txt, notes/todo.txt]
```

**📖 La leçon : comparer deux fichiers.** `Files.mismatch(a, b)` (Java 12) rend `-1` si les deux fichiers ont exactement le même contenu, sinon la position du premier octet différent.

**👉 À toi :**

- **La source :** `deleteTree(Path.of(Data.SANDBOX))`, puis, pour chaque `chemin|contenu` de `Data.FILES`, `createDirectories(parent)` et `writeString` dans `source/`.
- **La sauvegarde complète :** `copyTree(source, sauvegarde-complete)`, puis les valeurs **distinctes** du `diff`.
- **Les modifications de la source**, dans cet ordre :
  1. `notes/todo.txt` devient `"acheter du pain\nappeler Leo"` ;
  2. création de `notes/courses.txt` (`"lait"`) ;
  3. suppression de `photos/2025/neige.jpg` ;
  4. `move` de `budget.csv` vers `notes/budget.csv`.
  
  Affiche le `diff`, une ligne par fichier (deux espaces devant).
- **L'incrément :** copie dans `increment/` (en créant les dossiers) seulement les `ajoute` et les `modifie`. Ici, `copy` sans option.

### ☐ Étape 3 — Ce qui échoue, et les parcours

```
  copy sur un fichier existant : FileAlreadyExistsException
  delete d'un dossier non vide : DirectoryNotEmptyException
  delete d'un absent : NoSuchFileException
  createDirectory sans parent : NoSuchFileException
  deleteIfExists d'un absent : false
isSameFile true, equals false, size 27, ...
list [notes, photos] ; find *.txt [notes/courses.txt, notes/idees.txt, notes/todo.txt]
```

**📖 La leçon : les options et les erreurs de `Files`.** Sans option, `copy` refuse d'écraser un fichier existant ; `StandardCopyOption.REPLACE_EXISTING` l'autorise. Chaque échec a sa propre exception (toutes filles d'`IOException`) : l'étape te les fait rencontrer une par une. `Files.deleteIfExists` ne se plaint pas si le fichier manque : elle rend `false`.

`Files.list(dossier)` ne donne que le **premier** niveau ; `Files.find(dossier, profondeur, test)` filtre en recevant chaque chemin **et** ses attributs.

**👉 À toi :**

- **Quatre erreurs**, chacune dans son `try`, plus un `deleteIfExists`. Affiche-les une par ligne, avec deux espaces devant :
  1. `copy` de `notes/todo.txt` sur `increment/notes/todo.txt`, sans option ;
  2. `delete(source/notes)` ;
  3. `delete(source/absent.txt)` ;
  4. `createDirectory(sandbox/a/b/c)` ;
  5. `deleteIfExists(source/absent.txt)`.
- **Ensuite :**
  - la même copie avec `REPLACE_EXISTING` (sans erreur) ;
  - `weird = source.resolve("notes/../notes/./todo.txt")` : `isSameFile(weird, …todo.txt)`, `equals`, `size(weird)` ;
  - `isDirectory` et `isRegularFile` de `notes`, puis `notExists` de la photo supprimée.
- **Les parcours :** `Files.list(source)` (relatif, trié), puis `Files.find(source, 10, (p, attrs) -> attrs.isRegularFile() && p.toString().endsWith(".txt"))`, chacun dans un try-with-resources.
- **Question :** pourquoi `isSameFile` dit `true` alors que `equals` dit `false` ?

---

## Checklist (vérifiée par `Check`)

- `Data.SANDBOX`, `Data.FILES` ;
- `Files.walk(`, `Comparator.reverseOrder()`, `Files.delete(`, `Files.createDirectories(`, `Files.writeString(` ;
- `Files.copy(`, `StandardCopyOption.REPLACE_EXISTING`, `Files.move(`, `Files.mismatch(` ;
- `Files.exists(`, `Files.size(`, `Files.isRegularFile`, `Files.isDirectory(` ;
- `catch (FileAlreadyExistsException`, `catch (DirectoryNotEmptyException`, `catch (NoSuchFileException` ;
- `Files.createDirectory(`, `Files.deleteIfExists(`, `Files.isSameFile(`, `Files.notExists(`, `Files.list(`, `Files.find(`, `try (Stream<Path>`.

---

## Sortie attendue complète

```
source : [budget.csv, notes/idees.txt, notes/todo.txt, photos/2025/neige.jpg, photos/2026/montagne.jpg, photos/2026/plage.jpg] ; tailles par dossier {budget.csv=25, notes=43, photos=48}
sauvegarde complete : 6 fichiers copies ; diff [inchange]
apres modifications :
  budget.csv : supprime
  notes/budget.csv : ajoute
  notes/courses.txt : ajoute
  notes/idees.txt : inchange
  notes/todo.txt : modifie (octet 26)
  photos/2025/neige.jpg : supprime
  photos/2026/montagne.jpg : inchange
  photos/2026/plage.jpg : inchange
increment : 3 fichiers [notes/budget.csv, notes/courses.txt, notes/todo.txt]
  copy sur un fichier existant : FileAlreadyExistsException
  delete d'un dossier non vide : DirectoryNotEmptyException
  delete d'un absent : NoSuchFileException
  createDirectory sans parent : NoSuchFileException
  deleteIfExists d'un absent : false
isSameFile true, equals false, size 27, isDirectory(notes) true, isRegularFile(notes) false, notExists true
list [notes, photos] ; find *.txt [notes/courses.txt, notes/idees.txt, notes/todo.txt]
```
