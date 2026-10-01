module shipping.app {
    // Le consommateur ne depend que de l'interface, jamais des fournisseurs.
    requires shipping.api;
    // uses : sans cette ligne, ServiceLoader.load lance ServiceConfigurationError a l'execution.
    uses com.example.shipping.api.ShippingRate;
}
