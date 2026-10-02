package ch3_makingdecisions.projects.p01_vending.solution;

/**
 * SOLUTION du projet 1 - une conception possible.
 */
public class Vending {

    static int credit;
    static int stockA1 = 2;
    static int stockB2 = 1;
    static int stockC3 = 1;
    static boolean happyHour;

    static String euros(int cents) {
        return cents / 100 + "." + cents / 10 % 10 + cents % 10;
    }

    // switch EXPRESSION : chaque branche rend une valeur ; yield sort une valeur d'un bloc { }.
    static int price(String code) {
        return switch (code) {
            case "A1" -> 120;
            case "B2" -> 150;
            case "C3" -> {
                int base = 180;
                yield happyHour ? base - 30 : base;
            }
            default -> -1;
        };
    }

    static String name(String code) {
        return switch (code) {
            case "A1" -> "Eau";
            case "B2" -> "Cafe";
            case "C3" -> "Chips";
            default -> "?";
        };
    }

    static int stock(String code) {
        return switch (code) {
            case "A1" -> stockA1;
            case "B2" -> stockB2;
            case "C3" -> stockC3;
            default -> 0;
        };
    }

    // switch INSTRUCTION classique : sans break, l'execution "tombe" dans le case suivant.
    static void take(String code) {
        switch (code) {
            case "A1":
                stockA1--;
                break;
            case "B2":
                stockB2--;
                break;
            default:
                stockC3--;
        }
    }

    static void insert(int coin) {
        // Plusieurs valeurs dans un meme case.
        switch (coin) {
            case 10, 20, 50, 100, 200 -> {
                credit += coin;
                System.out.println("PIECE " + euros(coin) + " -> credit " + euros(credit));
            }
            default -> System.out.println("PIECE " + coin + " refusee");
        }
    }

    static void choose(String code) {
        int price = price(code);
        if (price < 0) {
            System.out.println("CHOIX " + code + " -> produit inconnu");
        } else if (stock(code) == 0) {
            System.out.println("CHOIX " + code + " -> " + name(code) + " epuise");
        } else if (credit < price) {
            System.out.println("CHOIX " + code + " -> credit insuffisant (manque " + euros(price - credit) + ")");
        } else {
            credit -= price;
            take(code);
            System.out.println("CHOIX " + code + " -> " + name(code) + " servi (" + euros(price) + "), reste " + euros(credit));
        }
    }

    // Rendu glouton : la plus grosse piece possible, puis on passe a la piece inferieure.
    static void giveChange() {
        String coins = "";
        int coin = 200;
        int count = 0;
        while (credit > 0 && coin > 0) {
            if (credit >= coin) {
                credit -= coin;
                coins = coins + " " + coin;
                count++;
            } else {
                coin = switch (coin) {
                    case 200 -> 100;
                    case 100 -> 50;
                    case 50 -> 20;
                    case 20 -> 10;
                    default -> 0;
                };
            }
        }
        System.out.println("RENDU -> " + (count == 0 ? "rien a rendre" : count + " piece(s) :" + coins));
    }

    public static void main(String[] args) {
        // Boucle a indice : certaines commandes CONSOMMENT l'argument suivant (args[++i]).
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "PIECE" -> insert(Integer.parseInt(args[++i]));
                case "CHOIX" -> choose(args[++i]);
                case "RENDU" -> giveChange();
                case "HAPPY" -> {
                    happyHour = !happyHour;
                    System.out.println("HAPPY HOUR " + (happyHour ? "on" : "off"));
                }
                case "STOCK" -> System.out.println("STOCK A1=" + stockA1 + " B2=" + stockB2 + " C3=" + stockC3);
                default -> System.out.println("Commande inconnue : " + args[i]);
            }
        }
    }
}
