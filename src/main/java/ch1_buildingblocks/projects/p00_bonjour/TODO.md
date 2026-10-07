# Projet 0 — Bonjour : tes tout premiers pas

> **Ce projet est différent des autres.** Ici, **tout est expliqué** et tu as le droit de **recopier** le code donné. Le but n'est pas encore de réfléchir à un programme, mais d'apprendre les **gestes** que tu referas dans tous les projets :
> - créer un fichier Java ;
> - le lancer ;
> - lancer le correcteur `Check` ;
> - donner des mots au programme (les **arguments**) ;
> - compiler et lancer **dans le terminal**, avec `javac` et `java` ;
> - lire un message d'erreur sans paniquer.
>
> **Durée :** 1 h à 1 h 30. Va doucement : **lis chaque étape jusqu'au bout avant de toucher au clavier**.
>
> **Bloqué ?** [`INDICES.md`](INDICES.md) donne des indices repliés. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne les réponses aux questions, étape par étape.

---

## Les mots à connaître

Tu n'as pas besoin de tout retenir maintenant. Reviens ici quand un mot te bloque.

| Mot | Ce que ça veut dire, simplement |
|---|---|
| **programme** | une liste d'ordres donnés à l'ordinateur, écrits dans un ordre précis, comme une recette de cuisine |
| **code source** | le texte du programme, celui que **tu** écris |
| **fichier `.java`** | un fichier qui contient du code source Java. Par exemple `Bonjour.java` |
| **compiler** | **traduire** ton code source dans une langue que la machine Java comprend. L'ordinateur ne lit pas directement ton `.java` |
| **`javac`** | le **traducteur** (le « compilateur »). Il lit `Bonjour.java` et fabrique `Bonjour.class`. S'il trouve une faute, il refuse et explique pourquoi |
| **fichier `.class`** | la traduction fabriquée par `javac`. Tu ne l'ouvres jamais : il n'est pas fait pour être lu par un humain |
| **`java`** | celui qui **exécute** : il prend un `.class` et fait ce qui est écrit dedans |
| **classe** | une **boîte** qui contient du code. En Java, tout le code est rangé dans des classes. Un fichier `Bonjour.java` contient la classe `Bonjour` |
| **méthode** | une suite d'ordres qui porte un nom, à l'intérieur d'une classe |
| **`main`** | la méthode spéciale qui sert de **porte d'entrée** : quand on lance un programme, Java commence par là |
| **paquet** (*package*) | un **dossier** qui range les classes. Le nom du paquet est le chemin du dossier, avec des points à la place des `/` |
| **console** | la zone où ton programme écrit ses lignes. Dans IntelliJ, c'est le panneau **Run**, en bas |
| **terminal** | une fenêtre où l'on **tape des commandes** au lieu de cliquer. Dans IntelliJ, c'est le panneau **Terminal**, en bas |
| **argument** | un mot donné au programme **au moment où on le lance** |
| **IntelliJ** | le logiciel dans lequel tu écris ton code. Il colore le code, souligne les fautes en rouge, et lance les programmes avec un bouton |

---

## Étape 1 — Trouver ton dossier

**📖 Comprendre.** Tout le cours est rangé dans des dossiers, comme des tiroirs. Le code Java est sous `src/main/java/`. Ensuite, un dossier par chapitre, un dossier `projects`, puis un dossier par projet.

**👉 Fais exactement :**
1. Dans IntelliJ, regarde le panneau de **gauche**, appelé **Project**. S'il n'est pas visible, appuie sur **Alt + 1**.
2. Ouvre les dossiers un par un, en cliquant sur la petite flèche **›** devant chacun : `src` → `main` → `java` → `ch1_buildingblocks` → `projects` → `p00_bonjour`.

**👀 Ce que tu dois voir** dans `p00_bonjour` : un dossier `solution`, et les fichiers `Check.java`, `INDICES.md` et `TODO.md` (ce fichier).

**📖 Le nom du paquet.** Le chemin du dossier **après** `src/main/java/` donne le nom du paquet. On remplace simplement les `/` par des points :

```
dossier : src/main/java/  ch1_buildingblocks/projects/p00_bonjour
paquet  :                 ch1_buildingblocks.projects.p00_bonjour
```

**❓ Question :** quel est le nom du paquet du projet 1, `p01_receipt` ?

---

## Étape 2 — Créer la classe `Bonjour`

**👉 Fais exactement :**
1. Fais un **clic droit** sur le dossier `p00_bonjour`.
2. Choisis **New**, puis **Java Class**.
3. Tape `Bonjour`, avec un **B majuscule**, puis appuie sur **Entrée**.

**👀 Ce que tu dois voir :** un nouveau fichier `Bonjour.java` s'ouvre, avec ceci :

```java
package ch1_buildingblocks.projects.p00_bonjour;

public class Bonjour {
}
```

**📖 Comprendre, ligne par ligne :**
- `package ch1_buildingblocks.projects.p00_bonjour;` dit **dans quel dossier** est rangée la classe. IntelliJ l'a écrit pour toi, parce que tu as fait le clic droit sur le bon dossier. Le **point-virgule** `;` termine la phrase, comme un point à la fin d'une phrase en français.
- `public class Bonjour` veut dire : « voici une classe qui s'appelle `Bonjour`, et tout le monde a le droit de l'utiliser » (`public`).
- Les **accolades** `{` et `}` sont comme les deux côtés d'une boîte : tout ce qui appartient à la classe se met **entre les deux**.

**⚠️ La règle du nom :** une classe `public` doit être dans un fichier qui porte **exactement** son nom, majuscules comprises : `Bonjour` va dans `Bonjour.java`. Java fait la différence entre `B` et `b`.

---

## Étape 3 — La porte d'entrée `main`, et ta première ligne

**👉 Fais exactement :** place ton curseur **entre** les deux accolades de la classe, et tape (ou recopie) ceci, pour que ton fichier devienne :

```java
package ch1_buildingblocks.projects.p00_bonjour;

public class Bonjour {

    public static void main(String[] args) {
        System.out.println("Bienvenue dans le cours de Java.");
    }
}
```

**📖 Comprendre la ligne `main`.** Chaque mot a un rôle. Ne t'inquiète pas si tout n'est pas clair : tu les recroiseras souvent.
- `public` : tout le monde peut l'appeler. Java, en particulier, doit pouvoir l'appeler pour lancer ton programme.
- `static` : on peut l'appeler sans fabriquer d'objet `Bonjour` avant. Tu comprendras les objets au projet 1.
- `void` : la méthode **ne rend rien** ; elle fait juste son travail.
- `main` : le nom est **obligatoire**. C'est ce nom que Java cherche pour savoir par où commencer.
- `(String[] args)` : la liste des **mots** donnés au lancement. Tu t'en serviras à l'étape 5.
- Les accolades `{ }` de `main` contiennent les ordres à exécuter, **dans l'ordre, de haut en bas**.

**📖 Comprendre la ligne `println`.**
- `System.out.println(…)` veut dire « écris ceci dans la console, puis va à la ligne ». `println` se lit « print line » : imprime une ligne.
- Le texte est entre **guillemets droits** `"…"`. Les guillemets disent à Java : « ceci est du texte, ne cherche pas à le comprendre ». En Java, un texte s'appelle une **`String`**.
- Le `;` termine l'ordre. **Chaque ordre se termine par un `;`.**

**👉 Lance ton programme :**
1. Dans la marge de gauche de l'éditeur, à côté de la ligne `public class Bonjour`, il y a une **petite flèche verte ▶**. Clique dessus.
2. Choisis **Run 'Bonjour.main()'**.

**👀 Ce que tu dois voir** en bas, dans le panneau **Run** :

```
Bienvenue dans le cours de Java.

Process finished with exit code 0
```

`exit code 0` veut dire : « tout s'est bien passé ». Un autre nombre (souvent `1`) veut dire qu'il y a eu un problème.

**💡 Raccourci :** **Maj + F10** relance le dernier programme lancé.

---

## Étape 4 — Lancer le correcteur `Check`

**📖 Comprendre.** Chaque projet contient un fichier `Check.java`. C'est un **correcteur automatique** :
1. il lance ton programme ;
2. il compare ce que ton programme écrit avec ce qui est attendu, **ligne par ligne, au caractère près** ;
3. il vérifie que tu as utilisé les outils demandés.

**⚠️ Règle d'or :** tu **lances** `Check.java`, mais tu ne le **modifies jamais**.

**👉 Fais exactement :**
1. Double-clique sur `Check.java` dans le panneau de gauche.
2. Clique sur la flèche verte ▶ à côté de `public class Check`, puis sur **Run 'Check.main()'**.

**👀 Ce que tu dois voir** (ton programme n'écrit pas encore tout, donc c'est normal que ça échoue) :

```
=== Verification de ch1_buildingblocks.projects.p00_bonjour.Bonjour ===
[FAIL] sortie : 0/3 lignes justes avant la 1re difference (ligne 1)
       attendu : Bonjour, Marie !
       obtenu  : Bienvenue dans le cours de Java.
[FAIL] API : encore a placer dans ton code : [args[0], args[1]]
```

**📖 Lire la réponse de `Check` :**
- `[PASS]` veut dire « réussi », `[FAIL]` veut dire « pas encore ».
- `attendu` est ce que `Check` voulait lire. `obtenu` est ce que ton programme a écrit. Compare-les **lettre par lettre**, espaces compris.
- `encore a placer dans ton code` donne la liste des outils que tu n'as pas encore utilisés.

Quand tout est juste, la dernière ligne sera :

```
*** PROJET REUSSI : sortie identique et toute l'API pratiquee. ***
```

---

## Étape 5 — Les arguments : donner des mots au programme

**📖 Comprendre.** Quand on lance un programme, on peut lui donner des **mots**, à la suite. Java les range dans `args`, numérotés **à partir de 0** :
- `args[0]` est le **1er** mot ;
- `args[1]` est le **2e** mot.

Pourquoi à partir de 0 ? C'est une habitude des langages de programmation : le numéro dit « combien de cases il faut sauter » depuis le début. Le 1er mot est à 0 case du début.

**📖 Coller des textes avec `+`.** Entre deux textes, `+` les **colle** bout à bout : `"Bon" + "jour"` donne `Bonjour`. Les espaces ne s'ajoutent **pas** tout seuls : il faut les écrire **dans les guillemets**.

**👉 Fais exactement :** ajoute deux lignes **au-dessus** de la ligne `Bienvenue`, pour que `main` devienne :

```java
    public static void main(String[] args) {
        System.out.println("Bonjour, " + args[0] + " !");
        System.out.println("Tu habites a " + args[1] + ".");
        System.out.println("Bienvenue dans le cours de Java.");
    }
```

Remarque l'espace **après la virgule** dans `"Bonjour, "`, et l'espace **avant le point d'exclamation** dans `" !"`. On écrit `a` sans accent : les sorties du cours n'utilisent pas d'accents, pour éviter les problèmes d'affichage dans la console.

**👉 Lance `Bonjour` avec la flèche verte**, comme à l'étape 3.

**👀 Ce que tu dois voir :** une erreur !

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
	at ch1_buildingblocks.projects.p00_bonjour.Bonjour.main(Bonjour.java:6)
```

**📖 Lire cette erreur :**
- `Exception` veut dire que le programme s'est **arrêté en plein milieu**.
- `Index 0 out of bounds for length 0` : tu as demandé la case n° 0, mais la liste contient **0** mot. Tu n'as donné aucun mot au programme !
- `Bonjour.java:6` donne le **fichier** et le **numéro de ligne** du problème. Clique dessus : IntelliJ t'y emmène.

**👉 Donne les mots au programme :**
1. En haut, dans le menu **Run**, choisis **Edit Configurations…**
2. À gauche, clique sur **Bonjour**.
3. Trouve le champ **Program arguments** et tape : `Marie Nantes`. Un espace sépare les deux mots.
4. Clique sur **OK**, puis relance `Bonjour`.

**👀 Ce que tu dois voir :**

```
Bonjour, Marie !
Tu habites a Nantes.
Bienvenue dans le cours de Java.
```

**👉 Relance `Check`.** Tu dois obtenir `*** PROJET REUSSI ***`. `Check` ne lit **pas** tes Program arguments : il donne lui-même `Marie Nantes` à ton programme.

**🧪 Expériences** (dans **Program arguments**, puis relance `Bonjour`) :
1. Donne un seul mot, `Marie`. Que se passe-t-il, et à quelle ligne ?
2. Donne `"Marie Curie" Paris`, **avec** les guillemets. Combien de mots le programme a-t-il reçus ?

Remets ensuite `Marie Nantes`.

---

## Étape 6 — Le terminal : compiler et lancer sans bouton

**📖 Comprendre.** La flèche verte d'IntelliJ fait deux choses en cachette :
1. elle appelle le traducteur `javac` ;
2. puis elle appelle `java` pour exécuter.

À l'examen, on te demande de **taper ces commandes toi-même**. Tu vas donc les taper dans le terminal.

**👉 Ouvre le terminal :** appuie sur **Alt + F12**, ou passe par le menu **View** → **Tool Windows** → **Terminal**. Un panneau s'ouvre en bas, avec une ligne qui se termine par `>` et un curseur qui clignote. C'est là que tu tapes.

**👉 Vérifie où tu es :** tape `pwd`, puis **Entrée**. Tu dois voir un chemin qui se termine par **`Kurse`** : c'est le dossier du cours. **Toutes les commandes du cours se tapent depuis ce dossier.**

### Commande 1 — Traduire avec `javac`

Tape cette ligne en **une seule fois**, puis **Entrée** :

```
javac -d build/p00 src/main/java/ch1_buildingblocks/projects/p00_bonjour/Bonjour.java
```

**📖 Comprendre chaque morceau :**
- `javac` : appelle le traducteur ;
- `-d build/p00` : « range les traductions (`.class`) dans le dossier `build/p00` ». `d` comme *destination*. Le dossier `build` est ignoré par git : c'est ton brouillon ;
- le reste est le **chemin** du fichier à traduire, depuis le dossier `Kurse`.

**💡 Astuce :** tu n'es pas obligé de tout taper. Tape le début d'un nom de dossier, par exemple `src/ma`, puis appuie sur **Tab** : le terminal complète tout seul.

**👀 Ce que tu dois voir :** **rien** ! Une nouvelle ligne `>` apparaît, sans message. Pour `javac`, **pas de message = pas de faute**.

**👉 Regarde la traduction :** tape

```
ls build/p00/ch1_buildingblocks/projects/p00_bonjour
```

Tu dois voir un fichier `Bonjour.class`. `javac` a recréé tout seul les dossiers du paquet sous `build/p00`.

### Commande 2 — Exécuter avec `java`

```
java -cp build/p00 ch1_buildingblocks.projects.p00_bonjour.Bonjour Marie Nantes
```

**📖 Comprendre chaque morceau :**
- `java` : appelle l'exécuteur ;
- `-cp build/p00` : « cherche les `.class` dans le dossier `build/p00` ». `cp` veut dire *classpath*, le chemin des classes ;
- `ch1_buildingblocks.projects.p00_bonjour.Bonjour` : le **nom complet** de la classe, c'est-à-dire le paquet, un point, puis le nom. Avec des **points**, sans `/`, et **sans** `.java` ni `.class` ;
- `Marie Nantes` : les arguments, comme dans **Program arguments**.

**👀 Ce que tu dois voir :** les trois lignes du programme.

**💡 Astuce :** la **flèche du haut ↑** du clavier rappelle la commande précédente. Tu peux alors la modifier, au lieu de tout retaper.

**💡 Les messages en français :** sur ton ordinateur, `java` écrit ses erreurs en allemand. Ajoute `"-Duser.language=fr"` juste après `java`, **avec les guillemets**. Sans les guillemets, le terminal PowerShell coupe ce morceau en deux, et `java` ne comprend plus rien.

```
java "-Duser.language=fr" -cp build/p00 ch1_buildingblocks.projects.p00_bonjour.Bonjour Marie Nantes
```

`javac`, lui, écrit toujours en anglais. Le corrigé traduit chaque message.

**🧪 Expériences** (avec `"-Duser.language=fr"`) : tape chaque commande, lis le message, et essaie de comprendre ce qui manque.
1. Le nom court de la classe : `java "-Duser.language=fr" -cp build/p00 Bonjour Marie Nantes`
2. Aucun argument : la commande 2, sans `Marie Nantes`.

---

## Étape 7 — Les erreurs : les lire sans paniquer

**📖 Comprendre.** Il y a deux sortes d'erreurs :
- les erreurs de **traduction** : `javac` refuse ton fichier, et **rien** ne se lance ;
- les erreurs d'**exécution** : la traduction a marché, mais le programme s'arrête en route. Tu en as vu une à l'étape 5.

Un message de `javac` se lit toujours de la même façon :

```
src\main\java\ch1_buildingblocks\projects\p00_bonjour\Bonjour.java:6: error: ';' expected
        System.out.println("Bonjour, " + args[0] + " !")
                                                        ^
1 error
```

- d'abord le **fichier**, puis `:6:`, le **numéro de ligne** ;
- `error:` puis **le problème**. Ici, `';' expected` veut dire « j'attendais un point-virgule » ;
- en dessous, la ligne fautive, et un **chapeau `^`** qui pointe **l'endroit exact** ;
- à la fin, le nombre d'erreurs.

**⚠️ Important :** tant qu'un fichier du projet contient une faute de traduction, IntelliJ ne peut **rien** lancer, pas même `Check`. Il affiche alors **Build failed**, et la liste des fautes. Corrige d'abord les fautes.

**🧪 Expériences.** Pour chacune :
- fais la modification ;
- regarde ce qu'IntelliJ souligne en **rouge**, et pose la souris sur le soulignement pour lire son message ;
- tape la commande 1 de l'étape 6 (flèche du haut ↑ !) et lis le message de `javac` ;
- **remets le code comme avant** ;
- puis passe à l'expérience suivante.

1. Efface le `;` à la fin de la ligne `Bonjour, …`.
2. Efface les guillemets autour de `Bienvenue dans le cours de Java.`
3. Écris `system` au lieu de `System` (avec un petit `s`).
4. Écris `public class bonjour` (petit `b`) au lieu de `public class Bonjour`.
5. **Celle-ci est spéciale :** efface le mot `static`.
   - Compile avec la commande 1 : est-ce que `javac` se plaint ?
   - Lance avec la commande 2, avec `"-Duser.language=fr"` : que dit `java` ?
   - Regarde la marge d'IntelliJ : la flèche verte est-elle encore là ?

**❓ Question :** d'après l'expérience 5, `javac` et `java` vérifient-ils la même chose ?

---

## Étape 8 — Les commentaires : écrire tes réponses

**📖 Comprendre.** Un **commentaire** est un texte pour les **humains**. `javac` l'ignore complètement. Il y a deux façons d'en écrire :

```java
// Un commentaire sur une seule ligne : tout ce qui suit // jusqu'au bout de la ligne.

/* Un commentaire
   sur plusieurs lignes : tout ce qui est entre slash-étoile et étoile-slash. */
```

**Dans tout le cours, tu écris tes réponses aux questions en commentaire, dans ton code.** Comme ça, tu les retrouves quand tu révises.

**👉 Fais exactement :**
1. Au-dessus de `public static void main`, écris en commentaire tes réponses aux questions des étapes 1 et 7.
2. Relance `Check` : il doit toujours dire `PROJET REUSSI`, car les commentaires ne changent rien au programme.
3. Ouvre [`solution/CORRIGE.md`](solution/CORRIGE.md) et compare tes réponses.

---

## Ce que tu sais faire maintenant

Coche chaque ligne quand tu sais le faire **sans relire ce fichier** :

- [ ] créer une classe dans le bon dossier (clic droit → New → Java Class) ;
- [ ] écrire `main` et une ligne `System.out.println(…);` ;
- [ ] lancer un programme avec la flèche verte, et lire la console ;
- [ ] lancer `Check` et lire `attendu` / `obtenu` ;
- [ ] donner des arguments (Run → Edit Configurations… → Program arguments) ;
- [ ] taper `javac -d …` puis `java -cp …` dans le terminal ;
- [ ] lire un message d'erreur : le fichier, la ligne, le problème, le chapeau `^`.

Tu es prêt pour le **projet 1**, [`p01_receipt/TODO.md`](../p01_receipt/TODO.md).

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `System.out.println(` | 3 | ☐ |
| `args[0]` et `args[1]` | 5 | ☐ |

---

## Sortie attendue complète

Avec les arguments `Marie Nantes` :

```
Bonjour, Marie !
Tu habites a Nantes.
Bienvenue dans le cours de Java.
```
