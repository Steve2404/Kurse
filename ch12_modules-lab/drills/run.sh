#!/usr/bin/env bash
# Verifie les drills du chapitre 12.
#   ./run.sh              -> tes fichiers (dossiers exercise/)
#   ./run.sh solution     -> les corriges (dossiers solution/)
#   ./run.sh exercise 02  -> un seul drill
# Voir REVISION.md et le README.md de chaque drill.
set -uo pipefail
cd "$(dirname "$0")"

VARIANT="${1:-exercise}"
ONLY="${2:-}"
SEP=':'
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=';' ;; esac
JAVA_HOME_DETECTED=$(java -XshowSettings:properties -version 2>&1 | awk -F'= ' '/ *java.home/ {print $2}' | tr -d '\r')
JMODS="$JAVA_HOME_DETECTED/jmods"
passed=0
total=0

check() {  # check <libelle> <code retour de la condition>
    total=$((total + 1))
    if [ "$2" = "0" ]; then echo "[PASS] $1"; passed=$((passed + 1)); else echo "[FAIL] $1"; fi
}

# Assemble la bibliotheque commune + les module-info (et le Main) d'un dossier, dans $2.
assemble() {
    rm -rf "$2"
    mkdir -p "$2"
    cp -r library/. "$2/"
    cp -r "$1"/library.* "$2/"
}

drill01() {
    echo "=== Drill01 - Directives de module-info ($VARIANT) ==="
    local dir scenario B out expected
    for dir in Drill01_Directives/"$VARIANT"/*/; do
        scenario=$(basename "$dir")
        B="build/$VARIANT/d01/$scenario"
        assemble "$dir" "$B/src"
        expected=$(tr -d '\r' < "$dir/expected.txt")
        if javac -d "$B/mods" --module-source-path "$B/src" $(find "$B/src" -name "*.java") > "$B/compile.log" 2>&1; then
            out=$(java -p "$B/mods" -m library.app/com.example.library.app.Main 2>&1 | tr -d '\r' | head -1)
        else
            out="compilation : $(grep -m1 'error' "$B/compile.log" | sed 's/.*error: //')"
        fi
        local ok=1
        [ "$out" = "$expected" ] && ok=0
        # Deux TODO se verifient aussi dans le descripteur du module (le simple "ca marche" ne suffit pas).
        if [ "$ok" = "0" ] && [ "$scenario" = "04_exports_to" ]; then
            java -p "$B/mods" --describe-module library.service 2>&1 | grep -q "qualified exports com.example.library.service.internal to library.app" || { ok=1; out="$out (mais l'export n'est pas QUALIFIE)"; }
        fi
        if [ "$ok" = "0" ] && [ "$scenario" = "06_open_module" ]; then
            java -p "$B/mods" --describe-module library.model 2>&1 | head -1 | grep -q " open$" || { ok=1; out="$out (mais le MODULE entier n'est pas open)"; }
        fi
        check "TODO ${scenario:0:2} ${scenario:3} -> $out" "$ok"
    done
}

drill02() {
    echo "=== Drill02 - Commandes ($VARIANT) ==="
    B="build/$VARIANT/d02"
    SRC="$B/src"
    LEGACY="Drill02_Commands/legacy"
    rm -rf "$B"
    assemble Drill02_Commands/src "$SRC"
    # shellcheck source=/dev/null
    . "Drill02_Commands/$VARIANT/commands.sh"
    javac -d "$B/legacy" "$LEGACY/Legacy.java" > /dev/null 2>&1   # donne : la classe qui utilise sun.misc.Unsafe
    local out
    compile_all > "$B.compile.log" 2>&1
    [ -f "$B/mods/library.app/module-info.class" ]; check "TODO 1  compile_all : $B/mods construit" $?
    out=$(run_short 2>&1 | tr -d '\r' | head -1)
    [ "$out" = "3 livres, le plus ancien : Fondation" ]; check "TODO 2  run_short -> '$out'" $?
    out=$(describe_short 2>&1 | tr -d '\r')
    echo "$out" | grep -q "exports com.example.library.model"; check "TODO 3  describe_short : exports com.example.library.model" $?
    out=$(list_ours 2>&1 | tr -d '\r')
    [ "$(echo "$out" | grep -c '^library\.')" = "3" ]; check "TODO 4  list_ours : les 3 modules library.* (et eux seuls)" $?
    jar_all > /dev/null 2>&1
    [ -f "$B/jars/library.model.jar" ] && [ -f "$B/jars/library.service.jar" ] && [ -f "$B/jars/library.app.jar" ]
    check "TODO 5  jar_all : 3 jars dans $B/jars" $?
    out=$(run_jar 2>&1 | tr -d '\r' | head -1)
    [ "$out" = "3 livres, le plus ancien : Fondation" ]; check "TODO 6  run_jar -> '$out'" $?
    out=$(jar_describe 2>&1 | tr -d '\r')
    echo "$out" | grep -q "provides com.example.library.service.Catalog with com.example.library.service.internal.InMemoryCatalog"
    check "TODO 7  jar_describe : provides ... with ..." $?
    out=$(jdeps_summary 2>&1 | tr -d '\r')
    echo "$out" | grep -q "library.app -> library.service" && ! echo "$out" | grep -q "java.util"
    check "TODO 8  jdeps_summary : resume par module (library.app -> library.service), pas de detail par package" $?
    out=$(jdeps_internals 2>&1 | tr -d '\r')
    echo "$out" | grep -q "sun.misc.Unsafe"; check "TODO 9  jdeps_internals : signale sun.misc.Unsafe" $?
    out=$(resolution 2>&1 | tr -d '\r')
    echo "$out" | grep -q "library.app requires library.service"; check "TODO 10 resolution : 'library.app requires library.service'" $?
}

drill03() {
    echo "=== Drill03 - Kata melange ($VARIANT) ==="
    B="build/$VARIANT/d03"
    assemble "Drill03_MixedKata/$VARIANT" "$B/src"
    # shellcheck source=/dev/null
    . "Drill03_MixedKata/$VARIANT/kata.sh"
    local out
    if javac -d "$B/mods" --module-source-path "$B/src" $(find "$B/src" -name "*.java") > "$B/compile.log" 2>&1; then
        out=$(run_kata 2>&1 | tr -d '\r')
    else
        out="compilation : $(grep -m1 'error' "$B/compile.log" | sed 's/.*error: //')"
    fi
    local expected
    expected=$'Catalogue : 3 livres\nPremier titre : Dune\nAuteur lu par reflexion : Herbert'
    local ok=1
    [ "$out" = "$expected" ] && ok=0   # le statut d'abord : un $(...) dans le libelle ecraserait $?
    check "TODO 1-4 kata : les 3 module-info + la commande -> $(echo "$out" | head -1)" "$ok"
    [ "$out" = "$expected" ] || echo "$out" | sed 's/^/        /' | head -4
}

mkdir -p build
case "$ONLY" in
    01) drill01 ;;
    02) drill02 ;;
    03) drill03 ;;
    *) drill01; echo; drill02; echo; drill03 ;;
esac
echo
echo "--- Resultat : $passed/$total TODO reussis ($VARIANT) ---"
