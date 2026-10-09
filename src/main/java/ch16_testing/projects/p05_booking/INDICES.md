# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le modèle et les points de branchement

<details><summary>Indice 1</summary>

Un `record` peut avoir des méthodes en plus de ses composants (chapitre 7). Dans `overlaps`, `start` et `end` sont les composants de **cette** réservation.

</details>

<details><summary>Indice 2</summary>

`start.isBefore(otherEnd) && otherStart.isBefore(end)` : c'est la formule « a < d et c < b » avec les méthodes de `LocalDateTime`.

</details>

---

## Étape 2 — L'horloge, et les règles

<details><summary>Indice 1</summary>

Le service garde ses trois dépendances dans des champs `private final`. Chaque méthode qui a besoin de « maintenant » appelle `LocalDateTime.now(clock)`.

</details>

<details><summary>Indice 2</summary>

Pour le chevauchement : `repo.forRoom(room).stream().anyMatch(b -> b.overlaps(start, end))`, avec `end = start.plusMinutes(minutes)`.

</details>

---

## Étape 3 — Le faux et l'espion

<details><summary>Indice 1</summary>

Les doublures sont des classes `static` **dans** `BookingServiceTest`. Leurs champs peuvent rester accessibles au paquet (sans `private`) : le test lit directement la liste de l'espion.

</details>

<details><summary>Indice 2</summary>

Pour `forRoom`, le faux filtre ses valeurs : `bookings.values().stream().filter(b -> b.room().equals(room)).toList()`. Une `TreeMap` garde les ids dans l'ordre.

</details>

---

## Étape 4 — Le bouchon qui échoue

<details><summary>Indice 1</summary>

`static final class BrokenRepository extends InMemoryRepository` et une seule méthode redéfinie : `save`. Si le compilateur dit `cannot inherit from final`, retire `final` de ton faux.

</details>

<details><summary>Indice 2</summary>

Dans le test, crée un **autre** service avec le bouchon, mais le **même** espion : c'est l'espion que tu vérifies après l'exception.

</details>

---

## Étape 5 — Annuler

<details><summary>Indice 1</summary>

`repo.find(id).orElseThrow(() -> new NoSuchElementException("reservation inconnue : " + id))` donne la réservation, ou lance l'exception.

</details>

<details><summary>Indice 2</summary>

Dans le `@BeforeEach` du groupe, la réservation de 11 h 00 envoie un message à l'espion : `notifier.sent.clear()` juste après, pour que chaque test ne voie que ce que **lui** déclenche.

</details>

---

## Étape 6 — Les réservations du jour

<details><summary>Indice 1</summary>

`b.start().toLocalDate()` donne le jour d'une réservation ; compare-le à `LocalDate.now(clock)` avec `equals`.

</details>

<details><summary>Indice 2</summary>

`.sorted(Comparator.comparing(Booking::start))` puis `.toList()`. Dans le test, compare seulement les utilisateurs : `map(Booking::user)`.

</details>

---

## Étape 7 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quelle règle, quelle limite, quel message ou quel **effet** (enregistrement, suppression, notification) n'est vérifié par aucun test ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Une durée de 0 minute est acceptée.
2. Une durée de 255 minutes est acceptée.
3. Une durée qui n'est pas un multiple de 15 est acceptée.
4. Un début exactement à « maintenant » est accepté.
5. On peut réserver 31 jours à l'avance.
6. Deux réservations bout à bout sont considérées comme chevauchantes.
7. Le chevauchement est cherché dans la mauvaise salle (donc jamais trouvé).
8. La confirmation est envoyée **avant** l'enregistrement.
9. N'importe qui peut annuler la réservation d'un autre.
10. On peut annuler jusqu'à 1 h avant au lieu de 2 h.
11. L'annulation n'envoie plus de message.
12. `todayFor` rend aussi les réservations des autres jours.
13. `todayFor` ne trie plus par heure.
14. Toutes les réservations reçoivent l'id 1.
15. L'annulation ne supprime pas la réservation du dépôt.

</details>
