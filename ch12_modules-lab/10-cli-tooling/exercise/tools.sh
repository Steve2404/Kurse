# Lab 10 - A COMPLETER (lu par ../run.sh, ne pas l'executer seul).
#
# Deja pret quand ces fonctions sont appelees (on est dans le dossier du lab) :
#   SEP    separateur de chemins : ':' sous Linux/Mac, ';' sous Windows
#   B      le dossier de build de cette variante ; $B/mods contient greeting.api et greeting.app DEJA compiles
#   JMODS  le dossier jmods du JDK (les modules du JDK, pour jlink)
#
# Chaque fonction = UNE commande (les fonctions sont appelees dans l'ordre 1 -> 10).

# TODO 1 : decrire le module compile greeting.app (outil : java).
describe_app() {
    echo "TODO 1"
}

# TODO 2 : empaqueter $B/mods/greeting.api dans $B/jars/greeting.api.jar, avec la version de module 1.0.
package_api() {
    echo "TODO 2"
}

# TODO 3 : decrire le module contenu dans $B/jars/greeting.api.jar (outil : jar).
describe_jar() {
    echo "TODO 3"
}

# TODO 4 : empaqueter $B/mods/greeting.app dans $B/jars/greeting.app.jar en y inscrivant la classe
#          principale com.example.greeting.app.Main.
package_app() {
    echo "TODO 4"
}

# TODO 5 : lancer l'application depuis $B/jars en ne donnant QUE le nom du module (pas la classe).
run_from_jars() {
    echo "TODO 5"
}

# TODO 6 : afficher les dependances de greeting.app, jusqu'aux packages du JDK (outil : jdeps).
deps() {
    echo "TODO 6"
}

# TODO 7 : fabriquer dans $B/custom-runtime une image Java qui ne contient que ce dont greeting.app a besoin.
build_runtime() {
    echo "TODO 7"
}

# TODO 8 : lancer com.example.greeting.app.Main avec le java de $B/custom-runtime (sans module-path : tout est dedans).
run_runtime() {
    echo "TODO 8"
}

# TODO 9 : lancer l'application depuis $B/mods en affichant la resolution des modules (qui requiert qui).
show_resolution() {
    echo "TODO 9"
}

# TODO 10 : lister les modules presents dans $B/custom-runtime.
runtime_modules() {
    echo "TODO 10"
}
