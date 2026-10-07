# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Modéliser une ligne

<details><summary>Indice 1</summary>

Des records : `Stop(name, offset)`, `Line(id, first, last, frequency, stops)`, et plus tard `Trip(line, from, departure, to, arrival)`. Le cumul se calcule **une fois** au chargement, avec une boucle.

</details>

<details><summary>Indice 2</summary>

`Stream.iterate(first, t -> !t.isAfter(last), t -> t.plusMinutes(frequency))` : la version à 3 arguments (Java 9) s'arrête d'elle-même.

</details>

---

## Étape 2 — `ARRETS`, `PAGE <n> <taille>`

<details><summary>Indice 1</summary>

`lines.values().stream().flatMap(l -> l.stops().stream()).map(Stop::name).distinct().sorted()`. Mets cette chaîne dans une méthode qui rend le `Stream`, pour la réutiliser.

</details>

<details><summary>Indice 2</summary>

Page n de taille t : `skip((n - 1) * t).limit(t)`. Si le `joining` rend `""`, la page est vide.

</details>

---

## Étape 3 — `LIGNES`

<details><summary>Indice 1</summary>

`Comparator.comparing((Line l) -> l.stops().size()).reversed()` inverse **seulement** ce premier critère, car il est appliqué tout de suite. Les `thenComparing…` viennent **après**.

</details>

<details><summary>Indice 2</summary>

Le type `(Line l)` est écrit dans la lambda : sans lui, `reversed()` ne saurait pas quel est le type des éléments.

</details>

---

## Étape 4 — `CIRCUIT <ligne> <ligne>`

<details><summary>Indice 1</summary>

`Stream.ofNullable(lines.get(id))` (Java 9) : un stream de 0 élément si l'id est inconnu, sinon d'un élément.

</details>

<details><summary>Indice 2</summary>

`Stream.concat(…, …).flatMap(l -> l.stops().stream()).map(Stop::name).distinct()`. `distinct` garde la **première** occurrence et l'ordre.

</details>

---

## Étape 5 — `PROCHAIN <ligne> <arrêt> <heure>` : la paresse prouvée

<details><summary>Indice 1</summary>

`List<LocalTime> generated = new ArrayList<>();`, puis `departures().peek(generated::add).map(d -> d.plusMinutes(offset)).filter(…).findFirst()`. La taille de la liste donne le nombre calculé.

</details>

<details><summary>Indice 2</summary>

Calcule : à quel départ (06:00 + k × 20 min) le passage au Musée (+10) atteint-il 07:52 ? Compte les départs générés jusqu'à lui, inclus.

</details>

---

## Étape 6 — `HORAIRES <ligne> <arrêt> <de> <à>`

<details><summary>Indice 1</summary>

`dropWhile(a -> a.isBefore(from))` jette le préfixe. `takeWhile(a -> !a.isAfter(to))` garde jusqu'au premier qui dépasse.

</details>

<details><summary>Indice 2</summary>

Les deux s'arrêtent au **premier** élément qui change la condition. Ils ne regardent pas la suite.

</details>

---

## Étape 7 — `DIRECT <départ> <arrivée> <heure>`

<details><summary>Indice 1</summary>

`goesFromTo(from, to)` : les deux arrêts existent **et** l'offset de `from` est plus petit que celui de `to`.

</details>

<details><summary>Indice 2</summary>

`.filter(l -> l.goesFromTo(from, to)).map(l -> l.nextTrip(…)).flatMap(Optional::stream).min(Comparator.comparing(Trip::arrival))`.

</details>

---

## Étape 8 — `CORRESPONDANCE <départ> <arrivée> <heure>` : l'algorithme

<details><summary>Indice 1</summary>

Trois niveaux de `flatMap` : les lignes A, puis les arrêts S de A après le départ (chacun donne un trajet ou rien), puis les lignes B ≠ A de S vers l'arrivée. On finit par un record `Connection(leg1, leg2)`.

</details>

<details><summary>Indice 2</summary>

`min(Comparator.comparing((Connection c) -> c.second().arrival()).thenComparing(c -> c.first().departure(), Comparator.reverseOrder()))`.

</details>

---

## Étape 9 — `ACCESSIBLE <ligne>`

<details><summary>Indice 1</summary>

`noneMatch(Data.NOT_ACCESSIBLE::contains)` sur les noms des arrêts.

</details>

<details><summary>Indice 2</summary>

Les coupables : la même source, avec `filter(Data.NOT_ACCESSIBLE::contains)`, puis `joining(", ")`.

</details>

---

## Étape 10 — `TICKETS <n>`

<details><summary>Indice 1</summary>

Le compteur est un **champ** de l'objet réseau : une lambda peut modifier un champ, et sa valeur survit entre deux appels.

</details>

<details><summary>Indice 2</summary>

`Stream.generate(() -> String.format("T%03d", ++ticketCounter)).limit(n)`.

</details>

---

## Étape 11 — `RESEAU`

<details><summary>Indice 1</summary>

`allMatch(l -> l.stops().size() >= 4)`, `anyMatch(l -> l.stop("Phare").isPresent())`, `noneMatch(l -> l.first().isBefore(LocalTime.of(5, 0)))`.

</details>

<details><summary>Indice 2</summary>

Pour la question : sur un stream vide, `allMatch` répond à « y a-t-il un contre-exemple ? », et `anyMatch` à « y a-t-il un exemple ? ».

</details>

---

## Étape 12 — `main`

<details><summary>Indice 1</summary>

Un `switch` en flèche. PROCHAIN et HORAIRES passent par la méthode « avec ligne et arrêt ».

</details>

<details><summary>Indice 2</summary>

`Data.COMMANDS.forEach(network::execute)`.

</details>
