# Drill02 - corrige (lu par ../../run.sh ; ne pas l'executer seul).
#
# Deja pret : SEP (separateur de chemins), B (dossier de build), SRC (les 3 modules library.* complets, a compiler),
#             $B/legacy (une classe de classpath deja compilee qui utilise sun.misc.Unsafe).
# UNE commande par fonction, avec les FORMES COURTES quand elles existent (-p, -m, -d, -c, -f, -e).

# --module-source-path : un sous-dossier par module ; -d recoit un dossier par module.
compile_all() {
    javac -d "$B/mods" --module-source-path "$SRC" $(find "$SRC" -name "*.java")
}

# -p = --module-path, -m = --module (module/classe).
run_short() {
    java -p "$B/mods" -m library.app/com.example.library.app.Main
}

# -d = --describe-module (attention : pour javac, -d veut dire "dossier de sortie" !).
describe_short() {
    java -p "$B/mods" -d library.model
}

# --list-modules liste le JDK ET le module-path ; grep garde les notres.
list_ours() {
    java -p "$B/mods" --list-modules | grep "^library"
}

# -c creer, -f fichier, -e classe principale (--main-class), -C "se placer dans ce dossier" puis . = tout.
jar_all() {
    mkdir -p "$B/jars"
    jar -c -f "$B/jars/library.model.jar" -C "$B/mods/library.model" .
    jar -c -f "$B/jars/library.service.jar" -C "$B/mods/library.service" .
    jar -c -f "$B/jars/library.app.jar" -e com.example.library.app.Main -C "$B/mods/library.app" .
}

# Sans /Classe : java prend la classe principale inscrite dans le jar par -e.
run_jar() {
    java -p "$B/jars" -m library.app
}

# jar -d = --describe-module ; on y voit requires, exports ET provides ... with.
jar_describe() {
    jar -d -f "$B/jars/library.service.jar"
}

# -s (--summary) : seulement le graphe des modules, sans le detail par package.
jdeps_summary() {
    jdeps -s --module-path "$B/mods" -m library.app
}

# --jdk-internals : les API internes (sun.misc.Unsafe...) et leur remplacement conseille.
jdeps_internals() {
    jdeps --jdk-internals "$B/legacy"
}

# --show-module-resolution : chaque module resolu et qui l'a demande, puis le programme.
resolution() {
    java -p "$B/mods" --show-module-resolution -m library.app/com.example.library.app.Main
}
