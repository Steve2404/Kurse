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
