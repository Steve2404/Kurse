# 🏠 Palais mental — chapitre 3 : la cuisine, stations 1 à 6

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Le salon garde les chapitres 1 et 2 : ta balade commence par lui.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 1. 🚪 La porte de la cuisine — `if`, `else` et `instanceof` avec motif

- **Image :** la porte a un **judas**. Tu regardes : « est-ce un `Integer` ? ». Si oui, la porte s'ouvre **et** te donne un badge **`i`** avec le prénom de l'invité : `if (o instanceof Integer i)`. Le badge reste valable **partout où la porte est sûre** que c'était un `Integer`, même après un `if (!(…)) return;`. Un `else` perdu dans le couloir s'accroche toujours au **`if` le plus proche**.
- **À retenir :**
  - un `else` appartient au **`if` sans `else` le plus proche**, quelle que soit l'indentation ;
  - `if (o instanceof Integer i)` : `i` n'existe que là où le test est **sûrement vrai** ;
  - avec `if (!(o instanceof Integer i)) return;`, `i` est utilisable **après** le `if` ;
  - en Java 17, `instanceof` avec motif **ne compile pas** si l'expression est déjà du type du motif (ou d'un sous-type).
- **Mon image :** …

### 2. 🚰 L'évier — le `switch` classique

- **Image :** l'évier n'accepte que **certains ingrédients** : des petits entiers (`int`, `short`, `byte`, `char`), leurs **enveloppes**, des **`String`** et des **`enum`**. Un **`long`**, un **`boolean`**, un **`double`** : la bonde les **recrache**. Les étiquettes des `case` sont **gravées dans l'inox** (des constantes). Sans **bouchon `break`**, l'eau **coule** dans tous les `case` du dessous.
- **À retenir :**
  - types acceptés : `int` et plus petits, leurs enveloppes, `String`, `enum` (et `var` qui en est un) ; **pas** `long`, `boolean`, `float`, `double` ;
  - chaque `case` est une **constante connue à la compilation** (littéral, `final` initialisé avec une constante, valeur d'`enum`) ;
  - sans `break`, l'exécution **continue** dans les `case` suivants (fall-through) ; `default` peut être n'importe où.
- **Mon image :** …

### 3. 🚿 Le robinet — l'expression `switch`

- **Image :** un robinet moderne à **flèches `->`** : chaque flèche ne remplit **qu'un seul verre**, pas de débordement. Le robinet doit avoir une sortie pour **tous les cas** (sinon il fuit : `default`). Quand une flèche ouvre un **bloc `{ }`**, le verre ne se remplit que si tu cries **« YIELD ! »** comme un cri de guerre. Et le robinet entier se termine par un **point-virgule** qui goutte.
- **À retenir :**
  - `int n = switch (x) { case 1 -> 10; default -> 0; };` : les `case ->` **ne tombent pas** dans les suivants ;
  - une expression `switch` doit être **exhaustive** : `default`, ou toutes les valeurs d'un `enum` ;
  - dans un bloc, la valeur se rend avec **`yield`** ;
  - toutes les branches doivent donner un type compatible ; ne pas oublier le **`;`** final.
- **Mon image :** …

### 4. 🔥 Les plaques — `while` et `do/while`

- **Image :** deux casseroles. La **`while`** regarde d'abord si l'eau est froide **avant** d'allumer : parfois elle n'allume **jamais**. La **`do/while`** allume le feu **tout de suite**, une fois au moins, et vérifie **après**. Son couvercle se ferme avec un **point-virgule** qui fait *tic*.
- **À retenir :**
  - `while (cond) { … }` peut s'exécuter **zéro** fois ;
  - `do { … } while (cond);` s'exécute **au moins une** fois, et se termine par **`;`** ;
  - une condition qui ne devient jamais fausse = boucle infinie : la variable doit changer **dans** la boucle.
- **Mon image :** …

### 5. ♨️ Le four — la boucle `for` et le `for` amélioré

- **Image :** le four a **trois boutons** : *départ*, *condition*, *pas* : `for (int i = 0; i < n; i++)`. Le premier bouton peut régler **plusieurs variables du même type** séparées par des virgules. Sans aucun bouton réglé, `for (;;)`, le four chauffe **pour toujours**. À côté, un **tapis à plats** (`for (var x : tableau)`) fait défiler chaque plat, sans numéro.
- **À retenir :**
  - `for (init; condition; mise à jour)` : les trois parties sont facultatives ; `for (;;)` est infini ;
  - l'initialisation peut déclarer **plusieurs variables d'un seul type** : `int i = 0, j = 5` ;
  - la variable déclarée dans le `for` n'existe **plus après** la boucle ;
  - le `for` amélioré marche sur un **tableau** ou un **`Iterable`** ; modifier sa variable ne change pas la collection.
- **Mon image :** …

### 6. 🗑️ La poubelle — `break`, `continue`, les étiquettes, le code inaccessible

- **Image :** la poubelle a des **étiquettes** collées sur ses deux couvercles, `DEHORS:` et `DEDANS:`. `break DEHORS` **jette tout** et sort de la boucle extérieure. `continue DEHORS` saute **au tour suivant** de la boucle extérieure. Une ligne écrite **juste après** un `break`, un `continue` ou un `return` tombe au fond de la poubelle : **personne ne la lira**, et le compilateur refuse.
- **À retenir :**
  - `break` sort de la boucle (ou du `switch`) la plus proche ; `break etiquette;` sort de la boucle étiquetée ;
  - `continue` passe au tour suivant ; `continue etiquette;` au tour suivant de la boucle étiquetée ;
  - une instruction **juste après** `break`, `continue` ou `return` dans le même bloc = **code inaccessible**, ne compile pas.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 1 : à quel `if` appartient un `else` ambigu ?
2. Station 2 : peut-on faire un `switch` sur un `long` ?
3. Station 3 : quel mot rend une valeur depuis un bloc d'une expression `switch` ?
4. Station 4 : combien de fois au minimum tourne un `do/while` ?
5. Station 5 : la variable `i` d'un `for` existe-t-elle après la boucle ?
6. Station 6 : que fait `continue DEHORS;` ?
