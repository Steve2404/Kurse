package ch19_final.projects.p07_review.solution;

import java.util.Objects;

/**
 * Un client. Un record : equals ET hashCode sont ecrits par le compilateur, et toujours d'accord entre eux
 * (le collegue avait ecrit equals sans hashCode : deux clients "egaux" se retrouvaient deux fois dans un HashSet).
 * toString masque l'adresse : les journaux (logs) sont lus par beaucoup de monde, et une adresse est une
 * donnee personnelle (RGPD).
 */
public record Customer(String id, String name, String email) {

    public Customer {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(email, "email");
    }

    @Override
    public String toString() {
        return "Customer[" + id + ", " + name + ", " + masked(email) + "]";
    }

    // ada@example.org -> a***@example.org ; sans @ (ou @ en tete) : tout est masque.
    static String masked(String email) {
        int at = email.indexOf('@');
        return at <= 0 ? "***" : email.charAt(0) + "***" + email.substring(at);
    }
}
