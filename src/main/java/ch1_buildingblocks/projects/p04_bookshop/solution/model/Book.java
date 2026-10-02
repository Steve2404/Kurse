package ch1_buildingblocks.projects.p04_bookshop.solution.model;

// Author est dans le MEME paquet : aucun import necessaire.
public class Book {
    String title;
    Author author;
    int year;

    public Book(String title, Author author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String title() {
        return title;
    }

    public Author author() {
        return author;
    }

    public int year() {
        return year;
    }

    public String describe() {
        return title + ", de " + author.fullName() + " (" + year + ")";
    }
}
