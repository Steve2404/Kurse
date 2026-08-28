package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 16 - Classes locales et anonymes : acceder aux champs prives de l'englobante, MAIS depuis une methode d'INSTANCE (niveau : difficile)
 * =======================================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Dans l'Exercise11 (classes locales) et l'Exercise12 (classes
 * anonymes), les methodes englobantes etaient STATIC, et les classes
 * locales/anonymes ne capturaient que des PARAMETRES ou variables
 * locales - jamais un champ prive d'INSTANCE, puisqu'une methode
 * static n'a justement AUCUNE instance ("this") a proposer. Ici,
 * c'est different : secret est un champ prive de CETTE instance
 * precise de la classe englobante, et revealViaLocalClass() /
 * revealViaAnonymousClass() sont des methodes D'INSTANCE (pas
 * static) - CA, precisement, c'est ce qui permet a Revealer (classe
 * locale) et a la classe anonyme de lire secret directement, sans
 * aucun getter, exactement comme Badge le faisait dans l'Exercise10
 * (classe interne) - la regle vaut aussi pour les classes LOCALES et
 * ANONYMES, tant qu'elles sont definies a l'interieur d'une methode
 * D'INSTANCE.
 *
 *
 * ==================================================================
 * TODO 1 : Revealer.reveal() (classe locale, dans une methode d'instance)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer "Local class voit : " + secret - secret n'est PAS un
 *      parametre de revealViaLocalClass() : c'est le champ prive de
 *      L'INSTANCE englobante, lisible ici uniquement parce que
 *      revealViaLocalClass() est une methode d'instance.
 *
 *
 * ==================================================================
 * TODO 2 : la classe anonyme de revealViaAnonymousClass()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer "Classe anonyme voit : " + secret - meme principe,
 *      via une classe anonyme cette fois plutot qu'une classe locale
 *      nommee.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : avec instance = new
 * Exercise16_LocalAndAnonymousAccessingPrivateMembers("Toto"),
 * instance.revealViaLocalClass() == "Local class voit : Toto",
 * instance.revealViaAnonymousClass() == "Classe anonyme voit : Toto".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Si revealViaLocalClass() etait declaree "static", secret ne
 *     serait PLUS du tout accessible depuis Revealer : il faudrait
 *     alors le passer explicitement en parametre au constructeur de
 *     Revealer, comme une variable locale ordinaire (voir
 *     Exercise11) - une methode static n'a AUCUNE instance "this" a
 *     proposer aux classes qu'elle contient.
 */
public class Exercise16_LocalAndAnonymousAccessingPrivateMembers {

    private final String secret;

    public Exercise16_LocalAndAnonymousAccessingPrivateMembers(String secret) {
        this.secret = secret;
    }

    interface Revealable {
        String reveal();
    }

    public String revealViaLocalClass() {
        class Revealer {
            String reveal() {
                throw new UnsupportedOperationException("TODO 1 : implementer Revealer.reveal()");
            }
        }
        return new Revealer().reveal();
    }

    public String revealViaAnonymousClass() {
        Revealable revealable = new Revealable() {
            @Override
            public String reveal() {
                throw new UnsupportedOperationException("TODO 2 : implementer la classe anonyme");
            }
        };
        return revealable.reveal();
    }

    public static void main(String[] args) {
        Exercise16_LocalAndAnonymousAccessingPrivateMembers instance =
                new Exercise16_LocalAndAnonymousAccessingPrivateMembers("Toto");

        ExerciseChecker.check("classe locale (dans une methode d'instance) lit le champ prive secret",
                instance.revealViaLocalClass().equals("Local class voit : Toto"));

        ExerciseChecker.check("classe anonyme (dans une methode d'instance) lit le champ prive secret",
                instance.revealViaAnonymousClass().equals("Classe anonyme voit : Toto"));

        ExerciseChecker.summary();
    }
}
