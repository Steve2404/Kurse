package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

/**
 * EXERCICE 3 - Les 4 niveaux d'acces ecrits par toi, compares a 40 verdicts reels de javac (niveau : difficile)
 * ===========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une classe Parent (paquet pkgA) a 4 champs : private, package (aucun
 * mot), protected, public. Selon OU se trouve le code qui essaie de
 * les lire, javac dit oui ou non. Tu ecris la regle ; main() la compare
 * aux verdicts que javac 17 a VRAIMENT rendus sur 10 situations x 4
 * modificateurs (chaque situation a ete compilee avec de vrais fichiers).
 *
 * -- Les 10 situations (ou se trouve le code qui lit le champ) --
 *
 *   SAME_CLASS                         : dans Parent elle-meme
 *   NESTED_CLASS                       : dans une classe imbriquee de Parent
 *   SAME_FILE_OTHER_CLASS              : dans une AUTRE classe top-level du meme fichier
 *   SAME_PACKAGE                       : dans une classe de pkgA (autre fichier)
 *   SUBCLASS_OTHER_PACKAGE_THIS        : Child extends Parent (pkgB), via this.champ
 *   SUBCLASS_OTHER_PACKAGE_CHILD_REF   : Child (pkgB), via une variable de type Child
 *   SUBCLASS_OTHER_PACKAGE_PARENT_REF  : Child (pkgB), via une variable de type Parent
 *   SUBCLASS_OTHER_PACKAGE_STATIC      : Child (pkgB), champ STATIC lu par Parent.champ
 *   OTHER_PACKAGE                      : une classe de pkgB sans heritage
 *   OTHER_PACKAGE_STATIC               : une classe de pkgB sans heritage, champ STATIC via Parent.champ
 *
 * -- Messages reels de javac 17 --
 *
 *   private   -> error: priv has private access in Parent
 *   package   -> error: pack is not public in Parent; cannot be accessed from outside package
 *   protected -> error: prot has protected access in Parent
 *   Une classe sans public d'un autre paquet : error: Hidden is not public in pkgA; cannot be accessed from outside package
 *
 *
 * ==================================================================
 * TODO 1 : canAccess(modifier, situation)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 *   private   : seulement DANS la classe (et ses classes imbriquees).
 *   package   : tout le meme paquet.
 *   protected : tout le meme paquet, PLUS les sous-classes d'un autre
 *               paquet, mais seulement "en tant qu'heritiere" (this, une
 *               variable de SON type, ou un membre static).
 *   public    : partout.
 *
 * -- Essayons a la main --
 *
 *   ("private", "NESTED_CLASS")                        -> true
 *   ("private", "SAME_FILE_OTHER_CLASS")               -> false (meme fichier ne suffit pas !)
 *   ("protected", "SUBCLASS_OTHER_PACKAGE_PARENT_REF") -> false
 *   ("protected", "SUBCLASS_OTHER_PACKAGE_STATIC")     -> true
 *
 * -- Le plan --
 *
 *   1. boolean inside = SAME_CLASS ou NESTED_CLASS.
 *   2. boolean samePackage = inside ou SAME_FILE_OTHER_CLASS ou SAME_PACKAGE.
 *   3. boolean asHeir = SUBCLASS_OTHER_PACKAGE_THIS, _CHILD_REF ou _STATIC.
 *   4. switch sur modifier : private -> inside ; package -> samePackage ;
 *      protected -> samePackage || asHeir ; public -> true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : narrowest(situations...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La bonne pratique : donner le MOINS d'acces possible. On donne les
 * situations ou le champ DOIT etre lisible ; on rend le modificateur le
 * plus ferme qui marche partout : private, puis package, puis
 * protected, puis public.
 *
 * -- Essayons a la main --
 *
 *   (NESTED_CLASS)                             -> "private"
 *   (SAME_PACKAGE, SAME_CLASS)                 -> "package"
 *   (SUBCLASS_OTHER_PACKAGE_THIS)              -> "protected"
 *   (SUBCLASS_OTHER_PACKAGE_PARENT_REF)        -> "public"
 *
 * -- Le plan --
 *
 *   1. Pour chaque modificateur de MODIFIERS (du plus ferme au plus ouvert) :
 *      si canAccess est vrai pour TOUTES les situations, le rendre.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : canAccess (TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : errorMessage(modifier, field, owner)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ("private", "priv", "Parent")   -> "priv has private access in Parent"
 *   ("package", "pack", "Parent")   -> "pack is not public in Parent; cannot be accessed from outside package"
 *   ("protected", "prot", "Parent") -> "prot has protected access in Parent"
 *   ("public", ...)                 -> "" (jamais d'erreur)
 *
 * -- Le plan --
 *
 *   1. Un switch expression qui fabrique la phrase.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Comparer des String avec equals, ou utiliser un switch sur le String.
 */
public class Exercise03_AccessModifiersRules {

    public static final String[] MODIFIERS = {"private", "package", "protected", "public"};

    public static boolean canAccess(String modifier, String situation) {
        throw new UnsupportedOperationException("TODO 1 : implementer canAccess()");
    }

    public static String narrowest(String... situations) {
        throw new UnsupportedOperationException("TODO 2 : implementer narrowest()");
    }

    public static String errorMessage(String modifier, String field, String owner) {
        throw new UnsupportedOperationException("TODO 3 : implementer errorMessage()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 : private, package, protected, public (true = compile).
        Object[][] javac = {
                {"SAME_CLASS", true, true, true, true},
                {"NESTED_CLASS", true, true, true, true},
                {"SAME_FILE_OTHER_CLASS", false, true, true, true},
                {"SAME_PACKAGE", false, true, true, true},
                {"SUBCLASS_OTHER_PACKAGE_THIS", false, false, true, true},
                {"SUBCLASS_OTHER_PACKAGE_CHILD_REF", false, false, true, true},
                {"SUBCLASS_OTHER_PACKAGE_PARENT_REF", false, false, false, true},
                {"SUBCLASS_OTHER_PACKAGE_STATIC", false, false, true, true},
                {"OTHER_PACKAGE", false, false, false, true},
                {"OTHER_PACKAGE_STATIC", false, false, false, true}};
        int agree = 0;
        for (Object[] row : javac) {
            for (int m = 0; m < 4; m++) {
                if (canAccess(MODIFIERS[m], (String) row[0]) == (Boolean) row[m + 1]) {
                    agree++;
                } else {
                    System.out.println("   desaccord : " + MODIFIERS[m] + " / " + row[0]);
                }
            }
        }
        ExerciseChecker.check("canAccess() == javac sur 40 cas (" + agree + " d'accord)", agree == 40);

        ExerciseChecker.check("narrowest : private, package, protected, public",
                narrowest("NESTED_CLASS").equals("private") && narrowest("SAME_PACKAGE", "SAME_CLASS").equals("package")
                        && narrowest("SUBCLASS_OTHER_PACKAGE_THIS").equals("protected")
                        && narrowest("SUBCLASS_OTHER_PACKAGE_PARENT_REF").equals("public"));

        ExerciseChecker.check("errorMessage == les 3 messages de javac, et \"\" pour public",
                errorMessage("private", "priv", "Parent").equals("priv has private access in Parent")
                        && errorMessage("package", "pack", "Parent").equals("pack is not public in Parent; cannot be accessed from outside package")
                        && errorMessage("protected", "prot", "Parent").equals("prot has protected access in Parent")
                        && errorMessage("public", "pub", "Parent").isEmpty());

        ExerciseChecker.summary();
    }
}
