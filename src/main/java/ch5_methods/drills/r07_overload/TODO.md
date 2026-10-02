# Drill de rappel 7 — La résolution des surcharges

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`** dans le paquet `ch5_methods.drills.r07_overload`. Chaque surcharge rend son type (`"int"`, `"Object"`…).
- **Les surcharges à écrire :**

| Famille | Surcharges |
|---|---|
| `f` | `int`, `long`, `Integer`, `int...` |
| `g` | `double`, `Object` |
| `h` | `Long`, `Number` |
| `k` | `String`, `Object` |
| `m` | `int[]`, `Object` |
| `p` | `short`, `char`, `float` |

- **Prédis chaque mot sur papier** avant de lancer. C'est le vrai score.

## Défis

`byte b = 1;` et `short s = 3;`.

- ☐ **D01.** `f(1)`, `f(b)`, `f('c')`, `f(1L)`, `f(Integer.valueOf(1))`, `f()` et `f(1, 2)`.
  → `D01 : int int int long Integer int... int...`
- ☐ **D02.** `g(1)`, `g(1.5f)`, `g('a')`, `g("s")` et `g(true)`.
  → `D02 : double double double Object Object`
- ☐ **D03.** `h(1L)`, `h(1)`, `h(Long.valueOf(1))` et `h(1.0)`.
  → `D03 : Long Number Long Number`
- ☐ **D04.** Avec `Object o = "texte";` : `k("a")`, `k(o)`, `k(null)` et `k((Object) null)`.
  → `D04 : String Object String Object`
- ☐ **D05.** `m(new int[2])`, `m(null)`, `m(new int[2][2])` et `m(new Integer[1])`.
  → `D05 : int[] int[] Object Object`
- ☐ **D06.** `p(s)`, `p('x')`, `p(3)`, `p(b)` et `p(3L)`.
  → `D06 : short char float short float`

## Expériences (hors sortie attendue)

1. Ajoute `f(Object)`, puis appelle `f(null)` : quel résultat, ou quelle erreur ?
2. `h(1)` : pourquoi `Number` et pas `Long` ? Ajoute `h(long)` : que devient le résultat ?
3. Ajoute `p(int)`, puis appelle `p(b)` : `short` ou `int` ?
4. Deux méthodes `int q(int a, long b)` et `int q(long a, int b)`, puis `q(1, 1)` : que dit `javac` ?

## Sortie attendue complète

```
D01 : int int int long Integer int... int...
D02 : double double double Object Object
D03 : Long Number Long Number
D04 : String Object String Object
D05 : int[] int[] Object Object
D06 : short char float short float
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les trois phases** (la première qui trouve au moins une version applicable s'arrête) :
1. **Sans boxing ni varargs :** type exact, élargissement primitif (`byte` → `short` → `int` → `long` → `float` → `double` ; `char` → `int`), élargissement de référence (vers une superclasse ou une interface) ;
2. **Avec boxing / unboxing**, éventuellement suivi d'un élargissement de **référence** (`int` → `Integer` → `Number` → `Object`) ;
3. **Avec varargs.**

**Dans une phase,** Java choisit la version **la plus spécifique**, celle dont les paramètres sont acceptés par toutes les autres. Si aucune ne l'est : *ambiguous*.

**Les pièges :**
- `int` ne devient **jamais** un `Long`, ni un `long` puis un `Long` ;
- `byte` → `short` est un élargissement, mais `byte` → `char` n'en est pas un ;
- `long` → `float` est un élargissement (avec perte de précision possible) ;
- `null` va à la version la plus spécifique (`String` plutôt qu'`Object`, `int[]` plutôt qu'`Object`) ;
- c'est le type **déclaré** de l'argument qui compte (`Object o = "texte"` → `k(Object)`).

</details>
