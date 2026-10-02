// SOLUTION - le coeur : toutes les directives d'un module "complet".
module events.core {
    requires transitive events.api;
    requires geo.tools;                                  // module AUTOMATIQUE (jar sans module-info)
    exports events.core;
    exports events.core.internal to events.app;          // export qualifie
    opens events.core.state to events.audit;             // reflexion profonde pour l'audit seulement
    uses events.api.Notifier;
}
