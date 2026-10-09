# Projet 7 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`EditorTest.java`](EditorTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** et **JUnit 5.11.4**.

---

## Étape 1 — Ce que coûte le legacy

La sortie de `Data` (vérifiée) :

```
texte : B0nj0ur m0nde | mots a l'ecran : 2
apres annuler : Bonjour monde | mots a l'ecran : 2
apres annuler encore : Bonjour le monde | mots a l'ecran : 2
10 000 frappes : 49995000 caracteres gardes pour annuler
```

**Question — les trois lignes :** le remplacement des `o` par des `0`, puis deux annulations. Le texte revient bien en arrière, mais l'écran affiche toujours **2 mots** alors que `Bonjour le monde` en a 3 : `undo` remet le texte **sans** mettre à jour le compteur. Chaque endroit qui change le texte doit penser à prévenir l'écran ; un seul oubli, et l'écran ment. Avec l'observateur, c'est le **document** qui prévient, quel que soit le chemin du changement.

**Question — le coût :** **49 995 000** caractères (vérifié) : avant la frappe numéro k, le texte fait k caractères et le legacy en garde une copie, donc 0 + 1 + … + 9 999 = 9 999 × 10 000 / 2 = 49 995 000. C'est en **O(n²)** (chapitre 17) : 100 000 frappes en garderaient près de 5 milliards. En ne gardant que **ce qui a changé** (un caractère par frappe), il en faudrait **10 000**.

**Question — une nouvelle action :** un `case` de plus dans le `switch` de `apply`, en espérant ne rien casser dans les autres, et l'appel ne serait toujours qu'un texte (`"upper"`), sans aucune vérification du compilateur.

---

## Étape 2 — Le document et ses observateurs

Le code : [`DocumentEvent.java`](DocumentEvent.java), [`DocumentListener.java`](DocumentListener.java), [`Document.java`](Document.java), [`ChangeLog.java`](ChangeLog.java), [`WordCounter.java`](WordCounter.java). Les tests : `documentInsertsDeletesAndChecksPositions`, `anUnsubscribedListenerHearsNothing`, `aListenerMayUnsubscribeWhileBeingNotified`, `wordCount`.

**Expérience — sans copie** (vérifié) : le test du désabonnement échoue avec `ConcurrentModificationException`. L'abonné modifie la liste pendant que `forEach` la parcourt ; `ArrayList` le détecte et s'arrête net : le second abonné n'est jamais prévenu. Avec `List.copyOf(listeners)`, on parcourt une **photographie** de la liste : les changements s'appliqueront à la notification suivante.

---

## Étape 3 — Les commandes et l'historique

Le code : [`Command.java`](Command.java), [`InsertCommand.java`](InsertCommand.java), [`DeleteCommand.java`](DeleteCommand.java), [`History.java`](History.java). Les tests : `undoAndRedoWalkThroughTheHistory`, `aNewActionErasesTheRedoStack`, `historyForgetsTheOldestBeyondItsCapacity`.

**Expérience — 10 000 commandes :** chaque `InsertCommand` ne garde que son texte, un caractère : **10 000** caractères en tout, contre 49 995 000 pour le legacy, soit environ 5 000 fois moins. Et le coût **grandit comme le nombre de frappes**, pas comme son carré.

**Question — une commande de plus :** **rien**. `History` ne connaît que l'interface `Command` : on écrit `UpperCaseCommand implements Command`, et on la passe à `run`. C'est encore le principe ouvert/fermé (projet 2), appliqué aux actions.

---

## Étape 4 — Le memento et la macro

Le code : [`ReplaceAllCommand.java`](ReplaceAllCommand.java), [`MacroCommand.java`](MacroCommand.java), et `Snapshot` dans [`Document.java`](Document.java). Les tests : `replaceAllUndoesWithASnapshot`, `macroUndoesInReverseOrder`, `observersAreNotifiedOfEveryChange`.

**Question — l'ordre inverse :** chaque étape a été faite sur l'état laissé par la **précédente** ; pour la défaire, il faut retrouver exactement cet état, donc défaire d'abord la **dernière**. Comme on enlève des vêtements dans l'ordre inverse où on les a mis. Sur l'exemple, défaire dans l'ordre normal donne `'La te'` (vérifié) au lieu de `'tarte'` : on supprime les 3 premiers caractères de `tarte aux pommes`, on remet `La ` devant, puis on coupe 11 caractères à partir de la position 5.

**Question — le memento acceptable :** le memento n'est pris que pour **une** sorte d'action, rare (remplacer partout), et seulement quand elle a lieu : son coût est la taille du document **par remplacement**, pas par frappe. Le legacy, lui, photographiait tout à **chaque** frappe. On choisit, action par action, la façon d'annuler la moins chère : l'opération inverse quand elle est simple et sûre, la photographie quand l'inverse est impossible.

---

## Étape 5 — Les mutants

Les 16 mutants sont tués par les 14 tests de référence. Le seul test `aListenerMayUnsubscribeWhileBeingNotified` en attrape 4 le premier (vérifié dans la sortie de `Check solution` : les mutants 1, 2, 3 et 5), parce qu'il traverse **tout** le chemin de la notification : insérer à la fin, prévenir sur une copie, désabonner.
