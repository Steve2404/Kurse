# Drill de rappel 1 — La syntaxe des lambdas

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall01.java`, paquet `ch8_lambdas.drills.r01_syntax`. Sous `Recall01`, trois interfaces :
  - `interface Op { int apply(int a, int b); }` ;
  - `interface Tester { boolean test(String s); }` (pas `Check` : ce nom est déjà pris par le correcteur du paquet) ;
  - `interface Maker { String make(); }`.
- Dans `Recall01`, deux méthodes `static` :
  - `int combine(int[] values, int start, Op op)`, qui accumule ;
  - `Tester startsWith(String prefix)`, qui **rend** `s -> s.startsWith(prefix)`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_syntax` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Quatre `Op`, appliqués à (3, 4) :
  - `add = (a, b) -> a + b` ;
  - `mul = (int a, int b) -> a * b` ;
  - `max = (var a, var b) -> { return a > b ? a : b; }` ;
  - `first = (final int a, final int b) -> a`.
  → `D01 : 7 12 4 3`
- ☐ **D02.** Trois `Tester` :
  - `empty = s -> s.isEmpty()`, testé sur `""` ;
  - `longer = (s) -> s.length() > 3`, testé sur `"abc"` ;
  - `notEmpty = (String s) -> { boolean r = !s.isEmpty(); return r; }`, testé sur `"x"`.
  → `D02 : true false true`
- ☐ **D03.** `hello = () -> "bonjour"` et `block = () -> { return "bloc"; }`.
  → `D03 : bonjour bloc`
- ☐ **D04.** Sur `{2, 5, 3}` :
  - `combine(values, 0, add)` ;
  - `combine(values, 1, (a, b) -> a * b)` ;
  - `combine(values, Integer.MIN_VALUE, max)`.
  → `D04 : 10 30 5`
- ☐ **D05.** `startsWith("ja")` testé sur `"java"` puis `"kotlin"`, puis `startsWith("")` testé sur `"x"`.
  → `D05 : true false true`
- ☐ **D06.** `Runnable r = () -> sb.append("run");` (un `StringBuilder sb`), exécuté deux fois. Affiche `sb`.
  → `D06 : runrun`

## Expériences (hors sortie attendue)

Écris chaque ligne, lis l'erreur, puis retire-la :
1. `Op o = a, b -> a + b;`
2. `Op o = (int a, b) -> a + b;`
3. `Op o = (var a, int b) -> a + b;`
4. `Tester c = s -> { s.isEmpty() };` et `Tester c = s -> return s.isEmpty();`
5. `Op o = (a, b) -> { a + b; };`
6. `var v = () -> "x";`

## Sortie attendue complète

```
D01 : 7 12 4 3
D02 : true false true
D03 : bonjour bloc
D04 : 10 30 5
D05 : true false true
D06 : runrun
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les paramètres :**
- `()` : obligatoires si aucun paramètre ;
- `x` : un seul paramètre sans type, parenthèses facultatives ;
- `(x, y)` : plusieurs, sans type ;
- `(int x, int y)` : tous typés ;
- `(var x, var y)` : tous en `var` ;
- **jamais de mélange** : pas de `(int x, y)` ni de `(var x, int y)` ;
- `final` n'est possible qu'avec un type ou `var`.

**Le corps :**
- **une expression** : pas d'accolades, pas de `return`, pas de `;` ;
- **un bloc** `{ … }` : des instructions complètes avec `;`, et `return` si une valeur est attendue.

**Le type d'une lambda :**
- il vient **du contexte** (le type cible) : une variable, un paramètre, un retour, un cast ;
- `var v = () -> …` est impossible, faute de type cible.

**Un corps `void` :** une expression-instruction (`sb.append(…)`, `i++`, un appel) convient aussi à une interface qui ne rend rien.

</details>
