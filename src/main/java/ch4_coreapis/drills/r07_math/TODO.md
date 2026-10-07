# Drill de rappel 7 — La classe `Math`

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`** dans le paquet `ch4_coreapis.drills.r07_math`.
- Tu dois déclarer `long fromDouble = Math.round(3.5);` et `int fromFloat = Math.round(3.5f);`. Le type du résultat **est** la question.

**Les notions de ce drill ont été apprises dans :** projet 4 (étape 2). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_math` → **New** → **Java Class** → `Recall07`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall07`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Quatre appels :
  - `max(3, 7)` ;
  - `min(-2, 5)` ;
  - `max(3, 7.5)` ;
  - `min(4L, 9)`.
  → `D01 : 7 -2 7.5 4`
- ☐ **D02.** `fromDouble`, `fromFloat`, puis `round(-3.5)`, `round(-3.6)` et `round(2.4)`.
  → `D02 : 4 4 -3 -4 2`
- ☐ **D03.** Quatre appels :
  - `ceil(3.2)` ;
  - `floor(3.8)` ;
  - `ceil(-3.2)` ;
  - `floor(-3.2)`.
  → `D03 : 4.0 3.0 -3.0 -4.0`
- ☐ **D04.** Cinq appels :
  - `pow(2, 8)` ;
  - `pow(9, 0.5)` ;
  - `sqrt(16)` ;
  - `abs(-4.5)` ;
  - `abs(-4)`.
  → `D04 : 256.0 3.0 4.0 4.5 4`
- ☐ **D05.** `Math.random()` est **aléatoire**, donc on teste son intervalle, pas sa valeur :
  - un `double random` : est-il dans `[0, 1[` ?
  - un dé `int dice` de 1 à 6, construit avec `Math.random()` : est-il dans `[1, 6]` ?
  → `D05 : true true`
- ☐ **D06.** Trois calculs :
  - `pow(10, 3)` converti en `int` ;
  - `12.3456` arrondi à 2 décimales avec `round` et `100.0` ;
  - `round(1234.5)` arrondi à la dizaine inférieure par une division entière (`/ 10 * 10`).
  → `D06 : 1000 12.35 1230`
- ☐ **D07.** Trois cas limites :
  - `abs(Integer.MIN_VALUE)` ;
  - `max(Double.NaN, 1)` ;
  - `sqrt(-1)`.
  → `D07 : -2147483648 NaN NaN`
- ☐ **D08.** Quatre calculs, sans parenthèses autour des opérations `/` et `%` :
  - `Math.floorDiv(-7, 2)`, puis `-7 / 2` ;
  - `Math.floorMod(-7, 3)`, puis `-7 % 3`.
  → `D08 : -4 -3 2 -1`

## Expériences (hors sortie attendue)

1. `int r = Math.round(3.5);` : que dit `javac` ? Pourquoi ?
2. `int p = Math.pow(2, 3);` : même question.
3. Lance plusieurs fois et affiche `random` : la valeur change, mais D05 reste `true true`. C'est pour cela qu'on ne compare jamais une valeur aléatoire.
4. `Math.round(0.49999999999999994)` : prédis, puis vérifie.

## Sortie attendue complète

```
D01 : 7 -2 7.5 4
D02 : 4 4 -3 -4 2
D03 : 4.0 3.0 -3.0 -4.0
D04 : 256.0 3.0 4.0 4.5 4
D05 : true true
D06 : 1000 12.35 1230
D07 : -2147483648 NaN NaN
D08 : -4 -3 2 -1
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les types de retour :**
- `round(double)` rend un `long` ; `round(float)` rend un `int` ;
- `ceil`, `floor`, `pow` et `sqrt` rendent toujours un `double` ;
- `max`, `min` et `abs` gardent le type promu des arguments.

**L'arrondi :**
- `round` va vers le haut à `.5` : `round(-3.5)` vaut `-3`, `round(2.5)` vaut `3` ;
- `ceil` va vers +∞, `floor` vers -∞.

**`Math.random()` :**
- un `double` dans `[0.0, 1.0[` ;
- un entier de a à b : `(int) (Math.random() * (b - a + 1)) + a`.

**Les cas limites :**
- `abs(Integer.MIN_VALUE)` déborde et reste négatif ;
- `sqrt` d'un négatif, ou `NaN` dans `max`, donne `NaN`.

**La division :**
- `/` et `%` tronquent vers 0 ;
- `floorDiv` et `floorMod` arrondissent vers -∞.

</details>
