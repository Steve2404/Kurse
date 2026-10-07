# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Permissions.java`](Permissions.java).
>
> Les messages d'erreur ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18).

---

## Étape 1 — Lire et afficher un mode

**Le code de l'étape :**

```java
static String triplet(int bits) {
    return "" + ((bits & 4) != 0 ? 'r' : '-') + ((bits & 2) != 0 ? 'w' : '-') + ((bits & 1) != 0 ? 'x' : '-');
}

static String symbolic(int mode) {
    return triplet(mode >> 6 & 7) + triplet(mode >> 3 & 7) + triplet(mode & 7);
}

static String show(String label, int mode) {
    return label + " : " + Integer.toOctalString(mode) + " " + symbolic(mode);
}
```

Dans `main` : `int mode = Integer.decode(args[0]);`, puis `show("mode", mode) + " (decimal " + mode + ")"`.

**Question — `parseInt("0754")` ?** Il rend **754** : `parseInt` lit toujours en base 10 et ignore le zéro de tête. `Integer.decode("0754")` rend **492** = 7 × 64 + 5 × 8 + 4, car `decode` lit le `0` comme « octal ».

**Piège de priorité — `bits & 4 != 0` :**

```
error: bad operand types for binary operator '&'
  first type:  int
  second type: boolean
```

`!=` est **plus prioritaire** que `&`. Java lit donc `bits & (4 != 0)`, soit `int & boolean`, ce qui est impossible. Il faut écrire `(bits & 4) != 0`.

**Question — `mode >> 6 & 7` :** les décalages sont **plus prioritaires** que `&`. Java lit `(mode >> 6) & 7`. Ici, par hasard, `mode >> (6 & 7)` donnerait aussi 7, puisque `6 & 7 = 6`. Mais ce n'est pas la lecture de Java.

**Le décodage de `0754` :**
- `0754 >> 6` = 7 = `111`, soit `rwx`.
- `0754 >> 3 & 7` = 5 = `101`, soit `r-x`.
- `0754 & 7` = 4 = `100`, soit `r--`.

---

## Étape 2 — Les masques et les commandes `chmod`

**Le code de l'étape :**

```java
static final int OWNER_READ = 1 << 8;   // 0400
static final int OWNER_WRITE = 1 << 7;  // 0200
static final int OTHERS_EXEC = 1;       // 0001
static final int OTHERS_READ = 1 << 2;  // 0004

System.out.println(show("nouveau fichier", 0666 & ~umask));
System.out.println(show("nouveau dossier", 0777 & ~umask));
System.out.println(show("+x autres", mode | OTHERS_EXEC));
System.out.println(show("-w proprietaire", mode & ~OWNER_WRITE));
System.out.println(show("bascule lecture autres", mode ^ OTHERS_READ));
System.out.println(show("bascule deux fois", mode ^ OTHERS_READ ^ OTHERS_READ));
```

**Les calculs en binaire** (umask `022` = `000 010 010`) :
- `0666 & ~022` : `110 110 110` et pas `000 010 010`, ce qui donne `110 100 100` = **644**.
- `0754 & ~0200` : on éteint le bit `w` du propriétaire, `111` devient `101`, ce qui donne **554**.
- `0754 ^ 04` : le bit `r` des autres est allumé dans `100`, il s'éteint (`000`), ce qui donne **750**.

**Les quatre opérations :**

| Opération | Opérateur | Effet sur le bit visé |
|---|---|---|
| ajouter | `mode \| m` | toujours 1 |
| retirer | `mode & ~m` | toujours 0 |
| basculer | `mode ^ m` | inversé |
| tester | `(mode & m) != 0` | lu, sans modification |

**Question — pourquoi basculer deux fois revient-il au départ ?** `x ^ m ^ m` = `x ^ (m ^ m)` = `x ^ 0` = `x`. Un bit XOR 1 s'inverse ; inversé deux fois, il revient. Les bits où `m` vaut 0 ne bougent jamais.

---

## Étape 3 — La décision d'accès

**Le code de l'étape :**

```java
boolean inGroup = (groups & 1 << fileGroup) != 0;
int rights = owner ? mode >>> 6 & 7 : inGroup ? mode >> 3 & 7 : mode & 7;
String who = owner ? "proprietaire" : inGroup ? "groupe" : "autres";
System.out.println("proprietaire peut lire : " + ((mode & OWNER_READ) != 0) + ", ecrire : " + ((mode & OWNER_WRITE) != 0));
System.out.println("groupes " + Integer.toBinaryString(groups) + ", groupe du fichier " + fileGroup
        + " -> membre : " + inGroup);
System.out.println("acces accorde en tant que " + who + " : " + triplet(rights)
        + ", ecriture " + ((rights & 2) != 0 ? "autorisee" : "refusee"));
```

**Le calcul :**
- `0xB` = 11 = `1011`.
- `1 << 3` = `1000`, et `1011 & 1000` = `1000` ≠ 0 : l'utilisateur est **membre**.
- Il n'est pas propriétaire (`false`), donc on prend le triplet du groupe : `5` = `r-x`. Le bit `w` (2) est éteint : écriture **refusée**.

**`>>>` ou `>>` ici ?** Le mode est **positif** : le bit de signe vaut 0. Les deux décalages font donc entrer des 0 et donnent le même résultat. La différence n'apparaît qu'avec un nombre négatif (étape 4).

---

## Étape 4 — Les pièges de `~`, `>>` et `>>>`

**Le code de l'étape :**

```java
System.out.println("~mode = " + ~mode + ", ~mode & 0777 = " + Integer.toOctalString(~mode & 0777));
System.out.println("-16 >> 2 = " + (-16 >> 2) + ", -16 >>> 28 = " + (-16 >>> 28));
```

**Question — pourquoi `~492` vaut `-493` ?** `~` inverse les **32** bits de l'`int`, y compris le bit de signe, qui passe à 1 : le résultat est négatif. En complément à deux, `-x` = `~x + 1`, donc `~x` = **`-x - 1`** = −493. Pour ne garder que les 9 bits du mode, on masque : `~mode & 0777` = `000 010 011` = **23** en octal, soit les droits « inverses ».

**Question — `-16 >>> 28` à la main :**

```
-16        = 1111 1111 1111 1111 1111 1111 1111 0000
-16 >>> 28 = 0000 0000 0000 0000 0000 0000 0000 1111   = 15
-16 >> 2   = 1111 1111 1111 1111 1111 1111 1111 1100   = -4
```

- `>>>` fait entrer des **zéros** à gauche : le résultat devient positif.
- `>>` recopie le **bit de signe** : un nombre négatif reste négatif. `-16 >> 2` = −16 / 4 = **−4**.
