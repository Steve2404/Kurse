package beyondclasses.exercises;

import beyondclasses.ExerciseChecker;

/**
 * EXERCICE 3 - Methodes static et private d'interface : la boite a outils publique vs les astuces secretes (niveau : difficile)
 * ====================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine l'atelier d'un menuisier. Certains outils sont EXPOSES sur
 * l'etabli, accessibles a n'importe qui qui vient a l'atelier : ce
 * sont les methodes STATIC de l'interface (MathHelper.square(3),
 * appelees par le NOM de l'interface, jamais par une instance -
 * contrairement aux constantes de l'Exercise01, elles ne sont PAS
 * "heritees" sans prefixe). D'autres outils restent dans le TIROIR
 * SECRET du menuisier, jamais montres aux clients, mais qu'IL
 * utilise en coulisses pour fabriquer ses propres objets : ce sont
 * les methodes PRIVATE (et PRIVATE STATIC) de l'interface - invisibles
 * depuis l'exterieur, meme pour une classe qui implemente
 * l'interface, mais reutilisables PAR l'interface elle-meme pour
 * eviter de recopier du code entre plusieurs default/static methods.
 *
 * REGLE IMPORTANTE : une methode static ne peut appeler QUE d'autres
 * membres static (elle n'a pas d'instance "this" a disposition) -
 * donc une methode private static, elle aussi sans "this". Une
 * methode default (ou private NON static), elle, a un "this" et peut
 * appeler n'importe quel membre, static ou non.
 *
 *
 * ==================================================================
 * TODO 1 : MathHelper.squarePlusDouble(n) (methode static)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * square(3) vaut 9. doubleIt(3) (private static, deja fournie plus
 * bas) vaut 6. squarePlusDouble(3) doit donc valoir 15.
 *
 * -- Le plan --
 *
 *   1. Renvoyer square(n) + doubleIt(n) - les 2 sont des methodes
 *      static de la MEME interface, appelables directement par leur
 *      nom depuis une autre methode static.
 *
 *
 * ==================================================================
 * TODO 2 : MathHelper.scoreFor(n) (methode default)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * square(3) vaut 9. privateBonus(3) (private NON static, deja fournie
 * plus bas) vaut 3 + 1 = 4. scoreFor(3) doit donc valoir 9 - 4 = 5.
 *
 * -- Le plan --
 *
 *   1. Renvoyer square(n) - privateBonus(n) - remarque que scoreFor()
 *      (default, PAS static) peut appeler AUSSI bien square() (static)
 *      que privateBonus() (non static) : une methode default a acces
 *      a TOUT, contrairement a une methode static.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne, et les vraies "boites separees"
 * (doubleIt, privateBonus) sont deja fournies - c'est justement LE
 * PROPOS de cet exercice de les reutiliser, pas de les recrire.
 *
 * Exemple a verifier : MathHelper.squarePlusDouble(3) == 15. Une
 * classe QUELCONQUE qui implemente MathHelper (Impl, fournie plus
 * bas, vide) : new Impl().scoreFor(3) == 5.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - MathHelper.squarePlusDouble(3) : methode static, appelee par le
 *     NOM de l'interface (comme une methode static de classe).
 *   - new Impl().scoreFor(3) : methode default, appelee sur une
 *     INSTANCE (ici Impl, qui n'a besoin de RIEN ecrire de plus - le
 *     default suffit).
 *   - doubleIt() et privateBonus() ne sont PAS testables directement
 *     depuis main() : elles sont private, invisibles depuis
 *     l'exterieur de l'interface - c'est voulu.
 */
public class Exercise03_StaticAndPrivateInterfaceMethods {

    interface MathHelper {
        static int square(int n) {
            return n * n;
        }

        private static int doubleIt(int n) {
            return n * 2;
        }

        static int squarePlusDouble(int n) {
            throw new UnsupportedOperationException("TODO 1 : implementer squarePlusDouble()");
        }

        private int privateBonus(int n) {
            return n + 1;
        }

        default int scoreFor(int n) {
            throw new UnsupportedOperationException("TODO 2 : implementer scoreFor()");
        }
    }

    static class Impl implements MathHelper {
    }

    public static void main(String[] args) {
        ExerciseChecker.check("squarePlusDouble() (static) combine square() et doubleIt() (les 2 static)",
                MathHelper.squarePlusDouble(3) == 15);

        ExerciseChecker.check("scoreFor() (default) combine square() (static) et privateBonus() (non static)",
                new Impl().scoreFor(3) == 5);

        ExerciseChecker.summary();
    }
}
