# Lab 09 - corrige (lu par ../run.sh).

# Sur le classpath, meme un jar modulaire devient du "unnamed module" : on donne une LISTE
# de chemins separee par $SEP, et le nom d'une classe (pas de module).
run_unnamed() {
    java -cp "$B/jars/mathutils-1.0.jar${SEP}$B/classpath-app-out" AppMain
}

# Sur le module-path, un jar sans module-info devient un automatic module : il exporte TOUT.
# --module (ou -m) prend module/classe.
run_automatic() {
    java --module-path "$B/modpath-auto${SEP}$B/app-auto" --module app/com.example.app.Main
}

# Meme commande, mais le jar a un module-info : c'est un named module, qui n'exporte que ce qu'il declare.
run_named() {
    java -p "$B/modpath-named${SEP}$B/app-named" -m app/com.example.app.Main
}

# Regle du nom automatique : on enleve ".jar", puis la version (-2.3.1), puis tout caractere
# non alphanumerique devient un point : string-tools -> string.tools.
AUTOMATIC_NAME="string.tools"
