package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

import java.util.List;
import java.util.Map;

/**
 * EXERCICE 2 - Ordre des catch, multi-catch, throws et redefinition : ta regle comparee a 27 verdicts de javac (niveau : difficile)
 * =================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les exceptions forment un arbre genealogique (PARENT, plus bas) :
 *
 *   Throwable
 *   |-- Error                 (unchecked) -- StackOverflowError
 *   `-- Exception             (checked)
 *       |-- IOException       (checked)   -- FileNotFoundException
 *       |-- SQLException      (checked)
 *       `-- RuntimeException  (unchecked) -- IllegalArgumentException -- NumberFormatException
 *                                         -- IllegalStateException, ArithmeticException, NullPointerException
 *
 * CHECKED = le compilateur t'oblige a t'en occuper (catch ou throws).
 * Ce sont Throwable, Exception et leurs descendants, SAUF ceux de
 * RuntimeException et d'Error.
 *
 * -- Verdicts reels de javac 17 (tous les cas sont dans main) --
 *
 *   catch (Exception e) {} catch (IOException e) {}        -> error: exception IOException has already been caught
 *   try { } catch (IOException e) {}                        -> error: exception IOException is never thrown in body of corresponding try statement
 *   try { throw new IOException(); } catch (FileNotFoundException e) {}  -> compile (le IOException PEUT etre un FileNotFoundException)
 *   try { } catch (Exception e) {}  /  catch (Throwable e) / catch (Error e) / catch (IllegalArgumentException e) -> compile
 *   catch (NumberFormatException | RuntimeException e)     -> error: Alternatives in a multi-catch statement cannot be related by subclassing
 *   catch (A | B e) { e = null; }                          -> error: multi-catch parameter e may not be assigned  (un catch simple : compile)
 *   Parent m() throws IOException ; Child m() throws Exception -> error: m() in C cannot override m() in P
 *                                                               overridden method does not throw Exception
 *   void m() { throw new IOException(); }                  -> error: unreported exception IOException; must be caught or declared to be thrown
 *   try { }  (ni catch, ni finally, ni ressource)          -> error: 'try' without 'catch', 'finally' or resource declarations
 *   try { } finally { } catch (Exception e) { }            -> error: 'catch' without 'try'
 *   throw new RuntimeException(); System.out.println();    -> error: unreachable statement
 *
 *
 * ==================================================================
 * TODO 1 : isChecked(type)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   IOException -> true ; Exception -> true ; Throwable -> true
 *   NumberFormatException -> false ; StackOverflowError -> false
 *
 * -- Le plan --
 *
 *   1. Descendant de RuntimeException ou d'Error -> false.
 *   2. Sinon -> true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isA (donnee plus bas).
 *
 *
 * ==================================================================
 * TODO 2 : catchChainError(thrown, catches)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * thrown : les exceptions CHECKED que le bloc try peut lancer. catches :
 * les blocs catch, dans l'ordre. Rendre la 1re erreur, ou "OK".
 *
 *   - Un catch dont le type descend d'un catch PLUS HAUT ne sera jamais
 *     atteint : "exception X has already been caught".
 *   - Un catch d'une checked PRECISE (pas Exception, pas Throwable) que le
 *     try ne peut pas lancer : "exception X is never thrown in body of
 *     corresponding try statement". "Peut lancer" : un thrown descend de X,
 *     OU X descend d'un thrown (un IOException peut etre un FileNotFoundException).
 *
 * -- Essayons a la main --
 *
 *   ([IOException], [Exception, IOException])    -> exception IOException has already been caught
 *   ([], [IOException])                          -> exception IOException is never thrown in body of corresponding try statement
 *   ([IOException], [FileNotFoundException])     -> OK
 *
 * -- Le plan --
 *
 *   1. Pour chaque catch i : si un catch j < i est un ancetre (ou le meme) -> "already been caught".
 *   2. Sinon, si i est checked, n'est ni Exception ni Throwable, et n'est relie a aucun thrown -> "never thrown".
 *   3. Tout va bien -> "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isA et isChecked.
 *
 *
 * ==================================================================
 * TODO 3 : multiCatchError(alternatives)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Deux alternatives differentes par leur position, dont l'une descend de l'autre (ou egales)
 *      -> "Alternatives in a multi-catch statement cannot be related by subclassing".
 *   2. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isA.
 *
 *
 * ==================================================================
 * TODO 4 : overrideError(parentThrows, childThrows)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le parent a promis a ses appelants : "je ne lance que ces checked-la".
 * L'enfant peut promettre MOINS (rien, ou un sous-type), jamais PLUS.
 * Les unchecked sont toujours permises.
 *
 * -- Essayons a la main --
 *
 *   ([IOException], [Exception])              -> overridden method does not throw Exception
 *   ([IOException], [FileNotFoundException])  -> OK
 *   ([], [IllegalStateException])             -> OK
 *
 * -- Le plan --
 *
 *   1. Pour chaque exception checked de l'enfant : descend-elle d'une exception du parent ?
 *   2. Non -> "overridden method does not throw " + elle.  Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isChecked et isA.
 *
 *
 * ==================================================================
 * TODO 5 : unreportedError(thrown, caught, declared)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * thrown : ce que le code lance (ou ce que les methodes appelees
 * declarent). Chaque checked doit etre attrapee (un catch d'un ancetre)
 * OU declaree (un throws d'un ancetre).
 *
 * -- Le plan --
 *
 *   1. Pour chaque thrown checked ni attrapee ni declaree -> "unreported exception " + X + "; must be caught or declared to be thrown".
 *   2. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : "une de ces exceptions est-elle un ancetre de X ?" sert deux
 * fois (caught, declared) : ecris-la une fois.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - isA("NumberFormatException", "RuntimeException") == true ; isA(x, x) == true.
 *   - Deux boucles imbriquees : for (int i ...) for (int j = 0; j < i; j++).
 */
public class Exercise02_CatchAndThrowsRules {

    // L'arbre des exceptions du jeu : chaque type -> son parent direct.
    static final Map<String, String> PARENT = Map.ofEntries(
            Map.entry("Exception", "Throwable"), Map.entry("Error", "Throwable"),
            Map.entry("StackOverflowError", "Error"), Map.entry("IOException", "Exception"),
            Map.entry("FileNotFoundException", "IOException"), Map.entry("SQLException", "Exception"),
            Map.entry("RuntimeException", "Exception"), Map.entry("IllegalArgumentException", "RuntimeException"),
            Map.entry("NumberFormatException", "IllegalArgumentException"), Map.entry("IllegalStateException", "RuntimeException"),
            Map.entry("ArithmeticException", "RuntimeException"), Map.entry("NullPointerException", "RuntimeException"));

    // "a descend-il de b (ou est-il b) ?" : on remonte les parents de a.
    public static boolean isA(String a, String b) {
        for (String t = a; t != null; t = PARENT.get(t)) {
            if (t.equals(b)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChecked(String type) {
        throw new UnsupportedOperationException("TODO 1 : implementer isChecked()");
    }

    public static String catchChainError(List<String> thrown, List<String> catches) {
        throw new UnsupportedOperationException("TODO 2 : implementer catchChainError()");
    }

    public static String multiCatchError(List<String> alternatives) {
        throw new UnsupportedOperationException("TODO 3 : implementer multiCatchError()");
    }

    public static String overrideError(List<String> parentThrows, List<String> childThrows) {
        throw new UnsupportedOperationException("TODO 4 : implementer overrideError()");
    }

    public static String unreportedError(List<String> thrown, List<String> caught, List<String> declared) {
        throw new UnsupportedOperationException("TODO 5 : implementer unreportedError()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("isChecked : IOException, Exception, Throwable oui ; NumberFormatException, StackOverflowError non",
                isChecked("IOException") && isChecked("Exception") && isChecked("Throwable")
                        && !isChecked("NumberFormatException") && !isChecked("StackOverflowError"));

        // Verdicts REELS de javac 17 : {exceptions checked lancees par le try, catch dans l'ordre, verdict}.
        String caught = "exception %s has already been caught";
        String never = "exception %s is never thrown in body of corresponding try statement";
        Object[][] chains = {
                {List.of("IOException"), List.of("Exception", "IOException"), String.format(caught, "IOException")},
                {List.of("IOException"), List.of("IOException", "Exception"), "OK"},
                {List.of("FileNotFoundException"), List.of("IOException", "FileNotFoundException"), String.format(caught, "FileNotFoundException")},
                {List.of(), List.of("RuntimeException", "IllegalArgumentException"), String.format(caught, "IllegalArgumentException")},
                {List.of(), List.of("IOException"), String.format(never, "IOException")},
                {List.of("IOException"), List.of("FileNotFoundException"), "OK"},
                {List.of("IOException"), List.of("IOException", "SQLException"), String.format(never, "SQLException")},
                {List.of(), List.of("Exception"), "OK"},
                {List.of(), List.of("Throwable"), "OK"},
                {List.of(), List.of("IllegalArgumentException"), "OK"},
                {List.of(), List.of("Error"), "OK"},
                {List.of(), List.of("ArithmeticException", "Exception"), "OK"}};
        int agree = 0;
        for (Object[] c : chains) {
            @SuppressWarnings("unchecked")
            String mine = catchChainError((List<String>) c[0], (List<String>) c[1]);
            if (mine.equals(c[2])) {
                agree++;
            }
        }
        ExerciseChecker.check("catchChainError == javac sur 12 cas (" + agree + " d'accord)", agree == 12);

        String related = "Alternatives in a multi-catch statement cannot be related by subclassing";
        ExerciseChecker.check("multiCatchError == javac sur 3 cas",
                multiCatchError(List.of("NumberFormatException", "RuntimeException")).equals(related)
                        && multiCatchError(List.of("ArithmeticException", "ArithmeticException")).equals(related)
                        && multiCatchError(List.of("ArithmeticException", "NullPointerException")).equals("OK"));

        String notThrow = "overridden method does not throw %s";
        Object[][] overrides = {
                {List.of("IOException"), List.of("Exception"), String.format(notThrow, "Exception")},
                {List.of("IOException"), List.of("FileNotFoundException"), "OK"},
                {List.of("IOException"), List.of(), "OK"},
                {List.of(), List.of("IllegalStateException"), "OK"},
                {List.of(), List.of("IOException"), String.format(notThrow, "IOException")},
                {List.of("IOException"), List.of("SQLException"), String.format(notThrow, "SQLException")}};
        agree = 0;
        for (Object[] c : overrides) {
            @SuppressWarnings("unchecked")
            String mine = overrideError((List<String>) c[0], (List<String>) c[1]);
            if (mine.equals(c[2])) {
                agree++;
            }
        }
        ExerciseChecker.check("overrideError == javac sur 6 cas (" + agree + " d'accord)", agree == 6);

        String unreported = "unreported exception %s; must be caught or declared to be thrown";
        Object[][] reports = {
                {List.of("IOException"), List.of(), List.of(), String.format(unreported, "IOException")},
                {List.of("IllegalStateException"), List.of(), List.of(), "OK"},
                {List.of("StackOverflowError"), List.of(), List.of(), "OK"},
                {List.of("FileNotFoundException"), List.of(), List.of("IOException"), "OK"},
                {List.of("Exception"), List.of(), List.of(), String.format(unreported, "Exception")},
                {List.of("IOException"), List.of("Exception"), List.of(), "OK"}};
        agree = 0;
        for (Object[] c : reports) {
            @SuppressWarnings("unchecked")
            String mine = unreportedError((List<String>) c[0], (List<String>) c[1], (List<String>) c[2]);
            if (mine.equals(c[3])) {
                agree++;
            }
        }
        ExerciseChecker.check("unreportedError == javac sur 6 cas (" + agree + " d'accord)", agree == 6);

        ExerciseChecker.summary();
    }
}
