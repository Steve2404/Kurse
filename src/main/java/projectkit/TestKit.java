package projectkit;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Le correcteur des projets et des drills de TESTS (chapitre 16 et suivants, ne pas modifier).
 *
 * Dans un projet de tests, tu ecris DEUX choses dans le paquet du projet :
 *   - le code (les classes dont l'enonce donne le nom et les methodes) ;
 *   - les tests : chaque fichier dont le nom finit par Test.java.
 *
 * Le correcteur recopie ces fichiers dans un paquet temporaire, les compile et lance les tests
 * avec JUnit, quatre fois :
 *   1. TES tests sur TON code : tout doit passer ;
 *   2. TES tests sur le code de REFERENCE (solution/) : tout doit passer, sinon un de tes tests
 *      attend un resultat faux ;
 *   3. les tests de REFERENCE sur TON code : tout doit passer, sinon ton code a un bug ;
 *   4. TES tests sur des MUTANTS : des copies du code de reference ou l'on a glisse UN bug chacune.
 *      Un bon jeu de tests TUE chaque mutant (au moins un test echoue). Un mutant qui survit
 *      montre un cas que tes tests ne regardent pas.
 */
public final class TestKit {

    /** Un mutant : dans le fichier file du code de reference, le texte from est remplace par to. */
    public record Mutant(String file, String from, String to) {
    }

    /** Le resultat d'un lancement de tests. */
    /** Un test echoue : son nom (et son nom affiche) et le message de l'echec. */
    private record Failure(String test, String message) {

        @Override
        public String toString() {
            return test + " : " + message;
        }
    }

    private record Run(String compileError, int found, int succeeded, List<Failure> failures,
                       Map<String, int[]> perMethod, String crash) {

        int failed() {
            return found - succeeded;
        }
    }

    private static final AtomicInteger RUN_NUMBER = new AtomicInteger();

    private TestKit() {
    }

    /**
     * Le Check d'un projet de tests.
     *
     * @param checkClass la classe Check du projet (son paquet est celui du projet)
     * @param args       les arguments de Check ("solution" : verifier le corrige)
     * @param minTests   le nombre minimal de tests (un test parametre compte chaque execution)
     * @param mutants    les bugs a faire attraper par tes tests
     * @param apiCode    les elements d'API attendus dans ton code (hors tests), meme syntaxe que ProjectChecker
     * @param apiTests   les elements d'API attendus dans tes tests
     */
    public static boolean checkProject(Class<?> checkClass, String[] args, int minTests, List<Mutant> mutants,
                                       List<String> apiCode, List<String> apiTests) throws IOException {
        boolean solution = args.length > 0 && args[0].equals("solution");
        String pkg = checkClass.getPackageName();
        Path projectDir = dirOf(pkg);
        Path solutionDir = projectDir.resolve("solution");
        Map<String, String> data = sources(projectDir, pkg, true);
        Map<String, String> refAll = sources(solutionDir, pkg + ".solution", false);
        Map<String, String> mine = solution ? refAll : sources(projectDir, pkg, false);
        Map<String, String> myCode = code(mine);
        Map<String, String> myTests = tests(mine);
        Map<String, String> refCode = code(refAll);
        Map<String, String> refTests = tests(refAll);

        System.out.println("=== Verification des tests de " + pkg + (solution ? ".solution" : "") + " ===");
        if (myTests.isEmpty()) {
            System.out.println("[ERREUR] aucun fichier de test : cree une classe dont le nom finit par Test (ex. PrixTest.java)");
        }
        boolean ok = !myTests.isEmpty();

        Run mineOnMine = run(merge(data, myCode, myTests));
        ok &= report("tes tests sur TON code", mineOnMine);
        if (mineOnMine.compileError() == null && mineOnMine.found() < minTests) {
            System.out.println("[FAIL] seulement " + mineOnMine.found() + " tests : il en faut au moins " + minTests);
            ok = false;
        }
        if (!solution) {
            ok &= report("tes tests sur le code de REFERENCE", run(merge(data, refCode, myTests)));
            ok &= report("les tests de REFERENCE sur TON code", run(merge(data, myCode, refTests)));
        }

        int killed = 0;
        List<String> survivors = new ArrayList<>();
        for (int i = 0; i < mutants.size(); i++) {
            Mutant m = mutants.get(i);
            Map<String, String> mutated = new LinkedHashMap<>(refCode);
            String original = mutated.get(m.file());
            if (original == null || !original.contains(m.from())) {
                System.out.println("[ERREUR interne] mutant " + (i + 1) + " : texte introuvable dans " + m.file());
                ok = false;
                continue;
            }
            mutated.put(m.file(), original.replace(m.from(), m.to()));
            Run r = run(merge(data, mutated, myTests));
            if (r.compileError() != null) {
                System.out.println("   mutant " + (i + 1) + " : tes tests ne compilent pas avec le code de reference");
                survivors.add(String.valueOf(i + 1));
            } else if (r.failed() > 0 || r.crash() != null) {
                killed++;
                System.out.println("   mutant " + (i + 1) + " : tue (par " + firstFailedTest(r) + ")");
            } else {
                survivors.add(String.valueOf(i + 1));
                System.out.println("   mutant " + (i + 1) + " : SURVIT (aucun de tes tests ne le remarque)");
            }
        }
        if (!mutants.isEmpty()) {
            boolean allKilled = killed == mutants.size();
            System.out.println((allKilled ? "[PASS]" : "[FAIL]") + " mutants : " + killed + "/" + mutants.size() + " tues"
                    + (allKilled ? "" : " ; survivants : " + survivors + " (le palier 2 de INDICES.md dit ce que chacun change)"));
            ok &= allKilled;
        }

        System.out.println("--- API de ton code ---");
        ok &= ProjectChecker.evaluateApi(withoutComments(myCode), apiCode);
        System.out.println("--- API de tes tests ---");
        ok &= ProjectChecker.evaluateApi(withoutComments(myTests), apiTests);
        System.out.println();
        System.out.println(ok ? "*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***"
                : "*** Pas encore : lis les lignes [FAIL] ci-dessus. ***");
        return ok;
    }

    /**
     * Le Check d'un drill de tests : lance tes tests et affiche, pour chaque methode de test (ordre alphabetique),
     * "nom : executions, reussies". Cette sortie est comparee a la sortie attendue.
     */
    public static boolean checkDrill(Class<?> checkClass, String[] args, List<String> expected, List<String> api)
            throws IOException {
        boolean solution = args.length > 0 && args[0].equals("solution");
        String pkg = checkClass.getPackageName() + (solution ? ".solution" : "");
        Path dir = dirOf(pkg);
        Map<String, String> data = sources(dirOf(checkClass.getPackageName()), checkClass.getPackageName(), true);
        Map<String, String> mine = sources(dir, pkg, false);
        System.out.println("=== Verification du drill " + pkg + " ===");
        Run r = run(merge(data, mine, Map.of()));
        List<String> actual = new ArrayList<>();
        if (r.compileError() != null) {
            System.out.println("[ERREUR] ton code ne compile pas :");
            System.out.println(r.compileError());
        } else {
            r.perMethod().forEach((name, counts) -> actual.add(name + " : " + counts[0] + " executions, " + counts[1] + " reussies"));
        }
        boolean outputOk = ProjectChecker.compare("rapport", expected, actual);
        boolean apiOk = ProjectChecker.evaluateApi(withoutComments(mine), api);
        System.out.println();
        System.out.println(outputOk && apiOk ? "*** DRILL REUSSI ***" : "*** Pas encore : lis les lignes [FAIL] ci-dessus. ***");
        return outputOk && apiOk;
    }

    // ------------------------------------------------------------------ lancement

    private static boolean report(String label, Run r) {
        if (r.compileError() != null) {
            System.out.println("[FAIL] " + label + " : ne compile pas");
            System.out.println(r.compileError());
            return false;
        }
        if (r.crash() != null) {
            System.out.println("[FAIL] " + label + " : " + r.crash());
            return false;
        }
        boolean ok = r.found() > 0 && r.failed() == 0;
        System.out.println((ok ? "[PASS] " : "[FAIL] ") + label + " : " + r.found() + " tests, " + r.succeeded() + " reussis");
        r.failures().stream().limit(3).forEach(f -> System.out.println("       echec : " + f));
        if (r.failures().size() > 3) {
            System.out.println("       ... et " + (r.failures().size() - 3) + " autres echecs");
        }
        return ok;
    }

    private static String firstFailedTest(Run r) {
        if (r.failures().isEmpty()) {
            return r.crash();
        }
        return r.failures().get(0).test();
    }

    /** Compile les sources (nom de fichier -> texte) dans un paquet neuf, puis lance tous les tests trouves. */
    private static Run run(Map<String, String> files) throws IOException {
        String runPkg = "checkrun.r" + RUN_NUMBER.incrementAndGet();
        Path out = Files.createTempDirectory("testkit");
        List<JavaFileObject> units = new ArrayList<>();
        Map<String, String> renamed = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : files.entrySet()) {
            String text = e.getValue().replaceFirst("(?m)^package\\s+[\\w.]+\\s*;", "package " + runPkg + ";");
            renamed.put(e.getKey(), text);
            units.add(new Source(e.getKey(), text));
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fm = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            List<String> options = List.of("-d", out.toString(), "-classpath", System.getProperty("java.class.path"),
                    "-proc:none", "-nowarn", "-encoding", "UTF-8", "-parameters");
            boolean compiled = compiler.getTask(null, fm, diagnostics, options, null, units).call();
            if (!compiled) {
                String errors = diagnostics.getDiagnostics().stream()
                        .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
                        .limit(3)
                        .map(d -> "       " + ((Source) d.getSource()).name + " ligne " + d.getLineNumber() + " : "
                                + d.getMessage(java.util.Locale.FRENCH).lines().findFirst().orElse(""))
                        .collect(Collectors.joining("\n"));
                return new Run(errors, 0, 0, List.of(), Map.of(), null);
            }
        }
        List<String> testClasses = new ArrayList<>();
        for (Map.Entry<String, String> e : renamed.entrySet()) {
            if (e.getValue().contains("@Test") || e.getValue().contains("@ParameterizedTest")
                    || e.getValue().contains("@RepeatedTest") || e.getValue().contains("@TestFactory")) {
                testClasses.add(runPkg + "." + e.getKey().replace(".java", ""));
            }
        }
        return launch(out, testClasses);
    }

    private static Run launch(Path classes, List<String> testClasses) throws IOException {
        URLClassLoader loader = new URLClassLoader(new URL[]{classes.toUri().toURL()}, TestKit.class.getClassLoader());
        List<Failure> failures = new ArrayList<>();
        Map<String, int[]> perMethod = new TreeMap<>();
        int[] totals = new int[2];
        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "testkit");
            t.setDaemon(true);
            return t;
        });
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        String crash = null;
        // La sortie des tests ne doit pas se meler au rapport.
        System.setOut(new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8));
        try {
            Future<?> f = executor.submit(() -> {
                Thread.currentThread().setContextClassLoader(loader);
                LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
                for (String name : testClasses) {
                    try {
                        builder.selectors(DiscoverySelectors.selectClass(loader.loadClass(name)));
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException(e);
                    }
                }
                LauncherDiscoveryRequest request = builder.build();
                Launcher launcher = LauncherFactory.create();
                launcher.execute(request, new TestExecutionListener() {
                    @Override
                    public void executionFinished(TestIdentifier id, TestExecutionResult result) {
                        boolean failed = result.getStatus() != TestExecutionResult.Status.SUCCESSFUL;
                        if (!id.isTest()) {
                            if (failed) {
                                failures.add(new Failure(id.getDisplayName(), message(result)));
                            }
                            return;
                        }
                        String method = id.getSource().filter(MethodSource.class::isInstance)
                                .map(s -> ((MethodSource) s).getMethodName()).orElse(id.getDisplayName());
                        int[] counts = perMethod.computeIfAbsent(method, k -> new int[2]);
                        counts[0]++;
                        totals[0]++;
                        if (failed) {
                            failures.add(new Failure(method + (id.getDisplayName().startsWith(method) ? "" : " [" + id.getDisplayName() + "]"),
                                    message(result)));
                        } else {
                            counts[1]++;
                            totals[1]++;
                        }
                    }
                });
            });
            f.get(60, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            crash = "les tests ne se terminent pas en 60 s (boucle infinie ?)";
        } catch (Exception e) {
            crash = "erreur pendant les tests : " + e;
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
            executor.shutdownNow();
        }
        if (crash == null && !failures.isEmpty() && totals[0] == 0) {
            crash = failures.get(0).toString();
        }
        return new Run(null, totals[0], totals[1], failures, perMethod, crash);
    }

    // Pour un assertAll, la 1re ligne ("Multiple Failures (2 failures)") ne dit rien : on ajoute le 1er echec.
    private static String message(TestExecutionResult result) {
        return result.getThrowable().map(t -> {
            List<String> lines = t.getMessage() == null ? List.of() : t.getMessage().lines().toList();
            String text = lines.isEmpty() ? "" : " : " + lines.get(0);
            if (lines.size() > 1 && lines.get(0).startsWith("Multiple Failures")) {
                text += " ; 1er echec : " + lines.get(1).strip().replaceFirst("^[\\w.]+Error: ", "");
            }
            return t.getClass().getSimpleName() + text;
        }).orElse(result.getStatus().toString());
    }

    // ------------------------------------------------------------------ sources

    private static Path dirOf(String pkg) {
        return Path.of("src/main/java", pkg.replace('.', '/'));
    }

    /**
     * Les .java du dossier (sans les sous-dossiers). dataOnly : seulement Data.java ; sinon tout sauf Data.java et Check.java.
     * Les imports du paquet du projet sont retires (tout est recopie dans un seul paquet).
     */
    private static Map<String, String> sources(Path dir, String pkg, boolean dataOnly) throws IOException {
        Map<String, String> result = new TreeMap<>();
        if (!Files.isDirectory(dir)) {
            return result;
        }
        String projectPkg = pkg.endsWith(".solution") ? pkg.substring(0, pkg.length() - ".solution".length()) : pkg;
        try (Stream<Path> files = Files.list(dir)) {
            for (Path p : files.filter(p -> p.toString().endsWith(".java")).sorted(Comparator.naturalOrder()).toList()) {
                String name = p.getFileName().toString();
                boolean isData = name.equals("Data.java");
                if (name.equals("Check.java") || isData != dataOnly) {
                    continue;
                }
                String text = Files.readString(p, StandardCharsets.UTF_8)
                        .replaceAll("(?m)^import\\s+" + Pattern.quote(projectPkg) + "(\\.solution)?\\.[\\w*]+\\s*;\\s*$", "");
                result.put(name, text);
            }
        }
        return result;
    }

    private static boolean isTest(String fileName) {
        return fileName.endsWith("Test.java");
    }

    private static Map<String, String> tests(Map<String, String> all) {
        Map<String, String> r = new TreeMap<>();
        all.forEach((k, v) -> {
            if (isTest(k)) {
                r.put(k, v);
            }
        });
        return r;
    }

    private static Map<String, String> code(Map<String, String> all) {
        Map<String, String> r = new TreeMap<>();
        all.forEach((k, v) -> {
            if (!isTest(k)) {
                r.put(k, v);
            }
        });
        return r;
    }

    @SafeVarargs
    private static Map<String, String> merge(Map<String, String>... parts) {
        Map<String, String> r = new TreeMap<>();
        for (Map<String, String> part : parts) {
            r.putAll(part);
        }
        return r;
    }

    private static String withoutComments(Map<String, String> files) {
        return files.values().stream()
                .map(t -> t.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("//[^\n]*", ""))
                .collect(Collectors.joining("\n"));
    }

    /** Une source en memoire, pour le compilateur. */
    private static final class Source extends SimpleJavaFileObject {
        final String name;
        final String text;

        Source(String name, String text) {
            super(URI.create("string:///" + name), Kind.SOURCE);
            this.name = name;
            this.text = text;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return text;
        }
    }
}
