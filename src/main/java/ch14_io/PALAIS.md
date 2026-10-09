# 🏠 Palais mental — chapitre 14 : la terrasse, stations 1 à 6

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Toute la maison, du salon à la douche 2 (chapitres 1 à 13), vient avant dans la balade.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 1. 🚪 La porte-fenêtre — `Path` et ses opérations

- **Image :** la porte-fenêtre donne sur un **jardin de chemins**. `Path.of("a/b")` trace un chemin, sans vérifier qu'il existe. **`normalize()`** est un **balai** qui efface les `.` et les `..` inutiles. **`resolve(x)`** colle `x` au bout du chemin, sauf si `x` est **absolu** : alors `x` **gagne tout seul**. **`relativize`** trace le chemin **de l'un vers l'autre**, mais **refuse** de mélanger un chemin absolu et un relatif. **`toRealPath()`** va **vraiment marcher** jusqu'au fichier : s'il n'existe pas, `IOException`.
- **À retenir :**
  - `Path.of(…)` / `Paths.get(…)` ; `new File(…)` est l'ancienne API ; `file.toPath()`, `path.toFile()` ;
  - les opérations sur `Path` ne touchent **pas** au disque, sauf `toRealPath()` (fichier requis, `IOException`) ;
  - `normalize()` retire `.` et `..` ; `resolve(absolu)` rend l'absolu ;
  - `relativize` : les deux absolus ou les deux relatifs, sinon `IllegalArgumentException` ;
  - `getFileName()`, `getParent()`, `getRoot()`, `getNameCount()`, `subpath(debut, fin)` (fin exclue, sans la racine).
- **Mon image :** …

### 2. 🍽️ La table — `Files` : créer, copier, déplacer, supprimer

- **Image :** sur la table, une **boîte à outils `Files`**. `createDirectory` ne construit **qu'un étage** : si le parent manque, il tombe (`NoSuchFileException`) ; si le dossier existe déjà, il crie `FileAlreadyExistsException`. `createDirectories` construit **tous les étages**, sans râler. `copy` et `move` refusent d'**écraser** sans l'option `REPLACE_EXISTING`. `delete` d'un **dossier plein** : `DirectoryNotEmptyException` ; d'un fichier absent : `NoSuchFileException`. `deleteIfExists` rend juste **vrai ou faux**.
- **À retenir :**
  - `createDirectory` (un niveau, erreur s'il existe) contre `createDirectories` (tous les niveaux, pas d'erreur s'il existe) ;
  - `copy(source, cible)`, `move(source, cible)` : `FileAlreadyExistsException` sans `StandardCopyOption.REPLACE_EXISTING` ; `ATOMIC_MOVE` ;
  - `delete` : `NoSuchFileException`, `DirectoryNotEmptyException` ; `deleteIfExists` → `boolean` ;
  - `exists`, `isDirectory`, `isRegularFile`, `isSameFile`, `size`, `getLastModifiedTime`.
- **Mon image :** …

### 3. 🪑 Les chaises — les flux d'entrée/sortie

- **Image :** autour de la table, deux familles de chaises. Les chaises **`InputStream` / `OutputStream`** mangent des **octets** ; les chaises **`Reader` / `Writer`** mangent des **caractères**. Une chaise **basse** (`FileInputStream`, `FileReader`) touche directement le fichier ; une chaise **haute** (`BufferedReader`, `ObjectInputStream`, `PrintWriter`) se **pose sur** une chaise basse. Quand le plat est fini, `read()` rend **−1**, et `readLine()` rend **`null`**.
- **À retenir :**
  - octets : `InputStream`, `OutputStream` ; caractères : `Reader`, `Writer` ;
  - bas niveau : `FileInputStream`, `FileOutputStream`, `FileReader`, `FileWriter` ; haut niveau (enveloppe) : `Buffered…`, `Object…Stream`, `PrintWriter`, `PrintStream` ;
  - `read()` → −1 en fin de flux ; `readLine()` → `null` ;
  - fermer la chaise haute ferme aussi la basse ; toujours un `try` avec ressources.
- **Mon image :** …

### 4. ⛱️ Le parasol — lire et parcourir avec `Files`

- **Image :** le parasol a des **baleines** : `readAllLines` déverse **tout le fichier d'un coup** dans une liste (attention aux gros fichiers). `lines` fait couler les lignes **une par une** dans un stream, à **fermer**. `readString` et `writeString` d'un seul geste. Sous le parasol, trois **explorateurs** : `list` (un seul niveau), `walk` (tous les niveaux, ou une profondeur donnée), `find` (avec un critère sur le chemin **et** ses attributs).
- **À retenir :**
  - `Files.readAllLines(p)` → `List<String>` ; `Files.lines(p)` → `Stream<String>` paresseux (à fermer) ; `readString`, `writeString`, `write` ;
  - `newBufferedReader(p)`, `newBufferedWriter(p)` ;
  - `Files.list(dossier)` : un niveau ; `Files.walk(dossier, profondeur)` : en profondeur ; `Files.find(dossier, profondeur, (chemin, attributs) -> …)` ;
  - `walk` ne suit pas les liens symboliques par défaut (`FileVisitOption.FOLLOW_LINKS`).
- **Mon image :** …

### 5. 🪴 Les plantes en pot — la sérialisation

- **Image :** tu mets une plante **en conserve** (`writeObject`). Seules les plantes marquées **`Serializable`** entrent dans le bocal. Les feuilles **`transient`** et les feuilles **`static`** ne sont **pas** mises en conserve. Quand tu rouvres le bocal (`readObject`), la plante revient **sans repasser** par son constructeur ni ses initialiseurs ; seul le constructeur sans argument du **premier ancêtre non sérialisable** s'exécute. Les feuilles `transient` reviennent avec leur valeur **par défaut** (`0`, `null`).
- **À retenir :**
  - `Serializable` : interface **marqueur** ; tous les champs non `transient` doivent être sérialisables, sinon `NotSerializableException` ;
  - `transient` et `static` ne sont **pas** sérialisés ; à la lecture : valeur par défaut ;
  - à la désérialisation : ni constructeur ni initialiseurs de la classe ; le constructeur sans argument du premier parent **non** `Serializable` s'exécute ;
  - `ObjectOutputStream.writeObject`, `ObjectInputStream.readObject` (→ `Object`, à caster) ; fin du flux → `EOFException` ; `serialVersionUID`.
- **Mon image :** …

### 6. 🔥 Le barbecue — la console et les flux standard

- **Image :** au barbecue, un **interphone `Console`** : parfois il n'y en a **pas** (`System.console()` rend `null`, dans un IDE par exemple). Il demande `readLine` ou `readPassword`, qui rend un **tableau de `char`** (qu'on peut effacer, contrairement à une `String`). Trois **cheminées** fument : `System.in`, `System.out`, `System.err`. Un `PrintWriter` ne **crie jamais** : il ne lance pas d'`IOException`, il faut regarder `checkError()`.
- **À retenir :**
  - `System.console()` peut rendre `null` ; `readLine()`, `readPassword()` → `char[]`, `printf`, `writer()`, `reader()` ;
  - `System.in` (`InputStream`), `System.out` et `System.err` (`PrintStream`) ;
  - `PrintStream` et `PrintWriter` ne lancent pas d'`IOException` : `checkError()` ;
  - `BasicFileAttributes` via `Files.readAttributes(p, BasicFileAttributes.class)` ; `BasicFileAttributeView` pour modifier.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 1 : que rend `Path.of("/a").resolve("/b")` ?
2. Station 2 : quelle différence entre `createDirectory` et `createDirectories` ?
3. Station 3 : que rend `read()` à la fin d'un flux ?
4. Station 4 : quelle méthode charge tout le fichier en mémoire ?
5. Station 5 : quel constructeur s'exécute à la désérialisation ?
6. Station 6 : pourquoi `readPassword()` rend-il un `char[]` ?
