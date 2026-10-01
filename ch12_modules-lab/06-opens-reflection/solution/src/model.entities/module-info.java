module model.entities {
    // exports donne acces aux membres PUBLICS a la compilation et a l'execution.
    exports com.example.model.entities;
    // opens (ici qualifie) autorise la reflexion PROFONDE (setAccessible sur un champ prive) a l'execution seulement.
    opens com.example.model.entities to reflect.tool;
}
