# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Les modules de la correction sont dans `ch12_modules/p02_pricing/solution/`, et le script dans [`build.sh`](build.sh).
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur une copie de la solution. Chaque message est traduit et expliqué.

---

## Étape 1 — L'API et le localisateur

**Les `module-info.java` :**

```java
module pricing.api {
    exports pricing.api;
}

module pricing.engine {
    requires transitive pricing.api;     // l'API de PricingEngine montre des PricingRule
    exports pricing.engine;
    uses pricing.api.PricingRule;        // je cherche des fournisseurs de ce service
}
```

**Le localisateur** (`PricingEngine`) :

```java
static List<PricingRule> loadRules() {
    return ServiceLoader.load(PricingRule.class).stream()
            .map(ServiceLoader.Provider::get)                      // crée chaque fournisseur
            .sorted(Comparator.comparing(PricingRule::name))      // l'ordre n'est pas garanti : on trie
            .toList();
}
```

**Les quatre rôles d'un service :**

| Rôle | Ici | Ce qu'il déclare |
|---|---|---|
| interface de service | `pricing.api` | `exports` de l'interface |
| localisateur | `pricing.engine` | `uses` + `ServiceLoader` |
| fournisseurs | `pricing.basic`, `pricing.premium` | `provides … with …` |
| consommateur | `shop.app` | `requires` du localisateur, et rien d'autre |

---

## Étape 2 — Les fournisseurs et le consommateur

```java
module pricing.basic {
    requires pricing.api;
    provides pricing.api.PricingRule with pricing.basic.TenPercent, pricing.basic.ThreeForTwo;
}

module pricing.premium {
    requires pricing.api;
    provides pricing.api.PricingRule with pricing.premium.Coupons;
}

module shop.app {
    requires pricing.engine;     // il ne connaît aucun fournisseur
}
```

**Question — pourquoi `PricingRule` pour le coupon ?** `Provider.type()` rend le type de l'objet que le fournisseur **annonce**. Pour une classe créée par son constructeur, c'est la classe elle-même (`TenPercent`). Pour une classe qui a une méthode `public static provider()`, c'est le **type rendu par cette méthode**. Ici, `provider()` rend une `PricingRule`, d'où `PricingRule`, et non `Coupons`.

**Question — que faut-il à `TenPercent` ?** Vérifié en cassant la solution :
- la classe doit être **`public`**. Sinon : `error: TenPercent is not public in pricing.basic` ;
- elle doit avoir un constructeur **`public` sans argument**. Sinon : `error: the no arguments constructor of the service implementation is not public: TenPercent` (« le constructeur sans argument de l'implémentation du service n'est pas public ») ;
- **ou bien**, comme `Coupons`, une méthode `public static provider()`, et alors le constructeur peut rester privé.

---

## Étape 3 — Le script `build.sh`

**Le script :** [`build.sh`](build.sh).

**Expérience 1 — retirer `uses` :** la compilation **réussit**. L'erreur arrive à l'**exécution**, au premier `ServiceLoader.load` :

```
Exception in thread "main" java.util.ServiceConfigurationError: pricing.api.PricingRule: module pricing.engine does not declare `uses`
```

Traduction : « le module `pricing.engine` ne déclare pas `uses` ». `javac` ne vérifie pas les appels à `ServiceLoader` : c'est la JVM qui contrôle.

**Expérience 2 — `--limit-modules shop.app` :** aucun fournisseur n'est observable. Vérifié : la 1re ligne devient `regles : [] ; types []`, et chaque panier `-> [] remise 0`. `best` parcourt zéro règle et rend une remise nulle.

**Expérience 3 — retirer `pricing.premium` de `-m` :** il n'est **pas** compilé (vérifié : le dossier de sortie n'a que 4 modules). La 1re ligne perd `coupon-15` : `regles : [3pour2+, soldes-10] ; types [TenPercent, ThreeForTwo]`. Contrairement à `library.model` au projet 1, personne ne le **requiert** : `javac` n'a aucune raison d'aller le chercher.

**Les lignes `binds` :** `--show-module-resolution` montre que `pricing.engine` (qui `uses` le service) **lie** (`binds`) chaque module qui le fournit.
