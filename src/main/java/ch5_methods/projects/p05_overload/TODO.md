# Projet 5 — Le laboratoire des surcharges et le sérialiseur JSON

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 5) :** la **surcharge** (*overloading*) et l'ordre exact dans lequel Java choisit une version :
1. le type **exact**, ou un **élargissement** primitif, ou une **référence plus générale** — sans boxing ni varargs ;
2. avec **boxing / unboxing** ;
3. avec **varargs**.

Dans chaque phase, Java prend la version **la plus spécifique**. Aussi au programme :
- `null` ;
- une surcharge `static` à côté d'une surcharge d'instance ;
- le **piège majeur** : la surcharge est choisie **à la compilation**, d'après le **type déclaré** de l'argument.

Côté algorithmes : un **sérialiseur JSON** récursif (échappement des caractères, tableaux 2D, objets imbriqués).

**Ce qui est donné :** `Check.java` seulement.

**Ce que TU crées :** dans le paquet `ch5_methods.projects.p05_overload`, trois classes :
- **`Printer`** : les familles de surcharges ;
- **`Json`** : le sérialiseur ;
- **`OverloadLab`** : le `main`.

Chaque surcharge de `Printer` **rend son nom** (`"int"`, `"Integer"`, `"int..."`…).

**Règle du crescendo :** chapitres 1 à 5. Pas de constructeur, de collection ni de `try/catch`.

**Méthode de travail :** pour chaque appel, écris ta **prédiction** en commentaire **avant** de lancer. Compte tes erreurs.

**Tes outils pour ce projet** (pas d'arguments, pas de `Data`) :

```
javac -d build/ch5-p05 -sourcepath src/main/java src/main/java/ch5_methods/projects/p05_overload/OverloadLab.java
java "-Duser.language=fr" -cp build/ch5-p05 ch5_methods.projects.p05_overload.OverloadLab
```

---

## Tableau de bord

### ☐ Étape 1 — La famille `show`

**📖 La leçon : la surcharge, plusieurs méthodes du même nom.** Une classe peut avoir plusieurs méthodes du même nom, si leurs **paramètres** diffèrent (en nombre ou en type). Java choisit selon les valeurs passées :

```java
static String decris(int x)    { return "int"; }
static String decris(double x) { return "double"; }
static String decris(String s) { return "String"; }

decris(5)      // "int"
decris(2.5)    // "double"
decris("a")    // "String"
decris('x')    // "int" : pas de version char, le char s'élargit vers int
```

**📖 La leçon : comment Java choisit, en 4 phases.** Il essaie une phase après l'autre, et s'arrête **dès qu'une phase trouve** une méthode. Dans une phase, il prend la plus **précise** :
1. **le type exact**, ou un **élargissement** sans boîte : `byte` → `short` → `int` → `long` → `float` → `double`, ou une classe vers sa classe mère (`String` → `Object`) ;
2. **avec emballage ou déballage** (`int` ↔ `Integer`) ;
3. **avec varargs** (`int...`) ;
4. rien ne convient : erreur de compilation.

```java
static String g(double d)  { return "double"; }
static String g(Integer i) { return "Integer"; }
g(3)                         // "double" : phase 1 (élargissement) avant phase 2 (emballage)

static String h(Object o)  { return "Object"; }
static String h(int... v)  { return "int..."; }
h(3)                         // "Object" : phase 2 (emballage en Integer, qui est un Object) avant phase 3
```

**Méthode :** pour chaque appel de l'étape, écris en commentaire **la phase** qui a trouvé, puis vérifie en lançant.

**👉 À toi :**

Six surcharges `static String show(…)` : `int`, `long`, `double`, `Integer`, `Object`, `int...`.

```
show : byte int, short int, char int, int int, long long, float double
show : Integer Integer, Short Object, String Object, rien int..., 1,2 int..., int[] int...
```
- `byte`, `short` et `char` s'élargissent vers le plus petit type disponible : `int`.
- `float` → `double`.
- **Le piège `Short` :** `Short` → `Object` est un élargissement **de référence** (phase 1). Il gagne sur l'unboxing `Short` → `short` → `int` (phase 2).
- `show(new int[] {1})` : un `int[]` correspond **exactement** au paramètre `int...`.
- **Expérience :** `Printer.show(null)` → ambigu entre `Integer` et `int[]`. Lis le message.

### ☐ Étape 2 — `box`, `pick`, `text`

**📖 Rappel :** les 4 phases de l'étape 1. Retiens aussi : c'est le **type déclaré** de la variable qui compte, pas l'objet réellement rangé dedans. Une variable `Object` qui contient un texte choisit la version `Object`.

**👉 À toi :**

Les surcharges : `box(long)`, `box(Integer)`, `box(Object)` ; `pick(Long)`, `pick(Object)` ; `text(String)`, `text(CharSequence)`, `text(Object)`.

```
box : 5 long, Integer Integer, 5.0 Object, 'c' long
pick : 5 Object, 5L Long, null Long
text : "a" String, builder CharSequence, null String, Object "z" Object, cast CharSequence
```
- **L'élargissement passe avant le boxing** : `box(5)` → `long`, et non `Integer`.
- `5.0` devient un `Double`, qui est un `Object`.
- **`int` ne devient jamais un `Long`** : boxing puis élargissement primitif, c'est interdit. `pick(5)` donne donc `Integer` → `Object`.
- **`null`** va à la version la plus spécifique : `String` est un `CharSequence`, qui est un `Object`.
- `Object hidden = "z";` → `text(hidden)` choisit `Object`. C'est le type **déclaré** qui compte, pas l'objet réel.

### ☐ Étape 3 — `sum`, `add`, et `static` à côté d'instance

```
sum : 1,2 int,int, 1,2,3 int... ; add : 1,2 long,long, Integer Integer,Integer ; twice 42 abab
```

**📖 La leçon : ce qui compte pour distinguer deux surcharges.** Seuls le **nom** et la **liste des types des paramètres** (la « signature ») comptent. L'expérience te fait vérifier si le type rendu, ou `[]` contre `...`, suffisent.

**👉 À toi :**

- `sum(int a, int b)` et `sum(int... v)` : les paramètres fixes gagnent.
- `add(long, long)` et `add(Integer, Integer)` : l'élargissement gagne.
- `String twice(String s)` est **d'instance** (appelée sur `new Printer()`), et `static String twice(int n)` coexiste : signatures différentes.
- **Expériences :**
  - deux méthodes qui ne diffèrent que par le **type de retour** : quelle erreur ?
  - `void f(int[] a)` et `void f(int... a)` dans la même classe : quelle erreur ?

### ☐ Étape 4 — Le sérialiseur JSON

```
42 true 2.5 "il dit \"oui\"\\non"
[1,2,3] [[1,2],[],[3]] ["a","b\"c"]
type declare Object : "7" 7 "sept" null
[1,"deux",3.0,false,null,[4,5],["six"]]
{"nom":"Ada","age":36,"langages":["Java","C"],"notes":[[18,15],[12]],"adresse":{"ville":"Paris"},"vide":{}}
```

**📖 Rappel :** le pattern matching `instanceof Type variable` (chapitre 3, projet 4). Les guillemets et l'antislash dans un texte s'écrivent `\"` et `\\` (chapitre 1, projet 2). `replace` remplace toutes les occurrences (chapitre 4, projet 1).

**👉 À toi :**

- **Les surcharges `static String toJson(…)`**, sept en tout : `int`, `boolean`, `double`, `String`, `int[]`, `int[][]`, `String[]`. Plus **`toJson(Object)`**.
  - **`toJson(String)`** : `null` → `null` ; sinon entre guillemets. Échappe d'abord `\` en `\\`, puis `"` en `\"`, puis le saut de ligne en `\n`.
    - **Question :** pourquoi l'ordre des `replace` compte-t-il ?
  - **`toJson(int[][])`** réutilise `toJson(int[])` pour chaque ligne.
- **Les données des trois premières lignes :**
  - ligne 1 : `toJson(42)`, `toJson(true)`, `toJson(2.5)` et `toJson("il dit \"oui\"\\non")`. Dans le code Java, ce texte contient de vrais guillemets et un antislash suivi de `non` ;
  - ligne 2 : `toJson(new int[] {1, 2, 3})`, `toJson(new int[][] {{1, 2}, {}, {3}})` et `toJson(new String[] {"a", "b\"c"})` ;
  - ligne 3 : `naive(number)`, `toJson(number)`, `toJson(text)` (avec `Object text = "sept"`) et `toJson((Object) null)`.
- **Le piège :** `Object number = 7;` puis `Json.toJson(number)` appelle **`toJson(Object)`**, pas `toJson(int)`.
  - Une méthode `naive(Object o)`, qui fait `"\"" + o + "\""`, montre le mauvais résultat `"7"`.
  - `toJson(Object)` doit donc **redistribuer** à la main avec `instanceof` et le pattern matching (chapitre 3) : `String`, `Integer`, `Boolean`, `Double`, `int[]`, `String[]`, `null`.
- **`array(Object... values)`** : chaque élément est un `Object`, donc il passe par `toJson(Object)`. Appelle-la avec `1, "deux", 3.0, false, null, new int[] {4, 5}, new String[] {"six"}`.
- **`field(String key, String json)`** et **`object(String... fields)`** construisent un objet JSON. Ils s'emboîtent : un objet peut contenir un objet, et `object()` sans argument donne `{}`.

---

## Checklist (vérifiée par `Check`)

- 6 `show`, 3 `box`, 2 `pick` et 3 `text` ;
- `show(int... `, `pick(Long `, `text(CharSequence ` ;
- `Short` ;
- 7 `toJson` et `toJson(Object ` ;
- `instanceof`, `String...`, `Object...` ;
- une méthode d'instance appelée sur `new X()`.

---

## Sortie attendue complète

```
show : byte int, short int, char int, int int, long long, float double
show : Integer Integer, Short Object, String Object, rien int..., 1,2 int..., int[] int...
box : 5 long, Integer Integer, 5.0 Object, 'c' long
pick : 5 Object, 5L Long, null Long
text : "a" String, builder CharSequence, null String, Object "z" Object, cast CharSequence
sum : 1,2 int,int, 1,2,3 int... ; add : 1,2 long,long, Integer Integer,Integer ; twice 42 abab
42 true 2.5 "il dit \"oui\"\\non"
[1,2,3] [[1,2],[],[3]] ["a","b\"c"]
type declare Object : "7" 7 "sept" null
[1,"deux",3.0,false,null,[4,5],["six"]]
{"nom":"Ada","age":36,"langages":["Java","C"],"notes":[[18,15],[12]],"adresse":{"ville":"Paris"},"vide":{}}
```
