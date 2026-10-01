module text.utils {
    // transitive : Money apparait dans la signature publique de Formatter.euros, donc tout lecteur
    // de text.utils doit aussi pouvoir lire core.
    requires transitive core;
    exports com.example.text;
}
