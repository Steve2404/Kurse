// SOLUTION - le coeur : il depend d'un module du JDK autre que java.base (java.logging),
// pour que jdeps et jlink aient quelque chose a montrer.
module inv.core {
    requires java.logging;
    exports inv.core;
}
