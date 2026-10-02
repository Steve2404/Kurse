#!/bin/bash
# SOLUTION du script du projet 3. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/p03_config/solution
OUT=build/ch12/p03_config/solution
rm -rf "$OUT"

# 1. Sans option : config.app utilise un paquet NON exporte -> erreur de compilation.
#    On nomme TOUS les modules dans -m : javac ne compile sinon que les classes atteignables (Vault ne le serait pas).
echo "--- javac sans --add-exports"
javac -d "$OUT/mods" --module-source-path "$P/src" -m config.app,config.model,config.legacy,config.binder 2>&1 | grep -E "error:|declared in" | sed 's/.*error:/error:/; s/^ *//' || true

# 2. --add-exports module/paquet=module-cible ouvre l'acces a la COMPILATION.
javac -d "$OUT/mods" --module-source-path "$P/src" --add-exports config.model/config.model.internal=config.app -m config.app,config.model,config.legacy,config.binder

# 3. A l'execution, il faut le redire : sinon IllegalAccessError ; et le coffre est refuse (paquet non ouvert).
echo "--- java sans option"
java -p "$OUT/mods" -m config.app/config.app.Main

# 4. --add-exports a l'execution, et --add-opens pour la reflexion profonde sur le coffre.
echo "--- java avec --add-exports et --add-opens"
java -p "$OUT/mods" --add-exports config.model/config.model.internal=config.app --add-opens config.model/config.model.secret=config.binder \
    -m config.app/config.app.Main | tail -3

# 5. Le descripteur d'un module ouvert, et celui d'un opens qualifie.
echo "--- describe-module"
java -p "$OUT/mods" --describe-module config.legacy | sed 's/ file:.*//' | sort
java -p "$OUT/mods" --describe-module config.model | sed 's/ file:.*//' | sort
