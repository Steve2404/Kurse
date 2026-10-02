# Projet 6 — L'éditeur de texte

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 9) :**
- **`List<String>`** pour le texte : `add(index, …)`, `set`, `remove(index)` ;
- deux **`Deque<Edit>`** utilisées comme **piles** : `push`, `poll` (qui rend `null` si vide), `clear` ;
- une **`Deque<String>`** pour les fichiers récents : `removeFirstOccurrence`, `addFirst`, `removeLast` ;
- **`NavigableSet<String>`** (`TreeSet`) : `subSet(de, true, à, false)` pour l'**autocomplétion**, et `ceiling`, `lower`, `higher`, `first`, `last`, `descendingSet().headSet(…)` ;
- **`Deque<Character>`** comme pile pour les parenthèses ;
- **`Set.retainAll`** pour filtrer des candidats ;
- **`Map<String, List<String>>`** pour les fichiers, avec `computeIfAbsent`.

Côté algorithmes :
- **annuler et rétablir** ;
- la **vérification des parenthèses** ;
- les **suggestions orthographiques** à une modification près (suppression, échange, remplacement, insertion).

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch9_collections.projects.p06_editor` :
- le record `Edit` ;
- **`Editor`** (le `main`).

**Règle du crescendo :** chapitres 1 à 9. Pas de stream.

---

## Tableau de bord

### ☐ Étape 1 — Modifier et annuler

```
OPEN main.java  =>  ouvert main.java (0 lignes), recents [main.java]
ADD int x = (a + b;  =>  add -> [int x = (a + b;] (annulables 1)
...
UNDO  =>  annule DELETE -> [// debut, int x = (a + b);, list.add(map.get(k));]
REDO  =>  retabli SET -> [// debut, int x = (a + b);, list.add(map.get(k));]
```
- **`record Edit(String type, int index, String before, String after)`**.
- **Les champs d'`Editor`** :
  - `Map<String, List<String>> files` (une `TreeMap`) et `List<String> lines`, le fichier courant ;
  - `Deque<Edit> undo` et `redo` (des `ArrayDeque`) ;
  - `Deque<String> recent` ;
  - `NavigableSet<String> dictionary`, un `TreeSet` construit depuis `Data.DICTIONARY`.
- **`apply(Edit e, boolean forward)`** :
  - ADD et INSERT ajoutent à l'indice (ou retirent si on annule) ;
  - DELETE retire (ou remet `before`) ;
  - SET remplace par `after` (ou par `before`).
- **`record(Edit e)`** : applique, `undo.push(e)`, puis **`redo.clear()`**.
  - **Question :** pourquoi une nouvelle modification efface-t-elle la pile de rétablissement ?
- **`String run(String command)`** découpe avec `split(" ", 3)` :
  - `ADD` ajoute en fin (le texte est `command.substring(4)`) ;
  - `INSERT i texte`, `SET i texte`, `DELETE i` ;
  - `UNDO` : `undo.poll()` (`rien a annuler` si `null`), applique à l'envers, puis `redo.push` ;
  - `REDO` : le symétrique ;
  - une modification rend `type en minuscules -> lignes (annulables N)`.
- **`OPEN f`** :
  - `computeIfAbsent(f, k -> new ArrayList<>())` ;
  - vide les deux piles ;
  - `recent.removeFirstOccurrence(f)`, puis `addFirst(f)`, puis `removeLast()` au-delà de `Data.RECENT` ;
  - rend `ouvert f (N lignes), recents [...]`.
- Chaque ligne de sortie est `commande  =>  résultat`.

### ☐ Étape 2 — Compléter

```
COMPLETE ma  =>  completer ma : [main, map, math] (ceiling main)
COMPLETE zz  =>  completer zz : [] (ceiling null)
```
- `dictionary.subSet(p, true, p + Character.MAX_VALUE, false)` donne tous les mots qui commencent par p. Ajoute `dictionary.ceiling(p)`.
- **Question :** pourquoi `p + Character.MAX_VALUE` comme borne haute ?

### ☐ Étape 3 — Vérifier et corriger

```
main.java : 4 lignes [ok] [ok] [ok] [erreur colonne 9]
...
orthographe : retrun->[return] mapp->[map] lsit->[list] deqeu->[deque] set->[set] xyz->[]
dictionnaire : premier add, dernier set, avant list int, apres list listiterator, descendant [set, return, retain, queue, math, map, main]
```
- **`static String brackets(String line)`**, une pile `Deque<Character>` :
  - un ouvrant `([{` → `push` ;
  - un fermant → la pile ne doit pas être vide et `pop` doit correspondre, sinon `erreur colonne i` ;
  - à la fin : `ok`, ou `non ferme X`.
- Pour chaque fichier (dans l'ordre de la `TreeMap`) : `nom : N lignes`, suivi de `[résultat]` pour chaque ligne.
- **`static Set<String> suggestions(String word, Set<String> dict)`** :
  1. si le mot est connu, rends-le seul ;
  2. sinon, génère dans un `TreeSet` toutes les variantes à **une** modification près : supprimer une lettre, échanger deux voisines, remplacer une lettre par a–z, insérer une lettre a–z ;
  3. puis `candidates.retainAll(dict)`.
- **La dernière ligne :**
  - `first()`, `last()`, `lower("list")` et `higher("list")` ;
  - `descendingSet().headSet("long")`.
  - **Question :** pourquoi ce `headSet` contient-il les mots **après** `long` dans l'ordre alphabétique ?

---

## Checklist (vérifiée par `Check`)

- `Data.COMMANDS` et `Data.DICTIONARY` ;
- `record Edit(`, `Deque<Edit> undo = new ArrayDeque<>()` ;
- `push`, `poll()`, `redo.clear()` ;
- `removeFirstOccurrence(`, `addFirst(`, `removeLast()` ;
- `NavigableSet<String>`, `subSet(`, `ceiling(`, `lower(`, `higher(`, `descendingSet()` ;
- `Deque<Character>`, `retainAll(`, `computeIfAbsent(`.

---

## Sortie attendue complète

```
OPEN main.java  =>  ouvert main.java (0 lignes), recents [main.java]
ADD int x = (a + b;  =>  add -> [int x = (a + b;] (annulables 1)
ADD list.add(map.get(k));  =>  add -> [int x = (a + b;, list.add(map.get(k));] (annulables 2)
INSERT 0 // debut  =>  insert -> [// debut, int x = (a + b;, list.add(map.get(k));] (annulables 3)
SET 1 int x = (a + b);  =>  set -> [// debut, int x = (a + b);, list.add(map.get(k));] (annulables 4)
DELETE 0  =>  delete -> [int x = (a + b);, list.add(map.get(k));] (annulables 5)
UNDO  =>  annule DELETE -> [// debut, int x = (a + b);, list.add(map.get(k));]
UNDO  =>  annule SET -> [// debut, int x = (a + b;, list.add(map.get(k));]
REDO  =>  retabli SET -> [// debut, int x = (a + b);, list.add(map.get(k));]
OPEN util.java  =>  ouvert util.java (0 lignes), recents [util.java, main.java]
ADD if (x) { y[0] = 1; }  =>  add -> [if (x) { y[0] = 1; }] (annulables 1)
OPEN main.java  =>  ouvert main.java (3 lignes), recents [main.java, util.java]
ADD retrun [x);  =>  add -> [// debut, int x = (a + b);, list.add(map.get(k));, retrun [x);] (annulables 1)
COMPLETE ma  =>  completer ma : [main, map, math] (ceiling main)
COMPLETE li  =>  completer li : [list, listiterator] (ceiling list)
COMPLETE zz  =>  completer zz : [] (ceiling null)
OPEN notes.txt  =>  ouvert notes.txt (0 lignes), recents [notes.txt, main.java, util.java]
UNDO  =>  rien a annuler
REDO  =>  rien a retablir
OPEN test.java  =>  ouvert test.java (0 lignes), recents [test.java, notes.txt, main.java]
main.java : 4 lignes [ok] [ok] [ok] [erreur colonne 9]
notes.txt : 0 lignes
test.java : 0 lignes
util.java : 1 lignes [ok]
orthographe : retrun->[return] mapp->[map] lsit->[list] deqeu->[deque] set->[set] xyz->[]
dictionnaire : premier add, dernier set, avant list int, apres list listiterator, descendant [set, return, retain, queue, math, map, main]
```
