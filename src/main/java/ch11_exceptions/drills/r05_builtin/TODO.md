# Drill de rappel 5 — Les exceptions du JDK

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall05`** dans le paquet `ch11_exceptions.drills.r05_builtin`, avec la méthode `static String probe(Runnable action)`. Elle lance l'action :
  - si une `RuntimeException` est levée, elle rend `<nom simple>: <message>` ;
  - sinon, elle rend `rien`.
- Prépare : `int[] three = new int[3]`, `int zero = 0`, `Object text = "java"`, `String nothing = null` et `Object[] strings = new String[1]`.

## Défis

Chaque morceau est un `probe(() -> …)` ; sépare-les par ` | `.

- ☐ **D01.** `System.out.print(10 / zero)` ; `three[3] = 1` ; `"abc".charAt(5)`.
  → `D01 : ArithmeticException: / by zero | ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3 | StringIndexOutOfBoundsException: String index out of range: 5`
- ☐ **D02.** `Integer.parseInt("12.5")` ; `"ab".repeat(-1)` ; `new int[-1].clone()`.
  → `D02 : NumberFormatException: For input string: "12.5" | IllegalArgumentException: count is negative: -1 | NegativeArraySizeException: -1`
- ☐ **D03.** Seulement le **nom** (la partie avant `:`) pour `System.out.print((Integer) text)` et pour `nothing.length()`.
  → `D03 : ClassCastException | NullPointerException`
- ☐ **D04.** `List.of(1).add(2)` ; `Optional.empty().get()` ; `List.of().iterator().next()`.
  → `D04 : UnsupportedOperationException: null | NoSuchElementException: No value present | NoSuchElementException: null`
- ☐ **D05.** `strings[0] = 1` ; `LocalDate.of(2026, 2, 30)`.
  → `D05 : ArrayStoreException: java.lang.Integer | DateTimeException: Invalid date 'FEBRUARY 30'`
- ☐ **D06.** Une lambda en bloc qui lève `new IllegalStateException("etat invalide")` ; puis `System.out.print("")`.
  → `D06 : IllegalStateException: etat invalide | rien`

## Expériences (hors sortie attendue)

1. Dans D03, pourquoi n'affiche-t-on pas le message de `NullPointerException` ? Lis-le une fois dans la console (*helpful NullPointerException*).
2. `probe(() -> { throw new Exception(); })` : quelle erreur de compilation ? Pourquoi ?
3. Un `int` divisé par 0.0 (un `double`) : exception ou `Infinity` ?
4. `"abc".substring(2, 1)` : quelle exception ?

## Sortie attendue complète

```
D01 : ArithmeticException: / by zero | ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3 | StringIndexOutOfBoundsException: String index out of range: 5
D02 : NumberFormatException: For input string: "12.5" | IllegalArgumentException: count is negative: -1 | NegativeArraySizeException: -1
D03 : ClassCastException | NullPointerException
D04 : UnsupportedOperationException: null | NoSuchElementException: No value present | NoSuchElementException: null
D05 : ArrayStoreException: java.lang.Integer | DateTimeException: Invalid date 'FEBRUARY 30'
D06 : IllegalStateException: etat invalide | rien
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Exception | Déclenchée par |
|---|---|
| `ArithmeticException` | division **entière** par 0, débordement des `Math.…Exact` |
| `ArrayIndexOutOfBoundsException` | indice hors d'un tableau |
| `StringIndexOutOfBoundsException` | `charAt`, `substring` hors bornes |
| `NegativeArraySizeException` | `new int[-1]` |
| `ClassCastException` | cast vers un type incompatible à l'exécution |
| `NullPointerException` | appel sur `null`, `unboxing` de `null` |
| `IllegalArgumentException` | argument refusé (`NumberFormatException` en est une fille) |
| `IllegalStateException` | appel au mauvais moment |
| `UnsupportedOperationException` | collection immuable modifiée |
| `NoSuchElementException` | `Optional.get` vide, `next()` sans élément |
| `ArrayStoreException` | mauvais type rangé dans un tableau covariant |
| `DateTimeException` | date ou heure invalide (`DateTimeParseException` en est une fille) |

Toutes ces exceptions sont **non vérifiées**. Un `Runnable` ne peut lever que des non vérifiées.

</details>
