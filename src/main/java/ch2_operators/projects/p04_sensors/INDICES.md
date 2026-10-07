# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La trame et les contrôles qui se comptent

<details><summary>Indice 1</summary>

La classe trame a 4 champs et un constructeur avec `this.`. Les 4 contrôles sont des méthodes `static` de `Sensors`. Chacune reçoit une trame et commence par `controls++;`.

</details>

<details><summary>Indice 2</summary>

« entre −20 et 50 » s'écrit `f.temperature >= -20 && f.temperature <= 50`. Java n'a pas de `-20 <= t <= 50` : cette forme compare un `boolean` à un `int`.

</details>

---

## Étape 2 — `&&` contre `&`

<details><summary>Indice 1</summary>

Dans la méthode de rapport : `controls = 0;`, puis `boolean lazy = c1(f) && c2(f) && c3(f) && c4(f);`, puis mémoriser `controls` dans une variable locale. Recommencer avec `&`.

</details>

<details><summary>Indice 2</summary>

Pour la prédiction : avec `&&`, compte les contrôles jusqu'au **premier** qui échoue, inclus. Si aucun n'échoue, c'est 4. Avec `&`, c'est toujours 4.

</details>

---

## Étape 3 — `^` et `||`

<details><summary>Indice 1</summary>

« exactement un des deux échoue » : `!temperatureOk(f) ^ !humidityOk(f)`. Le XOR vaut `true` quand les deux côtés **diffèrent**.

</details>

<details><summary>Indice 2</summary>

Le numéro de trame : `"trame #" + ++frames + …`, comme pour le ticket du projet 1. Le compteur `frames` est distinct de `controls`.

</details>

---

## Étape 4 — Incréments, affectations, références

<details><summary>Indice 1</summary>

Pour chaque terme, note deux choses : la **valeur rendue** et la **nouvelle valeur de `id`**. `id++` rend l'ancienne valeur ; `++id` rend la nouvelle. L'évaluation va de gauche à droite.

</details>

<details><summary>Indice 2</summary>

- Pour `instanceof`, déclare `Object number = Integer.valueOf(42);`, `Object text = "42";` et `Object nothing = null;`.
- Pour l'expérience, écris le littéral `"texte"` directement devant `instanceof Integer`. `javac` connaît alors son type exact.

</details>
