module orders.app {
    // L'application assemble les trois modules ; les dependances forment maintenant un arbre, sans boucle.
    requires orders.common;
    requires orders.processing;
    requires orders.shipping;
}
