# Projet 7 (CAPSTONE) — La boutique internationale

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Notions visées :** tout le chapitre 11.
- **`ResourceBundle`** :
  - des fichiers `.properties` que **tu écris** (les 3 séparateurs `=` `:` espace, les commentaires `#` et `!`, la ligne continuée par `\`) ;
  - `getBundle`, l'**ordre de recherche** (la locale demandée, sa langue, puis la locale **par défaut**, puis la racine) et la **chaîne des parents** ;
  - `getLocale`, `getString`, `containsKey`, `keySet` ;
  - `MissingResourceException` (`getKey`) ;
  - `Control.getNoFallbackControl` ;
- **`MessageFormat`** : les `{0}` `{1}`, et l'apostrophe **doublée** `''` ;
- **`Properties`** : `setProperty`, `getProperty` avec valeur par défaut, `stringPropertyNames`, et le piège `put` d'une valeur non `String` ;
- **`Locale`** :
  - quatre façons de la construire (constructeur, constante, `Builder`, `forLanguageTag`) ;
  - `toString` et `toLanguageTag`, `getDisplayName` ;
  - `IllformedLocaleException` ;
  - les catégories `Locale.Category.FORMAT` et `DISPLAY` ;
- les exceptions et les formats des projets précédents.

Côté algorithme : un **taux de traduction** par locale. Une clé est traduite si sa valeur diffère de celle de la racine.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :**
- dans le paquet `ch11_exceptions.projects.p07_shop` : `UnknownProductException`, `Catalog` et **`Shop`** (le `main`) ;
- **quatre fichiers** dans `src/main/resources/ch11_exceptions/projects/p07_shop/` : `shop.properties`, `shop_fr.properties`, `shop_fr_CA.properties` et `shop_de.properties`.

**Règle du crescendo :** chapitres 1 à 11 (voir `PARCOURS.md`).

---

## Tableau de bord

### ☐ Étape 1 — Les bundles (fichiers `.properties`)

Le nom de base est **`ch11_exceptions.projects.p07_shop.shop`** : le paquet, puis `shop`, sans suffixe ni extension. Écris exactement ces clés et valeurs, en ASCII :

| Clé | `shop.properties` (racine) | `shop_fr` | `shop_fr_CA` | `shop_de` |
|---|---|---|---|---|
| `welcome` | `Welcome to {0}, {1}!` | `Bienvenue chez {0}, {1} !` | `Bienvenue chez {0}, mon ami {1} !` | `Willkommen bei {0}, {1}!` |
| `cart` | `Your cart: {0} item(s) for {1}.` | `Votre panier : {0} article(s) pour {1}.` | — | `Ihr Warenkorb: {0} Artikel fuer {1}.` |
| `shipping` | `Free shipping from {0}` | — | — | — |
| `error` | `Order rejected: {0}` | `Commande refusee : {0}` | — | — |
| `bye` | `Goodbye` | `Au revoir` | — | `Auf Wiedersehen` |
| `legal` | `Prices include VAT. Delivery within 3 days.` | — | — | — |
| `promo` | — | `Aujourd'hui : -{0} % sur l'article {1}` | — | — |

**Dans `shop.properties`** :
- écris `shipping` avec le séparateur `:`, et `bye` avec un simple espace ;
- coupe `legal` en deux lignes, avec `\` en fin de première ligne ;
- ajoute un commentaire `#` et un commentaire `!`.

**Dans `shop_fr.properties`** : la valeur de `promo` est un **motif `MessageFormat`**. Comment faut-il y écrire les apostrophes ?

**Question :** pourquoi `shop_fr_CA` n'a-t-il besoin que d'une seule clé ?

### ☐ Étape 2 — Réglages et catalogue

```
reglages : [promo.item, shipping.free, shop.name], promo 10 %, stock null (pas une String !), get 42
```
- **Première instruction du `main` :** `Locale.setDefault(Locale.GERMANY)`.
- **Les réglages :**
  1. `Properties settings = new Properties()`, remplie avec `setProperty` depuis `Data.SETTINGS` (`cle=valeur`) ;
  2. puis `settings.put("stock", 42)`.
  
  Affiche les noms triés (`stringPropertyNames()`), `getProperty("promo.percent", "10")`, `getProperty("stock")`, puis `get("stock")`.
- **`UnknownProductException extends Exception`** : construite avec `(String product)`, message `produit inconnu : <produit>`.
- **`Catalog(String[] lines)`** (`produit=prix`) :
  - `long price(String product) throws UnknownProductException` ;
  - `long[] total(String cart) throws UnknownProductException` rend `{nombre d'articles, total en centimes}` pour `produit:quantite,produit:quantite`. Une quantité illisible lève `NumberFormatException`.

### ☐ Étape 3 — Les clients

```
[fr-FR -> bundle fr] Bienvenue chez Javashop, Marie ! | Votre panier : 5 article(s) pour 30,97_€. | Free shipping from 50,00_€ | Aujourd'hui : -10 % sur l'article stylo | Au revoir
[it-IT -> bundle de] Willkommen bei Javashop, Giulia! | ...
[en -> bundle de] Willkommen bei Javashop, Sam! | Order rejected: produit inconnu : parapluie | Auf Wiedersehen
```
- **`static String message(ResourceBundle bundle, String key, Locale locale, Object... values)`** rend `new MessageFormat(bundle.getString(key), locale).format(values)`.
- **Pour chaque `balise|prenom|panier` de `Data.CUSTOMERS`**, avec `locale = Locale.forLanguageTag(balise)` et `bundle = ResourceBundle.getBundle(BASE, locale)` :
  1. `[<balise> -> bundle <bundle.getLocale(), ou racine si vide>] ` ;
  2. `welcome` avec `(nom de la boutique, prénom)` ;
  3. dans un `try` :
     - ` | ` + `cart` avec `(nombre d'articles, total formaté par getCurrencyInstance(locale))` ;
     - si le total est sous `shipping.free`, ` | ` + `shipping` avec le seuil formaté ;
  4. `catch (UnknownProductException | NumberFormatException e)` → ` | ` + `error` avec `e.getMessage()` ;
  5. si `bundle.containsKey("promo")`, ` | ` + `promo` avec `(promo.percent, promo.item)` ;
  6. ` | ` + `bundle.getString("bye")`.
  
  La ligne entière passe par `visible` (projet 5).
- **Questions :**
  - Pourquoi `it-IT` et `en` obtiennent-ils l'**allemand** ?
  - Pourquoi `fr-CA` n'obtient-il **jamais** de clé allemande (`shipping` vient de la racine) ?

### ☐ Étape 4 — Racine, erreurs, traduction

```
racine : [bye, cart, error, legal, shipping, welcome] ; legal = Prices include VAT. Delivery within 3 days.
cle absente : MissingResourceException, cle nope
traduction : | fr-CA 4/6 (67_%) | fr 4/6 (67_%) | de 3/6 (50_%) | en 0/6 (0_%)
```
- **La racine :** le bundle de `Locale.ROOT`. Affiche ses clés triées et `legal`.
- **Deux erreurs :**
  - `getString("nope")` → `catch (MissingResourceException e)` : nom simple et `getKey()` ;
  - `getBundle` d'un nom de base `…p07_shop.missing` → nom simple seulement.
- **`static String coverage(String tag)`** :
  1. `noFallback = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)` ;
  2. charge la racine et le bundle de la balise, tous deux avec `noFallback` ;
  3. compte les clés de la racine dont la valeur **diffère** dans le bundle ;
  4. rend `<balise> <traduites>/<total> (<ratio en getPercentInstance(Locale.FRANCE)>)`.
  
  Applique-le à chaque balise de `Data.COVERAGE` (ligne visible).
- **Question :** que donnerait `en` **sans** `noFallback` (locale par défaut allemande) ?

### ☐ Étape 5 — Locales et catégories

```
locales : fr_CA fr-CA egales true, français (Canada) / Französisch (Kanada), langue fr, pays CA
Builder : IllformedLocaleException
categories : 1_234,50_€ ; German (Germany) ; defaut de_DE, FORMAT fr_FR, DISPLAY en_US
```
- **Quatre locales :**
  - `a = new Locale("FR", "ca")` ;
  - `b = Locale.CANADA_FRENCH` ;
  - `c = new Locale.Builder().setLanguage("fr").setRegion("CA").build()` ;
  - `d = Locale.forLanguageTag("fr-CA")`.
  
  Affiche `a`, `d.toLanguageTag()`, l'égalité des quatre, `a.getDisplayName(Locale.FRENCH)`, `a.getDisplayName(Locale.GERMAN)`, `getLanguage()` et `getCountry()`.
- **`new Locale.Builder().setRegion("Canada")`** → `catch (IllformedLocaleException e)` : son nom simple.
- **Les catégories :**
  1. `Locale.setDefault(Locale.Category.FORMAT, Locale.FRANCE)` ;
  2. `Locale.setDefault(Locale.Category.DISPLAY, Locale.US)` ;
  3. affiche `NumberFormat.getCurrencyInstance().format(1234.5)`, `Locale.GERMANY.getDisplayName()`, puis `Locale.getDefault()` et la locale de chaque catégorie (ligne visible).
- **Question :** pourquoi `Locale.getDefault()` vaut-il encore `de_DE` ?

---

## Checklist (vérifiée par `Check`)

- `Data.SETTINGS`, `Data.CATALOG`, `Data.CUSTOMERS`, `Data.COVERAGE` ;
- `new Properties()`, `.setProperty(`, `.getProperty(`, `.stringPropertyNames()` ;
- `ResourceBundle.getBundle(`, `.getLocale()`, `new MessageFormat(`, `.containsKey(`, `catch (MissingResourceException`, `.getKey()` ;
- `ResourceBundle.Control.getNoFallbackControl(`, `Locale.ROOT` ;
- `new Locale.Builder()`, `Locale.CANADA_FRENCH`, `Locale.forLanguageTag(`, `.toLanguageTag()`, `.getDisplayName(`, `catch (IllformedLocaleException` ;
- `Locale.Category.FORMAT`, `Locale.Category.DISPLAY`, `Locale.setDefault(Locale.GERMANY)` ;
- `catch (UnknownProductException | NumberFormatException`, `NumberFormat.getPercentInstance(`.

---

## Sortie attendue complète

```
reglages : [promo.item, shipping.free, shop.name], promo 10 %, stock null (pas une String !), get 42
[fr-FR -> bundle fr] Bienvenue chez Javashop, Marie ! | Votre panier : 5 article(s) pour 30,97_€. | Free shipping from 50,00_€ | Aujourd'hui : -10 % sur l'article stylo | Au revoir
[fr-CA -> bundle fr_CA] Bienvenue chez Javashop, mon ami Louis ! | Votre panier : 2 article(s) pour 60,89_$_CA. | Aujourd'hui : -10 % sur l'article stylo | Au revoir
[de-DE -> bundle de] Willkommen bei Javashop, Jonas! | Ihr Warenkorb: 3 Artikel fuer 64,48_€. | Auf Wiedersehen
[it-IT -> bundle de] Willkommen bei Javashop, Giulia! | Ihr Warenkorb: 1 Artikel fuer 1,99_€. | Free shipping from 50,00_€ | Auf Wiedersehen
[en -> bundle de] Willkommen bei Javashop, Sam! | Order rejected: produit inconnu : parapluie | Auf Wiedersehen
[en-US -> bundle de] Willkommen bei Javashop, Ann! | Order rejected: For input string: "x" | Auf Wiedersehen
racine : [bye, cart, error, legal, shipping, welcome] ; legal = Prices include VAT. Delivery within 3 days.
cle absente : MissingResourceException, cle nope
bundle absent : MissingResourceException
traduction : | fr-CA 4/6 (67_%) | fr 4/6 (67_%) | de 3/6 (50_%) | en 0/6 (0_%)
locales : fr_CA fr-CA egales true, français (Canada) / Französisch (Kanada), langue fr, pays CA
Builder : IllformedLocaleException
categories : 1_234,50_€ ; German (Germany) ; defaut de_DE, FORMAT fr_FR, DISPLAY en_US
```
