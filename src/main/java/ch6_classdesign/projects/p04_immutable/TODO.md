# Projet 4 — Les objets immuables

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 6) :** écrire une **classe immuable** selon les 5 règles de l'examen :
1. la classe est **`final`** (personne ne peut en hériter et ajouter un état modifiable) ;
2. tous les champs sont **`private final`** ;
3. **aucun setter** ;
4. **copies défensives** des objets mutables, à l'**entrée** (constructeur ou fabrique) et à la **sortie** (getter) ;
5. chaque « modification » rend un **nouvel objet**.

Et aussi :
- un **constructeur `private`** avec une fabrique `static` ;
- **`equals(Object)` et `hashCode()`**, redéfinis ensemble ;
- `toString()`.

Côté algorithmes :
- **répartition** d'une somme sans perte de centime ;
- fractions **normalisées** (PGCD) ;
- produit de matrices et **puissance rapide**, qui donnent Fibonacci ;
- **déterminant exact** par élimination de Gauss sur des fractions.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch6_classdesign.projects.p04_immutable` :
- `Money`, `Fraction`, `Matrix` (les trois `final`) ;
- **`ImmutableLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 6.
- Pas de `try/catch` : une addition de devises différentes rend `null`.
- Pas de `record` (chapitre 7) : c'est justement ce que tu écris à la main.

---

## Tableau de bord

### ☐ Étape 1 — `Money`

```
money : 19.99 EUR + 5.01 EUR = 25.00 EUR ; prix inchange 19.99 EUR ; 15 % = 3.75 EUR ; EUR + USD = null
partage 3 : [33.34 EUR, 33.33 EUR, 33.33 EUR] ; 50/30/20 de 0.07 : [0.04 EUR, 0.02 EUR, 0.01 EUR]
egalite : true false true false
```
- **Les champs et la création :**
  - `private final long cents` et `private final String currency` ;
  - un constructeur **`private`** ;
  - la fabrique `static Money of(long cents, String currency)`.
- **`plus(Money)`** rend un **nouveau** `Money`, ou `null` si les devises diffèrent.
- **`times(int percent)`** : `Math.round(cents * percent / 100.0)`.
- **`Money[] allocate(int... ratios)`** :
  1. chaque part reçoit `cents * ratio / total`, en division entière ;
  2. les centimes restants vont **un par un** aux premières parts.
  - La somme des parts égale toujours le total.
- **`equals(Object o)`** : `o instanceof Money m &&` même montant et même devise. **`hashCode()`** est cohérent avec `equals`.
- `toString()` donne `19.99 EUR`.
- **Les appels exacts :**
  - `price = of(1999, "EUR")` et `total = price.plus(of(501, "EUR"))` ;
  - `total.times(15)` ;
  - `price.plus(of(100, "USD"))` ;
  - `of(Data.BILL, "EUR").allocate(Data.SHARES)` et `of(7, "EUR").allocate(Data.WEIGHTS)` ;
  - pour l'égalité, avec `of(500, "EUR")` : `equals`, `==`, l'égalité des `hashCode`, puis `equals` avec `of(500, "USD")`.

### ☐ Étape 2 — `Fraction`

```
fractions : -3/4 1/2 3/2 H(10)=7381/2520 true
```
- **Le constructeur `private` normalise** :
  - il divise par le PGCD (récursif, en valeur absolue) ;
  - il met le signe au numérateur.
  - Après lui, l'objet est valide pour toujours.
- **Les membres :**
  - les constantes `ZERO` et `ONE` ;
  - les fabriques `of(num, den)` et `of(n)` ;
  - `plus`, `minus`, `times`, `divide`, `isZero` ;
  - `equals` et `hashCode` : la forme normalisée est unique, donc on compare les champs ;
  - `toString` : `7381/2520`, ou `2` si le dénominateur vaut 1.
- **Les appels exacts :**
  - `of(6, -8)` ;
  - `of(1, 3).plus(of(1, 6))` ;
  - `of(2, 3).divide(of(4, 9))` ;
  - H(10) = 1 + 1/2 + … + 1/10 ;
  - `of(2, 4).equals(of(1, 2))`.

### ☐ Étape 3 — `Matrix` et les copies défensives

```
copies defensives : [[1, 2], [3, 4]] intacte ; transposee [[1, 3], [2, 4]] ; carre [[7, 10], [15, 22]] ; egal a lui-meme reconstruit true
fibonacci par puissance : F(10)=55 F(50)=12586269025 F(90)=2880067194370816120
```
- **Les champs et la création :**
  - `private final long[][] cells` et un constructeur `private` qui reçoit un tableau **déjà neuf** ;
  - `static Matrix of(long[][] source)` **copie** chaque ligne ;
  - `long[][] toArray()` rend une **copie** ;
  - `identity(n)` et `get(r, c)`.
- **La preuve :** avec `raw = {{1, 2}, {3, 4}}`, crée `m = Matrix.of(raw)`, puis :
  - `raw[0][0] = 99` ;
  - `out = m.toArray(); out[1][1] = 99;`.
  
  `m` doit rester `[[1, 2], [3, 4]]`.
- **Les méthodes de calcul :**
  - `times(Matrix)` ;
  - `transpose()` ;
  - **`power(int n)`**, en puissance rapide **récursive** : chaque étape rend un nouvel objet ;
  - `equals` avec `Arrays.deepEquals`, `hashCode` avec `Arrays.deepHashCode`, `toString` avec `deepToString`.
- **Fibonacci :** pour `{{1, 1}, {1, 0}}`, F(n) est la case (0, 1) de la puissance n. Calcule F(10), F(50) et F(90).

### ☐ Étape 4 — Le déterminant exact

```
determinants : 49 30 0 5 ; det(A^3) = det(A)^3 : 117649
```
- **`Fraction determinant()`**, par élimination de Gauss sur un `Fraction[][]` :
  1. pour chaque colonne, cherche un pivot non nul (sinon, rends `ZERO`) ;
  2. un échange de lignes change le signe ;
  3. multiplie le résultat par le pivot ;
  4. élimine sous le pivot.
- Calcule-le pour `M3`, `M4`, `SINGULAR` et `HALVES`, puis pour `Matrix.of(M3).power(3)`.
- **Expériences :**
  - écris `class Euro extends Money` : quelle erreur ?
  - ajoute un setter à `Money` : que devient la garantie ?
  - retire le `clone()` de `of(...)` : que montre la ligne des copies défensives ?

---

## Checklist (vérifiée par `Check`)

- `Data.M3` et `Data.WEIGHTS` ;
- `final class Money`, `final class Fraction`, `final class Matrix` ;
- `private Money(` et `private Fraction(` ;
- `clone()` ;
- 3 `public boolean equals(Object` et 3 `public int hashCode()` ;
- `Arrays.deepEquals` et `Arrays.deepHashCode` ;
- **aucun** setter.

---

## Sortie attendue complète

```
money : 19.99 EUR + 5.01 EUR = 25.00 EUR ; prix inchange 19.99 EUR ; 15 % = 3.75 EUR ; EUR + USD = null
partage 3 : [33.34 EUR, 33.33 EUR, 33.33 EUR] ; 50/30/20 de 0.07 : [0.04 EUR, 0.02 EUR, 0.01 EUR]
egalite : true false true false
fractions : -3/4 1/2 3/2 H(10)=7381/2520 true
copies defensives : [[1, 2], [3, 4]] intacte ; transposee [[1, 3], [2, 4]] ; carre [[7, 10], [15, 22]] ; egal a lui-meme reconstruit true
fibonacci par puissance : F(10)=55 F(50)=12586269025 F(90)=2880067194370816120
determinants : 49 30 0 5 ; det(A^3) = det(A)^3 : 117649
```
