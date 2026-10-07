# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `InvoiceLine`, `Invoice`, `Money` et `Billing`.
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — La facture

**Le code :** [`InvoiceLine.java`](InvoiceLine.java), [`Invoice.java`](Invoice.java) et le début du `main` de [`Billing.java`](Billing.java).

**`Locale.setDefault(Locale.US)` en premier :** sans lui, tout format **sans** locale explicite dépendrait de la machine (allemande chez toi). La sortie changerait d'un ordinateur à l'autre.

**Question — y a-t-il une conversion ?** **Non.** `getCurrencyInstance(locale)` prend la **monnaie** de la locale et ses **conventions d'affichage** (symbole, séparateurs, nombre de décimales). Le même nombre 835,80 devient « $835.80 » ou « ￥836 » : c'est un **format**, pas un taux de change. Le yen n'a pas de décimales, d'où l'arrondi à 836.

**Question — pourquoi 5,5 % s'affiche `6 %` ?** `getPercentInstance` affiche **0 décimale** par défaut. 5,5 est arrondi, et la règle par défaut est **HALF_EVEN** : une moitié s'arrondit vers le chiffre **pair**. Vérifié : 5,5 % **et** 6,5 % donnent tous deux `6 %`. Avec `setMinimumFractionDigits(1)`, on obtient `5,5 %`.

**La TVA par taux :** on additionne d'abord les bases de chaque taux, puis on calcule et on arrondit **une fois** par taux. Arrondir ligne par ligne pourrait donner quelques centimes d'écart.

---

## Étape 2 — Arrondis et motifs

**Le code :** les lignes `arrondis` et `motif`.

**HALF_EVEN par défaut :** 2.5 donne **2** (pair), 3.5 donne **4** (pair). Avec `HALF_UP`, 2.5 donne **3**. HALF_EVEN, l'« arrondi du banquier », évite de biaiser les sommes vers le haut.

**Question — pourquoi 1.005 donne `1` et 2.675 donne `2.67` ?** Ces décimaux n'ont **pas** de valeur exacte en binaire. Vérifié avec `new BigDecimal(…)`, qui montre la valeur réellement stockée :
- 1.005 vaut en réalité `1.00499999999999989…`, juste **sous** la moitié, donc arrondi à 1.00, affiché `1` ;
- 2.675 vaut en réalité `2.67499999999999982…`, donc `2.67`.

Ce n'est pas un bug d'arrondi : le nombre **n'est pas** 2.675.

**Les motifs `DecimalFormat` :**
- `#,##0.00` : groupement par milliers, au moins un chiffre avant la virgule, exactement 2 après ;
- `'#'000` : `'#'` est un dièse **littéral**, et `000` exige 3 chiffres. 7.25 devient `-#007`.

---

## Étape 3 — Compact et lecture

**Le code :** les lignes `compact` et `lu`.

**Le format compact** (Java 12) : `1K`, `1.2K` (avec une décimale autorisée), `1 thousand` (LONG en anglais) et `1 Tausend` (LONG en allemand).

**`parse` lit le plus long début valide**, et ignore la suite :
- **`"12abc"`** donne **12** : la lecture s'arrête à `a`, sans exception, puisque **quelque chose** a été lu ;
- **`"abc"`** : rien n'est lisible, donc `ParseException`, position 0 ;
- **`"12.50"`** en monnaie US : le format monétaire **exige** le symbole `$`, donc `ParseException`.

**Question — pourquoi `"1 234,56"` donne `1` en français ?** Le séparateur de milliers français, en Java 17, est l'**espace fine insécable** (U+202F), et non l'espace ordinaire. Vérifié : le code du caractère dans `format(1234.56)` vaut **8239** (U+202F). Le texte tapé avec une espace **ordinaire** s'arrête donc après le `1`. Relu à partir de son propre `format`, le nombre redonne bien 1234.56.

---

## Étape 4 — Partage et prêt

**Le code :** [`Money.java`](Money.java) et la fin du `main`.

**Le plus fort reste :** 835,80 € en 2/3/4. Les parts entières perdent quelques centimes, distribués aux parts dont le **reste** de division est le plus grand. La somme tombe **exactement** sur le total : c'est la même idée qu'`allocate` au chapitre 6 (projet 4).

**Le prêt :** la mensualité constante vient de la formule d'annuité. Les arrondis au centime font qu'il reste quelques centimes à la fin : la **dernière** échéance solde exactement le reste. Le coût du crédit est la somme des intérêts.

**`String.format(Locale.FRANCE, "%,10.2f", …)`** : `,` active le groupement, `10` est la largeur et `.2` le nombre de décimales. Avec la locale française, on obtient la virgule décimale et l'espace insécable pour les milliers.
