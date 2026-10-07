# Projet 4 — Une application en plusieurs paquets, construite en ligne de commande

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 1) :**
- les **paquets** : `package`, dossiers et paquets, sous-paquets ;
- les **imports** : explicite, joker `*`, `java.lang` implicite, **conflit de noms** ;
- le **nom pleinement qualifié** d'une classe ;
- `public` sur une classe (visible hors de son paquet) ;
- les **commandes** du JDK :
  - `javac -d` (compiler) ;
  - `java -cp` (exécuter) ;
  - `jar` (empaqueter, avec ou sans point d'entrée) ;
  - `java -jar` ;
  - le lancement d'un **fichier source unique**.

**Ce qui est donné :** `Check.java`.

**Ce que TU crées :**
- les classes de l'application, réparties dans **4 sous-paquets** de `ch1_buildingblocks.projects.p04_bookshop` : `app`, `model`, `export`, `tools`. La classe du `main` est **`app.Main`** ;
- le script **`commandes.sh`**, à la racine du dossier du projet (à côté de `Check.java`).

**Règle du crescendo :** seulement le chapitre 1, plus `+ - * / %`. Pas de `if`, pas de boucle, pas de méthodes de `String`, pas de module (chapitre 12).

**Ce que tu sais déjà faire** (projets 0 à 3) :
- créer une classe, lancer, lancer `Check`, donner des arguments ;
- taper `javac -d` et `java -cp` pour **un** fichier ;
- écrire des classes, des champs, des constructeurs et des méthodes.

**Ce que ce projet t'apprend en plus :**
- ranger des classes dans **plusieurs paquets** ;
- les relier avec `import` ;
- construire l'application **sans IntelliJ**, avec un **script** de commandes.

**Créer un sous-paquet dans IntelliJ :**
1. Clic droit sur le dossier `p04_bookshop` → **New** → **Package**.
2. IntelliJ propose le nom du paquet parent. Complète-le pour obtenir `ch1_buildingblocks.projects.p04_bookshop.model`, puis **Entrée**.
3. Un dossier `model` apparaît dans `p04_bookshop`. Clic droit dessus → **New** → **Java Class** pour y créer une classe : la ligne `package` sera juste.
4. Fais de même pour `app`, `export` et `tools`.

**Lancer `Check` :** comme d'habitude. Il vérifie l'application **et** ton script. Tant que le script n'existe pas, la partie « script » échoue : c'est normal jusqu'à l'étape 5.

---

## Le problème

Une librairie veut un outil qui décrit un livre **et** le formate pour un export tableur. Pour bien ranger le code :
- le **modèle** (livre, auteur) vit dans un paquet ;
- **l'export** vit dans un autre paquet ;
- le **lanceur** vit dans un troisième ;
- un petit **outil autonome** vit dans un quatrième.

Le hic : le modèle **et** l'export ont chacun une classe qui s'appelle… `Book`.

Ensuite, tu **construis et lances l'application toi-même en ligne de commande**, comme sur un serveur sans IDE. `Check` exécute ton script.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle (paquet `model`)

**📖 La leçon : un paquet par rôle, et `public` pour ouvrir la porte.** Un gros programme range ses classes dans plusieurs **paquets**, comme une maison range ses affaires dans plusieurs pièces. Deux règles :
- **Une classe `public` par fichier**, et le fichier porte son nom. Ici, chaque classe aura donc **son propre fichier**.
- **Ce qui est utilisé depuis un autre paquet doit être `public`** : la classe, son constructeur et ses méthodes. Sans `public`, la classe est « privée au paquet » : invisible depuis les autres pièces de la maison.

Exemple, sur un petit zoo :

```java
// fichier zoo/animaux/Chat.java
package zoo.animaux;

public class Chat {
    public String nom;

    public Chat(String nom) {          // public : le paquet zoo.app pourra faire new Chat(...)
        this.nom = nom;
    }

    public String cri() {              // public : le paquet zoo.app pourra l'appeler
        return nom + " fait miaou";
    }
}
```

Sans `public` devant `class Chat`, `javac` refuse, **dans l'autre paquet** :

```
error: Chat is not public in zoo.animaux; cannot be accessed from outside package
```

(« `Chat` n'est pas public dans `zoo.animaux` : inaccessible depuis l'extérieur du paquet »)

**Deux classes du même paquet** se voient **sans** rien écrire de plus.

**👉 À toi :**

- **Un auteur :** prénom et nom. Il sait donner son nom complet (`Frank Herbert`) et son nom de tri (`Herbert, Frank`).
- **Un livre :** titre, auteur et année. Il sait se décrire (`Dune, de Frank Herbert (1965)`).
- **Question :** le livre utilise l'auteur. Faut-il un `import` ? Pourquoi ?
- **Les classes et méthodes utilisées depuis un autre paquet** doivent être `public`.
  - **Expérience :** retire `public` devant `class Author`, puis lis l'erreur de `javac` **dans le paquet `app`**.

### ☐ Étape 2 — L'export (paquet `export`) : le conflit de noms

**📖 La leçon : `import`, ou le nom complet.** Pour utiliser `Chat` depuis **un autre** paquet, il y a deux façons :

```java
// fichier zoo/app/Visite.java
package zoo.app;

import zoo.animaux.Chat;          // 1re façon : on importe, puis on écrit simplement Chat

public class Visite {
    public static void main(String[] args) {
        Chat felix = new Chat("Felix");
        System.out.println(felix.cri());                      // Felix fait miaou

        zoo.animaux.Chat tom = new zoo.animaux.Chat("Tom");   // 2e façon : le nom complet, sans import
        System.out.println(tom.cri());                        // Tom fait miaou
    }
}
```

Le **nom pleinement qualifié** (paquet + `.` + classe) marche **toujours**, même sans import. Sans import **ni** nom complet, `javac` répond `error: cannot find symbol` : il ne sait pas de quel `Chat` tu parles.

**👉 À toi :**

- La classe d'export s'appelle **aussi `Book`**. Elle enveloppe un livre du modèle et produit `"Dune";"Herbert, Frank";1965`.
- **Le piège :** elle a besoin de `model.Book`, mais elle **est** elle-même un `Book`.
  - **Expérience :** écris `import ….model.Book;` dans ce fichier. Lis l'erreur de `javac`.
  - Comment désigner `model.Book` sans import ?

### ☐ Étape 3 — Le lanceur (paquet `app`)

```
MODELE : Dune, de Frank Herbert (1965)
EXPORT : "Dune";"Herbert, Frank";1965
```

**📖 La leçon : le joker `*`, et le paquet offert.**
- `import zoo.animaux.*;` importe **toutes** les classes du paquet `zoo.animaux`, d'un coup. Mais **seulement** celles-là : pas celles des sous-paquets.
- Le paquet **`java.lang`** est importé **automatiquement** dans tous les fichiers. C'est pour cela que tu as pu utiliser `String`, `System` ou `Integer` sans jamais écrire d'`import`.
- Si **deux** paquets importés contiennent une classe du **même nom**, et que tu écris ce nom seul, `javac` ne sait pas laquelle choisir.

**👉 À toi :**

- **Les arguments :** titre, prénom, nom, année. `Check` lance `app.Main Dune Frank Herbert 1965`.
- **Contrainte :** importe le paquet `model` **avec le joker `*`**. Écris `export.Book` avec son **nom pleinement qualifié**.
- **Question :** `Integer` n'est importé nulle part. Pourquoi est-il utilisable ?
- **Expériences :**
  1. Ajoute aussi `import ….export.*;`. Ça compile tant que tu n'écris pas `Book` seul. Et si tu l'écris ? Lis l'erreur.
  2. Dans un fichier quelconque, écris `import java.util.*;` et `import java.sql.*;`, puis déclare une variable de type `Date`. Que dit `javac` ? Que change un `import java.util.Date;` explicite ajouté en plus ?
  3. Le joker `….p04_bookshop.*` importe-t-il les classes de `….p04_bookshop.model` ?

### ☐ Étape 4 — L'outil autonome (paquet `tools`)

**📖 La leçon : lancer un fichier source directement.** Depuis Java 11, un programme tenant dans **un seul fichier** peut se lancer **sans `javac`** : `java` le traduit en mémoire, puis l'exécute. Aucun `.class` n'est écrit sur le disque :

```
java Salut.java Lea
```

Ici, on donne le **nom du fichier**, avec `.java` : c'est la seule fois où `java` accepte un nom de fichier.

**👉 À toi :**

- Un programme `Hello` d'**un seul fichier**, qui affiche `Bonjour <nom> ! (lance sans javac, depuis un seul fichier source)`.
- **Question :** ce programme pourrait-il utiliser `model.Book` s'il était lancé en mode « fichier source unique » ? Pourquoi ?

### ☐ Étape 5 — Le script `commandes.sh`

**📖 La leçon : un script, c'est une liste de commandes dans un fichier.** Au lieu de taper les commandes une par une dans le terminal, on les écrit dans un fichier texte, une par ligne. Un seul ordre les lance toutes, dans l'ordre. Les scripts `.sh` sont lus par **bash**, un autre terminal que PowerShell, installé avec Git.

**Créer le fichier :** clic droit sur `p04_bookshop` → **New** → **File** → `commandes.sh`. Si IntelliJ propose d'installer un plugin pour les scripts, tu peux accepter ou ignorer.

**Lancer le script toi-même, depuis le dossier `Kurse`, dans le terminal PowerShell d'IntelliJ :**

```
& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch1_buildingblocks/projects/p04_bookshop/commandes.sh
```

- `&` veut dire à PowerShell : « lance le programme dont le chemin suit ».
- Le chemin complet de bash est **obligatoire** : un simple `bash` risque de lancer un **autre** bash de Windows (WSL), qui ne trouve pas Java.
- `Check` lance ton script exactement de cette façon.

**Ce qu'on écrit dans un script bash :**

```bash
#!/bin/bash
# Une ligne qui commence par # est un commentaire.
set -e                       # s'arrêter dès qu'une commande échoue
echo "Bonjour"               # echo affiche un texte
```

- **`|` (le « tuyau »)** envoie ce qu'affiche une commande à la commande suivante, au lieu de l'écran.
- **`grep -c 'motif'`** compte les lignes qui contiennent le motif. Dans le motif, `\.` est un vrai point, et `$` veut dire « fin de ligne ».
- **`$( … )`** remplace la commande entre parenthèses par ce qu'elle affiche.

Par exemple, `echo "fruits en p : $(printf 'pomme\npoire\nprune\n' | grep -c '^p')"` affiche `fruits en p : 3`.

**📖 La leçon : `javac`, `java` et `jar` sur plusieurs paquets.** Voici toute la chaîne, sur le zoo des étapes 1 et 2 (les sources sont sous `src/`) :

```bash
# Traduire : on donne TOUS les fichiers. -d recrée les dossiers des paquets sous build/zoo.
javac -d build/zoo src/zoo/animaux/Chat.java src/zoo/app/Visite.java
#   -> build/zoo/zoo/animaux/Chat.class et build/zoo/zoo/app/Visite.class

# Lancer : -cp dit OÙ chercher les .class ; puis le nom COMPLET de la classe du main.
java -cp build/zoo zoo.app.Visite

# Empaqueter : un .jar est une archive (comme un .zip) de .class.
#   --create --file : crée l'archive ; -C build/zoo . : « entre dans build/zoo et prends tout (.) »
jar --create --file build/zoo.jar -C build/zoo .
jar --list --file build/zoo.jar          # affiche le contenu de l'archive, une ligne par entrée
java -cp build/zoo.jar zoo.app.Visite    # on lance depuis l'archive

# Archive exécutable : --main-class écrit le point d'entrée dans l'archive (son « manifeste »).
jar --create --file build/zoo-app.jar --main-class zoo.app.Visite -C build/zoo .
java -jar build/zoo-app.jar              # plus besoin de donner le nom de la classe
```

`jar --list` affiche aussi des dossiers (`zoo/`, `zoo/app/`…) et un fichier `META-INF/MANIFEST.MF`. C'est pourquoi on ne compte que les lignes qui **finissent** par `.class`.

**👉 À toi :**

`Check` exécute ton script avec **bash**, depuis la **racine du dépôt** (le dossier qui contient `src/`). Tous les chemins partent donc de là, par exemple `src/main/java/ch1_buildingblocks/projects/p04_bookshop/app/Main.java`.

Écris des dossiers de travail **sous `build/`** (ignoré par git). Exemple : `build/ch1-p04`.

Le script enchaîne 5 actions :
1. **Compiler** les 4 classes de l'application avec `javac`, en rangeant les `.class` dans `build/ch1-p04/classes` (option `-d`).
   - **Question :** où `javac` aurait-il mis les `.class` sans `-d` ? Quelle arborescence `-d` crée-t-il ?
2. **Lancer** `app.Main` depuis ce dossier de classes avec `java -cp`, avec les arguments `Hyperion Dan Simmons 1989`.
   - **Expériences :**
     - lance sans `-cp` ;
     - lance avec le nom de classe **suivi de `.class`** ;
     - lance avec le nom **court** `Main`.
     
     Lis chaque erreur.
3. **Empaqueter** les classes dans `build/ch1-p04/bookshop.jar` avec `jar`.
   - Affiche `classes dans le jar : N` : liste le contenu du jar et compte les lignes qui finissent par `.class` (`grep -c`).
   - Puis lance `app.Main` **depuis le jar** (`-cp` du jar) avec `Fondation Isaac Asimov 1951`.
4. **Créer un jar exécutable** `bookshop-app.jar` dont le manifeste désigne le point d'entrée (option `--main-class` de `jar`), puis le lancer avec **`java -jar`** et `"Le Hobbit" John Tolkien 1937`.
   - Attention aux guillemets autour d'un argument qui contient une espace.
   - **Expérience :** `java -jar bookshop.jar` (le premier jar, sans point d'entrée) : quel message ?
5. **Lancer `Hello.java` directement** avec `java` (sans `javac`), avec l'argument `Lea`.

- **Conseil :** mets `set -e` en tête du script, pour qu'il s'arrête à la première commande qui échoue.
- **Sous Windows :** dans un `-cp` qui contient **plusieurs** chemins, le séparateur est `;` (et non `:`). Ici, un seul chemin suffit à chaque fois.

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| 4 paquets `…app`, `…model`, `…export`, `…tools` | 1 à 4 | ☐ |
| `public class Book` | 1, 2 | ☐ |
| nom pleinement qualifié de `model.Book` (dans `export.Book`) | 2 | ☐ |
| `import ….model.*;` | 3 | ☐ |
| `new ….export.Book(` (nom pleinement qualifié dans `Main`) | 3 | ☐ |
| `Integer.parseInt` | 3 | ☐ |
| script : sortie identique | 5 | ☐ |

---

## Sortie attendue complète

```
MODELE : Dune, de Frank Herbert (1965)
EXPORT : "Dune";"Herbert, Frank";1965
```

## Sortie attendue du script

```
MODELE : Hyperion, de Dan Simmons (1989)
EXPORT : "Hyperion";"Simmons, Dan";1989
classes dans le jar : 4
MODELE : Fondation, de Isaac Asimov (1951)
EXPORT : "Fondation";"Asimov, Isaac";1951
MODELE : Le Hobbit, de John Tolkien (1937)
EXPORT : "Le Hobbit";"Tolkien, John";1937
Bonjour Lea ! (lance sans javac, depuis un seul fichier source)
```
