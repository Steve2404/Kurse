# Projet 4 — Le tournoi (passage par valeur et autoboxing)

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 5) :**
- **Java passe TOUT par valeur** :
  - la valeur d'un primitif ;
  - la valeur d'une **référence** (donc l'objet est partagé, mais la variable de l'appelant ne change jamais) ;
- **réassigner** un paramètre ou **modifier** l'objet qu'il désigne ;
- les objets **immuables** en paramètre (`String`, `Integer`) ;
- **retourner** une valeur plutôt que la modifier ;
- l'**autoboxing** et l'**unboxing** : le cache `Integer` (−128 à 127), `==` contre `equals`, `Long.equals(5)`, `null` dans un `Integer[]`, `Character` et `Double`.

Côté algorithmes :
- les **permutations** par échanges et retour arrière ;
- la **permutation suivante** dans l'ordre lexicographique ;
- le **rang** d'une permutation par calcul ;
- un **calendrier toutes rondes** (méthode du cercle) ;
- un **classement** multi-critères.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch5_methods.projects.p04_tournament`, la classe **`Tournament`** (le `main` et toutes les méthodes `static`).

**Règle du crescendo :** chapitres 1 à 5. Pas de constructeur, de collection ni de `try/catch`.

**Tes outils pour ce projet** (pas d'arguments) :

```
javac -d build/ch5-p04 -sourcepath src/main/java src/main/java/ch5_methods/projects/p04_tournament/Tournament.java
java "-Duser.language=fr" -cp build/ch5-p04 ch5_methods.projects.p04_tournament.Tournament
```

---

## Tableau de bord

### ☐ Étape 1 — Ce qui change, ce qui ne change pas

```
echange : primitifs a=1 b=2, tableau [2, 1]
references : Lions! Ours, reassigne [1, 2, 3], modifie [10, 2, 3]
immuables : Lions 5, resultat ignore 5, resultat garde 6
```

**📖 La leçon : Java passe toujours une copie.** Quand tu appelles une méthode, chaque paramètre reçoit une **copie** de la valeur donnée. Deux cas très différents en découlent :
- pour un **primitif** (`int`…), la copie est le nombre lui-même. La méthode peut changer sa copie, l'original ne bouge pas ;
- pour un **objet** ou un **tableau**, la valeur copiée est **l'étiquette** (chapitre 1, projet 3, étape 5). La méthode a donc une 2e étiquette sur **le même** objet. Si elle **modifie l'objet**, l'appelant le voit. Si elle **ré-attache sa copie d'étiquette** à un autre objet, l'appelant ne le voit pas.

```java
static void anniversaire(int age)   { age = age + 1; }            // change sa copie : sans effet dehors
static void anniversaire(int[] ages) { ages[0] = ages[0] + 1; }   // modifie LE tableau : visible dehors
static void remplace(int[] ages)    { ages = new int[] {99}; }    // ré-attache sa copie : sans effet dehors

int age = 8;
anniversaire(age);          // age vaut toujours 8
int[] ages = {8};
anniversaire(ages);         // ages[0] vaut 9
remplace(ages);             // ages[0] vaut toujours 9
```

**Méthode de travail :** pour chaque appel, dessine deux colonnes, l'appelant et la méthode, avec leurs variables, et des flèches vers les objets.

**👉 À toi :**

- **Deux surcharges de `swap`** :
  - `static void swap(int a, int b)` : elle n'échange rien chez l'appelant ;
  - `static void swap(int[] arr, int i, int j)` : elle échange vraiment.
- `static void touch(StringBuilder x, StringBuilder y)` : `x.append('!')` est visible dehors ; `y = new StringBuilder("??")` ne l'est pas.
- `static void reassign(int[] arr)`, qui fait `arr = new int[] {…}`, et `static void mutate(int[] arr)`, qui fait `arr[0] *= 10`.
- `static void tryChange(String s, Integer n)` fait `s += "!"` et `n++` : ni `s` ni `n` ne changent chez l'appelant.
- `static int increment(int n)` : appelle-la une fois **en ignorant** le résultat, puis une fois en le gardant.
- **Les valeurs de départ :**
  - `a = 1`, `b = 2`, `pair = {1, 2}` ;
  - `sb1 = "Lions"`, `sb2 = "Ours"`, `numbers = {1, 2, 3}` ;
  - `s = "Lions"`, `Integer n = 5`, `counter = 5`.
- **L'ordre des appels :**
  1. `reassign(numbers)`, puis note `Arrays.toString(numbers)` (la valeur `reassigne`) ;
  2. `mutate(numbers)` ;
  3. `increment(counter)` sans affecter le résultat, puis `int returned = increment(counter)`.
- **Pour chaque ligne, dessine** les variables de l'appelant et de la méthode (deux colonnes), avec des flèches vers les objets.

### ☐ Étape 2 — L'autoboxing

```
cache Integer : 127 true, 128 false, equals true, Long.equals(5) false, 5L == 5 true, compare true
unboxing : total 7, AB, Integer.valueOf("42") + 1 = 43, Double 5.0
```

**📖 La leçon : l'emballage automatique (autoboxing).** Java passe tout seul d'un primitif à sa boîte, et inversement :

```java
Integer boite = 5;          // emballage : 5 devient Integer.valueOf(5)
int nombre = boite;         // déballage : boite.intValue()
boite + nombre              // 10 : déballé pour le calcul
```

Pour comparer le **contenu** de deux boîtes, utilise `equals` (comme pour les `String`). L'étape te fait découvrir pourquoi `==` est piégeux.

**👉 À toi :**

- `Integer small1 = 127, small2 = 127;` et `big1 = 128, big2 = 128;` : compare-les avec `==`, puis `big1.equals(big2)`.
- `Long five = 5L;` :
  - `five.equals(5)` est faux : le 5 devient un `Integer`, pas un `Long` ;
  - `five == 5` est vrai : unboxing, puis comparaison numérique.
- `big1 < 200` : l'unboxing est automatique avec `<`.
- **Un `Integer[]` avec un `null`**, `{3, null, 4}` : additionne en sautant le `null`.
  - **Expérience :** retire le test. Quelle exception ?
- `Character letter = 'A';` puis `(char) (letter + 1)`.
- Affiche aussi `Integer.valueOf("42") + 1` (un `Integer` unboxé pour l'addition).
- `Double.valueOf(5)` compile (un `int` passé à une méthode qui attend un `double`), mais `Double d = 5;` non.
  - **Expérience :** vérifie-le, puis explique pourquoi.

### ☐ Étape 3 — Les permutations

```
permutations par echanges : ABC ACB BAC BCA CBA CAB (6)
ordre lexicographique : ABCD ABDC ACBD ACDB ADBC ADCB ... DCBA (24 au total)
rang de CADB : par enumeration 14, par calcul 14, le tableau est reste sur DCBA
```

**📖 La leçon : la récursion, une méthode qui s'appelle elle-même.** Pour résoudre un problème, on le ramène au **même problème, en plus petit**, jusqu'à un cas si simple qu'on connaît la réponse : le **cas de base**.

```java
static int somme(int n) {          // 1 + 2 + … + n
    if (n == 0) return 0;          // cas de base : on s'arrête
    return n + somme(n - 1);       // le même problème, en plus petit
}
```

`somme(3)` → `3 + somme(2)` → `3 + 2 + somme(1)` → `3 + 2 + 1 + somme(0)` → `3 + 2 + 1 + 0` = 6. **Déroule-la sur papier**, un appel par ligne, en décalant chaque appel vers la droite.

**Le retour arrière** (*backtracking*) est une récursion qui **essaie** un choix, explore la suite, puis **défait** son choix avant d'essayer le suivant.

**👉 À toi :**

- **`static void permute(char[] c, int k, StringBuilder out)`**, par retour arrière sur les 3 premières lettres de `Data.WORD` :
  1. pour i de k à la fin : échange c[k] et c[i] ;
  2. recurse sur k + 1 ;
  3. **défais** l'échange.
  - Quand `k == c.length`, ajoute la permutation et incrémente un compteur `static`.
  - Il faut une 3e surcharge : `swap(char[], int, int)`.
  - **Question :** pourquoi le compteur doit-il être `static` (ou rendu), et pas une variable locale ?
- **`static boolean next(char[] c)`**, la permutation suivante **en place** :
  1. cherche le plus grand i tel que `c[i] < c[i + 1]` ;
  2. cherche le plus grand j tel que `c[j] > c[i]` ;
  3. échange-les, puis inverse la fin du tableau.
  - Elle rend `false` s'il n'y a plus de suivante.
  - Enumère avec un `do { … } while (next(word));`. Affiche les 6 premières, puis la dernière.
- **`static int rank(String word)`** : pour chaque position, compte les lettres plus petites placées **après** elle, multiplie par (n − 1 − i)!, additionne, puis ajoute 1.
  - `factorial` est **récursive**.

### ☐ Étape 4 — Le calendrier et le classement

```
J1 : Lions 2-0 Requins, Ours 1-1 Tigres, Aigles 3-2 Loups
J2 : Lions 3-0 Tigres, Requins 1-0 Loups, Ours 2-0 Aigles
J3 : Lions 0-0 Loups, Tigres 1-1 Aigles, Requins 2-2 Ours
J4 : Lions 1-0 Aigles, Loups 1-2 Ours, Tigres 2-2 Requins
J5 : Lions 2-0 Ours, Aigles 3-2 Requins, Loups 2-0 Tigres
classement : 1.Lions 13pts(+8) 2.Ours 8pts(+1) 3.Aigles 7pts(-1) 4.Requins 5pts(-2) 5.Loups 4pts(-1) 6.Tigres 3pts(-5)
```

**📖 Rappel :** une méthode `void` qui remplit des tableaux **passés en paramètre** : l'appelant voit les changements, car c'est le même tableau (étape 1). La rotation avec `System.arraycopy` : chapitre 4, projet 2, étape 4.

**👉 À toi :**

- **La méthode du cercle** pour t équipes :
  - un tableau `ring` contient les indices 0 à t − 1 ;
  - à chaque journée, le match m oppose `ring[m]` (domicile) à `ring[t - 1 - m]` ;
  - après la journée, `ring[1..t-1]` tourne d'un cran vers la droite (`System.arraycopy`), et `ring[0]` reste fixe.
- **Les scores sont calculés**, pas inventés :
  - buts à domicile = `(longueur du nom de l'équipe à domicile + journée) % 4` ;
  - buts à l'extérieur = `(indice domicile × 2 + indice extérieur + journée) % 3`.
- **`static void record(int[] points, int[] diff, int home, int away, int hg, int ag)`** remplit les tableaux **de l'appelant** : 3 points pour une victoire, 1 pour un nul.
- **Le classement** : un tri par insertion sur un tableau d'**indices**, avec une méthode `before(…)` : points, puis différence de buts, puis nom (`compareTo`).

---

## Checklist (vérifiée par `Check`)

- `Data.TEAMS`, `Data.WORD` et `Data.TARGET` ;
- `Integer`, `Long` et `Character` ;
- `swap(int, int)` et `swap(int[], int, int)` ;
- une méthode qui reçoit un `StringBuilder`, une qui reçoit `String` et `Integer` ;
- `System.arraycopy`, `compareTo` et `do {`.

---

## Sortie attendue complète

```
echange : primitifs a=1 b=2, tableau [2, 1]
references : Lions! Ours, reassigne [1, 2, 3], modifie [10, 2, 3]
immuables : Lions 5, resultat ignore 5, resultat garde 6
cache Integer : 127 true, 128 false, equals true, Long.equals(5) false, 5L == 5 true, compare true
unboxing : total 7, AB, Integer.valueOf("42") + 1 = 43, Double 5.0
permutations par echanges : ABC ACB BAC BCA CBA CAB (6)
ordre lexicographique : ABCD ABDC ACBD ACDB ADBC ADCB ... DCBA (24 au total)
rang de CADB : par enumeration 14, par calcul 14, le tableau est reste sur DCBA
J1 : Lions 2-0 Requins, Ours 1-1 Tigres, Aigles 3-2 Loups
J2 : Lions 3-0 Tigres, Requins 1-0 Loups, Ours 2-0 Aigles
J3 : Lions 0-0 Loups, Tigres 1-1 Aigles, Requins 2-2 Ours
J4 : Lions 1-0 Aigles, Loups 1-2 Ours, Tigres 2-2 Requins
J5 : Lions 2-0 Ours, Aigles 3-2 Requins, Loups 2-0 Tigres
classement : 1.Lions 13pts(+8) 2.Ours 8pts(+1) 3.Aigles 7pts(-1) 4.Requins 5pts(-2) 5.Loups 4pts(-1) 6.Tigres 3pts(-5)
```
