# Projet 4 — Le laboratoire numérique (interfaces primitives)

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 8) :** les **interfaces fonctionnelles primitives**, qui évitent le boxing :
- `DoubleUnaryOperator`, `DoubleBinaryOperator`, `DoubleSupplier` ;
- `IntUnaryOperator`, `IntBinaryOperator`, `IntPredicate`, `IntSupplier`, `IntFunction`, `IntToDoubleFunction`, `IntToLongFunction` ;
- `ToIntFunction`, `ObjIntConsumer`, `BooleanSupplier` ;
- leurs méthodes : `applyAsDouble`, `applyAsInt`, `getAsInt`, `getAsDouble`, `getAsBoolean`, `test`, `accept` ;
- leurs combinaisons : `andThen`, `compose`, `identity`, `and`, `or`, `negate` ;
- une **fonction qui rend une fonction** (la dérivée) ;
- les références `Math::sin`, `Integer::sum`, `Math::max`, `Math::hypot`, `Numeric::gcd`.

Côté algorithmes :
- la **dichotomie** ;
- la méthode de **Newton** (avec une dérivée numérique) ;
- l'**intégration de Simpson** ;
- la **section dorée** ;
- la **réduction** générique ;
- **Collatz** ;
- **Monte-Carlo** pour π.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch8_lambdas.projects.p04_numeric` :
- **`Numeric`** (les algorithmes) ;
- **`NumericApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 8. Pas de stream (`IntStream` est au chapitre 10). Pas de `%f` : arrondis avec `Math.round(x * 1_000_000) / 1_000_000.0`.

---

## Tableau de bord

### ☐ Étape 1 — Les algorithmes de `Numeric`

- **`record Result(double value, int iterations)`**, imbriqué. Son `toString()` = `valeur arrondie + " en " + n + " iterations"`.
- **`static double round(double)`** arrondit à 6 décimales.
- **`static DoubleUnaryOperator derivative(DoubleUnaryOperator f)`** rend `x -> (f(x+h) - f(x-h)) / (2h)`, avec `h = 1e-6`.
- **`bisection(f, a, b, eps)`** : tant que `b - a > eps`, prends le milieu m, et garde `[a, m]` si `f(a) * f(m) <= 0`, sinon `[m, b]`. Elle rend `(a + b) / 2` et le nombre de tours.
- **`newton(f, x, eps)`** : `step = f(x) / f'(x)`, puis `x -= step`, tant que `|step| > eps` (50 tours au plus). Un `do/while`.
- **`simpson(f, a, b, n)`** : `h/3 × [f(a) + f(b) + Σ (4 si i impair, 2 si pair) × f(a + i h)]`.
- **`goldenMax(f, a, b, eps)`**, avec `r = (√5 − 1) / 2`, c = b − r(b − a) et d = a + r(b − a) : si `f(c) > f(d)`, alors `b = d`, sinon `a = c`.
- **`reduce(int[] values, int identity, IntBinaryOperator op)`** et **`count(int[] values, IntPredicate keep)`**.
- **`stepsUntil(int x, IntUnaryOperator f, IntPredicate stop)`**.
- **`gcd(int, int)`**, récursif.

### ☐ Étape 2 — Racines, intégrales, maximum

```
racine de 2 : dichotomie 1.414214 en 31 iterations, Newton 1.414214 en 5 iterations
cos(x) = x : dichotomie 0.739085 en 30 iterations, Newton 0.739085 en 4 iterations
integrales : x^2 sur [0,3] = 9.0, sin sur [0,pi] = 2.0, exp(-x^2) sur [-5,5] = 1.772454 (racine de pi 1.772454)
maximum de -(x-1.5)^2+4 : x = 1.5 en 40 iterations ; derivee de sin en 0 = 1.0
```
- **Racine de 2 :** `x -> x * x - 2`, avec la dichotomie sur [0, 2] et Newton depuis 1.
- **cos(x) = x :** `x -> Math.cos(x) - x`, avec la dichotomie sur [0, 1] et Newton depuis 1.
- **Les intégrales de Simpson :**
  - `x -> x * x` sur [0, 3] avec n = 10 ;
  - `Math::sin` sur [0, π] avec n = 100 ;
  - `x -> Math.exp(-x * x)` sur [−5, 5] avec n = 200 ;
  - puis √π arrondi.
- **Le maximum :** `goldenMax` de `x -> -(x - 1.5) * (x - 1.5) + 4` sur [−10, 10] avec eps = `1e-7`. Puis `derivative(Math::sin).applyAsDouble(0)` arrondi.

### ☐ Étape 3 — Composer, réduire, filtrer

```
composition : 9.0 7.0 7.0 ; collatz(27) 111 pas, 22
reductions : somme 455, max 120, pgcd 1, ppcm 60
filtres : pairs 6, pairs et grands 4, premiers ou impairs 1, premiers <= 100 : 25
```
- **Ligne `composition` :**
  - `plus1.andThen(times3)` et `plus1.compose(times3)` appliqués à 2 ;
  - `DoubleUnaryOperator.identity()` appliqué à 7 ;
  - `stepsUntil(27, collatz, n -> n == 1)`, avec `collatz = n -> n % 2 == 0 ? n / 2 : 3 * n + 1` ;
  - `IntUnaryOperator.identity().andThen(collatz)` appliqué à 7.
- **Ligne `reductions`**, sur `Data.NUMBERS` :
  - la somme avec `Integer::sum` (identité 0) ;
  - le max avec `Math::max` (identité `Integer.MIN_VALUE`) ;
  - le PGCD avec `Numeric::gcd` (identité 0) ;
  - le PPCM de `{4, 6, 10, 15}` avec `lcm = (a, b) -> a / gcd(a, b) * b` (identité 1).
- **Ligne `filtres`**, avec `even`, `big = x -> x > 50` et `prime` (une lambda en bloc avec une boucle) :
  - `count(even)` et `count(even.and(big))` ;
  - `count(prime.or(even.negate()))` ;
  - le nombre de premiers dans `range(1, 100)`, une méthode `static int[] range(int from, int to)` de `NumericApp`.

### ☐ Étape 4 — Les autres formes, et Monte-Carlo

```
voyelles : lambda=## fonction=### interface=#### java=## predicat=### 77 ; racine(49) 7.0, 20! 2432902008176640000
monte-carlo : 15631 / 20000 -> pi ~ 3.1262, proche true ; hypot(3, 4) 5.0
```
- **Ligne `voyelles` :**
  - `ToIntFunction<String> vowels`, qui compte les `aeiouy` avec `replaceAll("[^aeiouy]", "")` ;
  - `IntFunction<String> bar = k -> "#".repeat(k)` ;
  - pour chaque mot de `Data.WORDS` : `mot=barre`, suivi d'un espace ;
  - puis `ObjIntConsumer<StringBuilder> appendTwice` ajoute `77` ;
  - `IntToDoubleFunction root = Math::sqrt` appliqué à 49 ;
  - `IntToLongFunction factorial` appliqué à 20.
- **Monte-Carlo :**
  - `DoubleSupplier rnd` fait `seed = (seed * 25214903917L + 11) & ((1L << 48) - 1)`, puis rend `(seed >>> 22) / (double) (1L << 26)`, avec `seed` initialisé à `Data.SEED` ;
  - `Data.SAMPLES` tirages de (x, y) ; on compte ceux où x² + y² ≤ 1 dans un `int[] inside` ;
  - `IntSupplier counter = () -> inside[0]` ;
  - `BooleanSupplier closeToPi` vrai si l'écart à π est < 0,05 ;
  - `DoubleBinaryOperator hypot = Math::hypot` appliqué à (3, 4).
- **Expériences :**
  - `IntPredicate p = (Integer x) -> x > 0;` : quelle erreur ?
  - `DoubleUnaryOperator d = Math::abs;` : quelle surcharge d'`abs` est choisie ?
  - pourquoi `Function<Integer, Integer>` est-il plus lent que `IntUnaryOperator` ? (Le boxing.)

---

## Checklist (vérifiée par `Check`)

- `Data.NUMBERS` et `Data.SAMPLES` ;
- `DoubleUnaryOperator` et `static DoubleUnaryOperator derivative(` ;
- `IntBinaryOperator`, `IntPredicate`, `IntUnaryOperator`, `ToIntFunction<String>`, `IntFunction<String>`, `IntToDoubleFunction`, `IntToLongFunction`, `ObjIntConsumer<StringBuilder>` ;
- `DoubleSupplier`, `BooleanSupplier`, `DoubleBinaryOperator` ;
- `Math::sin`, `Integer::sum`, `Numeric::gcd` ;
- `applyAsDouble(` et `applyAsInt(`.

---

## Sortie attendue complète

```
racine de 2 : dichotomie 1.414214 en 31 iterations, Newton 1.414214 en 5 iterations
cos(x) = x : dichotomie 0.739085 en 30 iterations, Newton 0.739085 en 4 iterations
integrales : x^2 sur [0,3] = 9.0, sin sur [0,pi] = 2.0, exp(-x^2) sur [-5,5] = 1.772454 (racine de pi 1.772454)
maximum de -(x-1.5)^2+4 : x = 1.5 en 40 iterations ; derivee de sin en 0 = 1.0
composition : 9.0 7.0 7.0 ; collatz(27) 111 pas, 22
reductions : somme 455, max 120, pgcd 1, ppcm 60
filtres : pairs 6, pairs et grands 4, premiers ou impairs 1, premiers <= 100 : 25
voyelles : lambda=## fonction=### interface=#### java=## predicat=### 77 ; racine(49) 7.0, 20! 2432902008176640000
monte-carlo : 15631 / 20000 -> pi ~ 3.1262, proche true ; hypot(3, 4) 5.0
```
