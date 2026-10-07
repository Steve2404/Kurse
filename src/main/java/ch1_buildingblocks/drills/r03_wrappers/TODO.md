# Drill de rappel 3 — Les classes enveloppes

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall03`**.
- Chapitre 1 seulement.
- **Calcule chaque résultat à la main avant d'exécuter.**

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 3) et projet 2 (étape 5). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r03_wrappers` → **New** → **Java Class** → `Recall03`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher. Par exemple, pour un défi `D01` qui attend `D01 : 8 16`, écris un `System.out.println("D01 : " + … + " " + …);`, où les `…` sont les valeurs que **Java** calcule.
4. **Lance `Recall03`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `"123"` converti en `int` primitif, puis en objet `Integer`, puis cet objet ramené au primitif.
  → `D01 : 123 123 123`
- ☐ **D02.** `"2.5"` converti en `double` primitif. Puis `"2.5"` et `"-2.9"` convertis en objet `Double`, ramenés en `int` par une méthode de l'objet.
  → `D02 : 2.5 2 -2`
- ☐ **D03.** L'objet `Integer` de 257 ramené en `byte`, celui de 128 ramené en `byte`, l'objet `Long` de 70000 ramené en `short`.
  → `D03 : 1 -128 4464`
- ☐ **D04.** `"777"` lu en base 8, `"1F"` lu en base 16, `"+15"` lu en base 10.
  → `D04 : 511 31 15`
- ☐ **D05.** La méthode d'`Integer` qui comprend les préfixes, appliquée à `"0x1F"`, `"#1F"`, `"017"` et `"-17"`.
  → `D05 : 31 31 15 -17`
- ☐ **D06.** `"tRuE"` et `"yes"` convertis en `boolean`. Puis `"false"` converti en objet `Boolean`, ramené au primitif.
  → `D06 : true false false`
- ☐ **D07.** `"123456789012"` en `long`, `"0.5"` en `float`, `"-32768"` en `short`.
  → `D07 : 123456789012 0.5 -32768`
- ☐ **D08.** Le minimum d'un `int`, `"1e-2"` converti en objet `Double`, puis le maximum d'un `char` **en nombre** (affecte-le d'abord à un `int`).
  → `D08 : -2147483648 0.01 65535`

## Expériences (hors sortie attendue)

1. `Integer.parseInt("12.0")`, puis `Integer.parseInt("")`, puis `Integer.parseInt(" 12")` : quelle exception chaque fois ?
2. `Integer.parseInt("1F")` sans base : que se passe-t-il ?
3. `Boolean.parseBoolean(null)` : exception ou valeur ?

## Sortie attendue complète

```
D01 : 123 123 123
D02 : 2.5 2 -2
D03 : 1 -128 4464
D04 : 511 31 15
D05 : 31 31 15 -17
D06 : true false false
D07 : 123456789012 0.5 -32768
D08 : -2147483648 0.01 65535
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Primitif | Enveloppe | Texte → primitif | Texte → objet | Objet → primitif |
|---|---|---|---|---|
| `boolean` | `Boolean` | `parseBoolean` | `valueOf` | `booleanValue()` |
| `byte` | `Byte` | `parseByte` | `valueOf` | `byteValue()` |
| `short` | `Short` | `parseShort` | `valueOf` | `shortValue()` |
| `int` | **`Integer`** | `parseInt` | `valueOf` | `intValue()` |
| `long` | `Long` | `parseLong` | `valueOf` | `longValue()` |
| `float` | `Float` | `parseFloat` | `valueOf` | `floatValue()` |
| `double` | `Double` | `parseDouble` | `valueOf` | `doubleValue()` |
| `char` | **`Character`** | — | — | `charValue()` |

**Ce qu'il faut retenir :**
- les objets enveloppes numériques ont **tous** les `xxxValue()` (`Integer` a `byteValue()`, `doubleValue()`…). La conversion **tronque** (`2.9` → `2`, `-2.9` → `-2`) ou **déborde** (`257` → `1` en `byte`) ;
- `parseInt(texte, base)` lit dans une autre base. `Integer.decode` comprend `0x`, `#` et `0` (octal) ;
- `parseBoolean` rend `true` seulement pour `"true"`, sans tenir compte de la casse. Tout le reste, `null` compris, rend `false`, sans exception ;
- un texte non numérique pour `parseInt` lève une **`NumberFormatException`**.

</details>
