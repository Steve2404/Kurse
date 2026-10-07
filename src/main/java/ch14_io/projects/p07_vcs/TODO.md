# Projet 7 (CAPSTONE) — Le mini gestionnaire de versions

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées :** tout le chapitre 14 dans une application :
- `Files` : lire et écrire du texte et des octets, copier, supprimer, `walk`, `list` ;
- les flux d'**objets** : un commit est un record **sérialisé**, ouvert avec `Files.newInputStream`/`newOutputStream` ;
- un fichier `HEAD` en texte.

Côté algorithmes :
- un **stockage par contenu** : chaque contenu est copié **une seule fois**, sous le nom de son empreinte (**FNV-1a** sur 64 bits) ;
- le **status** (ajouté, modifié, supprimé) et le **checkout** (remettre le dossier de travail dans l'état d'un commit) ;
- un **diff de lignes** par **plus longue sous-suite commune** (LCS), en programmation dynamique.

**Ce que TU crées :** dans `ch14_io.projects.p07_vcs` : le record `Commit`, `Repository` et **`MiniGit`** (le `main`).

**Règle du crescendo :** chapitres 1 à 14.

**C'est le projet-bilan du chapitre 14** : un mini-Git. Quand tu bloques, relis la leçon d'origine :

| Tu dois… | Leçon à relire |
|---|---|
| des chemins relatifs affichés avec `/` | projet 1, étapes 1 et 2 |
| créer des dossiers, lire et écrire des fichiers | projet 2, étape 1 |
| parcourir avec `Files.walk` et `Files.list` | projet 2, étapes 1 et 3 |
| `copy` avec `REPLACE_EXISTING` | projet 2, étape 3 |
| sérialiser un `record` | projet 4, étape 1 |
| `readAllBytes`, `readAllLines` | projet 2, étape 1 |
| la programmation dynamique sur un tableau 2D | chapitre 12, projet 5 |

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch14-p07 -sourcepath src/main/java src/main/java/ch14_io/projects/p07_vcs/MiniGit.java
java "-Duser.language=fr" -cp build/ch14-p07 ch14_io.projects.p07_vcs.MiniGit
```

`Files.readAllBytes(chemin)` lit tout un fichier dans un `byte[]` : c'est sur ces octets qu'on calcule l'empreinte.

---

## Tableau de bord

### ☐ Étape 1 — Le dépôt

- **`record Commit(int id, String message, int parent, TreeMap<String, String> snapshot) implements Serializable`** : `snapshot` associe chemin → empreinte.
- **`Repository(Path work)`** : son dossier caché `meta = work/.minigit` contient `objects/` et `commits/` (`createDirectories`), et `HEAD` (contenu `"0"` au départ).
  - **`static String hash(byte[] bytes)`** (FNV-1a) :
    - `h = 0xcbf29ce484222325L` ;
    - pour chaque octet : `h ^= octet & 0xFF`, puis `h *= 0x100000001b3L` ;
    - rends `Long.toHexString(h)`.
  - **`int head()`** : lit `HEAD` (`readString`, puis `strip`).
  - **`TreeMap<String, String> scan()`** : `Files.walk(work)`, fichiers ordinaires hors `meta` → chemin relatif (avec `/`) → `hash(Files.readAllBytes(p))`.
  - **`Commit load(int id)`** : `ObjectInputStream(Files.newInputStream(commits/<id>.ser))`.
  - **`Commit commit(String message)`** :
    1. `scan()` ;
    2. copie chaque contenu **pas encore stocké** dans `objects/<empreinte>` ;
    3. crée le commit `head() + 1` (parent `head()`), et l'écrit avec `ObjectOutputStream(Files.newOutputStream(...))` ;
    4. met `HEAD` à jour.
  - **`Map<String, String> status()`** : compare `scan()` au snapshot du commit courant (vide si `HEAD` vaut 0) → `ajoute`, `modifie` ou `supprime`, dans une `TreeMap`.
  - **`void checkout(int id)`** :
    1. supprime les fichiers suivis absents du commit ;
    2. recopie chaque objet du commit à sa place (`REPLACE_EXISTING`, en créant les dossiers) ;
    3. met `HEAD` à jour.
  - **`List<String> contentAt(int id, String file)`** : `readAllLines` de l'objet, ou une liste vide.
  - **`int objectCount()`** : avec `Files.list(objects)`.
  - **`static List<String> diff(List<String> a, List<String> b)`** :
    1. `lcs[i][j]` = la longueur de la plus longue sous-suite commune de `a[i..]` et `b[j..]`, remplie de la fin vers le début ;
    2. avance `i`/`j` : si les lignes sont égales, `"  " + ligne` ; sinon, si `lcs[i+1][j] >= lcs[i][j+1]`, `"- " + a[i]` ; sinon `"+ " + b[j]` ;
    3. vide les restes (`- ` puis `+ `).

### ☐ Étape 2 — Le scénario

```
commit 1 "premiere version" (parent 0) : [courses.txt, recette.txt], objets stockes 2
status : {courses.txt=supprime, notes.txt=ajoute, recette.txt=modifie}
log : 3 "sans oeufs" 2 "ajout vanille" 1 "premiere version"
diff 1 -> 3 recette.txt :
    farine
  - oeufs
  ...
checkout 1 : fichiers [courses.txt, recette.txt], HEAD 1
```
- **Le départ :** supprime le bac à sable s'il existe, puis crée un `Repository`.
- **Pour chaque commande de `Data.SCRIPT`**, découpée par `split(" ", 3)` :
  - `write f a/b/c` : `Files.write(f, List.of("a", "b", "c"))` ;
  - `delete f` : `Files.delete` ;
  - `commit message` : affiche `commit <id> "<message>" (parent <p>) : <fichiers du snapshot>, objets stockes <n>` ;
  - `status` : `status : <map>` ;
  - `log` : remonte les parents depuis `head()`, sous la forme `log : <id> "<message>" ...` ;
  - `diff A B fichier` : une ligne `diff A -> B fichier :`, puis chaque ligne du diff précédée de deux espaces ;
  - `checkout N` : `checkout N : fichiers <clés de scan()>, HEAD <head()>` ;
  - `cat f` : `cat f : <readAllLines>`.
- **Questions :**
  - Pourquoi n'y a-t-il que 5 objets stockés après 3 commits de 2 fichiers ?
  - Pourquoi `status` est-il vide juste après `checkout 1` ?

---

## Checklist (vérifiée par `Check`)

- `Data.SANDBOX`, `Data.SCRIPT`, `record Commit(`, `implements Serializable` ;
- `Files.newInputStream(`, `Files.newOutputStream(`, `new ObjectOutputStream(`, `new ObjectInputStream(` ;
- `Files.readAllBytes(`, `Files.copy(`, `StandardCopyOption.REPLACE_EXISTING` ;
- `Files.writeString(`, `Files.readString(`, `Files.readAllLines(`, `Files.write(` ;
- `Files.walk(`, `Files.list(`, `Files.delete(`, `new int[`.

---

## Sortie attendue complète

```
commit 1 "premiere version" (parent 0) : [courses.txt, recette.txt], objets stockes 2
status : {courses.txt=supprime, notes.txt=ajoute, recette.txt=modifie}
commit 2 "ajout vanille" (parent 1) : [notes.txt, recette.txt], objets stockes 4
status : {recette.txt=modifie}
commit 3 "sans oeufs" (parent 2) : [notes.txt, recette.txt], objets stockes 5
log : 3 "sans oeufs" 2 "ajout vanille" 1 "premiere version"
diff 1 -> 3 recette.txt :
    farine
  - oeufs
  - lait
  + lait entier
    sucre
  + vanille
  + sel
checkout 1 : fichiers [courses.txt, recette.txt], HEAD 1
status : {}
cat recette.txt : [farine, oeufs, lait, sucre]
checkout 3 : fichiers [notes.txt, recette.txt], HEAD 3
```
