# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier (`app/`, `model/`, `export/`, `tools/`, `commandes.sh`).
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur une copie du projet où le paquet s'appelle `shop` au lieu de `ch1_buildingblocks.projects.p04_bookshop`. Chez toi, les noms seront plus longs, mais les messages sont les mêmes. Sur un Windows en allemand, `java` affiche ses erreurs en allemand ; le texte anglais s'obtient avec `java -Duser.language=en …`.
>
> Dans les extraits, `P` remplace `ch1_buildingblocks.projects.p04_bookshop`.

---

## Étape 1 — Le modèle (paquet `model`)

**Le code de l'étape :**

```java
package P.model;

public class Author {
    String firstName;
    String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String fullName() { return firstName + " " + lastName; }
    public String sortName() { return lastName + ", " + firstName; }
}
```

```java
package P.model;

public class Book {
    String title;
    Author author;
    int year;

    public Book(String title, Author author, int year) { … trois this.x = x; … }

    public String title()  { return title; }
    public Author author() { return author; }
    public int year()      { return year; }

    public String describe() {
        return title + ", de " + author.fullName() + " (" + year + ")";
    }
}
```

**Question — faut-il un `import` pour `Author` dans `Book` ?** **Non.** Les deux classes sont dans le **même paquet**, et les classes d'un même paquet se voient sans import. On n'importe jamais non plus `java.lang`.

**Expérience — retirer `public` devant `class Author`.** Le message dépend de la **forme de l'import** dans `app` :
- avec le joker `import P.model.*;` :

  ```
  error: cannot find symbol
    symbol:   class Author
  ```

  Un joker n'importe que les classes **publiques** : `Author` est donc tout simplement introuvable ;
- avec un import explicite `import P.model.Author;` :

  ```
  error: Author is not public in P.model; cannot be accessed from outside package
  ```

**À retenir :** une classe sans modificateur n'est visible que dans **son** paquet. Pour être utilisée ailleurs, la classe doit être `public`, et aussi son constructeur et ses méthodes.

---

## Étape 2 — L'export (paquet `export`) : le conflit de noms

**Le code de l'étape :**

```java
package P.export;

public class Book {
    P.model.Book source;

    public Book(P.model.Book source) {
        this.source = source;
    }

    public String csv() {
        return "\"" + source.title() + "\";\"" + source.author().sortName() + "\";" + source.year();
    }
}
```

(Dans ton fichier, `P` est écrit en entier.)

**Expérience — `import P.model.Book;` dans ce fichier :**

```
error: Book is already defined in this compilation unit
```

Le fichier déclare déjà une classe `Book`. Un import ne peut pas introduire un **deuxième** `Book` : le nom simple serait ambigu.

**Comment désigner `model.Book` sans import ?** Par son **nom pleinement qualifié**, `P.model.Book`. Il est permis partout où un type est attendu, sans aucun import. Ici, `Book` seul désigne la classe en cours (`export.Book`).

---

## Étape 3 — Le lanceur (paquet `app`)

**Le code de l'étape :**

```java
package P.app;

import P.model.*;

public class Main {
    public static void main(String[] args) {
        Book book = new Book(args[0], new Author(args[1], args[2]), Integer.parseInt(args[3]));
        P.export.Book line = new P.export.Book(book);
        System.out.println("MODELE : " + book.describe());
        System.out.println("EXPORT : " + line.csv());
    }
}
```

**Question — pourquoi `Integer` sans import ?** `Integer` est dans **`java.lang`**, importé automatiquement dans tous les fichiers (comme `String`, `System`, `Math`…).

**Expérience 1 — ajouter `import P.export.*;` :**
- Tant que `Book` n'apparaît pas seul, ça **compile**. Deux jokers qui contiennent le même nom ne posent aucun problème si ce nom n'est pas utilisé.
- Dès qu'on écrit `Book` :

  ```
  error: reference to Book is ambiguous
  ```

**Expérience 2 — `import java.util.*;` et `import java.sql.*;`, puis `Date d;` :**

```
error: reference to Date is ambiguous
```

Les deux paquets ont une classe `Date`. Avec **en plus** `import java.util.Date;`, ça compile : un import **explicite** l'emporte sur un joker, et `Date` désigne `java.util.Date`. En revanche, **deux imports explicites** du même nom simple (`java.util.Date` et `java.sql.Date`) ne compilent jamais.

**Expérience 3 — `P.*` importe-t-il les classes de `P.model` ?** **Non.** Un joker n'importe que les classes **du paquet lui-même**, jamais celles de ses sous-paquets. Pour Java, `P` et `P.model` sont deux paquets **sans lien**, même si les dossiers sont imbriqués. `Author` reste introuvable : `cannot find symbol`.

---

## Étape 4 — L'outil autonome (paquet `tools`)

**Le code de l'étape :**

```java
package P.tools;

public class Hello {
    public static void main(String[] args) {
        System.out.println("Bonjour " + args[0] + " ! (lance sans javac, depuis un seul fichier source)");
    }
}
```

**Question — pourrait-il utiliser `model.Book` en mode « fichier source unique » ?**
- **Pas depuis les sources du projet.** `java Hello.java` compile **ce seul fichier**, en mémoire. Il ne compile pas les autres `.java`, même s'ils sont dans le dossier voisin. Avec un `import P.model.Book;`, lancé seul :

  ```
  error: package P.model does not exist
  ```

- **Oui, si leurs `.class` existent déjà** et qu'on les donne avec `-cp` : `java -cp build/ch1-p04/classes …/Hello.java Lea` fonctionne. Le fichier source unique peut utiliser les classes **déjà compilées** du classpath, mais jamais d'autres fichiers source.

---

## Étape 5 — Le script `commandes.sh`

**Le script :**

```bash
#!/bin/bash
set -e

SRC=src/main/java
P=ch1_buildingblocks/projects/p04_bookshop
MAIN=ch1_buildingblocks.projects.p04_bookshop.app.Main
OUT=build/ch1-p04
rm -rf "$OUT"

# 1. Compiler
javac -d "$OUT/classes" "$SRC/$P/app/Main.java" "$SRC/$P/model/Author.java" "$SRC/$P/model/Book.java" "$SRC/$P/export/Book.java"

# 2. Lancer depuis le dossier de classes
java -cp "$OUT/classes" "$MAIN" Hyperion Dan Simmons 1989

# 3. Jar simple, comptage, lancement depuis le jar
jar --create --file "$OUT/bookshop.jar" -C "$OUT/classes" .
echo "classes dans le jar : $(jar --list --file "$OUT/bookshop.jar" | grep -c '\.class$')"
java -cp "$OUT/bookshop.jar" "$MAIN" Fondation Isaac Asimov 1951

# 4. Jar exécutable
jar --create --file "$OUT/bookshop-app.jar" --main-class "$MAIN" -C "$OUT/classes" .
java -jar "$OUT/bookshop-app.jar" "Le Hobbit" John Tolkien 1937

# 5. Fichier source unique
java "$SRC/$P/tools/Hello.java" Lea
```

**Question 1 — sans `-d`, où vont les `.class` ?** **À côté de chaque `.java`**, dans le dossier source : `app/Main.class`, `model/Book.class`… Avec `-d build/ch1-p04/classes`, `javac` **crée l'arborescence des paquets** sous ce dossier : `build/ch1-p04/classes/ch1_buildingblocks/projects/p04_bookshop/model/Book.class`, etc. C'est cette arborescence que `java -cp` et `jar` attendent.

**Expériences 2 — mauvais lancements :**

| Commande | Message |
|---|---|
| sans `-cp` | `Error: Could not find or load main class ….app.Main` puis `Caused by: java.lang.ClassNotFoundException: ….app.Main` |
| `….app.Main.class` | `Error: Could not find or load main class ….app.Main.class` |
| `Main` (nom court) | `Error: Could not find or load main class Main` |

- **Sans `-cp`**, `java` cherche dans le dossier courant (la racine du dépôt), où il n'y a pas de `.class`.
- **`java` attend un nom de classe**, pas un nom de fichier. Il comprend `Main.class` comme « la classe `class` du paquet `….Main` ».
- **Le nom court ne suffit pas** : il faut le nom **pleinement qualifié**, paquet compris.

**Le comptage :** `jar --list` affiche une ligne par entrée : les dossiers, `META-INF/MANIFEST.MF` et les 4 `.class`. `grep -c '\.class$'` ne compte que les lignes qui **finissent** par `.class`, donc 4. Le `\.` désigne un vrai point ; `.` seul accepterait n'importe quel caractère.

**Expérience 4 — `java -jar bookshop.jar` (sans point d'entrée) :**

```
no main manifest attribute, in build/ch1-p04/bookshop.jar
```

`java -jar` lit la classe à lancer dans le manifeste (`META-INF/MANIFEST.MF`, ligne `Main-Class:`). `--main-class` écrit cette ligne ; le premier jar ne l'a pas.

**Les guillemets :** sans eux, `Le Hobbit` deviendrait deux arguments : `args[0] = "Le"`, `args[1] = "Hobbit"`. Tout serait décalé, et `Integer.parseInt("Tolkien")` lèverait une `NumberFormatException`.

**À savoir par cœur pour l'examen :**

| Commande | Rôle |
|---|---|
| `javac -d dossier fichiers.java` | compiler, `.class` rangés selon les paquets |
| `java -cp chemin nom.complet.Classe args` | lancer (aussi `-classpath`, `--class-path`) |
| `jar -cvf x.jar -C dossier .` | créer un jar (forme courte de `--create --file`) |
| `jar -cfe x.jar nom.complet.Classe -C dossier .` | jar avec point d'entrée (`e` = `--main-class`) |
| `java -jar x.jar args` | lancer un jar exécutable |
| `java Fichier.java args` | lancer un fichier source unique, sans `javac` |
