# Drill de rappel 6 — Les classes abstraites

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall06.java`, paquet `ch6_classdesign.drills.r06_abstract`. Les classes :

| Classe | Contenu |
|---|---|
| `Instrument` (abstraite) | `static int count` ; `private final String name` ; `protected Instrument(String name)` (incrémente `count`) ; `abstract String sound()` ; `getName()` ; `String play(int times)` rend `name + ":" + sound().repeat(times)` ; `static String describeAll()` rend `"orchestre de " + count` |
| `Strings extends Instrument` (abstraite) | `protected Strings(String name)` ; `abstract int strings()` ; elle **implémente** `sound()` = `"~".repeat(strings())` |
| `Guitar extends Strings` | nom `guitare`, 6 cordes |
| `Violin extends Strings` | nom `violon`, 4 cordes |
| `Drum extends Instrument` | nom `tambour`, `sound()` rend `boum` |
| `Marker` (abstraite) | **aucune** méthode abstraite ; `String tag()` rend `"marque par " + getClass().getSimpleName()` |
| `Concrete extends Marker` | vide |

## Défis

- ☐ **D01.** `new Guitar().play(1)` et `new Violin().play(2)`.
  → `D01 : guitare:~~~~~~ violon:~~~~~~~~`
- ☐ **D02.** `new Drum().play(3)`.
  → `D02 : tambour:boumboumboum`
- ☐ **D03.** `Instrument[] band = {new Guitar(), new Drum(), new Violin()}`. Affiche les noms, puis le total des cordes, avec `instanceof Strings s` et `s.strings()`.
  → `D03 : guitare tambour violon 10`
- ☐ **D04.** `Instrument.count`, puis `Instrument.describeAll()`.
  → `D04 : 6 orchestre de 6`
- ☐ **D05.** `Instrument v = new Violin();`. Affiche `v instanceof Strings`, `v instanceof Instrument` et le nom simple de sa classe.
  → `D05 : true true Violin`
- ☐ **D06.** `Marker m = new Concrete();`, puis `m.tag()`.
  → `D06 : marque par Concrete`

## Expériences (hors sortie attendue)

1. `new Instrument("x")` et `new Marker()` : quelles erreurs ?
2. `abstract String sound() { return ""; }` : quelle erreur ?
3. `private abstract`, `final abstract` et `static abstract` sur une méthode : lesquelles sont refusées, et pourquoi ?
4. Retire `strings()` de `Violin` : que doit-on alors faire de `Violin` ?
5. `abstract final class X {}` : quelle erreur ?

## Sortie attendue complète

```
D01 : guitare:~~~~~~ violon:~~~~~~~~
D02 : tambour:boumboumboum
D03 : guitare tambour violon 10
D04 : 6 orchestre de 6
D05 : true true Violin
D06 : marque par Concrete
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Une classe abstraite :**
- elle ne s'instancie pas ;
- elle peut avoir des constructeurs, des champs, des méthodes concrètes, des méthodes `static`… et **zéro ou plusieurs** méthodes abstraites ;
- elle ne peut pas être `final`.

**Une méthode abstraite :**
- elle n'a pas de corps (`;`) et n'existe que dans une classe abstraite ;
- elle ne peut pas être `private`, `final` ni `static` (elles ne se redéfinissent pas).

**La première classe concrète** de la hiérarchie doit implémenter **toutes** les méthodes abstraites héritées qui ne le sont pas encore. Une sous-classe abstraite peut en implémenter une partie et en ajouter de nouvelles.

**Le constructeur d'une classe abstraite** s'exécute par `super(...)` depuis la sous-classe concrète.

</details>
