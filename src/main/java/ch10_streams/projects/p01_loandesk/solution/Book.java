package ch10_streams.projects.p01_loandesk.solution;

// TODO 1 : record immuable ; le stock, qui change, vit dans le guichet (Map isbn -> nombre).
public record Book(String isbn, String title, String author) {
    public static Book parse(String line) {
        String[] p = line.split(";");
        return new Book(p[0], p[1], p[2]);
    }
}
