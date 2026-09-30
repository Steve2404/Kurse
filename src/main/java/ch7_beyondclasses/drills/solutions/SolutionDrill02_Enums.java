package ch7_beyondclasses.drills.solutions;

import ch7_beyondclasses.drills.Catalog;

import java.util.EnumMap;
import java.util.Map;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.drills.exercises.Drill02_Enums.
 */
public class SolutionDrill02_Enums {

    enum Size {
        S(10) {
            @Override
            Size bigger() {
                // Chaque constante redefinit la methode abstract dans son corps.
                return M;
            }
        },
        M(15) {
            @Override
            Size bigger() {
                return L;
            }
        },
        L(20) {
            @Override
            Size bigger() {
                return L;
            }
        };

        private final int width;

        Size(int width) {
            // Constructeur implicitement private, appele une fois par constante.
            this.width = width;
        }

        int width() {
            return width;
        }

        abstract Size bigger();
    }

    public static int count() {
        // values() rend un tableau neuf de toutes les constantes.
        return Size.values().length;
    }

    public static Size parse(String text) {
        // valueOf cherche par NOM exact (IllegalArgumentException sinon).
        return Size.valueOf(text);
    }

    public static int position(Size size) {
        // ordinal() : l'index dans l'ordre de declaration, a partir de 0.
        return size.ordinal();
    }

    public static boolean isSmallerThan(Size a, Size b) {
        // Les enums sont Comparable (ordre de declaration) ; < ne compile pas.
        return a.compareTo(b) < 0;
    }

    public static String label(Size size) {
        // Toutes les constantes couvertes : pas besoin de default.
        return switch (size) {
            case S -> "petit";
            case M -> "moyen";
            case L -> "grand";
        };
    }

    public static Map<Size, Integer> countSizes() {
        // EnumMap garde l'ordre des constantes.
        Map<Size, Integer> counts = new EnumMap<>(Size.class);
        for (String code : Catalog.SIZES) {
            counts.merge(Size.valueOf(code), 1, Integer::sum);
        }
        return counts;
    }

    public static Size widest() {
        Size best = Size.S;
        for (Size s : Size.values()) {
            if (s.width() > best.width()) {
                best = s;
            }
        }
        return best;
    }

    public static Size fromWidth(int width) {
        // valueOf cherche par nom : pour un autre critere, on parcourt values().
        for (Size s : Size.values()) {
            if (s.width() == width) {
                return s;
            }
        }
        return null;
    }
}
