# Drill de rappel 4 — Enums avec champs, constructeurs et corps par constante

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall04.java`, paquet `ch7_beyondclasses.drills.r04_enums_plus`. Sous `Recall04` :

| Type | Contenu |
|---|---|
| `interface Timed` | `int seconds();` ; `default String describe()` rend `this + " " + seconds() + "s"` |
| `enum Coin` | `CENT(1), FIVE(5), TEN(10), QUARTER(25)` ; un champ `value` ; un constructeur `Coin(int value)` ; `value()` ; `static Coin largest()` (la dernière constante de `values()`) ; `static String change(int amount)` ; **`toString()` redéfini** pour rendre `name().toLowerCase()` |
| `enum Light implements Timed` | `RED(30)`, `GREEN(25)`, `YELLOW(5)`, chacune avec un **corps** qui redéfinit `abstract Light next()` (RED → GREEN → YELLOW → RED) ; un champ `seconds` |

- **`change(amount)`** est un rendu glouton : de la plus grosse pièce à la plus petite, chaque pièce utilisée donne `NxPIECE`, et les groupes sont séparés par un espace.

## Défis

- ☐ **D01.** `QUARTER.value()`, `CENT.ordinal()`, `Coin.valueOf("TEN").value()` et `Coin.largest()`.
  → `D01 : 25 0 10 quarter`
- ☐ **D02.** `Coin.change(68)`, ` | `, puis `Coin.change(30)`.
  → `D02 : 2xquarter 1xten 1xfive 3xcent | 1xquarter 1xfive`
- ☐ **D03.** Pars de `RED` et fais 4 pas : affiche chaque feu, puis le total des secondes des 4 feux.
  → `D03 : RED GREEN YELLOW RED 90`
- ☐ **D04.** Trois résultats :
  - `[` + `Light.RED.getClass().getSimpleName()` + `]` ;
  - `Light.RED.getDeclaringClass().getSimpleName()` ;
  - `Light.RED instanceof Timed`.
  → `D04 : [] Light true`
- ☐ **D05.** `Light.GREEN.describe()`, puis `Coin.FIVE`.
  → `D05 : GREEN 25s five`

## Expériences (hors sortie attendue)

1. Rends le constructeur de `Coin` `public` : quelle erreur ?
2. Mets un membre **avant** la liste des constantes : quelle erreur ? Et oublie le `;` après `QUARTER(25)` ?
3. Retire le corps de `YELLOW` : quelle erreur ?
4. Pourquoi D04 affiche-t-il `[]` ? (Une constante avec un corps est une sous-classe **anonyme** de l'enum.)
5. `Coin.valueOf("five")` fonctionne-t-il, alors que `toString()` affiche `five` ?

## Sortie attendue complète

```
D01 : 25 0 10 quarter
D02 : 2xquarter 1xten 1xfive 3xcent | 1xquarter 1xfive
D03 : RED GREEN YELLOW RED 90
D04 : [] Light true
D05 : GREEN 25s five
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**L'ordre dans un enum :**
- les constantes viennent **d'abord**, terminées par `;` s'il y a des membres ensuite ;
- puis les champs, le constructeur et les méthodes.

**Le constructeur :**
- toujours `private` (implicitement) ;
- appelé une fois par constante, au chargement de l'enum.

**Un corps par constante :** `RED(30) { … }` crée une sous-classe anonyme qui redéfinit une méthode, abstraite ou non. `getDeclaringClass()` rend l'enum lui-même.

**`toString` ou `name` :**
- `toString()` est redéfinissable ;
- `name()` est `final`, et c'est lui que `valueOf` utilise.

**Un enum peut :**
- implémenter des interfaces ;
- avoir des méthodes `static`, abstraites et des champs.

**Un enum ne peut pas :**
- étendre une classe (il étend déjà `Enum`) ;
- être étendu.

</details>
