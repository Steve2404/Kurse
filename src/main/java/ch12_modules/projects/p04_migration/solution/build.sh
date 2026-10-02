#!/bin/bash
# SOLUTION du script du projet 4. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/p04_migration/solution
OUT=build/ch12/p04_migration/solution
rm -rf "$OUT"

# 1. Les vieilles bibliotheques : compilees "a l'ancienne" (pas de module-info), puis mises en jars.
javac -d "$OUT/legacy" $(find "$P/legacy" -name "*.java")
mkdir -p "$OUT/jars"
jar --create --file "$OUT/jars/acme-text-2.1.jar" -C "$OUT/legacy" com/acme/text
jar --create --file "$OUT/jars/old_utils.jar" --manifest "$P/manifest.txt" -C "$OUT/legacy" com/acme/utils

# 2. Le nom de module que Java DEDUIT pour chaque jar sans descripteur.
#    -J passe une option a la JVM de l'outil : on force l'anglais (sinon jar repond dans la langue de la machine).
echo "--- describe-module des jars"
jar -J-Duser.language=en --describe-module --file "$OUT/jars/acme-text-2.1.jar" | sed 's/ jar:.*//' | sort
jar -J-Duser.language=en --describe-module --file "$OUT/jars/old_utils.jar" | sed 's/ jar:.*//' | sort

# 3. Sur le CLASSPATH, tout le code va dans le module sans nom.
echo "--- classpath"
java -cp "$OUT/legacy" com.acme.demo.Demo

# 4. Sans -p, le module nomme ne trouve pas ses dependances.
echo "--- javac sans module path"
javac -d "$OUT/mods" --module-source-path "$P/src" -m blog.app 2>&1 | grep "error:" | sed 's/.*error:/error:/' || true

# 5. Avec les jars sur le MODULE PATH : ils deviennent des modules automatiques.
javac -d "$OUT/mods" -p "$OUT/jars" --module-source-path "$P/src" -m blog.app
jar --create --file "$OUT/jars/blog.app.jar" --main-class blog.app.Main -C "$OUT/mods/blog.app" .
echo "--- module path"
java -p "$OUT/jars" -m blog.app

# 6. Un cycle de requires est interdit.
echo "--- cycle"
javac -d "$OUT/cycles" --module-source-path "$P/cycles" -m cycle.a,cycle.b 2>&1 | grep "error:" | sed 's/.*error:/error:/' | sort || true

# 7. jdeps : de quoi depend un jar (resume -s). ATTENTION : pour jdeps, -p veut dire --package ; on ecrit --module-path.
echo "--- jdeps"
jdeps -s "$OUT/jars/acme-text-2.1.jar"
jdeps -s --module-path "$OUT/jars" -m blog.app
