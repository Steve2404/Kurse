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

**Ce que tu sais déjà faire** (projets 0 et 1) : créer la classe, lancer avec la flèche verte, lancer `Check`, essayer un exemple dans une classe `Atelier` puis la supprimer, coller des textes avec `+`, utiliser `parseInt`, `valueOf` et `intValue`. Ce projet n'a **pas** d'arguments : la flèche verte suffit.

**Tes deux commandes pour les expériences** (terminal, Alt + F12, depuis `Kurse`) :

```
javac -d build/p02 src/main/java/ch1_buildingblocks/projects/p02_specsheet/SpecSheet.java
java "-Duser.language=fr" -cp build/p02 ch1_buildingblocks.projects.p02_specsheet.SpecSheet
```

**Comment faire une expérience « est-ce que ça compile ? »** : écris la ligne demandée dans `main`, tape la commande `javac`, **lis** le message (le corrigé le traduit), puis **efface** la ligne. Une ligne qui ne compile pas empêche IntelliJ de lancer quoi que ce soit, même `Check`.

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

**📖 La leçon : un type primitif, c'est une boîte de taille fixe.** Un **type** dit quelle sorte de valeur une variable peut contenir. Java a **8 types « primitifs »**, les plus simples :

| Type | Contient | Exemple |
|---|---|---|
| `byte`, `short`, `int`, `long` | des nombres **entiers**, du plus petit au plus grand | `42` |
| `float`, `double` | des nombres **à virgule** | `2.5` |
| `char` | **un** caractère | `'A'` |
| `boolean` | vrai ou faux | `true` |

**Un bit**, c'est un petit interrupteur : allumé (1) ou éteint (0). Avec 8 interrupteurs, on peut faire 2 × 2 × … × 2 = **256** combinaisons différentes. Un `byte` (8 bits) peut donc ranger 256 nombres différents, la moitié négatifs et l'autre positifs. Plus un type a de bits, plus il peut ranger de grands nombres.

**📖 La leçon : demander à Java les tailles et les bornes.** Chaque type primitif a une **classe enveloppe** (tu as vu `Integer` et `Boolean` au projet 1). Elle contient des **constantes**, des valeurs fixées par Java, qu'on lit avec `NomDeLaClasse.NOM_DE_LA_CONSTANTE` :

```java
System.out.println(Integer.MAX_VALUE);   // 2147483647 : le plus grand int
System.out.println(Short.MIN_VALUE);     // -32768     : le plus petit short
System.out.println(Byte.SIZE);           // 8          : le nombre de bits d'un byte
```

**👉 À toi :**
- Affiche le titre, puis une ligne par type, avec `SIZE`, `MIN_VALUE` et `MAX_VALUE` de son enveloppe. Respecte **exactement** les espaces : le nom du type est suivi d'espaces pour que tous les `:` soient alignés.
- Pour l'instant, écris la partie `defaut …` en dur ; tu la remplaceras à l'étape 2.

**❓ Questions :**
- Quelle est l'enveloppe de `int` ? Et de `char` ? (Leurs noms ne suivent pas la règle des 6 autres.) Astuce : dans IntelliJ, tape le début d'un nom (`Integ`…, `Charac`…) et regarde ce qu'il propose.
- **`float` et `double` :** pourquoi n'affiche-t-on pas `MIN_VALUE` ? Lis la Javadoc de `Double.MIN_VALUE` : place le curseur sur `MIN_VALUE` et appuie sur **Ctrl + Q** (la documentation s'ouvre dans une petite fenêtre). Ce n'est pas le plus petit nombre négatif.

### ☐ Étape 2 — Les valeurs par défaut

```
..., defaut 0
char    : 16 bits, de 0 a 65535, defaut (code) 0
boolean : taille non fixee par Java, defaut false
```

**📖 La leçon : un champ a une valeur par défaut, une variable locale non.**
- Une **variable locale** est déclarée **dans** une méthode (comme dans `main`). Java exige qu'on lui donne une valeur avant de la lire.
- Un **champ** est déclaré **dans la classe, hors de toute méthode** (comme les champs de l'article au projet 1). Si on ne lui donne pas de valeur, Java lui en met une **par défaut** : `0` pour les nombres, `false` pour un `boolean`, `null` (« rien ») pour un objet comme `String`.

**📖 La leçon : `static`, pour que `main` puisse lire le champ.** `main` est `static` : elle tourne **sans objet**. Elle ne peut donc pas lire un champ ordinaire, qui appartient à **un** objet. Javac refuse :

```
error: non-static variable score cannot be referenced from a static context
```

(« la variable non static `score` ne peut pas être utilisée depuis un contexte static »). La solution la plus simple : déclarer le champ `static` lui aussi. Un champ `static` appartient à **la classe**, pas à un objet :

```java
public class Atelier {
    static int score;          // champ static, jamais initialisé
    static String joueur;
    static boolean fini;

    public static void main(String[] args) {
        System.out.println(score + " " + joueur + " " + fini);   // affiche 0 null false
    }
}
```

**📖 La leçon : un `char` est un nombre déguisé.** Chaque caractère a un **code** : `'a'` a le code 97, `'b'` le code 98… Un `char` peut donc être rangé dans un `int`, sans rien écrire de spécial. On obtient alors son code :

```java
char lettre = 'a';
int code = lettre;                            // pas de perte possible : un int est plus grand qu'un char
System.out.println(lettre + " " + code);      // affiche a 97
```

**👉 À toi :**
- Les valeurs par défaut ne s'écrivent pas : elles viennent de **8 champs que tu n'initialises pas**, un par type.
- **Le `char` :** sa valeur par défaut est le caractère de code 0, invisible à l'affichage. Pour voir son **code**, affecte le `char` à une variable `int`, sans cast.
- **Les bornes du `char` :** même technique pour `Character.MIN_VALUE` et `MAX_VALUE`.

**🧪 Expérience :** dans `main`, déclare une variable **locale** `int x;` et affiche-la. Que dit `javac` ? Pourquoi les champs ont-ils une valeur par défaut, et pas les variables locales ?

**❓ Question :** pourquoi l'affectation `char` → `int` compile-t-elle ? Que dirait `javac` dans l'autre sens (`int` vers `char`) avec une variable ? Essaie : `int n = 65; char c = n;`.

### ☐ Étape 3 — Un nombre, cinq écritures

```
decimal 255 | binaire 255 | octal 255 | hexa 255 | avec _ 255
255 s'ecrit 11111111 en binaire, 377 en octal, ff en hexa
litteraux : 3000000000 2.5 1000.0 1000000
```

**📖 La leçon : compter avec moins (ou plus) de 10 chiffres.** D'habitude, on compte en **base 10** : 10 chiffres, de 0 à 9. Après 9, on passe à 10 (« une dizaine et zéro »). D'autres bases existent :
- **base 2 (binaire)** : seulement 0 et 1. On compte 0, 1, 10, 11, 100… ;
- **base 8 (octal)** : de 0 à 7 ;
- **base 16 (hexadécimal)** : de 0 à 9, puis `a` (10), `b` (11)… jusqu'à `f` (15).

Un **littéral** est une valeur écrite directement dans le code. Java reconnaît la base grâce au **début** du littéral. Voici le nombre **dix**, écrit de cinq façons :

```java
int dix1 = 10;       // décimal
int dix2 = 0b1010;   // binaire : commence par 0b (8 + 2 = 10)
int dix3 = 012;      // octal : commence par un simple 0 (1 × 8 + 2 = 10)
int dix4 = 0xA;      // hexadécimal : commence par 0x (A = 10)
int dix5 = 1_0;      // le _ est permis entre deux chiffres, pour la lisibilité ; Java l'ignore
System.out.println(dix1 + " " + dix2 + " " + dix3 + " " + dix4 + " " + dix5);   // 10 10 10 10 10
```

Dans l'autre sens, `Integer.toBinaryString(10)` rend le texte `"1010"`, `Integer.toOctalString(10)` rend `"12"` et `Integer.toHexString(10)` rend `"a"`.

**📖 La leçon : les lettres à la fin d'un littéral.**
- Un nombre entier écrit tel quel est un `int`. S'il est **trop grand** pour un `int` (plus de 2 147 483 647), il faut un **`L`** à la fin pour en faire un `long` : `5_000_000_000L`.
- Un nombre à virgule écrit tel quel est un `double`. Pour un `float`, il faut un **`f`** à la fin : `1.5f`.
- **`e` veut dire « fois 10 puissance »** : `2.5e2` = 2,5 × 100 = `250.0`. C'est toujours un `double`.

**👉 À toi :**
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

**🧪 Expériences** (une ligne à la fois dans `main`, puis la commande `javac`, puis efface la ligne) :
1. `long big = 3000000000;` (sans `L`)
2. `float f = 2.5;` (sans `f`)
3. `int a = 0b102;`
4. `int b = 08;`
5. `int c = _100;`, puis `int d = 100_;`, puis `double e = 1_.5;`

**❓ Question :** que vaut `010` ? Prédis, puis vérifie avec `System.out.println(010);`.

### ☐ Étape 4 — Les caractères

```
A A A 66
```

**📖 La leçon : trois façons d'écrire un caractère.** Un `char` s'écrit entre **apostrophes** `'…'` (un texte `String`, lui, est entre guillemets `"…"`). Il peut aussi s'écrire avec son **code**, de deux façons :

```java
char x = 'a';         // le caractère lui-même
char y = 'a';    // \u suivi du code en hexadécimal, sur 4 chiffres (61 en hexa = 97)
char z = 97;          // le code en décimal, sans apostrophes
System.out.println(x + " " + y + " " + z);   // affiche a a a
```

**👉 À toi :**
- Trois `char` qui valent tous `'A'`, écrits de trois façons :
  - un littéral caractère ;
  - un **échappement Unicode** `'\u…'` (le code de `A` est 41 en hexadécimal) ;
  - un **nombre entier** (le code décimal de `A`).
- Puis le code de `'B'`, obtenu sans le recopier : la technique de l'étape 2.

**❓ Question :** `char fromCode = 65;` compile. Pourquoi `int n = 65; char c = n;` ne compile-t-il pas ? (Le chapitre 2 généralisera cette règle.)

### ☐ Étape 5 — Les conversions par les classes enveloppes

```
Double.valueOf("3.99").intValue() = 3
Integer.valueOf(300).byteValue() = 44
Integer.parseInt("ff", 16) = 255, Integer.parseInt("-101", 2) = -5
...
```

**📖 La leçon : des guillemets dans un texte.** Un texte commence et finit par `"`. Pour mettre un guillemet **à l'intérieur**, on écrit `\"` : le `\` dit « ce guillemet-ci ne termine pas le texte ».

```java
System.out.println("Il a dit \"oui\".");   // affiche Il a dit "oui".
```

Sans les `\`, `javac` croit que le texte s'arrête à `"Il a dit "`, et ne comprend plus la suite : `error: ')' expected`.

**📖 La leçon : les conversions des enveloppes.**
- **`xxxValue()`** sort la valeur de la boîte, **dans le type demandé** : `intValue()` en `int`, `byteValue()` en `byte`, `doubleValue()` en `double`…
- **Vers un entier, la partie après la virgule est coupée**, pas arrondie : `Double.valueOf("7.8").intValue()` vaut `7`.
- **Vers un type trop petit, le nombre « fait le tour »**, comme un compteur kilométrique qui repasse à zéro. Un `byte` ne range que 256 valeurs : `Integer.valueOf(260).byteValue()` vaut `260 - 256` = `4`.
- **`parseInt(texte, base)`** lit un texte écrit dans une autre base : `Integer.parseInt("12", 8)` vaut `10`, car `12` en octal vaut 1 × 8 + 2.

**👉 À toi :**
- Chaque ligne affiche **l'appel lui-même**, puis son résultat. Le texte de l'appel contient des guillemets : échappe-les.

**❓ Questions, à calculer à la main AVANT d'exécuter** (écris tes prédictions en commentaire, puis compare) :
- pourquoi `3.99` donne-t-il `3` et pas `4` ?
- pourquoi `300` en `byte` donne-t-il `44` ? Calcule `300 - 256`.
- que rend `parseInt` avec une base 16 ? Avec une base 2 et un signe `-` ?
- pourquoi `parseBoolean("oui")` rend-il `false` sans erreur, alors que `parseInt("oui")` lève une exception ? Essaie `Integer.parseInt("oui");` au début de `main`, lance, puis efface.

**Différence clé :** `parseXxx` rend un **primitif**, `valueOf` rend un **objet** enveloppe. `xxxValue()` passe de l'objet au primitif.

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
