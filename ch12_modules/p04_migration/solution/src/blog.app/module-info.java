// SOLUTION - migration "top-down" : l'application devient un module NOMME, ses dependances restent
// des jars sans module-info, utilises comme modules AUTOMATIQUES (nom tire du fichier ou du manifeste).
module blog.app {
    requires acme.text;          // acme-text-2.1.jar -> on retire la version et ".jar", '-' devient '.'
    requires com.acme.utils;     // old_utils.jar : son manifeste impose Automatic-Module-Name
}
