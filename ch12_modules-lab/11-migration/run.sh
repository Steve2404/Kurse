#!/usr/bin/env bash
# Migre le petit projet core <- text-utils <- app de 3 facons (bottom-up, top-down, tout nomme)
# avec les fichiers de exercise/ puis de solution/. Voir ENONCE.md.
set -uo pipefail
cd "$(dirname "$0")"

SEP=':'
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=';' ;; esac
EXPECTED="Total : 12,50 EUR"

check() {  # check <libelle> <sortie obtenue> <log a montrer si echec>
    if [ "$2" = "$EXPECTED" ]; then
        echo "[PASS] $1 -> '$2'"
    else
        echo "[FAIL] $1 -> '$2'"
        [ -f "$3" ] && grep -h "error\|Exception" "$3" | head -3 | sed 's/^/        /'
        failed=1
    fi
}

run_variant() {
    local variant="$1"
    local B="build/$variant"
    failed=0
    BOTTOM_UP_FLAGS=""
    # shellcheck source=/dev/null
    . "$variant/flags.sh"
    echo "=== $variant ==="
    rm -rf "$B"
    mkdir -p "$B/jars" "$B/cls"

    # Etape 0 (donnee) : le projet d'origine, 3 jars sans module-info, sur le classpath.
    javac -d "$B/cls/core" src/core/com/example/core/Money.java
    javac -cp "$B/cls/core" -d "$B/cls/text" src/text-utils/com/example/text/Formatter.java
    javac -cp "$B/cls/core${SEP}$B/cls/text" -d "$B/cls/app" src/app/com/example/app/Main.java
    jar --create --file "$B/jars/core-1.0.jar" -C "$B/cls/core" .
    jar --create --file "$B/jars/text-utils-2.1.jar" -C "$B/cls/text" .
    jar --create --file "$B/jars/app-3.0.jar" -C "$B/cls/app" .
    local out
    out=$(java -cp "$B/jars/core-1.0.jar${SEP}$B/jars/text-utils-2.1.jar${SEP}$B/jars/app-3.0.jar" com.example.app.Main 2>&1 | tr -d '\r')
    [ "$out" = "$EXPECTED" ] && echo "[INFO] depart : tout sur le classpath -> '$out'"

    # Etape 1 - bottom-up (TODO 1 et 2) : core devient un module nomme, le reste reste sur le classpath.
    local U="$B/bottom-up"
    mkdir -p "$U/src/core" "$U/mp"
    cp -r src/core/com "$U/src/core/"
    cp "$variant/core-module-info.java" "$U/src/core/module-info.java"
    # shellcheck disable=SC2086
    {
        javac -d "$U/mods" --module-source-path "$U/src" $(find "$U/src" -name "*.java") \
        && jar --create --file "$U/mp/core.jar" -C "$U/mods/core" . \
        && javac -p "$U/mp" $BOTTOM_UP_FLAGS -d "$U/text" src/text-utils/com/example/text/Formatter.java \
        && javac -p "$U/mp" $BOTTOM_UP_FLAGS -cp "$U/text" -d "$U/app" src/app/com/example/app/Main.java
    } > "$U.log" 2>&1
    # shellcheck disable=SC2086
    out=$(java -p "$U/mp" $BOTTOM_UP_FLAGS -cp "$U/text${SEP}$U/app" com.example.app.Main 2>&1 | tr -d '\r')
    check "TODO 1-2 bottom-up (core nomme, le reste sur le classpath)" "$out" "$U.log"

    # Etape 2 - top-down (TODO 3) : app devient un module nomme, les 2 jars deviennent des modules automatiques.
    local D="$B/top-down"
    mkdir -p "$D/src/app" "$D/mp"
    cp -r src/app/com "$D/src/app/"
    cp "$variant/app-module-info.java" "$D/src/app/module-info.java"
    cp "$B/jars/core-1.0.jar" "$B/jars/text-utils-2.1.jar" "$D/mp/"
    javac -p "$D/mp" -d "$D/mods" --module-source-path "$D/src" $(find "$D/src" -name "*.java") > "$D.log" 2>&1
    out=$(java -p "$D/mp${SEP}$D/mods" -m app/com.example.app.Main 2>&1 | tr -d '\r')
    check "TODO 3   top-down (app nomme, core et text-utils automatiques)" "$out" "$D.log"

    # Etape 3 - fin de migration (TODO 4, avec 1 et 3) : les 3 modules sont nommes.
    local F="$B/full"
    mkdir -p "$F/src/core" "$F/src/text.utils" "$F/src/app"
    cp -r src/core/com "$F/src/core/" && cp "$variant/core-module-info.java" "$F/src/core/module-info.java"
    cp -r src/text-utils/com "$F/src/text.utils/" && cp "$variant/text-utils-module-info.java" "$F/src/text.utils/module-info.java"
    cp -r src/app/com "$F/src/app/" && cp "$variant/app-module-info.java" "$F/src/app/module-info.java"
    javac -d "$F/mods" --module-source-path "$F/src" $(find "$F/src" -name "*.java") > "$F.log" 2>&1
    out=$(java -p "$F/mods" -m app/com.example.app.Main 2>&1 | tr -d '\r')
    check "TODO 4   tout est nomme (core, text.utils, app)" "$out" "$F.log"
    return $failed
}

run_variant exercise
exercise_status=$?
echo
run_variant solution
solution_status=$?

echo
if [ $exercise_status -eq 0 ]; then
    echo "--- Exercice resolu : bravo, la migration est complete ! ---"
else
    echo "--- Exercice pas encore resolu (normal au depart) : voir ENONCE.md ---"
fi
if [ $solution_status -ne 0 ]; then
    echo "--- ATTENTION : la solution de reference a echoue elle aussi, signale ce bug ---"
fi
