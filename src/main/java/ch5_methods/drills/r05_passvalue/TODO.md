# Drill de rappel 5 — Le passage par valeur

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall05`** dans le paquet `ch5_methods.drills.r05_passvalue`.
- Pour chaque défi, **prédis** sur papier avant de lancer.

## Défis

- ☐ **D01.** Deux méthodes :
  - `static void bump(int n)` fait `n++` ;
  - `static int bumped(int n)` rend `n + 1`.
  
  Avec `n = 1` : appelle `bump(n)`, puis `m = bumped(n)`. Affiche n et m.
  → `D01 : 1 2`
- ☐ **D02.** Deux méthodes :
  - une surcharge `static void bump(int[] a)` fait `a[0]++` ;
  - `static void replace(int[] a)` fait `a = new int[] {100}; a[0]++;`.
  
  Avec `a = {1}` : appelle `bump(a)`, puis `replace(a)`.
  → `D02 : 2`
- ☐ **D03.** Deux méthodes :
  - `static void grow(StringBuilder sb)` ajoute `"+"` ;
  - `static void swapRefs(StringBuilder x, StringBuilder y)` échange les deux paramètres.
  
  Avec `x = "x"` et `y = "y"` : appelle `grow(x)`, puis `swapRefs(x, y)`.
  → `D03 : x+ y`
- ☐ **D04.** Deux méthodes :
  - `static void shout(String s)` fait `s = s.toUpperCase()` ;
  - `static String shouted(String s)` rend la majuscule.
  
  Avec `s = "hey"` : appelle les deux.
  → `D04 : hey HEY`
- ☐ **D05.** `static int[] doubled(int[] a)` travaille sur un `clone()` et le rend. Affiche `src = {1, 2, 3}`, puis le résultat.
  → `D05 : [1, 2, 3] [2, 4, 6]`
- ☐ **D06.** `static void fill(int[][] grid, int v)` : `Arrays.fill` sur chaque ligne, dans un for-each. Appelle-la avec `new int[2][2]` et 7.
  → `D06 : [[7, 7], [7, 7]]`
- ☐ **D07.** `int[] alias = src;`, puis `alias[2] = 30;`, puis `bump(alias)`. Affiche `src`.
  → `D07 : [2, 2, 30]`
- ☐ **D08.** `chain = new StringBuilder("a")`, puis `grow` deux fois. Ensuite `StringBuilder other = chain;` puis `other = new StringBuilder("z");`. Affiche `chain` et `other`.
  → `D08 : a++ z`

## Expériences (hors sortie attendue)

1. Écris une méthode qui « échange » deux `Integer`. Pourquoi est-ce impossible en Java ?
2. Dans D06, remplace `Arrays.fill(row, v)` par `row = new int[] {v, v}` : que devient la grille ?
3. Dans D05, retire le `clone()` : que devient `src` ?

## Sortie attendue complète

```
D01 : 1 2
D02 : 2
D03 : x+ y
D04 : hey HEY
D05 : [1, 2, 3] [2, 4, 6]
D06 : [[7, 7], [7, 7]]
D07 : [2, 2, 30]
D08 : a++ z
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Java passe tout par valeur :** le paramètre est une **copie** de l'argument.

| Argument | Ce qui est copié | Modifier l'objet (`a[0]++`, `sb.append`) | Réassigner le paramètre (`a = …`) |
|---|---|---|---|
| primitif | la valeur | — | invisible dehors |
| référence | l'adresse de l'objet | **visible** dehors : l'objet est le même | invisible dehors |
| `String`, `Integer` (immuables) | l'adresse | impossible : toute « modification » crée un nouvel objet | invisible dehors |

**Pour changer la variable de l'appelant :** la méthode **rend** la nouvelle valeur, et l'appelant l'affecte (`x = f(x)`).

**Le piège :** appeler une méthode qui rend une valeur sans l'affecter. Le résultat est perdu.

</details>
