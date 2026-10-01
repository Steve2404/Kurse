package com.example.library.app;

import com.example.library.model.Book;
import com.example.library.service.Catalog;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) throws Exception {
        List<Book> books = ServiceLoader.load(Catalog.class).findFirst().orElseThrow().books();
        System.out.println("Catalogue : " + books.size() + " livres");
        System.out.println("Premier titre : " + books.get(0).title());
        Field author = Book.class.getDeclaredField("author");
        author.setAccessible(true);
        System.out.println("Auteur lu par reflexion : " + author.get(books.get(0)));
    }
}
