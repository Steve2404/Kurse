# Drill de rappel 4 — Récursivité et retour arrière

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p06.

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée **`public final class Recall04`** dans le paquet `ch17_algorithms.drills.r04_backtracking`, avec les méthodes **`public static`** ci-dessous, **exactement** avec ces signatures.
- Tu n'écris pas de tests : les tests de référence (dans `solution/`) vérifient ton code. Pas de `Math.pow`.

**Les notions de ce drill ont été apprises dans :** projet 6 (étapes 1 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée `Recall04`, écris les méthodes dans l'ordre (une valeur bidon pour un défi pas fini), `// D04 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `long power(long base, int exp)` : la puissance rapide, O(log exp) (`exp >= 0`).
  → `d01 : 4 executions, 4 reussies`
- ☐ **D02.** `List<List<Integer>> permutations(List<Integer> items)` : toutes les permutations, en choisissant les éléments de gauche à droite.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `List<List<Integer>> subsets(List<Integer> items)` : tous les sous-ensembles, **sans** l'élément d'abord, **avec** ensuite.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `List<List<Integer>> combinationSum(int[] candidates, int target)` : les combinaisons croissantes (chaque candidat réutilisable) de somme `target`, dans l'ordre lexicographique ; sans modifier le tableau reçu.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `List<String> parentheses(int n)` : les chaînes de `n` paires bien formées, `(` avant `)`.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `int nQueens(int n)` : le nombre de placements de `n` reines (12 reines en moins de 3 secondes).
  → `d06 : 3 executions, 3 reussies`

## Sortie attendue complète

```
d01 : 4 executions, 4 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 3 executions, 3 reussies
```

<details><summary>Ouvrir la carte</summary>

**Le gabarit** (à savoir par cœur) :

```java
void explorer(etat) {
    if (complet) { resultats.add(new ArrayList<>(etat)); return; }   // une COPIE
    for (chaque choix possible) {
        choisir;  explorer(etat);  défaire;                            // défaire dans l'ordre inverse
    }
}
```

- **Puissance** : `half = power(b, e / 2)` **une seule fois** ; `e` pair : `half * half`, impair : `half * half * b`.
- **Permutations** : `boolean[] used` ; pour chaque `i` libre : marquer, ajouter, explorer, retirer, démarquer.
- **Sous-ensembles** : `explorer(index + 1)` sans l'élément, puis l'ajouter, `explorer(index + 1)`, le retirer.
- **Combinaisons** : copier et trier les candidats ; boucle depuis `start` ; appel récursif avec `i` (réutilisable) et `reste - c[i]` ; couper dès que `c[i] > reste`.
- **Parenthèses** : ouvrir si `ouvertes < n` ; fermer si `fermées < ouvertes`.
- **Reines** : une reine par ligne ; trois tableaux : colonnes, diagonales `ligne - colonne + n`, anti-diagonales `ligne + colonne` (taille `2n`).

</details>
