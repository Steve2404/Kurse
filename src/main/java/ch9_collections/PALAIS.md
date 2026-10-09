# 🏠 Palais mental — chapitre 9 : la chambre 3, stations 1 à 6

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Le salon, la cuisine, les chambres 1 et 2 (chapitres 1 à 8) viennent avant dans la balade.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 1. 🚪 La porte de la chambre 3 — les listes qu'on ne peut pas changer

- **Image :** sur la porte, trois **affiches**. L'affiche **`List.of`** est **plastifiée** : tu essaies d'ajouter, d'enlever ou de modifier, elle te repousse (`UnsupportedOperationException`) ; tu lui tends `null`, elle te gifle (`NullPointerException`). L'affiche **`Arrays.asList`** est **collée sur un tableau** : tu peux **remplacer** une case (`set`), et le tableau change avec, mais pas en **ajouter** ni en **enlever**. L'affiche **`List.copyOf`** est une **photocopie plastifiée**.
- **À retenir :**
  - `List.of(…)`, `Set.of(…)`, `Map.of(…)`, `List.copyOf(…)` : **non modifiables**, et refusent `null` ;
  - `Arrays.asList(…)` : **taille fixe**, reliée au tableau ; `set` marche (et modifie le tableau), `add` et `remove` → `UnsupportedOperationException` ;
  - `Set.of` ou `Map.of` avec un doublon → `IllegalArgumentException`.
- **Mon image :** …

### 2. 🛏️ Le lit — `List` et ses pièges

- **Image :** sur le lit, des **chaussettes numérotées** à partir de 0. Tu dis `remove(1)` à une liste d'`Integer` : elle enlève **la chaussette numéro 1** (l'**indice**), pas la chaussette qui porte le **chiffre 1**. Pour enlever le chiffre 1, il faut le **déguiser en objet** : `remove(Integer.valueOf(1))`. Un **aspirateur `removeIf`** aspire toutes les chaussettes qui répondent oui à une question ; un **pinceau `replaceAll`** repeint chaque chaussette.
- **À retenir :**
  - `List<Integer>` : `remove(int)` enlève par **indice**, `remove(Object)` enlève par **valeur** ;
  - `add(i, x)`, `set(i, x)` (rend l'ancienne valeur), `get(i)`, `indexOf`, `contains` ;
  - `removeIf(Predicate)`, `replaceAll(UnaryOperator)`, `forEach(Consumer)` ;
  - modifier une `ArrayList` pendant un `for` amélioré → `ConcurrentModificationException`.
- **Mon image :** …

### 3. 🛌 L'oreiller — `Set` et `Map`

- **Image :** sous l'oreiller, deux boîtes. La boîte **`Set`** refuse les **jumeaux** : `add` d'un doublon rend **`false`**. Sa version **`TreeSet`** range tout **dans l'ordre** et hurle si tu lui donnes `null`. La boîte **`Map`** a des **tiroirs à étiquettes** : `put` sur une étiquette déjà prise te rend **l'ancien contenu**. `merge` mélange l'ancien et le nouveau ; si le mélange donne `null`, le tiroir **disparaît**. `getOrDefault` te donne un objet de secours.
- **À retenir :**
  - `HashSet` : pas d'ordre ; `TreeSet` : trié, `null` interdit ; `LinkedHashSet` : ordre d'insertion ; `add` rend `false` pour un doublon ;
  - `Map.put` rend l'**ancienne valeur** (ou `null`) ; `putIfAbsent`, `getOrDefault`, `computeIfAbsent`, `computeIfPresent` ;
  - `merge(k, v, f)` : si `k` est absente, met `v` ; sinon `f(ancienne, v)` ; si `f` rend `null`, la clé est **supprimée** ;
  - `HashMap` accepte **une** clé `null`, `TreeMap` non ; `keySet()`, `values()`, `entrySet()`.
- **Mon image :** …

### 4. 🕯️ La table de nuit — `Queue` et `Deque`

- **Image :** sur la table de nuit, une **file de fourmis**. Les fourmis **polies** (`offer`, `poll`, `peek`) répondent **`null`** ou `false` quand il n'y a rien. Les fourmis **brutales** (`add`, `remove`, `element`) **explosent** (exception). Une **pile d'assiettes** (`Deque` en pile) : `push` pose **devant**, `pop` prend **devant**. `ArrayDeque` refuse `null`.
- **À retenir :**
  - `Queue` : `offer` / `poll` / `peek` (rendent `false` ou `null`) contre `add` / `remove` / `element` (lancent une exception) ;
  - `Deque` : `offerFirst`, `offerLast`, `pollFirst`, `pollLast`, `peekFirst`, `peekLast` ;
  - en pile : `push` = `addFirst`, `pop` = `removeFirst`, `peek` = `peekFirst` ;
  - `ArrayDeque` n'accepte pas `null` ; `LinkedList` est à la fois `List` et `Deque`.
- **Mon image :** …

### 5. ⏰ Le réveil — trier : `Comparable` et `Comparator`

- **Image :** le réveil sonne et tous les objets **se mettent en rang**. Un objet `Comparable` porte en lui **son ordre naturel** (`compareTo`, une seule façon). Un **arbitre extérieur** `Comparator` peut imposer **n'importe quel ordre** (`compare`). L'arbitre enchaîne les règles : `comparing(…)`, puis `thenComparing(…)`, puis tout **à l'envers** avec `reversed()`. Le verdict est un nombre : **négatif** = je passe avant, **0** = égaux, **positif** = je passe après.
- **À retenir :**
  - `Comparable<T>.compareTo(T)` (dans la classe, ordre naturel) ; `Comparator<T>.compare(T, T)` (à l'extérieur) ;
  - résultat négatif / 0 / positif ; `compareTo` cohérent avec `equals` de préférence ;
  - `Comparator.comparing(f).thenComparing(g).reversed()` ; `naturalOrder()`, `reverseOrder()`, `nullsFirst` ;
  - `Collections.sort(liste)` ; `Collections.binarySearch` exige une liste triée **dans le même ordre**.
- **Mon image :** …

### 6. 🔦 La lampe de chevet — les génériques

- **Image :** la lampe éclaire des **bocaux étiquetés `<T>`**. À l'exécution, les étiquettes **s'effacent** (l'effacement de type). Un bocal `List<Object>` n'est **pas** un bocal `List<String>`, même si une `String` est un `Object`. Un bocal **`? extends Fruit`** est un **bocal de lecture** : tu peux en sortir des fruits, mais tu n'y mets **rien**. Un bocal **`? super Pomme`** accepte qu'on y **mette des pommes**. Les primitifs n'entrent pas dans les bocaux (`List<int>` refusé).
- **À retenir :**
  - l'effacement de type : pas de `new T()`, pas de `instanceof List<String>`, pas de `List<int>` ;
  - `List<String>` n'est **pas** un sous-type de `List<Object>` ;
  - `? extends T` (borne haute) : lecture, **pas d'ajout** ; `? super T` (borne basse) : ajout de `T` permis ;
  - méthode générique : `<T> T premier(List<T> l)`, le `<T>` **avant** le type de retour ; le diamant `<>` à droite.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 1 : `Arrays.asList(1, 2).set(0, 5)` marche-t-il ? Et `add(3)` ?
2. Station 2 : sur `List<Integer> l = [5, 1, 7]`, que fait `l.remove(1)` ?
3. Station 3 : que rend `map.put(k, v)` quand `k` existait déjà ?
4. Station 4 : que rend `poll()` sur une file vide ?
5. Station 5 : que signifie un résultat négatif de `compare(a, b)` ?
6. Station 6 : peut-on ajouter un élément dans une `List<? extends Number>` ?
