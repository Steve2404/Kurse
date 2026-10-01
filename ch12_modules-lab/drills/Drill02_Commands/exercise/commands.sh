# Drill02 - A COMPLETER (lu par ../../run.sh ; ne pas l'executer seul).
#
# Deja pret : SEP (separateur de chemins), B (dossier de build), SRC (les 3 modules library.* complets, a compiler),
#             $B/legacy (une classe de classpath deja compilee qui utilise sun.misc.Unsafe).
# UNE commande par fonction, avec les FORMES COURTES quand elles existent (-p, -m, -d, -c, -f, -e).

# TODO 1  : compiler les 3 modules de $SRC dans $B/mods (compilation multi-module).
compile_all() {
    echo "TODO 1"
}

# TODO 2  : lancer com.example.library.app.Main du module library.app depuis $B/mods (formes courtes).
run_short() {
    echo "TODO 2"
}

# TODO 3  : decrire le module library.model (forme courte de --describe-module).
describe_short() {
    echo "TODO 3"
}

# TODO 4  : lister les modules observables avec $B/mods sur le module-path, et ne garder que les lignes library.*
list_ours() {
    echo "TODO 4"
}

# TODO 5  : creer $B/jars/library.model.jar, library.service.jar et library.app.jar (ce dernier avec sa classe principale).
jar_all() {
    echo "TODO 5"
}

# TODO 6  : lancer l'application depuis $B/jars en ne donnant que le nom du module.
run_jar() {
    echo "TODO 6"
}

# TODO 7  : decrire le module contenu dans $B/jars/library.service.jar (formes courtes de jar).
jar_describe() {
    echo "TODO 7"
}

# TODO 8  : jdeps sur library.app ($B/mods), en RESUME (une ligne par dependance de module).
jdeps_summary() {
    echo "TODO 8"
}

# TODO 9  : jdeps : lister les API INTERNES du JDK utilisees par les classes de $B/legacy.
jdeps_internals() {
    echo "TODO 9"
}

# TODO 10 : lancer l'application en affichant la resolution des modules.
resolution() {
    echo "TODO 10"
}
