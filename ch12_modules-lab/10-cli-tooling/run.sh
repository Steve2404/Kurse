#!/usr/bin/env bash
# Compile greeting.api / greeting.app, puis appelle dans l'ordre les 10 fonctions de
# exercise/tools.sh et de solution/tools.sh, et verifie chaque sortie. Voir ENONCE.md.
set -uo pipefail
cd "$(dirname "$0")"

SEP=':'
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=';' ;; esac
JAVA_HOME_DETECTED=$(java -XshowSettings:properties -version 2>&1 | awk -F'= ' '/ *java.home/ {print $2}' | tr -d '\r')
JMODS="$JAVA_HOME_DETECTED/jmods"

check() {  # check <libelle> <code retour de la condition>
    if [ "$2" = "0" ]; then echo "[PASS] $1"; else echo "[FAIL] $1"; failed=1; fi
}

run_variant() {
    local variant="$1"
    B="build/$variant"
    failed=0
    echo "=== $variant ==="
    rm -rf "$B"
    javac -d "$B/mods" --module-source-path src $(find src -name "*.java")
    # shellcheck source=/dev/null
    . "$variant/tools.sh"

    local out
    out=$(describe_app 2>&1 | tr -d '\r')
    echo "$out" | grep -q "requires greeting.api" && echo "$out" | grep -q "requires java.base mandated"
    check "TODO 1  describe_app : requires greeting.api + java.base mandated" $?
    package_api > /dev/null 2>&1
    [ -f "$B/jars/greeting.api.jar" ]; check "TODO 2  package_api : $B/jars/greeting.api.jar existe" $?
    out=$(describe_jar 2>&1 | tr -d '\r')
    echo "$out" | grep -q "greeting.api@1.0" && echo "$out" | grep -q "exports com.example.greeting.api"
    check "TODO 3  describe_jar : greeting.api@1.0, exports com.example.greeting.api" $?
    package_app > /dev/null 2>&1
    [ -f "$B/jars/greeting.app.jar" ] && jar --describe-module --file "$B/jars/greeting.app.jar" 2>&1 | grep -q "main-class com.example.greeting.app.Main"
    check "TODO 4  package_app : classe principale inscrite dans le jar" $?
    out=$(run_from_jars 2>&1 | tr -d '\r')
    [ "$out" = "Bonjour, Steve !" ]; check "TODO 5  run_from_jars -> '$out'" $?
    out=$(deps 2>&1 | tr -d '\r')
    echo "$out" | grep -q "greeting.app -> greeting.api" && echo "$out" | grep -qE "java\.(io|lang)"
    check "TODO 6  deps : greeting.app -> greeting.api, et les packages du JDK" $?
    build_runtime > /dev/null 2>&1
    [ -d "$B/custom-runtime/bin" ]; check "TODO 7  build_runtime : $B/custom-runtime construit" $?
    out=$(run_runtime 2>&1 | tr -d '\r')
    [ "$out" = "Bonjour, Steve !" ]; check "TODO 8  run_runtime -> '$out'" $?
    out=$(show_resolution 2>&1 | tr -d '\r')
    echo "$out" | grep -q "greeting.app requires greeting.api" && echo "$out" | grep -q "Bonjour, Steve !"
    check "TODO 9  show_resolution : 'greeting.app requires greeting.api' puis le programme" $?
    out=$(runtime_modules 2>&1 | tr -d '\r')
    local count
    count=$(echo "$out" | grep -c "@\|^greeting\|^java")
    echo "$out" | grep -q "^greeting.app" && echo "$out" | grep -q "^java.base" && [ "$count" -le 5 ]
    check "TODO 10 runtime_modules : java.base + nos 2 modules seulement ($count lignes)" $?
    if [ -d "$B/custom-runtime" ]; then
        echo "        taille de l'image : $(du -sk "$B/custom-runtime" | cut -f1) Ko ; JDK complet : $(du -sk "$JAVA_HOME_DETECTED" | cut -f1) Ko"
    fi
    return $failed
}

run_variant exercise
exercise_status=$?
echo
run_variant solution
solution_status=$?

echo
if [ $exercise_status -eq 0 ]; then
    echo "--- Exercice resolu : bravo ! ---"
else
    echo "--- Exercice pas encore resolu (normal au depart) : voir ENONCE.md ---"
fi
if [ $solution_status -ne 0 ]; then
    echo "--- ATTENTION : la solution de reference a echoue elle aussi, signale ce bug ---"
fi
