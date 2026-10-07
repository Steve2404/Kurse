# Projet 8 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet, avec une méthode par étape, est dans [`TextLab.java`](TextLab.java) : `anagrams`, `rle`, `caesar` et `ciphers`, `bigNumbers`, `prefixAndRotation`, `palindrome`, `justify`, `caseInsensitiveSort`, `romans` et `value`, `bases`.
>
> Les valeurs et les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Anagrammes

**Le code :** la méthode `anagrams()`.

**Les deux méthodes :**
- Le compteur de 26 cases est en **O(n)**.
- Le tri des lettres est en **O(n log n)**.

Elles doivent toujours être d'accord, et le `!` sert de garde-fou.

**Question — `apple` et `paple` ?** **Oui.** Les deux contiennent a, e, l, p, p. Triées, elles donnent toutes deux `aelpp` (vérifié). Les lettres sont **les mêmes avec les mêmes répétitions**, seul l'ordre change.

**Pour `Dormitory/dirty room`**, retirer l'espace est indispensable : sinon, `' ' - 'a'` donnerait un indice **négatif** (−65), et la ligne lèverait une `ArrayIndexOutOfBoundsException`.

---

## Étape 2 — Compression RLE

**Le code :** la méthode `rle()`.

**Pourquoi `number * 10 + (c - '0')` ?** Pour lire `12a`, `number` vaut 0, puis 0 × 10 + 1 = 1, puis 1 × 10 + 2 = 12, puis on rencontre `a`. `c - '0'` convertit le **caractère** chiffre en sa **valeur** (`'7' - '0'` = 7).

**Question — quand RLE allonge-t-il le texte ?** Quand il y a peu de répétitions : chaque lettre **isolée** coûte 2 caractères (`1a`). Vérifié : `abcd` devient `1a1b1c1d`, avec un gain de **−4**. RLE ne convient qu'aux données qui ont de longues séries (images à aplats, par exemple).

---

## Étape 3 — César et Vigenère

**Le code :** les méthodes `caesar()` et `ciphers()`.

**Question — pourquoi `26 - shift` et pas `-shift` ?** En Java, le signe de `%` suit le dividende : **`-1 % 26` vaut −1** (vérifié). Décoder `a` avec −3 donnerait `'a' + (0 - 3) % 26` = `'a' - 3`, le caractère **`^`** (code 94), qui n'est pas une lettre. Avec `26 - 3` = 23, le calcul reste positif : (0 + 23) % 26 = 23, soit `x`. On aurait aussi pu utiliser le modulo positif du chapitre 2.

**ROT13 deux fois :** 13 + 13 = 26 ≡ 0, on revient au texte. ROT13 est son propre inverse.

**`new String(char[])` et `String.valueOf(char[])`** font la même chose : créer un `String` à partir des caractères. L'énoncé demande les deux pour les connaître.

**Vigenère vérifié à la main sur les 3 premières lettres :**
- A + L(11) = **L** ;
- T(19) + E(4) = 23 = **X** ;
- T(19) + M(12) = 31 % 26 = 5 = **F**.

---

## Étape 4 — Grands nombres

**Le code :** la méthode `bigNumbers()`.

**Question — pourquoi pas un `long` ?** `Long.MAX_VALUE` = 9223372036854775807 a **19 chiffres**. Le résultat en a **21**, et chacun des deux nombres de départ en a déjà 20. Vérifié : le simple littéral `98765432109876543210L` ne compile pas, avec `error: integer number too large`. Il faudrait `BigInteger` (hors programme ici). L'algorithme à la main n'a pas de limite.

**La factorielle à l'envers :** stocker les unités en case 0 permet d'**ajouter des chiffres à la fin** du tableau quand le nombre grandit, sans rien décaler.

**Question — pourquoi exactement 6 zéros ?** Chaque zéro final vient d'un facteur **10 = 2 × 5**. Dans 25 !, il y a beaucoup plus de facteurs 2 que de 5, donc on compte les 5 :
- 5, 10, 15, 20 et 25 apportent chacun un 5 ;
- 25 = 5 × 5 en apporte un **second**.

On obtient 5 + 1 = **6**. En formule : ⌊25/5⌋ + ⌊25/25⌋ = 5 + 1.

---

## Étape 5 — Préfixe, rotations, occurrences

**Le code :** la méthode `prefixAndRotation()`.

**Le préfixe :** on part de `interstellar`.
- `internet` force à raccourcir jusqu'à `inter`.
- `interval` et `internal` commencent déjà par `inter`.

**Les rotations — l'astuce `a + a` :** toutes les rotations de `abcd` apparaissent dans `abcdabcd`. Par exemple, `cdab` y est en position **2**.

**Question — `{"aa", "a"}` sans le test de longueur :** `"aaaa".contains("a")` vaut **`true`** (vérifié, indice 0). On conclurait à tort que `a` est une rotation de `aa`. Une rotation garde forcément la même longueur.

**Les occurrences de `ana` dans `bananarama ananas` :** les positions trouvées avec chevauchement sont **1, 3, 11, 13** (vérifié). Sans chevauchement, après 1 on repart de 4 et on trouve 11, puis on repart de 14 : **2**.

---

## Étape 6 — Le plus long palindrome

**Le code :** la méthode `palindrome()`.

**Les 2n − 1 centres :** le centre pair `center` vise la lettre `center / 2`, et le centre impair vise l'espace entre `center / 2` et `center / 2 + 1`. Par exemple, `geeksskeeg` a un nombre **pair** de lettres : son centre est **entre** les deux `s`.

**Question — la complexité :**
- l'expansion autour des centres est en **O(n²)** : 2n − 1 centres, et chaque expansion fait au plus n/2 pas ;
- la méthode naïve est en **O(n³)** : O(n²) sous-chaînes, chacune vérifiée en O(n).

(Il existe un algorithme en O(n), celui de Manacher, hors programme.)

---

## Étape 7 — Justifier un paragraphe

**Le code :** la méthode `justify()`.

**La 1re ligne, à la main (largeur 24) :**
- `Java` (4) + `strings` (7) + `are` (3) = 14 lettres, plus 2 espaces minimum = 16 ≤ 24.
- Ajouter `immutable` (9) donnerait 14 + 9 + 3 = 26 > 24 : on s'arrête à 3 mots.
- Espaces à placer : 24 − 14 = 10, dans 2 trous. 10 / 2 = 5, et 10 % 2 = 0 : 5 et 5.

On obtient `Java     strings     are`.

**La 3e ligne :**
- `method returns a new` : 6 + 7 + 1 + 3 = 17 lettres, 3 trous, 24 − 17 = 7 espaces.
- 7 / 3 = 2, et 7 % 3 = 1 : le **premier** trou reçoit 3 espaces, les autres 2.

On obtient `method   returns  a  new`.

---

## Étape 8 — Trier sans tenir compte de la casse

**Le code :** la méthode `caseInsensitiveSort()`.

**Stable :** `Apple` est avant `apple` dans les données, et `banana` avant `Banana`. Avec `> 0` strict, des éléments « égaux sans casse » ne se doublent jamais : `Apple apple banana Banana`.

**L'ordre naturel :** `Arrays.sort` sur des `String` compare les codes Unicode. Les majuscules (65 à 90) passent avant les minuscules (97 à 122). Vérifié : `[Apple, Banana, apple, banana, cherry, date]`.

---

## Étape 9 — Les chiffres romains

**Le code :** les méthodes `romans()` et `value()`.

**Pourquoi mettre `CM`, `CD`, `XC`… dans les tableaux ?** Pour que le glouton les produise directement. 1994 donne M (reste 994), puis CM (94), puis XC (4), puis IV : **MCMXCIV**. Sans ces paires, on obtiendrait `MDCCCCLXXXXIIII`.

**La relecture de `MCMXC` :**
- M = +1000 ;
- C = −100 (suivi de M, plus grand) ;
- M = +1000 ;
- X = −10 (suivi de C) ;
- C = +100.

Total : **1990**.

---

## Étape 10 — Bases 2 et 16

**Le code :** la méthode `bases()`.

**`insert(0, …)` :** les divisions successives donnent les chiffres de **droite à gauche** (le premier reste est le chiffre des unités). Insérer en tête remet tout dans l'ordre, sans `reverse()`.

**2026 en hexadécimal :**
- 2026 = 126 × 16 + **10** (A) ;
- 126 = 7 × 16 + **14** (E) ;
- 7 = 0 × 16 + **7**.

Lu à l'envers : **7EA**.
