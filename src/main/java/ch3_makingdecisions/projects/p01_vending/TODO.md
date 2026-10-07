# Projet 1 — Le distributeur automatique

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 3) :**
- `if` / `else if` / `else` ;
- le **`switch` instruction**, classique (`case …:` avec `break`, et le « fall-through ») et en flèche (`case … ->`) ;
- le **`switch` expression**, avec **`yield`** ;
- `case` à **plusieurs valeurs** ;
- le `switch` sur un **`String`** ;
- la boucle **`for` à indice** qui consomme des arguments ;
- la boucle **`while`**.

**Ce qui est donné :** `Check.java`. Les commandes arrivent par les arguments.

**Ce que TU crées :** tout le programme, dans le paquet `ch3_makingdecisions.projects.p01_vending`. La classe du `main` s'appelle **`Vending`**.

**Règle du crescendo :** chapitres 1 à 3. Pas de méthode de `String` (même pas `equals` : c'est le `switch` qui compare les textes), pas de tableau créé par toi, pas de collection.

**Ce que le chapitre 3 t'apprend :** jusqu'ici, ton programme exécutait ses lignes **une seule fois, de haut en bas**, et choisissait avec des ternaires. Maintenant, il va pouvoir :
- **choisir** quel bloc de lignes exécuter : `if`, `switch` ;
- **répéter** des lignes : les boucles `for`, `while` et `do/while`.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet. Les gestes de base sont ceux du **projet 0 du chapitre 1**.

**Tes outils pour ce projet :**
- **Arguments dans IntelliJ :** ils sont longs. Copie-les depuis `Check.java` (la ligne `ARGS`) en enlevant les guillemets et les virgules, ou recopie ceci dans Run → Edit Configurations… → **Vending** → Program arguments :

```
PIECE 200 PIECE 30 CHOIX D9 CHOIX A1 CHOIX B2 PIECE 100 CHOIX B2 HAPPY CHOIX C3 PIECE 50 PIECE 50 PIECE 20 CHOIX C3 CHOIX C3 STOCK RENDU PIECE 200 PIECE 100 PIECE 50 PIECE 20 PIECE 10 RENDU TICKET
```

- **Terminal** (depuis `Kurse`) : `java` reçoit les mêmes arguments à la suite.

```
javac -d build/ch3-p01 src/main/java/ch3_makingdecisions/projects/p01_vending/Vending.java
java "-Duser.language=fr" -cp build/ch3-p01 ch3_makingdecisions.projects.p01_vending.Vending PIECE 200 CHOIX A1 RENDU
```

(Ici avec quelques arguments seulement, pour essayer.)

---

## Le problème

Un distributeur reçoit une suite de commandes, données en arguments, **l'une après l'autre** :

| Commande | Effet |
|---|---|
| `PIECE <centimes>` | insère une pièce : 10, 20, 50, 100 ou 200 ; toute autre valeur est **refusée** |
| `CHOIX <code>` | achète un produit : `A1` Eau 1.20, `B2` Café 1.50, `C3` Chips 1.80 (**1.50 pendant la happy hour**) |
| `HAPPY` | active ou désactive la happy hour |
| `STOCK` | affiche les stocks (au départ : A1 = 2, B2 = 1, C3 = 1) |
| `RENDU` | rend le crédit avec **le moins de pièces possible** |
| autre | `Commande inconnue : …` |

**Le refus d'un achat :**
1. produit inconnu ;
2. sinon produit épuisé ;
3. sinon crédit insuffisant (avec le montant manquant).

Les arguments de `Check` enchaînent 33 commandes : lis-les en haut de `Check.java`.

---

## Tableau de bord

### ☐ Étape 1 — La boucle de commandes

**📖 La leçon : `if`, exécuter un bloc seulement si…** Le ternaire choisit une **valeur** ; `if` choisit des **lignes** à exécuter :

```java
int t = 25;
if (t > 30) {
    System.out.println("canicule");
} else if (t > 20) {                  // testé seulement si la 1re condition était fausse
    System.out.println("beau temps");  // <- c'est ce bloc qui s'exécute
} else {                              // si aucune condition n'était vraie
    System.out.println("frais");
}
```

**📖 La leçon : la boucle `for` à indice, répéter en comptant.** Elle a trois parties, séparées par des `;` :

```java
for (int i = 1; i <= 3; i++) {        // départ ; condition pour continuer ; ce qu'on fait après chaque tour
    System.out.println("tour " + i);  // tour 1, tour 2, tour 3
}
```

`args.length` donne le **nombre** d'arguments. Pour parcourir tous les arguments : `for (int i = 0; i < args.length; i++)`, et l'argument courant est `args[i]`.

**📖 La leçon : le `switch` en flèche, choisir selon une valeur.** Plus lisible qu'une longue chaîne de `if` quand on compare une même valeur à plusieurs constantes. Il marche aussi sur un **texte** :

```java
// lancé avec : AJOUTE 3 RETIRE 1 AJOUTE 10 AFFICHE
int total = 0;
for (int i = 0; i < args.length; i++) {
    switch (args[i]) {
        case "AJOUTE" -> total = total + Integer.parseInt(args[++i]);   // ++i : passe à l'argument suivant ET le lit
        case "RETIRE" -> total = total - Integer.parseInt(args[++i]);
        case "AFFICHE" -> System.out.println("total = " + total);        // total = 12
        default -> System.out.println("inconnu : " + args[i]);           // aucune flèche ne correspond
    }
}
```

`args[++i]` utilise ce que tu as appris au chapitre 2 : `++i` augmente `i` **avant** de rendre sa valeur. La commande « mange » ainsi l'argument qui la suit, et la boucle ne le traitera pas une 2e fois.

**👉 À toi :**

- Parcours `args` avec une boucle `for` **à indice**.
- `PIECE` et `CHOIX` **consomment** aussi l'argument suivant.
  - **Question :** pourquoi un for-each ne convient-il pas ici ?
  - Écris l'accès au paramètre avec un **pré-incrément** de l'indice, dans l'appel lui-même.
- Choisis le traitement avec un `switch` **sur le texte de la commande**.

### ☐ Étape 2 — Les pièces

```
PIECE 2.00 -> credit 2.00
PIECE 30 refusee
```

**📖 La leçon : un `case` pour plusieurs valeurs.** On sépare les valeurs par des virgules :

```java
switch (jour) {
    case 6, 7 -> System.out.println("week-end");
    default -> System.out.println("semaine");
}
```

Les valeurs d'un `case` doivent être des **constantes** : des littéraux comme `6` ou `"AJOUTE"`, ou des variables `final` initialisées avec une constante.

**👉 À toi :**

- Un `case` à **plusieurs valeurs** accepte les 5 pièces valides ; `default` refuse le reste.
- **Question :** que dit `javac` si deux `case` portent la même valeur ? Et si une valeur n'est pas une **constante** ?

### ☐ Étape 3 — Le catalogue : des `switch` expressions

**📖 La leçon : le `switch` qui rend une valeur.** Un `switch` peut **calculer** une valeur, comme un ternaire à plusieurs branches. On le met après `return`, ou à droite d'un `=`, et il se termine par `;` :

```java
static String saison(int mois) {
    return switch (mois) {
        case 12, 1, 2 -> "hiver";
        case 3, 4, 5 -> "printemps";
        case 6, 7, 8 -> {                 // une branche peut être un bloc de plusieurs lignes…
            String s = "ete";
            yield s + " !";               // …qui rend sa valeur avec yield
        }
        default -> "automne";
    };
}
```

`saison(1)` vaut `hiver`, `saison(7)` vaut `ete !` et `saison(10)` vaut `automne`.

**📖 La leçon : le `switch` classique, avec `:` et `break`.** C'est l'ancienne forme, très présente à l'examen. Java saute au `case` qui correspond, puis exécute **tout ce qui suit**, jusqu'à un `break` ou la fin. Sans `break`, il « tombe » dans les `case` suivants : c'est le **fall-through**.

```java
int etage = 2;
switch (etage) {
    case 3:
        System.out.println("passe au 3");
    case 2:
        System.out.println("passe au 2");    // on entre ici…
    case 1:
        System.out.println("passe au 1");    // …et on continue ici, faute de break
        break;                              // on sort
    default:
        System.out.println("rez-de-chaussee");
}
```

affiche `passe au 2` puis `passe au 1`.

**👉 À toi :**

- **Le prix, le nom et le stock d'un code** : écris trois méthodes, chacune réduite à **un** `return switch (…) { … };`.
- **Le prix de `C3`** dépend de la happy hour : sa branche est un **bloc** `{ … }` qui rend sa valeur avec **`yield`**.
  - **Questions :**
    - pourquoi un `switch` expression **doit-il** avoir un `default` ici ?
    - que se passe-t-il si une branche ne rend rien ?
- **Retirer un produit du stock :**
  - **Contrainte :** écris-le avec le `switch` **classique** (`case "A1":` … `break;` … `default:`).
  - **Expérience :** retire un `break`. Achète un A1 et regarde les stocks. C'est le **fall-through**.

### ☐ Étape 4 — L'achat

```
CHOIX D9 -> produit inconnu
CHOIX A1 -> Eau servi (1.20), reste 0.80
CHOIX B2 -> credit insuffisant (manque 0.70)
CHOIX C3 -> Chips epuise
```

**📖 Rappel :** la chaîne `if` / `else if` / `else` (étape 1) s'arrête à la **première** condition vraie. L'ordre des tests compte donc : ici, c'est l'ordre des refus.

**👉 À toi :**

- Une chaîne `if` / `else if` / `else`, dans l'ordre des refus.

### ☐ Étape 5 — Le rendu de monnaie : l'algorithme glouton

```
RENDU -> 5 piece(s) : 200 100 50 20 10
RENDU -> rien a rendre
```

**📖 La leçon : `while`, répéter tant que…** On ne sait pas d'avance combien de tours il faudra : on répète **tant que** la condition est vraie.

```java
int argent = 100;
int jours = 0;
while (argent > 0) {            // la condition est testée AVANT chaque tour
    argent = argent - 30;
    jours++;
}
System.out.println(jours + " jours, reste " + argent);   // 4 jours, reste -20
```

**⚠️** Si rien ne change dans la boucle, la condition reste vraie **pour toujours** : le programme ne s'arrête plus. Dans IntelliJ, le bouton carré rouge ■ du panneau Run l'arrête.

**👉 À toi :**

- Tant qu'il reste du crédit, rends la **plus grosse** pièce possible. Quand elle est trop grosse, passe à la pièce **inférieure**.
- **Contrainte :** pas de tableau de pièces. La « pièce inférieure » est donnée par un `switch` **expression** (200 → 100 → 50 → 20 → 10 → 0).
- **Calcule à la main** le rendu de 3.80. Pourquoi l'algorithme glouton est-il optimal avec ces pièces-là, mais pas avec n'importe quelles pièces ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `args[++i]` | 1 | ☐ |
| `switch` sur un `String` | 1 | ☐ |
| `case` à plusieurs valeurs | 2 | ☐ |
| `switch` expression affectée, `yield` | 3, 5 | ☐ |
| `switch` classique : `break;`, `default:` | 3 | ☐ |
| `else if` | 4 | ☐ |
| `while (` | 5 | ☐ |

---

## Sortie attendue complète

```
PIECE 2.00 -> credit 2.00
PIECE 30 refusee
CHOIX D9 -> produit inconnu
CHOIX A1 -> Eau servi (1.20), reste 0.80
CHOIX B2 -> credit insuffisant (manque 0.70)
PIECE 1.00 -> credit 1.80
CHOIX B2 -> Cafe servi (1.50), reste 0.30
HAPPY HOUR on
CHOIX C3 -> credit insuffisant (manque 1.20)
PIECE 0.50 -> credit 0.80
PIECE 0.50 -> credit 1.30
PIECE 0.20 -> credit 1.50
CHOIX C3 -> Chips servi (1.50), reste 0.00
CHOIX C3 -> Chips epuise
STOCK A1=1 B2=0 C3=0
RENDU -> rien a rendre
PIECE 2.00 -> credit 2.00
PIECE 1.00 -> credit 3.00
PIECE 0.50 -> credit 3.50
PIECE 0.20 -> credit 3.70
PIECE 0.10 -> credit 3.80
RENDU -> 5 piece(s) : 200 100 50 20 10
Commande inconnue : TICKET
```
