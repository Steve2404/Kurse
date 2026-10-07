package ch1_buildingblocks.projects.p04_bookshop.solution.export;

// Piege : cette classe s'appelle deja Book. On ne peut donc PAS importer model.Book
// ("Book is already defined in this compilation unit") : on utilise son nom pleinement qualifie.
public class Book {
    ch1_buildingblocks.projects.p04_bookshop.solution.model.Book source;

    public Book(ch1_buildingblocks.projects.p04_bookshop.solution.model.Book source) {
        this.source = source;
    }

    public String csv() {
        return "\"" + source.title() + "\";\"" + source.author().sortName() + "\";" + source.year();
    }
}
