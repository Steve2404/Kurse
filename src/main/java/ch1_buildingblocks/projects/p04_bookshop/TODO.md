# Projet 4 — Une application en plusieurs paquets, construite en ligne de commande

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

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

- **Un auteur :** prénom et nom. Il sait donner son nom complet (`Frank Herbert`) et son nom de tri (`Herbert, Frank`).
- **Un livre :** titre, auteur et année. Il sait se décrire (`Dune, de Frank Herbert (1965)`).
- **Question :** le livre utilise l'auteur. Faut-il un `import` ? Pourquoi ?
- **Les classes et méthodes utilisées depuis un autre paquet** doivent être `public`.
  - **Expérience :** retire `public` devant `class Author`, puis lis l'erreur de `javac` **dans le paquet `app`**.

### ☐ Étape 2 — L'export (paquet `export`) : le conflit de noms

- La classe d'export s'appelle **aussi `Book`**. Elle enveloppe un livre du modèle et produit `"Dune";"Herbert, Frank";1965`.
- **Le piège :** elle a besoin de `model.Book`, mais elle **est** elle-même un `Book`.
  - **Expérience :** écris `import ….model.Book;` dans ce fichier. Lis l'erreur de `javac`.
  - Comment désigner `model.Book` sans import ?

### ☐ Étape 3 — Le lanceur (paquet `app`)

```
MODELE : Dune, de Frank Herbert (1965)
EXPORT : "Dune";"Herbert, Frank";1965
```
- **Les arguments :** titre, prénom, nom, année. `Check` lance `app.Main Dune Frank Herbert 1965`.
- **Contrainte :** importe le paquet `model` **avec le joker `*`**. Écris `export.Book` avec son **nom pleinement qualifié**.
- **Question :** `Integer` n'est importé nulle part. Pourquoi est-il utilisable ?
- **Expériences :**
  1. Ajoute aussi `import ….export.*;`. Ça compile tant que tu n'écris pas `Book` seul. Et si tu l'écris ? Lis l'erreur.
  2. Dans un fichier quelconque, écris `import java.util.*;` et `import java.sql.*;`, puis déclare une variable de type `Date`. Que dit `javac` ? Que change un `import java.util.Date;` explicite ajouté en plus ?
  3. Le joker `….p04_bookshop.*` importe-t-il les classes de `….p04_bookshop.model` ?

### ☐ Étape 4 — L'outil autonome (paquet `tools`)

- Un programme `Hello` d'**un seul fichier**, qui affiche `Bonjour <nom> ! (lance sans javac, depuis un seul fichier source)`.
- **Question :** ce programme pourrait-il utiliser `model.Book` s'il était lancé en mode « fichier source unique » ? Pourquoi ?

### ☐ Étape 5 — Le script `commandes.sh`

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
