# Projet 1 — L'analyseur de texte

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 4) :** l'API **`String`**, presque en entier :
- `length`, `charAt`, `indexOf` (dont `indexOf(texte, départ)`), `substring` ;
- la casse ;
- `equals` / `equalsIgnoreCase` ;
- `startsWith` / `endsWith` / `contains` ;
- `replace`, `split` ;
- `strip` / `stripLeading` / `stripTrailing` / `trim` ;
- `isEmpty` / `isBlank` ;
- `indent`, `stripIndent`, `translateEscapes` ;
- `repeat`, `concat`, `formatted` / `String.format` ;
- le chaînage.

Plus `Arrays.sort` sur un tableau de mots.

**Ce qui est donné :** `Data.java` (le texte) et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch4_coreapis.projects.p01_textstats`. La classe du `main` s'appelle **`TextStats`**.

**Règle du crescendo :** chapitres 1 à 4. Pas de collection (`List`, `Map`…), pas de lambda, pas de `chars()` ni de `lines()` (des streams du chapitre 10).

**Attention au formatage :** n'utilise pas `%f`. Ta JVM est réglée en allemand, donc `%.2f` écrit `3,14` ; le formatage localisé est au chapitre 11. `%s` et `%d` sont sûrs.

**Ce que le chapitre 4 t'apprend :** les **outils tout faits** de Java, ce qu'on appelle l'**API** :
- les textes (`String`, `StringBuilder`) ;
- les tableaux et la classe `Arrays` ;
- les calculs de `Math` ;
- les dates et les heures.

Tu ne vas plus tout programmer toi-même : tu vas apprendre à **utiliser** ces outils. Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet. Les gestes de base sont ceux du **projet 0 du chapitre 1**.

**`Data.java` est donné :** il contient les données du projet, sous forme de constantes. Ouvre-le et lis-le. Tu t'en sers avec `Data.NOM_DE_LA_CONSTANTE`, comme `Integer.MAX_VALUE` au chapitre 1.

**Tes outils pour ce projet** (pas d'arguments, la flèche verte suffit) :
- **Terminal** (depuis `Kurse`) :

```
javac -d build/ch4-p01 -sourcepath src/main/java src/main/java/ch4_coreapis/projects/p01_textstats/TextStats.java
java "-Duser.language=fr" -cp build/ch4-p01 ch4_coreapis.projects.p01_textstats.TextStats
```

- **`-sourcepath src/main/java`** dit à `javac` : « si mon fichier a besoin d'autres classes (ici `Data`), va chercher leurs fichiers `.java` à partir de ce dossier ». Tu n'as donc qu'**un** fichier à donner.

**Trouver une méthode et lire sa documentation :** tape `texte.` (avec le point) puis attends : IntelliJ liste toutes les méthodes. **Ctrl + Q** sur un nom affiche sa documentation (chapitre 1, projet 5).

---

## Le problème

Un rédacteur veut un rapport sur son texte (`Data.TEXT`, 3 lignes) :
- des statistiques ;
- le mot le plus long ;
- les mots les plus fréquents ;
- les palindromes ;
- la recherche et la censure d'un mot ;
- une mise en titre ;
- le nettoyage d'une ligne « sale ».

**Chaque ligne de la sortie attendue pratique un groupe de méthodes.** Pour chaque méthode, demande-toi : que rend-elle (un **nouveau** `String`, un `int`, un `boolean`) ? Et que se passe-t-il aux bornes ?

---

## Tableau de bord

### ☐ Étape 1 — Découper et compter

```
LIGNES : 3, MOTS : 39, CARACTERES : 200, VOYELLES : 67
PLUS LONG : palindromes (11), PLUS COURT : a
```

**📖 La leçon : un `String` a des méthodes.** Un texte est un **objet**. Il sait faire des choses, qu'on lui demande avec un **point** : `texte.methode(…)`. Les positions des lettres sont numérotées **à partir de 0** :

```
 B  o  n  j  o  u  r
 0  1  2  3  4  5  6
```

```java
String s = "Bonjour";
s.length()          // 7 : le nombre de caractères
s.charAt(0)         // 'B' : le caractère en position 0
s.charAt(6)         // 'r' : le dernier, en position length() - 1
s.indexOf('j')      // 3 : la position du 1er 'j'
s.indexOf("our")    // 4 : la position où commence "our"
s.indexOf('z')      // -1 : pas trouvé
```

`s.charAt(7)` est hors du texte : le programme s'arrête avec une `StringIndexOutOfBoundsException`.

**📖 La leçon : découper avec `split`, et ton premier tableau.** `split` coupe un texte à chaque séparateur, et rend un **tableau** de morceaux : une rangée de cases numérotées à partir de 0.

```java
String phrase = "le petit chat dort";
String[] mots = phrase.split(" ");    // String[] : « un tableau de String »
mots.length                           // 4 : le nombre de cases (sans parenthèses !)
mots[0]                               // "le"
mots[3]                               // "dort"
for (String m : mots) {               // le for-each du chapitre 3 marche sur un tableau
    System.out.print("[" + m + "]");  // [le][petit][chat][dort]
}
```

Le séparateur de `split` est une **expression régulière** : un petit langage pour décrire des motifs. Pour l'instant, retiens que la plupart des caractères s'y désignent eux-mêmes ; l'étape te demande de trouver comment écrire « une espace ou plus ».

**📖 La leçon : transformer un texte.**

```java
"Bonjour".toUpperCase()        // "BONJOUR"
"Bonjour".toLowerCase()        // "bonjour"
"Bonjour".replace('o', '0')    // "B0nj0ur" : remplace TOUTES les occurrences
"  chat  ".strip()             // "chat" : retire les espaces au début et à la fin
```

**⚠️ Un `String` ne change jamais.** Ces méthodes rendent un **nouveau** texte, et l'ancien reste intact. Pour garder le résultat, il faut le ranger :

```java
String s = "chat";
s.toUpperCase();              // le résultat est perdu : s vaut toujours "chat"
s = s.toUpperCase();          // s vaut maintenant "CHAT"
```

**👉 À toi :**

- **Les lignes :** `split("\n")`.
- **Les mots :**
  - remplace la ponctuation (`.` `,` `:` `;`) et les sauts de ligne par des espaces avec `replace` ;
  - puis `strip` ;
  - puis `split` sur « une ou plusieurs espaces ».
  - **Question :** `split` prend une **expression régulière**. Quelle expression signifie « une espace ou plus » ? Que donnerait `split(" ")` sur `"a  b"` ?
- **Les voyelles :** parcours avec `charAt`. **Astuce :** `"aeiouy".indexOf(c) >= 0` dit si `c` est une voyelle. Pense aux majuscules.
- **Question :** `text.length()` compte-t-il les `\n` ?

### ☐ Étape 2 — Les mots les plus fréquents, sans collection

```
FREQUENTS : le x4, un x4
```

**📖 La leçon : créer un tableau toi-même.** Un tableau a une **taille fixe**, choisie à sa création. Ses cases reçoivent d'abord une **valeur par défaut** : `0` pour des nombres, `null` pour des objets.

```java
int[] notes = new int[3];             // 3 cases : [0, 0, 0]
notes[0] = 12;                        // range 12 dans la case 0
notes[2] = 15;                        // [12, 0, 15]
int[] autres = {5, 3, 9, 1};          // créé et rempli d'un coup
System.out.println(Arrays.toString(notes));   // [12, 0, 15]
```

`Arrays.toString(t)` sert à **afficher** un tableau : il faut écrire `import java.util.Arrays;` en haut du fichier (chapitre 1, projet 4).

**📖 La leçon : trier.** `Arrays.sort(t)` trie le tableau **sur place** : le tableau lui-même est modifié. Les textes sont rangés selon les codes des caractères : majuscules avant minuscules.

**📖 La leçon : comparer deux textes.** `==` compare les **étiquettes** (chapitre 2, projet 4), pas le contenu. Pour comparer le **contenu** de deux textes, on utilise toujours `equals` :

```java
"chat".equals(autreTexte)             // true si les lettres sont les mêmes
"Chat".equalsIgnoreCase("chat")       // true : ignore majuscules et minuscules
```

**👉 À toi :**

- **L'algorithme :**
  1. copie les mots en **minuscules** dans un nouveau tableau ;
  2. **trie**-le avec `Arrays.sort` : les mots identiques deviennent **voisins** ;
  3. compte la longueur de chaque **série**, en gardant les deux plus longues. À égalité, la première rencontrée dans l'ordre alphabétique gagne.
- **Question :** pourquoi comparer les mots avec `equals` et non `==` ?

### ☐ Étape 3 — Palindromes, positions, censure

```
PALINDROMES : radar kayak rotor anna bob elle
POSITIONS de "radar" : 3 83 139
CENSURE : Un *****, un kayak, un rotor ; Bob a tout note dans son carnet.
```

**📖 La leçon : chercher dans un texte.**

```java
"Bonjour".contains("jou")      // true
"banane".indexOf("an", 2)      // 3 : cherche "an" à partir de la position 2
"chat".indexOf("a", 3)         // -1 : plus de "a" après la position 3
"ab".repeat(3)                 // "ababab"
```

Pour trouver **toutes** les positions d'un mot, on répète `indexOf(mot, départ)` dans une boucle, en repartant juste après la dernière trouvaille. La boucle s'arrête quand `indexOf` rend `-1`.

**👉 À toi :**

- **Les palindromes :**
  - un mot de **plus de 2 lettres** qui se lit pareil dans les deux sens, sans tenir compte de la casse ;
  - teste-le avec deux indices qui se rapprochent ;
  - chaque palindrome n'est listé **qu'une fois**, dans l'ordre d'apparition. Sans collection, comment retenir ceux déjà vus ? Pense à `contains` sur une chaîne témoin.
- **Les positions :** toutes les positions d'un mot, avec une boucle sur `indexOf(mot, départ)`.
- **La censure :** chaque lettre du mot censuré est remplacée par `*` (avec `repeat`).

### ☐ Étape 4 — Mise en titre et tests

```
TITRE : Le Radar Du Kayak Detecte Un Rotor : Anna Et Bob Notent Le Niveau.
COMMENCENT par "Le " : 1, FINISSENT par "." : 3, contient "Bob" : true, egal sans casse : true
```

**📖 La leçon : `substring`, extraire un morceau.** Avec `"Bonjour"` (positions 0 à 6) :

```java
"Bonjour".substring(3)       // "jour" : de la position 3 jusqu'à la fin
"Bonjour".substring(0, 3)    // "Bon"  : de 0 inclus à 3 EXCLU
"Bonjour".substring(3, 5)    // "jo"   : de 3 inclus à 5 exclu
```

**Retiens :** la fin est **exclue**. La longueur du morceau vaut donc `fin - début`.

**📖 La leçon : tester le début, la fin, l'égalité.**

```java
"Bonjour".startsWith("Bon")    // true
"Bonjour".endsWith("r")        // true
```

**👉 À toi :**

- **La mise en titre :** chaque mot de la 1re ligne commence par une majuscule, le reste en minuscules. Utilise `substring(0, 1)` et `substring(1)`.
  - **Question :** que rend `substring(1)` sur un mot d'une lettre ? Et `substring(0, 1)` sur `""` ?

### ☐ Étape 5 — Nettoyage et méthodes de Java 11 à 15

```
SALE : [   Total\tfinal :   42 points   ] -> strip [...] -> stripLeading [...] -> stripTrailing [...]
ECHAPPEMENTS : [Total	final :   42 points], vide true, blanc true, trim [x]
INDENTE :
  a
  b
DESINDENTE :
x
  y
12
```

**📖 La leçon : des méthodes récentes (Java 11 à 15).**

```java
"  ".isBlank()          // true : vide, ou seulement des espaces
"".isEmpty()            // true : vraiment vide
"  ".isEmpty()          // false : il contient deux espaces
```

Les autres (`stripLeading`, `stripTrailing`, `indent`, `stripIndent`, `translateEscapes`) sont expliquées dans l'étape elle-même. Pour chacune, essaie-la sur un petit texte et **affiche le résultat entre crochets** `"[" + … + "]"` : c'est le seul moyen de **voir** les espaces.

**👉 À toi :**

- **`MESSY` contient `\t` écrit en deux caractères** (un antislash et un `t`). `translateEscapes()` le transforme en **vraie** tabulation.
- **`trim` contre `strip` :** les deux retirent les espaces. Quelle est la différence, à propos de l'Unicode ?
- **`indent(2)`** ajoute 2 espaces **et normalise les fins de ligne** : la chaîne obtenue se termine toujours par `\n`.
- **`stripIndent()`** retire l'indentation **commune** à toutes les lignes.
  - **Le piège du `12` :** sur `"   x\n     y\n"`, il ne retire **rien**, et la longueur reste 12. Pourquoi ? (Le dernier `\n` crée une dernière ligne **vide**…)

### ☐ Étape 6 — Formatage et chaînage

```
FORMATE : a     |  39|true [   ok]
CHAINAGE : w0rld!
```

**📖 La leçon : formater un texte avec des largeurs.** `String.format(modèle, valeurs…)` remplace chaque `%…` du modèle par une valeur. `"modèle".formatted(valeurs…)` fait la même chose :

```java
String.format("[%5s]", "ab")       // "[   ab]" : %s = un texte, 5 = largeur, aligné à droite
String.format("[%-5s]", "ab")      // "[ab   ]" : le - aligne à gauche
String.format("[%3d]", 7)          // "[  7]"   : %d = un entier
"%s a %d ans".formatted("Lea", 8)  // "Lea a 8 ans"
```

**📖 La leçon : enchaîner les appels.** Chaque méthode de `String` rend un nouveau `String`, sur lequel on peut aussitôt appeler une autre méthode. On lit de **gauche à droite** : `"  Abc ".strip().toLowerCase()` fait d'abord `strip`, puis met le résultat en minuscules.

**👉 À toi :**

- **`"%-6s|%4d|%s".formatted(…)` et `String.format("[%5s]", …)` :**
  - que signifient le `-` et le nombre ?
  - que se passe-t-il si la valeur est plus longue que la largeur ?
- **Le chaînage :** `"  Hello World  ".strip().toLowerCase().replace("o", "0").substring(6).concat("!")`. **Prédis** le résultat, en écrivant la valeur intermédiaire après chaque appel.

---

## Checklist (vérifiée par `Check`)

`split`, `charAt`, `length`, `indexOf` (dont `indexOf(texte, départ)`), `substring`, `toUpperCase`, `toLowerCase`, `equals`, `equalsIgnoreCase`, `startsWith`, `endsWith`, `contains`, `replace`, `strip`, `stripLeading`, `stripTrailing`, `trim`, `isEmpty`, `isBlank`, `indent`, `stripIndent`, `translateEscapes`, `formatted`, `String.format`, `repeat`, `concat`, `Arrays.sort` ☐

---

## Sortie attendue complète

```
LIGNES : 3, MOTS : 39, CARACTERES : 200, VOYELLES : 67
PLUS LONG : palindromes (11), PLUS COURT : a
FREQUENTS : le x4, un x4
PALINDROMES : radar kayak rotor anna bob elle
POSITIONS de "radar" : 3 83 139
CENSURE : Un *****, un kayak, un rotor ; Bob a tout note dans son carnet.
TITRE : Le Radar Du Kayak Detecte Un Rotor : Anna Et Bob Notent Le Niveau.
COMMENCENT par "Le " : 1, FINISSENT par "." : 3, contient "Bob" : true, egal sans casse : true
SALE : [   Total\tfinal :   42 points   ] -> strip [Total\tfinal :   42 points] -> stripLeading [Total\tfinal :   42 points   ] -> stripTrailing [   Total\tfinal :   42 points]
ECHAPPEMENTS : [Total	final :   42 points], vide true, blanc true, trim [x]
INDENTE :
  a
  b
DESINDENTE :
x
  y
12
FORMATE : a     |  39|true [   ok]
CHAINAGE : w0rld!
```
