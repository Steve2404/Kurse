# Drill de rappel 3 — `Set`, `TreeSet`, `NavigableSet`

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall03`** dans le paquet `ch9_collections.drills.r03_set`.
- Dans le **même fichier**, crée deux types package-private :
  - une classe **`Spot`** avec deux champs `final int x, y` et un constructeur, **sans** `equals` ni `hashCode` ;
  - un record **`Place(int x, int y)`**.

## Défis

- ☐ **D01.** `source = List.of("delta", "alpha", "charlie", "alpha", "bravo")`. Construis à partir d'elle un `HashSet`, un `LinkedHashSet` et un `TreeSet`.

  Affiche la taille du `HashSet`, le `LinkedHashSet`, puis le `TreeSet`.
  → `D01 : 4 [delta, alpha, charlie, bravo] [alpha, bravo, charlie, delta]`
- ☐ **D02.** `NavigableSet<Integer> n = new TreeSet<>(List.of(10, 5, 20, 15, 30))`. Affiche :
  - `first()`, `last()` ;
  - `floor(12)`, `ceiling(12)` ;
  - `lower(10)`, `higher(30)`.
  → `D02 : 5 30 10 15 5 null`
- ☐ **D03.** Affiche :
  - `headSet(15)`, `tailSet(15)`, `subSet(5, 20)` ;
  - `headSet(15, true)` ;
  - `descendingSet()`.
  → `D03 : [5, 10] [15, 20, 30] [5, 10, 15] [5, 10, 15] [30, 20, 15, 10, 5]`
- ☐ **D04.** `polled = n.pollFirst()`. Affiche `polled`, `n.pollLast()`, puis `n`.
  → `D04 : 5 30 [10, 15, 20]`
- ☐ **D05.** `a = new TreeSet<>(Set.of(1, 2, 3, 4))` et `b = Set.of(3, 4, 5)`. Calcule l'**union**, l'**intersection** et la **différence** a − b, chacune dans une **copie** `TreeSet` de `a`.
  → `D05 : [1, 2, 3, 4, 5] [3, 4] [1, 2]`
- ☐ **D06.** Un `TreeSet<String>` avec le comparateur `(x, y) -> x.length() - y.length()`. Ajoute-lui `List.of("ab", "cd", "efg", "h")`.

  Affiche l'ensemble, sa taille, puis `contains("zz")`.
  → `D06 : [h, ab, efg] 3 true`
- ☐ **D07.** Le contrat `equals`/`hashCode` :
  - `Set<Spot> spots = new HashSet<>(List.of(new Spot(1, 2), new Spot(1, 2)))` ;
  - la même chose avec deux `Place(1, 2)`.

  Affiche :
  - les deux tailles ;
  - `new Spot(1, 2).equals(new Spot(1, 2))` ;
  - `places.contains(new Place(1, 2))`.
  → `D07 : 2 1 false true`

## Expériences (hors sortie attendue)

1. Pourquoi `contains("zz")` rend-il `true` alors que `"zz"` n'a jamais été ajouté ?
2. `new TreeSet<Object>().add(new Object())` : quelle exception ?
3. `new TreeSet<String>().add(null)` contre `new HashSet<String>().add(null)`.
4. Redéfinis seulement `equals` dans `Spot` (sans `hashCode`) : que vaut la taille de `spots` ? Pourquoi est-ce « probablement 2 » ?

## Sortie attendue complète

```
D01 : 4 [delta, alpha, charlie, bravo] [alpha, bravo, charlie, delta]
D02 : 5 30 10 15 5 null
D03 : [5, 10] [15, 20, 30] [5, 10, 15] [5, 10, 15] [30, 20, 15, 10, 5]
D04 : 5 30 [10, 15, 20]
D05 : [1, 2, 3, 4, 5] [3, 4] [1, 2]
D06 : [h, ab, efg] 3 true
D07 : 2 1 false true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Classe | Ordre | `null` |
|---|---|---|
| `HashSet` | aucun | permis (un seul) |
| `LinkedHashSet` | d'insertion | permis |
| `TreeSet` | trié (`Comparable` ou `Comparator`) | interdit (NPE) |

**`NavigableSet`** :
- `lower` (<), `floor` (≤), `ceiling` (≥), `higher` (>) rendent `null` s'il n'y a rien ;
- `headSet(x)` exclut x, `tailSet(x)` l'inclut, `subSet(a, b)` va de a inclus à b exclu. Chacune a une version avec deux booléens ;
- ces ensembles sont des **vues**.

**Le piège :** un `TreeSet` utilise `compare`, et non `equals`. `compare == 0` signifie un doublon.

**Le contrat de `HashSet` et `HashMap` :**
- `hashCode` choisit le compartiment, puis `equals` tranche ;
- deux objets `equals` **doivent** avoir le même `hashCode` ;
- sans redéfinition, c'est l'identité : `new Spot(1, 2)` n'est égal qu'à lui-même ;
- un record génère `equals` et `hashCode` à partir de ses composants.

</details>
