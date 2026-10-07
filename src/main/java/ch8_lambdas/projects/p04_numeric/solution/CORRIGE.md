# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Numeric.java`](Numeric.java) et [`NumericApp.java`](NumericApp.java).
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Les algorithmes de `Numeric`

**Le code :** [`Numeric.java`](Numeric.java).

**Des fonctions en paramètre :** `bisection`, `newton`, `simpson` et `goldenMax` ne savent pas **quelle** fonction elles traitent. On leur passe la fonction comme on passerait un nombre. C'est tout l'intérêt des lambdas : un algorithme écrit une fois, appliqué à n'importe quelle fonction.

**Une fonction qui rend une fonction :** `derivative(f)` ne calcule rien. Elle **construit** une nouvelle lambda qui, appelée en x, calculera la pente de f autour de x. `newton` l'utilise sans jamais écrire la dérivée à la main.

**Pourquoi les interfaces primitives ?** `DoubleUnaryOperator` prend et rend des `double`. Un `Function<Double, Double>` emballerait chaque nombre dans un objet `Double` (boxing), des milliers de fois dans une boucle.

---

## Étape 2 — Racines, intégrales, maximum

**Le code :** les quatre premières lignes du `main`.

**Dichotomie contre Newton :**
- La dichotomie divise l'intervalle par 2 à chaque tour : il faut log₂(2 / 10⁻⁹) ≈ 30.9, soit **31** tours pour √2 (vérifié). Elle converge **toujours**, lentement.
- Newton double à peu près le nombre de chiffres justes à chaque tour : **5** tours. Il est très rapide près de la racine, mais il peut diverger si l'on part trop loin.

**Simpson :**
- Il approche la courbe par des **paraboles**. Pour x² (elle-même une parabole), le résultat est **exact** dès n = 10 : 9.0.
- ∫ e^(−x²) sur [−5, 5] ≈ √π : l'intégrale de Gauss. Au-delà de ±5, la fonction est négligeable.

**La section dorée :** elle réduit l'intervalle d'un facteur 0.618 à chaque tour, sans dérivée. Partie de [−10, 10] avec ε = 10⁻⁷, il faut log(20 / 10⁻⁷) / log(1 / 0.618) ≈ 40 tours.

---

## Étape 3 — Composer, réduire, filtrer

**Le code :** les lignes `composition`, `reductions` et `filtres`.

**`andThen` contre `compose` sur des `double` :**
- `plus1.andThen(times3)(2)` = (2 + 1) × 3 = **9.0** ;
- `plus1.compose(times3)(2)` = 2 × 3 + 1 = **7.0**.

**`reduce` avec une identité :** l'identité est l'élément « neutre » de l'opération. C'est 0 pour la somme, `Integer.MIN_VALUE` pour le max, 0 pour le PGCD (pgcd(0, a) = a) et 1 pour le PPCM. Elle donne aussi le résultat d'un tableau **vide**.

**Les références de méthodes static :** `Integer::sum`, `Math::max` et `Numeric::gcd` ont toutes la forme `(int, int) -> int` : elles conviennent à `IntBinaryOperator`.

**Les combinaisons de prédicats :** `prime.or(even.negate())` correspond à « premier **ou** impair ». Dans `{84, 36, 120, 48, 60, 17, 90}`, seul 17 est concerné, d'où **1**.

---

## Étape 4 — Les autres formes, et Monte-Carlo

**Le code :** la fin du `main`.

**Le tableau des formes primitives de ce projet :**

| Interface | Méthode | Exemple |
|---|---|---|
| `ToIntFunction<T>` | `int applyAsInt(T)` | `vowels` |
| `IntFunction<R>` | `R apply(int)` | `bar` |
| `IntToDoubleFunction` | `double applyAsDouble(int)` | `root` |
| `IntToLongFunction` | `long applyAsLong(int)` | `factorial` |
| `ObjIntConsumer<T>` | `void accept(T, int)` | `appendTwice` |
| `DoubleSupplier` | `double getAsDouble()` | `rnd` |
| `IntSupplier` | `int getAsInt()` | `counter` |
| `BooleanSupplier` | `boolean getAsBoolean()` | `closeToPi` |
| `DoubleBinaryOperator` | `double applyAsDouble(double, double)` | `hypot` |

**Monte-Carlo :** la proportion de points tombés dans le quart de disque tend vers π/4. Avec 20000 tirages, on obtient π ≈ 3.1262 : l'erreur diminue comme 1/√n, donc lentement.

**Expériences :**
- **`IntPredicate p = (Integer x) -> x > 0;`** :

  ```
  error: incompatible types: incompatible parameter types in lambda expression
  ```

  Le paramètre d'`IntPredicate.test` est un **`int`**. Un type explicite dans la lambda doit être **exactement** celui-là : pas de boxing automatique pour les paramètres.
- **`DoubleUnaryOperator d = Math::abs;`** : `Math.abs` a 4 surcharges (`int`, `long`, `float`, `double`). Le type cible attend `double` vers `double`, donc c'est **`abs(double)`**. Vérifié : `d.applyAsDouble(-2.5)` donne `2.5`. La surcharge est choisie d'après l'interface visée.
- **Pourquoi `Function<Integer, Integer>` est plus lent :** chaque appel déballe l'`Integer` en `int`, calcule, puis remballe le résultat dans un **nouvel** objet `Integer` (au-delà du cache de −128 à 127). Ce sont des allocations et du travail pour le ramasse-miettes. Vérifié sur 50 millions d'appels : environ 7 ms contre 2 ms pour `IntUnaryOperator`. Le JIT optimise beaucoup, et l'écart réel dépend du programme, mais il ne joue jamais en faveur du boxing.
