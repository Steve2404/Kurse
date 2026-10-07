# Projet 7 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 6. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les bundles (fichiers `.properties`)

<details><summary>Indice 1</summary>

Les fichiers vont dans `src/main/resources/`, dans **le même chemin de dossiers que le paquet**. `getBundle` les cherche dans le classpath, comme une classe.

</details>

<details><summary>Indice 2</summary>

Dans un motif `MessageFormat`, une apostrophe **seule** ouvre un texte littéral. Les `{0}` qui suivent ne seraient plus remplacés. Pour une vraie apostrophe, double-la.

</details>

---

## Étape 2 — Réglages et catalogue

<details><summary>Indice 1</summary>

`Properties` hérite de `Hashtable<Object, Object>` : `put` accepte n'importe quel objet, mais `getProperty` ne rend que les valeurs `String`.

</details>

<details><summary>Indice 2</summary>

`total` : `cart.split(",")`, puis chaque entrée `split(":")`. `Integer.parseInt` lève toute seule la `NumberFormatException` voulue. `price` rend un `Long` de la map, et lance l'exception si c'est `null`.

</details>

---

## Étape 3 — Les clients

<details><summary>Indice 1</summary>

`bundle.getLocale()` donne la locale du fichier **réellement trouvé**. Pour la racine, c'est `Locale.ROOT`, dont le `toString()` est vide.

</details>

<details><summary>Indice 2</summary>

Les montants sont en centimes : formate `total / 100.0`. Les deux exceptions se rattrapent dans **un seul** `catch` multiple, avec `|`.

</details>

---

## Étape 4 — Racine, erreurs, traduction

<details><summary>Indice 1</summary>

`ResourceBundle.getBundle(BASE, Locale.ROOT)` charge directement `shop.properties`. `keySet()` donne un `Set` non trié : passe par un stream trié.

</details>

<details><summary>Indice 2</summary>

`coverage` : un stream sur `root.keySet()`, `filter(k -> !bundle.getString(k).equals(root.getString(k)))`, puis `count()`. Le ratio est un `double` : caste avant de diviser.

</details>

---

## Étape 5 — Locales et catégories

<details><summary>Indice 1</summary>

`new Locale("FR", "ca")` normalise la casse : la langue en minuscules, le pays en majuscules. Le `Builder`, lui, **vérifie** chaque valeur : une région est un code de 2 lettres (ou 3 chiffres).

</details>

<details><summary>Indice 2</summary>

`getDisplayName()` **sans** argument utilise la catégorie `DISPLAY`. `getCurrencyInstance()` sans argument utilise la catégorie `FORMAT`.

</details>
