#!/usr/bin/env bash
# Compile et execute la partie EXERCICE, puis la partie SOLUTION, pour
# comparer. Voir ENONCE.md avant de lancer ce script.
set -uo pipefail
cd "$(dirname "$0")"

EXPECTED_OUTPUT="Bonjour, Steve !"

run_variant() {
    local variant="$1"
    echo "=== $variant ==="
    rm -rf "$variant/mods"
    local sources
    sources=$(find "$variant/src" -name "*.java")
    if ! javac -d "$variant/mods" --module-source-path "$variant/src" $sources 2> "$variant/compile.log"; then
        echo "[COMPILATION ECHOUEE]"
        cat "$variant/compile.log"
        return 1
    fi
    local output
    output=$(java --module-path "$variant/mods" --module greeting.app/com.example.greeting.app.Main 2>&1)
    if [ "$output" = "$EXPECTED_OUTPUT" ]; then
        echo "[PASS] Sortie == '$EXPECTED_OUTPUT'"
        return 0
    else
        echo "[FAIL] Sortie inattendue : '$output'"
        return 1
    fi
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
    echo "--- Exercice pas encore resolu (normal au depart) : lis le message de compilation ci-dessus et ENONCE.md ---"
fi
if [ $solution_status -ne 0 ]; then
    echo "--- ATTENTION : la solution de reference a echoue elle aussi, signale ce bug ---"
fi
