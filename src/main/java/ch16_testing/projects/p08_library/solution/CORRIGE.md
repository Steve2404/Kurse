# Projet 8 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`LibraryService.java`](LibraryService.java) et les petits types à côté ; les tests de référence dans [`LibraryServiceTest.java`](LibraryServiceTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), **JUnit 5.11.4** et **Mockito 5.14.2**, sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — L'amende, en tableau

**Le code :** `fine` et le groupe `Fine` des tests.

**Question — les limites :**
- le retard nul : **0** (gratuit) et **1** (20 centimes) ; un retard **négatif** (−5) ;
- le premium : **3** (encore offert) et **4** (20 centimes) ;
- le plafond : **50** (1000 pile) et **51** (toujours 1000) pour un adhérent normal ; **53** et **54** pour un premium (le plafond arrive 3 jours plus tard) ;
- un cas au milieu (10 jours : 200) pour vérifier le calcul lui-même.

---

## Étape 2 — Le décor

**Le code :** le `@BeforeEach setUp()` des tests, avec ses trois `lenient().when(…)`.

**Question — pourquoi pas `@InjectMocks` ?** `@InjectMocks` appelle le constructeur en lui passant **des simulacres**. L'horloge n'en est pas un : c'est une vraie `Clock` figée. Faute de simulacre de type `Clock`, Mockito passerait `null`, et le premier `LocalDate.now(clock)` lancerait une `NullPointerException`. Quand un objet a besoin d'une vraie valeur à côté des simulacres, on l'appelle soi-même.

---

## Étape 3 — Emprunter

**Le code :** `borrow` et le groupe `Borrow` des tests.

**Question — l'ordre des refus :** le test `overdueIsReportedBeforeTheLimit` : un adhérent avec **trois** prêts, dont un en retard. Il est refusé pour les deux raisons, mais le message doit être `retard en cours : M1`. Si le code vérifiait la limite d'abord, le message serait `limite atteinte : M1` (c'est le mutant 16, tué par ce test seul).

**Question — les dates :** le 15 mai 2026 + 21 jours = **le 5 juin 2026** ; + 28 jours = **le 12 juin 2026** (mai a 31 jours). Vérifié par les tests `aMemberBorrowsForTwentyOneDays` et `aPremiumMemberBorrowsForTwentyEightDays`.

---

## Étape 4 — Rendre

**Le code :** `giveBack` et le groupe `GiveBack` des tests.

**L'expérience :** avec les tests de référence, **chaque ligne** de `LibraryService` est exécutée par au moins un test : chaque refus et chaque branche ont leur test dédié. (Je ne l'ai pas mesuré dans IntelliJ, seulement vérifié en relisant le code.) Mais 100 % de lignes vertes ne prouve **pas** que les tests sont bons : la couverture dit qu'une ligne a été **exécutée**, pas que son résultat a été **vérifié**. Un test sans aucun `assert` (projet 1, étape 2) colore les lignes en vert tout en ne vérifiant rien. Les **mutants** mesurent ce que la couverture ne voit pas : un mutant survit sur une ligne verte quand aucun test ne regarde son résultat.

---

## Étape 5 — Les mutants

Les 16 mutants sont tués par les tests de référence (vérifié). Le mutant 1 de la première version (`charged < 0` au lieu de `charged <= 0`) était **équivalent** : quand `charged` vaut 0, le calcul donne 0 de toute façon. Il a été remplacé par `charged <= 1`, qui rend vraiment le 1er jour gratuit.
