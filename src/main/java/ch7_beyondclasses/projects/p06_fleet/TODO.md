# Projet 6 — La flotte multimodale (polymorphisme et casts)

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 7) :** le **polymorphisme** :
- **un seul objet, plusieurs types de référence** (`Amphibian`, `Car`, `Vehicle`, `Object`, `Sailable`) ;
- le type de la **référence** décide de ce qu'on peut appeler ;
- le type de l'**objet** décide de la version exécutée ;
- le **cast** :
  - upcast implicite ;
  - downcast explicite, **sûr seulement après `instanceof`** ;
  - cast vers une interface (qui compile pour toute classe non `final`) ;
- les **tableaux de types interface** (`Flyable[]`) ;
- une classe qui étend une classe **et** implémente une interface (`Amphibian extends Car implements Sailable`) ;
- `super.horn()` : la version **héritée** de `Car`, elle-même une `default` de `Drivable`.

Côté algorithmes : **Dijkstra** en O(n²), avec un temps qui dépend des capacités de chaque véhicule (route, mer, air).

**Ce qui est donné :** `Data.java` (lieux et liaisons) et `Check.java`.

**Ce que TU crées :** dans le paquet `ch7_beyondclasses.projects.p06_fleet` :
- l'enum `Mode` ;
- les interfaces `Drivable`, `Sailable`, `Flyable` ;
- `Vehicle` (abstraite), `Car`, `Amphibian`, `Boat`, `Plane`, `Drone` ;
- **`FleetApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 7. Pas de `try/catch` : on ne provoque jamais de `ClassCastException`, on teste avant.

---

## Tableau de bord

### ☐ Étape 1 — Les capacités

- **`enum Mode { ROAD, SEA, AIR }`**.
- **Les interfaces :**
  - `Drivable` : `int roadSpeed()` et `default String horn()`, qui rend `tut` ;
  - `Sailable` : `int seaSpeed()` ;
  - `Flyable` : `int airSpeed()` et `default int altitude()`, qui rend 1000.
- **`Vehicle`** (`abstract`) :
  - `protected final String name` ;
  - `abstract String kind()` ;
  - **`speedOn(Mode)`**, un `switch` expression. Chaque cas teste `this instanceof Drivable` (ou `Sailable`, `Flyable`) **puis** fait le cast `((Drivable) this).roadSpeed()` (ou l'équivalent). Sinon, 0 ;
  - `capabilities()` = `ROAD@90 …`, pour les modes de vitesse non nulle, dans l'ordre de `values()` ;
  - `toString()` = `kind name`.
- **Les classes :**

| Classe | Déclaration | `kind()` | Vitesses |
|---|---|---|---|
| `Car` | `extends Vehicle implements Drivable` | `voiture` | route 90 |
| `Amphibian` | `extends Car implements Sailable` | `amphibie` | route 60 (redéfinie), mer 15 ; `horn()` = `"pouet-" + super.horn()` |
| `Boat` | `extends Vehicle implements Sailable` | `bateau` | mer 40 |
| `Plane` | `extends Vehicle implements Flyable, Drivable` | `avion` | air 600, route 30 |
| `Drone` | `extends Vehicle implements Flyable` | `drone` | air 80 ; `altitude()` redéfinie à 120 |

- **Expérience :** dans `Amphibian`, écris `Drivable.super.horn()`. Pourquoi est-ce refusé, alors que `Car` implémente `Drivable` ?

### ☐ Étape 2 — Le plus rapide trajet

```
voiture Clio [ROAD@90] : inaccessible
bateau Nautilus [SEA@40] : inaccessible
amphibie Hippo [ROAD@60 SEA@15] : Ville > Village > Phare > Ile en 185.0 min
avion Concorde [ROAD@30 AIR@600] : Ville > Ile en 7.0 min
drone Bzz [AIR@80] : Ville > Ile en 52.5 min
```
- La flotte : `{new Car("Clio"), new Boat("Nautilus"), new Amphibian("Hippo"), new Plane("Concorde"), new Drone("Bzz")}`, dans un **`Vehicle[]`**.
- **`static String fastest(Vehicle v)`**, Dijkstra de `Data.FROM` à `Data.TO` :
  - `time[]` part à `Double.MAX_VALUE`, et `prev[]` à −1 ;
  - à chaque tour, prends le lieu non traité de plus petit temps (le premier en cas d'égalité) ;
  - relâche chaque liaison qui le touche, si `v.speedOn(Mode.valueOf(mode)) > 0`. Le temps d'une liaison est `km * 60.0 / vitesse` minutes ;
  - le chemin se reconstruit avec `prev`, sous la forme `A > B > C en X min`, le temps arrondi à 0,1 ;
  - si la destination n'est jamais atteinte, rends `inaccessible`.

### ☐ Étape 3 — Un objet, plusieurs vues

```
un seul objet : 60 amphibie pouet-tut true 15 Amphibian
altitudes : 1000 120 ; klaxons : tut pouet-tut tut (3 roulants)
casts surs : Clio=voiture Nautilus=non Hippo=voiture Concorde=non Bzz=non
```
- **Ligne 1 :** `Amphibian hippo = (Amphibian) fleet[2];` (downcast), `Car asCar = hippo;`, `Vehicle asVehicle = hippo;`, `Object asObject = hippo;`. Affiche :
  - `asCar.roadSpeed()` ;
  - `asVehicle.kind()` ;
  - `asCar.horn()` ;
  - `asObject instanceof Sailable` ;
  - `((Sailable) asObject).seaSpeed()` ;
  - `asObject.getClass().getSimpleName()`.
- **Ligne 2 :** remplis un `Flyable[2]` et un `Drivable[5]` avec `instanceof` et pattern. Affiche les altitudes, les klaxons, puis le nombre de véhicules qui roulent.
- **Ligne 3 :** pour chaque véhicule, `if (v instanceof Car)`, puis `Car car = (Car) v;` et `name=voiture` ; sinon `name=non`.
- **Expériences :**
  - `(Car) fleet[1]` sans test : ça compile ? Que se passe-t-il à l'exécution ?
  - `Boat b = (Boat) new Car("x");` : pourquoi `javac` refuse-t-il ?
  - `String s = (String) fleet[0];` : quelle erreur ?
  - `asVehicle.roadSpeed()` : quelle erreur ? Le type de la référence ne la connaît pas.

---

## Checklist (vérifiée par `Check`)

- `Data.LINKS` et `Data.FROM` ;
- `enum Mode`, `interface Drivable`, `interface Sailable`, `interface Flyable` ;
- `extends Car implements Sailable` et `implements Flyable, Drivable` ;
- les 3 casts `((Drivable) this)`, `((Sailable) this)`, `((Flyable) this)` ;
- `(Amphibian) fleet[2]` et `(Sailable) asObject` ;
- `Flyable[]`, `Drivable[]`, `super.horn()`, `Mode.valueOf(`.

---

## Sortie attendue complète

```
voiture Clio [ROAD@90] : inaccessible
bateau Nautilus [SEA@40] : inaccessible
amphibie Hippo [ROAD@60 SEA@15] : Ville > Village > Phare > Ile en 185.0 min
avion Concorde [ROAD@30 AIR@600] : Ville > Ile en 7.0 min
drone Bzz [AIR@80] : Ville > Ile en 52.5 min
un seul objet : 60 amphibie pouet-tut true 15 Amphibian
altitudes : 1000 120 ; klaxons : tut pouet-tut tut (3 roulants)
casts surs : Clio=voiture Nautilus=non Hippo=voiture Concorde=non Bzz=non
```
