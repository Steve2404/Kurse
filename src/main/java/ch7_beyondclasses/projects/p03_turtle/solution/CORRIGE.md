# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Command`, `Move`, `Turn`, `PenUp`, `PenDown`, `Repeat`, `Macro`, `Square`, `Stairs`, `Direction`, `Turtle` et `TurtleApp`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des types de test réduits.

---

## Étape 1 — Les commandes

**Le code :** [`Command.java`](Command.java), les records, [`Macro.java`](Macro.java), [`Square.java`](Square.java) et [`Stairs.java`](Stairs.java).

**`sealed` :** `Command` déclare **la liste complète** de ses implémentations. Le code qui traite une `Command` (la tortue) sait qu'il n'y a que ces 6 cas.

**Les trois façons de « fermer » un sous-type de `permits` :**
- `final` : plus aucun sous-type, et c'est le cas implicite de tout record ;
- `sealed` : de nouveau une liste fermée ;
- `non-sealed` : rouvre la hiérarchie à **n'importe qui**.

**`Move` normalise dans son constructeur compact :** `new Move(-2)` donne `Move[steps=0]`. La règle est appliquée **avant** l'affectation du champ final, donc aucun `Move` invalide ne peut exister.

**`Turn.RIGHT` et `Turn.LEFT`** sont des champs `static` dans un record, ce qui est permis. Un record ne peut pas avoir de champ d'**instance** autre que ses composants.

**Expériences :**

| Expérience | Résultat (vérifié) |
|---|---|
| `class Jump implements Command` | `error: class is not allowed to extend sealed class: C (as it is not listed in its permits clause)` |
| retirer `non-sealed` de `Macro` | `error: sealed, non-sealed or final modifiers expected` |
| `class Spiral extends Square` | **compile** : `Macro` est `non-sealed`, donc ses sous-classes (et leurs sous-classes) sont libres |

---

## Étape 2 — Analyser le programme

**Le code :** `parseBlock` et `parse` de [`TurtleApp.java`](TurtleApp.java), et les trois premières lignes du `main`.

**La récursion de l'analyseur :** `REPEAT 2 [ REPEAT 3 [ … ] MOVE 1 ]`. Le 1er `parseBlock` rencontre `REPEAT` et rappelle `parseBlock` pour le corps, qui rencontre un autre `REPEAT`, et ainsi de suite. Chaque appel s'arrête à **son** `]`. `pos` est partagé (`static`) : chaque appel avance dans le même texte. Imbrication maximale : **2**.

**57 commandes élémentaires :**

| Commande | Calcul | Taille |
|---|---|---|
| `SQUARE 4` | 4 × (Move + Turn) | 8 |
| PenUp, Move 6, PenDown | 1 chacune | 3 |
| `REPEAT 2 [ REPEAT 3 [4] MOVE 1 ]` | 2 × (3 × 4 + 1) | 26 |
| PenUp, Turn, Move, Turn, Move, Turn, Turn, PenDown | 1 chacune | 8 |
| `STAIRS 3` | 3 × 4 | 12 |

Total : 8 + 3 + 26 + 8 + 12 = **57**.

**Question — pourquoi l'égalité des deux `Repeat` est fausse ?** L'`equals` **généré** d'un record compare chaque composant avec `equals`. Pour un tableau, `equals` est celui d'`Object`, qui compare les **références**. Deux `new Command[0]` sont deux objets, donc `false`, même s'ils sont vides tous les deux. Pour une égalité de contenu, il faudrait redéfinir `equals` avec `Arrays.equals` (et `hashCode` avec `Arrays.hashCode`). Les records sont faits pour des composants **immuables**.

**Les `toString` générés :** `Move[steps=3]` et `PenUp[]`. Ce sont le nom du record, puis ses composants entre crochets.

---

## Étape 3 — Dessiner

**Le code :** [`Direction.java`](Direction.java) et [`Turtle.java`](Turtle.java), puis la fin du `main`.

**`Math.floorMod` :** tourner à gauche depuis NORTH (ordinal 0) donne `0 + (-1)` = −1. `-1 % 4` vaut −1, ce qui serait un indice invalide. `floorMod(-1, 4)` vaut **3**, soit WEST.

**La chaîne `instanceof` et `sealed` :** comme `Command` est scellée, la chaîne `if / else if` de `run` couvre **tous** les cas possibles. Avec Java 21, un `switch` à patterns le vérifierait même à la compilation (l'exhaustivité). En Java 17, ce n'est qu'en *preview*.

**`Macro` dans `run` :** on ne connaît pas `Square` ni `Stairs`. On exécute ce que la macro **déplie**, ce qui permet d'ajouter une macro sans toucher à `Turtle`. C'est l'intérêt de la branche `non-sealed`.

**`picture()`** coupe les lignes vides du bas, et `stripTrailing()` les espaces de fin. Les espaces **de tête** et **du milieu**, eux, font partie du dessin.
