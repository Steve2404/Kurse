# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Editor.java`](Editor.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Analyser une commande

**Le code de l'étape :**

```java
static String run(String command) {
    String[] parts = command.split(" ", 2);
    String rest = parts.length > 1 ? parts[1] : "";
    switch (parts[0]) {
        case "APPEND" -> { … }
        …
        default -> { return "commande inconnue : " + parts[0]; }
    }
    return parts[0] + " -> [" + buffer + "] (" + buffer.length() + ")";
}
```

**Question — le 2e argument de `split` :** c'est la **limite** du nombre de morceaux. Avec `2`, `split` coupe **au premier** séparateur seulement, et le reste est gardé **tel quel**, espaces compris.

Vérifié :

| Appel | Résultat |
|---|---|
| `"INSERT 8 le grand ".split(" ")` | `[INSERT, 8, le, grand]` : le texte est éclaté, et l'**espace finale est perdue** (les vides de fin sont supprimés) |
| `"INSERT 8 le grand ".split(" ", 2)` | `[INSERT, 8 le grand ]` |
| `"8 le grand ".split(" ", 2)[1]` | `le grand ` (avec l'espace) |

Sans la limite, on obtiendrait `Bonjour le grandmonde`.

---

## Étape 2 — Les modifications

**Le code de l'étape :**

```java
static final StringBuilder buffer = new StringBuilder();

case "APPEND" -> { save(); buffer.append(rest); }
case "INSERT" -> {
    String[] p = rest.split(" ", 2);
    save();
    buffer.insert(Integer.parseInt(p[0]), p[1]);
}
case "REPLACE" -> {
    String[] p = rest.split(" ", 3);
    save();
    buffer.replace(Integer.parseInt(p[0]), Integer.parseInt(p[1]), p[2]);
}
case "DELETE" -> {
    String[] p = rest.split(" ");
    save();
    buffer.delete(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
}
case "DELCHAR" -> { save(); buffer.deleteCharAt(Integer.parseInt(rest)); }
```

**À la main — pourquoi `REPLACE 11 16` :**

```
B o n j o u r   l e   g  r  a  n  d     m  o  n  d  e
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21
```

`grand` occupe les positions 11 à 15. La fin étant **exclue**, l'intervalle est `[11, 16)`. Même logique pour `INSERT 8` : on insère **avant** la position 8, celle du `m` de `monde` dans `Bonjour monde`.

**Question — pourquoi peut-on chaîner `sb.append("a").append("b")` ?** `append` modifie le `StringBuilder` **et rend `this`**, le même objet. Le 2e `append` s'applique donc à ce même objet. Vérifié : `sb == sb.append("x")` vaut **`true`**. C'est le contraire de `String`, où chaque méthode rend un **nouvel** objet.

---

## Étape 3 — Couper, coller, inverser

**Le code de l'étape :**

```java
case "CUT" -> {
    String[] p = rest.split(" ");
    int start = Integer.parseInt(p[0]);
    int end = Integer.parseInt(p[1]);
    save();
    clipboard = buffer.substring(start, end);
    buffer.delete(start, end);
    return "CUT -> [" + buffer + "] (" + buffer.length() + "), presse-papiers [" + clipboard + "]";
}
case "PASTE" -> {
    String[] p = rest.split(" ", 2);
    int position = Integer.parseInt(p[0]);
    if (position > buffer.length()) {
        return "PASTE -> refuse : position " + position + " hors limites (longueur " + buffer.length() + ")";
    }
    save();
    buffer.insert(position, clipboard);
}
case "REVERSE" -> { save(); buffer.reverse(); }
```

**`substring` ne modifie pas le buffer.** Sur un `StringBuilder`, il rend un **`String`** et laisse le buffer intact. Vérifié : sur `"abcdef"`, `substring(1, 3)` rend `bc`, et le buffer vaut toujours `abcdef`. D'où l'appel à `delete` ensuite.

**Expérience — `insert(99, …)` sans vérification :**

```
java.lang.StringIndexOutOfBoundsException: offset 99, length 3
```

(vérifié sur un buffer de longueur 3.) `insert` accepte une position de 0 à `length()` inclus, et lève une exception au-delà.

---

## Étape 4 — La pile d'annulation dans un tableau

**Le code de l'étape :**

```java
static final String[] history = new String[Data.HISTORY];
static int size;

static void save() {
    if (size == history.length) {
        System.arraycopy(history, 1, history, 0, history.length - 1);
        size--;
    }
    history[size++] = buffer.toString();
}

static String undo() {
    if (size == 0) {
        return "rien a annuler";
    }
    buffer.setLength(0);
    buffer.append(history[--size]);
    return "annule";
}
```

**`System.arraycopy(src, 1, dst, 0, n - 1)` :** copie les cases 1 à n−1 vers les cases 0 à n−2. Tout glisse d'un cran vers la gauche, et la case 0 (la plus ancienne) est écrasée.

**`toString()` est indispensable :** `history[size] = buffer` rangerait le **même** objet, qui continuerait de changer. `toString()` en fait une **copie figée** (un `String` immuable).

**Question — pourquoi le texte n'est-il pas revenu à vide ?** L'historique ne garde que **5** copies. Il y a eu 9 sauvegardes : APPEND, INSERT, REPLACE, DELETE (annulée), DELCHAR, CUT, PASTE 0, REVERSE et REVERSE. Le `PASTE 99` refusé ne sauve rien. Au moment de PASTE 0, puis des deux REVERSE, l'historique était plein. **3 copies** ont donc été oubliées : `""` (avant APPEND), `Bonjour monde` (avant INSERT) et `Bonjour le grand monde` (avant REPLACE). Le plus loin qu'on puisse remonter est `Bonjour le GRAND monde`, la copie sauvée avant DELCHAR.

---

## Étape 5 — Pool de chaînes, égalité, immutabilité

**Le code de l'étape :**

```java
String a = "java";
String b = "java";
String c = new String("java");
String d = c.intern();
final String half = "ja";
String e = half + "va";
String part = "ja";
String f = part + "va";
StringBuilder s1 = new StringBuilder("x");
StringBuilder s2 = new StringBuilder("x");
String equality = "sb.equals " + s1.equals(s2) + ", contenus " + s1.toString().equals(s2.toString());
StringBuilder s3 = s1.append("y");
String immutable = "abc";
immutable.toUpperCase();
```

**Les explications :**

| Comparaison | Résultat | Pourquoi |
|---|---|---|
| `a == b` | `true` | deux littéraux identiques désignent **le même** objet du **pool** |
| `a == c` | `false` | `new String` crée **toujours** un nouvel objet, hors du pool |
| `a.equals(c)` | `true` | même **contenu** |
| `a == c.intern()` | `true` | `intern()` rend l'objet du pool qui a ce contenu |
| `a == half + "va"` | `true` | `half` est `final` et initialisé par un littéral : c'est une **constante de compilation**. `javac` calcule `"java"` lui-même et l'écrit comme un littéral |
| `a == part + "va"` | `false` | `part` n'est pas `final` : la concaténation se fait à l'**exécution** et crée un nouvel objet |
| `a == (part + "va").intern()` | `true` | `intern()` ramène au pool |
| `s1.equals(s2)` | `false` | `StringBuilder` n'a **pas redéfini** `equals` : c'est celui d'`Object`, qui compare les références |
| `s1.toString().equals(s2.toString())` | `true` | on compare des `String` |
| `s1 == s3` | `true` | `append` rend `this` |
| `new StringBuilder(50).length()` | `0` | 50 est la **capacité** (la place réservée), pas la longueur |

**Question — `immutable.toUpperCase();` seul ne change rien. Pourquoi ?** Un `String` est **immuable** : `toUpperCase()` rend un **nouveau** `String` (`ABC`) et laisse l'original intact. Ici, le résultat n'est rangé nulle part, donc il est perdu. Vérifié : après cette ligne, `immutable` vaut toujours `abc`. Il aurait fallu écrire `immutable = immutable.toUpperCase();`. C'est une question d'examen classique.
