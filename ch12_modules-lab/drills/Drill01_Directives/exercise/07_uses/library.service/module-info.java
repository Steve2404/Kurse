module library.service {
    requires transitive library.model;
    exports com.example.library.service;
    provides com.example.library.service.Catalog with com.example.library.service.internal.InMemoryCatalog;
}
