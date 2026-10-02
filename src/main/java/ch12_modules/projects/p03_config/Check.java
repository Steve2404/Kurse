package ch12_modules.projects.p03_config;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script build.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/p03_config,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "build.sh";

    static final String MODULES = "ch12_modules/p03_config";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- javac sans --add-exports",
            "error: package config.model.internal is not visible",
            "(package config.model.internal is declared in module config.model, which does not export it)",
            "--- java sans option",
            "lie : example.org:8443 (debug) [web, api] ; dump {debug=true, host=example.org, limits={maxClients=100, timeoutMs=3000}, port=8443, tags=[web, api]}",
            "module ouvert : {mode=batch, retries=3}",
            "config.model ouvert a config.binder true, a config.app false ; config.legacy ouvert a tous true ; secret ouvert a config.binder false",
            "coffre refuse : InaccessibleObjectException - Unable to make public config.model.secret.Vault() accessible: module config.model does not \"exports config.model.secret\" to module config.binder",
            "interne refuse : IllegalAccessError - class config.app.Main (in module config.app) cannot access class config.model.internal.Defaults (in module config.model) because module config.model does not export config.model.internal to module config.app",
            "--- java avec --add-exports et --add-opens",
            "config.model ouvert a config.binder true, a config.app false ; config.legacy ouvert a tous true ; secret ouvert a config.binder true",
            "coffre : {token=s3cret}",
            "interne : port par defaut 8080",
            "--- describe-module",
            "config.legacy",
            "exports config.legacy",
            "requires java.base mandated",
            "config.model",
            "contains config.model.internal",
            "contains config.model.secret",
            "exports config.model",
            "qualified opens config.model to config.binder",
            "requires java.base mandated");
            // SCRIPT-END

    static final List<String> API = List.of(
            "exports config.model;", "opens config.model to config.binder;", "open module config.legacy", "module config.binder",
            "requires config.binder;", ".setAccessible(true)", ".getDeclaredField(", ".getDeclaredConstructor()",
            ".getDeclaredFields()", "Class.forName(", ".isOpen(", "catch (IllegalAccessError",
            "throws ReflectiveOperationException", "--add-exports config.model/config.model.internal=config.app", "--add-opens config.model/config.model.secret=config.binder", "--describe-module config.legacy",
            "--describe-module config.model",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
