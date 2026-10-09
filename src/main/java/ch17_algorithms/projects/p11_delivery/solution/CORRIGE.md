# Projet 11 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Delivery.java`](Delivery.java), et les tests de référence dans [`DeliveryTest.java`](DeliveryTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — Les temps entre les lieux

La ville des tests de référence : 7 carrefours, rues 0-1 (4), 0-2 (1), 2-1 (2), 1-3 (5), 2-3 (8), 3-4 (3), 4-0 (10), 4-5 (2) ; le carrefour 6 est isolé. Les temps entre 0, 1 et 3 (vérifiés) : 0 → 1 = **3** (par 2 : 1 + 2, et non 4 en direct), 0 → 3 = **8** (0-2-1-3), 1 → 3 = **5**.

**Question — `k` Dijkstra :** un Dijkstra depuis un lieu donne les temps vers **tous** les carrefours d'un coup. `k` Dijkstra donnent donc toutes les paires. En faire un par paire referait `k` fois le même travail depuis chaque lieu.

---

## Étape 2 — Held-Karp

Les tournées de la ville des tests (vérifiées) :
- `{1}` : **6** (0 → 1 en 3, et retour en 3) ;
- `{5, 1}` : **25**, ordre `[1, 5]`. Les deux ordres coûtent 25 : 0 → 1 (3), 1 → 5 (10, par 3 et 4), 5 → 0 (12) ; ou l'inverse. À égalité, la règle garde la fin au plus petit indice de `stops` (`5`, à l'indice 0) : l'ordre est donc `[1, 5]` ;
- `{3, 5, 1, 4}` : **25**, ordre `[1, 3, 4, 5]`.

(En écrivant ce projet, les tests de référence attendaient d'abord 21 pour ces tournées, calculé trop vite : le code avait raison. Calculer à la main, puis vérifier avec un oracle, c'est exactement la leçon de l'étape 3.)

---

## Étape 3 — L'oracle

**Le code :** le test `heldKarpMatchesBruteForce` et sa méthode `bruteForce`. Sur les 20 villes au hasard, Held-Karp et l'oracle donnent toujours le même temps (vérifié).

---

## Étape 4 — Le plus de livraisons à l'heure

Les résultats (vérifiés) :
- `{100, 200, 1000, 2000}` avec les échéances `{200, 1300, 1250, 3200}` : **3** ;
- `{1, 2}` avec `{3, 2}` : **2** (la livraison de 2 minutes d'abord, finie à 2 ; puis l'autre, finie à 3) ;
- `{3, 3, 3}` avec `{3, 6, 6}` : **2** ;
- `{5, 1, 1, 3}` avec `{5, 6, 6, 8}` : **3**. Le glouton garde 5, puis 1 (fini à 6), puis le 2e 1 (fini à 7 : trop tard). Il abandonne alors **la plus longue** (5) : il reste 1 + 1 = 2 minutes. La livraison de 3 minutes finit à 5, avant 8 : **3** livraisons. En abandonnant la plus **courte**, il resterait 5 + 1 = 6 minutes, et la livraison de 3 minutes finirait à 9, trop tard : 2 seulement. C'est le mutant 12, qui survivait avant l'ajout de ce cas.

---

## Étape 5 — Les mutants

Les 13 mutants sont tués par les tests de référence (vérifié). Trois mutants de la première version ont été remplacés, et deux étaient **équivalents** pour une raison intéressante :
- **revisiter un arrêt** (oublier de vérifier que `j` n'est pas déjà dans le masque) ne change rien. Les temps viennent de Dijkstra, ce sont des **plus courts chemins** : ils respectent l'inégalité triangulaire, et un détour par un arrêt déjà visité ne peut jamais rendre une tournée moins chère ;
- **oublier de vérifier qu'une case est atteinte** ne change rien non plus : les rues étant à double sens, un arrêt inaccessible l'est depuis tous les autres, et ses temps valent tous `Long.MAX_VALUE`, déjà ignorés.

Un troisième (lire le parent après avoir retiré l'arrêt du masque) épuisait la mémoire (`OutOfMemoryError`), ce qui mettait toute la vérification en danger.

---

## Expériences de fin de projet

1. Tous les ordres contre Held-Karp (vérifié, sur une ville où toutes les paires sont reliées) :

   | arrêts | tous les ordres | Held-Karp |
   |---|---|---|
   | 8 | 1 ms | 1 ms |
   | 9 | 5 ms | 0 ms |
   | 10 | 58 ms | 2 ms |
   | 11 | 625 ms | 1 ms |

   Chaque arrêt de plus multiplie l'oracle par environ 11 (k!) : 12 arrêts prendraient environ 7 secondes, 13 environ 1 minute 30. Held-Karp reste sous quelques millisecondes.
2. Pour 20 arrêts, la table aurait 2²⁰ × 20 ≈ **21 millions** de cases `long` (168 Mo), et le calcul 2²⁰ × 20² ≈ 420 millions d'étapes : faisable, mais lourd. Au-delà, c'est impossible : le problème du voyageur de commerce n'a **aucun** algorithme rapide connu (il est « NP-difficile »). Pour 100 arrêts, on utilise des **approximations** (gloutons, recherche locale), qui donnent une bonne tournée sans garantir la meilleure. Limiter à 12 arrêts garde la réponse exacte et instantanée.
