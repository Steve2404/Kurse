module library.service {
    // transitive : library.app, qui ne requiert que library.service, lit aussi library.model.
    requires transitive library.model;
    exports com.example.library.service;
}
