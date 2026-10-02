#!/bin/bash
# SOLUTION du script du projet 5. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/p05_runtime/solution
OUT=build/ch12/p05_runtime/solution
rm -rf "$OUT"

# 1. Compiler, puis empaqueter ; --module-version inscrit une version dans le descripteur du module.
javac -d "$OUT/mods" --module-source-path "$P/src" -m inv.app
mkdir -p "$OUT/jars"
jar --create --file "$OUT/jars/inv.core.jar" -C "$OUT/mods/inv.core" .
jar --create --file "$OUT/jars/inv.app.jar" --main-class inv.app.Main --module-version 1.2 -C "$OUT/mods/inv.app" .
echo "--- execution sur le JDK complet"
java -p "$OUT/jars" -m inv.app

# 2. jdeps : -s resume, -R suit les dependances recursivement ; --print-module-deps donne la liste pour jlink.
echo "--- jdeps"
jdeps -s -R --module-path "$OUT/jars" -m inv.app
jdeps --print-module-deps --module-path "$OUT/jars" "$OUT/jars/inv.app.jar"

# 3. jlink : une image d'execution qui ne contient QUE les modules necessaires (+ un lanceur).
#    Les modules du JDK (jmods) sont trouves automatiquement.
jlink --module-path "$OUT/jars" --add-modules inv.app --output "$OUT/image" --launcher inventaire=inv.app \
    --strip-debug --no-header-files --no-man-pages
echo "--- modules de l'image (versions du JDK retirees)"
"$OUT/image/bin/java" --list-modules | sed -E 's/^(java\.[a-z.]+)@.*/\1/'

# 4. Le lanceur cree par jlink : plus besoin de -p ni de -m.
echo "--- lanceur"
"$OUT/image/bin/inventaire" | head -1
