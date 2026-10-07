# Projet 2 — Les droits d'accès Unix, bit par bit

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 2) :**
- les opérateurs **bit à bit** `&`, `|`, `^`, `~` ;
- les **décalages** `<<`, `>>`, `>>>` ;
- les masques ;
- la **priorité** entre décalages, `&` et comparaisons ;
- les littéraux **octaux** (chapitre 1) au service des bits.

**Ce qui est donné :** `Check.java`. Arguments : `0754 022 0xB 3 false`.

**Ce que TU crées :** tout le programme, dans le paquet `ch2_operators.projects.p02_permissions`. La classe du `main` s'appelle **`Permissions`**.

**Règle du crescendo :** chapitres 1 et 2. Pas de `if`, pas de boucle, pas de méthode de `String`.

**Tes outils pour ce projet :**
- **Arguments dans IntelliJ :** Run → Edit Configurations… → **Permissions** → Program arguments : `0754 022 0xB 3 false`.
- **Terminal** (depuis `Kurse`) :

```
javac -d build/ch2-p02 src/main/java/ch2_operators/projects/p02_permissions/Permissions.java
java "-Duser.language=fr" -cp build/ch2-p02 ch2_operators.projects.p02_permissions.Permissions 0754 022 0xB 3 false
```

**À quoi sert ce projet ?** Sous Linux, chaque fichier a des droits : lire (`r`), écrire (`w`), exécuter (`x`), pour trois personnes (le propriétaire, le groupe, les autres). Ces 9 droits sont rangés dans **9 bits**, comme 9 interrupteurs. Tu vas allumer, éteindre et tester ces interrupteurs avec les opérateurs **bit à bit**. Relis d'abord la leçon sur le binaire et l'octal : chapitre 1, projet 2, étape 3.

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

**📖 La leçon : les opérateurs bit à bit.** Ils travaillent **chiffre binaire par chiffre binaire**, en colonne, comme une addition posée. Avec 6 (`110`) et 3 (`011`) :

| Opérateur | Règle, pour chaque colonne | `6 ? 3` en binaire | Résultat |
|---|---|---|---|
| `&` (et) | 1 seulement si les **deux** valent 1 | `110 & 011 = 010` | `2` |
| `\|` (ou) | 1 si **au moins un** vaut 1 | `110 \| 011 = 111` | `7` |
| `^` (ou exclusif) | 1 si **un seul** vaut 1 | `110 ^ 011 = 101` | `5` |
| `~` (non) | inverse chaque bit (un seul nombre) | `~5` | `-6` (étape 4) |

**📖 La leçon : les décalages.** `<<` pousse les bits vers la **gauche** : `1 << 3` donne `1000`, c'est-à-dire `8`. Chaque cran multiplie par 2. `>>` les pousse vers la **droite** : `40 >> 2` donne `10`. Chaque cran divise par 2.

**📖 La leçon : tester un bit.** Pour savoir si un interrupteur est allumé, on le « masque » avec `&` : tous les autres bits deviennent 0. Exemple : trois lampes, salon = bit de valeur 1, cuisine = 2, chambre = 4. `lampes = 0b101` : salon et chambre allumés.

```java
System.out.println((lampes & 4) != 0);   // true  : la chambre est allumée
System.out.println((lampes & 2) != 0);   // false : la cuisine est éteinte
```

**⚠️ Les parenthèses sont obligatoires.** `!=` passe **avant** `&` :

```java
System.out.println(lampes & 4 != 0);
// error: bad operand types for binary operator '&'
```

Java calcule d'abord `4 != 0` (un `boolean`), puis essaie `lampes & true` : impossible.

**Les priorités utiles, de la plus forte à la plus faible :** `* / %`, puis `+ -`, puis `<< >> >>>`, puis `< > <= >=`, puis `== !=`, puis `&`, puis `^`, puis `|`, puis `&&`, puis `||`, puis `? :`, puis `= += -=`…

**👉 À toi :**

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

**📖 La leçon : allumer, éteindre, basculer.** Avec les lampes `0b101` :

```java
lampes | 2       // allume la cuisine        : 111
lampes & ~4      // éteint la chambre        : 001   (~4 = « tout sauf la chambre »)
lampes ^ 1       // bascule le salon         : 100   (allumé -> éteint, ou l'inverse)
```

Un **masque** est un nombre qui sert à choisir des bits. On le construit par décalage, pour qu'il se lise : `1 << 2` est « le bit n° 2 » (la chambre), ce qui est plus clair que le nombre magique `4`.

**👉 À toi :**

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

**📖 Rappel :** le ternaire imbriqué (projet 1, étape 1) choisit entre plusieurs valeurs : la première condition vraie l'emporte.

**👉 À toi :**

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

**📖 La leçon : les nombres négatifs en binaire.** Un `int` a 32 bits. Le bit le plus à gauche est le **signe** : 1 pour un nombre négatif. Par exemple, `-8` s'écrit `11111111111111111111111111111000`. Deux conséquences :
- `~x` inverse les 32 bits, et le résultat vaut toujours `-x - 1` : `~5` vaut `-6` ;
- **`>>` garde le signe** (il recopie le bit de gauche) : `-8 >> 1` vaut `-4` ;
- **`>>>` insère des zéros** à gauche, le nombre devient positif : `-8 >>> 29` garde seulement les 3 bits de gauche, `111`, soit `7`.

**👉 À toi :**

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
