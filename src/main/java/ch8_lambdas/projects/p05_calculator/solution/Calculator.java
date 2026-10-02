package ch8_lambdas.projects.p05_calculator.solution;

import ch8_lambdas.projects.p05_calculator.Data;

import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.UnaryOperator;

/**
 * SOLUTION du projet 5 - la calculatrice : infixe -> postfixe (gare de triage), puis evaluation sur une pile.
 */
public class Calculator {

    private final Registry registry = new Registry();
    private final ToDoubleFunction<String> variables;

    public Calculator(ToDoubleFunction<String> variables) {
        this.variables = variables;
    }

    // Gare de triage de Dijkstra : les operateurs attendent sur une pile selon leur priorite.
    public Token[] toPostfix(Token[] tokens) {
        IntFunction<Token[]> newArray = Token[]::new;            // reference de constructeur de TABLEAU
        Token[] out = newArray.apply(tokens.length);
        Token[] stack = newArray.apply(tokens.length);
        int n = 0;
        int top = 0;
        for (Token t : tokens) {
            switch (t.kind()) {
                case NUMBER, VAR -> out[n++] = t;
                case FUNCTION, LEFT -> stack[top++] = t;
                case COMMA -> {
                    while (stack[top - 1].kind() != Token.Kind.LEFT) {
                        out[n++] = stack[--top];
                    }
                }
                case OPERATOR -> {
                    // ^ est associatif a DROITE : on ne depile que les operateurs strictement plus prioritaires.
                    while (top > 0 && stack[top - 1].kind() == Token.Kind.OPERATOR
                            && (stack[top - 1].precedence() > t.precedence() || stack[top - 1].precedence() == t.precedence() && !t.text().equals("^"))) {
                        out[n++] = stack[--top];
                    }
                    stack[top++] = t;
                }
                case RIGHT -> {
                    while (stack[top - 1].kind() != Token.Kind.LEFT) {
                        out[n++] = stack[--top];
                    }
                    top--;                                                         // la parenthese ouvrante
                    if (top > 0 && stack[top - 1].kind() == Token.Kind.FUNCTION) {
                        out[n++] = stack[--top];
                    }
                }
            }
        }
        while (top > 0) {
            out[n++] = stack[--top];
        }
        return java.util.Arrays.copyOf(out, n);
    }

    public double evaluate(Token[] postfix) {
        double[] stack = new double[postfix.length];
        int top = 0;
        for (Token t : postfix) {
            switch (t.kind()) {
                case NUMBER -> stack[top++] = Double.parseDouble(t.text());
                case VAR -> stack[top++] = variables.applyAsDouble(t.text());
                default -> {
                    if (registry.isBinary(t.text())) {
                        double b = stack[--top];
                        double a = stack[--top];
                        stack[top++] = registry.binary(t.text()).applyAsDouble(a, b);
                    } else {
                        stack[top - 1] = registry.unary(t.text()).applyAsDouble(stack[top - 1]);
                    }
                }
            }
        }
        return stack[0];
    }

    public static void main(String[] args) {
        // Reference vers une methode d'instance d'un objet PRECIS : vars::lookup lie a CET objet.
        Variables vars = new Variables(Data.NAMES, Data.VALUES);
        Calculator calc = new Calculator(vars::lookup);
        Function<String, Token> classify = Token::classify;              // static
        Formatter fmt = new Formatter(4);
        DoubleFunction<String> show = fmt::format;                      // instance d'un objet precis
        UnaryOperator<String> clean = String::strip;                    // instance sur le PARAMETRE
        for (String expr : Data.EXPRESSIONS) {
            String[] words = clean.apply(expr).split(" ");
            Token[] tokens = new Token[words.length];
            for (int i = 0; i < words.length; i++) {
                tokens[i] = classify.apply(words[i]);
            }
            Token[] post = calc.toPostfix(tokens);
            StringBuilder rpn = new StringBuilder();
            for (Token t : post) {
                rpn.append(t).append(' ');
            }
            System.out.println(expr + "  =>  " + rpn.toString().strip() + "  =  " + show.apply(calc.evaluate(post)));
        }

        // Les quatre sortes de references, et leur lambda equivalente.
        Supplier<StringBuilder> fresh = StringBuilder::new;              // constructeur
        BiFunction<String, Token.Kind, Token> make = Token::new;         // constructeur du record, deux parametres
        BiFunction<String, String, Boolean> same = String::equalsIgnoreCase;   // instance sur le 1er parametre
        Function<String, Integer> parse = Integer::parseInt;            // static
        StringBuilder sb = fresh.get().append("ref");
        System.out.println("references : " + sb + " " + make.apply("pi", Token.Kind.VAR).kind() + " " + same.apply("JAVA", "java") + " "
                + (parse.apply("41") + 1) + " " + show.apply(Math.PI) + " " + new Formatter(0).format(2.5));
    }
}
