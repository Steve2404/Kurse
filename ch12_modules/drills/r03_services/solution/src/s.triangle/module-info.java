// SOLUTION - 3/4 bis : un fournisseur par methode provider().
module s.triangle {
    requires s.api;
    provides s.api.Shape with s.triangle.TriangleFactory;
}
