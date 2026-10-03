# Drill de rappel 6 — Kata : flux standard, `Console`, choisir la bonne classe

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall06`** dans le paquet `ch14_io.drills.r06_kata`. Le `main` déclare `throws IOException`. Elle a :
  - `static int sum(InputStream in)` : un `Scanner` sur `in`, qui additionne tant que `hasNextInt()` ;
  - `static int vowels(Reader r) throws IOException` : compte les voyelles (`aeiouy`, sans tenir compte de la casse), caractère par caractère.

## Défis

- ☐ **D01.** `sum` d'un `ByteArrayInputStream` de `"4 8 15 16 23 42"` (en UTF-8), puis `vowels(new StringReader("Entrees Sorties"))`.
  → `D01 : 108 6`
- ☐ **D02.** Redirige `System.out` vers un `PrintStream` sur un `ByteArrayOutputStream` (avec `System.setOut`), affiche `capture`, puis **restaure** l'original. Affiche le contenu capturé.
  → `D02 : capture`
- ☐ **D03.** `Console console = System.console()` : rend `"pas de console"` si elle vaut `null`, sinon `"console presente"`. Affiche si la valeur est l'une des deux.
  → `D03 : true`
- ☐ **D04.** `k.txt` = `"ligne 1\nligne 2\nligne 3"`, dans `build/ch14/r06_kata`. Affiche :
  - la 2e ligne (`Files.lines` avec `skip(1).findFirst()`, dans un try-with-resources) ;
  - `readString(…).length()` ;
  - `Files.size(…)`.
  → `D04 : ligne 2 23 23`

## Expériences (hors sortie attendue)

1. Dans un vrai terminal, utilise `console.readLine("Nom ? ")` et `console.readPassword("Code ? ")`. Pourquoi `readPassword` rend-il un `char[]`, et pas une `String` ?
2. Lance D03 depuis l'IDE, puis depuis un terminal : `System.console()` change-t-il ?
3. Ferme `System.in` (avec un try-with-resources sur un `Scanner`), puis relis : que se passe-t-il ?

## Sortie attendue complète

```
D01 : 108 6
D02 : capture
D03 : true
D04 : ligne 2 23 23
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Les flux standard :**
  - `System.in` est un `InputStream` ;
  - `System.out` et `System.err` sont des `PrintStream` ;
  - on peut les remplacer avec `System.setIn`, `System.setOut`, `System.setErr`.
- **Ne ferme pas `System.in`** (ni un `Scanner` dessus) si tu dois relire plus tard.
- **`Console`** (`System.console()`, qui peut être `null`) :
  - `readLine()`, `readLine(format, args)` ;
  - `readPassword()`, qui rend un `char[]` à **effacer** après usage ;
  - `format()`/`printf()` ;
  - `writer()` (un `PrintWriter`) et `reader()` (un `Reader`) ;
  - pas d'exception vérifiée.
- **Choisir sa classe :**
  - du texte ligne à ligne → `BufferedReader`, ou `Files.lines` ;
  - un fichier entier → `readString` / `readAllLines` ;
  - du binaire → `InputStream` ;
  - des objets → `ObjectInputStream` ;
  - du formaté → `PrintWriter`.

</details>
