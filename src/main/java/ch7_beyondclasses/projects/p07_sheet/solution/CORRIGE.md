# Projet 7 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Ref`, `Op`, `Expr`, `Content`, `Sheet` et `SheetApp`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des fichiers de test réduits.

---

## Étape 1 — Les briques

**Le code :** [`Ref.java`](Ref.java), [`Op.java`](Op.java), [`Expr.java`](Expr.java) et [`Content.java`](Content.java).

**Tout le chapitre 7 dans quatre petits types :**
- `Ref` est un **record** avec une fabrique et un `toString` redéfini ;
- `Op` est un **enum à corps de constantes**, avec une méthode abstraite par constante ;
- `Expr` et `Content` sont des **interfaces scellées** qui **contiennent** leurs records (imbriqués, donc implicitement `static`).

**`sealed` sans `permits`** (`Expr`) : Java déduit la liste des sous-types autorisés : ce sont ceux déclarés **dans le même fichier**, ici les 4 records imbriqués. `Content`, lui, les liste explicitement, et les deux formes sont équivalentes ici.

**`Expr.references` est une méthode `static` d'interface**, appelée `Expr.references(…)`. Elle parcourt l'arbre récursivement avec des `instanceof`, et la hiérarchie scellée garantit qu'aucun cas n'est oublié.

**`DIVIDE` rend `NaN`** au lieu de lever une exception : les exceptions sont au chapitre 11. `NaN` se propage dans les calculs, et `shown` l'affiche `#DIV0`.

---

## Étape 2 — La feuille

**Le code :** `Parser` et `set` de [`Sheet.java`](Sheet.java), et le début du `main`.

**`Parser` est une classe imbriquée `static`** : elle n'a besoin d'aucune feuille. `new Sheet.Parser("B2/A1+B1")` fonctionne sans objet `Sheet`.

**L'arbre de B3 :** `B2/A1+B1` donne `Binary[PLUS, Binary[DIVIDE, B2, A1], B1]`. `/` est plus prioritaire que `+`, donc la division est **en bas** de l'arbre et s'évalue d'abord. Le `toString` généré des records imbriqués affiche toute la structure.

**Le compte des cellules :** 3 nombres (A1, A2, A3), 1 texte (C1) et **8** formules.

---

## Étape 3 — Recalculer dans le bon ordre

**Le code :** `Evaluator`, `recalculate` (avec `Graph`), `formulaOrder`, `shown` et `visit`, puis `print` dans `SheetApp`.

**Pourquoi un tri topologique ?** B3 dépend de B2 et B1. Si l'on calculait B3 **avant** B2, on lirait un `values` pas encore à jour. L'algorithme de Kahn calcule une cellule seulement quand **toutes** celles dont elle dépend sont faites (degré entrant 0).

**L'ordre `B1 B2 B3 C3 C2 D3` :** on part des nombres A1, A2 et A3 (degré 0), puis on suit les dépendances. La file traite les ids par **ordre croissant**, et l'id vaut `col * ROWS + row`. À degré égal, l'ordre suit donc les colonnes.

**Les cycles :** D1 dépend de D2, qui dépend de D1. Leurs degrés ne tombent **jamais** à 0, donc ils ne sont jamais traités : `#CYCLE`. D3 dépend de `E9` (hors feuille, ignorée par `add`) et de C2 : il est calculé normalement, et `E9` vaut 0.

**Les trois sortes de classes imbriquées de `Sheet` :**
- `Parser` est **static** : aucune feuille nécessaire ;
- `Evaluator` est **interne** : il lit `Sheet.this.values`, les valeurs de **sa** feuille ;
- `Graph` est **locale** à `recalculate` : elle n'a de sens que pendant un recalcul.

Le `print` de `SheetApp` ajoute une **classe anonyme** (`CellVisitor`).

---

## Étape 4 — Modifier et recalculer

**Le code :** la fin du `main`.

**A1 passe à 5 :** tout ce qui en dépend est recalculé dans le bon ordre. B1 vaut 10, B2 vaut 55, B3 vaut 55 / 5 + 10 = 21, etc. Le `-4.5` de D3 montre l'affichage décimal.

**Expériences :**
- **`record Mod(…) implements Expr` dans un autre fichier** (vérifié) :

  ```
  error: class is not allowed to extend sealed class: Expr (as it is not listed in its permits clause)
  ```

  Sans `permits`, la liste implicite contient seulement les sous-types **du même fichier source**. Un autre fichier n'en fait pas partie. Il faudrait ajouter `permits` avec **tous** les sous-types, ou déplacer `Mod` dans `Expr.java`.
- **`new Sheet.Evaluator()` depuis `SheetApp`** (vérifié) :

  ```
  error: an enclosing instance that contains Sheet.Evaluator is required
  ```

  Un `Evaluator` doit appartenir à une feuille. **`sheet.new Evaluator()`, lui, compile** (vérifié) : il est lié à `sheet`, et la classe (sans modificateur) est visible dans le paquet.
