# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`SpecSheet.java`](SpecSheet.java).
>
> Les messages d'erreur ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18). `javac` affiche aussi la ligne fautive et un `^` sous l'endroit exact ; seule la 1re ligne du message est recopiée ici.

---

## Étape 1 — Les 8 primitifs : tailles et bornes

**Le code de l'étape** (une ligne par type, sur ce modèle) :

```java
System.out.println("byte    : " + Byte.SIZE + " bits, de " + Byte.MIN_VALUE + " a " + Byte.MAX_VALUE + ", defaut " + defaultByte);
```

`SIZE` donne la taille en **bits**. `MIN_VALUE` et `MAX_VALUE` donnent les bornes. Ce sont des champs `static final`, sans parenthèses.

**Question — les enveloppes de `int` et de `char` :** `Integer` et `Character`. Les six autres suivent la règle « nom du primitif avec une majuscule » : `Byte`, `Short`, `Long`, `Float`, `Double`, `Boolean`.

**Question — pourquoi pas `MIN_VALUE` pour `float` et `double` ?** Parce que `Double.MIN_VALUE` n'est **pas** le plus petit nombre négatif. C'est le plus petit nombre **strictement positif** : `4.9E-324`. Le plus petit négatif est `-Double.MAX_VALUE` (`-1.7976931348623157E308`). Afficher « de 4.9E-324 a … » serait donc faux. C'est un piège d'examen classique.

---

## Étape 2 — Les valeurs par défaut

**Le code de l'étape :**

```java
static byte defaultByte;
static short defaultShort;
static int defaultInt;
static long defaultLong;
static float defaultFloat;
static double defaultDouble;
static char defaultChar;
static boolean defaultBoolean;
```

Et pour le `char`, dans `main` :

```java
int minChar = Character.MIN_VALUE;
int maxChar = Character.MAX_VALUE;
int defaultCharCode = defaultChar;
```

**Les valeurs par défaut des champs :** `0` pour `byte`, `short`, `int` et `long` ; `0.0` pour `float` et `double` ; `'\u0000'` (code 0) pour `char` ; `false` pour `boolean` ; `null` pour toute référence.

**Expérience — `int x;` local, puis l'afficher :**

```
error: variable x might not have been initialized
```

**Pourquoi la différence ?**
- Un **champ** peut être lu par n'importe quelle méthode, à n'importe quel moment. `javac` ne peut pas prouver qu'il a été affecté avant. Java lui donne donc une valeur par défaut dès la création.
- Une **variable locale** n'existe que dans sa méthode. `javac` peut suivre chaque chemin du code et exige une affectation **avant toute lecture**. Déclarer `int x;` sans le lire est permis ; c'est la **lecture** qui est refusée.

**Question — pourquoi `int code = unChar;` compile-t-il ?** C'est un **élargissement** : un `char` (16 bits, de 0 à 65535) tient toujours dans un `int` (32 bits). Aucune perte possible, donc aucun cast.

**Dans l'autre sens**, avec une variable :

```
error: incompatible types: possible lossy conversion from int to char
```

Un `int` peut valoir `-1` ou `70000`, qu'un `char` ne peut pas représenter.

---

## Étape 3 — Un nombre, cinq écritures

**Le code de l'étape :**

```java
int decimal = 255;
int binary = 0b1111_1111;
int octal = 0377;
int hexa = 0xFF;
int underscored = 2_5_5;
System.out.println("decimal " + decimal + " | binaire " + binary + " | octal " + octal + " | hexa " + hexa + " | avec _ " + underscored);
System.out.println("255 s'ecrit " + Integer.toBinaryString(255) + " en binaire, " + Integer.toOctalString(255)
        + " en octal, " + Integer.toHexString(255) + " en hexa");
long big = 3_000_000_000L;
float price = 2.5f;
double thousand = 1e3;
System.out.println("litteraux : " + big + " " + price + " " + thousand + " " + 1_000_000);
```

**Les calculs :**
- **Octal** : 255 = 3 × 64 + 7 × 8 + 7, donc `0377`. Autre méthode : `11 111 111` en groupes de 3 bits.
- **Hexa** : 255 = 15 × 16 + 15, donc `0xFF`. Autre méthode : `1111 1111` en groupes de 4 bits.
- `1e3` vaut 1 × 10³. C'est toujours un **`double`**, d'où l'affichage `1000.0`.
- `toHexString` écrit les lettres en **minuscules** : `ff`.

**Expériences :**

| Ligne | Erreur de `javac` | Pourquoi |
|---|---|---|
| `long big = 3000000000;` | `integer number too large` | un littéral sans suffixe est un `int` ; 3 milliards dépasse `Integer.MAX_VALUE`. Le type de la variable (`long`) n'y change rien : le littéral est refusé avant l'affectation. |
| `float f = 2.5;` | `incompatible types: possible lossy conversion from double to float` | un littéral décimal sans suffixe est un `double` |
| `int a = 0b102;` | `';' expected` | `2` n'est pas un chiffre binaire. `javac` lit `0b10`, puis trouve un `2` inattendu. |
| `int b = 08;` | `';' expected` | même chose en octal : `javac` lit `0`, puis trouve un `8` inattendu (les chiffres octaux vont de 0 à 7) |
| `int c = _100;` | `cannot find symbol` (`symbol: variable _100`) | un mot qui commence par `_` est un **identificateur**, pas un nombre. `javac` cherche une variable nommée `_100`. |
| `int d = 100_;` | `illegal underscore` | `_` en fin de nombre |
| `double e = 1_.5;` | `illegal underscore` | `_` collé au point décimal |

**La règle des `_` :** uniquement **entre deux chiffres**. Jamais au début, à la fin, à côté du `.`, ni à côté d'un préfixe ou d'un suffixe (`0x_FF`, `100_L`).

**Question — que vaut `010` ?** **`8`**. Un `0` en tête signifie **octal** : 1 × 8 + 0.

---

## Étape 4 — Les caractères

**Le code de l'étape :**

```java
char letter = 'A';
char unicode = 'A';
char fromCode = 65;
int nextCode = 'B';
System.out.println(letter + " " + unicode + " " + fromCode + " " + nextCode);
```

Affiche `A A A 66` : 0x41 = 4 × 16 + 1 = 65, et `'B'` vaut 66.

**Le piège de l'affichage :** `letter + " " + …` commence par un `char` suivi d'un `String`, donc tout est concaténé. Sans la chaîne, `letter + unicode` serait une **addition** de codes : 65 + 65 = 130.

**Question — pourquoi `char fromCode = 65;` compile, mais pas `int n = 65; char c = n;` ?**
- `65` est une **constante** connue de `javac`, et elle tient dans un `char`. Java autorise alors la conversion implicite vers `char`, `byte` ou `short`.
- `n` est une **variable** : `javac` ne raisonne pas sur sa valeur, seulement sur son type `int`, qui peut ne pas tenir. Erreur : `incompatible types: possible lossy conversion from int to char`.
- Une constante **hors limites** est refusée aussi : `char c = 70000;` et `char c = -1;` donnent la même erreur.

---

## Étape 5 — Les conversions par les classes enveloppes

**Le code de l'étape** (une ligne sur ce modèle) :

```java
System.out.println("Double.valueOf(\"3.99\").intValue() = " + Double.valueOf("3.99").intValue());
```

Le texte affiché recopie l'appel, avec chaque `"` écrit `\"`. Pour la ligne `Long`, l'addition doit être entre parenthèses : `(Long.valueOf("42").longValue() + 1)`. Sans elles, on obtiendrait `421`.

**Questions :**
- **`3.99` → `3` :** `intValue()` **tronque** vers zéro, il ne fait pas d'arrondi. `-3.99` donnerait `-3`.
- **`300` → `44` :** un `byte` garde les 8 bits de poids faible. 300 = 256 + 44, et le bit « 256 » est perdu. Pas d'erreur, pas d'exception.
- **`parseInt("ff", 16)` → `255` :** le 2e argument est la **base**. f = 15, donc 15 × 16 + 15 = 255.
- **`parseInt("-101", 2)` → `-5` :** `101` en base 2 vaut 4 + 0 + 1 = 5, et le signe `-` est accepté.
- **`parseBoolean("oui")` → `false` sans erreur :** sa règle est « `true` si le texte est `true` en ignorant la casse, sinon `false` ». Aucun texte n'est invalide. `parseInt`, lui, doit produire un nombre ; si le texte n'en est pas un, il lève une exception :

```
Exception in thread "main" java.lang.NumberFormatException: For input string: "oui"
```

**À retenir :**

| Méthode | Prend | Rend |
|---|---|---|
| `Integer.parseInt("42")` | `String` | `int` (primitif) |
| `Integer.valueOf("42")` | `String` (ou `int`) | `Integer` (objet) |
| `unInteger.intValue()` | (rien) | `int` |
| `unInteger.byteValue()`, `doubleValue()`… | (rien) | l'autre primitif, avec les règles de conversion |
