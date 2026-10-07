# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Words.java`](Words.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Compter et indexer

**Le code :** `tokens` et le début du `main` jusqu'à la ligne `ordre d'apparition`.

**Trois `Map`, trois usages :**

| Map | Ordre des clés | Usage ici |
|---|---|---|
| `HashMap` | aucun garanti | compter vite (`freq`) |
| `LinkedHashMap` | ordre d'**insertion** | se souvenir de l'ordre d'apparition |
| `TreeMap` | ordre **trié** des clés | un index alphabétique |

**Les méthodes « tout en un » :**
- `merge(k, 1, Integer::sum)` remplace « si absent alors 1, sinon ancien + 1 » ;
- `putIfAbsent` n'écrit que si la clé est absente ;
- `computeIfAbsent` crée la valeur (le `TreeSet`) seulement si besoin, et la rend pour qu'on y ajoute.

**`get("python")` rend `null`** : la clé est absente. `getOrDefault` évite ce `null`. `containsValue(4)` vaut `false`, car aucun mot n'apparaît 4 fois.

**`HashSet` pour les mots vides :** `contains` est en temps **constant** en moyenne, au lieu de parcourir une liste.

---

## Étape 2 — Top-k, index, requêtes

**Le code :** le tas, l'index et les requêtes.

**Question — pourquoi un tas de taille k ?** Trier toutes les entrées coûte **O(n log n)**. Un tas qu'on garde à k éléments coûte **O(n log k)** : chaque insertion et chaque retrait coûte log k. Pour k petit et n grand, c'est bien moins cher, et la mémoire reste en O(k).

**Pourquoi un tas « le plus faible d'abord » ?** On veut **garder** les plus forts. Le tas met en tête celui qu'on doit jeter dès qu'il y en a un de trop. À égalité de compte, `comparingByKey(reverseOrder())` fait sortir les mots **alphabétiquement les plus grands**, ce qui garde `collections` avant `map`.

**`Map.Entry.comparingByValue()`** est un comparateur prêt à l'emploi sur les entrées.

**Les requêtes ensemblistes** sur des **copies** :
- `retainAll` donne l'intersection (`java & collections` → documents 1 et 3) ;
- `addAll` donne l'union ;
- `removeAll` donne la différence.

Sans copie, la requête modifierait l'index lui-même.

---

## Étape 3 — Anagrammes, `merge` qui supprime, les trois `Set`

**Le code :** la fin du `main`.

**Modifier une map par sa vue :** `groups.values()` n'est pas une copie. `removeIf` dessus **retire les entrées** de la map. C'est la même idée que `subList` au projet 1.

**`sort(null)`** : `List.sort` avec un comparateur `null` utilise l'**ordre naturel** des éléments.

**La trace de `stock` (vérifiée) :**

| Opération | `stock` |
|---|---|
| départ | `{java=2, map=1}` |
| `merge("java", -1, …)` | `{java=1, map=1}` |
| `merge("map", -1, …)` : 1 − 1 = 0, le remappage rend `null`, la clé est **supprimée** | `{java=1}` |
| `merge("set", 5, Integer::sum)` : clé absente, elle prend 5 | `{java=1, set=5}` |
| `compute("java", …)` : 1 × 10 | `{java=10, set=5}` |
| `computeIfPresent("set", …)` : 5 + 1 | `{java=10, set=6}` |
| `replaceAll(v * 2)` | `{java=20, set=12}` |
| `remove("set", 999)` : la valeur ne correspond pas, rien n'est retiré | `false` |

**Les trois `Set` :** `LinkedHashSet` garde l'ordre d'insertion (`set, map, java, liste`), `TreeSet` trie (`java, liste, map, set`). Le doublon `map` disparaît, et `add` d'un élément déjà présent rend `false`.

**Expériences :** les trois compilent. Vérifié à l'exécution :

| Code | Résultat |
|---|---|
| `Map.of("a", 1, "a", 2)` | `IllegalArgumentException: duplicate key: a` |
| `new TreeMap<String, Integer>().put(null, 1)` | `NullPointerException`. Une `TreeMap` doit **comparer** les clés, et `null.compareTo(…)` est impossible |
| `new HashMap<String, Integer>().put(null, 1)` | **réussit** : `{null=1}`. `HashMap` accepte **une** clé `null`, avec un traitement spécial |
