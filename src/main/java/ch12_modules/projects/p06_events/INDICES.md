# Projet 6 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 5. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le jar hérité et le modèle

<details><summary>Indice 1</summary>

`Math.toRadians` convertit les degrés en radians avant `Math.sin` et `Math.cos`. Le carré d'un sinus : `Math.pow(Math.sin(x), 2)`.

</details>

<details><summary>Indice 2</summary>

Le jar hérité s'appellera `geo-tools-1.0.jar` : son nom de module automatique sera donc `geo.tools` (projet 4).

</details>

---

## Étape 2 — Le cœur

<details><summary>Indice 1</summary>

Deux `PriorityQueue` : les salles occupées, triées par **heure de fin** (un petit `record` salle + fin), et les numéros de salles libres (ordre naturel des entiers).

</details>

<details><summary>Indice 2</summary>

`events.core` lit `com.geo.Distance` grâce à `requires geo.tools;` : un module automatique exporte tous ses paquets (projet 4).

</details>

---

## Étape 3 — Fournisseurs, audit, application

<details><summary>Indice 1</summary>

`Auditor` reçoit un objet d'un **autre** module : il le fouille avec `getDeclaredFields()` et `setAccessible(true)`. Cela ne marche que parce que `events.core` **ouvre** `events.core.state` à `events.audit` (projet 3).

</details>

<details><summary>Indice 2</summary>

Pour la dernière ligne : `Dispatcher.class.getModule().getDescriptor().requires()`, puis garde les noms qui commencent par `geo`.

</details>

---

## Étape 4 — Le script `build.sh`

<details><summary>Indice 1</summary>

La boucle bash : `for m in a b c; do jar --create --file "$OUT/jars/$m.jar" -C "$OUT/mods/$m" .; done`.

</details>

<details><summary>Indice 2</summary>

`sed -n '2,4p'` n'affiche que les lignes 2 à 4. `jlink` doit **échouer** ici : c'est le message de refus que la sortie attend.

</details>
