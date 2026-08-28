#!/usr/bin/env bash
# Compile et execute la partie EXERCICE, puis la partie SOLUTION.
# Voir ENONCE.md avant de lancer ce script. Contrairement aux labs
# precedents, la compilation de l'exercice REUSSIT ici - c'est
# l'execution qui echoue tant que le TODO n'est pas fait.
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
    local output
    output=$(java --module-path "$variant/mods" --module notify.app/com.example.notify.app.Main 2>&1)
    if echo "$output" | grep -q "^Email envoye : Commande #42 expediee$" && echo "$output" | grep -q "^Notifieurs trouves : 1$"; then
        echo "[PASS] Le ServiceLoader a trouve EmailNotifier :"
        echo "$output"
        return 0
    elif echo "$output" | grep -q "does not declare .uses."; then
        echo "[ECHEC A L'EXECUTION - attendu tant que le TODO n'est pas fait] :"
        echo "$output"
        return 1
    else
        echo "[FAIL] Sortie inattendue :"
        echo "$output"
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
    echo "--- Exercice pas encore resolu (normal au depart) : lis l'erreur ci-dessus et ENONCE.md ---"
fi
if [ $solution_status -ne 0 ]; then
    echo "--- ATTENTION : la solution de reference a echoue elle aussi, signale ce bug ---"
fi
