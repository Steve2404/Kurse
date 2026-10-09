# Projet 7 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Relire comme un senior

<details><summary>Indice 1</summary>

Prends le cahier des charges point par point, et pour chacun, cherche la ligne de `Data.java` qui le réalise. Un point sans ligne (« jamais dans le futur ») est un défaut ; une ligne qui le réalise mal aussi. Puis repasse avec la liste de contrôle.

</details>

<details><summary>Indice 2</summary>

Ne te contente pas de ce que la démo affiche : elle ne montre que sept défauts. Suis le chemin d'un achat avec le code `DOUBLE` : `record` rend 100 points… et que compte `balance` ensuite ? Regarde aussi ce que fait `register` si on l'appelle deux fois avec le même identifiant.

</details>

---

## Étape 2 — L'égalité, l'argent, les paliers

<details><summary>Indice 1</summary>

Dans un record, tu peux redéfinir `toString` comme une méthode ordinaire ; `equals` et `hashCode` restent ceux du compilateur.

</details>

<details><summary>Indice 2</summary>

Pour masquer : `int at = email.indexOf('@')` ; si `at <= 0`, tout masquer ; sinon `email.charAt(0) + "***" + email.substring(at)`.

</details>

---

## Étape 3 — Le service

<details><summary>Indice 1</summary>

Un petit `record Batch(int points, LocalDate earnedOn)` privé : chaque achat ajoute un lot avec les points **gagnés** (bonus compris). `balance` additionne les lots dont `today.isBefore(earnedOn.plusYears(1))`.

</details>

<details><summary>Indice 2</summary>

`register` : `customers.putIfAbsent(id, customer)` rend l'ancien client s'il existait (alors refuser), puis crée les deux listes du client. Une méthode `requireKnown(id)` évite de répéter le contrôle du client inconnu.

</details>

---

## Étape 4 — L'import

<details><summary>Indice 1</summary>

`split(";", -1)` garde les champs vides : `"C1;10;2025-01-01;"` donne 4 champs, dont le dernier est vide (pas de code). Une méthode par champ difficile : `cents(texte, ligne)` et `date(texte, ligne)`, chacune avec son `try`/`catch` qui fabrique le message.

</details>

<details><summary>Indice 2</summary>

`longValueExact()` lance `ArithmeticException` s'il reste des décimales après `movePointRight(2)` (trois décimales au départ). Pour le lecteur espion : `class TrackingReader extends Reader`, qui délègue `read(char[], int, int)` à un `StringReader` et met un booléen à `true` dans `close()`.

</details>

---

## Étape 5 — Plusieurs fils

<details><summary>Indice 1</summary>

Le même schéma qu'au projet 4 : un pool de 8 fils, 8 `CompletableFuture.runAsync`, chacun attend `gate.await()` puis fait ses 500 achats ; `gate.countDown()` les libère ensemble ; `allOf(...).join()` attend la fin.

</details>

<details><summary>Indice 2</summary>

Ferme le pool dans un `finally` (`shutdownNow()`), et vérifie les nombres **après** le `join`.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Chaque mutant remet un défaut du collègue : relis la remarque correspondante de ta revue, et demande-toi quel test l'aurait montré **avec son code**.

</details>

<details><summary>Indice 2 — ce que change chaque mutant</summary>

| Mutant | Le défaut remis | Le test qui le tue |
|---|---|---|
| 1 | `hashCode` d'identité : `equals` sans `hashCode` | deux clients dans un `HashSet` |
| 2 | les montants convertis avec un `double` | `19.99`, `0.29`, ou `10.001` refusé |
| 3 | `==` sur le code promo | le code tapé ou lu dans un fichier |
| 4 | GOLD au-dessus de 1 000, pas à partir de | 1 000 points |
| 5 | SILVER au-dessus de 300 | 300 points |
| 6 | `Math.round` au lieu de l'euro entier | 9,99 € |
| 7 | `LocalDate.now()` sans horloge | l'expiration, avec l'horloge de 2025 |
| 8 | `plusDays(365)` | le 14 janvier 2025 |
| 9 | plus de refus des achats futurs | l'achat du 15 janvier 2025 |
| 10 | l'erreur avalée, ce qui a été lu est rendu | une ligne fausse |
| 11 | le flux jamais fermé | le lecteur espion |
| 12 | la liste interne rendue | l'historique non modifiable |
| 13 | les lots de points `static` | deux services indépendants |
| 14 | une `ArrayList` partagée entre fils | les 8 fils de 500 achats |
| 15 | l'adresse en clair dans `toString` | le `toString` masqué |
| 16 | une double inscription acceptée | la double inscription |

</details>
