# Drill de rappel 7 — Les classes imbriquées

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall07.java`, paquet `ch7_beyondclasses.drills.r07_nested`. Sous `Recall07` : `interface Greeter { String greet(); }` et `class Outer`.
- **`Outer`** contient :
  - `private int secret = 7;` et `private static int count = 4;` ;
  - une **classe interne** `Inner` :
    - `private int secret = 1;` (elle masque celui d'`Outer`) et `int x = 10;` ;
    - `sum()` rend `x + Outer.this.secret` ;
    - `who()` rend `secret + "/" + this.secret + "/" + Outer.this.secret` ;
  - une **classe `static`** `Nested` : `static final int LIMIT = 99;` ; `value()` rend `count + 1` ;
  - `Inner makeInner()`, qui rend `new Inner()` ;
  - `int local(int factor)` : une variable locale `base = 2`, puis une **classe locale** `Local` dont `calc()` rend `base * factor * secret` ;
  - `Greeter anon(String name)` : rend une **classe anonyme** dont `greet()` rend `"salut " + name` ;
  - `int counter()` :
    - `int[] calls = {0};` ;
    - une **anonyme qui étend `Object`** : son `toString()` incrémente `calls[0]` et rend `anonyme` ;
    - concatène deux appels à `toString()` ;
    - rend `calls[0] + longueur`.

## Défis

- ☐ **D01.** `Outer o = new Outer(); Outer.Inner in = o.new Inner();`. Affiche `in.sum()` et `o.makeInner().sum()`.
  → `D01 : 17 17`
- ☐ **D02.** `in.who()`.
  → `D02 : 1/1/7`
- ☐ **D03.** `new Outer.Nested().value()`, puis `Outer.Nested.LIMIT`.
  → `D03 : 5 99`
- ☐ **D04.** `o.local(3)`.
  → `D04 : 42`
- ☐ **D05.** `g = o.anon("Ana")`. Affiche `g.greet()`, `g.getClass().isAnonymousClass()`, puis `[` + `getSimpleName()` + `]`.
  → `D05 : salut Ana true []`
- ☐ **D06.** Dans `main`, une anonyme `new Greeter() { … }` dont `greet()` rend `bonjour`. Affiche son `greet()`, puis `o.counter()`.
  → `D06 : bonjour 16`

## Expériences (hors sortie attendue)

1. `new Outer.Inner()` depuis `main` : quelle erreur ?
2. Dans `Nested`, lis `secret` : quelle erreur ? Pourquoi ?
3. Dans `local`, écris `base++;` **après** la classe locale : quelle erreur ?
4. Dans `counter()`, remplace `int[] calls` par un `int calls` et fais `calls++` dans l'anonyme : quelle erreur ?
5. Une classe anonyme peut-elle avoir un constructeur ? Peut-elle implémenter **deux** interfaces ?
6. Déclare une classe locale `public class X {}` : quelle erreur ?

## Sortie attendue complète

```
D01 : 17 17
D02 : 1/1/7
D03 : 5 99
D04 : 42
D05 : salut Ana true []
D06 : bonjour 16
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Sorte | Où | Création | Voit les membres d'instance de l'englobante ? | Modificateurs d'accès |
|---|---|---|---|---|
| **interne** (membre) | dans la classe | `outer.new Inner()` | oui, via `Outer.this.x` | tous |
| **`static` imbriquée** | dans la classe | `new Outer.Nested()` | non (seulement le `static`) | tous |
| **locale** | dans une méthode | `new Local()` dans la méthode | oui (si la méthode est d'instance) + les locales **effectively final** | aucun |
| **anonyme** | dans une expression | `new Type() { … }` | comme une locale | aucun |

- Une anonyme étend **une** classe **ou** implémente **une** interface, et n'a pas de constructeur.
- L'englobante voit les membres `private` de ses classes imbriquées, et inversement.
- Depuis Java 16, une classe interne peut avoir des membres `static`.

</details>
