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

**Ce que le chapitre 8 t'apprend :** les **lambdas**, c'est-à-dire des morceaux de code qu'on range dans une variable, qu'on passe à une méthode et qu'on exécute plus tard. Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet.

**Les types avec `<…>`.** Tu vas écrire des types comme `Function<String, Integer>`. Les types entre chevrons disent **ce qui entre et ce qui sort** : ici, une fonction qui reçoit un `String` et rend un `Integer`. Ce sont des types **génériques**. Ici, tu ne fais que les **utiliser** ; le chapitre 9 t'apprendra à en écrire.

**Les imports :** les interfaces de ce chapitre sont dans `java.util.function` : écris `import java.util.function.*;` en haut de tes fichiers.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch8-p01 -sourcepath src/main/java src/main/java/ch8_lambdas/projects/p01_pipeline/PipelineApp.java
java "-Duser.language=fr" -cp build/ch8-p01 ch8_lambdas.projects.p01_pipeline.PipelineApp
```

---

## Tableau de bord

### ☐ Étape 1 — L'interface `Step`

**📖 La leçon : une interface fonctionnelle, une lambda.** Une interface qui a **une seule** méthode abstraite est dite **fonctionnelle**. On peut alors écrire sa méthode **sur place**, sans créer de classe : c'est une **lambda**. L'annotation `@FunctionalInterface` demande à `javac` de vérifier qu'il n'y a bien qu'une méthode abstraite.

```java
@FunctionalInterface
interface Calcul { int applique(int a, int b); }

Calcul fois = (a, b) -> a * b;          // paramètres -> résultat
Calcul max = (a, b) -> {                // un corps de plusieurs lignes : accolades et return
    if (a > b) return a;
    return b;
};
fois.applique(3, 4)                     // 12
max.applique(3, 9)                      // 9
```

Avant le chapitre 8, il aurait fallu une classe anonyme (chapitre 7, projet 5) : une lambda est la même chose, en beaucoup plus court.

**📖 La leçon : les façons d'écrire une lambda.** Elles sont toutes équivalentes :

```java
Function<String, String> a = s -> s.strip();                            // un paramètre : parenthèses facultatives
Function<String, String> b = (s) -> s.strip();
Function<String, String> c = (String s) -> s.strip();                   // avec le type
Function<String, String> d = (var s) -> s.strip();                      // avec var
Function<String, String> e = s -> { String r = s.strip(); return r; };  // un bloc
```

Une lambda sans paramètre s'écrit `() -> …`, et une lambda à deux paramètres `(a, b) -> …`.

**📖 La leçon : la référence de méthode `::`.** Quand une lambda ne fait qu'appeler une méthode qui existe déjà, on peut écrire juste son nom. Quatre sortes :

| Écriture | Équivalent lambda | Exemple |
|---|---|---|
| `Classe::methodeStatic` | `x -> Classe.methodeStatic(x)` | `Integer::parseInt` |
| `objet::methode` | `x -> objet.methode(x)` | `prefixe::startsWith` |
| `Classe::methodeDInstance` | `x -> x.methode()` | `String::length`, `String::toLowerCase` |
| `Classe::new` | `x -> new Classe(x)` | `StringBuilder::new`, `String[]::new` |

Une interface peut aussi étendre une interface fonctionnelle toute faite, et y ajouter des méthodes `default` et `static` (chapitre 7) : elle reste fonctionnelle tant qu'elle n'a qu'une méthode abstraite.

**👉 À toi :**

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

**📖 Rappel :** `then` et `identity` sont les méthodes de ton interface (étape 1). Pour enchaîner un pipeline, on part de `identity()` et on ajoute chaque étape avec `then`.

**👉 À toi :**

- Pour chaque pipeline de `Data.PIPELINES` : le pipeline, ` :`, puis chaque texte de `Data.TEXTS` transformé, entre crochets.
- **Question :** pourquoi `rle | unrle` et `caesar 3 | caesar 23` redonnent-ils le texte d'origine ?

### ☐ Étape 3 — Composer des `Function`

```
andThen ok!ok!, compose okok!, identite meme
types : ****** <ababab> kotlin kiwi
```

**📖 La leçon : les interfaces fonctionnelles toutes faites.** Java en fournit un grand nombre dans `java.util.function`. Les principales :

| Interface | Reçoit | Rend | Méthode à appeler |
|---|---|---|---|
| `Supplier<T>` | rien | un `T` | `get()` |
| `Consumer<T>` | un `T` | rien | `accept(t)` |
| `BiConsumer<T, U>` | un `T` et un `U` | rien | `accept(t, u)` |
| `Predicate<T>` | un `T` | un `boolean` | `test(t)` |
| `BiPredicate<T, U>` | un `T` et un `U` | un `boolean` | `test(t, u)` |
| `Function<T, R>` | un `T` | un `R` | `apply(t)` |
| `BiFunction<T, U, R>` | un `T` et un `U` | un `R` | `apply(t, u)` |
| `UnaryOperator<T>` | un `T` | un `T` | `apply(t)` |
| `BinaryOperator<T>` | deux `T` | un `T` | `apply(t1, t2)` |

```java
Predicate<String> court = s -> s.length() <= 4;
court.test("kiwi")                                         // true
Function<String, Integer> taille = s -> s.length();
taille.apply("pomme")                                      // 5
BiFunction<String, Integer, String> repete = (s, n) -> s.repeat(n);
repete.apply("ab", 3)                                      // "ababab"
Supplier<String> salut = () -> "coucou";
salut.get()                                                // "coucou"
```

**📖 La leçon : composer des fonctions.** `f.andThen(g)` fait `f` **puis** `g`. `f.compose(g)` fait `g` **puis** `f`.

```java
Function<Integer, Integer> fois2 = x -> x * 2;
Function<Integer, Integer> plus1 = x -> x + 1;
fois2.andThen(plus1).apply(5)        // (5 × 2) + 1 = 11
fois2.compose(plus1).apply(5)        // (5 + 1) × 2 = 12
Function.<Integer>identity().apply(7)   // 7 : rend son entrée telle quelle
```

**👉 À toi :**

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

**📖 La leçon : ce qu'une lambda peut lire.** Une lambda peut **lire** les variables locales qui l'entourent, si elles ne changent plus jamais (« effectivement `final` », comme pour les classes locales et anonymes du chapitre 7). Pour garder un état qui change, on passe par un **tableau** (on modifie ses cases, pas la variable) ou par un **champ** d'objet.

**👉 À toi :**

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
