# Projet 5 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`ReportCard.java`](ReportCard.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — La moyenne et le piège du cast

**Le code de l'étape :**

```java
int coefficients = cMaths + cPhysics + cFrench;
double points = maths * cMaths + physics * cPhysics + french * cFrench;
double average = points / coefficients;
System.out.println("points " + points + " / coefficients " + coefficients + " = " + average);
int whole = (int) points;
System.out.println("piege : (int) points = " + whole + " ; " + whole + " / " + coefficients + " = " + whole / coefficients
        + " ; (double) (" + whole + " / " + coefficients + ") = " + (double) (whole / coefficients)
        + " ; (double) " + whole + " / " + coefficients + " = " + (double) whole / coefficients);
```

**Question — les types :** `maths * cMaths` est un `double` (`double * int` est promu en `double`). La somme des coefficients est un `int`. `points / coefficients` donne `double / int`, donc un `double` : 79.5 / 6 = **13.25**. Calcul : 14.5 × 3 + 9.5 × 2 + 17 × 1 = 43.5 + 19 + 17 = 79.5.

**Le piège — à quoi s'applique le cast :**

| Expression | Ce qui se passe | Résultat |
|---|---|---|
| `(int) points` | 79.5 tronqué | `79` |
| `whole / coefficients` | `int / int` : division entière | `13` |
| `(double) (whole / coefficients)` | le cast porte sur la **parenthèse**, déjà tronquée à 13 | `13.0` |
| `(double) whole / coefficients` | le cast porte sur `whole` **seul** (il est plus prioritaire que `/`), puis `double / int` | `13.166666666666666` |

**À retenir :** le cast est un opérateur **unaire**, plus prioritaire que `*` et `/`. Il ne s'applique qu'à l'opérande qui le suit.

---

## Étape 2 — Pénalité, options et bonus

**Le code de l'étape :**

```java
static final int DELEGATE = 1;
static final int SPORT = 1 << 1;
static final int LATIN = 1 << 2;

int options = Integer.parseInt(args[7], 2);
double penalty = absences > 3 ? (absences - 3) * 0.25 : 0;
average -= penalty;
average += (options & LATIN) != 0 ? 0.5 : 0;
average += (options & DELEGATE) != 0 ? 0.2 : 0;
average = average > 20 ? 20 : average;
System.out.println("absences " + absences + " -> penalite " + penalty + " ; options " + Integer.toBinaryString(options)
        + " : " + ((options & DELEGATE) != 0 ? "delegue " : "") + ((options & SPORT) != 0 ? "sport " : "")
        + ((options & LATIN) != 0 ? "latin" : ""));
```

**Le calcul :**
- 4 absences, donc (4 − 3) × 0.25 = **0.25**.
- `"101"` en base 2 = 5 : bit 0 (délégué) et bit 2 (latin) allumés, bit 1 (sport) éteint.
- Moyenne : 13.25 − 0.25 + 0.5 + 0.2 = **13.7**.

**Le ternaire `? (absences - 3) * 0.25 : 0` :** ses deux branches sont `double` et `int`. Le résultat est promu en `double`, d'où `0.0` (et non `0`) quand il n'y a pas de pénalité.

---

## Étape 3 — Arrondi, mention, écart, lettre

**Le code de l'étape :**

```java
static double tenth(double value) {
    return (int) (value * 10 + 0.5) / 10.0;
}

static String mention(double avg) {
    return avg >= 16 ? "Tres bien" : avg >= 14 ? "Bien" : avg >= 12 ? "Assez bien" : avg >= 10 ? "Passable" : "Ajourne";
}

double rounded = tenth(average);
double gap = tenth(rounded - CLASS_AVERAGE);
char letter = (char) ('A' + (int) ((20 - rounded) / 4));
… "admis : " + (rounded >= 10 && absences < 10) + ", felicitations : "
        + (rounded >= 16 || rounded >= 14 && (options & DELEGATE) != 0)
```

**Question — pourquoi `/ 10.0` et pas `/ 10` ?** `(int) (…)` est un `int` (137). `137 / 10` est une division **entière** : on obtiendrait **13**, et la décimale serait perdue. `137 / 10.0` est promu en `double` : **13.7**.

**L'écart :**
- `13.7 - 11.5` vaut `2.1999999999999993` en `double`, car 13.7 n'a pas de valeur exacte en binaire (11.5, si).
- Arrondi : 21.999… + 0.5 = 22.49…, tronqué à 22, puis 22 / 10.0 = **2.2**.
- Écart positif, donc le `+` s'affiche : `+2.2`.

**La lettre :** (20 − 13.7) / 4 = 1.575, tronqué à **1**. `'A' + 1` = 66, et le cast `(char)` donne **`B`**.

**Admis :** 13.7 ≥ 10 **et** 4 < 10, donc `true`.

**Question — `a || b && c` :** **`&&` est plus prioritaire** que `||`. Java lit `a || (b && c)`, ce qui est exactement la règle « ≥ 16, **ou** (≥ 14 **et** délégué) ». Le résultat ici est `false || (false && true)` = `false`. Des parenthèses rendraient l'intention plus claire, sans changer le résultat.

Vérifié : avec `a = true, b = false, c = false`, `a || b && c` vaut `true`, alors que `(a || b) && c` vaut `false`.
