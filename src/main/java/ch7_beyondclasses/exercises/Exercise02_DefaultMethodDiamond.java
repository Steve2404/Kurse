package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 2 - Methodes default : le "diamant" de l'heritage multiple, et un default qui appelle un abstract (niveau : difficile)
 * ======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * jdbc... non, voir beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une methode "default" a un CORPS (contrairement a une methode
 * abstraite) : une classe qui implemente l'interface n'est donc PAS
 * OBLIGEE de la reecrire, elle "herite" du comportement par defaut.
 * MAIS : si un Robot signe DEUX contrats differents (Greeter ET
 * Waver) qui proposent CHACUN un default greet(String) avec la MEME
 * signature, Java refuse de deviner LEQUEL des deux tu voulais - ce
 * serait comme avoir 2 grands-parents qui te donnent CHACUN un
 * conseil different sous le MEME nom "le conseil du jour" : impossible
 * de savoir lequel suivre sans le demander explicitement. Le
 * compilateur t'OBLIGE alors a ecrire toi-meme greet(String) dans
 * Robot, et te laisse a l'interieur choisir : NomDeLInterface.super.methode(...)
 * te permet d'appeler PRECISEMENT le default de l'interface que tu
 * nommes, comme dire "je choisis le conseil de CE grand-parent-la".
 *
 *
 * ==================================================================
 * TODO 1 : Robot.greet(name)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Greeter.greet("Ada") vaut "Bonjour Ada". Waver.greet("Ada") vaut
 * "Coucou Ada". On veut que Robot COMBINE les 2, plutot que de
 * choisir arbitrairement l'un ou l'autre.
 *
 * -- Le plan --
 *
 *   1. Renvoyer Greeter.super.greet(name) + " / " + Waver.super.greet(name).
 *
 *
 * ==================================================================
 * TODO 2 : Robot.name()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Describable propose un default describe() qui appelle name() - MAIS
 * name() elle-meme n'a PAS de corps dans Describable (elle est
 * abstraite) : Describable ne sait pas QUEL nom donner, elle fait
 * juste CONFIANCE a la classe qui l'implementera pour le lui dire.
 * C'est une chaine : le default describe() s'appuie sur une methode
 * que TOI seul, dans Robot, sais completer.
 *
 * -- Le plan --
 *
 *   1. Renvoyer le champ label deja stocke dans Robot.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : new Robot("R2D2").greet("Ada") ==
 * "Bonjour Ada / Coucou Ada". new Robot("R2D2").describe() ==
 * "Je suis R2D2" (describe() n'est PAS a reecrire : c'est le default
 * de Describable, deja fourni, qui appelle notre name()).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Sans le NomDeLInterface.super.methode(...), appeler juste
 *     super.greet(name) ne compile PAS ici : "super" tout seul ne
 *     marche que pour l'heritage de CLASSE (un seul parent possible),
 *     jamais pour choisir entre PLUSIEURS default methods
 *     d'interfaces en conflit.
 */
public class Exercise02_DefaultMethodDiamond {

    interface Greeter {
        default String greet(String name) {
            return "Bonjour " + name;
        }
    }

    interface Waver {
        default String greet(String name) {
            return "Coucou " + name;
        }
    }

    interface Describable {
        String name(); // abstrait : chaque classe doit le completer

        default String describe() {
            return "Je suis " + name(); // un default PEUT appeler une methode abstraite
        }
    }

    static class Robot implements Greeter, Waver, Describable {
        private final String label;

        Robot(String label) {
            this.label = label;
        }

        @Override
        public String greet(String name) {
            throw new UnsupportedOperationException("TODO 1 : implementer greet()");
        }

        @Override
        public String name() {
            throw new UnsupportedOperationException("TODO 2 : implementer name()");
        }
    }

    public static void main(String[] args) {
        Robot robot = new Robot("R2D2");

        ExerciseChecker.check("greet() combine les 2 default methods via NomInterface.super",
                robot.greet("Ada").equals("Bonjour Ada / Coucou Ada"));

        ExerciseChecker.check("describe() (default deja fourni) s'appuie sur notre name()",
                robot.describe().equals("Je suis R2D2"));

        ExerciseChecker.summary();
    }
}
