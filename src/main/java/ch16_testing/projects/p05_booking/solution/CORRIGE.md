# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Booking.java`](Booking.java), [`BookingRepository.java`](BookingRepository.java), [`Notifier.java`](Notifier.java) et [`BookingService.java`](BookingService.java), les tests de référence (avec leurs doublures) dans [`BookingServiceTest.java`](BookingServiceTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**, sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — Le modèle et les points de branchement

**Le code :** `Booking.java` (avec `overlaps`), `BookingRepository.java`, `Notifier.java`.

**Question — la formule sur papier :**
- 10 h–11 h et 11 h–12 h : a = 10, b = 11, c = 11, d = 12. `a < d` (10 < 12) est vrai, mais `c < b` (11 < 11) est **faux** : pas de chevauchement. Bout à bout, c'est permis : la fin est **exclue**.
- 10 h–11 h et 10 h 45–11 h 15 : 10 < 11 h 15 vrai, 10 h 45 < 11 vrai : **chevauchement**.

---

## Étape 2 — L'horloge, et les règles

**Le code :** le constructeur et `book` dans `BookingService.java` ; la constante `NINE_AM` et les tests `invalidDurationsAreRejected`, `durationsFromFifteenToTwoHundredFortyByQuarters`, `startMustBeStrictlyInTheFuture` et `atMostThirtyDaysAhead`. Maintenant + 30 jours = le **1er avril 2026, 9 h 00** (mars a 31 jours).

**Question — `!start.isAfter(now)` :** la règle dit « **strictement** après maintenant ». `!start.isAfter(now)` refuse tout ce qui n'est pas strictement après, **y compris `start == now`**. `start.isBefore(now)` laisserait passer un début à 9 h 00 pile. C'est le mutant 4, tué par le test du début à 9 h 00.

---

## Étape 3 — Le faux et l'espion

**Le code :** les classes `InMemoryRepository` et `RecordingNotifier`, le `@BeforeEach setUp()`, et les tests `aValidBookingIsSavedAndConfirmed`, `idsComeFromTheRepository` et `overlappingBookingIsRejectedButBackToBackIsFine`. Le message de confirmation exact : `ana <- Reservation 1 : Atlas le 2026-03-02T10:00` (`LocalDateTime.toString()` n'affiche pas les secondes quand elles valent 0).

**Question — pourquoi un faux plutôt que H2 ?** Le faux est **instantané** (une `Map`), sans SQL, sans connexion, sans schéma. Il rend le test **centré sur une seule chose** : les règles du service. Si le test échoue, c'est le service, pas une requête SQL. Le vrai dépôt JDBC aurait **ses propres** tests, avec H2 : ce sont des **tests d'intégration**, plus lents, qu'on fait en plus, pas à la place.

---

## Étape 4 — Le bouchon qui échoue

**Le code :** la classe `BrokenRepository` et le test `nothingIsSentWhenSavingFails`.

**Question — le bug que lui seul attrape :** l'**ordre** entre l'enregistrement et la notification. Si `book` envoyait la confirmation **avant** `repo.save`, l'utilisateur recevrait « Reservation 1 » pour une réservation qui n'a jamais été enregistrée. Avec le faux qui marche, les deux ordres donnent le même résultat final : seul un dépôt qui **échoue** montre la différence. C'est le mutant 8.

---

## Étape 5 — Annuler

**Le code :** `cancel` dans `BookingService.java` et la classe `WhenCancelling` des tests.

**L'expérience** (vérifiée) : avec `Clock.offset(horloge, Duration.ofMinutes(1))`, le deuxième service croit qu'il est **9 h 01**. Annuler la réservation de 11 h 00 lance `IllegalStateException: trop tard pour annuler : 1` : il reste 1 h 59. `Clock.offset` permet de faire « passer le temps » dans un test, sans attendre et sans `Thread.sleep`.

---

## Étape 6 — Les réservations du jour

**Le code :** `todayFor` et le test `todayListsOnlyTodaysBookingsOfTheRoomInOrder` : `[eve, ana]` (10 h 00 avant 16 h 00).

---

## Étape 7 — Les mutants

Tous les mutants sont tués par les tests de référence. Le mutant 1 de la première version de `Check` (`minutes < 1` au lieu de `minutes < 15`) était **équivalent** : les durées de 1 à 14 sont de toute façon refusées par la règle « multiple de 15 ». Il a été remplacé par `minutes < 0`, qui laisse vraiment passer 0 (0 est un multiple de 15).

---

## Expériences de fin de projet

1. Avec `Clock.systemUTC()` (vérifié le 9 octobre 2026), seuls **5 tests sur 19** passent encore : les réservations de mars 2026 sont dans le passé, `book` lance `debut dans le passe : 2026-03-02T16:00`. Le même test était vert en février et rouge en octobre : c'est pour cela qu'on fige l'horloge.
2. Avec `LocalDateTime.now()` dans `book` (vérifié) : tes tests **et** ceux de référence échouent (`debut dans le passe`, pour la même raison), et la partie API affiche `[FAIL] API : interdit ici (…) : [LocalDateTime.now()]`.
