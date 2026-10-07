# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le résumé d'un morceau, et sa fusion

<details><summary>Indice 1</summary>

Dessine deux morceaux voisins, par exemple `[3 3 5 5]` et `[5 5 5 2]`. La plus longue série traverse la frontière : elle vaut la queue du 1er plus la tête du 2e.

</details>

<details><summary>Indice 2</summary>

Si le 1er morceau n'est **qu'une** série (`prefix == longueur`) et que `last == next.first`, sa tête continue dans le suivant : le nouveau `prefix` vaut `longueur + next.prefix`. Même raisonnement pour le `suffix`, à partir du morceau suivant.

</details>

---

## Étape 2 — Les threads du découpage

<details><summary>Indice 1</summary>

Deux boucles séparées : la 1re fait `start()` sur tous les threads, la 2e fait `join()` sur tous. Un `join()` juste après chaque `start()` ferait tout travailler l'un après l'autre.

</details>

<details><summary>Indice 2</summary>

`Thread.currentThread().getName()` donne le nom du thread **qui exécute** la ligne. Le nom se donne au constructeur : `new Thread(tache, "dl-0")`, ou `super(nom)` dans une classe qui étend `Thread`.

</details>

---

## Étape 3 — `run()` contre `start()`, et les états

<details><summary>Indice 1</summary>

Pour que `bloque` soit **BLOCKED**, `main` doit **déjà** tenir le verrou quand `bloque` essaie d'entrer : démarre les trois threads **à l'intérieur** du `synchronized (lock)` de `main`.

</details>

<details><summary>Indice 2</summary>

`waitFor` attend que l'état soit atteint : sans lui, tu lirais un état trop tôt (`RUNNABLE`), car un thread qui vient de démarrer n'est pas encore en train de dormir.

</details>

---

## Étape 4 — Interruption, daemon, erreurs

<details><summary>Indice 1</summary>

Quand `sleep` est interrompu, il lance `InterruptedException` **et efface** le drapeau d'interruption. Une boucle qui teste `isInterrupted()` ne dort pas : le drapeau reste levé.

</details>

<details><summary>Indice 2</summary>

`setDaemon` n'est permis qu'**avant** `start()`, et un thread ne peut démarrer qu'**une** fois. Les deux erreurs sont la même exception, non vérifiée : attrape-la dans un `try`.

</details>
