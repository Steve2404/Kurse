// SOLUTION - l'interface de service ; elle expose des types du modele, d'ou le transitive.
module events.api {
    requires transitive events.model;
    exports events.api;
}
