# Projet 8 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Anagrammes

<details><summary>Indice 1</summary>

`counts[a.charAt(i) - 'a']++` : `'a' - 'a'` vaut 0, `'z' - 'a'` vaut 25. Les deux chaînes doivent aussi avoir la même longueur.

</details>

<details><summary>Indice 2</summary>

Parcours `counts` avec un for-each : dès qu'une case n'est pas 0, ce n'est pas un anagramme (`break`). Le `!` s'ajoute avec `same == Arrays.equals(sa, sb) ? "" : "!"`.

</details>

---

## Étape 2 — Compression RLE

<details><summary>Indice 1</summary>

Compresser : un `while (i < length)` extérieur. Mémorise `c = text.charAt(i)`, puis une boucle intérieure avance `i` tant que le caractère vaut `c` et compte la série.

</details>

<details><summary>Indice 2</summary>

Décompresser : `number` part de 0. Si le caractère est un chiffre (`c >= '0' && c <= '9'`), alors `number = number * 10 + (c - '0')`. Sinon, ajoute la lettre répétée et remets `number` à 0.

</details>

---

## Étape 3 — César et Vigenère

<details><summary>Indice 1</summary>

`new StringBuilder(s)`, puis pour chaque `i` : si `c` est entre `'a'` et `'z'`, alors `sb.setCharAt(i, (char) ('a' + (c - 'a' + shift) % 26))`. Même chose avec `'A'` pour les majuscules.

</details>

<details><summary>Indice 2</summary>

Vigenère : le décalage de la position i est `key.charAt(i % key.length()) - 'A'`. Déchiffrer : `(enc[i] - 'A' - k + 26) % 26`.

</details>

---

## Étape 4 — Grands nombres

<details><summary>Indice 1</summary>

Addition : `i` et `j` partent de la fin des deux chaînes. `while (i >= 0 || j >= 0 || carry > 0)` : `d = carry`, plus chaque chiffre disponible (`charAt(i--) - '0'`). Puis `append(d % 10)` et `carry = d / 10`.

</details>

<details><summary>Indice 2</summary>

Factorielle : `digits[0] = 1`, `size = 1`. Pour chaque f, multiplie chaque case avec retenue. Puis, tant qu'il reste une retenue, ajoute des cases (`digits[size++] = c % 10`). Un tableau de 40 cases suffit pour 25 !.

</details>

---

## Étape 5 — Préfixe, rotations, occurrences

<details><summary>Indice 1</summary>

Préfixe : pour chaque mot, `while (!w.startsWith(prefix)) prefix = prefix.substring(0, prefix.length() - 1);`.

</details>

<details><summary>Indice 2</summary>

Occurrences : `for (int from = hay.indexOf(n); from >= 0; from = hay.indexOf(n, from + 1))`. Pour « sans chevauchement », remplace `+ 1` par `+ n.length()`.

</details>

---

## Étape 6 — Le plus long palindrome

<details><summary>Indice 1</summary>

`for (int center = 0; center < 2 * n - 1; center++)`. Après le `while` d'expansion, les bornes ont dépassé d'une case de chaque côté : la longueur vaut `right - left - 1` et le début `left + 1`.

</details>

<details><summary>Indice 2</summary>

Condition d'expansion : `left >= 0 && right < n && s.charAt(left) == s.charAt(right)`. Garde la meilleure longueur et son début.

</details>

---

## Étape 7 — Justifier un paragraphe

<details><summary>Indice 1</summary>

`j` part de `i`. Ajoute le mot `j` tant que `lettres + words[j].length() + (j - i) <= WIDTH`, où `(j - i)` est le nombre minimum d'espaces déjà nécessaires.

</details>

<details><summary>Indice 2</summary>

`gaps = j - i - 1` et `spaces = WIDTH - lettres`. Le trou n° g reçoit `spaces / gaps + (g < spaces % gaps ? 1 : 0)` espaces. Dernière ligne, ou `gaps == 0` : `String.join` puis `repeat` à droite.

</details>

---

## Étape 8 — Trier sans tenir compte de la casse

<details><summary>Indice 1</summary>

Le même tri par insertion qu'au projet 7, avec `w[j].compareToIgnoreCase(key) > 0` à la place de `a[j] > key`.

</details>

<details><summary>Indice 2</summary>

Le `> 0` strict garde le tri stable : `Apple` (avant `apple` dans les données) reste devant.

</details>

---

## Étape 9 — Les chiffres romains

<details><summary>Indice 1</summary>

Vers les romains : pour chaque indice k des tableaux, `while (rest >= VALUES[k])`, ajoute `SYMBOLS[k]` et retire la valeur.

</details>

<details><summary>Indice 2</summary>

Depuis les romains : pour chaque lettre, si elle a une suivante **plus grande**, soustrais sa valeur ; sinon, ajoute-la.

</details>

---

## Étape 10 — Bases 2 et 16

<details><summary>Indice 1</summary>

`for (int x = n; x > 0; x /= 2) bin.insert(0, x % 2);`, et la même chose avec 16 et `digits.charAt(x % 16)`.

</details>

<details><summary>Indice 2</summary>

`Integer.toHexString(255)` rend `ff` en minuscules : compare avec `equalsIgnoreCase` pour accepter ton `FF`.

</details>
