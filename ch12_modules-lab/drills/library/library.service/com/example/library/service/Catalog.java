package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.service.internal.InMemoryCatalog;

import java.util.List;

/** L'interface du service : un catalogue de livres. Book apparait dans l'API publique. */
public interface Catalog {

    List<Book> books();

    static Catalog defaultCatalog() {
        return new InMemoryCatalog();
    }
}
