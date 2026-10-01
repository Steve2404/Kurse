module library.service {
    requires transitive library.model;
    exports com.example.library.service;
    // Export QUALIFIE : seul library.app voit le package interne.
    exports com.example.library.service.internal to library.app;
}
