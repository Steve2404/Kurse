module library.service {
    requires transitive library.model;
    exports com.example.library.service;
    // provides ... with : l'implementation reste dans un package NON exporte.
    provides com.example.library.service.Catalog with com.example.library.service.internal.InMemoryCatalog;
}
