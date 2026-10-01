# Lab 10 - corrige (lu par ../run.sh).

# --describe-module (-d) lit le module-info compile : requires (dont java.base "mandated"), exports, contains.
describe_app() {
    java --module-path "$B/mods" --describe-module greeting.app
}

# --module-version s'inscrit dans le module-info du jar (visible ensuite : greeting.api@1.0).
package_api() {
    mkdir -p "$B/jars"
    jar --create --file "$B/jars/greeting.api.jar" --module-version 1.0 -C "$B/mods/greeting.api" .
}

# jar --describe-module (-d) lit le descripteur DANS le jar, sans rien executer.
describe_jar() {
    jar --describe-module --file "$B/jars/greeting.api.jar"
}

# --main-class (-e) inscrit la classe principale : on pourra lancer avec -m greeting.app tout court.
package_app() {
    jar --create --file "$B/jars/greeting.app.jar" --main-class com.example.greeting.app.Main -C "$B/mods/greeting.app" .
}

# Le module-path peut etre un dossier de jars modulaires ; -m sans /Classe utilise la classe inscrite par --main-class.
run_from_jars() {
    java -p "$B/jars" -m greeting.app
}

# jdeps donne le graphe des modules ET, package par package, ce qui est vraiment utilise (java.io, java.lang...).
deps() {
    jdeps --module-path "$B/mods" -m greeting.app
}

# jlink a besoin de NOS modules ET de ceux du JDK (jmods) ; --add-modules donne la racine, jlink suit les requires.
build_runtime() {
    jlink --module-path "$B/mods${SEP}$JMODS" --add-modules greeting.app --output "$B/custom-runtime" \
          --no-header-files --no-man-pages
}

# Les modules sont DANS l'image : plus besoin de --module-path.
run_runtime() {
    "$B/custom-runtime/bin/java" -m greeting.app/com.example.greeting.app.Main
}

# --show-module-resolution affiche chaque module resolu et qui l'a demande, avant de lancer le programme.
show_resolution() {
    java -p "$B/mods" --show-module-resolution -m greeting.app/com.example.greeting.app.Main
}

# L'image ne contient que java.base et nos 2 modules.
runtime_modules() {
    "$B/custom-runtime/bin/java" --list-modules
}
