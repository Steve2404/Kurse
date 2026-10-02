package ch3_makingdecisions.projects.p04_rpn.solution;

/**
 * SOLUTION du projet 4 - une conception possible.
 * Une calculatrice "polonaise inverse" a 4 registres, comme les HP : X (affiche), Y, Z, T.
 */
public class Rpn {

    static Number x = 0;
    static Number y = 0;
    static Number z = 0;
    static Number t = 0;

    static void push(Number value) {
        t = z;
        z = y;
        y = x;
        x = value;
    }

    // Apres une operation a deux operandes, la pile "descend" : T est recopie (comme sur une HP).
    static void dropAfterOperation(Number result) {
        x = result;
        y = z;
        z = t;
    }

    static Number parse(String token) {
        double value = Double.parseDouble(token);
        // Piege : "value == (int) value ? Integer.valueOf(..) : Double.valueOf(..)" promouvrait l'Integer en double !
        // Un if/else (ou un cast en Number sur chaque branche) garde le bon type.
        if (value == (int) value) {
            return Integer.valueOf((int) value);
        }
        return Double.valueOf(value);
    }

    static Number compute(String op, Number a, Number b) {
        // Pattern matching : le test ET la variable typee en une fois ; i et j n'existent que si le test reussit.
        if (a instanceof Integer i && b instanceof Integer j) {
            return switch (op) {
                case "+" -> i + j;
                case "-" -> i - j;
                case "x" -> i * j;
                default -> {
                    if (i % j == 0) {
                        yield i / j;
                    }
                    yield (double) i / j;
                }
            };
        }
        double p = a.doubleValue();
        double q = b.doubleValue();
        return switch (op) {
            case "+" -> p + q;
            case "-" -> p - q;
            case "x" -> p * q;
            default -> p / q;
        };
    }

    static boolean isZero(Number n) {
        // Portee de flux : si le test est FAUX on sort ; apres le if, d est donc forcement defini.
        if (!(n instanceof Double d)) {
            return n.intValue() == 0;
        }
        return d == 0.0;
    }

    static Number negate(Number n) {
        if (n instanceof Integer i) {
            return -i;
        } else if (n instanceof Double d) {
            return -d;
        }
        return n;
    }

    static String stack() {
        return "T=" + t + " Z=" + z + " Y=" + y + " X=" + x;
    }

    public static void main(String[] args) {
        int step = 0;
        for (String token : args) {
            step++;
            String note = "";
            switch (token) {
                case "+", "-", "x" -> dropAfterOperation(compute(token, y, x));
                case "/" -> {
                    if (isZero(x)) {
                        note = " (ERREUR division par zero, pile inchangee)";
                    } else {
                        dropAfterOperation(compute(token, y, x));
                    }
                }
                case "DUP" -> push(x);
                case "SWAP" -> {
                    Number tmp = x;
                    x = y;
                    y = tmp;
                }
                case "DROP" -> dropAfterOperation(y);
                case "CHS" -> x = negate(x);
                case "CLR" -> {
                    x = 0;
                    y = 0;
                    z = 0;
                    t = 0;
                }
                default -> push(parse(token));
            }
            System.out.println(step + ". " + token + " -> " + stack() + note
                    + (x instanceof Double ? " (X decimal)" : ""));
        }
        Object boxed = 5 > 3 ? Integer.valueOf(1) : Double.valueOf(2.5);
        System.out.println("piege du ternaire : " + boxed + " est un " + (boxed instanceof Integer ? "Integer" : "Double"));
    }
}
