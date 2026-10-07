# Drill de rappel 8 — Choisir la bonne interface

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall08`** dans le paquet `ch8_lambdas.drills.r08_choose`.
- **Pour chaque lambda donnée, c'est toi qui choisis le type de la variable** : l'interface du JDK la plus précise, sans boxing quand c'est possible. Les types attendus sont vérifiés par `Check`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 3) et projet 4 (étape 1) : choisir la bonne interface. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r08_choose` → **New** → **Java Class** → `Recall08`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall08`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall08`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Trois fournisseurs : `() -> "t"`, `() -> 2.5` (un `double`) et `() -> false`.
  → `D01 : t 2.5 false`
- ☐ **D02.** Deux consommateurs qui écrivent dans un `StringBuilder out` :
  - `out::append`, pour un `String` ;
  - `(a, n) -> out.append(n).append(a)`, pour un `String` et un `Integer`.
  
  Appelle le premier avec `"x"` et le second avec `("y", 2)`, puis affiche `out`.
  → `D02 : x2y`
- ☐ **D03.** Trois tests :
  - `String::isBlank`, sur `" "` ;
  - `(str, ch) -> str.indexOf(ch) >= 0`, avec un `String` et un `Character`, sur `("java", 'v')` ;
  - `n -> n > 0`, sur un `int` : −1.
  → `D03 : true true false`
- ☐ **D04.** Quatre fonctions :
  - `str -> str.charAt(0)`, sur `"zeta"` ;
  - `String::substring`, avec un `String` et un `Integer`, sur `("lambda", 3)` ;
  - `str -> str + "?"`, sur `"quoi"` ;
  - `String::concat`, sur `("con", "cat")`.
  → `D04 : z bda quoi? concat`
- ☐ **D05.** Trois fonctions :
  - `str -> str.length() / 2.0`, sur `"abc"` ;
  - `String::compareTo`, sur `("b", "a")` ;
  - `(a, b) -> a % b`, sur deux `int` : (17, 5).
  → `D05 : 1.5 1 2`

## Expériences (hors sortie attendue)

1. Pour D04, essaie `Function<String, String>` pour `String::substring` : quelle erreur ?
2. Pour D05, essaie `BiFunction<String, String, Integer>` pour `String::compareTo` : ça compile ? Qu'est-ce que `ToIntBiFunction` évite ?
3. `str -> str + "?"` convient-il aussi à `Function<String, String>` ? Alors pourquoi préférer `UnaryOperator` ?

## Sortie attendue complète

```
D01 : t 2.5 false
D02 : x2y
D03 : true true false
D04 : z bda quoi? concat
D05 : 1.5 1 2
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Méthode : regarde la forme de la lambda, puis choisis.**

| Paramètres | Résultat | Interface |
|---|---|---|
| aucun | T / `int` / `long` / `double` / `boolean` | `Supplier<T>` / `IntSupplier` / `LongSupplier` / `DoubleSupplier` / `BooleanSupplier` |
| T | rien | `Consumer<T>` |
| T, U | rien | `BiConsumer<T, U>` |
| T | `boolean` | `Predicate<T>` |
| T, U | `boolean` | `BiPredicate<T, U>` |
| `int` | `boolean` | `IntPredicate` |
| T | R | `Function<T, R>` (si R = T : `UnaryOperator<T>`) |
| T, U | R | `BiFunction<T, U, R>` (si tout est T : `BinaryOperator<T>`) |
| T | `int` / `double` | `ToIntFunction<T>` / `ToDoubleFunction<T>` |
| T, U | `int` | `ToIntBiFunction<T, U>` |
| `int`, `int` | `int` | `IntBinaryOperator` |

</details>
