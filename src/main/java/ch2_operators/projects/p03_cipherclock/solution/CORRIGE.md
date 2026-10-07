# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`CipherClock.java`](CipherClock.java).
>
> Les messages d'erreur et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Le modulo toujours positif

**Le code de l'étape :**

```java
static int mod(int value, int base) {
    return (value % base + base) % base;
}
```

**Vérification à la main :**

| v | v % 26 | + 26 | % 26 |
|---|---|---|---|
| −3 | −3 | 23 | **23** |
| −29 | −3 | 23 | **23** |
| 29 | 3 | 29 | **3** |
| 0 | 0 | 26 | **0** |

**À retenir :** en Java, le signe de `a % b` est **celui de `a`** (le dividende). `-3 % 26` = −3, `7 % -3` = 1, `-7 % 3` = −1. Le second `% base` est indispensable : sans lui, 29 donnerait 29.

---

## Étape 2 — Le chiffre de César

**Le code de l'étape :**

```java
static char shift(char c, int key) {
    return c == ' ' ? c : (char) (mod(c - 'A' + key, 26) + 'A');
}

static String word(int key) {
    return "" + shift(Data.C1, key) + shift(Data.C2, key) + … + shift(Data.C8, key);
}
```

**Question — pourquoi le cast `(char)` ?** `c - 'A' + key` est un **`int`**. Dans une opération arithmétique, `char`, `byte` et `short` sont **promus en `int`**. `mod(...) + 'A'` est donc un `int`, et le ranger dans un `char` demande un cast. Sans lui :

```
error: incompatible types: possible lossy conversion from int to char
```

**Piège — `shift(C1) + shift(C2)` :** deux `char` additionnés donnent un `int`. Par exemple, `'O' + 'C'` affiche **146** (79 + 67), pas `OC`. Le `"" +` en tête fait de toute l'expression une concaténation : `"" + 'O' + 'C'` donne `OC`.

**L'aller-retour :** chiffrer de 3 puis de −29 revient à chiffrer de 3 + (−29) = −26. Or −26 ≡ 0 (mod 26) : un tour complet de l'alphabet, chaque lettre revient à elle-même.

**La ligne `'A' + 2 = 67, (char) ('A' + 2) = C, 'Z' - 'A' = 25` :** `'A'` vaut 65. Le cast transforme 67 en la lettre `C`. `'Z' - 'A'` = 90 − 65 = 25.

---

## Étape 3 — L'horloge modulaire

**Le code de l'étape :**

```java
static String twoDigits(int n) {
    return n < 10 ? "0" + n : "" + n;
}

static String clock(int hour, int minute, int delta) {
    int total = hour * 60 + minute + delta;
    int inDay = mod(total, Data.MINUTES_PER_DAY);
    int days = (total - inDay) / Data.MINUTES_PER_DAY;
    return twoDigits(inDay / 60) + ":" + twoDigits(inDay % 60)
            + (days == 0 ? "" : days > 0 ? " (+" + days + " j)" : " (" + days + " j)");
}
```

**Les calculs (départ 23:45 = 1425 min) :**
- **+50 :** 1475, `mod` donne 35, soit 00:35. Jours : (1475 − 35) / 1440 = **+1**.
- **−1500 :** −75, `mod` donne 1365, soit 22:45. Jours : (−75 − 1365) / 1440 = −1440 / 1440 = **−1**.

**Pourquoi pas `total / 1440` ?** `/` **tronque vers zéro** : `-75 / 1440` = **0**, alors qu'on a reculé d'un jour. `total - inDay` est toujours un multiple **exact** de 1440, donc la division ne tronque plus rien. On obtient la division « plancher ».

---

## Étape 4 — Les débordements

**Le code de l'étape :**

```java
byte counter = 120;
counter += 10;
System.out.println("byte 120 += 10 -> " + counter + ", (byte) 200 = " + (byte) 200 + ", (short) 40000 = " + (short) 40000);
int max = Integer.MAX_VALUE;
System.out.println("MAX_VALUE + 1 = " + (max + 1) + ", en long : " + (max + 1L));
System.out.println("(int) 9.99 = " + (int) 9.99 + ", (int) -9.99 = " + (int) -9.99 + ", (int) 3e10 = " + (int) 3e10);
System.out.println("7 / 2 = " + 7 / 2 + ", 7 / 2.0 = " + 7 / 2.0 + ", 7 % -3 = " + 7 % -3 + ", -7 % 3 = " + -7 % 3);
System.out.println("0.1 + 0.2 = " + (0.1 + 0.2) + ", 0.1f + 0.2f = " + (0.1f + 0.2f));
```

**Expérience — `counter = counter + 10;` :**

```
error: incompatible types: possible lossy conversion from int to byte
```

`counter + 10` est promu en `int`. Ranger un `int` dans un `byte` demande un cast. `counter += 10` compile, car il contient le **cast caché** `counter = (byte) (counter + 10)`. Le résultat déborde : 130 − 256 = **−126**.

**Les calculs :**
- **`(byte) 200`** = 200 − 256 = **−56**.
- **`(short) 40000`** = 40000 − 65536 = **−25536**.
- **`MAX_VALUE + 1`** = **−2147483648** : on « fait le tour » vers `MIN_VALUE`. Avec le **littéral** `1L`, l'addition se fait en `long` : 2147483648.
- **`(int) 9.99`** = 9 et **`(int) -9.99`** = −9 : la troncature va **vers zéro**.
- **`(int) 3e10`** = **2147483647** : un `double` trop grand est **saturé** à `Integer.MAX_VALUE`. Il ne fait pas le tour comme un `int` vers `byte`.
- **`7 / 2`** = 3 (deux `int`) ; **`7 / 2.0`** = 3.5 (promotion en `double`).

**Question — pourquoi `0.1f + 0.2f` s'affiche « juste » ?** Le résultat n'est pas plus exact : `0.1f` vaut en réalité `0.10000000149011612`. Mais l'affichage d'un `float` ne garde que le **nombre minimal de chiffres** qui identifie ce `float` parmi tous les `float`, et `0.3` suffit. Un `double` a beaucoup plus de précision, donc l'affichage doit montrer plus de chiffres pour le distinguer : `0.30000000000000004`.
