#!/usr/bin/env bash
# Construit la meme bibliotheque sous 3 formes (classpath, automatic, named), puis appelle
# les fonctions de exercise/launch.sh et de solution/launch.sh. Voir ENONCE.md.
set -uo pipefail
cd "$(dirname "$0")"

# Separateur de chemins : ':' sous Linux/Mac, ';' sous Windows (Git Bash lance le java Windows).
SEP=':'
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=';' ;; esac

check() {  # check <libelle> <condition vraie ?>
    if [ "$2" = "0" ]; then echo "[PASS] $1"; else echo "[FAIL] $1"; failed=1; fi
}

run_variant() {
    local variant="$1"
    B="build/$variant"
    failed=0
    echo "=== $variant ==="
    rm -rf "$B"
    mkdir -p "$B/jars" "$B/modpath-auto" "$B/modpath-named" "$B/lib-named/mathutils"

    # La bibliotheque SANS module-info, puis AVEC le module-info de la variante (TODO 1).
    javac -d "$B/classes-unnamed" lib-src/com/example/mathutils/Calc.java
    cp -r lib-src/com "$B/lib-named/mathutils/"
    cp "$variant/mathutils-module-info.java" "$B/lib-named/mathutils/module-info.java"
    javac -d "$B/classes-named" --module-source-path "$B/lib-named" $(find "$B/lib-named" -name "*.java") 2> "$B/named.log"
    jar --create --file "$B/jars/mathutils-1.0.jar" -C "$B/classes-unnamed" .
    jar --create --file "$B/jars/string-tools-2.3.1.jar" -C "$B/classes-unnamed" .
    cp "$B/jars/mathutils-1.0.jar" "$B/modpath-auto/"
    [ -d "$B/classes-named/mathutils" ] && jar --create --file "$B/modpath-named/mathutils-1.0.jar" -C "$B/classes-named/mathutils" .

    # Les applications, compilees contre chaque forme.
    javac -cp "$B/jars/mathutils-1.0.jar" -d "$B/classpath-app-out" classpath-app/AppMain.java
    javac -d "$B/app-auto" --module-path "$B/modpath-auto" --module-source-path modpath-app-src $(find modpath-app-src -name "*.java")
    javac -d "$B/app-named" --module-path "$B/modpath-named" --module-source-path modpath-app-src $(find modpath-app-src -name "*.java") 2>> "$B/named.log"

    # shellcheck source=/dev/null
    . "$variant/launch.sh"

    local named_ok=1
    grep -q "error" "$B/named.log" || named_ok=0
    check "TODO 1 : le module nomme mathutils compile et est utilisable par app" "$named_ok"
    [ "$named_ok" = "0" ] || sed 's/^/        /' "$B/named.log" | head -4

    local out
    out=$(run_unnamed 2>&1 | tr -d '\r')
    [ "$out" = "Resultat (classpath) : 36" ]; check "TODO 2 : unnamed (classpath) -> '$out'" $?
    out=$(run_automatic 2>&1 | tr -d '\r')
    [ "$out" = "Resultat (module) : 36" ]; check "TODO 3 : automatic -> '$out'" $?
    java --module-path "$B/modpath-auto" --list-modules 2>&1 | grep -q "mathutils.*automatic"
    check "         (java --list-modules etiquette bien mathutils 'automatic')" $?
    out=$(run_named 2>&1 | tr -d '\r')
    [ "$out" = "Resultat (module) : 36" ]; check "TODO 4 : named -> '$out'" $?

    local derived
    derived=$(jar --describe-module --file "$B/jars/string-tools-2.3.1.jar" 2>&1 | tr -d '\r' | grep automatic | sed 's/@.*//')
    [ "$AUTOMATIC_NAME" = "$derived" ]; check "TODO 5 : nom automatique de string-tools-2.3.1.jar -> '$AUTOMATIC_NAME' (jar dit '$derived')" $?
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
