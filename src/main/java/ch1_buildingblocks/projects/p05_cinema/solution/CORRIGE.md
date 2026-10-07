# Projet 5 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier (`app/Cinema.java`, `model/`).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18). Dans les extraits, `P` remplace `ch1_buildingblocks.projects.p05_cinema`.

---

## Étape 1 — Le modèle (paquet `model`)

**La séance (`model/Screening.java`) :**

```java
package P.model;

public class Screening {

    static int step;

    int room = log("Seance : champ salle", 7);                                   // [1]

    {
        note("Seance : bloc d'initialisation (capacite = " + this.capacity + ")"); // [2]
    }

    int capacity = log("Seance : champ capacite", 120);                          // [3]
    String film;
    String version;

    public Screening(String film, String version) {
        this.film = film;
        this.version = version;
        note("Seance : constructeur (film = " + this.film + ")");                // [4]
    }

    static void note(String message) {
        step = step + 1;
        System.out.println("[" + step + "] " + message);
    }

    static int log(String field, int value) {
        note(field + " = " + value);
        return value;
    }

    public String title()           { return film + " (" + version + "), salle " + room; }
    public int seatsLeft(int sold)  { return capacity - sold; }
}
```

**Pourquoi ça produit cet ordre :** les initialiseurs de champs et le bloc s'exécutent **dans l'ordre du texte**, puis le constructeur (voir le projet 3). À la ligne [2], `capacity` n'est pas encore initialisé : `this.capacity` lit sa valeur par défaut, **0**. Le nom simple `capacity` serait refusé (`illegal forward reference`).

**La ligne de tarif (`model/TicketLine.java`) :** trois champs, un constructeur avec `this.`, et quatre méthodes `public` : `count()`, `totalCents()` et `line()`, qui rend `label + count + " x " + Money.euros(unitCents) + " = " + Money.euros(totalCents())`.

**Les montants (`model/Money.java`) :**

```java
public class Money {
    public static String euros(int cents) {
        return cents / 100 + "." + cents / 10 % 10 + cents % 10;
    }
}
```

Une méthode `static` : on l'appelle `Money.euros(...)` sans créer d'objet `Money`.

---

## Étape 2 — Le lanceur (paquet `app`)

**Le code de l'étape :**

```java
package P.app;

import P.model.Money;
import P.model.Screening;
import P.model.TicketLine;

public class Cinema {
    public static void main(String[] args) {
        var screening = new Screening(args[0], args[1]);
        var full = new TicketLine("Plein   : ", Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        var reduced = new TicketLine("Reduit  : ", Integer.valueOf(args[4]).intValue(), Integer.valueOf(args[5]).intValue());
        final int promo = Integer.decode(args[6]);
        boolean loyal = Boolean.parseBoolean(args[7]);
        …
    }
}
```

**Question — quelle méthode comprend `0x`, `#` et `0` ?** **`Integer.decode`**. Elle lit le préfixe pour choisir la base :
- `0x` ou `0X` ou `#` : hexadécimal ;
- `0` : octal ;
- sinon : décimal.

`Integer.decode("0x0F")` rend 15.

`parseInt` ne comprend **aucun** préfixe : `Integer.parseInt("0x0F")` lève une exception.

```
Exception in thread "main" java.lang.NumberFormatException: For input string: "0x0F"
```

**Pourquoi `args[0]` vaut bien `Dune 2` :** le shell découpe les arguments aux espaces, **sauf** entre guillemets. `"Dune 2"` arrive donc en un seul argument, sans les guillemets.

---

## Étape 3 — Le billet

**Le calcul à la main :**

| Ligne | Calcul | Résultat |
|---|---|---|
| Plein | 2 × 1150 | 2300, soit `23.00` |
| Réduit | 3 × 850 | 2550, soit `25.50` |
| Sous-total | 2300 + 2550 | 4850, soit `48.50` |
| Remise | 4850 × 15 / 100 = 72750 / 100 | **727** (division entière), soit `7.27` |
| À payer | 4850 − 727 | 4123, soit `41.23` |
| Places restantes | 120 − (2 + 3) | 115 |
| Code en binaire | 15 = 8 + 4 + 2 + 1 | `1111` |
| Code en octal | 15 = 1 × 8 + 7 | `17` |

**La ligne du code promo :** `"Code promo " + args[6] + " : -" + promo + "% = -" + Money.euros(discount)`. Elle affiche **le texte reçu** (`0x0F`, tel quel) **et** le nombre décodé (`15`).

**Les text blocks :**

```java
static final String HEADER = """
        +==============================+
        |        CINEMA LE PHARE       |
        +==============================+""";

static final String FOOTER = """
             Bonne seance !
        """;
```

- Dans `FOOTER`, le texte commence 5 colonnes **à droite** des `"""` fermants. Java retire l'indentation commune (celle des `"""`), et il reste **5 espaces**.
- Les `"""` fermants sont seuls sur leur ligne, donc le texte finit par un saut de ligne : `System.out.print(FOOTER);`.
- Dans `HEADER`, les `"""` sont collés : pas de saut final, donc `println`.

**Question — `Integer.decode("010")` et `Integer.parseInt("010")` :**
- `Integer.decode("010")` vaut **8** : le `0` de tête signifie **octal**, comme dans un littéral Java (`010` vaut 8, voir le projet 2).
- `Integer.parseInt("010")` vaut **10** : `parseInt` travaille toujours en base 10 (sauf base donnée en 2e argument), et les zéros de tête ne comptent pas.

---

## Étape 4 — Revue finale

**1. Les fichiers :** **4 fichiers `.java`**, **4 classes publiques**, **2 paquets** :

| Fichier | Paquet |
|---|---|
| `app/Cinema.java` | `P.app` |
| `model/Screening.java` | `P.model` |
| `model/TicketLine.java` | `P.model` |
| `model/Money.java` | `P.model` |

- **Le nom du fichier** doit être celui de sa classe `public`. C'est une règle de `javac` : sinon, `class X is public, should be declared in a file named X.java`. D'où **un fichier par classe publique**.
- **Le dossier** doit suivre le paquet (`…/p05_cinema/model/`). Si on lui donne les fichiers explicitement, `javac` compile même s'ils sont mal rangés. Mais les outils qui **cherchent** les sources par leur paquet ne les trouveraient pas : IntelliJ, Maven, `javac -sourcepath`.
- **Les `.class`, eux, doivent obligatoirement** être rangés selon le paquet : c'est ainsi que `java -cp` les trouve. `javac -d` crée cette arborescence tout seul.

**2. La compilation à la main** (depuis la racine du dépôt ; vérifié en direct sur la solution) :

```bash
javac -d build/ch1-p05/classes \
  src/main/java/ch1_buildingblocks/projects/p05_cinema/app/Cinema.java \
  src/main/java/ch1_buildingblocks/projects/p05_cinema/model/*.java

java -cp build/ch1-p05/classes ch1_buildingblocks.projects.p05_cinema.app.Cinema "Dune 2" VO 2 1150 3 850 0x0F true
```

Une variante sans lister les fichiers de `model` : `javac -d build/ch1-p05/classes -sourcepath src/main/java src/main/java/…/app/Cinema.java`. `-sourcepath` dit où chercher les sources des classes importées, et `javac` compile alors aussi les 3 classes de `model`. On obtient 4 `.class`.

**3. Le ramasse-miettes — quels objets sont éligibles à la fin du `main` ?**
- **Pendant** le `main`, les objets créés **sans** être rangés dans une variable sont éligibles dès leur dernière utilisation. Par exemple, les objets `Integer` rendus par `Integer.valueOf(args[4])` et `Integer.valueOf(args[5])` le deviennent juste après `intValue()`. Il en va de même des chaînes intermédiaires des concaténations.
- **À la fin du `main`**, les variables locales `screening`, `full`, `reduced` (et `args`) disparaissent. **Tous** les objets créés deviennent éligibles : la séance, les deux lignes de tarif et leurs chaînes. Les deux text blocks font exception : ce sont des constantes `static` de la classe, encore référencées.
- **Rappel :** éligible ne veut pas dire détruit. Ici, le programme se termine juste après, et la JVM rend toute la mémoire d'un coup, sans forcément lancer le ramasse-miettes.
