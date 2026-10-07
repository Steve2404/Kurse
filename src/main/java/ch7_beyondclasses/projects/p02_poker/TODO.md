# Projet 2 — Le poker (enums et records)

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 7) :**
- les **enums** :
  - des constantes avec arguments ;
  - des champs, un constructeur (implicitement `private`) et des méthodes ;
  - un enum qui **implémente une interface** ;
- une **méthode abstraite** dans un enum, avec un **corps par constante** ;
- `values()`, `valueOf()`, `ordinal()`, `name()`, `compareTo()`, et `==` entre constantes ;
- les **records** :
  - les composants, les accesseurs générés ;
  - `equals`, `hashCode` et `toString` générés ;
  - `toString` redéfini ;
  - un **constructeur compact** ;
  - un constructeur supplémentaire qui délègue avec `this(...)` ;
  - un accesseur redéfini.

Côté algorithmes :
- le mélange de **Fisher-Yates** avec un générateur congruentiel (déterministe) ;
- la **détection des mains** ;
- le **départage** (groupes, puis hauteurs ; la roue A-2-3-4-5) ;
- la **meilleure main de 5 parmi 7** (21 combinaisons).

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch7_beyondclasses.projects.p02_poker` :
- l'interface `Symbolic` ;
- les enums `Suit`, `Rank`, `Category` ;
- les records `Card` et `Hand` ;
- **`Poker`** (le `main`).

**Règle du crescendo :** chapitres 1 à 7. Pas de collection, de lambda ni de `Comparable` (l'interface est générique : chapitre 9). `Hand` a sa propre méthode `compareTo(Hand)`.

---

## Tableau de bord

### ☐ Étape 1 — Couleurs et rangs

```
couleurs : C=CLUBS(noire) D=DIAMONDS(rouge) H=HEARTS(rouge) S=SPADES(noire)
rangs : 13, 10 1 -12 TEN couleur true
```
- **`Symbolic`** :
  - `char symbol()` ;
  - `default String describe()`, qui rend `symbol() + "=" + this`. Le `toString()` d'un enum est son nom.
- **`enum Suit implements Symbolic`** :
  - `CLUBS('C', false)`, `DIAMONDS('D', true)`, `HEARTS('H', true)`, `SPADES('S', false)` ;
  - les champs `symbol` et `red` ;
  - `isRed()` ;
  - `static Suit of(char)`, qui parcourt `values()`.
- **`enum Rank implements Symbolic`** : de `TWO(2, '2')` à `ACE(14, 'A')` (le dix s'écrit `T`), avec `value()` et `of(char)`.
- **La 2e ligne :**
  - `Rank.values().length` ;
  - `Rank.valueOf("QUEEN").ordinal()` ;
  - `ACE.compareTo(KING)` et `TWO.compareTo(ACE)` ;
  - `Rank.of('T').name()` ;
  - `Category.values()[5].label()` ;
  - `Suit.of('H') == Suit.HEARTS`.

### ☐ Étape 2 — Les catégories et les mains

```
AS KS QS JS TS -> quinte flush
...
AH JD 8C 5S 3D -> hauteur
departage : roue < suite au 6 true, paire de rois kicker As > kicker 4 true, record egal true
```
- **`enum Category`**, de la plus faible à la plus forte : `HIGH_CARD("hauteur")`, `PAIR("paire")`, `TWO_PAIR("double paire")`, `THREE_OF_A_KIND("brelan")`, `STRAIGHT("suite")`, `FLUSH("couleur")`, `FULL_HOUSE("full")`, `FOUR_OF_A_KIND("carre")`, `STRAIGHT_FLUSH("quinte flush")`.
  - `abstract boolean matches(int[] groups, boolean flush, boolean straight)` : **chaque constante** l'implémente dans son corps `{ … }`.
  - `groups` = les tailles des groupes de même rang, triées décroissantes (`{3, 2, …}` pour un full).
  - `static Category of(…)` parcourt `values()` de la **dernière** à la première et rend la première qui correspond.
- **`record Card(Rank rank, Suit suit)`** :
  - `static Card parse("AS")` et `static Card[] parseAll("AS KS …")` ;
  - `toString()` redéfini → `AS`.
- **`record Hand(Card[] cards, Category category, int[] key)`** :
  - un **constructeur compact** `public Hand { cards = cards.clone(); key = key.clone(); }` ;
  - un constructeur **`Hand(Card[] cards)`** qui délègue avec `this(cards, evaluateCategory(cards), tieBreak(cards))` ;
  - `tieBreak` = les valeurs triées par nombre d'exemplaires décroissant, puis par valeur décroissante. La roue A-2-3-4-5 donne `{5, 4, 3, 2, 1}` ;
  - `isStraight` : 5 valeurs consécutives, ou la roue ;
  - la couleur (`flush`) : toutes les cartes ont la **même** couleur, testé avec `==` ;
  - `public int compareTo(Hand other)` : la catégorie (`compareTo` des enums), puis la clé, case par case ;
  - l'accesseur `cards()` est redéfini pour rendre une **copie** ;
  - `toString()` = `cartes -> label`.
- **Le départage :**
  - `AC 2D 3H 4S 5C` contre `2C 3D 4H 5S 6C` ;
  - `KH KS 2D 3C AC` contre `KC KD 2H 3S 4C` ;
  - `new Card(Rank.ACE, Suit.SPADES).equals(Card.parse("AS"))`.

### ☐ Étape 3 — Mélanger et distribuer

```
joueur 1 : 9H 3S 8H 6C 6S -> paire
...
gagnant : joueur 1 avec paire
```
- **Le générateur :** `seed = Data.SEED`, puis `nextInt(bound)` :
  - `seed = (seed * 6364136223846793005L + 1442695040888963407L) & Long.MAX_VALUE;` ;
  - `return (int) ((seed >>> 17) % bound);`.
- **Le jeu :** 52 cartes, toutes les couleurs (dans l'ordre de `values()`) puis tous les rangs.
- **Fisher-Yates :** pour i de 51 à 1, échange `deck[i]` et `deck[nextInt(i + 1)]`.
- **La donne :** le joueur p reçoit les cartes `deck[c * PLAYERS + p]`, pour c de 0 à 4. Le meilleur l'emporte (le premier en cas d'égalité).

### ☐ Étape 4 — Texas hold'em

```
hold'em sur KH 9H 4C 9S 2H : [AH 3H: couleur] [9D KD: full] [KC KS: full] [QH JH: couleur]
gagnant hold'em : joueur 3 (KH 9H 9S KC KS -> full)
```
- `static Hand best(Card[] seven)` : les 21 façons d'**exclure** 2 cartes parmi 7. Garde la meilleure main (la première en cas d'égalité).
- Chaque joueur = `Data.BOARD` + `Data.HOLES[p]`.
- **Expériences :**
  - `new Suit('X', true)` : quelle erreur ?
  - retire le corps de `PAIR` : quelle erreur ?
  - `Rank.valueOf("queen")` : que se passe-t-il à l'exécution ?
  - pourquoi `record Hand` avec des tableaux a-t-il besoin de copies défensives ?

---

## Checklist (vérifiée par `Check`)

- `Data.HANDS`, `Data.SEED`, `Data.BOARD` ;
- `enum Suit implements Symbolic`, `enum Rank implements Symbolic`, `enum Category` ;
- `record Card(` et `record Hand(` ;
- `abstract boolean matches(` et ses 9 corps ;
- `values()`, `ordinal()`, `compareTo(`, `valueOf(`, `name()` ;
- `public Hand {` et `this(cards`.

---

## Sortie attendue complète

```
couleurs : C=CLUBS(noire) D=DIAMONDS(rouge) H=HEARTS(rouge) S=SPADES(noire)
rangs : 13, 10 1 -12 TEN couleur true
AS KS QS JS TS -> quinte flush
9C 9D 9H 9S 2D -> carre
3H 3D 3S 7C 7D -> full
2H 8H 5H JH KH -> couleur
9D TC JS QH KD -> suite
AC 2D 3H 4S 5C -> suite
7S 7H 7D KC 2S -> brelan
4C 4D JH JS AC -> double paire
QD QS 6C 3H 2S -> paire
AH JD 8C 5S 3D -> hauteur
departage : roue < suite au 6 true, paire de rois kicker As > kicker 4 true, record egal true
joueur 1 : 9H 3S 8H 6C 6S -> paire
joueur 2 : JS 4D 5S 2H 4C -> paire
joueur 3 : TD 3H QH KC AD -> hauteur
joueur 4 : JD 6H 7C 2D 9S -> hauteur
gagnant : joueur 1 avec paire
hold'em sur KH 9H 4C 9S 2H : [AH 3H: couleur] [9D KD: full] [KC KS: full] [QH JH: couleur]
gagnant hold'em : joueur 3 (KH 9H 9S KC KS -> full)
```
