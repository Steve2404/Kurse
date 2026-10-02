package ch6_classdesign.projects.p06_algebra.solution;

/**
 * SOLUTION - un analyseur par descente recursive : une methode par niveau de priorite.
 * expr   := term (('+' | '-') term)*
 * term   := factor (('*' | '/') factor)*
 * factor := atom ('^' entier)?
 * atom   := nombre | 'x' | '(' expr ')'
 */
public class Parser {

    private final String text;
    private int pos;

    public Parser(String text) {
        this.text = text.replace(" ", "");
    }

    public static Expr parse(String text) {
        return new Parser(text).expr();
    }

    private Expr expr() {
        Expr e = term();
        while (pos < text.length() && (peek() == '+' || peek() == '-')) {
            char op = text.charAt(pos++);
            Expr r = term();
            e = op == '+' ? new Add(e, r) : new Sub(e, r);   // associativite a gauche
        }
        return e;
    }

    private Expr term() {
        Expr e = factor();
        while (pos < text.length() && (peek() == '*' || peek() == '/')) {
            char op = text.charAt(pos++);
            Expr r = factor();
            e = op == '*' ? new Mul(e, r) : new Div(e, r);
        }
        return e;
    }

    private Expr factor() {
        Expr base = atom();
        if (pos < text.length() && peek() == '^') {
            pos++;
            return new Pow(base, (int) number());
        }
        return base;
    }

    private Expr atom() {
        char c = peek();
        if (c == '(') {
            pos++;
            Expr inside = expr();
            pos++;   // la parenthese fermante
            return inside;
        }
        if (c == 'x') {
            pos++;
            return new Var();
        }
        return new Num(number());
    }

    private double number() {
        int start = pos;
        while (pos < text.length() && (Character.isDigit(peek()) || peek() == '.')) {
            pos++;
        }
        return Double.parseDouble(text.substring(start, pos));
    }

    private char peek() {
        return text.charAt(pos);
    }
}
