package library.model;

/**
 * SOLUTION - un livre : un record public dans un paquet EXPORTE, donc visible des modules qui lisent library.model.
 */
public record Book(String isbn, String title, String author, int year, Genre genre) {

    public static Book parse(String line) {
        String[] p = line.split(";");
        return new Book(p[0], p[1], p[2], Integer.parseInt(p[3]), Genre.valueOf(p[4]));
    }
}
