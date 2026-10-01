module shipping.express {
    requires shipping.api;
    // Un 2e fournisseur du MEME service : ServiceLoader les trouvera tous les deux.
    provides com.example.shipping.api.ShippingRate with com.example.shipping.express.ExpressRate;
}
