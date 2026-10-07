# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`League.java`](League.java).
>
> Les messages et les sorties ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18), sur la solution ou sur des copies volontairement cassées.

---

## Étape 1 — Le modèle

**Le code :** les records `Match` et `Stats`.

**Question clé — les deux propriétés d'un monoïde :**
1. **`ZERO` est neutre** : `ZERO.plus(s)` et `s.plus(ZERO)` valent `s`, quel que soit s ;
2. **`plus` est associatif** : `(a.plus(b)).plus(c)` vaut `a.plus(b.plus(c))`.

Avec ces deux propriétés, on peut découper la saison **n'importe comment**, réduire chaque morceau, puis recombiner, et obtenir le même résultat. C'est ce qu'exige `reduce`, surtout en parallèle. Une addition champ par champ les vérifie toutes les deux.

---

## Étape 2 — `RESULTATS` : `collect` à 3 arguments

**Le code :** `RESULTS_ACCUMULATOR`, `RESULTS_COMBINER` et `results`.

**Le piège du combiner :** avec `StringBuilder::append`, deux morceaux se **collent** sans séparateur. Vérifié : `"A | B"` combiné avec `"C | D"` donne `A | BC | D`. Le combiner doit poser `" | "` entre deux morceaux **non vides**.

**Question — `collect` plutôt que `reduce("", …)` :** `reduce` avec des `String` crée une **nouvelle** chaîne à chaque élément, car les `String` sont immuables. Le coût est quadratique. `collect` est une réduction **mutable** : un seul `StringBuilder` par morceau, modifié sur place.

---

## Étape 3 — `BUTS` : `reduce` à 3 arguments

**Le code :** `GOALS_COMBINER` et `goals`.

**Question — pourquoi la forme à 2 arguments ne compile pas ?** `reduce(identité, accumulateur)` exige que **tout** soit du type des éléments : `BinaryOperator<Match>`. Ici, le résultat est un `int` et les éléments des `Match`. Vérifié :

```
error: no suitable method found for reduce(int,(s,m)->s + m.g())
    (argument mismatch; int cannot be converted to Mt)
```

La forme à 3 arguments accepte un type de résultat **différent** : l'accumulateur combine (résultat, élément), et le **combiner** combine deux résultats partiels. Le combiner n'est appelé que si le flux est **découpé** (en parallèle), jamais en séquentiel.

**Piège — l'identité `10` au lieu de `0` :**
- un seul passage ajoute 10 **une fois** ;
- deux morceaux réduits séparément l'ajoutent **deux fois**.

Vérifié sur 1, 2, 3 et 4 : un passage donne **20**, deux morceaux (1, 2) et (3, 4) donnent **30**. L'identité n'est pas **neutre** pour l'addition, donc le résultat dépend du découpage. Dans le projet, la ligne devient `BUTS : 45 …`, et le contrôle `COMBINER` répond `buts non`.

---

## Étape 4 — `PLUS LARGE VICTOIRE` : `reduce` sans identité

**Le code :** `biggestWin`.

**Question — pourquoi un `Optional` ?** Sans identité, il n'y a **rien** à rendre pour un flux vide (une saison sans victoire). `Optional` le dit explicitement.

**L'associativité à égalité :** l'opérateur `b.margin() > a.margin() ? b : a` garde **le plus à gauche** parmi les égaux. Pour x, y et z de même écart, `(x op y) op z` donne `x op z`, soit x, et `x op (y op z)` donne `x op y`, soit x aussi. Il est associatif, et donc correct par morceaux. Avec `>=`, il garderait le plus **récent**, et serait aussi associatif, mais ne respecterait plus la règle « le plus ancien ».

---

## Étape 5 — `CLASSEMENT` : ton propre `Collector`

**Le code :** `TABLE_ORDER` et `TABLE`.

**Les 4 parties :**
- **supplier** : `HashMap::new` (une table vide par morceau) ;
- **accumulator** : un match met à jour **deux** équipes avec `merge(équipe, bilan, Stats::plus)` ;
- **combiner** : verse la table de droite dans celle de gauche ;
- **finisher** : passe de la table à la liste triée.

**Lions contre Aigles, à la main :** même total, **11 points** (3 victoires, 2 nuls). On départage à la différence de buts : Lions 14:6 donne **+8**, Aigles 9:7 donne **+2**. Lions passe devant.

**Piège OCP :** `reversed()` placé à la **fin** d'une chaîne inverse **tous** les critères, y compris le nom, qui passerait de Z à A. Il faut inverser **chaque** critère numérique séparément, puis ajouter `thenComparing(Row::team)` en croissant.

---

## Étape 6 — `BILAN Lions` : `reduce` à 2 arguments

**Le code :** `seasonOf`.

**`reduce(Stats.ZERO, Stats::plus)`** : le type ne change pas (des `Stats` vers un `Stats`), donc 2 arguments suffisent. Le monoïde de l'étape 1 sert ici directement.

**Question — pourquoi la comparaison est gratuite ?** Un record **génère** `equals` à partir de ses composants. Deux `Stats` construits par des chemins différents, avec les mêmes 6 nombres, sont égaux, sans une ligne de code de comparaison.

---

## Étape 7 — `SERIES SANS DEFAITE` : l'algorithme fusionnable

**Le code :** le record `Segment`, `unbeaten` et `streaks`.

**Aigles à la main :** N N V V V D, coupé en `[N N V]` et `[V V D]`.
- **Gauche** : longueur 3, tout est sans défaite. Début 3, fin 3, meilleure 3.
- **Droite** : longueur 3. Début 2 (V V), fin 0 (le D), meilleure 2.
- **Fusion** :
  - début : la gauche est **entière** sans défaite, donc 3 + début(droite) = 3 + 2 = **5** ;
  - fin : celle de droite (0), car la droite n'est pas entière ;
  - meilleure : max(3, 2, fin(gauche) + début(droite) = 3 + 2) = **5**.

**Pourquoi « début = longueur » quand tout est sans défaite :** la série du début **traverse tout** le morceau, et elle peut se prolonger dans le morceau suivant.

**L'identité `(0, 0, 0, 0)`** : un morceau vide ne change rien à la fusion.

**`reduce((a, b) -> a + ", " + b)`** concatène sans `joining`. Sur 4 noms, le coût des `String` immuables est négligeable.

---

## Étape 8 — `CONTROLE`

**Le code :** les lignes `points` et `wins` de `report`.

**Le contrôle croisé :** chaque victoire distribue 3 points, et chaque nul 2 points (1 par équipe). 9 × 3 + 3 × 2 = **33**, le total du classement. Une erreur dans `statsFor` ou dans le `Collector` romprait cette égalité.

---

## Étape 9 — `COMBINER` : la preuve, à la main

**Le code :** `partial`, `combined`, et la fin de `report`.

**Question — l'ordre des appels du `Collector`, et combien de fois :** pour deux morceaux de 5 et 7 matchs :
1. `supplier()` : **2 fois** (un conteneur par morceau) ;
2. `accumulator()` : **12 fois** (une fois par match, dans son morceau) ;
3. `combiner()` : **1 fois** (fusionner les deux conteneurs) ;
4. `finisher()` : **1 fois** (sur le résultat final).

**Casser volontairement, vérifié sur des copies de la solution :**

| Modification | Ligne `COMBINER` |
|---|---|
| identité `10` au lieu de `0` pour les buts | `buts non` (et `BUTS : 45`) |
| combiner des buts qui ignore la gauche (`(l, r) -> r`) | `buts non`, alors que la ligne `BUTS` reste juste : un passage séquentiel n'appelle **jamais** le combiner |
| combiner des résultats sans séparateur | `resultats non` |

**La leçon :** un passage unique ne prouve **rien** sur le combiner, car il ne l'appelle pas. Seul un découpage le teste. C'est exactement ce qu'un stream **parallèle** ferait, sans prévenir.
