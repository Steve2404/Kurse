#!/bin/bash
# SOLUTION du script du projet 1. Check le lance depuis la RACINE du depot : les chemins partent de la.
set -e

P=ch12_modules/p01_library/solution
OUT=build/ch12/p01_library/solution
rm -rf "$OUT"

# 1. Compiler les 3 modules d'un coup : --module-source-path pointe sur le dossier qui contient UN dossier par module ;
#    -m dit lesquels compiler (leurs dependances sont trouvees toutes seules) ; -d range un dossier par module.
javac -d "$OUT/mods" --module-source-path "$P/src" -m library.app,library.service,library.model

# 2. Lancer : -p (--module-path) ou chercher les modules, -m module/classe principale.
java -p "$OUT/mods" -m library.app/library.app.Main

# 3. Decrire un module tel que la JVM le voit (on retire le chemin, propre a chaque machine).
echo "--- describe-module library.service"
java -p "$OUT/mods" --describe-module library.service | sed 's/ file:.*//' | sort

# 4. Un jar par module ; celui de l'application note sa classe principale (--main-class).
mkdir -p "$OUT/jars"
jar --create --file "$OUT/jars/library.model.jar" -C "$OUT/mods/library.model" .
jar --create --file "$OUT/jars/library.service.jar" -C "$OUT/mods/library.service" .
jar --create --file "$OUT/jars/library.app.jar" --main-class library.app.Main -C "$OUT/mods/library.app" .
echo "--- describe-module du jar library.app"
jar --describe-module --file "$OUT/jars/library.app.jar" | sed 's/ jar:.*//' | sort

# 5. Lancer depuis les jars : -m module SEUL suffit, la classe principale est dans le descripteur.
echo "--- depuis les jars"
java -p "$OUT/jars" -m library.app | head -1

# 6. L'intrus doit ECHOUER : on n'affiche que le message d'erreur (sans le chemin du fichier).
echo "--- intrus"
javac -d "$OUT/intruder" -p "$OUT/mods" --module-source-path "$P/intruder" -m library.intruder 2>&1 \
    | grep -E "error:|declared in" | sed 's/.*error:/error:/; s/^ *//' || true
