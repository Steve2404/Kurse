# Projet 5 — Le laboratoire des surcharges et le sérialiseur JSON

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

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

---

## Tableau de bord

### ☐ Étape 1 — La famille `show`

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
- **Les surcharges `static String toJson(…)`**, sept en tout : `int`, `boolean`, `double`, `String`, `int[]`, `int[][]`, `String[]`. Plus **`toJson(Object)`**.
  - **`toJson(String)`** : `null` → `null` ; sinon entre guillemets. Échappe d'abord `\` en `\\`, puis `"` en `\"`, puis le saut de ligne en `\n`.
    - **Question :** pourquoi l'ordre des `replace` compte-t-il ?
  - **`toJson(int[][])`** réutilise `toJson(int[])` pour chaque ligne.
- **Le piège :** `Object number = 7;` puis `Json.toJson(number)` appelle **`toJson(Object)`**, pas `toJson(int)`.
  - Une méthode `naive(Object o)`, qui fait `"\"" + o + "\""`, montre le mauvais résultat `"7"`.
  - `toJson(Object)` doit donc **redistribuer** à la main avec `instanceof` et le pattern matching (chapitre 3) : `String`, `Integer`, `Boolean`, `Double`, `int[]`, `String[]`, `null`.
- **`array(Object... values)`** : chaque élément est un `Object`, donc il passe par `toJson(Object)`.
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
