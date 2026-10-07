# Drill de rappel 2 — Types primitifs et littéraux

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall02`**.
- Chapitre 1 seulement : pas de `if`, pas de boucle, pas de cast.

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 1 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r02_literals` → **New** → **Java Class** → `Recall02`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher. Par exemple, pour un défi `D01` qui attend `D01 : 8 16`, écris un `System.out.println("D01 : " + … + " " + …);`, où les `…` sont les valeurs que **Java** calcule.
4. **Lance `Recall02`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Le littéral `10` écrit en octal, en hexadécimal, en binaire, puis en décimal, tels quels dans le `println`.
  → `D01 : 8 16 2 10`
- ☐ **D02.** Un million avec des `_`, `0xFFFF` avec un `_`, puis `0b10101010` avec un `_`.
  → `D02 : 1000000 65535 170`
- ☐ **D03.** Un `long` de 9 milliards (avec `_`), puis la plus grande valeur d'un `long` (constante).
  → `D03 : 9000000000 9223372036854775807`
- ☐ **D04.** Un `float` qui vaut 1,25, un `double` qui vaut 125 écrit en notation scientifique, puis `3.0e-1` tel quel.
  → `D04 : 1.25 125.0 0.3`
- ☐ **D05.** Trois `char` qui valent `J`, écrits de trois façons (littéral, `'\u…'`, entier), affichés **collés**. Puis le code de `'K'`.
  → `D05 : JJJ 75`
- ☐ **D06.** Avec les constantes des classes enveloppes : le minimum d'un `byte`, le maximum d'un `short`, puis les tailles en bits d'un `char` et d'un `int`.
  → `D06 : -128 32767 16 32`
- ☐ **D07.** Les valeurs par défaut d'un `double`, d'un `boolean` et d'un `String`, lues dans **trois champs non initialisés**.
  → `D07 : 0.0 false null`
- ☐ **D08.** 4095 en hexadécimal, 64 en octal, 10 en binaire.
  → `D08 : fff 100 1010`

## Expériences (hors sortie attendue)

Pour chacune, écris la ligne, lis l'erreur de `javac`, puis retire-la :
1. `int a = 0b2;`
2. `int b = 09;`
3. `long c = 9000000000;`
4. `float d = 1.25;`
5. `int e = 1_000_;`
6. `double f = 1._5;`
7. `int g = 0x_FF;`

**Question :** pourquoi `char c = 74;` compile-t-il, mais pas `int i = 74; char c = i;` ?

## Sortie attendue complète

```
D01 : 8 16 2 10
D02 : 1000000 65535 170
D03 : 9000000000 9223372036854775807
D04 : 1.25 125.0 0.3
D05 : JJJ 75
D06 : -128 32767 16 32
D07 : 0.0 false null
D08 : fff 100 1010
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Type | Bits | Bornes | Défaut (champ) |
|---|---|---|---|
| `byte` | 8 | −128 … 127 | 0 |
| `short` | 16 | −32 768 … 32 767 | 0 |
| `int` | 32 | −2³¹ … 2³¹−1 | 0 |
| `long` | 64 | −2⁶³ … 2⁶³−1 | 0L |
| `float` | 32 | — | 0.0f |
| `double` | 64 | — | 0.0 |
| `char` | 16 | 0 … 65 535 (non signé) | `'\u0000'` |
| `boolean` | — | `true` / `false` | `false` |
| référence | — | — | `null` |

**Les littéraux :**
- les préfixes :
  - `0` → **octal** (piège : `010` = 8) ;
  - `0x` / `0X` → hexadécimal ;
  - `0b` / `0B` → binaire ;
- un nombre entier seul est un `int` : il faut `L` pour un `long` trop grand ;
- un nombre décimal seul est un `double` : il faut `f` pour un `float` ;
- **`_` :** seulement **entre deux chiffres**. Jamais au début, à la fin, à côté du `.`, après `0x`, ou avant `L` / `f`.

**Les variables :**
- une **variable locale** n'a **pas** de valeur par défaut : la lire sans l'initialiser ne compile pas ;
- **les enveloppes :** `Integer.toHexString`, `toOctalString`, `toBinaryString`, et les constantes `SIZE`, `MIN_VALUE`, `MAX_VALUE`.

</details>
