# ch12_modules-lab - Chapitre 12 (Modules / JPMS)

Ce dossier est SEPARE du build Maven (`src/`, `pom.xml`) : le Java
Platform Module System se manipule avec `javac`/`java`/`jar`/`jdeps`/
`jlink` en ligne de commande, pas avec Maven ici. Chaque lab est
independant, avec son propre `ENONCE.md` (l'equivalent du long
commentaire pedagogique en tete des `ExerciseNN_*.java` des autres
chapitres) et son propre `run.sh` executable.

Prerequis : un JDK 9+ avec `javac`, `java`, `jar`, `jdeps` et `jlink`
accessibles dans le PATH (verifie avec `java -version`).

## Comment utiliser un lab

```bash
cd ch12_modules-lab/01-exports-requires
cat ENONCE.md      # lire l'histoire, le plan, les indices
./run.sh           # lance l'etat actuel (exercice ECHOUE au depart, c'est normal)
# ... editer le(s) fichier(s) TODO indique(s) dans exercise/ ...
./run.sh           # relancer jusqu'a ce que la partie "exercise" passe aussi
```

Les dossiers `exercise/` et `solution/` de chaque lab sont des arbres
source `--module-source-path` complets et independants - ne regarde
`solution/` qu'apres avoir essaye `exercise/` par toi-meme.

## Index des labs (ranges par theme)

| # | Dossier | Theme | Notion | A faire |
|---|---------|-------|--------|---------|
| 01 | `01-exports-requires` | Directives de base | `module-info.java`, `exports`, `requires` | 1 directive |
| 02 | `02-qualified-exports` | Directives de base | export qualifie `exports ... to ...` | 1 nom de module |
| 03 | `03-requires-transitive` | Directives de base | `requires transitive` | 1 mot-cle |
| 04 | `04-service-provider` | Services | `provides`/`uses` + `ServiceLoader` (les 4 parties d'un service) | 1 directive `uses` |
| 05 | `05-service-advanced` | Services (avance) | 2 fournisseurs, methode `provider()`, `ServiceLoader.stream()`, `Provider.type()` | 5 TODO |
| 06 | `06-opens-reflection` | Reflexion | `opens` (reflexion profonde contre `exports`) | 1 directive `opens` |
| 07 | `07-command-line-overrides` | Reflexion (avance) | `--add-exports` (javac ET java), `--add-opens`, `--add-modules` | 4 options |
| 08 | `08-cyclic-dependency` | Cycles | cycles interdits par le JPMS | refactoring (module commun) |
| 09 | `09-module-types` | Types de modules | named / automatic / unnamed, nom automatique | 5 TODO (module-info + commandes) |
| 10 | `10-cli-tooling` | Outils | `--describe-module`, `jar`, `jdeps`, `jlink`, `--show-module-resolution` | 10 commandes |
| 11 | `11-migration` | Capstone | migration bottom-up, top-down, puis tout nomme | 4 TODO |

Les **drills** (memorisation) sont dans `drills/` : voir `drills/REVISION.md`
pour le parcours complet (labs + drills) et la repetition espacee.

Sous Windows, lancer les scripts depuis **Git Bash** ; ils choisissent
eux-memes le bon separateur de chemins (`;` sous Windows, `:` ailleurs).
Les fichiers generes vont dans `build/` (ignore par git).

Chaque `run.sh` compile avec le VRAI compilateur et affiche les VRAIS
messages d'erreur JPMS (pas des messages reconstitues) - c'est
volontaire : sur l'examen comme en vrai, c'est le message exact du
compilateur (ou de la JVM) qui indique quelle directive corriger.
