// SOLUTION - un module OUVERT : tous ses paquets sont ouverts a la reflexion, pour tout le monde.
// (Dans un open module, la directive opens est interdite : tout est deja ouvert.)
open module config.legacy {
    exports config.legacy;
}
