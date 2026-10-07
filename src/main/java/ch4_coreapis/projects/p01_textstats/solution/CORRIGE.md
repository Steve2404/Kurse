# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`TextStats.java`](TextStats.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Découper et compter

**Le code de l'étape :**

```java
static String[] words(String text) {
    String clean = text.replace('.', ' ').replace(',', ' ').replace(':', ' ').replace(';', ' ').replace('\n', ' ');
    return clean.strip().split(" +");
}

static int vowels(String text) {
    int count = 0;
    for (int i = 0; i < text.length(); i++) {
        if ("aeiouy".indexOf(Character.toLowerCase(text.charAt(i))) >= 0) {
            count++;
        }
    }
    return count;
}
```

Puis `text.split("\n")` pour les lignes, et une boucle for-each pour le plus long et le plus court.

**Question — l'expression « une espace ou plus » :** `" +"`. Le `+` d'une expression régulière signifie « l'élément précédent, une fois ou plus ». Avec `split(" ")`, chaque espace est un séparateur, et deux espaces consécutives entourent une chaîne **vide**. Vérifié : `"a  b".split(" ")` donne `[a, , b]` (3 éléments), et `"a  b".split(" +")` donne `[a, b]`.

**Deux détails de `split` (vérifiés) :**
- Une espace **en tête** donne un premier élément vide : `" a b".split(" ")` donne `[, a, b]`. C'est pour ça qu'on fait `strip()` avant.
- Les chaînes vides **en fin** de résultat sont supprimées : `"a b  ".split(" ")` donne `[a, b]`.

**Question — `length()` compte-t-il les `\n` ?** **Oui** : un `\n` est un caractère comme un autre. Les 200 caractères incluent les 3 sauts de ligne. Le text block se termine par un `\n`, car ses `"""` fermants sont seuls sur leur ligne.

**À retenir :** `String` est **immuable**. `replace`, `strip` et `toLowerCase` rendent une **nouvelle** chaîne, d'où l'enchaînement.

---

## Étape 2 — Les mots les plus fréquents, sans collection

**Le code de l'étape :**

```java
String[] sorted = new String[words.length];
for (int i = 0; i < words.length; i++) {
    sorted[i] = words[i].toLowerCase();
}
Arrays.sort(sorted);
for (int i = 0; i < sorted.length; ) {
    int j = i;
    while (j < sorted.length && sorted[j].equals(sorted[i])) {
        j++;
    }
    int run = j - i;
    if (run > topCount) {
        second = top; secondCount = topCount;
        top = sorted[i]; topCount = run;
    } else if (run > secondCount) {
        second = sorted[i]; secondCount = run;
    }
    i = j;
}
```

**Pourquoi trier ?** Sans `Map`, on ne peut pas associer un mot à un compteur. Une fois triés, les mots égaux sont **voisins**, et compter une série se fait en une passe. Ici, `le` et `un` apparaissent 4 fois chacun. `le` vient avant dans l'ordre alphabétique, donc il est trouvé en premier, et `>` (strict) ne le remplace pas par `un`.

**La boucle `for` sans 3e partie :** `for (int i = 0; i < n; )`. C'est le corps qui avance `i` (`i = j`), d'une série entière à la fois.

**Question — pourquoi `equals` et pas `==` ?** `==` compare les **références**. `toLowerCase()` crée une **nouvelle** chaîne à chaque appel qui change quelque chose. Vérifié : `"Le".toLowerCase() == "LE".toLowerCase()` vaut `false`, alors que `equals` vaut `true`. Piège supplémentaire : si rien ne change, `"le".toLowerCase()` rend le **même** objet, et `==` donne `true`. Le résultat de `==` dépend donc des données : un bug intermittent garanti.

---

## Étape 3 — Palindromes, positions, censure

**Le code de l'étape :**

```java
static boolean palindrome(String word) {
    String w = word.toLowerCase();
    for (int i = 0, j = w.length() - 1; i < j; i++, j--) {
        if (w.charAt(i) != w.charAt(j)) {
            return false;
        }
    }
    return w.length() > 2;
}

String seen = " ";
for (String w : words) {
    String key = " " + w.toLowerCase() + " ";
    if (palindrome(w) && !seen.contains(key)) {
        palindromes = palindromes + w.toLowerCase() + " ";
        seen = seen + w.toLowerCase() + " ";
    }
}

int from = 0;
int found;
while ((found = text.indexOf(Data.CENSORED, from)) >= 0) {
    positions = positions + found + " ";
    from = found + 1;
}
lines[2].replace(Data.CENSORED, "*".repeat(Data.CENSORED.length()))
```

**Pourquoi des espaces autour de la clé ?** Sans eux, `seen.contains("elle")` serait vrai si `seen` contenait déjà `"belle"`. Les espaces forcent un mot **entier**.

**L'affectation dans la condition :** `(found = text.indexOf(…)) >= 0` affecte `found`, **puis** compare sa valeur (chapitre 2). Les parenthèses sont obligatoires : `>=` est plus prioritaire que `=`.

**`from = found + 1` :** repartir **après** le début de l'occurrence trouvée. Avec `from = found`, `indexOf` retrouverait la même position indéfiniment.

---

## Étape 4 — Mise en titre et tests

**Le code de l'étape :**

```java
static String capitalize(String word) {
    return word.isEmpty() ? word : word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
}

for (String w : lines[0].split(" ")) {
    title = title + capitalize(w) + " ";
}
… line.startsWith("Le ") … line.endsWith(".") … text.contains("Bob") … "KAYAK".equalsIgnoreCase(words[3])
```

**Question — les bornes de `substring` :**
- `"a".substring(1)` rend **`""`**. L'indice de début peut valoir `length()` : il désigne la fin.
- `"".substring(0, 1)` **lève une exception**, car la fin dépasse la longueur :

```
java.lang.StringIndexOutOfBoundsException: begin 0, end 1, length 0
```

C'est pour ça que `capitalize` teste `isEmpty()` d'abord.

**Les résultats :**
- **Commencent par « Le »** : 1. La 2e ligne commence par `Elle`, la 3e par `Un`.
- **Finissent par « . »** : 3. Attention, `split("\n")` ne garde pas les `\n`.

---

## Étape 5 — Nettoyage et méthodes de Java 11 à 15

**Le code de l'étape :**

```java
String messy = Data.MESSY;
System.out.println("SALE : [" + messy + "] -> strip [" + messy.strip() + "] -> stripLeading [" + messy.stripLeading()
        + "] -> stripTrailing [" + messy.stripTrailing() + "]");
System.out.println("ECHAPPEMENTS : [" + messy.strip().translateEscapes() + "], vide " + "".isEmpty() + ", blanc " + "   ".isBlank()
        + ", trim [" + " \t x \t ".trim() + "]");
System.out.print("INDENTE :\n" + "a\nb".indent(2));
System.out.println("DESINDENTE :\n" + "   x\n     y".stripIndent() + "\n" + "   x\n     y\n".stripIndent().length());
```

**`translateEscapes()` :** dans `Data`, `"\\t"` est écrit avec deux antislashs : la chaîne contient **deux** caractères, `\` et `t`. `translateEscapes()` les remplace par **une** vraie tabulation. Vérifié : 4 caractères (`a\tb`) deviennent 3 (`a`, tabulation de code 9, `b`).

**`isEmpty` et `isBlank` :** `"".isEmpty()` est vrai (longueur 0). `"   ".isBlank()` est vrai (rien que des blancs), alors que `"   ".isEmpty()` serait faux.

**Question — `trim` contre `strip` :**
- `trim()` retire les caractères de code **≤ 32** (espace, `\t`, `\n`…).
- `strip()` retire tous les **blancs Unicode**, comme l'espace cadratin ` `.

Vérifié : sur `" x "`, `trim()` ne retire rien (longueur 3), alors que `strip()` donne `x` (longueur 1).

**`indent(2)` :** ajoute 2 espaces devant **chaque** ligne et termine par `\n`. `indent(-2)` retire jusqu'à 2 espaces. Vérifié aussi : `"a\r\nb".indent(0)` normalise `\r\n` en `\n` (longueur 4).

**Le piège du `12` :** `stripIndent()` retire l'indentation **commune** à toutes les lignes. Mais `"   x\n     y\n"` a **trois** lignes : `   x`, `     y`, et une dernière ligne **vide** après le `\n`. Une dernière ligne vide compte pour **0** espace d'indentation. Le minimum est donc 0, et rien n'est retiré : la longueur reste 3 + 1 + 5 + 1 + 2 = **12**. Sans le `\n` final, il y a deux lignes, le minimum vaut 3, et on obtient `x` et `  y`.

---

## Étape 6 — Formatage et chaînage

**Le code de l'étape :**

```java
"%-6s|%4d|%s".formatted(shortest, words.length, true) + " " + String.format("[%5s]", "ok")
"  Hello World  ".strip().toLowerCase().replace("o", "0").substring(6).concat("!")
```

**Question — le `-` et le nombre :**
- Le nombre est une **largeur minimale**.
- Le `-` aligne à **gauche**, en complétant à droite. Sans `-`, on aligne à droite.

Vérifié :
- `"%-6s|".formatted("a")` donne `a     |` ;
- `String.format("[%5s]", "ok")` donne `[   ok]`.

**Une valeur plus longue que la largeur n'est jamais coupée.** La largeur est un **minimum**. Vérifié : `%-6s` sur `"abcdefgh"` donne `abcdefgh`, `%4d` sur `123456` donne `123456`, et `[%5s]` sur `"toolong"` donne `[toolong]`.

**Le chaînage, appel par appel (vérifié) :**

| Appel | Résultat |
|---|---|
| `strip()` | `Hello World` |
| `toLowerCase()` | `hello world` |
| `replace("o", "0")` | `hell0 w0rld` (**toutes** les occurrences) |
| `substring(6)` | `w0rld` (h=0 e=1 l=2 l=3 0=4 espace=5) |
| `concat("!")` | `w0rld!` |

Chaque appel s'applique au **résultat** du précédent, une nouvelle chaîne à chaque fois. L'original `"  Hello World  "` n'est jamais modifié.
