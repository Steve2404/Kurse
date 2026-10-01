module library.app {
    requires library.service;
    // uses : sinon ServiceConfigurationError ('does not declare uses') a l'execution.
    uses com.example.library.service.Catalog;
}
