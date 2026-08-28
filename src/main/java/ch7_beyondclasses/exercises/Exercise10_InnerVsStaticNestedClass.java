package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 10 - Classe interne (inner) vs classe imbriquee statique (static nested) (niveau : moyen)
 * =========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une classe INTERNE (sans "static"), c'est comme le badge d'un
 * employe : ce badge n'existe QUE parce qu'une ENTREPRISE (une
 * instance de la classe englobante) existe deja - impossible de
 * fabriquer un badge "dans le vide", sans entreprise pour le
 * delivrer. C'est pour ca que la syntaxe pour creer un Badge est
 * "outer.new Badge()" (ou outer est deja une instance existante) et
 * PAS juste "new Badge()". En echange de cette contrainte, le Badge a
 * un GROS avantage : il peut lire directement les champs PRIVES de
 * l'entreprise qui l'a delivre, meme sans getter, comme s'ils etaient
 * les siens.
 *
 * Une classe imbriquee STATIC, elle, n'a besoin d'AUCUNE instance
 * englobante : "new Standalone(...)" tout court suffit, exactement
 * comme une classe normale - elle est juste RANGEE a l'interieur
 * d'une autre classe pour des raisons d'organisation, sans aucun lien
 * de dependance avec une instance particuliere.
 *
 *
 * ==================================================================
 * TODO 1 : Badge.print() (classe INTERNE, non-static)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer "Badge de " + owner - owner est un champ PRIVE de la
 *      classe englobante (Exercise10_InnerVsStaticNestedClass),
 *      directement lisible ici SANS aucun getter, precisement parce
 *      que Badge est une classe INTERNE (pas static).
 *
 *
 * ==================================================================
 * TODO 2 : Standalone.print() (classe imbriquee STATIC)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer "Badge independant : " + label (son PROPRE champ,
 *      recu dans SON PROPRE constructeur - aucun lien avec une
 *      instance englobante).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : avec outer = new
 * Exercise10_InnerVsStaticNestedClass("Steve"),
 * outer.new Badge().print() == "Badge de Steve". new
 * Standalone("VIP").print() == "Badge independant : VIP" (AUCUN outer
 * necessaire pour celle-la).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "outer.new Badge()" : syntaxe SPECIALE, obligatoire pour
 *     instancier une classe interne depuis l'exterieur de la classe
 *     englobante - jamais "new Outer.Badge()" (ca, c'est la syntaxe
 *     pour une classe imbriquee STATIC).
 */
public class Exercise10_InnerVsStaticNestedClass {

    private final String owner;

    public Exercise10_InnerVsStaticNestedClass(String owner) {
        this.owner = owner;
    }

    class Badge {
        String print() {
            throw new UnsupportedOperationException("TODO 1 : implementer Badge.print()");
        }
    }

    static class Standalone {
        private final String label;

        Standalone(String label) {
            this.label = label;
        }

        String print() {
            throw new UnsupportedOperationException("TODO 2 : implementer Standalone.print()");
        }
    }

    public static void main(String[] args) {
        Exercise10_InnerVsStaticNestedClass outer = new Exercise10_InnerVsStaticNestedClass("Steve");
        Exercise10_InnerVsStaticNestedClass.Badge badge = outer.new Badge();
        ExerciseChecker.check("Badge (classe interne) lit directement le champ prive owner",
                badge.print().equals("Badge de Steve"));

        Standalone standalone = new Standalone("VIP");
        ExerciseChecker.check("Standalone (static) n'a besoin d'AUCUNE instance englobante",
                standalone.print().equals("Badge independant : VIP"));

        ExerciseChecker.summary();
    }
}
