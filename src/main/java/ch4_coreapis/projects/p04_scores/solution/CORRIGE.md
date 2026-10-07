# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Scores.java`](Scores.java).
>
> Les valeurs, les messages d'erreur et les exceptions ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Trier sans abîmer l'original

**Le code de l'étape :**

```java
int[] sorted = Arrays.copyOf(a, a.length);
Arrays.sort(sorted);
System.out.println("TRIEES : " + Arrays.toString(sorted) + ", original intact : " + Arrays.toString(a));
```

**Question — `int[] copy = notes; Arrays.sort(copy);` ?** Cela trie **l'original**. `copy = notes` copie la **référence**, pas le tableau : les deux variables désignent le **même** objet. Vérifié : après le tri, `notes` vaut `[1, 2, 3]`, et `copy == notes` vaut `true`.

---

## Étape 2 — Statistiques et types de `Math`

**Le code de l'étape :**

```java
int sum = 0;
int min = Integer.MAX_VALUE;
int max = Integer.MIN_VALUE;
for (int note : a) {
    sum += note;
    min = Math.min(min, note);
    max = Math.max(max, note);
}
double mean = (double) sum / a.length;
double variance = 0;
for (int note : a) {
    variance += Math.pow(note - mean, 2);
}
double deviation = Math.sqrt(variance / a.length);

static double median(int[] sorted) {
    int n = sorted.length;
    return n % 2 == 1 ? sorted[n / 2] : (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
}
```

**Les calculs :**
- Somme : 580. Moyenne : 580 / 8 = **72.5**. Sans le cast, `580 / 8` donnerait **72**.
- Médiane : les 4e et 5e valeurs triées sont 72 et 78, donc (72 + 78) / 2.0 = **75.0**.

**Les types de retour (vérifiés) :**

| Appel | Type | Exemple |
|---|---|---|
| `Math.round(double)` | `long` | `Math.round(2.5)` donne `3` |
| `Math.round(float)` | `int` | `Math.round(2.5f)` donne `3` |
| `Math.ceil`, `Math.floor` | `double` | `Math.ceil(72.5)` donne `73.0` |
| `Math.pow` | `double` | `Math.pow(2, 10)` donne `1024.0` |

Piège vérifié : `int r = Math.round(2.5);` ne compile pas, avec `error: incompatible types: possible lossy conversion from long to int`.

**Question — pourquoi `Math.round(-2.5)` vaut −2 ?** `Math.round(x)` est défini comme `floor(x + 0.5)`. Pour −2.5 : `floor(-2.0)` = **−2**. L'arrondi se fait **vers le haut** (vers +∞) pour les « moitiés », pas « loin de zéro ». Vérifié : `round(2.5)` = 3, `round(-2.5)` = −2, `round(-2.6)` = −3, `round(-2.4)` = −2.

---

## Étape 3 — Rechercher

**Le code de l'étape :**

```java
static int binarySearch(int[] sorted, int key) {
    int low = 0;
    int high = sorted.length - 1;
    while (low <= high) {
        int mid = (low + high) >>> 1;
        if (sorted[mid] < key) {
            low = mid + 1;
        } else if (sorted[mid] > key) {
            high = mid - 1;
        } else {
            return mid;
        }
    }
    return -(low + 1);
}
```

**La trace pour 60** dans `[47, 58, 64, 72, 78, 85, 85, 91]` :
- mid = 3 (72 > 60), donc high = 2 ;
- mid = 1 (58 < 60), donc low = 2 ;
- mid = 2 (64 > 60), donc high = 1.

La boucle s'arrête avec low = 2 : 60 s'insérerait en position 2. Résultat : −(2 + 1) = **−3**.

**`>>> 1` plutôt que `/ 2` :** si `low + high` dépasse `Integer.MAX_VALUE`, la somme devient négative. `>>> 1` la relit comme un nombre positif. C'est le correctif historique d'un bug du JDK.

**Questions :**
- **Pourquoi « −1 » ?** Sans lui, un point d'insertion **0** donnerait `-0` = 0, impossible à distinguer de « trouvé à l'indice 0 ». Avec `-(0) - 1` = **−1**, tout résultat négatif signifie « absent ». Vérifié : chercher 10 dans `[47, 58]` donne −1, chercher 47 donne 0.
- **Avec des doublons :** la Javadoc ne garantit **pas lequel** est trouvé. Vérifié : dans `[1, 2, 2, 2, 3]`, chercher 2 donne l'indice 2, celui du milieu, pas le premier.
- **Tableau non trié :** le résultat est **non défini**. Pas d'exception, juste une réponse fausse. Vérifié : dans `[5, 1, 4, 2]`, chercher 5 donne **−5** (« absent »), alors que 5 est à l'indice 0.

---

## Étape 4 — Fusionner, faire tourner, comparer

**Le code de l'étape :**

```java
static int[] merge(int[] a, int[] b) {
    int[] result = new int[a.length + b.length];
    int i = 0, j = 0, k = 0;
    while (i < a.length && j < b.length) {
        result[k++] = a[i] <= b[j] ? a[i++] : b[j++];
    }
    while (i < a.length) { result[k++] = a[i++]; }
    while (j < b.length) { result[k++] = b[j++]; }
    return result;
}

static void rotateRight(int[] t, int k) {
    k %= t.length;
    reverse(t, 0, t.length - 1);
    reverse(t, 0, k - 1);
    reverse(t, k, t.length - 1);
}
```

**La complexité de la fusion :** **O(n + m)**. Chaque élément est copié exactement une fois. C'est la base du tri fusion.

**La rotation à la main :**

| Étape | Tableau |
|---|---|
| départ | `[50, 66, 70, 88, 95]` |
| inverser tout | `[95, 88, 70, 66, 50]` |
| inverser [0, 1] | `[88, 95, 70, 66, 50]` |
| inverser [2, 4] | `[88, 95, 50, 66, 70]` |

**Les comparaisons :**
- **`Arrays.equals(B, copie)`** vaut `true` (mêmes valeurs), et **`==`** vaut `false` (deux objets).
- **`compare(B, plus long)`** vaut −1 : même préfixe, et B est plus **court**.
- **`compare(A trié, B)`** vaut −1 : 47 < 50 dès la 1re case.
- **`mismatch(B, rotation)`** vaut 0 : 50 contre 88, dès la case 0.
- **`mismatch(B, copie)`** vaut −1 : aucune différence.

**Question — la valeur ajoutée par `copyOf(B, 6)` :** la **valeur par défaut** du type, donc `0` pour un `int[]` et `null` pour un `String[]`. Vérifié : `copyOf({1, 2}, 4)` donne `[1, 2, 0, 0]`, et `copyOf({"a"}, 2)` donne `[a, null]`.

---

## Étape 5 — Noms, podium, tableaux irréguliers, valeurs par défaut

**Le code de l'étape :**

```java
String[] names = Arrays.copyOf(Data.NAMES, Data.NAMES.length);
Arrays.sort(names);

for (int rank = 0; rank < 3; rank++) {
    int note = sorted[sorted.length - 1 - rank];
    String who = "";
    for (int i = 0; i < a.length; i++) {
        if (a[i] == note) {
            who = who.isEmpty() ? Data.NAMES[i] : who + "/" + Data.NAMES[i];
        }
    }
    podium += (rank + 1) + ". " + who + " " + note + " (+" + Data.BONUSES[rank][0] + ", " + Data.BONUSES[rank].length + " bonus possibles) ";
}

int[] empty = new int[3];
String[] noNames = new String[2];
double[][] grid = new double[2][];
Arrays.fill(empty, 7);
```

**Le tri des `String` :** il suit l'ordre des codes Unicode : `'B'` = 66 et `'Z'` = 90 passent avant `'a'` = 97. D'où `Bob, Hugo, Zoe` avant `adam`.

**Le tableau irrégulier :** `{{5, 3, 1}, {3, 1}, {1}}` a des lignes de **3, 2 et 1** cases (vérifié). Chaque ligne est un tableau indépendant, de taille libre.

**Pourquoi 85 apparaît deux fois au podium :** les rangs 2 et 3 du tableau trié valent tous les deux 85. Le programme cherche les élèves **par note** : il trouve donc Hugo et Bob deux fois. C'est voulu, pour montrer les ex æquo.

**Question — `new double[2][]` :** un tableau de **2 références** de type `double[]`, qui valent **`null`**. Les **lignes** n'ont pas été créées : il n'y a encore aucun `double`, donc pas de `0.0`. Vérifié : `Arrays.toString(g)` donne `[null, null]`, et lire `g[0][0]` lève :

```
java.lang.NullPointerException: Cannot load from double array because "<local2>[0]" is null
```

**`Math.min(-0.0, 0.0)`** donne `-0.0` : pour les `double`, Java considère −0.0 plus petit que 0.0.
