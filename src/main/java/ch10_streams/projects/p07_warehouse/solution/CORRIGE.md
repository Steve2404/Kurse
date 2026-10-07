# Projet 7 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Warehouse.java`](Warehouse.java).
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Le modèle

**Le code :** les enums et les records en tête de [`Warehouse.java`](Warehouse.java).

**Question — les deux usages de l'ordre des constantes :**
1. **le tri** : `Comparator.comparing(… tier())` compare les enums par leur **ordre de déclaration** (leur `ordinal()`). GOLD, déclaré en premier, passe donc avant STANDARD ;
2. **l'affichage** : une `EnumMap` range ses clés dans l'ordre de l'enum, d'où `{COMPLETE=…, PARTIELLE=…, REFUSEE=…}`.

**Le piège de l'email :** `"K2;Hugo;Sud;STANDARD;".split(";")` donne **4** morceaux, et `split(";", -1)` en donne **5** (vérifié). Une limite **négative** demande de garder les chaînes vides de la fin, et `p[4]` existe alors toujours.

---

## Étape 2 — Refus et file de priorité

**Le code :** le début de `run`.

**Associer et éliminer en une chaîne :** `customer(id).map(c -> new Result(…)).stream()` donne un stream de 0 ou 1 élément. `flatMap` fait disparaître les commandes sans client, sans `filter` + `get`. C'est la technique `Optional::stream` du projet 1.

**L'ordre :** Tom (GOLD) passe en premier, avec O8 (01/09), puis Lea (GOLD), avec O2 (02/09), puis Tom avec O4 (03/09). Viennent ensuite les STANDARD, par date puis par id.

---

## Étape 3 — L'allocation : l'algorithme

**Le code :** `allocate`, et la boucle de `run`.

**Les cas de la sortie :**
- **O4** (Tom, GOLD) demande 1 vélo (en stock) et 2 tapis (stock 0) : **PARTIELLE**, il manque B2x2.
- **O7** a une lampe manquante **et** un article inconnu (Z9) : **PARTIELLE**.
- **O9** demande un tapis, mais le stock est vide : rien n'est servi, donc **REFUSEE**.

**Question de conception — un `stream().map(...)` qui modifie le stock ?** **Non.** La Javadoc de `java.util.stream` demande des lambdas **sans effet de bord** (« non-interfering » et « stateless ») : leur résultat ne doit pas dépendre d'un état modifié pendant le traitement. Ici, le résultat de chaque commande **dépend du stock laissé par les précédentes**, et donc de l'ordre exact de passage. En parallèle, l'ordre et les accès concurrents au stock rendraient le résultat imprévisible. Une boucle `for` dit clairement « dans cet ordre, avec cet état ». Les streams reviennent **après**, pour analyser les résultats, qui sont immuables.

---

## Étape 4 — Avis et bons de livraison

**Le code :** les blocs « avis » et « bons » de `run`.

**`orElseGet`** construit le texte « par courrier » seulement si l'email manque (projet 1).

**Question — un compteur dans une lambda :** `n` serait une variable locale **modifiée** : interdit. Vérifié : `map(s -> "BL-" + ++n)` donne `error: local variables referenced from a lambda expression must be final or effectively final`. `IntStream.rangeClosed(1, n)` produit les numéros **sans état** : chaque numéro vient de sa position.

---

## Étape 5 — Les regroupements

**Le code :** le début de `report`.

**La fabrique d'`EnumMap` :** `groupingBy(clé, fabrique, aval)` attend un `Supplier<Map>`. `EnumMap` n'a pas de constructeur sans argument : il lui faut la **classe** de l'enum. Vérifié : `EnumMap::new` ne compile pas (`no suitable method found for groupingBy(…)`), d'où `() -> new EnumMap<>(Status.class)`.

**A RECOMMANDER :** `flatMap(r -> r.allocations().stream())` passe des commandes à leurs lignes servies, puis `groupingBy(sku, summingInt(missing))` cumule les manques. B2 manque 3 fois (2 pour O4, 1 pour O9).

**`collectingAndThen(summingLong(…), Warehouse::money)`** met directement le texte formaté dans la `Map`.

---

## Étape 6 — Statistiques

**Le code :** les lignes PANIERS, PANIER MOYEN et TOP CLIENTS.

**`LongSummaryStatistics`** donne le nombre, le min, le max, le total et la moyenne d'un `LongStream`, en un passage.

**`teeing`** calcule la moyenne et le max en un seul passage. La moyenne en centimes est un `double` (17358.57…), et `Math.round` la ramène à un nombre entier de centimes : **173.59**. Le résultat est rangé dans une variable `String` (le piège de `println` du projet 5).

---

## Étape 7 — Fin de journée et contrôle comptable

**Le code :** la fin de `report`.

**`TreeMap` pour le stock :** les skus sont triés sans effort pour RUPTURES et STOCK RESTANT.

**Le contrôle comptable :** valeur initiale = valeur expédiée + valeur restante. Si l'allocation retirait trop (ou pas assez) du stock, l'égalité casserait. C'est un test **d'invariant**, indépendant du détail des commandes.

**`reduce(0L, Long::sum)`** : l'identité `0L` est un `Long`. Avec `0` (un `int`), le type de l'identité ne correspondrait pas à celui des éléments (`Long`).
