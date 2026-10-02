// SOLUTION - 3/4 : un fournisseur classique.
module s.square {
    requires s.api;
    provides s.api.Shape with s.square.Square;
}
