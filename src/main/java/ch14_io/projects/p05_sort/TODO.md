# Projet 5 — Le tri externe (fichiers texte avec NIO.2)

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 14) :**
- **`Files.newBufferedWriter`** et **`Files.newBufferedReader`** (UTF-8 par défaut) ;
- **`Files.lines`** : un `Stream<String>` **paresseux**, à **fermer** ;
- **charger tout le fichier :** `readAllLines`, `readString` (en mémoire : à éviter pour un gros fichier) ;
- **écrire :** `Files.write(chemin, lignes)`, `Files.writeString` ;
- **les options d'ouverture :** `StandardOpenOption.APPEND` (ajouter) et `CREATE_NEW` (échoue si le fichier existe, avec `FileAlreadyExistsException`).

Côté algorithme : le **tri externe**, pour un fichier trop gros pour la mémoire.
1. **Découper :** lire par paquets de `CHUNK` lignes, trier chaque paquet, et l'écrire dans son propre fichier.
2. **Fusionner :** un **tas** contient la première ligne non lue de chaque paquet. On sort la plus petite, puis on la remplace par la suivante de **son** paquet.

À tout instant, il n'y a qu'**une** ligne par paquet en mémoire.

**Ce que TU crées :** dans `ch14_io.projects.p05_sort` : `ExternalSort` et **`SortLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 14.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch14-p05 -sourcepath src/main/java src/main/java/ch14_io/projects/p05_sort/SortLab.java
java "-Duser.language=fr" -cp build/ch14-p05 ch14_io.projects.p05_sort.SortLab
```

---

## Tableau de bord

### ☐ Étape 1 — Créer et analyser le gros fichier

```
fichier : 60000 lignes, 622 scores >= 9900 ; par millier {0=6010, 1=5990, ...}
```

**📖 La leçon : lire un gros fichier ligne par ligne.** `Files.lines(chemin)` rend un `Stream<String>` qui lit le fichier **au fur et à mesure**, sans le charger en entier. Il garde le fichier ouvert : ferme-le avec un try-with-resources :

```java
try (Stream<String> lignes = Files.lines(tarte)) {
    long n = lignes.filter(l -> l.startsWith("f")).count();
}
```

`Files.newBufferedWriter(chemin)` et `Files.newBufferedReader(chemin)` donnent directement les flux tamponnés du projet 3.

**👉 À toi :**

- **Le bac à sable :** supprime-le s'il existe, puis `createDirectories(sandbox/paquets)`.
- **`scores.txt`** : avec `Files.newBufferedWriter`, écris les `Data.LINES` lignes `Data.line(i)`, chacune suivie de `newLine()`.
- **Trois parcours**, chacun avec **son propre** `Files.lines(...)` dans un try-with-resources :
  - `count()` ;
  - le nombre de scores ≥ 9 900 ;
  - `groupingBy(score / 1000, TreeMap::new, counting())`.
- **Question :** pourquoi ne peut-on pas réutiliser le même `Stream` pour les trois ?

### ☐ Étape 2 — Le tri externe

```
tri externe : 9 paquets de 7000 lignes max, fusion de 60000 lignes, identique au tri en memoire true
podium : [joueur25366;9999, joueur39869;9999, joueur50438;9999] ; dernier joueur82825;0
```

**📖 Rappel :** le tas (`PriorityQueue`, chapitre 9) et la fusion de listes triées (chapitre 5, projet 6). `Files.write(chemin, liste)` écrit une liste de lignes d'un coup.

**👉 À toi :**

- **`final class ExternalSort`** :
  - `public static final Comparator<String> ORDER` : le score décroissant, puis le joueur croissant ;
  - `record Head(String line, BufferedReader reader)` ;
  - **`List<Path> splitSorted(Path input, Path dir, int chunk)`** : lit avec `newBufferedReader`/`readLine`. Chaque fois que le tampon atteint `chunk` lignes (et à la fin, s'il en reste), elle le trie, l'écrit (`Files.write`) dans `paquet-<n>.txt` et le vide ;
  - **`void merge(List<Path> parts, Path output)`** :
    1. une `PriorityQueue<Head>` ordonnée par `ORDER` sur la ligne ;
    2. ouvre un lecteur par paquet et y met sa 1re ligne ;
    3. puis, tant que le tas n'est pas vide : `poll`, écris la ligne (`newBufferedWriter`), et ajoute la ligne suivante de **ce** lecteur ;
    4. ferme tous les lecteurs dans un `finally`.
- **Dans `main`** :
  1. `splitSorted(scores, paquets, Data.CHUNK)`, puis `merge(…, trie.txt)` ;
  2. vérifie avec `readAllLines(scores)` trié par `ORDER`, comparé à `readAllLines(trie.txt)` ;
  3. affiche les 3 premières lignes et la dernière.

### ☐ Étape 3 — Options d'ouverture

```
resume : [lignes=60000, paquets=9, meilleur=joueur25366;9999] ; CREATE_NEW sur un fichier existant FileAlreadyExistsException ; readString 3 lignes
```

**📖 La leçon : les options d'ouverture.** `writeString` et `write` acceptent des options (`StandardOpenOption`) :
- `APPEND` : ajouter à la fin ;
- `CREATE_NEW` : créer, mais refuser si le fichier existe déjà.

Sans option, le comportement par défaut est l'objet de l'expérience de l'étape.

**👉 À toi :**

- **`resume.txt`**, dans cet ordre :
  1. `writeString("lignes=" + n + System.lineSeparator())` ;
  2. puis `writeString(…, APPEND)` pour les paquets ;
  3. puis `Files.write(…, List.of("meilleur=" + …), APPEND)` ;
  4. puis `writeString(…, CREATE_NEW)`, qui doit lever une exception.
- **L'affichage :** `readAllLines`, puis `Files.readString(…).lines().count()`.
- **Expérience :** `writeString` sans option sur un fichier existant : ajout ou remplacement ?

---

## Checklist (vérifiée par `Check`)

- `Data.SANDBOX`, `Data.LINES`, `Data.CHUNK`, `Data.line(` ;
- `Files.newBufferedWriter(`, `Files.newBufferedReader(`, `Files.lines(`, `Stream<String>` ;
- `Files.readAllLines(`, `Files.write(`, `Files.writeString(`, `Files.readString(` ;
- `StandardOpenOption.APPEND`, `StandardOpenOption.CREATE_NEW`, `catch (FileAlreadyExistsException` ;
- `PriorityQueue<`, `Collectors.groupingBy(`.

---

## Sortie attendue complète

```
fichier : 60000 lignes, 622 scores >= 9900 ; par millier {0=6010, 1=5990, 2=5984, 3=5995, 4=5991, 5=5989, 6=6006, 7=6015, 8=6006, 9=6014}
tri externe : 9 paquets de 7000 lignes max, fusion de 60000 lignes, identique au tri en memoire true
podium : [joueur25366;9999, joueur39869;9999, joueur50438;9999] ; dernier joueur82825;0
resume : [lignes=60000, paquets=9, meilleur=joueur25366;9999] ; CREATE_NEW sur un fichier existant FileAlreadyExistsException ; readString 3 lignes
```
