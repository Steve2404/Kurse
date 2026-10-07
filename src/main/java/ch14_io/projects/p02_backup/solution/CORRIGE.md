# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Backup.java`](Backup.java) et [`BackupLab.java`](BackupLab.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Les outils sur les arbres

**Supprimer un arbre :**

```java
try (Stream<Path> paths = Files.walk(root)) {
    paths.sorted(Comparator.reverseOrder()).forEach(p -> { … Files.delete(p); … });
}
```

**Question — pourquoi l'ordre inverse ?** `Files.delete` refuse un dossier **non vide** (`DirectoryNotEmptyException`, étape 3). `walk` donne un dossier **avant** son contenu ; trié à l'envers, chaque chemin vient **après** ceux qu'il contient (`a/b/c.txt` avant `a/b`, avant `a`). Quand on arrive à un dossier, son contenu est déjà supprimé.

---

## Étape 2 — Sauvegarder, modifier, comparer

**Le `diff` :** `modifie (octet 26)` vient de `Files.mismatch` : les deux versions de `todo.txt` sont identiques jusqu'à l'octet 25. Le déplacement de `budget.csv` apparaît comme une **suppression** plus un **ajout** : le `diff` compare des chemins, il ne sait pas qu'il s'agit du même contenu.

---

## Étape 3 — Ce qui échoue, et les parcours

**Les erreurs** (toutes des sous-classes d'`IOException`) :
- `copy` sur un fichier existant, sans option : `FileAlreadyExistsException`. Avec `REPLACE_EXISTING`, il remplace ;
- `delete` d'un dossier non vide : `DirectoryNotEmptyException` ;
- `delete` d'un fichier absent : `NoSuchFileException`, alors que `deleteIfExists` rend simplement `false` ;
- `createDirectory` sans parent : `NoSuchFileException`. `createDirectories` créerait aussi les parents.

**Question — pourquoi `isSameFile` dit `true` et `equals` dit `false` ?** `equals` compare les chemins **comme des textes** : `notes/../notes/./todo.txt` n'est pas écrit comme `notes/todo.txt`. `isSameFile` interroge le **disque** : les deux chemins mènent au même fichier.
