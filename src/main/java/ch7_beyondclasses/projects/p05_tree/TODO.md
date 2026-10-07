# Projet 5 — L'arbre binaire de recherche (classes imbriquées)

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 7) :** les **quatre sortes de classes imbriquées**, au service d'une vraie structure de données :
1. **imbriquée `static`** (`Node`, `Builder`) : elle n'a pas besoin d'une instance de l'englobante ;
2. **interne** (`Cursor`) :
   - chaque instance est liée à **un** arbre ;
   - elle lit ses champs avec `SortedTree.this.root` ;
   - elle se crée par `tree.new Cursor()` ;
3. **locale** (`RangeCounter`) : déclarée dans une méthode, elle lit des variables **effectively final** ;
4. **anonyme** : `new SortedTree.Visitor() { … }` implémente une interface sur place.

Et aussi une **interface imbriquée** (implicitement `static`).

Côté algorithmes :
- insertion, **suppression à trois cas** (avec le successeur) ;
- hauteur ;
- parcours infixe **itératif** avec une pile ;
- parcours **en largeur** ;
- comptage d'intervalle avec élagage ;
- plancher et plafond ;
- **ancêtre commun** ;
- reconstruction **équilibrée**.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch7_beyondclasses.projects.p05_tree` :
- **`SortedTree`**, qui contient toutes les classes imbriquées ;
- **`TreeApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 7. Pas de collection : la pile et la file sont des tableaux. Pas de lambda : c'est justement le rôle des classes anonymes ici.

---

## Tableau de bord

### ☐ Étape 1 — La structure

```
infixe : 5 10 20 30 35 40 45 50 60 65 70 80 (12 valeurs, hauteur 5, 3e plus petite 20)
```
- **Dans `SortedTree`** :
  - `public interface Visitor { void visit(int value, int depth); }` ;
  - `private static class Node`, avec `value`, `left`, `right` ;
  - `public static class Builder`, avec un `SortedTree` interne, `add(int... values)` (qui rend `this`) et `build()` ;
  - les champs `private Node root` et `private int size` ;
  - `insert` (itératif ; ignore les doublons et rend `false` dans ce cas).
- **`public class Cursor`** (interne) :
  - une pile `Node[]` de taille `size + 1` ;
  - le constructeur empile `SortedTree.this.root` et toute sa branche gauche ;
  - `hasNext()` ;
  - `next()` dépile, empile la branche gauche du fils droit, et rend la valeur.
- `public Cursor cursor()` rend `new Cursor()`, et `height()` est récursive.
- **Dans `main`** :
  - `new SortedTree.Builder().add(Data.VALUES).build()` ;
  - un premier curseur par `tree.cursor()` ;
  - un second par **`tree.new Cursor()`**, appelé 3 fois pour la 3e plus petite valeur.

### ☐ Étape 2 — Le dessin et les niveaux

```
        80
    70
            65
        60
50
...
par niveaux : 50 | 30 70 | 20 40 60 80 | 10 35 45 65 | 5
```
- `visitSideways(Visitor v)` : parcours droite, nœud, gauche, avec la profondeur.
- Dans `main`, une **classe anonyme** `new SortedTree.Visitor() { … }` ajoute à un `StringBuilder` `"    ".repeat(depth) + value + "\n"`. Affiche-le avec `print`.
- `levels()` : parcours en largeur, avec une file `Node[]` et un `int[]` des profondeurs. Les niveaux sont séparés par ` | `.

### ☐ Étape 3 — Requêtes

```
entre 33 et 66 : 6 ; plancher(42) 40, plafond(42) 45, plancher(4) -2147483648 ; ancetre(35, 45) 40, ancetre(5, 65) 50
somme 510, profondeur max 4
```
- **`countBetween(int low, int high)`** déclare une **classe locale** `RangeCounter` :
  - un champ `count` ;
  - `walk(Node)` élague : à gauche seulement si la valeur est `> low`, à droite seulement si elle est `< high`.
  - `low` et `high` sont lus depuis la classe locale, donc ils doivent rester effectively final.
- `floor` et `ceiling` descendent sans récursion. Faute de valeur, ils rendent `Integer.MIN_VALUE` et `Integer.MAX_VALUE`.
- `commonAncestor(a, b)` : on descend tant que a et b sont du même côté.
- **Une 2e classe anonyme** cumule la somme et la profondeur maximale dans un `int[] stats` (un tableau, car un `int` local ne pourrait pas être modifié).
- **Expérience :** dans `countBetween`, écris `low++;` après la classe locale : quelle erreur ?

### ☐ Étape 4 — Supprimer et rééquilibrer

```
suppressions : 30=true 50=true 99=false -> 60 | 35 70 | 20 40 65 80 | 10 45 | 5
equilibre : hauteur 5 -> 4 ; 40 | 10 65 | 5 20 45 70 | 35 60 80
```
- **`remove(int)`**, récursif, avec trois cas :
  - feuille ;
  - un seul enfant ;
  - deux enfants : copie la valeur du successeur (le minimum du sous-arbre droit), puis supprime ce successeur.
  - Rend `true` si la taille a baissé.
- **`static SortedTree balanced(int[] sorted)`** : le milieu (`(lo + hi) / 2`) devient la racine, récursivement. Une méthode `static` de `SortedTree` peut créer des `Node` et remplir `root`.
- Récupère les valeurs triées avec un `Cursor` dans une boucle `for (SortedTree.Cursor c = tree.cursor(); c.hasNext(); )`.
- **Expériences :**
  - `new SortedTree.Cursor()` depuis `TreeApp` : quelle erreur ?
  - rends `Node` non `static` : que change-t-il pour `balanced`, qui est `static` ?

---

## Checklist (vérifiée par `Check`)

- `Data.VALUES` et `Data.DELETE` ;
- `public interface Visitor`, `private static class Node`, `public static class Builder`, `public class Cursor` ;
- `SortedTree.this.root`, `class RangeCounter` ;
- `tree.new Cursor()`, `new SortedTree.Builder()`, 2 `new SortedTree.Visitor()`.

---

## Sortie attendue complète

```
infixe : 5 10 20 30 35 40 45 50 60 65 70 80 (12 valeurs, hauteur 5, 3e plus petite 20)
        80
    70
            65
        60
50
            45
        40
            35
    30
        20
            10
                5
par niveaux : 50 | 30 70 | 20 40 60 80 | 10 35 45 65 | 5
entre 33 et 66 : 6 ; plancher(42) 40, plafond(42) 45, plancher(4) -2147483648 ; ancetre(35, 45) 40, ancetre(5, 65) 50
somme 510, profondeur max 4
suppressions : 30=true 50=true 99=false -> 60 | 35 70 | 20 40 65 80 | 10 45 | 5
equilibre : hauteur 5 -> 4 ; 40 | 10 65 | 5 20 45 70 | 35 60 80
```
