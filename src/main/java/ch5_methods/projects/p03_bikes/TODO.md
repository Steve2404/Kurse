# Projet 3 — Les vélos en libre-service (le monde `static`)

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 5) :**
- **membres de classe ou d'instance** : ce qui est partagé (`static`) et ce qui appartient à chaque objet ;
- l'**ordre d'initialisation** :
  - les champs `static` et les blocs `static { }`, dans l'ordre du fichier, **au premier usage** de la classe ;
  - le bloc `static` de la classe du `main`, **avant** `main` ;
  - les champs d'instance et le bloc `{ }` d'instance, à **chaque** `new` ;
- `static final` affecté dans un bloc `static` ; `final` d'instance affecté dans un bloc d'instance ;
- **appeler un membre `static` via une référence `null`** ;
- l'**import static** (`java.lang.Math.min` et ta propre classe).

Côté algorithmes : les plus courts chemins de **Floyd-Warshall**, la station la plus proche, un rééquilibrage **glouton**.

**Ce qui est donné :** `Data.java` (stations, routes, trajets) et `Check.java`.

**Ce que TU crées :** dans le paquet `ch5_methods.projects.p03_bikes`, trois classes :
- **`Network`** : uniquement des membres `static` ;
- **`Station`** : des objets, et un registre `static` ;
- **`BikeApp`** : le `main`.

**Règle du crescendo :** chapitres 1 à 5. Pas de constructeur écrit par toi : `Station.create(…)` est une fabrique `static`. Pas de collection.

---

## Tableau de bord

### ☐ Étape 1 — Tracer l'ordre de chargement

```
[charge] BikeApp
main commence
[charge] Network : champ NAMES
[charge] Network : bloc static 1 (5 stations)
[charge] Network : bloc static 2 (Floyd-Warshall, 12 raccourcis trouves)
Gare -> Parc : 5 km, diametre 9 km, voisin le plus proche du Parc 3 km
```
- **`BikeApp`** a un bloc `static` qui affiche `[charge] BikeApp`. Il s'exécute **avant** la première ligne de `main`.
- **`Network`**, dans cet ordre exact du fichier :
  1. `static final String[] NAMES = load();` : la méthode `private static` `load()` affiche sa trace et rend une copie de `Data.STATIONS` ;
  2. `static final int SIZE;` **sans valeur**, puis un **bloc static 1** qui l'affecte et affiche la trace ;
  3. `static final int[][] DIST = new int[SIZE][SIZE];` ;
  4. un **bloc static 2** qui exécute Floyd-Warshall et compte les raccourcis trouvés.
- **Floyd-Warshall :**
  - initialise `DIST` à 0 sur la diagonale et à `INF = 1_000` ailleurs ;
  - remplis les routes de `Data.ROADS`, dans les deux sens (découpe avec `split("[- ]")`) ;
  - pour chaque k, puis i, puis j : si passer par k est plus court, mets à jour (et compte).
- La ligne `Gare -> Parc` appelle `km(index("Gare"), index("Parc"))`, `diameter()` et `closest(2)` (le Parc).
- Les traces de `Network` apparaissent **après** `main commence` : la classe n'est chargée qu'à son premier usage.
- Méthodes `static` à écrire : `index(String)`, `km(int, int)`, `diameter()` (la plus grande distance) et `closest(int)` (la plus petite distance vers une autre station). `closest` utilise `min`, importé avec `import static java.lang.Math.min;`.
- **Expériences :**
  - déplace le bloc static 1 **avant** la déclaration de `NAMES` : que dit `javac` (*illegal forward reference*) ?
  - supprime l'affectation de `SIZE` : quelle erreur ?

### ☐ Étape 2 — Des objets avec un registre partagé

```
[objet] station #0
[objet] station #1
[objet] station #2
[objet] station #3
[objet] station #4
depart : Gare=6/8 Centre=1/6 Parc=0/5 Port=3/6 Campus=8/10
```
- **`Station`** :
  - champs d'instance : `final int id`, `String name`, `int bikes`, `int capacity` ;
  - champs `static` : `private static final Station[] ALL = new Station[10]` et `private static int created`.
- **Un bloc d'instance `{ … }`** affecte `id = created++` et affiche la trace. Il s'exécute à chaque `new Station()`.
- **`static Station create(String, int, int)`** : `new Station()`, remplir les champs, enregistrer dans `ALL[id]`.
- `static int count()`, `static Station get(int)`, `static String snapshot()`.
- **Questions :**
  - pourquoi `id` peut-il être `final` alors qu'on ne l'affecte pas sur sa ligne ?
  - une méthode `static` peut-elle lire `name` directement, sans référence ?

### ☐ Étape 3 — Les trajets

```
Parc->Gare : vide, marche vers Centre (3 km) 2 km
Centre->Port : vide, marche vers Gare (2 km) 9 km
Centre->Parc : vide, marche vers Gare (2 km) 5 km
Campus->Port : 2 km
Campus->Port : 2 km
Campus->Port : plein, depot a Campus (+2 km a pied) 0 km
Gare->Port : plein, depot a Campus (+2 km a pied) 7 km
Gare->Port : plein, depot a Campus (+2 km a pied) 7 km
arrivee : Gare=3/8 Centre=0/6 Parc=1/5 Port=6/6 Campus=8/10
km a velo 34, km a pied 13
```
- `static Station nearest(Station from, boolean needBike)` : la station la plus proche **par la route** (`Network.km`) qui a un vélo (ou une place libre), autre que `from`. À égalité de distance, la première trouvée (indice le plus petit) gagne.
- Dans `BikeApp`, une méthode `private static String ride(Station from, Station to)` :
  - si le départ est vide, on marche vers la station la plus proche qui a un vélo ;
  - si l'arrivée est pleine, on dépose à la station la plus proche qui a une place, puis on marche.
  - `ride` met à jour deux compteurs `private static int walked, ridden` de `BikeApp`.
- `km` est importé en static depuis `Network` : `import static …Network.km;`.

### ☐ Étape 4 — Le camion et le `static` via `null`

```
camion : 1 Port->Parc 3 Campus->Centre 1 Port->Gare (18 km) => Gare=4/8 Centre=3/6 Parc=2/5 Port=4/6 Campus=5/10
static via null : 5 stations, Campus (#4 sur 5)
```
- **Le rééquilibrage glouton.** Une station a un **surplus** si `bikes > capacity / 2`, un **manque** si `bikes < capacity / 2`.
  - Tant qu'il existe un couple (surplus, manque), prends le plus proche. Déplace `min(surplus, manque)` vélos et additionne les km.
  - En cas d'égalité de distance, le premier couple trouvé (boucle a, puis b) gagne.
- **Le `static` via `null`** : `Station nothing = null;`, puis `nothing.count()`.
  - Ça marche : Java ne regarde que le **type déclaré** pour un membre `static`. Aucune erreur, et `javac` ne dit rien par défaut : seul `javac -Xlint:static` (ou IntelliJ) signale `[static] static method should be qualified by type name`.
  - **Question :** et `nothing.describe()`, une méthode d'instance ?
- `describe()` est une méthode **d'instance** : elle lit `name`, `id` et le `static` `created`. La dernière ligne l'appelle sur `Station.get(4)`.

---

## Checklist (vérifiée par `Check`)

- `Data.ROADS` et `Data.TRIPS` ;
- deux blocs `static { }`, un `static final int`, un bloc `{ }` d'objet, un champ `final int` d'objet ;
- `import static` ;
- une variable de référence initialisée à `null` ;
- la relaxation de Floyd-Warshall (`[i][k] + …[k][j]`) ;
- un registre `private static …[]`.

---

## Sortie attendue complète

```
[charge] BikeApp
main commence
[charge] Network : champ NAMES
[charge] Network : bloc static 1 (5 stations)
[charge] Network : bloc static 2 (Floyd-Warshall, 12 raccourcis trouves)
Gare -> Parc : 5 km, diametre 9 km, voisin le plus proche du Parc 3 km
[objet] station #0
[objet] station #1
[objet] station #2
[objet] station #3
[objet] station #4
depart : Gare=6/8 Centre=1/6 Parc=0/5 Port=3/6 Campus=8/10
Parc->Gare : vide, marche vers Centre (3 km) 2 km
Centre->Port : vide, marche vers Gare (2 km) 9 km
Centre->Parc : vide, marche vers Gare (2 km) 5 km
Campus->Port : 2 km
Campus->Port : 2 km
Campus->Port : plein, depot a Campus (+2 km a pied) 0 km
Gare->Port : plein, depot a Campus (+2 km a pied) 7 km
Gare->Port : plein, depot a Campus (+2 km a pied) 7 km
arrivee : Gare=3/8 Centre=0/6 Parc=1/5 Port=6/6 Campus=8/10
km a velo 34, km a pied 13
camion : 1 Port->Parc 3 Campus->Centre 1 Port->Gare (18 km) => Gare=4/8 Centre=3/6 Parc=2/5 Port=4/6 Campus=5/10
static via null : 5 stations, Campus (#4 sur 5)
```
