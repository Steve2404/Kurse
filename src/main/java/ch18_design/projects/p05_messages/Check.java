package ch18_design.projects.p05_messages;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Email.java", "require(from != null, \"expediteur manquant\");", "require(true, \"expediteur manquant\");"),
            new Mutant("Email.java", "require(!to.isEmpty(), \"aucun destinataire\");", "require(true, \"aucun destinataire\");"),
            new Mutant("Email.java", "require(!subject.isBlank(), \"sujet vide\");", "require(!subject.isEmpty(), \"sujet vide\");"),
            new Mutant("Email.java", "totalKb <= MAX_ATTACHMENTS_KB", "totalKb < MAX_ATTACHMENTS_KB"),
            new Mutant("Email.java", "Stream.concat(to.stream(), cc.stream())", "to.stream()"),
            new Mutant("Email.java", "this.to = List.copyOf(b.to);", "this.to = java.util.Collections.unmodifiableList(b.to);"),
            new Mutant("Email.java", "this.attachments = List.copyOf(b.attachments);", "this.attachments = b.attachments;"),
            new Mutant("Email.java", "        b.cc.addAll(cc);\n", ""),
            new Mutant("Email.java", "if (!cc.isEmpty()) {", "if (cc != null) {"),
            new Mutant("Email.java", "if (priority != Priority.NORMAL) {", "if (priority == Priority.HIGH) {"),
            new Mutant("Email.java", "private Priority priority = Priority.NORMAL;", "private Priority priority = Priority.LOW;"),
            new Mutant("Email.java", ".body(\"Votre commande vous attend au comptoir.\").priority(Priority.HIGH)", ".body(\"Votre commande vous attend au comptoir.\")"),
            new Mutant("EmailAddress.java", "raw.strip().toLowerCase()", "raw.strip()"),
            new Mutant("EmailAddress.java", "CACHE.computeIfAbsent(normalized, EmailAddress::new)", "new EmailAddress(normalized)"),
            new Mutant("EmailAddress.java", "\\\\.[a-z]{2,}", "(\\\\.[a-z]{2,})?"),
            new Mutant("Attachment.java", "if (sizeKb < 1) {", "if (sizeKb < 0) {"));

    static final List<String> API_CODE = List.of(
            "record EmailAddress(", "static EmailAddress of(String", "computeIfAbsent(", "enum Priority", "record Attachment(",
            "public final class Email", "private Email(Builder", "public static final class Builder", "public static Builder builder()",
            "public Builder toBuilder()", "public Email build()", "return this;", "static Email welcome(", "static Email orderReady(",
            "List.copyOf(", "!public void set", "!Legacy",
            "max:method=12",
            "in:Email.java!public Email(##le constructeur d'Email est prive",
            "in:Email.java=private Builder()##le builder s'obtient par Email.builder()");

    static final List<String> API_TESTS = List.of(
            "Email.builder()", "toBuilder()", "assertSame(", "assertThrows(", "@ParameterizedTest", "\"\"\"",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 12, MUTANTS, API_CODE, API_TESTS);
    }
}
