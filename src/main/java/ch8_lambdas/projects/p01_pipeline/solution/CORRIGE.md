# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Step.java`](Step.java), [`Memo.java`](Memo.java) et [`PipelineApp.java`](PipelineApp.java).
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — L'interface `Step`

**Le code :** [`Step.java`](Step.java).

**Une interface fonctionnelle** a **exactement une** méthode abstraite. `Step` en hérite une (`apply`, de `Function` via `UnaryOperator`), et n'en ajoute aucune : les méthodes `default`, `static` et `private` ne comptent pas. Une lambda peut donc **être** une `Step`.

**Les syntaxes de lambda du tableau :**

| Forme | Exemple |
|---|---|
| un paramètre sans type, sans parenthèses | `s -> s.strip()` |
| un paramètre typé, parenthèses obligatoires | `(String s) -> s.toUpperCase()` |
| `var` (Java 11+) | `(var s) -> s.replaceAll(" +", " ")` |
| corps en **bloc** : accolades, `return`, `;` | `s -> { …; return sb.toString(); }` |
| référence de méthode d'instance « sur le paramètre » | `String::toLowerCase` ≡ `s -> s.toLowerCase()` |
| référence de méthode static | `Step::title` ≡ `s -> Step.title(s)` |

**`then` est un combinateur :** il ne calcule rien, il **construit** une nouvelle lambda qui appliquera `this` puis `next`. `parse` empile ces lambdas, et rien ne s'exécute avant le premier `apply`.

**`yield` avec un bloc :** la branche `repeat` calcule `times`, puis `yield` rend la lambda. `times` est capturé, et il est effectively final.

---

## Étape 2 — Appliquer les pipelines

**Le code :** la 1re boucle du `main` de [`PipelineApp.java`](PipelineApp.java).

**Question — pourquoi `rle | unrle` et `caesar 3 | caesar 23` redonnent le texte ?**
- **`caesar 3 | caesar 23`** : chaque lettre est décalée de 3, puis de 23, soit 26 ≡ 0 modulo 26. C'est un tour complet de l'alphabet.
- **`rle | unrle`** : `decode` est l'**inverse** d'`encode` : chaque `<nombre><caractère>` redonne la série. Mais seulement si le texte ne contient **pas de chiffres** ! Vérifié : `"a11b"` est encodé `1a211b`, et `decode` lit ensuite **211** fois `b`. L'aller-retour échoue. Les trois textes de `Data` n'ont pas de chiffre, c'est pour ça qu'ils passent.

---

## Étape 3 — Composer des `Function`

**Le code :** le bloc « andThen / compose » et la ligne `types`.

**`andThen` contre `compose` :**
- `exclaim.andThen(doubled)` : `"ok"` donne `"ok!"`, puis `"ok!ok!"` ;
- `exclaim.compose(doubled)` : `"ok"` donne `"okok"`, puis `"okok!"`.

**`andThen` peut changer de type :** `Function<String, Integer>` suivie de `Function<Integer, String>` donne une `Function<String, String>`. `"lambda"` donne 6, puis `******`.

**Les interfaces de `java.util.function` de ce projet :**

| Interface | Méthode | Exemple |
|---|---|---|
| `Function<T, R>` | `R apply(T)` | `length` |
| `UnaryOperator<T>` | une `Function<T, T>` | `brackets` |
| `BiFunction<T, U, R>` | `R apply(T, U)` | `repeat` |
| `BinaryOperator<T>` | une `BiFunction<T, T, T>` | `longer`, `minBy(…)` |

**`BinaryOperator.minBy(comparateur)`** est une fabrique **static** : elle rend un opérateur qui garde le plus petit selon le comparateur. Ici, c'est la longueur, donc `kiwi`.

---

## Étape 4 — État et mémoïsation

**Le code :** le bloc « compteur » et [`Memo.java`](Memo.java).

**Question — pourquoi pas `int applied = 0;` ?** Une lambda ne peut **lire** que des variables locales *effectively final*, et elle ne peut **jamais** les modifier :

```
error: local variables referenced from a lambda expression must be final or effectively final
```

(vérifié avec `n++`.) La lambda peut être exécutée **plus tard**, après la fin de la méthode qui l'a créée, alors que la variable locale n'existe plus. Java capture donc une **copie de sa valeur**, et la modifier n'aurait pas de sens. Avec `int[] applied`, la **référence** est capturée et ne change pas, mais la case du tableau, elle, est partagée.

**`Memo` modifie des champs :** un champ n'est pas une variable locale. La lambda accède à `this.calls` par la référence `this`, capturée, et peut donc le modifier.

**Les 6 appels :** `" a  b "`, `"c d"`, `" a  b "`, `"c d"`, `" a  b "`, `"e"`. Les 3e, 4e et 5e sont déjà en cache : **3 depuis le cache**.

**Expériences :**

| Expérience | Erreur de `javac` (vérifiée) |
|---|---|
| `int n = 0;` puis `n++` dans une lambda | `local variables referenced from a lambda expression must be final or effectively final` |
| une 2e méthode abstraite dans `Step` | `Unexpected @FunctionalInterface annotation` `Step is not a functional interface` `multiple non-overriding abstract methods found in interface Step` |
| `(s) -> return s;` | `illegal start of expression` : sans accolades, le corps est une **expression**, et `return` est une instruction. Il faut écrire `s -> s`, ou `s -> { return s; }` |
| `(String s, t) -> s` | `invalid lambda parameter declaration` `(cannot mix implicitly-typed and explicitly-typed parameters)` : tous typés, ou aucun |

**`@FunctionalInterface` est facultatif**, mais il fait vérifier par `javac` qu'il y a bien une seule méthode abstraite. Une lambda marcherait sans l'annotation.
