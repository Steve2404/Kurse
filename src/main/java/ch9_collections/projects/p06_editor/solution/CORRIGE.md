# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Edit.java`](Edit.java) et [`Editor.java`](Editor.java).

---

## Étape 1 — Modifier et annuler

**Le code :** `apply`, `record` et `run` d'[`Editor.java`](Editor.java), sauf `COMPLETE`.

**Deux piles :**
- annuler, c'est dépiler de `undo`, défaire, puis empiler sur `redo` ;
- rétablir, c'est l'inverse.

Une `Deque` utilisée avec `push`, `poll` et `peek` est la pile recommandée en Java : `java.util.Stack` est une ancienne classe synchronisée, déconseillée.

**`poll` plutôt que `pop` :** sur une pile vide, `poll` rend `null`, ce qui permet de répondre `rien a annuler`, alors que `pop` lèverait `NoSuchElementException`.

**Question — pourquoi une nouvelle modification efface la pile de rétablissement ?** Les `Edit` de `redo` décrivent des modifications **à partir d'un état précis** du texte. Ce sont des indices de lignes, des contenus « avant ». Une nouvelle modification change cet état : rétablir un ancien `Edit` l'appliquerait au mauvais endroit. Par exemple, un `DELETE 0` qui supprimerait une ligne qui n'est plus la bonne. Tous les éditeurs font ainsi : après une nouvelle frappe, `Ctrl+Y` ne rétablit plus rien.

**La liste des fichiers récents :** `removeFirstOccurrence` retire le fichier s'il y était déjà, `addFirst` le met en tête, et `removeLast` retire le plus ancien au-delà de 3. C'est une petite liste LRU, faite avec une `Deque`.

**`computeIfAbsent` pour `OPEN` :** le fichier est créé vide à la 1re ouverture. Ensuite, on retrouve **la même** liste, avec ses lignes.

---

## Étape 2 — Compléter

**Le code :** le `case "COMPLETE"`.

**Question — pourquoi `p + Character.MAX_VALUE` comme borne haute ?** Dans l'ordre des `String`, tous les mots qui commencent par `ma` sont **entre** `ma` (inclus) et « `ma` suivi du plus grand caractère possible » (exclu). `Character.MAX_VALUE` (`￿`) est plus grand que toute lettre, donc `main`, `map` et `math` sont < `ma￿`, et `mb…` est au-delà. C'est un intervalle `[p, p + '￿')` dans un ensemble trié : le préfixe devient une recherche par **plage**, en O(log n).

**`ceiling("zz")` vaut `null`** : aucun mot du dictionnaire n'est ≥ `zz`.

---

## Étape 3 — Vérifier et corriger

**Le code :** `brackets`, `suggestions` et la fin du `main`.

**Les parenthèses :** chaque ouvrant est empilé, et chaque fermant doit correspondre au **dernier** ouvrant non fermé, le sommet de la pile. Dans `retrun [x);`, le `)` (colonne 9) ne correspond pas au `[` du sommet : `erreur colonne 9`.

**Les suggestions :** pour un mot de longueur n, il y a environ 54n + 25 variantes (n suppressions, n − 1 échanges, 25n remplacements, 26(n + 1) insertions). `retainAll(dict)` ne garde que les vrais mots. `retrun` devient `return` par un **échange** de lettres voisines, et `mapp` devient `map` par une **suppression**.

**Question — pourquoi `descendingSet().headSet("long")` contient les mots après `long` ?** `descendingSet()` est une **vue** du même ensemble, dans l'ordre **inverse** : set, return, retain, …, add. Dans cet ordre, `headSet("long")` donne les éléments qui viennent **avant** `long`, donc ceux qui sont **après** dans l'ordre alphabétique : `[set, return, retain, queue, math, map, main]`. `long` lui-même est exclu.
