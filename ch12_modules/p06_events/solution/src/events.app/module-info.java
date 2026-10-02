// SOLUTION - l'application : events.model et events.api lui arrivent par transitivite via events.core.
module events.app {
    requires events.core;
    requires events.audit;
}
