package ch18_design.projects.p05_messages;

import java.util.ArrayList;
import java.util.List;

/**
 * FOURNI (ne pas modifier) : l'ancienne classe des e-mails de la boulangerie.
 * Des constructeurs "telescopiques" (2, 4, 5 parametres...) et des setters : un e-mail peut exister
 * a moitie rempli, et il change dans le dos de celui qui l'a construit. Lance main pour voir trois surprises.
 */
public final class Data {

    private Data() {
    }

    public static void main(String[] args) {
        // Surprise 1 : que veut dire true ? (il faut ouvrir la classe pour le savoir)
        LegacyEmail promo = new LegacyEmail("contact@boulangerie.fr", "ada@example.org", "Promo", "-20 % sur les tartes", true);
        System.out.println("1. " + promo);

        // Surprise 2 : un e-mail a moitie construit existe, et rien ne l'empeche de partir.
        LegacyEmail draft = new LegacyEmail("contact@boulangerie.fr", "bob@example.org");
        System.out.println("2. " + draft);

        // Surprise 3 : la liste des destinataires est partagee avec l'appelant.
        List<String> team = new ArrayList<>(List.of("chef@boulangerie.fr"));
        LegacyEmail memo = new LegacyEmail("contact@boulangerie.fr", "x@example.org", "Planning", "Lundi 6 h");
        memo.setTo(team);
        team.add("stagiaire@example.org");
        System.out.println("3. " + memo);
    }

    public static final class LegacyEmail {
        private String from;
        private List<String> to = new ArrayList<>();
        private String subject;
        private String body;
        private boolean urgent;

        public LegacyEmail(String from, String to) {
            this.from = from;
            this.to.add(to);
        }

        public LegacyEmail(String from, String to, String subject, String body) {
            this(from, to);
            this.subject = subject;
            this.body = body;
        }

        public LegacyEmail(String from, String to, String subject, String body, boolean urgent) {
            this(from, to, subject, body);
            this.urgent = urgent;
        }

        public void setTo(List<String> to) {
            this.to = to;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        @Override
        public String toString() {
            return "de " + from + " a " + to + ", sujet " + subject + (urgent ? " (URGENT)" : "") + " : " + body;
        }
    }
}
