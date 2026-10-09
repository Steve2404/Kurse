# Projet 11 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les temps entre les lieux

<details><summary>Indice 1</summary>

Pour chaque lieu `places[i]`, un Dijkstra donne les temps vers **tous** les carrefours ; garde seulement ceux vers les autres lieux : `t[i][j] = d[places[j]]`.

</details>

<details><summary>Indice 2</summary>

Rue à double sens : dans le constructeur, ajoute l'arête dans les deux listes d'adjacence.

</details>

---

## Étape 2 — Held-Karp

<details><summary>Indice 1</summary>

Construis `places = {0, stops[0], stops[1], …}` et calcule `travelTimes(places)` : l'arrêt `i` de `stops` est le lieu `i + 1`, et le dépôt est le lieu 0.

</details>

<details><summary>Indice 2</summary>

Trois boucles : `mask` de 1 à `(1 << k) - 1`, puis `i` (dans `mask`, et case déjà atteinte), puis `j` (hors de `mask`, et route possible). Pour remonter : à partir de la meilleure fin `i` et du masque complet, ajoute `stops[i]`, lis `parent[mask][i]` **avant** de retirer `i` du masque, puis continue jusqu'au masque vide, et retourne la liste.

</details>

---

## Étape 3 — L'oracle

<details><summary>Indice 1</summary>

L'oracle est un retour arrière (projet 6) : depuis l'endroit actuel, essaie chaque arrêt pas encore visité, et garde le meilleur total, en ajoutant le retour au dépôt quand tout est visité.

</details>

<details><summary>Indice 2</summary>

Pour une ville connexe au hasard : relie chaque paire de carrefours avec une probabilité (par exemple 2 chances sur 3), puis ajoute une ligne `0-1-2-…` aux temps élevés pour être sûr que tout est relié.

</details>

---

## Étape 4 — Le plus de livraisons à l'heure

<details><summary>Indice 1</summary>

`Integer[] ordre` des indices, trié avec `Arrays.sort(ordre, Comparator.comparingInt(i -> deadlines[i]))`. Un tas max : `new PriorityQueue<>(Collections.reverseOrder())`.

</details>

<details><summary>Indice 2</summary>

Garde le temps total dans un `long`. Après chaque ajout, si le total dépasse l'échéance de la livraison qu'on vient d'ajouter, `total -= tas.poll()`. La réponse est la taille du tas.

</details>

---

## Étape 5 — Les mutants

<details><summary>Indice : ce que change chaque mutant</summary>

1. Les rues ne vont plus que dans un sens.
2. Un temps négatif est accepté.
3. Dijkstra oublie le temps déjà parcouru.
4. Le dépôt est accepté comme arrêt.
5. Un arrêt en double est accepté.
6. Treize arrêts sont acceptés.
7. À égalité, la tournée finit au dernier arrêt possible au lieu du premier.
8. La tournée oublie le retour au dépôt.
9. Le premier trajet depuis le dépôt est gratuit.
10. L'ordre des arrêts n'est pas retourné.
11. Les livraisons sont triées par durée au lieu d'échéance.
12. On abandonne la livraison la plus courte au lieu de la plus longue.
13. Finir pile à l'échéance est considéré comme un retard.

</details>
