# Drill de rappel 7 — Les variables dans les lambdas

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`** dans le paquet `ch8_lambdas.drills.r07_variables`, avec :
  - `private int instanceCounter;` ;
  - `private static int staticCounter;` ;
  - `private final String name = "drill";` ;
  - `IntSupplier counters()`, qui rend `() -> ++instanceCounter + 10 * ++staticCounter` ;
  - `String whoIsThis()` :
    - une lambda `() -> this.name` ;
    - une classe anonyme `new Supplier<>() { private final String name = "anonyme"; … get() rend this.name }` ;
    - elle rend les deux résultats, séparés par un espace.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 4) et projet 3 (étape 2). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_variables` → **New** → **Java Class** → `Recall07`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall07`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `int base = 5;` et `IntSupplier plusBase = () -> base + 1;`.
  → `D01 : 6`
- ☐ **D02.** `Recall07 r = new Recall07(); IntSupplier c = r.counters();`. Appelle `c.getAsInt()` une fois sans l'afficher. Puis affiche `c.getAsInt()`, `r.instanceCounter` et `staticCounter`.
  → `D02 : 22 2 2`
- ☐ **D03.** `int[] box = {0};` et `Runnable inc = () -> box[0]++;`, exécuté 3 fois. Affiche `box[0]`.
  → `D03 : 3`
- ☐ **D04.** `r.whoIsThis()`.
  → `D04 : drill anonyme`
- ☐ **D05.** `String label = "x";` et `Supplier<String> twice = () -> label + label;`.
  → `D05 : xx`
- ☐ **D06.** Dans cet ordre :
  1. `StringBuilder sb = new StringBuilder("a");` ;
  2. `Supplier<String> view = sb::toString;` ;
  3. `sb.append("b");`.
  
  Puis affiche `view.get()`.
  → `D06 : ab`

## Expériences (hors sortie attendue)

1. Après D01, écris `base = 6;` : quelle erreur, et sur quelle ligne ?
2. Dans D03, remplace le tableau par `int count = 0;` et `() -> count++` : quelle erreur ?
3. `String label = "y";` dans le corps de la lambda de D05 : quelle erreur ? (Un paramètre ou une variable locale d'une lambda ne peut pas masquer une variable locale englobante.)
4. `IntSupplier bad = () -> { int base = 1; return base; };` après D01 : quelle erreur ?
5. Pourquoi D06 affiche-t-il `ab` et non `a` ?

## Sortie attendue complète

```
D01 : 6
D02 : 22 2 2
D03 : 3
D04 : drill anonyme
D05 : xx
D06 : ab
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Ce que lit la lambda | Lire | Modifier |
|---|---|---|
| ses propres paramètres et variables locales | oui | oui |
| une variable locale ou un paramètre de la méthode englobante | oui, si **effectively final** | **non** |
| un champ d'instance (via `this`) | oui | oui |
| un champ `static` | oui | oui |
| le **contenu** d'un objet capturé (tableau, `StringBuilder`) | oui | oui : c'est la **référence** qui est figée |

**La portée :**
- une lambda ne crée **pas** de nouvelle portée pour les noms ;
- ses paramètres et variables ne peuvent pas reprendre le nom d'une variable locale englobante ;
- **`this`** désigne l'objet englobant. Dans une classe anonyme, `this` désigne l'anonyme.

**Effectively final :** jamais réaffectée **nulle part** dans la méthode, ni avant, ni après la lambda.

</details>
