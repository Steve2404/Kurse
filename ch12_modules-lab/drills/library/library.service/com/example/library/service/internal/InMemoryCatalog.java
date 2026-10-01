package com.example.library.service.internal;

import com.example.library.model.Book;
import com.example.library.service.Catalog;

import java.util.List;

/** L'implementation, dans un package INTERNE (normalement non exporte). */
public class InMemoryCatalog implements Catalog {

    @Override
    public List<Book> books() {
        return List.of(new Book("Dune", "Herbert", 1965), new Book("Fondation", "Asimov", 1951),
                new Book("Hyperion", "Simmons", 1989));
    }
}
