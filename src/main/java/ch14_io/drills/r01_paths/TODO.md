# Drill de rappel 1 — `Path` et `File`

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall01`** dans le paquet `ch14_io.drills.r01_paths`, avec `static String s(Path p)`, qui remplace `\` par `/`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_paths` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `p = Path.of("a", "b", "c", "d.txt")`. Affiche :
  - `s(p)`, `getFileName()`, `s(getParent())`, `getRoot()` ;
  - `getNameCount()`, `getName(0)`, `s(subpath(1, 3))`.
  → `D01 : a/b/c/d.txt d.txt a/b/c null 4 a b/c`
- ☐ **D02.** `normalize()` de `Paths.get("x/./y/../z")`, de `"../a/../../b"` et de `"a/b/.."`. Puis : la normalisation de `"./"` est-elle vide ?
  → `D02 : x/z ../../b a true`
- ☐ **D03.** `base = Path.of("projet/src")`. Affiche :
  - `resolve("Main.java")`, `resolveSibling("test")`, `resolve("")` ;
  - si `resolve` d'un chemin **absolu** rend un chemin absolu.
  → `D03 : projet/src/Main.java projet/test projet/src true`
- ☐ **D04.**
  - `Path.of("a/b").relativize(Path.of("a/c/d"))` ;
  - puis l'inverse ;
  - puis `x` relativisé vers `x` : le résultat est-il vide ?
  → `D04 : ../c/d ../../b true`
- ☐ **D05.** Sur `p` :
  - `startsWith("a")`, `startsWith("a/b")`, `startsWith("a/")` ;
  - `endsWith("d.txt")`, `endsWith(".txt")`.
  
  Puis `Path.of("a/b").equals(Path.of("a/./b"))`, et la même chose avec `normalize()`.
  → `D05 : true true true true false false true`
- ☐ **D06.** `f = new File("dossier", "fichier.txt")`. Affiche `getName()`, `getParent()`, `exists()`, `isAbsolute()`, `s(f.toPath())`, puis `f.toPath().toFile().getName()`.
  → `D06 : fichier.txt dossier false false dossier/fichier.txt fichier.txt`

## Expériences (hors sortie attendue)

1. `Path.of("a/b").relativize(Path.of("/a/b"))` : que se passe-t-il ?
2. Que rend `Path.of("a").getParent()` ? Et `Path.of("").getFileName()` ?
3. Les méthodes de `Path` modifient-elles le chemin, ou en rendent-elles un nouveau ?

## Sortie attendue complète

```
D01 : a/b/c/d.txt d.txt a/b/c null 4 a b/c
D02 : x/z ../../b a true
D03 : projet/src/Main.java projet/test projet/src true
D04 : ../c/d ../../b true
D05 : true true true true false false true
D06 : fichier.txt dossier false false dossier/fichier.txt fichier.txt
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **`Path` est IMMUABLE** : chaque méthode rend un **nouveau** `Path`.
- Aucune méthode de `Path` ne touche au disque, sauf `toRealPath()` (qui exige un fichier existant).
- **Les fabriques :** `Path.of(…)` (Java 11), `Paths.get(…)`, `FileSystems.getDefault().getPath(…)`, et `file.toPath()` ↔ `path.toFile()`.
- **`getName(0)`** est le nom le plus proche de la racine ; `getRoot()` vaut `null` pour un chemin relatif.
- **`subpath(début, fin)`** : de début inclus à fin exclu, sans la racine.
- **`resolve(x)`** : si x est absolu, rend x ; sinon `this/x`. **`resolveSibling(x)`** = `getParent().resolve(x)`.
- **`relativize`** : les deux chemins doivent être de même nature (relatifs, ou absolus) ; sinon `IllegalArgumentException`.
- **`normalize`** retire `.` et résout `..`. **`equals`** ne normalise pas.
- **`startsWith` et `endsWith`** comparent des **noms entiers** : `"a/b"` ne commence pas par `"a/"`… mais `"a/"` est lu comme le chemin `a`.

</details>
