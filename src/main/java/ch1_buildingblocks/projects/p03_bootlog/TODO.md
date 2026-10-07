# Projet 3 — Le journal de démarrage d'un serveur

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 1) :**
- **l'ordre d'initialisation** d'un objet : champs et blocs d'initialisation dans l'ordre du texte, **puis** le constructeur ;
- les **valeurs par défaut** des champs, et le piège de la lecture anticipée ;
- la **portée** des variables : locale, de bloc, d'instance, de classe ;
- le **masquage** d'un champ par une variable locale ;
- `var` et `final` ;
- le **ramasse-miettes** (quand un objet devient-il éligible ?).

**Ce qui est donné :** `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch1_buildingblocks.projects.p03_bootlog`. La classe du `main` s'appelle **`BootLog`**. Le serveur est une **autre** classe, dans le même fichier ou dans le sien.

**Règle du crescendo :** seulement le chapitre 1, plus `+ - * / %`. Pas de `if`, pas de boucle, pas de `this(...)` entre constructeurs ni de bloc `static { }` (chapitre 6). `Check` les refuse.

**Ce que tu sais déjà faire** (projets 0 à 2) :
- créer la classe, lancer, lancer `Check` ;
- essayer un exemple dans une classe `Atelier`, puis la supprimer ;
- écrire une 2e classe dans le même fichier ;
- écrire un champ `static` ;
- écrire une méthode qui reçoit un paramètre et rend un résultat ;
- appeler une méthode `static` d'une autre classe avec `NomDeClasse.methode(…)`.

Ce projet n'a **pas** d'arguments.

**Tes deux commandes pour les expériences** (terminal, Alt + F12, depuis `Kurse`) :

```
javac -d build/p03 src/main/java/ch1_buildingblocks/projects/p03_bootlog/BootLog.java
java "-Duser.language=fr" -cp build/p03 ch1_buildingblocks.projects.p03_bootlog.BootLog
```

Si tu mets le serveur dans **son propre fichier** `Server.java`, donne les deux fichiers à `javac`, à la suite, séparés par un espace.

**Pour chaque expérience « est-ce que ça compile ? »** : écris la ligne, tape la commande `javac`, lis le message, puis **efface** la ligne.

---

## Le problème

Quand un serveur démarre, l'équipe veut un **journal numéroté** de chaque étape de construction de l'objet, pour comprendre dans quel ordre Java initialise tout.

La sortie attendue **est** ce journal. Ton travail consiste à placer les champs, les blocs et le constructeur **de sorte que Java produise exactement cet ordre**.

Tu n'as **pas le droit de tricher** en écrivant toutes les lignes dans le constructeur. `Check` exige de vrais blocs d'initialisation et un champ initialisé par un appel de méthode. Et les valeurs `null` et `0` du journal doivent venir **de Java**, pas d'un texte écrit à la main.

---

## Tableau de bord

### ☐ Étape 1 — L'outil de journalisation

**📖 La leçon : `=` range une valeur, ce n'est pas « égal ».** En Java, `step = step + 1` se lit **de droite à gauche** : « calcule `step + 1`, puis **range** le résultat dans `step` ». C'est ainsi qu'on fait avancer un compteur.

**📖 La leçon : une méthode qui fait quelque chose, et une méthode qui rend quelque chose.**
- Une méthode `void` **fait** un travail (afficher, compter…) et ne rend rien.
- Une méthode avec un type de retour **rend** une valeur avec `return`, qu'on peut ranger dans une variable. Elle peut aussi afficher quelque chose **avant** de rendre sa valeur.

```java
public class Atelier {
    static int clics;                        // champ static jamais initialisé : vaut 0

    static void clic(String qui) {           // void : fait un travail, ne rend rien
        clics = clics + 1;
        System.out.println(clics + ". clic de " + qui);
    }

    static int montre(String nom, int valeur) {   // affiche, PUIS rend la valeur reçue
        System.out.println(nom + " vaut " + valeur);
        return valeur;
    }

    public static void main(String[] args) {
        clic("Lea");                         // 1. clic de Lea
        clic("Tom");                         // 2. clic de Tom
        int age = montre("age", 7);          // affiche "age vaut 7", et range 7 dans age
        System.out.println(age + 1);         // 8
    }
}
```

**👉 À toi :**

- La classe `BootLog` contient un **compteur d'étapes**.
  - **Contrainte :** ce compteur n'est **jamais initialisé explicitement**. Pourquoi vaut-il pourtant 0 au départ ?
- Une méthode « noter » augmente le compteur (`step = step + 1`, car `++` et `+=` sont au chapitre 2) et affiche `numéro. message`.
- Deux méthodes « journaliser puis rendre » (une pour un `int`, une pour un `String`) permettent d'**initialiser un champ en l'écrivant au journal** :
  ```
  1. Server : champ port = 8080
  ```

### ☐ Étape 2 — L'ordre d'initialisation : la classe `Server`

```
1. Server : champ port = 8080
2. Server : bloc A (port = 8080, name = null)
3. Server : champ name = alpha
4. Server : lecture anticipee de late = 0
5. Server : champ late = 42
6. Server : bloc B (early = 0, late = 42)
7. Server : constructeur debut (this.maxUsers = 0, parametre maxUsers = 50)
8. Server : constructeur fin (maxUsers = 50, serveur n 1)
```

**📖 La leçon : dans quel ordre Java fabrique un objet.** Quand on écrit `new Gateau()`, Java :
1. donne à **tous** les champs leur valeur par défaut (`0`, `null`, `false`) ;
2. exécute, **de haut en bas dans le texte**, les initialisations de champs (`= …`) et les **blocs d'initialisation** ;
3. exécute **ensuite seulement** le corps du constructeur, **même s'il est écrit au milieu** de la classe.

Un **bloc d'initialisation** est une paire d'accolades `{ … }` écrite directement dans la classe, hors de toute méthode. Il s'exécute à chaque `new`.

```java
class Gateau {
    String farine = annonce("1. champ farine");
    {
        System.out.println("2. bloc d'initialisation");
    }
    Gateau() {
        System.out.println("4. constructeur");     // écrit en 3e position, mais exécuté en dernier
    }
    String sucre = annonce("3. champ sucre");

    static String annonce(String texte) {
        System.out.println(texte);
        return texte;
    }
}
```

`new Gateau()` affiche `1. champ farine`, `2. bloc d'initialisation`, `3. champ sucre`, puis `4. constructeur`.

**👉 À toi :**

**À la main d'abord.** Avant d'écrire une ligne, dessine l'ordre des champs, des deux blocs et du constructeur dans ta classe, de façon à reproduire ce journal.

**Les pièges à comprendre (et à expliquer en commentaire) :**
- **Ligne 2, `name = null`.** Le bloc A lit `name` **avant** sa déclaration.
  - **Expérience :** écris `name` tout seul dans le bloc A. Lis l'erreur de `javac`.
  - Comment le lire légalement ? Que vaut-il à ce moment-là, et pourquoi ?
- **Lignes 4 à 6, `early = 0`.** Le champ `early` est initialisé par un **appel de méthode** qui lit `late`, déclaré plus bas. Ici, pas d'erreur de compilation, mais la valeur lue n'est pas 42.
  - **Question :** pourquoi le compilateur refuse-t-il la lecture directe (ligne 2) mais accepte-t-il la lecture via une méthode ?
- **Ligne 7, `this.maxUsers = 0`.** Le paramètre du constructeur porte **le même nom** que le champ.
  - Au début du constructeur, que vaut le champ ? Et le paramètre ?
- **Le compteur de serveurs créés** est un champ **de classe** (`static`) de `BootLog`, incrémenté par le constructeur.

### ☐ Étape 3 — Deux objets, deux initialisations

```
--- 2e serveur ---
9. Server : champ port = 8080
...
16. Server : constructeur fin (maxUsers = 10, serveur n 2)
```

**📖 La leçon : `var`, Java devine le type.** Au lieu d'écrire le type d'une variable **locale**, on peut écrire `var`. Java regarde la valeur à droite du `=` et en déduit le type, **une fois pour toutes** :

```java
var age = 30;        // Java déduit : int
var nom = "Lea";     // Java déduit : String
age = "trente";      // refusé : age est un int pour toujours
                     // error: incompatible types: String cannot be converted to int
```

`var` ne marche que pour les variables **locales**, déclarées dans une méthode.

**📖 La leçon : un champ `static` est partagé, un champ ordinaire non.** Chaque objet a **ses propres** champs ordinaires. Un champ `static` existe en **un seul exemplaire**, partagé par toute la classe :

```java
class Chat {
    static int nes;           // un seul compteur pour tous les chats
    int vies = 9;             // chaque chat a ses propres vies
    Chat() { nes = nes + 1; }
}
```

Avec deux chats `a` et `b`, puis `a.vies = a.vies - 1;` : `a.vies` vaut 8, `b.vies` vaut toujours 9, et `Chat.nes` vaut 2.

**👉 À toi :**

- Le `main` crée **deux** serveurs (50 puis 10 utilisateurs). Déclare-les avec **`var`**.
- **Question :** pourquoi tout le journal recommence-t-il pour le 2e objet, alors que le compteur d'étapes, lui, continue ?
- **Expériences :** pour chacune, lis l'erreur de `javac`, puis retire la ligne.
  1. `var x;`
  2. `var y = null;`
  3. `var a = 1, b = 2;`
  4. réaffecte un `var` déclaré avec un `Server` en lui donnant un `String`

### ☐ Étape 4 — La portée et le masquage

```
17. portee : port local = 9090, champ this.port = 8080
18. portee : dans le bloc, backup = 9091
19. portee : name local = local, champ this.name = alpha
```

**📖 La leçon : la portée, là où une variable existe.** Une variable existe **de la ligne où elle est déclarée jusqu'à l'accolade `}` qui ferme son bloc**. Après, elle n'existe plus :

```java
int total = 5;
{
    int bonus = 2;
    System.out.println(total + bonus);   // 7 : les deux existent ici
}
System.out.println(bonus);              // refusé : bonus n'existe plus
                                        // error: cannot find symbol  (symbol: variable bonus)
```

**📖 La leçon : le masquage.** Si une méthode déclare une variable locale qui porte **le même nom** qu'un champ, le nom seul désigne la variable **locale**, la plus proche. Le champ existe toujours : on l'atteint avec `this.nom`. C'est le même mécanisme que dans le constructeur du projet 1.

**👉 À toi :**

- Une méthode du serveur déclare une variable **locale** `port` qui **masque** le champ. Affiche les deux.
- **Un bloc `{ }` à l'intérieur de la méthode** déclare une variable `backup`.
  - **Expérience :** utilise `backup` **après** l'accolade fermante. Que dit `javac` ?
- Même chose avec une variable locale `name`.

### ☐ Étape 5 — Le bilan et le ramasse-miettes

```
2 serveurs crees (attendu 2), 19 etapes journalisees
first et second designent le serveur de 10 utilisateurs
```

**📖 La leçon : une variable objet est une étiquette, pas l'objet.** Une variable comme `first` ne **contient** pas le serveur : elle est une **étiquette** attachée à un objet rangé ailleurs, dans la mémoire. Imagine des ballons tenus par des ficelles :
- `first = second;` détache l'étiquette `first` de son ballon, et l'attache au ballon de `second` ;
- un ballon que **plus aucune** ficelle ne tient s'envole : il devient **éligible au ramasse-miettes** (*garbage collector*), le service de Java qui récupère la mémoire inutilisée ;
- **éligible ne veut pas dire détruit tout de suite** : Java passe quand il veut.

`final` sur une variable locale : tu l'as vu au projet 1. Une nouvelle affectation est refusée par `javac` : `cannot assign a value to final variable …`.

**👉 À toi :**

- Le nombre attendu est une variable locale **`final`**.
  - **Expérience :** essaie de la modifier. Lis l'erreur.
- **À la fin, fais `first = second;`.** Réponds en commentaire :
  - après cette ligne, combien d'objets `Server` sont **éligibles** au ramasse-miettes ? Lequel ?
  - et si on avait fait `first = null;` à la place ?
  - `System.gc()` garantit-il que l'objet sera détruit ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `static int` (compteur jamais initialisé) | 1 | ☐ |
| au moins 2 blocs d'initialisation `{ … }` | 2 | ☐ |
| un champ initialisé par un appel de méthode (`int early = readLate();`) | 2 | ☐ |
| `this.name`, `this.maxUsers` | 2 | ☐ |
| `var` | 3 | ☐ |
| `this.port` (masquage) | 4 | ☐ |
| `final int` | 5 | ☐ |

---

## Sortie attendue complète

```
--- 1er serveur ---
1. Server : champ port = 8080
2. Server : bloc A (port = 8080, name = null)
3. Server : champ name = alpha
4. Server : lecture anticipee de late = 0
5. Server : champ late = 42
6. Server : bloc B (early = 0, late = 42)
7. Server : constructeur debut (this.maxUsers = 0, parametre maxUsers = 50)
8. Server : constructeur fin (maxUsers = 50, serveur n 1)
--- 2e serveur ---
9. Server : champ port = 8080
10. Server : bloc A (port = 8080, name = null)
11. Server : champ name = alpha
12. Server : lecture anticipee de late = 0
13. Server : champ late = 42
14. Server : bloc B (early = 0, late = 42)
15. Server : constructeur debut (this.maxUsers = 0, parametre maxUsers = 10)
16. Server : constructeur fin (maxUsers = 10, serveur n 2)
--- portee ---
17. portee : port local = 9090, champ this.port = 8080
18. portee : dans le bloc, backup = 9091
19. portee : name local = local, champ this.name = alpha
--- bilan ---
2 serveurs crees (attendu 2), 19 etapes journalisees
first et second designent le serveur de 10 utilisateurs
```
