// SOLUTION - 2/4 : le localisateur (uses) ; il expose Shape dans son API, d'ou le transitive.
module s.locator {
    requires transitive s.api;
    exports s.locator;
    uses s.api.Shape;
}
