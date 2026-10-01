package com.example.library.app;

import com.example.library.service.internal.InMemoryCatalog;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println(new InMemoryCatalog().books().size());
    }
}
