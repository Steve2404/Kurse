# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`BootLog.java`](BootLog.java).
>
> Les messages d'erreur ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18). `javac` affiche aussi la ligne fautive et un `^` sous l'endroit exact.

---

## Étape 1 — L'outil de journalisation

**Le code de l'étape** (dans `BootLog`) :

```java
static int step;
static int created;

static void note(String message) {
    step = step + 1;
    System.out.println(step + ". " + message);
}

static int logInt(String field, int value) {
    note(field + " = " + value);
    return value;
}

static String logText(String field, String value) {
    note(field + " = " + value);
    return value;
}
```

**Question — pourquoi `step` vaut 0 sans initialisation ?** C'est un **champ** (ici de classe). Les champs reçoivent une valeur par défaut : `0` pour un `int`. Seules les variables **locales** doivent être affectées avant d'être lues.

**L'idée clé :** `logInt` et `logText` **rendent** la valeur reçue. On peut donc écrire `int port = BootLog.logInt("Server : champ port", 8080);`. Le champ reçoit 8080, et le journal prouve **quand** cette initialisation a eu lieu.

---

## Étape 2 — L'ordre d'initialisation : la classe `Server`

**Le code de l'étape :**

```java
class Server {
    int port = BootLog.logInt("Server : champ port", 8080);                        // ligne 1

    {
        BootLog.note("Server : bloc A (port = " + port + ", name = " + this.name + ")"); // ligne 2
    }

    String name = BootLog.logText("Server : champ name", "alpha");                // ligne 3

    int early = readLate();                                                        // ligne 4
    int late = BootLog.logInt("Server : champ late", 42);                          // ligne 5

    {
        BootLog.note("Server : bloc B (early = " + early + ", late = " + late + ")"); // ligne 6
    }

    int maxUsers;

    Server(int maxUsers) {                                                         // lignes 7 et 8
        BootLog.note("Server : constructeur debut (this.maxUsers = " + this.maxUsers + ", parametre maxUsers = " + maxUsers + ")");
        this.maxUsers = maxUsers;
        BootLog.created = BootLog.created + 1;
        BootLog.note("Server : constructeur fin (maxUsers = " + this.maxUsers + ", serveur n " + BootLog.created + ")");
    }

    int readLate() {
        BootLog.note("Server : lecture anticipee de late = " + late);
        return late;
    }
}
```

**La règle (à savoir par cœur pour l'examen) :** à chaque `new Server(...)`, Java :
1. met **tous** les champs à leur valeur par défaut (`0`, `null`, `false`) ;
2. exécute les **initialiseurs de champs et les blocs d'instance, dans l'ordre où ils sont écrits**, de haut en bas ;
3. exécute **ensuite** le corps du constructeur.

**Ligne 2, `name = null` — expérience : `name` seul dans le bloc A :**

```
error: illegal forward reference
```

Le nom simple d'un champ ne peut pas être **lu** dans un initialiseur placé **avant** sa déclaration. `this.name` est accepté : le compilateur ne contrôle que le nom simple. On lit alors la valeur par défaut, **`null`**, puisque l'initialiseur `= "alpha"` n'a pas encore tourné (étape 1 de la règle).

**Lignes 4 à 6, `early = 0` — question : pourquoi la lecture via une méthode est-elle acceptée ?** La règle « illegal forward reference » ne vise que le **nom simple écrit dans l'initialiseur lui-même**. `javac` n'analyse pas ce que fait une méthode appelée. Dans `readLate()`, `late` est un accès normal à un champ, valide n'importe où. À l'exécution, l'initialiseur `= 42` n'a pas encore tourné : `late` vaut encore **0**. `early` reçoit donc 0, et c'est ce que montre le bloc B, alors que `late` vaut 42 à ce moment-là.

**Ligne 7, `this.maxUsers = 0` :**
- Au début du constructeur, **tous les initialiseurs ont déjà tourné**. Mais `maxUsers` n'en a pas (`int maxUsers;`) : le champ vaut sa valeur par défaut, **0**.
- `maxUsers` seul désigne le **paramètre** (50), qui masque le champ.
- Après `this.maxUsers = maxUsers;`, le champ vaut 50 (ligne 8).

**Le compteur de serveurs :** `created` est `static`. Il n'en existe **qu'un** pour toute la classe, partagé par tous les objets. D'où « serveur n 1 », puis « serveur n 2 ».

---

## Étape 3 — Deux objets, deux initialisations

**Le code de l'étape** (dans `main`) :

```java
System.out.println("--- 1er serveur ---");
var first = new Server(50);
System.out.println("--- 2e serveur ---");
var second = new Server(10);
```

**Question — pourquoi le journal recommence-t-il, mais pas le compteur ?** Les champs et les blocs d'**instance** s'exécutent **à chaque création d'objet** : chaque `Server` a son propre `port`, son `name`, etc. Le compteur `step` est un champ `static` de `BootLog` : il existe **une seule fois**, il n'est jamais remis à zéro et continue donc de 8 à 9.

**Expériences :**

| Ligne | Erreur de `javac` | Pourquoi |
|---|---|---|
| `var x;` | `cannot infer type for local variable x` `(cannot use 'var' on variable without initializer)` | `var` déduit le type de l'initialiseur ; sans initialiseur, rien à déduire |
| `var y = null;` | `cannot infer type for local variable y` `(variable initializer is 'null')` | `null` n'a pas de type utilisable |
| `var a = 1, b = 2;` | `'var' is not allowed in a compound declaration` | une seule variable par déclaration `var` |
| `var s = new Server(); s = "texte";` | `incompatible types: String cannot be converted to Server` | le type déduit (`Server`) est **fixé pour toujours**. `var` n'est pas un type dynamique. |

**À retenir sur `var` :** uniquement pour les variables **locales** (jamais pour un champ, un paramètre ou un type de retour), toujours avec un initialiseur. `var` n'est pas un mot réservé : `int var = 3;` compile.

---

## Étape 4 — La portée et le masquage

**Le code de l'étape** (dans `Server`, appelé par `first.showScope();`) :

```java
void showScope() {
    int port = 9090;
    BootLog.note("portee : port local = " + port + ", champ this.port = " + this.port);
    {
        int backup = port + 1;
        BootLog.note("portee : dans le bloc, backup = " + backup);
    }
    String name = "local";
    BootLog.note("portee : name local = " + name + ", champ this.name = " + this.name);
}
```

**Le masquage :** la variable locale `port` cache le champ. `port` seul désigne la locale (9090) ; `this.port` désigne toujours le champ (8080). Ce n'est pas une erreur. En revanche, **deux variables locales** de même nom dans des portées imbriquées seraient refusées.

**Expérience — `backup` après l'accolade fermante :**

```
error: cannot find symbol
  symbol:   variable backup
```

Une variable déclarée dans un bloc `{ }` n'existe que jusqu'à l'accolade fermante. Les quatre portées de ce projet sont :

| Portée | Exemple | Vit jusqu'à |
|---|---|---|
| de bloc | `backup` | la fin du bloc `{ }` |
| locale | `port` dans `showScope` | la fin de la méthode |
| d'instance | `this.port` | la disparition de l'objet |
| de classe | `BootLog.step` | la fin du programme |

---

## Étape 5 — Le bilan et le ramasse-miettes

**Le code de l'étape** (fin de `main`) :

```java
System.out.println("--- bilan ---");
final int expected = 2;
System.out.println(created + " serveurs crees (attendu " + expected + "), " + step + " etapes journalisees");
first = second;
System.out.println("first et second designent le serveur de " + first.maxUsers + " utilisateurs");
```

**Expérience — modifier `expected` :**

```
error: cannot assign a value to final variable expected
```

**Questions sur le ramasse-miettes :**
- **Après `first = second;` :** **un** objet est éligible, le serveur de **50** utilisateurs. Plus aucune variable n'y fait référence. `first` et `second` désignent tous deux le serveur de 10.
- **Avec `first = null;` à la place :** même réponse, le serveur de 50 devient éligible. Le serveur de 10 reste référencé par `second`.
- **`System.gc()` garantit-il la destruction ?** **Non.** C'est une **suggestion** à la JVM, qui peut l'ignorer. Java ne garantit ni **quand** ni **si** un objet éligible sera réellement récupéré.

**Le piège d'examen :** « éligible » ne veut pas dire « détruit ». Ce sont les **objets** qui sont éligibles, pas les variables. Une variable n'est qu'une flèche vers un objet.
