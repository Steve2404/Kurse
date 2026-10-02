#!/bin/bash
# SOLUTION du script du projet 2. Check le lance depuis la RACINE du depot.
set -e

P=ch12_modules/p02_pricing/solution
OUT=build/ch12/p02_pricing/solution
rm -rf "$OUT"

# 1. Tout compiler : les fournisseurs ne sont requis par PERSONNE, il faut donc les nommer dans -m.
javac -d "$OUT/mods" --module-source-path "$P/src" -m shop.app,pricing.basic,pricing.premium

# 2. Avec tous les modules observables : ServiceLoader trouve les 3 regles.
echo "--- tous les fournisseurs"
java -p "$OUT/mods" -m shop.app/shop.app.Main

# 3. --limit-modules restreint les modules OBSERVABLES : sans pricing.premium, plus de coupon.
echo "--- sans pricing.premium"
java -p "$OUT/mods" --limit-modules shop.app,pricing.basic -m shop.app/shop.app.Main

# 4. Le descripteur d'un fournisseur : provides ... with, et ses paquets NON exportes (contains).
echo "--- describe-module pricing.premium"
java -p "$OUT/mods" --describe-module pricing.premium | sed 's/ file:.*//' | sort

# 5. La resolution : "binds" = un fournisseur lie a un uses (on ne garde que NOS modules, sans chemin).
echo "--- resolution"
java -p "$OUT/mods" --show-module-resolution -m shop.app/shop.app.Main | grep -E "^(pricing|shop)[^ ]* binds " | sed 's/ file:.*//' | sort
