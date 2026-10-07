# Drill de rappel 12 — Les algorithmes classiques, de mémoire

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 25 min, puis 15 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire : **écris chaque algorithme sans le relire** dans `p07` ou `p08`.
- Crée la classe **`Recall12`** dans le paquet `ch4_coreapis.drills.r12_algos`.
- `Arrays.sort` est **interdit** dans ce drill : tu tries à la main. `Arrays.toString`, `deepToString` et `binarySearch` (pour vérifier) sont permis.
- Le but est la **fluidité** : l'examen et les entretiens supposent que ces boucles sortent sans réfléchir.

**Les notions de ce drill ont été apprises dans :** projets 7 et 8 (les laboratoires d'algorithmes). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r12_algos` → **New** → **Java Class** → `Recall12`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall12` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall12`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Inverse `{1, 2, 3, 4, 5, 6}` **en place**, avec deux indices qui se rapprochent dans un seul `for`.
  → `D01 : [6, 5, 4, 3, 2, 1]`
- ☐ **D02.** Une méthode `search(int[], int)` : la recherche dichotomique avec la convention `-(point d'insertion) - 1`. Le milieu se calcule avec `>>> 1`. Sur `{3, 8, 15, 17, 23, 29, 42}`, cherche :
  - 23 ;
  - 10 ;
  - 50.
  
  Puis vérifie que `search(…, 10)` est égal à `Arrays.binarySearch(…, 10)`.
  → `D02 : 4 -3 -8 true`
- ☐ **D03.** Le tri par insertion de `{5, 2, 9, 1, 5, 6}`.
  → `D03 : [1, 2, 5, 5, 6, 9]`
- ☐ **D04.** Kadane sur `{-2, 1, -3, 4, -1, 2, 1, -5, 4}`, en deux lignes de `Math.max` dans la boucle.
  → `D04 : 6`
- ☐ **D05.** Le nombre de nombres premiers ≤ 100, avec un crible `new boolean[101]`.
  → `D05 : 25`
- ☐ **D06.** Deux résultats :
  - `"A man, a plan, a canal: Panama"` est-il un palindrome, en ne gardant que les lettres en minuscules ?
  - les mots de `"le chat noir"` dans l'ordre inverse.
  → `D06 : true noir chat le`
- ☐ **D07.** La lettre la plus fréquente de `"mississippi"`, avec un `new int[26]`, suivie de son nombre. À égalité, garde la première dans l'alphabet.
  → `D07 : i4`
- ☐ **D08.** Deux calculs :
  - le carré de la matrice `{{1, 2}, {3, 4}}`, avec trois boucles imbriquées (`square[r][c] += …`) ;
  - la rotation à droite de 2 crans de `{1, 2, 3, 4, 5}`, dans un nouveau tableau, avec un indice modulo la longueur.
  → `D08 : [[7, 10], [15, 22]] [4, 5, 1, 2, 3]`

## Expériences (hors sortie attendue)

1. Dans D02, remplace `low <= high` par `low < high` : quelle recherche échoue ?
2. Dans D03, remplace `b[j] > key` par `b[j] >= key` : le résultat change-t-il ? Et la stabilité ?
3. Dans D04, que rend ton Kadane si **tous** les nombres sont négatifs ? Est-ce correct ?
4. Dans D08, réécris la rotation **en place** avec trois inversions (comme p04).

## Sortie attendue complète

```
D01 : [6, 5, 4, 3, 2, 1]
D02 : 4 -3 -8 true
D03 : [1, 2, 5, 5, 6, 9]
D04 : 6
D05 : 25
D06 : true noir chat le
D07 : i4
D08 : [[7, 10], [15, 22]] [4, 5, 1, 2, 3]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Algorithme | Idée en une ligne | Complexité |
|---|---|---|
| inversion en place | `i` part du début, `j` de la fin ; échange ; tant que `i < j` | O(n) |
| dichotomie | `low <= high` ; `mid = (low + high) >>> 1` ; absent : `-(low + 1)` | O(log n) |
| tri par insertion | décale vers la droite tant que `> key`, puis pose `key` | O(n²), O(n) si déjà trié |
| Kadane | `cur = max(x, cur + x)` ; `best = max(best, cur)` | O(n) |
| crible | pour chaque premier i, barre i·i, i·i + i… | O(n log log n) |
| palindrome | nettoyer, puis comparer à l'inverse (ou deux indices) | O(n) |
| fréquences | `int[26]`, `freq[c - 'a']++` | O(n) |
| produit de matrices | `C[r][c] += A[r][k] * B[k][c]` | O(n³) |
| rotation de k | `res[(i + k) % n] = a[i]` | O(n) |

**Les pièges :**
- un indice hors limites, à cause de `<=` au lieu de `<` ;
- la division entière ;
- le débordement de `low + high` ;
- trier l'original alors qu'il fallait une copie.

</details>
