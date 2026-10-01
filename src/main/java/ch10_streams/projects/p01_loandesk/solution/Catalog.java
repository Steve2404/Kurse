package ch10_streams.projects.p01_loandesk.solution;

import java.util.Optional;

// TODO 3 : la composition "isbn sinon titre" est ecrite une fois, dans l'interface.
public interface Catalog {
    Optional<Book> byIsbn(String isbn);

    Optional<Book> byTitle(String title);

    // or (Java 9) : le Supplier n'est appele que si byIsbn est vide, et le resultat reste un Optional.
    default Optional<Book> find(String query) {
        return byIsbn(query).or(() -> byTitle(query));
    }
}
