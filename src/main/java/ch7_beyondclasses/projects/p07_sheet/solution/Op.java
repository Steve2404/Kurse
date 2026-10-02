package ch7_beyondclasses.projects.p07_sheet.solution;

/**
 * SOLUTION - les operateurs : un enum avec un champ, et un CORPS PAR CONSTANTE pour apply().
 */
public enum Op {
    PLUS('+') {
        @Override
        public double apply(double a, double b) {
            return a + b;
        }
    },
    MINUS('-') {
        @Override
        public double apply(double a, double b) {
            return a - b;
        }
    },
    TIMES('*') {
        @Override
        public double apply(double a, double b) {
            return a * b;
        }
    },
    DIVIDE('/') {
        @Override
        public double apply(double a, double b) {
            return b == 0 ? Double.NaN : a / b;
        }
    };

    private final char symbol;

    Op(char symbol) {
        this.symbol = symbol;
    }

    public abstract double apply(double a, double b);

    public char symbol() {
        return symbol;
    }

    public static Op of(char c) {
        for (Op op : values()) {
            if (op.symbol == c) {
                return op;
            }
        }
        return null;
    }
}
