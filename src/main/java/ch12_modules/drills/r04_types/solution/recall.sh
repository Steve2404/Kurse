#!/bin/bash
# SOLUTION du drill 4. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/drills/r04_types/solution
OUT=build/ch12/r04_types/solution
rm -rf "$OUT"

javac -d "$OUT/legacy" "$P/legacy/t/util/Tools.java"
mkdir -p "$OUT/names" "$OUT/split" "$OUT/lib"

echo "--- D01 noms automatiques"
for f in math-utils-3.0.jar string_tools.jar parser2-1.0.0-SNAPSHOT.jar my.cool.lib.jar; do
    jar --create --file "$OUT/names/$f" -C "$OUT/legacy" .
    echo "$f -> $(jar --describe-module --file "$OUT/names/$f" | grep automatic | sed 's/ jar:.*//')"
done

echo "--- D02 paquet partage"
cp "$OUT/names/math-utils-3.0.jar" "$OUT/names/string_tools.jar" "$OUT/split/"
# --add-modules ALL-MODULE-PATH : tous les modules du module path deviennent des racines -> resolution, puis echec.
# L'ordre des deux modules dans le message varie : on ne garde que le paquet en cause.
java -p "$OUT/split" --add-modules ALL-MODULE-PATH -version 2>&1 | grep -o "ResolutionException: .*"     | sed -E 's/.*contains package ([a-z.]+),.*/ResolutionException : paquet \1 dans deux modules/' || true

echo "--- D03 module nomme -> module automatique"
cp "$OUT/names/math-utils-3.0.jar" "$OUT/lib/"
javac -d "$OUT/mods" -p "$OUT/lib" --module-source-path "$P/src" -m t.app
# Un SEUL dossier de module path : on y range aussi le jar de l'application.
jar --create --file "$OUT/lib/t.app.jar" -C "$OUT/mods/t.app" .
java -p "$OUT/lib" -m t.app/t.app.Main

echo "--- D04 Automatic-Module-Name"
jar --create --file "$OUT/names/tools-9.9.jar" --manifest "$P/manifest.txt" -C "$OUT/legacy" .
jar --describe-module --file "$OUT/names/tools-9.9.jar" | grep automatic
