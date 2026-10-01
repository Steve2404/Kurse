# Lab 11 - corrige (lu par ../run.sh).

# Le classpath ne "requiert" rien : --add-modules ajoute core comme racine du graphe, sinon
# javac dit "package com.example.core does not exist" (le module n'est pas resolu).
BOTTOM_UP_FLAGS="--add-modules core"
