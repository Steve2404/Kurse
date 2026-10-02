#!/bin/bash
# SOLUTION du drill 6 (kata) : uniquement des options COURTES. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/drills/r06_kata/solution
OUT=build/ch12/r06_kata/solution
rm -rf "$OUT"
mkdir -p "$OUT/jars"

echo "--- D01"
javac -d "$OUT/mods" --module-source-path "$P/src" -m k.app,k.core
java -p "$OUT/mods" -m k.app/k.app.Main

echo "--- D02 java -d"
java -p "$OUT/mods" -d k.core | sed 's/ file:.*//' | grep -v "java.base mandated" | sort

echo "--- D03 jar -c -f -e, puis jar -d -f"
jar -c -f "$OUT/jars/k.core.jar" -C "$OUT/mods/k.core" .
jar -c -f "$OUT/jars/k.app.jar" -e k.app.Main -C "$OUT/mods/k.app" .
jar -d -f "$OUT/jars/k.app.jar" | sed 's/ jar:.*//' | grep -v "java.base mandated" | sort
java -p "$OUT/jars" -m k.app
