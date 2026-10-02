# Drill de rappel 6 — Les records

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall06.java`, paquet `ch7_beyondclasses.drills.r06_records`. Sous `Recall06` :

| Record | Contenu |
|---|---|
| `record Point(int x, int y)` | `static final Point ORIGIN = new Point(0, 0)` ; un **constructeur compact** qui remplace un x négatif par 0 ; `Point(int both)` délègue avec `this(both, both)` ; `static Point of("4,5")` ; `double dist()` = √(x² + y²) |
| `record Range(int lo, int hi)` | `static int count` ; un constructeur compact qui **échange** lo et hi si `lo > hi`, puis incrémente `count` |
| `record Person(String name, int[] scores)` | rien d'autre |

## Défis

- ☐ **D01.** `p = new Point(3, 4)`. Affiche p, `p.x()`, `p.y()` et `p.dist()`.
  → `D01 : Point[x=3, y=4] 3 4 5.0`
- ☐ **D02.** `q = new Point(3, 4)`. Affiche :
  - `p.equals(q)` et `p == q` ;
  - l'égalité des `hashCode` ;
  - `p.equals(new Point(4, 3))`.
  → `D02 : true false true false`
- ☐ **D03.** `new Point(-5, 2)`, `new Range(9, 2)` et `new Point(7)`.
  → `D03 : Point[x=0, y=2] Range[lo=2, hi=9] Point[x=7, y=7]`
- ☐ **D04.** `Point.ORIGIN`, `Point.of("4,5")`, puis `Range.count`.
  → `D04 : Point[x=0, y=0] Point[x=4, y=5] 1`
- ☐ **D05.** `a` et `b` sont deux `Person("Ana", new int[] {1, 2})`. Affiche :
  - `a.equals(b)` ;
  - `a.name().equals(b.name())` ;
  - `Arrays.equals(a.scores(), b.scores())`.
  → `D05 : false true true`
- ☐ **D06.** Dans `main`, déclare un record **local** `record Pair(String key, int value) {}`. Avec `pair = new Pair("k", 1)`, affiche pair, `pair.key()`, puis `new Pair("k", 1).equals(pair)`.
  → `D06 : Pair[key=k, value=1] k true`

## Expériences (hors sortie attendue)

1. Ajoute un champ d'instance `int z;` dans `Point` : quelle erreur ?
2. Dans le constructeur compact, écris `this.x = 0;` : quelle erreur ?
3. `Point(int both) { x = both; y = both; }` (sans `this(...)`) : quelle erreur ?
4. Écris le constructeur **canonique long** `Point(int x, int y) { … }` en oubliant d'affecter y : quelle erreur ?
5. `record Point3(int x) extends Point` : quelle erreur ? Et `record R(int a) implements Comparable` (interfaces : oui ; héritage : non) ?
6. Un setter `void setX(int x)` : peut-il modifier le champ ?

## Sortie attendue complète

```
D01 : Point[x=3, y=4] 3 4 5.0
D02 : true false true false
D03 : Point[x=0, y=2] Range[lo=2, hi=9] Point[x=7, y=7]
D04 : Point[x=0, y=0] Point[x=4, y=5] 1
D05 : false true true
D06 : Pair[key=k, value=1] k true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Un `record R(int a, String b)` génère :**
- des champs `private final` ;
- le constructeur canonique `R(int a, String b)` ;
- les accesseurs `a()` et `b()` (et non `getA()`) ;
- `equals`, `hashCode` et `toString` (`R[a=1, b=x]`) ;
- la classe est implicitement `final`.

**Les trois sortes de constructeurs :**
- **compact** : `R { … }`. Pas de parenthèses ; on lit et on **réaffecte les paramètres** ; les champs sont affectés après, automatiquement ;
- **canonique long** : `R(int a, String b) { this.a = a; … }`, où chaque champ doit être affecté ;
- **supplémentaire** : sa première instruction est `this(...)`.

**Permis :** des membres `static` (champs, méthodes), des méthodes d'instance, des records ou classes imbriqués, `implements`, et des records locaux.

**Interdit :** des champs d'instance en plus, `extends`, et modifier un champ.

**Le piège :** un composant tableau. `equals` compare la référence du tableau, et le contenu reste modifiable sans copie défensive.

</details>
