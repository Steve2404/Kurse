package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 7 - Simuler l'ordre d'initialisation, compare au journal de VRAIES classes (niveau : avance)
 * ===================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand on ecrit "new Enfant()", Java construit dans un ordre precis :
 *
 *   1. (une seule fois, au premier usage) le static du PARENT, puis le static de l'ENFANT,
 *      chacun dans l'ordre du code source ;
 *   2. les champs et blocs d'instance du PARENT (dans l'ordre du code), puis le constructeur du PARENT ;
 *   3. les champs et blocs d'instance de l'ENFANT (dans l'ordre du code), puis le constructeur de l'ENFANT.
 *
 * Au 2e "new", l'etape 1 ne recommence PAS : la classe est deja chargee.
 *
 * Plus bas (apres main), de vraies classes Base et Derived ecrivent
 * dans LOG chaque etape. Tu ecris le SIMULATEUR ; main() compare ta
 * simulation au vrai journal.
 *
 * Le piege celebre : si le constructeur du parent appelle une methode
 * redefinie par l'enfant, cette methode voit les champs de l'enfant
 * encore a leur valeur PAR DEFAUT (0, null, false...), car l'etape 3
 * n'a pas encore eu lieu.
 *
 *
 * ==================================================================
 * TODO 1 : simulate(parent, child, alreadyLoaded)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque classe est decrite par un ClassParts : ses morceaux static
 * (dans l'ordre), ses morceaux d'instance (champs et blocs, dans
 * l'ordre) et le texte de son constructeur. On rend la liste des
 * etapes dans l'ordre ou Java les execute.
 *
 * -- Essayons a la main --
 *
 *   parent  : static [PS], instance [PF, PB], ctor PC
 *   enfant  : static [CS], instance [CB, CF], ctor CC
 *   1er new -> [PS, CS, PF, PB, PC, CB, CF, CC]
 *   2e new  -> [PF, PB, PC, CB, CF, CC]
 *
 * -- Le plan --
 *
 *   1. Si !alreadyLoaded : ajouter parent.statics puis child.statics.
 *   2. Ajouter parent.instance, puis parent.constructor.
 *   3. Ajouter child.instance, puis child.constructor.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : defaultValueOf(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La valeur d'un champ AVANT son initialisation, en texte : "0" pour
 * int/long/short/byte, "0.0" pour double/float, "false" pour boolean,
 * le caractere nul pour char (on rend "\u0000"), "null" pour tout le
 * reste (String, objets). main() verifie avec le piege reel : le
 * constructeur de Base appelle describe(), redefinie dans Derived, qui
 * lit un champ int et un champ String pas encore initialises.
 *
 * -- Le plan --
 *
 *   1. Un switch expression sur le type.
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
 *   - list.addAll(autreListe) ajoute tous les elements dans l'ordre.
 */
public class Exercise07_InitializationTracer {

    public record ClassParts(List<String> statics, List<String> instance, String constructor) {
    }

    public static List<String> simulate(ClassParts parent, ClassParts child, boolean alreadyLoaded) {
        throw new UnsupportedOperationException("TODO 1 : implementer simulate()");
    }

    public static String defaultValueOf(String type) {
        throw new UnsupportedOperationException("TODO 2 : implementer defaultValueOf()");
    }

    public static void main(String[] args) {
        ClassParts parent = new ClassParts(List.of("PS"), List.of("PF", "PB"), "PC");
        ClassParts child = new ClassParts(List.of("CS"), List.of("CB", "CF"), "CC");
        ExerciseChecker.check("simulate, exemple du plan (1er new puis 2e new)",
                simulate(parent, child, false).equals(List.of("PS", "CS", "PF", "PB", "PC", "CB", "CF", "CC"))
                        && simulate(parent, child, true).equals(List.of("PF", "PB", "PC", "CB", "CF", "CC")));

        // Les VRAIES classes, decrites comme dans leur code source (voir plus bas).
        ClassParts base = new ClassParts(List.of("Base static 1", "Base static 2"),
                List.of("Base field", "Base block"), "Base ctor");
        ClassParts derived = new ClassParts(List.of("Derived static"),
                List.of("Derived block", "Derived field"), "Derived ctor");

        LOG.clear();
        new Derived();
        List<String> firstReal = new ArrayList<>(LOG);
        LOG.clear();
        new Derived();
        List<String> secondReal = new ArrayList<>(LOG);
        ExerciseChecker.check("1er new Derived() : simulation == vrai journal " + firstReal, simulate(base, derived, false).equals(firstReal));
        ExerciseChecker.check("2e new Derived() : le static ne recommence pas " + secondReal, simulate(base, derived, true).equals(secondReal));

        ExerciseChecker.check("defaultValueOf : 0, 0.0, false, null, caractere nul",
                defaultValueOf("int").equals("0") && defaultValueOf("double").equals("0.0") && defaultValueOf("boolean").equals("false")
                        && defaultValueOf("String").equals("null") && defaultValueOf("char").equals("\u0000"));
        ExerciseChecker.check("le piege reel : describe() pendant le constructeur de Base a vu " + Derived.seenDuringParentConstructor,
                Derived.seenDuringParentConstructor.equals("size=" + defaultValueOf("int") + ", label=" + defaultValueOf("String")));

        ExerciseChecker.summary();
    }

    // Deja ecrit : le journal et les vraies classes (ne pas modifier).
    static final List<String> LOG = new ArrayList<>();

    static String log(String step) {
        LOG.add(step);
        return step;
    }

    static class Base {
        static {
            log("Base static 1");
        }

        static String staticField = log("Base static 2");

        String field = log("Base field");

        {
            log("Base block");
        }

        Base() {
            log("Base ctor");
            describe();
        }

        void describe() {
        }
    }

    static class Derived extends Base {
        static String seenDuringParentConstructor;

        static {
            log("Derived static");
        }

        {
            log("Derived block");
        }

        int size = 5;
        String label = log("Derived field");

        Derived() {
            log("Derived ctor");
        }

        @Override
        void describe() {
            if (seenDuringParentConstructor == null) {
                seenDuringParentConstructor = "size=" + size + ", label=" + label;
            }
        }
    }
}
