// SOLUTION - un module qui essaie d'utiliser le paquet interne : il doit ECHOUER a la compilation,
// car library.service.internal n'est exporte qu'a library.app.
module library.intruder {
    requires library.service;
}
