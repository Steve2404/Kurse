package ch18_design.projects.p05_messages.solution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Un e-mail IMMUABLE, construit par un BUILDER. Le constructeur est prive : la seule facon d'obtenir
 * un Email est build(), qui valide TOUT avant de le creer. Un e-mail a moitie rempli ne peut pas exister.
 */
public final class Email {

    static final int MAX_ATTACHMENTS_KB = 10_000;
    static final EmailAddress SHOP = EmailAddress.of("contact@boulangerie.fr");

    private final EmailAddress from;
    private final List<EmailAddress> to;
    private final List<EmailAddress> cc;
    private final String subject;
    private final String body;
    private final List<Attachment> attachments;
    private final Priority priority;

    // Des COPIES non modifiables : le builder peut continuer a changer, cet e-mail ne bougera plus.
    private Email(Builder b) {
        this.from = b.from;
        this.to = List.copyOf(b.to);
        this.cc = List.copyOf(b.cc);
        this.subject = b.subject;
        this.body = b.body;
        this.attachments = List.copyOf(b.attachments);
        this.priority = b.priority;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Les fabriques statiques : des e-mails tout faits, avec un NOM qui dit ce qu'ils sont.
    public static Email welcome(String to) {
        return builder().from(SHOP).to(to).subject("Bienvenue").body("Merci de votre inscription !").build();
    }

    public static Email orderReady(String to, String orderId) {
        return builder().from(SHOP).to(to).subject("Commande " + orderId + " prete")
                .body("Votre commande vous attend au comptoir.").priority(Priority.HIGH).build();
    }

    /** Un builder pre-rempli avec cet e-mail : pour fabriquer une VARIANTE sans toucher a l'original. */
    public Builder toBuilder() {
        Builder b = builder().from(from).subject(subject).body(body).priority(priority);
        b.to.addAll(to);
        b.cc.addAll(cc);
        b.attachments.addAll(attachments);
        return b;
    }

    public EmailAddress from() {
        return from;
    }

    public List<EmailAddress> to() {
        return to;
    }

    public List<EmailAddress> cc() {
        return cc;
    }

    public String subject() {
        return subject;
    }

    public String body() {
        return body;
    }

    public List<Attachment> attachments() {
        return attachments;
    }

    public Priority priority() {
        return priority;
    }

    /** Le texte de l'e-mail ; les lignes facultatives n'apparaissent que si elles ont un contenu. */
    public String render() {
        StringBuilder out = new StringBuilder("De : " + from + "\nA : " + joined(to) + "\n");
        if (!cc.isEmpty()) {
            out.append("Cc : ").append(joined(cc)).append('\n');
        }
        out.append("Sujet : ").append(subject).append('\n');
        if (priority != Priority.NORMAL) {
            out.append("Priorite : ").append(priority).append('\n');
        }
        if (!attachments.isEmpty()) {
            out.append("Pieces jointes : ").append(joined(attachments)).append('\n');
        }
        return out.append('\n').append(body).append('\n').toString();
    }

    private static String joined(List<?> items) {
        return items.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    /** Le BUILDER : mutable, chaque methode rend this pour enchainer les appels. */
    public static final class Builder {
        private EmailAddress from;
        private final List<EmailAddress> to = new ArrayList<>();
        private final List<EmailAddress> cc = new ArrayList<>();
        private String subject = "";
        private String body = "";
        private final List<Attachment> attachments = new ArrayList<>();
        private Priority priority = Priority.NORMAL;

        private Builder() {
        }

        public Builder from(EmailAddress from) {
            this.from = from;
            return this;
        }

        public Builder from(String from) {
            return from(EmailAddress.of(from));
        }

        public Builder to(String... addresses) {
            Stream.of(addresses).map(EmailAddress::of).forEach(to::add);
            return this;
        }

        public Builder cc(String... addresses) {
            Stream.of(addresses).map(EmailAddress::of).forEach(cc::add);
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder attach(String name, int sizeKb) {
            attachments.add(new Attachment(name, sizeKb));
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        /** Valide TOUT, puis cree l'e-mail. Le builder reste utilisable : un second build() donne un autre e-mail. */
        public Email build() {
            require(from != null, "expediteur manquant");
            require(!to.isEmpty(), "aucun destinataire");
            require(!subject.isBlank(), "sujet vide");
            int totalKb = attachments.stream().mapToInt(Attachment::sizeKb).sum();
            require(totalKb <= MAX_ATTACHMENTS_KB, "pieces jointes trop lourdes : " + totalKb + " Ko");
            requireNoDuplicate();
            return new Email(this);
        }

        // Une adresse ne recoit l'e-mail qu'une fois, qu'elle soit dans "A" ou dans "Cc".
        private void requireNoDuplicate() {
            Set<EmailAddress> seen = new HashSet<>();
            Stream.concat(to.stream(), cc.stream())
                    .filter(address -> !seen.add(address))
                    .findFirst()
                    .ifPresent(address -> require(false, "destinataire en double : " + address));
        }

        private static void require(boolean condition, String message) {
            if (!condition) {
                throw new IllegalStateException(message);
            }
        }
    }
}
