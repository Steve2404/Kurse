// SOLUTION - le module des services.
module library.service {
    // transitive : tout module qui requiert library.service lit AUSSI library.model (Book apparait dans l'API publique).
    requires transitive library.model;

    exports library.service;
    // Export QUALIFIE : seul library.app peut utiliser ce paquet interne.
    exports library.service.internal to library.app;
}
