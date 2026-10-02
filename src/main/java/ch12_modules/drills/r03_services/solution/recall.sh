#!/bin/bash
# SOLUTION du drill 3. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/drills/r03_services/solution
OUT=build/ch12/r03_services/solution
rm -rf "$OUT"

javac -d "$OUT/mods" --module-source-path "$P/src" -m s.app,s.square,s.triangle
echo "--- D02 tous"
java -p "$OUT/mods" -m s.app/s.app.Main
echo "--- D03 carre seul"
java -p "$OUT/mods" --limit-modules s.app,s.square -m s.app/s.app.Main
echo "--- D04 aucun fournisseur"
java -p "$OUT/mods" --limit-modules s.app -m s.app/s.app.Main
echo "--- D05"
java -p "$OUT/mods" --describe-module s.triangle | sed 's/ file:.*//' | grep -v "java.base mandated" | sort
java -p "$OUT/mods" --describe-module s.locator | sed 's/ file:.*//' | grep -v "java.base mandated" | sort
