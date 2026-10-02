# Drill de rappel 2 — Les interfaces fonctionnelles

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall02.java`, paquet `ch8_lambdas.drills.r02_functional`. Sous `Recall02` :

| Interface | Contenu |
|---|---|
| `@FunctionalInterface interface Greeter` | `String greet(String name);` ; `default Greeter twice()`, qui rend `n -> greet(greet(n))` ; `static Greeter polite()`, qui rend `n -> "Bonjour " + n` ; `boolean equals(Object o);` et `String toString();` redéclarées |
| `interface Shout extends Greeter` | vide |
| `@FunctionalInterface interface Echo extends Greeter` | redéclare `String greet(String name);` |
| `@FunctionalInterface interface Counter` | `int next();` ; `static Counter start()`, qui rend un compteur (`int[] n = {0}; return () -> ++n[0];`) |

## Défis

- ☐ **D01.** `Greeter.polite().greet("Ana")`.
  → `D01 : Bonjour Ana`
- ☐ **D02.** `Greeter g = n -> n + "!";`. Affiche `g.twice().greet("hi")`, puis `g.twice().twice().greet("x")`.
  → `D02 : hi!! x!!!!`
- ☐ **D03.** `Shout s = n -> n.toUpperCase();`, `Echo e = n -> n + n;` et `Greeter asGreeter = e;`. Affiche `s.greet("ok")`, `e.greet("ab")`, puis `asGreeter.greet("c")`.
  → `D03 : OK abab cc`
- ☐ **D04.** Trois résultats :
  - `Greeter a = n -> "[" + n + "]"` appliqué à `"x"` ;
  - `UnaryOperator<String> b`, avec le **même** corps, appliqué à `"x"` ;
  - `Object o = (Greeter) n -> n;`, puis `o instanceof Greeter`.
  → `D04 : [x] [x] true`
- ☐ **D05.** Une **classe anonyme** `new Greeter() { … }` qui rend `"anonyme " + n`, appliquée à `"z"`. Puis `Counter.start().next()` deux fois (deux compteurs différents).
  → `D05 : anonyme z 1 1`

## Expériences (hors sortie attendue)

1. Ajoute `void reset();` à `Greeter` : que dit `@FunctionalInterface` ? Et sans l'annotation, qu'est-ce qui casse ?
2. `@FunctionalInterface interface Empty {}` : quelle erreur ?
3. `@FunctionalInterface` sur une **classe**, ou sur une interface avec `boolean equals(Object o);` **seule** : quelles erreurs ?
4. `Object o = n -> n;` (sans cast) : quelle erreur ?
5. Une interface qui hérite de **deux** interfaces fonctionnelles aux méthodes différentes : est-elle fonctionnelle ?

## Sortie attendue complète

```
D01 : Bonjour Ana
D02 : hi!! x!!!!
D03 : OK abab cc
D04 : [x] [x] true
D05 : anonyme z 1 1
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Une interface fonctionnelle** a **exactement une** méthode abstraite.

**Ne comptent pas :**
- les méthodes `default`, `static` et `private` ;
- les méthodes abstraites qui redéclarent une méthode **publique** d'`Object` (`equals`, `hashCode`, `toString`).

**L'héritage :**
- une interface qui hérite d'une interface fonctionnelle sans ajouter de méthode abstraite reste fonctionnelle ;
- redéclarer la même méthode ne change rien.

**`@FunctionalInterface` :** facultative, elle demande à `javac` de **vérifier** la règle (une erreur sinon). Elle ne s'applique qu'aux interfaces.

**Le type cible :** une même lambda peut viser plusieurs interfaces compatibles. Vers `Object`, il faut un cast vers l'interface.

**Une classe anonyme** peut implémenter n'importe quelle interface, avoir un état et plusieurs méthodes. Une lambda, non.

</details>
