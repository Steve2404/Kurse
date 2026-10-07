# Drill de rappel 4 — Composer des fonctions

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`** dans le paquet `ch8_lambdas.drills.r04_compose`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 3), projet 2 (étape 1) et projet 3 (étape 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r04_compose` → **New** → **Java Class** → `Recall04`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall04`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall04`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Avec `plus2 = x -> x + 2` et `times3 = x -> x * 3` (des `Function<Integer, Integer>`) :
  - `plus2.andThen(times3).apply(1)` ;
  - `plus2.compose(times3).apply(1)` ;
  - `Function.<Integer>identity().apply(5)` ;
  - `UnaryOperator.<String>identity().apply("id")`.
  → `D01 : 9 5 5 id`
- ☐ **D02.** Avec `notEmpty` et `shortWord` (longueur < 5) :
  - `notEmpty.and(shortWord)` sur `"java"` puis `"lambda"` ;
  - `notEmpty.negate().or(shortWord)` sur `""` ;
  - `Predicate.not(notEmpty)` sur `"x"` ;
  - `Predicate.isEqual("a")` sur `"a"`.
  → `D02 : true false true false true`
- ☐ **D03.** `first` ajoute `1:` + s, `second` ajoute ` 2:` + la longueur. Applique `first.andThen(second)` à `"abc"`.
  → `D03 : 1:abc 2:3`
- ☐ **D04.** `BiFunction<Integer, Integer, Integer> mult`, puis `mult.andThen(x -> "=" + x).apply(6, 7)`.
  → `D04 : =42`
- ☐ **D05.** `BinaryOperator.minBy` et `maxBy` avec `(a, b) -> a.length() - b.length()`. Applique-les à `("pomme", "kiwi")`, puis `minBy` à `("ab", "cd")`.
  → `D05 : kiwi pomme ab`
- ☐ **D06.** `trim = String::strip`, puis `trim.andThen(String::length).andThen(n -> n * n)`, appliqué à `"  abc  "`.
  → `D06 : 9`

## Expériences (hors sortie attendue)

1. `BiFunction` a-t-il une méthode `compose` ? Pourquoi ?
2. `Predicate<String> p = notEmpty.and(s -> s.length())` : quelle erreur ?
3. `Supplier` a-t-il `andThen` ? Et `Consumer` a-t-il `compose` ?
4. Pourquoi faut-il écrire `Function.<Integer>identity()` ici, alors que `Function<Integer, Integer> id = Function.identity();` suffit ?

## Sortie attendue complète

```
D01 : 9 5 5 id
D02 : true false true false true
D03 : 1:abc 2:3
D04 : =42
D05 : kiwi pomme ab
D06 : 9
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Interface | Méthodes de composition |
|---|---|
| `Function` | `f.andThen(g)` = g(f(x)) ; `f.compose(g)` = f(g(x)) ; `static identity()` |
| `UnaryOperator` | `static identity()` (et ce qu'elle hérite de `Function`) |
| `BiFunction` | `andThen(Function)` seulement : le résultat a deux entrées et une sortie |
| `Predicate` | `and`, `or`, `negate` ; `static not(p)`, `static isEqual(obj)` |
| `BiPredicate` | `and`, `or`, `negate` |
| `Consumer` / `BiConsumer` | `andThen` : l'un après l'autre, avec la même entrée |
| `BinaryOperator` | `static minBy(comparateur)` et `maxBy(comparateur)` (à égalité, le premier argument) |
| `Supplier` | aucune |

`and` et `or` sont **court-circuités** : le second n'est évalué que si nécessaire.

</details>
