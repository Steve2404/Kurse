# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Lire le legacy et nommer ses odeurs

<details><summary>Indice 1</summary>

Lis le code avec un crayon : à côté de chaque variable d'une lettre, écris ce qu'elle contient vraiment (`t` : le total des pièces en centimes…). Les règles de prix sont dans les `if` de la fin et dans le calcul de `qt`.

</details>

<details><summary>Indice 2</summary>

Pour compter les copies : **Ctrl+F** dans `Data.java`, tape `< 10 ? "0"`, et lis le compteur en haut de l'éditeur. Pour `"PART;X;deux;100"`, cherche ce que fait `Integer.parseInt` avec un texte qui n'est pas un nombre (chapitre 11).

</details>

---

## Étape 2 — Le filet de sécurité : la caractérisation

<details><summary>Indice 1</summary>

Un test répété reçoit son numéro par un paramètre : `void goldenMaster(RepetitionInfo info)`, puis `info.getCurrentRepetition()`. Construis la liste des lignes dans une `ArrayList`, avec une boucle `for` de 0 à `count`.

</details>

<details><summary>Indice 2</summary>

Pour les refus, deux `assertThrows` : l'un sur le legacy, l'autre sur ta copie. Chacun **rend** l'exception attrapée : garde-les dans deux variables et compare leurs `getMessage()`. Pour l'expérience de comptage, recopie le même tirage dans un `main` et ajoute des compteurs, sans construire de texte.

</details>

---

## Étape 3 — Un objet valeur : `Money`

<details><summary>Indice 1</summary>

Le constructeur compact d'un record s'écrit sans parenthèses : `public Money { if (cents < 0) throw … }`. Chaque opération fait `return new Money(…)` : on ne modifie jamais `this`.

</details>

<details><summary>Indice 2</summary>

`String.format("%d,%02d", cents / 100, cents % 100)` : `%02d` écrit un nombre sur **deux** chiffres, complété par un zéro à gauche. Dans ta copie du legacy, remplace d'abord **un seul** format, relance, puis les autres.

</details>

---

## Étape 4 — Remplacer le code de type par du polymorphisme

<details><summary>Indice 1</summary>

Le quart d'heure commencé, c'est une division arrondie **vers le haut** : `(minutes + 15 - 1) / 15` (chapitre 17, `minCapacity`). Les minutes facturées sont `quarters() * 15`, et le libellé coupe ces minutes en heures (`/ 60`) et minutes (`% 60`).

</details>

<details><summary>Indice 2</summary>

Dans `LineParser`, le `switch` en flèches rend directement la ligne : `return switch (fields[0]) { case "PART" -> part(line, fields); … default -> throw invalid(line); };`. `invalid` **rend** l'exception (`return new IllegalArgumentException(…)`) : c'est l'appelant qui la lance avec `throw`. Garde l'ordre du legacy dans `part` : `requireLength`, puis `parseInt`, puis `parseLong`, puis le test des valeurs.

</details>

---

## Étape 5 — Une responsabilité par classe

<details><summary>Indice 1</summary>

Une méthode privée `sum(lines, category)` suffit pour les trois sous-totaux : `lines.stream().filter(l -> l.category() == category).map(InvoiceLine::price).reduce(Money.ZERO, Money::plus)`. Les minutes de main-d'œuvre : `mapToInt(InvoiceLine::billedMinutes).sum()`.

</details>

<details><summary>Indice 2</summary>

Le cas papier : 300 minutes, c'est 20 quarts d'heure, donc 360,00 ; plus de 240 minutes, donc 5 % de remise sur la main-d'œuvre. La remise fidélité porte sur les **pièces seules**. Le net retire les deux remises ; la TVA porte sur le net.

</details>

---

## Étape 6 — La façade, et le legacy disparaît

<details><summary>Indice 1</summary>

Si une méthode dépasse 12 lignes, sélectionne un groupe de lignes qui fait **une** chose (les lignes optionnelles de l'impression, une remise) et **Ctrl+Alt+M**.

</details>

<details><summary>Indice 2</summary>

Les quatre lignes de la façade : `List<InvoiceLine> lines = textLines.stream().map(LineParser::parse).toList();`, puis le `Customer`, puis `new InvoiceCalculator().compute(…)`, puis `return new InvoicePrinter().print(…)`.

</details>

---

## Étape 7 — Les forfaits

<details><summary>Indice 1</summary>

Fais la liste avant de coder : où les autres sortes de lignes sont-elles lues ? additionnées ? affichées ? Ce sont les seuls endroits à toucher.

</details>

<details><summary>Indice 2</summary>

Le calcul papier de `"Ba"` : pièces 200,00 (le seuil est inclus : remise de 20,00), forfaits 3,50, net 183,50, TVA 36,70, total 220,20. La ligne `Forfaits : 3,50` vient juste après `Main-d'oeuvre : 0,00`.

</details>

---

## Étape 8 — Les mutants

<details><summary>Indice 1</summary>

Le maître étalon tue presque tout, sauf ce que le hasard ne tire jamais : un seuil **pile**, un refus, et ce que le legacy ne connaît pas (les forfaits).

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. `percent` tronque au lieu d'arrondir au plus proche.
2. `format` écrit `0,5` au lieu de `0,05`.
3. Un nombre exact de quarts d'heure (15, 30… minutes) est facturé un quart d'heure de trop.
4. Le libellé de la main-d'œuvre écrit `(1 h 0)` au lieu de `(1 h 00)`.
5. Le seuil de la remise fidélité devient exclu : 200,00 pile n'y a plus droit.
6. La remise fidélité est donnée à tout le monde, fidèle ou non.
7. Le seuil de la remise main-d'œuvre devient inclus : 240 minutes pile y ont droit.
8. La TVA est calculée **avant** les remises.
9. Les forfaits comptent dans la base de la remise fidélité.
10. La ligne `Forfaits` s'affiche toujours, même à zéro.
11. Une quantité de 0 n'est plus refusée par le lecteur (c'est `Part` qui la refuse, avec un **autre** message).
12. Le nombre de champs d'un forfait n'est plus vérifié.

</details>
