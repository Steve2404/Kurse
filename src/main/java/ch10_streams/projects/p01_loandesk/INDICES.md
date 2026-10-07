# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Concevoir le modèle

<details><summary>Indice 1</summary>

Le livre peut être un record immuable (isbn, titre, auteur). Le **stock**, qui change, vit à part dans le guichet : une `Map` isbn → nombre.

</details>

<details><summary>Indice 2</summary>

Le membre garde l'email **brut**, qui peut être `null`. Une méthode `email()` fabrique l'`Optional` : `Optional.ofNullable(raw).map(String::strip).filter(e -> !e.isBlank())`.

</details>

---

## Étape 2 — Une interface de recherche de livres

<details><summary>Indice 1</summary>

La méthode `default` combine les deux recherches abstraites. Cherche dans la Javadoc d'`Optional` une méthode ajoutée en Java 9 qui rend un **`Optional`** de secours.

</details>

<details><summary>Indice 2</summary>

`byIsbn(q).or(() -> byTitle(q))`.

</details>

---

## Étape 3 — L'état du guichet et ses recherches

<details><summary>Indice 1</summary>

`LinkedHashMap` garde l'ordre d'insertion. Une file par livre : `Map<String, Deque<String>>`.

</details>

<details><summary>Indice 2</summary>

`Optional.ofNullable(members.get(id))` transforme le `null` de `Map.get` en `Optional` vide. L'emprunt se trouve avec un stream : `loans.stream().filter(…).findFirst()`.

</details>

---

## Étape 4 — `EMPRUNT <membre> <isbn>`

<details><summary>Indice 1</summary>

`findMember(m).map(member -> byIsbn(i).map(book -> action.apply(member, book)).orElse("REFUS : livre inconnu …")).orElse("REFUS : membre inconnu …")`.

</details>

<details><summary>Indice 2</summary>

Pour mettre en attente : `computeIfAbsent(isbn, k -> new ArrayDeque<>())`, puis `addLast`. La position est la taille de la file.

</details>

---

## Étape 5 — La pénalité de retard

<details><summary>Indice 1</summary>

`if (jours <= 3) return Optional.empty();`, sinon `Optional.of(Math.min((jours - 3) * 50, 1000))`.

</details>

<details><summary>Indice 2</summary>

Vérifie : pour 6 jours, (6 − 3) × 50 = 150 ; pour 40 jours, 37 × 50 = 1850, plafonné à 1000.

</details>

---

## Étape 6 — `RETOUR <membre> <isbn> <jours de retard>`

<details><summary>Indice 1</summary>

La fin de ligne : `fee.map(c -> "penalite " + money(c)).orElse("sans penalite")`.

</details>

<details><summary>Indice 2</summary>

Le suivant de la file : `Optional.ofNullable(waitlists.get(isbn)).map(Deque::pollFirst).flatMap(this::findMember).ifPresent(next -> { … })`.

</details>

---

## Étape 7 — `CONTACT <membre>`

<details><summary>Indice 1</summary>

Un seul appel qui traite les deux cas : une méthode d'`Optional` ajoutée en Java 9, qui prend une action **et** une action de secours.

</details>

<details><summary>Indice 2</summary>

`findMember(id).ifPresentOrElse(m -> …, () -> …)`.

</details>

---

## Étape 8 — `INFO <isbn ou titre>`

<details><summary>Indice 1</summary>

Le texte de recherche est tout ce qui suit `INFO ` : `command.substring("INFO ".length())`.

</details>

<details><summary>Indice 2</summary>

En attente : `Optional.ofNullable(waitlists.get(isbn)).map(Deque::size).orElse(0)`.

</details>

---

## Étape 9 — `BILAN` (4 lignes)

<details><summary>Indice 1</summary>

- Ligne 1 : `returns.stream().map(Return::feeCents).filter(c -> c > 0)`.
- Ligne 2 : `mapToInt(Return::days).max()` pour le nombre, `max(Comparator.comparingInt(Return::days))` pour le retour lui-même.

</details>

<details><summary>Indice 2</summary>

Ligne 3 : `members.values().stream().map(Member::email).flatMap(Optional::stream).toList()`.

</details>

---

## Étape 10 — Le `main` de `LoanDesk`

<details><summary>Indice 1</summary>

`switch (p[0])` avec un `case` par commande. Le `default` produit `REFUS : commande inconnue …`.

</details>

<details><summary>Indice 2</summary>

`Data.COMMANDS.forEach(desk::execute)` : une référence de méthode sur l'objet `desk`.

</details>
