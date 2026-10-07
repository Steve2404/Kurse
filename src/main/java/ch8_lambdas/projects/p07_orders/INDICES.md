# Projet 7 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 6. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les données

<details><summary>Indice 1</summary>

`Purchase.parse` : après les 3 premiers mots, chaque mot `BOOK:2` se coupe sur `:`. Deux tableaux de taille `p.length - 3`.

</details>

<details><summary>Indice 2</summary>

`Promo.parse` : `value` et `minimum` sont des variables locales, capturées par les lambdas. Le choix de l'effet se fait avec un ternaire entre deux lambdas : `p[1].equals("fixed") ? amount -> … : amount -> …`.

</details>

---

## Étape 2 — Le moteur

<details><summary>Indice 1</summary>

Le currying : `percent -> amount -> …`. C'est une lambda qui rend une lambda. `PERCENT_OFF.apply(15)` donne la fonction « −15 % », qu'on applique ensuite avec `applyAsLong`.

</details>

<details><summary>Indice 2</summary>

- `bestPromos` : deux boucles i et j. `LongUnaryOperator first = promos[i]::apply;` est une référence sur un objet précis, et `first.andThen(promos[j]::apply)` les enchaîne.
- La livraison : `LongSupplier shipping = () -> { … }`, appelée **seulement** si le prix est < 5000.

</details>

---

## Étape 3 — Le programme

<details><summary>Indice 1</summary>

`Consumer<String> log = audit::append;`, avec `audit` un `StringBuilder`. `log.andThen(separator).andThen(counter)` forme un seul `Consumer` passé au moteur.

</details>

<details><summary>Indice 2</summary>

Pour les questions, calcule à la main sur 10000 : −10 % puis −500, et −500 puis −10 %.

</details>
