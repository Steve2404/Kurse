# Projet 2 — Le laboratoire des nombres

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 3) :**
- les boucles **`for`**, **`while`** et **`do/while`** (et quand choisir laquelle) ;
- les **boucles imbriquées** ;
- les **étiquettes** avec `continue` et `break` étiquetés ;
- le `switch` expression ;
- de vrais **algorithmes** numériques.

**Ce qui est donné :** `Check.java`. Argument : `60` (la limite).

**Ce que TU crées :** tout le programme, dans le paquet `ch3_makingdecisions.projects.p02_numberlab`. La classe du `main` s'appelle **`NumberLab`**.

**Règle du crescendo :** chapitres 1 à 3. Pas de tableau, pas de `Math`, pas de méthode de `String`.

**Tes outils pour ce projet :**
- **Argument dans IntelliJ :** Run → Edit Configurations… → **NumberLab** → Program arguments : `60`.
- **Terminal** (depuis `Kurse`) :

```
javac -d build/ch3-p02 src/main/java/ch3_makingdecisions/projects/p02_numberlab/NumberLab.java
java "-Duser.language=fr" -cp build/ch3-p02 ch3_makingdecisions.projects.p02_numberlab.NumberLab 60
```

**Conseil :** ce projet contient de vrais algorithmes. Pour chaque étape, **déroule d'abord l'algorithme à la main**, sur papier, avec un petit nombre. Puis code.

---

## Le problème

Un petit laboratoire de théorie des nombres. **Chaque ligne de la sortie est un algorithme** à concevoir toi-même. L'énoncé donne la définition, pas la méthode.

---

## Tableau de bord

### ☐ Étape 1 — Les nombres premiers

```
PREMIERS <= 60 : 2 3 5 7 11 ... 59
```

**📖 La leçon : des boucles dans des boucles.** Une boucle peut en contenir une autre. Pour **chaque** tour de la boucle extérieure, la boucle intérieure fait **tous** ses tours.

**📖 La leçon : sauter à la suite d'une autre boucle, avec une étiquette.** `continue` passe au tour suivant de **sa** boucle. `break` sort de **sa** boucle. Pour viser la boucle **extérieure**, on lui donne un nom, une **étiquette**, suivie de `:` :

```java
ligne:                                   // l'étiquette de la boucle extérieure
for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
        if (j > i) continue ligne;       // passe directement au i suivant
        System.out.print(i + "" + j + " ");
    }
}
// affiche : 11 21 22 31 32 33
```

`System.out.print` (sans `ln`) écrit **sans** aller à la ligne.

**👉 À toi :**

- Un nombre est premier s'il n'a **aucun** diviseur entre 2 et lui-même (exclu).
- **Efficacité :** il suffit de tester les diviseurs `d` tant que `d * d <= n`. Pourquoi ?
- **Contrainte :** la boucle intérieure (les diviseurs) doit, dès qu'elle trouve un diviseur, passer directement au **candidat suivant** de la boucle extérieure. Utilise un **`continue` étiqueté**, sans variable booléenne.

### ☐ Étape 2 — Les nombres parfaits

```
PARFAITS <= 10000 : 6 28 496 8128
```

**📖 Rappel :** `n % d == 0` veut dire « `d` divise `n` » (le reste est nul).

**👉 À toi :**

- Un nombre est parfait s'il est égal à la somme de ses diviseurs **propres** (lui-même exclu) : 6 = 1 + 2 + 3.
- **Efficacité :** tester jusqu'à 10 000 avec tous les diviseurs fait 50 millions de tours. En ne montant que jusqu'à `d * d <= n`, chaque diviseur `d` en apporte **deux** : `d` et `n / d`. Attention au carré parfait, qu'il ne faut pas compter deux fois.

### ☐ Étape 3 — La conjecture de Collatz

```
COLLATZ < 60 : depart 54, 112 etapes
```

**📖 Rappel du chapitre 2 :** un `int` déborde au-delà de 2 147 483 647 et « fait le tour » sans prévenir. Un `long` va beaucoup plus loin.

**👉 À toi :**

- **La règle :** depuis n, si n est pair, on passe à n / 2 ; sinon à 3n + 1. On s'arrête à 1.
- **Le calcul :** pour chaque départ de 1 à 59, compte les étapes, puis garde le départ le plus long. À égalité, garde le premier.
- **Piège :** les valeurs intermédiaires dépassent largement 60. Quel type choisir ?
- **Question :** pourquoi `while (n != 1)` plutôt que `do/while` ici ? Pense au départ 1.

### ☐ Étape 4 — Palindromes et nombres d'Armstrong

```
PALINDROMES 100..200 : 101 111 121 ... 191
ARMSTRONG 3 chiffres : 153 370 371 407
```

**📖 La leçon : `do/while`, au moins une fois.** C'est un `while` dont la condition est testée **à la fin** de chaque tour. Le corps s'exécute donc **au moins une fois** :

```java
int n = 0;
do {
    System.out.println("au moins une fois : " + n);   // s'affiche, même si n vaut 0
} while (n > 0);                                     // attention au ; final

while (n > 0) {
    System.out.println("jamais");                    // ne s'affiche pas : testé AVANT
}
```

**👉 À toi :**

- **Retourner un nombre**, sans `String` : prends le dernier chiffre avec `% 10`, enlève-le avec `/= 10`, puis reconstruis.
  - **Contrainte :** écris-le avec un **`do/while`**.
  - **Question :** pour quel nombre le `do/while` se comporte-t-il mieux qu'un `while` ?
- **Un nombre d'Armstrong à 3 chiffres** est égal à la somme des **cubes** de ses chiffres (153 = 1 + 125 + 27). Le cube s'écrit sans `Math`.

### ☐ Étape 5 — Euclide et la recherche à double boucle

```
PGCD(1071, 462) = 21 en 3 divisions, PPCM = 23562
FACTEURS de 391 : 17 x 23
```

**📖 La leçon : sortir de deux boucles d'un coup.** Un `break` étiqueté sort de la boucle nommée, **et** de toutes celles qu'elle contient :

```java
recherche:
for (int i = 1; i <= 5; i++) {
    for (int j = 1; j <= 5; j++) {
        if (i * j == 6) {
            System.out.println("trouve " + i + " x " + j);   // trouve 2 x 3
            break recherche;                                  // fin des DEUX boucles
        }
    }
}
```

**👉 À toi :**

- **Le PGCD d'Euclide :** tant que `b ≠ 0`, on remplace (a, b) par (b, a mod b). Compte les divisions.
  - Le PPCM vaut `a × b / pgcd`. Dans quel ordre faire le calcul pour éviter un débordement ?
- **Les facteurs de 391 :** cherche le premier couple x ≤ y tel que x × y = 391.
  - **Contraintes :**
    - dès qu'il est trouvé, **sors des deux boucles d'un coup** avec un `break` étiqueté ;
    - et dès que `x * y` dépasse la cible, arrête la boucle intérieure avec un `break` **simple**.

### ☐ Étape 6 — FizzBuzz, version `switch`

```
FIZZBUZZ : 1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz
```

**📖 Rappel :** le `switch` qui rend une valeur (projet 1, étape 3) peut transformer un petit code numérique en mot.

**👉 À toi :**

- **Contrainte :** pas de chaîne `if`. Calcule un **code** :
  - +1 si le nombre est divisible par 3 ;
  - +2 s'il est divisible par 5.
  
  Un `switch` expression transforme ce code en mot.

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| étiquette de boucle, `continue` étiqueté | 1 | ☐ |
| `break` étiqueté | 5 | ☐ |
| `for (`, `while (`, `do { … } while (` | toutes | ☐ |
| `% 10`, `/= 10` | 4 | ☐ |
| `switch` expression | 6 | ☐ |

---

## Sortie attendue complète

```
PREMIERS <= 60 : 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59
PARFAITS <= 10000 : 6 28 496 8128
COLLATZ < 60 : depart 54, 112 etapes
PALINDROMES 100..200 : 101 111 121 131 141 151 161 171 181 191
ARMSTRONG 3 chiffres : 153 370 371 407
PGCD(1071, 462) = 21 en 3 divisions, PPCM = 23562
FACTEURS de 391 : 17 x 23
FIZZBUZZ : 1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz
```
