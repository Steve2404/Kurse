# Projet 8 — Le laboratoire d'algorithmes sur les chaînes

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 4) :** les **algorithmes classiques sur le texte**, avec `String`, `StringBuilder`, `char` et les tableaux :
- l'arithmétique des `char` (`c - 'a'`, `c - '0'`, `(char) (…)`) ;
- les **compteurs** `int[26]` ;
- la compression **RLE** ;
- les chiffrements de **César** et de **Vigenère** ;
- l'addition et la factorielle de **grands nombres** écrits en chaînes ou en tableaux de chiffres ;
- le **préfixe commun** et le test de **rotation** ;
- les occurrences **avec ou sans chevauchement** ;
- le plus long **palindrome** (expansion autour d'un centre) ;
- la **justification** d'un paragraphe ;
- un tri **insensible à la casse** ;
- les chiffres **romains** dans les deux sens ;
- les **bases** 2 et 16.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch4_coreapis.projects.p08_textlab`. La classe du `main` s'appelle **`TextLab`**.

**Règle du crescendo :** chapitres 1 à 4. Pas de `Map` pour compter (un `int[26]`), pas de `chars()`, pas de `BigInteger` (c'est justement ce que tu réécris), pas de lambda.

**Ce projet est le 2e laboratoire d'algorithmes**, cette fois sur des **textes**. La méthode est celle du projet 7 : dessiner, prendre un petit exemple, faire un tableau de suivi des variables, puis coder.

**Tes outils pour ce projet** (pas d'arguments) :

```
javac -d build/ch4-p08 -sourcepath src/main/java src/main/java/ch4_coreapis/projects/p08_textlab/TextLab.java
java "-Duser.language=fr" -cp build/ch4-p08 ch4_coreapis.projects.p08_textlab.TextLab
```

---

## Tableau de bord

### ☐ Étape 1 — Anagrammes

```
anagrammes : Listen=oui triangle=oui Dormitory=oui apple=oui abc=non
```

**📖 La leçon : compter des lettres dans un tableau.** Une lettre minuscule `c` a un rang dans l'alphabet : `c - 'a'` (de 0 pour `a` à 25 pour `z`, chapitre 2, projet 3). Ce rang sert d'**indice** dans un tableau de 26 compteurs :

```java
int[] compte = new int[26];
compte['c' - 'a']++;      // une lettre c de plus : la case 2 passe à 1
```

**👉 À toi :**

- Découpe chaque paire de `Data.ANAGRAMS` avec `split("/")`. Mets en minuscules et retire les espaces.
- **Méthode 1 :** un `new int[26]`. Chaque lettre de la 1re chaîne ajoute 1 à la case `c - 'a'`, chaque lettre de la 2e retire 1. Anagrammes si tout revient à 0.
- **Méthode 2 :** `toCharArray()`, `Arrays.sort` sur les deux, puis `Arrays.equals`.
- Les deux méthodes doivent donner le même verdict. Si elles diffèrent, ajoute un `!` après le verdict : il ne doit jamais apparaître.
- **Question :** `apple` et `paple` sont-elles vraiment des anagrammes ? Vérifie lettre par lettre.

### ☐ Étape 2 — Compression RLE

```
rle : aaabccddddde -> 3a1b2c5d1e -> aaabccddddde aller-retour true gain 2
```

**📖 La leçon : d'un chiffre écrit vers un nombre.** `'7' - '0'` vaut `7` : les codes des chiffres se suivent. Pour lire `"12"` chiffre par chiffre : on part de 0, puis `nombre = nombre * 10 + chiffre` à chaque chiffre (0 × 10 + 1 = 1, puis 1 × 10 + 2 = 12).

**👉 À toi :**

- **Compresser** `Data.RLE` : pour chaque série de lettres identiques, écris sa longueur puis la lettre.
- **Décompresser** : lis le nombre chiffre par chiffre (`nombre = nombre * 10 + (c - '0')`), puis répète la lettre (`String.valueOf(c).repeat(n)`).
  - Ton décodeur doit accepter un nombre à **plusieurs chiffres** (`12a`).
- Le gain est la différence de longueur.
- **Question :** sur quel texte la compression RLE **allonge**-t-elle le résultat ?

### ☐ Étape 3 — César et Vigenère

```
cesar : Khoor, Zruog! | Hello, World! | rot13 deux fois Hello, World!
vigenere : LXFOPVEFRNHR | ATTACKATDAWN
```

**📖 La leçon : modifier un caractère.** Un `String` ne change pas. On copie donc le texte dans un `StringBuilder`, puis `sb.setCharAt(i, nouveau)` remplace le caractère en position `i`. Avec un `char[]` (`texte.toCharArray()`), on écrit directement `lettres[i] = nouveau;`, puis `new String(lettres)` refait un texte.

**Rappels :** `Character.isUpperCase(c)` et `Character.isLowerCase(c)` disent si `c` est une majuscule ou une minuscule. Le modulo avec des négatifs : chapitre 2, projet 3, étape 1.

**👉 À toi :**

- **César :** une méthode `caesar(String, int)` qui copie le texte dans un `StringBuilder` puis remplace chaque lettre avec `setCharAt`.
  - Majuscules et minuscules gardent leur casse ; la ponctuation ne change pas.
  - La formule : `(char) ('a' + (c - 'a' + shift) % 26)`.
  - Décoder = encoder avec `26 - shift`.
  - **Question :** pourquoi pas `-shift` ? Que vaut `-1 % 26` en Java ?
- **Vigenère :** chaque lettre est décalée par la lettre de la clé à la position `i % longueur de la clé`.
  - Travaille dans des `char[]`. Affiche le chiffré avec `new String(…)` et le déchiffré avec `String.valueOf(…)`.
  - Au déchiffrement, ajoute 26 avant le `%` pour rester positif.

### ☐ Étape 4 — Grands nombres

```
addition : 111111111011111111100 (21 chiffres, long max 9223372036854775807)
factorielle 25 : 15511210043330985984000000 (6 zeros a la fin)
```

**📖 Conseil :** pose l'addition `987 + 145` sur papier, comme à l'école, de droite à gauche, et note la retenue à chaque colonne. Ton programme fait exactement les mêmes gestes.

**👉 À toi :**

- **L'addition** de `Data.BIG_A` et `Data.BIG_B`, comme à l'école :
  - de droite à gauche, avec une retenue ;
  - la boucle continue tant qu'il reste un chiffre **ou** une retenue ;
  - les chiffres s'ajoutent à la fin d'un `StringBuilder`, puis tu appelles `reverse()`.
  - **Question :** pourquoi un `long` ne suffirait-il pas ? Compare le nombre de chiffres avec `Long.MAX_VALUE`.
- **La factorielle de `Data.FACTORIAL`** :
  - un `int[]` de chiffres **à l'envers** (les unités en case 0) et une taille courante ;
  - multiplie chaque chiffre par f, avec une retenue qui peut faire grandir le nombre ;
  - relis à l'endroit dans un `StringBuilder`.
- **Les zéros finaux** : compte-les en partant de la fin.
  - **Question :** pourquoi y en a-t-il exactement 6 ? (Compte les facteurs 5 dans 25 !.)

### ☐ Étape 5 — Préfixe, rotations, occurrences

```
prefixe commun : inter
rotations : erbottlewat=true(3) cdab=true(2) acbd=false a=false
occurrences de ana : 4 avec chevauchement, 2 sans
```

**📖 Rappel :** `startsWith`, `concat` (colle deux textes, comme `+`), `contains` et `indexOf(texte, départ)` (projet 1, étapes 3 et 4).

**👉 À toi :**

- **Le préfixe commun** de `Data.PREFIX_WORDS` : pars du premier mot et raccourcis-le (`substring`) tant qu'un mot ne commence pas par lui (`startsWith`).
- **Les rotations :** b est une rotation de a si les longueurs sont égales **et** si `a.concat(a)` contient b.
  - Le nombre entre parenthèses est l'indice de b dans `a + a`.
  - Les quatre paires sont définies dans ton code : `{"waterbottle", "erbottlewat"}`, `{"abcd", "cdab"}`, `{"abcd", "acbd"}` et `{"aa", "a"}`.
  - **Question :** sans le test de longueur, que donnerait `{"aa", "a"}` ?
- **Les occurrences** de `"ana"` dans `"bananarama ananas"`, avec `indexOf(texte, départ)` :
  - avec chevauchement : la recherche suivante part de `trouvé + 1` ;
  - sans chevauchement : elle part de `trouvé + longueur`.

### ☐ Étape 6 — Le plus long palindrome

```
plus long palindrome : geeksskeeg (10) verifie true
```

**📖 Conseil :** sur `"abba"`, essaie chaque centre possible à la main (sur une lettre, puis entre deux lettres), et écarte deux doigts tant que les lettres sous tes doigts sont égales.

**👉 À toi :**

- **L'expansion autour d'un centre** sur `Data.PALINDROME_SOURCE` : il y a 2n − 1 centres, sur une lettre (palindrome impair) ou entre deux lettres (palindrome pair).
  - Pour le centre k : `left = k / 2` et `right = left + k % 2`.
  - Écarte `left` et `right` tant que les lettres sont égales.
- **La vérification :** le résultat est égal à son inverse (`StringBuilder.reverse`).
- **Question :** quelle est la complexité ? Et celle de la méthode naïve, qui teste toutes les sous-chaînes ?

### ☐ Étape 7 — Justifier un paragraphe

```
|Java     strings     are|
|immutable    so    every|
|method   returns  a  new|
|object  while  a builder|
|changes itself          |
```

**📖 Conseil :** découpe à la main les premiers mots sur une largeur de 20, avec un crayon : combien de mots tiennent ? Combien d'espaces restent à répartir ?

**👉 À toi :**

- **Le remplissage glouton :** prends autant de mots que possible, en comptant au minimum un espace entre deux mots, sans dépasser `Data.WIDTH`.
- **La répartition :** les espaces en trop sont répartis entre les trous. Les **premiers** trous reçoivent un espace de plus quand la division ne tombe pas juste (`espaces / trous` et `espaces % trous`).
- **La dernière ligne** (ou une ligne d'un seul mot) est alignée à gauche : `String.join(" ", Arrays.copyOfRange(…))`, puis des espaces à droite.
- Chaque ligne est encadrée par `|`.

### ☐ Étape 8 — Trier sans tenir compte de la casse

```
sans casse : Apple apple banana Banana cherry date | naturel : Apple Banana apple banana cherry date
```

**📖 La leçon : comparer deux textes pour les trier.** `a.compareTo(b)` rend un nombre **négatif** si `a` vient avant `b`, **0** s'ils sont égaux, **positif** si `a` vient après. `compareToIgnoreCase` fait de même en ignorant majuscules et minuscules. Le tri par insertion se déroule comme au projet 7, étape 1.

**👉 À toi :**

- Un **tri par insertion** sur une copie de `Data.UNSORTED`, avec `compareToIgnoreCase`.
- À égalité (`Apple` et `apple`), l'ordre d'origine est conservé : c'est un tri **stable**.
- Compare avec l'ordre « naturel » d'`Arrays.sort` : les majuscules d'abord.

### ☐ Étape 9 — Les chiffres romains

```
romains : 4=IV 9=IX 14=XIV 1994=MCMXCIV 2026=MMXXVI 3999=MMMCMXCIX
relus : XLII=42 MCMXC=1990 CDXLIV=444
```

**📖 Rappel :** des tableaux parallèles (projet 6, étape 1) et un `switch` qui rend une valeur (chapitre 3, projet 1, étape 3).

**👉 À toi :**

- **Vers les romains** (glouton) : deux tableaux **parallèles**, `{1000, 900, 500, 400, …, 1}` et `{"M", "CM", "D", "CD", …, "I"}`. Prends toujours la plus grande valeur possible.
- **Depuis les romains** : une méthode `value(char)` avec un `switch` expression. Une valeur plus petite **avant** une plus grande se soustrait.

### ☐ Étape 10 — Bases 2 et 16

```
bases : 10=1010b/Ah 255=11111111b/FFh 2026=11111101010b/7EAh relu 2026 255
```

**📖 La leçon : écrire devant.** `sb.insert(0, x)` ajoute `x` **au début** du `StringBuilder`. Pour convertir 10 en binaire : 10 % 2 = 0 (écrit devant), puis 5 % 2 = 1, puis 2 % 2 = 0, puis 1 % 2 = 1 → `1010`.

**👉 À toi :**

- **La conversion :** pour 10, 255 et 2026, divise par 2 (puis par 16) et écris chaque reste **devant** : `insert(0, …)`.
  - Le chiffre hexadécimal vient de `"0123456789ABCDEF".charAt(reste)`.
- **La vérification** avec `Integer.toBinaryString` et `Integer.toHexString` (en minuscules : utilise `equalsIgnoreCase`). Un `!` apparaîtrait en cas d'écart.
- **La relecture** avec `Integer.parseInt(texte, base)` de `"11111101010"` (base 2) et `"FF"` (base 16).

---

## Checklist (vérifiée par `Check`)

- `Data.ANAGRAMS` et `Data.PARAGRAPH` ;
- `new int[26]`, `charAt`, `- 'a'`, `- '0'`, un cast `(char) (…)` ;
- `setCharAt`, `reverse()`, `insert(0, …)` ;
- `compareToIgnoreCase`, `startsWith`, `concat`, `contains` ;
- `String.join`, `Arrays.copyOfRange`, `toCharArray`, `String.valueOf`, `repeat` ;
- `Integer.toBinaryString`, `toHexString`, `parseInt` ;
- `indexOf(texte, départ + 1)`.

---

## Sortie attendue complète

```
anagrammes : Listen=oui triangle=oui Dormitory=oui apple=oui abc=non
rle : aaabccddddde -> 3a1b2c5d1e -> aaabccddddde aller-retour true gain 2
cesar : Khoor, Zruog! | Hello, World! | rot13 deux fois Hello, World!
vigenere : LXFOPVEFRNHR | ATTACKATDAWN
addition : 111111111011111111100 (21 chiffres, long max 9223372036854775807)
factorielle 25 : 15511210043330985984000000 (6 zeros a la fin)
prefixe commun : inter
rotations : erbottlewat=true(3) cdab=true(2) acbd=false a=false
occurrences de ana : 4 avec chevauchement, 2 sans
plus long palindrome : geeksskeeg (10) verifie true
|Java     strings     are|
|immutable    so    every|
|method   returns  a  new|
|object  while  a builder|
|changes itself          |
sans casse : Apple apple banana Banana cherry date | naturel : Apple Banana apple banana cherry date
romains : 4=IV 9=IX 14=XIV 1994=MCMXCIV 2026=MMXXVI 3999=MMMCMXCIX
relus : XLII=42 MCMXC=1990 CDXLIV=444
bases : 10=1010b/Ah 255=11111111b/FFh 2026=11111101010b/7EAh relu 2026 255
```
