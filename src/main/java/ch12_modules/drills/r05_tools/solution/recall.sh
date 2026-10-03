#!/bin/bash
# SOLUTION du drill 5. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/drills/r05_tools/solution
OUT=build/ch12/r05_tools/solution
rm -rf "$OUT"

echo "--- D01 describe-module java.sql"
java --describe-module java.sql | sed 's/@[0-9.]*//' | sort

echo "--- D02 list-modules"
java --list-modules | grep "^java\.s" | sed 's/@.*//'

echo "--- D03 jdeps"
javac -d "$OUT/mods" --module-source-path "$P/src" -m r.report
java -p "$OUT/mods" -m r.report/r.report.Main
jdeps -s --module-path "$OUT/mods" -m r.report
jdeps --print-module-deps --module-path "$OUT/mods" -m r.report

echo "--- D04 jdk-internals"
javac -d "$OUT/cp" --add-exports java.base/sun.security.x509=ALL-UNNAMED "$P/internal/Peek.java" 2>/dev/null
jdeps --jdk-internals "$OUT/cp" | grep -- "->" | tr -s " " | sed "s/^ //"

echo "--- D05 jlink"
jlink --module-path "$OUT/mods" --add-modules r.report --output "$OUT/image" --strip-debug --compress=2 --no-header-files --no-man-pages
"$OUT/image/bin/java" --list-modules | sed 's/@.*//'
"$OUT/image/bin/java" -m r.report/r.report.Main

echo "--- D06 jmod"
mkdir -p "$OUT/jmods"
jmod create --class-path "$OUT/mods/r.report" --main-class r.report.Main --module-version 3.1 "$OUT/jmods/r.report.jmod"
jmod describe "$OUT/jmods/r.report.jmod" | sort
jmod list "$OUT/jmods/r.report.jmod" | sort
# Un .jmod sert a jlink, mais java refuse de l'executer.
java -p "$OUT/jmods/r.report.jmod" -m r.report 2>&1 | grep -o "JMOD format not supported at execution time" || true
jlink --module-path "$OUT/jmods" --add-modules r.report --output "$OUT/image-jmod"
"$OUT/image-jmod/bin/java" -m r.report
