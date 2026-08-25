#!/usr/bin/env bash
# Compile et execute la partie EXERCICE, puis la partie SOLUTION.
# Voir ENONCE.md avant de lancer ce script.
set -uo pipefail
cd "$(dirname "$0")"

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

    local trusted_output untrusted_output ok=0
    trusted_output=$(java --module-path "$variant/mods" --module pricing.trusted/com.example.pricing.trusted.Main 2>&1)
    untrusted_output=$(java --module-path "$variant/mods" --module pricing.untrusted/com.example.pricing.untrusted.Main 2>&1)

    if [ "$trusted_output" = "Prix trusted apres remise : 90.0" ]; then
        echo "[PASS] pricing.trusted -> '$trusted_output'"
    else
        echo "[FAIL] pricing.trusted -> '$trusted_output'"
        ok=1
    fi
    if [ "$untrusted_output" = "Prix untrusted apres remise : 90.0" ]; then
        echo "[PASS] pricing.untrusted -> '$untrusted_output'"
    else
        echo "[FAIL] pricing.untrusted -> '$untrusted_output'"
        ok=1
    fi
    return $ok
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
    echo "--- Exercice pas encore resolu (normal au depart pour pricing.untrusted) : lis le message de compilation et ENONCE.md ---"
fi
if [ $solution_status -ne 0 ]; then
    echo "--- ATTENTION : la solution de reference a echoue elle aussi, signale ce bug ---"
fi
