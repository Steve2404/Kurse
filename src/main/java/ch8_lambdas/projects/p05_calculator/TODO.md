# Projet 5 — La calculatrice (références de méthode)

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 8) :** les **quatre sortes de références de méthode**, plus les constructeurs :

| Sorte | Exemple ici | Lambda équivalente |
|---|---|---|
| méthode `static` | `Math::sqrt`, `Token::classify`, `Integer::parseInt` | `x -> Math.sqrt(x)` |
| instance d'un objet **précis** | `vars::lookup`, `fmt::format` | `n -> vars.lookup(n)` |
| instance sur le **paramètre** | `String::strip`, `String::equalsIgnoreCase` | `s -> s.strip()`, `(a, b) -> a.equalsIgnoreCase(b)` |
| constructeur | `StringBuilder::new`, `Token::new`, `Token[]::new` | `() -> new StringBuilder()`, `n -> new Token[n]` |

Et aussi :
- une même référence `Math::max` qui s'adapte au type cible ;
- des tableaux de `DoubleUnaryOperator` et de `DoubleBinaryOperator` (non génériques) ;
- `ToDoubleFunction`, `DoubleFunction`, `IntFunction`.

Côté algorithmes :
- l'algorithme de la **gare de triage** de Dijkstra (infixe → postfixe), avec les priorités, l'**associativité à droite** de `^`, et des fonctions à un ou deux arguments ;
- l'**évaluation** de la notation polonaise inverse sur une pile.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch8_lambdas.projects.p05_calculator` :
- `Token` (un record avec un enum imbriqué `Kind`) ;
- `Registry`, `Formatter`, `Variables` ;
- **`Calculator`** (le `main`).

**Règle du crescendo :** chapitres 1 à 8. Pas de `Map` pour la table des fonctions : ce sont des tableaux parallèles.

**À quoi sert ce projet ?** Une calculatrice qui lit une expression comme `3 + 4 * 2`, la transforme en notation **postfixée** (`3 4 2 * +`, comme la calculatrice RPN du chapitre 3), puis la calcule. Chaque fonction mathématique est rangée dans un tableau sous forme de lambda.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch8-p05 -sourcepath src/main/java src/main/java/ch8_lambdas/projects/p05_calculator/Calculator.java
java "-Duser.language=fr" -cp build/ch8-p05 ch8_lambdas.projects.p05_calculator.Calculator
```

---

## Tableau de bord

### ☐ Étape 1 — Les jetons et la table des fonctions

**📖 La leçon : un tableau de lambdas.** Un tableau peut contenir des lambdas, comme n'importe quels objets :

```java
String[] noms = {"double", "carre"};
IntUnaryOperator[] ops = {n -> n * 2, n -> n * n};
ops[1].applyAsInt(5)       // 25 : la lambda rangée en case 1
```

Avec deux tableaux parallèles (chapitre 4, projet 6), on retrouve une lambda par son nom.

**👉 À toi :**

- **`record Token(String text, Kind kind)`**, avec `enum Kind { NUMBER, VAR, OPERATOR, FUNCTION, LEFT, RIGHT, COMMA }` imbriqué.
  - **`static Token classify(String text)`** :
    - un chiffre, ou un `-` suivi d'autre chose → `NUMBER` ;
    - `(` → `LEFT`, `)` → `RIGHT`, `,` → `COMMA` ;
    - un caractère de `+-*/^` → `OPERATOR` ;
    - une seule lettre → `VAR` ;
    - sinon → `FUNCTION`.
  - `precedence()` : `^` vaut 3, `*` et `/` valent 2, le reste 1.
  - `toString()` rend `text`.
- **`Registry`** : des tableaux `String[]` et `DoubleUnaryOperator[]` / `DoubleBinaryOperator[]`.
  - Le constructeur enregistre :
    - `sqrt` → `Math::sqrt`, `abs` → `Math::abs`, `neg` → `x -> -x`, `sq` → `x -> x * x` ;
    - `+` → `Double::sum`, `-`, `*` et `/` → des lambdas ;
    - `^` → `Math::pow`, `max` → `Math::max`, `min` → `Math::min`, `hyp` → `Math::hypot`.
  - `isBinary(name)`, `unary(name)` et `binary(name)` font les recherches.
- **`Formatter(int decimals)`** : `String format(double)` arrondit, et affiche un entier sans `.0`.
- **`Variables(String[] names, double[] values)`** : `double lookup(String name)`, ou `NaN` si le nom est inconnu.

### ☐ Étape 2 — La gare de triage et l'évaluation

```
3 + 4 * 2  =>  3 4 2 * +  =  11
2 ^ 3 ^ 2  =>  2 3 2 ^ ^  =  512
max ( 3 , 7 ) * 2  =>  3 7 max 2 *  =  14
...
```

**📖 Conseil :** déroule la « gare de triage » à la main sur `3 + 4 * 2` : trois colonnes, « jeton lu », « pile », « sortie ». Chaque ligne du tableau correspond à un jeton.

**📖 Rappel :** une référence de méthode sur un objet, `vars::lookup` (projet 1, étape 1, 2e ligne du tableau des références).

**👉 À toi :**

- **`Calculator(ToDoubleFunction<String> variables)`**.
- **`Token[] toPostfix(Token[] tokens)`** : la sortie et la pile viennent de `IntFunction<Token[]> newArray = Token[]::new`.
  - nombre ou variable → sortie ;
  - fonction ou `(` → pile ;
  - `,` → dépile vers la sortie jusqu'à `(` ;
  - opérateur → dépile les opérateurs **strictement** plus prioritaires, ou **égaux si le nouveau n'est pas `^`**, puis empile ;
  - `)` → dépile jusqu'à `(`, retire `(`, et si le sommet est une fonction, sors-la ;
  - à la fin, vide la pile.
- **`double evaluate(Token[] postfix)`** : une pile de `double`.
  - Une variable passe par `variables.applyAsDouble(nom)`.
  - Un opérateur binaire (`registry.isBinary`) dépile b puis a ; sinon on applique la fonction unaire au sommet.
- **Dans `main`** :
  - `new Calculator(vars::lookup)` ;
  - `Function<String, Token> classify = Token::classify` ;
  - `DoubleFunction<String> show = fmt::format`, avec `fmt = new Formatter(4)` ;
  - `UnaryOperator<String> clean = String::strip`.
  - Chaque ligne : `expression  =>  postfixe  =  valeur` (deux espaces de chaque côté de `=>` et de `=`).

### ☐ Étape 3 — Les références, une par une

```
references : ref VAR true 42 3.1416 3
```

**📖 Rappel :** les quatre sortes de références de méthodes (projet 1, étape 1). Quelques exemples de plus :

```java
Supplier<StringBuilder> nouveau = StringBuilder::new;           // constructeur sans argument
Function<String, StringBuilder> fabrique = StringBuilder::new;  // le même nom, le constructeur à 1 argument
IntFunction<String[]> tableau = String[]::new;                  // fabrique un tableau de la taille donnée
BiFunction<String, String, Boolean> pareil = String::equalsIgnoreCase;
pareil.apply("A", "a")                                          // true : "A".equalsIgnoreCase("a")
```

C'est le **type de la variable** qui dit à Java quel constructeur, ou quelle surcharge, choisir.

**👉 À toi :**

- Dans cet ordre :
  - `Supplier<StringBuilder> fresh = StringBuilder::new`, puis `fresh.get().append("ref")` ;
  - `BiFunction<String, Token.Kind, Token> make = Token::new` (le constructeur du record), puis `make.apply("pi", VAR).kind()` ;
  - `BiFunction<String, String, Boolean> same = String::equalsIgnoreCase`, appliqué à `("JAVA", "java")` ;
  - `Function<String, Integer> parse = Integer::parseInt`, puis `parse.apply("41") + 1` ;
  - `show.apply(Math.PI)` ;
  - `new Formatter(0).format(2.5)`.
- **Expériences :**
  - `Function<String, String> f = String::substring;` : quelle erreur ? Quelle interface conviendrait ?
  - `Supplier<Token> s = Token::new;` : quelle erreur ?
  - écris l'équivalent lambda de chaque référence de l'étape, et vérifie que le résultat est le même.

---

## Checklist (vérifiée par `Check`)

- `Data.EXPRESSIONS` et `Data.VALUES` ;
- `Math::sqrt`, `Math::pow`, `Double::sum` ;
- `vars::lookup`, `fmt::format`, `String::strip` ;
- `Token::classify`, `Token[]::new`, `StringBuilder::new`, `Token::new` ;
- `String::equalsIgnoreCase`, `Integer::parseInt` ;
- `ToDoubleFunction<String>`, `DoubleFunction<String>` ;
- `DoubleUnaryOperator[]` et `DoubleBinaryOperator[]`.

---

## Sortie attendue complète

```
3 + 4 * 2  =>  3 4 2 * +  =  11
( 1 + 2 ) * ( 3 + 4 )  =>  1 2 + 3 4 + *  =  21
2 ^ 3 ^ 2  =>  2 3 2 ^ ^  =  512
100 / 10 / 5  =>  100 10 / 5 /  =  2
sqrt ( 16 ) + abs ( -3 )  =>  16 sqrt -3 abs +  =  7
max ( 3 , 7 ) * 2  =>  3 7 max 2 *  =  14
hyp ( x , y ) + z  =>  x y hyp z +  =  7
sq ( x - y ) / neg ( z )  =>  x y - sq z neg /  =  -0.5
min ( 2 ^ 10 , 1000 ) - 1  =>  2 10 ^ 1000 min 1 -  =  999
references : ref VAR true 42 3.1416 3
```
