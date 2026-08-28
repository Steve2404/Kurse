#!/usr/bin/env bash
# Verifie REELLEMENT, sur ta propre machine et ta propre version du
# JDK, les comptes approximatifs de modules donnes par le livre
# (Question 4 de ENONCE.md). Aucune compilation ici, juste
# "java --list-modules" analyse.
set -uo pipefail

java_count=$(java --list-modules | grep -c "^java\.")
jdk_count=$(java --list-modules | grep -c "^jdk\.")
total_count=$(java --list-modules | wc -l | tr -d ' ')
java_base_present=$(java --list-modules | grep -c "^java\.base")

echo "Modules commencant par 'java.*' : $java_count (le livre dit \"about 20\")"
echo "Modules commencant par 'jdk.*'  : $jdk_count (le livre dit \"about 30\")"
echo "Total de modules listes         : $total_count"
echo

if [ "$java_base_present" -eq 1 ]; then
    echo "[PASS] java.base est bien present (toujours implicite dans tout module)"
else
    echo "[FAIL] java.base est introuvable - verifie ton installation Java"
    exit 1
fi

if [ "$java_count" -ge 15 ] && [ "$java_count" -le 30 ]; then
    echo "[PASS] le compte de java.* est dans l'ordre de grandeur du livre (~20)"
else
    echo "[INFO] le compte de java.* ($java_count) s'ecarte de l'estimation du livre - normal si ta version du JDK est tres differente de celle du livre"
fi

if [ "$jdk_count" -ge 20 ]; then
    echo "[PASS] le compte de jdk.* est au moins dans l'ordre de grandeur du livre (~30, souvent plus sur JDK recents)"
else
    echo "[INFO] le compte de jdk.* ($jdk_count) est plus bas que l'estimation du livre - verifie ta version du JDK"
fi
