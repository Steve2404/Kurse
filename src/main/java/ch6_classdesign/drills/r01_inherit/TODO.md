# Drill de rappel 1 — L'héritage

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Dans le paquet `ch6_classdesign.drills.r01_inherit`, écris le fichier `Recall01.java`. Il contient `public class Recall01` (le `main`) **et**, sous elle, les classes non publiques `Animal`, `Dog`, `Puppy`, `Rock`. Plusieurs classes de premier niveau dans un fichier, c'est permis : une seule est publique.
- Les classes :

| Classe | Contenu |
|---|---|
| `Animal` | `static int count` ; `public String name = "animal"` ; `protected int legs = 4` ; `private final int id` ; un constructeur qui fait `id = ++count` ; `String sound()` rend `...` ; `final int id()` |
| `Dog extends Animal` | son constructeur met `name = "chien"` ; `sound()` rend `ouaf` ; `String describe()` rend `chien` |
| `Puppy extends Dog` | son constructeur met `name = "Rex"` ; `sound()` rend `kai` ; `describe()` rend `"chiot<" + super.describe() + ">"` ; `toString()` rend `"Puppy " + name` |
| `Rock` | `final class Rock {}`, vide |

## Défis

- ☐ **D01.** `Puppy p = new Puppy();`. Affiche `p.name`, `p.legs`, `p.sound()` et `p.describe()`.
  → `D01 : Rex 4 kai chiot<chien>`
- ☐ **D02.** `Object o = p;` et `Animal a = p;`. Affiche :
  - `o` ;
  - `a.sound()` ;
  - `p instanceof Dog`, `o instanceof Animal` et `a instanceof Puppy`.
  → `D02 : Puppy Rex kai true true true`
- ☐ **D03.** Les parents directs : `Puppy.class.getSuperclass().getSimpleName()`, puis la même chose pour `Dog` et pour `Animal`. Termine par `Object.class.getSuperclass()`.
  → `D03 : Dog Animal Object null`
- ☐ **D04.** `p.id()`, puis `new Dog().id()`, puis `Animal.count`.
  → `D04 : 1 2 2`
- ☐ **D05.** Deux `Rock`, `r1` et `r2`. Affiche `r1.equals(r2)`, `r1.equals(r1)`, puis si `r1.toString()` contient `.Rock@`.
  → `D05 : false true true`

## Expériences (hors sortie attendue)

1. `class Stone extends Rock {}` : quelle erreur ?
2. `class Hybrid extends Dog, Animal {}` : que dit `javac` ? (L'héritage est simple.)
3. Dans `Puppy`, redéfinis `id()` : quelle erreur ?
4. Dans `Puppy`, lis `id` directement (le champ `private`) : quelle erreur ?
5. Pourquoi `Animal.count` vaut-il 2 et pas 3 à D04 ?

## Sortie attendue complète

```
D01 : Rex 4 kai chiot<chien>
D02 : Puppy Rex kai true true true
D03 : Dog Animal Object null
D04 : 1 2 2
D05 : false true true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**L'héritage simple :**
- une classe a **un seul** parent direct (`extends`) ;
- sans `extends`, son parent est `Object` ;
- `Object` est la seule classe sans parent.

**Ce qui s'hérite :**
- les membres `public` et `protected` ;
- les membres package-private, si on est dans le même paquet ;
- **jamais** les membres `private`, ni les constructeurs.

**Les modificateurs de classe :**
- `final class` : aucune sous-classe ;
- `abstract class` : pas de `new`.

**L'objet réel décide :** la variable peut être de type parent (`Animal a = p`), mais l'objet reste un `Puppy` : `a.sound()` donne `kai`.

**`Object` fournit, entre autres :**
- `toString()` : `nom.complet.Classe@hash` par défaut ;
- `equals()` : `==` par défaut ;
- `hashCode()` et `getClass()`.

</details>
