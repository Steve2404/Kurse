#!/bin/bash
# SOLUTION du script du projet 6 (capstone). Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/p06_events/solution
OUT=build/ch12/p06_events/solution
rm -rf "$OUT"
mkdir -p "$OUT/jars"

# 1. La bibliotheque heritee : compilee sans module, empaquetee avec un nom de fichier versionne.
javac -d "$OUT/legacy" $(find "$P/legacy" -name "*.java")
jar --create --file "$OUT/jars/geo-tools-1.0.jar" -C "$OUT/legacy" .

# 2. Les modules : le jar herite est sur le module path (-p) ; on nomme l'application ET les fournisseurs.
javac -d "$OUT/mods" -p "$OUT/jars" --module-source-path "$P/src" -m events.app,events.email,events.sms

# 3. Un jar par module, tous dans le meme dossier que le jar herite.
for m in events.model events.api events.core events.email events.sms events.audit; do
    jar --create --file "$OUT/jars/$m.jar" -C "$OUT/mods/$m" .
done
jar --create --file "$OUT/jars/events.app.jar" --main-class events.app.Main -C "$OUT/mods/events.app" .

echo "--- complet"
java -p "$OUT/jars" -m events.app

echo "--- sans le fournisseur sms"
java -p "$OUT/jars" --limit-modules events.app,events.email -m events.app | sed -n '2,4p'

echo "--- describe-module events.core"
jar --describe-module --file "$OUT/jars/events.core.jar" | sed 's/ jar:.*//' | sort

echo "--- jdeps"
jdeps -s -R --module-path "$OUT/jars" -m events.app | grep -v "^java\."

# 4. jlink refuse les modules AUTOMATIQUES : on n'affiche que la ligne d'erreur.
echo "--- jlink"
jlink --module-path "$OUT/jars" --add-modules events.app --output "$OUT/image" 2>&1 | grep -i "automatic" | sed 's/ from file:.*//' || true
