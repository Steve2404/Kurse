# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`NumberLab.java`](NumberLab.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Les nombres premiers

**Le code de l'étape :**

```java
static String primes(int limit) {
    String result = "";
    outer:
    for (int n = 2; n <= limit; n++) {
        for (int d = 2; d * d <= n; d++) {
            if (n % d == 0) {
                continue outer;
            }
        }
        result = result + " " + n;
    }
    return result;
}
```

**Question — pourquoi `d * d <= n` suffit-il ?** Si `n = a × b` avec `a ≤ b`, alors `a × a ≤ a × b = n`, donc `a ≤ √n`. Tout nombre composé a un diviseur **au plus égal à sa racine**. Si aucun `d ≤ √n` ne divise `n`, aucun plus grand non plus. Écrire `d * d <= n` évite `Math.sqrt` et les `double`.

**`continue outer`** quitte la boucle des diviseurs **et** passe directement au `n++` de la boucle extérieure. La ligne `result = …` est sautée. Un `continue` simple ne ferait que passer au `d` suivant.

---

## Étape 2 — Les nombres parfaits

**Le code de l'étape :**

```java
int sum = 1;
for (int d = 2; d * d <= n; d++) {
    if (n % d == 0) {
        sum += d;
        int pair = n / d;
        if (pair != d) {
            sum += pair;
        }
    }
}
if (sum == n) { … }
```

**L'efficacité :** pour 28, `d` va de 2 à 5. On trouve 2 (et 14), puis 4 (et 7) : 1 + 2 + 14 + 4 + 7 = 28. Soit **4 tours** au lieu de 27.

**Le carré parfait :** pour 16, `d = 4` donne `pair = 4`. Sans le `if (pair != d)`, 4 serait compté deux fois.

---

## Étape 3 — La conjecture de Collatz

**Le code de l'étape :**

```java
static int collatzSteps(long n) {
    int steps = 0;
    while (n != 1) {
        n = n % 2 == 0 ? n / 2 : 3 * n + 1;
        steps++;
    }
    return steps;
}
```

Dans `main`, `if (steps > bestSteps)` : le `>` strict garde le **premier** départ à égalité.

**Question — quel type ?** **`long`.** Pour les départs inférieurs à 60, la valeur la plus haute atteinte est **9232** (vérifié), qui tient dans un `int`. Mais la règle `3n + 1` fait grimper très vite d'autres départs. Vérifié : depuis **113383**, un `int` déborde et devient négatif (`-1812855948`) après 120 étapes. La boucle ne s'arrêterait alors plus correctement. `long` repousse ce problème très loin.

**Question — `while` plutôt que `do/while` ?** Pour le départ **1**, il y a **0** étape : 1 est déjà l'arrivée. `while` teste **avant** le premier tour, donc ne fait rien. Un `do/while` ferait un tour de toute façon, `1 → 4 → 2 → 1`, et compterait **3** étapes (vérifié).

---

## Étape 4 — Palindromes et nombres d'Armstrong

**Le code de l'étape :**

```java
static int reverse(int n) {
    int reversed = 0;
    do {
        reversed = reversed * 10 + n % 10;
        n /= 10;
    } while (n > 0);
    return reversed;
}

static boolean armstrong(int n) {
    int sum = 0;
    for (int rest = n; rest > 0; rest /= 10) {
        int digit = rest % 10;
        sum += digit * digit * digit;
    }
    return sum == n;
}
```

**Question — quand `do/while` vaut-il mieux qu'un `while` ?** Pour **0**. Le nombre 0 a **un** chiffre, le chiffre 0 :
- le `do/while` le traite une fois ;
- un `while (n > 0)` ne ferait **aucun** tour.

Pour `reverse(0)`, le résultat est le même (0), par chance, puisque `reversed` part de 0. Mais si on **comptait** les chiffres, le `while` donnerait 0 chiffre pour le nombre 0, ce qui est faux. Le `do/while` exprime « au moins un chiffre ».

**Vérification :** 153 = 1 + 125 + 27 ; 370 = 27 + 343 + 0 ; 371 = 27 + 343 + 1 ; 407 = 64 + 0 + 343.

---

## Étape 5 — Euclide et la recherche à double boucle

**Le code de l'étape :**

```java
int a = 1071;
int b = 462;
int rounds = 0;
while (b != 0) {
    int r = a % b;
    a = b;
    b = r;
    rounds++;
}
// PPCM : 1071 / a * 462

search:
for (int x = 2; x < target; x++) {
    for (int y = x; y < target; y++) {
        if (x * y == target) {
            pair = x + " x " + y;
            break search;
        }
        if (x * y > target) {
            break;
        }
    }
}
```

**La trace d'Euclide :**

| a | b | a % b |
|---|---|---|
| 1071 | 462 | 147 |
| 462 | 147 | 21 |
| 147 | 21 | 0 |

3 divisions, PGCD = **21**. PPCM = 1071 / 21 × 462 = 51 × 462 = **23562**.

**Question — l'ordre du PPCM :** `a × b / pgcd` calcule d'abord `a × b`, qui peut **déborder** alors que le résultat final tiendrait. Vérifié avec a = 100000, b = 300000, pgcd = 100000 :
- `a * b / g` donne **−647** (débordement) ;
- `a / g * b` donne **300000**.

Diviser d'abord est exact, puisque `pgcd` divise `a`.

**Les deux `break` :**
- `break search;` quitte les **deux** boucles d'un coup, dès que 17 × 23 est trouvé.
- `break;` (simple) ne quitte que la boucle **intérieure**, quand `x * y` dépasse 391 : inutile d'essayer des `y` plus grands, on passe au `x` suivant.

---

## Étape 6 — FizzBuzz, version `switch`

**Le code de l'étape :**

```java
for (int i = 1; i <= 15; i++) {
    String word = switch ((i % 3 == 0 ? 1 : 0) + (i % 5 == 0 ? 2 : 0)) {
        case 1 -> "Fizz";
        case 2 -> "Buzz";
        case 3 -> "FizzBuzz";
        default -> "" + i;
    };
    fizz = fizz + " " + word;
}
```

**L'idée :** chaque test vaut un **bit** (1 pour 3, 2 pour 5). Les 4 combinaisons donnent les codes 0 à 3. On passe ainsi de deux conditions imbriquées à **un seul** choix à 4 issues. Le `default` couvre le code 0, celui du nombre lui-même.
