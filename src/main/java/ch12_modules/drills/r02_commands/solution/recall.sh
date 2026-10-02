#!/bin/bash
# SOLUTION du drill 2. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/drills/r02_commands/solution
OUT=build/ch12/r02_commands/solution
rm -rf "$OUT"

echo "--- D01"
javac -d "$OUT/mods" --module-source-path "$P/src" -m c.app
ls "$OUT/mods"

echo "--- D02"
java -p "$OUT/mods" -m c.app/c.app.Main
java --module-path "$OUT/mods" --module c.app/c.app.Main

echo "--- D03"
mkdir -p "$OUT/jars"
jar --create --file "$OUT/jars/c.lib.jar" -C "$OUT/mods/c.lib" .
jar --create --file "$OUT/jars/c.app.jar" --main-class c.app.Main --module-version 2.0 -C "$OUT/mods/c.app" .
java -p "$OUT/jars" -m c.app

echo "--- D04"
jar --list --file "$OUT/jars/c.app.jar"

echo "--- D05"
java -p "$OUT/jars" --list-modules | grep "^c\." | sed 's/ file:.*//'

echo "--- D06"
javac -d "$OUT/cp" -p "$OUT/mods" --add-modules c.lib "$P/cp/Legacy.java"
java -p "$OUT/mods" --add-modules c.lib -cp "$OUT/cp" Legacy

echo "--- D07"
java -p "$OUT/mods" --show-module-resolution -m c.app/c.app.Main | grep "^\(root \)\?c\." | sed 's/ file:.*//'

echo "--- D08"
jdeps -s --module-path "$OUT/mods" -m c.app
