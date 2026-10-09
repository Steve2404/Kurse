# Chapitre 16 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, la règle du crescendo, et **comment `Check` vérifie des tests**) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire et à tester**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé (avec une leçon par étape) ;
- `INDICES.md` : deux indices repliés par étape ;
- `Check.java` : le correcteur ;
- `Data.java` : des données, seulement dans p07 (le programme bogué à déboguer) ;
- `solution/` : à n'ouvrir qu'à la fin, avec son `CORRIGE.md`.

**Tout le code et tous les tests, c'est toi qui les écris.**

| ☐ | Projet | Notions | Ce que tu crées | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_vat` — la caisse et la TVA | `@Test`, `assertEquals`, `assertThrows`, valeurs limites, mutants | `VatCalculator`, `VatCalculatorTest` | l'arrondi au centime sans `double`, l'ordre attendu/obtenu |
| ☐ | `p02_cart` — le panier de l'épicerie | `@BeforeEach`, `@Nested`, `@DisplayName`, `assertAll`, invariants | `Cart`, `CartTest` | un refus ne doit rien changer ; seuils de livraison et de remise |
| ☐ | `p03_tax` — le guichet des impôts | `@ParameterizedTest`, `@CsvSource`, `@ValueSource`, `@MethodSource`, `@NullAndEmptySource`, classes d'équivalence | `TaxCalculator`, `PasswordPolicy` et leurs tests | barème marginal, quotient familial, une règle cassée par cas |
| ☐ | `p04_roman` — les chiffres romains | **TDD** (rouge, vert, refactoring), test aller-retour, `assertTimeout`, mutant équivalent | `RomanNumerals`, `RomanNumeralsTest` | ne jamais écrire de code sans test rouge ; refuser `IIII` |
| ☐ | `p05_booking` — les réservations de salles | injection de dépendances, faux, espion, bouchon, `Clock.fixed` | `Booking`, `BookingRepository`, `Notifier`, `BookingService`, `BookingServiceTest` | figer le temps ; tester ce qui se passe quand une dépendance casse |
| ☐ | `p06_payment` — le service de paiement | **Mockito** : `@Mock`, `when`, `verify`, `ArgumentCaptor`, `InOrder`, réponses successives | 8 petits types et `PaymentServiceTest` | l'ordre banque, dépôt, courriel ; une seule nouvelle tentative |
| ☐ | `p07_debug` — l'inventaire bogué | **le débogueur** : points d'arrêt, pas à pas, évaluation, conditions, exceptions ; un test par bug | `Inventory`, `InventoryTest` | six bugs, dont un qui en cache un autre |
| ☐ | `p08_library` — **capstone** médiathèque | tout le chapitre | 7 types et `LibraryServiceTest` | des règles métier à transformer seul en tests ; 16 mutants |
