# Drill de rappel 2 — Les constructeurs

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall02.java`, paquet `ch6_classdesign.drills.r02_constructors`. `Recall02` contient :
  - `static final StringBuilder LOG = new StringBuilder();` ;
  - `static String flush()`, qui rend `LOG` sans les espaces de bord, puis le vide.
- Chaque constructeur ajoute son nom à `LOG`, précédé d'un espace.
- Les classes non publiques :

| Classe | Contenu |
|---|---|
| `Point` | `int x, y` ; `Point()` délègue avec `this(0, 0)`, puis note ` Point()` ; `Point(int x, int y)` affecte avec `this.x = x`… puis note ` Point(int,int)` |
| `Point3D extends Point` | `int z` ; `Point3D()` note seulement ` Point3D()` (sans `super` écrit) ; `Point3D(int x, int y, int z)` appelle `super(x, y)`, affecte z, note ` Point3D(int,int,int)` |
| `Empty` | vide : aucun constructeur écrit |
| `Weird` | `Weird()` note ` constructeur` ; une **méthode** `void Weird()` note ` methode` |
| `Ticket` | `private static int next = 100` ; `final int number` ; `Ticket()` délègue avec `this(next++)` ; `Ticket(int number)` |
| `Config` | `static int created` ; `final String mode` ; un constructeur **`private`** `Config(String)` qui compte ; une fabrique `static Config defaults()` qui rend `new Config("standard")` |

## Défis

- ☐ **D01.** `new Point()`, puis `flush()`, `|` et ses coordonnées `x,y`.
  → `D01 : Point(int,int) Point() | 0,0`
- ☐ **D02.** `new Point3D()`, puis le journal et `x,y,z`.
  → `D02 : Point(int,int) Point() Point3D() | 0,0,0`
- ☐ **D03.** `new Point3D(1, 2, 3)`, puis le journal et `x,y,z`.
  → `D03 : Point(int,int) Point3D(int,int,int) | 1,2,3`
- ☐ **D04.** `Weird w = new Weird(); w.Weird();`. Affiche le journal, puis `new Empty() != null`.
  → `D04 : constructeur methode true`
- ☐ **D05.** `new Ticket()`, `new Ticket(42)`, `new Ticket()` : affiche leurs trois numéros.
  → `D05 : 100 42 101`
- ☐ **D06.** `Config.defaults()` : affiche son `mode`, puis `Config.created`.
  → `D06 : standard 1`

## Expériences (hors sortie attendue)

1. Dans `Point3D()`, appelle `this(0, 0, 0)` **puis** `super()` : quelle erreur ?
2. Supprime `Point()` (garde `Point(int, int)`) : pourquoi `Point3D()` ne compile-t-il plus ?
3. Dans `Ticket(int)`, oublie d'affecter `number` : quelle erreur ?
4. `new Config("x")` depuis `Recall02` : quelle erreur ?
5. Deux constructeurs qui s'appellent mutuellement avec `this(...)` : que dit `javac` ?
6. Ajoute `Empty(int n) {}` à `Empty` : `new Empty()` compile-t-il encore ?

## Sortie attendue complète

```
D01 : Point(int,int) Point() | 0,0
D02 : Point(int,int) Point() Point3D() | 0,0,0
D03 : Point(int,int) Point3D(int,int,int) | 1,2,3
D04 : constructeur methode true
D05 : 100 42 101
D06 : standard 1
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**La première ligne d'un constructeur :**
- c'est `this(...)` **ou** `super(...)`, jamais les deux ;
- si tu n'écris rien, javac insère `super();`, et le parent **doit** alors avoir un constructeur sans argument accessible.

**Le constructeur par défaut :**
- javac le crée **seulement** si la classe n'en déclare aucun ;
- il n'a pas de paramètre, il a l'accès de la classe, et son corps est `super();`.

**Un constructeur :**
- porte le nom de la classe et n'a **pas de type de retour** ;
- avec `void`, c'est une méthode.

**`this` :**
- `this.x = x` lève l'ambiguïté avec le paramètre ;
- `this(...)` appelle un autre constructeur de la même classe ;
- les appels `this(...)` ne doivent pas tourner en rond.

**Un champ `final` d'instance :**
- il est affecté exactement une fois, sur chaque chemin : sur sa ligne, dans un bloc `{ }` ou dans chaque constructeur ;
- un constructeur qui délègue avec `this(...)` ne l'affecte pas lui-même.

**Un constructeur `private`, plus une fabrique `static` :** on contrôle la création (compteur, valeurs par défaut, cache).

</details>
