# Projet 3 — Le chiffre de César, l'horloge et les débordements

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 2) :**
- l'**arithmétique sur les `char`** ;
- les **casts** (`(char)`, `(byte)`, `(short)`, `(int)`) ;
- la **promotion numérique** ;
- le **modulo avec des négatifs** ;
- la division entière tronquée et la division « plancher » ;
- les **débordements** ;
- le cast **implicite** des affectations composées ;
- la précision des `float` et des `double`.

**Ce qui est donné :** `Data.java` (le message, lettre par lettre) et `Check.java`. Arguments : `3 -29 23 45 50 -1500`.

**Ce que TU crées :** tout le programme, dans le paquet `ch2_operators.projects.p03_cipherclock`. La classe du `main` s'appelle **`CipherClock`**.

**Règle du crescendo :** chapitres 1 et 2. Pas de `if`, pas de boucle, pas de méthode de `String` (d'où le message donné lettre par lettre dans `Data`).

**Tes outils pour ce projet :**
- **Ce qui est donné :** `Data.java` contient les lettres du message. Lis-le : tu t'en sers avec `Data.NOM`.
- **Arguments dans IntelliJ :** Run → Edit Configurations… → **CipherClock** → Program arguments : `3 -29 23 45 50 -1500`.
- **Terminal** (depuis `Kurse`) : `javac` reçoit **les deux** fichiers.

```
javac -d build/ch2-p03 src/main/java/ch2_operators/projects/p03_cipherclock/CipherClock.java src/main/java/ch2_operators/projects/p03_cipherclock/Data.java
java "-Duser.language=fr" -cp build/ch2-p03 ch2_operators.projects.p03_cipherclock.CipherClock 3 -29 23 45 50 -1500
```

---

## Le problème

Trois petits outils, et un même ennemi : **le modulo des nombres négatifs**.
1. **Le chiffre de César.** On décale chaque lettre de *k* rangs dans l'alphabet, en revenant au début après `Z`. Une clé négative, ou plus grande que 26, doit marcher.
2. **L'horloge.** Ajouter ou retrancher des minutes à une heure, en passant minuit dans les deux sens, et en comptant les jours franchis.
3. **Les débordements.** Ce que deviennent les nombres quand ils ne tiennent plus dans leur type.

---

## Tableau de bord

### ☐ Étape 1 — Le modulo toujours positif

```
cle -29 = cle 23 ; -3 % 26 = -3, mod(-3, 26) = 23
```

**📖 La leçon : le reste d'une division avec des négatifs.** En Java, le résultat de `%` prend **le signe du nombre de gauche** (le dividende) :

```java
System.out.println(-5 % 4);    // -1
System.out.println(5 % -4);    // 1
```

Pour une horloge ou un alphabet, on veut un résultat **toujours** entre 0 et b − 1. À toi de trouver comment corriger le `-1` en `3` (pour 4 cases), en une expression. Indice : ajouter `b` ne change pas la « position » dans le cercle.

**👉 À toi :**

- **Constat :** en Java, `-3 % 26` vaut `-3`. Le signe du résultat suit celui du **dividende**.
- **Ta méthode `mod(v, b)`** doit rendre un résultat **toujours** entre 0 et b − 1, en **une** expression, sans `if` ni `Math`.
- Vérifie-la à la main pour −3, −29, 29 et 0.

### ☐ Étape 2 — Le chiffre de César

```
clair     : OCP JAVA
chiffre 3 : RFS MDYD
aller-retour : OCP JAVA (cle totale -26)
'A' + 2 = 67, (char) ('A' + 2) = C, 'Z' - 'A' = 25
```

**📖 La leçon : calculer avec des `char`.** Un `char` est un nombre déguisé (chapitre 1, projet 2, étape 2). Dans un calcul, Java le **transforme en `int`** :

```java
System.out.println('a' + 1);           // 98 : le code de 'a' (97), plus 1
System.out.println('a' + 'b');         // 195 : deux codes additionnés, pas "ab" !
System.out.println("" + 'a' + 'b');    // ab : un texte vide devant force la concaténation
```

**📖 La leçon : le cast, forcer une conversion.** Pour revenir au caractère, on écrit le type voulu **entre parenthèses** devant la valeur : c'est un **cast**.

```java
System.out.println((char) ('a' + 1));  // b
char c = 'a';
char d = c + 1;                        // refusé :
// error: incompatible types: possible lossy conversion from int to char
char e = (char) (c + 1);               // accepté : e vaut 'b'
```

Le cast dit à `javac` : « je sais que je risque de perdre de l'information, fais-le quand même ».

**📖 La leçon : la promotion.** Dans un calcul, les petits types `byte`, `short` et `char` deviennent **toujours** des `int`. Même deux `byte` additionnés donnent un `int` :

```java
byte a = 10;
byte b = 20;
byte s = a + b;   // error: incompatible types: possible lossy conversion from int to byte
```

**👉 À toi :**

- **Une lettre :** `c - 'A'` donne son rang (0 à 25). Décale ce rang, ramène-le dans 0..25 avec ta méthode, puis rajoute `'A'`.
  - **Question :** pourquoi faut-il un **cast** `(char)` à la fin ? Quel est le type de `c - 'A' + key` ?
- **L'espace** n'est pas chiffré : un ternaire.
- **Le mot** est la concaténation des 8 lettres de `Data`.
  - **Piège :** `shift(C1) + shift(C2)` additionne deux `char`, donc donne un **nombre**. Comment forcer une concaténation de texte ?
- **L'aller-retour :** chiffrer avec `key + back` (soit 3 + (−29) = −26) doit rendre le message clair. Pourquoi ?

### ☐ Étape 3 — L'horloge modulaire

```
depart 23:45 | +50 min -> 00:35 (+1 j) | -1500 min -> 22:45 (-1 j)
-75 / 1440 = 0 (division tronquee), plancher = -1
```

**📖 La leçon : la division entière coupe vers zéro.** `7 / 2` vaut `3`, et `-7 / 2` vaut `-3` (et non `-4`). Pour une horloge, la veille correspond pourtant à `-1` : relis l'astuce de l'étape.

**📖 La leçon : afficher sur 2 chiffres.** Sans méthode de `String`, un ternaire ajoute le zéro :

```java
int sec = 7;
System.out.println(sec < 10 ? "0" + sec : "" + sec);   // 07
```

Le `"" + sec` de la 2e branche sert à ce que les deux branches soient des **textes**.

**👉 À toi :**

- **Le calcul :** passe tout en **minutes depuis minuit**, ajoute le décalage, puis ramène dans la journée avec `mod(…, Data.MINUTES_PER_DAY)`.
- **Les jours franchis :** `/` **tronque** vers zéro (`-75 / 1440 = 0`), alors qu'il faut −1 (la veille).
  - Astuce : `(total - minutesDansLeJour) / 1440` est une division **exacte**.
- **L'affichage :**
  - les heures et les minutes sur 2 chiffres, avec un ternaire ;
  - le suffixe ` (+1 j)`, ` (-1 j)` ou rien, avec un ternaire imbriqué.

### ☐ Étape 4 — Les débordements

```
byte 120 += 10 -> -126, (byte) 200 = -56, (short) 40000 = -25536
MAX_VALUE + 1 = -2147483648, en long : 2147483648
(int) 9.99 = 9, (int) -9.99 = -9, (int) 3e10 = 2147483647
7 / 2 = 3, 7 / 2.0 = 3.5, 7 % -3 = 1, -7 % 3 = -1
0.1 + 0.2 = 0.30000000000000004, 0.1f + 0.2f = 0.3
```

**📖 La leçon : les débordements.** Chaque type a des bornes (chapitre 1, projet 2). Quand un calcul les dépasse, le nombre **fait le tour**, sans erreur :

```java
byte b = 100;
b += 100;                                   // -56 : 200 ne tient pas dans un byte (max 127), 200 - 256 = -56
System.out.println((byte) 300);             // 44 : 300 - 256
System.out.println(Integer.MAX_VALUE + 1);  // -2147483648 : le plus grand int, plus 1, fait le tour
System.out.println(Integer.MAX_VALUE + 1L); // 2147483648 : avec 1L, le calcul se fait en long
```

Remarque : `b += 100` compile, alors que `b = b + 100` serait refusé. L'étape te demande de trouver pourquoi.

**📖 La leçon : du `double` vers l'`int`.** Le cast `(int)` **coupe** la partie après la virgule, vers zéro. Si le nombre est trop grand, il s'arrête à la borne :

```java
(int) 7.9     // 7
(int) -7.9    // -7
(int) 1e20    // 2147483647 : Integer.MAX_VALUE
```

**📖 La leçon : les nombres à virgule ne sont pas exacts.** `1.1 + 2.2` affiche `3.3000000000000003`, et en `float`, `1.1f + 2.2f` affiche `3.3000002`. Le binaire ne sait pas écrire exactement la plupart des nombres à virgule.

**👉 À toi :**

**Calcule chaque valeur à la main avant d'exécuter.**
- **`counter += 10` compile, `counter = counter + 10` non.**
  - **Expérience :** écris la seconde forme, puis lis l'erreur de `javac`.
  - Quel cast caché ajoute `+=` ?
- **`(byte) 200` :** 200 − 256. Fais pareil pour `(short) 40000`.
- **`Integer.MAX_VALUE + 1` :** le résultat « fait le tour ». Comment l'éviter avec un **littéral** (pas avec un cast) ?
- **Le cast `double` vers `int` :** il tronque vers zéro, et pour un nombre trop grand, il s'arrête à la borne. Ce n'est pas le même comportement que pour `int` vers `byte`.
- **Question :** pourquoi `0.1f + 0.2f` s'affiche-t-il « juste », alors que `0.1 + 0.2` non ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| modulo positif `(v % b + b) % b` | 1 | ☐ |
| `- 'A'`, `+ 'A'`, `(char)` | 2 | ☐ |
| `Data.C1` … `Data.C8` | 2 | ☐ |
| `+=` sur un `byte`, `(byte)`, `(short)` | 4 | ☐ |
| `Integer.MAX_VALUE`, `1L`, `(int)` | 4 | ☐ |
| `2.0`, `0.1f` | 4 | ☐ |

---

## Sortie attendue complète

```
=== CESAR ===
clair     : OCP JAVA
chiffre 3 : RFS MDYD
cle -29 = cle 23 ; -3 % 26 = -3, mod(-3, 26) = 23
aller-retour : OCP JAVA (cle totale -26)
'A' + 2 = 67, (char) ('A' + 2) = C, 'Z' - 'A' = 25
=== HORLOGE ===
depart 23:45 | +50 min -> 00:35 (+1 j) | -1500 min -> 22:45 (-1 j)
-75 / 1440 = 0 (division tronquee), plancher = -1
=== DEBORDEMENTS ===
byte 120 += 10 -> -126, (byte) 200 = -56, (short) 40000 = -25536
MAX_VALUE + 1 = -2147483648, en long : 2147483648
(int) 9.99 = 9, (int) -9.99 = -9, (int) 3e10 = 2147483647
7 / 2 = 3, 7 / 2.0 = 3.5, 7 % -3 = 1, -7 % 3 = -1
0.1 + 0.2 = 0.30000000000000004, 0.1f + 0.2f = 0.3
```
