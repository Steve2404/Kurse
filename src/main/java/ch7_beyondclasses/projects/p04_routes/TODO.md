# Projet 4 — La tournée de livraison (records)

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 7) :** les **records** dans tous leurs détails :
- un record qui **implémente une interface** : ses accesseurs `lat()` et `lon()` implémentent les méthodes abstraites ;
- le **constructeur compact** qui **normalise** les paramètres ;
- un **constructeur supplémentaire** qui délègue avec `this(...)` ;
- un **champ `static`** (le compteur) et des méthodes `static` ou d'instance ;
- un **record imbriqué** (`Route.Stats`) ;
- un accesseur redéfini, pour la copie défensive ;
- `equals`, `hashCode` et `toString` générés ;
- une interface avec une constante et une méthode `default`.

Côté algorithmes :
- la distance de **Haversine** sur le globe ;
- l'heuristique du **plus proche voisin** ;
- l'amélioration **2-opt** ;
- l'**optimum exact** par force brute (9! = 362 880 tournées), pour mesurer l'écart.

**Ce qui est donné :** `Data.java` (10 villes, mal saisies) et `Check.java`.

**Ce que TU crées :** dans le paquet `ch7_beyondclasses.projects.p04_routes` :
- l'interface `Located` ;
- les records `City` et `Route` ;
- **`RoutesApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 7. Pas de collection ni de lambda.

---

## Tableau de bord

### ☐ Étape 1 — Les villes

```
villes : Paris Lyon Marseille Toulouse Bordeaux Nantes Lille Strasbourg Nice Rennes
record : City[name=Paris, lat=48.8566, lon=2.3522] | egal true | hash egal true | City[name=Nowhere, lat=0.0, lon=0.0] | creees 13
```
- **`interface Located`** :
  - `double EARTH_RADIUS_KM = 6371;` ;
  - `double lat();` et `double lon();` ;
  - **`default double distanceTo(Located other)`** (Haversine) : a = sin²(Δφ/2) + cos φ₁ · cos φ₂ · sin²(Δλ/2), puis d = 2R · asin(√a), angles en radians (`Math.toRadians`).
- **`record City(String name, double lat, double lon) implements Located`** :
  - un **constructeur compact** : `strip()`, puis la première lettre en majuscule et le reste en minuscules ; la latitude est bornée à ±90, la longitude à ±180 ; on incrémente `private static int created` ;
  - un **constructeur `City(String name)`** qui fait `this(name, 0, 0)` ;
  - `static City parse(String line)` découpe sur `"\\|"` ;
  - `static int created()` ;
  - `initials()` = les 3 premières lettres en majuscules.
- **La ligne `record` :**
  - le `toString` généré de la première ville ;
  - `equals` avec `new City("PARIS ", 48.8566, 2.3522)` ;
  - l'égalité des `hashCode` avec `new City(" paris", …)` ;
  - `new City("nowhere")` ;
  - `City.created()`.
  - **Question :** pourquoi 13, et non 10 ?

### ☐ Étape 2 — Les distances

```
distances : PAR-MAR 660 km, LIL-MAR 834 km, BOR-STR 758 km
```
- Construis la matrice `dist[i][j] = cities[i].distanceTo(cities[j])`.
- Affiche les distances arrondies (`Math.round`) des paires (0, 2), (6, 2) et (4, 7).

### ☐ Étape 3 — Trois tournées

```
plus proche voisin : PAR-LIL-STR-LYO-MAR-NIC-TOU-BOR-NAN-REN-PAR = 2794 km
apres 2-opt : PAR-LIL-STR-LYO-NIC-MAR-TOU-BOR-NAN-REN-PAR = 2666 km
optimum (362880 tournees) : PAR-LIL-STR-LYO-NIC-MAR-TOU-BOR-NAN-REN-PAR = 2666 km
ecart glouton 5 %, ecart 2-opt 0 % ; Stats[legs=10, longest=408.0, longestLeg=LIL-STR]
```
- **`record Route(int[] order, double km)`** :
  - un constructeur compact qui copie `order`, et l'accesseur `order()` redéfini pour rendre une copie ;
  - `static double length(int[] order, double[][] dist)` : la boucle revient au départ ;
  - `describe(City[])` = `PAR-LIL-…-PAR = 2794 km` ;
  - un **record imbriqué** `record Stats(int legs, double longest, String longestLeg)` ;
  - `stats(…)` trouve l'étape la plus longue (la première en cas d'égalité), avec `longest` arrondi.
- **Le plus proche voisin :** depuis la ville 0, va toujours à la ville non visitée la plus proche (la première en cas d'égalité).
- **2-opt :** tant qu'on gagne, pour i de 1 à n − 2 et k de i + 1 à n − 1 : si `dist[a][c] + dist[b][d] < dist[a][b] + dist[c][d] - 1e-9` (avec a = order[i-1], b = order[i], c = order[k], d = order[(k+1) % n]), inverse le tronçon i..k.
- **L'optimum :** permute les positions 1 à n − 1 par échanges récursifs, et garde la plus courte (strictement meilleure de plus de 1e-9). Compte les tournées évaluées.
- **Les écarts :** `Math.round(100 * (km - optimum) / optimum)`.
- **Expériences :**
  - ajoute un champ d'instance `private int visits;` dans `City` : quelle erreur ?
  - dans le constructeur compact, écris `this.name = name;` : quelle erreur ?
  - `class Capital extends City` : quelle erreur ?

---

## Checklist (vérifiée par `Check`)

- `Data.CITIES` ;
- `interface Located` et `default double distanceTo(` ;
- `record City(`, `implements Located`, `public City {`, `this(name, 0, 0)`, `private static int created` ;
- `record Route(`, `record Stats(`, `public Route {` ;
- `Math.asin(`.

---

## Sortie attendue complète

```
villes : Paris Lyon Marseille Toulouse Bordeaux Nantes Lille Strasbourg Nice Rennes
record : City[name=Paris, lat=48.8566, lon=2.3522] | egal true | hash egal true | City[name=Nowhere, lat=0.0, lon=0.0] | creees 13
distances : PAR-MAR 660 km, LIL-MAR 834 km, BOR-STR 758 km
plus proche voisin : PAR-LIL-STR-LYO-MAR-NIC-TOU-BOR-NAN-REN-PAR = 2794 km
apres 2-opt : PAR-LIL-STR-LYO-NIC-MAR-TOU-BOR-NAN-REN-PAR = 2666 km
optimum (362880 tournees) : PAR-LIL-STR-LYO-NIC-MAR-TOU-BOR-NAN-REN-PAR = 2666 km
ecart glouton 5 %, ecart 2-opt 0 % ; Stats[legs=10, longest=408.0, longestLeg=LIL-STR]
```
