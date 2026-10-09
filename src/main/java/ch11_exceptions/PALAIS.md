# 🏠 Palais mental — chapitre 11 : la douche 1, stations 1 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Le salon, la cuisine et les trois chambres (chapitres 1 à 10) viennent avant dans la balade.
> **Quand :** stations 1 à 6 après les projets sur les exceptions, stations 7 à 12 après les projets sur la localisation ; puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

## Les exceptions (stations 1 à 6)

### 1. 🚪 La porte de la douche — la famille des exceptions

- **Image :** sur la porte, un **arbre généalogique** qui dégouline. Tout en haut, l'ancêtre **`Throwable`**. À gauche, la branche **`Error`** : des **inondations** (le système), qu'on ne rattrape pas. À droite, **`Exception`** : des **gouttes vérifiées** que le compilateur t'oblige à **éponger** (`catch`) ou à **signaler** (`throws`). Sous elle, une branche rebelle **`RuntimeException`** : des gouttes **non vérifiées**, que personne ne t'oblige à traiter.
- **À retenir :**
  - `Throwable` → `Error` (grave, ne pas attraper) et `Exception` ;
  - `RuntimeException` et ses filles : **non vérifiées** ;
  - les autres `Exception` (`IOException`, `ParseException`…) : **vérifiées**, à attraper ou déclarer avec `throws` ;
  - `throw` lance une exception ; `throws` la déclare dans la signature.
- **Mon image :** …

### 2. 🪞 Le miroir — `try`, `catch`, `finally`

- **Image :** dans le miroir, trois serviettes accrochées. **`try`** : tu tentes la douche. Les **`catch`** attrapent l'eau, **de la plus petite à la plus grande** : si la **grande serviette** (`Exception`) est accrochée **avant** la petite (`IOException`), la petite ne servira **jamais**, et le compilateur refuse. **`finally`** est la serviette qui **sert toujours**, même après un `return`. Si elle fait elle-même un `return`, elle **écrase** tout le reste.
- **À retenir :**
  - `try` doit être suivi d'au moins un `catch` ou d'un `finally` (sauf `try` avec ressources) ;
  - les `catch` vont du **plus précis** au **plus général** ; l'inverse = code inaccessible, ne compile pas ;
  - `finally` s'exécute **toujours** (sauf `System.exit`) ; un `return` dans `finally` remplace le résultat et efface l'exception ;
  - attraper une exception vérifiée que le `try` **ne peut pas** lancer ne compile pas.
- **Mon image :** …

### 3. 🚰 Le lavabo — le multi-catch

- **Image :** le lavabo a **deux bondes** reliées par une **barre `|`** : `catch (FileNotFoundException | NumberFormatException e)`. Les deux bondes doivent être **de familles différentes** : si l'une est la **mère** de l'autre, le lavabo **se bouche** (ne compile pas). La variable `e` est **coulée dans le béton** : tu ne peux pas la réaffecter.
- **À retenir :**
  - `catch (A | B e)` : un seul bloc pour plusieurs types ;
  - `A` et `B` ne doivent **pas** être parent et enfant ;
  - `e` est implicitement `final` ;
  - une seule variable pour tous les types.
- **Mon image :** …

### 4. 🚿 Le robinet — le `try` avec ressources

- **Image :** tu ouvres **trois robinets** dans l'ordre A, B, C, entre les parenthèses du `try`. Quand la douche finit (normalement ou à cause d'une exception), ils se **referment tout seuls, à l'envers** : C, B, A, **avant** même que les serviettes `catch` et `finally` ne servent. Si un robinet **fuit en se fermant**, sa fuite est **rangée dans la poche** de l'exception principale (`getSuppressed()`).
- **À retenir :**
  - `try (var a = …; var b = …) { … }` : les ressources implémentent `AutoCloseable` (ou `Closeable`) ;
  - fermeture **dans l'ordre inverse** de la déclaration, **avant** `catch` et `finally` ;
  - une exception lancée par `close()` pendant une autre exception devient **supprimée** : `e.getSuppressed()` ;
  - une ressource déclarée avant le `try` doit être `final` ou effectivement finale : `try (a; b)`.
- **Mon image :** …

### 5. 🚿 Le pommeau — les exceptions classiques

- **Image :** le pommeau crache **un jet différent par trou** : `ArithmeticException` (division d'un entier par zéro), `ArrayIndexOutOfBoundsException` (case inexistante), `ClassCastException` (mauvais déguisement), `NullPointerException` (tu tapes sur du vide), `NumberFormatException` (`"abc"` n'est pas un nombre ; c'est une fille d'`IllegalArgumentException`). Au milieu, un jet glacé `Error` : `StackOverflowError` (récursion sans fin), `ExceptionInInitializerError` (un bloc `static` qui explose).
- **À retenir :**
  - non vérifiées : `ArithmeticException`, `ArrayIndexOutOfBoundsException`, `ClassCastException`, `NullPointerException`, `IllegalArgumentException` → `NumberFormatException`, `IllegalStateException`, `UnsupportedOperationException` ;
  - vérifiées : `IOException` → `FileNotFoundException` ; `ParseException` ;
  - erreurs : `StackOverflowError`, `ExceptionInInitializerError`, `NoClassDefFoundError`, `OutOfMemoryError`.
- **Mon image :** …

### 6. 🧼 Le savon — redéfinition, exceptions personnalisées

- **Image :** le savon de l'enfant doit **glisser moins** que celui du parent : une méthode redéfinie ne peut **pas** lancer une exception vérifiée **nouvelle ou plus large**. Elle peut en lancer **moins**, ou des non vérifiées autant qu'elle veut. Tu peux **sculpter ton propre savon** : une classe qui `extends Exception` (vérifiée) ou `extends RuntimeException` (non vérifiée), avec un constructeur qui prend un **message** et une **cause**.
- **À retenir :**
  - redéfinition : pas d'exception vérifiée nouvelle ou plus large ; moins ou aucune, c'est permis ; les non vérifiées sont libres ;
  - exception personnalisée : `extends Exception` (vérifiée) ou `extends RuntimeException` (non vérifiée) ;
  - constructeurs habituels : `()`, `(String message)`, `(Throwable cause)`, `(String message, Throwable cause)` ;
  - `getMessage()`, `getCause()`, `printStackTrace()`.
- **Mon image :** …

---

## La localisation (stations 7 à 12)

### 7. 🧺 La serviette — `Locale`

- **Image :** la serviette porte deux broderies : la **langue en minuscules** (`fr`) et le **pays en MAJUSCULES** (`FR`), reliées par un **tiret bas** : `fr_FR`. Tu peux l'acheter toute faite (`Locale.FRANCE`), la coudre avec `new Locale("fr", "FR")`, ou la tricoter avec un `Locale.Builder`. `Locale.setDefault` change la serviette de **toute la maison**.
- **À retenir :**
  - format : `langue_PAYS` (`fr_FR`, `en_US`) ; la langue seule (`fr`) est valide ;
  - en Java 17 : `Locale.FRANCE`, `new Locale("fr", "FR")`, `new Locale.Builder().setLanguage("fr").setRegion("FR").build()` ;
  - `Locale.getDefault()`, `Locale.setDefault(l)`, et `setDefault(Locale.Category.FORMAT, l)` / `DISPLAY`.
- **Mon image :** …

### 8. 🛁 Le tapis de bain — `NumberFormat`

- **Image :** sur le tapis, une **machine à imprimer les nombres** avec quatre boutons : `getInstance` (nombre), `getCurrencyInstance` (**argent**), `getPercentInstance` (**pourcentage** : 0,5 devient 50 %), `getCompactNumberInstance` qui **écrase** 7 123 456 en **« 7M »** (style `SHORT`) ou « 7 million » (style `LONG`). Dans l'autre sens, `parse` lit un texte, et s'il échoue, il lance une **`ParseException` vérifiée** qu'il faut éponger.
- **À retenir :**
  - `NumberFormat.getInstance(l)`, `getCurrencyInstance(l)`, `getPercentInstance(l)`, `getIntegerInstance(l)` ;
  - `getCompactNumberInstance(l, NumberFormat.Style.SHORT)` : `7_123_456` → `"7M"` (en `en_US`) ;
  - `format` : nombre → texte ; `parse` : texte → `Number`, lance `ParseException` (**vérifiée**) ;
  - en `fr_FR`, les milliers sont séparés par une espace insécable.
- **Mon image :** …

### 9. 🪝 Le porte-serviette — `DecimalFormat`

- **Image :** deux crochets sur le porte-serviette. Le crochet **`#`** est **timide** : s'il n'y a pas de chiffre, il **disparaît**. Le crochet **`0`** est **têtu** : il accroche un **zéro** même s'il n'y a rien. `new DecimalFormat("#,##0.00")` affiche 1234,5 comme `1,234.50` (en anglais).
- **À retenir :**
  - `#` : chiffre facultatif (rien si absent) ; `0` : chiffre obligatoire (zéro si absent) ;
  - `,` : séparateur de milliers ; `.` : séparateur décimal (traduits selon la `Locale`) ;
  - les décimales en trop sont **arrondies** (mode `HALF_EVEN` par défaut).
- **Mon image :** …

### 10. 🪥 La brosse à dents — `DateTimeFormatter`

- **Image :** sur le manche de la brosse, un **motif gravé** `dd MMMM yyyy`. Le **grand `M`** est le **Mois**, le **petit `m`** est la **minute** (le petit dort moins longtemps). Le **grand `H`** compte de 0 à 23, le **petit `h`** de 1 à 12. Un mot ordinaire doit être **entre apostrophes** : `'à'`. Si tu demandes l'**heure** à une simple **date** (`LocalDate`), la brosse **se casse** (`UnsupportedTemporalTypeException`).
- **À retenir :**
  - `DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", l)` ; `date.format(f)` ou `f.format(date)` ;
  - `M` mois, `m` minute ; `H` 0–23, `h` 1–12 ; `MMM` mois abrégé, `MMMM` mois en entier ; `E` jour de la semaine ;
  - du texte entre `'…'` ; `''` pour une apostrophe ;
  - formater une `LocalDate` avec un motif d'heure → `UnsupportedTemporalTypeException`.
- **Mon image :** …

### 11. 🗄️ L'étagère — `ResourceBundle`

- **Image :** sur l'étagère, des **flacons de shampoing** par langue. Tu demandes `fr_FR`. Tu cherches **dans cet ordre** : `fr_FR`, puis `fr`, puis la **langue de la maison** (la `Locale` par défaut, par exemple `de_DE` puis `de`), puis le **flacon sans étiquette** (le fichier de base). Quand tu as trouvé ton flacon, il peut **emprunter** des clés à ses **parents** (`fr_FR` → `fr` → base), mais **jamais** à la maison par défaut. Une clé introuvable fait **tomber l'étagère** (`MissingResourceException`).
- **À retenir :**
  - recherche : `langue_PAYS` demandé → `langue` demandée → `Locale` par défaut (`langue_PAYS`, puis `langue`) → fichier de base → `MissingResourceException` ;
  - une fois le fichier trouvé, les clés manquantes viennent de ses **parents** seulement (même langue, puis base) ;
  - `ResourceBundle.getBundle("Nom", locale)`, `getString(cle)`, `keySet()` ;
  - fichiers `Nom_fr_FR.properties` ; `cle=valeur`, `cle:valeur` ou `cle valeur`.
- **Mon image :** …

### 12. 🗑️ La poubelle — `MessageFormat` et `Properties`

- **Image :** sur la poubelle, un **pochoir** avec des trous numérotés : `"Bonjour {0}, tu as {1} messages"`. `MessageFormat.format` remplit les trous **dans l'ordre des numéros**. À côté, un **carnet `Properties`** : `getProperty(cle)` rend `null` si la clé manque, `getProperty(cle, secours)` rend la valeur de secours.
- **À retenir :**
  - `MessageFormat.format("… {0} … {1}", a, b)` : les `{n}` sont remplacés par les arguments ;
  - `Properties` : `setProperty`, `getProperty(cle)` (→ `null` si absente), `getProperty(cle, defaut)` ;
  - `Properties` se manipule comme une `Map` de chaînes (c'est une `Hashtable`) ; `load` lit un fichier `.properties`.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 1 : `IOException` est-elle vérifiée ? Et `IllegalArgumentException` ?
2. Station 2 : pourquoi `catch (Exception e)` avant `catch (IOException e)` ne compile-t-il pas ?
3. Station 3 : peut-on écrire `catch (IOException | Exception e)` ?
4. Station 4 : dans quel ordre se ferment les ressources ?
5. Station 5 : de quelle classe hérite `NumberFormatException` ?
6. Station 6 : une méthode redéfinie peut-elle ajouter `throws IOException` ?
7. Station 7 : quelle partie de `fr_FR` est en majuscules ?
8. Station 8 : quelle exception lance `NumberFormat.parse` ?
9. Station 9 : quelle est la différence entre `#` et `0` ?
10. Station 10 : que veut dire `mm` ? et `MM` ?
11. Station 11 : après `fr_FR` et `fr`, où cherche-t-on ?
12. Station 12 : que rend `getProperty(cle)` si la clé manque ?
