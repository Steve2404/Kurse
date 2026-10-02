#!/bin/bash
# SOLUTION du script du projet 4. Check le lance depuis la RACINE du depot : les chemins partent de la.
set -e

SRC=src/main/java
P=ch1_buildingblocks/projects/p04_bookshop/solution
MAIN=ch1_buildingblocks.projects.p04_bookshop.solution.app.Main
OUT=build/ch1-p04
rm -rf "$OUT"

# 1. Compiler les 4 classes de l'application ; -d range les .class dans des dossiers qui suivent les paquets.
javac -d "$OUT/classes" "$SRC/$P/app/Main.java" "$SRC/$P/model/Author.java" "$SRC/$P/model/Book.java" "$SRC/$P/export/Book.java"

# 2. Lancer depuis le dossier de classes : -cp dit OU chercher ; on donne le nom COMPLET de la classe, sans .class.
java -cp "$OUT/classes" "$MAIN" Hyperion Dan Simmons 1989

# 3. Empaqueter les classes dans un jar (-C : se placer dans ce dossier avant d'ajouter "."), puis lancer depuis le jar.
jar --create --file "$OUT/bookshop.jar" -C "$OUT/classes" .
echo "classes dans le jar : $(jar --list --file "$OUT/bookshop.jar" | grep -c '\.class$')"
java -cp "$OUT/bookshop.jar" "$MAIN" Fondation Isaac Asimov 1951

# 4. Un jar EXECUTABLE : --main-class ecrit le point d'entree dans le manifeste ; java -jar n'a plus besoin du nom de classe.
jar --create --file "$OUT/bookshop-app.jar" --main-class "$MAIN" -C "$OUT/classes" .
java -jar "$OUT/bookshop-app.jar" "Le Hobbit" John Tolkien 1937

# 5. Programme d'un seul fichier source : java compile en memoire et lance, sans javac ni .class.
java "$SRC/$P/tools/Hello.java" Lea
