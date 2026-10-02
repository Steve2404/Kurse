# Drill de rappel 1 — Les interfaces

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall01.java`, paquet `ch7_beyondclasses.drills.r01_interfaces`. Sous `Recall01`, déclare :

| Type | Contenu |
|---|---|
| `interface Shape` | `int UNIT = 1;` ; `double area();` ; `String name();` |
| `interface Named` | `String name();` (même signature que dans `Shape`) |
| `interface Polygon extends Shape` | `int MAX_SIDES = 12;` ; `int sides();` |
| `interface Colored` | `String color();` |
| `abstract class Base implements Polygon, Named` | elle ne fournit que `name()`, qui rend `"polygone a " + sides() + " cotes"` |
| `class Square extends Base implements Colored` | un côté ; `area()`, `sides()` (4), `color()` (`rouge`) |
| `class Circle implements Shape` | un rayon ; `area()` = π r² ; `name()` rend `cercle` |

## Défis

- ☐ **D01.** `Square sq = new Square(3)`. Affiche :
  - `Shape.UNIT` et `Square.UNIT` ;
  - `sq.area()`, `sq.sides()` et `sq.name()`.
  → `D01 : 1 1 9.0 4 polygone a 4 cotes`
- ☐ **D02.** `Shape[] shapes = {sq, new Circle(1)}`. Pour chaque forme, affiche `name=aire`, l'aire arrondie à 2 décimales.
  → `D02 : polygone a 4 cotes=9.0 cercle=3.14`
- ☐ **D03.** `Object circle = shapes[1]`. Affiche :
  - `sq instanceof Polygon` et `sq instanceof Colored` ;
  - `circle instanceof Polygon` et `circle instanceof Named`.
  → `D03 : true true false false`
- ☐ **D04.** `Colored c = sq;`. Affiche `c.color()`, `((Shape) c).area()` et `((Named) c).name()`.
  → `D04 : rouge 9.0 polygone a 4 cotes`
- ☐ **D05.** `new Circle(2).name()`, puis `Polygon.MAX_SIDES`.
  → `D05 : cercle 12`

## Expériences (hors sortie attendue)

1. Dans `Square`, implémente `area()` **sans** `public` : quelle erreur ?
2. Dans `Shape`, écris `int UNIT;` (sans valeur) : quelle erreur ?
3. `Shape.UNIT = 2;` dans `main` : quelle erreur ?
4. `new Shape()` : quelle erreur ?
5. `interface Polygon implements Shape` : que dit `javac` ?
6. Ajoute `int name();` à `Named` : quelle erreur dans `Base` ? (Même nom, mêmes paramètres, mais un retour incompatible.)
7. Dans `Shape`, essaie tour à tour `protected double area();`, `final double area();`, `private double area();` (sans corps) et un constructeur `Shape() {}` : quelles erreurs ?

## Sortie attendue complète

```
D01 : 1 1 9.0 4 polygone a 4 cotes
D02 : polygone a 4 cotes=9.0 cercle=3.14
D03 : true true false false
D04 : rouge 9.0 polygone a 4 cotes
D05 : cercle 12
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les modificateurs implicites :**
- un champ d'interface est `public static final`, et doit être initialisé ;
- une méthode sans corps est `public abstract` ;
- l'interface elle-même est `abstract`.

**L'héritage :**
- une interface **étend** (`extends`) une ou **plusieurs** interfaces ;
- une classe **implémente** (`implements`) plusieurs interfaces, et n'étend qu'une classe ;
- l'ordre est `class A extends B implements C, D`.

**L'implémentation :**
- obligatoirement `public` (on ne réduit pas l'accès) ;
- une classe abstraite peut en laisser une partie non implémentée ;
- deux méthodes abstraites de même signature venant de deux interfaces : **une** implémentation suffit, si les types de retour sont compatibles.

**Les casts vers une interface :** ils compilent pour toute classe non `final`, et échouent à l'exécution si l'objet ne l'implémente pas.

</details>
