package com.example.library.app;

import com.example.library.model.Book;

import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws Exception {
        Field title = Book.class.getDeclaredField("title");
        title.setAccessible(true);
        System.out.println(title.get(new Book("Dune", "Herbert", 1965)));
    }
}
