package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 5 - Enums complexes : constructeur/champs/methodes, et une methode abstraite par valeur (niveau : difficile)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un enum "complexe" n'est pas QUE une liste de noms : chaque valeur
 * peut porter ses PROPRES informations, comme une fiche d'identite.
 * PLANET.EARTH n'est pas juste un mot, c'est un objet qui connait sa
 * masse ET son rayon, parce qu'on les lui a donnes AU MOMENT de sa
 * creation, via un CONSTRUCTEUR (implicitement PRIVATE - impossible
 * de creer une planete supplementaire depuis l'exterieur avec "new
 * Planet(...)", contrairement a une classe normale : la LISTE
 * complete des planetes est figee une fois pour toutes dans le fichier
 * de l'enum lui-meme).
 *
 * Encore plus fort : une methode d'enum peut etre ABSTRACT. Dans ce
 * cas, CHAQUE valeur (ADD, SUBTRACT...) doit fournir SON PROPRE corps
 * de methode, comme si chaque valeur etait une mini sous-classe
 * anonyme - exactement comme des freres et soeurs qui partagent le
 * meme nom de famille (Operation), mais dont CHACUN sait faire un
 * calcul different quand on lui demande apply(a, b).
 *
 *
 * ==================================================================
 * TODO 1 : Planet.surfaceGravity()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * La gravite de surface se calcule avec G * masse / rayon^2, ou G =
 * 6.67300E-11 (deja fournie comme constante). EARTH a une masse de
 * 5.976e+24 et un rayon de 6.37814e6 : sa gravite de surface vaut
 * environ 9.8.
 *
 * -- Le plan --
 *
 *   1. Renvoyer G * mass / (radius * radius).
 *
 *
 * ==================================================================
 * TODO 2 : Operation.ADD.apply(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer a + b.
 *
 *
 * ==================================================================
 * TODO 3 : Operation.SUBTRACT.apply(a, b)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer a - b.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : Planet.EARTH.surfaceGravity() est proche de
 * 9.8 (a 0.1 pres). Operation.ADD.apply(3, 4) == 7,
 * Operation.SUBTRACT.apply(10, 4) == 6.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Le constructeur d'un enum est TOUJOURS private (meme en
 *     ecrivant juste "Planet(double mass, double radius) { ... }"
 *     sans le mot-cle - il est IMPOSSIBLE de le declarer public).
 *   - "ADD { public int apply(int a, int b) { ... } }," : le corps de
 *     methode s'ecrit ENTRE ACCOLADES juste apres le nom de la
 *     valeur, avant la virgule qui separe les valeurs suivantes.
 *   - Puisque Operation declare une methode abstraite, CHAQUE valeur
 *     de l'enum DOIT fournir son propre corps - en oublier UNE seule
 *     fait planter la COMPILATION de tout l'enum, pas juste un test.
 */
public class Exercise05_ComplexEnums {

    enum Planet {
        MERCURY(3.303e+23, 2.4397e6),
        VENUS(4.869e+24, 6.0518e6),
        EARTH(5.976e+24, 6.37814e6);

        private static final double G = 6.67300E-11;

        private final double mass;
        private final double radius;

        Planet(double mass, double radius) {
            this.mass = mass;
            this.radius = radius;
        }

        double surfaceGravity() {
            throw new UnsupportedOperationException("TODO 1 : implementer surfaceGravity()");
        }
    }

    enum Operation {
        ADD {
            @Override
            public int apply(int a, int b) {
                throw new UnsupportedOperationException("TODO 2 : implementer ADD.apply()");
            }
        },
        SUBTRACT {
            @Override
            public int apply(int a, int b) {
                throw new UnsupportedOperationException("TODO 3 : implementer SUBTRACT.apply()");
            }
        };

        public abstract int apply(int a, int b);
    }

    public static void main(String[] args) {
        double earthGravity = Planet.EARTH.surfaceGravity();
        ExerciseChecker.check("Planet.EARTH.surfaceGravity() est proche de 9.8 -> " + earthGravity,
                Math.abs(earthGravity - 9.8) < 0.1);

        ExerciseChecker.check("Operation.ADD.apply(3, 4) == 7", Operation.ADD.apply(3, 4) == 7);
        ExerciseChecker.check("Operation.SUBTRACT.apply(10, 4) == 6", Operation.SUBTRACT.apply(10, 4) == 6);

        ExerciseChecker.summary();
    }
}
