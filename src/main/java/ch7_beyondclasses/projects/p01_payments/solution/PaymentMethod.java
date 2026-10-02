package ch7_beyondclasses.projects.p01_payments.solution;

/**
 * SOLUTION - le contrat commun a tous les moyens de paiement.
 */
public interface PaymentMethod {

    // Un champ d'interface est implicitement public static final : c'est une constante.
    long MAX_CENTS = 500_000;

    // Une methode sans corps est implicitement public abstract.
    String label();

    boolean isValid();

    long fee(long amount);

    // default : une implementation que les classes heritent, et peuvent redefinir.
    default String pay(long amount) {
        if (!isValid()) {
            return line("REFUSE (invalide)");
        }
        if (amount > MAX_CENTS) {
            return line("REFUSE (plafond " + money(MAX_CENTS) + ")");
        }
        return line("OK " + money(amount) + " + frais " + money(fee(amount)));
    }

    // private : un outil reserve aux methodes default de l'interface (Java 9+).
    private String line(String result) {
        return label() + " : " + result;
    }

    // static : appartient a l'interface ; on l'appelle PaymentMethod.money(...), jamais via un objet.
    static String money(long cents) {
        long rest = cents % 100;
        return cents / 100 + "." + (rest < 10 ? "0" : "") + rest;
    }

    // private static : un outil pour les methodes static de l'interface.
    private static int value(char c) {
        return Character.isDigit(c) ? c - '0' : c - 'A' + 10;
    }

    // Validation IBAN (mod 97) : on deplace les 4 premiers caracteres a la fin, les lettres deviennent 10..35,
    // puis on calcule le reste modulo 97 chiffre par chiffre (le nombre entier serait trop grand pour un long).
    static boolean ibanValid(String iban) {
        String moved = iban.substring(4) + iban.substring(0, 4);
        int rest = 0;
        for (int i = 0; i < moved.length(); i++) {
            int v = value(moved.charAt(i));
            rest = v >= 10 ? (rest * 100 + v) % 97 : (rest * 10 + v) % 97;
        }
        return rest == 1;
    }

    // Algorithme de Luhn : depuis la droite, on double un chiffre sur deux (moins 9 au-dela de 9).
    static boolean luhnValid(String digits) {
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            int d = digits.charAt(digits.length() - 1 - i) - '0';
            if (i % 2 == 1) {
                d *= 2;
                if (d > 9) {
                    d -= 9;
                }
            }
            sum += d;
        }
        return sum % 10 == 0;
    }

    // Le chiffre de controle a ajouter a un numero partiel pour qu'il passe Luhn.
    static int luhnCheckDigit(String partial) {
        for (int d = 0; d <= 9; d++) {
            if (luhnValid(partial + d)) {
                return d;
            }
        }
        return -1;
    }
}
