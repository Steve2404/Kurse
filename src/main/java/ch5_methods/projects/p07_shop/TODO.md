# Projet 7 (CAPSTONE) — La boutique

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Notions visées :** tout le chapitre 5 à la fois :
- des **paquets** et des niveaux d'accès choisis au plus juste ;
- l'encapsulation et des **fabriques `static`** ;
- des constantes `static final` et des compteurs `static` ;
- les **varargs** (dont passer un `int[]` construit à la main) ;
- les **surcharges** (`find` par référence ou par position, `discount` en pourcentage `int` ou en montant `long`, `pad` pour `String` ou `long`) ;
- l'**import static** ;
- une **copie défensive** (le passage de référence) ;
- un **retour arrière** récursif pour le meilleur panier.

**Ce qui est donné :** `Data.java` (produits, commandes, budget) et `Check.java`.

**Ce que TU crées :** une application dans des sous-paquets de `ch5_methods.projects.p07_shop` :

| Paquet | Classes | Rôle |
|---|---|---|
| `util` | `Format` | `money(long)`, `pad(String, int)`, `pad(long, int)`, `pad(String)` |
| `model` | `Product`, `Catalog` | les données ; seul `Catalog` modifie un `Product` |
| `service` | `Basket` | le meilleur panier |
| `app` | `ShopApp` | le `main` (`app.ShopApp`) |

**Règle du crescendo :** chapitres 1 à 5. Pas de constructeur écrit par toi, pas d'héritage, pas de collection, pas de `try/catch`.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle

- **`Product`** :
  - `public static final int MAX_STOCK = 99` ;
  - `private static int created` ;
  - des champs `private` : référence, nom, prix (centimes), stock, vendus ;
  - la fabrique `public static Product of(…)`, qui plafonne le stock à `MAX_STOCK` ;
  - des getters publics.
  - **Package-private** : `int adjust(int delta)` (le stock reste entre 0 et `MAX_STOCK` ; elle rend la variation réelle), `void sell(int)`, `void setPrice(long)`.
- **`Catalog`** :
  - un `private Product[]` qui double de taille (`Arrays.copyOf`) ;
  - `public void add(Product... items)` ;
  - deux surcharges de `find` : `Product find(String sku)` et `Product find(int position)` (à partir de 1, `null` si hors limites) ;
  - `public Product[] all()` rend une **copie**.
  - **Question :** que pourrait faire l'appelant si `all()` rendait le tableau interne ?

### ☐ Étape 2 — Les commandes

```
VENTE K01 2       | ok
VENTE E03 3       | rupture (2 en stock)
STOCK W06 2 3     | +5 (2 livraison(s))
VENTE W06 4       | ok
VENTE X99 1       | inconnu
STOCK C04         | +0 (0 livraison(s))
REMISE H05 25     | nouveau prix 59.92
VENTE H05 3       | ok
INFO 3            | E03 Ecran 189.90
INFO 9            | aucun produit a cette position
VENTE K01 3       | ok
STOCK E03 1 1 1 1 | +4 (4 livraison(s))
VENTE S02 10      | ok
REMISE T07 90c    | nouveau prix 12.00
```
- **`String sell(String sku, int qty)`** rend `ok`, `inconnu` ou `rupture (n en stock)`, et cumule le chiffre d'affaires.
- **`int restock(String sku, int... deliveries)`** : zéro, une ou plusieurs livraisons. Elle rend la quantité réellement ajoutée.
  - Dans `ShopApp`, construis un `int[]` à partir des mots de la commande, puis passe-le au varargs.
- **Deux surcharges de `discount`** :
  - `long discount(String sku, int percent)` : arrondi avec `Math.round(prix * percent / 100.0)` ;
  - `long discount(String sku, long cents)` : un montant fixe, plancher à 0.
  - La commande `90c` (suffixe `c`) appelle la version `long`.
  - **Question :** que se passerait-il si on appelait `discount("T07", 90)` ?
- Chaque ligne affiche la commande sur 18 caractères (`pad(order, 18)`), puis `| ` et le résultat.

### ☐ Étape 3 — L'inventaire

```
REF      PRODUIT  PRIX     STOCK  VENDUS
K01      Clavier  49.90        0      5  <- a commander
...
chiffre d'affaires 847.86, produits crees 7, copie defensive intacte true
```
- **`Format`** : `WIDTH = 9`.
  - `pad(String)` délègue à `pad(String, WIDTH)` : un texte est aligné à **gauche** ;
  - `pad(long, int)` aligne un nombre à **droite**.
  - `money` vient de l'import static, comme `pad`.
- **Une ligne de produit** :
  - `pad(ref)`, `pad(nom)`, `pad(money(prix), 9)` ;
  - puis le stock sur 5, deux espaces, les vendus sur 5 ;
  - puis `  <- a commander` si le stock est inférieur à 3.
  - La ligne d'en-tête se termine par `VENDUS`, **sans** espace final.
- **La copie défensive :** mets la case 0 d'une copie à `null`, puis vérifie que `catalog.find(1)` existe toujours.

### ☐ Étape 4 — Le meilleur panier

```
meilleur panier pour 150.00 : Souris Casque Webcam Tapis = 146.72 (65 noeuds explores)
```
- **`public static boolean[] best(long budget, Product... candidates)`**, dans `service.Basket` : dépenser **le plus possible** sans dépasser le budget, avec **un exemplaire** par produit.
- **Le retour arrière** `explore(p, i, budget, spent, chosen, best)` :
  1. compte le nœud ;
  2. si `spent` bat le record, recopie la sélection courante dans `best` (la case n de `best` contient le record) ;
  3. branche « je prends » **seulement si** le produit est en stock et rentre dans le budget (élagage) ;
  4. puis la branche « je ne prends pas ».
- Le compteur `explored` est `private static`, lu par `public static int explored()`.
- **Question :** combien de nœuds sans l'élagage ? (2⁸ − 1 = 255)

---

## Checklist (vérifiée par `Check`)

- `Data.PRODUCTS`, `Data.ORDERS` et `Data.BUDGET` ;
- les paquets `model` et `app` ;
- `import static` ;
- `Product...` ;
- deux `Product find(` et deux `long discount(` ;
- `int restock(String`, un `int...` ;
- une méthode package-private ;
- `Arrays.copyOf`, `static final int`, `private static int`.

---

## Sortie attendue complète

```
VENTE K01 2       | ok
VENTE E03 3       | rupture (2 en stock)
STOCK W06 2 3     | +5 (2 livraison(s))
VENTE W06 4       | ok
VENTE X99 1       | inconnu
STOCK C04         | +0 (0 livraison(s))
REMISE H05 25     | nouveau prix 59.92
VENTE H05 3       | ok
INFO 3            | E03 Ecran 189.90
INFO 9            | aucun produit a cette position
VENTE K01 3       | ok
STOCK E03 1 1 1 1 | +4 (4 livraison(s))
VENTE S02 10      | ok
REMISE T07 90c    | nouveau prix 12.00
REF      PRODUIT  PRIX     STOCK  VENDUS
K01      Clavier  49.90        0      5  <- a commander
S02      Souris   19.90        2     10  <- a commander
E03      Ecran    189.90       6      0
C04      Cable    7.90        30      0
H05      Casque   59.92        1      3  <- a commander
W06      Webcam   54.90        1      4  <- a commander
T07      Tapis    12.00        7      0
chiffre d'affaires 847.86, produits crees 7, copie defensive intacte true
meilleur panier pour 150.00 : Souris Casque Webcam Tapis = 146.72 (65 noeuds explores)
```
