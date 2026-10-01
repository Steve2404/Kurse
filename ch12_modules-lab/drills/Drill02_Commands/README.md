# Drill 02 - Les commandes : javac, java, jar, jdeps

Mode d'emploi : voir `../Drill01_Directives/README.md`. Les modules de
`src/` sont CORRECTS : on ne travaille que les commandes, avec les
FORMES COURTES quand elles existent. Un TODO = une fonction de
`exercise/commands.sh` = une commande. Verifie : `./run.sh exercise 02`.

## Les TODO

| TODO | Fonction | Forme visee |
|---|---|---|
| 1 | `compile_all` | `javac -d ... --module-source-path ...` |
| 2 | `run_short` | `java -p ... -m module/classe` |
| 3 | `describe_short` | `java -p ... -d module` |
| 4 | `list_ours` | `java -p ... --list-modules` (+ `grep`) |
| 5 | `jar_all` | `jar -c -f ... [-e classe] -C dossier .` |
| 6 | `run_jar` | `java -p jars -m module` (sans classe) |
| 7 | `jar_describe` | `jar -d -f fichier.jar` |
| 8 | `jdeps_summary` | `jdeps -s --module-path ... -m module` |
| 9 | `jdeps_internals` | `jdeps --jdk-internals dossier` |
| 10 | `resolution` | `java -p ... --show-module-resolution -m ...` |

Remettre a zero : `git restore ch12_modules-lab/drills/Drill02_Commands/exercise`

---

## CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher)

| Long | Court | Outil |
|---|---|---|
| `--module-path` | `-p` | javac, java, jdeps, jlink |
| `--module` | `-m` | java, javac, jdeps |
| `--describe-module` | `-d` | java, jar (pour javac, `-d` = dossier de sortie !) |
| `--create` / `--file` | `-c` / `-f` | jar |
| `--main-class` | `-e` | jar |
| `--summary` | `-s` | jdeps |
| `--list-modules` | - | java |
| `--show-module-resolution` | - | java |
| `--jdk-internals` | - | jdeps |
| `--add-modules`, `--add-exports`, `--add-opens` | - | java (et javac sauf opens) |

- `jar -C dossier .` : "va dans ce dossier et prends tout".
- `jlink --module-path mods<SEP>$JAVA_HOME/jmods --add-modules m --output image`.
