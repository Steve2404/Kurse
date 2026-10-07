# Drill de rappel 2 — `List`, `ListIterator`, `LinkedList`

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall02`** dans le paquet `ch9_collections.drills.r02_list`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r02_list` → **New** → **Java Class** → `Recall02`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall02`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall02`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `List<String> list = new ArrayList<>()`.
  1. `add("b")`, `add(0, "a")`, `add("c")` ;
  2. `old = list.set(1, "B")`.
  
  Affiche `list`, `old`, `get(2)`, `indexOf("c")`, puis `indexOf("z")`.
  → `D01 : [a, B, c] b c 2 -1`
- ☐ **D02.** `nums = new ArrayList<>(List.of(10, 20, 30, 20))`.
  1. `nums.remove(1)` ;
  2. `nums.remove(Integer.valueOf(20))`.
  
  Affiche `nums`, puis `lastIndexOf(30)`.
  → `D02 : [10, 30] 1`
- ☐ **D03.** `words = new ArrayList<>(List.of("pomme", "kiwi", "banane"))`.
  1. `replaceAll(String::toUpperCase)` ;
  2. `sort(null)`.
  → `D03 : [BANANE, KIWI, POMME]`
- ☐ **D04.** `big = new ArrayList<>(List.of(1, 2, 3, 4, 5))`, puis `view = big.subList(1, 4)`.
  1. `view.set(0, 20)` ;
  2. `view.clear()`.
  
  Affiche `big`.
  → `D04 : [1, 5]`
- ☐ **D05.** Un `ListIterator<String>` sur `new ArrayList<>(List.of("a", "b", "c"))`.
  1. **Vers l'avant :** ajoute `nextIndex()`, puis `next()` ;
  2. **Vers l'arrière :** ajoute `previous()`.
  → `D05 : 0a1b2ccba`
- ☐ **D06.** Deux mini-exercices :
  - `toClean = new ArrayList<>(List.of(1, 2, 3, 4))` : avec un `Iterator`, retire les impairs (`i.remove()`) ;
  - `linked = new LinkedList<>(List.of("m"))` : `addFirst("d")`, puis `addLast("f")`.
  
  Affiche `toClean`, `linked`, `getFirst() + getLast()`, `removeFirst()`, puis `linked`.
  → `D06 : [2, 4] [d, m, f] df d [m, f]`

## Expériences (hors sortie attendue)

1. Après `view.clear()`, modifie `big` (avec `big.add(9)`), puis lis `view.size()`. Quelle exception ?
2. `new ArrayList<>(List.of(1)).get(1)` : quelle exception ?
3. `it.set("x")` juste après la création d'un `ListIterator`, avant tout `next()` : quelle exception ?

## Sortie attendue complète

```
D01 : [a, B, c] b c 2 -1
D02 : [10, 30] 1
D03 : [BANANE, KIWI, POMME]
D04 : [1, 5]
D05 : 0a1b2ccba
D06 : [2, 4] [d, m, f] df d [m, f]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **`set(i, e)`** rend l'**ancienne** valeur. **`add(i, e)`** décale les éléments suivants.
- **`remove(int)`** retire à l'**indice** ; **`remove(Object)`** retire la valeur. Sur une `List<Integer>`, `remove(1)` est donc l'indice 1.
- **`subList(a, b)`** : de a inclus à b exclu. C'est une **vue** : la modifier modifie la liste d'origine. Modifier la structure de l'origine invalide la vue (`ConcurrentModificationException`).
- **`sort(null)`** trie selon l'ordre naturel.
- **`ListIterator`** :
  - `hasNext`, `next`, `nextIndex` ;
  - `hasPrevious`, `previous`, `previousIndex` ;
  - `set`, `add`, `remove`.
- **`LinkedList`** est une `List` **et** une `Deque` : `addFirst`, `addLast`, `getFirst`, `getLast`, `removeFirst`, `removeLast`.

</details>
