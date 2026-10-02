# Drill de rappel 2 — Concaténation, immutabilité et méthodes récentes de `String`

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall02`** dans le paquet `ch4_coreapis.drills.r02_string2`.
- Pas de `%f` (JVM en allemand).

## Défis

- ☐ **D01.** Une seule expression `"D01 : " + …` qui affiche, dans l'ordre :
  - `1 + 2` **sans** parenthèses ;
  - `(1 + 2)` ;
  - `('a' + 'b')` ;
  - `("" + 'a' + 'b')` ;
  - `"x" + null`.
  → `D01 : 12 3 195 ab xnull`
- ☐ **D02.** Pars de `s = "start"`, puis trois `+=` successifs : `1 + 2`, le char `'c'`, puis `true`.
  → `D02 : start3ctrue`
- ☐ **D03.** Sur `immutable = "abc"` :
  1. appelle `concat("def")` puis `toUpperCase()` **sans** récupérer le résultat ;
  2. ensuite, chaîne `concat("def").toUpperCase().substring(2)` dans une autre variable ;
  3. affiche les deux variables.
  → `D03 : abc CDEF`
- ☐ **D04.** Deux appels à `indent`, en remplaçant chaque `\n` du résultat par `|`, chacun entre crochets :
  - `"a\n  b".indent(2)` ;
  - `"    x\n  y".indent(-2)`.
  → `D04 : [  a|    b|] [  x|y|]`
- ☐ **D05.** Deux résultats entre crochets :
  - `"  x\n    y".stripIndent()`, avec `\n` remplacé par `|` ;
  - `translateEscapes()` sur le texte Java `"a\\tb\\\\c"`.
  
  Entre `a` et `b`, la sortie contient une **vraie tabulation**.
  → `D05 : [x|  y] [a	b\c]`
- ☐ **D06.** Trois formatages :
  - `"%s=%d".formatted(…)` donne `n=42` ;
  - un `String.format` qui produit `[ab  ][  ab][ab]` avec `%-4s`, `%4s` et `%.2s` sur `"ab"`, `"ab"` et `"abcdef"` ;
  - `42` sur 5 chiffres avec des zéros.
  → `D06 : n=42 [ab  ][  ab][ab] 00042`
- ☐ **D07.** Trois résultats :
  - `"Abc".charAt(0) + 'b'`, placé **après** `"D07 : "` ;
  - `('A' + "bc")` ;
  - `"abc".toUpperCase().equals("ABC")`.
  → `D07 : Ab Abc true`
- ☐ **D08.** Une variable `String text = null` concaténée après `"valeur "`, puis la longueur de `("" + null)`.
  → `D08 : valeur null 4`

## Expériences (hors sortie attendue)

1. `System.out.println('a' + 'b' + "c");` : quel affichage ? Et `"c" + 'a' + 'b'` ?
2. `text.length()` avec `text == null` : quelle exception ?
3. `"x".indent(0)` : la longueur vaut-elle 1 ou 2 ?
4. `String.format("%d", 3.5)` : que se passe-t-il ?
5. `String.format("%.2f", 3.14159)` : sur ta JVM réglée en allemand, tu obtiens `3,14` et non `3.14`. `%f` dépend de la `Locale` (chapitre 11), c'est pour cela que les `Check` ne l'utilisent jamais.

## Sortie attendue complète

```
D01 : 12 3 195 ab xnull
D02 : start3ctrue
D03 : abc CDEF
D04 : [  a|    b|] [  x|y|]
D05 : [x|  y] [a	b\c]
D06 : n=42 [ab  ][  ab][ab] 00042
D07 : Ab Abc true
D08 : valeur null 4
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**L'opérateur `+` :**
- il s'évalue de **gauche à droite** ;
- tant qu'aucun opérande n'est un `String`, c'est une addition (`char + char` donne un `int`) ;
- dès qu'un `String` apparaît, tout ce qui suit est concaténé ;
- `null` devient le texte `"null"`.

**L'immutabilité :**
- chaque méthode de `String` rend un **nouveau** `String` ;
- un appel dont on jette le résultat ne change rien.

**`indent(n)` :**
- il ajoute (ou retire, si `n < 0`) n espaces sur **chaque** ligne ;
- il normalise les fins de ligne et ajoute un `\n` final.

**`stripIndent()` :**
- il retire l'indentation commune ;
- il n'ajoute **pas** de `\n` final.

**`translateEscapes()` :**
- il transforme les séquences écrites `\t`, `\\` et `\n` en vrais caractères.

**Les formats :**
- `%s` : un texte ; `%d` : un entier ; `%f` : un décimal (6 chiffres par défaut, `%.2f` pour 2, séparateur selon la langue) ; `%n` : une fin de ligne ;
- `%-4s` aligne à gauche, `%4s` à droite ;
- `%.2s` tronque ; `%05d` complète avec des zéros ;
- un mauvais type donne une `IllegalFormatConversionException`.

</details>
