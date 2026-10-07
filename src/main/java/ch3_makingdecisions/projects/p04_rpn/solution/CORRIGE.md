# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Rpn.java`](Rpn.java).
>
> Les messages d'erreur et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Les registres et la pile

**Le code de l'étape :**

```java
static Number x = 0;
static Number y = 0;
static Number z = 0;
static Number t = 0;

static void push(Number value) {
    t = z;
    z = y;
    y = x;
    x = value;
}

static void dropAfterOperation(Number result) {
    x = result;
    y = z;
    z = t;
}
```

**Question — `Number x = 0;` range quel objet ?** Un **`Integer`** (vérifié avec `x.getClass()`). Le littéral `0` est un `int`. L'**autoboxing** le transforme en `Integer`, qui **est un** `Number` (sous-classe). Le type **déclaré** est `Number`, le type **réel** de l'objet est `Integer`.

**Pourquoi cet ordre dans `push` ?** Si on écrivait `y = x;` en premier, l'ancien Y serait perdu avant d'être copié dans Z. On décale du haut vers le bas.

---

## Étape 2 — Lire un nombre : le piège du ternaire

**Le code de l'étape :**

```java
static Number parse(String token) {
    double value = Double.parseDouble(token);
    if (value == (int) value) {
        return Integer.valueOf((int) value);
    }
    return Double.valueOf(value);
}
```

Et à la fin du `main` :

```java
Object boxed = 5 > 3 ? Integer.valueOf(1) : Double.valueOf(2.5);
System.out.println("piege du ternaire : " + boxed + " est un " + (boxed instanceof Integer ? "Integer" : "Double"));
```

**Question — pourquoi le ternaire ne rend-il pas un `Integer` ?** Quand les deux branches d'un ternaire sont **numériques** (même emballées), Java applique la **promotion numérique** du chapitre 2 :
1. il **déballe** les deux : `int 1` et `double 2.5` ;
2. il promeut le tout en **`double`** : 1 devient `1.0` ;
3. il **remballe** le résultat en `Double`.

Le type du ternaire est donc **toujours** `double`, quelle que soit la branche choisie. Vérifié : `true ? Integer.valueOf(1) : Double.valueOf(2.5)` donne `1.0`, de classe `Double`. Avec un `if` / `else`, chaque `return` garde son type.

---

## Étape 3 — Le calcul : le pattern matching

**Le code de l'étape :**

```java
static Number compute(String op, Number a, Number b) {
    if (a instanceof Integer i && b instanceof Integer j) {
        return switch (op) {
            case "+" -> i + j;
            case "-" -> i - j;
            case "x" -> i * j;
            default -> {
                if (i % j == 0) {
                    yield i / j;
                }
                yield (double) i / j;
            }
        };
    }
    double p = a.doubleValue();
    double q = b.doubleValue();
    return switch (op) {
        case "+" -> p + q;
        case "-" -> p - q;
        case "x" -> p * q;
        default -> p / q;
    };
}
```

**Question — pourquoi `j` serait inutilisable avec `||` ?**

```
error: cannot find symbol
  symbol:   variable i
error: cannot find symbol
  symbol:   variable j
```

Avec `A || B`, on entre dans le `if` si **l'un ou l'autre** est vrai. Si `A` est vrai, `B` n'est même pas évalué (court-circuit), et `j` n'est jamais affecté. Si c'est `B` qui est vrai, alors `A` était faux, et `i` n'existe pas. Le compilateur n'accepte une variable de pattern que là où elle est **certainement** définie. Avec `&&`, dans le `if`, les deux tests ont réussi, donc les deux variables existent.

**Pourquoi `X=7` et pas `X=7.0` ?** Les branches du `switch` sont des `int` (`i + j`) et un `double` (`(double) i / j`). Pourtant, `3 4 +` affiche bien `7`. Le ternaire, lui, convertissait tout en `double`. La différence vient du **contexte** :
- un `switch` expression placé **directement** dans un `return` (ou une affectation) dont le type est une **référence** (`Number`) convertit **chaque branche séparément** vers ce type : `i + j` devient un `Integer`, `(double) i / j` un `Double` ;
- un ternaire dont les deux branches sont **numériques** (`int`, `Integer`, `double`, `Double`…) applique **toujours** la promotion, quel que soit le contexte.

Vérifié en direct :

| Expression | Résultat |
|---|---|
| `return switch (op) { case "+" -> 3 + 4; default -> 7 / 2.0; };` (méthode qui rend `Number`) | `7`, un `Integer` |
| `return c ? 3 + 4 : 7 / 2.0;` (même méthode) | `7.0` |
| `return c ? Integer.valueOf(7) : Double.valueOf(3.5);` | `7.0` |
| `var v = switch (…) { case 0 -> 3 + 4; default -> 2.5; };` | `7.0` : sans type cible, le switch est promu lui aussi |
| `true ? (Number) Integer.valueOf(7) : Double.valueOf(3.5)` | `7` : le cast en `Number` fait de la 1re branche une référence non numérique, donc plus de promotion |

**À retenir :** avec des nombres emballés, un ternaire est un piège. Préfère un `if` / `else`, comme dans `parse`.

---

## Étape 4 — Zéro et changement de signe : la portée de flux

**Le code de l'étape :**

```java
static boolean isZero(Number n) {
    if (!(n instanceof Double d)) {
        return n.intValue() == 0;
    }
    return d == 0.0;
}

static Number negate(Number n) {
    if (n instanceof Integer i) {
        return -i;
    } else if (n instanceof Double d) {
        return -d;
    }
    return n;
}
```

**Question — pourquoi `d` est-il utilisable après le `if` ?** C'est la **portée de flux** (*flow scoping*). Le compilateur raisonne sur les chemins possibles :
- si `n instanceof Double d` est **faux**, la condition `!(…)` est vraie, et le corps se termine par `return` ;
- donc, **si** l'exécution atteint la ligne après le `if`, c'est que le test était **vrai**, et `d` a été affecté.

Sans le `return` dans le `if`, le compilateur refuserait `d` : `error: cannot find symbol`. Vérifié avec `if (n instanceof Double d) { } System.out.println(d);`.

---

## Étape 5 — La boucle et le `switch` des commandes

**Le code de l'étape :**

```java
int step = 0;
for (String token : args) {
    step++;
    String note = "";
    switch (token) {
        case "+", "-", "x" -> dropAfterOperation(compute(token, y, x));
        case "/" -> {
            if (isZero(x)) {
                note = " (ERREUR division par zero, pile inchangee)";
            } else {
                dropAfterOperation(compute(token, y, x));
            }
        }
        case "DUP" -> push(x);
        case "SWAP" -> {
            Number tmp = x;
            x = y;
            y = tmp;
        }
        case "DROP" -> dropAfterOperation(y);
        case "CHS" -> x = negate(x);
        case "CLR" -> { x = 0; y = 0; z = 0; t = 0; }
        default -> push(parse(token));
    }
    System.out.println(step + ". " + token + " -> " + stack() + note
            + (x instanceof Double ? " (X decimal)" : ""));
}
```

**La trace des premiers jetons :**
- `3 4 +` : 3 + 4 = 7.
- `2 x` : 7 × 2 = 14.
- `DUP x` : 14 × 14 = 196.
- `7 /` : 196 / 7 = **28**, division exacte, donc un `Integer`.
- `2.5 +` : 28 + 2.5 = **30.5**, un `Double` est en jeu, donc un `Double`.

**Expérience — `case Integer i ->` dans un `switch` sur un `Number`, avec `javac` 17 :**

```
error: patterns in switch statements are a preview feature and are disabled by default.
  (use --enable-preview to enable patterns in switch statements)
```

En Java 17, les **patterns dans `switch`** sont une fonctionnalité en *preview*. Ils sont devenus définitifs en Java 21. Pour l'examen 17 : `instanceof` avec variable est **définitif** (depuis Java 16), mais pas `case Type var`.

**Un autre piège de pattern (vérifié) :** `Integer n = 1; if (n instanceof Integer i)` ne compile pas en Java 17. On obtient `error: expression type Integer is a subtype of pattern type Integer` : un test toujours vrai est refusé.
