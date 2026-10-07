# Drill de rappel 7 — Kata mixte chronométré (tout le chapitre 3)

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min, **sans carte**. C'est le test final de chaque cycle.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`**.
- `Check` lance ton `main` avec `30 un 7 deux 12`. Le 1er argument est la limite.

**Les notions de ce drill ont été apprises dans :** projets 1 à 5 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_kata` → **New** → **Java Class** → `Recall07`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall07` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
   Ce drill reçoit des **arguments** : lance `Recall07` une fois, puis Run → Edit Configurations… → **Recall07** → Program arguments : `30 un 7 deux 12`.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** La somme des entiers de 1 à la limite qui sont multiples de 3 **ou** de 5.
  → `D01 : 225`
- ☐ **D02.** Le PGCD de 48 et 180 (Euclide, avec `while`).
  → `D02 : 12`
- ☐ **D03.** Pour chaque argument (for-each) :
  1. un `switch` expression le transforme en `Object` : `un` et `deux` restent du texte, le reste devient un nombre ;
  2. une méthode classe ensuite cet `Object` : `pair` (pattern + `&&`), `impair` ou `texte`.
  → `D03 : pair texte impair texte pair`
- ☐ **D04.** Le nombre de nombres premiers jusqu'à la limite (étiquette et `continue outer`).
  → `D04 : 10`
- ☐ **D05.** Le nombre d'étapes de Collatz depuis 27.
  → `D05 : 111`
- ☐ **D06.** Les chiffres romains de 1 à 4 : un `switch` expression dans une boucle, avec un `default` en bloc et `yield`.
  → `D06 : I II III IV`
- ☐ **D07.** Le nombre de chiffres de la limite (`do/while`).
  → `D07 : 2`
- ☐ **D08.** Un escalier de 3 marches : 1, 2 puis 3 dièses, chaque marche suivie de `|` (boucles imbriquées).
  → `D08 : #|##|###|`

## Sortie attendue complète

```
D01 : 225
D02 : 12
D03 : pair texte impair texte pair
D04 : 10
D05 : 111
D06 : I II III IV
D07 : 2
D08 : #|##|###|
```

## Après le kata

- **Pour chaque défi raté ou trop lent :** refais le drill de son thème le lendemain.
  - r01 : `if` et patterns ;
  - r02 et r03 : `switch` ;
  - r04 et r05 : boucles ;
  - r06 : sauts.
- **Note ton temps** dans `drills/README.md`.
