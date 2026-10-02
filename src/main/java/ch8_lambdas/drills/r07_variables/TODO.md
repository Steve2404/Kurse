# Drill de rappel 7 — Les variables dans les lambdas

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`** dans le paquet `ch8_lambdas.drills.r07_variables`, avec :
  - `private int instanceCounter;` ;
  - `private static int staticCounter;` ;
  - `private final String name = "drill";` ;
  - `IntSupplier counters()`, qui rend `() -> ++instanceCounter + 10 * ++staticCounter` ;
  - `String whoIsThis()` :
    - une lambda `() -> this.name` ;
    - une classe anonyme `new Supplier<>() { private final String name = "anonyme"; … get() rend this.name }` ;
    - elle rend les deux résultats, séparés par un espace.

## Défis

- ☐ **D01.** `int base = 5;` et `IntSupplier plusBase = () -> base + 1;`.
  → `D01 : 6`
- ☐ **D02.** `Recall07 r = new Recall07(); IntSupplier c = r.counters();`. Appelle `c.getAsInt()` une fois sans l'afficher. Puis affiche `c.getAsInt()`, `r.instanceCounter` et `staticCounter`.
  → `D02 : 22 2 2`
- ☐ **D03.** `int[] box = {0};` et `Runnable inc = () -> box[0]++;`, exécuté 3 fois. Affiche `box[0]`.
  → `D03 : 3`
- ☐ **D04.** `r.whoIsThis()`.
  → `D04 : drill anonyme`
- ☐ **D05.** `String label = "x";` et `Supplier<String> twice = () -> label + label;`.
  → `D05 : xx`
- ☐ **D06.** Dans cet ordre :
  1. `StringBuilder sb = new StringBuilder("a");` ;
  2. `Supplier<String> view = sb::toString;` ;
  3. `sb.append("b");`.
  
  Puis affiche `view.get()`.
  → `D06 : ab`

## Expériences (hors sortie attendue)

1. Après D01, écris `base = 6;` : quelle erreur, et sur quelle ligne ?
2. Dans D03, remplace le tableau par `int count = 0;` et `() -> count++` : quelle erreur ?
3. `String label = "y";` dans le corps de la lambda de D05 : quelle erreur ? (Un paramètre ou une variable locale d'une lambda ne peut pas masquer une variable locale englobante.)
4. `IntSupplier bad = () -> { int base = 1; return base; };` après D01 : quelle erreur ?
5. Pourquoi D06 affiche-t-il `ab` et non `a` ?

## Sortie attendue complète

```
D01 : 6
D02 : 22 2 2
D03 : 3
D04 : drill anonyme
D05 : xx
D06 : ab
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Ce que lit la lambda | Lire | Modifier |
|---|---|---|
| ses propres paramètres et variables locales | oui | oui |
| une variable locale ou un paramètre de la méthode englobante | oui, si **effectively final** | **non** |
| un champ d'instance (via `this`) | oui | oui |
| un champ `static` | oui | oui |
| le **contenu** d'un objet capturé (tableau, `StringBuilder`) | oui | oui : c'est la **référence** qui est figée |

**La portée :**
- une lambda ne crée **pas** de nouvelle portée pour les noms ;
- ses paramètres et variables ne peuvent pas reprendre le nom d'une variable locale englobante ;
- **`this`** désigne l'objet englobant. Dans une classe anonyme, `this` désigne l'anonyme.

**Effectively final :** jamais réaffectée **nulle part** dans la méthode, ni avant, ni après la lambda.

</details>
