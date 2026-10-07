# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les exceptions de la machine

<details><summary>Indice 1</summary>

Une exception **abstraite** : `public abstract class VmException extends Exception`, avec un constructeur `protected` et une méthode abstraite `code()`. Chaque fille implémente `code()`.

</details>

<details><summary>Indice 2</summary>

Pour la question : la machine **rattrape** toutes les `VmException` avec ses gestionnaires `TRY`. Une limite de pas doit-elle pouvoir être rattrapée par le programme qu'elle surveille ?

</details>

---

## Étape 2 — La machine

<details><summary>Indice 1</summary>

Une méthode `private int pop(int index) throws StackUnderflowException` centralise le test de la pile vide. `step` est un `switch` sur le premier mot, qui rend `pc + 1` par défaut.

</details>

<details><summary>Indice 2</summary>

Dans `run`, le `catch` : `Handler h = handlers.pop(); while (stack.size() > h.depth()) stack.pop(); stack.push(code); pc = h.target();`.

</details>

---

## Étape 3 — Les programmes

<details><summary>Indice 1</summary>

`program.split("\\|")` sépare le nom du code. Une `Machine` neuve par programme.

</details>

<details><summary>Indice 2</summary>

`state()` : copie la pile dans une liste, puis `Collections.reverse`. Le sommet d'une `Deque` utilisée comme pile est en **tête**.

</details>

---

## Étape 4 — Les règles de `finally`

<details><summary>Indice 1</summary>

Écris les quatre méthodes **exactement** comme l'énoncé les décrit, puis observe ce que chacune rend. Le comportement est fixé par le langage.

</details>

<details><summary>Indice 2</summary>

Pour la question sur l'`ArithmeticException`, regarde `getCause()` et `getSuppressed()` de l'exception attrapée.

</details>
