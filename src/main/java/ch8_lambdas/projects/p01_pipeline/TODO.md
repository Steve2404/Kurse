# Projet 1 — Le pipeline de texte

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 8) :**
- **toutes les syntaxes de lambda** :
  - `s -> …` et `(s) -> …` ;
  - `(String s) -> …` et `(var s) -> …` ;
  - un corps en bloc `{ …; return …; }` ;
- une **interface fonctionnelle à toi**, `Step extends UnaryOperator<String>` ;
- `@FunctionalInterface` ;
- une méthode `default` qui **rend une lambda** (un combinateur) ;
- des **références de méthode** : `String::toLowerCase`, `Step::title` ;
- **capturer** une variable *effectively final* ;
- `Function` et ses méthodes `andThen`, `compose` et `identity`, qui changent le type du résultat ;
- `BiFunction`, `BinaryOperator` (et `minBy`), `UnaryOperator` ;
- **modifier un compteur** depuis une lambda : un tableau, ou un champ.

Côté algorithmes :
- un **analyseur de pipelines** ;
- les codages **RLE** et **César** ;
- la **mémoïsation** sans collection.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch8_lambdas.projects.p01_pipeline` :
- l'interface **`Step`** ;
- la classe **`Memo`** ;
- **`PipelineApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 8.
- Pas de collection, de stream ni d'`Optional`.
- Pas de `Comparator` (chapitre 9).
- Pas de type générique déclaré par toi (`interface X<T>`) : tu **utilises** les génériques du JDK, sans en déclarer.
- Pas de `try/catch`.

---

## Tableau de bord

### ☐ Étape 1 — L'interface `Step`

- **`@FunctionalInterface public interface Step extends UnaryOperator<String>`** : la seule méthode abstraite est `apply`, héritée.
- **`default Step then(Step next)`** rend `s -> next.apply(apply(s))`.
- **`static Step identity()`** rend `s -> s`.
- **`static Step of(String command)`**, un `switch` expression qui rend une lambda par commande, en variant les syntaxes :

| Commande | Lambda attendue |
|---|---|
| `trim` | `s -> s.strip()` |
| `lower` | `String::toLowerCase` |
| `upper` | `(String s) -> s.toUpperCase()` |
| `squeeze` | `(var s) -> s.replaceAll(" +", " ")` |
| `reverse` | inverse avec un `StringBuilder` |
| `title` | `Step::title` (méthode `private static` : chaque mot en minuscules, première lettre en majuscule, un espace entre les mots) |
| `novowels` | `s -> s.replaceAll("[aeiouAEIOU]", "")` |
| `caesar N` | une méthode `private static Step caesar(int shift)` qui rend une lambda (lettres décalées, casse conservée) |
| `replace A B` | `s -> s.replace(p[1], p[2])`, où `p` est capturé |
| `repeat N` | un **bloc** dans le `case`, puis `yield s -> { … return …; }` : le texte répété N fois, séparé par ` / ` |
| `rle` / `unrle` | `Step::encode` / `Step::decode` (RLE : `aaab` ↔ `3a1b`) |

- **`static Step parse(String pipeline)`** : découpe sur `\\|`, puis enchaîne avec `then` à partir de `identity()`.

### ☐ Étape 2 — Appliquer les pipelines

```
trim | squeeze | title : [Le Java Est Un Langage] [Aaabbbccccd] [Bonjour Le Monde]
rle : [3 1l1e2 1J1a1v1a3 1e1s1t3 1u1n1 1l1a1n1g1a1g1e3 ] [3a3b4c1d] [1B1o1n1j1o1u1r1 1l1e1 1M1o1n1d1e]
...
```
- Pour chaque pipeline de `Data.PIPELINES` : le pipeline, ` :`, puis chaque texte de `Data.TEXTS` transformé, entre crochets.
- **Question :** pourquoi `rle | unrle` et `caesar 3 | caesar 23` redonnent-ils le texte d'origine ?

### ☐ Étape 3 — Composer des `Function`

```
andThen ok!ok!, compose okok!, identite meme
types : ****** <ababab> kotlin kiwi
```
- `Function<String, String> exclaim = s -> s + "!"` et `doubled = s -> s + s`. Affiche :
  - `exclaim.andThen(doubled).apply("ok")` ;
  - `exclaim.compose(doubled).apply("ok")` ;
  - `Function.<String>identity().apply("meme")`.
- **Ligne `types` :**
  - `Function<String, Integer> length = String::length` et `Function<Integer, String> stars = n -> "*".repeat(n)` ; leur `length.andThen(stars)` est une `Function<String, String>`, appliquée à `"lambda"` ;
  - `BiFunction<String, Integer, String> repeat`, avec `.andThen(brackets)` (un `UnaryOperator` qui entoure de `<>`), appliquée à `("ab", 3)` ;
  - `BinaryOperator<String> longer` (le plus long, le premier à égalité), appliqué à `("java", "kotlin")` ;
  - `BinaryOperator.minBy((String a, String b) -> a.length() - b.length())`, appliqué à `("pomme", "kiwi")`.

### ☐ Étape 4 — État et mémoïsation

```
compteur : X apres 2 appels
memo : [B A] [D C] [B A] [D C] [B A] [E] -> 6 appels, 3 depuis le cache
```
- **Le compteur :** `int[] applied = {0};` et une `Step counted` qui incrémente `applied[0]` puis rend `s.strip()`. Puis `counted.then(counted).then(Step.of("upper"))`, appliquée à `"  x  "`.
  - **Question :** pourquoi un `int applied = 0;` ne marcherait-il pas ?
- **`Memo`** :
  - deux tableaux `keys` et `values`, de capacité donnée ;
  - des compteurs `calls` et `hits`, qui sont des **champs** : une lambda peut les modifier ;
  - `Step wrap(Step f)` rend une lambda qui cherche d'abord dans le cache ;
  - `stats()` rend `N appels, M depuis le cache`.
  - Dans `main` : `new Memo(4)`, puis `wrap` de `Step.parse("trim | squeeze | title | reverse")`, appliqué à `{" a  b ", "c d", " a  b ", "c d", " a  b ", "e"}`.
- **Expériences :**
  - écris `int n = 0; Step bad = s -> { n++; return s; };` : quelle erreur ?
  - ajoute une 2e méthode abstraite à `Step` : que devient `@FunctionalInterface` ?
  - `Step x = (s) -> return s;` : quelle erreur ? Et `(String s, t) -> s` ?

---

## Checklist (vérifiée par `Check`)

- `Data.TEXTS` et `Data.PIPELINES` ;
- `@FunctionalInterface`, `interface Step extends UnaryOperator<String>`, `default Step then(` ;
- `String::toLowerCase`, `(String s) ->`, `(var s) ->`, `Step::title`, `yield s -> {` ;
- `andThen`, `compose`, `Function.<String>identity()` ;
- `BiFunction<String, Integer, String>`, `BinaryOperator.minBy(` ;
- `int[] applied` et `class Memo`.

---

## Sortie attendue complète

```
trim | squeeze | title : [Le Java Est Un Langage] [Aaabbbccccd] [Bonjour Le Monde]
rle : [3 1l1e2 1J1a1v1a3 1e1s1t3 1u1n1 1l1a1n1g1a1g1e3 ] [3a3b4c1d] [1B1o1n1j1o1u1r1 1l1e1 1M1o1n1d1e]
rle | unrle : [   le  Java   est   un langage   ] [aaabbbccccd] [Bonjour le Monde]
lower | caesar 3 | upper : [   OH  MDYD   HVW   XQ ODQJDJH   ] [DDDEEEFFFFG] [ERQMRXU OH PRQGH]
caesar 3 | caesar 23 : [   le  Java   est   un langage   ] [aaabbbccccd] [Bonjour le Monde]
novowels | reverse | replace l 1 : [   ggn1 n   ts   vJ  1   ] [dccccbbb] [dnM 1 rjnB]
trim | squeeze | repeat 2 : [le Java est un langage / le Java est un langage] [aaabbbccccd / aaabbbccccd] [Bonjour le Monde / Bonjour le Monde]
andThen ok!ok!, compose okok!, identite meme
types : ****** <ababab> kotlin kiwi
compteur : X apres 2 appels
memo : [B A] [D C] [B A] [D C] [B A] [E] -> 6 appels, 3 depuis le cache
```
