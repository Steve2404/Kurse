# Projet 1 — La boîte à outils statistique

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Notions visées (chapitre 5) :**
- **concevoir des méthodes** : modificateur d'accès, `static`, type de retour (`int`, `double`, `int[]`, `double[]`, `String`, `void`), nom, paramètres ;
- les **varargs** :
  - les trois façons d'appeler (une liste, un tableau, rien) ;
  - un paramètre fixe **avant** le varargs ;
  - un varargs de tableaux (`int[]...`) ;
  - `Object...` ;
  - `null` casté en `int[]`, en `Object` ou en `Object[]` ;
- `return;` dans une méthode `void` ;
- des méthodes **`private`** d'aide.

Côté algorithmes : médiane, modes, **quickselect** (le k-ième plus petit sans tout trier), moyenne glissante, histogramme vertical.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch5_methods.projects.p01_stats`, **deux classes** :
- **`Stats`** : la boîte à outils, uniquement des méthodes `static` ;
- **`StatsReport`** : le `main`, qui appelle `Stats`.

**Règle du crescendo :** chapitres 1 à 5.
- Pas de constructeur écrit par toi (chapitre 6).
- Pas d'héritage, de `record`, d'`enum` ni d'`interface`.
- Pas de collection, de lambda ni de stream.
- Pas de `try/catch`.
- Pas de `%f` : arrondis avec `Math.round(x * 100) / 100.0`.

---

## Tableau de bord

### ☐ Étape 1 — `sum` et `count` : un varargs est un tableau

```
somme : liste 6, tableau 149, rien 0
compte : 10 0 0 -1
```
- `static int sum(int... values)`.
  - Appelle-la avec `1, 2, 3`, puis avec `Data.TEMPS`, puis **sans argument**.
  - **Question :** que contient `values` quand on n'a rien passé ? (Indice : ce n'est pas `null`.)
- `static int count(int... values)` rend la longueur, ou `-1` si le tableau est `null`.
  - Appelle-la avec `Data.TEMPS`, sans argument, avec `new int[0]`, puis avec `(int[]) null`.
  - **Expérience :** écris `count(null)` sans le cast. Que dit `javac` ? Et pour une méthode `Object...` ?

### ☐ Étape 2 — Un paramètre fixe avant le varargs

```
moyenne : 14.64 (piege : le premier compte deux fois) 5.0 5.0
moyenne juste : 14.9, max 25, max d'un seul -3
```
- `static double average(int first, int... rest)` : le paramètre `first` **impose** au moins une valeur.
  - **Expérience :** `average()` ne compile pas. Lis le message.
- **Le piège :** `average(t[0], t)` compte la première valeur deux fois. Fais l'appel juste avec `Arrays.copyOfRange(t, 1, t.length)`.
- **Les appels exacts** (avec `t = Data.TEMPS` et `rest`, la copie sans la première case) :
  - 1re ligne : `average(t[0], t)`, le texte du piège, `average(5)` et `average(4, 5, 6)` ;
  - 2e ligne : `average(t[0], rest)`, `max(t[0], rest)` et `max(-3)`.
- `static int max(int first, int... rest)`.
- L'arrondi à 2 décimales passe par une méthode **`private static double round2(double)`** : personne d'autre n'en a besoin.
- **Questions :**
  - pourquoi `int... rest, int first` ne compile-t-il pas ?
  - et deux varargs dans la même méthode ?

### ☐ Étape 3 — Rendre des tableaux, sans abîmer celui de l'appelant

```
mediane : 15.0 2.0, original intact [12, 15, 9, 22, 18, 15, 7, 25, 15, 11]
modes : [15] [1, 2]
k-iemes : 1er 7, 3e 11, 10e 25, original intact true
moyenne glissante (3) : [12.0, 15.33, 16.33, 18.33, 13.33, 15.67, 15.67, 17.0]
```
- **`median(int...)`** trie une **copie** (`Arrays.copyOf`). Pour un nombre pair de valeurs, c'est la moyenne des deux du milieu.
- **`modes(int...)`** rend un **`int[]`** : toutes les valeurs les plus fréquentes, dans l'ordre croissant.
  - Trie une copie, mesure la plus longue série, puis recopie les valeurs dont la série a cette longueur.
  - Rends un tableau de la bonne taille : `Arrays.copyOf(result, k)`.
- **`kth(int k, int... values)`** : le k-ième plus petit, par **quickselect**.
  1. Fais une copie avec `clone()`.
  2. Partitionne autour du dernier élément (partition de Lomuto, dans une méthode `private`).
  3. Ne continue que du côté qui contient la position k − 1.
  - L'échange de deux cases est une méthode `private static void swap(int[], int, int)`.
  - **Question :** pourquoi l'échange se voit-il chez l'appelant, alors que Java passe tout par valeur ?
- **Les appels exacts :**
  - `median(t)` et `median(3, 1, 2)`, puis `Arrays.toString(t)` ;
  - `modes(t)` et `modes(1, 1, 2, 2, 3)` ;
  - `kth(1, t)`, `kth(3, t)` et `kth(10, t)`, puis `t[0] == 12` ;
  - `movingAverage(3, t)`.
- **`movingAverage(int window, int... values)`** rend un **`double[]`** de taille `n - window + 1`, calculé par fenêtre glissante (ajoute l'entrant, retire le sortant).

### ☐ Étape 4 — Varargs de tableaux et d'objets

```
concat : [3, 8, 1, 9, 4] 0
join : a, b, c |  | lun/mar/mer/jeu/ven
describe : 4 element(s) : 1 deux 3.0 c | 0 element(s) : | 1 element(s) : null | tableau null | true
```
- `static int[] concat(int[]... arrays)` : chaque argument est un `int[]`. Recopie-les avec `System.arraycopy`.
  - Appelle-la avec `Data.SHOP_A, Data.SHOP_B, Data.SHOP_C`, puis affiche la longueur de `concat()`.
- `static String join(String separator, String... parts)` : écris la boucle toi-même.
  - Appelle-la avec `", ", "a", "b", "c"`, puis `"-"` seul (le résultat est `""`), puis `"/", Data.LABELS` (un `String[]` passe directement).
  - Les trois résultats sont séparés par ` | `.
- `static String describe(Object... items)` : le nombre d'éléments suivi de chacun, ou `tableau null`.
  - `describe(1, "deux", 3.0, 'c')` : chaque primitif est **emballé** (`Integer`, `Double`, `Character`).
  - `(Object) null` : **un** élément qui vaut `null`.
  - `(Object[]) null` : le tableau **lui-même** est `null`.
  - `(Object) new int[] {1, 2}` : **un** élément, qui est un tableau. Affiche seulement si la description commence par `1 element`, car un `int[]` s'affiche `[I@…`, ce qui change à chaque exécution.
  - **Question :** pourquoi un `int[]` ne peut-il pas devenir un `Object[]`, alors qu'un `String[]` le peut ?

### ☐ Étape 5 — Un histogramme et une sortie anticipée

```
 5 |     ##
 4 |     ##      ##
 3 | ##  ##      ##
 2 | ##  ##      ##  ##
 1 | ##  ##  ##  ##  ##
   +--------------------
    lun mar mer jeu ven
histogramme impossible : 5 etiquettes pour 2 valeurs
```
- `static void histogram(String[] labels, int... counts)` : le varargs vient **après** le tableau.
- Si les tailles diffèrent, affiche le message puis **`return;`**.
- **L'algorithme :**
  - une ligne par niveau, du plus haut (`max`) jusqu'à 1 ;
  - chaque colonne fait 4 caractères : ` ## ` ou 4 espaces ;
  - le niveau est formaté avec `String.format("%2d |", level)` ;
  - retire les espaces de fin de ligne (`stripTrailing`).
- L'axe : `   +` puis `----` par étiquette. Les noms : 4 espaces, puis chaque étiquette sur 4 caractères (`%-4s`), sans espaces finaux.
- Appelle-la deux fois : avec `Data.LABELS, Data.VOTES`, puis avec `Data.LABELS, 1, 2`.

---

## Checklist (vérifiée par `Check`)

- `Data.TEMPS` et `Data.LABELS` ;
- un paramètre fixe suivi d'un `int...` ;
- `int[]...`, `Object...`, `String...` ;
- les trois casts : `(int[]) null`, `(Object[]) null`, `(Object) null` ;
- `private static` et `return;` ;
- `Arrays.copyOf` et `clone()` ;
- une méthode qui rend un `double[]`, une qui rend un `int[]` ;
- `String.format`.

---

## Sortie attendue complète

```
somme : liste 6, tableau 149, rien 0
compte : 10 0 0 -1
moyenne : 14.64 (piege : le premier compte deux fois) 5.0 5.0
moyenne juste : 14.9, max 25, max d'un seul -3
mediane : 15.0 2.0, original intact [12, 15, 9, 22, 18, 15, 7, 25, 15, 11]
modes : [15] [1, 2]
k-iemes : 1er 7, 3e 11, 10e 25, original intact true
moyenne glissante (3) : [12.0, 15.33, 16.33, 18.33, 13.33, 15.67, 15.67, 17.0]
concat : [3, 8, 1, 9, 4] 0
join : a, b, c |  | lun/mar/mer/jeu/ven
describe : 4 element(s) : 1 deux 3.0 c | 0 element(s) : | 1 element(s) : null | tableau null | true
 5 |     ##
 4 |     ##      ##
 3 | ##  ##      ##
 2 | ##  ##      ##  ##
 1 | ##  ##  ##  ##  ##
   +--------------------
    lun mar mer jeu ven
histogramme impossible : 5 etiquettes pour 2 valeurs
```
