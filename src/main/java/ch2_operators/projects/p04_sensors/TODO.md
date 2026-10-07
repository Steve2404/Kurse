# Projet 4 — La validation de trames de capteurs

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 2) :**
- le **court-circuit** de `&&` et `||`, face à `&` et `|` sur des booléens ;
- `^` ;
- les **effets de bord** dans une condition ;
- les incréments **préfixés et suffixés** dans une même expression ;
- l'**affectation comme expression** (`x = y = 4`, `(x += 2) * x`, `(flag = true)`) ;
- `==` et `!=` sur des **références** ;
- `instanceof`.

**Ce qui est donné :** `Check.java`. Arguments : `22 55 80 true` (température, humidité, batterie, signature).

**Ce que TU crées :** tout le programme, dans le paquet `ch2_operators.projects.p04_sensors`. La classe du `main` s'appelle **`Sensors`**. Une trame est une **autre** classe.

**Règle du crescendo :** chapitres 1 et 2. Pas de `if`, pas de boucle. Avec `instanceof`, pas de variable (`o instanceof String s` est du chapitre 3).

**Tes outils pour ce projet :**
- **Arguments dans IntelliJ :** Run → Edit Configurations… → **Sensors** → Program arguments : `22 55 80 true`.
- **Terminal** (depuis `Kurse`) :

```
javac -d build/ch2-p04 src/main/java/ch2_operators/projects/p04_sensors/Sensors.java
java "-Duser.language=fr" -cp build/ch2-p04 ch2_operators.projects.p04_sensors.Sensors 22 55 80 true
```

---

## Le problème

Une station reçoit des trames de capteurs. Une trame est valide si **quatre contrôles** passent, dans cet ordre :
1. la température est entre −20 et 50 ;
2. l'humidité est entre 0 et 100 ;
3. la batterie est au-dessus de 10 % ;
4. la trame est signée.

Chaque contrôle **coûte cher** : l'équipe veut savoir **combien de contrôles ont réellement été exécutés** selon l'opérateur utilisé.

---

## Tableau de bord

### ☐ Étape 1 — La trame et les contrôles qui se comptent

**📖 La leçon : `&&` (et) et `||` (ou) sur des `boolean`.**
- `a && b` est vrai si `a` **et** `b` sont vrais. « Entre 0 et 10 » s'écrit `x >= 0 && x <= 10` : on ne peut **pas** écrire `0 <= x <= 10` en Java.
- `a || b` est vrai si **au moins un** des deux est vrai.

Rappel : un compteur `static`, et une méthode qui fait un travail **puis** rend une valeur, ont été vus au chapitre 1 (projet 3, étape 1).

**👉 À toi :**

- **Une trame :** température, humidité, batterie et signature. Elle est construite avec un constructeur.
- **Quatre méthodes de contrôle :** chacune **incrémente un compteur `static`**, puis rend son verdict.
  - Un verdict « entre −20 et 50 » s'écrit avec `&&`.

### ☐ Étape 2 — `&&` contre `&`

```
trame #2 (70 C, 40 %, batterie 90 %, signee) : rejetee | && 1 controle(s), & 4 | ...
trame #3 (25 C, 120 %, batterie 5 %, non signee) : rejetee | && 2 controle(s), & 4 | ...
```

**📖 La leçon : le court-circuit.** `&&` est paresseux : si la partie de gauche est **fausse**, le résultat sera faux de toute façon, donc Java **n'évalue pas** la partie de droite. `&` sur des `boolean` évalue **toujours** les deux côtés. Pour le voir, prenons deux méthodes qui disent quand elles sont appelées :

```java
static boolean faux(String nom) { System.out.println("verifie " + nom); return false; }
static boolean vrai(String nom) { System.out.println("verifie " + nom); return true; }

System.out.println(faux("A") && vrai("B"));   // affiche "verifie A", puis false  : B n'est jamais appelé
System.out.println(faux("C") & vrai("D"));    // affiche "verifie C", "verifie D", puis false
System.out.println(vrai("E") || vrai("F"));   // affiche "verifie E", puis true   : || s'arrête au 1er vrai
```

Un **effet de bord**, c'est ce qu'une méthode fait **en plus** de rendre sa valeur : afficher, augmenter un compteur… Avec `&&`, certains effets de bord n'ont donc **pas lieu**.

**👉 À toi :**

- **La validation :** valide la trame **deux fois**, en remettant le compteur à 0 avant chacune :
  - une fois en chaînant les 4 contrôles avec `&&` ;
  - une fois avec `&`.
- **À la main d'abord :** pour chacune des 5 trames de la sortie attendue, prédis les deux nombres de contrôles.
- **Questions :**
  - le **verdict** peut-il différer entre `&&` et `&` ? Et les **effets de bord** ?
  - quand préférer `&` en pratique ?

### ☐ Étape 3 — `^` et `||`

```
... | un seul defaut climat true | alerte oui
```

**📖 La leçon : `^` sur des `boolean`, le « ou exclusif ».** `a ^ b` est vrai si **exactement un** des deux est vrai :

```java
true ^ true     // false
true ^ false    // true
```

C'est le « fromage **ou** dessert » du menu : l'un ou l'autre, pas les deux.

**👉 À toi :**

- **« Un seul défaut climat » :** exactement **un** des deux contrôles climatiques (température, humidité) échoue. Ce n'est pas « au moins un ». Quel opérateur exprime « l'un ou l'autre, mais pas les deux » ?
- **L'alerte :** température au-dessus de 45 **ou** batterie sous 20 %.
- **Le numéro de trame :** un compteur `static`, incrémenté dans l'expression du texte.

### ☐ Étape 4 — Incréments, affectations, références

```
increments : a = 12, b = 2, id = 5
affectations : x = 6, y = 4, z = 36, affectation (vraie)
references : f1 == f2 false, f1 == f3 true, f1 != f2 true
instanceof : Integer true, Number true, texte Integer false, null Object false
```

**📖 La leçon : plusieurs `++` dans un même calcul.** Java lit le calcul **de gauche à droite**, et chaque `++` agit **au moment où il est lu** :

```java
int n = 3;
int r = n++ * 10 + n;    // n++ rend 3 (puis n vaut 4) ; 3 * 10 + 4 = 34
```

**📖 La leçon : une affectation rend une valeur.** `b = 7` range 7 dans `b`, **et** vaut 7. On peut donc enchaîner, de droite à gauche :

```java
int a, b;
a = b = 7;                           // b reçoit 7, puis a reçoit 7
int k = 2;
System.out.println((k += 3) * k);    // (k += 3) vaut 5 et k devient 5 : 5 * 5 = 25
```

**📖 La leçon : `==` sur des objets compare les étiquettes.** Pour deux objets, `==` ne compare **pas** leur contenu. Il demande : « ces deux étiquettes sont-elles attachées au **même** objet ? » (chapitre 1, projet 3, étape 5) :

```java
Point p1 = new Point(1, 2);
Point p2 = new Point(1, 2);     // un AUTRE objet, avec le même contenu
Point p3 = p1;                  // la même étiquette que p1
p1 == p2                        // false
p1 == p3                        // true
```

**📖 La leçon : `instanceof`, « est-ce un… ? ».** `objet instanceof Type` répond `true` si l'objet est de ce type, ou d'un type plus précis :

```java
Object o = 42;             // une boîte Integer, rangée dans une variable de type Object
o instanceof Integer       // true
o instanceof Number        // true : un Integer est une sorte de Number
o instanceof String        // false
```

**👉 À toi :**

- **Les incréments.** Avec `id = 5`, écris exactement `a = id++ + ++id;` puis `b = id-- - --id;`.
  - **Calcule à la main** la valeur rendue **et** l'effet de chaque terme, de gauche à droite.
- **Les affectations.**
  - `x = y = 4;` : dans quel ordre ? Que vaut l'expression `y = 4` ?
  - `z = (x += 2) * x;` : quelle valeur de `x` est utilisée à droite ?
  - `(flag = true) ? … : …` compile alors que `flag` valait `false`. Pourquoi ? C'est le piège classique `=` au lieu de `==`.
- **Les références.** Deux trames **identiques** créées avec `new` sont-elles `==` ? Et deux variables qui désignent **le même** objet ?
- **`instanceof` sur des `Object` :**
  - un `Integer` est-il un `Number` ?
  - `null instanceof Object` vaut-il `true` ou `false` ?
  - **Expérience :** `"texte" instanceof Integer` écrit **directement** (pas via une variable `Object`). Que dit `javac`, et pourquoi ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `&&` (au moins 3), `&` entre deux appels | 2 | ☐ |
| ` ^ `, `||` | 3 | ☐ |
| `++`, `--` | 1, 4 | ☐ |
| `x = y = 4`, `(x += 2)`, `(flag = true)` | 4 | ☐ |
| `==`, `!=` | 4 | ☐ |
| `instanceof` | 4 | ☐ |

---

## Sortie attendue complète

```
trame #1 (22 C, 55 %, batterie 80 %, signee) : valide | && 4 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte non
trame #2 (70 C, 40 %, batterie 90 %, signee) : rejetee | && 1 controle(s), & 4 | meme verdict true | un seul defaut climat true | alerte oui
trame #3 (25 C, 120 %, batterie 5 %, non signee) : rejetee | && 2 controle(s), & 4 | meme verdict true | un seul defaut climat true | alerte oui
trame #4 (-30 C, 150 %, batterie 50 %, signee) : rejetee | && 1 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte non
trame #5 (10 C, 10 %, batterie 11 %, non signee) : rejetee | && 4 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte oui
increments : a = 12, b = 2, id = 5
affectations : x = 6, y = 4, z = 36, affectation (vraie)
references : f1 == f2 false, f1 == f3 true, f1 != f2 true
instanceof : Integer true, Number true, texte Integer false, null Object false
trames analysees : 5
```
