# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`SortedTree.java`](SortedTree.java) et [`TreeApp.java`](TreeApp.java).
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des classes de test réduites.

---

## Les 4 sortes de classes imbriquées, toutes dans ce projet

| Sorte | Exemple | Liée à un objet englobant ? | Où |
|---|---|---|---|
| **imbriquée static** | `Node`, `Builder` (et l'interface `Visitor`) | non | membre de la classe |
| **interne** (non static) | `Cursor` | **oui** : `SortedTree.this` | membre de la classe |
| **locale** | `RangeCounter` | oui (dans une méthode d'instance) | **dans** une méthode |
| **anonyme** | `new SortedTree.Visitor() { … }` | selon l'endroit | dans une expression |

---

## Étape 1 — La structure

**Le code :** `Visitor`, `Node`, `Builder`, `Cursor`, `insert` et `height` de [`SortedTree.java`](SortedTree.java).

**`Node` est `private static`** : un nœud n'a pas besoin de connaître l'arbre, il ne contient que ses valeurs. `private` le cache complètement : `TreeApp` ne voit jamais de `Node`.

**`Builder` est `public static`** : on l'écrit `new SortedTree.Builder()`, **sans** arbre existant. `add` rend `this`, d'où le chaînage `.add(…).build()`.

**`Cursor` est une classe interne** : chaque curseur **appartient** à un arbre précis, et lit ses champs (`SortedTree.this.root`, `size`). Deux syntaxes de création :
- `tree.cursor()` : dans une méthode d'instance, `new Cursor()` signifie `this.new Cursor()` ;
- `tree.new Cursor()` : la syntaxe explicite **objet.new Interne()**.

**Les doublons :** `VALUES` contient deux fois 40, et le 2e est ignoré : **12** valeurs.

**Le parcours infixe itératif :** la pile contient les nœuds « en attente ». On descend toujours à gauche (les plus petits d'abord). Après avoir rendu un nœud, on traite son sous-arbre droit de la même façon. Les valeurs sortent donc **triées**.

---

## Étape 2 — Le dessin et les niveaux

**Le code :** `visitSideways`, `sideways` et `levels`, et la 1re classe anonyme du `main`.

**La classe anonyme :** `new SortedTree.Visitor() { public void visit(…) { … } }` crée **en une expression** une classe sans nom qui implémente `Visitor`, et son unique objet. Elle **capture** `sideways`, une variable locale du `main`, qui doit être *effectively final*. On ne la réaffecte pas : on **modifie** le `StringBuilder` qu'elle désigne, ce qui est permis.

**Droite, nœud, gauche :** le plus grand est imprimé en premier, en haut. Penche la tête à gauche : l'arbre apparaît, racine à gauche.

**`levels()`** est un parcours en largeur (chapitre 4, le labyrinthe). La file traite les nœuds niveau par niveau.

---

## Étape 3 — Requêtes

**Le code :** `countBetween` (avec `RangeCounter`), `floor`, `ceiling`, `commonAncestor`, et la 2e classe anonyme.

**L'élagage de `RangeCounter` :** si la valeur d'un nœud est ≤ `low`, **tout** son sous-arbre gauche est plus petit encore. Inutile d'y descendre. Même chose à droite avec `high`.

**`int[] stats` dans la 2e anonyme :** une variable locale capturée doit être effectively final. On ne peut donc pas écrire `sum += value` sur un `int` local. Avec un tableau, la **référence** ne change pas, seul le contenu change.

**Expérience — `low++;` après la classe locale :**

```
error: local variables referenced from an inner class must be final or effectively final
```

Dès qu'une variable locale (ou un paramètre) est lue par une classe locale ou anonyme, **toute** modification, même placée **après**, la rend non effectively final.

**Les résultats :**
- `plancher(42)` = 40 : la plus grande valeur ≤ 42.
- `plancher(4)` = `Integer.MIN_VALUE` : aucune valeur n'est ≤ 4.
- `ancetre(5, 65)` = 50 : 5 est à gauche de 50 et 65 à droite, ils se séparent dès la racine.

---

## Étape 4 — Supprimer et rééquilibrer

**Le code :** les deux `remove`, `balanced` et `build`, et la fin du `main`.

**Les trois cas de suppression :**
- **une feuille** : on rend `null`, et le parent perd ce fils ;
- **un seul enfant** : on rend cet enfant, qui prend la place du nœud ;
- **deux enfants** (30, puis 50) : on remplace la valeur par celle du **successeur**, le plus petit du sous-arbre droit, puis on supprime ce successeur, qui a au plus un enfant droit. L'ordre reste correct.

Supprimer 50 (la racine) la remplace par **60**. Supprimer 99 (absent) rend `false`, car la taille ne change pas.

**L'équilibrage :** dans un tableau trié, le milieu devient la racine, puis récursivement chaque moitié. La hauteur passe de 5 à **4** : ⌈log₂(11 + 1)⌉ = 4 pour 11 valeurs.

**Expériences :**
- **`new SortedTree.Cursor()` depuis `TreeApp`** :

  ```
  error: an enclosing instance that contains S.Cursor is required
  ```

  Un `Cursor` doit appartenir à **un** arbre. Il faut écrire `tree.new Cursor()`.
- **`Node` non `static`** : `balanced` et `build` sont `static`, donc sans arbre englobant. Elles ne peuvent plus créer de `Node`, qui exigerait un `SortedTree.this` :

  ```
  error: non-static variable this cannot be referenced from a static context
  ```

  C'est la raison d'être de `static` sur une classe imbriquée : pas de lien caché vers l'objet englobant.
