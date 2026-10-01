module library.model {
    exports com.example.library.model;
    // opens ... to : setAccessible(true) sur un champ prive, a l'execution, pour library.app seulement.
    opens com.example.library.model to library.app;
}
