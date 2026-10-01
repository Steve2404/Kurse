module shipping.post {
    requires shipping.api;
    // provides ... with : PostRate n'a pas de constructeur public, ServiceLoader appelle sa methode public static provider().
    // Le package n'est PAS exporte : le consommateur ne connait que l'interface.
    provides com.example.shipping.api.ShippingRate with com.example.shipping.post.PostRate;
}
