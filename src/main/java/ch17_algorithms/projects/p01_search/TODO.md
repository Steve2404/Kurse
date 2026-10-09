# Projet 1 — Chercher vite : la dichotomie et ses variantes (complexité O)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **la complexité** : compter les étapes d'un algorithme quand l'entrée grandit, la notation **O** (O(1), O(log n), O(n), O(n log n), O(n²)) ;
- **la recherche dichotomique** (*binary search*) et son **invariant de boucle** ;
- ses variantes : le **point d'insertion**, la **borne inférieure** et la **borne supérieure**, le comptage en O(log n) ;
- **chercher avec une question** (le « premier mauvais commit » de `git bisect`) ;
- **la dichotomie sur la réponse** : chercher la plus petite valeur qui marche ;
- **deux débordements classiques** : `(lo + hi) / 2` et `mid * mid` ;
- **tester la vitesse** et les boucles infinies : `assertTimeoutPreemptively`.

**Ce que TU crées :** dans `ch17_algorithms.projects.p01_search` :
- **`Search`**, une classe utilitaire (ses méthodes sont imposées : les tests de référence les appellent) ;
- **`SearchTest`**, tes tests (comme au chapitre 16).

**Règle du crescendo :** tout Java 17, JUnit et Mockito (chapitres 1 à 16). Ici, **tu écris la dichotomie toi-même** : `Arrays.binarySearch`, `Collections.binarySearch` et `Math.sqrt` sont interdits dans ton code (tu peux t'en servir dans tes tests, pour comparer). Pas de `System.out` ni de `Thread.sleep` dans tes tests.

**Ce que le chapitre 17 t'apprend :** un programme juste mais lent ne sert à rien quand les données grandissent. Ce chapitre t'apprend les **familles d'algorithmes** qu'un développeur senior reconnaît au premier coup d'œil, et à dire **combien de temps** prend un algorithme avant même de le lancer. Les tests de référence de `Check` contiennent des **tests de vitesse** : un algorithme juste mais trop lent échoue.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : un dictionnaire en papier, et le jeu du « plus ou moins ».

> **🧰 Tes outils pour ce projet**
>
> - **Lancer tes tests :** **Ctrl+Maj+F10** dans `SearchTest`. **Lancer `Check` :** flèche verte à côté de `Check.main`.
> - **Mesurer le temps d'un morceau de code**, pour une expérience : `long debut = System.nanoTime();` … `long ms = (System.nanoTime() - debut) / 1_000_000;`. Fais tes mesures dans une petite classe à part avec un `main` (par exemple `Mesure.java`), jamais dans les tests.
> - **Un test qui ne s'arrête pas :** le carré rouge (**Ctrl+F2**) arrête le lancement.

---

## Tableau de bord

### ☐ Étape 1 — Compter les étapes : la recherche linéaire

**📖 La leçon : la complexité.** Pour trouver le mot « zèbre » dans un dictionnaire, tu pourrais lire les mots un par un depuis « a ». Avec 100 000 mots, jusqu'à 100 000 lectures ; avec un dictionnaire deux fois plus gros, deux fois plus. On dit que cette recherche est **en O(n)** : le travail grandit **comme** la taille `n` de l'entrée.

La notation **O** garde seulement la **forme** de la croissance, sans les détails :

| Complexité | Nom | Pour n = 1 000 000 | Exemple |
|---|---|---|---|
| O(1) | constante | 1 étape | lire `tableau[i]` |
| O(log n) | logarithmique | ~20 étapes | la dichotomie |
| O(n) | linéaire | 1 million | lire tout le tableau |
| O(n log n) | quasi linéaire | ~20 millions | un bon tri (projet 2) |
| O(n²) | quadratique | mille milliards | comparer chaque paire |

Un ordinateur fait environ un milliard d'étapes simples par seconde : O(n log n) sur un million, c'est instantané ; O(n²), c'est un quart d'heure.

**👉 À toi :** crée `public final class Search` (constructeur `private`), avec **`public static int linear(int[] a, int key)`** : l'indice de la **première** case qui vaut `key`, ou −1. Tes premiers tests dans `SearchTest` : une valeur présente plusieurs fois, la dernière case, une valeur absente, un tableau vide.

**❓ Question :** combien de comparaisons fait `linear` dans le pire des cas pour un tableau de `n` cases ? Et dans le meilleur ?

### ☐ Étape 2 — La dichotomie

**📖 La leçon : couper en deux.** Au jeu du « plus ou moins » entre 1 et 100, on ne propose pas 1, 2, 3… : on propose **50**. « Plus grand » ? On sait que c'est entre 51 et 100 : on propose **75**. À chaque question, **la moitié** des possibilités disparaît : 100 → 50 → 25 → 13 → 7 → 4 → 2 → 1. **7 questions** au plus, au lieu de 100. Pour un million de nombres : **20** questions. C'est O(log n).

Dans un tableau **trié**, on garde deux indices `lo` et `hi`, avec un **invariant** : « si la clé est là, elle est entre `lo` et `hi` ». On regarde le milieu, et on jette la moitié qui ne peut pas la contenir :

```java
// dans un tableau trié de prénoms, trouver "Lina"
int lo = 0, hi = prenoms.length - 1;
while (lo <= hi) {                          // tant qu'il reste au moins une case possible
    int mid = lo + (hi - lo) / 2;           // le milieu (voir le piège ci-dessous)
    int c = prenoms[mid].compareTo("Lina");
    if (c < 0) lo = mid + 1;                // le milieu est trop petit : on jette la moitié gauche ET le milieu
    else if (c > 0) hi = mid - 1;           // trop grand : on jette la moitié droite et le milieu
    else return mid;                        // trouvé
}
```

**Le piège du milieu :** `(lo + hi) / 2` **déborde** quand `lo + hi` dépasse `Integer.MAX_VALUE` (chapitre 2). `lo + (hi - lo) / 2` donne le même milieu, sans jamais déborder.

**👉 À toi :** **`public static int binary(int[] sorted, int key)`**, sur un tableau trié par ordre croissant, avec **le même contrat qu'`Arrays.binarySearch`** : l'indice d'une case qui vaut `key` ; sinon `-(point d'insertion) - 1`, où le point d'insertion est l'indice où il faudrait insérer `key` pour garder le tableau trié. (À la fin de la boucle, c'est `lo`.)

**Tes tests :** compare ton résultat à `Arrays.binarySearch` pour des clés présentes, absentes au début, au milieu, à la fin ; un tableau vide ; un tableau d'une case.

**❓ Questions :**
- Pourquoi le contrat rend-il `-(point d'insertion) - 1`, et pas simplement `-(point d'insertion)` ?
- Combien de tours de boucle au plus pour 1 000 000 de cases ?

### ☐ Étape 3 — Les bornes : le premier, le dernier, combien

**📖 La leçon : l'intervalle demi-ouvert.** Avec des doublons (`{2, 4, 4, 4, 7}`), `binary` trouve **un** des 4, pas forcément le premier. Pour le premier, on cherche **la borne inférieure** : le premier indice dont la valeur est `>= key`. L'astuce : chercher dans un intervalle **demi-ouvert** `[lo, hi[`, avec `hi = length`, car « aucun » est une réponse possible (l'indice `length`). On ne s'arrête pas en trouvant : on resserre jusqu'à ce que `lo == hi`.

```java
// le premier prénom >= "Lina" dans un tableau trié
int lo = 0, hi = prenoms.length;            // [lo, hi[ : hi est EXCLU
while (lo < hi) {                           // il reste au moins une case
    int mid = lo + (hi - lo) / 2;
    if (prenoms[mid].compareTo("Lina") < 0) lo = mid + 1;   // mid est trop petit : la réponse est après
    else hi = mid;                          // mid convient peut-être : on le GARDE dans l'intervalle
}
return lo;                                  // lo == hi : la réponse
```

**👉 À toi :**
- **`public static int lowerBound(int[] sorted, int key)`** : le premier indice dont la valeur est `>= key` (`sorted.length` s'il n'y en a pas) ;
- **`public static int upperBound(int[] sorted, int key)`** : le premier indice dont la valeur est `> key` ;
- **`public static int count(int[] sorted, int key)`** : combien de fois `key` apparaît, en O(log n).

**Tes tests :** avec `{2, 4, 4, 4, 7, 9, 12}`, pour une clé présente plusieurs fois, la plus petite, la plus grande, une absente au milieu, une plus petite que tout, une plus grande que tout. Calcule chaque résultat à la main.

**❓ Question :** une seule différence d'un caractère sépare `lowerBound` et `upperBound` : laquelle, et pourquoi ?

### ☐ Étape 4 — Chercher avec une question (et se protéger des boucles infinies)

**📖 La leçon : la dichotomie sans tableau.** La dichotomie marche dès qu'une question a une réponse **monotone** : « non, non, non… puis oui, oui, oui ». C'est ce que fait `git bisect` : parmi 1000 versions, la version 1 marchait, la 1000 est cassée ; on teste la 500, puis la 250 ou la 750… En 10 essais, on trouve **la première** version cassée.

Une dichotomie mal écrite **ne s'arrête jamais** (un `lo` qui n'avance plus). Pour qu'un test le signale au lieu de bloquer, on l'entoure d'un délai :

```java
int r = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> monCalcul());   // coupe le calcul après 2 s, et rend sa valeur
```

**👉 À toi :** **`public static int firstBad(int n, IntPredicate isBad)`** : la plus petite version `v` de 1 à `n` telle que `isBad.test(v)`, ou −1 s'il n'y en a pas. On suppose que la réponse est monotone.

**Tes tests :**
- toutes les versions mauvaises (1), seulement la dernière (n), aucune (−1) ;
- **`n = Integer.MAX_VALUE`**, avec la première mauvaise version à 2 000 000 000 : la bonne réponse, en **au plus 32 appels** (compte-les avec un `AtomicInteger`, chapitre 13) ; entoure ce test d'un `assertTimeoutPreemptively` de 2 secondes.

**🧪 Expérience :** dans une petite classe à part, affiche `(1_500_000_000 + 2_000_000_000) / 2` puis `1_500_000_000 + (2_000_000_000 - 1_500_000_000) / 2`.

### ☐ Étape 5 — La racine carrée entière

**👉 À toi :** **`public static long isqrt(long n)`** : le plus grand `r` tel que `r * r <= n`, par dichotomie entre 0 et `n`. Un `n` négatif lance `IllegalArgumentException("nombre negatif : " + n)`.

**Le piège :** pour un grand `n`, `mid * mid` **déborde**. Compare plutôt `mid <= n / mid` (attention à `mid == 0`).

**Tes tests :** 0, 1, 3, 4, 15, 16, 1 000 000, et **`Long.MAX_VALUE`** (sa racine entière vaut 3 037 000 499), avec un délai de 2 secondes ; le nombre négatif.

**🧪 Expérience :** affiche `3037000500L * 3037000500L`.

### ☐ Étape 6 — La dichotomie sur la réponse

**📖 La leçon : chercher la plus petite valeur qui marche.** Une imprimerie doit imprimer des livres en 5 jours : quelle est la plus petite vitesse (pages par jour) qui suffit ? On ne sait pas calculer la réponse directement, mais on sait **vérifier** une vitesse : « avec 300 pages par jour, j'ai fini en 6 jours : non ». Et si une vitesse suffit, toute vitesse plus grande suffit aussi : la réponse est **monotone**. Alors on fait une dichotomie **sur la vitesse**, entre la plus petite et la plus grande valeur possible.

**👉 À toi :** **`public static int minCapacity(int[] weights, int days)`** : un camion livre des colis **dans l'ordre** du tableau. Chaque jour, il charge des colis tant que la somme ne dépasse pas sa capacité, puis il part. Rends la **plus petite capacité** qui permet de tout livrer en `days` jours au plus.
- tableau vide, ou `days < 1` : `IllegalArgumentException("livraison impossible")` ;
- les bornes de la dichotomie : la capacité est au moins le **plus gros colis**, et au plus la **somme** de tous les colis ;
- une petite méthode privée compte les jours nécessaires pour une capacité donnée (glouton : on remplit tant que ça rentre).

**Tes tests :** les colis 1 à 10, en 1, 2, 5, 10 et 20 jours (calcule à la main) ; les deux refus.

**❓ Question :** quelle est la complexité de `minCapacity`, avec `n` colis et `S` la somme des poids ?

### ☐ Étape 7 — La vitesse, et les mutants

**👉 À toi :** lance `Check`. Les tests de référence vérifient aussi la **vitesse** : un million de recherches dans un million de nombres en moins de 3 secondes. Puis tue les **9** mutants. Bloqué ? Le palier 2 de `INDICES.md` dit, replié, ce que change chaque mutant.

**Astuce :** deux mutants provoquent des **boucles infinies**. Si tes tests n'ont pas de délai, `Check` attend 20 secondes avant de conclure. Avec `assertTimeoutPreemptively`, il conclut en 2.

### Expériences (hors sortie attendue)

1. **La forme de la croissance**, dans une petite classe `Mesure` avec un `main` : pour `n` = 100 000, 1 000 000, puis 10 000 000, remplis un tableau trié, puis chronomètre 1000 appels à `linear` et 1000 appels à `binary`. Quand `n` est multiplié par 10, que deviennent les deux temps ?
2. Dans un test de vitesse, remplace temporairement l'appel à `binary` par `linear`. Recopie le message.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class Search` et les huit signatures exactes, `lo + (hi - lo) / 2` ; jamais `Arrays.binarySearch`, `Collections.binarySearch` ni `Math.sqrt`.
- **Tes tests :** au moins **15** tests (chaque cas compte), `@Test`, `@ParameterizedTest`, `assertEquals(`, `assertThrows(`, `Integer.MAX_VALUE` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 9 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p01_search ===
[PASS] tes tests sur TON code : 36 tests, 36 reussis
[PASS] tes tests sur le code de REFERENCE : 36 tests, 36 reussis
[PASS] les tests de REFERENCE sur TON code : 36 tests, 36 reussis
   mutant 1 : tue (par …)
   …
   mutant 9 : tue (par negativeSquareRootIsRejected)
[PASS] mutants : 9/9 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
