module mathutils {
    // Un module NOMME n'exporte que ce qu'il declare : sans cette ligne, le module app aurait
    // "package com.example.mathutils is not visible" (alors qu'en automatic, tout est exporte d'office).
    exports com.example.mathutils;
}
