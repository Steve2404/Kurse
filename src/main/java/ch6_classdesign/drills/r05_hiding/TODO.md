# Drill de rappel 5 — Masquer (hide) ou redéfinir (override)

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall05.java`, paquet `ch6_classdesign.drills.r05_hiding`. Les classes :

| Classe | Contenu |
|---|---|
| `P` | `String name = "P"` ; `static int count = 1` ; `static String s()` rend `P.s` ; `String i()` rend `P.i` ; `String readName()` rend `name` ; `private String secret()` rend `secret de P` ; `String reveal()` rend `secret()` |
| `C extends P` | `String name = "C"` ; `static int count = 2` ; `static String s()` rend `C.s` ; `i()` redéfini rend `C.i` ; sa propre `private String secret()` rend `secret de C` ; `String ownSecret()` rend `secret()` ; `String both()` rend `name + "/" + super.name + "/" + this.name` |

## Défis

`C c = new C();` et `P asP = c;`.

- ☐ **D01.** `asP.name`, `c.name`, `asP.readName()` et `c.both()`.
  → `D01 : P C P C/P/C`
- ☐ **D02.** `asP.s()`, `c.s()`, `P.s()` et `C.s()`. Les avertissements de `javac` sont voulus.
  → `D02 : P.s C.s P.s C.s`
- ☐ **D03.** `asP.i()` et `c.i()`.
  → `D03 : C.i C.i`
- ☐ **D04.** `c.reveal()` et `c.ownSecret()`.
  → `D04 : secret de P secret de C`
- ☐ **D05.** `asP.count`, `c.count`, `P.count` et `C.count`.
  → `D05 : 1 2 1 2`

## Expériences (hors sortie attendue)

1. Rends `s()` non `static` dans `C` seulement : quelle erreur ? Et l'inverse (`i()` `static` dans `C`) ?
2. Mets `@Override` sur `s()` dans `C` : quelle erreur ?
3. Rends `secret()` non privée dans les deux classes : que devient `c.reveal()` ?

## Sortie attendue complète

```
D01 : P C P C/P/C
D02 : P.s C.s P.s C.s
D03 : C.i C.i
D04 : secret de P secret de C
D05 : 1 2 1 2
```

## Carte mémoire (à lire **après** le drill)

| Membre | Mécanisme | Choisi d'après | Moment |
|---|---|---|---|
| méthode d'instance | **redéfinition** | le type de l'**objet** | exécution |
| méthode `static` | **masquage** | le type de la **référence** | compilation |
| champ (`static` ou non) | **masquage** | le type de la **référence** | compilation |
| méthode `private` | ni l'un ni l'autre : une méthode **distincte** | la classe où l'appel est écrit | compilation |

**Les règles de cohérence :**
- `static` dans le parent, `static` dans l'enfant : masquage ;
- d'instance dans le parent, d'instance dans l'enfant : redéfinition ;
- un mélange des deux : erreur de compilation.

**Les champs :**
- un champ masqué existe **deux fois** dans l'objet ;
- `super.name` désigne celui du parent.

</details>
