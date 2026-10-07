# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Symbolic`, `Suit`, `Rank`, `Category`, `Card`, `Hand` et `Poker`.
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18), sur des types de test réduits.

---

## Étape 1 — Couleurs et rangs

**Le code :** [`Symbolic.java`](Symbolic.java), [`Suit.java`](Suit.java) et [`Rank.java`](Rank.java).

**Un enum est une classe :** il peut avoir des champs, un constructeur (toujours `private`, même sans le mot-clé), des méthodes, et implémenter une interface. Ses constantes sont les **seules** instances possibles.

**La ligne `rangs`, expliquée :**
- `Rank.values().length` vaut **13**. `values()` rend un **nouveau** tableau à chaque appel.
- `Rank.valueOf("QUEEN").ordinal()` vaut **10** : TWO est 0, donc QUEEN est 10.
- `ACE.compareTo(KING)` vaut **1** et `TWO.compareTo(ACE)` vaut **−12** : `compareTo` rend la **différence** des `ordinal()`.
- `Rank.of('T').name()` vaut `TEN`.
- `Category.values()[5].label()` vaut `couleur` : la 6e constante est FLUSH.
- `Suit.of('H') == Suit.HEARTS` vaut `true` : chaque constante est **unique**, donc `==` suffit.

**`describe()` dans l'interface** utilise `this`, qui est la constante, et son `toString()` par défaut rend son **nom** : `C=CLUBS`.

---

## Étape 2 — Les catégories et les mains

**Le code :** [`Category.java`](Category.java), [`Card.java`](Card.java) et [`Hand.java`](Hand.java).

**Des constantes avec un corps :** chaque constante de `Category` est en réalité une petite **sous-classe anonyme** de l'enum, qui implémente `matches`. L'ordre des constantes encode la **force** des mains, et `compareTo` compare donc directement les catégories.

**`Category.of` part de la plus forte :** une quinte flush correspond aussi à `FLUSH`, `STRAIGHT` et `HIGH_CARD`. En partant d'en haut, on garde la **meilleure**.

**Les records :**
- `record Card(Rank rank, Suit suit)` génère les champs `private final`, le constructeur canonique, les **accesseurs** `rank()` et `suit()`, puis `equals`, `hashCode` et `toString`.
- Deux `Card` de même rang et même couleur sont donc `equals`, d'où `record egal true`. `toString` est redéfini ici pour afficher `AS`.
- **Le constructeur compact** `public Hand { … }` n'a pas de liste de paramètres. On y **remplace** les paramètres (`cards = cards.clone()`) **avant** l'affectation automatique des champs.
- **Un constructeur supplémentaire** (`Hand(Card[] cards)`) **doit** appeler le canonique avec `this(…)`.

**La roue A-2-3-4-5 :** l'As y compte 1. `tieBreak` rend `{5, 4, 3, 2, 1}`, donc la roue est la **plus petite** suite : `roue < suite au 6 true`.

**Le kicker :** deux paires de rois, puis on compare les cartes restantes. L'As bat le 4, d'où `true`.

---

## Étape 3 — Mélanger et distribuer

**Le code :** `nextInt` et la partie « donne » du `main` de [`Poker.java`](Poker.java).

**Pourquoi un générateur maison ?** `Math.random()` ou `new Random()` sans graine donnent une sortie différente à chaque lancement, et `Check` ne pourrait pas comparer. Un générateur congruentiel avec une graine fixe est **déterministe**.

**`& Long.MAX_VALUE`** met le bit de signe à 0, donc le résultat reste positif. **`>>> 17`** jette les bits de poids faible, les moins aléatoires d'un générateur congruentiel.

**Fisher-Yates :** à l'étape i, on choisit au hasard parmi les i + 1 premières cartes celle qui ira en position i. Chaque ordre final a la **même** probabilité. Une erreur fréquente, `nextInt(52)` à chaque étape, donne un mélange **biaisé**.

---

## Étape 4 — Texas hold'em

**Le code :** la méthode `best` et la fin du `main`.

**C(7, 5) = 21 :** choisir les 5 cartes gardées revient à choisir les 2 **exclues**, avec deux boucles `a < b`.

**Le résultat :** le tableau `KH 9H 4C 9S 2H` contient une paire de 9. Le joueur 3 (`KC KS`) fait **full** (K K K 9 9), comme le joueur 2 (`9D KD`, qui fait 9 9 9 K K). La clé départage : brelan de **rois** contre brelan de 9. Le joueur 3 gagne.

**Expériences :**

| Expérience | Résultat |
|---|---|
| `new Suit('X', true)` | `error: enum types may not be instantiated` : les instances d'un enum sont fixées à la compilation |
| retirer le corps de `PAIR` | `error: Category is abstract; cannot be instantiated` : sans corps, `PAIR` serait une instance directe de l'enum abstrait |
| `Rank.valueOf("queen")` | **à l'exécution** : `IllegalArgumentException: No enum constant Rank.queen`. `valueOf` respecte la casse **exacte** du nom |

**Question — pourquoi `Hand` a besoin de copies défensives ?** Un record rend ses champs `final`, mais `final` sur un **tableau** ne protège que la référence. Vérifié sur un record de test **sans** copie : après `k[0] = 99` (le tableau passé au constructeur) et `h.key()[1] = 77` (le tableau rendu par l'accesseur), le record contient `[99, 77]`. Un record n'est donc immuable que si ses composants le sont. D'où les `clone()` dans le constructeur compact **et** dans l'accesseur `cards()` redéfini.
