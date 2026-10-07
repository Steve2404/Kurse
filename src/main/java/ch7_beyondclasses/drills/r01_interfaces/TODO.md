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

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_interfaces` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
