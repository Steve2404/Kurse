# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`VatCalculator.java`](VatCalculator.java), et les tests de référence dans [`VatCalculatorTest.java`](VatCalculatorTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**, sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — Le code : la TVA en centimes

**Le code :** les méthodes `vat` et `gross` de `VatCalculator.java`.

**Question — `vat(1999, 20)` à la main :** 1999 × 20 = 39 980 ; + 50 = 40 030 ; ÷ 100 = **400** (division entière). Sans le `+ 50`, on obtiendrait 399 : 399,8 centimes tronqués. Vérifié : `400` et `399`.

**Question — sans test ?** Il faudrait écrire un `main` qui affiche des résultats, puis les vérifier **à l'œil**, à chaque modification, pour chaque cas. Personne ne le fait vraiment : on vérifie une fois, puis on oublie. Un test fait cette vérification **tout seul**, à chaque lancement, en une seconde.

---

## Étape 2 — Ton premier test

**Le code :** les trois premiers tests de `VatCalculatorTest.java`.

**Les expériences** (vérifiées) :
1. `assertEquals(399, VatCalculator.vat(1999, 20))` échoue avec : `expected: <399> but was: <400>`.
2. Avec les arguments inversés : `expected: <400> but was: <399>`. Le message dit que le code **devait** rendre 400 et a rendu 399 : on croit que **le code** est faux, alors que c'est le test. Avec le bon ordre, le message dit la vérité.
3. Le texte apparaît **devant** le message : `la TVA de 19,99 EUR ==> expected: <…> but was: <…>`.
4. Le test sans `assert` est **vert**. Il vérifie seulement que l'appel ne lance pas d'exception. Il serait encore vert avec un calcul complètement faux : il ne sert presque à rien.
5. Non : l'ordre de la fenêtre Run n'est pas celui du fichier. JUnit utilise un ordre **fixe mais volontairement imprévisible** (calculé à partir des noms des méthodes). Dans l'essai, `b_swapped` est passé avant `a_wrongExpected`, et `i_strings` avant `h_message`.

**Question — pourquoi des tests indépendants ?** Parce que l'ordre n'est pas garanti, et qu'on lance souvent **un seul** test (la flèche à côté d'une méthode). Un test qui compte sur ce qu'un autre a préparé devient rouge ou vert selon l'ordre, et personne ne comprend pourquoi. Chaque test prépare lui-même tout ce dont il a besoin (le *Arrange*).

---

## Étape 3 — Tester les exceptions

**Le code :** les tests `negativeAmountIsRejectedWithItsValueInTheMessage`, `rateAboveOneHundredIsRejected` et `negativeRateIsRejected`.

**Les expériences** (vérifiées) :
1. Avec le mauvais type : `Unexpected exception type thrown, expected: <java.lang.IllegalStateException> but was: <java.lang.IllegalArgumentException>`.
2. Avec un appel valide : `Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.`

**Question — pourquoi vérifier le message ?** Les deux contrôles de `vat` lancent le **même type**. Un test qui vérifie seulement le type passerait même si le **mauvais** contrôle se déclenchait. Le message prouve que **c'est la bonne règle** qui a refusé l'entrée. Il fait aussi partie de ce que voit l'utilisateur : c'est le mutant 9.

---

## Étape 4 — Les valeurs limites

**Le code :** les tests `zeroCentIsAllowed`, `zeroAndOneHundredPercentAreAllowed`, `halfACentRoundsUp` et `roundingGoesToTheNearestCent`. `gross(1000, 100)` vaut **2000** : 100 % de TVA double le prix.

**Question — les quatre taux :** **−1** et **101** (juste dehors : refusés), **0** et **100** (juste sur les limites : acceptés). Ils attrapent les quatre erreurs possibles : `<` ou `<=` en bas, `>` ou `>=` en haut.

**Question — sans le `+ 50` :** `vat(1999, 20)` rendrait **399** au lieu de 400. Le test de l'arrondi au plus proche le voit. `halfACentRoundsUp` aussi : `vat(25, 2)` rendrait 0 au lieu de 1. Vérifié : c'est le mutant 1, tué par ces tests.

---

## Étape 5 — Les catégories et l'affichage

**Le code :** les méthodes `rateFor` et `format`, et les cinq derniers tests.

**Les expériences** (vérifiées) :
1. `assertEquals(0.3, 0.1 + 0.2)` échoue : `expected: <0.3> but was: <0.30000000000000004>`.
2. Avec la tolérance `1e-9` : **vert**. Pour des `double`, on compare toujours avec une tolérance. Pour de l'argent, on évite les `double`.

**Question — le piège de `format(-5)` :** en Java, `-5 / 100` vaut **0** et `-5 % 100` vaut **−5** (le reste a le signe du dividende, chapitre 2). Sans traitement du signe, `String.format("%d,%02d EUR", …)` donne **`0,-5 EUR`**, et −150 donne `-1,-50 EUR` (vérifié). Le signe disparaît pour les montants de moins d'un euro, et se retrouve au mauvais endroit pour les autres. D'où : le signe à part, et le calcul sur `Math.abs(cents)`.

**Question — pourquoi `"  INTERMEDIAIRE "` ?** Parce que l'énoncé promet d'ignorer les espaces et la casse. Un test avec `"intermediaire"` seul passerait même si le code oubliait `strip()` et `toLowerCase`. C'est le mutant 7.

---

## Étape 6 — Les mutants

**Question — le mutant du message :** un test qui vérifie le message **exact**, avec la valeur : `assertEquals("montant negatif : -1", e.getMessage())`. Un test qui vérifie seulement le type, ou seulement que le message **commence par** « montant negatif », le laisse survivre.

---

## Expériences de fin de projet

1. Avec `>= 100` dans ton code, le test des taux 0 et 100 échoue. Il ne montre pas un `expected/but was` : il montre l'exception lancée par ton code, `IllegalArgumentException : taux invalide : 100` (vérifié avec `Check`). Une exception inattendue dans un test le fait échouer.
2. Sans le test des taux 0 et 100, **le mutant 2 survit** (vérifié : `mutant 2 : SURVIT`, puis `[FAIL] mutants : 8/9 tues`).
3. IntelliJ montre le test `@Disabled` comme **ignoré**, il n'est pas exécuté. `Check` compte **un test de moins**, et ce que ce test vérifiait n'est plus vérifié. Dans l'essai, en désactivant `zeroCentIsAllowed`, le compte passe à 14 tests et le mutant 3 survit.
