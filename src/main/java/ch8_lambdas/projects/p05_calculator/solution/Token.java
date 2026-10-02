package ch8_lambdas.projects.p05_calculator.solution;

/**
 * SOLUTION - un jeton : un record (son constructeur peut servir de reference : Token::new).
 */
public record Token(String text, Kind kind) {

    public enum Kind { NUMBER, VAR, OPERATOR, FUNCTION, LEFT, RIGHT, COMMA }

    // Une methode static utilisee comme Function<String, Token> : Token::classify.
    public static Token classify(String text) {
        char c = text.charAt(0);
        Kind kind;
        if (Character.isDigit(c) || c == '-' && text.length() > 1) {
            kind = Kind.NUMBER;
        } else if (text.equals("(")) {
            kind = Kind.LEFT;
        } else if (text.equals(")")) {
            kind = Kind.RIGHT;
        } else if (text.equals(",")) {
            kind = Kind.COMMA;
        } else if ("+-*/^".contains(text)) {
            kind = Kind.OPERATOR;
        } else if (text.length() == 1) {
            kind = Kind.VAR;
        } else {
            kind = Kind.FUNCTION;
        }
        return new Token(text, kind);
    }

    public int precedence() {
        return switch (text) {
            case "^" -> 3;
            case "*", "/" -> 2;
            default -> 1;
        };
    }

    @Override
    public String toString() {
        return text;
    }
}
