# Projet 1 — Le ticket de caisse en ligne de commande

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md), et fais le **projet 0** [`p00_bonjour`](../p00_bonjour/TODO.md) : il t'apprend les gestes (créer une classe, lancer, `Check`, arguments, terminal) que ce projet suppose connus.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 1) :**
- la méthode `main` et ses **arguments** ;
- deux classes dans **un** fichier ;
- les champs, un constructeur et `this` ;
- les classes enveloppes pour convertir du texte en nombre (`parseInt`, `valueOf`, `intValue`, `parseBoolean`) ;
- les **text blocks** et leurs espaces ;
- `final`.

**Ce qui est donné :** `Check.java` seulement. Les données arrivent par les **arguments de la ligne de commande**.

**Ce que TU crées :** tout le programme, dans le paquet `ch1_buildingblocks.projects.p01_receipt`. La classe du `main` s'appelle **`Receipt`**.

**Règle du crescendo :** tu n'as droit qu'au chapitre 1, plus les opérateurs `+ - * / %`. Donc pas de `if`, pas de boucle, pas de méthode de `String` (`length`, `substring`…), pas de `StringBuilder` ni de `String.format`. `Check` les refuse. Tout se fait avec des variables, des objets, des appels de méthodes et des calculs.

**Comment lire chaque étape :**
- **📖 La leçon** t'apprend la notion, avec un exemple sur **un autre sujet** que le ticket. Recopie l'exemple dans un fichier d'essai si tu veux le voir tourner (voir l'encadré ci-dessous).
- **👉 À toi** dit ce que tu dois construire pour le ticket.
- **🧪 Expériences** et **❓ Questions** : tu essaies, tu observes, tu écris ta réponse **en commentaire** dans ton code (comme au projet 0, étape 8).

---

## Tes outils pour ce projet

**Lancer `Receipt` avec ses 8 arguments, dans IntelliJ :**
1. Lance `Receipt` une première fois avec la flèche verte ▶. Il plantera faute d'arguments (`ArrayIndexOutOfBoundsException`) dès que ton `main` lira `args`. C'est normal : ce premier lancement crée la **configuration** `Receipt`.
2. Menu **Run** → **Edit Configurations…** → à gauche, **Receipt** → champ **Program arguments** : tape `Dune 3 1250 Fondation 2 990 10 true` → **OK**.
3. Relance. Pour une expérience avec d'autres arguments, change ce champ, puis **remets-le** comme avant.

**Lancer `Receipt` dans le terminal** (Alt + F12, depuis le dossier `Kurse`) :

```
javac -d build/p01 src/main/java/ch1_buildingblocks/projects/p01_receipt/Receipt.java
java "-Duser.language=fr" -cp build/p01 ch1_buildingblocks.projects.p01_receipt.Receipt Dune 3 1250 Fondation 2 990 10 true
```

La 1re commande traduit, la 2e exécute. Tu les as apprises au projet 0, étape 6. La flèche du haut ↑ les rappelle.

**Essayer un exemple de leçon sans abîmer ton projet :**
1. Clic droit sur le dossier `p01_receipt` → **New** → **Java Class** → tape `Atelier` (les exemples s'appellent ainsi).
2. **Garde** la 1re ligne `package …;` écrite par IntelliJ, et remplace **tout le reste** par l'exemple.
3. Lance-la avec la flèche verte, et observe.
4. **Supprime-la** ensuite : clic droit sur `Atelier.java` → **Delete**. C'est obligatoire avant de lancer `Check`, qui lit **tous** les fichiers `.java` du dossier : une classe en trop fausserait sa vérification.

**Vérifier :** lance `Check.java` après chaque étape. Tant que les étapes suivantes ne sont pas faites, il dira `[FAIL]` : regarde surtout si la **première ligne fausse** a avancé.

---

## Le problème

Une librairie veut un petit programme de caisse **lancé en ligne de commande**. Le vendeur tape les articles, une remise et la carte de fidélité :

```
java ch1_buildingblocks.projects.p01_receipt.Receipt Dune 3 1250 Fondation 2 990 10 true
```

Les 8 arguments, dans l'ordre :
1. le nom du 1er article ;
2. sa quantité ;
3. son prix unitaire **en centimes** ;
4. le nom du 2e article ;
5. sa quantité ;
6. son prix unitaire en centimes ;
7. la remise en % ;
8. la carte de fidélité (`true` ou `false`).

Le programme imprime le ticket. `Check` lance ton `main` avec exactement ces arguments.

---

## Tableau de bord

### ☐ Étape 1 — La classe `Receipt` et son `main`

**📖 La leçon : deux façons d'écrire la liste des arguments.** Au projet 0, tu as écrit `main(String[] args)`. Les crochets `[]` veulent dire « **une liste** de `String` ». Java accepte une 2e écriture, avec **trois points** :

```java
public static void main(String[] args)    // la liste, écrite avec des crochets
public static void main(String... args)   // la même liste, écrite avec trois points
```

Les deux veulent dire **exactement** la même chose pour `main`. Les trois points s'appellent **varargs** (« nombre variable d'arguments »). Ils se collent au **type** `String`, jamais au nom `args`.

**👉 À toi :**
- Crée `Receipt.java` dans le dossier `p01_receipt`, avec la méthode `main` (comme au projet 0, étapes 2 et 3).
- **Contrainte :** écris `main` avec la forme **varargs** du paramètre, et non `String[]`.

**❓ Question :** quelles autres écritures de `main` sont valides ? Pense à `final`, à la place des crochets et au nom du paramètre.

**🧪 Comment chercher la réponse.** Une écriture est « valide » si **deux** contrôles passent : `javac` la traduit, **et** `java` accepte de la lancer. Pour chaque variante ci-dessous :
1. remplace ta ligne `main` par la variante ;
2. tape la commande `javac` de l'encadré « Tes outils » : un message d'erreur ? Note-le ;
3. si `javac` n'a rien dit, tape la commande `java` : un message d'erreur ? Note-le ;
4. remets ta forme varargs.

Recopie ce tableau en commentaire et remplis-le :

```
// variante                                          javac ?   java ?
// public static void main(String args[])
// public static void main(final String... args)
// static public void main(String[] x)
// public final static void main(String[] a)
// public static void main(String args...)
// private static void main(String[] args)
```

**🧪 Expérience :** retire `static` de `main`. Ça compile ? Que dit `java` au lancement ? Remets-le. (Tu l'as déjà fait au projet 0, étape 7 : retrouves-tu le même message ?)

### ☐ Étape 2 — L'article : une 2e classe dans le même fichier

**📖 La leçon : une classe, c'est un moule ; un objet, c'est ce qu'on fabrique avec.** Pense à un moule à gâteau. Le moule décrit la forme, et on s'en sert pour fabriquer **plusieurs** gâteaux. En Java :
- la **classe** est le moule : elle décrit ce que chaque objet **retient** et ce qu'il **sait faire** ;
- un **objet** est un gâteau : on le fabrique avec le mot **`new`**.

Voici une classe `Rectangle` (un autre sujet que le ticket), puis son utilisation dans un `main` :

```java
public class Atelier {

    public static void main(String[] args) {
        Rectangle tapis = new Rectangle("tapis", 3, 2);   // fabrique un 1er objet
        Rectangle porte = new Rectangle("porte", 1, 2);   // fabrique un 2e objet, avec le même moule
        System.out.println(tapis.description());          // demande au tapis sa description
        System.out.println("Aire : " + tapis.aire());     // demande au tapis son aire
        System.out.println(porte.description());
        System.out.println(tapis.nom);                    // lit directement un champ du tapis
    }
}

class Rectangle {
    // Les CHAMPS : ce que chaque rectangle retient. Chaque objet a les siens.
    String nom;
    int largeur;
    int hauteur;

    // Le CONSTRUCTEUR : il remplit les champs à la naissance de l'objet.
    // Il porte le nom de la classe, et n'a pas de type de retour (pas même void).
    Rectangle(String nom, int largeur, int hauteur) {
        this.nom = nom;            // this.nom = le champ de CET objet ; nom = le paramètre reçu
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    // Une MÉTHODE qui rend un int : le type avant le nom, et "return" donne le résultat.
    int aire() {
        return largeur * hauteur;
    }

    // Une MÉTHODE qui rend un String.
    String description() {
        return nom + " de " + largeur + " sur " + hauteur;
    }
}
```

Ce programme affiche :

```
tapis de 3 sur 2
Aire : 6
porte de 1 sur 2
tapis
```

**Les mots à retenir :**
- `int` : le type des **nombres entiers** (`3`, `1250`…). `String` : le type des **textes**.
- **Un champ** est une variable qui appartient à l'objet. Chaque rectangle a **son** `largeur`.
- **`this`** veut dire « **cet** objet-ci ». Dans le constructeur, le paramètre `nom` et le champ `nom` ont le même nom. `nom` tout seul désigne le **paramètre** ; pour parler du champ, on écrit `this.nom`.
- **Appeler une méthode :** `objet.methode()`. Les parenthèses sont **obligatoires**, même vides.
- **Deux classes dans un fichier :** c'est permis, mais **une seule** peut être `public` : celle qui porte le nom du fichier. L'autre s'écrit `class Rectangle`, sans `public`, **après** l'accolade fermante de la 1re classe.

**👉 À toi :**
- Un **article** a un nom, une quantité et un prix unitaire en centimes. Il sait calculer son total et produire sa ligne de ticket. C'est toi qui choisis le nom de la classe, de ses champs et de ses méthodes.
- **Contrainte :** écris cette classe **dans `Receipt.java`**, sous la classe `Receipt`.
- **Le constructeur** reçoit les trois valeurs. Ses paramètres portent **les mêmes noms** que les champs.
- Pour l'essayer tout de suite, fabrique un article **en dur** dans `main` (par exemple avec `"Dune"`, `3` et `1250`), et affiche sa ligne. Les vrais arguments arrivent à l'étape 3, et les euros à l'étape 4 : pour l'instant, la ligne peut afficher les centimes bruts.

**❓ Question :** ta classe d'article peut-elle être `public` ? Essaie, compile (commande `javac`), lis l'erreur, puis corrige.

**🧪 Expérience :** dans le constructeur, écris `name = name;` au lieu de `this.name = name;` (avec **tes** noms de champ). Lance : qu'affiche la ligne de l'article ? Pourquoi `javac` ne dit-il rien ? Remets `this.`.

### ☐ Étape 3 — Lire les arguments

**📖 La leçon : un texte n'est pas un nombre.** Les arguments arrivent **toujours** sous forme de texte (`String`). Le texte `"12"` n'est **pas** le nombre `12` :

```java
System.out.println("12" + 1);   // affiche 121 : le texte "12", collé au 1
System.out.println(12 + 1);     // affiche 13  : une vraie addition
```

Pour calculer, il faut **convertir** le texte en nombre. Java range ces outils dans des **classes enveloppes**. Chaque type simple a la sienne : `Integer` pour `int`, `Boolean` pour `boolean`… Pense à une **boîte** autour du nombre :

```java
int n = Integer.parseInt("12");          // parseInt rend directement le NOMBRE : n vaut 12
Integer boite = Integer.valueOf("12");   // valueOf rend une BOÎTE (un objet Integer) qui contient 12
int dedans = boite.intValue();           // intValue() ouvre la boîte et sort le nombre
boolean oui = Boolean.parseBoolean("true");   // le texte "true" devient la valeur true
```

`boolean` est le type des valeurs **vrai/faux** : `true` ou `false`.

**📖 La leçon : `final`, une variable qu'on ne change plus.** Une variable se déclare avec son type, puis reçoit une valeur avec `=`. Avec `final` devant, elle ne peut plus **jamais** changer :

```java
final int annee = 2026;
annee = 2027;   // refusé par javac : cannot assign a value to final variable annee
```

**👉 À toi :**
- Pour le 1er article, `Integer.parseInt(...)` (rend un `int`).
- Pour le 2e article, `Integer.valueOf(...)` (rend un **objet** `Integer`), puis `intValue()` pour obtenir le primitif. Le résultat est le même, mais la nature du retour diffère.
- Pour la carte de fidélité, `Boolean.parseBoolean(...)`.
- La remise est une variable locale `final` : elle ne doit plus changer une fois lue.
- Remplace ton article fabriqué en dur par les deux articles lus dans `args[0]` à `args[5]`.

**🧪 Expériences.** Pour chacune, note **le nom de l'exception** (le mot qui finit par `Exception`) et la phrase qui suit :
1. **Seulement 3 arguments.** Dans **Program arguments**, mets `Dune 3 1250`, puis relance `Receipt`. Ou bien, dans le terminal, la commande `java` avec seulement ces 3 mots. Quelle exception, et pourquoi ?
2. **Un prix à virgule.** Remets les 8 arguments, mais écris `12.50` au lieu de `1250`. Quelle exception lève `parseInt` ?
3. **`parseBoolean` sur d'autres mots.** Ajoute **temporairement**, au tout début de `main`, ces deux lignes :
   ```java
   System.out.println(Boolean.parseBoolean("TRUE"));
   System.out.println(Boolean.parseBoolean("oui"));
   ```
   Lance `Receipt`. Que rend chacune ? Puis **efface** ces deux lignes : `Check` exige la sortie exacte.

Remets ensuite les 8 arguments normaux.

### ☐ Étape 4 — Les montants

```
Dune x 3 a 12.50 = 37.50
Fondation x 2 a 9.90 = 19.80
```

**📖 La leçon : `/` et `%` sur des entiers.** Entre deux `int`, la division `/` est une division **entière**. Elle garde le quotient et jette le reste. `%` (« modulo ») donne justement **le reste** :

```java
System.out.println(7 / 2);   // 3 : dans 7, il y a 3 fois 2…
System.out.println(7 % 2);   // 1 : …et il reste 1
```

**Un exemple qui ressemble à ton problème : des minutes en heures.**

```java
int minutes = 135;
System.out.println(minutes / 60 + "h" + minutes % 60);   // affiche 2h15
```

`135 / 60` = 2 heures, et `135 % 60` = 15 minutes restantes. Mais attention, avec `minutes = 125`, on obtient **`2h5`**, et non `2h05` : le **zéro de tête** a disparu, car `125 % 60` vaut le nombre `5`. Ton ticket a exactement ce piège : `9.90` ne doit pas devenir `9.9`, ni `0.05` devenir `0.5`.

**📖 La leçon : une méthode à toi, avec un paramètre.** Une méthode peut **recevoir** une valeur (son **paramètre**, entre parenthèses) et **rendre** un résultat avec `return`. Si elle est `static` et écrite dans `Receipt`, `main` l'appelle par son nom ; une **autre** classe l'appelle avec `Receipt.` devant :

```java
public class Atelier {
    static String encadre(String mot) {    // reçoit un String, rend un String
        return "[" + mot + "]";
    }
    public static void main(String[] args) {
        System.out.println(encadre("chat"));         // affiche [chat]
    }
}
class Niche {
    String etiquette() {
        return "Niche " + Atelier.encadre("Rex");    // depuis une autre classe : rend "Niche [Rex]"
    }
}
```

**👉 À toi :**
- **Tous les calculs se font en centimes** (`int`).
- Pour afficher `3750` sous la forme `37.50`, **sans `if` ni `String.format`**, écris une méthode qui n'utilise que `/`, `%` et la concaténation.
  - Indice : 3750 donne 37 euros, puis le chiffre des dizaines de centimes, puis celui des unités.
  - Calcule à la main pour `990`, `573` et `5157`, **avant** de coder.
- Utilise-la dans la ligne de chaque article.

**❓ Question :** pourquoi les centimes en `int`, plutôt que `12.50` en `double` ? (`double` est le type des nombres à virgule.) Pour t'aider, ajoute temporairement `System.out.println(0.1 + 0.2);` au début de `main`, lance, regarde, puis efface la ligne.

### ☐ Étape 5 — Le ticket : deux text blocks

```
+--------------------------------+
|       LIBRAIRIE DU PORT        |
|  12, quai des Brumes - Nantes  |
+--------------------------------+
...
    Merci de votre visite !
      "Lire, c'est voyager."
```

**📖 La leçon : le text block, un texte sur plusieurs lignes.** Il commence par **trois guillemets** `"""` suivis d'un **retour à la ligne**, et se termine par trois guillemets `"""` :

```java
String poeme = """
        Il pleut,
          il mouille.
        """;
System.out.print(poeme);
```

affiche (sans espace devant `Il`, et 2 espaces devant `il`) :

```
Il pleut,
  il mouille.
```

**Les trois règles à connaître :**
1. **Les espaces de gauche.** Dans ton code, les lignes sont décalées vers la droite pour être jolies. Java retire ce décalage commun, qu'on appelle l'indentation « accidentelle ». Pour savoir combien en retirer, il regarde **toutes** les lignes du bloc, **y compris celle des `"""` fermants**, et prend **la plus petite** marge. Si on recule les `"""` fermants de 4 colonnes vers la gauche, chaque ligne garde donc 4 espaces de plus :
   ```java
   String poeme = """
           Il pleut,
             il mouille.
       """;
   ```
   affiche `····Il pleut,` puis `······il mouille.` (chaque `·` est un espace).
2. **Le saut de ligne final.** Si les `"""` fermants sont **seuls sur leur ligne**, le texte **finit par un saut de ligne**. Avec `println`, qui en ajoute un autre, on obtient donc une ligne vide en plus. Avec `print`, non. Si les `"""` fermants sont **collés** à la dernière ligne (`il mouille.""";`), il n'y a pas de saut de ligne final.
3. **Un `\` en fin de ligne** colle la ligne suivante à celle-ci :
   ```java
   String poeme = """
           Il pleut, \
           il mouille.
           """;
   ```
   s'affiche sur **une seule** ligne : `Il pleut, il mouille.`

Enfin, dans un text block, les guillemets `"` s'écrivent **tels quels**, sans `\` devant : `Il a dit "bonjour".`

**👉 À toi :**
- **L'en-tête** est un text block.
  - **Contrainte :** dans ton code source, la ligne de l'adresse est écrite sur **deux lignes**, mais elle s'affiche sur **une seule** (règle 3).
- **Le pied** est un autre text block. Ses lignes commencent par **4 et 6 espaces**.
  - Ces espaces viennent de la **position des `"""` fermants** (règle 1), et non d'espaces tapés dans des guillemets.
  - Si ta sortie diffère, `Check` affiche les espaces de tête sous forme de points `·`.
- Choisis `print` ou `println` pour chaque text block (règle 2), pour qu'il n'y ait **aucune** ligne vide en trop.

**❓ Questions :**
- Quel caractère, en fin de ligne d'un text block, supprime le saut de ligne ?
- Comment Java calcule-t-il l'indentation « accidentelle » qu'il retire ? Explique-le avec tes mots.
- Les guillemets de la citation **ne s'échappent pas** dans un text block. Quand faudrait-il un `\"` ?

### ☐ Étape 6 — Le résumé

```
Articles   : 5
Sous-total : 57.30
Remise 10% : -5.73
TOTAL      : 51.57
Carte fidelite : true
```

**📖 La leçon : `+` se lit de gauche à droite.** Java calcule `a + b + c` comme `(a + b) + c`. Dès qu'un des deux côtés d'un `+` est un **texte**, ce `+` **colle** au lieu d'additionner, et le résultat est un texte. Les **parenthèses** forcent un calcul à se faire en premier, comme en mathématiques.

**👉 À toi :**
- La remise vaut `sous-total × % / 100` en centimes (division entière). Calcule-la à la main : `5730 × 10 / 100` = ?
- Le total vaut sous-total − remise.
- Le nombre d'articles est la somme des **quantités** des deux articles.
- Regarde bien les espaces de chaque ligne : `Articles` est suivi de **3** espaces avant `:`.

**❓ Question :** que vaudrait `"Articles : " + 3 + 2` ? Et `"Articles : " + (3 + 2)` ? Pourquoi ? Vérifie avec deux lignes temporaires au début de `main`, puis efface-les. (Le chapitre 2 détaillera cette règle.)

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `main(String... args)` | 1 | ☐ |
| 2 classes (`class` ×2) dans ton code | 2 | ☐ |
| `this.` | 2 | ☐ |
| `args[7]` | 3 | ☐ |
| `Integer.parseInt`, `Integer.valueOf` + `intValue()`, `Boolean.parseBoolean` | 3 | ☐ |
| `final int` | 3 | ☐ |
| 2 text blocks (`"""` ×4) | 5 | ☐ |
| `\` en fin de ligne dans un text block | 5 | ☐ |

---

## Sortie attendue complète

```
+--------------------------------+
|       LIBRAIRIE DU PORT        |
|  12, quai des Brumes - Nantes  |
+--------------------------------+
Dune x 3 a 12.50 = 37.50
Fondation x 2 a 9.90 = 19.80
--------------------------------
Articles   : 5
Sous-total : 57.30
Remise 10% : -5.73
TOTAL      : 51.57
Carte fidelite : true
    Merci de votre visite !
      "Lire, c'est voyager."
```
