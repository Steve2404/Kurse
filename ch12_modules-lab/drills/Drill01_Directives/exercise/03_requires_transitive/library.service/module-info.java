module library.service {
    // TODO 3 : Book apparait dans l'API de Catalog ; tout lecteur de library.service doit aussi lire library.model.
    requires library.model;
    exports com.example.library.service;
}
