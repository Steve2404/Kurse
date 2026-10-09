# Projet 2 — Le classement du marathon (les tris)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **le tri par insertion** : O(n²), mais très rapide sur des données presque triées ;
- **diviser pour régner** : le **tri fusion**, O(n log n) toujours ;
- **le tri rapide** (*quicksort*) : la partition, le **choix du pivot**, la **partition en trois** pour les doublons ;
- **le tri par comptage** : O(n + max), sans aucune comparaison ;
- **la stabilité** d'un tri, et un tri générique avec un `Comparator` ;
- **les cas pièges** d'un tri : vide, une case, déjà trié, à l'envers, que des doublons, des négatifs.

**Ce que TU crées :** dans `ch17_algorithms.projects.p02_sorting` :
- **`Runner`** (`record Runner(String name, int seconds)`) et **`Sorting`** (méthodes imposées) ;
- **`SortingTest`**, tes tests.

**Règle du crescendo :** tout Java 17, JUnit et Mockito. **Tu écris les tris toi-même** : dans ton code, aucun tri tout fait (`Arrays.sort`, `Collections.sort`, `.sort(`, `.sorted(`), ni `TreeMap`, `TreeSet` ou `PriorityQueue`. Dans tes tests, `Arrays.sort` est permis pour fabriquer le résultat attendu. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : un jeu de cartes, et une pile de copies à corriger.

> **🧰 Tes outils pour ce projet**
>
> - **Tester plusieurs tris avec le même test :** un tri de tableau est un `Consumer<int[]>` (chapitre 8) ; une référence de méthode `Sorting::mergeSort` en est un. Un `@MethodSource` peut donc fournir `Arguments.of("fusion", (Consumer<int[]>) Sorting::mergeSort)`.
> - **Mesurer**, pour les expériences : une petite classe `Mesure` avec un `main` et `System.nanoTime()` (projet 1).

---

## Tableau de bord

### ☐ Étape 1 — Le tri par insertion, et les tableaux pièges

**📖 La leçon : trier ses cartes.** Quand tu ramasses des cartes une par une, tu glisses chaque nouvelle carte **à sa place** parmi celles que tu tiens déjà (qui sont triées) : tu la compares à sa voisine de gauche, et tu la fais reculer tant que la voisine est plus grande.

```java
// trier des notes de copies, par insertion
for (int i = 1; i < notes.length; i++) {
    int carte = notes[i];                       // la carte qu'on vient de ramasser
    int j = i - 1;
    while (j >= 0 && notes[j] > carte) {        // tant que la voisine de gauche est plus grande…
        notes[j + 1] = notes[j];                // … on la décale d'une case vers la droite
        j--;
    }
    notes[j + 1] = carte;                       // la carte prend la place libérée
}
```

Au pire (un tableau à l'envers), chaque carte recule jusqu'au début : 1 + 2 + … + (n−1) ≈ n²/2 décalages, donc **O(n²)**. Mais sur un tableau **presque trié**, chaque carte ne recule que d'une ou deux cases : presque **O(n)**.

**👉 À toi :** `public final class Sorting` (constructeur `private`) avec **`public static void insertionSort(int[] a)`**, qui trie `a` sur place, par ordre croissant.

**Tes tests :** un test paramétré qui essaie un tri sur **tous les tableaux pièges** : vide, une case, déjà trié, à l'envers, que des doublons, avec des négatifs, et un tableau mélangé avec des doublons. Compare à une copie triée par `Arrays.sort`. Écris-le pour qu'il puisse servir aux **autres** tris (voir l'encadré des outils).

**❓ Question :** pour un tableau de 4 cases à l'envers, `{4, 3, 2, 1}`, combien de décalages fait le tri par insertion ?

### ☐ Étape 2 — Diviser pour régner : le tri fusion

**📖 La leçon : deux piles triées se fusionnent vite.** Deux correcteurs ont chacun trié leur pile de copies. Pour faire une seule pile triée, on regarde le dessus des deux piles, on prend la plus petite, et on recommence : **n** gestes pour **n** copies. Le tri fusion applique l'idée **récursivement** (chapitre 5) :
1. couper le tableau en deux moitiés ;
2. trier chaque moitié (par le même tri fusion) ;
3. fusionner les deux moitiés triées.

Il y a log₂(n) niveaux de découpage, et chaque niveau coûte n gestes de fusion : **O(n log n)**, dans tous les cas.

**👉 À toi :** **`public static void mergeSort(int[] a)`**. Conseils :
- une méthode privée récursive `mergeSort(int[] a, int[] tmp, int lo, int hi)` qui trie `a[lo, hi[` (`hi` exclu) ;
- **un seul** tableau auxiliaire `tmp`, créé une fois, de la taille de `a` ;
- après la fusion dans `tmp`, recopie le morceau dans `a` avec `System.arraycopy`.

**Tes tests :** ton test paramétré de l'étape 1 sur le tri fusion aussi.

**🧪 Expérience :** dans `Mesure`, chronomètre `insertionSort` et `mergeSort` sur des tableaux **au hasard** de 10 000, 20 000, 40 000 et 80 000 nombres (fais un premier tour d'échauffement sans afficher). Quand `n` double, par combien est multiplié chaque temps ?

### ☐ Étape 3 — Le tri rapide, et ses deux pièges

**📖 La leçon : la partition.** Pour trier une pile de copies, on choisit une copie au hasard, le **pivot** (disons la note 12). On fait trois tas : les notes **< 12**, les notes **= 12**, les notes **> 12**. Le tas du milieu est déjà à sa place ; on recommence sur les deux autres tas. Si le pivot coupe à peu près au milieu, il y a log₂(n) niveaux : **O(n log n)** en moyenne, et sans tableau auxiliaire.

**Piège 1, le mauvais pivot :** la version « naïve » prend **toujours le premier élément** comme pivot, avec une partition en deux tas. Sur un tableau **déjà trié**, le premier élément est le plus petit : un tas vide et un tas de n−1. On ne coupe plus en deux, on enlève **un** élément par niveau : **n niveaux**, O(n²), et une récursion si profonde qu'elle fait **déborder la pile** d'appels. D'où un pivot choisi **au hasard** : `ThreadLocalRandom.current().nextInt(lo, hi + 1)` (bornes `lo` incluse et `hi + 1` exclue).

**Piège 2, les doublons :** si tous les éléments sont égaux, une partition en **deux** tas met tout du même côté, et on retombe dans O(n²). La partition en **trois** tas (le « drapeau hollandais » de Dijkstra) range les égaux au milieu, une fois pour toutes :

```
lo          lt          i           gt          hi
[  < pivot  |  = pivot  |  à voir   |  > pivot  ]
```

- `a[i] < pivot` : échange `a[lt]` et `a[i]`, puis avance `lt` **et** `i` ;
- `a[i] > pivot` : échange `a[i]` et `a[gt]`, recule `gt` (n'avance **pas** `i` : la valeur reçue n'a pas encore été vue) ;
- sinon : avance `i`.

À la fin, on trie récursivement `[lo, lt − 1]` et `[gt + 1, hi]`.

**👉 À toi :** **`public static void quickSort(int[] a)`**, avec un pivot au hasard et la partition en trois.

**Tes tests :** ton test paramétré sur le tri rapide aussi ; et les cas qui piègent, en grand (avec `assertTimeoutPreemptively` de 3 s) : un million de nombres **déjà triés**, un million de nombres qui ne valent que 0, 1 ou 2.

**🧪 Expérience :** écris la version naïve dans `Mesure` (pivot = premier élément, partition en deux de Lomuto :

```java
static void naif(int[] a, int lo, int hi) {
    if (lo >= hi) return;
    int p = a[lo], i = lo;
    for (int j = lo + 1; j <= hi; j++) if (a[j] < p) { i++; int t = a[i]; a[i] = a[j]; a[j] = t; }
    int t = a[lo]; a[lo] = a[i]; a[i] = t;
    naif(a, lo, i - 1);
    naif(a, i + 1, hi);
}
```

Chronomètre-la sur un tableau **trié** de 5 000, 10 000 et 20 000 nombres, puis essaie 100 000. Que se passe-t-il ?

### ☐ Étape 4 — Trier sans comparer : le tri par comptage

**📖 La leçon : compter au lieu de comparer.** Pour trier 200 copies notées de 0 à 20, inutile de comparer les copies : on fait 21 tas, un par note, et on les ramasse dans l'ordre. **O(n + max)** : bien plus rapide que n log n, mais seulement quand les valeurs sont **petites** et **entières**. On démontre qu'un tri qui **compare** ne peut pas faire mieux que O(n log n) ; le tri par comptage ne compare jamais, il échappe à cette limite.

**👉 À toi :** **`public static void countingSort(int[] a, int max)`** : toutes les valeurs doivent être entre 0 et `max` ; sinon `IllegalArgumentException("valeur hors limites : " + v)` (avec la première valeur fautive), **sans** avoir modifié le tableau.

**Tes tests :** un petit tableau ; une valeur trop grande et une négative (les messages) ; dix millions de valeurs de 0 à 100 en moins de 3 secondes.

**❓ Question :** pourquoi le tri par comptage serait-il une mauvaise idée pour trier des `int` quelconques ?

### ☐ Étape 5 — La stabilité : le classement du marathon

**📖 La leçon : un tri stable.** Un tri est **stable** si deux éléments **égaux** gardent leur ordre de départ. Exemple : une liste de copies déjà triée par **nom** ; on la trie par **note** avec un tri stable : à note égale, les copies restent dans l'ordre alphabétique. Un tri instable les mélangerait. Le tri fusion est stable **si**, en cas d'égalité, il prend l'élément de la moitié **gauche** (`<=`, pas `<`). (Pour des `int`, la stabilité ne se voit pas : deux 4 sont indiscernables. Elle compte pour des **objets**.)

**👉 À toi :**
- **`public record Runner(String name, int seconds)`** ;
- **`public static <T> List<T> mergeSort(List<T> list, Comparator<? super T> cmp)`** : un tri fusion **stable** et **générique** (chapitre 9), qui rend une **nouvelle** liste et ne modifie pas celle reçue (elle peut être non modifiable, comme un `List.of`).

**Tes tests :**
- cinq coureurs, avec deux paires de temps égaux, triés par temps : à temps égal, l'**ordre de la liste de départ** est gardé ;
- la liste reçue n'est pas modifiée ; une liste vide.

**❓ Question :** pourquoi `<=` rend-il le tri stable, et `<` non ?

### ☐ Étape 6 — La vitesse, et les mutants

**👉 À toi :** lance `Check`. Les tests de référence trient un million de nombres au hasard (fusion et rapide), un million de nombres déjà triés, un million de doublons (rapide), un million de nombres presque triés (insertion, en 2 secondes), et dix millions de petites valeurs (comptage). Puis tue les **10** mutants.

### Expériences (hors sortie attendue)

1. Dans **ton** tri rapide, remplace la branche « égal » (`i++`) par un échange vers la droite (`swap(a, i, gt--)`), comme une partition en deux. Lance tes tests : que se passe-t-il avec `{4, 4, 4, 4}` ? Remets le code.
2. Dans ton tri générique, remplace `<= 0` par `< 0`. Quel test le voit ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class Sorting`, les cinq signatures exactes, `record Runner(String name, int seconds)` ; jamais `Arrays.sort`, `Collections.sort`, `.sort(`, `.sorted(`, `TreeMap`, `TreeSet`, `PriorityQueue`.
- **Tes tests :** au moins **10** tests, `@Test`, `@ParameterizedTest`, `assertArrayEquals(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 10 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p02_sorting ===
[PASS] tes tests sur TON code : 13 tests, 13 reussis
[PASS] tes tests sur le code de REFERENCE : 13 tests, 13 reussis
[PASS] les tests de REFERENCE sur TON code : 13 tests, 13 reussis
   mutant 1 : tue (par sortsEveryTrickyArray [tri insertion])
   …
   mutant 10 : tue (par genericMergeSortIsStable)
[PASS] mutants : 10/10 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
