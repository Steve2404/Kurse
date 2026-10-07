# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Token`, `Registry`, `Formatter`, `Variables` et `Calculator` (qui contient le `main`).
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Les 4 sortes de références de méthodes, toutes dans ce projet

| Sorte | Exemple | Lambda équivalente |
|---|---|---|
| méthode **static** | `Math::sqrt`, `Token::classify`, `Integer::parseInt` | `x -> Math.sqrt(x)` |
| méthode d'instance d'un **objet précis** | `vars::lookup`, `fmt::format` | `name -> vars.lookup(name)` |
| méthode d'instance **sur le paramètre** | `String::strip`, `String::equalsIgnoreCase` | `s -> s.strip()`, `(a, b) -> a.equalsIgnoreCase(b)` |
| **constructeur** | `StringBuilder::new`, `Token::new`, `Token[]::new` | `() -> new StringBuilder()`, `n -> new Token[n]` |

---

## Étape 1 — Les jetons et la table des fonctions

**Le code :** [`Token.java`](Token.java), [`Registry.java`](Registry.java), [`Formatter.java`](Formatter.java) et [`Variables.java`](Variables.java).

**Une table de fonctions :** le nom `"sqrt"` est associé à **une fonction**, et l'évaluateur la retrouve par son nom. Ajouter `cube` ne demanderait qu'une ligne dans le constructeur. Les fonctions sont des **données**.

**`Math::max` dans un `DoubleBinaryOperator`** : `Math.max` a 4 surcharges. Le type cible `(double, double) -> double` choisit `max(double, double)`.

**`Double::sum`** est une méthode static existante, qui convient à `(double, double) -> double`. `neg` et `sq` n'ont pas de méthode toute faite, d'où des lambdas.

---

## Étape 2 — La gare de triage et l'évaluation

**Le code :** [`Calculator.java`](Calculator.java).

**La gare de triage (Dijkstra) :** les nombres passent directement à la sortie, et les opérateurs attendent sur une pile. Un opérateur sort quand un opérateur **moins** prioritaire arrive. L'écriture postfixe obtenue n'a plus besoin de parenthèses ni de priorités.

**L'associativité de `^` :** `2 ^ 3 ^ 2` = 2^(3^2) = 2^9 = **512**, et non (2^3)^2 = 64. À priorité **égale**, on dépile pour `+ - * /` (associatifs à gauche : `100 / 10 / 5` = 2), mais **pas** pour `^` (associatif à droite).

**`vars::lookup` comme `ToDoubleFunction<String>` :** la calculatrice ne connaît pas `Variables`. Elle reçoit seulement « une fonction qui, pour un nom, donne une valeur ». La référence est **liée à l'objet `vars`**.

**`Token[]::new` comme `IntFunction<Token[]>` :** une référence de constructeur de **tableau**, qui prend la taille et rend le tableau.

---

## Étape 3 — Les références, une par une

**Le code :** la fin du `main`.

**Les équivalences, vérifiées :** les lambdas `() -> new StringBuilder()`, `(t, k) -> new Tk(t, k)`, `(x, y) -> x.equalsIgnoreCase(y)` et `x -> Integer.parseInt(x)` donnent les mêmes résultats que les références : `ref 1 true 42`.

**`String::equalsIgnoreCase` dans une `BiFunction<String, String, Boolean>` :** pour une méthode d'instance « sur le paramètre », le **1er** paramètre de l'interface devient l'objet, et les suivants les arguments. `same.apply("JAVA", "java")` revient donc à `"JAVA".equalsIgnoreCase("java")`.

**`new Formatter(0).format(2.5)`** donne **`3`** : `Math.round(2.5)` vaut 3, et le résultat entier s'affiche sans `.0`.

**Expériences :**
- **`Function<String, String> f = String::substring;`** :

  ```
  error: incompatible types: invalid method reference
    incompatible types: String cannot be converted to int
  ```

  `Function<String, String>` ne fournit qu'**un** paramètre (le `String`, qui devient l'objet), et `substring` a besoin d'un `int` en plus. Une `BiFunction<String, Integer, String>` convient. Vérifié : `f.apply("lambda", 2)` donne `mbda`.
- **`Supplier<Token> s = Token::new;`** :

  ```
  error: incompatible types: invalid constructor reference
    required: String,int
    found:    no arguments
  ```

  (vérifié sur un record de test.) `Supplier.get()` n'a aucun paramètre, et le seul constructeur de `Token` en exige deux. La référence ne s'adapte que si la forme correspond.
