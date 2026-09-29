package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * EXERCICE 2 - La signature de main() : ca COMPILE toujours, mais ca se LANCE seulement si c'est la bonne (niveau : moyen/difficile)
 * ===================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Le piege central --
 *
 * Java n'exige JAMAIS qu'une classe ait un main() valide pour
 * COMPILER. Une methode NOMMEE "main" avec une MAUVAISE signature
 * (pas static, private, qui renvoie un int...) compile parfaitement :
 * c'est juste une methode ordinaire. L'erreur n'apparait qu'au
 * LANCEMENT (java NomDeClasse). La preuve : CE fichier contient 8
 * petites classes, dont 5 avec un main() "rate"... et il compile.
 *
 * Messages REELS du lanceur java 17 (en anglais), verifies en direct :
 *
 *   pas static : Error: Main method is not static in class C, please define the main method as:
 *   private    : Error: Main method not found in class D, please define the main method as:
 *   int        : Error: Main method must return a value of type void in class E, please ...
 *   aucun main : Error: Main method not found in class N, please define the main method as:
 *
 * (Sur une machine allemande, le lanceur les affiche en allemand :
 * "Fehler: Hauptmethode ist nicht static in Klasse C..." - meme regle.)
 *
 * Les 3 signatures VALIDES : public static void main(String[] args),
 * public static void main(String... args), public static void
 * main(String args[]) - et les modificateurs "final" ou l'ordre
 * "final static public" sont permis aussi.
 *
 * Ici, TU vas ecrire le "controleur" que le lanceur java execute :
 * on lui donne une classe, il dit si elle est lancable, et sinon
 * quel message il afficherait. Pour regarder une classe "de
 * l'interieur", on utilise la REFLEXION (Class, Method, Modifier) :
 * ce n'est pas au programme de l'examen, les indices en bas donnent
 * tout ce qu'il faut.
 *
 *
 * ==================================================================
 * TODO 1 : findMain(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le lanceur cherche UNE porte d'entree precise : une methode qui
 * s'appelle "main" ET qui prend un tableau de String. Une methode
 * main(String) (un seul String, pas un tableau) n'est PAS cette
 * porte : c'est une autre methode, qui porte juste le meme nom.
 *
 * -- Essayons a la main --
 *
 *   ValidVarargs : main(String... args) -> String... EST un String[] -> trouvee
 *   WrongParam   : main(String arg)     -> pas de main(String[])     -> null
 *   NoMain       : aucune methode main   -> null
 *
 * -- Le plan --
 *
 *   1. Demander a la classe SA methode declaree "main" avec un
 *      parametre String[].
 *   2. Si elle n'existe pas, rendre null.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'EST une boite magique : les TODO 2 et 3 l'utilisent.
 *
 *
 * ==================================================================
 * TODO 2 : diagnose(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le gardien de la porte verifie 3 choses, dans cet ordre :
 *   1. la porte existe et on a le droit de l'ouvrir (public) ?
 *      sinon "INTROUVABLE" (une porte private, c'est comme pas de
 *      porte du tout pour lui) ;
 *   2. elle s'ouvre sans objet (static) ? sinon "PAS_STATIC" (le
 *      lanceur n'a encore cree AUCUN objet quand il demarre) ;
 *   3. elle ne rend rien (void) ? sinon "PAS_VOID".
 * Si tout est bon : "LANCABLE".
 *
 * -- Essayons a la main --
 *
 *   ValidArray, ValidVarargs, ValidCStyle -> LANCABLE
 *   NotStatic (public void main)          -> PAS_STATIC
 *   PrivateMain (private static void)     -> INTROUVABLE
 *   ReturnsInt (public static int)        -> PAS_VOID
 *   NoMain, WrongParam                    -> INTROUVABLE
 *
 * -- Le plan --
 *
 *   1. Chercher la methode (TODO 1).
 *   2. Absente OU pas public -> "INTROUVABLE".
 *   3. Pas static -> "PAS_STATIC".
 *   4. Type de retour different de void -> "PAS_VOID".
 *   5. Sinon -> "LANCABLE".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : findMain (TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : launcherMessage(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On traduit le diagnostic en phrase, comme le lanceur (en version
 * courte, sans le "please define..." qui suit).
 *
 * -- Essayons a la main --
 *
 *   LANCABLE    -> "OK"
 *   INTROUVABLE -> "Main method not found in class NoMain"
 *   PAS_STATIC  -> "Main method is not static in class NotStatic"
 *   PAS_VOID    -> "Main method must return a value of type void in class ReturnsInt"
 *
 * -- Le plan --
 *
 *   1. Diagnostiquer (TODO 2).
 *   2. Choisir la phrase selon le diagnostic, en ajoutant le nom
 *      simple de la classe.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : diagnose (TODO 2). Un switch sur le diagnostic est ideal.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main" ci-dessus, pour
 * les 8 classes imbriquees de ce fichier.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - try { return type.getDeclaredMethod("main", String[].class); }
 *     catch (NoSuchMethodException e) { return null; }
 *   - int mods = m.getModifiers(); Modifier.isPublic(mods), Modifier.isStatic(mods)
 *   - m.getReturnType() == void.class
 *   - type.getSimpleName() -> "NoMain"
 */
public class Exercise02_MainMethodSignature {

    static class ValidArray {
        public static void main(String[] args) {
        }
    }

    static class ValidVarargs {
        public static void main(String... args) {
        }
    }

    static class ValidCStyle {
        final static public void main(final String args[]) {
        }
    }

    static class NotStatic {
        public void main(String[] args) {
        }
    }

    static class PrivateMain {
        private static void main(String[] args) {
        }
    }

    static class ReturnsInt {
        public static int main(String[] args) {
            return 0;
        }
    }

    static class NoMain {
    }

    static class WrongParam {
        public static void main(String arg) {
        }
    }

    public static Method findMain(Class<?> type) {
        throw new UnsupportedOperationException("TODO 1 : implementer findMain()");
    }

    public static String diagnose(Class<?> type) {
        throw new UnsupportedOperationException("TODO 2 : implementer diagnose()");
    }

    public static String launcherMessage(Class<?> type) {
        throw new UnsupportedOperationException("TODO 3 : implementer launcherMessage()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 findMain(ValidVarargs) trouve main(String...)", findMain(ValidVarargs.class) != null);
        ExerciseChecker.check("1 findMain(WrongParam) == null (main(String) n'est pas main(String[]))",
                findMain(WrongParam.class) == null);
        ExerciseChecker.check("1 findMain(NoMain) == null", findMain(NoMain.class) == null);

        ExerciseChecker.check("2 ValidArray / ValidVarargs / ValidCStyle -> LANCABLE",
                diagnose(ValidArray.class).equals("LANCABLE") && diagnose(ValidVarargs.class).equals("LANCABLE")
                        && diagnose(ValidCStyle.class).equals("LANCABLE"));
        ExerciseChecker.check("2 NotStatic -> PAS_STATIC", diagnose(NotStatic.class).equals("PAS_STATIC"));
        ExerciseChecker.check("2 PrivateMain -> INTROUVABLE", diagnose(PrivateMain.class).equals("INTROUVABLE"));
        ExerciseChecker.check("2 ReturnsInt -> PAS_VOID", diagnose(ReturnsInt.class).equals("PAS_VOID"));
        ExerciseChecker.check("2 NoMain et WrongParam -> INTROUVABLE",
                diagnose(NoMain.class).equals("INTROUVABLE") && diagnose(WrongParam.class).equals("INTROUVABLE"));

        ExerciseChecker.check("3 launcherMessage(ValidArray) == OK", launcherMessage(ValidArray.class).equals("OK"));
        ExerciseChecker.check("3 launcherMessage(NoMain)",
                launcherMessage(NoMain.class).equals("Main method not found in class NoMain"));
        ExerciseChecker.check("3 launcherMessage(NotStatic)",
                launcherMessage(NotStatic.class).equals("Main method is not static in class NotStatic"));
        ExerciseChecker.check("3 launcherMessage(ReturnsInt)",
                launcherMessage(ReturnsInt.class).equals("Main method must return a value of type void in class ReturnsInt"));
        ExerciseChecker.check("3 launcherMessage(PrivateMain)",
                launcherMessage(PrivateMain.class).equals("Main method not found in class PrivateMain"));

        ExerciseChecker.summary();
    }
}
