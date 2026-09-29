package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 16 - Ecris ton propre calculateur qui respecte la precedence de Java (niveau : difficile/capstone)
 * =========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Le contexte --
 *
 * Les exercices 14 et 15 t'ont fait UTILISER la precedence. Ici tu la
 * PROGRAMMES : une methode evaluate("2 + 3 * 4") qui rend 14, comme
 * Java. Expressions acceptees : des entiers positifs, + - * / %, des
 * parentheses et des espaces. Division ENTIERE, comme en Java.
 *
 * Les 2 regles a respecter :
 *   1. * / % passent AVANT + - (priorite).
 *   2. A priorite egale, on calcule de GAUCHE a DROITE :
 *      10 - 4 - 3 == (10 - 4) - 3 == 3  (et pas 10 - (4 - 3) == 9)
 *      20 / 3 * 3 == (20 / 3) * 3 == 18 (division entiere : 20 / 3 == 6)
 *
 * L'astuce classique : un etage par niveau de priorite.
 *   expression : une suite de "termes" separes par + ou -
 *   terme      : une suite de "facteurs" separes par * / ou %
 *   facteur    : un nombre, ou une expression ENTRE PARENTHESES
 * Chaque etage appelle l'etage du dessous : c'est ce qui donne la
 * priorite a * sur +, et aux parentheses sur tout.
 *
 * main() compare ton resultat avec le VRAI calcul de Java sur 10
 * expressions (Java calcule la meme expression ecrite en code).
 *
 * La position de lecture dans la liste de morceaux est gardee dans
 * une boite int[] pos (pos[0] = indice du prochain morceau a lire),
 * partagee par les 3 etages.
 *
 *
 * ==================================================================
 * TODO 1 : tokenize(expression)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Avant de calculer, on decoupe la phrase en morceaux : les nombres
 * (plusieurs chiffres colles = un seul nombre), les operateurs et les
 * parentheses. Les espaces sont jetes.
 *
 * -- Essayons a la main --
 *
 *   "12+(3 * 4)" -> [12, +, (, 3, *, 4, )]
 *
 * -- Le plan --
 *
 *   1. Parcourir les caracteres.
 *   2. Espace : ignorer. Chiffre : lire tous les chiffres qui suivent
 *      et ajouter le nombre complet. Autre : ajouter ce caractere seul.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est la 1re boite magique ; evaluate l'utilise.
 *
 *
 * ==================================================================
 * TODO 2 : parseFactor(tokens, pos)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * L'etage le plus bas. Si le morceau est "(", on calcule TOUTE
 * l'expression a l'interieur (en appelant l'etage du haut !), puis on
 * saute la ")". Sinon c'est un nombre : on le convertit.
 *
 * -- Le plan --
 *
 *   1. Lire le morceau courant et avancer pos.
 *   2. "(" -> valeur = parseExpression(...) ; avancer pos pour sauter ")".
 *   3. Sinon -> Integer.parseInt du morceau.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : parseExpression (TODO 4) - les etages s'appellent entre eux.
 *
 *
 * ==================================================================
 * TODO 3 : parseTerm(tokens, pos)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un terme, c'est "facteur * facteur / facteur ...". On garde un
 * resultat qu'on combine avec chaque nouveau facteur, de GAUCHE a
 * DROITE : c'est ca qui donne (20 / 3) * 3.
 *
 * -- Le plan --
 *
 *   1. resultat = parseFactor.
 *   2. Tant que le morceau courant est *, / ou % : le lire, lire le
 *      facteur suivant, combiner.
 *   3. Rendre le resultat.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : parseFactor (TODO 2).
 *
 *
 * ==================================================================
 * TODO 4 : parseExpression(tokens, pos)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   Meme chose que TODO 3, un etage plus haut : parseTerm, puis tant
 *   que le morceau est + ou - : lire le terme suivant et combiner.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : parseTerm (TODO 3).
 *
 *
 * ==================================================================
 * TODO 5 : evaluate(expression)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Decouper (TODO 1). 2. pos = {0}. 3. Rendre parseExpression.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : TODO 1 et 4.
 *
 *
 * Exemple a verifier : "2 + 3 * 4" -> 14 ; "10 - 4 - 3" -> 3 ;
 * "20 / 3 * 3" -> 18 ; "7 % 3 * 2" -> 2 ; "(1 + 2) * (3 + 4)" -> 21 ;
 * "8 - 2 * 3 + 1" -> 3 ; "((2))" -> 2 ; "42" -> 42.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Character.isDigit(c) ; expression.substring(start, i)
 *   - "morceau courant existe ?" : pos[0] < tokens.size()
 *   - String op = tokens.get(pos[0]); pos[0]++;
 *   - switch (op) { case "*": r = r * f; break; case "/": ... }
 */
public class Exercise16_ExpressionEvaluator {

    public static List<String> tokenize(String expression) {
        throw new UnsupportedOperationException("TODO 1 : implementer tokenize()");
    }

    public static int parseFactor(List<String> tokens, int[] pos) {
        throw new UnsupportedOperationException("TODO 2 : implementer parseFactor()");
    }

    public static int parseTerm(List<String> tokens, int[] pos) {
        throw new UnsupportedOperationException("TODO 3 : implementer parseTerm()");
    }

    public static int parseExpression(List<String> tokens, int[] pos) {
        throw new UnsupportedOperationException("TODO 4 : implementer parseExpression()");
    }

    public static int evaluate(String expression) {
        throw new UnsupportedOperationException("TODO 5 : implementer evaluate()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 tokenize(\"12+(3 * 4)\") == [12, +, (, 3, *, 4, )]",
                tokenize("12+(3 * 4)").equals(List.of("12", "+", "(", "3", "*", "4", ")")));

        String[] expressions = {"2 + 3 * 4", "10 - 4 - 3", "20 / 3 * 3", "7 % 3 * 2", "2 * 3 % 4",
                "100 / 10 / 5", "(1 + 2) * (3 + 4)", "8 - 2 * 3 + 1", "((2))", "42"};
        int[] javaValues = {2 + 3 * 4, 10 - 4 - 3, 20 / 3 * 3, 7 % 3 * 2, 2 * 3 % 4,
                100 / 10 / 5, (1 + 2) * (3 + 4), 8 - 2 * 3 + 1, ((2)), 42};
        int ok = 0;
        for (int i = 0; i < expressions.length; i++) {
            int mine = evaluate(expressions[i]);
            if (mine == javaValues[i]) {
                ok++;
            } else {
                System.out.println("   ecart : " + expressions[i] + " -> " + mine + " au lieu de " + javaValues[i]);
            }
        }
        ExerciseChecker.check("2-5 evaluate donne le meme resultat que Java sur 10 expressions (" + ok + "/10)", ok == 10);
        ExerciseChecker.check("2-5 gauche a droite : 10 - 4 - 3 == 3 et 20 / 3 * 3 == 18",
                evaluate("10 - 4 - 3") == 3 && evaluate("20 / 3 * 3") == 18);

        ExerciseChecker.summary();
    }
}
