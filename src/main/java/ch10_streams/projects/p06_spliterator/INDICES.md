# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le modèle

<details><summary>Indice 1</summary>

`record Item(int quantity, String article, long unitCents)` et `record Transaction(String id, String customer, List<Item> items, List<String> unreadable)`.

</details>

<details><summary>Indice 2</summary>

`"12.50".replace(".", "")` donne `"1250"`, puis `Long.parseLong`. Cela suppose toujours 2 décimales.

</details>

---

## Étape 2 — `tryAdvance` : lire UNE transaction

<details><summary>Indice 1</summary>

Le spliterator a trois champs : la liste des lignes, `from` (modifiable) et `to` (final). `tryAdvance` avance `from` au-delà de ce qu'il lit.

</details>

<details><summary>Indice 2</summary>

- Saute ce qui ne commence pas par `TX `. Si `from >= to`, rends `false`.
- Lis l'en-tête (`from++`), puis les lignes jusqu'au prochain en-tête. Une ligne d'article valide a 6 mots : `±`, qte, `x`, article, `a`, prix.

</details>

---

## Étape 3 — `estimateSize` et `characteristics`

<details><summary>Indice 1</summary>

`estimateSize()` rend `to - from` : un nombre de **lignes**, donc un majorant du nombre de transactions.

</details>

<details><summary>Indice 2</summary>

`return ORDERED | NONNULL | IMMUTABLE;`. Les constantes sont des bits : `|` les combine.

</details>

---

## Étape 4 — En faire un `Stream`

<details><summary>Indice 1</summary>

`StreamSupport.stream(new TransactionSpliterator(log, 0, log.size()), false)`.

</details>

<details><summary>Indice 2</summary>

Le meilleur client : `groupingBy(Transaction::customer, TreeMap::new, summingLong(Transaction::cents))`, puis `max(Map.Entry.comparingByValue())`.

</details>

---

## Étape 5 — `trySplit` : l'algorithme de découpe

<details><summary>Indice 1</summary>

`mid = (from + to) >>> 1`, puis `while (mid < to && !isHeader(lines.get(mid))) mid++;`. Si `mid >= to`, rends `null`. Sinon, crée le préfixe `[from, mid)`, puis fais `from = mid`.

</details>

<details><summary>Indice 2</summary>

La découpe récursive : `prefix = s.trySplit()`. Si c'est `null`, ajoute `s` à la liste. Sinon, découpe d'abord `prefix`, puis `s`, pour garder l'ordre gauche → droite.

</details>

---

## Étape 6 — `tryAdvance` puis `forEachRemaining`

<details><summary>Indice 1</summary>

Un `StringBuilder` capturé par la lambda de `tryAdvance` reçoit l'id. Une liste reçoit le reste avec `forEachRemaining(rest::add)`.

</details>

<details><summary>Indice 2</summary>

Le dernier appel : `s.tryAdvance(t -> { })`, avec une action qui ne fait rien.

</details>

---

## Étape 7 — Répartir des lots avec le spliterator d'une `List`

<details><summary>Indice 1</summary>

`Data.JOBS.spliterator()`. Si `estimateSize() > max`, coupe avec `trySplit()` et recommence sur les deux morceaux.

</details>

<details><summary>Indice 2</summary>

Un morceau assez petit se vide dans une liste avec `forEachRemaining(batch::add)`.

</details>

---

## Étape 8 — Lire les caractéristiques

<details><summary>Indice 1</summary>

Une `TreeMap<Integer, String>` constante → nom affiche les noms dans l'ordre **croissant** des valeurs.

</details>

<details><summary>Indice 2</summary>

`s.hasCharacteristics(constante)` pour chaque entrée, puis `s.getExactSizeIfKnown()`.

</details>

---

## Étape 9 — `main`

<details><summary>Indice 1</summary>

Affiche les transactions, puis les anomalies, puis COMPTE, CA TOTAL, MEILLEUR CLIENT, DECOUPAGE, PREMIERE, LOTS et les caractéristiques.

</details>

<details><summary>Indice 2</summary>

Les anomalies : `all.forEach(t -> t.unreadable().forEach(l -> …))`.

</details>
