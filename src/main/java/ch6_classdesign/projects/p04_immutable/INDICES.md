# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.
>
> **Les 5 règles d'une classe immuable**, valables pour les trois classes :
> 1. `final class` ;
> 2. des champs `private final` ;
> 3. aucun setter ;
> 4. des copies défensives des objets modifiables, à l'entrée **et** à la sortie ;
> 5. chaque « modification » rend un **nouvel** objet.

---

## Étape 1 — `Money`

<details><summary>Indice 1</summary>

`plus` : `currency.equals(other.currency) ? new Money(cents + other.cents, currency) : null`. Depuis la classe `Money`, on peut lire `other.cents` même s'il est `private` : l'accès privé est **par classe**, pas par objet.

</details>

<details><summary>Indice 2</summary>

- `allocate` : la somme des ratios, puis `cents * ratio / total` pour chaque part, en soustrayant au fur et à mesure du reste. Puis une boucle qui donne +1 aux parts 0, 1, 2… tant qu'il reste des centimes.
- `hashCode` : `31 * Long.hashCode(cents) + currency.hashCode()`.

</details>

---

## Étape 2 — `Fraction`

<details><summary>Indice 1</summary>

Le constructeur reçoit `num` et `den`, calcule `g = gcd(|num|, |den|)`, inverse les deux signes si `den < 0`, puis affecte `num / g` et `den / g`. Attention : `gcd(0, 0)` doit rendre au moins 1 (`Math.max(a, 1)`).

</details>

<details><summary>Indice 2</summary>

- `a/b + c/d = (ad + cb) / bd` ;
- `a/b ÷ c/d = ad / bc` ;
- H(10) : une boucle qui part de `Fraction.ZERO` et fait `h = h.plus(of(1, i))`.

On écrit bien `h = …` : `plus` ne modifie pas `h`.

</details>

---

## Étape 3 — `Matrix` et les copies défensives

<details><summary>Indice 1</summary>

Une méthode `private static long[][] copy(long[][] src)` : un nouveau tableau de lignes, et chaque ligne `src[i].clone()`. `of` l'utilise à l'entrée, `toArray` à la sortie.

</details>

<details><summary>Indice 2</summary>

`power(n)` : si n vaut 0, `identity`. Sinon `half = power(n / 2)`, puis `sq = half.times(half)`, et encore `sq.times(this)` si n est impair.

</details>

---

## Étape 4 — Le déterminant exact

<details><summary>Indice 1</summary>

Copie les cases dans un `Fraction[][]`. `det = ONE`. Pour chaque colonne : cherche la 1re ligne `>= col` où la case n'est pas nulle. S'il n'y en a pas, rends `ZERO`.

</details>

<details><summary>Indice 2</summary>

Après l'échange éventuel (`det = det.times(of(-1))`), `det = det.times(a[col][col])`. Pour chaque ligne r sous le pivot : `factor = a[r][col] / a[col][col]`, puis `a[r][c] = a[r][c] - factor × a[col][c]`.

</details>
