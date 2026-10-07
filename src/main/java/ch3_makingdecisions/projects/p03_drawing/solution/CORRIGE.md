# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Drawing.java`](Drawing.java).
>
> Les messages d'erreur et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Les outils

**Le code de l'étape :**

```java
static String repeat(char c, int times) {
    String s = "";
    for (int i = 0; i < times; i++) {
        s = s + c;
    }
    return s;
}

static int digits(int n) {
    int count = 1;
    while (n >= 10) {
        n /= 10;
        count++;
    }
    return count;
}

static String padLeft(int value, int width) {
    return repeat(' ', width - digits(value)) + value;
}
```

**Pourquoi `count` part de 1 ?** Tout nombre a au moins un chiffre, même 0. Avec `while (n >= 10)`, 7 donne 1 chiffre, et 10 donne 2.

---

## Étape 2 — Pyramide et losange creux

**Le code de l'étape :**

```java
for (int row = 1; row <= n; row++) {
    System.out.println(repeat(' ', n - row) + repeat('*', 2 * row - 1));
}

for (int row = -(n - 1); row <= n - 1; row++) {
    int half = n - 1 - (row < 0 ? -row : row);
    String line = repeat(' ', n - 1 - half) + "*";
    if (half > 0) {
        line = line + repeat(' ', 2 * half - 1) + "*";
    }
    System.out.println(line);
}
```

**Le tableau du losange (n = 5) :**

| row | \|row\| | half | espaces de tête | espaces du milieu |
|---|---|---|---|---|
| −4 | 4 | 0 | 4 | (pointe : une seule étoile) |
| −3 | 3 | 1 | 3 | 1 |
| 0 | 0 | 4 | 0 | 7 |
| 4 | 4 | 0 | 4 | (pointe) |

**Pourquoi un indice de −(n − 1) à n − 1 ?** Le haut et le bas sont **symétriques**. Avec la valeur absolue, une seule formule décrit les deux moitiés, sans deux boucles.

---

## Étape 3 — Damier et croix

**Le code de l'étape :**

```java
for (int c = 0; c < 2 * n; c++) {
    line = line + ((r + c) % 2 == 0 ? '#' : '.');
}

for (int c = 0; c < n; c++) {
    if (c != r && c != n - 1 - r) {
        line = line + ' ';
        continue;
    }
    line = line + (c == r && c == n - 1 - r ? '+' : c == r ? '\\' : '/');
}
```

**Le `continue`** saute la fin du tour pour cette case. Le code « sur une diagonale » n'a donc pas besoin d'un `else`, ni d'un niveau d'indentation de plus.

**Piège — le caractère `\` :** `'\'` ne compile pas :

```
error: unclosed character literal
```

`\'` est la séquence d'échappement d'une **apostrophe** : le littéral n'est donc jamais fermé. Il faut **doubler** la barre : `'\\'`.

**Le croisement :** `+` n'apparaît que si `c == r` **et** `c == n - 1 - r`, c'est-à-dire `r = (n − 1) / 2`. Cela n'arrive que pour un `n` impair (ici 5, ligne 2).

---

## Étape 4 — La table de multiplication alignée

**Le code de l'étape :**

```java
String header = "   |";
for (int c = 1; c <= n; c++) {
    header = header + padLeft(c, 4);
}
System.out.println(header);
System.out.println("---+" + repeat('-', 4 * n));
for (int r = 1; r <= n; r++) {
    String line = padLeft(r, 2) + " |";
    for (int c = 1; c <= n; c++) {
        line = line + padLeft(r * c, 4);
    }
    System.out.println(line);
}
```

**Le calcul des largeurs :** `" 1 |"` fait 4 caractères (2 + 2), comme `"   |"` : les colonnes s'alignent. Chaque cellule fait 4 caractères, donc la ligne de tirets en compte 4 × 5 = 20.

---

## Étape 5 — Le triangle de Pascal

**Le code de l'étape :**

```java
static void pascal(int n) {
    for (int r = 0; r < n; r++) {
        String line = repeat(' ', 2 * (n - 1 - r));
        long value = 1;
        for (int k = 0; k <= r; k++) {
            line = line + padLeft((int) value, 4);
            value = value * (r - k) / (k + 1);
        }
        System.out.println(line);
    }
}
// appelée avec pascal(n + 1) : 6 lignes pour n = 5
```

**Vérification sur la ligne 4 :**

| k | C(4, k) | calcul du suivant |
|---|---|---|
| 0 | 1 | 1 × 4 / 1 = 4 |
| 1 | 4 | 4 × 3 / 2 = 6 |
| 2 | 6 | 6 × 2 / 3 = 4 |
| 3 | 4 | 4 × 1 / 4 = 1 |
| 4 | 1 | — |

**Le centrage :** chaque nombre occupe 4 caractères. Décaler une ligne de 2 espaces de plus que la suivante place chaque nombre **entre** les deux nombres du dessous.

**Question — pourquoi multiplier avant de diviser ?** `C(r, k) × (r − k)` est **toujours** divisible par `(k + 1)` : le résultat est un coefficient binomial, donc un entier. Diviser d'abord **tronque** quand `C(r, k)` n'est pas divisible par `(k + 1)`. Vérifié sur la ligne 5 :
- multiplier puis diviser donne `1 5 10 10 5 1` ;
- diviser puis multiplier donne `1 5 8 6 2 0`. Dès k = 1, 5 / 2 = 2 au lieu de 2.5.

Sur la ligne 4, l'ordre inverse donne par chance le bon résultat. C'est pour ça qu'il faut tester plusieurs lignes.
