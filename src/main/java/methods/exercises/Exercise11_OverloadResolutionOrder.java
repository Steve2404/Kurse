package methods.exercises;

import methods.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 11 - Quelle surcharge Java choisit-il ? PREDIT a la main, puis verifie contre le VRAI comportement (niveau : difficile)
 * =======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * methods.exercises.Exercise01_MethodDeclarationQuiz.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand plusieurs methodes portent le MEME nom (des surcharges),
 * Java choisit TOUJOURS la version la "moins couteuse a atteindre"
 * pour les arguments fournis, dans CET ordre de preference strict :
 *
 *   1. EXACT : le type de l'argument correspond PILE-POIL a un
 *      parametre (int -> int).
 *   2. ELARGISSEMENT (widening) : le type primitif de l'argument est
 *      "agrandi" vers un type primitif COMPATIBLE, sans perte
 *      possible (short -> int, int -> long...) - Java choisit le
 *      plus PETIT elargissement suffisant.
 *   3. AUTOBOXING : l'argument primitif est range dans SA boite
 *      wrapper exacte (int -> Integer) - UNIQUEMENT si AUCUN
 *      elargissement primitif ne convenait.
 *   4. VARARGS : en tout DERNIER recours, si rien d'autre ne convient.
 *
 * Piege classique de l'examen : entre ELARGIR vers un AUTRE type
 * primitif et AUTOBOXER vers la boite du type D'ORIGINE, Java prefere
 * TOUJOURS elargir (etape 2 passe avant l'etape 3, meme si autoboxer
 * "semble" plus proche du type d'origine).
 *
 *
 * ==================================================================
 * TODO : buildExpectedResolutions()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. En lisant les 6 surcharges plus bas (widthPick, boxPick,
 *      varargsPick - deja entierement ecrites, RIEN a completer
 *      la-dedans) et les 5 appels de main() (deja fournis aussi),
 *      ecrire a la main, dans l'ORDRE de ces 5 appels, le libelle
 *      EXACT que chacun doit renvoyer.
 *   2. Les renvoyer dans une List<String>.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un exercice de PREDICTION, pas de calcul.
 *
 * Exemple a verifier : buildExpectedResolutions() doit correspondre
 * EXACTEMENT aux 5 VRAIS resultats obtenus en appelant vraiment
 * chaque surcharge (voir main() plus bas) - si ca ne correspond pas,
 * c'est qu'une des 4 etapes de preference a ete mal appliquee.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - widthPick(short) : aucune surcharge n'accepte EXACTEMENT un
 *     short - entre widthPick(int) et widthPick(long), Java choisit
 *     le PLUS PETIT elargissement suffisant.
 *   - boxPick(int) [avec SEULEMENT boxPick(long) et boxPick(Integer)
 *     disponibles, AUCUN boxPick(int)] : l'elargissement (int -> long)
 *     passe AVANT l'autoboxing (int -> Integer), meme si Integer
 *     "ressemble" plus a int que long.
 *   - varargsPick(int) [avec SEULEMENT varargsPick(Integer) et
 *     varargsPick(int...) disponibles] : l'autoboxing passe AVANT les
 *     varargs, le DERNIER recours de la liste.
 */
public class Exercise11_OverloadResolutionOrder {

    static String widthPick(int x) {
        return "int";
    }

    static String widthPick(long x) {
        return "long";
    }

    static String boxPick(long x) {
        return "long";
    }

    static String boxPick(Integer x) {
        return "Integer";
    }

    static String varargsPick(Integer x) {
        return "Integer";
    }

    static String varargsPick(int... x) {
        return "int...";
    }

    public static List<String> buildExpectedResolutions() {
        throw new UnsupportedOperationException("TODO : implementer buildExpectedResolutions()");
    }

    public static void main(String[] args) {
        List<String> predicted = buildExpectedResolutions();

        short s = 5;
        List<String> real = new ArrayList<>();
        real.add(widthPick(5));
        real.add(widthPick(5L));
        real.add(widthPick(s));
        real.add(boxPick(5));
        real.add(varargsPick(5));

        ExerciseChecker.check("l'ordre de resolution PREDIT correspond EXACTEMENT au vrai comportement -> " + real,
                predicted.equals(real));

        ExerciseChecker.summary();
    }
}
