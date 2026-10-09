# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`JsonTypesTest.java`](JsonTypesTest.java), [`JsonParserTest.java`](JsonParserTest.java), [`JsonWriterTest.java`](JsonWriterTest.java) et [`RoundTripTest.java`](RoundTripTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17.0.18** (Temurin) et **JUnit 5.11.4**, le 9 octobre 2026.

---

## Étape 1 — Les six cas

Le code : [`Json.java`](Json.java), [`JsonNull.java`](JsonNull.java), [`JsonBool.java`](JsonBool.java), [`JsonNumber.java`](JsonNumber.java), [`JsonString.java`](JsonString.java), [`JsonArray.java`](JsonArray.java), [`JsonObject.java`](JsonObject.java).

Le cœur de `JsonObject` :

```java
public JsonObject {
    members.forEach((key, value) -> {
        Objects.requireNonNull(key, "cle");
        Objects.requireNonNull(value, "valeur de " + key);
    });
    members = Collections.unmodifiableMap(new LinkedHashMap<>(members));
}
```

**Expérience — un septième cas :** le compilateur refuse (vérifié) :

```
JsonDate.java:2: error: class is not allowed to extend sealed class: Json (as it is not listed in its permits clause)
```

Et `Json.class.isSealed()` vaut `true`, `getPermittedSubclasses().length` vaut `6`.

**Question — 8 clés :** `Map.copyOf` rend une map dont l'ordre dépend du **hachage** des clés et d'un sel tiré à chaque lancement de la JVM (chapitre 18). Avec 2 clés, l'ordre a une chance sur deux de tomber juste par hasard : le test passerait parfois avec un code faux. Avec 8 clés dans le désordre, la chance de retrouver par hasard l'ordre exact est d'une sur 40 320 (8 !).

**Question — égaux dans un ordre différent :** non. L'**égalité** dit « même contenu » : `{"a":1,"b":2}` et `{"b":2,"a":1}` décrivent le même objet pour la norme JSON, qui dit que l'ordre des membres n'a pas de sens. L'**ordre** gardé sert à la **présentation** (un export lisible, un fichier qui ne change pas d'une écriture à l'autre, des différences propres dans Git). `equals` vient de `Map.equals`, qui ignore l'ordre, et `hashCode` est cohérent avec lui.

---

## Étape 2 — La grammaire et les valeurs simples

Le code : [`JsonParser.java`](JsonParser.java), méthodes `value`, `literal`, `string`, `escape`, `unicode`, `number`, `integerPart`, `exponent`. Le nombre suit la grammaire mot pour mot :

```java
private JsonNumber number() {
    int start = pos;
    skip('-');
    integerPart(start);
    if (skip('.') && !digits()) {
        throw errorAt(start, "nombre invalide");
    }
    exponent(start);
    try {
        return new JsonNumber(new BigDecimal(text.substring(start, pos)));
    } catch (NumberFormatException e) {
        throw errorAt(start, "nombre invalide");
    }
}
```

**Expérience — `-1.5e+2` :** le test échoue (vérifié) :

```
expected: <JsonNumber[value=-150]> but was: <JsonNumber[value=-1.5E+2]>
```

`BigDecimal` garde le nombre **tel qu'il est écrit** : 15 chiffres significatifs « -15 » et une échelle de -1 (soit -15 × 10¹). C'est la même **valeur** que -150 (`compareTo` rend 0), mais pas le même **objet** pour `equals`. Et `new BigDecimal("1e3").toString()` rend `1E+3`. Le test de référence attend donc `new BigDecimal("-1.5E+2")`.

**Question — l'emoji :** Java range ses chaînes en **UTF-16** : un `char` fait 16 bits, soit 65 536 valeurs, et 😀 (U+1F600) n'y tient pas. Il s'écrit avec **deux** `char`, une **paire de substitution** (`\ud83d` puis `\ude00`). JSON a repris la même règle. Ton parseur n'a rien de spécial à faire : il décode chaque `\u` en un `char` et les ajoute l'un après l'autre ; la chaîne Java contient alors la paire, c'est-à-dire l'emoji (`length()` vaut 2).

**Question — `012` :** la grammaire JSON l'interdit, et ce n'est pas un caprice : dans plusieurs langages (JavaScript ancien, C, Java pour les littéraux), un `0` en tête annonce un nombre en **octal** : `012` vaut 10. Le refuser évite que deux programmes lisent le même document différemment.

---

## Étape 3 — Tableaux et objets

Le code : `array`, `object`, `member`, et les outils `closes`, `separator`, `peekOrFail`.

```java
private JsonArray array() {
    enter();
    List<Json> values = new ArrayList<>();
    if (!closes(']')) {
        do {
            values.add(value());
        } while (separator(']', "',' ou ']' attendu"));
    }
    depth--;
    return new JsonArray(values);
}
```

**Question — `[[1], [[2]]]` :** **4** appels de `value()` ouverts en même temps au plus profond : celui du tableau extérieur, celui de `[[2]]`, celui de `[2]`, et celui de `2`. Chacun attend que le suivant ait fini pour continuer sa boucle. La profondeur de la pile suit la profondeur du document : c'est tout l'enjeu de l'étape 5.

---

## Étape 4 — Les erreurs

Le code : [`JsonException.java`](JsonException.java), et `error` / `errorAt` dans le parseur. Les 48 documents faux du test de référence `errors` donnent tous le message attendu (vérifié) ; quelques-uns, pour comparer avec les tiens :

| Document (texte Java) | Message |
|---|---|
| `""` | `ligne 1, colonne 1 : fin du texte inattendue` |
| `"[1,]"` | `ligne 1, colonne 4 : valeur attendue` |
| `"\"\\u12g4\""` | `ligne 1, colonne 2 : echappement invalide` |
| `"{\"a\":1,\"b\":2,\"a\":3}"` | `ligne 1, colonne 14 : cle en double : "a"` |
| `"truex"` | `ligne 1, colonne 5 : texte en trop apres la valeur` |
| `"[\n  1,\n  2\n  3\n]"` | `ligne 4, colonne 3 : ',' ou ']' attendu` |
| `"\r\n\r\n  @"` | `ligne 3, colonne 3 : valeur attendue` |

Et `Data.BROKEN` : `ligne 5, colonne 5 : ',' ou ']' attendu`.

**Expérience — la position dans IntelliJ :** l'accolade fautive est à la **ligne 33, colonne 17** de `Data.java` (vérifié). La ligne 5 du texte est bien la ligne 33 du fichier (le texte commence à la ligne 29) ; la colonne diffère de **12** : un text block retire l'indentation commune de ses lignes (ici les 12 espaces alignés sur les `"""` fermants). Le parseur compte dans **le texte qu'il reçoit**, pas dans le fichier source : c'est pour cela qu'un message d'erreur doit aussi dire **quel** document est en cause.

**Question — le début du nombre :** `1e99999999999` est faux **dans son ensemble** : aucun caractère n'est fautif seul, et l'humain doit voir le nombre entier, donc on pointe son début. Pour `[1 2]`, au contraire, `1` est juste, et c'est le `2` qui arrive au mauvais endroit : on pointe ce caractère, là où il faut ajouter la virgule.

---

## Étape 5 — La profondeur

```java
private void enter() {
    if (++depth > MAX_DEPTH) {
        throw error("trop de niveaux d'imbrication (" + MAX_DEPTH + " au plus)");
    }
    pos++; // '[' ou '{'
}
```

**Expérience — sans limite :** avec `MAX_DEPTH = Integer.MAX_VALUE` (vérifié, pile par défaut de la JVM sous Windows) : 1 000, 2 000 et 3 000 niveaux passent ; à **5 000** niveaux, `StackOverflowError`. Un document de 10 Ko (`[` répété 5 000 fois) suffit donc à faire tomber le fil qui le lit. Avec la limite de 500 : `ligne 1, colonne 501 : trop de niveaux d'imbrication (500 au plus)`, même pour 100 000 `[`.

**Question — 1 000 voisins :** il attrape l'oubli de `depth--`. Sans lui, la profondeur ne fait que **monter** : chaque tableau vide de `[[],[],[],…]` l'augmente d'un, et le 500e voisin dépasse la limite, alors que le document n'a que **deux** niveaux. Le mutant 2 fait exactement cela (et le mutant 3 pour les objets).

---

## Étape 6 — L'écrivain

Le code : [`JsonWriter.java`](JsonWriter.java).

```java
private static void escape(char c, StringBuilder out) {
    switch (c) {
        case '"' -> out.append("\\\"");
        case '\\' -> out.append("\\\\");
        case '\n' -> out.append("\\n");
        case '\r' -> out.append("\\r");
        case '\t' -> out.append("\\t");
        case '\b' -> out.append("\\b");
        case '\f' -> out.append("\\f");
        default -> out.append(c < 0x20 ? String.format("\\u%04x", (int) c) : String.valueOf(c));
    }
}
```

**Expérience — `+` contre `StringBuilder` (vérifié, une mesure sur cette machine) :**

| n | `s = s + …` | `StringBuilder` |
|---|---|---|
| 10 000 | 102 ms | 1 ms |
| 20 000 | 297 ms | 1 ms |
| 40 000 | 1 050 ms | moins de 1 ms |

Quand n double, la concaténation prend environ **3 à 4 fois** plus de temps : elle est quadratique, chaque `+` recopie tout le texte déjà écrit. Le `StringBuilder` reste linéaire. Pour 100 000 tâches, la lecture de 3,5 millions de caractères prend environ 20 à 70 ms, et l'écriture compacte autant.

**Question — compact ou pretty :** sur le réseau, chaque octet compte et personne ne lit le texte : pour 100 tâches comme celles de l'atelier, `compact` fait 9 681 caractères contre 15 782 en `pretty`, soit 39 % de moins (vérifié). Un fichier de configuration ou un export est **lu** et **comparé** par des humains (et par `git diff`, ligne par ligne) : `pretty` met un élément par ligne, et une modification ne touche qu'une ligne.

---

## Étape 7 — Le test de propriété

Le code : [`RoundTripTest.java`](RoundTripTest.java).

**Expérience — l'aller-retour seul (vérifié) :** 401 tests, et **6 mutants tués sur 24** (les mutants 7, 8, 10, 17, 20, 24). Les 18 autres survivent : [1, 2, 3, 4, 5, 6, 9, 11, 12, 13, 14, 15, 16, 18, 19, 21, 22, 23].

**Question — `\u001F` :** l'écrivain mutant écrit `\u001F`, et ton parseur lit les deux casses : l'aller-retour redonne la même valeur. La propriété ne vérifie que la **cohérence** entre l'écrivain et le parseur, pas que chacun suit la **norme** : deux bugs symétriques (ou un bug que l'autre côté tolère) passent ensemble. De même, aucune donnée générée n'est **fausse** : les messages d'erreur, la profondeur, `012` ne sont jamais essayés. Conclusion : les deux sortes de tests se complètent. Les **exemples** fixent le comportement exact attendu (le format, les erreurs, les limites) ; la **propriété** explore des centaines de combinaisons auxquelles personne ne pense (un `"` dans une clé dans un tableau dans un objet).

---

## Étape 8 — Lire un objet métier, les mutants

Le code : la fin de [`JsonObject.java`](JsonObject.java) et [`JsonDemo.java`](JsonDemo.java).

**Les mutants :** avec les tests de référence, les **24 sont tués** (vérifié). Le tableau de l'indice 2 dit ce que change chacun.

**Expérience — `JsonDemo` (vérifié) :**

```
Atelier vélos : version 3
{"board":"Atelier vélos","version":3,"columns":["A faire","En cours","Fini"],"wipLimit":2,"tasks":[…,"points":1E+1,"done":false,"assignee":null}]}
[
  "A faire",
  "En cours",
  "Fini"
]
refus : ligne 5, colonne 5 : ',' ou ']' attendu
```

`1e1` est devenu `1E+1` : l'écrivain affiche le `BigDecimal` (`toString`), qui garde la forme « 1 × 10¹ » sans la recalculer. C'est le **même nombre**, et un document JSON valide. Mais un outil qui compare les textes (et non les valeurs) verrait une différence : c'est pourquoi on compare des documents en les **relisant**, jamais octet par octet.
