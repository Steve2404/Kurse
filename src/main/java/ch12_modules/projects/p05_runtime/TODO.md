# Projet 5 — L'image d'exécution (`jdeps`, `jlink`, graphe des modules)

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 12) :**
- **`jdeps`** : `-s` (résumé), `-R` (récursif), `--module-path`, `--print-module-deps` ;
- **`jlink`** :
  - une image d'exécution **minimale** ne contient que les modules nécessaires ;
  - les options `--module-path`, `--add-modules`, `--output`, `--launcher nom=module`, `--strip-debug`, `--no-header-files`, `--no-man-pages` ;
- **`jar --module-version`** ;
- `java --list-modules` d'une image ;
- **le graphe des modules à l'exécution :**
  - `ModuleLayer.boot().findModule(…)` ;
  - `ModuleDescriptor` avec `requires()`, `packages()`, `mainClass()`, `version()`.

Côté algorithmes :
- le **sac à dos 0/1** en programmation dynamique (réassort sous budget) ;
- un **ordre topologique** des modules, par parcours en profondeur.

**Ce que TU crées :** tes 2 modules dans `ch12_modules/p05_runtime/src/` (`inv.core`, `inv.app`), et ton script `build.sh` dans ce dossier.

**Règle du crescendo :** chapitres 1 à 12.

---

## Tableau de bord

### ☐ Étape 1 — Les modules

- **`inv.core`** : `requires java.logging;`, exporte `inv.core`.
  - `record Item(String name, int cost, int value)` ;
  - `final class Restock`, avec un `private static final Logger LOG = Logger.getLogger(Restock.class.getName())` et `static String loggerName()`. C'est ce qui rend `java.logging` **nécessaire** ;
  - **`static List<Item> choose(List<Item> items, int budget)`** : un tableau `best[i][b]` (meilleure valeur avec les i premiers articles et un budget b) ; on **remonte** ensuite le choix (un article est pris si `best[i][b] != best[i - 1][b]`), puis on rend la liste dans l'ordre d'origine.
- **`inv.app`** : `requires inv.core;`, et `inv.app.Main`, avec :
  ```java
  static final List<Item> ITEMS = List.of(new Item("cafe", 12, 30), new Item("the", 7, 14), new Item("sucre", 4, 9), new Item("biscuits", 9, 21),
          new Item("lait", 6, 10), new Item("miel", 11, 25));
  static final int BUDGET = 30;
  ```

### ☐ Étape 2 — Le programme

```
reassort (budget 30) : [cafe, the, miel], cout 30, valeur 69
inv.app requiert [inv.core, java.base], inv.core requiert [java.base, java.logging]
ordre de chargement : [java.base, java.logging, inv.core, inv.app]
inv.app : paquets [inv.app], classe principale inv.app.Main, version 1.2
```
- **`static Set<String> requiresOf(String name)`** : les noms des `requires` du descripteur de `ModuleLayer.boot().findModule(name).orElseThrow()`, dans un `TreeSet`.
- **`static void visit(String name, Set<String> seen, List<String> order)`** : un parcours en profondeur. Un module est ajouté **après** ses dépendances.
- **Les 5 lignes :**
  1. le réassort : noms choisis, coût, valeur ;
  2. `journal : ` + `loggerName()` ;
  3. `requiresOf` des deux modules ;
  4. l'ordre de chargement depuis `inv.app` ;
  5. pour le descripteur de `inv.app` : `packages()`, `mainClass()` (ou `aucune`), et `version()` (ou `aucune`).
- **Question :** pourquoi la version vaut-elle `1.2` seulement quand on lance depuis le jar ?

### ☐ Étape 3 — Le script `build.sh`

```
--- jdeps
inv.app -> inv.core
...
--- modules de l'image (versions du JDK retirees)
inv.app@1.2
inv.core
java.base
java.logging
--- lanceur
reassort (budget 30) : [cafe, the, miel], cout 30, valeur 69
```
- **En tête :** `P=ch12_modules/p05_runtime` et `OUT=build/ch12/p05_runtime`.
- **Les commandes :**
  1. `javac -d "$OUT/mods" --module-source-path "$P/src" -m inv.app` ;
  2. les jars `inv.core.jar`, puis `inv.app.jar` avec `--main-class inv.app.Main --module-version 1.2` ;
  3. `echo "--- execution sur le JDK complet"`, puis `java -p "$OUT/jars" -m inv.app` ;
  4. `echo "--- jdeps"`, puis :
     - `jdeps -s -R --module-path "$OUT/jars" -m inv.app` ;
     - `jdeps --print-module-deps --module-path "$OUT/jars" "$OUT/jars/inv.app.jar"` ;
  5. `jlink --module-path "$OUT/jars" --add-modules inv.app --output "$OUT/image" --launcher inventaire=inv.app --strip-debug --no-header-files --no-man-pages`.
     - Les modules du JDK sont trouvés tout seuls ;
  6. `echo "--- modules de l'image (versions du JDK retirees)"`, puis `"$OUT/image/bin/java" --list-modules | sed -E 's/^(java\.[a-z.]+)@.*/\1/'` ;
  7. `echo "--- lanceur"`, puis `"$OUT/image/bin/inventaire" | head -1`.
- **Questions :**
  - Pourquoi l'image ne contient-elle que 4 modules, alors que le JDK en a plus de 60 ?
  - Que contient le dossier `$OUT/image/bin` ?
- **Expérience :** ajoute `--bind-services` à `jlink`. Combien de modules maintenant ? Pourquoi ?

---

## Checklist (vérifiée par `Check`)

- **Dans les `module-info`** : `requires java.logging;`, `module inv.app`, `requires inv.core;`.
- **Dans le Java** : `Logger.getLogger(`, `ModuleLayer.boot()`, `.findModule(`, `.getDescriptor()`, `.requires()`, `.packages()`, `.mainClass()`, `.version()`, `new int[`.
- **Dans le script** : `--module-version 1.2`, `jdeps -s -R --module-path`, `jdeps --print-module-deps`, `jlink --module-path`, `--add-modules inv.app`, `--output`, `--launcher inventaire=inv.app`, `--strip-debug`, `--no-header-files`, `--no-man-pages`, `/image/bin/java" --list-modules`, `/image/bin/inventaire"`.

---

## Sortie attendue complète

```
--- execution sur le JDK complet
reassort (budget 30) : [cafe, the, miel], cout 30, valeur 69
journal : inv.core.Restock
inv.app requiert [inv.core, java.base], inv.core requiert [java.base, java.logging]
ordre de chargement : [java.base, java.logging, inv.core, inv.app]
inv.app : paquets [inv.app], classe principale inv.app.Main, version 1.2
--- jdeps
inv.app -> inv.core
inv.app -> java.base
inv.core -> java.base
inv.core -> java.logging
inv.core,java.base
--- modules de l'image (versions du JDK retirees)
inv.app@1.2
inv.core
java.base
java.logging
--- lanceur
reassort (budget 30) : [cafe, the, miel], cout 30, valeur 69
```
