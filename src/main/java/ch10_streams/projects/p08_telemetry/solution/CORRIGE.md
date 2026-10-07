# Projet 8 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Telemetry.java`](Telemetry.java).
>
> Les messages, les exceptions et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Le modèle et les mesures

**Le code :** les records `Sample` et `Rule`, la table `MEASURES` et les fonctions nommées.

**Question — pourquoi les octets doivent être un `long` ?** La plus grande valeur, **3 900 000 000**, dépasse `Integer.MAX_VALUE` (2 147 483 647). Elle ne tiendrait pas dans un `int`.

**`ToDoubleFunction<Sample>`** : `applyAsDouble` rend un `double` primitif, sans emballer chaque valeur dans un `Double`. `Sample::memMb` (un `int`) et `Sample::bytes` (un `long`) conviennent aussi : le résultat s'**élargit** en `double`.

---

## Étape 2 — Vue d'ensemble

**Le code :** `overview`.

**Question — `toArray()` sans argument :** il rend un **`Object[]`**. Le caster en `String[]` échoue à l'exécution, car le tableau **est** un `Object[]`, pas un `String[]` qui contiendrait des objets. Vérifié :

```
java.lang.ClassCastException: class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.String;
```

`toArray(String[]::new)` reçoit une fonction qui crée un **vrai** `String[]` de la bonne taille.

**Questions sur `findAny` :**
- **Prévisible ici** : un **seul** échantillon passe le filtre (db1 à 93.5). « N'importe lequel » parmi un seul, c'est celui-là.
- **Imprévisible en général** : la spécification autorise `findAny` à rendre n'importe quel élément qui passe le filtre, pour permettre au parallèle de rendre le premier **trouvé** par n'importe quel fil. En séquentiel, l'implémentation actuelle rend souvent le premier, mais rien ne le **garantit**. L'examen teste la spécification, pas l'implémentation.

---

## Étape 3 — Les `long`

**Le code :** `longs`.

**`reduce(Integer.MIN_VALUE, MAX)`** : l'identité d'un max est la plus petite valeur possible. Ainsi, `max(MIN_VALUE, x) = x`.

**Question — la mémoire en `int` :** le cumul vaut 112 600 Mo, et 112 600 × 1 048 576 ≈ 1,18 × 10¹¹, soit environ 55 fois `Integer.MAX_VALUE`. En `int`, la multiplication **déborde** sans erreur. Vérifié sur 110 000 Mo : en `int`, on obtient `-620756992` (un nombre négatif) ; avec un `long`, on obtient `115343360000`. D'où `asLongStream()` **avant** la multiplication.

**Les deux calendriers :** `IntStream.rangeClosed(0, 5).mapToLong(…)` (l'index de la minute) et `LongStream.iterate(start, t -> t <= end, t -> t + 60)`. Les deux donnent les 6 mêmes instants : `Arrays.equals` vaut `true`.

**Les trous :** cache1 n'a pas d'échantillon à 14:15:20 ni à 14:16:20. Pour chaque instant attendu, le `LongPredicate` vérifie qu'**aucun** échantillon de ce serveur ne le couvre (`noneMatch`).

---

## Étape 4 — Les `double` et `Stream.empty()`

**Le code :** `region` et `doubles`.

**Question — pourquoi ces sommes de `double` sont exactes :** 0.5 = 2⁻¹ est exactement représentable en binaire. Tout multiple de 0.5 (dans des limites raisonnables) l'est aussi, et leurs sommes de même. Le problème du projet 6 venait de décimaux comme 0.1, qui n'ont **pas** de valeur exacte en binaire.

**`Stream.empty()` plutôt que `null` :** l'appelant peut toujours enchaîner `.mapToDouble(…).average()` sans test. Une région inconnue donne un `OptionalDouble` **vide**, affiché `absent`. « Pas de mesure » n'est pas « une moyenne de 0 ».

---

## Étape 5 — Les alertes et la santé

**Le code :** `alerts` et `names`.

**Le `BooleanSupplier`** n'est évalué **qu'au moment du test**, dans la partition. Si la maintenance changeait en cours de route, on aurait toujours la valeur à jour, et pas une valeur capturée une fois pour toutes.

**La santé :** db1 a 1 alerte CPU (masquée) et 3 alertes MÉMOIRE (masquées), soit 4 alertes. 100 − 4 × 15 = **40**.

**Question — `compose` à la place d'`andThen` ?** `f.compose(g)` applique g **avant** f. L'ordre serait donc inversé : plancher, puis score, puis pénalité. Vérifié pour 4 alertes :
- `andThen` donne 4 × 15 = 60, puis 100 − 60 = **40**, puis max(40, 0) = 40 ;
- `compose` donne max(4, 0) = 4, puis 100 − 4 = 96, puis 96 × 15 = **1440**.

Le résultat n'a plus de sens.

---

## Étape 6 — Profils et relances

**Le code :** `profiles`.

**L'accumulateur d'`IntStream.collect`** est un **`ObjIntConsumer<R>`** : il reçoit le conteneur et un `int` **primitif**. Un `BiConsumer<R, Integer>` exigerait un boxing. `StringBuilder::append` convient, car `append(int)` existe.

**Question — pourquoi une lambda ne peut pas garder d'état :** une lambda n'a pas de champ à elle. Elle ne peut modifier qu'un champ **extérieur**, ou la case d'un tableau capturé. Vérifié : `IntSupplier sup = () -> { … next *= 2; … }` avec `next` local donne `error: local variables referenced from a lambda expression must be final or effectively final`. Une **classe anonyme** (chapitre 7) peut déclarer son propre champ `private int next`, et l'état vit **dans** le fournisseur.

---

## Étape 7 — Les pièges du chapitre

**Le code :** `reuse`.

**Les expériences, vérifiées :**
1. **Deux `count()` sur le même stream** : `IllegalStateException: stream has already been operated upon or closed`, à la ligne du **second** `count()`.
2. **`add` sur une liste de `toList()`** : `UnsupportedOperationException`. `Stream.toList()` (Java 16) rend une liste **non modifiable**. `collect(Collectors.toList())` rend une liste ordinaire, modifiable en pratique (vérifié : `[x, y]`), mais sa Javadoc ne le **garantit** pas.
3. **`toUnmodifiableMap(Sample::server, s -> s)`** : `IllegalStateException: Duplicate key web1 (attempted merging values …)`. Chaque serveur a plusieurs échantillons, donc plusieurs fois la même clé, et il n'y a pas de fonction de fusion.
4. **`Optional.of(null)`** : `NullPointerException`. `Optional.ofNullable(null)` rend `Optional.empty`.
