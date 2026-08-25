#!/usr/bin/env bash
# Construit mathutils.jar (sans module-info) et mathutils-named.jar
# (avec), puis fait tourner la meme bibliotheque de 3 facons. Voir
# ENONCE.md - essaie de predire le resultat AVANT de lancer.
set -uo pipefail
cd "$(dirname "$0")"

rm -rf classes-unnamed classes-named jars classpath-app-out modpath-app-mods-auto modpath-app-mods-named modpath-auto modpath-named
mkdir -p jars modpath-auto modpath-named

javac -d classes-unnamed lib-src/com/example/mathutils/Calc.java
javac -d classes-named --module-source-path lib-src-named $(find lib-src-named -name "*.java")

jar --create --file jars/mathutils-1.0.jar -C classes-unnamed .
jar --create --file jars/mathutils-named-1.0.jar -C classes-named/mathutils .
cp jars/mathutils-1.0.jar modpath-auto/
cp jars/mathutils-named-1.0.jar modpath-named/

echo "=== (a) Sur le CLASSPATH : unnamed module ==="
javac -cp jars/mathutils-1.0.jar -d classpath-app-out classpath-app/AppMain.java
output_a=$(java -cp "jars/mathutils-1.0.jar:classpath-app-out" AppMain)
echo "$output_a"

echo
echo "=== (b) Sur le MODULE-PATH, jar SANS module-info : automatic module ==="
javac -d modpath-app-mods-auto --module-path modpath-auto --module-source-path modpath-app-src $(find modpath-app-src -name "*.java")
output_b=$(java --module-path "modpath-auto:modpath-app-mods-auto" --module app/com.example.app.Main)
echo "$output_b"
list_b=$(java --module-path modpath-auto --list-modules 2>&1 | grep mathutils)
echo "java --list-modules -> $list_b"

echo
echo "=== (c) Sur le MODULE-PATH, jar AVEC module-info : named module ==="
javac -d modpath-app-mods-named --module-path modpath-named --module-source-path modpath-app-src $(find modpath-app-src -name "*.java")
output_c=$(java --module-path "modpath-named:modpath-app-mods-named" --module app/com.example.app.Main)
echo "$output_c"
list_c=$(java --module-path modpath-named --list-modules 2>&1 | grep mathutils)
echo "java --list-modules -> $list_c"

echo
status=0
[ "$output_a" = "Resultat (classpath) : 36" ] && echo "[PASS] (a) classpath" || { echo "[FAIL] (a) classpath : $output_a"; status=1; }
[ "$output_b" = "Resultat (module) : 36" ] && echo "[PASS] (b) automatic - resultat" || { echo "[FAIL] (b) resultat : $output_b"; status=1; }
echo "$list_b" | grep -q "automatic" && echo "[PASS] (b) automatic - bien etiquete 'automatic' dans --list-modules" || { echo "[FAIL] (b) pas etiquete automatic : $list_b"; status=1; }
[ "$output_c" = "Resultat (module) : 36" ] && echo "[PASS] (c) named - resultat" || { echo "[FAIL] (c) resultat : $output_c"; status=1; }
if echo "$list_c" | grep -q "automatic"; then
    echo "[FAIL] (c) named ne devrait PAS etre etiquete automatic : $list_c"
    status=1
else
    echo "[PASS] (c) named - PAS etiquete automatic (comme attendu)"
fi

exit $status
