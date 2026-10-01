module notify.app {
    // Le consommateur ne depend QUE de l'interface, jamais du fournisseur.
    requires notify.api;
    // uses : sans cette ligne, ServiceLoader.load lance ServiceConfigurationError a l'execution (ca compile pourtant).
    uses com.example.notify.api.Notifier;
}
