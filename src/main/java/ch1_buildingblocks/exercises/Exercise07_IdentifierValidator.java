package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 7 - Les noms en Java : ecris toi-meme le controleur d'identifiants (niveau : difficile)
 * ===============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Les regles d'un identifiant (nom de variable, de methode, de classe) --
 *
 *   1. Le 1er caractere : une lettre (de n'importe quel alphabet, "ete"
 *      avec accents compris), $ ou _. JAMAIS un chiffre.
 *   2. Les suivants : lettres, chiffres, $ ou _. Jamais d'espace ni de "-".
 *   3. Pas un mot reserve : class, int, goto, const... ni les
 *      litteraux true, false, null.
 *   4. Depuis Java 9, "_" TOUT SEUL est un mot-cle interdit (mais "__"
 *      ou "_value" restent permis).
 *   5. PIEGE : var, record, yield, sealed, permits ne sont PAS des
 *      mots reserves au sens strict : "int var = 1;" compile !
 *
 * Verdicts REELS de javac 17 pour "int NOM = 1;" :
 *
 *   $price ok   _value ok   __ ok   $ ok   Class ok   ete (avec accents) ok
 *   var ok      record ok   yield ok   sealed ok   permits ok
 *   1value  ERREUR : not a statement
 *   _       ERREUR : as of release 9, '_' is a keyword, and may not be used as an identifier
 *   class, true, null, goto, const  ERREUR : not a statement
 *   my-name, "my name"              ERREUR : ';' expected
 *
 * Le tableau RESERVED ci-dessous contient les mots reserves ET les 3
 * litteraux. "_" n'y est PAS : c'est au TODO 3 de le gerer.
 *
 *
 * ==================================================================
 * TODO 1 : isReserved(word)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Certains mots appartiennent a Java : on ne peut pas donner a son
 * chien le nom "class", comme on ne peut pas appeler son chien
 * "Police". On cherche le mot dans la liste.
 *
 * -- Essayons a la main --
 *
 *   "class" -> true ; "goto" -> true (reserve mais jamais utilise) ;
 *   "Class" -> false (majuscule : Java distingue la casse) ; "var" -> false
 *
 * -- Le plan --
 *
 *   1. Pour chaque mot de RESERVED : s'il est egal a word, true.
 *   2. Sinon false.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 3.
 *
 *
 * ==================================================================
 * TODO 2 : hasValidCharacters(word)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Java connait deja la liste des caracteres permis au debut et au
 * milieu d'un nom : Character.isJavaIdentifierStart et
 * Character.isJavaIdentifierPart. On les applique : le 1er caractere
 * avec la 1re regle, tous les autres avec la 2e.
 *
 * -- Essayons a la main --
 *
 *   "$price" -> '$' peut commencer, le reste est permis -> true
 *   "1value" -> '1' ne peut pas commencer -> false
 *   "my-name" -> '-' n'est jamais permis -> false
 *   "" -> false (pas de 1er caractere)
 *
 * -- Le plan --
 *
 *   1. Vide -> false.
 *   2. 1er caractere : isJavaIdentifierStart, sinon false.
 *   3. Chaque caractere suivant : isJavaIdentifierPart, sinon false.
 *   4. true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 3.
 *
 *
 * ==================================================================
 * TODO 3 : isValidIdentifier(word)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On combine tout : bons caracteres, pas reserve, et pas "_" tout seul.
 *
 * -- Le plan --
 *
 *   1. null -> false.
 *   2. "_" -> false.
 *   3. Sinon : hasValidCharacters ET pas isReserved.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : TODO 1 et 2.
 *
 *
 * Exemple a verifier : les 21 verdicts javac du tableau ci-dessus.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - for (String r : RESERVED) if (r.equals(word)) return true;
 *   - Character.isJavaIdentifierStart(word.charAt(0))
 *   - for (int i = 1; i < word.length(); i++) Character.isJavaIdentifierPart(word.charAt(i))
 *   - "_".equals(word)
 */
public class Exercise07_IdentifierValidator {

    public static final String[] RESERVED = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
            "volatile", "while", "true", "false", "null"};

    public static boolean isReserved(String word) {
        throw new UnsupportedOperationException("TODO 1 : implementer isReserved()");
    }

    public static boolean hasValidCharacters(String word) {
        throw new UnsupportedOperationException("TODO 2 : implementer hasValidCharacters()");
    }

    public static boolean isValidIdentifier(String word) {
        throw new UnsupportedOperationException("TODO 3 : implementer isValidIdentifier()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 isReserved : class et goto oui ; Class et var non",
                isReserved("class") && isReserved("goto") && !isReserved("Class") && !isReserved("var"));
        ExerciseChecker.check("2 hasValidCharacters : $price oui ; 1value, my-name, \"\" non",
                hasValidCharacters("$price") && !hasValidCharacters("1value") && !hasValidCharacters("my-name")
                        && !hasValidCharacters(""));

        String[] accepted = {"$price", "_value", "__", "$", "Class", "été", "var", "record", "yield", "sealed", "permits"};
        String[] rejected = {"1value", "_", "class", "true", "null", "goto", "const", "my-name", "my name", ""};
        boolean allAccepted = true;
        for (String word : accepted) {
            allAccepted &= isValidIdentifier(word);
        }
        boolean allRejected = true;
        for (String word : rejected) {
            allRejected &= !isValidIdentifier(word);
        }
        ExerciseChecker.check("3 accepte les 11 noms que javac accepte (dont var, record, ete)", allAccepted);
        ExerciseChecker.check("3 rejette les 10 noms que javac refuse (dont _ tout seul)", allRejected);
        ExerciseChecker.check("3 isValidIdentifier(null) == false", !isValidIdentifier(null));

        ExerciseChecker.summary();
    }
}
