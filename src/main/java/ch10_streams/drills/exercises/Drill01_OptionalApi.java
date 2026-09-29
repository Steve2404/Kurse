package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Member;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DRILL 01 - Toutes les methodes de Optional<T> (projet bibliotheque)
 * ===================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule, comme des gammes au piano. Chaque TODO
 * tient en 1 a 3 lignes et vise UNE methode precise de l'API.
 *
 *   1. Chronometre-toi. Note ton temps et ton score dans
 *      drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Si tu bloques
 *      plus d'une minute : regarde la carte, puis cache-la et reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir
 *      REVISION.md pour remettre les TODO a vide et pour le calendrier).
 *
 * Les donnees viennent TOUJOURS de ch10_streams.drills.Library (lis ce
 * fichier une fois). Ici : les membres et leur email (null pour Hugo,
 * blanc pour Tom).
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : emailOf(m)          [ofNullable]  la boite de l'email brut (peut etre null).
 * TODO 2  : contactEmail(m)     [map, filter] email nettoye (strip), vide si blanc ou absent.
 *           Lea -> "lea@mail.fr" ; Hugo -> vide ; Tom -> vide.
 * TODO 3  : hasEmail(m)         [isPresent]   contactEmail est-il plein ?
 * TODO 4  : hasNoEmail(m)       [isEmpty]     contactEmail est-il vide ?
 * TODO 5  : emailOrDefault(m)   [orElse]      sinon "accueil@biblio.fr".
 * TODO 6  : emailOrGenerated(m, generator) [orElseGet] sinon generator.get()
 *           (le generateur ne doit JAMAIS etre appele si l'email existe).
 * TODO 7  : emailOrFail(m)      [orElseThrow(Supplier)] sinon
 *           IllegalStateException("Pas d'email : " + nom).
 * TODO 8  : emailOrNoSuchElement(m) [orElseThrow()] sinon NoSuchElementException.
 * TODO 9  : unsafeGet(box)      [get]         ouvre la boite sans verifier.
 * TODO 10 : sendIfPossible(m, outbox) [ifPresent] ajoute "mail->" + email.
 * TODO 11 : notifyMember(m, log) [ifPresentOrElse] "mail:" + email, sinon "courrier:" + nom.
 * TODO 12 : domainOf(m)         [map]         la partie apres '@' ; Ines -> "biblio.org".
 * TODO 13 : findMember(id)      [findFirst]   le membre d'id donne dans Library.MEMBERS.
 * TODO 14 : emailOfMemberId(id) [flatMap]     findMember + contactEmail.
 *           "M1" -> "lea@mail.fr" ; "M2" -> vide ; "M9" -> vide.
 * TODO 15 : emailOrBackup(m, backup) [or]     contactEmail(m), sinon contactEmail(backup)
 *           (reste une boite !).
 * TODO 16 : allEmails()         [Optional::stream] les emails utilisables de tous
 *           les membres -> [lea@mail.fr, ines@biblio.org].
 * TODO 17 : strictWrap(s)       [of]          Optional.of(s) (lance NPE si s == null).
 * TODO 18 : noEmail()           [empty]       une boite vide de String.
 * TODO 19 : longEmailOnly(m, minLength) [filter] contactEmail seulement si sa
 *           longueur >= minLength.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Creer   : Optional.of(v)  Optional.ofNullable(v)  Optional.empty()
 *   Tester  : isPresent()  isEmpty() (Java 11)
 *   Ouvrir  : get()  orElse(T)  orElseGet(Supplier<T>)
 *             orElseThrow() (Java 10)  orElseThrow(Supplier<X extends Throwable>)
 *   Agir    : ifPresent(Consumer<T>)  ifPresentOrElse(Consumer<T>, Runnable) (9)
 *   Transfo : map(Function<T,U>) -> Optional<U>
 *             flatMap(Function<T,Optional<U>>) -> Optional<U>
 *             filter(Predicate<T>)
 *             or(Supplier<Optional<T>>) (9)
 *             stream() -> 0 ou 1 element (9)
 * ---------------------------------------------------------------------
 */
public class Drill01_OptionalApi {

    public static Optional<String> emailOf(Member m) {
        throw new UnsupportedOperationException("TODO 1 : implementer emailOf()");
    }

    public static Optional<String> contactEmail(Member m) {
        throw new UnsupportedOperationException("TODO 2 : implementer contactEmail()");
    }

    public static boolean hasEmail(Member m) {
        throw new UnsupportedOperationException("TODO 3 : implementer hasEmail()");
    }

    public static boolean hasNoEmail(Member m) {
        throw new UnsupportedOperationException("TODO 4 : implementer hasNoEmail()");
    }

    public static String emailOrDefault(Member m) {
        throw new UnsupportedOperationException("TODO 5 : implementer emailOrDefault()");
    }

    public static String emailOrGenerated(Member m, Supplier<String> generator) {
        throw new UnsupportedOperationException("TODO 6 : implementer emailOrGenerated()");
    }

    public static String emailOrFail(Member m) {
        throw new UnsupportedOperationException("TODO 7 : implementer emailOrFail()");
    }

    public static String emailOrNoSuchElement(Member m) {
        throw new UnsupportedOperationException("TODO 8 : implementer emailOrNoSuchElement()");
    }

    public static String unsafeGet(Optional<String> box) {
        throw new UnsupportedOperationException("TODO 9 : implementer unsafeGet()");
    }

    public static void sendIfPossible(Member m, List<String> outbox) {
        throw new UnsupportedOperationException("TODO 10 : implementer sendIfPossible()");
    }

    public static void notifyMember(Member m, List<String> log) {
        throw new UnsupportedOperationException("TODO 11 : implementer notifyMember()");
    }

    public static Optional<String> domainOf(Member m) {
        throw new UnsupportedOperationException("TODO 12 : implementer domainOf()");
    }

    public static Optional<Member> findMember(String id) {
        throw new UnsupportedOperationException("TODO 13 : implementer findMember()");
    }

    public static Optional<String> emailOfMemberId(String id) {
        throw new UnsupportedOperationException("TODO 14 : implementer emailOfMemberId()");
    }

    public static Optional<String> emailOrBackup(Member m, Member backup) {
        throw new UnsupportedOperationException("TODO 15 : implementer emailOrBackup()");
    }

    public static List<String> allEmails() {
        throw new UnsupportedOperationException("TODO 16 : implementer allEmails()");
    }

    public static Optional<String> strictWrap(String s) {
        throw new UnsupportedOperationException("TODO 17 : implementer strictWrap()");
    }

    public static Optional<String> noEmail() {
        throw new UnsupportedOperationException("TODO 18 : implementer noEmail()");
    }

    public static Optional<String> longEmailOnly(Member m, int minLength) {
        throw new UnsupportedOperationException("TODO 19 : implementer longEmailOnly()");
    }

    public static void main(String[] args) {
        Member lea = Library.MEMBERS.get(0);
        Member hugo = Library.MEMBERS.get(1);
        Member ines = Library.MEMBERS.get(2);
        Member tom = Library.MEMBERS.get(3);

        ExerciseChecker.check("1  emailOf(Lea) plein, emailOf(Hugo) vide, emailOf(Tom) == '  '",
                emailOf(lea).equals(Optional.of("lea@mail.fr")) && emailOf(hugo).isEmpty() && emailOf(tom).equals(Optional.of("  ")));
        ExerciseChecker.check("2  contactEmail : Lea plein, Hugo et Tom vides",
                contactEmail(lea).equals(Optional.of("lea@mail.fr")) && contactEmail(hugo).isEmpty() && contactEmail(tom).isEmpty());
        ExerciseChecker.check("3  hasEmail(Ines) && !hasEmail(Tom)", hasEmail(ines) && !hasEmail(tom));
        ExerciseChecker.check("4  hasNoEmail(Hugo) && !hasNoEmail(Lea)", hasNoEmail(hugo) && !hasNoEmail(lea));
        ExerciseChecker.check("5  emailOrDefault(Tom) == accueil@biblio.fr, (Lea) == lea@mail.fr",
                emailOrDefault(tom).equals("accueil@biblio.fr") && emailOrDefault(lea).equals("lea@mail.fr"));

        int[] calls = {0};
        Supplier<String> generator = () -> {
            calls[0]++;
            return "genere@biblio.fr";
        };
        ExerciseChecker.check("6  emailOrGenerated(Lea) sans appeler le generateur",
                emailOrGenerated(lea, generator).equals("lea@mail.fr") && calls[0] == 0);
        ExerciseChecker.check("6  emailOrGenerated(Hugo) appelle le generateur 1 fois",
                emailOrGenerated(hugo, generator).equals("genere@biblio.fr") && calls[0] == 1);

        String message = null;
        try {
            emailOrFail(hugo);
        } catch (IllegalStateException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("7  emailOrFail(Hugo) lance IllegalStateException(\"Pas d'email : Hugo\")",
                "Pas d'email : Hugo".equals(message) && emailOrFail(ines).equals("ines@biblio.org"));

        boolean nse = false;
        try {
            emailOrNoSuchElement(tom);
        } catch (NoSuchElementException e) {
            nse = true;
        }
        ExerciseChecker.check("8  emailOrNoSuchElement(Tom) lance NoSuchElementException", nse);

        ExerciseChecker.check("9  unsafeGet(Optional[x]) == x", unsafeGet(Optional.of("x")).equals("x"));

        List<String> outbox = new java.util.ArrayList<>();
        Library.MEMBERS.forEach(m -> sendIfPossible(m, outbox));
        ExerciseChecker.check("10 sendIfPossible -> [mail->lea@mail.fr, mail->ines@biblio.org]",
                outbox.equals(List.of("mail->lea@mail.fr", "mail->ines@biblio.org")));

        List<String> log = new java.util.ArrayList<>();
        Library.MEMBERS.forEach(m -> notifyMember(m, log));
        ExerciseChecker.check("11 notifyMember -> [mail:lea@mail.fr, courrier:Hugo, mail:ines@biblio.org, courrier:Tom]",
                log.equals(List.of("mail:lea@mail.fr", "courrier:Hugo", "mail:ines@biblio.org", "courrier:Tom")));

        ExerciseChecker.check("12 domainOf(Ines) == biblio.org, domainOf(Hugo) vide",
                domainOf(ines).equals(Optional.of("biblio.org")) && domainOf(hugo).isEmpty());
        ExerciseChecker.check("13 findMember(M3) == Ines, findMember(M9) vide",
                findMember("M3").map(Member::name).equals(Optional.of("Ines")) && findMember("M9").isEmpty());
        ExerciseChecker.check("14 emailOfMemberId : M1 plein, M2 vide, M9 vide",
                emailOfMemberId("M1").equals(Optional.of("lea@mail.fr")) && emailOfMemberId("M2").isEmpty()
                        && emailOfMemberId("M9").isEmpty());
        ExerciseChecker.check("15 emailOrBackup(Hugo, Ines) == ines@..., (Hugo, Tom) vide",
                emailOrBackup(hugo, ines).equals(Optional.of("ines@biblio.org")) && emailOrBackup(hugo, tom).isEmpty());
        ExerciseChecker.check("16 allEmails == [lea@mail.fr, ines@biblio.org]",
                allEmails().equals(List.of("lea@mail.fr", "ines@biblio.org")));

        boolean npe = false;
        try {
            strictWrap(null);
        } catch (NullPointerException e) {
            npe = true;
        }
        ExerciseChecker.check("17 strictWrap(\"a\") plein, strictWrap(null) lance NPE",
                strictWrap("a").equals(Optional.of("a")) && npe);
        ExerciseChecker.check("18 noEmail() est vide", noEmail().isEmpty());
        ExerciseChecker.check("19 longEmailOnly(Lea, 11) plein, (Lea, 12) vide",
                longEmailOnly(lea, 11).isPresent() && longEmailOnly(lea, 12).isEmpty());

        ExerciseChecker.summary();
    }
}
