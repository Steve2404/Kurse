# Drill de rappel 8 — La récursivité

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire. **Aucune boucle** dans les méthodes de ce drill : tout est récursif.
- Crée la classe **`Recall08`** dans le paquet `ch5_methods.drills.r08_recursion`.

## Défis

- ☐ **D01.** Trois méthodes :
  - `int sumTo(int n)` ;
  - `String reverse(String s)` ;
  - `int countChar(String s, char c)`.
  
  Appelle `sumTo(100)`, `reverse("recursion")` et `countChar("banana", 'a')`.
  → `D01 : 5050 noisrucer 3`
- ☐ **D02.** Deux méthodes :
  - `int max(int[] a, int i)` : le maximum à partir de l'indice i ;
  - `long pow(long x, int n)`, en exponentiation rapide.
  
  Appelle `max({3, 9, 2, 7}, 0)`, `pow(2, 30)` et `pow(7, 0)`.
  → `D02 : 9 1073741824 1`
- ☐ **D03.** `int ackermannLike(int n)` : 1 si n ≤ 1, sinon `f(n / 2) + f(n - 1)`. Un compteur `static int depth` compte **tous** les appels. Affiche `f(6)`, puis `depth`.
  → `D03 : 10 19`
- ☐ **D04.** `String toBase(int n, int base)`, avec les chiffres `"0123456789ABCDEF"`. Appelle-la avec 255 en base 16, 10 en base 2 et 0 en base 8.
  → `D04 : FF 1010 0`
- ☐ **D05.** `int paths(int r, int c, int[][] memo)` : le nombre de chemins sur une grille (droite ou bas). Elle vaut 1 si `r == 0` ou `c == 0`. Appelle `paths(2, 2, …)` et `paths(10, 10, …)`.
  → `D05 : 6 184756`
- ☐ **D06.** `void countdown(int n, StringBuilder out)` : ajoute n **avant** l'appel récursif et **après** lui. Le cas de base est `n < 0`. Appelle-la avec 3.
  → `D06 : 3 2 1 0 0 1 2 3`

## Expériences (hors sortie attendue)

1. Supprime le cas de base de `sumTo` : quelle erreur, et à quel moment ?
2. `sumTo(100_000)` : que se passe-t-il ? Pourquoi une boucle n'a-t-elle pas ce problème ?
3. Dans D05, retire la mémoïsation pour `paths(16, 16)` : mesure la différence.

## Sortie attendue complète

```
D01 : 5050 noisrucer 3
D02 : 9 1073741824 1
D03 : 10 19
D04 : FF 1010 0
D05 : 6 184756
D06 : 3 2 1 0 0 1 2 3
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Écrire une méthode récursive :**
1. le **cas de base** : une réponse directe, sans appel ;
2. le **cas récursif** : un appel sur un problème **plus petit**, qui finit par atteindre le cas de base ;
3. ce qui est **partagé** passe en paramètre (un tableau, un `StringBuilder`) ou dans un champ `static`.

**La pile d'appels :**
- chaque appel a ses propres variables locales ;
- le code placé **après** l'appel récursif s'exécute en remontant (D06) ;
- trop de niveaux provoquent une `StackOverflowError`.

**La mémoïsation :** si les mêmes sous-problèmes reviennent (Fibonacci, chemins), on mémorise leur résultat dans un tableau. On passe d'un temps exponentiel à un temps polynomial.

**Les schémas classiques :**
- réduire de 1 (`n - 1`) ;
- couper en deux (`n / 2` : puissance, dichotomie, tri fusion) ;
- choisir ou ne pas choisir (sous-ensembles, retour arrière).

</details>
