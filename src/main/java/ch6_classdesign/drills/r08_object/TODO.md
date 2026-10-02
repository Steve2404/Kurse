# Drill de rappel 8 — Les méthodes d'`Object` : `toString`, `equals`, `hashCode`

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall08.java`, paquet `ch6_classdesign.drills.r08_object`. Les classes :

| Classe | Contenu |
|---|---|
| `Plain` | vide |
| `Point` | `private final int x, y` ; `equals(Object o)` : `this == o`, sinon `o instanceof Point p && …` ; `hashCode()` = `31 * x + y` ; `toString()` = `(x, y)` |
| `BadPoint` | `private final int x, y` ; **le piège** : `public boolean equals(BadPoint o)` (paramètre `BadPoint`, pas `Object`) |

## Défis

- ☐ **D01.** Deux `Plain`, a et b. Affiche :
  - si `a.toString()` contient `.Plain@` ;
  - `a.equals(b)` et `a.equals(a)` ;
  - `a.hashCode() == a.hashCode()`.
  → `D01 : true false true true`
- ☐ **D02.** `p1 = new Point(1, 2)` et `p2 = new Point(1, 2)`. Affiche :
  - p1 ;
  - `p1.equals(p2)` et `p2.equals(p1)` ;
  - `p1 == p2` ;
  - l'égalité des `hashCode`.
  → `D02 : (1, 2) true true false true`
- ☐ **D03.** `p1.equals(null)`, `p1.equals("(1, 2)")` et `p1.equals(new Point(2, 1))`.
  → `D03 : false false false`
- ☐ **D04.** `q1` et `q2` sont deux `BadPoint(1, 2)`, et `Object asObject = q2;`. Affiche `q1.equals(q2)`, puis `q1.equals(asObject)`.
  → `D04 : true false`
- ☐ **D05.** `Object o = new Point(3, 4);`. Affiche o, `o.equals(new Point(3, 4))`, puis le nom simple de sa classe.
  → `D05 : (3, 4) true Point`

## Expériences (hors sortie attendue)

1. Mets `@Override` sur `equals(BadPoint)` : quelle erreur ? C'est exactement le filet de sécurité qu'il faut.
2. Redéfinis `equals` dans `Point`, mais pas `hashCode` : deux points égaux ont-ils encore le même `hashCode` ? (Le chapitre 9 montrera ce que cela casse dans un `HashSet`.)
3. Écris `public String toString()` sans `public` : quelle erreur ?

## Sortie attendue complète

```
D01 : true false true true
D02 : (1, 2) true true false true
D03 : false false false
D04 : true false
D05 : (3, 4) true Point
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les signatures exactes à redéfinir :**
- `public String toString()` ;
- `public boolean equals(Object obj)` ;
- `public int hashCode()`.

**Le contrat de `equals` :**
- réflexif (`x.equals(x)`), symétrique, transitif, cohérent ;
- `x.equals(null)` vaut `false`.

**Le contrat de `hashCode` :**
- si `a.equals(b)`, alors `a.hashCode() == b.hashCode()` ;
- l'inverse n'est pas exigé (des collisions sont permises).

**Le piège :** `equals(MaClasse o)` est une **surcharge**. Une collection, ou tout appel avec une référence `Object`, utilisera encore `Object.equals` (`==`).

**Par défaut** (hérité d'`Object`) :
- `equals` vaut `==` ;
- `toString` donne `nom.complet.Classe@hexa` ;
- `hashCode` dépend de l'identité de l'objet.

</details>
