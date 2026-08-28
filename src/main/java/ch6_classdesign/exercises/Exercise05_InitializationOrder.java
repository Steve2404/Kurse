package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 5 - L'ordre d'initialisation, PREDIT a la main puis verifie contre le VRAI comportement (niveau : difficile)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * classdesign.exercises.Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Construire un Child, c'est comme monter un immeuble a 2 etages :
 * on ne peut JAMAIS commencer le 2e etage (Child) avant que le REZ-
 * DE-CHAUSSEE (Parent) soit ENTIEREMENT termine, du sol au plafond.
 * Et a CHAQUE etage, il y a 2 phases bien separees : d'abord tout ce
 * qui est "collectif" (les champs static et blocs static - ils ne se
 * construisent QU'UNE SEULE FOIS pour TOUT le batiment, jamais
 * refaits a chaque nouvel appartement), PUIS tout ce qui est "prive a
 * CET appartement precis" (les champs d'instance, les blocs
 * d'instance, et enfin le constructeur - CES LA se rejouent a CHAQUE
 * new). L'ordre EXACT, du tout premier au tout dernier evenement pour
 * "new Child()" :
 *
 *   1. static de Parent (dans l'ordre ou ils apparaissent dans le fichier)
 *   2. static de Child (dans l'ordre ou ils apparaissent)
 *   3. instance de Parent (dans l'ordre d'apparition), PUIS le constructeur de Parent
 *   4. instance de Child (dans l'ordre d'apparition), PUIS le constructeur de Child
 *
 *
 * ==================================================================
 * TODO : buildExpectedOrder()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. En lisant Parent et Child plus bas (chaque ligne "log.add(...)"
 *      y est deja ecrite, RIEN a completer la-dedans), ecrire a la
 *      main, dans le BON ordre, les 10 libelles exacts qui seront
 *      empiles dans le log lors du tout premier "new Child()" du
 *      programme.
 *   2. Les renvoyer dans une List<String>, dans cet ordre precis.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit - c'est un exercice de PREDICTION,
 * pas de calcul.
 *
 * Exemple a verifier : buildExpectedOrder() doit correspondre
 * EXACTEMENT au vrai log rempli par new Child() (voir main() plus
 * bas, deja fourni) - s'ils ne correspondent pas, c'est que l'ordre
 * predit est faux quelque part.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Les blocs static/instance de Parent et Child ne dependent PAS
 *     de leur position dans le fichier par rapport aux champs -
 *     c'est l'ordre D'APPARITION DANS LE CODE (static var puis static
 *     block ici, mais ca pourrait etre l'inverse selon comment c'est
 *     ecrit) qui compte, jamais un ordre "logique" suppose.
 */
public class Exercise05_InitializationOrder {

    static class Parent {
        static List<String> log = new ArrayList<>();

        static int staticVar = logStatic("Parent.staticVar");
        static {
            log.add("Parent.staticBlock");
        }

        int instanceVar = logInstance("Parent.instanceVar");
        {
            log.add("Parent.instanceBlock");
        }

        Parent() {
            log.add("Parent.constructor");
        }

        static int logStatic(String s) {
            log.add(s);
            return 0;
        }

        int logInstance(String s) {
            log.add(s);
            return 0;
        }
    }

    static class Child extends Parent {
        static int staticVar2 = logStatic("Child.staticVar");
        static {
            log.add("Child.staticBlock");
        }

        int instanceVar2 = logInstance("Child.instanceVar");
        {
            log.add("Child.instanceBlock");
        }

        Child() {
            log.add("Child.constructor");
        }
    }

    public static List<String> buildExpectedOrder() {
        throw new UnsupportedOperationException("TODO : implementer buildExpectedOrder()");
    }

    public static void main(String[] args) {
        List<String> predicted = buildExpectedOrder();

        Child child = new Child();
        List<String> real = Parent.log;

        ExerciseChecker.check("l'ordre PREDIT correspond EXACTEMENT au vrai log de new Child()",
                predicted.equals(real));

        ExerciseChecker.summary();
    }
}
