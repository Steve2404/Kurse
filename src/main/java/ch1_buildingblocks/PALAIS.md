# 🏠 Palais mental — chapitre 1 : le salon, stations 1 à 6

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md) (la méthode, et l'étape 0 où tu choisis tes vrais objets).
> **Quand :** après le projet `p05_cinema`, puis avant chaque répétition des drills.
> **Comment :** pour chaque station, lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute. Si l'image ne te parle pas, invente la tienne et écris-la dans « Mon image ».

---

### 1. 🚪 La porte du salon — `main` et les commandes

- **Image :** un **portier géant** garde la porte. Sur son uniforme est brodé `public static void main(String[] args)`. Tu arrives avec des **valises numérotées 0, 1, 2** : il hurle « **ZÉRO** d'abord ! » et prend la valise 0. Derrière lui, deux machines : un **compacteur `javac`** qui écrase ton `.java` en cube `.class`, et un **tapis roulant `java`** qui fait avancer le cube.
- **À retenir :**
  - la signature : `public static void main(String[] args)` ; `String... args`, `String args[]` et `final String[] args` marchent aussi ;
  - `args[0]` est le **premier mot après le nom de la classe** ;
  - `javac -d build …Fichier.java` crée les `.class` dans `build` ; `java -cp build paquet.Classe a b` lance (on donne le **nom de la classe**, sans `.class`) ;
  - `java Fichier.java` lance un **fichier seul** sans `javac` (compilation en mémoire).
- **Mon image :** …

### 2. 🛋️ Le canapé — les 8 primitifs, les valeurs par défaut, `var`

- **Image :** **8 coussins** sur le canapé, du plus petit au plus gros. Le minuscule **`byte`** crie d'une voix aiguë « **−128 à 127** ! ». Le coussin **`char`** porte une lettre et refuse d'être négatif. Le coussin **`boolean`** est un interrupteur. Les coussins **posés sur le canapé** (les champs) sont **gonflés à zéro** tout seuls. Celui que **tu tiens dans tes bras** (une variable locale) est **vide** : tu t'assois dessus, **pschhh**, ça ne compile pas. Un coussin sans étiquette marqué **`var`** prend la forme de ce que tu mets dedans **tout de suite**.
- **À retenir :**
  - les tailles : `byte` 8 bits, `short` 16, `int` 32, `long` 64, `float` 32, `double` 64, `char` 16 (**non signé**, 0 à 65 535) ;
  - un **champ** reçoit une valeur par défaut : `0`, `0.0`, `false`, `'\u0000'`, `null` ;
  - une **variable locale** n'en a pas : la lire avant de l'initialiser **ne compile pas** ;
  - `var` : **seulement** pour une variable locale, **initialisée sur la même ligne**, jamais avec `null` seul ; `var` n'est pas un mot réservé (une variable peut s'appeler `var`).
- **Mon image :** …

### 3. 🪵 La table basse — les littéraux

- **Image :** sur la table, un **donut** (un `0`) se colle devant le nombre **10**. Il le mange et il ne reste que **8** : `010` vaut 8, c'est de l'**octal**. À côté, une **croix** (`0x`) marque l'hexadécimal et une **bouteille** (`0b`) le binaire. Des **petits ponts** `_` relient les chiffres entre eux, mais aucun pont ne pend dans le vide au bord d'un nombre. Un **long serpent `L`** s'enroule autour des nombres trop grands, une **feuille `f`** se pose sur les décimaux qui veulent être `float`.
- **À retenir :**
  - `0` devant = octal (`010` = 8), `0x` = hexadécimal, `0b` = binaire ;
  - `_` **seulement entre deux chiffres** : jamais au début, à la fin, à côté du `.`, après `0x`, ni avant `L` ou `f` ;
  - un entier seul est un `int` : `L` pour un `long` trop grand ; un décimal seul est un `double` : `f` pour un `float`.
- **Mon image :** …

### 4. 📺 La télévision — les text blocks

- **Image :** la télé ne s'allume que si tu tapes **trois guillemets `"""` puis Entrée**. L'image arrive, mais tout l'écran **glisse vers la gauche** jusqu'à toucher la ligne la plus à gauche (le `"""` du bas compte aussi). Les espaces en bout de ligne **tombent par terre**. Un **antislash `\`** au bout d'une ligne **colle** cette ligne à la suivante ; un petit **`\s`** tient un espace dans sa main pour qu'il ne tombe pas.
- **À retenir :**
  - un text block commence par `"""` **suivi d'un retour à la ligne** ;
  - l'indentation commune est retirée ; la ligne la plus à gauche, **y compris** celle du `"""` fermant, fixe la marge ;
  - les espaces de fin de ligne sont supprimés, sauf `\s` ;
  - `\` en fin de ligne = pas de saut de ligne ;
  - `"""` fermant **sur la dernière ligne de texte** = pas de saut de ligne final.
- **Mon image :** …

### 5. 💡 La lampe — l'ordre d'initialisation et le ramasse-miettes

- **Image :** la lampe a une colonne d'**ampoules**. Quand tu crées un objet, elles s'allument **une par une, de haut en bas**, dans l'ordre où elles sont écrites (champs et blocs `{ }` mélangés). Seulement après, l'**interrupteur** (le constructeur) fait *clic*. Plus tard, quand **plus personne ne regarde** la lampe (aucune référence), un **aspirateur** (le ramasse-miettes) **peut** l'aspirer… quand il veut.
- **À retenir :**
  - à chaque `new` : les champs et les blocs d'initialisation **dans l'ordre du fichier**, **puis** le constructeur ;
  - lire un champ, par son nom simple, dans un initialiseur **placé au-dessus** de sa déclaration ne compile pas ;
  - un objet devient **éligible** au ramasse-miettes quand plus aucune référence n'y mène ;
  - `System.gc()` est une **suggestion**, jamais une garantie.
- **Mon image :** …

### 6. 📚 L'étagère — paquets, imports, noms

- **Image :** les livres sont rangés **par rayon** (les paquets). Tu cries `import java.util.*;` : tous les livres **de ce rayon** sautent dans tes bras, mais **pas** ceux des petits rayons du dessous (les sous-paquets). Le rayon **`java.lang`** est déjà dans tes bras, toujours. Deux rayons ont chacun un livre **`Date`** : tu ne peux pas dire « donne-moi `Date` » sans préciser, sauf si tu as nommé l'un des deux exprès. Sur la tranche des livres, les titres commencent par une **lettre, `$` ou `_`**, jamais par un chiffre, et aucun ne s'appelle `_` tout seul ni `class`.
- **À retenir :**
  - `import a.b.*;` importe les **classes** de `a.b`, pas celles de `a.b.c` ;
  - `java.lang` est importé automatiquement ;
  - un import **explicite** gagne sur un joker ; deux jokers qui ont la même classe ne gênent **que si** tu utilises ce nom ; le **nom pleinement qualifié** (`java.util.Date`) règle tout ;
  - un identificateur commence par une lettre, `$` ou `_` ; pas de chiffre au début, pas de mot réservé, pas de `_` seul.
- **Mon image :** …

---

## ⚡ La balade éclair (réponds de tête, puis vérifie)

1. Station 1 : quel mot de la ligne de commande est `args[0]` ?
2. Station 2 : que se passe-t-il si tu lis une variable locale jamais initialisée ?
3. Station 3 : combien vaut `010` ? Où le `_` est-il interdit ?
4. Station 4 : qu'est-ce qui fixe la marge de gauche d'un text block ?
5. Station 5 : quel est l'ordre entre champs, blocs et constructeur ?
6. Station 6 : `import java.util.*;` importe-t-il `java.util.concurrent` ?
