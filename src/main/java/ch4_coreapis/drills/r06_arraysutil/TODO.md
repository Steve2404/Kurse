# Drill de rappel 6 — La classe `Arrays`

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall06`** dans le paquet `ch4_coreapis.drills.r06_arraysutil`.
- Prédis chaque valeur **avant** de lancer : `compare` et `binarySearch` sont les pièges préférés de l'examen.

**Les notions de ce drill ont été apprises dans :** projet 3 (étape 4) et projet 4 (étapes 1 à 5). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r06_arraysutil` → **New** → **Java Class** → `Recall06`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall06`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall06`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Trie `{6, 9, 1, 8}`.
  → `D01 : [1, 6, 8, 9]`
- ☐ **D02.** Trie `{"10", "9", "Zebre", "apple", "100", "Apple"}`.
  → `D02 : [10, 100, 9, Apple, Zebre, apple]`
- ☐ **D03.** Quatre `binarySearch` dans `{2, 4, 6, 8}` : 6 (présent), puis 1, 5 et 9 (absents).
  → `D03 : 2 -1 -3 -5`
- ☐ **D04.** Quatre `Arrays.compare` :
  - `{1, 2}` contre `{1, 2}` ;
  - `{1, 2}` contre `{1, 2, 3}` ;
  - `{1, 3}` contre `{1, 2, 3}` ;
  - `{"a"}` contre `{"B"}`.
  → `D04 : 0 -1 1 31`
- ☐ **D05.** Trois `Arrays.mismatch` :
  - `{1, 2}` contre `{1, 2}` ;
  - `{1, 2}` contre `{1, 3}` ;
  - `{1, 2}` contre `{1, 2, 3}`.
  → `D05 : -1 1 2`
- ☐ **D06.** Deux tableaux `{1, 2}` distincts : `==`, `.equals` et `Arrays.equals`.
  → `D06 : false false true`
- ☐ **D07.** Quatre résultats :
  - un `int[4]` rempli de 7 par `Arrays.fill`, puis remis à 0 entre les indices 1 (inclus) et 3 (exclu) ;
  - `Arrays.copyOf` de `{2, 4, 6, 8}` sur 2 cases ;
  - le même `copyOf` sur 6 cases ;
  - `Arrays.copyOfRange(…, 1, 3)`.
  → `D07 : [7, 0, 0, 7] [2, 4] [2, 4, 6, 8, 0, 0] [4, 6]`
- ☐ **D08.** Lance `binarySearch(…, 4)` sur `{5, 1, 4}`, **non trié**. Selon le signe du résultat, affiche `trouve par hasard` ou `resultat imprevisible`, puis le tableau.
  → `D08 : trouve par hasard [5, 1, 4]`

## Expériences (hors sortie attendue)

1. Calcule à la main le `-(point d'insertion) - 1` de D03 pour 1, 5 et 9.
2. D08 avec `{5, 4, 1}` : que donne la recherche de 4 ? de 1 ? Retiens : sur un tableau non trié, le résultat n'est **pas défini**.
3. `Arrays.compare(new int[] {}, new int[] {1})`, et `Arrays.mismatch` sur deux tableaux vides ?
4. Pourquoi `"a"` contre `"B"` donne-t-il 31 ? Calcule `'a' - 'B'`.

## Sortie attendue complète

```
D01 : [1, 6, 8, 9]
D02 : [10, 100, 9, Apple, Zebre, apple]
D03 : 2 -1 -3 -5
D04 : 0 -1 1 31
D05 : -1 1 2
D06 : false false true
D07 : [7, 0, 0, 7] [2, 4] [2, 4, 6, 8, 0, 0] [4, 6]
D08 : trouve par hasard [5, 1, 4]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**L'ordre de tri des textes :**
- les chiffres, puis les MAJUSCULES, puis les minuscules (l'ordre Unicode) ;
- `"10"`, `"100"` et `"9"` se comparent caractère par caractère.

**`binarySearch` :**
- il exige un tableau trié ;
- trouvé : l'indice ;
- absent : `-(point d'insertion) - 1` ;
- non trié : le résultat n'est pas défini.

**`compare(a, b)` :**
- négatif si `a < b`, 0 si égaux, positif si `a > b` ;
- la première différence décide ;
- sinon, le plus court est le plus petit ;
- pour les textes, il rend la valeur de `compareTo`.

**`mismatch(a, b)` :**
- `-1` si les tableaux sont égaux ;
- sinon le premier indice différent, ou la longueur du plus court.

**`equals` :**
- `Arrays.equals` compare les contenus ;
- `.equals` d'un tableau compare les références.

**Les copies :**
- `copyOf` tronque ou complète avec les valeurs par défaut ;
- `copyOfRange(a, de, à)` : la fin est exclue ;
- `fill(a, de, à, v)` : la fin est exclue.

</details>
