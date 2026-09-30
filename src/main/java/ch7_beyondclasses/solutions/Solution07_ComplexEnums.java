package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise07_ComplexEnums.
 */
public class Solution07_ComplexEnums {

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
            // Une methode d'enum lit les champs fixes par le constructeur (toujours private) de SA constante.
            return G * mass / (radius * radius);
        }
    }

    enum Operation {
        ADD {
            @Override
            public int apply(int a, int b) {
                // Chaque constante redefinit la methode abstract de l'enum dans son propre corps.
                return a + b;
            }
        },
        SUBTRACT {
            @Override
            public int apply(int a, int b) {
                // Meme principe : une implementation differente par constante.
                return a - b;
            }
        };

        public abstract int apply(int a, int b);
    }
}
