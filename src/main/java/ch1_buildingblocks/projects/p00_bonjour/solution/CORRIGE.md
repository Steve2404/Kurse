# Projet 0 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Bonjour.java`](Bonjour.java).
>
> Tous les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18), en tapant les commandes du `TODO.md` dans le terminal. `javac` écrit en anglais : chaque message est **traduit** et **expliqué**.

---

## Étape 1 — Trouver ton dossier

**Réponse :** le dossier est `src/main/java/ch1_buildingblocks/projects/p01_receipt`. On garde la partie après `src/main/java/` et on remplace les `/` par des points. Le paquet est donc **`ch1_buildingblocks.projects.p01_receipt`**.

---

## Étape 2 — Créer la classe `Bonjour`

Rien à vérifier, sauf ces deux points :
- le fichier s'appelle `Bonjour.java`, avec un **B majuscule** ;
- la 1re ligne est `package ch1_buildingblocks.projects.p00_bonjour;`.

Si la ligne `package` manque, ou si elle est différente, c'est que le clic droit n'a pas été fait sur le dossier `p00_bonjour`. Supprime le fichier (clic droit → **Delete**) et recommence.

---

## Étape 3 — La porte d'entrée `main`

**Le code de l'étape :**

```java
package ch1_buildingblocks.projects.p00_bonjour;

public class Bonjour {

    public static void main(String[] args) {
        System.out.println("Bienvenue dans le cours de Java.");
    }
}
```

**Pas de flèche verte ?** Vérifie la ligne `main` mot par mot. La flèche n'apparaît que si `main` est écrite **exactement** comme une porte d'entrée : `public static void main(String[] args)`.

---

## Étape 4 — Lancer le correcteur `Check`

La sortie montrée dans le `TODO.md` est bien celle qu'on obtient à ce moment-là. Tu ne dois rien corriger encore : l'étape 5 ajoute ce qui manque.

---

## Étape 5 — Les arguments

**Le code de l'étape :** voir [`Bonjour.java`](Bonjour.java).

**Expérience 1 — un seul mot, `Marie` :**

```
Bonjour, Marie !
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
	at ch1_buildingblocks.projects.p00_bonjour.Bonjour.main(Bonjour.java:7)
```

- La 1re ligne **s'affiche** : `args[0]` existe, c'est `Marie`.
- Puis le programme s'arrête à la **ligne 7**, celle de `args[1]`. Le message veut dire : « case n° 1 demandée, mais la liste n'a que **1** mot ». Les cases vont donc seulement de 0 à 0.
- La 3e ligne (`Bienvenue…`) ne s'affiche **jamais** : après une exception, le programme ne continue pas.

**Expérience 2 — `"Marie Curie" Paris` :**

```
Bonjour, Marie Curie !
Tu habites a Paris.
Bienvenue dans le cours de Java.
```

Le programme a reçu **2** mots : `Marie Curie` (les guillemets regroupent les deux mots en un seul argument), puis `Paris`. Les guillemets eux-mêmes ne font **pas** partie de l'argument.

---

## Étape 6 — Le terminal

**Expérience 1 — le nom court, `Bonjour` :**

```
Erreur : impossible de trouver ou de charger la classe principale Bonjour
Causé par : java.lang.ClassNotFoundException: Bonjour
```

`java` cherche un fichier `Bonjour.class` **directement** dans `build/p00`. Or il se trouve dans `build/p00/ch1_buildingblocks/projects/p00_bonjour/`. Il faut toujours donner le **nom complet**, paquet compris.

On obtient la même erreur en écrivant `…/Bonjour.java` ou `…Bonjour.class` : `java` attend un **nom de classe**, pas un nom de fichier.

**Expérience 2 — aucun argument :**

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
	at ch1_buildingblocks.projects.p00_bonjour.Bonjour.main(Bonjour.java:6)
```

C'est la même erreur que dans IntelliJ sans Program arguments : la liste `args` est vide.

**Sans les guillemets autour de `-Duser.language=fr`**, dans PowerShell :

```
Fehler: Hauptklasse .language=fr konnte nicht gefunden oder geladen werden
```

PowerShell a coupé le morceau au point. `java` a reçu `-Duser` d'un côté, et `.language=fr` de l'autre, qu'il a pris pour un **nom de classe**. D'où la règle : **toujours** des guillemets autour de `"-Duser.language=fr"`.

---

## Étape 7 — Les erreurs

**Expérience 1 — le `;` effacé :**

```
Bonjour.java:6: error: ';' expected
```

« Point-virgule attendu », ligne 6. Le chapeau `^` pointe juste **après** la parenthèse fermante : c'est là que `javac` attendait le `;`.

**Expérience 2 — les guillemets effacés :** `javac` affiche **5 erreurs** sur la ligne 8 : `')' expected`, `';' expected` (deux fois), `<identifier> expected` et `not a statement`. Sans guillemets, `javac` croit que `Bienvenue`, `dans`, `le`… sont des **noms** de choses Java, et il ne comprend plus rien à la phrase.

**La leçon :** une seule faute peut produire **plusieurs** messages. Corrige toujours la **première** erreur d'abord, puis recompile : les suivantes disparaissent souvent toutes seules.

**Expérience 3 — `system` avec un petit `s` :**

```
Bonjour.java:6: error: package system does not exist
```

« Le paquet `system` n'existe pas ». Java fait la différence entre majuscules et minuscules : `system` n'est pas `System`. Comme `system.out` ressemble à un nom de paquet, `javac` cherche un paquet `system`, et n'en trouve pas.

**Expérience 4 — `public class bonjour` :**

```
Bonjour.java:3: error: class bonjour is public, should be declared in a file named bonjour.java
```

« La classe `bonjour` est publique : elle devrait être dans un fichier nommé `bonjour.java` ». C'est la règle du nom de l'étape 2 : le nom du fichier et celui de la classe publique doivent être **identiques**, majuscules comprises.

**Expérience 5 — `static` effacé :**
- `javac` ne dit **rien** : la traduction réussit, `Bonjour.class` est fabriqué ;
- `java` refuse de lancer :

```
Erreur : la méthode principale n'est pas static dans la classe ch1_buildingblocks.projects.p00_bonjour.Bonjour, définissez la méthode principale comme suit :
   public static void main(String[] args)
```

- dans IntelliJ, la **flèche verte disparaît** : IntelliJ ne reconnaît plus de porte d'entrée.

**Réponse à la question : non, `javac` et `java` ne vérifient pas la même chose.**
- `javac` vérifie que le texte est du **Java correct**, comme on vérifie l'orthographe et la grammaire d'une phrase. Une méthode `main` sans `static` est du Java correct : c'est une méthode ordinaire.
- `java` vérifie, au lancement, qu'il existe une **porte d'entrée** de la bonne forme : `public static void main(String[] args)`. Sinon, il refuse de démarrer.

Cette différence revient **très souvent** à l'examen : « est-ce que ça compile ? » et « est-ce que ça se lance ? » sont deux questions différentes.

---

## Étape 8 — Les commentaires

Exemple de réponses écrites en commentaire :

```java
    // Etape 1 : le paquet de p01 est ch1_buildingblocks.projects.p01_receipt
    // Etape 7 : javac verifie que le Java est correct ; java verifie au lancement
    //           qu'il existe un main "public static void main(String[] args)".
    public static void main(String[] args) {
```

`Check` dit toujours `PROJET REUSSI` : les commentaires sont ignorés par `javac`, et ne changent rien au programme.
