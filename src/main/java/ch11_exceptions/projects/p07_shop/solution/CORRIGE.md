# Projet 7 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : [`UnknownProductException.java`](UnknownProductException.java), [`Catalog.java`](Catalog.java) et [`Shop.java`](Shop.java). Les quatre bundles de la solution sont dans `src/main/resources/ch11_exceptions/projects/p07_shop/solution/`.
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Les bundles (fichiers `.properties`)

**Le code :** les quatre fichiers `shop*.properties` de la solution. Par exemple, la racine :

```properties
# le bundle RACINE : il contient TOUTES les cles (dernier repli de la recherche).
welcome=Welcome to {0}, {1}!
cart=Your cart: {0} item(s) for {1}.
shipping: Free shipping from {0}
error=Order rejected: {0}
! un commentaire peut aussi commencer par '!'
bye Goodbye
legal=Prices include VAT. \
      Delivery within 3 days.
```

Les espaces au début de la ligne continuée sont **ignorés** : `legal` vaut bien `Prices include VAT. Delivery within 3 days.`

**Question — les apostrophes de `promo` :** il faut les **doubler** :

```properties
promo=Aujourd''hui : -{0} % sur l''article {1}
```

Vérifié avec des apostrophes **simples** : `MessageFormat` prend tout ce qui se trouve entre deux apostrophes pour du texte littéral. Le résultat est `Aujourdhui : -{0} % sur larticle stylo` : les apostrophes disparaissent, et `{0}` n'est **pas** remplacé.

**Question — pourquoi `shop_fr_CA` n'a besoin que d'une clé ?** À cause de la **chaîne des parents** : le bundle `fr_CA` a pour parent `fr`, qui a pour parent la racine. Une clé absente de `fr_CA` est cherchée dans `shop_fr`, puis dans `shop`. Il suffit donc d'écrire ce qui **diffère** du français général.

---

## Étape 2 — Réglages et catalogue

**Le code :** [`UnknownProductException.java`](UnknownProductException.java), [`Catalog.java`](Catalog.java) et le début du `main`.

**Le piège `put` :** `Properties` hérite de `Hashtable<Object, Object>`. `put("stock", 42)` est accepté par le compilateur, mais la valeur est un `Integer`. Vérifié :
- `getProperty("stock")` rend **`null`**, parce que la valeur n'est pas une `String` ;
- `getProperty("stock", "def")` rend même la valeur par défaut, `def` ;
- `stringPropertyNames()` **ne contient pas** `stock` ;
- seul `get("stock")` rend `42`.

**`getProperty("promo.percent", "10")`** : la clé n'existe pas dans `Data.SETTINGS`, d'où la valeur par défaut `10`.

**`total` :** `NumberFormatException` est non vérifiée, donc elle n'a pas besoin d'être déclarée. Seule `UnknownProductException`, vérifiée, apparaît dans le `throws`.

---

## Étape 3 — Les clients

**Le code :** la boucle sur `Data.CUSTOMERS` et la méthode `message`.

**L'ordre de recherche de `getBundle(BASE, locale)`** : `shop_<langue>_<PAYS>`, puis `shop_<langue>`, puis **la locale par défaut** (ici `de_DE`, puis `de`), et enfin la racine `shop`.

**Question — pourquoi `it-IT` et `en` obtiennent l'allemand ?** Il n'existe ni `shop_it` ni `shop_en`. La recherche passe donc à la **locale par défaut**, `Locale.GERMANY`, fixée en première instruction du `main`, et trouve `shop_de`. C'est pour cela que `setDefault` est fait **avant** tout `getBundle`. Vérifié : avec `Locale.setDefault(Locale.ROOT)`, `it-IT` obtiendrait la racine (locale vide).

**Question — pourquoi `fr-CA` n'obtient jamais de clé allemande ?** Le repli vers la locale par défaut n'a lieu que si **aucun** fichier n'est trouvé pour la locale demandée. Ici `shop_fr_CA` existe : sa chaîne de parents est `fr_CA` → `fr` → racine, **sans** `de`. `shipping` vient donc de la racine (anglais). De même pour `fr-BE` : il trouve `shop_fr`, sans passer par l'allemand.

**La monnaie suit la locale, pas le bundle :** `it-IT` a des textes allemands mais un montant en euros, et `fr-CA` un montant en `$ CA`.

**`en-US`** : la quantité `x` lève `NumberFormatException`, dont le message est `For input string: "x"`.

---

## Étape 4 — Racine, erreurs, traduction

**Le code :** la fin du `main` et la méthode `coverage`.

**Les deux `MissingResourceException`** : la même classe sert pour une **clé** absente (`getKey()` donne `nope`) et pour un **bundle** absent. Elle est non vérifiée, mais on peut l'attraper.

**Le taux de traduction :** `fr-CA` a 4/6, comme `fr`, et non 1/6. Même avec `noFallback`, la **chaîne des parents** reste : `welcome` vient de `fr_CA`, et `cart`, `error` et `bye` de `fr`.

**Question — `en` sans `noFallback` ?** On obtiendrait **3/6 (50 %)**, comme l'allemand (vérifié). Sans bundle `en`, la recherche se replie sur la locale par défaut, et c'est `shop_de` qui serait comparé à la racine. `getNoFallbackControl` supprime **seulement** ce repli vers la locale par défaut : pour `en`, on tombe sur la racine (locale vide), d'où 0/6.

---

## Étape 5 — Locales et catégories

**Le code :** la dernière partie du `main`.

**Les quatre locales sont égales :** `new Locale("FR", "ca")` normalise la casse et donne `fr_CA`. `Locale.forLanguageTag("FR-ca")` aussi (vérifié). Une `Locale` est une valeur : `equals` compare la langue et le pays.

**`toString` contre `toLanguageTag`** : `fr_CA`, avec un tiret bas, est le format Java ; `fr-CA`, avec un tiret, est la balise IETF (BCP 47).

**`IllformedLocaleException`** : le `Builder` valide chaque champ. `"Canada"` n'est pas un code de région. Le message est `Ill-formed region: Canada [at index 0]`.

**Question — pourquoi `Locale.getDefault()` vaut encore `de_DE` ?** `setDefault(Category, …)` ne change que **la** catégorie indiquée. La locale par défaut générale reste `de_DE`. Dans l'autre sens, vérifié : `Locale.setDefault(Locale.US)` (sans catégorie) remet **les deux** catégories à `en_US`.

Conséquences sur la ligne :
- `getCurrencyInstance()` sans argument suit **FORMAT** (fr_FR), d'où `1 234,50 €` ;
- `getDisplayName()` sans argument suit **DISPLAY** (en_US), d'où `German (Germany)`.
