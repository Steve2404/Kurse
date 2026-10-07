# Drill de rappel 2 — Le chemin dans `try` / `catch` / `finally`

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall02`** dans le paquet `ch11_exceptions.drills.r02_flow`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 3) et projet 4 (étape 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r02_flow` → **New** → **Java Class** → `Recall02`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall02`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall02`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `static String run(boolean fail, List<String> trace)` :
  - le `try` ajoute `A`, puis lève une `IllegalStateException` si `fail`, sinon ajoute `B` et rend `"try"` ;
  - le `catch` ajoute `C` et rend `"catch"` ;
  - le `finally` ajoute `D`.
  
  Affiche `run(false, …)` et sa liste, puis ` | `, puis `run(true, …)` et sa liste.
  → `D01 : try [A, B, D] | catch [A, C, D]`
- ☐ **D02.** `static String swallow()` : `try { throw new IllegalArgumentException("perdue"); } finally { return "finally avale l'exception"; }`. Mets `@SuppressWarnings("finally")` sur la méthode.
  → `D02 : finally avale l'exception`
- ☐ **D03.** `static StringBuilder builder()` : `sb = new StringBuilder("x")` ; le `try` rend `sb`, le `finally` fait `sb.append("!")`.
  → `D03 : x!`
- ☐ **D04.** `static String nested(List<String> trace)` :
  - un `try` intérieur ajoute `t1`, puis lève `IllegalStateException("interne")` ; son `finally` ajoute `f1` ;
  - le `catch` extérieur ajoute `c2:` + message ; le `finally` extérieur ajoute `f2` ;
  - la méthode rend la trace jointe par des espaces.
  → `D04 : t1 f1 c2:interne f2`
- ☐ **D05.** `static String fromCatch(List<String> trace)`, en deux niveaux :
  - **intérieur :** le `try` lève `IllegalStateException("premiere")` ; le `catch` ajoute `catch` et lève `UnsupportedOperationException("seconde")` ; le `finally` ajoute `finally` ;
  - **extérieur :** `catch (RuntimeException e)` ajoute le message ;
  - la méthode rend la trace jointe.
  → `D05 : catch finally seconde`
- ☐ **D06.** Une boucle `for i de 0 à 2` :
  - dans un `try` : si `i == 1`, `continue` ; sinon `count += 10` ;
  - le `finally` fait `count++`.
  
  Affiche `count`.
  → `D06 : 23`

## Expériences (hors sortie attendue)

1. Dans D01, ajoute `return "finally";` dans le `finally` : que rendent les deux appels ?
2. `try { }` seul, sans `catch` ni `finally` : compile-t-il ? Et un `try` avec seulement un `finally` ?
3. Un `catch` placé **après** le `finally` : quelle erreur ?
4. Dans le `catch`, la variable `e` est-elle utilisable dans le `finally` ?

## Sortie attendue complète

```
D01 : try [A, B, D] | catch [A, C, D]
D02 : finally avale l'exception
D03 : x!
D04 : t1 f1 c2:interne f2
D05 : catch finally seconde
D06 : 23
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **L'ordre est imposé :** `try`, puis zéro ou plusieurs `catch`, puis zéro ou un `finally`. Il faut au moins un `catch` ou un `finally`. Les accolades sont obligatoires.
- **`finally` s'exécute toujours :**
  - après un `return` (la valeur est **déjà calculée**), un `break`, un `continue` ou une exception ;
  - sauf `System.exit()` ou un arrêt brutal de la JVM.
- **Un `return` dans `finally`** remplace le `return` du `try` ou du `catch`, et **avale** l'exception en cours.
- **Une exception lancée dans `catch` ou `finally`** remplace celle en cours. L'originale est perdue (pas supprimée).
- **Si la valeur rendue est un objet modifiable,** `finally` peut le modifier (même référence).

</details>
