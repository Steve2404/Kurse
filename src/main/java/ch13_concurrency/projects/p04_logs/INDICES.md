# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les agrégats concurrents

<details><summary>Indice 1</summary>

`merge(clé, 1, Integer::sum)` fait « lire, ajouter, écrire » **en une seule** opération atomique sur une `ConcurrentHashMap`. C'est ce qui la rend sûre entre plusieurs threads.

</details>

<details><summary>Indice 2</summary>

Découpe la ligne sur l'espace, puis chaque morceau sur `=` : la valeur est après le `=`.

</details>

---

## Étape 2 — Le pipeline

<details><summary>Indice 1</summary>

`put` attend s'il n'y a plus de place dans la file, `take` attend s'il n'y a rien à prendre. Le consommateur fait une boucle `while (true)` qui sort quand il reçoit la pilule.

</details>

<details><summary>Indice 2</summary>

Les pilules ne s'envoient qu'**après** la fin de **tous** les producteurs (leur `awaitTermination`). Chaque consommateur s'arrête à **une** pilule : il en faut une par consommateur.

</details>

---

## Étape 3 — Les règles des collections

<details><summary>Indice 1</summary>

`offer(x, délai, unité)` et `poll(délai, unité)` attendent au plus le délai, puis abandonnent : `false` pour `offer`, `null` pour `poll`.

</details>

<details><summary>Indice 2</summary>

Une `CopyOnWriteArrayList` fait une **copie** du tableau à chaque modification. Un for-each parcourt la version qui existait **au début** de la boucle.

</details>
