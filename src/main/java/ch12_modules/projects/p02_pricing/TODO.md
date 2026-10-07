# Projet 2 — La caisse à remises (services)

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 12) :** les **services**, avec leurs quatre rôles :
- l'**interface de service** (*service provider interface*) ;
- le **localisateur**, avec `uses` et `ServiceLoader` ;
- les **fournisseurs**, avec `provides … with …`, dont un par **méthode `provider()`** ;
- le **consommateur**.

Et aussi :
- `ServiceLoader.load(…).stream()`, `ServiceLoader.Provider`, `get()` et `type()` ;
- `--limit-modules`, qui retire des modules **observables** au lancement ;
- `--describe-module` d'un fournisseur (`provides`, `contains`) ;
- `--show-module-resolution` et les liens `binds`.

Côté algorithme : la **meilleure combinaison de remises**. Toutes les combinaisons sont parcourues avec un **masque de bits** sur n règles ; une combinaison de 2 règles ou plus n'est valable que si elles sont toutes cumulables.

**Ce que TU crées :**
- **tes 6 modules**, dans `ch12_modules/p02_pricing/src/` : `pricing.api`, `pricing.engine`, `pricing.basic`, `pricing.premium` et `shop.app` ;
- **ton script** `build.sh`, dans ce dossier.

**Règle du crescendo :** chapitres 1 à 12.

**Tes outils pour ce projet :** tes modules dans `ch12_modules/p02_pricing/src/`, ton script `build.sh` à côté de ce `TODO.md`. Pour créer les dossiers et lancer le script : projet 1, en-tête.

```
& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch12_modules/projects/p02_pricing/build.sh
```

---

## Tableau de bord

### ☐ Étape 1 — L'API et le localisateur

**📖 La leçon : un service, des modules qui ne se connaissent pas.** Un **service** permet à un programme d'utiliser des implémentations qu'il ne connaît **pas** à l'avance : des « plugins ». Il y a quatre rôles :
1. **l'interface de service** : le contrat (une interface Java, exportée) ;
2. **le localisateur** : il déclare `uses Interface;` et cherche les implémentations avec `ServiceLoader` ;
3. **les fournisseurs** : ils déclarent `provides Interface with Implementation;`. Ils n'exportent rien ;
4. **le consommateur** : il utilise le localisateur.

```java
// le localisateur
ServiceLoader<Saveur> chargeur = ServiceLoader.load(Saveur.class);
for (Saveur s : chargeur) { … }                        // crée et parcourt chaque fournisseur trouvé
chargeur.stream().map(ServiceLoader.Provider::get)     // la même chose, en stream
chargeur.stream().map(ServiceLoader.Provider::type)    // la CLASSE de chaque fournisseur, sans le créer
```

**📖 Rappel :** un masque de bits pour énumérer toutes les combinaisons (chapitre 2, projet 2, et chapitre 5, projet 7).

**👉 À toi :**

- **`pricing.api`** : exporte `pricing.api`, qui contient l'interface `PricingRule` :
  - `String name()` ;
  - `long discount(List<Long> prices)` ;
  - `boolean stackable()`.
- **`pricing.engine`** : `requires transitive pricing.api`, exporte `pricing.engine`, et **`uses pricing.api.PricingRule`**.
- **`final class PricingEngine`** :
  - `static List<PricingRule> loadRules()` : `ServiceLoader.load(PricingRule.class).stream().map(ServiceLoader.Provider::get)`, trié par nom (l'ordre des fournisseurs n'est **pas garanti**) ;
  - `static List<String> providerTypes()` : le nom simple de `p.type()` pour chaque fournisseur, trié, **sans** les instancier ;
  - `record Choice(List<String> rules, long discount)` ;
  - `static Choice best(List<PricingRule> rules, List<Long> prices)` :
    - pour `mask` de 1 à `(1 << n) - 1`, prends les règles `i` telles que `(mask & 1 << i) != 0` ;
    - si 2 règles ou plus, et qu'une n'est pas cumulable : ignore ;
    - la remise est la somme des `discount` ;
    - garde la plus forte, et à égalité la combinaison la plus courte.

### ☐ Étape 2 — Les fournisseurs et le consommateur

**📖 La leçon : écrire un fournisseur.** Une classe `public` qui réalise l'interface, avec **soit** un constructeur `public` sans argument, **soit** une méthode `public static provider()` qui rend l'objet. Le module la déclare :

```java
module cuisine.epices {
    requires cuisine.api;
    provides cuisine.api.Saveur with cuisine.epices.Poivre, cuisine.epices.Cannelle;
}
```

**👉 À toi :**

| Module | `module-info.java` | Contenu |
|---|---|---|
| `pricing.basic` | `requires pricing.api` ; `provides pricing.api.PricingRule with pricing.basic.TenPercent, pricing.basic.ThreeForTwo` ; **n'exporte rien** | `TenPercent` : `soldes-10`, 10 % du total (division entière), non cumulable. `ThreeForTwo` : `3pour2`, prix triés du plus cher au moins cher, chaque 3e article offert, cumulable |
| `pricing.premium` | `requires pricing.api` ; `provides pricing.api.PricingRule with pricing.premium.Coupons` | `final class Coupons`, avec un constructeur privé et `public static PricingRule provider()`. Elle rend `coupon-15` : 1 500 de remise si le total ≥ 10 000, cumulable |
| `shop.app` | `requires pricing.engine` **seulement** | `shop.app.Main` |

- **`Main`**, avec les paniers :
  ```java
  static final List<List<Long>> CARTS = List.of(List.of(2_000L, 1_500L, 1_000L), List.of(4_000L, 3_000L, 2_000L, 1_500L, 500L),
          List.of(9_000L, 2_500L), List.of(800L));
  ```
  - **1re ligne :** `regles : [<nom>, suivi de + si cumulable>] ; types <providerTypes()>` ;
  - **puis, par panier :** `panier <panier> total <t> -> <règles> remise <r>, a payer <t - r>`.
- **Questions :**
  - Pourquoi `providerTypes()` donne-t-il `PricingRule` pour le coupon, et pas `Coupons` ?
  - Que faut-il à `TenPercent` pour être un fournisseur valable (classe, constructeur) ?

### ☐ Étape 3 — Le script `build.sh`

```
--- sans pricing.premium
regles : [3pour2+, soldes-10] ; types [TenPercent, ThreeForTwo]
...
--- resolution
pricing.engine binds pricing.basic
pricing.engine binds pricing.premium
```

**📖 La leçon : choisir les modules visibles au lancement.**
- `--limit-modules m1,m2` : seuls ces modules (et ceux qu'ils requièrent) restent visibles ;
- `--show-module-resolution` : affiche comment la JVM a trouvé chaque module, et les liens `binds` (un localisateur relié à un fournisseur).

**👉 À toi :**

- **En tête :** `P=ch12_modules/p02_pricing` et `OUT=build/ch12/p02_pricing`, `set -e`, puis `rm -rf "$OUT"`.
- **Les commandes :**
  1. `javac -d "$OUT/mods" --module-source-path "$P/src" -m shop.app,pricing.basic,pricing.premium` (les fournisseurs ne sont requis par personne : il faut les nommer) ;
  2. `echo "--- tous les fournisseurs"`, puis `java -p "$OUT/mods" -m shop.app/shop.app.Main` ;
  3. `echo "--- sans pricing.premium"`, puis la même commande avec `--limit-modules shop.app,pricing.basic` ;
  4. `echo "--- describe-module pricing.premium"`, puis `--describe-module pricing.premium | sed 's/ file:.*//' | sort` ;
  5. `echo "--- resolution"`, puis `java -p … --show-module-resolution -m shop.app/shop.app.Main`, filtré pour ne garder que **nos** liens :
     ```bash
     | grep -E "^(pricing|shop)[^ ]* binds " | sed 's/ file:.*//' | sort
     ```
- **Expériences :**
  - retire `uses` de `pricing.engine` : compilation, ou exécution ? Quelle erreur ?
  - lance avec `--limit-modules shop.app` : que rend `best` ?
  - retire `pricing.premium` de `-m` à l'étape 1 : que devient la première ligne ?

---

## Checklist (vérifiée par `Check`)

- **Dans les `module-info`** : `module pricing.api`, `requires transitive pricing.api;`, `uses pricing.api.PricingRule;`, les deux directives `provides …` (exactement comme dans le tableau), `requires pricing.engine;`.
- **Dans le Java** : `public static PricingRule provider()`, `ServiceLoader.load(`, `.stream()`, `ServiceLoader.Provider`, `.type()`, `record Choice(`, `1 << `.
- **Dans le script** : `-m shop.app,pricing.basic,pricing.premium`, `--limit-modules shop.app,pricing.basic`, `--describe-module pricing.premium`, `--show-module-resolution`, ` binds `.

---

## Sortie attendue complète

```
--- tous les fournisseurs
regles : [3pour2+, coupon-15+, soldes-10] ; types [PricingRule, TenPercent, ThreeForTwo]
panier [2000, 1500, 1000] total 4500 -> [3pour2] remise 1000, a payer 3500
panier [4000, 3000, 2000, 1500, 500] total 11000 -> [3pour2, coupon-15] remise 3500, a payer 7500
panier [9000, 2500] total 11500 -> [coupon-15] remise 1500, a payer 10000
panier [800] total 800 -> [soldes-10] remise 80, a payer 720
--- sans pricing.premium
regles : [3pour2+, soldes-10] ; types [TenPercent, ThreeForTwo]
panier [2000, 1500, 1000] total 4500 -> [3pour2] remise 1000, a payer 3500
panier [4000, 3000, 2000, 1500, 500] total 11000 -> [3pour2] remise 2000, a payer 9000
panier [9000, 2500] total 11500 -> [soldes-10] remise 1150, a payer 10350
panier [800] total 800 -> [soldes-10] remise 80, a payer 720
--- describe-module pricing.premium
contains pricing.premium
pricing.premium
provides pricing.api.PricingRule with pricing.premium.Coupons
requires java.base mandated
requires pricing.api
--- resolution
pricing.engine binds pricing.basic
pricing.engine binds pricing.premium
```
