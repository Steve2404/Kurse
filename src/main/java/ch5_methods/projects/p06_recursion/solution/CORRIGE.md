# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`RecursionLab.java`](RecursionLab.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Les bases

**Le code :** les méthodes `factorial`, `digitSum`, `power`, `gcd`, `binary` et `palindrome`.

**`binary(37)` déroulé :**
- `binary(37)` = `binary(18) + 1`, et `binary(18)` = `binary(9) + 0` ;
- `binary(9)` = `binary(4) + 1`, `binary(4)` = `binary(2) + 0`, `binary(2)` = `binary(1) + 0` ;
- `binary(1)` = `"1"`.

En remontant : `"100101"`. Le reste est ajouté **après** l'appel, donc les chiffres sortent dans le bon ordre.

**Question — pourquoi 7 multiplications et pas 19 ?** `power(3, 20)` s'appelle sur 10, 5, 2, 1, puis 0 : n est divisé par 2 à chaque niveau. Chaque niveau fait 1 multiplication (`half * half`), plus 1 si n est impair (5 et 1) : 1 + 1 + 2 + 1 + 2 = **7** (vérifié). La boucle naïve en fait n − 1 = 19. La complexité est **O(log n)** contre O(n).

**Expériences :**
- **`factorial(21)`** déborde sans erreur : on obtient `-4249290049419214848` (vérifié). 21 ! ≈ 5,1 × 10¹⁹ dépasse `Long.MAX_VALUE` ≈ 9,2 × 10¹⁸.
- **Une récursion sans cas de base** ne s'arrête jamais. Chaque appel ajoute un cadre sur la **pile d'appels**, et quand la pile est pleine :

```
Exception in thread "main" java.lang.StackOverflowError
```

C'est une `Error`, pas une `Exception` : le programme n'est pas censé la rattraper.

---

## Étape 2 — Fibonacci : le coût des appels répétés

**Le code :** les méthodes `fibNaive` et `fibMemo`.

**Pourquoi 242785 appels ?** `fibNaive(25)` appelle `fibNaive(24)` **et** `fibNaive(23)`. Or `fibNaive(24)` rappelle aussi `fibNaive(23)`, et ainsi de suite : les mêmes valeurs sont recalculées un nombre exponentiel de fois.

**Question — pourquoi 49 appels avec la mémoïsation ?** Chaque `fibMemo(n)` pour n de 25 à 2 est **calculé une seule fois**, et fait exactement 2 appels : n − 1 (calculé) et n − 2 (déjà en mémoire, ou cas de base). Cela donne 1 appel initial + 2 × 24 = **49** (vérifié). La complexité passe de O(φⁿ) à **O(n)**.

**Le tableau partagé :** `memo` est un **paramètre**, mais tous les appels reçoivent **la même référence** (projet 4). Ce que l'un écrit dans `memo[n]`, les autres le lisent. Une valeur 0 signifie « pas encore calculé » : c'est possible ici, car `fib(n)` > 0 pour n ≥ 1.

**`fibonacci(90)`** tient encore dans un `long`. `fibNaive(90)` demanderait des milliards de milliards d'appels.

---

## Étape 3 — Diviser pour régner

**Le code :** les méthodes `mergeSort`, `quickSort` et `search`.

**Le tri fusion :** découper jusqu'à des morceaux d'un élément (déjà triés), puis **fusionner** deux à deux. Le `<=` de la fusion prend l'élément de **gauche** en cas d'égalité : les deux `3` gardent leur ordre, et le tri est **stable**. Complexité : O(n log n) dans **tous** les cas.

**Le tri rapide :** la partition place le pivot à sa place définitive, puis on trie récursivement les deux côtés. Complexité O(n log n) en moyenne, mais **O(n²)** si le pivot est toujours le plus petit ou le plus grand (tableau déjà trié, avec Lomuto).

**`clone()` :** les deux tris modifient le tableau reçu, d'où les copies. L'original affiché est intact.

**La recherche récursive :**
- 43 donne 6 (indice dans `[3, 3, 9, 10, 27, 38, 43, 82]`).
- 11 donne −5 : 11 s'insérerait en position 4, d'où −(4 + 1).

---

## Étape 4 — Hanoï, sous-ensembles, combinaisons

**Le code :** les méthodes `hanoi`, `subsets` et `combinations`.

**Hanoï :** pour déplacer n disques de A vers C, il faut d'abord libérer le grand disque. On déplace donc n − 1 disques sur B (en passant par C), puis le grand sur C, puis les n − 1 de B sur C (en passant par A). Le nombre de coups vérifie d(n) = 2 × d(n − 1) + 1, soit **2ⁿ − 1** = 15 pour 4 disques.

**Les sous-ensembles :** chaque élément a 2 choix, laissé ou pris, d'où 2³ = 8. L'ordre de la sortie vient de l'ordre des appels : « laissé » d'abord. Le premier sous-ensemble est donc `{}` (tout laissé), et le second `{3}` (seul le dernier pris).

**L'élagage des combinaisons :** pour choisir k éléments parmi 1..n en partant de i, il faut au moins k éléments restants, donc `i <= n - k + 1`. Sans cette borne, le résultat est le même, mais on explore des branches qui ne peuvent jamais aboutir. C(5, 3) = **10**.

---

## Étape 5 — Les N reines

**Le code :** les méthodes `queens` et `safe`.

**Le retour arrière :** on pose une reine par ligne. Si aucune colonne n'est sûre, la boucle se termine sans rien rendre, et on **revient** à la ligne précédente, qui essaie sa colonne suivante. `cols[row]` est simplement **écrasé** à l'essai suivant : pas besoin de « défaire ».

**Le test de diagonale :** deux reines (r1, c1) et (r2, c2) sont sur la même diagonale si `|c1 − c2| == |r1 − r2|`. Ici, `row - r` est toujours positif, puisque r < row.

**`Arrays.fill(solution, -1)` :** −1 signifie « pas encore de solution ». La 1re solution trouvée y est copiée (`System.arraycopy`) ; les suivantes ne la remplacent pas. Résultats : 4 solutions pour 6 × 6, **92** pour 8 × 8.

---

## Étape 6 — Les îles et la monnaie

**Le code :** les méthodes `fill`, `ways` et `fewest`.

**Question — marquer après la récursion ?** La case reste `#` pendant que ses voisines sont explorées. La voisine revient donc sur elle, qui repart vers la voisine, etc., à l'infini. Vérifié sur une île de 2 cases : **`StackOverflowError`**. Marquer **avant** garantit qu'une case n'est comptée qu'une fois.

**Le rendu de monnaie — `ways` :** à chaque étape, deux choix. Soit on **utilise** la pièce i (et on reste sur i, car elle peut resservir), soit on **passe** à la pièce suivante. Ne jamais revenir à une pièce précédente évite de compter `1+2` et `2+1` comme deux façons. `memo[i][montant]` stocke chaque sous-résultat : sans lui, le calcul serait exponentiel.

**`fewest(63)` = 4 :** 50 + 10 + 2 + 1. Pour 100, il suffit de **2** pièces (50 + 50). `memo[montant] == 0` signifie « pas encore calculé », ce qui est possible puisque `fewest` vaut au moins 1 pour un montant > 0.
