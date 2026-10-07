# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Printer.java`](Printer.java), [`Json.java`](Json.java) et [`OverloadLab.java`](OverloadLab.java).
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18).

---

## La règle de tout le projet

`javac` choisit une surcharge **à la compilation**, d'après les **types déclarés** des arguments, en trois phases :

| Phase | Conversions permises | Exemple |
|---|---|---|
| 1 | aucune, ou un **élargissement** (primitif : `byte` vers `int` ; référence : `Short` vers `Object`) | `show(b)` (byte) donne `int` |
| 2 | phase 1 **plus** le boxing et l'unboxing | `box(5.0)` donne `Object` (via `Double`) |
| 3 | phase 2 **plus** les varargs | `show()` donne `int...` |

Dans une phase, si plusieurs méthodes conviennent, `javac` prend la **plus spécifique**. S'il n'y en a pas une seule, l'appel est **ambigu**.

---

## Étape 1 — La famille `show`

**Le code :** les six méthodes `show` de [`Printer.java`](Printer.java). Chacune rend le nom de son paramètre.

**Les choix expliqués :**
- `byte`, `short` et `char` s'élargissent vers `int`, `long` et `double`. Le plus **spécifique** est `int`.
- `float` ne s'élargit qu'en `double`.
- `Integer` correspond **exactement**, dès la phase 1.
- **`Short` donne `Object`** : `Short` vers `Object` est un élargissement de **référence**, possible dès la phase 1. Déballer `Short` en `short` pour l'élargir ensuite en `int` demanderait la phase 2, qui n'est jamais atteinte.
- `String` donne `Object`.
- `show()` et `show(1, 2)` ne trouvent rien en phases 1 et 2, donc on passe au varargs.
- **`show(new int[] {1})`** donne `int...` dès la phase 1 : un `int[]` **est** le type du paramètre `int...`.

**Expérience — `Printer.show(null)` :**

```
error: reference to show is ambiguous
  both method show(Integer) in M and method show(int...) in M match
```

`null` convient à **toutes** les versions à référence : `Integer`, `Object` et `int[]` (le type réel de `int...`). `Integer` et `int[]` sont tous deux plus spécifiques qu'`Object`, mais **aucun** des deux n'est un sous-type de l'autre. Il n'y a donc pas de vainqueur.

---

## Étape 2 — `box`, `pick`, `text`

**Le code :** les familles `box`, `pick` et `text` de `Printer`.

**Les choix expliqués :**
- **`box(5)` donne `long`** : l'élargissement (phase 1) passe **avant** le boxing (phase 2).
- **`box(Integer.valueOf(5))` donne `Integer`** : correspondance exacte.
- **`box(5.0)` donne `Object`** : `double` ne s'élargit pas en `long`. En phase 2, il est emballé en `Double`, qui est un `Object`.
- **`box('c')` donne `long`** : un `char` s'élargit en `long` dès la phase 1.
- **`pick(5)` donne `Object`** : un `int` peut devenir un `Integer` (boxing), puis un `Object` (élargissement de référence). Il ne peut **jamais** devenir un `Long`, car il faudrait emballer **puis** élargir. Vérifié : sans la version `Object`, l'appel ne compile pas, avec `error: incompatible types: int cannot be converted to Long`.
- **`pick(5L)` donne `Long`**, par boxing exact.
- **`pick(null)` donne `Long`** : `Long` est plus spécifique qu'`Object`.
- **`text(null)` donne `String`** : `String` est un `CharSequence`, qui est un `Object`. C'est le plus spécifique.
- **`text(hidden)` donne `Object`** : le type **déclaré** est `Object`. Le choix se fait à la compilation, où l'objet réel (`"z"`) est inconnu.
- **`text((CharSequence) "a")` donne `CharSequence`** : un cast change le type déclaré, donc le choix.

---

## Étape 3 — `sum`, `add`, et `static` à côté d'instance

**Le code :** les méthodes `sum`, `add` et `twice` de `Printer`.

**Les choix :**
- `sum(1, 2)` donne `int,int` : phase 1. Le varargs n'arrive qu'en phase 3.
- `add(1, 2)` donne `long,long` : élargissement en phase 1, avant le boxing de la phase 2.

**Expérience — deux méthodes qui ne diffèrent que par le type de retour :**

```
error: method f() is already defined in class M
```

La **signature** d'une méthode, c'est son nom et ses types de paramètres. Le type de retour n'en fait **pas** partie. Lors d'un appel `f();` dont on ignore le résultat, `javac` ne pourrait pas choisir.

**Expérience — `void f(int[] a)` et `void f(int... a)` :**

```
error: cannot declare both f(int...) and f(int[]) in M
```

Pour la JVM, `int...` **est** un `int[]` : les deux signatures sont identiques.

**`twice` static et d'instance :** `twice(String)` et `twice(int)` ont des paramètres différents. Ce sont donc deux surcharges valides, l'une `static`, l'autre non. Le mot `static` ne compte pas dans la signature. Vérifié : deux `twice(int)`, l'un static et l'autre non, sont refusés avec `error: method twice(int) is already defined in class M`.

---

## Étape 4 — Le sérialiseur JSON

**Le code :** toute la classe [`Json.java`](Json.java).

**Question — pourquoi l'ordre des `replace` compte ?** Si l'on échappe `"` **d'abord**, on **crée** des antislashs (`"` devient `\"`). Le `replace` de `\` les doublerait ensuite, pour donner `\\"` : faux. Vérifié sur `a"b` :
- antislash d'abord, puis guillemet : on obtient **`a\"b`** (juste) ;
- guillemet d'abord, puis antislash : on obtient **`a\\"b`** (faux).

Règle : on échappe toujours le caractère d'échappement **en premier**.

**Le piège du type déclaré :** `Object number = 7;` puis `toJson(number)` appelle **`toJson(Object)`**. La surcharge se choisit sur le type **déclaré** (`Object`), pas sur l'objet réel (`Integer`). `naive` montre le résultat sans redistribution : `"7"`, entre guillemets, comme si c'était un texte. `toJson(Object)` répare ça **à l'exécution**, avec `instanceof` : il retrouve le vrai type et rappelle la bonne surcharge.

**`toJson(i.intValue())` et non `toJson(i)` :** `i` est un `Integer`. `toJson(i)` choisirait `toJson(Object)` (élargissement de référence, phase 1, avant tout unboxing), donc une **récursion infinie**. Vérifié : `java.lang.StackOverflowError`. Le `.intValue()` force `toJson(int)`.

**`array(Object... values)`** : `null` devient un **élément** `null` (rendu `null`), et `new int[] {4, 5}` devient **un** élément. Chaque élément passe par `toJson(Object)`.

**L'emboîtement :** `object(field("ville", …))` produit le texte `{"ville":"Paris"}`, qui devient la **valeur** du champ `adresse`. `object()` sans argument donne `{}`, car `String.join` sur un tableau vide rend `""`.
