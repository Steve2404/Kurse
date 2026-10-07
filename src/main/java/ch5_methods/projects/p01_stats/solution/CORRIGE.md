# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Stats.java`](Stats.java) (les méthodes) et [`StatsReport.java`](StatsReport.java) (les appels).
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — `sum` et `count` : un varargs est un tableau

**Le code de l'étape :**

```java
public static int sum(int... values) {
    int total = 0;
    for (int v : values) {
        total += v;
    }
    return total;
}

public static int count(int... values) {
    if (values == null) {
        return -1;
    }
    return values.length;
}
```

**Question — que contient `values` sans argument ?** Un tableau **vide**, de longueur 0, jamais `null`. Vérifié : `values == null` vaut `false`, et `values.length` vaut 0. C'est pour ça que `sum()` rend 0 sans planter.

**Expérience — `count(null)` sans cast :**
- avec `int...`, ça **compile sans rien dire**, et `values` vaut `null` : rendu −1 (vérifié). `null` ne peut pas être un `int`, donc `javac` le prend pour le **tableau** `int[]` lui-même ;
- avec `Object...`, ça compile, mais avec un **avertissement** :

```
warning: non-varargs call of varargs method with inexact argument type for last parameter;
  cast to Object for a varargs call
  cast to Object[] for a non-varargs call and to suppress this warning
```

`null` pourrait être **un élément** (`Object`) ou **le tableau** (`Object[]`). `javac` choisit le tableau, mais il te demande de lever l'ambiguïté avec un cast.

---

## Étape 2 — Un paramètre fixe avant le varargs

**Le code de l'étape :**

```java
public static double average(int first, int... rest) {
    return round2((first + sum(rest)) / (1.0 + rest.length));
}

public static int max(int first, int... rest) {
    int best = first;
    for (int v : rest) {
        best = Math.max(best, v);
    }
    return best;
}

private static double round2(double x) {
    return Math.round(x * 100) / 100.0;
}
```

**Expérience — `average()` :**

```
error: method average in class M cannot be applied to given types;
  required: int,int[]
  found:    no arguments
  reason: actual and formal argument lists differ in length
```

Le varargs peut être vide, mais `first` est obligatoire. Remarque que `javac` affiche le varargs comme un **`int[]`**.

**Le piège :** `average(t[0], t)` fait la somme 12 + 149 = 161 sur 11 valeurs, soit 14.64. Le 12 est compté deux fois. La moyenne juste vaut 149 / 10 = **14.9**.

**Questions :**
- **`int... rest, int first`** ne compile pas : `error: varargs parameter must be the last parameter`. Si le varargs n'était pas le dernier, `javac` ne saurait pas où il s'arrête.
- **Deux varargs dans la même méthode** : **même erreur** (vérifié). Le premier ne serait pas en dernière position. Il y a donc **au plus un** varargs, et toujours en dernier.

---

## Étape 3 — Rendre des tableaux, sans abîmer celui de l'appelant

**Le code :** les méthodes `median`, `modes`, `kth`, `partition`, `swap` et `movingAverage` de [`Stats.java`](Stats.java).

**Les points clés :**
- `median` et `modes` commencent par `Arrays.copyOf(values, values.length)`, et `kth` par `values.clone()`. Quand on appelle `median(t)`, `values` **est** le tableau `t` de l'appelant. Le trier sans copie modifierait `t`.
- **Modes :** dans `TEMPS`, 15 apparaît 3 fois. Le résultat est `[15]`. Dans `1, 1, 2, 2, 3`, 1 et 2 apparaissent deux fois : `[1, 2]`.
- **Quickselect :** la partition place le pivot à sa **position définitive** p. Si p vaut k − 1, c'est la réponse. Sinon, on ne continue que d'**un** côté. C'est O(n) en moyenne, contre O(n log n) pour un tri complet.

**Question — pourquoi l'échange se voit-il chez l'appelant ?** Java passe tout **par valeur**, mais pour un tableau, la valeur copiée est une **référence**. La méthode reçoit une copie de la flèche, qui pointe vers **le même** tableau. Modifier `a[i]` modifie donc ce tableau. En revanche, **réaffecter** le paramètre (`a = new int[] {9, 9};`) ne change que la copie locale de la flèche. Vérifié : après `swap(t, 0, 1)` puis `reassign(t)`, `t` vaut `[2, 1]`. L'échange se voit, la réaffectation non.

**La moyenne glissante :** n − window + 1 = 10 − 3 + 1 = **8** valeurs. La 1re vaut (12 + 15 + 9) / 3 = 12.0.

---

## Étape 4 — Varargs de tableaux et d'objets

**Le code :** les méthodes `concat`, `join` et `describe`.

**`concat(int[]... arrays)` :** `arrays` est un `int[][]`. `concat()` sans argument donne un `int[][]` vide, donc un résultat de longueur **0**.

**`join("/", Data.LABELS)` :** un `String[]` passé à un `String...` est utilisé **tel quel** comme tableau. Il n'est pas emballé dans un autre tableau.

**Les quatre `describe` :**

| Appel | `items` vaut | Résultat |
|---|---|---|
| `describe(1, "deux", 3.0, 'c')` | `{Integer, String, Double, Character}` (autoboxing) | `4 element(s) : 1 deux 3.0 c` |
| `describe()` | tableau vide | `0 element(s) :` |
| `describe((Object) null)` | `{null}` : **un** élément | `1 element(s) : null` |
| `describe((Object[]) null)` | `null` : le tableau lui-même | `tableau null` |

**Question — pourquoi un `int[]` ne devient pas un `Object[]` ?** Un `Object[]` contient des **références**, alors qu'un `int[]` contient des `int` **primitifs**, qui ne sont pas des objets. Il n'y a aucun lien de type entre `int[]` et `Object[]` :

```
error: incompatible types: int[] cannot be converted to Object[]
```

Un `String[]`, lui, **est** un `Object[]` (un `String` est un `Object`). Les tableaux de références sont *covariants*. Conséquence vérifiée :
- `describe(new int[] {1, 2})` reçoit **1** élément, le tableau entier (`int[]`) ;
- `describe(new String[] {"x", "y"})` reçoit **2** éléments.

---

## Étape 5 — Un histogramme et une sortie anticipée

**Le code :** la méthode `histogram`.

**`return;` dans une méthode `void` :** il sort immédiatement. Le reste de la méthode, ici le dessin, n'est pas exécuté. Pas de valeur après `return`, puisque la méthode ne rend rien.

**Le paramètre `String[] labels, int... counts` :** le tableau d'étiquettes est un paramètre **normal**, avant le varargs. L'appel `histogram(Data.LABELS, 1, 2)` passe donc un tableau, puis **deux** `int` qui forment le varargs : 5 étiquettes contre 2 valeurs, d'où le message.

**Le dessin :** pour `VOTES = {3, 5, 1, 4, 2}`, le niveau 5 n'a que `mar` (5). Au niveau 4, `mar` et `jeu`. Chaque colonne fait 4 caractères (` ## `), ce qui aligne les étiquettes (`%-4s`) dessous.
