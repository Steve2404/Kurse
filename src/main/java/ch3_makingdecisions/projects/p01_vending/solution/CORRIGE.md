# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Vending.java`](Vending.java).
>
> Les messages d'erreur ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18).

---

## Étape 1 — La boucle de commandes

**Le code de l'étape :**

```java
for (int i = 0; i < args.length; i++) {
    switch (args[i]) {
        case "PIECE" -> insert(Integer.parseInt(args[++i]));
        case "CHOIX" -> choose(args[++i]);
        case "RENDU" -> giveChange();
        case "HAPPY" -> {
            happyHour = !happyHour;
            System.out.println("HAPPY HOUR " + (happyHour ? "on" : "off"));
        }
        case "STOCK" -> System.out.println("STOCK A1=" + stockA1 + " B2=" + stockB2 + " C3=" + stockC3);
        default -> System.out.println("Commande inconnue : " + args[i]);
    }
}
```

**Question — pourquoi pas un for-each ?** Un for-each donne **un élément à la fois**, sans indice. On ne peut ni lire l'élément **suivant**, ni le **sauter**. Ici, `PIECE` doit lire `200` puis empêcher la boucle de traiter `200` comme une commande. Seul un indice qu'on avance soi-même (`++i`) permet ça.

**Pourquoi `++i` et pas `i++` ?** `args[++i]` incrémente **d'abord**, puis lit la case **suivante** : le paramètre. `args[i++]` relirait la commande elle-même (`"PIECE"`), puis avancerait.

---

## Étape 2 — Les pièces

**Le code de l'étape :**

```java
static void insert(int coin) {
    switch (coin) {
        case 10, 20, 50, 100, 200 -> {
            credit += coin;
            System.out.println("PIECE " + euros(coin) + " -> credit " + euros(credit));
        }
        default -> System.out.println("PIECE " + coin + " refusee");
    }
}
```

**Questions — les erreurs de `case` :**

| Code | Erreur de `javac` |
|---|---|
| deux fois `case 10` | `duplicate case label` |
| `case v` avec `int v = 10;` | `constant expression required` |

Une valeur de `case` doit être une **constante connue à la compilation** : un littéral, une constante `static final`, ou une variable locale `final` initialisée par une constante. Vérifié : avec `final int v = 10;`, `case v` compile.

---

## Étape 3 — Le catalogue : des `switch` expressions

**Le code de l'étape :**

```java
static int price(String code) {
    return switch (code) {
        case "A1" -> 120;
        case "B2" -> 150;
        case "C3" -> {
            int base = 180;
            yield happyHour ? base - 30 : base;
        }
        default -> -1;
    };
}
// name(code) et stock(code) : même forme, une valeur par case.

static void take(String code) {
    switch (code) {
        case "A1":
            stockA1--;
            break;
        case "B2":
            stockB2--;
            break;
        default:
            stockC3--;
    }
}
```

**Question — pourquoi le `default` est-il obligatoire ?** Un `switch` **expression** doit **toujours** rendre une valeur. Sur un `String`, les valeurs possibles sont infinies : sans `default`, certaines n'auraient pas de résultat.

```
error: the switch expression does not cover all possible input values
```

Un `switch` **instruction**, lui, peut se passer de `default` : il ne fait simplement rien.

**Question — une branche bloc qui ne rend rien :**

```
error: switch rule completes without providing a value
  (switch rules in switch expressions must either provide a value or throw)
```

Une branche `-> { … }` d'un switch expression doit se terminer par `yield valeur;` (ou lever une exception). `return` y est interdit, car il quitterait la **méthode**, pas le switch : `error: attempt to return out of a switch expression`.

**Expérience — le fall-through :** sans le `break` après `stockA1--;`, acheter un A1 exécute `stockA1--` **puis continue** dans `case "B2":` et exécute `stockB2--`. Vérifié : en partant de A1 = 2, B2 = 1, on obtient `A1=1 B2=0`. Le café disparaît sans avoir été vendu. Dans un `switch` classique, un `case` n'est qu'une **étiquette d'entrée** : l'exécution continue jusqu'au prochain `break`.

---

## Étape 4 — L'achat

**Le code de l'étape :**

```java
static void choose(String code) {
    int price = price(code);
    if (price < 0) {
        System.out.println("CHOIX " + code + " -> produit inconnu");
    } else if (stock(code) == 0) {
        System.out.println("CHOIX " + code + " -> " + name(code) + " epuise");
    } else if (credit < price) {
        System.out.println("CHOIX " + code + " -> credit insuffisant (manque " + euros(price - credit) + ")");
    } else {
        credit -= price;
        take(code);
        System.out.println("CHOIX " + code + " -> " + name(code) + " servi (" + euros(price) + "), reste " + euros(credit));
    }
}
```

**Pourquoi cet ordre ?** Une chaîne `else if` s'arrête au **premier** test vrai. Un produit inconnu n'a pas de stock : il faut donc le tester en premier. Les refus apparaissent ainsi dans l'ordre de l'énoncé.

**Trace :**
- Crédit 0.80, `B2` coûte 1.50 : il manque **0.70**.
- Pendant la happy hour, `C3` coûte 1.50, et 1.50 − 0.30 = **1.20**.

---

## Étape 5 — Le rendu de monnaie : l'algorithme glouton

**Le code de l'étape :**

```java
static void giveChange() {
    String coins = "";
    int coin = 200;
    int count = 0;
    while (credit > 0 && coin > 0) {
        if (credit >= coin) {
            credit -= coin;
            coins = coins + " " + coin;
            count++;
        } else {
            coin = switch (coin) {
                case 200 -> 100;
                case 100 -> 50;
                case 50 -> 20;
                case 20 -> 10;
                default -> 0;
            };
        }
    }
    System.out.println("RENDU -> " + (count == 0 ? "rien a rendre" : count + " piece(s) :" + coins));
}
```

**Le rendu de 3.80, à la main :**

| Crédit | Pièce essayée | Action |
|---|---|---|
| 380 | 200 | rend 200, reste 180 |
| 180 | 200 | trop grosse, passe à 100 |
| 180 | 100 | rend 100, reste 80 |
| 80 | 100 → 50 | rend 50, reste 30 |
| 30 | 50 → 20 | rend 20, reste 10 |
| 10 | 20 → 10 | rend 10, reste 0 |

Résultat : 5 pièces, `200 100 50 20 10`.

**Question — pourquoi le glouton est-il optimal ici, mais pas toujours ?**
- Avec 10, 20, 50, 100 et 200 (un système dit **canonique**, comme l'euro), prendre la plus grosse pièce n'empêche jamais une meilleure solution.
- Avec des pièces de 1, 3 et 4, il échoue. Pour rendre 6, le glouton prend 4 + 1 + 1 (3 pièces), alors que 3 + 3 (2 pièces) est meilleur. Il faut alors une recherche exhaustive ou de la programmation dynamique.
