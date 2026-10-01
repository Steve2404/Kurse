#!/usr/bin/env bash
# Compile les 3 modules de src/ avec les options de exercise/flags.sh (puis solution/flags.sh),
# lance vault.inspector et verifie les 3 lignes. Voir ENONCE.md.
set -uo pipefail
cd "$(dirname "$0")"

EXPECTED=$'indice : 12**\ncode : 1234\naudit OK'

run_variant() {
    local variant="$1"
    local B="build/$variant"
    COMPILE_FLAGS="" RUN_EXPORTS="" RUN_OPENS="" RUN_ADD_MODULES=""
    # shellcheck source=/dev/null
    . "$variant/flags.sh"
    echo "=== $variant ==="
    rm -rf "$B"
    mkdir -p "$B"
    # shellcheck disable=SC2086
    if ! javac -d "$B/mods" --module-source-path src $COMPILE_FLAGS $(find src -name "*.java") 2> "$B/compile.log"; then
        echo "[COMPILATION ECHOUEE] -> TODO 1"
        grep "error" "$B/compile.log" | head -3
        return 1
    fi
    local output
    # shellcheck disable=SC2086
    output=$(java --module-path "$B/mods" $RUN_EXPORTS $RUN_OPENS $RUN_ADD_MODULES \
             --module vault.inspector/com.example.vault.inspector.Main 2>&1 | tr -d '\r')
    if [ "$output" = "$EXPECTED" ]; then
        echo "[PASS] Les 3 acces forces fonctionnent :"
        echo "$output"
        return 0
    fi
    echo "[FAIL] Sortie obtenue :"
    echo "$output" | grep -v "^\s*at " | head -4
    if echo "$output" | grep -q "IllegalAccessError"; then
        echo "  -> indice : TODO 2 (a l'execution, le package internal n'est pas exporte)"
    elif echo "$output" | grep -q "InaccessibleObjectException"; then
        echo "  -> indice : TODO 3 (reflexion profonde refusee : package non ouvert)"
    elif echo "$output" | grep -q "ClassNotFoundException"; then
        echo "  -> indice : TODO 4 (le module vault.audit n'a pas ete charge)"
    fi
    return 1
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
