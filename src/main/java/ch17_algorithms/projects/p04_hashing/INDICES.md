# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — « L'ai-je déjà vu ? »

<details><summary>Indice 1</summary>

Une `Map<Integer, Integer>` de la **valeur** vers son **indice**. Pour chaque `j`, cherche d'abord le complément, **puis** enregistre `a[j]` : sinon une case pourrait faire la paire avec elle-même.

</details>

<details><summary>Indice 2</summary>

`Integer i = seen.get(target - a[j]);` puis `if (i != null) return new int[]{i, j};`. Pour garder le premier indice d'un doublon : `seen.putIfAbsent(a[j], j)`.

</details>

---

## Étape 2 — Compter et regrouper

<details><summary>Indice 1</summary>

La signature d'un mot : `char[] lettres = mot.toCharArray(); Arrays.sort(lettres); new String(lettres)`.

</details>

<details><summary>Indice 2</summary>

Pour trier les mots de `topWords` : `Comparator.comparing((String w) -> compte.get(w)).reversed().thenComparing(Comparator.naturalOrder())`. Le `(String w)` aide le compilateur à deviner le type avant `.reversed()`.

</details>

---

## Étape 3 — La plus longue suite consécutive

<details><summary>Indice 1</summary>

Parcours le `HashSet`, pas le tableau : les doublons ne seront vus qu'une fois.

</details>

<details><summary>Indice 2</summary>

Pour une valeur `v` sans `v - 1` : une boucle `while (values.contains(v + longueur)) longueur++;`.

</details>

---

## Étape 4 — Ta propre table de hachage

<details><summary>Indice 1</summary>

Un tableau générique ne se crée pas directement (`new Node<K, V>[8]` ne compile pas) : `(Node<K, V>[]) new Node[8]`, avec `@SuppressWarnings("unchecked")`. Une méthode privée `indexOf(key, longueur)` calcule le seau.

</details>

<details><summary>Indice 2</summary>

Pour retirer dans une chaîne, garde le maillon **précédent** pendant le parcours : si la clé est en tête, le seau pointe sur le suivant ; sinon, `precedent.next = n.next`.

</details>

---

## Étape 5 — Le cache LRU

<details><summary>Indice 1</summary>

Deux méthodes privées font tout le travail : `unlink(n)` (le maillon se décroche : son précédent et son suivant se donnent la main) et `appendNewest(n)` (le maillon s'accroche juste avant la sentinelle de fin).

</details>

<details><summary>Indice 2</summary>

L'entrée la plus ancienne est toujours `oldest.next` (le maillon juste après la sentinelle de début). Quand tu la chasses, retire-la **aussi** de la `HashMap`.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel `hashCode` négatif, quelle collision, quelle clé égale mais pas identique, quel ordre du cache n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Le seau se calcule avec `%` (un indice négatif pour un hash négatif).
2. `put` compare les clés avec `==`.
3. La table s'agrandit trop tard (à 100 % au lieu de 75 %).
4. Retirer au milieu d'une chaîne coupe la suite de la chaîne.
5. `remove` ne diminue pas la taille.
6. Un `get` du cache ne rend pas l'entrée récente.
7. Le cache chasse la plus **récente** au lieu de la plus ancienne.
8. Le cache chassé reste dans la `HashMap`.
9. Un `put` sur une clé existante ne la rend pas récente.
10. `twoSum` garde le dernier indice d'un doublon au lieu du premier.
11. `firstUnique` accepte un caractère vu deux fois.
12. `topWords` ne départage plus les égalités par ordre alphabétique.
13. `longestConsecutive` recompte une suite à partir de chacun de ses éléments (O(n²)).

</details>
