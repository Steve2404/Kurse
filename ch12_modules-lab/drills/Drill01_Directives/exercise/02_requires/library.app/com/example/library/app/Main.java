package com.example.library.app;

import com.example.library.model.Book;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println(new Book("Dune", "Herbert", 1965).title());
    }
}
