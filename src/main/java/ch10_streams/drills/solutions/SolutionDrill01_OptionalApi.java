package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Member;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill01_OptionalApi.
 */
public class SolutionDrill01_OptionalApi {

    public static Optional<String> emailOf(Member m) {
        // ofNullable : l'email de Hugo est null, Optional.of lancerait une NullPointerException.
        return Optional.ofNullable(m.email());
    }

    public static Optional<String> contactEmail(Member m) {
        // map ne s'execute que si la boite est pleine ; filter la vide si l'email est blanc.
        return emailOf(m).map(String::strip).filter(e -> !e.isEmpty());
    }

    public static boolean hasEmail(Member m) {
        // isPresent : true si la boite contient une valeur.
        return contactEmail(m).isPresent();
    }

    public static boolean hasNoEmail(Member m) {
        // isEmpty (Java 11) : le contraire de isPresent.
        return contactEmail(m).isEmpty();
    }

    public static String emailOrDefault(Member m) {
        // orElse : la valeur de secours est TOUJOURS calculee (ici une simple constante, donc sans cout).
        return contactEmail(m).orElse("accueil@biblio.fr");
    }

    public static String emailOrGenerated(Member m, Supplier<String> generator) {
        // orElseGet : le Supplier n'est appele QUE si la boite est vide (paresseux).
        return contactEmail(m).orElseGet(generator);
    }

    public static String emailOrFail(Member m) {
        // orElseThrow(Supplier) : on choisit l'exception, creee seulement si la boite est vide.
        return contactEmail(m).orElseThrow(() -> new IllegalStateException("Pas d'email : " + m.name()));
    }

    public static String emailOrNoSuchElement(Member m) {
        // orElseThrow() sans argument (Java 10) : NoSuchElementException si vide.
        return contactEmail(m).orElseThrow();
    }

    public static String unsafeGet(Optional<String> box) {
        // get() ne verifie rien : NoSuchElementException si vide. A eviter sans isPresent avant.
        return box.get();
    }

    public static void sendIfPossible(Member m, List<String> outbox) {
        // ifPresent : pas de "sinon", rien ne se passe pour une boite vide.
        contactEmail(m).ifPresent(e -> outbox.add("mail->" + e));
    }

    public static void notifyMember(Member m, List<String> log) {
        // ifPresentOrElse (Java 9) : Consumer si plein, Runnable (sans parametre) si vide.
        contactEmail(m).ifPresentOrElse(e -> log.add("mail:" + e), () -> log.add("courrier:" + m.name()));
    }

    public static Optional<String> domainOf(Member m) {
        // On transforme la valeur SANS ouvrir la boite : map rend un nouvel Optional.
        return contactEmail(m).map(e -> e.substring(e.indexOf('@') + 1));
    }

    public static Optional<Member> findMember(String id) {
        // findFirst rend deja un Optional : le membre peut ne pas exister.
        return Library.MEMBERS.stream().filter(m -> m.id().equals(id)).findFirst();
    }

    public static Optional<String> emailOfMemberId(String id) {
        // contactEmail rend un Optional : flatMap evite Optional<Optional<String>>.
        return findMember(id).flatMap(SolutionDrill01_OptionalApi::contactEmail);
    }

    public static Optional<String> emailOrBackup(Member m, Member backup) {
        // or (Java 9) rend une BOITE de secours, calculee seulement si la 1re est vide.
        return contactEmail(m).or(() -> contactEmail(backup));
    }

    public static List<String> allEmails() {
        // Optional::stream (Java 9) : 0 ou 1 element, donc flatMap retire les boites vides.
        return Library.MEMBERS.stream()
                .map(SolutionDrill01_OptionalApi::contactEmail)
                .flatMap(Optional::stream)
                .toList();
    }

    public static Optional<String> strictWrap(String s) {
        // Optional.of exige une valeur non nulle : null -> NullPointerException immediate.
        return Optional.of(s);
    }

    public static Optional<String> noEmail() {
        // Optional.empty() : le type String est deduit du type de retour.
        return Optional.empty();
    }

    public static Optional<String> longEmailOnly(Member m, int minLength) {
        // filter garde la valeur si le predicat est vrai, sinon rend une boite vide.
        return contactEmail(m).filter(e -> e.length() >= minLength);
    }
}
