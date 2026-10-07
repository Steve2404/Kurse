# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Money`, `Fraction`, `Matrix` et `ImmutableLab`.
>
> Les messages et les sorties ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18), sur une copie de la solution.

---

## Étape 1 — `Money`

**Le code :** [`Money.java`](Money.java).

**Pourquoi `price` est inchangé :** `plus` ne touche pas `this`, elle **rend un nouvel objet**. C'est la même logique que `String` au chapitre 4. Une variable qui « change » doit être réaffectée : `total = price.plus(…)`.

**`allocate` sans perdre de centime :**
- 10000 / 3 = 3333 par part, soit 9999 au total. Il reste **1** centime, donné à la 1re part : 33.34, 33.33, 33.33.
- Pour 7 centimes en 50/30/20, les parts entières valent 3, 2 et 1 (6 au total), et le centime restant va à la 1re : **0.04, 0.02, 0.01**.

Arrondir chaque part séparément donnerait une somme fausse (3.5 + 2.1 + 1.4 arrondis, etc.).

**`equals` et `hashCode` :**
- `equals` redéfini : deux `Money` de même montant **et** même devise sont égales, même si ce sont deux objets différents (`==` vaut `false`).
- **Le contrat :** deux objets égaux selon `equals` **doivent** avoir le même `hashCode`. Sinon, une `HashSet` ou une `HashMap` (chapitre 9) les rangerait dans des cases différentes et ne les retrouverait pas.
- `o instanceof Money m && …` gère aussi `null` : `null instanceof X` vaut `false`.

---

## Étape 2 — `Fraction`

**Le code :** [`Fraction.java`](Fraction.java).

**La normalisation dans le constructeur :** `of(6, -8)` donne un PGCD de 2, donc `3/-4`, puis le signe passe en haut : **`-3/4`**. Comme **tout** objet passe par ce constructeur, chaque fraction a une forme **unique**. `equals` peut donc comparer les champs directement : `2/4` est stocké `1/2`, donc `of(2, 4).equals(of(1, 2))` vaut `true`.

**Les calculs :**
- `1/3 + 1/6` = (6 + 3) / 18 = 9/18 = **1/2** ;
- `2/3 ÷ 4/9` = 18/12 = **3/2**.

**`ZERO` et `ONE`** : des constantes partagées sans danger. Un objet immuable peut être partagé partout, puisque personne ne peut le modifier.

---

## Étape 3 — `Matrix` et les copies défensives

**Le code :** [`Matrix.java`](Matrix.java), sans `determinant`.

**Pourquoi deux copies ?** `final long[][] cells` empêche seulement de **réaffecter** `cells`. Les **cases** restent modifiables, par quiconque a une référence vers le même tableau :
- **à l'entrée** : sans copie, `raw[0][0] = 99` modifie la matrice, puisqu'elle **est** `raw` ;
- **à la sortie** : sans copie, `out[1][1] = 99` modifie aussi la matrice.

**La copie doit être profonde :** `source.clone()` seul copierait le tableau de **lignes**, mais les lignes elles-mêmes resteraient partagées. On copie donc chaque ligne.

**Le constructeur `private` qui ne copie pas** n'est appelé que de l'intérieur, avec un tableau tout neuf (résultat de `times`, `transpose`…). Le copier une 2e fois serait inutile.

**Fibonacci :** `{{1,1},{1,0}}ⁿ = {{F(n+1), F(n)}, {F(n), F(n−1)}}`. La puissance rapide calcule F(90) en ~7 multiplications de matrices au lieu de 89 additions.

---

## Étape 4 — Le déterminant exact

**Le code :** la méthode `determinant()` de [`Matrix.java`](Matrix.java).

**Pourquoi des `Fraction` et pas des `double` ?** L'élimination divise par les pivots. Avec des `double`, `HALVES` donnerait un résultat comme 4.999999…, et une matrice singulière, un « presque 0 ». Les fractions sont **exactes**, et c'est l'immuabilité qui les rend faciles à manipuler.

**`det(A³) = det(A)³`** : 49³ = **117649**. C'est une vérification croisée de `power` **et** de `determinant`.

**Expériences :**
- **`class Euro extends Money`** (vérifié) :

  ```
  error: cannot inherit from final Money
  error: constructor Money in class Money cannot be applied to given types;
  ```

  `final` interdit toute sous-classe. Sans `final`, une sous-classe pourrait ajouter un champ modifiable, ou redéfinir `plus` pour modifier l'objet, ce qui casserait la garantie. Le constructeur `private` ferme aussi la porte : une sous-classe ne pourrait pas l'appeler.
- **Un setter dans `Money`** : la garantie disparaît. Un objet partagé (en constante, dans une collection, dans une autre `Money`…) pourrait changer « dans le dos » de ses utilisateurs, et son `hashCode` changerait, ce qui le perdrait dans une `HashSet`. Et `cents` ne pourrait plus être `final`.
- **Retirer la copie de `of(...)`** (vérifié) : la ligne devient `copies defensives : [[99, 2], [3, 4]] intacte ; transposee [[99, 3], [2, 4]] ; carre [[9807, 206], [309, 22]] ; egal a lui-meme reconstruit false`. La matrice a bougé en même temps que `raw`, et tous les calculs suivants sont faux.
