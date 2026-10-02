package ch6_classdesign.projects.p07_media.solution;

/**
 * SOLUTION - un livre : 2 minutes de lecture par page.
 */
public class Book extends Media {

    public static final int MINUTES_PER_PAGE = 2;

    private final String author;
    private final int pages;
    private final Isbn isbn;

    public Book(String title, int year, String[] tags, String author, int pages, String isbn) {
        super(title, year, tags);
        this.author = author;
        this.pages = pages;
        this.isbn = new Isbn(isbn);
    }

    @Override
    public String kind() {
        return "livre";
    }

    @Override
    public int minutes() {
        return pages * MINUTES_PER_PAGE;
    }

    // Etendre plutot que remplacer : super.matches(...) garde la recherche commune.
    @Override
    public boolean matches(String query) {
        return super.matches(query) || author.equalsIgnoreCase(query);
    }

    public Isbn getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return super.toString() + " de " + author;
    }
}
