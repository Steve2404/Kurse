package classdesign.exercises;

import classdesign.ExerciseChecker;

/**
 * EXERCICE 1 - Heritage simple : ce qu'on recoit d'un parent, et ce qu'on recoit TOUJOURS d'Object (niveau : moyen)
 * ========================================================================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Java n'autorise qu'UN SEUL parent direct par classe ("extends
 * Vehicle", jamais "extends Vehicle, Autre" - contrairement aux
 * interfaces de l'Exercise01 du chapitre precedent, qui ELLES
 * permettent l'heritage multiple). En heritant de Vehicle, Car recoit
 * AUTOMATIQUEMENT l'acces a tout ce que Vehicle declare public ou
 * protected - et, comme Car et Vehicle sont ici dans le MEME paquet,
 * meme les membres "package" (sans aucun modificateur) restent
 * accessibles. ET, tout en haut de la chaine, MEME sans jamais ecrire
 * "extends Object" nulle part, TOUTE classe Java herite en silence de
 * java.lang.Object - c'est de LA que viennent toString(), equals(),
 * hashCode()... des methodes que Car n'a JAMAIS ecrites, mais qu'elle
 * possede quand meme.
 *
 *
 * ==================================================================
 * TODO 1 : Car.describe()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer "Car marque " + brand + " | " + publicInfo + " | " + packageInfo
 *      - brand (protected), publicInfo (public) et packageInfo
 *      (package, sans modificateur) sont TOUS les 3 herites de
 *      Vehicle, directement lisibles ici sans aucun getter.
 *
 *
 * ==================================================================
 * TODO 2 : isDefaultObjectEquality(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Car n'a JAMAIS ecrit sa propre methode equals() : elle utilise donc
 * TELLE QUELLE celle heritee d'Object, qui compare 2 objets par leur
 * IDENTITE (est-ce EXACTEMENT le meme objet en memoire ?), jamais par
 * leurs valeurs internes - meme 2 voitures avec EXACTEMENT la meme
 * marque restent "differentes" pour equals() tant que ce sont 2
 * INSTANCES separees.
 *
 * -- Le plan --
 *
 *   1. Renvoyer a.equals(a) && !a.equals(b) - un objet est TOUJOURS
 *      egal a lui-meme, mais jamais egal a un AUTRE objet, meme
 *      "identique en apparence", avec l'equals() par defaut d'Object.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : avec Car c = new Car(), c.describe() rend
 * "Car marque Toyota | Vehicule generique | Info de paquet".
 * isDefaultObjectEquality(new Car(), new Car()) == true (2 INSTANCES
 * differentes, meme si elles se ressemblent).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "class Car extends Vehicle" : un SEUL nom apres "extends",
 *     jamais une liste separee par des virgules (contrairement a
 *     "implements", qui lui accepte plusieurs interfaces).
 */
public class Exercise01_InheritanceBasics {

    static class Vehicle {
        protected String brand = "Toyota";
        public String publicInfo = "Vehicule generique";
        String packageInfo = "Info de paquet";

        public String honk() {
            return "Pouet";
        }
    }

    static class Car extends Vehicle {
        String describe() {
            throw new UnsupportedOperationException("TODO 1 : implementer describe()");
        }
    }

    public static boolean isDefaultObjectEquality(Car a, Car b) {
        throw new UnsupportedOperationException("TODO 2 : implementer isDefaultObjectEquality()");
    }

    public static void main(String[] args) {
        Car car = new Car();
        ExerciseChecker.check("describe() lit les champs protected/public/package herites",
                car.describe().equals("Car marque Toyota | Vehicule generique | Info de paquet"));
        ExerciseChecker.check("honk() (public, herite) reste appelable sur un Car",
                car.honk().equals("Pouet"));

        ExerciseChecker.check("equals() par defaut d'Object compare par IDENTITE, pas par valeur",
                isDefaultObjectEquality(new Car(), new Car()));

        ExerciseChecker.summary();
    }
}
