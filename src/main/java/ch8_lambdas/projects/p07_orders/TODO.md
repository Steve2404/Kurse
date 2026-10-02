# Projet 7 (CAPSTONE) — Le moteur de commandes

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Notions visées :** tout le chapitre 8 :
- les règles métier sont des **fonctions assemblées à partir des données** ;
- les validations : **`Validation extends Predicate<Purchase>`**, créées par un `switch` qui rend des lambdas ;
- le **currying** : `Function<Integer, LongUnaryOperator> PERCENT_OFF = percent -> amount -> …` ;
- les interfaces **primitives `long`** : `LongUnaryOperator`, `LongPredicate`, `LongSupplier`, `ToLongFunction`, et `LongUnaryOperator.identity()` ;
- **`andThen` ou `compose`** : l'ordre des remises change le prix ;
- les références **`promos[i]::apply`** et **`audit::append`** (sur des objets précis) ;
- des **`Consumer` chaînés** (`andThen`) pour les notifications ;
- une **livraison paresseuse** (`LongSupplier`, calculé seulement si nécessaire) ;
- `BiFunction`, `ToIntFunction`.

Côté algorithmes :
- choisir la **meilleure combinaison** de codes promo (un seul code, ou deux dans les deux ordres) ;
- la tarification par paliers ;
- le poids arrondi au kilo supérieur.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch8_lambdas.projects.p07_orders` :
- `Purchase`, `Promo`, `Validation` ;
- **`Engine`** (le `main`).

**Règle du crescendo :** chapitres 1 à 8. Pas de collection ni de stream. Les montants sont en centimes.

---

## Tableau de bord

### ☐ Étape 1 — Les données

- **`record Purchase(String id, String customer, String tier, String[] skus, int[] quantities)`** :
  - un constructeur compact qui copie les tableaux ;
  - `parse("P1 alice gold BOOK:2 PEN:5")` ;
  - `lineCount()`, `sku(i)`, `quantity(i)`.
  - **Attention :** ne nomme pas la méthode `lines()`, qui est un jeton interdit (`String.lines()` est un stream).
- **`record Promo(String code, LongPredicate eligible, LongUnaryOperator effect)`** :
  - `parse("MINUS5 fixed 500 3000")` :
    - `fixed` → `amount -> Math.max(0, amount - valeur)` ;
    - `percent` → `amount -> amount - Math.round(amount * valeur / 100.0)` ;
    - `eligible` → `amount -> amount >= minimum`.
  - `long apply(long amount)` n'applique l'effet que si la promo est éligible **à ce moment-là**.
- **`@FunctionalInterface interface Validation extends Predicate<Purchase> {}`**.

### ☐ Étape 2 — Le moteur

```
P1 alice : 44.30 -gold-> 37.65 -[TEN puis MINUS5]-> 28.88 + port 5.90 = 34.78
P3 chloe : REJETEE (not-empty)
P6 fred : 26.70 -none-> 26.70 -[TEN]-> 24.03 + port 6.90 = 30.93
...
```
- **`Engine(String[] catalog, Consumer<String> notify)`** : des tableaux parallèles `skus`, `prices` et `weights`, et `index(sku)`.
- **`Validation rule(String text)`**, un `switch` :
  - `not-empty` → au moins une ligne ;
  - `known-skus` → toutes les références existent ;
  - `max-qty N` → un bloc qui déclare une `ToIntFunction<Purchase>` pour la quantité totale, puis `yield o -> total.applyAsInt(o) <= max`.
- **`static Validation[] rules(Engine e, String[] texts)`**.
- **Le currying :**
  - `static final Function<Integer, LongUnaryOperator> PERCENT_OFF = percent -> amount -> amount - Math.round(amount * percent / 100.0)` ;
  - `tierDiscount(tier)` : gold → `PERCENT_OFF.apply(15)`, silver → 10, bronze → 5, sinon `LongUnaryOperator.identity()`.
- **`static String bestPromos(long amount, Promo[] promos, long[] result)`** :
  1. pour chaque code i, essaie-le seul ;
  2. **si il a fait baisser le prix**, essaie chaque autre code j après lui : `LongUnaryOperator first = promos[i]::apply;` puis `first.andThen(promos[j]::apply)`. La paire ne compte que si elle bat le code seul ;
  3. garde le meilleur prix (strictement), et l'étiquette `CODE` ou `CODE1 puis CODE2` (sinon `aucun`) ;
  4. le prix va dans `result[0]`.
- **`process(Purchase o, Validation[] rules, String[] ruleNames, Promo[] promos)`** :
  1. la première règle qui échoue : notifie `id rejetee (règle)` et rends `id client : REJETEE (règle)` ;
  2. sinon :
     - `BiFunction<String, Integer, Long> lineTotal` et `ToLongFunction<Purchase> subtotal` donnent le brut ;
     - applique la remise de niveau, puis les meilleurs codes ;
     - la **livraison** est un `LongSupplier`, qui ne calcule le poids que si on l'appelle : 490 + 100 par kilo **commencé**. Elle est **gratuite** si le prix après codes est ≥ 5000 ;
     - notifie `id acceptee total` ;
     - rends la ligne `id client : brut -niveau-> après niveau -[codes]-> après codes + port P = total`.
- `static String money(long)` écrit `44.30`.

### ☐ Étape 3 — Le programme

```
acceptees 4/7 | journal : P1 acceptee 34.78 ; P2 acceptee 45.59 ; ... ; P7 acceptee 88.45 ;
currying : or(10000) 8500, -10% puis -5.00 8500, -5.00 puis -10% 8550, identite 10000
```
- **Les notifications** :
  - `Consumer<String> log = audit::append` ;
  - `separator`, qui ajoute ` ; ` ;
  - `counter`, qui compte les messages contenant `acceptee` ;
  - le moteur reçoit `log.andThen(separator).andThen(counter)`.
- Crée les règles et les promos, puis traite chaque commande de `Data.PURCHASES`.
- Affiche `acceptees N/7 | journal : ` + le journal, sans espaces de bord.
- **La ligne currying :**
  - `tierDiscount("gold")` appliqué à 10000 ;
  - `ten.andThen(minus500)` et `ten.compose(minus500)` appliqués à 10000, avec `ten = PERCENT_OFF.apply(10)` et `minus500 = amount -> amount - 500` ;
  - `tierDiscount("none")` appliqué à 10000.
- **Questions :**
  - pourquoi `-10% puis -5.00` n'est-il pas égal à `-5.00 puis -10%` ?
  - à quoi sert la condition « le premier code a agi » dans `bestPromos` ? (Regarde P6.)

---

## Checklist (vérifiée par `Check`)

- `Data.CATALOG`, `Data.PURCHASES`, `Data.PROMOS` ;
- `Function<Integer, LongUnaryOperator>`, `percent -> amount ->`, `LongUnaryOperator.identity()` ;
- `LongPredicate`, `LongSupplier`, `ToLongFunction<Purchase>`, `BiFunction<String, Integer, Long>` ;
- `interface Validation extends Predicate<Purchase>` ;
- `promos[i]::apply`, `audit::append`, `andThen`, `compose`.

---

## Sortie attendue complète

```
P1 alice : 44.30 -gold-> 37.65 -[TEN puis MINUS5]-> 28.88 + port 5.90 = 34.78
P2 bob : 52.70 -silver-> 47.43 -[TEN puis MINUS5]-> 37.69 + port 7.90 = 45.59
P3 chloe : REJETEE (not-empty)
P4 dan : REJETEE (max-qty 30)
P5 eve : REJETEE (known-skus)
P6 fred : 26.70 -none-> 26.70 -[TEN]-> 24.03 + port 6.90 = 30.93
P7 gina : 136.50 -silver-> 122.85 -[TEN puis BIG]-> 88.45 + port 0.00 = 88.45
acceptees 4/7 | journal : P1 acceptee 34.78 ; P2 acceptee 45.59 ; P3 rejetee (not-empty) ; P4 rejetee (max-qty 30) ; P5 rejetee (known-skus) ; P6 acceptee 30.93 ; P7 acceptee 88.45 ;
currying : or(10000) 8500, -10% puis -5.00 8500, -5.00 puis -10% 8550, identite 10000
```
