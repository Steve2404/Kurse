package ch1_buildingblocks.drills.exercises;

import ch1_buildingblocks.ExerciseChecker;
import ch1_buildingblocks.drills.Shop;

/**
 * DRILL 05 - KATA MELANGE : 13 questions du gerant de la caisse (tout le chapitre 1, sans indice)
 * ==============================================================================================
 *
 * Mode d'emploi : voir Drill01_WrapperApi, AVEC UNE DIFFERENCE : ici,
 * AUCUNE methode n'est indiquee. C'est a toi de choisir l'outil
 * (args et options, wrappers, char, blocs de texte, variables...),
 * comme dans un vrai programme. Fais-le en dernier, puis refais-le
 * regulierement : c'est le meilleur test "est-ce que je m'en souviens
 * vraiment ?".
 *
 * Donnees : ch1_buildingblocks.drills.Shop (ARGS, RAW_QUANTITIES,
 * PRODUCT_CODE, BINARY_FLAGS).
 *
 *
 * -- Les 13 questions --
 *
 * TODO 1  : argumentCount()        combien d'arguments dans Shop.ARGS ? -> 5.
 * TODO 2  : firstArticleToken()    le premier argument qui n'est pas une option -> "pomme:3:50".
 * TODO 3  : clientOrDefault()      le client de --client=, sinon "anonyme" -> "Lea".
 * TODO 4  : isVipCustomer()        l'interrupteur --vip est-il present ? -> true.
 * TODO 5  : validQuantityCount()   combien de Shop.RAW_QUANTITIES sont des int valides une
 *                                  fois les espaces retires ? -> 4 ("3", " 12 ", "+7", "-2").
 * TODO 6  : positiveQuantitySum()  somme des quantites valides ET strictement positives -> 22.
 * TODO 7  : totalCents()           total des articles de Shop.ARGS (quantite x prix) -> 468.
 * TODO 8  : formatEuros(cents)     468 -> "4.68", 5 -> "0.05".
 * TODO 9  : codeDigitSum()         somme des chiffres de Shop.PRODUCT_CODE "A7-X9" -> 16.
 * TODO 10 : codeLetterCount()      nombre de lettres de Shop.PRODUCT_CODE -> 2.
 * TODO 11 : flagsValue()           Shop.BINARY_FLAGS lu en binaire -> 10.
 * TODO 12 : looksLikeName(word)    le mot peut-il servir de nom de variable ? (regles de
 *                                  caracteres seulement, sans les mots reserves)
 *                                  "$total" -> true, "2total" -> false, "_x1" -> true, "" -> false.
 * TODO 13 : header(client)         bloc de texte -> "== TICKET ==\nClient : " + client + "\n".
 *
 * Pas de carte memoire ici : si tu bloques, retourne voir celle du
 * drill concerne (01 wrappers, 02 litteraux, 03 texte, 04 variables).
 */
public class Drill05_MixedKata {

    public static int argumentCount() {
        throw new UnsupportedOperationException("TODO 1 : implementer argumentCount()");
    }

    public static String firstArticleToken() {
        throw new UnsupportedOperationException("TODO 2 : implementer firstArticleToken()");
    }

    public static String clientOrDefault() {
        throw new UnsupportedOperationException("TODO 3 : implementer clientOrDefault()");
    }

    public static boolean isVipCustomer() {
        throw new UnsupportedOperationException("TODO 4 : implementer isVipCustomer()");
    }

    public static int validQuantityCount() {
        throw new UnsupportedOperationException("TODO 5 : implementer validQuantityCount()");
    }

    public static int positiveQuantitySum() {
        throw new UnsupportedOperationException("TODO 6 : implementer positiveQuantitySum()");
    }

    public static int totalCents() {
        throw new UnsupportedOperationException("TODO 7 : implementer totalCents()");
    }

    public static String formatEuros(int cents) {
        throw new UnsupportedOperationException("TODO 8 : implementer formatEuros()");
    }

    public static int codeDigitSum() {
        throw new UnsupportedOperationException("TODO 9 : implementer codeDigitSum()");
    }

    public static int codeLetterCount() {
        throw new UnsupportedOperationException("TODO 10 : implementer codeLetterCount()");
    }

    public static int flagsValue() {
        throw new UnsupportedOperationException("TODO 11 : implementer flagsValue()");
    }

    public static boolean looksLikeName(String word) {
        throw new UnsupportedOperationException("TODO 12 : implementer looksLikeName()");
    }

    public static String header(String client) {
        throw new UnsupportedOperationException("TODO 13 : implementer header()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  argumentCount() == 5", argumentCount() == 5);
        ExerciseChecker.check("2  firstArticleToken() == pomme:3:50", firstArticleToken().equals("pomme:3:50"));
        ExerciseChecker.check("3  clientOrDefault() == Lea", clientOrDefault().equals("Lea"));
        ExerciseChecker.check("4  isVipCustomer() == true", isVipCustomer());
        ExerciseChecker.check("5  validQuantityCount() == 4", validQuantityCount() == 4);
        ExerciseChecker.check("6  positiveQuantitySum() == 22", positiveQuantitySum() == 22);
        ExerciseChecker.check("7  totalCents() == 468", totalCents() == 468);
        ExerciseChecker.check("8  formatEuros(468) == 4.68, (5) == 0.05", formatEuros(468).equals("4.68") && formatEuros(5).equals("0.05"));
        ExerciseChecker.check("9  codeDigitSum() == 16", codeDigitSum() == 16);
        ExerciseChecker.check("10 codeLetterCount() == 2", codeLetterCount() == 2);
        ExerciseChecker.check("11 flagsValue() == 10", flagsValue() == 10);
        ExerciseChecker.check("12 looksLikeName : $total et _x1 oui ; 2total et \"\" non",
                looksLikeName("$total") && looksLikeName("_x1") && !looksLikeName("2total") && !looksLikeName(""));
        ExerciseChecker.check("13 header(Lea) == \"== TICKET ==\\nClient : Lea\\n\"",
                header("Lea").equals("== TICKET ==\nClient : Lea\n"));

        ExerciseChecker.summary();
    }
}
