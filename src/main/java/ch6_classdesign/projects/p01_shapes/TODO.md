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
- Un **tri par insertion** du tableau, par `area()` décroissante. Le tri est stable : à égalité, l'ordre initial reste.
- Puis le total des aires, et la forme au plus grand périmètre. Garde la première en cas d'égalité (`>` strict).

### ☐ Étape 3 — Le type de la référence ou de l'objet

```
Rectangle r = new Square(5) : carre aire=25.0 perimetre=20.0 5.0x5.0 (cote 5.0) | carre ? true | Square
```
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
