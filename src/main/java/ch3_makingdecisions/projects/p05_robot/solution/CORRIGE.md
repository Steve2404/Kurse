# Projet 5 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Robot.java`](Robot.java).

---

## Étape 1 — Les directions et les obstacles

**Le code de l'étape :**

```java
static boolean obstacle(int cx, int cy) {
    return (cx * 3 + cy * 5) % 11 == 0 && !(cx == 0 && cy == 0);
}

static int dx(String dir) {
    return switch (dir) {
        case "E" -> 1;
        case "O" -> -1;
        default -> 0;
    };
}

static int dy(String dir) {
    return switch (dir) {
        case "N" -> 1;
        case "S" -> -1;
        default -> 0;
    };
}
```

**Pourquoi exclure (0, 0) ?** 3 × 0 + 5 × 0 = 0, et 0 % 11 = 0 : sans l'exception, la case de départ serait un obstacle.

**L'idée :** pas de tableau au chapitre 3, donc la carte n'est pas **stockée**, elle est **calculée**. N'importe quelle case se teste en une formule.

---

## Étape 2 — La boucle des commandes

**Le code de l'étape :**

```java
width = Integer.parseInt(args[0]);
height = Integer.parseInt(args[1]);
battery = Integer.parseInt(args[2]);
int order = 0;

commands:
for (int i = 3; i < args.length; i += 2) {
    String dir = args[i];
    int steps = Integer.parseInt(args[i + 1]);
    order++;
    if (dx(dir) == 0 && dy(dir) == 0) {
        System.out.println(order + ". " + dir + steps + " : direction inconnue, ignoree");
        continue;
    }
    …
}
```

**Le pas `i += 2` :** chaque commande occupe **deux** arguments. Le `continue` simple suffit ici : on est directement dans le `for`, pas dans une boucle imbriquée.

---

## Étape 3 — Les pas : trois sorties différentes

**Le code de l'étape :**

```java
int done = 0;
while (done < steps) {
    if (battery == 0) {
        System.out.println(order + ". " + dir + steps + " : batterie vide en (" + x + "," + y + ") apres " + done + " pas");
        break commands;
    }
    int nx = x + dx(dir);
    int ny = y + dy(dir);
    if (nx < 0 || ny < 0 || nx >= width || ny >= height) {
        System.out.println(order + ". " + dir + steps + " : mur atteint en (" + x + "," + y + ") apres " + done + " pas");
        continue commands;
    }
    if (obstacle(nx, ny)) {
        System.out.println(order + ". " + dir + steps + " : obstacle en (" + nx + "," + ny + "), arret en (" + x + "," + y + ") apres " + done + " pas");
        continue commands;
    }
    x = nx;
    y = ny;
    battery--;
    travelled++;
    done++;
}
System.out.println(order + ". " + dir + steps + " : arrive en (" + x + "," + y + "), batterie " + battery);
```

**La trace (grille 10 × 8, batterie 25) :**

| # | Commande | Ce qui se passe | Batterie |
|---|---|---|---|
| 1 | E3 | (1,0), (2,0), (3,0) : arrivé | 22 |
| 2 | N2 | (3,1), (3,2) : arrivé | 20 |
| 3 | X1 | direction inconnue | 20 |
| 4 | E4 | (4,2) : 12 + 10 = 22, 22 % 11 = 0 → obstacle, 0 pas | 20 |
| 5 | S6 | (3,1), (3,0), puis y = −1 → mur, 2 pas | 18 |
| 6 | O2 | (2,0), (1,0) : arrivé | 16 |
| 7 | N9 | (1,1) à (1,5), puis (1,6) : 3 + 30 = 33 → obstacle, 5 pas | 11 |
| 8 | E1 | (2,5) : arrivé | 10 |
| 9 | N7 | (2,6), (2,7), puis y = 8 → mur, 2 pas | 8 |

Total : 3 + 2 + 2 + 2 + 5 + 1 + 2 = **17 pas**. Position (2, 7), distance 2 + 7 = **9**. Avec ces arguments, la batterie ne tombe jamais à 0. Pour voir le `break commands`, relance avec une batterie de 10 : la simulation s'arrête au milieu de la commande 7.

**Question — pourquoi un `break` simple ne suffit pas pour le mur ?** Un `break` simple quitte le **`while`**. L'exécution continue alors juste après, sur la ligne `… arrive en (…)`. La commande afficherait **deux** lignes : « mur atteint », puis « arrive en ». `continue commands` quitte le `while` **et** saute la fin du tour du `for`, ce qui passe directement à la commande suivante.

**Les trois sorties en résumé :**

| Situation | Instruction | Effet |
|---|---|---|
| direction inconnue | `continue;` | commande suivante (on est dans le `for`) |
| mur, obstacle | `continue commands;` | quitte le `while`, commande suivante |
| batterie vide | `break commands;` | quitte le `while` **et** le `for` : plus aucune commande |

---

## Étape 4 — Le bilan et la carte

**Le code de l'étape :**

```java
int manhattan = x + y;
System.out.println("BILAN : " + travelled + " pas parcourus, position (" + x + "," + y + "), distance au depart " + manhattan
        + ", batterie " + battery);

static void map() {
    for (int row = height - 1; row >= 0; row--) {
        String line = row + " ";
        for (int col = 0; col < width; col++) {
            char cell;
            if (col == x && row == y) {
                cell = 'R';
            } else if (col == 0 && row == 0) {
                cell = 'S';
            } else if (obstacle(col, row)) {
                cell = '#';
            } else {
                cell = '.';
            }
            line = line + cell;
        }
        System.out.println(line);
    }
}
```

**Pourquoi `row` décroissant ?** La console écrit de haut en bas, mais le robot a l'axe y vers le **haut** : (0, 0) est en bas à gauche. La première ligne imprimée est donc la ligne `height - 1` = 7.

**`char cell;` sans valeur initiale :** c'est permis, car chaque branche de la chaîne `if` / `else if` / `else` l'affecte. Le compilateur le vérifie (*affectation définitive*). Sans le `else` final, `line + cell` ne compilerait pas : `variable cell might not have been initialized`.
