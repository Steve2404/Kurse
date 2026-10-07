# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Located`, `City`, `Route` et `RoutesApp`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des records de test réduits.

---

## Étape 1 — Les villes

**Le code :** [`Located.java`](Located.java) et [`City.java`](City.java).

**Un record qui implémente une interface :** les accesseurs générés `lat()` et `lon()` **implémentent** les méthodes abstraites de `Located`. Le record hérite ensuite de la méthode `default distanceTo` sans rien écrire.

**Le constructeur compact normalise :** `"  paris"`, `"PARIS "` et `" paris"` deviennent tous `Paris`. Comme `equals` et `hashCode` sont **générés à partir des composants** (déjà normalisés), ces villes sont égales et ont le même `hashCode`.

**Question — pourquoi `creees 13` et pas 10 ?** Le compteur est incrémenté dans le constructeur compact, par lequel passe **toute** création. Il y a 10 villes lues, plus `new City("PARIS ", …)` pour `equals`, plus `new City(" paris", …)` pour `hashCode`, plus `new City("nowhere")` (qui passe aussi par le compact, via `this(name, 0, 0)`). Soit **13**. Les objets temporaires créés pour une comparaison comptent aussi.

---

## Étape 2 — Les distances

**Le code :** la construction de `dist` dans le `main` de [`RoutesApp.java`](RoutesApp.java).

**Haversine :** la Terre est (presque) une sphère, et la distance « à vol d'oiseau » suit un arc de grand cercle. La formule reste stable pour les petites distances, contrairement à la loi des cosinus sphérique. Paris-Marseille : **660 km** à vol d'oiseau (environ 775 km par la route).

**La matrice est calculée une seule fois :** les trois algorithmes la relisent des milliers de fois. La recalculer à chaque fois (sinus, cosinus, arcsinus) serait très lent.

---

## Étape 3 — Trois tournées

**Le code :** [`Route.java`](Route.java), et `nearestNeighbour`, `twoOpt` et `permute` dans `RoutesApp`.

**Le plus proche voisin (glouton) :** rapide, en O(n²), mais myope. Il fait ici 2794 km, soit **5 %** de plus que l'optimum.

**2-opt :** si deux arêtes de la tournée se **croisent**, inverser le tronçon entre elles les décroise et raccourcit la tournée. On recommence tant qu'on gagne. Ici, il trouve l'optimum (2666 km), mais ce n'est pas garanti en général : 2-opt s'arrête à un optimum **local**. Le `- 1e-9` évite de boucler à l'infini sur des gains nuls dus aux arrondis des `double`.

**L'optimum par force brute :** Paris reste en tête, et on permute les 9 autres : 9 ! = **362880** tournées. Avec 15 villes, ce serait 14 ! ≈ 87 milliards. C'est la limite de la force brute, d'où l'intérêt des heuristiques.

**Les records utilitaires :**
- `Route` garde ses copies défensives : `order` est un tableau (projet 2).
- `Route.Stats` est un **record imbriqué**, implicitement `static`. On l'écrit `Route.Stats` de l'extérieur. Il sert à **rendre plusieurs valeurs** d'une méthode, sans classe complète à écrire. Son `toString` généré donne `Stats[legs=10, longest=408.0, longestLeg=LIL-STR]`.

**Expériences :**

| Expérience | Erreur de `javac` (vérifiée) |
|---|---|
| `private int visits;` dans `City` | `field declaration must be static` `(consider replacing field with record component)` : l'état d'un record, ce sont **ses composants**, rien d'autre |
| `this.name = name;` dans le constructeur compact | `cannot assign a value to final variable name` : dans le compact, on modifie le **paramètre** `name`. Le champ est affecté **automatiquement à la fin**, et l'affecter soi-même ferait une double affectation |
| `class Capital extends City` | `cannot inherit from final City` : un record est implicitement `final` |
