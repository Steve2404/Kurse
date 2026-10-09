# Projet 1 — Le format d'échange : un parseur JSON écrit à la main

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 19) :**
- le format **JSON**, la langue commune de presque toutes les applications (les API web, les fichiers de configuration, les exports) ;
- un **type fermé** : une interface `sealed` et six `record`, qui décrivent **toutes** les valeurs possibles ;
- lire une **grammaire**, et l'écrire en code : le parseur par **descente récursive** ;
- des **messages d'erreur** qui disent **où** (ligne, colonne) et **quoi** ;
- la **sécurité** d'un parseur : une entrée hostile ne doit pas faire tomber le programme ;
- le **test de propriété** : des centaines de documents tirés au hasard, et une règle toujours vraie.

**Ce qui est FOURNI :** `Data.java` contient `BOARD`, l'export du tableau de tâches d'un atelier de vélos (le fil rouge du chapitre), et `BROKEN`, le même export abîmé par un copier-coller. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch19_final.projects.p01_json` : `Json`, `JsonNull`, `JsonBool`, `JsonNumber`, `JsonString`, `JsonArray`, `JsonObject` (avec son `Builder`), `JsonException`, `JsonParser`, `JsonWriter`, `JsonDemo`, et tes tests (par exemple `JsonTypesTest`, `JsonParserTest`, `JsonWriterTest`, `RoundTripTest`).

**Règle du crescendo :** tout Java 17, JUnit et Mockito, **sans bibliothèque JSON** et **sans expression régulière** : ni `Pattern`, ni `matches(`, ni `split(`, ni `replace…`, ni `Scanner`. C'est le but : comprendre ce que font ces bibliothèques. Aucune méthode de plus de **18 lignes**. Pas de `System.out`, ni de `Thread.sleep`, ni de `new Random(` dans tes tests.

> **Pourquoi écrire un parseur, alors que Jackson et Gson existent ?** En entreprise, tu utiliseras une bibliothèque. Mais un développeur senior sait **ce qu'elle fait** : pourquoi un nombre perd sa précision, pourquoi un document profond fait planter un serveur, comment lire une erreur « ligne 5, colonne 5 ». Et écrire un parseur est l'exercice qui fait le mieux travailler la **récursivité** sur un vrai problème. Tu réutiliseras ton JSON dans les projets 3 et 8.

---

## Tableau de bord

### ☐ Étape 1 — Les six cas d'une valeur JSON

**📖 La leçon : un type fermé.** Une valeur JSON est **exactement** l'une de ces six choses :

| JSON | Exemple | Ton type |
|---|---|---|
| null | `null` | `JsonNull` |
| un booléen | `true`, `false` | `JsonBool` |
| un nombre | `42`, `-3.25`, `1e3` | `JsonNumber` |
| une chaîne | `"vélo"` | `JsonString` |
| un tableau | `[1, "a", null]` | `JsonArray` |
| un objet | `{"id": 1, "tags": []}` | `JsonObject` |

Il n'y en aura **jamais** de septième. Java 17 sait l'exprimer : une interface **`sealed`** (scellée) donne la liste complète de ses implémentations avec `permits`, et le compilateur refuse toutes les autres. Chaque cas est un **`record`** (chapitre 7) : une valeur immuable, comparée par son contenu.

**Exemple sur un autre sujet :** `public sealed interface Shape permits Circle, Square {}`, puis `public record Circle(double radius) implements Shape {}` et `public record Square(double side) implements Shape {}`. Le code qui reçoit une `Shape` sait qu'il n'a que deux cas à traiter.

**📖 Deux pièges de conception :**
- **Les nombres.** JSON n'impose aucune limite de précision. Un `double` arrondit : `0.1 + 0.2` vaut `0.30000000000000004`, et un identifiant de 20 chiffres perd ses derniers chiffres. On garde donc le texte exact dans un **`BigDecimal`** (chapitre 4). Attention : `BigDecimal.equals` compare aussi l'**échelle** (le nombre de chiffres après la virgule) : `1.0` n'est pas égal à `1.00`.
- **L'ordre des clés d'un objet.** Un humain qui lit un export s'attend à retrouver `id`, `title`, `column` dans cet ordre. `Map.copyOf` **mélange** l'ordre (chapitre 18) : on copie dans une `LinkedHashMap`, puis on l'enveloppe avec `Collections.unmodifiableMap`.

**👉 À toi :**
- `public sealed interface Json permits JsonNull, JsonBool, JsonNumber, JsonString, JsonArray, JsonObject` ;
- `public record JsonNull()`, `public record JsonBool(boolean value)` ;
- `public record JsonNumber(BigDecimal value)` : refuse `null` (`Objects.requireNonNull`) ; une fabrique `public static JsonNumber of(long n)` ;
- `public record JsonString(String value)` : refuse `null` ;
- `public record JsonArray(List<Json> values)` : le constructeur compact fait une copie non modifiable avec `List.copyOf` (qui refuse aussi un élément `null`) ;
- `public record JsonObject(Map<String, Json> members)` : refuse une clé ou une valeur `null`, puis garde une copie **non modifiable** qui **conserve l'ordre**.

Tous les records implémentent `Json`.

**🧪 Les tests (`JsonTypesTest`) :** l'égalité par contenu (deux `JsonString("a")`, `JsonNumber.of(5)` et `new JsonNumber(new BigDecimal("5"))`) ; `1.0` différent de `1.00` ; les `null` refusés (cinq cas) ; la copie du tableau (modifier la liste d'origine ne change rien, et `add` lance `UnsupportedOperationException`) ; l'ordre d'un objet de **8 clés** dans le désordre (`z, y, x, w, v, u, t, s`) ; deux objets aux mêmes membres dans un ordre différent sont **égaux**, avec le même `hashCode`.

**🧪 Expériences :**
- crée un fichier `JsonDate.java` avec `public record JsonDate(String iso) implements Json {}`. Que dit le compilateur ? Supprime ensuite ce fichier ;
- dans un test, affiche (avec `assertEquals`) `Json.class.isSealed()` et `Json.class.getPermittedSubclasses().length`.

**❓ Questions :**
- Pourquoi un objet de **8 clés**, et pas 2, pour tester l'ordre ?
- Deux objets avec les mêmes membres dans un ordre différent sont égaux. Est-ce un problème, alors qu'on tient à l'ordre ?

### ☐ Étape 2 — La grammaire, et les valeurs simples

**📖 La leçon : lire une grammaire.** La norme JSON (RFC 8259, ou le site json.org et ses dessins « en voie ferrée ») décrit la langue avec des **règles** :

```
valeur  = objet | tableau | chaine | nombre | "true" | "false" | "null"
tableau = "[" ( valeur ( "," valeur )* )? "]"
objet   = "{" ( chaine ":" valeur ( "," chaine ":" valeur )* )? "}"
nombre  = "-"? ( "0" | [1-9][0-9]* ) ( "." [0-9]+ )? ( [eE] [+-]? [0-9]+ )?
chaine  = '"' ( un caractere sauf " et \ et les controles | \" | \\ | \/ | \b | \f | \n | \r | \t | \uXXXX )* '"'
espaces = espace, tabulation, \n, \r (permis entre tous les elements)
```

`|` veut dire « ou », `?` « facultatif », `*` « zéro, une ou plusieurs fois ».

**📖 La leçon : le parseur par descente récursive.** On écrit **une méthode par règle**, et un **curseur** `pos` (un `int`) qui avance dans le texte. Chaque méthode lit sa règle à partir de `pos`, et laisse `pos` juste **après** ce qu'elle a lu. Une méthode regarde **le caractère courant** pour décider quelle règle suit : `{` → un objet, `[` → un tableau, `"` → une chaîne, `-` ou un chiffre → un nombre, sinon un mot (`true`, `false`, `null`).

**Exemple sur un autre sujet :** une calculette qui lit `2+3+4` : `expression()` lit un `nombre()`, puis tant qu'elle voit `+`, avance d'un caractère et lit un autre `nombre()`.

**👉 À toi :** `public final class JsonParser`, avec un constructeur **privé** (le texte, le curseur) et `public static Json parse(String text)`. Dans cette étape, les valeurs simples :
- les mots `true`, `false`, `null` (`text.startsWith("true", pos)` regarde à partir de `pos`) ;
- les nombres : vérifie la **forme** exacte de la grammaire caractère par caractère (pas de `0` en tête comme `012`, au moins un chiffre après `.` et après `e`), puis `new BigDecimal(texte du nombre)` ;
- les chaînes, avec les **échappements** : `\"`, `\\`, `\/`, `\b`, `\f`, `\n`, `\r`, `\t` et `é` (4 chiffres hexadécimaux, majuscules ou minuscules : `Character.digit(c, 16)`) ; un caractère de contrôle (code < `0x20`, par exemple une vraie tabulation) est **interdit** dans une chaîne ;
- les espaces avant et après la valeur sont permis.

Une chaîne se construit dans un `StringBuilder`.

**🧪 Les tests (`JsonParserTest`, avec `@ParameterizedTest`) :** les trois mots ; au moins 10 nombres (dont `-0`, `1.50`, `1e3`, `2E-2`, et un nombre de 23 chiffres) ; au moins 10 chaînes (dont la chaîne vide, un `é` écrit tel quel, chaque échappement, `É` en majuscules, et un emoji écrit `😀`).

> Les données d'un test paramétré qui contiennent des guillemets et des antislashs s'écrivent mal dans `@CsvSource` : utilise `@MethodSource` et `Arguments.of(...)` (chapitre 16). Dans le code Java, `"\"a\\nb\""` est le texte JSON `"a\nb"`.

**🧪 Expérience :** parse `1e3`, puis `-1.5e+2`. Qu'affiche `assertEquals(new JsonNumber(new BigDecimal("-150")), …)` ? Et `new BigDecimal("1e3").toString()` ?

**❓ Questions :**
- Un emoji comme 😀 s'écrit `😀` : **deux** échappements. Pourquoi deux ? Que fait ton parseur avec chacun ?
- Pourquoi refuser `012`, alors que `Integer.parseInt("012")` l'accepte ?

### ☐ Étape 3 — Tableaux et objets : la récursion

**📖 La leçon : la récursion suit la grammaire.** Un tableau contient des **valeurs**, et une valeur peut être un tableau : `array()` appelle `value()`, qui peut rappeler `array()`. C'est la **pile d'appels** (chapitre 17) qui se souvient de l'endroit où l'on en était dans chaque tableau ouvert. La boucle d'un tableau suit la règle :
1. après `[`, si l'on voit `]` (après d'éventuels espaces), le tableau est vide ;
2. sinon : lire une valeur, puis voir **soit** `,` (une autre valeur suit), **soit** `]` (fin), **sinon** c'est une erreur.

Un objet suit la même boucle, avec des membres `"clé": valeur`.

**👉 À toi :**
- les tableaux et les objets, avec les espaces partout où la grammaire les permet ;
- un objet garde l'**ordre** du document (une `LinkedHashMap`) ;
- une **clé en double** est refusée (la norme laisse le choix ; une clé en double cache presque toujours une erreur) ;
- la clé vide `""` est permise.

**🧪 Les tests :** `[]`, `[ \n ]`, un tableau de trois valeurs différentes, des tableaux dans des tableaux ; `{}`, un objet avec un tableau, un objet dans un objet avec des `\r\n` autour ; l'ordre d'un objet de 5 clés ; la clé vide ; l'export `Data.BOARD` entier (le titre de la 2e tâche contient `"V-brake"` avec ses guillemets, celui de la 4e une tabulation et un antislash, ses points valent `1E+1`).

**❓ Question :** combien d'appels de `value()` sont « ouverts » en même temps, au plus profond, pendant la lecture de `[[1], [[2]]]` ?

### ☐ Étape 4 — Les erreurs : dire où et quoi

**📖 La leçon : un message d'erreur est fait pour un humain pressé.** `Unexpected character` ne sert à rien dans un fichier de 3 000 lignes. Un bon message dit **où** (ligne et colonne, comptées à partir de 1, comme dans IntelliJ) et **quoi** (ce qu'on attendait). Pour la position : la ligne est 1 + le nombre de `\n` avant l'erreur ; la colonne est la distance depuis le dernier `\n`.

**👉 À toi :** `public class JsonException extends RuntimeException` (non vérifiée : une donnée mal formée est une erreur de l'**appelant**, chapitre 11), constructeur `JsonException(String reason, int line, int column)`, message `"ligne " + line + ", colonne " + column + " : " + reason`, et deux accesseurs `line()` et `column()`. Les raisons, **exactement** :

| Raison | Quand | Position |
|---|---|---|
| `fin du texte inattendue` | le texte s'arrête alors qu'on attend encore quelque chose | la fin du texte |
| `valeur attendue` | aucune valeur ne commence ici (`tru`, `'a'`, `+1`, `]`) | le caractère |
| `nombre invalide` | un nombre mal formé (`-`, `012`, `1.`, `1e`, `1e99999999999`) | le **début** du nombre |
| `chaine non terminee` | pas de `"` fermant | la fin du texte |
| `caractere de controle dans une chaine` | un caractère de code < `0x20` dans une chaîne | ce caractère |
| `echappement invalide` | `\x`, ou un `\u` sans 4 chiffres hexadécimaux | l'**antislash** |
| `',' ou ']' attendu` / `',' ou '}' attendu` | après un élément | le caractère |
| `cle attendue` | une clé qui n'est pas une chaîne (`{a:1}`, `{"a":1,}`) | le caractère |
| `':' attendu` | après une clé | le caractère |
| `cle en double : "a"` | une clé déjà vue dans cet objet | le **début** de la 2e clé |
| `texte en trop apres la valeur` | il reste autre chose que des espaces après la valeur | ce qui reste |

`1e99999999999` a la bonne forme, mais l'exposant est trop grand pour `BigDecimal`, qui lance une `NumberFormatException` : attrape-la et transforme-la.

**🧪 Les tests :** un tableau d'au moins **40** documents faux, avec le **message entier** attendu (`assertThrows`, puis `getMessage()`), dont au moins trois sur plusieurs lignes ; `Data.BROKEN` (vérifie `line()` et `column()`) ; `JsonException` est bien une `RuntimeException`.

**🧪 Expérience :** dans IntelliJ, ouvre `Data.java`, place le curseur sur la ligne indiquée par l'erreur de `BROKEN` : la barre du bas affiche ligne et colonne. Pourquoi ne tombent-elles pas sur les mêmes nombres ?

**❓ Question :** pour `nombre invalide`, on donne le **début** du nombre ; pour `',' ou ']' attendu`, le caractère fautif. Pourquoi ces choix ?

### ☐ Étape 5 — Une entrée hostile : la profondeur

**📖 La leçon : un parseur reçoit des données d'inconnus.** Sur un serveur, n'importe qui peut envoyer n'importe quoi. Chaque `[` ouvre un appel de plus sur la **pile** : `"[".repeat(100_000)` fait déborder la pile (`StackOverflowError`), et une `Error` n'est pas une exception métier ; selon l'endroit, le fil qui lisait la requête meurt. C'est une **attaque par déni de service**, à une ligne de code. La parade : une **profondeur maximale**.

**👉 À toi :** une constante `static final int MAX_DEPTH = 500`. Chaque `[` ou `{` augmente la profondeur, chaque fermeture la diminue. La 501e ouverture imbriquée lance `trop de niveaux d'imbrication (500 au plus)`, à la position de ce `[` ou `{`.

**🧪 Les tests :** 500 niveaux passent, 501 non (`colonne 501`) ; les objets comptent aussi (250 objets + 251 tableaux) ; 1 000 tableaux **voisins** passent (la profondeur redescend) ; `"[".repeat(100_000)` donne une `JsonException`, pas une `StackOverflowError`.

**🧪 Expérience :** donne temporairement à `MAX_DEPTH` la valeur `Integer.MAX_VALUE` et parse `"[".repeat(n) + "]".repeat(n)` pour n = 1 000, 3 000, 5 000, 10 000. À partir de quand la pile déborde-t-elle ? Remets 500.

**❓ Question :** pourquoi tester **1 000 tableaux voisins** ? Quel oubli ce test attrape-t-il ?

### ☐ Étape 6 — L'écrivain

**📖 La leçon : le chemin inverse, et le prix des `String`.** Écrire un document, c'est parcourir l'arbre et produire du texte. Une `String` ne se modifie pas : `s = s + "…"` **recopie** tout le texte à chaque fois. Dans une boucle, le coût devient **quadratique** (chapitre 17). Un `StringBuilder` ajoute à la fin sans recopier.

Java 17 n'a pas encore le `switch` sur les types (il arrive en Java 21) : on enchaîne des `instanceof` avec motif (`if (json instanceof JsonArray a)`), et grâce à `sealed`, le dernier `else` ne peut être que `JsonNull`.

**👉 À toi :** `public final class JsonWriter` (constructeur privé) :
- `public static String compact(Json json)` : aucun espace : `{"id":7,"tags":["a","b"],"owner":null}` ;
- `public static String pretty(Json json)` : chaque élément sur sa ligne, **2 espaces** par niveau, `"clé": valeur` (un espace après les deux-points), et les conteneurs **vides** restent sur une ligne (`[]`, `{}`) ;
- les nombres : `value.toString()` ;
- les chaînes : `"` → `\"`, `\` → `\\`, les 5 contrôles courts (`\n`, `\r`, `\t`, `\b`, `\f`), les autres caractères de code < `0x20` → `\u` et **4 chiffres hexadécimaux en minuscules** (`String.format("\\u%04x", (int) c)`) ; tout le reste (les accents, `/`, les emojis) est écrit tel quel ;
- les clés s'échappent comme les chaînes.

**🧪 Les tests (`JsonWriterTest`) :** les valeurs simples ; au moins 10 échappements ; le même objet en `compact` et en `pretty` (un text block `"""…"""` pour le résultat attendu) ; un document imbriqué ; les conteneurs vides ; une clé à échapper ; un document de 100 000 objets écrit en moins de 3 secondes (`assertTimeoutPreemptively`).

**🧪 Expérience :** dans une méthode de test jetable, construis un texte de n morceaux avec `s = s + "{\"id\":" + i + "},"`, puis avec un `StringBuilder`, pour n = 10 000, 20 000 et 40 000, en mesurant avec `System.nanoTime()`. Quand n double, que fait chaque temps ?

**❓ Question :** pourquoi `compact` pour le réseau et `pretty` pour les fichiers lus par des humains ?

### ☐ Étape 7 — Le test de propriété : relire ce qu'on a écrit

**📖 La leçon : une propriété plutôt que des exemples.** Tes tests vérifient des exemples choisis par toi : tu ne penses pas à tout. Un **test de propriété** tire des **centaines** de valeurs au hasard et vérifie une règle toujours vraie : ici, l'**aller-retour** `parse(compact(x)).equals(x)`, et pareil avec `pretty`. Avec une graine fixe (un `SplittableRandom(numéro de répétition)`), un échec se rejoue à l'identique. Souviens-toi du chapitre 18 : les graines voisines de `new Random(…)` se ressemblent ; `SplittableRandom` n'a pas ce défaut.

**👉 À toi (`RoundTripTest`) :**
- un générateur `randomValue(SplittableRandom random, int depth)` qui tire l'un des six cas (au-delà de la profondeur 4, seulement les valeurs simples, pour que l'arbre reste petit) : des nombres avec une échelle variée (`BigDecimal.valueOf(long, scale)`, scale de -2 à 3), des chaînes faites de caractères **pièges** (`"`, `\`, `/`, `\n`, `\u0000`, `\u001f`, `é`, `€`, `{`, `]`, `,`, `:`…), des tableaux et objets de 0 à 4 éléments ;
- `@RepeatedTest(300)` : l'aller-retour en `compact` et en `pretty` ;
- `@RepeatedTest(100)` : `compact(parse(pretty(x)))` est égal à `compact(x)` ;
- un test qui vérifie que le générateur produit **vraiment** chacun des six cas (plus de 20 fois chacun sur 300 graines).

**🧪 Expérience :** garde **seulement** `RoundTripTest` (déplace les autres tests de côté) et lance `Check`. Combien de mutants survivent ? Lesquels ? Remets tes tests.

**❓ Question :** l'aller-retour passe, et pourtant ton écrivain pourrait écrire `\u001F` au lieu de `\u001f`. Pourquoi le test de propriété ne le voit-il pas ? Qu'en conclus-tu sur les deux sortes de tests ?

### ☐ Étape 8 — Lire un objet métier, et les mutants

**📖 La leçon : à la frontière, on vérifie tout.** Une application ne manipule pas des `Json` : elle veut une tâche, avec un titre (une chaîne) et des points (un entier). La lecture **typée** vérifie chaque champ et nomme celui qui manque, pour que le message serve à celui qui a envoyé le document.

**👉 À toi :**
- dans `JsonObject` : `Optional<Json> get(String key)` ; `String getString(String key)`, `long getLong(String key)` et `boolean getBoolean(String key)`, qui lancent `IllegalArgumentException("champ " + key + " : chaine attendue")` (ou `nombre entier attendu`, `booleen attendu`) si le champ manque ou n'a pas le bon type. Un entier : `longValueExact()`, qui refuse `1.5` et les nombres trop grands (`2.0` et `1e3` sont acceptés) ;
- `JsonObject.builder()` rend un `JsonObject.Builder` (classe imbriquée `public static final`, constructeur privé, chapitre 18) : `add(String key, Json value)`, et trois raccourcis `add(key, String)`, `add(key, long)`, `add(key, boolean)` ; une clé en double lance `IllegalArgumentException("cle en double : " + key)` ; `build()` rend un objet dans l'ordre des `add`, qui ne change plus si l'on continue à remplir le builder ;
- `public final class JsonDemo` avec `main` : parse `Data.BOARD`, affiche `Atelier vélos : version 3`, la version `compact`, les colonnes en `pretty`, puis `refus : ` suivi du message d'erreur de `Data.BROKEN`.

**🧪 Les tests :** les lectures typées (dont `2.0`, `1e3`, un champ absent) et leurs sept refus, avec le message exact ; le builder (l'ordre, le doublon, l'indépendance après `build()`).

**👉 Puis :** lance `Check`. Les 24 mutants changent le parseur (la profondeur, un échappement, la forme d'un nombre, les positions…), l'écrivain ou les types.

**🧪 Expérience :** lance `JsonDemo`. Qu'est devenu `1e1` dans la sortie compacte ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `sealed interface Json permits`, les six `record`, `class JsonException extends RuntimeException`, `final class JsonParser`, `final class JsonWriter`, `final class JsonDemo`, `BigDecimal`, `StringBuilder`, `List.copyOf(`, `LinkedHashMap`, `Data.BOARD` ; ni `Pattern`, ni `matches(`, ni `split(`, ni `replace`, ni `Scanner`.
- **La conception :** aucune méthode de plus de **18 lignes** ; `JsonParser.java` contient `MAX_DEPTH` ; `JsonObject.java` ne contient pas `Map.copyOf`.
- **Tes tests :** au moins **60** tests (chaque cas d'un test paramétré ou répété compte), `@ParameterizedTest`, `SplittableRandom`, `@RepeatedTest`, `assertThrows(`, `assertTimeoutPreemptively(`, `getMessage()` ; ni `System.out`, ni `Thread.sleep`, ni `new Random(`.
- **Les 24 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch19_final.projects.p01_json ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 516 tests, 516 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 24/24 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```

Le `Check` de ce projet prend environ 20 secondes : il lance tes tests une fois par mutant.
