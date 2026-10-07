# Projet 2 — La calculatrice robuste (traduire, chaîner, relancer)

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 11) :**
- une exception vérifiée qui **porte une position** ; une non vérifiée **avec cause** ;
- **traduire** une exception technique en exception du domaine, en **gardant la cause** ;
- `finally` qui s'exécute même après un `return` ;
- la **relance précise** (`catch (Exception e) { …; throw e; }` avec un `throws` étroit) ;
- `Math.addExact`, `subtractExact`, `multiplyExact`, `negateExact`, `absExact` : l'`ArithmeticException` au lieu du débordement silencieux ;
- **attraper une `Error`** (`StackOverflowError`), pour l'observer seulement ;
- **les exceptions et les lambdas :** une interface fonctionnelle qui déclare `throws`, un adaptateur qui enveloppe dans une non vérifiée, et un `Optional` vide à la place d'une erreur.

Côté algorithme : un **analyseur à descente récursive** qui évalue en lisant, et situe chaque erreur.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch11_exceptions.projects.p02_calculator` :
- `SyntaxException`, `EvaluationException`, le record `Token` et `ThrowingFunction<T, R>` ;
- `Parser` ;
- **`Calculator`** (le `main`).

**Règle du crescendo :** chapitres 1 à 11 (voir `PARCOURS.md`).

---

## Tableau de bord

### ☐ Étape 1 — Les types

- **`class SyntaxException extends Exception`** :
  - un champ `int position` et `int position()` ;
  - les constructeurs `(String message, int position)` et `(String message, int position, Throwable cause)`.
- **`class EvaluationException extends RuntimeException`** : construite avec `(String message, Throwable cause)`.
- **`record Token(String text, int position)`**.
- **`@FunctionalInterface interface ThrowingFunction<T, R>`** : `R apply(T value) throws SyntaxException`.

### ☐ Étape 2 — L'analyseur

- **`Parser(String text) throws SyntaxException`** découpe le texte en jetons (`static List<Token> tokenize(String)`) :
  - les espaces sont ignorés ;
  - une suite de chiffres, ou une suite de lettres, forme un jeton ;
  - chacun de `+-*/%(),` est un jeton ;
  - tout autre caractère : `SyntaxException("caractere inattendu '<c>'", i)` ;
  - à la fin, un jeton sentinelle `<fin>` à la position `text.length()`.
- **La grammaire**, une méthode par règle, chacune `throws SyntaxException` :
  ```
  expr    := term (('+' | '-') term)*            -> Math.addExact / Math.subtractExact
  term    := unary (('*' | '/' | '%') unary)*    -> Math.multiplyExact, /, %
  unary   := '-' unary | primary                 -> Math.negateExact
  primary := NOMBRE | NOM '(' [expr (',' expr)*] ')' | '(' expr ')'
  ```
- **`long parseAll()`** : `expr()`, puis `SyntaxException("fin attendue", position)` s'il reste un jeton avant `<fin>`.
- **`expect(String s)`** lève `SyntaxException("'<s>' attendu", position du jeton lu)`.
- **Un nombre :** `Long.parseLong`. Si elle lève une `NumberFormatException`, **traduis** en `SyntaxException("nombre trop grand", position, e)`.
- **Les fonctions :**
  - `max` et `min` : `IllegalArgumentException("<nom> attend au moins 1 argument")` s'il n'y a aucun argument ;
  - `abs` : `IllegalArgumentException("abs attend 1 argument")` si le nombre d'arguments n'est pas 1, puis `Math.absExact` ;
  - une autre fonction : `SyntaxException("fonction inconnue : <nom>", position du nom)`.
- Un autre jeton à la place d'une valeur : `SyntaxException("valeur attendue", position)`.
- **Question :** que rendrait `Math.abs(Long.MIN_VALUE)` ?

### ☐ Étape 3 — Évaluer et afficher

```
1 + 2 * 3 = 7
-(2 + 3) * 4 % 7 = -6
(4 + 6) / (5 - 5) -> evaluation impossible <- java.lang.ArithmeticException: / by zero
2 * (3 + 4 -> syntaxe : ')' attendu (position 10)
          ^
99999999999999999999 -> syntaxe : nombre trop grand (position 0) <- NumberFormatException
^
```
- **Dans `Calculator`**, deux champs : `static int evaluations` et `static final List<String> journal`.
- **`public static long evaluate(String text) throws SyntaxException`** :
  - rend `new Parser(text).parseAll()` ;
  - `catch (ArithmeticException | IllegalArgumentException e)` → `throw new EvaluationException("evaluation impossible", e)` ;
  - `finally` → `evaluations++`.
- **`static long logged(String text) throws SyntaxException`** : appelle `evaluate`. Dans un `catch (Exception e)`, ajoute le nom simple de l'exception au `journal`, puis **`throw e;`**.
  - **Question :** pourquoi `throws SyntaxException` suffit-il, alors qu'on attrape `Exception` ?
- **Pour chaque expression de `Data.EXPRESSIONS`**, appelle `logged` :
  - si tout va bien : `<texte> = <valeur>` ;
  - si `SyntaxException` : `<texte> -> syntaxe : <message> (position <p>)`, suivi de ` <- <nom simple de la cause>` si une cause existe. Puis une 2e ligne avec `" ".repeat(position) + "^"` ;
  - si `EvaluationException` : `<texte> -> <message> <- <cause>` (la cause par son `toString()`).

### ☐ Étape 4 — `Error`, `Optional` et lambdas

```
imbrication 100000 -> StackOverflowError (fille de VirtualMachineError)
valides [7, -6, -1], somme 0
lot interrompu : dans une lambda <- ')' attendu en position 6, resultats [42], evaluations 2
evaluations 27, journal [EvaluationException, SyntaxException, ...]
```
- **L'imbrication :** évalue `"(".repeat(Data.DEPTH) + "1" + ")".repeat(Data.DEPTH)`.
  - `catch (StackOverflowError e)` affiche son nom simple et celui de sa super-classe (`getClass().getSuperclass()`) ;
  - ajoute un `catch (SyntaxException e)` pour le compilateur.
- **`static Optional<Long> tryEvaluate(String text)`** rend `Optional.empty()` pour une `SyntaxException | EvaluationException`. Puis :
  - `Arrays.stream(Data.EXPRESSIONS).map(Calculator::tryEvaluate).flatMap(Optional::stream).toList()` ;
  - affiche la liste et sa somme.
- **`static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R> f)`** : la `Function` rendue attrape la `SyntaxException` et lève `new RuntimeException("dans une lambda", e)`.
- **Le lot :**
  1. note `evaluations` avant ;
  2. lance `Stream.of(Data.BATCH).map(unchecked(Calculator::evaluate)).forEach(results::add)` ;
  3. dans `catch (RuntimeException e)`, si `e.getCause() instanceof SyntaxException s`, affiche la ligne `lot interrompu`, avec les résultats déjà obtenus et le nombre d'évaluations faites.
- **La dernière ligne :** `evaluations <total>, journal <journal>`.
- **Questions :**
  - Pourquoi seulement 2 évaluations dans le lot ?
  - Pourquoi attraper une `Error` est-il une mauvaise idée en vrai ?

---

## Checklist (vérifiée par `Check`)

- `Data.EXPRESSIONS`, `Data.DEPTH`, `Data.BATCH` ;
- `class SyntaxException extends Exception`, `class EvaluationException extends RuntimeException`, `record Token(`, `interface ThrowingFunction<T, R>` ;
- `throws SyntaxException` ;
- les cinq `Math.…Exact(` ;
- `catch (NumberFormatException`, `catch (ArithmeticException | IllegalArgumentException`, `catch (StackOverflowError`, `catch (SyntaxException | EvaluationException` ;
- `finally`, `throw e;`, `throw new RuntimeException(` ;
- `Optional.empty()`, `instanceof SyntaxException`, `.getCause()`.

---

## Sortie attendue complète

```
1 + 2 * 3 = 7
-(2 + 3) * 4 % 7 = -6
max(3, 9, 4) - abs(-12) + min(5, 2) = -1
(4 + 6) / (5 - 5) -> evaluation impossible <- java.lang.ArithmeticException: / by zero
2 * (3 + 4 -> syntaxe : ')' attendu (position 10)
          ^
7 $ 2 -> syntaxe : caractere inattendu '$' (position 2)
  ^
9223372036854775807 + 1 -> evaluation impossible <- java.lang.ArithmeticException: long overflow
99999999999999999999 -> syntaxe : nombre trop grand (position 0) <- NumberFormatException
^
foo(1) + 2 -> syntaxe : fonction inconnue : foo (position 0)
^
min() -> evaluation impossible <- java.lang.IllegalArgumentException: min attend au moins 1 argument
1 + 2 3 -> syntaxe : fin attendue (position 6)
      ^
abs(-9223372036854775807 - 1) -> evaluation impossible <- java.lang.ArithmeticException: Overflow to represent absolute value of Long.MIN_VALUE
imbrication 100000 -> StackOverflowError (fille de VirtualMachineError)
valides [7, -6, -1], somme 0
lot interrompu : dans une lambda <- ')' attendu en position 6, resultats [42], evaluations 2
evaluations 27, journal [EvaluationException, SyntaxException, SyntaxException, EvaluationException, SyntaxException, SyntaxException, EvaluationException, SyntaxException, EvaluationException]
```
