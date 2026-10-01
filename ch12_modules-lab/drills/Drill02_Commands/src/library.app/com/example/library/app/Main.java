package com.example.library.app;

import com.example.library.model.Book;
import com.example.library.service.Catalog;

import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {
        List<Book> books = ServiceLoader.load(Catalog.class).findFirst().orElseThrow().books();
        Book oldest = books.stream().min(Comparator.comparingInt(Book::year)).orElseThrow();
        System.out.println(books.size() + " livres, le plus ancien : " + oldest.title());
    }
}
