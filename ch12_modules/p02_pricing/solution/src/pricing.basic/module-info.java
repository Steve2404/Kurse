// SOLUTION - un FOURNISSEUR : il n'exporte rien ; il declare ses implementations avec provides ... with.
module pricing.basic {
    requires pricing.api;
    provides pricing.api.PricingRule with pricing.basic.TenPercent, pricing.basic.ThreeForTwo;
}
