#!/usr/bin/env bash
# Compile et execute la partie EXERCICE, puis la partie SOLUTION. Voir ENONCE.md.
set -uo pipefail
cd "$(dirname "$0")"

EXPECTED=$'Fournisseurs trouves : 2\nFournisseurs : [ExpressRate, PostRate]\n2 kg : Poste (7.0)\n20 kg : Express (19.0)'

run_variant() {
    local variant="$1"
    local B="build/$variant"
    echo "=== $variant ==="
    rm -rf "$B"
    if ! javac -d "$B/mods" --module-source-path "$variant/src" $(find "$variant/src" -name "*.java") 2> "$B.compile.log"; then
        echo "[COMPILATION ECHOUEE]"
        cat "$B.compile.log"
        return 1
    fi
    local output
    output=$(java --module-path "$B/mods" --module shipping.app/com.example.shipping.app.Main 2>&1 | tr -d '\r')
    if [ "$output" = "$EXPECTED" ]; then
        echo "[PASS] Les 2 fournisseurs sont trouves et le moins cher est choisi :"
        echo "$output"
        return 0
    fi
    echo "[FAIL] Sortie obtenue :"
    echo "$output" | head -6
    if echo "$output" | grep -q "does not declare .uses."; then
        echo "  -> indice : TODO 3 (le consommateur doit declarer qu'il utilise le service)"
    elif echo "$output" | grep -q "TODO 4"; then
        echo "  -> indice : TODO 4 (providerTypes)"
    elif echo "$output" | grep -q "TODO 5"; then
        echo "  -> indice : TODO 5 (cheapest)"
    elif ! echo "$output" | grep -q "PostRate"; then
        echo "  -> indice : TODO 1 (PostRate n'est pas annonce comme fournisseur)"
    elif ! echo "$output" | grep -q "ExpressRate"; then
        echo "  -> indice : TODO 2 (ExpressRate n'est pas annonce comme fournisseur)"
    fi
    return 1
}

mkdir -p build
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
