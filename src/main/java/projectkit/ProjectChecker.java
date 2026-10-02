package projectkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Le correcteur des PROJETS (ne pas modifier).
 *
 * Dans un projet, c'est TOI qui ecris toute la structure (records, classes,
 * interfaces, methodes, main). Le correcteur ne connait donc rien de ton code
 * sauf le nom de la classe qui contient main. Il verifie deux choses :
 *
 *   1. le RESULTAT : il lance ton main, capture ce qu'il affiche, et compare
 *      ligne par ligne avec la sortie attendue (il montre la 1re difference) ;
 *   2. l'API : il lit tes fichiers .java et verifie que chaque methode de l'API
 *      visee par le projet y apparait au moins une fois (le but est de TOUTES
 *      les pratiquer, pas seulement celles qu'on connait deja).
 */
public final class ProjectChecker {

    private ProjectChecker() {
    }

    /**
     * @param mainClass   nom complet de la classe qui contient main
     * @param sourceDir   le dossier des .java a analyser (sous-dossiers exclus)
     * @param ignored     les fichiers donnes, a ne pas analyser (Data.java, Check.java...)
     * @param expected    la sortie attendue, ligne par ligne
     * @param requiredApi les appels que tes sources doivent contenir (ex. ".flatMap(") ;
     *                    prefixe "!" = appel INTERDIT (ex. "!.get()") ;
     *                    prefixe "3x" = au moins 3 occurrences (ex. "3x.reduce(")
     */
    public static void check(String mainClass, Path sourceDir, List<String> ignored,
                             List<String> expected, List<String> requiredApi) throws IOException {
        System.out.println("=== Verification de " + mainClass + " ===");
        List<String> actual = runMain(mainClass);
        boolean outputOk = compare(expected, actual);
        boolean apiOk = checkApi(sourceDir, ignored, requiredApi);
        System.out.println();
        if (outputOk && apiOk) {
            System.out.println("*** PROJET REUSSI : sortie identique et toute l'API pratiquee. ***");
        } else {
            System.out.println("*** Pas encore : " + (outputOk ? "" : "la sortie differe. ") + (apiOk ? "" : "il manque des methodes de l'API.") + " ***");
        }
    }

    /**
     * Raccourci pour le Check d'un projet : la classe main s'appelle mainSimpleName
     * dans le paquet de checkClass ; avec l'argument "solution", on verifie le
     * sous-paquet solution. Data.java, Check.java et les .md ne sont pas analyses.
     */
    public static void check(Class<?> checkClass, String mainSimpleName, String[] args,
                             List<String> expected, List<String> requiredApi) throws IOException {
        boolean solution = args.length > 0 && args[0].equals("solution");
        String pkg = checkClass.getPackageName() + (solution ? ".solution" : "");
        Path dir = Path.of("src/main/java", pkg.replace('.', '/'));
        check(pkg + "." + mainSimpleName, dir, List.of("Data.java", "Check.java"), expected, requiredApi);
    }

    private static List<String> runMain(String mainClass) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String crash = null;
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            Class.forName(mainClass).getMethod("main", String[].class).invoke(null, (Object) new String[0]);
        } catch (ClassNotFoundException e) {
            crash = "classe introuvable : " + mainClass + " (as-tu cree la classe avec ce nom et ce paquet ?)";
        } catch (NoSuchMethodException e) {
            crash = "pas de methode public static void main(String[] args) dans " + mainClass;
        } catch (InvocationTargetException e) {
            crash = "ton programme a lance " + e.getCause();
        } catch (ReflectiveOperationException e) {
            crash = e.toString();
        } finally {
            System.setOut(original);
        }
        List<String> lines = new ArrayList<>(buffer.toString(StandardCharsets.UTF_8).lines().map(String::stripTrailing).toList());
        if (crash != null) {
            System.out.println("[ERREUR] " + crash);
        }
        return lines;
    }

    private static boolean compare(List<String> expected, List<String> actual) {
        int same = 0;
        while (same < expected.size() && same < actual.size() && expected.get(same).equals(actual.get(same))) {
            same++;
        }
        if (same == expected.size() && same == actual.size()) {
            System.out.println("[PASS] sortie : les " + expected.size() + " lignes sont identiques");
            return true;
        }
        System.out.println("[FAIL] sortie : " + same + "/" + expected.size() + " lignes justes avant la 1re difference (ligne " + (same + 1) + ")");
        System.out.println("       attendu : " + (same < expected.size() ? expected.get(same) : "(rien de plus)"));
        System.out.println("       obtenu  : " + (same < actual.size() ? actual.get(same) : "(rien de plus)"));
        return false;
    }

    private static boolean checkApi(Path sourceDir, List<String> ignored, List<String> requiredApi) throws IOException {
        String code;
        try (Stream<Path> files = Files.list(sourceDir)) {
            code = files.filter(p -> p.toString().endsWith(".java") && !ignored.contains(p.getFileName().toString()))
                    .map(ProjectChecker::readWithoutComments)
                    .collect(Collectors.joining("\n"));
        }
        List<String> missing = new ArrayList<>();
        List<String> forbidden = new ArrayList<>();
        for (String api : requiredApi) {
            if (api.startsWith("!")) {
                if (code.contains(api.substring(1))) {
                    forbidden.add(api.substring(1));
                }
            } else if (api.matches("\\d+x.+")) {
                int wanted = Integer.parseInt(api.substring(0, api.indexOf('x')));
                String token = api.substring(api.indexOf('x') + 1);
                if (code.split(java.util.regex.Pattern.quote(token), -1).length - 1 < wanted) {
                    missing.add(token + " (au moins " + wanted + " fois)");
                }
            } else if (!code.contains(api)) {
                missing.add(api);
            }
        }
        if (!forbidden.isEmpty()) {
            System.out.println("[FAIL] API : interdit ici (Optional.get() ou notion d'un chapitre suivant, voir TODO.md) : " + forbidden);
        }
        if (missing.isEmpty()) {
            System.out.println("[PASS] API : toutes les methodes visees sont utilisees");
        } else {
            System.out.println("[FAIL] API : encore a placer dans ton code : " + missing);
        }
        return missing.isEmpty() && forbidden.isEmpty();
    }

    // Les commentaires ne comptent pas : un appel ecrit seulement dans un // TODO n'est pas une utilisation.
    private static String readWithoutComments(Path file) {
        try {
            return Files.readString(file).replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("//[^\n]*", "");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
