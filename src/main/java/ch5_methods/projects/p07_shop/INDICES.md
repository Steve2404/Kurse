# Projet 7 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 6. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le modèle

<details><summary>Indice 1</summary>

`Product.of` : `new Product()`, remplir les champs privés (on est dans la classe, c'est permis), plafonner avec `Math.min(stock, MAX_STOCK)`, puis `created++`. C'est le même principe que la fabrique du projet 2.

</details>

<details><summary>Indice 2</summary>

`adjust(delta)` : mémorise `before`, puis `stock = Math.max(0, Math.min(MAX_STOCK, stock + delta))`, puis rends `stock - before`. `all()` : `Arrays.copyOf(products, size)`.

</details>

---

## Étape 2 — Les commandes

<details><summary>Indice 1</summary>

Une méthode `private static String execute(Catalog, String)` dans `ShopApp`, avec un `switch` sur le premier mot. Pour `STOCK`, `new int[p.length - 2]` reçoit les mots 2 et suivants.

</details>

<details><summary>Indice 2</summary>

Pour `REMISE` : si le texte finit par `c`, retire-le et appelle `discount(sku, Long.parseLong(…))`, ce qui choisit la version `long`. Sinon, `Integer.parseInt` choisit la version `int`.

</details>

---

## Étape 3 — L'inventaire

<details><summary>Indice 1</summary>

`pad(String s, int width)` : complète à droite avec `" ".repeat(width - s.length())`. `pad(long n, int width)` : complète à **gauche**. `pad(String s)` : `return pad(s, WIDTH);`.

</details>

<details><summary>Indice 2</summary>

L'en-tête : `pad("REF") + pad("PRODUIT") + pad("PRIX", 9) + pad("STOCK", 7) + "VENDUS"`. Copie défensive : `Product[] copy = catalog.all(); copy[0] = null;`, puis teste `catalog.find(1) != null`.

</details>

---

## Étape 4 — Le meilleur panier

<details><summary>Indice 1</summary>

`best` remet `explored` à 0, crée `chosen` (n cases) et `best` (`long[n + 1]`), appelle `explore(…, 0, budget, 0, chosen, best)`, puis convertit `best[0..n-1]` en `boolean[]`.

</details>

<details><summary>Indice 2</summary>

Dans `explore`, après le record : si `i == p.length`, `return`. Branche « je prends » : `chosen[i] = true`, l'appel avec `spent + prix`, puis `chosen[i] = false`. Branche « je ne prends pas » : l'appel avec le même `spent`.

</details>
