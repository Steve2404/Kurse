package ch11_exceptions.projects.p02_calculator.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION - analyseur a descente recursive qui evalue en lisant.
 *   expr    := term (('+' | '-') term)*
 *   term    := unary (('*' | '/' | '%') unary)*
 *   unary   := '-' unary | primary
 *   primary := NOMBRE | NOM '(' [expr (',' expr)*] ')' | '(' expr ')'
 */
public class Parser {

    private static final String END = "<fin>";

    private final List<Token> tokens;
    private int index;

    public Parser(String text) throws SyntaxException {
        tokens = tokenize(text);
    }

    static List<Token> tokenize(String s) throws SyntaxException {
        List<Token> out = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            int start = i;
            if (c == ' ') {
                i++;
            } else if (Character.isDigit(c)) {
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    i++;
                }
                out.add(new Token(s.substring(start, i), start));
            } else if (Character.isLetter(c)) {
                while (i < s.length() && Character.isLetter(s.charAt(i))) {
                    i++;
                }
                out.add(new Token(s.substring(start, i), start));
            } else if ("+-*/%(),".indexOf(c) >= 0) {
                out.add(new Token(String.valueOf(c), i));
                i++;
            } else {
                throw new SyntaxException("caractere inattendu '" + c + "'", i);
            }
        }
        out.add(new Token(END, s.length()));                   // sentinelle : evite les depassements d'indice
        return out;
    }

    private Token peek() {
        return tokens.get(index);
    }

    private Token next() {
        return tokens.get(index++);
    }

    private void expect(String text) throws SyntaxException {
        Token t = next();
        if (!t.text().equals(text)) {
            throw new SyntaxException("'" + text + "' attendu", t.position());
        }
    }

    public long parseAll() throws SyntaxException {
        long value = expr();
        if (!peek().text().equals(END)) {
            throw new SyntaxException("fin attendue", peek().position());
        }
        return value;
    }

    // Les ...Exact lancent ArithmeticException au lieu de deborder en silence.
    private long expr() throws SyntaxException {
        long value = term();
        while (peek().text().equals("+") || peek().text().equals("-")) {
            String op = next().text();
            long right = term();
            value = op.equals("+") ? Math.addExact(value, right) : Math.subtractExact(value, right);
        }
        return value;
    }

    private long term() throws SyntaxException {
        long value = unary();
        while (peek().text().equals("*") || peek().text().equals("/") || peek().text().equals("%")) {
            String op = next().text();
            long right = unary();
            value = switch (op) {
                case "*" -> Math.multiplyExact(value, right);
                case "/" -> value / right;                       // right == 0 : ArithmeticException "/ by zero"
                default -> value % right;                        // le signe suit celui de value : -20 % 7 = -6
            };
        }
        return value;
    }

    private long unary() throws SyntaxException {
        if (peek().text().equals("-")) {
            next();
            return Math.negateExact(unary());
        }
        return primary();
    }

    private long primary() throws SyntaxException {
        Token t = next();
        if (Character.isDigit(t.text().charAt(0))) {
            try {
                return Long.parseLong(t.text());
            } catch (NumberFormatException e) {
                throw new SyntaxException("nombre trop grand", t.position(), e);   // traduire, en gardant la cause
            }
        }
        if (t.text().equals("(")) {
            long value = expr();
            expect(")");
            return value;
        }
        if (Character.isLetter(t.text().charAt(0)) && !t.text().equals(END)) {
            expect("(");
            List<Long> args = new ArrayList<>();
            if (!peek().text().equals(")")) {
                args.add(expr());
                while (peek().text().equals(",")) {
                    next();
                    args.add(expr());
                }
            }
            expect(")");
            return call(t, args);
        }
        throw new SyntaxException("valeur attendue", t.position());
    }

    private long call(Token name, List<Long> args) throws SyntaxException {
        switch (name.text()) {
            case "max", "min" -> {
                if (args.isEmpty()) {
                    throw new IllegalArgumentException(name.text() + " attend au moins 1 argument");
                }
                return name.text().equals("max") ? args.stream().mapToLong(Long::longValue).max().getAsLong()
                        : args.stream().mapToLong(Long::longValue).min().getAsLong();
            }
            case "abs" -> {
                if (args.size() != 1) {
                    throw new IllegalArgumentException("abs attend 1 argument");
                }
                return Math.absExact(args.get(0));               // Math.abs(Long.MIN_VALUE) rendrait un NEGATIF
            }
            default -> throw new SyntaxException("fonction inconnue : " + name.text(), name.position());
        }
    }
}
