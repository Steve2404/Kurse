package ch3_makingdecisions.drills.solutions;

import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.drills.exercises.Drill02_SwitchForms.
 */
public class SolutionDrill02_SwitchForms {

    public static String dayNameFr(Day day) {
        // Les 7 constantes sont listees : exhaustif sans default.
        return switch (day) {
            case MON -> "lundi";
            case TUE -> "mardi";
            case WED -> "mercredi";
            case THU -> "jeudi";
            case FRI -> "vendredi";
            case SAT -> "samedi";
            case SUN -> "dimanche";
        };
    }

    public static boolean isWeekendSwitch(Day day) {
        // Plusieurs constantes dans un seul case ; dans un case d'enum, on ecrit SAT (pas Day.SAT).
        return switch (day) {
            case SAT, SUN -> true;
            default -> false;
        };
    }

    public static int monthDays(int month) {
        // case empiles : le fall-through fait partager le meme code, break s'arrete.
        int days;
        switch (month) {
            case 2:
                days = 28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            default:
                days = -1;
        }
        return days;
    }

    public static int commandCode(String command) {
        // switch sur String : compare avec equals.
        switch (command) {
            case "start":
                return 1;
            case "stop":
                return 2;
            case "pause":
                return 3;
            default:
                return 0;
        }
    }

    public static boolean isVowel(char c) {
        // Plusieurs char dans un seul case.
        return switch (c) {
            case 'a', 'e', 'i', 'o', 'u', 'y' -> true;
            default -> false;
        };
    }

    public static String tempLabel(int t) {
        // Un bloc rend sa valeur avec yield.
        return switch (t) {
            case 0 -> "zero pile";
            default -> {
                String label = t < 0 ? "negatif" : "positif";
                yield label;
            }
        };
    }

    public static int fallThroughChar(char c) {
        // 'a' execute les deux n++ (pas de break entre eux), 'b' seulement le second.
        int n = 0;
        switch (c) {
            case 'a':
                n++;
            case 'b':
                n++;
                break;
            default:
                n = -1;
        }
        return n;
    }

    public static String nullSafeCommand(String command) {
        // Un switch sur null lancerait NullPointerException : on filtre avant.
        if (command == null) {
            return "aucune";
        }
        return switch (command) {
            default -> command.toUpperCase();
        };
    }

    public static String colonYield(int x) {
        // Forme ":" d'un switch expression : chaque case rend avec yield.
        return switch (x) {
            case 1:
                yield "un";
            case 2:
                yield "deux";
            default:
                yield "beaucoup";
        };
    }

    public static String levelName(int level) {
        // throw est une issue valide dans un switch expression.
        return switch (level) {
            case 1 -> "bas";
            case 2 -> "moyen";
            case 3 -> "haut";
            default -> throw new IllegalArgumentException("niveau : " + level);
        };
    }

    public static String byteLabel(byte b) {
        // byte est accepte ; 127 tient dans un byte, donc c'est une etiquette valide.
        switch (b) {
            case 0:
                return "zero";
            case 127:
                return "max";
            default:
                return "autre";
        }
    }

    public static int startCount() {
        // Le switch est dans la boucle : chaque tour teste une commande.
        int count = 0;
        for (String command : Week.COMMANDS) {
            switch (command) {
                case "start":
                    count++;
                    break;
                default:
                    break;
            }
        }
        return count;
    }
}
