module app {
    // Top-down : on commence par le module du HAUT. Les jars non migres deviennent des modules automatiques,
    // nommes d'apres le fichier : text-utils-2.1.jar -> text.utils (version retiree, '-' -> '.').
    requires text.utils;
    // Verifie en direct : ici "requires text.utils" suffirait (un automatic donne acces aux autres automatics),
    // mais Main utilise Money directement : on le dit explicitement, et ca restera juste une fois core nomme.
    requires core;
}
