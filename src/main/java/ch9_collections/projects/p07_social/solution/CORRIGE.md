# Projet 7 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`UnionFind.java`](UnionFind.java), [`Post.java`](Post.java) et [`Social.java`](Social.java).
>
> Les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Le graphe et les communautés

**Le code :** [`UnionFind.java`](UnionFind.java) et le début du `main` de [`Social.java`](Social.java).

**Union-Find :** chaque membre pointe vers un « parent », et la racine représente le groupe. Deux membres sont dans la même communauté si `find` rend la **même racine**. Avec la **compression de chemin**, chaque `find` raccourcit les chemins : les suivants sont presque instantanés.

**8 unions pour 12 amitiés :** 10 membres en 2 communautés demandent exactement 10 − 2 = **8** liens. Les 4 autres amitiés relient des membres **déjà** dans le même groupe (elles forment des cycles), et `union` rend `false` pour elles.

**Pourquoi Union-Find plutôt qu'un parcours ?** Le résultat est le même, mais Union-Find se met à jour **au fil des ajouts** d'amitiés, sans tout reparcourir.

---

## Étape 2 — Suggestions et séparation

**Le code :** les suggestions, les amis communs, puis le BFS des degrés.

**Les suggestions :** les amis d'amis, comptés avec `merge`. Eve est amie de chloe **et** de gina, toutes deux amies d'ana : **2** amis communs.

**`Map.Entry.<String, Integer>comparingByValue().reversed()`** : sans le type explicite, `javac` ne peut pas déduire les types de `comparingByValue()`. L'appel suivant (`.reversed()`) ne lui donne aucune information sur la cible.

**Les ensembles :** `retainAll` donne l'intersection, et `removeAll` la différence. Toujours sur des **copies**, pour ne pas modifier le graphe.

---

## Étape 3 — Le fil d'actualité

**Le code :** le record `Cursor` et la fusion avec la `PriorityQueue`.

**Question — pourquoi ne pas tout concaténer et trier ?**
- Concaténer les n publications, puis trier : **O(n log n)**.
- La fusion de k listes **déjà triées** avec un tas de k curseurs coûte **O(m log k)** pour les m premières publications. On s'arrête à 5 : on ne lit **que** ce qu'on affiche, au lieu de trier tout l'historique.

C'est la fusion du tri fusion, généralisée à k listes.

**`Comparator.comparing(Cursor::current, newestFirst)`** : on compare les curseurs par leur publication **courante**, selon l'ordre « plus récent d'abord ».

**Le constructeur compact de `Post` :** `Set.copyOf(tags)` rend un ensemble **immuable**. Le `TreeSet` passé par `parse` ne peut plus modifier le post après coup.

---

## Étape 4 — Tendances

**Le code :** la fin du `main`.

**Le tas min de taille 3 :** les tags sont parcourus, et dès qu'il y en a 4, le plus faible sort. Il reste voyage (105), cuisine (80) et sport (67).

**Les plus connectés :** ana, bob et chloe ont chacun 3 amis. À égalité, l'ordre naturel (alphabétique) départage.

**`Set.copyOf(tags) == tags` vaut `true`** : `tags` est déjà un ensemble immuable (créé par `Set.copyOf` dans le constructeur). Le copier encore serait inutile, donc Java rend **la même instance**. C'est un détail d'implémentation documenté, et il n'arrive qu'avec les collections immuables du JDK.

**Expérience — `all.get(0).tags().add("x")`** (vérifié) :

```
java.lang.UnsupportedOperationException
```

L'appel compile (`add` existe sur `Set`), mais il échoue à l'exécution. C'est le rôle du `Set.copyOf` dans le constructeur compact : sans lui, n'importe qui pourrait modifier les tags d'une publication.
