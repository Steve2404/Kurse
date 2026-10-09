# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`TaxCalculator.java`](TaxCalculator.java) et [`PasswordPolicy.java`](PasswordPolicy.java), les tests de référence dans [`TaxCalculatorTest.java`](TaxCalculatorTest.java) et [`PasswordPolicyTest.java`](PasswordPolicyTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**, sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — Le barème par tranches

**Le code :** `tax`, `marginalRate` et `checkIncome` dans `TaxCalculator.java`.

**Question — les calculs à la main :**
- `tax(60_000)` : 15 000 × 10 = 150 000 centièmes, plus 35 000 × 25 = 875 000 ; total 1 025 000 centièmes ; (1 025 000 + 50) / 100 = **10 250** ;
- `tax(10_004)` : 4 × 10 = 40 centièmes, soit 0,40 € ; (40 + 50) / 100 = **0** ;
- `tax(10_005)` : 5 × 10 = 50 centièmes, soit 0,50 € ; (50 + 50) / 100 = **1**.

Ils diffèrent parce que **0,50 € est la limite de l'arrondi** : 0,40 € descend à 0, 0,50 € monte à 1. Vérifié par le test `taxFollowsTheBrackets`.

---

## Étape 2 — Un tableau de cas

**Le code :** `taxFollowsTheBrackets` et `marginalRateChangesJustAfterEachLimit`.

**Les expériences** (vérifiées) :
1. La ligne `"abc, 0"` : `ParameterResolutionException : Error converting parameter at index 0: Failed to convert String "abc" to type long`. Les autres cas s'exécutent quand même.
2. La ligne à une seule colonne : `ParameterResolutionException : No ParameterResolver registered for parameter [long arg1] in method […]`. JUnit n'a rien à mettre dans le 2e paramètre.
3. Sans `name`, chaque cas s'appelle `[numéro] valeurs`, par exemple `[1] 10000, 0`, `[2] abc, 0`. C'est lisible, mais un nom qui dit **ce qui est vérifié** est meilleur.

**Question — 25 000 et 25 001, mais pas 40 000 ?** 25 000 et 25 001 sont **de part et d'autre d'une limite** : c'est là qu'un `<` à la place d'un `<=` change le résultat. 40 000 est **au milieu** d'une tranche : il se comporte comme 30 000 ou 50 000, et passerait avec presque tous les bugs de limite. Il appartient à la même **classe d'équivalence** que 25 001.

---

## Étape 3 — Une colonne, et une propriété

**Le code :** `negativeIncomeIsRejected` et `oneMoreEuroNeverCostsMoreThanOneEuro`.

**Question — ce qu'attrape la propriété :** un barème **non marginal**, qui taxerait **tout** le revenu au taux de la tranche atteinte. Avec lui, passer de 25 000 à 25 001 € ferait sauter l'impôt de 2500 à 6250 € : 3750 € pour un euro gagné. Un tableau de valeurs exactes ne le voit que s'il contient justement les bons revenus ; la propriété le voit sur **chaque** limite. C'est le mutant 10.

---

## Étape 4 — Des cas complexes

**Le code :** `taxWithShares`, le test `taxWithSharesSplitsTheIncome`, sa méthode `households()`, et `householdIsValidated`.

**L'expérience** (vérifiée) : sans `static`, tout le test est en échec, avant même le 1er cas : `PreconditionViolationException : Method '…' must be static: local factory methods must be static unless the PER_CLASS @TestInstance lifecycle mode is used`. (Depuis la correction de `Check`, ce genre de groupe en panne compte comme un échec.)

**Question — 2 adultes, 2 enfants :** 6 demi-parts, soit 3 parts. Le revenu d'une part vaut `100 000 / 6` = **16 666** (division entière : les 0,66 € disparaissent). `tax(16_666)` = 6666 × 10 = 66 660 centièmes, arrondis à **667** €. Puis 667 × 6 / 2 = **2001** €. Ce n'est pas un multiple « rond » car chaque part est **arrondie séparément**, puis multipliée par 3. Vérifié : `taxWithShares(50_000, 2, 2)` rend 2001. (En écrivant ce projet, le test de référence attendait d'abord 2000, calculé trop vite : il a échoué, et c'est le **test** qui avait tort. Calculer chaque cas à la main, c'est ce que l'énoncé te demande.)

---

## Étape 5 — Les mots de passe

**Le code :** `PasswordPolicy.java` et `PasswordPolicyTest.java`.

**Question — `"Abcde fghij1"` :** **non**, il ne casse pas « sans symbole ». La règle 7 vérifie que **tous** les caractères sont des lettres ou des chiffres ; l'espace n'est ni l'un ni l'autre, donc la règle 7 est satisfaite. Seule la règle 8 (« espace interdit ») est cassée. Vérifié : `violations("Abcde fghij1")` rend `[espace interdit]`. C'est pour cela que le cas du test porte le commentaire « l'espace compte comme symbole ».

---

## Étape 6 — Les mutants

**Question — la limite à 50 000 :** le cas **60 000** le voit : avec le mutant, la tranche de 50 001 à 60 000 est taxée à 40 % au lieu de 25 %, et `tax(60_000)` passe de 10 250 à 11 750. Le cas 100 000 aussi. Le test de propriété ne le voit pas : même à 40 %, un euro de plus ne coûte que 0,40 €, donc la propriété reste vraie. Une propriété vérifie une **forme** du résultat, pas sa valeur exacte : il faut les deux genres de tests.

---

## Expériences de fin de projet

1. Avec `@Test` et `@ParameterizedTest` ensemble (vérifié), JUnit affiche un avertissement `Possible configuration error: method […] resulted in multiple TestDescriptors`, puis lance la méthode **deux fois** : en test paramétré (les cas passent), et en test simple, qui échoue avec `No ParameterResolver registered for parameter [int arg0]`.
2. `@NullAndEmptySource` sur un `int` (vérifié) : le cas `null` échoue avec `Cannot convert null to primitive value of type int`, et le cas vide est refusé avec `@EmptySource cannot provide an empty argument to method […]: [int] is not a supported type.`
