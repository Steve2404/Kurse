# Drill de rappel 1 — L'interface `Collection`

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 8 min, puis 4 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall01`** dans le paquet `ch9_collections.drills.r01_collection`.
- Toutes les variables sont typées par l'interface **`Collection<…>`**.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 et 2) et projet 2 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_collection` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `Collection<String> c = new ArrayList<>();`.
  1. `added = c.add("java")` ;
  2. `c.add("map")` ;
  3. `c.add("java")`.
  
  Affiche `added`, `size()`, `isEmpty()`, `contains("map")`, puis `c`.
  → `D01 : true 3 false true [java, map, java]`
- ☐ **D02.** `Collection<String> set = new HashSet<>();`. Ajoute `"java"` deux fois, en gardant les deux résultats de `add`. Affiche-les, puis la taille.
  → `D02 : true false 1`
- ☐ **D03.** Sur `c` : `c.remove("java")`, puis `c.remove("python")`. Affiche les deux résultats, puis `c`.
  → `D03 : true false [map, java]`
- ☐ **D04.** `Collection<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6))`.
  1. `numbers.removeIf(n -> n % 2 == 0)` (garde le résultat) ;
  2. `numbers.forEach(…)` ajoute `n;` dans un `StringBuilder`.
  
  Affiche le résultat de `removeIf`, `numbers`, puis le `StringBuilder`.
  → `D04 : true [1, 3, 5] 1;3;5;`
- ☐ **D05.** `a = new ArrayList<>(List.of("x", "y", "z"))` et `b = List.of("y", "z", "w")`.
  1. `a.addAll(b)` ;
  2. `retained = a.retainAll(List.of("y", "w"))`.
  
  Affiche `a`, `retained`, puis `a.containsAll(List.of("y", "w"))`.
  → `D05 : [y, y, w] true true`
- ☐ **D06.** `c.clear()`. Affiche :
  - `c.isEmpty()` ;
  - `new ArrayList<>(List.of(1, 2)).equals(List.of(1, 2))` ;
  - `new HashSet<>(List.of(1, 2)).equals(List.of(1, 2))`.
  → `D06 : true true false`

## Expériences (hors sortie attendue)

1. Pourquoi `retainAll` garde-t-il **deux** `y` ?
2. Une `List` peut-elle être `equals` à un `Set` qui contient les mêmes éléments ?
3. Dans une boucle for-each sur `c`, appelle `c.remove(x)`. Que se passe-t-il ?

## Sortie attendue complète

```
D01 : true 3 false true [java, map, java]
D02 : true false 1
D03 : true false [map, java]
D04 : true [1, 3, 5] 1;3;5;
D05 : [y, y, w] true true
D06 : true true false
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Méthode de `Collection<E>` | Rend |
|---|---|
| `add(E)` | `boolean` : `false` si un `Set` contient déjà l'élément |
| `remove(Object)` | `boolean` : retire la **première** occurrence |
| `isEmpty()`, `size()`, `clear()`, `contains(Object)` | — |
| `addAll`, `removeAll`, `retainAll`, `containsAll` | `boolean` : vrai si la collection a changé (sauf `containsAll`) |
| `removeIf(Predicate)` | `boolean` |
| `forEach(Consumer)` | `void` |

**`equals` :** une `List` n'est égale qu'à une `List` (mêmes éléments, même ordre), et un `Set` qu'à un `Set`.

**`Map` n'étend pas `Collection`.**

</details>
