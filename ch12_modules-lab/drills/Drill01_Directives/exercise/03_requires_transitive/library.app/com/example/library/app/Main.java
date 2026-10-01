package com.example.library.app;

import com.example.library.model.Book;
import com.example.library.service.Catalog;

public class Main {

    public static void main(String[] args) throws Exception {
        Book first = Catalog.defaultCatalog().books().get(0);
        System.out.println(first.title());
    }
}
