module library.model {
    // Book est utilise a la compilation par les autres modules...
    exports com.example.library.model;
    // ... et library.app lit un champ PRIVE par reflexion : opens (qualifie, le plus etroit possible).
    opens com.example.library.model to library.app;
}
