# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Lire le legacy

<details><summary>Indice 1</summary>

Fais un tableau à trois lignes (POST, EXPRESS, PICKUP) et quatre colonnes : pays acceptés, poids maximal, prix en France, prix ailleurs. Les surcharges sont dans les deux `if` d'après le `switch`.

</details>

<details><summary>Indice 2</summary>

Cherche **tous** les endroits où apparaissent les noms des transporteurs : **Ctrl+F**, `"POST"`. Il y en a deux sortes : le `switch` et le tableau de `cheapest`.

</details>

---

## Étape 2 — Le filet de sécurité, et le piège des graines

<details><summary>Indice 1</summary>

`LongSupplier` est l'interface fonctionnelle d'une méthode sans paramètre qui rend un `long` : `outcome(() -> Data.LegacyShipping.price(carrier, grams, country, fragile))`. Les variables utilisées dans la lambda doivent être effectivement finales (chapitre 8) : ne les réaffecte pas.

</details>

<details><summary>Indice 2</summary>

Pour l'expérience : un tableau `int[] count = new int[4];`, une boucle de 1 à 400, et `count[new Random(n).nextInt(4)]++`. Affiche `Arrays.toString(count)`. Fais la même chose avec `new SplittableRandom(n)`.

</details>

---

## Étape 3 — Le colis se valide lui-même

<details><summary>Indice 1</summary>

Le constructeur compact d'un record : `public Parcel { if (grams < 1) throw … ; if (…) throw … ; }`. Le premier `if` qui échoue gagne : c'est ce qui fixe l'ordre des messages.

</details>

<details><summary>Indice 2</summary>

Les kilos commencés : `(grams + 999) / 1000`, la division entière arrondie vers le haut (chapitre 17, `minCapacity`).

</details>

---

## Étape 4 — Une stratégie par transporteur

<details><summary>Indice 1</summary>

Dans `PostRate`, une méthode privée `domesticPrice(int grams)` avec des `if` qui font chacun `return` : plus besoin de `else`. Au-delà de 5 kg : `1150 + (grams - 5000 + 999) / 1000 * 120`.

</details>

<details><summary>Indice 2</summary>

Pour une limite, deux cas de test : **sur** la limite et **juste après** (500 et 501, 2 000 et 2 001, 5 000 et 5 001, 30 000 et 30 001). 6 000 et 6 001 testent l'arrondi au kilo commencé.

</details>

---

## Étape 5 — Les surcharges

<details><summary>Indice 1</summary>

Une fabrique qui rend une lambda : `public static Surcharge customs() { return (parcel, base) -> EU.contains(parcel.country()) ? 0 : 800; }`.

</details>

<details><summary>Indice 2</summary>

Pour compter les classes avec l'héritage : chaque transporteur existe avec ou sans chacune des 3 surcharges, soit 2 × 2 × 2 variantes par transporteur.

</details>

---

## Étape 6 — Le calculateur

<details><summary>Indice 1</summary>

`putIfAbsent(code, rate)` rend `null` si le code était libre, et l'**ancienne** stratégie sinon. Pour la somme des surcharges : `surcharges.stream().mapToLong(s -> s.amount(parcel, base)).sum()`.

</details>

<details><summary>Indice 2</summary>

`quotes` : `rates.values().stream().filter(rate -> rate.accepts(parcel)).map(rate -> new Quote(rate.code(), finalPrice(rate, parcel))).sorted(…).toList()`. `cheapest` : `quotes(parcel).stream().findFirst()`.

</details>

---

## Étape 7 — Deux transporteurs de plus

<details><summary>Indice 1</summary>

Le fret à 40 000 g : 10 000 g au-delà de 30 kg, donc 10 kilos commencés : 49,00 + 10 × 0,90 = 58,00. À 30 001 g : 1 kilo commencé, 49,90.

</details>

<details><summary>Indice 2</summary>

Le vélo, colis fragile de 400 g : vélo 3,00 + 0,45 = 3,45 ; poste 4,95 + 0,74 = 5,69. Colis de 4 000 g : le vélo refuse, il reste la poste à 11,50.

</details>

---

## Étape 8 — Les mutants

<details><summary>Indice 1</summary>

Le hasard ne tombe presque jamais **pile** sur 500 g, 20 000 g ou 1 000 g, et le legacy ne connaît ni le fret ni le refus d'un code en double.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. 500 g pile passe au palier suivant de la poste.
2. La poste ne compte plus le kilo **commencé** au-delà de 5 kg.
3. Hors de France, la poste ajoute 7,50 au lieu de doubler.
4. L'express hors de France coûte 10,00 de plus au lieu de 15,00.
5. L'express accepte jusqu'à 31 kg.
6. Le point relais refuse la Belgique.
7. Le point relais refuse 20 000 g pile.
8. `startedKilos` compte un kilo de trop sur un poids rond (1 000 g → 2).
9. La surcharge fragile tronque au lieu d'arrondir.
10. Le Luxembourg n'est plus dans l'Union européenne (douane).
11. Les devis sont triés par code, plus par prix.
12. Un code en double n'est plus refusé.
13. Un colis refusé reçoit quand même un prix.
14. Chaque surcharge se calcule sur le prix **déjà surchargé** (la fragilité paie aussi sur la douane).
15. Le fret accepte 30 000 g pile.
16. Le fret ne compte plus le kilo commencé.

</details>
