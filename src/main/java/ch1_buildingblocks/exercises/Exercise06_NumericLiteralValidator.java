package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 6 - Le _ dans un nombre : ecris toi-meme la regle du compilateur (niveau : difficile)
 * =============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Le contexte --
 *
 * Java permet d'ecrire 1_000_000 au lieu de 1000000 pour la lisibilite.
 * Mais le _ a des regles strictes, que l'examen adore. Plutot que de
 * les apprendre par coeur, tu vas ECRIRE le petit bout du compilateur
 * qui les verifie, pour les litteraux DECIMAUX (base 10 : pas de 0x
 * ni de 0b dans cet exercice).
 *
 * La regle exacte : chaque groupe de _ (un ou plusieurs _ colles)
 * doit avoir un CHIFFRE (0-9) juste avant ET juste apres lui.
 * Jamais au debut, jamais a la fin, jamais colle au point, a un
 * exposant "e" ou a un suffixe "L".
 *
 * Verdicts REELS de javac 17 (chaque litteral compile dans
 * "static Object v = <litteral>;") :
 *
 *   1_000_000 ok       1__000 ok (plusieurs _ colles : permis)
 *   _1000     ERREUR : cannot find symbol (ce n'est meme pas un nombre :
 *                      c'est un IDENTIFIANT, Java cherche une variable _1000)
 *   1000_     ERREUR : illegal underscore
 *   1_000.0   ok       1_.0  ERREUR : illegal underscore   1._0 ERREUR : illegal underscore
 *   3.14_15   ok       1_000L ok        1_000_L ERREUR : illegal underscore
 *   2_5e3     ok       25_e3 ERREUR : illegal underscore   0_7  ok
 *
 * (Pour info, hors exercice : 0x_1F est illegal mais 0x1_F est permis,
 * car en hexadecimal F est un chiffre.)
 *
 *
 * ==================================================================
 * TODO 1 : startsWithDigit(literal)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour Java, un nombre commence TOUJOURS par un chiffre. "_1000"
 * commence par _ : Java le prend pour un NOM de variable, pas pour
 * un nombre. Donc ce n'est pas un litteral valide.
 *
 * -- Essayons a la main --
 *
 *   "1_000" -> true ; "_1000" -> false ; "" -> false
 *
 * -- Le plan --
 *
 *   1. Faux si le texte est vide.
 *   2. Sinon : le 1er caractere est-il un chiffre ?
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique utilisee par le TODO 3.
 *
 *
 * ==================================================================
 * TODO 2 : underscoresWellPlaced(literal)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine chaque groupe de _ comme un pont. Un pont doit avoir une
 * rive (un chiffre) de CHAQUE cote. "1__000" : un pont de 2 planches,
 * avec 1 a gauche et 0 a droite : ok. "1_.0" : a droite du pont, il y
 * a un point, pas un chiffre : le pont s'effondre.
 *
 * -- Essayons a la main --
 *
 *   "1__000"  : groupe "__" entre '1' et '0' -> ok -> true
 *   "1000_"   : groupe "_" en fin, rien a droite -> false
 *   "1_000_L" : 2e groupe entre '0' et 'L' -> 'L' n'est pas un chiffre -> false
 *   "3.14_15" : groupe entre '4' et '1' -> true
 *   "123"     : aucun _ -> true
 *
 * -- Le plan --
 *
 *   1. Parcourir le texte caractere par caractere.
 *   2. Quand on trouve un _ : regarder le caractere juste avant le
 *      GROUPE (il faut qu'il existe et soit un chiffre), avancer
 *      jusqu'au bout du groupe, puis regarder le caractere juste apres
 *      (il faut qu'il existe et soit un chiffre).
 *   3. Si une rive manque ou n'est pas un chiffre : false.
 *   4. Arrive au bout sans probleme : true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : "ce caractere est-il un chiffre 0-9 ?" revient 2 fois. Une
 * petite methode privee isDigit(char) rend le code plus clair.
 *
 *
 * ==================================================================
 * TODO 3 : isValidDecimalLiteral(literal)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On assemble les 2 controles : c'est un nombre (TODO 1) ET les ponts
 * tiennent (TODO 2).
 *
 * -- Le plan --
 *
 *   1. startsWithDigit ET underscoresWellPlaced.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : les TODO 1 et 2.
 *
 *
 * ==================================================================
 * TODO 4 : numericValue(literal)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les _ sont seulement DECORATIFS : 1_000 vaut exactement 1000. Pour
 * lire un litteral entier (avec peut-etre un suffixe L) comme le fait
 * Java, on retire les _ et le L, puis on convertit.
 *
 * -- Essayons a la main --
 *
 *   "1_000_000" -> 1000000 ; "1__000" -> 1000 ; "1_000L" -> 1000 ; "42l" -> 42
 *
 * -- Le plan --
 *
 *   1. Retirer tous les _.
 *   2. Si le texte finit par L ou l, retirer ce dernier caractere.
 *   3. Convertir en long.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : les 13 verdicts javac du tableau ci-dessus,
 * plus les valeurs du TODO 4.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - literal.isEmpty(), literal.charAt(i), literal.length()
 *   - chiffre : c >= '0' && c <= '9'  (plus strict que Character.isDigit,
 *     qui accepte aussi des chiffres d'autres alphabets)
 *   - literal.replace("_", ""), s.endsWith("L"), s.substring(0, s.length() - 1)
 *   - Long.parseLong(texte)
 */
public class Exercise06_NumericLiteralValidator {

    public static boolean startsWithDigit(String literal) {
        throw new UnsupportedOperationException("TODO 1 : implementer startsWithDigit()");
    }

    public static boolean underscoresWellPlaced(String literal) {
        throw new UnsupportedOperationException("TODO 2 : implementer underscoresWellPlaced()");
    }

    public static boolean isValidDecimalLiteral(String literal) {
        throw new UnsupportedOperationException("TODO 3 : implementer isValidDecimalLiteral()");
    }

    public static long numericValue(String literal) {
        throw new UnsupportedOperationException("TODO 4 : implementer numericValue()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 startsWithDigit : 1_000 oui, _1000 non, \"\" non",
                startsWithDigit("1_000") && !startsWithDigit("_1000") && !startsWithDigit(""));
        ExerciseChecker.check("2 underscoresWellPlaced : 1__000, 3.14_15, 123 oui ; 1000_, 1_000_L non",
                underscoresWellPlaced("1__000") && underscoresWellPlaced("3.14_15") && underscoresWellPlaced("123")
                        && !underscoresWellPlaced("1000_") && !underscoresWellPlaced("1_000_L"));

        String[] accepted = {"1_000_000", "1__000", "1_000.0", "3.14_15", "1_000L", "2_5e3", "0_7"};
        String[] rejected = {"_1000", "1000_", "1_.0", "1._0", "1_000_L", "25_e3"};
        boolean allAccepted = true;
        for (String lit : accepted) {
            allAccepted &= isValidDecimalLiteral(lit);
        }
        boolean allRejected = true;
        for (String lit : rejected) {
            allRejected &= !isValidDecimalLiteral(lit);
        }
        ExerciseChecker.check("3 accepte les 7 litteraux que javac accepte", allAccepted);
        ExerciseChecker.check("3 rejette les 6 litteraux que javac refuse", allRejected);

        ExerciseChecker.check("4 numericValue : 1_000_000 -> 1000000, 1__000 -> 1000, 1_000L -> 1000, 42l -> 42",
                numericValue("1_000_000") == 1_000_000L && numericValue("1__000") == 1000L
                        && numericValue("1_000L") == 1000L && numericValue("42l") == 42L);

        ExerciseChecker.summary();
    }
}
