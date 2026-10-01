module library.service {
    // transitive : Book apparait dans l'API de Catalog.
    requires transitive library.model;
    exports com.example.library.service;
    // Le fournisseur reste dans un package non exporte.
    provides com.example.library.service.Catalog with com.example.library.service.internal.InMemoryCatalog;
}
