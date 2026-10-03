# Drill de rappel 3 — Les flux `java.io`

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall03`** dans le paquet `ch14_io.drills.r03_iostreams`. Le `main` déclare `throws IOException`.
- Travaille dans `build/ch14/r03_iostreams` (`createDirectories`).

## Défis

- ☐ **D01.** Avec un `FileOutputStream` vers `octets.bin` : `write(65)`, `write(new byte[] {66, 67, 68})`, puis `write(-1)`. Relis avec `BufferedInputStream(FileInputStream)`, octet par octet (`read()` jusqu'à -1). Affiche les valeurs, ` | `, puis la taille.
  → `D01 : 65 66 67 68 255 | 5`
- ☐ **D02.**
  1. `BufferedWriter(FileWriter)` écrit `un`, `newLine()`, `deux` ;
  2. puis `new FileWriter(txt, true)` ajoute `System.lineSeparator() + "trois"` ;
  3. relis avec `BufferedReader`/`readLine`.
  
  Affiche le nombre de lignes, puis la dernière.
  → `D02 : 3 trois`
- ☐ **D03.** Un `PrintWriter` sur un `StringWriter` :
  - `print("a")`, `println(1)` ;
  - `printf(Locale.ROOT, "%5.2f|%-4s|%03d", 3.14159, "ok", 7)` ;
  - `format(Locale.ROOT, "%n%s", true)`.
  
  Affiche le texte, avec les sauts de ligne remplacés par `/`.
  → `D03 : a1/ 3.14|ok  |007/true`
- ☐ **D04.** `"é".getBytes` en UTF-8, puis en ISO-8859-1 : les longueurs. Puis `new String(utf8, UTF_8).equals("é")`, et la longueur de `new String(utf8, ISO_8859_1)`.
  → `D04 : 2 1 true 2`
- ☐ **D05.** `BufferedReader(new StringReader("ABCDEFG"))`, dans cet ordre :
  1. lis 1 caractère, puis `mark(10)` ;
  2. lis 2 caractères, puis `reset()`, puis lis 1 caractère ;
  3. ajoute le résultat de `skip(2)`, puis lis 1 caractère.
  
  Puis `markSupported()` d'un `ByteArrayInputStream`, et d'un `BufferedInputStream` qui l'enveloppe.
  → `D05 : ABCB2E true true`
- ☐ **D06.** Un `PrintStream` vers un `ByteArrayOutputStream` : `print("capture")`. Affiche le contenu capturé, puis `System.out instanceof PrintStream`, puis `System.err != System.out`.
  → `D06 : capture true true`

## Expériences (hors sortie attendue)

1. `new BufferedReader(new FileInputStream(f))` : compile-t-il ? Pourquoi ?
2. Oublie `close()` (et `flush()`) sur un `BufferedWriter` : que contient le fichier ?
3. `PrintWriter` lève-t-il des `IOException` ? Comment savoir qu'une écriture a échoué ?

## Sortie attendue complète

```
D01 : 65 66 67 68 255 | 5
D02 : 3 trois
D03 : a1/ 3.14|ok  |007/true
D04 : 2 1 true 2
D05 : ABCB2E true true
D06 : capture true true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| | Octets | Caractères |
|---|---|---|
| Classes de base (abstraites) | `InputStream` / `OutputStream` | `Reader` / `Writer` |
| Fichier | `FileInputStream` / `FileOutputStream` | `FileReader` / `FileWriter` |
| Tampon | `BufferedInputStream` / `BufferedOutputStream` | `BufferedReader` (`readLine`) / `BufferedWriter` (`newLine`) |
| Formatage | `PrintStream` (`System.out`, `System.err`) | `PrintWriter` |
| Objets / données | `ObjectInputStream`, `DataInputStream`… | — |
| Passerelle | — | `InputStreamReader` / `OutputStreamWriter` (avec un `Charset`) |

- **On n'enveloppe que du même « type »** : un `Reader` dans un `Reader`, un `InputStream` dans un `InputStream`. Les passerelles font la conversion.
- **`read()`** rend -1 à la fin ; **`readLine()`** rend `null`.
- **`write(int)`** n'écrit que les 8 bits de poids faible.
- **`FileWriter(f, true)`** ajoute à la fin. `close()` fait aussi `flush()`.
- **`mark(limite)` et `reset()`** ne marchent que si `markSupported()` (les flux bufferisés, pas `FileInputStream`).
- **`PrintStream` et `PrintWriter`** ne lèvent pas d'`IOException` : voir `checkError()`.

</details>
