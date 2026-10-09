# Projet 8 — Les urgences de l'hôpital (tas et files de priorité)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **le tas binaire** rangé dans un tableau : parent, enfants, remonter (*sift up*), descendre (*sift down*) ;
- **le tri par tas**, O(n log n) ;
- **`PriorityQueue`** et un `Comparator` qui définit « le plus urgent » ;
- **les k plus grands** avec un tas de taille k, O(n log k) ;
- **fusionner k listes triées** avec un tas de têtes de listes ;
- **la médiane en continu** avec **deux** tas.

**Ce que TU crées :** dans `ch17_algorithms.projects.p08_heaps` :
- **`MinHeap`**, **`Patient`**, **`Emergency`**, **`Heaps`** et **`MedianFinder`** (signatures imposées) ;
- tes tests : **`MinHeapTest`** et **`HeapsTest`** (pour les urgences, les k plus grands, la fusion et la médiane).

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Le tas `MinHeap` s'écrit à la main, avec deux méthodes privées `siftUp(int)` et `siftDown(int)`. Ailleurs, `PriorityQueue` est permise : c'est elle qu'on utilise au travail. Pas de tri tout fait dans ton code. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : un tournoi, et une file d'embarquement à l'aéroport.

> **🧰 Tes outils pour ce projet**
>
> - **Dessine le tas** à chaque étape : un arbre, et le tableau en dessous. Les indices des parents et des enfants se vérifient à l'œil.
> - **Lancer tous tes tests :** clic droit sur le dossier `p08_heaps` → **Run 'Tests in p08_heaps'**.

---

## Tableau de bord

### ☐ Étape 1 — Le tas binaire

**📖 La leçon : un tournoi dans un tableau.** Dans un tournoi par élimination, le champion est au sommet ; il n'est pas nécessaire de **classer** tous les joueurs pour savoir qui gagne. Un **tas min** est un arbre où **chaque parent est plus petit que ses enfants** : le minimum est toujours à la racine. Il ne trie pas tout ; il garde seulement le minimum accessible, ce qui est bien moins cher.

L'astuce : un tas se range **dans un tableau**, niveau par niveau. Pour la case `i` :
- son parent est en `(i - 1) / 2` ;
- ses enfants sont en `2i + 1` et `2i + 2`.

```
         1               tableau : [1, 3, 2, 5, 9, 8, 7]
       /   \              indices :  0  1  2  3  4  5  6
      3     2             le parent de 9 (indice 4) est en (4 - 1) / 2 = 1 : c'est 3.
     / \   / \
    5   9 8   7
```

- **Ajouter** : on pose la valeur **au bout** du tableau, puis on la fait **remonter** tant que son parent est plus grand (on échange) : O(log n).
- **Retirer le minimum** : on garde la racine, on met **le dernier** élément à sa place, puis on le fait **descendre** : on l'échange avec le **plus petit** de ses deux enfants, tant qu'il est plus grand que lui : O(log n).

**👉 À toi :** **`public final class MinHeap`**, sur un tableau d'`int` de 16 cases qui **double** quand il est plein :
- **`public void add(int v)`**, **`public int poll()`**, **`public int peek()`** (sur un tas vide : `NoSuchElementException("tas vide")`), **`public int size()`** ;
- deux méthodes privées **`siftUp(int i)`** et **`siftDown(int i)`** ;
- **`public static int[] heapSort(int[] a)`** : un nouveau tableau trié, avec n ajouts puis n retraits.

**Tes tests :** sept valeurs, retirées dans l'ordre croissant ; des doublons et des négatifs ; plus de 16 valeurs (le tableau grandit) ; un tas vide ; le tri par tas (et d'un tableau vide) ; un million de valeurs en moins de 3 secondes.

**❓ Question :** pourquoi, en descendant, faut-il échanger avec le **plus petit** des deux enfants ?

### ☐ Étape 2 — `PriorityQueue` : les urgences

**📖 La leçon : définir « le plus prioritaire ».** À l'embarquement, on appelle d'abord la classe affaires, puis, dans chaque classe, par ordre d'arrivée. `PriorityQueue` (le tas de Java) sort toujours **le plus petit** selon son ordre : on lui donne un `Comparator` qui dit que « plus petit » veut dire « plus prioritaire » (chapitre 9) :

```java
PriorityQueue<Passager> file = new PriorityQueue<>(
        Comparator.comparingInt(Passager::classe).thenComparingInt(Passager::arrivee));
```

`add` et `poll` sont en O(log n), `peek` en O(1). **Piège :** afficher ou parcourir une `PriorityQueue` ne donne **pas** l'ordre trié (voir l'expérience).

**👉 À toi :**
- **`public record Patient(String name, int severity, int arrival)`** (gravité de 1 à 5, 5 = vital) ;
- **`public final class Emergency`** : **`public void arrive(Patient p)`**, **`public Patient next()`** (le plus **grave** d'abord ; à gravité égale, le plus **ancien** numéro d'arrivée ; personne : `NoSuchElementException("personne en attente")`), **`public int waitingCount()`**.

**Tes tests :** cinq patients, avec deux égalités de gravité, dans un ordre d'arrivée mélangé ; la salle vide.

**🧪 Expérience :** ajoute 5, 1, 4, 2, 3 dans une `PriorityQueue<Integer>`, puis affiche-la et son `peek()`. Est-ce trié ?

### ☐ Étape 3 — Les k plus grands, et la fusion de listes

**📖 La leçon : un petit tas pour une grande foule.** Pour garder les 10 meilleurs scores parmi 10 millions, inutile de tout trier. Un tas **min** de taille 10 garde les 10 meilleurs vus jusqu'ici ; son sommet est le **plus faible** des 10. Un nouveau score meilleur que lui ? On ajoute le nouveau, on retire le sommet. Chaque score coûte O(log 10) : **O(n log k)** au total.

Pour **fusionner k listes triées**, un tas contient la **tête** de chaque liste : on sort la plus petite, et on la remplace par l'élément suivant de **sa** liste.

**👉 À toi :** `public final class Heaps` (constructeur `private`) :
- **`public static int[] topK(int[] a, int k)`** : les `k` plus grandes valeurs (ou toutes, s'il y en a moins), **de la plus grande à la plus petite** ; `k < 0` lance `IllegalArgumentException("k negatif : " + k)` ;
- **`public static List<Integer> mergeSorted(List<List<Integer>> lists)`** : la fusion triée. Dans le tas, mets des `int[]` `{valeur, numéro de liste, indice dans la liste}`, avec un comparateur sur la valeur.

**Tes tests :** les 3 plus grands, avec des doublons, avec `k` plus grand que le tableau, avec `k = 0`, et `k` négatif ; trois listes à fusionner, des listes vides, aucune liste.

### ☐ Étape 4 — La médiane en continu

**📖 La leçon : deux tas dos à dos.** La médiane d'un flux de nombres qui arrivent un par un : trier à chaque fois coûterait trop cher. On coupe les nombres en deux moitiés :
- la moitié **basse** dans un tas **max** (son sommet est le plus grand des petits) ;
- la moitié **haute** dans un tas **min** (son sommet est le plus petit des grands).

Les deux tas ont la même taille, ou la moitié basse en a **un de plus**. La médiane est au sommet de la moitié basse (taille impaire), ou la moyenne des deux sommets (taille paire).

Pour ajouter `v` en gardant ces règles : mets-le dans la moitié basse, fais passer le plus grand de la moitié basse dans la moitié haute, puis, si la moitié haute est devenue plus grosse, fais repasser son plus petit dans la moitié basse. Un tas max : `new PriorityQueue<>(Collections.reverseOrder())`.

**👉 À toi :** **`public final class MedianFinder`** : **`public void add(int v)`** et **`public double median()`** (aucun nombre : `NoSuchElementException("aucun nombre")`). Calcule la moyenne de deux `int` **en `long`**.

**Tes tests :** six ajouts, avec la médiane après chacun (calcule-les à la main) ; `Integer.MAX_VALUE` et `Integer.MAX_VALUE - 2` ; aucun nombre ; un test de vitesse (200 000 ajouts avec une médiane à chaque fois, un top 10 sur un million, une fusion de 1000 listes de 1000).

**❓ Question :** pourquoi chaque ajout passe-t-il d'abord par la moitié basse, puis par la moitié haute, même quand ce n'est pas nécessaire ?

### ☐ Étape 5 — La vitesse, et les mutants

**👉 À toi :** lance `Check`, et tue les **13** mutants.

### Expériences (hors sortie attendue)

1. Dans `Mesure`, compare, sur 10 millions de nombres au hasard, le temps de `Arrays.sort` (puis prendre les 10 derniers) et celui d'un tas de taille 10.
2. Dans ta médiane, retire le `(long)`. Que donne la médiane de `Integer.MAX_VALUE` et `Integer.MAX_VALUE - 2` ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** les cinq types et leurs signatures exactes, `private void siftUp(int`, `private void siftDown(int`, `PriorityQueue`, `Comparator` ; jamais `Arrays.sort`, `Collections.sort`, `.sorted(`.
- **Tes tests :** au moins **12** tests, `@Test`, `assertEquals(`, `assertArrayEquals(`, `assertThrows(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 13 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p08_heaps ===
[PASS] tes tests sur TON code : 14 tests, 14 reussis
[PASS] tes tests sur le code de REFERENCE : 14 tests, 14 reussis
[PASS] les tests de REFERENCE sur TON code : 14 tests, 14 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 13/13 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
