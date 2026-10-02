package library.app;

import library.model.Book;
import library.service.Catalog;
import library.service.internal.Normalizer;

import java.util.Arrays;
import java.util.List;

/**
 * SOLUTION du projet 1 - l'application modulaire.
 */
public class Main {

    static final String[] BOOKS = {
            "978-1;Le Petit Prince;Saint-Exupery;1943;ROMAN", "978-2;Dune;Herbert;1965;SF", "978-3;La Peste;Camus;1947;ROMAN",
            "978-4;Les Robots;Asimov;1950;SF", "978-5;Fondation;Asimov;1951;SF", "978-6;L'Etranger;Camus;1942;ROMAN",
            "978-7;Le Mythe de Sisyphe;Camus;1942;ESSAI", "978-8;La Nuit des temps;Barjavel;1968;SF", "978-9;Maigret;Simenon;1931;POLAR"};

    public static void main(String[] args) {
        List<Book> books = Arrays.stream(BOOKS).map(Book::parse).toList();
        Catalog catalog = new Catalog(books);
        System.out.println("catalogue : " + catalog.size() + " livres, par genre " + catalog.countByGenre());
        System.out.println("par decennie : " + catalog.byDecade());
        System.out.println("titres en 'p' : " + catalog.titlesStartingWith("p") + ", en 'n' : " + catalog.titlesStartingWith("n"));
        System.out.println("Camus : " + catalog.byAuthor("Camus").stream().map(Book::title).toList() + ", cle de 'L'Etranger' : " + Normalizer.sortKey("L'Etranger"));

        // A l'execution, chaque classe connait son module ; les exports se verifient aussi.
        Module app = Main.class.getModule();
        Module service = Catalog.class.getModule();
        Module model = Book.class.getModule();
        System.out.println("modules : " + app.getName() + ", " + service.getName() + ", " + model.getName() + " ; nomme " + app.isNamed());
        System.out.println("library.service.internal exporte a library.app " + service.isExported("library.service.internal", app) + ", a library.model "
                + service.isExported("library.service.internal", model) + ", a tous " + service.isExported("library.service.internal"));
        System.out.println("library.app lit library.model " + app.canRead(model) + " (par transitivite), library.model lit library.app " + model.canRead(app));
    }
}
