#!/usr/bin/env bash
# Ce lab est different des autres : "exercise/" ne compilera JAMAIS
# (c'est le point : un cycle de modules est refuse par construction).
# "solution/" montre UNE FACON de resoudre le probleme en cassant le
# cycle. Voir ENONCE.md.
set -uo pipefail
cd "$(dirname "$0")"

echo "=== exercise (doit ECHOUER - cycle attendu) ==="
rm -rf exercise/mods
sources=$(find exercise/src -name "*.java")
if javac -d exercise/mods --module-source-path exercise/src $sources 2> exercise/compile.log; then
    echo "[FAIL] la compilation a REUSSI alors qu'un cycle est present - inattendu !"
    exercise_status=1
else
    if grep -q "cyclic dependence" exercise/compile.log; then
        echo "[PASS - attendu] cycle bien detecte et refuse par javac :"
        cat exercise/compile.log
        exercise_status=0
    else
        echo "[FAIL] echec de compilation, mais pas pour la raison attendue :"
        cat exercise/compile.log
        exercise_status=1
    fi
fi

echo
echo "=== solution (doit REUSSIR - cycle casse via orders.common) ==="
rm -rf solution/mods
sources=$(find solution/src -name "*.java")
if ! javac -d solution/mods --module-source-path solution/src $sources 2> solution/compile.log; then
    echo "[FAIL] la solution de reference ne compile pas :"
    cat solution/compile.log
    solution_status=1
else
    output=$(java --module-path solution/mods --module orders.app/com.example.orders.app.Main 2>&1)
    expected=$'Commande 42 : en cours de traitement\nCommande 42 : etiquette prete'
    if [ "$output" = "$expected" ]; then
        echo "[PASS] $output"
        solution_status=0
    else
        echo "[FAIL] Sortie inattendue : $output"
        solution_status=1
    fi
fi

echo
if [ $exercise_status -eq 0 ] && [ $solution_status -eq 0 ]; then
    echo "--- Les 2 parties se comportent comme attendu : le cycle est bien refuse, et la solution le contourne. ---"
fi
