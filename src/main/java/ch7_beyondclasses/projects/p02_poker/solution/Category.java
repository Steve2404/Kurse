package ch7_beyondclasses.projects.p02_poker.solution;

/**
 * SOLUTION - les categories de mains, de la plus faible a la plus forte.
 * Chaque constante a son PROPRE corps qui implemente la methode abstraite matches(...).
 */
public enum Category {
    HIGH_CARD("hauteur") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return true;
        }
    },
    PAIR("paire") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return groups[0] == 2;
        }
    },
    TWO_PAIR("double paire") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return groups[0] == 2 && groups[1] == 2;
        }
    },
    THREE_OF_A_KIND("brelan") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return groups[0] == 3;
        }
    },
    STRAIGHT("suite") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return straight;
        }
    },
    FLUSH("couleur") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return flush;
        }
    },
    FULL_HOUSE("full") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return groups[0] == 3 && groups[1] == 2;
        }
    },
    FOUR_OF_A_KIND("carre") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return groups[0] == 4;
        }
    },
    STRAIGHT_FLUSH("quinte flush") {
        @Override
        boolean matches(int[] groups, boolean flush, boolean straight) {
            return flush && straight;
        }
    };

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    // Methode abstraite : chaque constante DOIT la fournir dans son corps.
    abstract boolean matches(int[] groups, boolean flush, boolean straight);

    // La categorie d'une main : on essaie de la plus forte a la plus faible.
    public static Category of(int[] groups, boolean flush, boolean straight) {
        Category[] all = values();
        for (int i = all.length - 1; i >= 0; i--) {
            if (all[i].matches(groups, flush, straight)) {
                return all[i];
            }
        }
        return HIGH_CARD;
    }
}
