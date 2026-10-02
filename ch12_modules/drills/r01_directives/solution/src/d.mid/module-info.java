// SOLUTION D02 - requires transitive : qui lit d.mid lit aussi d.base.
module d.mid {
    requires transitive d.base;
    exports d.mid;
}
