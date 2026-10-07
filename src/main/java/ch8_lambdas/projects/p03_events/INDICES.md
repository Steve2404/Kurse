# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La file d'événements

<details><summary>Indice 1</summary>

Dans un tas rangé dans un tableau, les enfants de la case i sont `2i + 1` et `2i + 2`, et son parent est `(i - 1) / 2`. La racine (case 0) est toujours le plus petit élément.

</details>

<details><summary>Indice 2</summary>

- `push` : range à la fin, puis `while (i > 0 && heap[i].before(heap[(i - 1) / 2]))`, échange et remonte.
- `pop` : garde `heap[0]`, mets `heap[--size]` à sa place, puis descends : compare avec les deux enfants et échange avec le plus petit, tant qu'il passe avant.

</details>

---

## Étape 2 — L'agence

<details><summary>Indice 1</summary>

`run()` : `schedule(0, "ouverture", this::arrival);`, puis `while (!events.isEmpty()) { Event e = events.pop(); clock = e.time(); e.action().run(); }`.

</details>

<details><summary>Indice 2</summary>

- La file circulaire : `waitingSince[tail % 64] = clock; waitingName[tail++ % 64] = name;`. La taille de la file vaut `tail - head`.
- La fin de service : `schedule(clock + duration, "fin", () -> { served++; freeTellers++; listener.accept(clock, name + " repart"); startServices(); });`.

</details>

---

## Étape 3 — Le programme

<details><summary>Indice 1</summary>

`Supplier<String> names = () -> "C" + ++counter[0];` : `++counter[0]` incrémente la case, puis rend la nouvelle valeur.

</details>

<details><summary>Indice 2</summary>

- `alarm` : si le message contient `"apres"`, lis le nombre entre `indexOf("apres ") + 6` et `indexOf(" min")`.
- Combine les écouteurs avec `journal.andThen(count).andThen(alarm)`.

</details>
