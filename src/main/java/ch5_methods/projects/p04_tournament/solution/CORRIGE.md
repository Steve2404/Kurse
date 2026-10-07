# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Tournament.java`](Tournament.java).
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Ce qui change, ce qui ne change pas

**Le code :** les méthodes `swap` (deux surcharges), `touch`, `reassign`, `mutate`, `tryChange` et `increment`, et le début du `main`.

**Le dessin, ligne par ligne :**

| Appel | Dans la méthode | Chez l'appelant |
|---|---|---|
| `swap(a, b)` | échange ses **copies** `a` et `b` | `a=1 b=2`, inchangés |
| `swap(pair, 0, 1)` | `arr` pointe vers **le même** tableau, et ses cases sont échangées | `[2, 1]` |
| `touch(sb1, sb2)` | `x.append('!')` modifie l'objet commun ; `y = new …` fait pointer la copie `y` ailleurs | `Lions!`, et `Ours` intact |
| `reassign(numbers)` | `arr` pointe vers un **nouveau** tableau `{9, 9, 9}` | `[1, 2, 3]`, inchangé |
| `mutate(numbers)` | `arr[0] *= 10` sur le tableau commun | `[10, 2, 3]` |
| `tryChange(s, n)` | `s += "!"` crée un **nouveau** `String` local ; `n++` crée un **nouvel** `Integer` local | `Lions 5`, inchangés |
| `increment(counter);` | rend 6, mais le résultat n'est rangé nulle part | `counter` vaut toujours 5 |
| `returned = increment(counter)` | rend 6 | `returned` vaut 6 |

**À retenir :** Java passe **toujours par valeur**. Pour un objet, la valeur copiée est la **référence**. On peut donc modifier l'objet partagé, mais jamais faire pointer la variable de l'appelant ailleurs. Pour « changer » un primitif ou un objet immuable, il faut **rendre** la nouvelle valeur.

---

## Étape 2 — L'autoboxing

**Le code :** le bloc « cache Integer » et « unboxing » du `main`.

**Les comparaisons :**
- **`small1 == small2`** pour 127 vaut `true` : `Integer.valueOf` rend le **même** objet, tiré du cache −128..127.
- **`big1 == big2`** pour 128 vaut `false` : deux objets distincts. `big1.equals(big2)` vaut `true`.
- **`five.equals(5)`** vaut `false` : le `5` est emballé en **`Integer`**, et `Long.equals` exige un `Long`. Vérifié : `five.equals(5L)` vaut `true`.
- **`five == 5`** vaut `true` : avec un primitif d'un côté, Java **déballe** le `Long` et compare des nombres.
- **`big1 < 200`** : `<` n'existe que pour les nombres, donc Java déballe automatiquement.

**Expérience — sans le test du `null` :**

```
java.lang.NullPointerException: Cannot invoke "java.lang.Integer.intValue()" because "<local6>" is null
```

`total += x` déballe `x` avec `x.intValue()`, appelé sur `null`. Le message le montre : l'unboxing est un **appel de méthode** caché.

**Expérience — `Double d = 5;` contre `Double.valueOf(5)` :**
- `Double d = 5;` ne compile pas : `error: incompatible types: int cannot be converted to Double`. Il faudrait **élargir** (`int` vers `double`) **puis emballer** (`double` vers `Double`), et Java refuse de combiner ces deux conversions dans une affectation. Même erreur pour `Long l = 5;` (vérifié).
- `Double.valueOf(5)` compile, car `valueOf` attend un **`double` primitif**. Passer un `int` à un paramètre `double` est un simple élargissement, et la méthode rend elle-même le `Double`. Affichage : `5.0`.

**`Character letter = 'A'; (char) (letter + 1)` :** `letter + 1` déballe le `Character`, puis fait l'arithmétique en `int` (66). Le cast redonne `'B'`.

---

## Étape 3 — Les permutations

**Le code :** les méthodes `permute`, `swap(char[], …)`, `next`, `rank` et `factorial`.

**Les trois surcharges de `swap` :** `swap(int, int)`, `swap(int[], int, int)` et `swap(char[], int, int)`. Elles portent le même nom avec des **listes de paramètres différentes**, et `javac` choisit selon les types des arguments.

**Le retour arrière :** après `permute(c, k + 1, out)`, le tableau doit être **remis dans son état** pour que l'essai suivant de la boucle parte de la bonne base. D'où le second `swap`. Remarque : l'ordre obtenu (`ABC ACB BAC BCA CBA CAB`) n'est **pas** lexicographique, à cause des échanges.

**Question — pourquoi le compteur doit être `static` (ou rendu) ?** Chaque appel récursif a ses **propres** variables locales. Un `int count` local serait un compteur différent à chaque niveau, perdu au retour. Le compteur `static` est **partagé** par tous les appels. L'autre solution est de faire rendre à `permute` le nombre trouvé, et d'additionner les retours.

**`next` sur `ACDB` :**
1. Le plus grand i avec `c[i] < c[i+1]` : i = 1 (`C < D`).
2. Le plus grand j avec `c[j] > c[1]` : j = 2 (`D`).
3. Échange : `ADCB`.
4. Inverse la fin `CB` : on obtient **`ADBC`**, la suivante.

**Le rang de `CADB` à la main :**

| Position | Lettre | Plus petites après elle | × (n−1−i)! |
|---|---|---|---|
| 0 | C | A, B : 2 | × 3! = 12 |
| 1 | A | aucune : 0 | × 2! = 0 |
| 2 | D | B : 1 | × 1! = 1 |
| 3 | B | 0 | × 0! = 0 |

13 permutations viennent **avant** CADB, donc son rang est **14** (vérifié en direct).

**« le tableau est resté sur DCBA » :** quand `next` rend `false`, le tableau contient la **dernière** permutation. `next` modifie le tableau **en place**, et les effets sont visibles après la boucle.

---

## Étape 4 — Le calendrier et le classement

**Le code :** la fin du `main`, et les méthodes `record` et `before`.

**La méthode du cercle :** l'équipe 0 reste fixe, et les autres tournent d'un cran à chaque journée. Sur 5 journées, chaque équipe rencontre une fois chacune des 5 autres. Journée 1, `ring = [0,1,2,3,4,5]` : les matchs sont 0-5, 1-4 et 2-3, soit Lions-Requins, Ours-Tigres et Aigles-Loups.

**Le premier score :**
- Lions à domicile : `(5 lettres + journée 1) % 4` = 6 % 4 = **2**.
- Requins à l'extérieur : `(0 × 2 + 5 + 1) % 3` = 6 % 3 = **0**.

On obtient `2-0`.

**`record` remplit les tableaux de l'appelant :** `points` et `diff` sont des **références** vers les tableaux du `main`. `points[home] += 3` modifie donc ces tableaux, sans `return`. C'est l'application directe de l'étape 1.

**Trier des indices plutôt que les équipes :** les trois tableaux `teams`, `points` et `diff` sont **parallèles**. Trier un tableau `order` d'indices garde leur alignement intact (voir le projet 6 du chapitre 4).
