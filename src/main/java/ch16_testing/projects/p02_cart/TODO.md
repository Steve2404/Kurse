# Projet 2 — Le panier de l'épicerie (`@BeforeEach`, `@Nested`, `assertAll`)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 16) :**
- **le cycle de vie** d'un test : JUnit crée **une instance neuve** de la classe de test pour **chaque** test ;
- **`@BeforeEach`** (préparer avant chaque test), et pour aller plus loin `@AfterEach`, `@BeforeAll`, `@AfterAll` ;
- **`assertAll`** : plusieurs vérifications, toutes signalées en une fois ;
- **`assertTrue`**, **`assertFalse`**, comparer des listes avec `assertEquals(List.of(…), …)` ;
- **`@Nested`** et **`@DisplayName`** : ranger les tests par situation, et leur donner un nom en français ;
- **tester un objet qui a un état** : un refus ne doit **rien** changer (un *invariant*).

Côté algorithmes : un panier trié, des seuils de livraison et de remise, une remise arrondie.

**Ce que TU crées :** dans `ch16_testing.projects.p02_cart` :
- **`Cart`**, le panier (ses méthodes publiques sont imposées ; l'intérieur, c'est toi qui le choisis) ;
- **`CartTest`**, tes tests.

**Règle du crescendo :** chapitres 1 à 15, plus JUnit. Pas de `double` ni de `float` dans `Cart` ; pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : une tirelire.

> **🧰 Tes outils pour ce projet**
>
> - **Lancer tes tests :** flèche verte à côté de `class CartTest` (tous), d'une classe `@Nested` (ce groupe), ou d'un `@Test` (un seul). Raccourci : **Ctrl+Maj+F10**.
> - **Relancer le dernier lancement :** **Maj+F10**. **Relancer seulement les tests échoués :** dans la fenêtre Run, le bouton ✘ avec une flèche (*Rerun Failed Tests*).
> - **Lancer `Check` :** flèche verte à côté de `Check.main`. Pas d'argument.

---

## Tableau de bord

### ☐ Étape 1 — Le panier, et un panier neuf pour chaque test

**📖 La leçon : `@BeforeEach`.** Quand tous les tests ont besoin du même objet de départ, on le prépare dans une méthode `@BeforeEach` : JUnit l'appelle **avant chaque** test.

```java
class TirelireTest {

    private Tirelire tirelire;      // un champ

    @BeforeEach
    void nouvelleTirelire() {
        tirelire = new Tirelire();  // avant CHAQUE test : une tirelire vide
    }

    @Test
    void uneTirelireNeuveEstVide() {
        assertEquals(0, tirelire.solde());
    }

    @Test
    void deposerAugmenteLeSolde() {
        tirelire.depose(200);
        assertEquals(200, tirelire.solde());
    }
}
```

Plus fort encore : JUnit crée **un nouvel objet `TirelireTest`** pour **chaque** test. Un champ modifié par un test n'est jamais vu par un autre. C'est ce qui rend les tests **indépendants** (projet 1, étape 2).

**👉 À toi :** crée `public final class Cart`. Les montants sont en centimes (`long`). Pour cette étape :
- **`public void add(String sku, int quantity, long unitCents)`** : ajoute `quantity` articles du code `sku` (*stock keeping unit*, le code article), au prix unitaire `unitCents`. Si l'article est déjà dans le panier, les quantités **s'additionnent** (les contrôles viennent à l'étape 4) ;
- **`public int quantity(String sku)`** : la quantité de cet article, **0** s'il est absent ;
- **`public boolean isEmpty()`** ;
- **`public List<String> lines()`** : une ligne par article, **triée par code article**, au format `CODE x quantité = total de la ligne`. Par exemple : `APPLE x 3 = 150` ;
- **`public long subtotalCents()`** : la somme des lignes.

Puis crée `CartTest` avec un champ `cart` et un `@BeforeEach` qui crée un panier neuf. Premiers tests :
- un panier neuf est vide, et son sous-total vaut 0 ;
- `quantity` d'un article absent vaut 0 ;
- ajouter deux fois le même article additionne les quantités.

**🧪 Expériences :**
1. Dans `CartTest`, ajoute un champ `int compteur = 0;` et deux tests qui font chacun `compteur++;` puis `assertEquals(1, compteur);`. Les deux passent-ils ?
2. Rends le champ `static`. Lance les deux tests ensemble : que se passe-t-il ? Supprime ensuite ces deux tests.

**❓ Question :** que choisis-tu pour ranger les lignes, si `lines()` doit être triée quel que soit l'ordre des ajouts ?

### ☐ Étape 2 — Vérifier plusieurs choses à la fois : `assertAll`

**📖 La leçon : `assertAll`.** Normalement, un test **s'arrête** au premier `assertEquals` qui échoue : tu ne vois qu'une erreur à la fois. `assertAll` exécute **toutes** les vérifications qu'on lui donne (des lambdas), et les signale **toutes** ensemble :

```java
@Test
void uneTirelireNeuve() {
    assertAll(
            () -> assertEquals(0, tirelire.solde()),
            () -> assertTrue(tirelire.estVide()),
            () -> assertFalse(tirelire.estCassee()));
}
```

- `assertTrue(x)` et `assertFalse(x)` vérifient un `boolean`. Pour comparer des nombres, préfère `assertEquals` : son message dit **quelle** valeur est sortie.
- Deux listes sont égales si elles ont les mêmes éléments **dans le même ordre** : `assertEquals(List.of("a", "b"), liste)`.

**👉 À toi :**
- un test « panier vide » avec `assertAll` : `isEmpty()` vrai, `lines()` égale `List.of()`, sous-total 0 ;
- un `@BeforeEach` (dans l'étape 3, il ira dans un groupe ; pour l'instant, mets ces ajouts au début du test) qui ajoute, **dans cet ordre** : `PEAR` ×2 à 120, `APPLE` ×3 à 50, `MILK` ×1 à 99 ;
- un test des lignes : `List.of("APPLE x 3 = 150", "MILK x 1 = 99", "PEAR x 2 = 240")` ;
- un test du sous-total (calcule-le à la main) avec `assertFalse(cart.isEmpty())`.

**🧪 Expérience :** écris un test temporaire qui contient `assertEquals(1, 2); assertEquals("a", "b");` à la suite, puis un autre qui met les deux dans un `assertAll`. Compare les deux messages d'échec. Supprime ensuite ces deux tests.

**❓ Question :** pourquoi ajouter les articles **dans le désordre** (`PEAR` avant `APPLE`) ?

### ☐ Étape 3 — Ranger les tests par situation : `@Nested` et `@DisplayName`

```
(fenêtre Run d'IntelliJ)
✔ Le panier
   ✔ quand il est vide
      ✔ n'a ni ligne, ni total, ni livraison
      ✔ rend 0 pour un article inconnu
   ✔ avec trois articles
      ✔ liste les lignes triees par code article
      …
```

**📖 La leçon : des groupes de tests.** Une classe interne (chapitre 7) marquée **`@Nested`** est un **groupe** de tests. Elle peut avoir son propre `@BeforeEach`, qui s'exécute **après** celui de la classe extérieure. **`@DisplayName("…")`** remplace, dans la fenêtre Run, le nom de la méthode par une phrase lisible.

```java
@DisplayName("La tirelire")
class TirelireTest {
    private Tirelire tirelire;

    @BeforeEach
    void nouvelleTirelire() { tirelire = new Tirelire(); }

    @Nested
    @DisplayName("avec 5 EUR dedans")
    class AvecCinqEuros {

        @BeforeEach
        void remplir() { tirelire.depose(500); }     // APRES nouvelleTirelire()

        @Test
        @DisplayName("on peut retirer 2 EUR")
        void retrait() {
            tirelire.retire(200);
            assertEquals(300, tirelire.solde());
        }
    }
}
```

La classe `@Nested` n'est **pas** `static` : elle doit pouvoir lire le champ `tirelire` de la classe extérieure.

**👉 À toi :** range tes tests :
- `@DisplayName("Le panier")` sur `CartTest` ;
- un groupe `@Nested` « quand il est vide » ;
- un groupe `@Nested` « avec trois articles », avec un `@BeforeEach` qui fait les trois ajouts de l'étape 2 ;
- un `@DisplayName` sur chaque groupe, et sur au moins trois tests.

**🧪 Expérience :** ajoute temporairement des `@BeforeAll` (méthode `static`), `@AfterEach` et `@AfterAll` (`static`), et un `System.out.println` dans chacune et dans les `@BeforeEach`. Lance tous les tests et note l'**ordre** des messages. (`Check` refuse `System.out` dans les tests : supprime tout ensuite.)

**❓ Question :** pourquoi une méthode `@BeforeAll` doit-elle être `static` ?

### ☐ Étape 4 — Refuser, sans rien abîmer

**📖 La leçon : les invariants.** Un objet qui a un **état** doit rester **cohérent** même quand il refuse une demande. Une tirelire qui refuse un retrait trop gros ne doit pas avoir perdu d'argent au passage. Pour le tester, on vérifie l'exception **et** l'état **après** :

```java
@Test
void unRetraitTropGrosEstRefuseSansToucherAuSolde() {
    tirelire.depose(500);
    assertThrows(IllegalStateException.class, () -> tirelire.retire(800));
    assertEquals(500, tirelire.solde());          // rien n'a bougé
}
```

Dans le code, cela veut dire : **tous les contrôles d'abord**, la modification en dernier.

**👉 À toi :**
- **Les contrôles de `add`**, dans cet ordre :
  1. `Objects.requireNonNull(sku, "sku absent")` ;
  2. un code vide ou fait d'espaces (`isBlank()`) : `IllegalArgumentException("sku vide")` ;
  3. `quantity < 1` : `IllegalArgumentException("quantite invalide : " + quantity)` ;
  4. `unitCents < 0` : `IllegalArgumentException("prix negatif : " + unitCents)` (un article **gratuit**, à 0, est permis) ;
  5. l'article est déjà là avec un **autre** prix : `IllegalStateException("prix different pour " + sku)` ;
  6. la quantité **totale** de l'article dépasserait 99 : `IllegalArgumentException("maximum 99 par article : " + sku)`.
- **`public void remove(String sku, int quantity)`** :
  - article absent : `NoSuchElementException("article absent : " + sku)` ;
  - `quantity < 1` : `IllegalArgumentException("quantite invalide : " + quantity)` ;
  - retirer **autant ou plus** que la quantité présente **supprime la ligne**.
- **`lines()`** rend une liste **non modifiable** (`List.copyOf`, chapitre 9).
- **Tes tests :** chaque refus (type et message), **avec** l'état vérifié après (le prix différent, le 100e article) ; l'article gratuit ; retirer une partie ; retirer tout ou plus ; un `lines().add(…)` qui lance `UnsupportedOperationException` ; un retrait d'article absent.

**❓ Questions :**
- 98 pommes dans le panier, on en ajoute 2 : que doit contenir le panier après le refus ?
- Pourquoi rendre une liste non modifiable plutôt que la liste interne du panier ?

### ☐ Étape 5 — Les codes promo et les frais de port

**📖 Rappel :** les valeurs limites (projet 1, étape 4) : juste sur le seuil, et juste à côté.

**👉 À toi :**
- **`public void applyCode(String code)`** :
  - un code a déjà été appliqué : `IllegalStateException("un seul code par panier")` ;
  - un code autre que `"MOINS10"` ou `"LIVRAISON"` : `IllegalArgumentException("code inconnu : " + code)`.
- **`public long discountCents()`** : avec `MOINS10` **et** un sous-total d'au moins **5000**, 10 % du sous-total, arrondi au centime le plus proche (`(sousTotal * 10 + 50) / 100`) ; sinon 0. Elle se recalcule à chaque appel.
- **`public long shippingCents()`** : **0** si le panier est vide, ou avec le code `LIVRAISON`, ou si le sous-total (avant remise) est d'au moins **3000** ; sinon **490**.
- **`public long totalCents()`** : sous-total − remise + livraison.
- **Tes tests :** les seuils de livraison (2999 et 3000) et de remise (4999 et 5000), l'arrondi de la remise (un article à 5005), le code `LIVRAISON` sur un petit panier, un code inconnu, un 2e code. Ajoute au test « panier vide » : livraison 0 et total 0.

**❓ Question :** avec le code `MOINS10` appliqué **avant** d'ajouter les articles, la remise doit-elle s'appliquer ? Comment ton code le garantit-il ?

### ☐ Étape 6 — Les mutants

**📖 Rappel :** un mutant **survit** quand aucun de tes tests ne voit son bug (projet 1, étape 6).

**👉 À toi :** lance `Check` et tue les **14** mutants. Pour chaque survivant, demande-toi quelle entrée donnerait un résultat différent, puis écris le test. Bloqué ? Le palier 2 de `INDICES.md` dit, replié, ce que change chaque mutant.

**❓ Question :** un mutant oublie d'**additionner** les quantités : `add` remplace la quantité au lieu de l'ajouter. Quel test le tue ?

### Expériences (hors sortie attendue)

1. Dans ton `Cart`, déplace le contrôle du maximum 99 **après** la modification du panier. Quel test échoue ? Remets le code.
2. Lance seulement le groupe « avec trois articles » (sa flèche verte). Le `@BeforeEach` de la classe extérieure s'exécute-t-il quand même ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class Cart` et les signatures exactes de `add`, `remove`, `quantity`, `isEmpty`, `lines`, `subtotalCents`, `applyCode`, `discountCents`, `shippingCents`, `totalCents` ; `NoSuchElementException(`, `IllegalStateException(` ; ni `double` ni `float`.
- **Tes tests :** au moins **20** tests, `@BeforeEach`, `@Nested`, `@DisplayName(`, `assertAll(`, `assertEquals(`, `assertTrue(`, `assertFalse(`, `assertThrows(`, `NoSuchElementException.class`, `UnsupportedOperationException.class`, `List.of(` ; ni `System.out` ni `Thread.sleep`.
- **Les 14 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

Les noms entre parenthèses sont ceux de **tes** tests (avec leur `@DisplayName` entre crochets).

```
=== Verification des tests de ch16_testing.projects.p02_cart ===
[PASS] tes tests sur TON code : 20 tests, 20 reussis
[PASS] tes tests sur le code de REFERENCE : 20 tests, 20 reussis
[PASS] les tests de REFERENCE sur TON code : 20 tests, 20 reussis
   mutant 1 : tue (par ninetyNineIsTheMaximum [99 par article au plus, et un refus ne change rien])
   …
   mutant 14 : tue (par freeShippingFromExactlyThirtyEuros [livraison gratuite a partir de 30 EUR pile])
[PASS] mutants : 14/14 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
