# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les ressources

<details><summary>Indice 1</summary>

`Channel` garde un `boolean open`. `close()` le met à `false`, note `ferme …`, puis lève l'exception si le nom commence par `~`. Note **avant** de lever, pour que la fermeture apparaisse dans le journal.

</details>

<details><summary>Indice 2</summary>

`Journal.close()` : `requested++`, puis `if (!closed) { closed = true; effective++; }`. Une redéfinition peut déclarer **moins** d'exceptions que la méthode d'origine, ici aucune.

</details>

---

## Étape 2 — Les travaux

<details><summary>Indice 1</summary>

Deux ressources séparées par `;` dans les parenthèses du `try`. Elles sont fermées dans l'ordre **inverse** de l'ouverture, **avant** le `catch`.

</details>

<details><summary>Indice 2</summary>

`describe` : `e.getSuppressed()` rend un tableau de `Throwable`. `Arrays.stream(…).map(Throwable::getMessage).toList()` donne la liste des messages.

</details>

---

## Étape 3 — Ressource existante et `Closeable`

<details><summary>Indice 1</summary>

`try (shared) { … }` (Java 9) : une variable **déjà déclarée**, effectively final, peut servir de ressource. Elle est fermée à la fin du bloc, mais la variable existe encore après.

</details>

<details><summary>Indice 2</summary>

`try (journal) { journal.close(); }` : une fermeture à la main, plus celle du `try`, soit 2 demandes.

</details>

---

## Étape 4 — Réessayer

<details><summary>Indice 1</summary>

Une boucle `for (number = 1; number <= max; number++)` avec un `try { return attempt.run(number); } catch (ServiceException e) { … }`. Un succès sort directement de la méthode.

</details>

<details><summary>Indice 2</summary>

Garde le dernier échec dans une variable `last`. Quand un nouvel échec arrive, l'ancien `last` passe dans la liste des précédents. À la fin, `previous.forEach(failure::addSuppressed)`.

</details>

---

## Étape 5 — Le disjoncteur

<details><summary>Indice 1</summary>

Trois champs : l'état, les échecs consécutifs et le délai restant. Le test de l'état OPEN se fait **en premier**, avant de regarder `outcome`.

</details>

<details><summary>Indice 2</summary>

Dans le `main`, deux `catch` : `CircuitOpenException` donne `refus`, et `ServiceException` donne `echec`. L'état s'affiche **après** l'appel, avec `breaker.state()`.

</details>
