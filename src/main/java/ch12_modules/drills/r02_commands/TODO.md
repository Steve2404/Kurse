# Drill de rappel 2 — Les commandes `javac`, `java`, `jar`

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Dans `ch12_modules/drills/r02_commands/` :
  - `src/c.lib/` : `module c.lib` exporte `c.lib`, qui contient `Calc.add(int, int)` ;
  - `src/c.app/` : `module c.app` requiert `c.lib`. Son `main` affiche `"somme " + Calc.add(2, 3) + " dans " + <nom du module de Main>` ;
  - `cp/Legacy.java` : **sans paquet**. Son `main` affiche `"classpath : somme " + Calc.add(10, 5) + ", module sans nom " + !<isNamed()>`.
- Ton script `recall.sh` va dans ce dossier, avec `P=ch12_modules/drills/r02_commands` et `OUT=build/ch12/r02_commands`. Chaque défi commence par `echo "--- Dnn"`.

## Défis

- ☐ **D01.** Compile `c.app` (et donc `c.lib`) dans `$OUT/mods`, puis `ls "$OUT/mods"`.
  → `c.app` puis `c.lib`
- ☐ **D02.** Lance `c.app/c.app.Main` deux fois : avec les options **courtes** (`-p`, `-m`), puis **longues** (`--module-path`, `--module`).
  → `somme 5 dans c.app` (deux fois)
- ☐ **D03.** Crée `$OUT/jars/c.lib.jar`, puis `$OUT/jars/c.app.jar` avec la classe principale `c.app.Main` et la version `2.0`. Lance `c.app` depuis les jars, **sans** nom de classe.
  → `somme 5 dans c.app`
- ☐ **D04.** Liste le contenu de `c.app.jar` (`jar --list`).
  → `META-INF/` … `c/app/Main.class`
- ☐ **D05.** `java -p "$OUT/jars" --list-modules`, en ne gardant que les lignes qui commencent par `c.`, puis `| sed 's/ file:.*//'`.
  → `c.app@2.0` puis `c.lib`
- ☐ **D06.** Compile `cp/Legacy.java` dans `$OUT/cp`, avec `-p "$OUT/mods" --add-modules c.lib`, puis lance-la avec le même module path, le même `--add-modules` et `-cp "$OUT/cp"`.
  → `classpath : somme 15, module sans nom true`
- ☐ **D07.** `--show-module-resolution` sur `c.app` : garde les lignes qui commencent par `root c.` ou `c.` (`grep "^\(root \)\?c\."`), puis `sed 's/ file:.*//'`.
  → `root c.app` puis `c.app requires c.lib`
- ☐ **D08.** `jdeps -s --module-path "$OUT/mods" -m c.app`.
  → `c.app -> c.lib` puis `c.app -> java.base`

## Expériences (hors sortie attendue)

1. Lance D06 **sans** `--add-modules c.lib` : quelle erreur ? Pourquoi un programme du classpath n'a-t-il pas `c.lib` comme racine ?
2. `java -p "$OUT/jars" -m c.app` **sans** `--main-class` dans le jar : que se passe-t-il ?
3. `javac -d out src/c.app/module-info.java src/c.app/c/app/Main.java`, sans `-p` : quelle erreur ?
4. Quelle est la forme longue de `-p` ? Et celle de `-m` ?

## Sortie attendue complète

```
--- D01
c.app
c.lib
--- D02
somme 5 dans c.app
somme 5 dans c.app
--- D03
somme 5 dans c.app
--- D04
META-INF/
META-INF/MANIFEST.MF
module-info.class
c/
c/app/
c/app/Main.class
--- D05
c.app@2.0
c.lib
--- D06
classpath : somme 15, module sans nom true
--- D07
root c.app
c.app requires c.lib
--- D08
c.app -> c.lib
c.app -> java.base
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Outil | Option | Sens |
|---|---|---|
| `javac` | `-d dossier` | où écrire les `.class` |
| `javac` | `--module-source-path src` | `src` contient un dossier par module |
| `javac` / `java` | `-p` = `--module-path` | où chercher les modules compilés ou les jars |
| `java` | `-m` = `--module mod/classe` | module racine et classe principale (la classe est facultative si le jar a une `main-class`) |
| `java` | `--add-modules m` | ajoute des modules racines (utile depuis le classpath) |
| `java` | `--list-modules`, `--describe-module m` (= `-d m`), `--show-module-resolution` | inspecter |
| `jar` | `--create --file f` (= `-c -f f`), `--main-class` (= `-e`), `--module-version`, `-C dossier .` | créer |
| `jar` | `--list` (= `-t`), `--describe-module` (= `-d`) | inspecter |

- Les options de **classpath** (`-cp`, `-classpath`, `--class-path`) existent toujours. Le code du classpath vit dans le **module sans nom**.

</details>
