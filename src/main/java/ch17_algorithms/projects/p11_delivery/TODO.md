# Projet 11 — **Capstone** : le GPS du livreur (tout le chapitre 17)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**C'est le projet-bilan :** trois algorithmes du chapitre, assemblés dans une vraie application, avec peu d'indices. Le vrai travail d'un développeur : **reconnaître** quel algorithme résout quel morceau du problème.

**Ce que tu dois savoir faire, et où le relire :**

| Tu dois… | Leçon à relire |
|---|---|
| calculer les temps les plus courts dans un réseau de rues | projet 9, étape 5 (Dijkstra) |
| ranger un graphe en listes d'adjacence | projet 9, étape 1 |
| remplir une table de programmation dynamique, et remonter la solution | projet 10, étapes 2 et 3 |
| représenter un sous-ensemble par un entier (un masque de bits) | **nouveau** : étape 2 ci-dessous |
| trier selon une clé, et garder un tas max | projet 2 (tris), projet 8 (tas) |
| tester un algorithme rapide contre un algorithme lent mais sûr | **nouveau** : étape 3 ci-dessous |

**Ce que TU crées :** dans `ch17_algorithms.projects.p11_delivery` :
- **`Delivery`** (signatures imposées) ;
- **`DeliveryTest`**, tes tests.

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

---

## Les règles du GPS

- La ville a `intersections` carrefours, numérotés de 0 à `intersections - 1`. Le **dépôt** est le carrefour 0.
- **`public Delivery(int intersections, int[][] streets)`** : chaque rue est `{a, b, minutes}`, **à double sens**. Un temps négatif lance `IllegalArgumentException("temps negatif : " + minutes)`.
- **`public long[][] travelTimes(int[] places)`** : le tableau des temps les plus courts entre les lieux donnés : `t[i][j]` = le temps de `places[i]` à `places[j]`, `Long.MAX_VALUE` si c'est impossible. **Un seul** Dijkstra par lieu.
- **`public long bestTour(int[] stops)`** : le temps minimal d'une tournée qui part du dépôt, passe par **tous** les arrêts (dans l'ordre qu'on veut), et revient au dépôt. Aucun arrêt : 0.
- **`public List<Integer> bestOrder(int[] stops)`** : l'ordre des arrêts de cette meilleure tournée. En cas d'égalité, celui que donne la règle de l'étape 2.
- **Les refus** de `bestTour` et `bestOrder`, dans cet ordre :
  - plus de **12** arrêts : `IllegalArgumentException("trop d'arrets : " + nombre)` ;
  - un arrêt qui est le dépôt, hors de la ville, ou en double : `IllegalArgumentException("arret invalide : " + arret)` ;
  - un arrêt inaccessible : `IllegalStateException("livraison impossible")`.
- **`public static int maxOnTime(int[] durations, int[] deadlines)`** : le livreur fait ses livraisons une par une, à partir de l'instant 0 ; la livraison `i` dure `durations[i]` et doit être **finie** au plus tard à `deadlines[i]`. Le plus grand nombre de livraisons qu'il peut faire à l'heure (il peut en abandonner). Tableaux de tailles différentes : `IllegalArgumentException("tailles differentes")`.

---

## Tableau de bord

### ☐ Étape 1 — Les temps entre les lieux

**👉 À toi :** le constructeur, un Dijkstra privé (projet 9), et `travelTimes`.

**Tes tests :** une petite ville de ton choix (dessine-la), les temps entre trois lieux (calcule-les à la main), un lieu isolé ; un temps négatif refusé.

**❓ Question :** pour `k` lieux, pourquoi faire `k` Dijkstra (un par lieu) plutôt que `k²` (un par paire) ?

### ☐ Étape 2 — La meilleure tournée : Held-Karp

**📖 La leçon : un ensemble dans un entier.** Essayer tous les ordres de `k` arrêts coûte k! : 479 millions pour 12 arrêts. Mais deux tournées partielles qui ont visité **les mêmes** arrêts et se trouvent **au même** arrêt ont exactement le même avenir : il suffit de garder la moins chère. C'est la programmation dynamique de Held et Karp, sur les **sous-ensembles** d'arrêts.

Un sous-ensemble de `k` arrêts se range dans un **entier** de `k` bits (un **masque**) : le bit `i` vaut 1 si l'arrêt `i` est visité.
- `1 << i` : le masque qui ne contient que l'arrêt `i` ;
- `mask & (1 << i)` : différent de 0 si l'arrêt `i` est dans `mask` ;
- `mask | (1 << j)` : `mask` avec l'arrêt `j` en plus ;
- `(1 << k) - 1` : tous les arrêts.

La table : `best[mask][i]` = le temps minimal pour partir du dépôt, visiter **exactement** les arrêts de `mask`, et finir à l'arrêt `i`.
- **départ** : `best[1 << i][i]` = le temps du dépôt à l'arrêt `i` ;
- **relation** : depuis `best[mask][i]`, aller à un arrêt `j` pas encore visité donne `best[mask | (1 << j)][j]` ;
- **réponse** : le minimum, sur tous les derniers arrêts `i`, de `best[tous][i]` + le retour au dépôt.

On parcourt les masques dans l'ordre croissant (un masque plus grand contient plus d'arrêts, il vient après). Coût : O(2ᵏ × k²), soit environ 600 000 opérations pour 12 arrêts.

**Pour retrouver l'ordre**, on retient, pour chaque case, l'arrêt d'où l'on venait (`parent[mask][j] = i`), puis on remonte depuis la meilleure fin (projet 10, étape 3). **La règle d'égalité**, pour un résultat unique :
- en remplissant la table, on ne remplace une case que si l'on fait **strictement** mieux ;
- pour la fin, on garde, à égalité, le **plus petit indice** d'arrêt dans `stops`.

**👉 À toi :** `bestTour` et `bestOrder`, avec les refus.

**Tes tests :** un arrêt, deux arrêts, quatre arrêts, aucun ; la tournée et son ordre ; les quatre refus.

### ☐ Étape 3 — Tester contre un oracle

**📖 La leçon : l'oracle.** Comment être sûr qu'un algorithme astucieux est juste ? On le compare à un algorithme **bête mais évidemment juste** (un *oracle*), sur beaucoup de petits cas tirés au hasard. Ici : essayer **tous** les ordres (le retour arrière du projet 6) sur des villes de 6 arrêts. Si les deux donnent toujours le même temps, on peut faire confiance à l'algorithme rapide sur les grands cas, où l'oracle serait trop lent.

`@RepeatedTest(20)` lance un test 20 fois ; son paramètre `RepetitionInfo` donne le numéro de la répétition, qui sert de **graine** au `Random` : chaque répétition a sa propre ville, et le test reste reproductible.

**👉 À toi :** un test répété 20 fois : une ville au hasard de 9 carrefours, 6 arrêts ; compare `bestTour` à ton oracle ; vérifie aussi que l'ordre rendu par `bestOrder` coûte bien ce temps.

### ☐ Étape 4 — Le plus de livraisons à l'heure

**📖 La leçon : un glouton réparé par un tas.** Livrer par échéance croissante est une bonne idée. Mais quand on dépasse une échéance, quelle livraison abandonner ? Celle qui **dure le plus longtemps** parmi celles gardées : elle libère le plus de temps pour la suite, et on perd une seule livraison de toute façon. Un **tas max** des durées gardées la donne en O(log n).

**👉 À toi :** `maxOnTime` : trie les indices par échéance (un `Integer[]` et un `Comparator`), puis ajoute chaque livraison ; si le temps total dépasse son échéance, retire la plus longue du tas.

**Tes tests :** un exemple de quatre livraisons ; deux livraisons qu'il faut faire dans l'ordre des échéances ; une impossible ; un cas où abandonner la plus **courte** serait une erreur (pense à ce qui vient **après**) ; aucune ; des tailles différentes ; un test de vitesse : 12 arrêts dans une ville de 10 000 carrefours, et 200 000 livraisons, en moins de 4 secondes.

### ☐ Étape 5 — Les mutants

**👉 À toi :** lance `Check`, et tue les **13** mutants. Ici, pas d'indice tout de suite : relis la règle concernée, cherche le cas qui manque, et écris-le. Le palier 2 de `INDICES.md` ne sert qu'en dernier recours.

### Expériences (hors sortie attendue)

1. Dans `Mesure`, chronomètre ton oracle et ton `bestTour` pour 8, 9, 10 et 11 arrêts (une ville où toutes les paires de carrefours sont reliées). Combien de temps prendrait l'oracle pour 12, puis 13 arrêts ?
2. Pourquoi `bestTour` refuse-t-il plus de 12 arrêts ? Calcule la taille de la table pour 20 arrêts.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class Delivery` et ses cinq signatures exactes, `PriorityQueue`, `1 << `.
- **Tes tests :** au moins **15** tests, `@BeforeEach`, `@Test`, `@RepeatedTest(`, `assertEquals(`, `assertThrows(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse et oracle compris**.
- **Les 13 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p11_delivery ===
[PASS] tes tests sur TON code : 27 tests, 27 reussis
[PASS] tes tests sur le code de REFERENCE : 27 tests, 27 reussis
[PASS] les tests de REFERENCE sur TON code : 27 tests, 27 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 13/13 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
