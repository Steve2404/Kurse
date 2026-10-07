# Projet 1 — Les formes géométriques

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 6) :**
- **déclarer une sous-classe** (`extends`) et l'**héritage simple** ;
- une **classe abstraite** avec des méthodes abstraites ;
- les **constructeurs** :
  - `super(...)` et `this(...)` en première instruction ;
  - un constructeur `protected` ;
  - une expression dans l'appel à `super` ;
- les **champs `final`** affectés dans le constructeur ;
- **redéfinir** (`@Override`) et appeler la version du parent avec `super.methode()` ;
- une **méthode `final`** qui sert de modèle (*template method*) ;
- `toString()` redéfini ;
- l'objet réel décide de la méthode exécutée : `Rectangle r = new Square(5)`.

Côté algorithmes :
- formule de **Héron** ;
- **aire du lacet** (*shoelace*) ;
- **enveloppe convexe** d'Andrew ;
- **point dans un polygone** par lancer de rayon ;
- tri par insertion d'objets.

**Ce qui est donné :** `Data.java` (des points) et `Check.java`.

**Ce que TU crées :** dans le paquet `ch6_classdesign.projects.p01_shapes` :
- `Shape` (abstraite) ;
- `Circle`, `Rectangle`, `Square`, `Triangle`, `Polygon` ;
- **`ShapesApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 6.
- Pas d'`interface`, de `record` ni d'`enum` (chapitre 7).
- Pas de classe imbriquée ni anonyme (chapitre 7).
- Pas de **cast d'objet** `(Square) r` (chapitre 7) : utilise `instanceof` avec pattern.
- Pas de collection, de lambda ni de `try/catch`.
- Pas de `%f` : arrondis avec `Math.round(x * 100) / 100.0`.

**Ce que le chapitre 6 t'apprend :** à construire des **familles de classes**. Une classe **mère** décrit ce qui est commun, et des classes **filles** ajoutent ou changent ce qui leur est propre. C'est l'**héritage**. Tu vas aussi écrire tes **propres constructeurs**, interdits au chapitre 5.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : des animaux.

**Une classe par fichier.** Mets chaque classe dans son propre fichier : clic droit sur le dossier du projet → **New** → **Java Class**. Les classes **imbriquées** (une classe écrite à l'intérieur d'une autre) sont interdites ici : elles arrivent au chapitre 7.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch6-p01 -sourcepath src/main/java src/main/java/ch6_classdesign/projects/p01_shapes/ShapesApp.java
java "-Duser.language=fr" -cp build/ch6-p01 ch6_classdesign.projects.p01_shapes.ShapesApp
```

---

## Tableau de bord

### ☐ Étape 1 — La hiérarchie

```
cercle aire=12.57 perimetre=12.57 rayon=2.0
cercle aire=3.14 perimetre=6.28 rayon=1.0
rectangle aire=12.0 perimetre=14.0 3.0x4.0
carre aire=9.0 perimetre=12.0 3.0x3.0 (cote 3.0)
triangle aire=6.0 perimetre=12.0
triangle aire=0.0 perimetre=8.0 INVALIDE
```

**📖 La leçon : écrire un constructeur.** Un constructeur porte **le nom de la classe** et n'a **pas** de type rendu. Il s'exécute à chaque `new`, et remplit les champs (chapitre 1, projet 1). Une classe peut en avoir plusieurs (surcharge, chapitre 5). L'un peut **déléguer** à un autre avec `this(…)`, qui doit être la **1re ligne** :

```java
public Chat(String nom) { super(nom); }
public Chat() { this("Minou"); }          // un chat sans nom s'appelle Minou
```

**📖 La leçon : `extends`, la classe fille.** `class Chien extends Animal` : un `Chien` **est un** `Animal`. Il hérite de ses champs et de ses méthodes. Le constructeur de la fille doit d'abord construire la partie « mère », avec **`super(…)`** en **1re ligne**. Si tu ne l'écris pas, Java ajoute tout seul `super()`, sans argument.

**📖 La leçon : `abstract`, une classe incomplète.** Une méthode `abstract` n'a **pas de corps** : chaque fille **doit** l'écrire. Une classe qui a une méthode abstraite est `abstract` : on ne peut pas en faire de `new` directement, seulement de ses filles.

**📖 La leçon : redéfinir une méthode.** Une fille peut **remplacer** une méthode de sa mère, en l'écrivant avec la même signature. On met **`@Override`** devant : `javac` vérifie alors qu'elle remplace bien quelque chose. `super.methode()` appelle la version de la mère. Une méthode **`final`** ne peut pas être redéfinie.

```java
abstract class Animal {
    private static int nes;
    private final String nom;
    protected Animal(String nom) {           // protected : seules les filles l'appellent
        this.nom = nom;
        nes++;
    }
    public abstract String cri();            // pas de corps : chaque animal a le sien
    public final String presente() {         // final : personne ne la change
        return nom + " fait " + cri() + bonus();
    }
    protected String bonus() { return ""; }  // une version par défaut
}

class Chien extends Animal {
    public Chien(String nom) { super(nom); }
    @Override
    public String cri() { return "ouaf"; }
    @Override
    protected String bonus() { return super.bonus() + " (remue la queue)"; }
}

class Chat extends Animal {
    public Chat(String nom) { super(nom); }
    public Chat() { this("Minou"); }
    @Override
    public String cri() { return "miaou"; }
}
```

```java
Animal[] zoo = {new Chien("Rex"), new Chat("Felix")};
for (Animal a : zoo) {
    System.out.println(a.presente());
}
// Rex fait ouaf (remue la queue)
// Felix fait miaou
```

Si tu fais une faute de frappe dans le nom d'une méthode marquée `@Override`, `javac` te prévient : `error: method does not override or implement a method from a supertype`.

**👉 À toi :**

- **`Shape`** (`abstract`) :
  - `private static int created` et un champ `private final String name` ;
  - un constructeur **`protected Shape(String name)`**, qui incrémente `created` ;
  - `public abstract double area()` et `public abstract double perimeter()` ;
  - **`public final String describe()`**, qui rend `nom aire=… perimetre=…` suivi de `extra()`. Les valeurs sont arrondies par `protected static double r2(double)` ;
  - `protected String extra()`, qui rend `""` par défaut ;
  - `public static int created()`, `getName()`, et `toString()` qui rend `describe()`.
- **`Circle(double radius)`** appelle `super("cercle")`. **`Circle()`** délègue avec `this(1)`. Son `extra()` ajoute ` rayon=…`.
- **`Rectangle`** a deux constructeurs :
  - `public Rectangle(double w, double h)`, qui délègue avec `this("rectangle", w, h)` ;
  - `protected Rectangle(String name, double w, double h)`.
  - Ses champs sont `protected final`. Son `extra()` ajoute ` 3.0x4.0`.
- **`Square extends Rectangle`** : `Square(double side)` appelle `super("carre", side, side)`. Son `extra()` rend `super.extra() + " (cote …)"`. Il **ne réécrit pas** `area()` : il en hérite.
- **`Triangle(a, b, c)`** utilise la formule de Héron.
  - Si l'inégalité triangulaire échoue, l'aire vaut 0 et `extra()` rend ` INVALIDE`. Les exceptions sont au chapitre 11.
- Dans `main` : un `Shape[]` = `{new Circle(2), new Circle(), new Rectangle(3, 4), new Square(3), new Triangle(3, 4, 5), new Triangle(1, 2, 5)}`. Affiche `describe()` de chacun.
- **Expériences :**
  - `new Shape("x")` ;
  - redéfinir `describe()` dans `Circle` ;
  - mettre `this(1)` en deuxième ligne de `Circle()` ;
  - retirer `super("cercle")` (quelle erreur, puisque `Shape` n'a pas de constructeur sans argument ?).

### ☐ Étape 2 — Trier des objets par leur méthode

```
par aire : cercle rectangle carre triangle cercle triangle
aire totale 42.71, plus grand perimetre : rectangle
```

**📖 Rappel :** le tri par insertion (chapitre 4, projet 7, étape 1) marche sur un tableau d'objets : on compare `t[j].area()` au lieu de `t[j]`.

**👉 À toi :**

- Un **tri par insertion** du tableau, par `area()` décroissante. Le tri est stable : à égalité, l'ordre initial reste.
- Puis le total des aires, et la forme au plus grand périmètre. Garde la première en cas d'égalité (`>` strict).

### ☐ Étape 3 — Le type de la référence ou de l'objet

```
Rectangle r = new Square(5) : carre aire=25.0 perimetre=20.0 5.0x5.0 (cote 5.0) | carre ? true | Square
```

**📖 La leçon : le polymorphisme, c'est l'objet qui décide.** Une variable de type `Animal` peut désigner un `Chat`. Deux règles :
- **ce qu'on a le droit d'appeler** dépend du type de la **variable** (`Animal` : seulement les méthodes d'`Animal`) ;
- **la version qui s'exécute** dépend de l'**objet** réel (le `Chat` répond `miaou`).

```java
Animal a = new Chat("Tom");
a.cri()                         // "miaou" : c'est un Chat
a instanceof Chat               // true
a.getClass().getSimpleName()    // "Chat" : le nom de la classe de l'OBJET
```

**👉 À toi :**

- La référence est de type `Rectangle`, l'objet est un `Square`.
- Affiche, dans cet ordre :
  - `r` (son `toString`) ;
  - `r instanceof Square` ;
  - `r.getClass().getSimpleName()`.
- **Question :** pourquoi `extra()` est-il celui de `Square`, alors que la variable est un `Rectangle` ?

### ☐ Étape 4 — L'enveloppe convexe et le point dans le polygone

```
enveloppe : (0,0)(4,0)(5,1)(4,3)(2,4)(0,3) -> polygone(6) aire=15.5 perimetre=15.12
dedans : (2.0,2.0)=true (5.0,3.0)=false (1.0,3.4)=true (-1.0,1.0)=false
formes creees : 8
```

**📖 La leçon : copier pour se protéger.** Si un constructeur range directement un tableau reçu, l'appelant garde une étiquette sur **le même** tableau et peut le modifier en douce. Avec `clone()`, l'objet garde sa propre copie (chapitre 4, projet 4).

**📖 Conseil :** pour l'enveloppe convexe, place les points sur du papier quadrillé et fais tourner un élastique autour : les sommets touchés forment l'enveloppe. Puis déroule l'algorithme à la main sur 5 points.

**👉 À toi :**

- **`Polygon(double[][] points)`** :
  - appelle `super("polygone(" + points.length + ")")` ;
  - **copie** chaque point (`clone()`).
  - L'aire vient de la formule du lacet : la moitié de |Σ (xᵢ·yᵢ₊₁ − xᵢ₊₁·yᵢ)|. Le périmètre utilise `Math.hypot`.
- **`public static Polygon convexHull(double[][] input)`** (Andrew) :
  1. copie, puis trie les points par x, puis par y (insertion) ;
  2. construis la chaîne **inférieure** : pour chaque point, retire le dernier sommet tant que le virage n'est pas à gauche (produit vectoriel `<= 0`), puis ajoute le point ;
  3. construis la chaîne **supérieure** de la même façon, en parcourant à l'envers ;
  4. le dernier point répète le premier : ne le garde pas.
  - Le produit vectoriel est une méthode `private static double cross(o, a, b)`.
- **`vertices()`** affiche les sommets en entiers : `(0,0)(4,0)…`.
- **`contains(x, y)`**, par lancer de rayon : pour chaque arête (i, j) qui **croise** l'horizontale y, si x est à gauche du point de croisement, inverse `inside`.
- Teste chaque point de `Data.TESTS`. La dernière ligne lit `Shape.created()` : 6 formes, plus le `Square(5)`, plus l'enveloppe.

---

## Checklist (vérifiée par `Check`)

- `Data.POINTS` et `Data.TESTS` ;
- `abstract class Shape`, `protected Shape(`, `public abstract double area()`, `public final String describe()`, `protected String extra()` ;
- 4 `extends Shape` et `extends Rectangle` ;
- `super(`, `this(`, `super.extra()`, `@Override` ;
- `instanceof Square`, `getSimpleName()`, `clone()`.

---

## Sortie attendue complète

```
cercle aire=12.57 perimetre=12.57 rayon=2.0
cercle aire=3.14 perimetre=6.28 rayon=1.0
rectangle aire=12.0 perimetre=14.0 3.0x4.0
carre aire=9.0 perimetre=12.0 3.0x3.0 (cote 3.0)
triangle aire=6.0 perimetre=12.0
triangle aire=0.0 perimetre=8.0 INVALIDE
par aire : cercle rectangle carre triangle cercle triangle
aire totale 42.71, plus grand perimetre : rectangle
Rectangle r = new Square(5) : carre aire=25.0 perimetre=20.0 5.0x5.0 (cote 5.0) | carre ? true | Square
enveloppe : (0,0)(4,0)(5,1)(4,3)(2,4)(0,3) -> polygone(6) aire=15.5 perimetre=15.12
dedans : (2.0,2.0)=true (5.0,3.0)=false (1.0,3.4)=true (-1.0,1.0)=false
formes creees : 8
```
