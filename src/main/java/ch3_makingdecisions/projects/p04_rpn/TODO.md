# Projet 4 — La calculatrice RPN à quatre registres

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 3) :**
- le **pattern matching** `instanceof Type variable` ;
- la **portée de flux** (`if (!(x instanceof T v)) return …;`, puis `v` utilisable après) ;
- `&&` dans un pattern ;
- le `switch` sur un `String` avec plusieurs valeurs par `case`, en instruction et en expression, avec `yield` ;
- le **for-each** ;
- `else if`.

**Ce qui est donné :** `Check.java`. Les jetons de calcul arrivent par les arguments.

**Ce que TU crées :** tout le programme, dans le paquet `ch3_makingdecisions.projects.p04_rpn`. La classe du `main` s'appelle **`Rpn`**.

**Règle du crescendo :** chapitres 1 à 3. Pas de méthode de `String` (ni `equals`, ni `contains`), pas de tableau ni de collection, et pas de pattern **dans** un `case` (`case Integer i ->` n'est qu'en *preview* en Java 17).

---

## Le problème

Les calculatrices HP calculent en **notation polonaise inverse**. On entre les nombres d'abord, l'opération ensuite : `3 4 +` donne 7.

Elles n'ont **pas de pile infinie**, mais 4 registres :
- **X**, le registre affiché ;
- **Y**, **Z** et **T**.

| Jeton | Effet |
|---|---|
| un nombre | **monte** la pile : T ← Z, Z ← Y, Y ← X, X ← nombre |
| `+` `-` `x` `/` | calcule `Y op X` dans X, puis **descend** la pile : Y ← Z, Z ← T (T est recopié) |
| `/` par zéro | message d'erreur, pile **inchangée** |
| `DUP` | recopie X (monte la pile) |
| `SWAP` | échange X et Y |
| `DROP` | jette X, puis descend la pile |
| `CHS` | change le signe de X |
| `CLR` | remet les 4 registres à 0 |

**La multiplication s'écrit `x`.** Sous Windows, le lanceur `java` remplace un argument `*` par la liste des fichiers du dossier courant !

**Les types :** les registres contiennent des **`Number`**. Un calcul entre deux `Integer` reste un `Integer` (sauf une division qui ne tombe pas juste). Dès qu'un `Double` est en jeu, le résultat est un `Double`.

---

## Tableau de bord

### ☐ Étape 1 — Les registres et la pile

- 4 champs de type `Number`, initialisés à 0.
  - **Question :** `Number x = 0;` compile. Quel est le type réel de l'objet rangé ?
- Deux méthodes : une qui « monte » la pile et une qui « descend » après une opération.

### ☐ Étape 2 — Lire un nombre : le piège du ternaire

```
10. 2.5 -> T=0 Z=0 Y=28 X=2.5 (X decimal)
```
- Lis le jeton avec `Double.parseDouble`. S'il n'a pas de partie décimale, range un `Integer`, sinon un `Double`.
- **Le piège** (dernière ligne de la sortie) :
  ```
  piege du ternaire : 1.0 est un Double
  ```
  `cond ? Integer.valueOf(1) : Double.valueOf(2.5)` ne rend **pas** un `Integer`, même quand `cond` est vrai. Pourquoi ? Pense à la promotion numérique du chapitre 2 et au **déballage**.
  - Lire un nombre avec **ce** ternaire serait donc faux. Utilise un `if` / `else`.
  - Écris ce piège tel quel à la fin du `main`.

### ☐ Étape 3 — Le calcul : le pattern matching

```
3. + -> T=0 Z=0 Y=0 X=7
22. / -> T=0 Z=0 Y=0 X=2.5 (X decimal)
```
- **Contrainte :** le cas « deux entiers » se teste en **un seul** `if`, avec deux `instanceof` à variable reliés par `&&`. Les deux variables servent directement dans le calcul.
  - **Question :** pourquoi la variable du 2e pattern serait-elle inutilisable avec `||` ?
- **Le choix de l'opération** est un `switch` expression. La branche de la division est un bloc avec `yield` : division exacte → `Integer`, sinon `Double`.
- Dans le cas général, passe en `double` avec `doubleValue()`.

### ☐ Étape 4 — Zéro et changement de signe : la portée de flux

```
14. / -> T=0 Z=0 Y=-30.5 X=0 (ERREUR division par zero, pile inchangee)
12. CHS -> T=0 Z=0 Y=0 X=-30.5 (X decimal)
```
- **« X vaut-il zéro ? »**
  - **Contrainte :** commence par `if (!(n instanceof Double d)) return …;`. Après ce `if`, `d` est **utilisable**.
  - **Question :** explique en commentaire pourquoi le compilateur le sait.
- **`CHS`** se fait avec `if` / `else if`, deux patterns, un pour chaque type.

### ☐ Étape 5 — La boucle et le `switch` des commandes

- Parcours les jetons avec un **for-each**. Un compteur numérote les étapes.
- **La commande** se choisit avec un `switch` en flèche. Un `case` à plusieurs valeurs regroupe `+`, `-` et `x`.
- **Après chaque jeton :** affiche la pile (`T=… Z=… Y=… X=…`), l'éventuelle erreur, puis `(X decimal)` si X est un `Double`.
- **Expérience :** écris `case Integer i -> …` dans un `switch` sur un `Number`. Que dit `javac` 17 ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `Number` | 1 | ☐ |
| `instanceof Integer i && …` | 3 | ☐ |
| `yield`, `(double)` | 3 | ☐ |
| `!(x instanceof T v)` (portée de flux) | 4 | ☐ |
| `else if` | 4 | ☐ |
| `for (String …)` | 5 | ☐ |
| `case "+", "-"…` (plusieurs valeurs) | 5 | ☐ |

---

## Sortie attendue complète

```
1. 3 -> T=0 Z=0 Y=0 X=3
2. 4 -> T=0 Z=0 Y=3 X=4
3. + -> T=0 Z=0 Y=0 X=7
4. 2 -> T=0 Z=0 Y=7 X=2
5. x -> T=0 Z=0 Y=0 X=14
6. DUP -> T=0 Z=0 Y=14 X=14
7. x -> T=0 Z=0 Y=0 X=196
8. 7 -> T=0 Z=0 Y=196 X=7
9. / -> T=0 Z=0 Y=0 X=28
10. 2.5 -> T=0 Z=0 Y=28 X=2.5 (X decimal)
11. + -> T=0 Z=0 Y=0 X=30.5 (X decimal)
12. CHS -> T=0 Z=0 Y=0 X=-30.5 (X decimal)
13. 0 -> T=0 Z=0 Y=-30.5 X=0
14. / -> T=0 Z=0 Y=-30.5 X=0 (ERREUR division par zero, pile inchangee)
15. DROP -> T=0 Z=0 Y=0 X=-30.5 (X decimal)
16. SWAP -> T=0 Z=0 Y=-30.5 X=0
17. 9 -> T=0 Z=-30.5 Y=0 X=9
18. - -> T=0 Z=0 Y=-30.5 X=-9
19. CLR -> T=0 Z=0 Y=0 X=0
20. 10 -> T=0 Z=0 Y=0 X=10
21. 4 -> T=0 Z=0 Y=10 X=4
22. / -> T=0 Z=0 Y=0 X=2.5 (X decimal)
piege du ternaire : 1.0 est un Double
```
