# Drill de rappel 1 — Déclarer des méthodes

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall01`** dans le paquet `ch5_methods.drills.r01_declare`. Toutes les méthodes sont `static`.
- Déclare les constantes **exactement** ainsi : `final static public String NAME = "r01";`, `public static final int LIMIT = 3;` et `static int $count;`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 et 2). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_declare` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Quatre méthodes :
  - `_twice(int)` rend le double ;
  - `long widen(int x)` rend x (sans cast) ; affiche `widen(Integer.MAX_VALUE) + 1` ;
  - `char letter()` rend `65` (sans cast) ;
  - `int truncate(double)` rend `(int) d`, appelée avec −3.9.
  → `D01 : 42 2147483648 A -3`
- ☐ **D02.** `void log(String s)` : si `s` est vide, `return;`. Sinon, `$count++`. Appelle-la avec `""`, `"a"` puis `"b"`, puis affiche `$count`.
  → `D02 : 2`
- ☐ **D03.** `int[] pair(int a, int b)`. Affiche `Arrays.toString(pair(1, 2))`, puis `pair(3, 4)[1]`.
  → `D03 : [1, 2] 4`
- ☐ **D04.** `String sign(int n)` : `+`, `-` ou `0`, avec un `if / else if` puis un `return` final.
  → `D04 : + - 0`
- ☐ **D05.** `NAME`, `LIMIT`, puis `value2()`, qui déclare `var v = 40;`, fait `v += 2` et le rend.
  → `D05 : r01 3 42`
- ☐ **D06.** `int start(int n)` : une locale `final int s;` affectée dans un `if` (n si positif) **ou** dans le `else` (0). Teste avec 5 et −1.
  → `D06 : 5 0`
- ☐ **D07.** `int firstNegative(int[] values)` rend l'indice du premier négatif (`return` dans la boucle), ou −1. Teste avec `{4, 0, -2, -5}` et un tableau vide.
  → `D07 : 2 -1`
- ☐ **D08.** Deux méthodes `area` : un côté (le carré) ou deux côtés (le rectangle).
  → `D08 : 16 10`

## Expériences (hors sortie attendue)

1. `static public void int f()`, `void static f()` et `public final static void f()` : lesquelles compilent ? (Le type de retour vient **juste avant** le nom.)
2. Une méthode `int g(int n) { if (n > 0) return 1; }` : quelle erreur ?
3. `static int _() { return 0; }`, `static int 2x() { … }` et `static int $() { … }` : lesquelles compilent ?
4. `static int h() { return 1L; }` et `static byte k() { return 200; }` : pourquoi refusées ?
5. `var` comme paramètre ou comme type de retour : que dit `javac` ?
6. Dans D06, retire le `else` : quelle erreur ?
7. **Effectively final.** Ajoute `final` devant **chaque** paramètre et chaque variable locale de `value2`, `firstNegative` et `_twice`. Celles qui compilent encore étaient *effectively final* : jamais réaffectées. Lesquelles cassent, et pourquoi (`v += 2`, `i++`) ?

## Sortie attendue complète

```
D01 : 42 2147483648 A -3
D02 : 2
D03 : [1, 2] 4
D04 : + - 0
D05 : r01 3 42
D06 : 5 0
D07 : 2 -1
D08 : 16 10
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**L'ordre d'une déclaration :**
`[accès] [static] [final] [autres] TypeDeRetour nom(paramètres) [throws …] { corps }`
- les modificateurs d'accès et optionnels s'écrivent dans n'importe quel ordre, mais **avant** le type de retour ;
- la **signature** = le nom + la liste des types des paramètres (ni le retour, ni les noms des paramètres).

**Le retour :**
- `void` : pas de valeur, `return;` permis pour sortir tôt ;
- sinon, chaque chemin doit finir par un `return valeur` (ou une exception) ;
- la valeur est convertie comme dans une affectation : élargissement implicite ; une constante qui tient dans le type est acceptée (`char c() { return 65; }`).

**Les noms :**
- lettres, chiffres, `_` et `$` ; ils ne commencent pas par un chiffre ;
- `_` seul est interdit ; un mot réservé est interdit.

**Les variables locales :**
- `final` : une seule affectation, éventuellement plus tard, mais sur chaque chemin ;
- **effectively final** : une variable jamais réaffectée après son initialisation, même sans le mot `final`. Ajouter `final` ne changerait rien. Les lambdas (chapitre 8) et les classes locales (chapitre 7) l'exigeront ;
- `var` : seulement pour une variable locale initialisée sur sa ligne (pas de champ, de paramètre ni de retour).

**Ce que le chapitre cite, mais qui se pratique plus tard :**
- les modificateurs optionnels `abstract` et `final` sur une méthode (chapitre 6), `default` (interfaces, chapitre 7), `synchronized` (chapitre 13), `native` et `strictfp` (à reconnaître seulement) ;
- la liste d'exceptions `throws …`, après les paramètres (chapitre 11) ;
- les champs `volatile` (chapitre 13) et `transient` (chapitre 14).

</details>
