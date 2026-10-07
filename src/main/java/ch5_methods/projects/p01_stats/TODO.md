# Projet 1 — La boîte à outils statistique

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

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

**Ce que le chapitre 5 t'apprend :** à **écrire tes propres méthodes**, et à bien les ranger :
- qui peut les appeler ;
- `static` ou pas ;
- ce qui arrive aux valeurs qu'on leur passe ;
- plusieurs méthodes du même nom ;
- une méthode qui s'appelle elle-même.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch5-p01 -sourcepath src/main/java src/main/java/ch5_methods/projects/p01_stats/StatsReport.java
java "-Duser.language=fr" -cp build/ch5-p01 ch5_methods.projects.p01_stats.StatsReport
```

`-sourcepath` : `javac` trouve tout seul `Data.java` et tes autres fichiers (chapitre 4, projet 1).

---

## Tableau de bord

### ☐ Étape 1 — `sum` et `count` : un varargs est un tableau

```
somme : liste 6, tableau 149, rien 0
compte : 10 0 0 -1
```

**📖 La leçon : l'anatomie d'une méthode.** Tu en écris depuis le chapitre 1. Voici tous ses morceaux, dans l'ordre :

```java
static          int          compte  (String... mots)  {  return mots.length;  }
//  modificateurs  type rendu    nom      paramètres          corps
```

- **le type rendu** : ce que la méthode donne en retour, ou `void` si elle ne rend rien ;
- **`return valeur;`** rend la valeur **et arrête** la méthode tout de suite ;
- **les paramètres** : les valeurs reçues, chacune avec son type.

**📖 La leçon : les varargs, « autant de valeurs que tu veux ».** Tu as vu `String... args` au chapitre 1. **À l'intérieur** de la méthode, un varargs est simplement un **tableau** :

```java
static int compte(String... mots) {
    return mots.length;                  // mots est un String[]
}
compte()                                 // 0 : aucune valeur, un tableau vide
compte("a", "b")                         // 2
compte(new String[] {"x"})               // 1 : on peut aussi passer directement un tableau
```

**👉 À toi :**

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

**📖 La leçon : un paramètre fixe, puis le varargs.** Un varargs doit être le **dernier** paramètre, et il n'y en a qu'un par méthode. Des paramètres ordinaires peuvent le précéder, et ils sont **obligatoires** :

```java
static String salue(String politesse, String... noms) {
    String r = politesse;
    for (String n : noms) r = r + " " + n;
    return r;
}
salue("Bonjour")                 // "Bonjour" : noms est vide
salue("Salut", "Lea", "Tom")     // "Salut Lea Tom"
```

**📖 La leçon : `private`, une méthode pour soi.** Une méthode `private` ne peut être appelée **que depuis sa propre classe**. On s'en sert pour les petits outils internes, que personne d'autre n'a besoin de connaître.

**👉 À toi :**

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

**📖 La leçon : une méthode qui rend un tableau.** Le type rendu peut être `int[]`, `double[]`… La méthode crée le tableau, le remplit, puis le rend avec `return`.

**📖 La leçon : ne pas abîmer le tableau de l'appelant.** Un tableau passé en paramètre n'est **pas copié** : la méthode reçoit une **2e étiquette** sur le même tableau (chapitre 1, projet 3, étape 5). Si elle le trie ou le modifie, l'appelant le voit. Pour travailler tranquille : `Arrays.copyOf(t, t.length)` ou `t.clone()` d'abord (chapitre 4, projet 4).

**👉 À toi :**

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

**📖 La leçon : un varargs d'objets.** `Object... items` accepte n'importe quelles valeurs : les primitifs sont automatiquement **emballés** dans leur classe enveloppe (`5` devient un `Integer`, `'c'` un `Character`). Avec `null`, Java hésite : est-ce **une** valeur nulle, ou le **tableau** lui-même qui est nul ? Le cast lève l'ambiguïté. L'étape te fait observer les deux cas.

**👉 À toi :**

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

**📖 La leçon : sortir tôt d'une méthode `void`.** Dans une méthode qui ne rend rien, `return;` (sans valeur) l'arrête tout de suite. C'est pratique pour refuser un cas impossible dès le début :

```java
static void imprime(String... lignes) {
    if (lignes.length == 0) {
        System.out.println("rien a imprimer");
        return;                       // la suite n'est pas exécutée
    }
    for (String l : lignes) System.out.println(l);
}
```

**👉 À toi :**

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
