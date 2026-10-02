package ch8_lambdas.projects.p01_pipeline.solution;

import java.util.function.UnaryOperator;

/**
 * SOLUTION - une etape de transformation : une interface FONCTIONNELLE (une seule methode abstraite, heritee
 * de UnaryOperator : apply). Les methodes default et static ne comptent pas.
 */
@FunctionalInterface
public interface Step extends UnaryOperator<String> {

    // Un combinateur ecrit a la main : il rend une NOUVELLE lambda qui enchaine this puis next.
    default Step then(Step next) {
        return s -> next.apply(apply(s));
    }

    static Step identity() {
        return s -> s;
    }

    // Une fabrique : chaque branche rend une lambda (plusieurs syntaxes possibles).
    static Step of(String command) {
        String[] p = command.strip().split(" ");
        return switch (p[0]) {
            case "trim" -> s -> s.strip();
            case "lower" -> String::toLowerCase;                       // reference de methode (instance sur le parametre)
            case "upper" -> (String s) -> s.toUpperCase();             // parametre type explicitement
            case "squeeze" -> (var s) -> s.replaceAll(" +", " ");       // var est permis pour un parametre de lambda
            case "reverse" -> s -> new StringBuilder(s).reverse().toString();
            case "title" -> Step::title;                                // reference de methode static
            case "novowels" -> s -> s.replaceAll("[aeiouAEIOU]", "");
            case "caesar" -> caesar(Integer.parseInt(p[1]));
            case "replace" -> s -> s.replace(p[1], p[2]);              // p est effectively final : capturable
            case "repeat" -> {
                int times = Integer.parseInt(p[1]);
                yield s -> {                                            // corps en bloc : accolades et return
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < times; i++) {
                        sb.append(i == 0 ? "" : " / ").append(s);
                    }
                    return sb.toString();
                };
            }
            case "rle" -> Step::encode;
            case "unrle" -> Step::decode;
            default -> identity();
        };
    }

    static Step parse(String pipeline) {
        Step result = identity();
        for (String command : pipeline.split("\\|")) {
            result = result.then(of(command));
        }
        return result;
    }

    private static String title(String s) {
        StringBuilder sb = new StringBuilder();
        for (String w : s.split(" ")) {
            if (!w.isEmpty()) {
                sb.append(sb.length() == 0 ? "" : " ").append(Character.toUpperCase(w.charAt(0))).append(w.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    private static Step caesar(int shift) {
        return s -> {
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                if (Character.isLowerCase(c)) {
                    sb.append((char) ('a' + (c - 'a' + shift) % 26));
                } else if (Character.isUpperCase(c)) {
                    sb.append((char) ('A' + (c - 'A' + shift) % 26));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        };
    }

    private static String encode(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            int j = i;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) {
                j++;
            }
            sb.append(j - i).append(s.charAt(i));
            i = j;
        }
        return sb.toString();
    }

    private static String decode(String s) {
        StringBuilder sb = new StringBuilder();
        int n = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                n = n * 10 + (c - '0');
            } else {
                sb.append(String.valueOf(c).repeat(n));
                n = 0;
            }
        }
        return sb.toString();
    }
}
