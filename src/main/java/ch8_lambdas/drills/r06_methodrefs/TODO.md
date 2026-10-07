# Drill de rappel 6 — Les références de méthode

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall06.java`, paquet `ch8_lambdas.drills.r06_methodrefs`.
- **La structure :**
  - sous `Recall06`, une classe `Base` dont `String describe(String s)` rend `"parent(" + s + ")"` ;
  - **`Recall06 extends Base`**, avec :
    - un champ `private final String suffix = "!"` ;
    - `String exclaim(String s)`, qui rend `s + suffix` ;
    - `describe` redéfini, qui rend `"enfant(" + s + ")"` ;
    - `String demo()`, qui utilise `this::exclaim`, `super::describe` et `this::describe`, appliqués à `"a"`, `"b"` et `"c"`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 1) et projet 5 (étape 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r06_methodrefs` → **New** → **Java Class** → `Recall06`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall06`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall06`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Quatre références :
  - `Integer::parseInt` sur `"40"`, + 2 ;
  - `prefix::concat`, avec `prefix = "pre-"`, sur `"fixe"` ;
  - `String::toUpperCase` sur `"abc"` ;
  - `BiFunction<String, String, Boolean> starts = String::startsWith` sur `("java", "ja")`.
  → `D01 : 42 pre-fixe ABC true`
- ☐ **D02.** Trois constructeurs :
  - `Supplier<StringBuilder> make = StringBuilder::new`, puis `make.get().append("vide").length()` ;
  - `Function<String, StringBuilder> makeWith = StringBuilder::new`, sur `"init"` ;
  - `IntFunction<int[]> array = int[]::new`, puis `array.apply(3).length`.
  → `D02 : 4 init 3`
- ☐ **D03.** `IntBinaryOperator intMax = Math::max` et `DoubleBinaryOperator doubleMax = Math::max`, appliqués à (3, 8).
  → `D03 : 8 8.0`
- ☐ **D04.** `Predicate<String> empty = String::isEmpty` et `blank = s -> s.isBlank()`. Teste-les sur `" "`, puis `empty.negate()` sur `"x"`.
  → `D04 : false true true`
- ☐ **D05.** `new Recall06().demo()`.
  → `D05 : a! parent(b) enfant(c)`
- ☐ **D06.** Deux références :
  - `Function<Integer, String> toText = String::valueOf`, sur 7 puis 8, résultats collés ;
  - `BiFunction<String, Integer, Character> charAt = String::charAt`, sur `("lambda", 2)`.
  → `D06 : 78 m`

## Expériences (hors sortie attendue)

1. Dans `main` (une méthode `static`), écris `Function<String, String> f = this::exclaim;` : quelle erreur ?
2. `Supplier<String> s = String::new;` compile-t-il ? Que rend `s.get()` ?
3. `Function<String, String> f = String::concat;` : quelle erreur ? Quelle interface conviendrait ?
4. Réécris chaque référence du drill en lambda équivalente.

## Sortie attendue complète

```
D01 : 42 pre-fixe ABC true
D02 : 4 init 3
D03 : 8 8.0
D04 : false true true
D05 : a! parent(b) enfant(c)
D06 : 78 m
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Sorte | Syntaxe | Lambda équivalente |
|---|---|---|
| méthode `static` | `Classe::methode` | `(a) -> Classe.methode(a)` |
| instance d'un objet précis | `objet::methode` (aussi `this::m`, `super::m`) | `(a) -> objet.methode(a)` |
| instance sur le paramètre | `Classe::methode` | `(obj, a) -> obj.methode(a)` : le **1er** paramètre devient l'objet |
| constructeur | `Classe::new` ; `int[]::new` | `(a) -> new Classe(a)` ; `n -> new int[n]` |

**Le choix de la surcharge** (`Math::max`, `StringBuilder::new`, `String::valueOf`) dépend du **type cible**.

**`Classe::methode` est ambigu à l'œil** (static, ou instance sur le paramètre) : c'est la signature de l'interface cible qui tranche.

**Une référence ne peut rien ajouter** : pas d'argument fixe, pas de calcul. Pour cela, il faut une lambda.

</details>
