package ch10_streams.drills;

import java.io.IOException;
import java.util.List;

/**
 * Les donnees du "projet bibliotheque" partagees par TOUS les drills.
 * ===================================================================
 *
 * Toujours les memes livres, les memes membres, les memes emprunts :
 * ton cerveau n'a plus a decouvrir les donnees, il se concentre sur
 * les METHODES de l'API. Lis ce fichier une fois, garde-le ouvert a
 * cote pendant les drills.
 *
 * LIVRES (BOOKS), dans cet ordre :
 *
 *   isbn titre                  auteur         genre     annee pages prix  tags
 *   B1   Dune                   Herbert        SF        1965  412   9.5   classique, espace
 *   B2   Fondation              Asimov         SF        1951  255   8.0   classique, empire
 *   B3   Les Robots             Asimov         SF        1950  253   7.5   robots
 *   B4   Le Petit Prince        Saint-Exupery  Conte     1943   96   6.0   classique, enfance
 *   B5   1984                   Orwell         Dystopie  1949  328   8.5   classique, politique
 *   B6   La Ferme des animaux   Orwell         Dystopie  1945  112   5.5   politique, fable
 *   B7   Neuromancien           Gibson         SF        1984  271   9.0   cyberpunk
 *   B8   Le Hobbit              Tolkien        Fantasy   1937  310  10.0   aventure, enfance
 *
 * MEMBRES (MEMBERS) : email peut etre null ou blanc !
 *
 *   id  nom   age  email
 *   M1  Lea   17   "lea@mail.fr"
 *   M2  Hugo  34   null
 *   M3  Ines  52   "ines@biblio.org"
 *   M4  Tom   25   "  "            (blanc = inutilisable)
 *
 * EMPRUNTS (LOANS) : qui, quel livre, quel mois, combien de jours de retard
 *
 *   M1 B1 mois 1 retard 0
 *   M2 B5 mois 1 retard 3
 *   M1 B8 mois 2 retard 0
 *   M3 B2 mois 2 retard 10
 *   M2 B1 mois 3 retard 0
 *   M3 B7 mois 3 retard 1
 *   M1 B4 mois 3 retard 0
 *   M2 B6 mois 3 retard 5
 *
 * RECHERCHES DEJA ECRITES (utiles pour les jointures) :
 *
 *   Library.book("B5")   -> le Book 1984
 *   Library.member("M2") -> le Member Hugo
 *   Library.loadSummary("B1") -> "Resume de Dune"
 *       (throws IOException : pour "B3", lance IOException("resume introuvable : B3"))
 */
public final class Library {

    public record Book(String isbn, String title, String author, String genre, int year, int pages,
                       double price, List<String> tags) {
    }

    public record Member(String id, String name, int age, String email) {
    }

    public record Loan(String memberId, String isbn, int month, int daysLate) {
    }

    public static final List<Book> BOOKS = List.of(
            new Book("B1", "Dune", "Herbert", "SF", 1965, 412, 9.5, List.of("classique", "espace")),
            new Book("B2", "Fondation", "Asimov", "SF", 1951, 255, 8.0, List.of("classique", "empire")),
            new Book("B3", "Les Robots", "Asimov", "SF", 1950, 253, 7.5, List.of("robots")),
            new Book("B4", "Le Petit Prince", "Saint-Exupery", "Conte", 1943, 96, 6.0, List.of("classique", "enfance")),
            new Book("B5", "1984", "Orwell", "Dystopie", 1949, 328, 8.5, List.of("classique", "politique")),
            new Book("B6", "La Ferme des animaux", "Orwell", "Dystopie", 1945, 112, 5.5, List.of("politique", "fable")),
            new Book("B7", "Neuromancien", "Gibson", "SF", 1984, 271, 9.0, List.of("cyberpunk")),
            new Book("B8", "Le Hobbit", "Tolkien", "Fantasy", 1937, 310, 10.0, List.of("aventure", "enfance")));

    public static final List<Member> MEMBERS = List.of(
            new Member("M1", "Lea", 17, "lea@mail.fr"),
            new Member("M2", "Hugo", 34, null),
            new Member("M3", "Ines", 52, "ines@biblio.org"),
            new Member("M4", "Tom", 25, "  "));

    public static final List<Loan> LOANS = List.of(
            new Loan("M1", "B1", 1, 0),
            new Loan("M2", "B5", 1, 3),
            new Loan("M1", "B8", 2, 0),
            new Loan("M3", "B2", 2, 10),
            new Loan("M2", "B1", 3, 0),
            new Loan("M3", "B7", 3, 1),
            new Loan("M1", "B4", 3, 0),
            new Loan("M2", "B6", 3, 5));

    /** Le livre d'isbn donne (on suppose qu'il existe). */
    public static Book book(String isbn) {
        return BOOKS.stream().filter(b -> b.isbn().equals(isbn)).findFirst().orElseThrow();
    }

    /** Le membre d'id donne (on suppose qu'il existe). */
    public static Member member(String id) {
        return MEMBERS.stream().filter(m -> m.id().equals(id)).findFirst().orElseThrow();
    }

    /**
     * Simule la lecture du resume d'un livre dans un fichier d'archive.
     * Methode qui lance une exception VERIFIEE (IOException) : le
     * resume de B3 est "introuvable". Utilisee par le drill 13.
     */
    public static String loadSummary(String isbn) throws IOException {
        if (isbn.equals("B3")) {
            throw new IOException("resume introuvable : " + isbn);
        }
        return "Resume de " + book(isbn).title();
    }

    private Library() {
    }
}
