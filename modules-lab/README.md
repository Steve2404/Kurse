# modules-lab - Chapitre 12 (Modules / JPMS)

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
cd modules-lab/01-exports-requires
cat ENONCE.md      # lire l'histoire, le plan, les indices
./run.sh           # lance l'etat actuel (exercice ECHOUE au depart, c'est normal)
# ... editer le(s) fichier(s) TODO indique(s) dans exercise/ ...
./run.sh           # relancer jusqu'a ce que la partie "exercise" passe aussi
```

Les dossiers `exercise/` et `solution/` de chaque lab sont des arbres
source `--module-source-path` complets et independants - ne regarde
`solution/` qu'apres avoir essaye `exercise/` par toi-meme.

## Index des labs

| # | Dossier | Notion | A corriger |
|---|---------|--------|------------|
| 01 | `01-exports-requires` | `module-info.java`, `exports`, `requires` | 1 ligne manquante |
| 02 | `02-qualified-exports` | Export qualifie `exports ... to ...` | 1 nom de module a ajouter |
| 03 | `03-requires-transitive` | `requires transitive` | 1 mot-cle a ajouter |
| 04 | `04-service-provider` | `provides`/`uses` + `ServiceLoader` (les 4 parties d'un service) | 1 directive `uses` manquante |
| 05 | `05-opens-reflection` | `opens` (reflexion profonde vs `exports`) | 1 directive `opens` manquante |
| 06 | `06-cyclic-dependency` | Cycles interdits par le JPMS | Refactoring (extraire un module commun) |
| 07 | `07-module-types` | Named / automatic / unnamed modules | Aucun (observation + prediction) |
| 08 | `08-cli-tooling` | `java --describe-module`, `jar --describe-module`, `jdeps`, `jlink` | Aucun (observation + prediction) |
| 09 | `09-concepts-quiz` | Migration top-down/bottom-up, comptes de modules JDK | Quiz ecrit + verification reelle |

Chaque `run.sh` compile avec le VRAI compilateur et affiche les VRAIS
messages d'erreur JPMS (pas des messages reconstitues) - c'est
volontaire : sur l'examen comme en vrai, c'est le message exact du
compilateur (ou de la JVM) qui indique quelle directive corriger.
