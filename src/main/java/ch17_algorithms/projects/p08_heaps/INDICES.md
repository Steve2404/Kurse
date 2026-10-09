# Projet 8 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le tas binaire

<details><summary>Indice 1</summary>

`add` : si le tableau est plein, `heap = Arrays.copyOf(heap, size * 2)` ; puis `heap[size] = v; siftUp(size); size++;`. `poll` : garde `heap[0]`, `size--`, `heap[0] = heap[size]`, puis `siftDown(0)`.

</details>

<details><summary>Indice 2</summary>

`siftDown(i)` : calcule `left = 2 * i + 1` et `right = left + 1` ; cherche l'indice du plus petit parmi `i`, `left` (si `left < size`) et `right` (si `right < size`). Si c'est `i`, stop ; sinon échange et continue depuis cet enfant.

</details>

---

## Étape 2 — Les urgences

<details><summary>Indice 1</summary>

« Plus grave d'abord » : `Comparator.comparingInt(Patient::severity).reversed()`, car `PriorityQueue` sort le plus **petit**.

</details>

<details><summary>Indice 2</summary>

Ajoute le départage : `.thenComparingInt(Patient::arrival)`. `poll()` rend `null` sur une file vide : transforme-le en exception.

</details>

---

## Étape 3 — Les k plus grands, et la fusion

<details><summary>Indice 1</summary>

`topK` : après chaque `add`, si le tas dépasse `k`, `poll()`. À la fin, il contient les `k` plus grands ; vide-le en remplissant le tableau **depuis la fin** (le plus petit sort en premier).

</details>

<details><summary>Indice 2</summary>

`mergeSorted` : `new PriorityQueue<int[]>((x, y) -> Integer.compare(x[0], y[0]))`. Au départ, la tête de chaque liste non vide ; à chaque sortie, ajoute l'élément suivant de la même liste, s'il existe.

</details>

---

## Étape 4 — La médiane

<details><summary>Indice 1</summary>

`add` : `low.add(v); high.add(low.poll()); if (high.size() > low.size()) low.add(high.poll());`.

</details>

<details><summary>Indice 2</summary>

`median` : si `low` est plus gros, `low.peek()` ; sinon `((long) low.peek() + high.peek()) / 2.0`.

</details>

---

## Étape 5 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel ordre exact, quelle égalité, quel grand nombre, quelle taille (plus de 16 éléments, `k` égal à la taille) n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Le parent de la case `i` est calculé en `i / 2` au lieu de `(i - 1) / 2`.
2. En descendant, l'enfant droit est comparé au parent au lieu du plus petit trouvé.
3. Le retrait met l'avant-dernier élément à la racine au lieu du dernier.
4. Le tableau grandit d'une seule case à la fois (O(n²)).
5. Un tas vide ne lance plus d'exception.
6. Les urgences ne départagent plus les égalités par l'arrivée.
7. Les urgences prennent le moins grave d'abord.
8. `topK` garde un élément de trop peu.
9. `topK` rend les valeurs dans l'ordre croissant.
10. La fusion oublie le dernier élément de chaque liste.
11. La médiane ne fait plus passer les nombres par la moitié haute.
12. La moyenne des deux sommets est calculée en `int` (elle déborde).
13. La moitié basse est un tas min au lieu d'un tas max.

</details>
