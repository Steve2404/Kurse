# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Receipt.java`](Receipt.java).
>
> Les messages d'erreur ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18). Sur un Windows en allemand, `java` les affiche en allemand (`Fehler: Hauptmethode ist nicht static…`). Le texte anglais s'obtient avec `java -Duser.language=en …`.

---

## Étape 1 — La classe `Receipt` et son `main`

**Le code de l'étape :**

```java
package ch1_buildingblocks.projects.p01_receipt;

public class Receipt {
    public static void main(String... args) {
    }
}
```

**Le piège :** les `...` appartiennent au **type** : `String... args`, comme `String[] args`. `String args...` ou `String vargs ...` ne compilent pas :

```
error: ',', ')', or '[' expected
```

**Question — les écritures valides de `main` :**

| Signature | Compile et se lance ? |
|---|---|
| `public static void main(String[] args)` | ✅ la forme classique |
| `public static void main(String args[])` | ✅ crochets après le nom (style C) |
| `public static void main(String... args)` | ✅ varargs |
| `public static void main(final String... args)` | ✅ `final` est permis sur un paramètre |
| `static public void main(String[] x)` | ✅ l'ordre des modificateurs est libre ; le nom du paramètre aussi |
| `public final static void main(String[] a)` | ✅ `final` est permis sur la méthode |
| `public static void main(String... args[])` | ❌ `legacy array notation not allowed on variable-arity parameter` |
| `public static int main(String[] args)` | ❌ ici `missing return statement` ; avec un `return 0;`, compile mais `java` refuse : `Error: Main method must return a value of type void` |
| `private static void main(String[] args)` | compile, mais `java` refuse : `Error: Main method not found in class M` |

**À retenir :** `public`, `static`, `void`, le nom `main` et un paramètre `String[]` ou `String...` sont obligatoires. Le reste (ordre des modificateurs, `final`, nom du paramètre, place des crochets) est libre.

**Expérience — retirer `static` :**
- `javac` : **compile sans erreur**. Une méthode d'instance nommée `main` est parfaitement légale.
- `java` : refuse de lancer, car la JVM cherche une méthode qu'elle peut appeler **sans objet** :

```
Error: Main method is not static in class Receipt, please define the main method as:
   public static void main(String[] args)
```

**Leçon :** il y a deux contrôles distincts. `javac` vérifie que le code est du Java valide ; `java` vérifie que la classe a un point d'entrée.

---

## Étape 2 — L'article : une 2e classe dans le même fichier

**Le code de l'étape** (sous la classe `Receipt`, dans le même fichier) :

```java
class Item {
    String name;
    int quantity;
    int unitCents;

    Item(String name, int quantity, int unitCents) {
        this.name = name;
        this.quantity = quantity;
        this.unitCents = unitCents;
    }

    int totalCents() {
        return quantity * unitCents;
    }

    String line() {
        return name + " x " + quantity + " a " + Receipt.euros(unitCents) + " = " + Receipt.euros(totalCents());
    }
}
```

(`Receipt.euros(...)` arrive à l'étape 4. Avant, `line()` peut afficher les centimes bruts.)

**Question — peut-elle être `public` ?** Non :

```
Receipt.java:…: error: class Item is public, should be declared in a file named Item.java
```

Un fichier `.java` contient **au plus une** classe `public`, et elle doit porter le nom du fichier. Les autres classes du fichier n'ont pas de modificateur : elles sont visibles dans le paquet seulement.

**Expérience — `name = name;` au lieu de `this.name = name;` :**
- La ligne du ticket devient `null x 3 a 12.50 = 37.50`.
- **Pourquoi `null` ?** Dans le constructeur, `name` seul désigne le **paramètre** (la variable la plus proche le « masque », c'est le *shadowing*). On affecte donc le paramètre à lui-même, et le champ garde sa **valeur par défaut** : `null` pour une référence.
- **Pourquoi `javac` ne dit rien ?** Affecter une variable à elle-même est légal. Ce n'est pas une erreur de compilation, juste une erreur de logique. IntelliJ, lui, souligne la ligne (« Variable is assigned to itself »).
- Les champs `quantity` et `unitCents` restent justes s'ils gardent leur `this.`.

---

## Étape 3 — Lire les arguments

**Le code de l'étape** (dans `main`) :

```java
Item first = new Item(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
Item second = new Item(args[3], Integer.valueOf(args[4]).intValue(), Integer.valueOf(args[5]).intValue());
final int percent = Integer.parseInt(args[6]);
boolean loyal = Boolean.parseBoolean(args[7]);
```

**À retenir :**
- `parseInt` rend un **`int`** (primitif).
- `valueOf` rend un **`Integer`** (objet). `intValue()` en extrait le primitif. Sans `intValue()`, Java ferait la conversion tout seul (*unboxing*), mais le projet te demande de l'écrire.
- `final` sur une variable locale : toute nouvelle affectation de `percent` serait une erreur de compilation.

**Expérience — seulement 3 arguments** (`Dune 3 1250`) :

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3
```

`args` a 3 cases, numérotées 0 à 2. Le premier accès hors limites est `args[3]`. Ça compile, car `javac` ne connaît pas le nombre d'arguments ; l'erreur n'arrive qu'à l'**exécution**.

**Expérience — `12.50` au lieu de `1250` :**

```
Exception in thread "main" java.lang.NumberFormatException: For input string: "12.50"
```

`parseInt` n'accepte que des chiffres entiers (avec un signe éventuel). Le point n'en fait pas partie.

**Expérience — `Boolean.parseBoolean` :**
- `"TRUE"` → `true` : la comparaison **ignore la casse**.
- `"oui"` → `false` : **tout** ce qui n'est pas « true » (en ignorant la casse) donne `false`, même `null`. Aucune exception, contrairement à `parseInt`.

---

## Étape 4 — Les montants

**Le code de l'étape** (dans `Receipt`) :

```java
static String euros(int cents) {
    return cents / 100 + "." + cents / 10 % 10 + cents % 10;
}
```

**Comment ça marche, pour `3750` :**
1. `cents / 100` = `37` : la division entière donne les euros.
2. `37 + "."` = `"37."` : dès qu'un côté est un `String`, `+` **concatène**.
3. `cents / 10 % 10` = `375 % 10` = `5` : le chiffre des dizaines de centimes. Il est collé : `"37.5"`.
4. `cents % 10` = `0` : le chiffre des unités. Il est collé : `"37.50"`.

**Calcul à la main :**

| Centimes | `/ 100` | `/ 10 % 10` | `% 10` | Résultat |
|---|---|---|---|---|
| `990` | 9 | 9 | 0 | `9.90` |
| `573` | 5 | 7 | 3 | `5.73` |
| `5157` | 51 | 5 | 7 | `51.57` |

**Le piège :** `cents / 100 + "." + cents % 100` semble plus simple, mais `cents % 100` perd le zéro de tête. Pour `5` centimes on obtient `0.5` au lieu de `0.05`, et pour `905` on obtient `9.5`.

**Question — pourquoi des centimes en `int` ?** Un `double` est stocké en binaire, et la plupart des décimaux (`0.1`, `12.50 - 0.1`…) n'y ont pas de valeur exacte. Par exemple, `0.1 + 0.2` affiche `0.30000000000000004`. Les centimes en `int` sont **exacts**. C'est la règle pour l'argent : un entier en plus petite unité, ou `BigDecimal`.

---

## Étape 5 — Le ticket : deux text blocks

**Le code de l'étape** (dans `Receipt`) :

```java
static final String HEADER = """
        +--------------------------------+
        |       LIBRAIRIE DU PORT        |
        |  12, quai des Brumes \
        - Nantes  |
        +--------------------------------+""";

static final String FOOTER = """
            Merci de votre visite !
              "Lire, c'est voyager."
        """;
```

Et dans `main` : `System.out.println(HEADER);` au début, puis `System.out.print(FOOTER);` à la fin.

**Question — quel caractère supprime le saut de ligne ?** Le **`\`** en fin de ligne. La ligne suivante est collée à celle-ci. L'espace avant le `\` est conservé, d'où `Brumes - Nantes`. L'indentation de la ligne suivante est retirée avant le collage, comme pour toutes les lignes.

**Question — comment Java calcule l'indentation « accidentelle » ?**
1. Il regarde toutes les lignes **non vides** du texte, **plus** la ligne des `"""` fermants si ceux-ci sont seuls sur leur ligne.
2. Il prend la **plus petite** indentation de ces lignes.
3. Il retire ce nombre d'espaces de **chaque** ligne.

Dans `FOOTER`, les lignes de texte commencent en colonne 12 et 14, les `"""` fermants en colonne 8. Le minimum est 8, il reste donc 4 et 6 espaces. C'est **la position des `"""` fermants** qui crée les espaces de tête.

**Le détail `print` / `println` :**
- Dans `HEADER`, les `"""` fermants sont **collés** au texte, il n'y a donc pas de saut de ligne final. On utilise `println`.
- Dans `FOOTER`, ils sont **seuls sur leur ligne**, le texte finit donc déjà par un saut de ligne. On utilise `print`. Avec `println`, on aurait une ligne vide en trop.

**Question — quand faut-il `\"` ?** Les guillemets `"` et `""` passent tels quels dans un text block. Il faut échapper quand le texte doit contenir **trois guillemets de suite** : sinon `"""` fermerait le bloc. On écrit `\"""`, ce qui affiche `"""`.

---

## Étape 6 — Le résumé

**Le code de l'étape** (dans `main`, après les deux lignes d'articles) :

```java
int subtotal = first.totalCents() + second.totalCents();
int discount = subtotal * percent / 100;

System.out.println("--------------------------------");
System.out.println("Articles   : " + (first.quantity + second.quantity));
System.out.println("Sous-total : " + euros(subtotal));
System.out.println("Remise " + percent + "% : -" + euros(discount));
System.out.println("TOTAL      : " + euros(subtotal - discount));
System.out.println("Carte fidelite : " + loyal);
```

**Calcul à la main :**
- Sous-total : 3 × 1250 + 2 × 990 = 3750 + 1980 = **5730**, affiché `57.30`.
- Remise : 5730 × 10 / 100 = 57300 / 100 = **573**, affiché `5.73`.
- Total : 5730 − 573 = **5157**, affiché `51.57`.

L'ordre `subtotal * percent / 100` compte : on multiplie **avant** de diviser. `percent / 100` d'abord donnerait `10 / 100` = `0` en division entière, donc aucune remise.

**Question — `"Articles : " + 3 + 2` ?**
- `"Articles : " + 3 + 2` affiche `Articles : 32`. Le `+` se lit de **gauche à droite** : `"Articles : " + 3` donne le `String` `"Articles : 3"`, puis `+ 2` colle `2`.
- `"Articles : " + (3 + 2)` affiche `Articles : 5`. Les parenthèses font l'addition entière **d'abord**.

C'est pourquoi la solution écrit `(first.quantity + second.quantity)` entre parenthèses.
