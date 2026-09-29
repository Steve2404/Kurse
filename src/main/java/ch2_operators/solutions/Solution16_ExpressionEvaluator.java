package ch2_operators.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch2_operators.exercises.Exercise16_ExpressionEvaluator.
 */
public class Solution16_ExpressionEvaluator {

    public static List<String> tokenize(String expression) {
        // Plusieurs chiffres colles forment UN nombre ; tout autre caractere (sauf espace) est un morceau seul.
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);
            if (c == ' ') {
                i++;
            } else if (Character.isDigit(c)) {
                int start = i;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    i++;
                }
                tokens.add(expression.substring(start, i));
            } else {
                tokens.add(String.valueOf(c));
                i++;
            }
        }
        return tokens;
    }

    public static int parseFactor(List<String> tokens, int[] pos) {
        // Les parentheses renvoient a l'etage le plus haut : c'est ce qui leur donne la priorite maximale.
        String token = tokens.get(pos[0]);
        pos[0]++;
        if (token.equals("(")) {
            int value = parseExpression(tokens, pos);
            pos[0]++;
            return value;
        }
        return Integer.parseInt(token);
    }

    public static int parseTerm(List<String> tokens, int[] pos) {
        // Une boucle qui combine au fur et a mesure = associativite de GAUCHE a DROITE.
        int result = parseFactor(tokens, pos);
        while (pos[0] < tokens.size()) {
            String op = tokens.get(pos[0]);
            if (!op.equals("*") && !op.equals("/") && !op.equals("%")) {
                break;
            }
            pos[0]++;
            int factor = parseFactor(tokens, pos);
            switch (op) {
                case "*":
                    result = result * factor;
                    break;
                case "/":
                    result = result / factor;
                    break;
                default:
                    result = result % factor;
            }
        }
        return result;
    }

    public static int parseExpression(List<String> tokens, int[] pos) {
        // + et - ne voient que des TERMES deja calcules : les * / % sont donc passes avant.
        int result = parseTerm(tokens, pos);
        while (pos[0] < tokens.size()) {
            String op = tokens.get(pos[0]);
            if (!op.equals("+") && !op.equals("-")) {
                break;
            }
            pos[0]++;
            int term = parseTerm(tokens, pos);
            result = op.equals("+") ? result + term : result - term;
        }
        return result;
    }

    public static int evaluate(String expression) {
        // Une seule boite pos partagee par les 3 etages : chacun reprend la lecture la ou l'autre s'est arrete.
        List<String> tokens = tokenize(expression);
        int[] pos = {0};
        return parseExpression(tokens, pos);
    }
}
