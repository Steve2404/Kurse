module core {
    // Bottom-up : on commence par le module SANS dependance. Le code du classpath (unnamed module)
    // lit tous les modules, mais ne voit que les packages EXPORTES.
    exports com.example.core;
}
