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

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 et 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_inherit` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
6. Mets `private class Rock` ou `protected class Rock` au premier niveau du fichier : quelle erreur ? (Une classe de premier niveau est `public` ou package-private.)

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
