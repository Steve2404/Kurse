# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Mode`, `Drivable`, `Sailable`, `Flyable`, `Vehicle`, `Car`, `Amphibian`, `Boat`, `Plane`, `Drone` et `FleetApp`.
>
> Les messages et les exceptions ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18), sur des classes de test réduites (`V`, `D`, `Car`, `Boat`).

---

## Étape 1 — Les capacités

**Le code :** les interfaces, [`Vehicle.java`](Vehicle.java) et les 5 classes.

**Plusieurs interfaces, un seul parent :** une classe n'a qu'**une** classe mère (`extends`), mais peut implémenter **plusieurs** interfaces. `Plane` est un `Vehicle`, et il sait voler **et** rouler.

**`this instanceof Drivable` dans `Vehicle` :** `Vehicle` ne connaît pas ses sous-classes. Mais à l'exécution, `this` est l'objet réel, et `instanceof` le teste. Le cast `((Drivable) this)` n'est fait **qu'après** le test : il est donc toujours sûr.

**`Amphibian` hérite de `Drivable` par `Car`**, et redéfinit `roadSpeed()` (60 au lieu de 90).

**Expérience — `Drivable.super.horn()` dans `Amphibian`** (vérifié) :

```
error: not an enclosing class: D
```

`X.super.m()` n'est permis que pour une interface implémentée **directement** par la classe. `Amphibian` implémente `Sailable` directement, mais `Drivable` seulement **par héritage** de `Car`. On passe donc par `super.horn()`, qui remonte à `Car` (qui en a hérité). Le message de `javac` est trompeur : il confond avec la syntaxe des classes internes.

---

## Étape 2 — Le plus rapide trajet

**Le code :** les méthodes `index` et `fastest` de [`FleetApp.java`](FleetApp.java).

**Le polymorphisme dans Dijkstra :** l'algorithme ne connaît **aucun** type de véhicule. Il demande seulement `v.speedOn(mode)`. Le même code donne cinq résultats différents.

**Les trajets, expliqués :**
- **Clio** roule seulement, et l'Île n'a aucune liaison ROAD : **inaccessible**.
- **Hippo** (route 60, mer 15) : Ville > Village (50 km route, 50 min), puis Phare (15 km route, 15 min), puis Île (30 km mer, 120 min). Total : **185.0 min**. Par le Port, il faudrait 30 min de route, puis 45 km de mer (180 min).
- **Concorde** vole directement Ville > Île : 70 km à 600 km/h, soit **7.0 min**.

**Pourquoi Dijkstra marche :** comme pour le BFS du chapitre 4, on traite les lieux par **temps croissant**. Une fois traité, un lieu a son temps définitif, car tous les temps sont positifs.

---

## Étape 3 — Un objet, plusieurs vues

**Le code :** la fin du `main`.

**Un seul objet, quatre références :** `hippo`, `asCar`, `asVehicle` et `asObject` désignent **le même** `Amphibian` :
- `asCar.roadSpeed()` vaut **60** : redéfini, donc c'est l'objet qui décide (pas 90) ;
- `asVehicle.kind()` vaut `amphibie`, pour la même raison ;
- `asObject instanceof Sailable` vaut `true` : l'objet est un `Sailable`, quelle que soit la variable ;
- `getClass()` vaut `Amphibian` : la classe **réelle**.

**Les casts « sûrs » :** `(Car) v` après `v instanceof Car`. L'Amphibian **est** un `Car` (sous-classe), d'où `Hippo=voiture`.

**Expériences :**

| Expérience | Résultat (vérifié) |
|---|---|
| `(Car) fleet[1]` sans test | **compile**, car un `Vehicle` *pourrait* être un `Car`. À l'exécution, `ClassCastException: class Boat cannot be cast to class Car` |
| `Boat b = (Boat) new Car("x");` | `error: incompatible types: Car cannot be converted to Boat`. `javac` **sait** qu'un `Car` ne peut jamais être un `Boat` : ce sont deux branches distinctes sous `Vehicle` |
| `String s = (String) fleet[0];` | `error: incompatible types: V cannot be converted to String`. `String` est `final` et n'hérite pas de `Vehicle` : aucun objet ne peut être les deux |
| `asVehicle.roadSpeed()` | `error: cannot find symbol` `symbol: method roadSpeed()` `location: variable v of type V`. Le **type de la référence** décide de ce qu'on peut appeler |

**La règle des casts :**
- `javac` refuse un cast **impossible** : deux classes sans lien d'héritage, ou une classe `final` qui n'implémente pas l'interface visée.
- Vers une **interface**, un cast depuis une classe non `final` compile toujours : une sous-classe pourrait l'implémenter.
- Un cast **possible mais faux** passe la compilation et échoue à l'exécution (`ClassCastException`). D'où le test `instanceof` avant.
