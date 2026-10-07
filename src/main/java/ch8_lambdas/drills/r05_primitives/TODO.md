# Drill de rappel 5 — Les interfaces fonctionnelles primitives

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall05`** dans le paquet `ch8_lambdas.drills.r05_primitives`.

**Les notions de ce drill ont été apprises dans :** projet 4 (étapes 1 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r05_primitives` → **New** → **Java Class** → `Recall05`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall05`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall05`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Trois fournisseurs :
  - `IntSupplier answer = () -> 42` ;
  - `LongSupplier big = () -> 1L << 40` ;
  - `BooleanSupplier yes = () -> true`.
  
  Utilise `getAsInt`, `getAsLong` et `getAsBoolean`.
  → `D01 : 42 1099511627776 true`
- ☐ **D02.** Avec `IntPredicate even`, `IntUnaryOperator square` et `IntBinaryOperator max = Math::max`, affiche :
  - `even.test(7)` et `even.negate().test(7)` ;
  - `square.applyAsInt(9)` ;
  - `square.andThen(n -> n + 1).applyAsInt(3)` ;
  - `max.applyAsInt(4, 9)`.
  → `D02 : false true 81 10 9`
- ☐ **D03.** Trois fonctions :
  - `ToIntFunction<String> length = String::length`, sur `"lambda"` ;
  - `ToIntBiFunction<String, String> both` (somme des longueurs), sur `("ab", "cde")` ;
  - `IntFunction<String> stars`, sur 4.
  → `D03 : 6 5 ****`
- ☐ **D04.** Trois fonctions :
  - `IntToDoubleFunction half = n -> n / 2.0`, sur 7 ;
  - `DoubleToIntFunction floor = d -> (int) Math.floor(d)`, sur −2.5 ;
  - `DoubleBinaryOperator avg`, sur (3, 4).
  → `D04 : 3.5 -3 3.5`
- ☐ **D05.** `ObjIntConsumer<StringBuilder> append = (s, n) -> s.append(n).append(',')`, appelé pour i de 1 à 3 avec i².
  → `D05 : 1,4,9,`
- ☐ **D06.** Les consommateurs primitifs, qui écrivent tous dans un `StringBuilder log` :
  - `IntConsumer i` ajoute `i` + n, chaîné par `andThen(n -> log.append('+'))`, puis `accept(1)` ;
  - `LongConsumer l` ajoute ` l` + n, puis `accept(2L)` ;
  - `DoubleConsumer d` ajoute ` d` + x, puis `accept(3.5)` ;
  - `ObjLongConsumer<StringBuilder> ol` ajoute ` ol` + n, avec `(log, 4L)` ;
  - `ObjDoubleConsumer<StringBuilder> od` ajoute ` od` + x, avec `(log, 5.5)`.
  
  Affiche `log`.
  → `D06 : i1+ l2 d3.5 ol4 od5.5`
- ☐ **D07.** Huit interfaces, une seule ligne :
  - `DoublePredicate positive`, puis `positive.negate().test(-1.5)` ;
  - `LongBinaryOperator gcd` (Euclide en boucle), sur (84, 36) ;
  - `LongFunction<String> hex = Long::toHexString`, sur 255 ;
  - `LongToIntFunction digits` (nombre de chiffres), sur 123456 ;
  - `LongToDoubleFunction kilo = n -> n / 1000.0`, sur 1500 ;
  - `DoubleToLongFunction round = Math::round`, sur 2.5 ;
  - `ToLongBiFunction<String, String> totalLength`, sur `("ab", "cde")` ;
  - `ToDoubleBiFunction<Integer, Integer> ratio`, sur (1, 4).
  → `D07 : true 12 ff 6 1.5 3 5 0.25`

## Expériences (hors sortie attendue)

1. `IntSupplier s = () -> 4L;` : quelle erreur ? Et `LongSupplier l = () -> 4;` ?
2. `IntFunction<String> f` utilise `apply`, mais `ToIntFunction<String>` utilise `applyAsInt` : pourquoi cette différence de nom ?
3. Existe-t-il une `BooleanUnaryOperator` ou une `CharPredicate` dans le JDK ?
4. `IntPredicate p = Integer::isEven;` : quelle erreur ? (Cette méthode n'existe pas.)
5. `DoubleToLongFunction r = Math::round;` compile, mais `DoubleToIntFunction r = Math::round;` ? Pourquoi ? (`Math.round(double)` rend un `long`.)

## Sortie attendue complète

```
D01 : 42 1099511627776 true
D02 : false true 81 10 9
D03 : 6 5 ****
D04 : 3.5 -3 3.5
D05 : 1,4,9,
D06 : i1+ l2 d3.5 ol4 od5.5
D07 : true 12 ff 6 1.5 3 5 0.25
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

Les versions primitives existent pour **`int`, `long` et `double`** (et `BooleanSupplier`). Elles évitent le boxing.

| Forme | `int` | Méthode |
|---|---|---|
| fournisseur | `IntSupplier` | `getAsInt()` |
| consommateur | `IntConsumer` | `accept(int)` |
| prédicat | `IntPredicate` | `test(int)` |
| opérateurs | `IntUnaryOperator`, `IntBinaryOperator` | `applyAsInt` |
| int → objet | `IntFunction<R>` | `apply(int)` |
| objet → int | `ToIntFunction<T>`, `ToIntBiFunction<T, U>` | `applyAsInt` |
| int → autre primitif | `IntToDoubleFunction`, `IntToLongFunction` | `applyAsDouble`, `applyAsLong` |
| objet + int | `ObjIntConsumer<T>` | `accept(T, int)` |

**La règle des noms :** la méthode s'appelle `applyAsX` / `getAsX` quand le **résultat** est un primitif X.

**Pour `long` et `double` :** le même schéma :
- `LongConsumer`, `DoublePredicate`, `LongFunction<R>` ;
- `LongBinaryOperator`, `ToLongBiFunction`, `ToDoubleBiFunction` ;
- `ObjLongConsumer`, `ObjDoubleConsumer`.

**Les conversions entre primitifs :** `IntToLong`, `IntToDouble`, `LongToInt`, `LongToDouble`, `DoubleToInt` et `DoubleToLong` (+ `Function`). Il n'en existe aucune vers ou depuis `boolean`.

**`boolean` :** seulement `BooleanSupplier` (`getAsBoolean`).

</details>
