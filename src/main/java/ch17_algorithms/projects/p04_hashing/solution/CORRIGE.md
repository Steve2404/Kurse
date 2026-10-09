# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Hashing.java`](Hashing.java), [`SimpleHashMap.java`](SimpleHashMap.java) et [`LruCache.java`](LruCache.java) ; les tests de référence dans les trois fichiers `…Test.java`.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — « L'ai-je déjà vu ? »

**L'expérience** (vérifiée) :

| n | version naïve | hachage |
|---|---|---|
| 10 000 | 9 ms | 0 ms |
| 20 000 | 30 ms | 1 ms |
| 40 000 | 106 ms | 1 ms |

La version naïve essaie toutes les paires (n²/2) : environ ×4 quand `n` double. Le hachage fait un seul passage.

---

## Étape 2 — Compter et regrouper

**Question — deux mots à égalité :** l'ordre de parcours d'une `HashMap` n'est **ni** alphabétique **ni** l'ordre d'arrivée : il dépend des `hashCode`. Vérifié : une `HashMap` contenant `ours` et `chat` les rend dans l'ordre `[ours, chat]`. Un tri qui ne départage pas les égalités garderait cet ordre-là (le tri est stable) : `topWords("ours chat", 2)` rendrait `[ours, chat]` au lieu de `[chat, ours]`. Sans un cas d'égalité dans les tests, ce bug passerait inaperçu, ou pire, passerait par hasard aujourd'hui et casserait avec d'autres mots (c'est le mutant 12).

---

## Étape 3 — La plus longue suite consécutive

**Question — pourquoi le début :** sans la condition, on lancerait le comptage depuis **chaque** valeur. Pour `{n, n−1, …, 1}` (une seule suite de longueur n), compter depuis 1 coûte n pas, depuis 2 coûte n−1, … : n²/2 pas, donc **O(n²)**. Avec la condition, seul 1 lance un comptage : chaque valeur est visitée au plus deux fois, **O(n)**. Le test de vitesse fait justement une suite d'un million de valeurs (c'est le mutant 13, qui dépasse le délai).

---

## Étape 4 — Ta propre table de hachage

**Question — ranger à nouveau :** le seau d'une clé est `floorMod(hash, nombreDeSeaux)`. Quand le nombre de seaux double, ce numéro **change** pour environ la moitié des clés (par exemple, un hash de 13 va dans le seau 5 sur 8 seaux, mais dans le seau 13 sur 16). Recopier les seaux tels quels laisserait ces clés à un endroit où `get` ne les chercherait plus.

---

## Étape 5 — Le cache LRU

**Le code :** `LruCache.java`. Toutes les opérations sont en O(1) : un accès à la `HashMap`, et quelques changements de liens dans la liste.

---

## Étape 6 — Les mutants

Les 13 mutants sont tués par les tests de référence (vérifié). En écrivant ce projet, cinq mutants ont d'abord **survécu**, et quatre ont révélé de vrais trous dans les tests de référence :
- `==` au lieu d'`equals` dans `put` : il manquait un remplacement avec une **autre** chaîne égale ;
- la chaîne coupée lors d'un `remove` : il manquait une collision à **trois** clés, en retirant celle du milieu ;
- le dernier indice gardé dans `twoSum` : il manquait un doublon qui compte ;
- l'ordre alphabétique des égalités : il manquait deux mots à égalité, dans un ordre de `HashMap` non alphabétique.

Le cinquième (`Math.abs(hash % n)`) était **équivalent** : il range les clés dans d'autres seaux, mais toujours dans des seaux valides et de façon cohérente.

---

## Expériences de fin de projet

1. **Le mauvais `hashCode`** (vérifié) :

   | n | `hashCode` constant | clés `Integer` |
   |---|---|---|
   | 5 000 | 46 ms | 1 ms |
   | 10 000 | 77 ms | 0 ms |
   | 20 000 | 292 ms | 3 ms |

   Toutes les clés `Mauvais` tombent dans **le même seau** : chaque `put` parcourt toute la chaîne (pour vérifier que la clé n'y est pas) : O(n) par ajout, O(n²) en tout. Agrandir la table n'y change rien. Un `hashCode` doit **répartir** les clés. Les `record` en génèrent un bon tout seuls (chapitre 7).
2. Avec `%` (vérifié), le test des clés négatives échoue : `ArrayIndexOutOfBoundsException : Index -7 out of bounds for length 8`.
