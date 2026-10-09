package ch19_final.drills.r06_review.solution;

/** Un record : equals et hashCode d'accord ; toString masque l'adresse (a***@example.org). */
public record Customer(String id, String email) {

    @Override
    public String toString() {
        int at = email.indexOf('@');
        return "Customer[" + id + ", " + (at <= 0 ? "***" : email.charAt(0) + "***" + email.substring(at)) + "]";
    }
}
