# Projet 6 — Le chasseur de doublons (attributs et visiteur de fichiers)

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 14) :**
- **les attributs :**
  - `Files.readAttributes(p, BasicFileAttributes.class)` : `size`, `isRegularFile`, `isDirectory`, `isSymbolicLink`, `lastModifiedTime` ;
  - `Files.getAttribute(p, "size")`, `getLastModifiedTime`, `setLastModifiedTime` ;
- **la VUE d'attributs** `BasicFileAttributeView` : `setTimes`, pour les **modifier** ;
- **`FileTime`**, construit à partir d'un `Instant` ;
- **`Files.walkFileTree`** avec un **`SimpleFileVisitor`** :
  - les méthodes `preVisitDirectory`, `visitFile`, `postVisitDirectory` ;
  - les résultats `FileVisitResult.CONTINUE` et `SKIP_SUBTREE` ;
- **`Files.walk(racine, profondeurMax)`**.

Côté algorithmes :
- **l'occupation disque** de chaque dossier, sous-dossiers compris ;
- **les doublons** : on groupe d'abord par **taille** (rapide), puis on compare les **contenus** avec `mismatch`, dans chaque groupe ;
- les **octets récupérables**.

**Ce que TU crées :** dans `ch14_io.projects.p06_duplicates` : `DiskUsage` et **`Dedup`** (le `main`).

**Règle du crescendo :** chapitres 1 à 14.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch14-p06 -sourcepath src/main/java src/main/java/ch14_io/projects/p06_duplicates/Dedup.java
java "-Duser.language=fr" -cp build/ch14-p06 ch14_io.projects.p06_duplicates.Dedup
```

---

## Tableau de bord

### ☐ Étape 1 — Le visiteur

```
parcours : 7 fichiers, [saute cache, fin du parcours] ; occupation (octets) {.=115, docs=51, docs/archives=34, musique=12, photos=52}
```

**📖 La leçon : un visiteur de fichiers.** `Files.walkFileTree(racine, visiteur)` parcourt l'arbre, et appelle les méthodes d'un **visiteur** à chaque étape. On étend `SimpleFileVisitor<Path>` et on redéfinit seulement ce qui nous intéresse ; chaque méthode rend la suite à donner au parcours (`CONTINUE`, `SKIP_SUBTREE`…) :

```java
List<String> vus = new ArrayList<>();
Files.walkFileTree(base, new SimpleFileVisitor<Path>() {          // une classe anonyme (chapitre 7)
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        vus.add(file.getFileName() + "=" + attrs.size());
        return FileVisitResult.CONTINUE;
    }
});
```

**👉 À toi :**

- **Les fichiers :** le bac à sable recréé, puis, pour chaque `chemin|contenu|date` de `Data.FILES` :
  1. `createDirectories` du parent, puis `writeString` ;
  2. puis `Files.setLastModifiedTime(f, FileTime.from(Instant.parse(date)))` : une date **fixe**, pour une sortie reproductible.
- **`DiskUsage extends SimpleFileVisitor<Path>`**, construit avec `(Path root, String skipped)` :
  - **`preVisitDirectory`** : si le nom du dossier vaut `skipped`, note `saute <chemin relatif>` et rend `SKIP_SUBTREE` ; sinon `CONTINUE` ;
  - **`visitFile`** : ajoute le fichier à la liste. Puis ajoute `attrs.size()` à **chaque** dossier ancêtre (chemin relatif avec `/`), et à `"."` (la racine) ;
  - **`postVisitDirectory`** : si c'est la racine, note `fin du parcours` ;
  - les accesseurs `usage()` (une `TreeMap`), `files()`, `events()`.
- **Dans `main`** : `Files.walkFileTree(root, visitor)`, avec `Data.SKIPPED`.

### ☐ Étape 2 — Lire et modifier les attributs

```
attributs de photos/plage.jpg : taille 17, fichier true, dossier false, lien false, modifie 2026-07-14T10:00:00Z ; getAttribute("size") 17
apres setTimes : 2026-12-25T00:00:00Z ; vue basic
plus recents : [photos/plage.jpg, photos/montagne.jpg, photos/copie de plage.jpg]
```

**📖 La leçon : les attributs d'un fichier.**

```java
BasicFileAttributes at = Files.readAttributes(f, BasicFileAttributes.class);
at.size()                 // la taille
at.isRegularFile()        // un fichier ordinaire ?
at.lastModifiedTime()     // un FileTime : la date de dernière modification
Files.setLastModifiedTime(f, FileTime.from(Instant.parse("2026-01-01T00:00:00Z")));   // fixer une date
```

Une date **fixe** rend la sortie identique d'une machine à l'autre (section 3 du `PARCOURS.md`).

**👉 À toi :**

- **La lecture :** `readAttributes(photo, BasicFileAttributes.class)`, puis `Files.getAttribute(photo, "size")`.
- **La modification :** `getFileAttributeView(photo, BasicFileAttributeView.class).setTimes(FileTime.from(Instant.parse("2026-12-25T00:00:00Z")), null, null)`. Affiche ensuite `getLastModifiedTime`, puis `view.name()`.
- **Les plus récents :** trie les fichiers visités par date de modification **décroissante** (`getLastModifiedTime`), puis affiche les 3 premiers.
- **Question :** que veut dire `null` dans `setTimes` ?

### ☐ Étape 3 — Les doublons

```
doublons : [docs/archives/cv-final.pdf, docs/cv.pdf]
doublons : [photos/copie de plage.jpg, photos/plage.jpg]
2 groupes, 34 octets recuperables (dossier cache ignore)
walk(racine, 2) par profondeur {0=1, 1=4, 2=8}
```

**📖 Rappel :** `Files.mismatch` (projet 2, étape 2), `groupingBy` dans une `TreeMap` (chapitre 10, projet 5).

**👉 À toi :**

- **`static List<List<Path>> duplicates(List<Path> files)`** :
  1. une `TreeMap` taille → fichiers ;
  2. dans chaque groupe de même taille : prends le premier, cherche tous ceux dont `mismatch` vaut -1 avec lui, et retire-les. Recommence tant qu'il reste au moins 2 fichiers ;
  3. ne garde que les groupes d'au moins 2 fichiers.
- **Le tri d'abord :** appelle-la sur les fichiers **triés** par chemin relatif, car l'ordre de parcours dépend du système de fichiers.
- **L'affichage :**
  - chaque groupe ;
  - puis le nombre de groupes, et les octets récupérables (taille × (taille du groupe − 1)) ;
  - puis `Files.walk(root, 2)`, en comptant les chemins par profondeur (0 pour la racine).
- **Question :** pourquoi grouper par taille **avant** de comparer les contenus ?

---

## Checklist (vérifiée par `Check`)

- `Data.SANDBOX`, `Data.FILES`, `Data.SKIPPED` ;
- `extends SimpleFileVisitor<Path>`, `preVisitDirectory(`, `visitFile(`, `postVisitDirectory(`, `FileVisitResult.SKIP_SUBTREE`, `FileVisitResult.CONTINUE`, `Files.walkFileTree(` ;
- `Files.setLastModifiedTime(`, `FileTime.from(`, `Files.readAttributes(`, `BasicFileAttributes`, `.lastModifiedTime()`, `Files.getAttribute(` ;
- `BasicFileAttributeView`, `.setTimes(`, `Files.getLastModifiedTime(` ;
- `Files.mismatch(`, `Files.walk(root, 2)`.

---

## Sortie attendue complète

```
parcours : 7 fichiers, [saute cache, fin du parcours] ; occupation (octets) {.=115, docs=51, docs/archives=34, musique=12, photos=52}
attributs de photos/plage.jpg : taille 17, fichier true, dossier false, lien false, modifie 2026-07-14T10:00:00Z ; getAttribute("size") 17
apres setTimes : 2026-12-25T00:00:00Z ; vue basic
plus recents : [photos/plage.jpg, photos/montagne.jpg, photos/copie de plage.jpg]
doublons : [docs/archives/cv-final.pdf, docs/cv.pdf]
doublons : [photos/copie de plage.jpg, photos/plage.jpg]
2 groupes, 34 octets recuperables (dossier cache ignore)
walk(racine, 2) par profondeur {0=1, 1=4, 2=8}
```
