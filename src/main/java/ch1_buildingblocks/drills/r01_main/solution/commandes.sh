#!/bin/bash
# SOLUTION du script du drill 1 (lance depuis la racine du depot).
set -e
SRC=src/main/java/ch1_buildingblocks/drills/r01_main/solution/Recall01.java
CLS=ch1_buildingblocks.drills.r01_main.solution.Recall01
OUT=build/ch1-r01
rm -rf "$OUT"
javac -d "$OUT" "$SRC"
java -cp "$OUT" "$CLS" un 1 0.25 TRUE "deux mots"
jar --create --file "$OUT/r01.jar" --main-class "$CLS" -C "$OUT" ch1_buildingblocks
java -jar "$OUT/r01.jar" trois 3 1.5 false "  espaces  "
java "$SRC" quatre 4 2 true seul
