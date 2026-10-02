// SOLUTION - kata : export simple, export qualifie, opens sans cible.
module k.core {
    exports k.core;
    exports k.core.util to k.app;
    opens k.core.model;
}
