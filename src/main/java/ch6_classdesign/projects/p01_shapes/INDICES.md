# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La hiérarchie

<details><summary>Indice 1</summary>

Dans le constructeur d'une sous-classe, la **première** instruction est `super(…)` (le parent) ou `this(…)` (un autre constructeur de la même classe). Jamais les deux, et jamais en 2e ligne.

</details>

<details><summary>Indice 2</summary>

- `describe()` : `name + " aire=" + r2(area()) + " perimetre=" + r2(perimeter()) + extra()`. `area()` et `extra()` sont ceux de l'**objet réel**.
- Héron : `s = (a + b + c) / 2`, puis `Math.sqrt(s * (s - a) * (s - b) * (s - c))`.
- L'inégalité : chaque côté est plus petit que la somme des deux autres.

</details>

---

## Étape 2 — Trier des objets par leur méthode

<details><summary>Indice 1</summary>

Le tri par insertion du chapitre 4, sur un `Shape[]` : `while (j >= 0 && shapes[j].area() < key.area())`. Le `<` strict garde la stabilité.

</details>

<details><summary>Indice 2</summary>

Une seule boucle for-each sur le tableau trié construit l'ordre, additionne les aires et retient le plus grand périmètre. Arrondis le total avec `Shape.r2`.

</details>

---

## Étape 3 — Le type de la référence ou de l'objet

<details><summary>Indice 1</summary>

`Rectangle r = new Square(5);`, puis `"… : " + r` (qui appelle `toString()`, donc `describe()`), `r instanceof Square` et `r.getClass().getSimpleName()`.

</details>

<details><summary>Indice 2</summary>

Pour la question, distingue ce que `javac` vérifie (le type de la **variable**) de ce que la JVM exécute (la méthode de l'**objet**).

</details>

---

## Étape 4 — L'enveloppe convexe et le point dans le polygone

<details><summary>Indice 1</summary>

- Le lacet : pour chaque i, `p = points[i]` et `q = points[(i + 1) % n]`, puis `sum += p[0] * q[1] - q[0] * p[1]`. Aire = `Math.abs(sum) / 2`.
- `cross(o, a, b)` = `(a[0] - o[0]) * (b[1] - o[1]) - (a[1] - o[1]) * (b[0] - o[0])`.

</details>

<details><summary>Indice 2</summary>

- Andrew : un tableau `hull` de taille 2n et un indice `k`. Chaîne inférieure : `while (k >= 2 && cross(hull[k-2], hull[k-1], p) <= 0) k--;`, puis `hull[k++] = p`. Chaîne supérieure : de `n - 2` à 0, avec une borne `lower = k + 1`. Résultat : les `k - 1` premiers.
- `contains` : `for (int i = 0, j = n - 1; i < n; j = i++)`.

</details>
