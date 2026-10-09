# Projet 1 — La caisse qui calcule la TVA (ton premier test JUnit)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 16) :**
- **pourquoi tester** : un programme qui « a l'air de marcher » n'est pas un programme qui marche ;
- **JUnit 5** :
  - une classe de test, des méthodes `@Test` ;
  - `assertEquals(attendu, obtenu)`, l'ordre des deux arguments ;
  - `assertThrows(Type.class, () -> …)`, qui rend l'exception pour vérifier son message ;
- **lancer des tests dans IntelliJ**, lire une barre verte ou rouge, lire un message d'échec ;
- **le schéma Arrange / Act / Assert** et des **noms de tests** qui disent ce qu'ils vérifient ;
- **les valeurs limites** : tester 0 et 100, pas 50 ;
- **les mutants** : un bon test **échoue** quand le code a un bug.

Côté algorithmes : un arrondi au centime le plus proche sans nombre à virgule, l'affichage d'un montant négatif.

**Ce que TU crées :** dans `ch16_testing.projects.p01_vat` :
- **`VatCalculator`**, le code (ses méthodes sont imposées : `Check` les appelle) ;
- **`VatCalculatorTest`**, tes tests (le nom d'une classe de test finit **toujours** par `Test`).

**Règle du crescendo :** chapitres 1 à 15, plus JUnit (ce chapitre). Deux interdits, vérifiés par `Check` :
- pas de `double` ni de `float` dans `VatCalculator` : l'argent se compte en **centimes**, dans des `long` ;
- pas de `System.out` ni de `Thread.sleep` dans tes tests : un test **vérifie**, il n'affiche pas.

**Ce que le chapitre 16 t'apprend :** jusqu'ici, `Check` vérifiait ton travail. À partir de maintenant, **c'est toi qui écris les vérifications**. Un développeur senior n'a jamais de `Check` : il a ses tests. Ce chapitre t'apprend à en écrire de bons, puis à trouver un bug avec le **débogueur**.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : une balance de cuisine.

> **🧰 Tes outils pour ce projet**
>
> - **Lancer tes tests :** ouvre `VatCalculatorTest`. Dans la marge de gauche, une **flèche verte** apparaît à côté de `class VatCalculatorTest` (tous les tests) et à côté de chaque `@Test` (un seul test). Clique dessus → **Run**. Raccourci : place le curseur dans la classe et tape **Ctrl+Maj+F10**.
> - **Lire le résultat :** la fenêtre **Run** s'ouvre en bas. À gauche, la liste des tests : ✔ vert = réussi, ✘ jaune ou rouge = échoué. Clique sur un test échoué : à droite, le message `expected: <…> but was: <…>`, et un lien bleu vers la ligne du test.
> - **Lancer `Check` :** flèche verte à côté de `Check.main`, comme d'habitude. Pas d'argument.
> - **Le terminal :** pas besoin dans ce chapitre. JUnit est une bibliothèque déclarée dans `pom.xml` : IntelliJ la télécharge et l'ajoute toute seule. (Si IntelliJ souligne `org.junit` en rouge : clique sur l'icône **Maven** à droite → bouton **Reload All Maven Projects**, les deux flèches en rond.)

---

## Tableau de bord

### ☐ Étape 1 — Le code : la TVA en centimes

**📖 La leçon : pourquoi jamais de `double` pour de l'argent.** Un `double` range les nombres en binaire. Beaucoup de nombres décimaux n'y tombent pas juste : `0.1 + 0.2` vaut `0.30000000000000004`. Sur une balance de cuisine qui pèse au gramme, on ne dit pas « 1,25 kg », on dit **1250 grammes** : un entier, toujours exact. Pour l'argent, c'est pareil : on compte en **centimes**, dans un `long`.

Il reste à **arrondir**. Pour calculer 2,5 % de 1250 grammes au gramme le plus proche :

```java
long part = (1250 * 25 + 500) / 1000;   // 31,25 g -> 31 ; le "+ 500" fait monter les demis
```

La division entière **tronque** (chapitre 2) : `31250 / 1000` donne 31. Ajouter **la moitié du diviseur** avant de diviser fait arrondir au plus proche : un reste de 0,5 ou plus fait monter d'une unité.

**👉 À toi :** crée la classe `final class VatCalculator` (avec un constructeur `private`, comme une classe utilitaire : personne ne fait `new VatCalculator()`).
- **`public static long vat(long netCents, int ratePercent)`** : la TVA d'un montant hors taxe, arrondie au centime le plus proche. La formule : `(netCents * ratePercent + 50) / 100`.
  - si `netCents < 0` : `throw new IllegalArgumentException("montant negatif : " + netCents)` ;
  - si `ratePercent < 0` ou `ratePercent > 100` : `throw new IllegalArgumentException("taux invalide : " + ratePercent)`.
- **`public static long gross(long netCents, int ratePercent)`** : le montant toutes taxes, `netCents + vat(netCents, ratePercent)`.

**❓ Questions :**
- Combien vaut `vat(1999, 20)` ? Calcule-le **à la main**, sur papier, avant d'écrire le moindre test.
- Comment saurais-tu, **sans test**, que ton code est juste ?

### ☐ Étape 2 — Ton premier test

```
(fenêtre Run d'IntelliJ)
✔ VatCalculatorTest
   ✔ vatOfTenEurosAtTwentyPercentIsTwoEuros()
   ✔ grossAddsTheVatToTheNetAmount()
```

**📖 La leçon : un test JUnit.** Un test est une petite méthode qui **appelle ton code** et **vérifie le résultat**. Tu l'écris une fois ; ensuite, il vérifie pour toi, à chaque lancement, en une seconde.

```java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceTest {

    @Test                                       // "cette méthode est un test"
    void deuxCentCinquanteGrammesPlusCentFontTroisCentCinquante() {
        Balance b = new Balance();              // Arrange : je prépare
        b.ajoute(250);
        b.ajoute(100);
        long poids = b.poids();                 // Act : j'agis
        assertEquals(350, poids);               // Assert : je vérifie (ATTENDU d'abord, OBTENU ensuite)
    }
}
```

- **`@Test`** marque une méthode de test : pas de `main`, JUnit trouve et lance tout seul chaque méthode marquée. La classe et les méthodes n'ont pas besoin d'être `public`.
- **`assertEquals(attendu, obtenu)`** : si les deux diffèrent, le test **échoue**, avec le message `expected: <350> but was: <340>`. L'ordre compte : la **valeur attendue** d'abord.
- **`import static`** (chapitre 5) permet d'écrire `assertEquals` au lieu de `Assertions.assertEquals`.
- **Le nom** du test est une phrase qui dit **ce qui est vérifié**. Quand il échoue dans six mois, son nom seul doit te dire ce qui est cassé.
- **Arrange / Act / Assert** : trois paragraphes, toujours dans cet ordre.

**👉 À toi :** crée `VatCalculatorTest` dans le même paquet (clic droit sur le dossier `p01_vat` → **New** → **Java Class**). Écris au moins **trois** tests :
- la TVA de 1000 centimes à 20 % ;
- le montant toutes taxes de 1000 centimes à 20 % ;
- un test qui relie les deux : `gross(1999, 20) - 1999` égale `vat(1999, 20)`.

Lance-les (flèche verte à côté de la classe). Tout doit être vert.

**🧪 Expériences** (remets ensuite tes tests comme avant) :
1. Écris volontairement une **mauvaise** valeur attendue : `assertEquals(399, VatCalculator.vat(1999, 20))`. Lance. Recopie le message d'échec.
2. **Inverse** les deux arguments : `assertEquals(VatCalculator.vat(1999, 20), 399)`. Lis le message : que dit-il maintenant ? Pourquoi est-ce trompeur ?
3. Ajoute un 3e argument, un texte : `assertEquals(399, VatCalculator.vat(1999, 20), "la TVA de 19,99 EUR")`. Où apparaît le texte ?
4. Écris un test **sans aucun `assert`**, qui appelle seulement `VatCalculator.vat(1999, 20)`. Est-il vert ou rouge ? Que vérifie-t-il ?
5. Lance tous tes tests trois fois et regarde l'**ordre** dans la fenêtre Run. Est-ce l'ordre du fichier ?

**❓ Question :** pourquoi un test ne doit-il **jamais dépendre** d'un autre test ?

### ☐ Étape 3 — Tester les exceptions

```
✔ negativeAmountIsRejectedWithItsValueInTheMessage()
✔ rateAboveOneHundredIsRejected()
```

**📖 La leçon : `assertThrows`.** Un bon code **refuse** les entrées absurdes. Il faut aussi le tester :

```java
@Test
void unPoidsNegatifEstRefuse() {
    Balance b = new Balance();
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> b.ajoute(-5));
    assertEquals("poids negatif : -5", e.getMessage());
}
```

- Le 2e argument est une **lambda** (chapitre 8) : JUnit l'exécute **lui-même**, et attrape l'exception.
- Si l'exception attendue arrive, `assertThrows` la **rend** : tu peux vérifier son message.
- Si **rien** n'est lancé, ou une exception d'un **autre type**, le test échoue.

**👉 À toi :** des tests pour :
- un montant négatif (`-1`) : le type **et** le message exact `montant negatif : -1` ;
- un taux de 101 : le type et le message `taux invalide : 101` ;
- un taux de −1, appelé par `gross` (le type suffit).

**🧪 Expériences :**
1. Remplace `IllegalArgumentException.class` par `IllegalStateException.class` dans un de tes tests. Recopie le message.
2. Appelle `assertThrows(IllegalArgumentException.class, () -> VatCalculator.vat(1, 20))` (un appel **valide**). Recopie le message.

**❓ Question :** pourquoi vérifier le **message**, et pas seulement le type de l'exception ?

### ☐ Étape 4 — Les valeurs limites

```
✔ zeroCentIsAllowed()
✔ zeroAndOneHundredPercentAreAllowed()
✔ halfACentRoundsUp()
✔ roundingGoesToTheNearestCent()
```

**📖 La leçon : où se cachent les bugs.** Les bugs aiment les **frontières**. Une balance qui accepte de 0 à 5000 grammes a deux frontières : 0 et 5000. Le développeur a pu écrire `poids < 0` ou `poids <= 0`, `poids > 5000` ou `poids >= 5000`. Pour attraper ces erreurs d'un caractère, on teste :
- **juste sur** chaque frontière (0 et 5000 : acceptés) ;
- **juste à côté**, dehors (−1 et 5001 : refusés).

Tester 2500 ne sert presque à rien : il marche avec les quatre versions. C'est l'**analyse des valeurs limites**. Pour un arrondi, la frontière est le **demi** : 0,49 descend, 0,50 monte.

**👉 À toi :** des tests pour :
- 0 centime : accepté (`gross(0, 20)` vaut 0) ;
- les taux 0 et 100 : acceptés (calcule à la main ce que doit rendre `gross(1000, 100)`) ;
- le demi-centime : `vat(25, 2)` (0,50 centime) et `vat(24, 2)` (0,48 centime) ;
- l'arrondi au plus proche : `vat(1999, 20)` (399,8 centimes).

**❓ Questions :**
- Quelles **quatre** valeurs de taux faut-il tester pour la règle « de 0 à 100 » ?
- Que rendrait `vat(1999, 20)` si on oubliait le `+ 50` ? Quel test le verrait ?

### ☐ Étape 5 — Les catégories et l'affichage

```
✔ eachCategoryHasItsRate()
✔ categoryIgnoresCaseAndSurroundingSpaces()
✔ unknownAndMissingCategoriesAreRejected()
✔ formatShowsEurosAndTwoDigitsOfCents()
✔ formatKeepsTheSignOfANegativeAmount()
```

**📖 La leçon : `Locale.ROOT` et `requireNonNull`.**
- `"TITRE".toLowerCase()` utilise la **langue de la machine**. En turc, la majuscule `I` devient un `ı` sans point : un programme qui compare des mots-clés casse sur un ordinateur turc. Pour un mot-clé (pas un texte affiché), écris `toLowerCase(Locale.ROOT)` : le résultat est le même partout.
- `Objects.requireNonNull(x, "message")` lance tout de suite une `NullPointerException` **avec ce message** si `x` est `null`. L'erreur arrive au bon endroit, avec une phrase claire.

```java
String unite = Objects.requireNonNull(saisie, "unite absente").strip().toLowerCase(Locale.ROOT);   // " KG " -> "kg"
```

**👉 À toi :**
- **`public static int rateFor(String category)`** :
  - `Objects.requireNonNull(category, "categorie absente")` ;
  - puis un `switch` sur `category.strip().toLowerCase(Locale.ROOT)` : `"standard"` → 20, `"intermediaire"` → 10, `"reduit"` → 5 ;
  - sinon : `throw new IllegalArgumentException("categorie inconnue : " + category)` (la catégorie **telle que reçue**).
- **`public static String format(long cents)`** : `"12,34 EUR"` pour 1234, `"0,05 EUR"` pour 5, `"-1,50 EUR"` pour −150. Calcule le signe à part, travaille sur `Math.abs(cents)`, et utilise `String.format("%s%d,%02d EUR", signe, euros, centimes)`.
- **Tes tests :** les trois catégories ; `"  INTERMEDIAIRE "` ; une catégorie inconnue (`"luxe"`, type et message) ; `null` (type `NullPointerException` et message) ; `format` de 1234, 5, 0, −150 et −5.

**🧪 Expériences :**
1. Dans un test, écris `assertEquals(0.3, 0.1 + 0.2)`. Recopie le message.
2. Puis `assertEquals(0.3, 0.1 + 0.2, 1e-9)` (un 3e argument : la **tolérance**). Vert ou rouge ?

**❓ Questions :**
- Pourquoi `format(-5)` est-il un cas piège, si l'on calcule `cents / 100` et `cents % 100` sans s'occuper du signe ?
- Pourquoi tester `"  INTERMEDIAIRE "`, et pas seulement `"intermediaire"` ?

### ☐ Étape 6 — Les mutants : tes tests attrapent-ils les bugs ?

```
=== Verification des tests de ch16_testing.projects.p01_vat ===
[PASS] tes tests sur TON code : 15 tests, 15 reussis
[PASS] tes tests sur le code de REFERENCE : 15 tests, 15 reussis
[PASS] les tests de REFERENCE sur TON code : 15 tests, 15 reussis
   mutant 1 : tue (par …)
…
[PASS] mutants : 9/9 tues
```

**📖 La leçon : tester les tests.** Un test vert ne prouve rien s'il serait **aussi** vert avec un code faux (ton expérience 4 de l'étape 2). Pour mesurer tes tests, `Check` fabrique des **mutants** : des copies du code de référence où il glisse **un seul petit bug**, du genre `<` au lieu de `<=`, ou un `+ 50` oublié. Puis il lance **tes** tests sur chaque mutant :
- au moins un test échoue : le mutant est **tué** 🎯, tes tests ont vu le bug ;
- tout reste vert : le mutant **survit**, il y a un cas que tes tests ne regardent pas.

Les professionnels font la même chose avec des outils comme PIT (le *mutation testing*).

**👉 À toi :** lance `Check`. Lis ses lignes dans l'ordre :
1. **tes tests sur TON code** : tout doit être vert ;
2. **tes tests sur le code de référence** : s'ils échouent, un de tes tests attend une **mauvaise** valeur ;
3. **les tests de référence sur TON code** : s'ils échouent, **ton code** a un bug (le nom du test qui échoue te dit lequel) ;
4. **les mutants** : pour chaque survivant, cherche quel cas tu n'as pas testé. Bloqué ? Le palier 2 de `INDICES.md` dit, replié, ce que change chaque mutant.

Tu as fini quand `Check` affiche `PROJET REUSSI`.

**❓ Question :** un mutant remplace `"montant negatif : " + netCents` par `"montant negatif"`. Quel genre de test le tue ?

### Expériences (hors sortie attendue)

1. Dans **ton** `VatCalculator`, remplace `> 100` par `>= 100`. Lance tes tests : lequel échoue ? Remets le code.
2. Supprime ton test des taux 0 et 100, puis lance `Check` : quel mutant survit ? Remets le test.
3. Ajoute `@Disabled` (import `org.junit.jupiter.api.Disabled`) au-dessus d'un `@Test`. Que montre IntelliJ pour ce test ? Que compte `Check` ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class VatCalculator`, les quatre signatures exactes, `throw new IllegalArgumentException(`, `Objects.requireNonNull(`, `Locale.ROOT`, `String.format(` ; ni `double` ni `float`.
- **Tes tests :** au moins **12** tests, `@Test`, `import static org.junit.jupiter.api.Assertions.`, `assertEquals(`, `assertThrows(`, `IllegalArgumentException.class`, `NullPointerException.class`, `.getMessage()` ; ni `System.out` ni `Thread.sleep`.
- **Les 9 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

Les noms entre parenthèses sont ceux de **tes** tests : ils seront différents. Le nombre de tests aussi (au moins 12).

```
=== Verification des tests de ch16_testing.projects.p01_vat ===
[PASS] tes tests sur TON code : 15 tests, 15 reussis
[PASS] tes tests sur le code de REFERENCE : 15 tests, 15 reussis
[PASS] les tests de REFERENCE sur TON code : 15 tests, 15 reussis
   mutant 1 : tue (par roundingGoesToTheNearestCent)
   mutant 2 : tue (par zeroAndOneHundredPercentAreAllowed)
   mutant 3 : tue (par zeroCentIsAllowed)
   mutant 4 : tue (par negativeRateIsRejected)
   mutant 5 : tue (par formatKeepsTheSignOfANegativeAmount)
   mutant 6 : tue (par formatKeepsTheSignOfANegativeAmount)
   mutant 7 : tue (par categoryIgnoresCaseAndSurroundingSpaces)
   mutant 8 : tue (par eachCategoryHasItsRate)
   mutant 9 : tue (par negativeAmountIsRejectedWithItsValueInTheMessage)
[PASS] mutants : 9/9 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
