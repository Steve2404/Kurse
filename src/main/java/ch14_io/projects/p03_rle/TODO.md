# Projet 3 — Le compresseur (les flux `java.io`)

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 14) :**
- **flux d'OCTETS** (`InputStream`/`OutputStream`) contre flux de **CARACTÈRES** (`Reader`/`Writer`) ;
- **`FileInputStream`/`FileOutputStream`**, enveloppés dans `BufferedInputStream`/`BufferedOutputStream` ;
- **`read()`** rend un `int` de 0 à 255, ou **-1** à la fin ; `read(byte[])` rend le nombre d'octets lus ; `& 0xFF` corrige le signe d'un `byte` ;
- **les caractères :**
  - `FileWriter` (en mode **ajout** avec `true`), `BufferedWriter.newLine()`, `PrintWriter.printf` ;
  - `FileReader`, `BufferedReader.readLine()` (qui rend `null` à la fin) ;
- **les encodages :** `OutputStreamWriter`/`InputStreamReader` avec `StandardCharsets` ;
- **se déplacer dans un flux :** `mark`, `reset`, `skip`, `markSupported` ;
- **`DataOutputStream`/`DataInputStream`**, avec `EOFException` ;
- **`java.io.File`** : `mkdirs`, `exists`, `isDirectory`, `length`, `getName`.

Côté algorithmes :
- la **compression RLE** (chaque série devient un couple `(longueur ≤ 255, octet)`), et la décompression ;
- une **somme de contrôle Adler-32** codée à la main, comparée à `java.util.zip.Adler32`.

**Ce que TU crées :** dans `ch14_io.projects.p03_rle` : `Rle` et **`StreamLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 14.

---

## Tableau de bord

### ☐ Étape 1 — RLE et Adler-32

```
dossier cree true, existe true, isDirectory true, nom p03_rle
RLE : 59800 -> 922 octets (1 %), restaure identique true
Adler-32 maison bb8f19b3, JDK bb8f19b3, identiques true
```
- **Le bac à sable :** supprime-le s'il existe (comme au projet 2), puis `new File(Data.SANDBOX)` : `mkdirs()`, `exists()`, `isDirectory()`, `getName()`.
- **Le fichier `brut.bin`** : avec `new BufferedOutputStream(new FileOutputStream(…))`, écris, pour chaque série `i < Data.RUNS`, l'octet `Data.value(i)` répété `Data.length(i)` fois.
- **`Rle.compress(String from, String to)`** :
  1. lis avec `read()` jusqu'à -1, en comptant les octets égaux consécutifs ;
  2. écris `count`, puis l'octet, à chaque changement d'octet **ou** quand `count` atteint 255.
- **`Rle.decompress`** : lis des couples `(count, octet)`, et réécris l'octet `count` fois.
- **L'affichage :** `File.length()` des deux fichiers, le pourcentage entier `compressé * 100 / brut`, puis `Files.mismatch(brut, restauré) == -1`.
- **`Rle.adler32(String file)`** :
  - `a = 1`, `b = 0` ;
  - lis par blocs de 4 096 avec `read(buffer)` ;
  - pour chaque octet, `a = (a + (octet & 0xFF)) % 65521`, puis `b = (b + a) % 65521` ;
  - rends `b << 16 | a`.
  
  Compare avec `Adler32.update(Files.readAllBytes(…))`, en hexadécimal (`Long.toHexString`).
- **Question :** pourquoi `read()` rend-il un `int` et non un `byte` ?

### ☐ Étape 2 — Texte, ajout, encodages

```
rapport : 5 lignes, derniere "total;;24.10"
encodages : 10 caracteres, UTF-8 14 octets, ISO-8859-1 10 octets ; UTF-8 relu en ISO-8859-1 : 14 caracteres, identique false
```
- **`rapport.txt`** :
  1. `new BufferedWriter(new FileWriter(…))` écrit l'en-tête `article;quantite;prix`, puis chaque ligne de `Data.ITEMS`, chacune suivie de `newLine()` ;
  2. puis `new PrintWriter(new FileWriter(…, true))` **ajoute** `printf(Locale.ROOT, "total;;%.2f%n", total)`, où le total est la somme des quantités × prix ;
  3. relis avec `BufferedReader`/`readLine` : compte les lignes, et garde la dernière.
- **Les encodages :** écris `Data.ACCENTS` avec `OutputStreamWriter(…, StandardCharsets.UTF_8)` dans `utf8.txt`, puis en `ISO_8859_1` dans `latin1.txt`.
  - Relis `utf8.txt` avec `InputStreamReader(…, ISO_8859_1)`, caractère par caractère (`read()` jusqu'à -1).
  - Affiche la longueur du texte, la taille des deux fichiers, puis la longueur relue et l'égalité.
- **Question :** pourquoi `é` prend-il 2 octets en UTF-8, et 1 en ISO-8859-1 ?

### ☐ Étape 3 — `mark`/`reset`, données typées

```
mark/reset : markSupported true, lecture artt|5|q
DataStream : 2026 3.5 fin, 17 octets, puis EOFException
```
- **`mark`/`reset`**, sur `BufferedReader(new FileReader(rapport))`, dans cet ordre :
  1. lis 2 caractères, puis `mark(100)` ;
  2. lis 1 caractère, puis `reset()`, puis lis 1 caractère ;
  3. `skip(5)` ;
  4. lis 1 caractère.
  
  La trace est : les caractères lus, `|`, le résultat de `skip`, `|`, puis le dernier caractère.
- **`donnees.bin`** :
  - `DataOutputStream` écrit `writeInt(2026)`, `writeDouble(3.5)` et `writeUTF("fin")` ;
  - `DataInputStream` relit dans le **même ordre**, puis un `readInt()` de trop → `catch (EOFException e)` ;
  - affiche la taille du fichier.
- **Expériences :**
  - oublie le `try-with-resources` sur le `BufferedOutputStream` de `brut.bin` : la taille est-elle encore 59 800 ?
  - `System.console()` dans l'IDE, puis dans un vrai terminal : que vaut-elle ?

---

## Checklist (vérifiée par `Check`)

- `Data.SANDBOX`, `Data.RUNS`, `Data.ITEMS`, `Data.ACCENTS`, `new File(`, `.mkdirs()` ;
- `new BufferedOutputStream(new FileOutputStream(`, `new BufferedInputStream(new FileInputStream(`, `.read()`, une boucle `(b = in.read()) != -1`, `.read(buffer)`, `& 0xFF` ;
- `new BufferedWriter(new FileWriter(`, `.newLine()`, `new PrintWriter(new FileWriter(`, `, true)`, `.printf(`, `new BufferedReader(new FileReader(`, `.readLine()` ;
- `new OutputStreamWriter(`, `new InputStreamReader(`, `StandardCharsets.UTF_8`, `StandardCharsets.ISO_8859_1` ;
- `.mark(`, `.reset()`, `.skip(`, `.markSupported()` ;
- `new DataOutputStream(`, `new DataInputStream(`, `catch (EOFException`, `Adler32`.

---

## Sortie attendue complète

```
dossier cree true, existe true, isDirectory true, nom p03_rle
RLE : 59800 -> 922 octets (1 %), restaure identique true
Adler-32 maison bb8f19b3, JDK bb8f19b3, identiques true
rapport : 5 lignes, derniere "total;;24.10"
encodages : 10 caracteres, UTF-8 14 octets, ISO-8859-1 10 octets ; UTF-8 relu en ISO-8859-1 : 14 caracteres, identique false
mark/reset : markSupported true, lecture artt|5|q
DataStream : 2026 3.5 fin, 17 octets, puis EOFException
```
