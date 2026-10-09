# Projet 3 — Le guichet des impôts (tests paramétrés, classes d'équivalence)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 16) :**
- **`@ParameterizedTest`** : **un** test, lancé sur **un tableau de cas** ;
- les sources de cas :
  - `@ValueSource` (une colonne : `ints`, `longs`, `strings`…) ;
  - `@CsvSource` (plusieurs colonnes), et sa forme `textBlock = """ … """` ;
  - `@NullAndEmptySource` (`null` et `""`) ;
  - `@MethodSource` (une méthode `static` qui rend un `Stream<Arguments>`) ;
- **`name = "…{0}…"`** : un nom lisible pour chaque cas ;
- **les classes d'équivalence** : un cas par famille d'entrées qui se comportent pareil, plus les valeurs limites entre familles ;
- **un test de propriété** : une règle vraie pour **toutes** les entrées.

Côté algorithmes : un barème **par tranches** (taux marginal), un quotient familial en demi-parts, une politique de mots de passe.

**Ce que TU crées :** dans `ch16_testing.projects.p03_tax` :
- **`TaxCalculator`** et **`PasswordPolicy`**, le code (leurs méthodes sont imposées) ;
- **`TaxCalculatorTest`** et **`PasswordPolicyTest`**, tes tests.

**Règle du crescendo :** chapitres 1 à 15, plus JUnit. Pas de `double` ni de `float` dans le code ; pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : la facture d'eau.

> **🧰 Tes outils pour ce projet**
>
> - **Les imports** des tests paramétrés viennent d'un autre paquet : `org.junit.jupiter.params.ParameterizedTest` et `org.junit.jupiter.params.provider.*` (`CsvSource`, `ValueSource`, `MethodSource`, `Arguments`, `NullAndEmptySource`). IntelliJ les propose avec **Alt+Entrée** sur le mot souligné en rouge.
> - **Lire les cas :** dans la fenêtre Run, un test paramétré se déplie : **une ligne par cas**, avec son nom. Un cas rouge n'empêche pas les autres de s'exécuter.
> - **Lancer `Check` :** flèche verte à côté de `Check.main`. Il compte **chaque cas** comme un test.

---

## Tableau de bord

### ☐ Étape 1 — Le barème par tranches

**📖 La leçon : un barème marginal.** La facture d'eau d'une ville coûte : rien pour les 50 premiers m³, 2 € le m³ de 51 à 100, 3 € le m³ au-delà. Pour 120 m³, on ne paie **pas** 120 × 3 € : chaque **tranche** est payée à **son** prix.

```
  0 à  50 m³ : 50 m³ × 0 € =   0 €
 51 à 100 m³ : 50 m³ × 2 € = 100 €
101 à 120 m³ : 20 m³ × 3 € =  60 €
                     total = 160 €
```

Le prix du **dernier** m³ (ici 3 €) s'appelle le **prix marginal**. Une boucle sur les tranches fait ce calcul : pour chaque tranche, on prend la partie de la consommation qui tombe **dedans** (`Math.min(conso, haut) - bas`, si la conso dépasse le bas de la tranche).

**👉 À toi :** crée `public final class TaxCalculator` (constructeur `private`). Le barème de Javaland, en **euros entiers** :

| Revenu annuel | Taux de la tranche |
|---|---|
| jusqu'à 10 000 | 0 % |
| de 10 001 à 25 000 | 10 % |
| de 25 001 à 60 000 | 25 % |
| au-delà de 60 000 | 40 % |

- **`public static long tax(long income)`** : l'impôt, **arrondi à l'euro le plus proche**. Additionne, pour chaque tranche, `partie × taux` (des **centièmes** d'euro), puis rends `(somme + 50) / 100`.
- **`public static int marginalRate(long income)`** : le taux de la tranche où tombe le revenu. Une limite appartient à la tranche **du dessous** : `marginalRate(10_000)` vaut 0.
- Les deux méthodes : un revenu négatif lance `IllegalArgumentException("revenu negatif : " + income)`.

Deux valeurs pour te repérer : `tax(25_000)` vaut **1500**, et `tax(100_000)` vaut **26 250**.

**❓ Question :** calcule à la main `tax(60_000)`, puis `tax(10_004)` et `tax(10_005)`. Pourquoi ces deux derniers sont-ils différents ?

### ☐ Étape 2 — Un tableau de cas : `@CsvSource`

```
(fenêtre Run d'IntelliJ)
✔ taxFollowsTheBrackets(long, long)
   ✔ impot de 0 = 0
   ✔ impot de 10000 = 0
   …
```

**📖 La leçon : un test paramétré.** Dix tests qui ne diffèrent que par leurs valeurs, c'est dix copies du même code. Un **test paramétré** s'écrit une fois, et JUnit le lance une fois **par ligne** de son tableau :

```java
@ParameterizedTest(name = "{0} m3 coutent {1} EUR")       // {0} = 1re colonne, {1} = 2e colonne
@CsvSource({
        "0,   0",
        "50,  0",
        "51,  2",
        "120, 160"
})
void laFactureSuitLesTranches(int m3, int euros) {      // un paramètre par colonne
    assertEquals(euros, FactureEau.prix(m3));
}
```

- `@ParameterizedTest` **remplace** `@Test` (ne mets pas les deux).
- Chaque chaîne de `@CsvSource` est une ligne ; les colonnes sont séparées par des **virgules** ; JUnit convertit chaque texte vers le type du paramètre (`"51"` → `int`).
- Pour un long tableau, la forme **text block** (chapitre 1) est plus lisible : `@CsvSource(textBlock = """ … """)`, une ligne par cas, sans guillemets autour de chaque ligne.
- `name` donne un nom à chaque cas : `{0}`, `{1}`… sont les colonnes, `{index}` le numéro du cas.

**👉 À toi :** dans `TaxCalculatorTest` :
- un test paramétré `@CsvSource(textBlock = …)` pour `tax`, avec un `name` qui montre le revenu et l'impôt. Tes lignes doivent couvrir : 0 ; chaque limite (10 000, 25 000, 60 000) et le revenu **juste au-dessus** ; l'arrondi (10 004 et 10 005) ; un grand revenu (100 000) ;
- un test paramétré `@CsvSource({…})` (la forme courte) pour `marginalRate`, sur chaque limite et juste au-dessus.

**🧪 Expériences** (supprime ensuite ces lignes) :
1. Ajoute une ligne `"abc, 0"` au tableau de `tax`. Recopie le message du cas rouge.
2. Ajoute une ligne avec **une seule** colonne : `"10000"`. Recopie le message.
3. Retire le `name = "…"`. Comment s'appellent maintenant les cas ?

**❓ Question :** pourquoi tester 25 000 **et** 25 001, mais pas 40 000 ?

### ☐ Étape 3 — Une colonne : `@ValueSource`, et une propriété

**📖 La leçon : `@ValueSource` et les propriétés.** Quand chaque cas n'a qu'**une** valeur, `@ValueSource` suffit : `@ValueSource(ints = {1, 2, 3})`, `@ValueSource(longs = {…})`, `@ValueSource(strings = {"a", "b"})`.

On peut aussi tester une **propriété** : une phrase vraie pour **toute** entrée, sans connaître le résultat exact. Pour la facture d'eau : « consommer 1 m³ de plus ne coûte jamais plus de 3 € ».

```java
@ParameterizedTest(name = "1 m3 de plus que {0} coute au plus 3 EUR")
@ValueSource(ints = {49, 50, 99, 100, 500})
void unM3DePlusCouteAuPlusTroisEuros(int m3) {
    int supplement = FactureEau.prix(m3 + 1) - FactureEau.prix(m3);
    assertTrue(supplement >= 0 && supplement <= 3, "supplement " + supplement);   // le message s'affiche si c'est faux
}
```

**👉 À toi :**
- un test `@ValueSource(longs = …)` : les revenus −1 et −10 000 sont refusés par `tax` **et** par `marginalRate`, avec le message exact ;
- un test de propriété `@ValueSource(longs = …)` : gagner **1 euro de plus** ne fait jamais payer **plus d'1 euro** d'impôt en plus (ni moins de 0). Choisis des revenus autour de chaque limite.

**❓ Question :** quel genre de bug ce test de propriété attraperait-il, qu'un tableau de valeurs exactes pourrait laisser passer ?

### ☐ Étape 4 — Des cas complexes : `@MethodSource`

**📖 La leçon : `@MethodSource`.** Quand les cas ont beaucoup de colonnes, des types qui ne s'écrivent pas en texte (une `List`…), ou méritent un commentaire chacun, on les fabrique dans une méthode **`static`** qui rend un `Stream<Arguments>` (chapitre 10) :

```java
@ParameterizedTest(name = "{0} m3 pour {1} personnes : {2} EUR")
@MethodSource("foyers")                    // le nom de la méthode qui fournit les cas
void prixParFoyer(int m3, int personnes, int euros) {
    assertEquals(euros, FactureEau.prixFoyer(m3, personnes));
}

static Stream<Arguments> foyers() {
    return Stream.of(
            Arguments.of(120, 1, 160),     // une personne : le tarif normal
            Arguments.of(120, 2, 40));     // deux personnes : 50 m3 gratuits chacune
}
```

**👉 À toi :**
- **`public static long taxWithShares(long income, int adults, int children)`** : le **quotient familial**. On compte en **demi-parts** :
  - 2 demi-parts par adulte ;
  - 1 demi-part pour chacun des **deux premiers** enfants ;
  - 2 demi-parts pour chaque enfant **à partir du 3e**.
  
  Le revenu d'**une part** vaut `income * 2 / demiParts` (division entière). L'impôt du foyer vaut `tax(revenuDUnePart) * demiParts / 2`.
  - `adults` doit valoir 1 ou 2, sinon `IllegalArgumentException("adultes : 1 ou 2 (recu " + adults + ")")` ;
  - `children < 0` : `IllegalArgumentException("enfants negatif : " + children)`.
- **Tes tests :** un test `@MethodSource` sur un revenu de 50 000, avec 1 adulte, puis 2 adultes et 0, 1, 2, 3 enfants, et un revenu 0. **Calcule chaque résultat à la main** (un commentaire par cas). Un test ordinaire `@Test` pour les trois refus (3 adultes, 0 adulte, −1 enfant).

**🧪 Expérience :** enlève le mot `static` de ta méthode de cas. Recopie le message. Remets-le.

**❓ Question :** pour 2 adultes et 2 enfants, on attendrait « 3 × l'impôt de 16 666,66 € ». Combien rend vraiment `taxWithShares(50_000, 2, 2)`, et pourquoi pas un multiple rond ?

### ☐ Étape 5 — Les mots de passe : les classes d'équivalence

**📖 La leçon : une famille, un cas.** Impossible de tester tous les mots de passe. On les range en **familles** (des *classes d'équivalence*) : dans une famille, tous les mots de passe se comportent pareil, donc **un** représentant suffit. Pour une règle « au moins 12 caractères », il y a deux familles (trop court, assez long), plus la **limite** entre elles (11 et 12).

Le piège : un cas qui casse **deux** règles à la fois ne dit pas laquelle il teste. Chaque cas casse **une seule** règle.

`@NullAndEmptySource` fournit deux cas : `null` et `""` (pour un paramètre `String`, `List`…).

**👉 À toi :** crée `public final class PasswordPolicy` :
- **`public static List<String> violations(String password)`** : la liste des règles cassées, **dans cet ordre** :
  1. `null` ou `""` : rend **seulement** `List.of("vide")` ;
  2. moins de 12 caractères : `"trop court"` ;
  3. plus de 64 caractères : `"trop long"` ;
  4. aucun chiffre (`Character::isDigit`) : `"sans chiffre"` ;
  5. aucune majuscule (`Character::isUpperCase`) : `"sans majuscule"` ;
  6. aucune minuscule (`Character::isLowerCase`) : `"sans minuscule"` ;
  7. **que** des lettres et des chiffres (`Character::isLetterOrDigit`) : `"sans symbole"` ;
  8. au moins un blanc (`Character::isWhitespace`) : `"espace interdit"`.
  
  Rends une liste non modifiable. Astuce : `password.chars()` donne un `IntStream` (chapitre 10) avec `noneMatch`, `allMatch`, `anyMatch`.
- **`public static boolean isValid(String password)`** : aucune violation.
- **Tes tests,** dans `PasswordPolicyTest` :
  - `@ValueSource(strings = …)` : trois mots de passe valides ;
  - `@NullAndEmptySource` : `List.of("vide")` ;
  - `@MethodSource` : un mot de passe par règle 2 et 4 à 8, qui casse **cette règle seulement**, avec la liste attendue ;
  - `@MethodSource` : les longueurs 11, 12, 64 et 65 (fabrique le mot de passe avec `"Aa1!" + "x".repeat(longueur - 4)`) ;
  - un cas qui casse **plusieurs** règles, pour vérifier l'**ordre** des messages.

**❓ Question :** `"Abcde fghij1"` casse-t-il la règle « sans symbole » ? Vérifie avec ton code, puis explique.

### ☐ Étape 6 — Les mutants

**👉 À toi :** lance `Check` et tue les **17** mutants. Bloqué ? Le palier 2 de `INDICES.md` dit, replié, ce que change chaque mutant.

**❓ Question :** un mutant place la 3e limite à 50 000 au lieu de 60 000. Lequel de tes cas le voit, et pourquoi le test de propriété ne suffit-il pas ?

### Expériences (hors sortie attendue)

1. Mets `@ParameterizedTest` **et** `@Test` sur la même méthode. Que se passe-t-il au lancement ?
2. Mets `@NullAndEmptySource` sur un test dont le paramètre est un `int`. Recopie le message.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class TaxCalculator` avec `static long tax(long income)`, `static int marginalRate(long income)`, `static long taxWithShares(long income, int adults, int children)` ; `final class PasswordPolicy` avec `static List<String> violations(String password)` et `static boolean isValid(String password)` ; ni `double` ni `float`.
- **Tes tests :** au moins **40** tests (chaque cas compte), `@ParameterizedTest`, `@CsvSource(` avec `textBlock = """`, `@ValueSource(longs` et `@ValueSource(strings`, `@NullAndEmptySource`, `@MethodSource(`, `Arguments.of(`, `Stream<Arguments>`, un `name = "…{0}…"` ; ni `System.out` ni `Thread.sleep`.
- **Les 17 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch16_testing.projects.p03_tax ===
[PASS] tes tests sur TON code : 49 tests, 49 reussis
[PASS] tes tests sur le code de REFERENCE : 49 tests, 49 reussis
[PASS] les tests de REFERENCE sur TON code : 49 tests, 49 reussis
   mutant 1 : tue (par taxFollowsTheBrackets [impot de 10005 = 1])
   …
   mutant 17 : tue (par nullAndEmptyAreJustEmpty [[1] password=null])
[PASS] mutants : 17/17 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
