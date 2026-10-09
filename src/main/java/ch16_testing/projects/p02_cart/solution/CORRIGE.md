# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Cart.java`](Cart.java), et les tests de référence dans [`CartTest.java`](CartTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**, sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — Le panier, et un panier neuf pour chaque test

**Le code :** `add` (sans ses contrôles), `quantity`, `isEmpty`, `lines`, `subtotalCents`, et le `@BeforeEach newCart()` des tests.

**Les expériences** (vérifiées) :
1. Avec un champ `int compteur` d'instance, les **deux** tests passent : chaque test reçoit un objet `CartTest` **neuf**, donc un compteur qui repart de 0.
2. Avec un champ `static`, le compteur est **partagé** par tous les objets : le 2e test lancé échoue avec `expected: <1> but was: <2>`. Lequel des deux échoue dépend de l'ordre de lancement, qui n'est pas garanti. C'est exactement le genre de test qui « marche chez moi » et casse ailleurs.

**Question — ranger les lignes :** une **`TreeMap`** (chapitre 9) : ses clés restent triées dans l'ordre naturel, quel que soit l'ordre des `put`. Avec une `HashMap`, l'ordre serait imprévisible ; avec une `LinkedHashMap`, ce serait l'ordre d'ajout (c'est le mutant 6).

---

## Étape 2 — Vérifier plusieurs choses à la fois

**Le code :** les tests `hasNothing`, `linesAreSortedBySku` et `totals`. Le sous-total vaut 2 × 120 + 3 × 50 + 1 × 99 = **489**.

**L'expérience** (vérifiée) :
- à la suite, seul le 1er échec est signalé : `expected: <1> but was: <2>` ; le 2e `assertEquals` n'est jamais exécuté ;
- dans un `assertAll`, une `MultipleFailuresError` signale **les deux** :

```
Multiple Failures (2 failures)
	org.opentest4j.AssertionFailedError: expected: <1> but was: <2>
	org.opentest4j.AssertionFailedError: expected: <a> but was: <b>
```

**Question — pourquoi dans le désordre ?** Si on ajoutait `APPLE`, `MILK`, `PEAR` dans l'ordre alphabétique, une `LinkedHashMap` (ordre d'ajout) donnerait **aussi** le bon résultat : le test ne prouverait pas que `lines()` **trie**. En ajoutant dans le désordre, seule une version qui trie vraiment passe.

---

## Étape 3 — Ranger les tests par situation

**Le code :** les classes `WhenEmpty` et `WithItems` de `CartTest.java`.

**L'expérience** (vérifiée, avec deux tests en haut et un test dans un groupe `@Nested`) : l'ordre des messages est

```
BeforeAll
BeforeEach, (test), AfterEach            ← pour chaque test de la classe extérieure
BeforeEach, Inner.BeforeEach, (test du groupe), AfterEach
AfterAll
```

`@BeforeAll` une seule fois **au tout début**, `@AfterAll` une seule fois **à la toute fin**. Pour un test d'un groupe `@Nested`, le `@BeforeEach` extérieur passe **avant** celui du groupe, et le `@AfterEach` extérieur après le test.

**Question — pourquoi `static` ?** `@BeforeAll` s'exécute **une fois**, avant **tous** les tests, donc avant qu'aucun objet de test n'existe (JUnit en crée un par test). Il n'y a pas d'objet sur lequel l'appeler : la méthode doit appartenir à la **classe**. (On peut changer cela avec `@TestInstance(Lifecycle.PER_CLASS)`, qui fait partager un seul objet à tous les tests ; c'est rarement une bonne idée.)

---

## Étape 4 — Refuser, sans rien abîmer

**Le code :** les contrôles de `add`, la méthode `remove`, `List.copyOf` dans `lines()`, et la classe `InvalidAdds` des tests.

**Question — 98 + 2 pommes :** le panier doit toujours contenir **98** pommes. L'ajout est refusé **en entier** : on n'ajoute pas « ce qui rentre ». Si ton code faisait le `put` avant le contrôle, il en contiendrait 100 (c'est l'expérience 1 de fin de projet).

**Question — la liste non modifiable :** si `lines()` rendait une liste modifiable, ou pire la structure interne, n'importe quel appelant pourrait ajouter une ligne `FRAUDE x 1 = 0` **sans passer par `add`**, donc sans aucun contrôle. Une liste non modifiable protège les invariants : seul `Cart` décide de ce que contient le panier. C'est le mutant 7.

---

## Étape 5 — Les codes promo et les frais de port

**Le code :** `applyCode`, `discountCents`, `shippingCents`, `totalCents`, et la classe `Thresholds` des tests. Pour un article à 5005 : (5005 × 10 + 50) / 100 = 50 100 / 100 = **501** (tronqué, ce serait 500).

**Question — le code appliqué avant les articles :** oui, la remise s'applique : `discountCents()` ne mémorise pas un montant, elle **recalcule** à chaque appel à partir du code et du panier **actuel**. Le test `discountFromExactlyFiftyEuros` applique justement le code d'abord, puis ajoute les articles.

---

## Étape 6 — Les mutants

**Question — le mutant qui remplace la quantité :** un test qui ajoute **deux fois** le même article et vérifie la somme (`addingTheSameItemAccumulates` : 3 + 2 = 5). Le test du maximum le tue aussi : avec le mutant, 99 puis 1 donne une quantité de 1, donc aucune exception.

---

## Expériences de fin de projet

1. Avec le contrôle du maximum placé **après** la modification, le test du maximum échoue (vérifié avec `Check`) : l'exception est bien lancée, mais la quantité vaut 100. `Check` affiche `Multiple Failures (1 failure) ; 1er echec : expected: <99> but was: <100>`. Le panier a été abîmé par un ajout refusé.
2. Oui : lancer un seul groupe `@Nested` exécute quand même le `@BeforeEach` de la classe extérieure, avant celui du groupe. Sans lui, le champ `cart` vaudrait `null`.
