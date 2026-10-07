# Drill de rappel 9 — Kata mixte chronométré (tout le chapitre 5)

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, **sans carte**. C'est le test final de chaque cycle.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall09`** dans le paquet `ch5_methods.drills.r09_kata`, avec :
  - `private static int calls;` ;
  - `static final String TAG;`, affecté à `"kata"` dans un bloc `static` ;
  - `import static java.lang.Math.abs;`.

**Les notions de ce drill ont été apprises dans :** projets 1 à 7 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r09_kata` → **New** → **Java Class** → `Recall09`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall09`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall09`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `int sum(int... v)` incrémente `calls`. Appelle-la sans argument, avec `1, 2`, puis avec `new int[] {3, 4, 5}`. Affiche ensuite `calls`.
  → `D01 : 0 3 12 3`
- ☐ **D02.** Trois surcharges `kind` : `long`, `Integer`, `Object`. Appelle `kind(1)`, `kind(Integer.valueOf(1))`, `kind(1.0)` et `kind('c')`.
  → `D02 : long Integer Object long`
- ☐ **D03.** Deux méthodes :
  - `void reset(int[] a, int v)` fait `a = new int[] {v}` ;
  - `void set(int[] a, int v)` fait `a[0] = v`.
  
  Avec `a = {1}` : appelle `reset(a, 5)`, note `Arrays.toString(a)`, puis appelle `set(a, 5)`.
  → `D03 : [1] [5]`
- ☐ **D04.** Remets `calls` à 0. `int gcd(int a, int b)`, récursif, incrémente `calls` et rend `abs(a)` au cas de base. Affiche `gcd(-84, 36)`, puis `calls`.
  → `D04 : 12 3`
- ☐ **D05.** `int[] minMax(int first, int... rest)`. Appelle-la avec `4, 9, -2, 7`, puis avec `3` seul.
  → `D05 : [-2, 9] [3, 3]`
- ☐ **D06.** `Integer x = 128, y = 128;`. Affiche `x == y`, `x.equals(y)` et `x <= y`.
  → `D06 : false true true`
- ☐ **D07.** `Recall09 none = null;`. Affiche `TAG`, puis `none.TAG.length()`.
  → `D07 : kata 4`

## Sortie attendue complète

```
D01 : 0 3 12 3
D02 : long Integer Object long
D03 : [1] [5]
D04 : 12 3
D05 : [-2, 9] [3, 3]
D06 : false true true
D07 : kata 4
```

## Après le kata

Pour chaque ✗, refais le drill thématique :

| Défi | Drill |
|---|---|
| D01, D05 | r02 |
| D02 | r07 |
| D03 | r05 |
| D04 | r08 |
| D06 | r06 |
| D07 | r04 |
