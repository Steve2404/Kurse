// TODO : geo.app (qui requires geo.calculator SEULEMENT) a besoin de
// pouvoir utiliser directement le Distance renvoye par Calculator.sum().
// Un simple "requires" ne fait confiance qu'a CE module - voir ENONCE.md.
module geo.calculator {
    requires geo.units;
    exports com.example.geo.calculator;
}
