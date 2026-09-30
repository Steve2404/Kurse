package ch7_beyondclasses.solutions;

import java.util.EnumMap;
import java.util.Map;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise08_TrafficLightStateMachine.
 */
public class Solution08_TrafficLightStateMachine {

    public enum Light {
        RED('R', 30) {
            @Override
            public Light next() {
                // Chaque constante redefinit la methode abstract dans son propre corps.
                return GREEN;
            }
        },
        GREEN('G', 25) {
            @Override
            public Light next() {
                return YELLOW;
            }
        },
        YELLOW('Y', 5) {
            @Override
            public Light next() {
                return RED;
            }
        };

        private final char code;
        private final int seconds;

        Light(char code, int seconds) {
            this.code = code;
            this.seconds = seconds;
        }

        public int seconds() {
            return seconds;
        }

        public abstract Light next();

        public static Light fromCode(char code) {
            // values() parcourt les constantes ; valueOf chercherait par NOM, pas par code.
            for (Light light : values()) {
                if (light.code == Character.toUpperCase(code)) {
                    return light;
                }
            }
            throw new IllegalArgumentException("code inconnu : " + code);
        }

        public static int cycleSeconds() {
            // Un tour complet = la somme de toutes les durees.
            int total = 0;
            for (Light light : values()) {
                total += light.seconds;
            }
            return total;
        }

        public static Light afterSteps(Light start, int steps) {
            // La machine a etats avance par next().
            Light current = start;
            for (int i = 0; i < steps; i++) {
                current = current.next();
            }
            return current;
        }

        public static Light colorAt(Light start, int t) {
            // Le modulo evite de faire tourner des milliers de cycles.
            Light current = start;
            int remaining = t % cycleSeconds();
            while (remaining >= current.seconds) {
                remaining -= current.seconds;
                current = current.next();
            }
            return current;
        }

        public static Map<Light, Integer> countDuring(Light start, int seconds) {
            // EnumMap : cles d'enum, rangees dans l'ordre de declaration.
            Map<Light, Integer> counts = new EnumMap<>(Light.class);
            for (int t = 0; t < seconds; t++) {
                counts.merge(colorAt(start, t), 1, Integer::sum);
            }
            return counts;
        }
    }
}
