# Drill de rappel 6 — Ordre d'initialisation et ramasse-miettes

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall06`** (avec le `main`) et une classe **`Egg`**.

**Les notions de ce drill ont été apprises dans :** projet 3 (étapes 1 et 2). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r06_init` → **New** → **Java Class** → `Recall06`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher. Par exemple, pour un défi `D01` qui attend `D01 : 8 16`, écris un `System.out.println("D01 : " + … + " " + …);`, où les `…` sont les valeurs que **Java** calcule.
4. **Lance `Recall06`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
