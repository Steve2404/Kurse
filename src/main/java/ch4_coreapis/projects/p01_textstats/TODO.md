# Projet 1 — L'analyseur de texte

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

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
