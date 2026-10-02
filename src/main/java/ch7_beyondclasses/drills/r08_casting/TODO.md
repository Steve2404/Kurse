# Drill de rappel 8 — Polymorphisme et casts

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall08.java`, paquet `ch7_beyondclasses.drills.r08_casting`. Sous `Recall08` :

| Type | Contenu |
|---|---|
| `interface Pet` | `default String owner()` rend `Leo` |
| `class Animal` | `String name = "animal"` ; `sound()` rend `...` ; `label()` rend `name` |
| `class Dog extends Animal implements Pet` | `String name = "chien"` (masque) ; `sound()` rend `ouaf` ; `fetch()` rend `rapporte` |
| `class Cat extends Animal` | `sound()` rend `miaou` |
| `final class Rock` | vide |

- Dans `Recall08`, `static String fetchIfDog(Animal a)` : si `a instanceof Dog`, fais `Dog d = (Dog) a;` et rends `d.fetch()`. Sinon, rends `impossible`.

## Défis

- ☐ **D01.** `Animal a = new Dog();`. Affiche `a.sound()`, `((Dog) a).fetch()`, puis `fetchIfDog(new Cat())`.
  → `D01 : ouaf rapporte impossible`
- ☐ **D02.** Quatre tests :
  - `a instanceof Pet` ;
  - `a instanceof Cat` ;
  - `new Cat() instanceof Pet` ;
  - `a instanceof Object`.
  → `D02 : true false false true`
- ☐ **D03.** `a.name`, `((Dog) a).name`, puis `a.label()`.
  → `D03 : animal chien animal`
- ☐ **D04.** `Object o = "texte";`. Avec `o instanceof String s && s.length() > 3`, affiche `long <longueur>`, sinon `court`. Puis `((String) o).toUpperCase()`.
  → `D04 : long 5 TEXTE`
- ☐ **D05.** `Pet p = new Dog();`, puis `Animal back = (Animal) p;`. Affiche `p.owner()`, `back.sound()`, puis `(Object) p == back`.
  → `D05 : Leo ouaf true`
- ☐ **D06.** Pour `{new Dog(), new Cat(), new Animal()}` : chaque `sound()`, suivi de `(pet)` si c'est un `Pet`.
  → `D06 : ouaf(pet) miaou ...`

## Expériences (hors sortie attendue)

1. `Cat c = (Cat) a;` : ça compile ? Que se passe-t-il à l'exécution ?
2. `Dog d = a;` (sans cast) : quelle erreur ?
3. `new Rock() instanceof Pet` : quelle erreur ? Pourquoi `new Cat() instanceof Pet` compile-t-il, lui ?
4. `String s = (String) a;` : quelle erreur ?
5. `a.fetch()` : quelle erreur ? Le type de la référence décide.
6. `Integer i = (Integer) (Object) "x";` : compile ? exécution ?

## Sortie attendue complète

```
D01 : ouaf rapporte impossible
D02 : true false false true
D03 : animal chien animal
D04 : long 5 TEXTE
D05 : Leo ouaf true
D06 : ouaf(pet) miaou ...
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Référence ou objet :**
- le type de la **référence** décide de ce qu'on peut **appeler** (à la compilation) ;
- le type de l'**objet** décide de la **version** d'une méthode redéfinie (à l'exécution) ;
- les **champs** et les membres `static` dépendent de la référence (ils ne sont pas polymorphes).

**Les casts :**
- **upcast** (vers un parent ou une interface) : implicite, toujours sûr ;
- **downcast** (vers un enfant) : explicite. Il compile si les types sont liés ; il lève une `ClassCastException` à l'exécution si l'objet n'est pas du bon type ;
- **entre classes sans lien** : erreur de compilation ;
- **vers une interface** : compile si la classe n'est pas `final` (une sous-classe pourrait l'implémenter) ; erreur si la classe est `final` et ne l'implémente pas.

**`instanceof` :**
- il suit les mêmes règles de compilation ;
- il vaut `false` pour `null` ;
- avec pattern, `x instanceof T t` évite le cast manuel.

</details>
