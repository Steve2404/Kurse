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

**Les notions de ce drill ont été apprises dans :** projet 4 (étape 1) et projet 7 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r08_object` → **New** → **Java Class** → `Recall08`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall08`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall08`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
