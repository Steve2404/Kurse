# Drill de rappel 5 — NIO.2 : texte, parcours, attributs

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall05`** dans le paquet `ch14_io.drills.r05_nio`, avec `static String rel(Path root, Path p)` : le chemin relatif avec `/`, ou `.` s'il est vide.
- Travaille dans `box = Path.of("build/ch14/r05_nio")`, supprimé au départ, puis `createDirectories(box/src/main)`.

**Les notions de ce drill ont été apprises dans :** projet 5 (étape 1) et projet 6 (étapes 1 et 2). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r05_nio` → **New** → **Java Class** → `Recall05`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall05`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall05`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
