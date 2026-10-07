# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Item.java`](Item.java) et [`Inventory.java`](Inventory.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Charger, et le piège `remove`

**Le code :** [`Item.java`](Item.java), puis le début du `main` jusqu'à la ligne `ventes`.

**`List<Item> items = new ArrayList<>();`** : la variable est du type de l'**interface**. Le reste du code ne dépend que de `List`, et l'on pourrait passer à une `LinkedList` en changeant une seule ligne. Le diamant `<>` déduit `<Item>` de la gauche.

**Le piège `remove`, sur `[3, 7, 3, 12, 7, 3, 1]` :**
- `sales.remove(1)` retire l'**indice** 1 (le premier 7) : on obtient `[3, 3, 12, 7, 3, 1]` ;
- `sales.remove(Integer.valueOf(3))` retire la **première valeur** 3 : on obtient `[3, 12, 7, 3, 1]`.

**Question — que ferait `sales.remove(3)` ?** Il retirerait l'élément à l'**indice 3**, pas la valeur 3. Vérifié : sur `[3, 12, 7, 3, 1]`, on obtient `[3, 12, 7, 1]`. Ici, c'est par chance un 3 qui est à l'indice 3. Pour la surcharge, `remove(int)` correspond **exactement** au littéral `3`, alors que `remove(Object)` demanderait un boxing (chapitre 5, phase 2).

---

## Étape 2 — Modifier en masse, trier

**Le code :** de `rupture` jusqu'à la ligne `subList`.

**Copier avant de filtrer :** `new ArrayList<>(items)` crée une **nouvelle** liste avec les mêmes éléments. `removeIf` sur la copie ne touche pas `items`.

**Le prix des articles de cuisine :** 890 + 890 / 10 = 979, soit **9.79** (division entière).

**Les comparateurs composés :** c'est l'API de ce que tu as écrit à la main au chapitre 8 (`Order`). `Comparator.comparing(clé)` crée l'ordre, `thenComparing(clé, ordre)` départage avec un ordre choisi, et `reverseOrder()` inverse l'ordre naturel.

**`comparingLong` et `comparingInt`** évitent le boxing des clés numériques.

**Question — que devient `items` après `top3.clear()` ?** `subList` rend une **vue**, pas une copie. Vider la vue **retire ces éléments de la liste d'origine**. Vérifié sur `[a, b, c, d, e]` : `subList(0, 3).clear()` laisse `[d, e]`. Pour une copie indépendante, il faut écrire `new ArrayList<>(items.subList(0, 3))`.

---

## Étape 3 — Parcourir en modifiant

**Le code :** le `ListIterator`, l'`Iterator` qui retire, puis l'analyse ABC.

**`ListIterator.set`** remplace l'élément courant **pendant** le parcours, en toute sécurité. Il a aussi `add`, `previous` et `hasPrevious`.

**Expérience — `items.remove(x)` dans un for-each :**

```
java.util.ConcurrentModificationException
```

Un for-each utilise un `Iterator` caché. Modifier la liste **par elle-même** pendant le parcours change son compteur interne de modifications. Au `next()` suivant, l'itérateur détecte l'écart et lève l'exception. Seul `iterator.remove()` (ou `removeIf`) retire en sécurité.

**L'analyse ABC :** les articles qui font les 70 premiers % de la valeur sont classés **A**, les 20 % suivants **B**, et le reste **C**. Calculer `cumul × 100 ≤ total × 70` en entiers évite les erreurs d'arrondi des `double`.

---

## Étape 4 — `LinkedList`, `Collections`, listes figées

**Le code :** la fin du `main`.

**La file :**
- `[cmd1, cmd2, cmd3]`, puis `addFirst` et `addLast` : `[urgent, cmd1, cmd2, cmd3, cmd4]`. `removeFirst()` rend `urgent`.
- `rotate(1)` : le dernier passe en tête, d'où `[cmd4, cmd1, cmd2, cmd3]`.

**`binarySearch` sur `[1, 3, 3, 7, 12]` :** 7 donne **3**. 5 donne **−4** : absent, il s'insérerait en position 3, d'où −(3 + 1). La liste **doit** être triée.

**Les listes figées :**

| Liste | `set` | `add` / `remove` | `null` |
|---|---|---|---|
| `Arrays.asList(…)` | **permis** (elle est adossée au tableau) | interdits | permis |
| `List.of(…)` | interdit | interdits | **interdit** |
| `List.copyOf(…)` | interdit | interdits | interdit |

**Expériences :** les trois **compilent** (ce sont des appels de méthodes de `List`), et **toutes échouent à l'exécution** (vérifié) :
- `fixed.add("w")` donne `UnsupportedOperationException` ;
- `List.of("a").set(0, "b")` donne `UnsupportedOperationException` ;
- `List.of("a", null)` donne `NullPointerException`.

**`List.of(1, 2).equals(Arrays.asList(1, 2))`** vaut `true` : l'égalité des `List` compare les **éléments dans l'ordre**, quelle que soit l'implémentation.
