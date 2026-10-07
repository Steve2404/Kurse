# Drill de rappel 5 — `for` et for-each

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall05`**.
- `Check` lance ton `main` avec `4 8 15 16`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 1), projet 3 (étape 2) et projet 4 (étape 5). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r05_for` → **New** → **Java Class** → `Recall05`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall05` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall05`** avec la flèche verte, pour voir tes lignes.
   Ce drill reçoit des **arguments** : lance `Recall05` une fois, puis Run → Edit Configurations… → **Recall05** → Program arguments : `4 8 15 16`.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Les entiers de 0 à 4 collés. Puis, de 10 en descendant de 3 tant que > 0, chacun suivi d'une espace.
  → `D01 : 01234 | 10 7 4 1`
- ☐ **D02.** Une **seule** boucle `for` avec **deux** variables : `i` part de 0, `j` de 6, `i++` et `j--` dans la mise à jour. Elle tourne tant que `i < j` et concatène `i` et `j` suivis d'une espace.
  → `D02 : 06 15 24`
- ☐ **D03.** Un `for` **sans initialisation ni mise à jour** (`for (; k < 3; )`), avec `k` déclaré avant et incrémenté dans le corps.
  → `D03 : 3`
- ☐ **D04.** Un **for-each** sur `args` : le nombre d'arguments, puis chacun entre crochets.
  → `D04 : 4 [4][8][15][16]`
- ☐ **D05.** La somme des arguments, avec un for-each.
  → `D05 : 43`
- ☐ **D06.** Les arguments **à l'envers**, avec un `for` à indice décroissant.
  → `D06 : 16 15 8 4`
- ☐ **D07.** 20! dans un `long`, avec `*=`.
  → `D07 : 2432902008176640000`
- ☐ **D08.** Deux `for` imbriqués : `i` va de 0 à 2, et `j` va **de `i`** à 2. Compte les tours.
  → `D08 : 6`
- ☐ **D09.** Un `for` dont le compteur est déclaré avec **`var`** (de 0 à 2, concaténé), puis un **for-each avec `var`** sur `args` (chaque argument précédé d'une virgule). Quels types `var` déduit-il ?
  → `D09 : 012,4,8,15,16`

## Expériences (hors sortie attendue)

1. `for (int i = 0, long j = 0; …)` : quelle erreur ?
2. Utilise `i` **après** la boucle `for (int i = 0; …)`. Que dit `javac` ?
3. Dans un for-each sur `args`, modifie la variable de boucle (`arg = "x";`). Est-ce que `args` change ?
4. `for ( ; ; ) { }` suivi d'une instruction : pourquoi l'instruction est-elle inaccessible ?

## Sortie attendue complète

```
D01 : 01234 | 10 7 4 1
D02 : 06 15 24
D03 : 3
D04 : 4 [4][8][15][16]
D05 : 43
D06 : 16 15 8 4
D07 : 2432902008176640000
D08 : 6
D09 : 012,4,8,15,16
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**`for (init ; condition ; mise à jour)` :**
- les trois parties sont **facultatives** ; `for ( ; ; )` est une boucle infinie ;
- **init :** plusieurs variables, mais du **même** type, en une seule déclaration (`int i = 0, j = 6`) ;
- **mise à jour :** plusieurs expressions séparées par des virgules (`i++, j--`) ;
- **la portée :** une variable déclarée dans `init` n'existe que dans la boucle ;
- **`var` est permis** : `for (var i = 0; …)` déduit `int`, et `for (var s : args)` déduit `String`. En revanche, `var i = 0, j = 1` (plusieurs variables) reste interdit.

**Le for-each `for (Type x : source)` :**
- la source est un **tableau** ou un `Iterable` (une collection, au chapitre 9) ;
- `x` est une **copie** de l'élément : la réaffecter ne change pas la source ;
- il ne donne pas l'indice ; il faut un `for` classique pour l'indice, le sens inverse ou un saut de 2.

</details>
