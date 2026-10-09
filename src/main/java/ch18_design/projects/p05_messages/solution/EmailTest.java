package ch18_design.projects.p05_messages.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    private static Email.Builder minimal() {
        return Email.builder().from("contact@boulangerie.fr").to("ada@example.org").subject("Promo");
    }

    private static String refusal(Email.Builder builder) {
        return assertThrows(IllegalStateException.class, builder::build).getMessage();
    }

    @Test
    void fullEmailRendersEveryLine() {
        Email email = Email.builder().from("contact@boulangerie.fr").to("ada@example.org", "bob@example.org")
                .cc("chef@boulangerie.fr").subject("Menu de la semaine").body("Voir le menu en piece jointe.")
                .attach("menu.pdf", 120).attach("plan.png", 80).priority(Priority.LOW).build();
        assertEquals("""
                De : contact@boulangerie.fr
                A : ada@example.org, bob@example.org
                Cc : chef@boulangerie.fr
                Sujet : Menu de la semaine
                Priorite : LOW
                Pieces jointes : menu.pdf (120 Ko), plan.png (80 Ko)

                Voir le menu en piece jointe.
                """, email.render());
    }

    @Test
    void optionalLinesAreHiddenAndDefaultsApply() {
        Email email = minimal().build();
        assertEquals(Priority.NORMAL, email.priority());
        assertEquals("", email.body());
        assertEquals("""
                De : contact@boulangerie.fr
                A : ada@example.org
                Sujet : Promo


                """, email.render());
    }

    @Test
    void buildValidatesEverything() {
        assertAll(
                () -> assertEquals("expediteur manquant", refusal(Email.builder().to("ada@example.org").subject("x"))),
                () -> assertEquals("aucun destinataire", refusal(Email.builder().from("a@example.org").subject("x"))),
                () -> assertEquals("sujet vide", refusal(minimal().subject("   "))),
                () -> assertEquals("destinataire en double : ada@example.org", refusal(minimal().cc("ADA@example.org"))),
                () -> assertEquals("destinataire en double : bob@example.org", refusal(minimal().to("bob@example.org", "bob@example.org"))),
                () -> assertEquals("pieces jointes trop lourdes : 10001 Ko",
                        refusal(minimal().attach("a.zip", 6000).attach("b.zip", 4001))));
    }

    @Test
    void attachmentsUpToTheLimitIncluded() {
        assertEquals(2, minimal().attach("a.zip", 6000).attach("b.zip", 4000).build().attachments().size());
        assertEquals("taille invalide : 0",
                assertThrows(IllegalArgumentException.class, () -> minimal().attach("vide.txt", 0)).getMessage());
    }

    // Le builder continue de vivre apres build() : l'e-mail deja construit ne doit pas bouger.
    @Test
    void builtEmailIsIndependentFromTheBuilder() {
        Email.Builder builder = minimal().attach("a.pdf", 10);
        Email first = builder.build();
        builder.to("bob@example.org").attach("b.pdf", 10).subject("Autre");
        Email second = builder.build();
        assertEquals(List.of(EmailAddress.of("ada@example.org")), first.to());
        assertEquals(1, first.attachments().size());
        assertEquals("Promo", first.subject());
        assertEquals(2, second.to().size());
        assertThrows(UnsupportedOperationException.class, () -> first.to().add(EmailAddress.of("x@example.org")));
        assertThrows(UnsupportedOperationException.class, () -> first.attachments().clear());
    }

    @Test
    void toBuilderMakesAVariantAndLeavesTheOriginal() {
        Email original = minimal().cc("chef@boulangerie.fr").body("Corps").attach("a.pdf", 10).priority(Priority.HIGH).build();
        Email variant = original.toBuilder().to("bob@example.org").subject("Rappel").build();
        assertAll(
                () -> assertEquals("Promo", original.subject()),
                () -> assertEquals(1, original.to().size()),
                () -> assertEquals(List.of(EmailAddress.of("ada@example.org"), EmailAddress.of("bob@example.org")), variant.to()),
                () -> assertEquals(List.of(EmailAddress.of("chef@boulangerie.fr")), variant.cc()),
                () -> assertEquals("Corps", variant.body()),
                () -> assertEquals(original.attachments(), variant.attachments()),
                () -> assertEquals(Priority.HIGH, variant.priority()));
    }

    @Test
    void staticFactories() {
        Email ready = Email.orderReady("ada@example.org", "CMD-7");
        assertEquals("""
                De : contact@boulangerie.fr
                A : ada@example.org
                Sujet : Commande CMD-7 prete
                Priorite : HIGH

                Votre commande vous attend au comptoir.
                """, ready.render());
        Email welcome = Email.welcome(" Bob@Example.org ");
        assertEquals("Bienvenue", welcome.subject());
        assertEquals(Priority.NORMAL, welcome.priority());
        assertEquals(List.of(EmailAddress.of("bob@example.org")), welcome.to());
    }

    @Test
    void addressesAreNormalizedAndShared() {
        assertSame(EmailAddress.of(" Ada@Example.ORG "), EmailAddress.of("ada@example.org"));
        assertEquals("ada@example.org", EmailAddress.of("ADA@example.org").value());
        assertNotSame(EmailAddress.of("ada@example.org"), EmailAddress.of("bob@example.org"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ada", "ada@example", "ada@@example.org", "a b@example.org", "@example.org", "ada@example.o"})
    void invalidAddresses(String raw) {
        assertThrows(IllegalArgumentException.class, () -> EmailAddress.of(raw));
    }
}
