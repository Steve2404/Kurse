package ch16_testing.projects.p08_library.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/** Les tests de reference du capstone : la fonction pure en tableau, le service avec Mockito et une horloge figee. */
@DisplayName("La mediatheque")
@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    static final LocalDate TODAY = LocalDate.of(2026, 5, 15);
    static final Clock CLOCK = Clock.fixed(Instant.parse("2026-05-15T10:00:00Z"), ZoneOffset.UTC);
    static final Member ANA = new Member("M1", "Ana", false);
    static final Member PREMIUM = new Member("M2", "Bob", true);

    @Mock
    Catalog catalog;
    @Mock
    LoanRepository loans;
    @Mock
    MemberDirectory members;
    @Mock
    Mailer mailer;

    LibraryService service;

    @BeforeEach
    void setUp() {
        // Pourquoi pas @InjectMocks : l'horloge n'est pas un simulacre ; on appelle le constructeur nous-memes.
        service = new LibraryService(catalog, loans, members, mailer, CLOCK);
        // Pourquoi lenient : ces reponses servent a la plupart des tests, pas a tous (Mockito strict refuserait).
        lenient().when(members.find("M1")).thenReturn(Optional.of(ANA));
        lenient().when(members.find("M2")).thenReturn(Optional.of(PREMIUM));
        lenient().when(catalog.copies("ISBN-1")).thenReturn(2);
    }

    static Loan loan(String isbn, String member, LocalDate due) {
        return new Loan(isbn, member, due.minusDays(21), due);
    }

    @Nested
    @DisplayName("l'amende")
    class Fine {

        @ParameterizedTest(name = "{0} jour(s) de retard, premium {1} : {2} centimes")
        @CsvSource({
                "-5, false, 0", "0, false, 0", "1, false, 20", "10, false, 200", "50, false, 1000", "51, false, 1000",
                "3, true, 0", "4, true, 20", "53, true, 1000", "54, true, 1000"})
        void followsTheRules(long daysLate, boolean premium, long expected) {
            assertEquals(expected, LibraryService.fine(daysLate, premium));
        }
    }

    @Nested
    @DisplayName("emprunter")
    class Borrow {

        @Test
        void aMemberBorrowsForTwentyOneDays() {
            when(loans.activeLoansOfMember("M1")).thenReturn(List.of());
            when(loans.activeLoansOfBook("ISBN-1")).thenReturn(List.of());

            Loan l = service.borrow("M1", "ISBN-1");

            assertEquals(new Loan("ISBN-1", "M1", TODAY, LocalDate.of(2026, 6, 5)), l);
            verify(loans).save(l);
        }

        @Test
        void aPremiumMemberBorrowsForTwentyEightDays() {
            when(loans.activeLoansOfMember("M2")).thenReturn(List.of());
            when(loans.activeLoansOfBook("ISBN-1")).thenReturn(List.of());

            ArgumentCaptor<Loan> saved = ArgumentCaptor.forClass(Loan.class);
            service.borrow("M2", "ISBN-1");
            verify(loans).save(saved.capture());
            assertEquals(LocalDate.of(2026, 6, 12), saved.getValue().due());
        }

        @Test
        void unknownMemberOrBook() {
            when(members.find("X")).thenReturn(Optional.empty());
            NoSuchElementException m = assertThrows(NoSuchElementException.class, () -> service.borrow("X", "ISBN-1"));
            NoSuchElementException b = assertThrows(NoSuchElementException.class, () -> service.borrow("M1", "ISBN-9"));
            assertAll(
                    () -> assertEquals("membre inconnu : X", m.getMessage()),
                    () -> assertEquals("livre inconnu : ISBN-9", b.getMessage()));
            verify(loans, never()).save(any());
        }

        @Test
        void threeLoansIsTheLimitButPremiumGoesToFive() {
            List<Loan> three = List.of(loan("A", "M1", TODAY), loan("B", "M1", TODAY), loan("C", "M1", TODAY));
            when(loans.activeLoansOfMember("M1")).thenReturn(three);
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.borrow("M1", "ISBN-1"));
            assertEquals("limite atteinte : M1", e.getMessage());

            when(loans.activeLoansOfMember("M2")).thenReturn(Collections.nCopies(4, loan("A", "M2", TODAY)));
            when(loans.activeLoansOfBook("ISBN-1")).thenReturn(List.of());
            assertEquals(TODAY, service.borrow("M2", "ISBN-1").borrowed());
            when(loans.activeLoansOfMember("M2")).thenReturn(Collections.nCopies(5, loan("A", "M2", TODAY)));
            assertThrows(IllegalStateException.class, () -> service.borrow("M2", "ISBN-1"));
        }

        @Test
        void twoLoansStillLeaveRoomForAThird() {
            when(loans.activeLoansOfMember("M1")).thenReturn(List.of(loan("A", "M1", TODAY), loan("B", "M1", TODAY)));
            when(loans.activeLoansOfBook("ISBN-1")).thenReturn(List.of());
            assertEquals("ISBN-1", service.borrow("M1", "ISBN-1").isbn());
        }

        @Test
        @DisplayName("un pret qui expire aujourd'hui n'est pas un retard ; hier, si")
        void overdueMeansBeforeToday() {
            when(loans.activeLoansOfMember("M1")).thenReturn(List.of(loan("A", "M1", TODAY)));
            when(loans.activeLoansOfBook("ISBN-1")).thenReturn(List.of());
            assertEquals("ISBN-1", service.borrow("M1", "ISBN-1").isbn());

            when(loans.activeLoansOfMember("M1")).thenReturn(List.of(loan("A", "M1", TODAY.minusDays(1))));
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.borrow("M1", "ISBN-1"));
            assertEquals("retard en cours : M1", e.getMessage());
        }

        @Test
        @DisplayName("le retard passe avant la limite")
        void overdueIsReportedBeforeTheLimit() {
            List<Loan> late = List.of(loan("A", "M1", TODAY.minusDays(1)), loan("B", "M1", TODAY), loan("C", "M1", TODAY));
            when(loans.activeLoansOfMember("M1")).thenReturn(late);
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.borrow("M1", "ISBN-1"));
            assertEquals("retard en cours : M1", e.getMessage());
        }

        @Test
        void allCopiesOut() {
            when(loans.activeLoansOfMember("M1")).thenReturn(List.of());
            when(loans.activeLoansOfBook("ISBN-1")).thenReturn(List.of(loan("ISBN-1", "M7", TODAY)));
            assertEquals("ISBN-1", service.borrow("M1", "ISBN-1").isbn());

            when(loans.activeLoansOfBook("ISBN-1")).thenReturn(List.of(loan("ISBN-1", "M7", TODAY), loan("ISBN-1", "M8", TODAY)));
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.borrow("M1", "ISBN-1"));
            assertEquals("plus d'exemplaire : ISBN-1", e.getMessage());
        }
    }

    @Nested
    @DisplayName("rendre")
    class GiveBack {

        @Test
        void onTimeCostsNothingAndSendsNothing() {
            Loan l = loan("ISBN-1", "M1", TODAY);
            when(loans.activeLoansOfMember("M1")).thenReturn(List.of(l));

            assertEquals(0, service.giveBack("M1", "ISBN-1"));
            verify(loans).close(l, TODAY);
            verifyNoInteractions(mailer);
        }

        @Test
        void tenDaysLateCostsTwoEurosAndSendsAMail() {
            Loan other = loan("ISBN-2", "M1", TODAY);
            Loan l = loan("ISBN-1", "M1", TODAY.minusDays(10));
            when(loans.activeLoansOfMember("M1")).thenReturn(List.of(other, l));

            assertEquals(200, service.giveBack("M1", "ISBN-1"));
            verify(loans).close(l, TODAY);
            verify(mailer).send("M1", "Amende : 200 centimes pour ISBN-1");
        }

        @Test
        void premiumMembersGetThreeFreeDays() {
            Loan l = loan("ISBN-1", "M2", TODAY.minusDays(3));
            when(loans.activeLoansOfMember("M2")).thenReturn(List.of(l));

            assertEquals(0, service.giveBack("M2", "ISBN-1"));
            verify(mailer, never()).send(anyString(), anyString());
        }

        @Test
        void unknownLoan() {
            when(loans.activeLoansOfMember("M1")).thenReturn(List.of(loan("ISBN-2", "M1", TODAY)));
            NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> service.giveBack("M1", "ISBN-1"));
            assertEquals("pret introuvable : M1 / ISBN-1", e.getMessage());
            verify(loans, never()).close(any(), any());
        }
    }
}
