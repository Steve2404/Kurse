# Drill de rappel 6 — Ordre d'initialisation et ramasse-miettes

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall06`** (avec le `main`) et une classe **`Egg`**.

## Défis

**La classe `Egg` :**
- un champ `static int made` ;
- un champ `String trace` initialisé à `"champ"` ;
- **deux blocs d'initialisation**, chacun complétant `trace` ;
- deux **constructeurs** : sans argument, et avec une taille ;
- un champ `copyOfLate` initialisé par une méthode qui lit un champ `late` (8), déclaré **après** lui ;
- un champ `size` (1).

**Les défis :**
- ☐ **D01.** La trace d'un œuf créé **sans argument**.
  → `D01 : champ > bloc1 > bloc2 > constructeur()`
- ☐ **D02.** La trace d'un œuf créé avec la taille 3.
  → `D02 : champ > bloc1 > bloc2 > constructeur(3)`
- ☐ **D03.** Pour un nouvel œuf : `late`, puis `copyOfLate`.
  → `D03 : 8 0`
- ☐ **D04.** Le nombre d'œufs créés jusqu'ici.
  → `D04 : 3`
- ☐ **D05.** Fais `Egg f = e; e = null;`, puis affiche la taille via `f`.
  → `D05 : 1`

## Expériences et questions (hors sortie attendue)

1. Dans un bloc d'initialisation, lis `late` **sans** `this.`, avant sa déclaration. Lis l'erreur de `javac`.
2. **D01 et D02 :** combien d'œufs sont **éligibles** au ramasse-miettes dès la fin de leur ligne ? Pourquoi ?
3. **Après `e = null;` :** l'œuf de D03 est-il éligible ? Et après `f = null;` ?
4. Un objet éligible est-il **forcément** détruit ? Que garantit `System.gc()` ?

## Sortie attendue complète

```
D01 : champ > bloc1 > bloc2 > constructeur()
D02 : champ > bloc1 > bloc2 > constructeur(3)
D03 : 8 0
D04 : 3
D05 : 1
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**L'ordre de création d'un objet :**
1. tous les champs reçoivent leur **valeur par défaut** (0, `false`, `null`) ;
2. les initialiseurs de champs et les blocs `{ }` s'exécutent **dans l'ordre du texte** ;
3. le **corps du constructeur** s'exécute.

**La lecture anticipée d'un champ :**
- lire un champ **avant** sa déclaration, par son nom simple, dans un initialiseur ou un bloc → erreur `illegal forward reference` ;
- via `this.champ` ou via une méthode → permis, et on lit la **valeur par défaut**.

**Les champs `static` :**
- un par **classe**, partagé par tous les objets ;
- valeur par défaut 0 s'il n'est pas initialisé.

**L'éligibilité au ramasse-miettes :**
- un objet devient **éligible** quand **plus aucune référence** accessible ne le désigne (réaffectation, `null`, fin de portée) ;
- `System.gc()` n'est qu'une **suggestion**, sans aucune garantie ;
- `finalize()` est déprécié : il ne faut pas s'en servir.

</details>
