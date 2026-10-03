# Drill de rappel 5 — NIO.2 : texte, parcours, attributs

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall05`** dans le paquet `ch14_io.drills.r05_nio`, avec `static String rel(Path root, Path p)` : le chemin relatif avec `/`, ou `.` s'il est vide.
- Travaille dans `box = Path.of("build/ch14/r05_nio")`, supprimé au départ, puis `createDirectories(box/src/main)`.

## Défis

- ☐ **D01.**
  1. `Files.write(src/data.txt, List.of("alpha", "beta", "gamma"))` ;
  2. puis `writeString(…, "delta" + System.lineSeparator(), APPEND)` ;
  3. et `src/main/App.java` écrit avec `newBufferedWriter` (contenu `class App {}`).
  
  Affiche `readAllLines`, puis sa taille.
  → `D01 : [alpha, beta, gamma, delta] 4`
- ☐ **D02.** `Files.lines(data)` : garde les lignes qui contiennent `a`, en majuscules.
  → `D02 : [ALPHA, BETA, GAMMA, DELTA]`
- ☐ **D03.** `newBufferedReader(data)` : deux `readLine()`.
  → `D03 : alpha beta`
- ☐ **D04.** Trois streams **dans le même** try-with-resources :
  - `Files.list(box/src)` : relatif, trié ;
  - `Files.walk(box)` : `count()` ;
  - `Files.find(box, 5, (p, a) -> a.isRegularFile() && p.toString().endsWith(".java"))`.
  → `D04 : [src/data.txt, src/main] 5 [src/main/App.java]`
- ☐ **D05.** `Files.walk(box, 1)`, relatif, trié.
  → `D05 : [., src]`
- ☐ **D06.** `setLastModifiedTime(data, FileTime.from(Instant.parse("2026-01-01T00:00:00Z")))`, puis `readAttributes(…, BasicFileAttributes.class)`. Affiche :
  - `isRegularFile()`, `isDirectory()` ;
  - `size() == Files.size(data)`, `lastModifiedTime()` ;
  - `getAttribute(data, "isDirectory")`, `isReadable(data)`.
  → `D06 : true false true 2026-01-01T00:00:00Z false true`

## Expériences (hors sortie attendue)

1. Utilise un `Stream` de `Files.lines` après la fin de son try-with-resources : que se passe-t-il ?
2. Quelle différence entre `Files.walk(p)` et `Files.list(p)` ? Lequel est récursif ?
3. `Files.readAllLines` sur un fichier de 4 Go : quel risque ?

## Sortie attendue complète

```
D01 : [alpha, beta, gamma, delta] 4
D02 : [ALPHA, BETA, GAMMA, DELTA]
D03 : alpha beta
D04 : [src/data.txt, src/main] 5 [src/main/App.java]
D05 : [., src]
D06 : true false true 2026-01-01T00:00:00Z false true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Lire | Écrire |
|---|---|
| `readString(p)`, `readAllLines(p)` (tout en mémoire) | `writeString(p, s[, options])`, `write(p, lignes[, options])` |
| `lines(p)` (paresseux, à **fermer**) | `newBufferedWriter(p[, options])` |
| `newBufferedReader(p)` | `newOutputStream(p)` |
| `readAllBytes(p)`, `newInputStream(p)` | `write(p, octets)` |

- **Les options :**
  - `CREATE` et `TRUNCATE_EXISTING` (par défaut pour l'écriture) ;
  - `APPEND` ;
  - `CREATE_NEW` (erreur si le fichier existe) ;
  - `READ` et `WRITE`.
- **Le charset par défaut** des méthodes de `Files` est **UTF-8**.
- **Les parcours** (`Stream<Path>`, à fermer) :
  - `list(dossier)` : un niveau ;
  - `walk(dossier[, profondeur])` : récursif, en profondeur d'abord ;
  - `find(dossier, profondeur, (chemin, attributs) -> …)` ;
  - `walkFileTree(dossier, visiteur)` : pas de stream.
- **Les attributs :** `readAttributes(p, BasicFileAttributes.class)` (lecture), `getFileAttributeView(p, BasicFileAttributeView.class)` (modification), `getAttribute(p, "size")`.

</details>
