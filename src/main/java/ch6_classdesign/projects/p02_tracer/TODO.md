# Projet 2 — Le traceur d'initialisation

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 6) :** l'**ordre exact** d'initialisation sur une hiérarchie de 3 classes :
1. **la classe**, une seule fois, du parent vers l'enfant : champs `static` et blocs `static`, dans l'ordre du fichier ;
2. **l'objet**, à chaque `new`, du parent vers l'enfant : pour chaque niveau, les champs d'instance et les blocs `{ }`, **puis** le reste du constructeur ;
3. `this(...)` et `super(...)` : qui appelle qui ;
4. le **piège** : une méthode redéfinie appelée **depuis le constructeur du parent** voit les champs de l'enfant **pas encore initialisés** ;
5. une **constante de compilation** ne charge pas sa classe ; un champ `static` hérité ne charge que la classe qui le **déclare** ;
6. les **champs `final`** d'instance, affectés dans le constructeur.

**Ce qui est donné :** `Check.java` seulement.

**Ce que TU crées :** dans le paquet `ch6_classdesign.projects.p02_tracer` :
- `Tracer` (le journal) ;
- `Vehicle` (abstraite), `Car extends Vehicle`, `ElectricCar extends Car` ;
- **`TracerApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 6.

**Méthode :** pour chaque étape, **écris d'abord sur papier** l'ordre des lignes que tu attends, puis compare.

---

## Tableau de bord

### ☐ Étape 1 — Le journal et les classes

- **`Tracer`** :
  - un compteur `private static int step` ;
  - `public static void log(String what)` affiche le numéro sur 2 chiffres (`01`, `02`…), un espace, puis `what` ;
  - `public static int log(String what, int v)` journalise `what = v` et **rend v**. Elle sert à initialiser un champ : `int wheels = Tracer.log("…", 4);`.
- **`Vehicle`** (`abstract`), dans cet ordre :
  1. `static int count = Tracer.log("[static] Vehicle.count", 0);` ;
  2. un bloc `static` → `[static] Vehicle bloc` ;
  3. `protected int wheels = Tracer.log("[objet] Vehicle.wheels", 4);` ;
  4. `private final int id;` et `protected final String name;` ;
  5. un bloc d'instance → `[objet] Vehicle bloc` ;
  6. `protected Vehicle(String name)` : affecte `name`, puis `id = ++count`, journalise `[ctor] Vehicle(String) id=…`, puis `[ctor] Vehicle voit label() = ` suivi de `label()` ;
  7. `protected String label()` rend `vehicule <nom>`.
- **`Car extends Vehicle`** :
  - un bloc `static` → `[static] Car bloc` ;
  - `protected int seats;` et un bloc d'instance → `[objet] Car bloc` ;
  - `public Car(String name)` délègue avec `this(name, 5)`, puis journalise `[ctor] Car(String)` ;
  - `public Car(String name, int seats)` appelle `super(name)`, affecte `seats`, puis journalise `[ctor] Car(String,int) seats=…` ;
  - `label()` est redéfini : `voiture <nom> <seats> places`.
- **`ElectricCar extends Car`** :
  - `public static final String KIND = "EV";` (une constante de compilation) ;
  - `static int built = Tracer.log("[static] ElectricCar.built", 0);` et un bloc `static` ;
  - `private int battery = Tracer.log("[objet] ElectricCar.battery", 50);` ;
  - `ElectricCar(String name, int battery)` : `super(name, 4)`, affecte `battery`, `built++`, journalise ;
  - `ElectricCar()` délègue avec `this("Anonyme", 100)`, puis journalise ;
  - `label()` est redéfini : `electrique <nom> batterie <battery>`.
- **`TracerApp`** a un bloc `static` → `[static] TracerApp bloc`.

### ☐ Étape 2 — Ce qui charge une classe, ou pas

```
01 [static] TracerApp bloc
02 main : KIND = EV (aucune classe chargee)
03 [static] Vehicle.count = 0
04 [static] Vehicle bloc
05 main : Car.count = 0 (Vehicle charge, pas Car)
```
- Dans `main`, journalise :
  - d'abord `main : KIND = ` + `ElectricCar.KIND` + ` (aucune classe chargee)` ;
  - puis `main : Car.count = ` + `Car.count` + ` (Vehicle charge, pas Car)`.
- **Questions :**
  - pourquoi lire `KIND` ne déclenche-t-il rien ? Et si `KIND` n'était pas `final` ?
  - `count` est déclaré dans `Vehicle` : quelle classe `Car.count` charge-t-il ?

### ☐ Étape 3 — Construire, trois fois

```
06 main : new ElectricCar("Zoe", 300)
07 [static] Car bloc
...
13 [ctor] Vehicle voit label() = electrique Zoe batterie 0
...
18 main : apres construction label() = electrique Zoe batterie 300
```
- Avant chaque construction, journalise `main : ` suivi de l'expression : `new ElectricCar("Zoe", 300)`, puis `new ElectricCar()`, puis `new Car("Clio")`.
- Après la première, journalise `main : apres construction label() = ` + `zoe.label()`.
- **Le piège de la ligne 13 :** `label()` de `ElectricCar` s'exécute pendant le constructeur de `Vehicle`. `battery` vaut alors **0**, ni 50 ni 300. Pourquoi ?
- Ligne finale : `main : ids … … …, electriques …, roues …`. Elle lit les trois `getId()`, `ElectricCar.built` et `clio.wheels`.
- **Expériences :**
  - appelle `label()` dans le constructeur de `Car` : que voit-on pour `Car("Clio")` ?
  - mets `id = ++count;` dans un seul des chemins d'un constructeur : quelle erreur (champ `final`) ?

---

## Checklist (vérifiée par `Check`)

- 3 blocs `static { }` et un bloc `{ }` d'objet ;
- `extends Vehicle` et `extends Car` ;
- `this(` et `super(` ;
- `static final String KIND` et `private final int id` ;
- `protected String label()` et `@Override` ;
- `abstract class Vehicle`.

---

## Sortie attendue complète

```
01 [static] TracerApp bloc
02 main : KIND = EV (aucune classe chargee)
03 [static] Vehicle.count = 0
04 [static] Vehicle bloc
05 main : Car.count = 0 (Vehicle charge, pas Car)
06 main : new ElectricCar("Zoe", 300)
07 [static] Car bloc
08 [static] ElectricCar.built = 0
09 [static] ElectricCar bloc
10 [objet] Vehicle.wheels = 4
11 [objet] Vehicle bloc
12 [ctor] Vehicle(String) id=1
13 [ctor] Vehicle voit label() = electrique Zoe batterie 0
14 [objet] Car bloc
15 [ctor] Car(String,int) seats=4
16 [objet] ElectricCar.battery = 50
17 [ctor] ElectricCar(String,int) battery=300
18 main : apres construction label() = electrique Zoe batterie 300
19 main : new ElectricCar()
20 [objet] Vehicle.wheels = 4
21 [objet] Vehicle bloc
22 [ctor] Vehicle(String) id=2
23 [ctor] Vehicle voit label() = electrique Anonyme batterie 0
24 [objet] Car bloc
25 [ctor] Car(String,int) seats=4
26 [objet] ElectricCar.battery = 50
27 [ctor] ElectricCar(String,int) battery=100
28 [ctor] ElectricCar()
29 main : new Car("Clio")
30 [objet] Vehicle.wheels = 4
31 [objet] Vehicle bloc
32 [ctor] Vehicle(String) id=3
33 [ctor] Vehicle voit label() = voiture Clio 0 places
34 [objet] Car bloc
35 [ctor] Car(String,int) seats=5
36 [ctor] Car(String)
37 main : ids 1 2 3, electriques 2, roues 4
```
