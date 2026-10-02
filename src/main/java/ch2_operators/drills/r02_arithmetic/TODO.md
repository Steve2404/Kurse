# Drill de rappel 2 — Arithmétique et promotion numérique

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall02`**.
- Calcule à la main d'abord.

## Défis

- ☐ **D01.** `17 / 5`, `17 % 5`, `17.0 / 5`, `17 / 5.0f`.
  → `D01 : 3 2 3.4 3.4`
- ☐ **D02.** `-17 / 5`, `-17 % 5`, `17 % -5`, `5.5 % 2`.
  → `D02 : -3 -2 2 1.5`
- ☐ **D03.** Deux `byte` 10 et 20 : leur somme rangée dans un `byte` (que faut-il ?), puis rangée dans un `int`.
  → `D03 : 30 30`
- ☐ **D04.** Un `short` 30 000 multiplié par lui-même, rangé dans un `int`.
  → `D04 : 900000000`
- ☐ **D05.** Un `int` 1 000 000 multiplié par 3000 : une fois rangé dans un `long` **sans débordement** (grâce à un littéral), une fois **avec** débordement.
  → `D05 : 3000000000 -1294967296`
- ☐ **D06.** `1.0f / 3` dans un `float`, `1.0 / 3` dans un `double`.
  → `D06 : 0.33333334 0.3333333333333333`
- ☐ **D07.** Le code de `'A' + 1` (dans un `int`), ce code redevenu `char`, puis `'a' - 'A'`.
  → `D07 : 66 B 32`
- ☐ **D08.** `1 / 2 + 1.0 / 2`, `2 + 3 * 4 % 5`, `10 - 2 - 3`.
  → `D08 : 0.5 4 5`

## Expériences (hors sortie attendue)

1. `byte b1 = 1, b2 = 2; byte b3 = b1 + b2;` : l'erreur de `javac`, et pourquoi.
2. `int i = 5L;`, puis `long l = 5.0;`, puis `float f = 1.5;` : les trois erreurs.
3. `int z = 1 / 0;` compile. Que se passe-t-il à l'exécution ? Et avec `1.0 / 0` ?

## Sortie attendue complète

```
D01 : 3 2 3.4 3.4
D02 : -3 -2 2 1.5
D03 : 30 30
D04 : 900000000
D05 : 3000000000 -1294967296
D06 : 0.33333334 0.3333333333333333
D07 : 66 B 32
D08 : 0.5 4 5
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les 4 règles de promotion numérique**, pour un opérateur binaire :
1. Si un opérande est `double`, le calcul se fait en `double` ; sinon, s'il y a un `float`, en `float` ; sinon, s'il y a un `long`, en `long`.
2. `byte`, `short` et `char` sont **toujours** promus en `int`, même `byte + byte`.
3. Le résultat a le type promu.
4. Un entier **combiné** à un flottant devient flottant.

**La division et le modulo :**
- la division entière **tronque vers zéro** : `-17 / 5 = -3` ;
- le **signe de `%`** suit le dividende (l'opérande de gauche) : `-17 % 5 = -2`, `17 % -5 = 2` ;
- `%` marche aussi sur les flottants.

**Les cas limites :**
- une division entière par 0 lève une `ArithmeticException` ;
- une division flottante par 0 donne `Infinity` ou `NaN`.

**Les débordements :** silencieux, l'entier « fait le tour ». Pour l'éviter, un littéral `L` dans le calcul.

**La priorité :** `* / %` passent avant `+ -`. À priorité égale, l'évaluation va de gauche à droite.

</details>
