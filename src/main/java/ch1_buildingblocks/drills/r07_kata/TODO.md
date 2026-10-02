# Drill de rappel 7 — Kata mixte chronométré (tout le chapitre 1)

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min, **sans carte**. C'est le test final de chaque cycle de révision.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`** (avec `main` en **varargs**) et une classe **`Box`**.
- `Check` lance ton `main` avec `3 0x1F rouge 0.25 false`.

## Défis

- ☐ **D01.** Le 1er argument converti en `int`, multiplié par le 2e argument (un hexadécimal **avec son préfixe**).
  → `D01 : 93`
- ☐ **D02.** Le 1er argument converti en binaire, puis le 2e en hexadécimal **sans préfixe**.
  → `D02 : 11 1f`
- ☐ **D03.** `Box` construit avec le 3e argument. Son journal (un champ `String`) vaut `"1"` à la déclaration ; un bloc d'initialisation ajoute `"2"` ; le constructeur ajoute `"3"`, puis le libellé. Déclare la boîte avec `var`.
  → `D03 : 123rouge`
- ☐ **D04.** Un champ `static long total` de `Recall07` (jamais initialisé), puis le compteur `static` de boîtes créées.
  → `D04 : 0 1`
- ☐ **D05.** `total` reçoit 3 milliards (écrit avec `_`) plus le 1er argument.
  → `D05 : 3000000003`
- ☐ **D06.** Un text block d'une ligne `** ticket **`, terminé par un saut de ligne, affiché avec `print`.
  → `D06 : ** ticket **`
- ☐ **D07.** Le 4e argument en `double` (dans une variable `final`), fois 100. Puis le 4e argument converti en objet `Double`, ramené en `int`.
  → `D07 : 25.0 0`
- ☐ **D08.** Le 5e argument converti en `boolean`. Puis `'\u0041'` et `'B'` affichés collés.
  → `D08 : false AB`

## Sortie attendue complète

```
D01 : 93
D02 : 11 1f
D03 : 123rouge
D04 : 0 1
D05 : 3000000003
D06 : ** ticket **
D07 : 25.0 0
D08 : false AB
```

## Après le kata

- **Pour chaque défi raté ou trop lent :** refais le drill de son thème le lendemain.
  - r01 : `main` et arguments ;
  - r02 : littéraux ;
  - r03 : classes enveloppes ;
  - r04 : text blocks ;
  - r05 : variables ;
  - r06 : initialisation.
- **Note ton temps** dans `drills/README.md`.
