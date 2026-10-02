package ch12_modules.projects.p06_events;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script build.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/p06_events,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "build.sh";

    static final String MODULES = "ch12_modules/p06_events";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- complet",
            "salles : {E1=1, E2=2, E3=1, E4=3, E5=2, E6=1}",
            "canaux : [email, sms]",
            "  email a Ana : E2 a Lyon (436 km)",
            "  sms a Bob : E5 11h (159 km)",
            "  email a Chloe : E2 a Lyon (0 km)",
            "  Dan : pas de canal fax",
            "audit : Counters{missing=1, sent=3}",
            "lectures : events.app lit events.model true, Distance dans [geo.tools]",
            "--- sans le fournisseur sms",
            "canaux : [email]",
            "  email a Ana : E2 a Lyon (436 km)",
            "  Bob : pas de canal sms",
            "--- describe-module events.core",
            "",
            "events.core",
            "exports events.core",
            "qualified exports events.core.internal to events.app",
            "qualified opens events.core.state to events.audit",
            "requires events.api transitive",
            "requires geo.tools",
            "requires java.base mandated",
            "uses events.api.Notifier",
            "--- jdeps",
            "events.api -> events.model",
            "events.api -> java.base",
            "events.app -> events.audit",
            "events.app -> events.core",
            "events.app -> events.model",
            "events.app -> java.base",
            "events.audit -> java.base",
            "events.core -> events.api",
            "events.core -> events.model",
            "events.core -> geo.tools",
            "events.core -> java.base",
            "events.model -> java.base",
            "geo.tools -> java.base",
            "--- jlink",
            "Error: automatic module cannot be used with jlink: geo.tools");
            // SCRIPT-END

    static final List<String> API = List.of(
            "requires transitive events.model;", "requires transitive events.api;", "requires geo.tools;", "exports events.core.internal to events.app;",
            "opens events.core.state to events.audit;", "uses events.api.Notifier;", "provides events.api.Notifier with events.email.EmailNotifier;", "provides events.api.Notifier with events.sms.SmsGateway;",
            "public static Notifier provider()", "PriorityQueue<", "ServiceLoader.load(", "Math.asin(",
            ".setAccessible(true)", "geo-tools-1.0.jar", "-m events.app,events.email,events.sms", "--main-class events.app.Main",
            "--limit-modules events.app,events.email", "jar --describe-module --file", "jdeps -s -R --module-path", "jlink --module-path",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
