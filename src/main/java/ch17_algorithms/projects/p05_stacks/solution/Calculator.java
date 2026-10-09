package ch17_algorithms.projects.p05_stacks.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * La calculatrice de poche : des piles partout.
 * Pourquoi ArrayDeque et pas java.util.Stack : Stack est une vieille classe synchronisee ; Deque est la pile moderne.
 */
public final class Calculator {

    private static final Map<Character, Character> OPENING = Map.of(')', '(', ']', '[', '}', '{');
    private static final Map<String, Integer> PRIORITY = Map.of("+", 1, "-", 1, "*", 2, "/", 2);

    private Calculator() {
    }

    // Chaque fermante doit fermer la DERNIERE ouvrante encore ouverte : c'est exactement une pile.
    public static boolean balanced(String s) {
        Deque<Character> open = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                open.push(c);
            } else if (OPENING.containsKey(c)) {
                if (open.isEmpty() || !OPENING.get(c).equals(open.pop())) {
                    return false;
                }
            }
        }
        // Piege : "((" n'a aucune fermante en trop, mais reste ouvert.
        return open.isEmpty();
    }

    // La notation polonaise inverse : "3 4 + 2 *" ; chaque operateur prend les DEUX derniers nombres de la pile.
    // Piege : l'ordre ; le 1er depile est l'operande de DROITE ("8 2 -" vaut 6, pas -6).
    public static long evalRpn(String expr) {
        Deque<Long> stack = new ArrayDeque<>();
        for (String token : expr.trim().split("\\s+")) {
            if (PRIORITY.containsKey(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("expression invalide : " + expr);
                }
                long right = stack.pop();
                long left = stack.pop();
                stack.push(apply(token, left, right));
            } else if (token.matches("\\d+")) {
                stack.push(Long.parseLong(token));
            } else {
                throw new IllegalArgumentException("expression invalide : " + expr);
            }
        }
        if (stack.size() != 1) {
            throw new IllegalArgumentException("expression invalide : " + expr);
        }
        return stack.pop();
    }

    private static long apply(String op, long left, long right) {
        return switch (op) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            default -> left / right;
        };
    }

    // L'algorithme de la gare de triage (Dijkstra) : les nombres passent tout droit, les operateurs attendent sur une voie de garage.
    // Piege : un operateur de MEME priorite qui attend sort d'abord (gauche a droite : 8 - 3 - 2 = 3, pas 7).
    public static List<String> toRpn(String infix) {
        List<String> output = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();
        int i = 0;
        while (i < infix.length()) {
            char c = infix.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
            } else if (Character.isDigit(c)) {
                int start = i;
                while (i < infix.length() && Character.isDigit(infix.charAt(i))) {
                    i++;
                }
                output.add(infix.substring(start, i));
            } else if (c == '(') {
                operators.push("(");
                i++;
            } else if (c == ')') {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("parentheses desequilibrees");
                }
                operators.pop();
                i++;
            } else if (PRIORITY.containsKey(String.valueOf(c))) {
                String op = String.valueOf(c);
                while (!operators.isEmpty() && !operators.peek().equals("(")
                        && PRIORITY.get(operators.peek()) >= PRIORITY.get(op)) {
                    output.add(operators.pop());
                }
                operators.push(op);
                i++;
            } else {
                throw new IllegalArgumentException("caractere inconnu : " + c);
            }
        }
        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(")) {
                throw new IllegalArgumentException("parentheses desequilibrees");
            }
            output.add(op);
        }
        return output;
    }

    public static long evaluate(String infix) {
        return evalRpn(String.join(" ", toRpn(infix)));
    }
}
