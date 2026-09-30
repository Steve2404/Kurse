package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 5 - Une boite a outils de validateurs : methodes default, static et private d'interface (niveau : avance)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Validator repond a une seule question : "ce texte est-il
 * acceptable ?" (sa methode abstraite test). Autour de cette unique
 * question, l'interface offre :
 *   - des methodes default pour COMBINER des validateurs (and, or, negate)
 *     et pour les utiliser (describe, firstFailure) ;
 *   - des methodes static qui FABRIQUENT des validateurs tout faits
 *     (minLength, containsDigit, allOf) ;
 *   - une methode private qui aide les default sans etre visible dehors.
 *
 * Comme Validator n'a qu'une methode abstraite, on peut en creer un avec
 * une lambda : s -> s.length() > 3.
 *
 *
 * ==================================================================
 * TODO 1 : and(other)    TODO 2 : or(other)    TODO 3 : negate()    [methodes default]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque methode rend un NOUVEAU validateur, qui interroge this et/ou
 * other. Dans une methode default, "this" est l'objet qui implemente.
 *
 * -- Le plan --
 *
 *   1. and : rendre s -> this.test(s) && other.test(s).
 *   2. or : rendre s -> this.test(s) || other.test(s).
 *   3. negate : rendre s -> !this.test(s).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : describe(s)    [default qui utilise une methode private]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   minLength(3).describe("java") -> "java : valide"      describe("go") -> "go : invalide"
 *
 * -- Le plan --
 *
 *   1. Rendre s + " : " + label(test(s)) ; label est la methode private deja ecrite.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : label (private, deja ecrite).
 *
 *
 * ==================================================================
 * TODO 5 : firstFailure(values...)    [default + varargs]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   minLength(3).firstFailure("java", "ok", "no") -> 1       tout valide -> -1
 *
 * -- Le plan --
 *
 *   1. Pour i de 0 a n - 1 : si !test(values[i]), rendre i. Sinon -1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : minLength(n)    TODO 7 : containsDigit()    TODO 8 : allOf(validators...)    [methodes static]
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. minLength : s -> s.length() >= n.
 *   2. containsDigit : vrai si un caractere est un chiffre (Character.isDigit).
 *   3. allOf : partir de s -> true, puis enchainer and(...) avec chaque validateur.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : allOf reutilise and (TODO 1).
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Dans une interface, une methode static s'appelle Validator.minLength(3) depuis l'exterieur.
 *   - s.chars().anyMatch(Character::isDigit) ou une boucle sur toCharArray().
 */
public class Exercise05_ValidatorToolkit {

    public interface Validator {
        boolean test(String s);

        default Validator and(Validator other) {
            throw new UnsupportedOperationException("TODO 1 : implementer and()");
        }

        default Validator or(Validator other) {
            throw new UnsupportedOperationException("TODO 2 : implementer or()");
        }

        default Validator negate() {
            throw new UnsupportedOperationException("TODO 3 : implementer negate()");
        }

        default String describe(String s) {
            throw new UnsupportedOperationException("TODO 4 : implementer describe()");
        }

        default int firstFailure(String... values) {
            throw new UnsupportedOperationException("TODO 5 : implementer firstFailure()");
        }

        private String label(boolean ok) {
            return ok ? "valide" : "invalide";
        }

        static Validator minLength(int n) {
            throw new UnsupportedOperationException("TODO 6 : implementer minLength()");
        }

        static Validator containsDigit() {
            throw new UnsupportedOperationException("TODO 7 : implementer containsDigit()");
        }

        static Validator allOf(Validator... validators) {
            throw new UnsupportedOperationException("TODO 8 : implementer allOf()");
        }
    }

    public static void main(String[] args) {
        Validator longEnough = s -> s.length() >= 4;
        Validator startsUpper = s -> !s.isEmpty() && Character.isUpperCase(s.charAt(0));
        ExerciseChecker.check("and : Java oui, java non, Go non",
                longEnough.and(startsUpper).test("Java") && !longEnough.and(startsUpper).test("java") && !longEnough.and(startsUpper).test("Go"));
        ExerciseChecker.check("or : Go oui (majuscule), go non", longEnough.or(startsUpper).test("Go") && !longEnough.or(startsUpper).test("go"));
        ExerciseChecker.check("negate : go oui, java non", longEnough.negate().test("go") && !longEnough.negate().test("java"));
        ExerciseChecker.check("describe (via la methode private label)",
                longEnough.describe("java").equals("java : valide") && longEnough.describe("go").equals("go : invalide"));
        ExerciseChecker.check("firstFailure : 1 et -1",
                longEnough.firstFailure("java", "ok", "no") == 1 && longEnough.firstFailure("java", "kotlin") == -1);
        ExerciseChecker.check("minLength(3) : abc oui, ab non", Validator.minLength(3).test("abc") && !Validator.minLength(3).test("ab"));
        ExerciseChecker.check("containsDigit : java17 oui, java non",
                Validator.containsDigit().test("java17") && !Validator.containsDigit().test("java"));
        Validator password = Validator.allOf(Validator.minLength(8), Validator.containsDigit(), startsUpper);
        ExerciseChecker.check("allOf : Secret123 oui, secret123 non, Secret non, allOf() accepte tout",
                password.test("Secret123") && !password.test("secret123") && !password.test("Secret") && Validator.allOf().test(""));

        ExerciseChecker.summary();
    }
}
