# Projet 2 — La fiche technique des types primitifs

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 1) :**
- les **8 types primitifs** : taille, bornes, valeurs par défaut ;
- les **littéraux** : décimal, binaire `0b`, octal `0`, hexadécimal `0x`, `_`, `L`, `f`, notation `1e3`, `char` en `'\u…'` ;
- les **classes enveloppes** : constantes `SIZE`, `MIN_VALUE` et `MAX_VALUE`, conversions `parseXxx`, `valueOf` et `xxxValue()`, bases `toBinaryString`…

**Ce qui est donné :** `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch1_buildingblocks.projects.p02_specsheet`. La classe du `main` s'appelle **`SpecSheet`**.

**Règle du crescendo :** seulement le chapitre 1, plus `+ - * / %`. Pas de `if`, pas de boucle, pas de méthodes de `String`, et **pas de cast** `(int)` / `(char)` (chapitre 2). `Check` les refuse. Chaque valeur vient d'une **constante d'enveloppe**, d'un **champ non initialisé** ou d'un **littéral**, jamais d'un nombre recopié à la main.

---

## Le problème

Tu écris l'aide-mémoire que tu aurais aimé avoir le premier jour. Le programme imprime la fiche de chaque type primitif, puis montre un même nombre écrit de cinq façons, les caractères, et les conversions (parfois surprenantes) des classes enveloppes.

**La règle d'or :** n'écris **jamais** `127`, `-32768` ou `65535` en dur. Ces nombres doivent **venir de Java**. Sinon, tu n'apprends rien.

---

## Tableau de bord

### ☐ Étape 1 — Les 8 primitifs : tailles et bornes

```
byte    : 8 bits, de -128 a 127, defaut 0
...
long    : 64 bits, de -9223372036854775808 a 9223372036854775807, defaut 0
float   : 32 bits, defaut 0.0
```
- **Taille et bornes :** chaque classe enveloppe a des constantes `SIZE`, `MIN_VALUE` et `MAX_VALUE`.
  - **Question :** quelle est l'enveloppe de `int` ? Et de `char` ? (Leurs noms ne suivent pas la règle des 6 autres.)
- **`float` et `double` :** pourquoi n'affiche-t-on pas `MIN_VALUE` ? Lis la Javadoc de `Double.MIN_VALUE` : ce n'est pas le plus petit nombre négatif.

### ☐ Étape 2 — Les valeurs par défaut

```
..., defaut 0
char    : 16 bits, de 0 a 65535, defaut (code) 0
boolean : taille non fixee par Java, defaut false
```
- Les valeurs par défaut ne s'écrivent pas : elles viennent de **8 champs que tu n'initialises pas**.
  - **Expérience :** déclare une variable **locale** `int x;` et affiche-la. Que dit `javac` ? Pourquoi les champs ont-ils une valeur par défaut, et pas les variables locales ?
- **Le `char` :** sa valeur par défaut est le caractère de code 0, invisible à l'affichage. Pour voir son **code**, affecte le `char` à une variable `int`, sans cast.
  - **Question :** pourquoi cette affectation compile-t-elle ? Que dirait `javac` dans l'autre sens (`int` vers `char`) avec une variable ?
- **Les bornes du `char` :** même technique pour `Character.MIN_VALUE` et `MAX_VALUE`.

### ☐ Étape 3 — Un nombre, cinq écritures

```
decimal 255 | binaire 255 | octal 255 | hexa 255 | avec _ 255
255 s'ecrit 11111111 en binaire, 377 en octal, ff en hexa
litteraux : 3000000000 2.5 1000.0 1000000
```
- **La première ligne :** déclare cinq `int` qui valent tous 255, chacun écrit avec un littéral différent :
  - en décimal ;
  - en **binaire avec des `_`** ;
  - en **octal** ;
  - en **hexadécimal** ;
  - en **décimal avec des `_`**.
- **La deuxième ligne :** les conversions inverses viennent de trois méthodes statiques d'`Integer`.
- **La troisième ligne :**
  - un `long` de 3 milliards **avec `_` et le suffixe** obligatoire ;
  - un `float` littéral ;
  - un `double` en **notation scientifique** ;
  - un million écrit avec des `_`.
- **Expériences :** pour chacune, lis l'erreur de `javac`, puis retire la ligne.
  1. `long big = 3000000000;` (sans `L`)
  2. `float f = 2.5;` (sans `f`)
  3. `int a = 0b102;`
  4. `int b = 08;`
  5. `int c = _100;`, puis `int d = 100_;`, puis `double e = 1_.5;`
  6. **Question :** que vaut `010` ? Prédis, puis vérifie.

### ☐ Étape 4 — Les caractères

```
A A A 66
```
- Trois `char` qui valent tous `'A'`, écrits de trois façons :
  - un littéral caractère ;
  - un **échappement Unicode** `'\u…'` (le code de `A` est 41 en hexadécimal) ;
  - un **nombre entier** (le code décimal de `A`).
- Puis le code de `'B'`, obtenu sans le recopier.
- **Question :** `char fromCode = 65;` compile. Pourquoi `int n = 65; char c = n;` ne compile-t-il pas ? (Le chapitre 2 généralisera cette règle.)

### ☐ Étape 5 — Les conversions par les classes enveloppes

```
Double.valueOf("3.99").intValue() = 3
Integer.valueOf(300).byteValue() = 44
Integer.parseInt("ff", 16) = 255, Integer.parseInt("-101", 2) = -5
...
```
- Chaque ligne affiche **l'appel lui-même**, puis son résultat. Attention aux guillemets **dans** une chaîne : il faut les échapper.
- **Questions, à calculer à la main AVANT d'exécuter :**
  - pourquoi `3.99` donne-t-il `3` et pas `4` ?
  - pourquoi `300` en `byte` donne-t-il `44` ? Calcule `300 - 256`.
  - que rend `parseInt` avec une base 16 ? Avec une base 2 et un signe `-` ?
  - pourquoi `parseBoolean("oui")` rend-il `false` sans erreur, alors que `parseInt("oui")` lève une exception ?
- **Différence clé :** `parseXxx` rend un **primitif**, `valueOf` rend un **objet** enveloppe. `xxxValue()` passe de l'objet au primitif.

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `Byte.SIZE`, `Byte.MIN_VALUE`, `Short.MAX_VALUE`, `Integer.MAX_VALUE`, `Long.MIN_VALUE`, `Float.SIZE`, `Double.SIZE` | 1 | ☐ |
| `Character.SIZE`, `Character.MAX_VALUE` | 1, 2 | ☐ |
| littéraux binaire avec `_`, octal, hexadécimal | 3 | ☐ |
| littéraux `long` avec `_` et `L`, `float`, `1e3` | 3 | ☐ |
| `Integer.toBinaryString`, `toOctalString`, `toHexString` | 3 | ☐ |
| échappement `'\u…'` | 4 | ☐ |
| `intValue`, `byteValue`, `doubleValue`, `longValue` | 5 | ☐ |
| `Integer.parseInt`, `Double.parseDouble`, `Float.valueOf`, `Boolean.parseBoolean`, `Short.parseShort` | 5 | ☐ |

---

## Sortie attendue complète

```
=== LES 8 TYPES PRIMITIFS ===
byte    : 8 bits, de -128 a 127, defaut 0
short   : 16 bits, de -32768 a 32767, defaut 0
int     : 32 bits, de -2147483648 a 2147483647, defaut 0
long    : 64 bits, de -9223372036854775808 a 9223372036854775807, defaut 0
float   : 32 bits, defaut 0.0
double  : 64 bits, defaut 0.0
char    : 16 bits, de 0 a 65535, defaut (code) 0
boolean : taille non fixee par Java, defaut false
=== UN NOMBRE, CINQ ECRITURES ===
decimal 255 | binaire 255 | octal 255 | hexa 255 | avec _ 255
255 s'ecrit 11111111 en binaire, 377 en octal, ff en hexa
litteraux : 3000000000 2.5 1000.0 1000000
=== CARACTERES ===
A A A 66
=== CONVERSIONS PAR LES ENVELOPPES ===
Double.valueOf("3.99").intValue() = 3
Integer.valueOf(300).byteValue() = 44
Integer.parseInt("ff", 16) = 255, Integer.parseInt("-101", 2) = -5
Double.parseDouble("1e3") = 1000.0, Float.valueOf("2.5").doubleValue() = 2.5
Boolean.parseBoolean("TRUE") = true, Boolean.parseBoolean("oui") = false
Long.valueOf("42").longValue() + 1 = 43, Short.parseShort("-7") = -7
```
