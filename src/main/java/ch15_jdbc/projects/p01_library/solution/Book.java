package ch15_jdbc.projects.p01_library.solution;

/**
 * Un livre. pages est un Integer : il peut etre null, comme la colonne SQL (un int ne le pourrait pas).
 */
public record Book(String isbn, String title, String author, int pubYear, Integer pages) {

    /** "isbn|titre|auteur|annee|pages" ; un champ pages vide donne null. split(..., -1) garde le dernier champ vide. */
    static Book parse(String line) {
        String[] f = line.split("\\|", -1);
        return new Book(f[0], f[1], f[2], Integer.parseInt(f[3]), f[4].isEmpty() ? null : Integer.valueOf(f[4]));
    }

    String shortName() {
        return title + " (" + pubYear + ")";
    }
}
