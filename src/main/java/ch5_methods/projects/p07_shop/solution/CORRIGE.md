# Projet 7 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `model/Product.java`, `model/Catalog.java`, `util/Format.java`, `service/Basket.java` et `app/ShopApp.java`.
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Le modèle

**Le code :** [`model/Product.java`](model/Product.java), et le début de [`model/Catalog.java`](model/Catalog.java) (`add`, les deux `find`, `all`).

**Les niveaux d'accès :**
- les **getters** sont `public` : `app` et `service` lisent les produits ;
- **`adjust`, `sell` et `setPrice` n'ont pas de modificateur** : seul `Catalog`, du même paquet `model`, peut changer un stock ou un prix. `ShopApp` doit passer par `Catalog`. C'est la même idée que `Ledger` au projet 2.

**Les deux `find` :** `find(String)` et `find(int)` sont deux surcharges. `find("K01")` cherche par référence, et `find(3)` par position.

**Question — si `all()` rendait le tableau interne ?** L'appelant recevrait une **référence** vers le tableau privé du catalogue. Il pourrait alors écrire `catalog.all()[0] = null;` et casser le catalogue, ou y mettre n'importe quel produit, sans passer par `add`. `private` protège la **variable**, pas l'objet qu'on laisse sortir. La **copie défensive** (`Arrays.copyOf`) rend un **autre** tableau : le modifier ne touche pas l'original. L'étape 3 le vérifie.

---

## Étape 2 — Les commandes

**Le code :** la méthode `execute` de [`ShopApp`](app/ShopApp.java), et `sell`, `restock` et les deux `discount` de `Catalog`.

**Le varargs `restock` :**
- `STOCK W06 2 3` passe `new int[] {2, 3}` : le stock passe de 0 à 5, d'où `+5`.
- `STOCK C04` passe un tableau **vide**, d'où `+0 (0 livraison(s))`.
- Un `int[]` passé à `int...` est utilisé **tel quel** (projet 1).

**`REMISE H05 25` :** 7990 × 25 / 100.0 = 1997.5, arrondi à 1998. Le nouveau prix vaut 7990 − 1998 = 5992, soit **59.92**.

**Question — `discount("T07", 90)` ?** `90` est un **`int`** : `javac` choisit `discount(String, int)`, la correspondance exacte, en phase 1. C'est donc une remise de **90 %**, pas de 90 centimes. Vérifié sur 12.90 : `discount("T07", 90)` donne **129** (1.29), alors que `discount("T07", 90L)` donne **1200** (12.00). Le suffixe `L` ou le type de la variable change la méthode appelée. C'est pour ça que `ShopApp` lit `90c` avec `Long.parseLong`.

---

## Étape 3 — L'inventaire

**Le code :** [`util/Format.java`](util/Format.java) et la boucle d'inventaire du `main`.

**Les trois `pad` :**
- `pad(String, int)` aligne à gauche ;
- `pad(long, int)` aligne à droite ;
- `pad(String)` **délègue** à `pad(s, WIDTH)`.

Une surcharge qui en appelle une autre évite de dupliquer le code.

**Pourquoi `pad(p.getStock(), 5)` choisit `pad(long, int)` :** `getStock()` rend un `int`. Il n'existe pas de `pad(int, int)` : en phase 1, l'`int` s'**élargit** en `long`. `pad(String, int)` ne convient pas, car un `int` ne devient jamais un `String`.

**Les imports static :** `import static …util.Format.money;` et `…Format.pad;` importent **toutes** les surcharges de ce nom.

**La copie défensive vérifiée :** `copy[0] = null` ne touche que la copie, donc `catalog.find(1)` trouve toujours `K01`, d'où `true`.

---

## Étape 4 — Le meilleur panier

**Le code :** [`service/Basket.java`](service/Basket.java).

**Le retour arrière :** chaque produit ouvre au plus deux branches, « je prends » et « je ne prends pas ». `chosen` est **un seul** tableau, partagé par tous les appels (même référence). On le remet à `false` après la branche « je prends », pour que la suite reparte d'un état propre. Quand une sélection bat le record, on la **recopie** dans `best`, sinon elle serait écrasée par les essais suivants.

**La case n de `best` :** `best` a n + 1 cases. Les cases 0 à n − 1 gardent la sélection, la case n garde le **montant** du record. Ainsi, une seule référence suffit à transporter les deux informations entre les appels (on n'a pas encore les records du chapitre 7).

**Question — combien de nœuds sans élagage ?** Un arbre binaire complet de profondeur 7 (7 produits) a **2⁸ − 1 = 255** nœuds. L'élagage n'en explore que **65**. Il coupe toute la branche « je prends » dès qu'un produit est trop cher pour le budget restant, ou en rupture : l'Écran à 189.90 n'est **jamais** pris pour un budget de 150.00, et la Webcam n'est prise que si elle a du stock.
