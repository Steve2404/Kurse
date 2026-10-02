// SOLUTION D04 - un fournisseur : provides ... with ... ; il n'exporte rien.
module d.plugin {
    requires d.base;
    provides d.base.Greeter with d.plugin.Hello;
}
