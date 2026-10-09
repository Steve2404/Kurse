# Projet 7 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Ce que coûte le legacy

<details><summary>Indice 1</summary>

Regarde `undo` : que met-il à jour, et qu'oublie-t-il, comparé à `apply` ?

</details>

<details><summary>Indice 2</summary>

Avant la frappe numéro k (en comptant de 0), le texte fait k caractères, et le legacy en garde une copie. Le total est donc 0 + 1 + 2 + … + 9 999.

</details>

---

## Étape 2 — Le document et ses observateurs

<details><summary>Indice 1</summary>

Une méthode privée `fire(DocumentEvent event)` : `List.copyOf(listeners).forEach(listener -> listener.changed(event));`. Une autre, `checkPosition(int position)`, sert à `insert` et à `delete`.

</details>

<details><summary>Indice 2</summary>

Dans le test, la classe anonyme `new DocumentListener() { … }` peut écrire `doc.removeListener(this)` : `this` y désigne l'abonné lui-même (une lambda ne le pourrait pas, son `this` serait la classe de test).

</details>

---

## Étape 3 — Les commandes et l'historique

<details><summary>Indice 1</summary>

`ArrayDeque` : `push` ajoute en tête, `pop` retire la tête, `removeLast` retire la queue (la plus ancienne). `undoStack.stream()` parcourt depuis la tête : la plus récente d'abord.

</details>

<details><summary>Indice 2</summary>

`DeleteCommand` a un champ **non final** `removed`, rempli dans `execute` par ce que rend `document.delete(…)`, et réutilisé par `undo` : `document.insert(position, removed)`.

</details>

---

## Étape 4 — Le memento et la macro

<details><summary>Indice 1</summary>

`Snapshot` est imbriquée dans `Document` : `Document` peut appeler son constructeur privé et lire son champ privé ; personne d'autre. `restore` : `text.setLength(0); text.append(snapshot.text);` puis l'événement.

</details>

<details><summary>Indice 2</summary>

`ReplaceAllCommand.execute` : la photographie, puis `String replaced = document.text().replace(target, replacement);`, puis supprimer tout le texte et insérer `replaced` à la position 0. La macro défait avec une boucle `for` qui descend de `steps.size() - 1` à 0.

</details>

---

## Étape 5 — Les mutants

<details><summary>Indice 1</summary>

Pour l'historique, teste les **bouts** : rien à annuler, rien à refaire, la capacité pile atteinte, une nouvelle action après une annulation.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. On ne peut plus insérer à la toute fin du texte.
2. Les abonnés sont prévenus sur la liste elle-même, sans copie.
3. `insert` ne prévient plus personne.
4. L'événement `delete` ne transporte plus le texte retiré.
5. `removeListener` ne désabonne plus.
6. Annuler une suppression remet le texte au début du document.
7. Une nouvelle action ne vide plus la pile « refaire ».
8. Annuler ne permet plus de refaire.
9. L'historique garde une commande de moins que sa capacité.
10. Annuler sans rien à annuler lance une exception au lieu de rendre `false`.
11. Une capacité de 0 est acceptée.
12. Les commandes sont empilées du mauvais côté : annuler défait la plus ancienne.
13. La macro défait ses étapes dans l'ordre normal.
14. Annuler un remplacement ne restaure rien.
15. Les mots sont séparés par une seule espace (plusieurs espaces font des mots vides).
16. Le compteur ne compte pas le texte déjà présent quand on l'abonne.

</details>
