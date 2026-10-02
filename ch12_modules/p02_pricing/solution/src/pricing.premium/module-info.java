// SOLUTION - un 2e fournisseur, dont l'implementation est creee par une methode provider().
module pricing.premium {
    requires pricing.api;
    provides pricing.api.PricingRule with pricing.premium.Coupons;
}
