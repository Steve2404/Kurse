module geo.calculator {
    // transitive : ses types (Distance) apparaissent dans l'API publique, donc tout lecteur de geo.calculator lit aussi geo.units.
    requires transitive geo.units;
    // Le package de l'API est exporte comme d'habitude.
    exports com.example.geo.calculator;
}
