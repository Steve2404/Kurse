package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 9 - Wrappers et blocs de texte en situation reelle : convertir, comparer, ne jamais planter (niveau : difficile)
 * ========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Le contexte --
 *
 * Une caisse enregistreuse recoit tout sous forme de TEXTE (clavier,
 * fichier, ligne de commande). Il faut le transformer en nombres et en
 * booleens avec les classes wrapper (Integer, Boolean, Character...),
 * sans tomber dans leurs pieges. Tous les resultats ci-dessous ont ete
 * verifies en executant le code avec Java 17.
 *
 *
 * ==================================================================
 * TODO 1 : parseQuantity(text)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Integer.parseInt est tres pointilleux : " 12" (avec un espace)
 * EXPLOSE (NumberFormatException: For input string: " 12"). Un nombre
 * trop grand pour un int ("2147483648") explose aussi. Par contre
 * "+7" et "-42" sont acceptes. La caisse doit nettoyer les espaces,
 * et rendre -1 si le texte n'est pas un int valide (au lieu de planter).
 *
 * -- Essayons a la main --
 *
 *   " 12 " -> strip -> "12" -> 12
 *   "+7" -> 7 ; "abc" -> -1 ; "2147483648" -> -1 ; "" -> -1
 *
 * -- Le plan --
 *
 *   1. Retirer les espaces autour.
 *   2. Essayer Integer.parseInt ; en cas de NumberFormatException, -1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : sameBoxedObject(value)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Integer.valueOf(v) ne fabrique PAS toujours une nouvelle boite :
 * pour les petits nombres de -128 a 127, Java garde des boites toutes
 * pretes dans une armoire (le "cache") et rend TOUJOURS la meme.
 * Au-dela, il fabrique une boite NEUVE a chaque fois. Or == compare
 * les BOITES (les references), pas les nombres dedans. D'ou :
 *   Integer.valueOf(127) == Integer.valueOf(127)  -> true
 *   Integer.valueOf(128) == Integer.valueOf(128)  -> false
 * Morale : pour comparer des Integer, toujours equals().
 *
 * -- Essayons a la main --
 *
 *   127 -> true ; 128 -> false ; -128 -> true ; -129 -> false
 *
 * -- Le plan --
 *
 *   1. Rendre Integer.valueOf(value) == Integer.valueOf(value).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une ligne, mais qui montre le piege.
 *
 *
 * ==================================================================
 * TODO 3 : unboxOrZero(box)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Integer peut etre null (une boite absente). "int n = box;" ouvre
 * la boite automatiquement (unboxing)... et si elle est null :
 * NullPointerException. Il faut regarder AVANT d'ouvrir.
 *
 * -- Essayons a la main --
 *
 *   unboxOrZero(5) -> 5 ; unboxOrZero(null) -> 0
 *
 * -- Le plan --
 *
 *   1. null -> 0 ; sinon la valeur.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : parseFlag(text)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Boolean.parseBoolean est le contraire de pointilleux : il rend true
 * UNIQUEMENT pour "true" (majuscules ignorees), et false pour TOUT le
 * reste, y compris "yes", "1" et null - sans jamais lancer d'exception.
 *
 * -- Essayons a la main --
 *
 *   "TRUE" -> true ; "yes" -> false ; null -> false
 *
 * -- Le plan --
 *
 *   1. Deleguer a Boolean.parseBoolean.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : digitValue(c)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le caractere '7' n'est pas le nombre 7 : c'est un dessin, dont le
 * code est 55. Character sait dire si c'est un chiffre (isDigit) et
 * quelle valeur il represente (getNumericValue).
 *
 * -- Essayons a la main --
 *
 *   '7' -> 7 ; 'x' -> -1 ; '0' -> 0
 *
 * -- Le plan --
 *
 *   1. Si c est un chiffre : sa valeur ; sinon -1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : receiptBlock()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un bloc de texte retire tout seul la marge commune a gauche, et
 * supprime les espaces en fin de ligne. Deux echappements changent ca :
 *   \s  a la fin d'une ligne = "garde un espace ici" ;
 *   \   a la fin d'une ligne = "ne va PAS a la ligne, colle la suivante".
 * Une ligne plus indentee que les autres garde son surplus d'espaces.
 *
 * -- Essayons a la main --
 *
 *   Le texte attendu (verifie) : "Ticket \npomme x3\n  fin\n"
 *   - "Ticket" suivi d'UN espace conserve (\s) puis retour a la ligne
 *   - "pomme " et "x3" colles sur une meme ligne (\ en fin de ligne)
 *   - "  fin" garde ses 2 espaces de plus que la marge
 *   - le """ final seul sur sa ligne -> un \n final
 *
 * -- Le plan --
 *
 *   1. Ecrire un bloc de texte de 4 lignes (+ la ligne du """ final)
 *      qui produit exactement ce texte.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - text.strip() ; try { ... Integer.parseInt(...) } catch (NumberFormatException e) { ... }
 *   - box == null ? 0 : box
 *   - Boolean.parseBoolean(text)
 *   - Character.isDigit(c), Character.getNumericValue(c)
 *   - return """
 *            Ticket\s
 *            pomme \
 *            x3
 *              fin
 *            """;
 *     (c'est presque la reponse : l'essentiel est de COMPRENDRE chaque ligne)
 */
public class Exercise09_WrappersInPractice {

    public static int parseQuantity(String text) {
        throw new UnsupportedOperationException("TODO 1 : implementer parseQuantity()");
    }

    public static boolean sameBoxedObject(int value) {
        throw new UnsupportedOperationException("TODO 2 : implementer sameBoxedObject()");
    }

    public static int unboxOrZero(Integer box) {
        throw new UnsupportedOperationException("TODO 3 : implementer unboxOrZero()");
    }

    public static boolean parseFlag(String text) {
        throw new UnsupportedOperationException("TODO 4 : implementer parseFlag()");
    }

    public static int digitValue(char c) {
        throw new UnsupportedOperationException("TODO 5 : implementer digitValue()");
    }

    public static String receiptBlock() {
        throw new UnsupportedOperationException("TODO 6 : implementer receiptBlock()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 parseQuantity(\" 12 \") == 12, (\"+7\") == 7",
                parseQuantity(" 12 ") == 12 && parseQuantity("+7") == 7);
        ExerciseChecker.check("1 parseQuantity(abc / 2147483648 / \"\") == -1",
                parseQuantity("abc") == -1 && parseQuantity("2147483648") == -1 && parseQuantity("") == -1);
        ExerciseChecker.check("2 sameBoxedObject : 127 et -128 true (cache), 128 et -129 false",
                sameBoxedObject(127) && sameBoxedObject(-128) && !sameBoxedObject(128) && !sameBoxedObject(-129));
        ExerciseChecker.check("3 unboxOrZero(5) == 5, unboxOrZero(null) == 0 (pas de NPE)",
                unboxOrZero(5) == 5 && unboxOrZero(null) == 0);
        ExerciseChecker.check("4 parseFlag : TRUE -> true ; yes et null -> false",
                parseFlag("TRUE") && !parseFlag("yes") && !parseFlag(null));
        ExerciseChecker.check("5 digitValue : '7' -> 7, '0' -> 0, 'x' -> -1",
                digitValue('7') == 7 && digitValue('0') == 0 && digitValue('x') == -1);
        ExerciseChecker.check("6 receiptBlock() == \"Ticket \\npomme x3\\n  fin\\n\"",
                receiptBlock().equals("Ticket \npomme x3\n  fin\n"));

        ExerciseChecker.summary();
    }
}
