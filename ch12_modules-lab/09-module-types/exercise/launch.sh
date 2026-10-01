# Lab 09 - A COMPLETER (lu par ../run.sh, ne pas l'executer seul).
#
# Deja pret quand ces fonctions sont appelees (on est dans le dossier du lab) :
#   SEP  separateur de chemins : ':' sous Linux/Mac, ';' sous Windows
#   B    le dossier de build de cette variante, qui contient :
#          $B/jars/mathutils-1.0.jar    la bibliotheque SANS module-info
#          $B/classpath-app-out/        AppMain compile (classe SANS package, SANS module)
#          $B/modpath-auto/             le jar SANS module-info, seul dans son dossier
#          $B/app-auto/                 le module app compile contre ce jar
#          $B/modpath-named/            le jar AVEC module-info (TODO 1), seul dans son dossier
#          $B/app-named/                le module app compile contre lui
#
# Chaque fonction doit LANCER le programme (une seule commande java).

# TODO 2 : lancer AppMain en mettant le jar ET $B/classpath-app-out sur le CLASSPATH (unnamed module).
run_unnamed() {
    echo "TODO 2 : run_unnamed"
}

# TODO 3 : lancer le module app (classe principale com.example.app.Main) avec $B/modpath-auto
#          ET $B/app-auto sur le MODULE-PATH (le jar devient un automatic module).
run_automatic() {
    echo "TODO 3 : run_automatic"
}

# TODO 4 : pareil avec $B/modpath-named et $B/app-named (named module).
run_named() {
    echo "TODO 4 : run_named"
}

# TODO 5 : le nom de module automatique que Java donne a un jar nomme string-tools-2.3.1.jar
AUTOMATIC_NAME="TODO 5"
