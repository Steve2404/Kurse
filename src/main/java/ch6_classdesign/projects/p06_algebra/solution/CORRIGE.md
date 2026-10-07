# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Expr`, `Num`, `Var`, `BinaryOp`, `Add`, `Sub`, `Mul`, `Div`, `Pow`, `Parser` et `Algebra`.
>
> Les messages et les sorties ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18), sur une copie de la solution.

---

## Étape 1 — L'arbre

**Le code :** [`Expr.java`](Expr.java), [`Num.java`](Num.java), [`Var.java`](Var.java), [`BinaryOp.java`](BinaryOp.java), les quatre opérations et [`Pow.java`](Pow.java).

**La hiérarchie à deux niveaux d'abstraction :**
- `Expr` déclare **4 méthodes abstraites**.
- `BinaryOp`, elle-même **abstraite**, en implémente 2 (`show`, `size`), communes à tous les opérateurs, et en ajoute une (`symbol`).
- `Add`, `Sub`, `Mul` et `Div` n'implémentent que ce qui leur est propre (`eval`, `derive`, `symbol`, `simplify`).

Une classe abstraite peut donc **implémenter** des méthodes abstraites héritées, sans tout implémenter.

**Pourquoi `Pow` n'est pas un `BinaryOp` :** son exposant est un `int`, pas une `Expr`. L'héritage doit refléter une vraie relation « est un » : on ne force pas une classe dans une hiérarchie qui ne lui correspond pas.

**`toString()` final dans `Expr`** : toute concaténation `"" + expr` passe par `show()`. Aucune sous-classe ne peut contourner ce format.

---

## Étape 2 — L'analyseur

**Le code :** [`Parser.java`](Parser.java).

**La descente récursive :** une méthode par niveau de priorité, du **moins** prioritaire (`expr` : `+` et `-`) au **plus** prioritaire (`atom`). `expr` appelle `term`, qui appelle `factor`, qui appelle `atom`. Les parenthèses rappellent `expr` : c'est la récursion. Ainsi `3*x^2` est lu comme `3 * (x ^ 2)` : `term` lit `3`, voit `*`, puis demande un `factor`, qui lit `x ^ 2` en entier.

**Question — pourquoi `2-3-4` doit donner `((2 - 3) - 4)` ?** La soustraction est **associative à gauche** : 2 − 3 − 4 = (2 − 3) − 4 = **−5**. Lu `2 - (3 - 4)`, on obtiendrait 2 − (−1) = **3**, ce qui est faux. La boucle `while` de `expr` produit cette lecture : à chaque tour, le **nouveau** nœud prend l'arbre déjà construit comme enfant **gauche**. Une version récursive naïve (`e = term(); if (+) return new Add(e, expr());`) donnerait l'associativité à **droite**.

**`private` sur `expr`, `term`… :** l'état (`pos`) n'a de sens que pendant une analyse. Seule la fabrique `parse` est publique.

---

## Étape 3 — Dériver et simplifier

**Le code :** les méthodes `derive` et `simplify` de chaque classe, et la boucle du `main`.

**Pourquoi simplifier les enfants d'abord ?** Dans `(x + 0) * 1`, il faut que `x + 0` devienne `x` **avant** de regarder le `* 1`. Sinon, la règle ne verrait pas une forme simple. La récursion fait remonter les simplifications du bas vers le haut de l'arbre.

**23 nœuds → 7 :** la dérivée brute de `3*x^2 + 2*x - 5` applique mécaniquement les règles, et produit des `0 * …`, des `* 1` et des `- 0`. La simplification les élimine : `((3 * (2 * x)) + 2)`.

**Le polymorphisme :** le `main` appelle `f.derive()` et `d.simplify()` sans savoir s'il s'agit d'un `Add` ou d'un `Pow`. Chaque nœud exécute **sa** version : on ajoute un opérateur en ajoutant une classe, sans modifier le reste.

---

## Étape 4 — Newton et polymorphisme

**Le code :** la fin du `main` d'[`Algebra.java`](Algebra.java).

**Newton :** la racine de x³ − 2x − 5 est ≈ 2.0946. Partie de 2, la méthode converge dès la 2e itération (à 4 décimales) : la convergence est **quadratique**.

**La dernière ligne :** `new Add(new Num(1), new Mul(new Var(), new Num(1)))` est un `Add`. `simplify()` simplifie d'abord `x * 1` en `x`, puis rend `new Add(Num(1), Var)`. Le résultat est encore un `Add`. Une simplification peut aussi rendre une classe **différente** de l'original : `Mul(x, 1).simplify()` rend un `Var`.

**Expériences :**
- **Retirer `simplify()` de `Sub`** : c'est alors la version **héritée** d'`Expr` qui s'exécute, et elle rend `this` sans toucher aux enfants. Vérifié : pour `3*x^2 + 2*x - 5`, la dérivée « simplifiée » devient `((((0 * (x ^ 2)) + (3 * ((2 * (x ^ 1)) * 1))) + ((0 * x) + (2 * 1))) - 0)`, avec **23 → 23** nœuds. La racine est un `Sub`, donc **rien** en dessous n'est simplifié. La valeur reste juste (14.0) : seule la forme change.
- **Redéfinir `toString()` dans `Num`** (vérifié) :

  ```
  error: toString() in Y cannot override toString() in Expr
    overridden method is final
  ```

  `final` s'applique à **toute** la descendance, même indirecte.
- **`new BinaryOp(a, b)`** (vérifié) :

  ```
  error: BinaryOp is abstract; cannot be instantiated
  ```

  Une classe abstraite peut avoir un constructeur (ici `protected`), mais il ne sert qu'aux sous-classes, via `super(...)`.
