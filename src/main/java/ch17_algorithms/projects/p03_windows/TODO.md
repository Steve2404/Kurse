# Projet 3 — Les capteurs et les salles (deux pointeurs, fenêtre glissante, intervalles)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **deux pointeurs** : aux deux bouts d'un tableau trié, ou dans le même sens (un qui lit, un qui écrit) ;
- **la fenêtre glissante** : de taille **fixe** (ajouter ce qui entre, retirer ce qui sort), puis de taille **variable** (la droite avance toujours, la gauche quand il le faut) ;
- **les intervalles** : fusionner, choisir le plus de créneaux possible (un **glouton**), compter les salles (un **balayage**) ;
- **le réflexe** : quand une solution recommence tout pour chaque position (O(n²)), chercher ce qu'on peut **garder** d'une position à la suivante (O(n)).

**Ce que TU crées :** dans `ch17_algorithms.projects.p03_windows` :
- **`Interval`** et **`Windows`** (signatures imposées) ;
- **`WindowsTest`**, tes tests.

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Les tris sont permis dans ce projet (`List.sort`, `Arrays.sort`) : tu les as écrits au projet 2. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : une étagère de livres, et un train de wagons.

> **🧰 Tes outils pour ce projet**
>
> - **Lancer tes tests :** **Ctrl+Maj+F10**. **Lancer `Check` :** flèche verte à côté de `Check.main`.
> - **Mesurer**, pour les expériences : une petite classe `Mesure` avec un `main` et `System.nanoTime()`.

---

## Tableau de bord

### ☐ Étape 1 — Deux pointeurs aux deux bouts

**📖 La leçon : resserrer par les deux bouts.** Sur une étagère, les livres sont rangés du plus fin au plus épais. On cherche deux livres dont les épaisseurs font **exactement** 11 cm ensemble. Essayer toutes les paires : O(n²). Mieux : un doigt sur le livre le plus fin (à gauche), un sur le plus épais (à droite).
- La somme est **trop petite** ? Le livre de gauche ne peut aller avec **aucun** autre (le plus épais ne suffit déjà pas) : on avance le doigt de **gauche**.
- **Trop grande** ? Le livre de droite ne peut aller avec aucun : on recule le doigt de **droite**.

Chaque pas élimine un livre : **O(n)**.

**👉 À toi :**
- **`public record Interval(int start, int end)`** : un créneau `[start, end[` (`end` est **exclu**). Son constructeur compact (chapitre 7) refuse un créneau vide : si `end <= start`, `IllegalArgumentException("intervalle vide : [" + start + ", " + end + "[")`.
- **`public final class Windows`** (constructeur `private`) avec **`public static int[] pairWithSum(int[] sorted, int target)`** : les indices `{i, j}` (avec `i < j`) de deux cases dont la somme vaut `target`, ou un tableau **vide** s'il n'y en a pas. Calcule la somme en `long` : deux grands `int` additionnés débordent.

**Tes tests :** une paire au milieu, la paire des deux bouts, aucune paire, un tableau d'une case, deux cases égales ; et `{Integer.MAX_VALUE - 1, Integer.MAX_VALUE}` avec la cible −3 (sans le `long`, la somme déborde et vaut… −3).

**❓ Question :** pourquoi ce raisonnement exige-t-il un tableau **trié** ?

### ☐ Étape 2 — Deux pointeurs dans le même sens

**📖 La leçon : un qui lit, un qui écrit.** Pour enlever les doublons d'un tableau trié **sans créer** d'autre tableau, un indice `i` **lit** chaque case, un indice `kept` dit où **écrire** la prochaine valeur gardée. On n'écrit que si la valeur lue diffère de la **dernière gardée**. Les deux avancent dans le même sens ; `kept` ne dépasse jamais `i`.

**👉 À toi :** **`public static int removeDuplicates(int[] sorted)`** : réécrit les valeurs distinctes au début du tableau, dans l'ordre, et rend leur nombre (le reste du tableau n'a pas d'importance).

**Tes tests :** `{1, 1, 2, 3, 3, 3, 7}` (le nombre, et le début du tableau avec `Arrays.copyOf`), un tableau vide, `{4, 4, 4}`.

### ☐ Étape 3 — La fenêtre de taille fixe

**📖 La leçon : faire glisser.** Un train de wagons pèse chacun un certain poids ; un pont supporte 3 wagons à la fois. Quel est le poids **maximal** sur le pont pendant le passage ? Additionner à chaque fois 3 wagons, c'est O(n × k). Mais d'une position à la suivante, la fenêtre **gagne** un wagon devant et **perd** un wagon derrière :

```
poids de la fenêtre suivante = poids actuel + wagon qui entre - wagon qui sort
```

Une seule addition et une soustraction par position : **O(n)**, quelle que soit la taille `k` de la fenêtre.

**👉 À toi :** **`public static long maxSumOfK(int[] a, int k)`** : la plus grande somme de `k` cases **consécutives**. Si `k < 1` ou `k > a.length` : `IllegalArgumentException("fenetre invalide : " + k)`.

**Tes tests :** avec `{2, 9, -1, 5, 8, -6, 3}`, les fenêtres de 1, 2, 3, 4 et 7 (calcule chaque résultat à la main, fenêtre par fenêtre) ; les fenêtres 0 et trop grande.

**🧪 Expérience :** dans `Mesure`, écris la version naïve (une double boucle qui additionne chaque fenêtre) et chronomètre-la contre la tienne, avec `k = n / 2`, pour `n` = 20 000, 40 000 et 80 000 (avec un tour d'échauffement).

### ☐ Étape 4 — La fenêtre de taille variable

**📖 La leçon : la chenille.** Une fenêtre variable avance comme une chenille : la **tête** (la droite) avance d'une case à chaque tour ; la **queue** (la gauche) avance **seulement** quand la fenêtre ne respecte plus la règle. Chaque indice ne fait qu'**avancer** : au total, au plus 2n pas, donc **O(n)**, même avec une boucle dans la boucle.

**👉 À toi :**
- **`public static int longestUniqueRun(String s)`** : la longueur du plus long morceau de `s` **sans caractère répété**. Une `Map<Character, Integer>` retient la **dernière position** de chaque caractère. Quand le caractère qui entre a déjà été vu **dans la fenêtre**, la gauche saute juste après lui. **Piège :** la gauche ne doit **jamais reculer** (pense à `"abba"`).
- **`public static int shortestAtLeast(int[] positive, int target)`** : la longueur de la plus **courte** fenêtre dont la somme atteint au moins `target` (toutes les valeurs sont positives), ou 0 s'il n'y en a pas. Ici, tant que la somme atteint la cible, on note la longueur et on avance la gauche.

**Tes tests :** `"abcabcbb"`, `"bbbbb"`, `"pwwkew"`, `"abba"`, `"dvdf"`, la chaîne vide, une chaîne sans répétition ; et avec `{2, 3, 1, 2, 4, 3}`, les cibles 7, 4, 15, 16 et 11.

**❓ Question :** que rendrait ton `longestUniqueRun("abba")` si la gauche pouvait reculer ? Déroule-le à la main.

### ☐ Étape 5 — Les intervalles : fusionner, choisir, compter

**📖 La leçon : trier d'abord, puis balayer.** Presque tous les problèmes d'intervalles se résolvent en **triant**, puis en parcourant une seule fois :
- **fusionner** : trier par **début** ; un créneau qui commence avant (ou pile à) la fin du dernier gardé le prolonge ;
- **garder le plus de réunions** dans une seule salle : c'est un **glouton** (on fait le meilleur choix **maintenant**, sans revenir en arrière). Le bon choix : parmi les réunions possibles, garder celle qui **finit le plus tôt**, car elle laisse le plus de temps aux suivantes ;
- **compter les salles** nécessaires : au moment le plus chargé, combien de réunions se chevauchent ? On trie **tous les débuts** et **toutes les fins** séparément, puis on les parcourt ensemble avec deux pointeurs : un début occupe une salle, une fin en libère une. Une fin à 10 h libère la salle **avant** un début à 10 h (la fin est exclue).

**👉 À toi :**
- **`public static List<Interval> merge(List<Interval> intervals)`** : les créneaux fusionnés, triés par début (deux créneaux qui se **touchent**, comme `[8, 10[` et `[10, 12[`, fusionnent aussi) ; ne modifie pas la liste reçue ;
- **`public static int maxNonOverlapping(List<Interval> intervals)`** : le plus grand nombre de créneaux deux à deux sans chevauchement ;
- **`public static int minRooms(List<Interval> meetings)`** : le nombre minimal de salles.

**Tes tests :** une fusion avec des chevauchements, des créneaux qui se touchent, un créneau inclus dans un autre, et une liste vide ; le glouton sur une dizaine de créneaux (compte à la main), et deux créneaux bout à bout ; trois cas de salles, dont des réunions bout à bout (une seule salle) ; un créneau vide refusé.

**❓ Question :** pourquoi trier par **fin**, et pas par **début**, dans le glouton ? Trouve trois créneaux où trier par début donne une mauvaise réponse.

### ☐ Étape 6 — La vitesse, et les mutants

**👉 À toi :** lance `Check`. Les tests de référence font tourner les fenêtres sur un million d'éléments, et les intervalles sur 200 000 créneaux, en moins de 3 secondes. Puis tue les **12** mutants.

### Expériences (hors sortie attendue)

1. Dans ton `minRooms`, remplace `<=` par `<` dans la comparaison entre fin et début. Quel cas de tes tests le voit ?
2. Dans ton `shortestAtLeast`, remplace le `while` intérieur par un `if`. Quel cas casse, et pourquoi ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Interval(int start, int end)`, `final class Windows` et les huit signatures exactes.
- **Tes tests :** au moins **20** tests, `@Test`, `@ParameterizedTest`, `assertEquals(`, `assertThrows(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 12 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p03_windows ===
[PASS] tes tests sur TON code : 27 tests, 27 reussis
[PASS] tes tests sur le code de REFERENCE : 27 tests, 27 reussis
[PASS] les tests de REFERENCE sur TON code : 27 tests, 27 reussis
   mutant 1 : tue (par pairWithSumDoesNotOverflow)
   …
   mutant 12 : tue (par anEmptyIntervalCannotExist)
[PASS] mutants : 12/12 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
