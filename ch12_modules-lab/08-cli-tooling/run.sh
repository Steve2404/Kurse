#!/usr/bin/env bash
# Compile greeting.api/greeting.app puis fait tourner java
# --describe-module, jar --describe-module, jdeps et jlink dessus.
# Voir ENONCE.md - essaie de predire chaque sortie AVANT de lancer.
set -uo pipefail
cd "$(dirname "$0")"

JAVA_HOME_DETECTED=$(java -XshowSettings:properties -version 2>&1 | awk -F'= ' '/ *java.home/ {print $2}')

rm -rf mods jars custom-runtime
sources=$(find src -name "*.java")
javac -d mods --module-source-path src $sources

echo "=== java --describe-module greeting.app ==="
describe_output=$(java --module-path mods --describe-module greeting.app)
echo "$describe_output"

echo
echo "=== jar --describe-module (greeting.api.jar) ==="
mkdir -p jars
jar --create --file jars/greeting.api.jar --module-version 1.0 -C mods/greeting.api .
jar_output=$(jar --describe-module --file jars/greeting.api.jar)
echo "$jar_output"

echo
echo "=== jdeps --module-path mods -m greeting.app ==="
jdeps_output=$(jdeps --module-path mods -m greeting.app)
echo "$jdeps_output"

echo
echo "=== jlink : image sur mesure ==="
jlink --module-path "mods:$JAVA_HOME_DETECTED/jmods" --add-modules greeting.app \
      --output custom-runtime --no-header-files --no-man-pages
run_output=$(./custom-runtime/bin/java --module-path mods --module greeting.app/com.example.greeting.app.Main)
echo "Execution via le runtime sur mesure : $run_output"
custom_size_kb=$(du -sk custom-runtime | cut -f1)
jdk_size_kb=$(du -sk "$JAVA_HOME_DETECTED" | cut -f1)
echo "Taille du runtime sur mesure : ${custom_size_kb} Ko -- Taille du JDK complet : ${jdk_size_kb} Ko"
modules_count=$(./custom-runtime/bin/java --list-modules | wc -l | tr -d ' ')
echo "Nombre de modules dans le runtime sur mesure : $modules_count"

echo
status=0
echo "$describe_output" | grep -q "requires greeting.api" && echo "[PASS] describe-module liste bien greeting.api" || { echo "[FAIL] greeting.api absent"; status=1; }
echo "$describe_output" | grep -q "requires java.base mandated" && echo "[PASS] describe-module liste java.base (mandated, jamais ecrit a la main)" || { echo "[FAIL] java.base absent"; status=1; }
echo "$jar_output" | grep -q "exports com.example.greeting.api" && echo "[PASS] jar --describe-module lit bien le module-info dans le jar" || { echo "[FAIL] jar describe-module incorrect"; status=1; }
echo "$jdeps_output" | grep -q "greeting.app -> greeting.api" && echo "[PASS] jdeps confirme la dependance greeting.app -> greeting.api" || { echo "[FAIL] jdeps ne montre pas la dependance module"; status=1; }
echo "$jdeps_output" | grep -qE "java\.(io|lang)" && echo "[PASS] jdeps descend jusqu'aux packages JDK precis (java.io/java.lang)" || { echo "[FAIL] jdeps ne descend pas au niveau package"; status=1; }
[ "$run_output" = "Bonjour, Steve !" ] && echo "[PASS] l'image jlink execute bien l'application" || { echo "[FAIL] execution via jlink incorrecte : $run_output"; status=1; }
if [ "$custom_size_kb" -lt "$jdk_size_kb" ]; then
    echo "[PASS] le runtime sur mesure est bien plus petit que le JDK complet"
else
    echo "[FAIL] le runtime sur mesure n'est pas plus petit ($custom_size_kb Ko vs $jdk_size_kb Ko)"
    status=1
fi

exit $status
