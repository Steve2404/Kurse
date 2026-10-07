# Drill de rappel 5 — Classes et interfaces scellées

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall05.java`, paquet `ch7_beyondclasses.drills.r05_sealed`. Sous `Recall05` :

| Type | Déclaration |
|---|---|
| `Vehicle` | `sealed abstract class Vehicle permits Car, Truck, Bike` |
| `Car` | `final class Car extends Vehicle` |
| `Truck` | `non-sealed class Truck extends Vehicle` |
| `Bike` | `sealed class Bike extends Vehicle permits EBike` |
| `EBike` | `final class EBike extends Bike` |
| `Lorry` | `class Lorry extends Truck` |
| `Shape` | `sealed interface Shape permits Circle, Square` |
| `Circle`, `Square` | `record Circle(double r) implements Shape`, `record Square(double side) implements Shape` |
| `Animal` | `sealed interface Animal` **sans permits**, avec `String sound();` |
| `Dog`, `Cat` | `final class Dog implements Animal` (`ouaf`), `final class Cat implements Animal` (`miaou`) |
| `Fuel` | `sealed interface Fuel permits Electric, Liquid` |
| `Electric` | `non-sealed interface Electric extends Fuel`, avec `default String plug()` qui rend `prise` |
| `Liquid` | `sealed interface Liquid extends Fuel permits Diesel` |
| `Diesel`, `Tesla` | `record Diesel(int litres) implements Liquid` ; `class Tesla implements Electric` |

- Dans `Recall05` :
  - `static String describe(Vehicle v)` : une chaîne d'`instanceof`, du plus précis au plus général (`EBike` avant `Bike`) ;
  - `static double area(Shape s)` : `instanceof` avec pattern.

**Les notions de ce drill ont été apprises dans :** projet 3 (étape 1) et projet 7 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r05_sealed` → **New** → **Java Class** → `Recall05`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall05`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall05`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `describe` de `{new Car(), new Truck(), new Bike(), new EBike(), new Lorry()}`.
  → `D01 : voiture camion velo velo electrique camion`
- ☐ **D02.** `area(new Circle(1))`, arrondie à 2 décimales, puis `area(new Square(3))`.
  → `D02 : 3.14 9.0`
- ☐ **D03.** Cinq résultats :
  - `Vehicle.class.isSealed()` et `Vehicle.class.getPermittedSubclasses().length` ;
  - `Truck.class.isSealed()` et `Bike.class.isSealed()` ;
  - `Animal.class.getPermittedSubclasses().length`.
  → `D03 : true 3 false true 2`
- ☐ **D04.** `java.lang.reflect.Modifier.isFinal(...)` sur les modificateurs de `Circle.class`, puis de `Car.class`.
  → `D04 : true true`
- ☐ **D05.** `new Lorry() instanceof Truck`, puis `new Dog().sound()` et `new Cat().sound()`.
  → `D05 : true ouaf miaou`
- ☐ **D06.** `Fuel f = new Diesel(40);`. Affiche :
  - `new Tesla().plug()` ;
  - `Fuel.class.getPermittedSubclasses().length` ;
  - `f instanceof Liquid` ;
  - `Electric.class.isSealed()` et `Liquid.class.isSealed()`.
  → `D06 : prise 2 true false true`

## Expériences (hors sortie attendue)

1. Retire `final` de `Car` : quelle erreur ?
2. `class Scooter extends Vehicle` (hors `permits`) : quelle erreur ? Et `class Moto extends Lorry` ?
3. Retire `EBike` de la liste `permits` de `Bike` : quelle erreur ?
4. `sealed class X` sans aucune sous-classe : quelle erreur ?
5. Pourquoi `Animal` peut-il omettre `permits` ? Que se passerait-il si `Dog` était dans un autre fichier ?
6. Retire `non-sealed` de `Electric` : quelle erreur ? (Une sous-interface d'une interface scellée doit être `sealed` ou `non-sealed`, jamais `final`.)

## Sortie attendue complète

```
D01 : voiture camion velo velo electrique camion
D02 : 3.14 9.0
D03 : true 3 false true 2
D04 : true true
D05 : true ouaf miaou
D06 : prise 2 true false true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Déclarer :**
- `sealed class X permits A, B` ou `sealed interface X permits A, B` ;
- les sous-types autorisés doivent être dans le **même paquet** (ou le même module nommé) ;
- `permits` peut être omis si les sous-types sont dans le **même fichier**.

**Chaque sous-type direct doit être :**
- `final` : fin de la lignée (les records et les enums le sont) ;
- `sealed` : une nouvelle liste `permits` ;
- `non-sealed` : la lignée se rouvre à tous.

**Une interface scellée** est implémentée par des classes ou des records, et peut aussi être **étendue** par des interfaces `sealed` ou `non-sealed`.

**L'intérêt :** la liste des sous-types est connue et fermée. Une chaîne d'`instanceof` couvre tous les cas.

</details>
