# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `SyntaxException`, `EvaluationException`, `Token`, `ThrowingFunction`, `Parser` et `Calculator`.
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Les types

**Le code :** [`SyntaxException.java`](SyntaxException.java), [`EvaluationException.java`](EvaluationException.java), [`Token.java`](Token.java) et [`ThrowingFunction.java`](ThrowingFunction.java).

**Deux natures d'erreur :**
- une erreur de **syntaxe** est **prévisible** : l'utilisateur a mal tapé. Elle est vérifiée, et l'appelant **doit** la traiter ;
- une erreur d'**évaluation** (division par zéro, dépassement) vient des valeurs. Elle est non vérifiée.

**La position dans l'exception :** une exception est un objet comme un autre. On peut y ranger des données utiles, comme la position pour placer le `^`.

---

## Étape 2 — L'analyseur

**Le code :** [`Parser.java`](Parser.java).

**Traduire une exception :** `NumberFormatException` est une exception **technique**. On la transforme en `SyntaxException` (l'erreur du **domaine**), en gardant l'originale comme **cause**. L'affichage montre les deux : `nombre trop grand … <- NumberFormatException`.

**Les `…Exact`** (`addExact`, `multiplyExact`, `negateExact`, `absExact`) lèvent une `ArithmeticException` au lieu de **déborder en silence**. `9223372036854775807 + 1` donne `long overflow`.

**Question — `Math.abs(Long.MIN_VALUE)` ?** **`-9223372036854775808`**, un nombre **négatif** (vérifié) ! −2⁶³ n'a pas d'opposé représentable dans un `long` (le max est 2⁶³ − 1) : le calcul déborde et retombe sur lui-même. `Math.absExact` lève à la place : `ArithmeticException: Overflow to represent absolute value of Long.MIN_VALUE`.

**`%` et le signe :** `-20 % 7` vaut **−6** (vérifié) : le signe suit le dividende (chapitre 2).

---

## Étape 3 — Évaluer et afficher

**Le code :** `evaluate`, `logged` et la boucle du `main` de [`Calculator.java`](Calculator.java).

**`finally` après un `return`** : le `return` de `evaluate` calcule sa valeur, **puis** le `finally` s'exécute, **puis** la méthode rend la valeur. `evaluations++` a donc lieu dans tous les cas : succès, exception attrapée ou exception relancée.

**Question — pourquoi `throws SyntaxException` suffit dans `logged` ?** C'est la **relance précise** (Java 7). Si le paramètre d'un `catch` n'est **pas réaffecté**, `throw e` ne peut relancer que ce que le `try` pouvait lever. Ici, la seule exception **vérifiée** possible est `SyntaxException`, et les autres sont non vérifiées. Vérifié : en réaffectant `e` dans le `catch`, `javac` perd cette précision et exige de déclarer `Exception` : `error: unreported exception Exception; must be caught or declared to be thrown`.

**Le `^` sous la position :** `" ".repeat(position) + "^"`. Pour `2 * (3 + 4`, il manque `)` en position 10, celle de la sentinelle `<fin>`, qui est la longueur du texte.

---

## Étape 4 — `Error`, `Optional` et lambdas

**Le code :** la fin du `main`, `tryEvaluate` et `unchecked`.

**`StackOverflowError`** : 100 000 parenthèses imbriquées font 100 000 appels récursifs. La pile déborde. C'est une `Error`, fille de `VirtualMachineError`, et non une `Exception`.

**`Optional` pour les erreurs prévues :** `tryEvaluate` transforme chaque échec en `Optional.empty()`, et le stream continue avec les suivants. On garde 7, −6 et −1.

**Envelopper pour une lambda :** `Function.apply` ne déclare aucune exception vérifiée. `unchecked` adapte une fonction qui en lève une, en l'enveloppant dans une `RuntimeException`. La cause est conservée, et le `catch` du `main` la retrouve avec `e.getCause() instanceof SyntaxException s`.

**Question — pourquoi seulement 2 évaluations dans le lot ?** Un stream traite les éléments **un par un**, et chacun traverse **toute** la chaîne avant le suivant. `"6 * 7"` donne 42, ajouté aux résultats. `"(1 + 2"` lève une exception, qui sort du `forEach` et **arrête** le stream. `"100 / 4"` n'est **jamais** évalué.

**Question — pourquoi attraper une `Error` est une mauvaise idée ?** Une `Error` signale un problème **grave de la JVM** : pile ou mémoire épuisée, classe introuvable… Le programme est dans un état **incertain**, et il n'y a en général rien de raisonnable à faire, à part s'arrêter. Ici, on l'attrape seulement pour l'**observer**.

**Le total de 27 évaluations :** 12 dans la boucle principale, 1 pour l'imbrication, 12 dans `tryEvaluate`, puis 2 dans le lot.
