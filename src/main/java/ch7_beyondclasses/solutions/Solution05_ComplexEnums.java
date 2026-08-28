package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise05_ComplexEnums.
 */
public class Solution05_ComplexEnums {

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
            return G * mass / (radius * radius);
        }
    }

    enum Operation {
        ADD {
            @Override
            public int apply(int a, int b) {
                return a + b;
            }
        },
        SUBTRACT {
            @Override
            public int apply(int a, int b) {
                return a - b;
            }
        };

        public abstract int apply(int a, int b);
    }
}
