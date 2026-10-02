# Projet 2 — Les droits d'accès Unix, bit par bit

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 2) :**
- les opérateurs **bit à bit** `&`, `|`, `^`, `~` ;
- les **décalages** `<<`, `>>`, `>>>` ;
- les masques ;
- la **priorité** entre décalages, `&` et comparaisons ;
- les littéraux **octaux** (chapitre 1) au service des bits.

**Ce qui est donné :** `Check.java`. Arguments : `0754 022 0xB 3 false`.

**Ce que TU crées :** tout le programme, dans le paquet `ch2_operators.projects.p02_permissions`. La classe du `main` s'appelle **`Permissions`**.

**Règle du crescendo :** chapitres 1 et 2. Pas de `if`, pas de boucle, pas de méthode de `String`.

---

## Le problème

Sous Unix, les droits d'un fichier tiennent sur **9 bits**, par groupes de 3 :
- `rwx` pour le propriétaire ;
- `rwx` pour son groupe ;
- `rwx` pour les autres.

On les écrit en **octal** : `0754` = `111 101 100` = `rwxr-xr--`.

Ton programme affiche ces droits, simule les commandes `chmod`, calcule les droits d'un nouveau fichier (`umask`) et décide si un utilisateur a le droit d'écrire.

**Les arguments :**
- `0754` : le mode du fichier ;
- `022` : le umask ;
- `0xB` : les groupes de l'utilisateur, **un bit par numéro de groupe** ;
- `3` : le numéro du groupe du fichier ;
- `false` : l'utilisateur est-il le propriétaire ?

---

## Tableau de bord

### ☐ Étape 1 — Lire et afficher un mode

```
mode : 754 rwxr-xr-- (decimal 492)
```
- **Lire :** `Integer.decode` comprend le `0` initial (octal) et le `0x` (hexadécimal). Que donnerait `parseInt` sur `"0754"` ?
- **Afficher en symboles :** écris une méthode « triplet » qui transforme 3 bits en `rwx`.
  - Chaque lettre vient d'un **test de bit** `(bits & masque) != 0`, suivi d'un ternaire.
  - **Piège de priorité :** pourquoi `bits & 4 != 0` ne compile-t-il pas ? (`!=` passe **avant** `&`.)
- **Afficher les 9 bits :** extrais chaque triplet par **décalage à droite** puis `& 7`.
  - **Question :** `mode >> 6 & 7` se lit-il `(mode >> 6) & 7` ou `mode >> (6 & 7)` ?

### ☐ Étape 2 — Les masques et les commandes `chmod`

```
nouveau fichier : 644 rw-r--r--
nouveau dossier : 755 rwxr-xr-x
+x autres : 755 rwxr-xr-x
-w proprietaire : 554 r-xr-xr--
bascule lecture autres : 750 rwxr-x---
bascule deux fois : 754 rwxr-xr--
```
- **Les masques :** déclare-les comme des **constantes construites par décalage** (`1 << 8` pour la lecture du propriétaire, etc.), pas comme des nombres magiques.
- **Les opérations :**
  - **nouveau fichier** = `0666` « et pas » umask. Quel est l'opérateur « pas » bit à bit ?
  - **ajouter** un droit : `|` ;
  - **retirer** un droit : `& ~` ;
  - **basculer** un droit : `^`.
- **Question :** pourquoi basculer deux fois revient-il au départ ?

### ☐ Étape 3 — La décision d'accès

```
proprietaire peut lire : true, ecrire : true
groupes 1011, groupe du fichier 3 -> membre : true
acces accorde en tant que groupe : r-x, ecriture refusee
```
- **L'appartenance au groupe n :** le bit n de l'ensemble des groupes. Construis le masque avec `1 << n`.
- **Les droits applicables :**
  - ceux du propriétaire s'il l'est ;
  - **sinon** ceux du groupe s'il en est membre ;
  - **sinon** ceux des autres.
  
  Un ternaire imbriqué choisit le triplet. Écris-le **une fois** avec `>>>`, une fois avec `>>`.

### ☐ Étape 4 — Les pièges de `~`, `>>` et `>>>`

```
~mode = -493, ~mode & 0777 = 23
-16 >> 2 = -4, -16 >>> 28 = 15
```
- **Question :** pourquoi `~492` vaut-il `-493` ? (Un `int` a 32 bits, et `~x` vaut `-x - 1`.)
- **Question :** `>>` recopie le bit de signe, `>>>` insère des zéros. Calcule `-16 >>> 28` à la main en binaire.

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `Integer.decode`, littéral octal `0666` | 1, 2 | ☐ |
| test de bit `(x & masque) != 0` | 1, 3 | ☐ |
| `>> 6`, `& 7`, `>>>` | 1, 3 | ☐ |
| `<<` (masques) | 2, 3 | ☐ |
| `& ~`, ` | `, ` ^ ` | 2 | ☐ |
| ternaires imbriqués | 3 | ☐ |
| `Integer.toOctalString`, `Integer.toBinaryString` | 1, 3 | ☐ |

---

## Sortie attendue complète

```
mode : 754 rwxr-xr-- (decimal 492)
nouveau fichier : 644 rw-r--r--
nouveau dossier : 755 rwxr-xr-x
+x autres : 755 rwxr-xr-x
-w proprietaire : 554 r-xr-xr--
bascule lecture autres : 750 rwxr-x---
bascule deux fois : 754 rwxr-xr--
proprietaire peut lire : true, ecrire : true
groupes 1011, groupe du fichier 3 -> membre : true
acces accorde en tant que groupe : r-x, ecriture refusee
~mode = -493, ~mode & 0777 = 23
-16 >> 2 = -4, -16 >>> 28 = 15
```
