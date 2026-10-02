package ch6_classdesign.projects.p07_media.solution;

/**
 * SOLUTION - un ISBN-13 IMMUABLE, avec sa cle de controle.
 */
public final class Isbn {

    private final String digits;

    public Isbn(String digits) {
        this.digits = digits;
    }

    // Cle ISBN-13 : poids 1, 3, 1, 3... sur les 12 premiers chiffres ; la cle complete a un multiple de 10.
    public int expectedCheck() {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (digits.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
        }
        return (10 - sum % 10) % 10;
    }

    public boolean isValid() {
        return digits.length() == 13 && digits.charAt(12) - '0' == expectedCheck();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Isbn i && i.digits.equals(digits);
    }

    @Override
    public int hashCode() {
        return digits.hashCode();
    }

    @Override
    public String toString() {
        return digits.substring(0, 3) + "-" + digits.substring(3, 12) + "-" + digits.charAt(12);
    }
}
