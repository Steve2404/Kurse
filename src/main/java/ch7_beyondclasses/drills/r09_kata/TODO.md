# Drill de rappel 9 — Kata mixte chronométré (tout le chapitre 7)

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 18 min, **sans carte**. C'est le test final de chaque cycle.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall09.java`, paquet `ch7_beyondclasses.drills.r09_kata`. Sous `Recall09` :

| Type | Contenu |
|---|---|
| `interface Priced` | `long price();` ; `long tax();` ; `default long total()` rend `price() + tax()` ; `static String currency()` rend `EUR` |
| `enum Tax` | `ZERO(0), REDUCED(55), NORMAL(200)` (pour mille) ; `long apply(long price)` rend `price * perMille / 1000` |
| `record Item(String name, long price, Tax rate) implements Priced` | un constructeur compact qui met le nom en majuscules ; `tax()` rend `rate.apply(price)` |
| `interface Discount` | `long apply(long price);` ; une constante `Discount NONE = new Discount() { … }` (anonyme, qui rend le prix inchangé) |
| `sealed interface Payment permits Card, Cash` | `default String describe()` rend `getClass().getSimpleName()` |
| `record Card(String last4) implements Payment` | rien d'autre |
| `record Cash(long amount) implements Payment` | `describe()` rend `"especes " + amount` |
| `class Cart` | `private int lines` ; une classe **interne** `Line(Item item, int qty)` qui incrémente `lines` et dont `subtotal()` rend `item.total() * qty` ; un **record imbriqué** `Totals(int count, long sum)` avec `avg()` = `sum / count` ; `label()` rend `"panier(" + lines + ")"` |

**Les notions de ce drill ont été apprises dans :** projets 1 à 7 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r09_kata` → **New** → **Java Class** → `Recall09`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall09`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall09`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `book = new Item("livre", 1200, Tax.REDUCED)` et `phone = new Item("telephone", 50000, Tax.NORMAL)`. Affiche book, `book.total()` et `phone.total()`.
  → `D01 : Item[name=LIVRE, price=1200, rate=REDUCED] 1266 60000`
- ☐ **D02.** `Tax.valueOf("ZERO").apply(1000)`, `NORMAL.ordinal()`, `Tax.values().length` et `Priced.currency()`.
  → `D02 : 0 2 3 EUR`
- ☐ **D03.** Une anonyme `promo` qui retire 10 % (`price - price / 10`). Affiche `promo.apply(phone.total())`, `Discount.NONE.apply(500)`, puis `new Cart().label()`.
  → `D03 : 54000 500 panier(0)`
- ☐ **D04.** `Payment pay = new Card("1234");`. Avec `instanceof Card c`, affiche `carte ` + `c.last4()` (sinon `autre`). Puis `new Cash(500).describe()` et `Payment.class.isSealed()`.
  → `D04 : carte 1234 especes 500 true`
- ☐ **D05.** `Cart cart = new Cart(); Cart.Line line = cart.new Line(book, 3);`. Affiche :
  - `line.subtotal()` ;
  - `new Cart.Totals(2, 100).avg()` ;
  - `book.equals(new Item("livre", 1200, Tax.REDUCED))`.
  → `D05 : 3798 50 true`

## Sortie attendue complète

```
D01 : Item[name=LIVRE, price=1200, rate=REDUCED] 1266 60000
D02 : 0 2 3 EUR
D03 : 54000 500 panier(0)
D04 : carte 1234 especes 500 true
D05 : 3798 50 true
```

## Après le kata

Pour chaque ✗, refais le drill thématique :

| Défi | Drill |
|---|---|
| D01 | r06, r01 |
| D02 | r03, r04 |
| D03 | r07, r02 |
| D04 | r05, r08 |
| D05 | r07, r06 |
