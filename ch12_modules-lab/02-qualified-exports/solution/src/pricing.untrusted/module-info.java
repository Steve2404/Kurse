module pricing.untrusted {
    // Ce module n'accede au package que parce que pricing.engine l'a ajoute a sa liste "to".
    requires pricing.engine;
}
