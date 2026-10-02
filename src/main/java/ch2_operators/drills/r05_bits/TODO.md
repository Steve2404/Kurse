# Drill de rappel 5 — Opérateurs bit à bit et décalages

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall05`**.
- Pose les calculs **en binaire** sur papier.

## Défis

- ☐ **D01.** Avec `a = 0b1100` et `b = 0b1010` : `a & b`, `a | b`, `a ^ b`, puis `a ^ b` en binaire.
  → `D01 : 8 14 6 110`
- ☐ **D02.** `~0`, `~5`, `~5 & 0xF`, `~~5`.
  → `D02 : -1 -6 10 5`
- ☐ **D03.** `1 << 4`, `3 << 2`, `256 >> 3`, `1 << 31`.
  → `D03 : 16 12 32 -2147483648`
- ☐ **D04.** `-32 >> 3`, `-1 >>> 28`, `-1 >> 28`, `8 >>> 1`.
  → `D04 : -4 15 -1 4`
- ☐ **D05.** Un ensemble de drapeaux à 0, puis quatre opérations :
  - allume le bit 2 avec `|=` ;
  - allume le bit 0 ;
  - bascule le bit 0 avec `^=` ;
  - éteins le bit 3 avec `&= ~`.
  
  Affiche la valeur, puis le binaire, puis le test « le bit 2 est-il allumé ? ».
  → `D05 : 4 100 true`
- ☐ **D06.** La couleur `0xFF8800` : extrais le rouge, le vert et le bleu (8 bits chacun) avec des décalages et `& 0xFF`.
  → `D06 : 255 136 0`
- ☐ **D07.** Recompose la couleur à partir des trois composantes avec `<<` et `|`. Compare-la à l'originale, puis affiche-la en hexadécimal.
  → `D07 : true ff8800`
- ☐ **D08.** `7 & 1`, `10 & 1` (pair ou impair sans `%`), puis `1 << 1 + 1` et `(1 << 1) + 1`.
  → `D08 : 1 0 4 3`
- ☐ **D09.** Les affectations composées de décalage :
  - `v = 5`, puis `v <<= 3` ;
  - `w = -64`, puis `w >>= 2` ;
  - `u = -64`, puis `u >>>= 28` ;
  - un `byte` 64, puis `<<= 1`.
  → `D09 : 40 -16 15 -128`

## Expériences (hors sortie attendue)

1. `boolean b = 5 & 3;` : quelle erreur ? (`&` sur deux `int` rend un `int`.)
2. `int s = 1 << 33;` : prédis, puis vérifie. Seuls les 5 bits de poids faible de la distance comptent.

## Sortie attendue complète

```
D01 : 8 14 6 110
D02 : -1 -6 10 5
D03 : 16 12 32 -2147483648
D04 : -4 15 -1 4
D05 : 4 100 true
D06 : 255 136 0
D07 : true ff8800
D08 : 1 0 4 3
D09 : 40 -16 15 -128
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Opérateur | Sur des entiers |
|---|---|
| `&` | 1 si les deux bits valent 1 (pour **isoler** ou **éteindre**) |
| `\|` | 1 si au moins un bit vaut 1 (pour **allumer**) |
| `^` | 1 si les bits diffèrent (pour **basculer**) |
| `~` | inverse les 32 (ou 64) bits ; `~x == -x - 1` |
| `<< n` | décale à gauche, multiplie par 2ⁿ (peut changer le signe) |
| `>> n` | décale à droite **en gardant le signe** |
| `>>> n` | décale à droite **en insérant des 0**, donc un résultat ≥ 0 sur un négatif (pour n > 0) |

**Les idiomes :**
- tester un bit : `(x & 1 << n) != 0` ;
- allumer : `x |= 1 << n` ;
- éteindre : `x &= ~(1 << n)` ;
- basculer : `x ^= 1 << n`.

**La priorité :** `+ -` passent **avant** les décalages, qui passent avant `< >`, avant `== !=`, avant `&`, avant `^`, avant `|`. Donc `1 << 1 + 1` vaut `1 << 2`.

</details>
