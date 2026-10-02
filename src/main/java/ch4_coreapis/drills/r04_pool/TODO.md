# Drill de rappel 4 — Le pool de chaînes et l'égalité

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`** dans le paquet `ch4_coreapis.drills.r04_pool`, avec une constante `static final String CONSTANT = "Hello"`.
- **Avant** de lancer, écris sur papier tes 8 lignes de prédiction. Compte ensuite tes erreurs : c'est le vrai score de ce drill.

## Défis

`x` vaut toujours le littéral `"Hello World"`.

- ☐ **D01.** `y` est un second littéral identique. Affiche `x == y` et `x.equals(y)`.
  → `D01 : true true`
- ☐ **D02.** `z = new String("Hello World")`. Affiche `x == z`, `x.equals(z)` et `x == z.intern()`.
  → `D02 : false true true`
- ☐ **D03.** `literal = "Hello" + " World"` (deux littéraux). Affiche `x == literal`.
  → `D03 : true`
- ☐ **D04.** `part = "Hello"` (une variable **non `final`**), puis `runtime = part + " World"`. Affiche `x == runtime` et `x == runtime.intern()`.
  → `D04 : false true`
- ☐ **D05.** `fromConstant = CONSTANT + " World"`. Affiche `x == fromConstant`.
  → `D05 : true`
- ☐ **D06.** Deux appels à `trim()` :
  - `trimmed` vient de `" Hello World".trim()` ;
  - `unchanged` vient de `"Hello World".trim()`, où il n'y a rien à retirer.
  
  Compare chacun à `x` avec `==`.
  → `D06 : false true`
- ☐ **D07.** Deux `new StringBuilder("ab")`. Affiche :
  - leur `equals` ;
  - `equals` sur leurs `toString()` ;
  - `==` sur leurs `toString()`.
  → `D07 : false true false`
- ☐ **D08.** `concatenated = "a".concat("b")`. Affiche :
  - `"ab" == concatenated` ;
  - `"ab".equals(concatenated)` ;
  - `"ab" == concatenated.intern()`.
  → `D08 : false true true`

## Expériences (hors sortie attendue)

1. Rends `part` **`final`** dans D04 : que devient `x == runtime` ? Pourquoi ?
2. Remplace `CONSTANT` par `static String` (sans `final`) : que devient D05 ?
3. `"Hello World".strip() == x`, et `x.toUpperCase().toLowerCase() == x` : prédis, puis vérifie.

## Sortie attendue complète

```
D01 : true true
D02 : false true true
D03 : true
D04 : false true
D05 : true
D06 : false true
D07 : false true false
D08 : false true true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Ce qui va dans le pool :**
- les littéraux ;
- les **constantes de compilation** : une concaténation de littéraux ou de variables `final` initialisées par un littéral ;
- elles sont calculées par `javac` et partagées.

**Ce qui est créé à l'exécution :**
- tout ce qui est calculé à l'exécution : une concaténation avec une variable non `final`, `concat`, `substring`, `new String(…)`…
- c'est donc un **nouvel** objet, et `==` donne `false`.

**`intern()` :**
- il rend l'instance du pool qui a le même contenu (et l'y ajoute au besoin).

**L'exception de `trim` et `strip` :**
- s'il n'y a rien à retirer, ils rendent le **même** objet.

**La règle d'or :**
- compare les contenus avec `equals`, jamais avec `==` ;
- `StringBuilder` n'a pas d'`equals` de contenu.

</details>
