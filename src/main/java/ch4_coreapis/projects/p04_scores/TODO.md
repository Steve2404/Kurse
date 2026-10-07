# Projet 4 — Les statistiques d'une classe

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 4) :**
- **`Arrays`** : `copyOf`, `sort` (de nombres et de `String`), `binarySearch`, `equals`, `compare`, `mismatch`, `fill`, `toString` ;
- les tableaux **irréguliers** ;
- les valeurs par défaut ;
- **`Math`** : `min`, `max`, `round`, `ceil`, `floor`, `pow`, `sqrt`, `abs`, avec leurs **types de retour** ;
- des algorithmes classiques **écrits à la main** :
  - recherche dichotomique ;
  - fusion de tableaux triés ;
  - rotation ;
  - médiane ;
  - écart type.

**Ce qui est donné :** `Data.java` (deux groupes de notes, des noms, des bonus) et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch4_coreapis.projects.p04_scores`. La classe du `main` s'appelle **`Scores`**.

**Règle du crescendo :** chapitres 1 à 4. Pas de collection, pas de `Comparator` ni de lambda, pas de stream.

**Tes outils pour ce projet** (pas d'arguments) :

```
javac -d build/ch4-p04 -sourcepath src/main/java src/main/java/ch4_coreapis/projects/p04_scores/Scores.java
java "-Duser.language=fr" -cp build/ch4-p04 ch4_coreapis.projects.p04_scores.Scores
```

---

## Tableau de bord

### ☐ Étape 1 — Trier sans abîmer l'original

```
TRIEES : [47, 58, 64, 72, 78, 85, 85, 91], original intact : [72, 85, 58, 91, 64, 85, 47, 78]
```

**📖 La leçon : copier un tableau.** `int[] b = a;` ne copie **pas** le tableau : `b` est une 2e étiquette sur **le même** tableau (chapitre 1, projet 3, étape 5). Pour une vraie copie :

```java
int[] t = {5, 3, 9, 1};
int[] copie = Arrays.copyOf(t, t.length);   // un NOUVEAU tableau, avec les mêmes valeurs
Arrays.sort(copie);
// t vaut toujours [5, 3, 9, 1], copie vaut [1, 3, 5, 9]
```

`t.clone()` fait aussi une copie. `Arrays.copyOf(t, 6)` copie et **agrandit** le tableau : les cases en plus reçoivent la valeur par défaut. `Arrays.copyOfRange(t, 1, 3)` copie les cases de 1 à 3 **exclu** : `[3, 9]`.

**👉 À toi :**

- `Arrays.sort` trie **sur place**. Comment garder l'original ?
- **Question :** que ferait `int[] copy = notes; Arrays.sort(copy);` ?

### ☐ Étape 2 — Statistiques et types de `Math`

```
MIN 47, MAX 91, MOYENNE 72.5, MEDIANE 75.0, ECART-TYPE 14.22
ARRONDIS de la moyenne : round 73, ceil 73.0, floor 72.0, round(-2.5) -2, round(2.5f) 3, abs(-7) 7
```

**📖 La leçon : la classe `Math`.** Pas besoin d'`import` : elle est dans `java.lang`.

```java
Math.max(3, 8)      // 8
Math.min(3, 8)      // 3
Math.abs(-4)        // 4
Math.pow(3, 2)      // 9.0 : 3 au carré (toujours un double)
Math.sqrt(16)       // 4.0 : la racine carrée
Math.round(2.6)     // 3   : l'entier le plus proche
Math.ceil(2.1)      // 3.0 : arrondi vers le haut
Math.floor(2.9)     // 2.0 : arrondi vers le bas
```

**Arrondir à 2 décimales :** `Math.round(1.234 * 100) / 100.0` vaut `1.23`. On décale de deux chiffres, on arrondit à l'entier, puis on redivise par `100.0`. Avec `.0`, la division se fait à virgule (chapitre 2).

**👉 À toi :**

- **La moyenne :** attention à la division entière.
- **La médiane :** sur un nombre **pair** de valeurs, c'est la moyenne des deux valeurs du milieu.
- **L'écart type :** la racine de la moyenne des carrés des écarts. Utilise `Math.pow` et `Math.sqrt`.
- **Les 2 décimales** sans `%f` : `Math.round(x * 100) / 100.0`.
- **Les types de retour à connaître :**
  - `round(double)` rend un `long`, `round(float)` un `int` ;
  - `ceil` et `floor` rendent des `double` ;
  - `pow` rend toujours un `double`.
  - **Question :** pourquoi `Math.round(-2.5)` vaut-il −2 ?

### ☐ Étape 3 — Rechercher

```
RECHERCHE 78 : main 4 / Arrays 4 | 60 : -3 / -3 | 10 : -1 / -1
```

**📖 La leçon : la recherche dichotomique.** Pour chercher un nombre dans un tableau **trié**, on regarde la case du **milieu** :
- si c'est le nombre cherché, c'est fini ;
- s'il est trop grand, on recommence dans la moitié **gauche** ;
- sinon, dans la moitié **droite**.

À chaque tour, la zone de recherche est coupée en deux. `Arrays.binarySearch(t, valeur)` fait exactement cela : `Arrays.binarySearch(new int[]{1, 3, 5, 7}, 5)` rend `2`, la position du 5. **Déroule-la à la main** sur `{1, 3, 5, 7}` avant d'écrire la tienne : note à chaque tour les bornes et le milieu.

**👉 À toi :**

- **Écris ta propre recherche dichotomique**, avec la même convention que `Arrays.binarySearch` :
  - si la valeur est trouvée, rends son indice ;
  - sinon, rends `-(point d'insertion) - 1`.
- **Questions :**
  - pourquoi ce « −1 » ? (Pense au point d'insertion 0.)
  - que garantit la Javadoc si le tableau contient des **doublons** ?
  - et s'il n'est **pas trié** ?

### ☐ Étape 4 — Fusionner, faire tourner, comparer

```
FUSION A+B : [47, 50, 58, ...], mediane 72.0
ROTATION de 2 : [88, 95, 50, 66, 70]
COMPARE : equals true, == false, compare(B, plus long) -1, compare(A trie, B) -1, mismatch(B, rotation) 0, mismatch(B, copie) -1
```

**📖 Rappel :** `Arrays.copyOf` et `Arrays.equals` (étape 1 et projet 3). Pour la fusion et la rotation, dessine les tableaux en cases sur papier, avec les indices dessous, et suis tes variables d'indice tour par tour.

**👉 À toi :**

- **La fusion** de deux tableaux triés : avance dans celui dont la tête est la plus petite, puis vide le reste. Complexité ?
- **La rotation à droite** de k cases, avec **trois inversions** et sans copie. Vérifie-la à la main sur `[50, 66, 70, 88, 95]`.
- **`Arrays.compare`** compare élément par élément ; à égalité de préfixe, le plus **court** est le plus petit.
- **`Arrays.mismatch`** rend l'indice de la 1re différence, ou −1.
  - `copyOf(B, 6)` ajoute une case : avec quelle valeur ?

### ☐ Étape 5 — Noms, podium, tableaux irréguliers, valeurs par défaut

```
NOMS TRIES : [Bob, Hugo, Zoe, adam, eva, ines, lea, tom] (majuscules avant minuscules)
PODIUM : 1. Zoe 91 (+5, 3 bonus possibles) 2. Hugo/Bob 85 (+3, 2 bonus possibles) 3. Hugo/Bob 85 (+1, 1 bonus possibles)
DEFAUTS : [0, 0, 0] [null, null] [null, null], fill [7, 7, 7], Math.pow(2, 10) 1024.0, min(-0.0, 0.0) -0.0
```

**📖 La leçon : les tableaux irréguliers.** Les lignes d'une grille n'ont pas forcément la même taille :

```java
int[][] irr = {{1}, {2, 3}, {4, 5, 6}};
irr[2].length      // 3
irr[1][1]          // 3
```

**📖 La leçon : remplir et assembler.**

```java
int[] f = new int[3];
Arrays.fill(f, 9);                     // [9, 9, 9]
String.join("-", "a", "b", "c")        // "a-b-c"
String.join(", ", tableauDeTextes)     // les cases, séparées par ", "
```

**👉 À toi :**

- **Le tri des `String`** suit l'ordre des codes de caractères : chiffres, puis majuscules, puis minuscules.
- **Le podium :** les 3 meilleures notes, avec les ex æquo réunis par `/`.
  - Le bonus du rang r est `Data.BONUSES[r][0]`. Combien de cases a chaque ligne de ce tableau **irrégulier** ?
- **Les valeurs par défaut :** que contient `new double[2][]` ? Pourquoi `null` et pas `0.0` ?

---

## Checklist (vérifiée par `Check`)

`Arrays.copyOf`, `sort`, `binarySearch`, `equals`, `compare`, `mismatch`, `fill`, `toString` ; `Math.min`, `max`, `pow`, `sqrt`, `round`, `ceil`, `floor`, `abs` ; ta propre recherche dichotomique (`-(… + 1)`) ; une fusion dans un nouveau tableau ; un accès à un tableau irrégulier `[i][0]` ☐

---

## Sortie attendue complète

```
NOTES A : [72, 85, 58, 91, 64, 85, 47, 78] (8 copies)
TRIEES : [47, 58, 64, 72, 78, 85, 85, 91], original intact : [72, 85, 58, 91, 64, 85, 47, 78]
MIN 47, MAX 91, MOYENNE 72.5, MEDIANE 75.0, ECART-TYPE 14.22
ARRONDIS de la moyenne : round 73, ceil 73.0, floor 72.0, round(-2.5) -2, round(2.5f) 3, abs(-7) 7
RECHERCHE 78 : main 4 / Arrays 4 | 60 : -3 / -3 | 10 : -1 / -1
FUSION A+B : [47, 50, 58, 64, 66, 70, 72, 78, 85, 85, 88, 91, 95], mediane 72.0
ROTATION de 2 : [88, 95, 50, 66, 70]
COMPARE : equals true, == false, compare(B, plus long) -1, compare(A trie, B) -1, mismatch(B, rotation) 0, mismatch(B, copie) -1
NOMS TRIES : [Bob, Hugo, Zoe, adam, eva, ines, lea, tom] (majuscules avant minuscules)
PODIUM : 1. Zoe 91 (+5, 3 bonus possibles) 2. Hugo/Bob 85 (+3, 2 bonus possibles) 3. Hugo/Bob 85 (+1, 1 bonus possibles)
DEFAUTS : [0, 0, 0] [null, null] [null, null], fill [7, 7, 7], Math.pow(2, 10) 1024.0, min(-0.0, 0.0) -0.0
```
