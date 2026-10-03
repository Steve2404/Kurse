#!/bin/bash
# SOLUTION du drill 1. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/drills/r01_directives/solution
OUT=build/ch12/r01_directives/solution
rm -rf "$OUT"

javac -d "$OUT/mods" --module-source-path "$P/src" -m d.app,d.plugin,d.open,d.extra
java -p "$OUT/mods" -m d.app/d.app.Main
for m in d.base d.mid d.friend d.plugin d.app d.open d.extra; do
    echo "--- $m"
    java -p "$OUT/mods" --describe-module "$m" | sed 's/ file:.*//' | grep -v "java.base mandated" | sort
done
echo "--- D07 requires static"
java -p "$OUT/mods" -m d.extra/d.extra.Main
java -p "$OUT/mods" --add-modules d.open -m d.extra/d.extra.Main
