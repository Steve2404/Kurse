package com.example.library.app;

import com.example.library.service.Catalog;

import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println(ServiceLoader.load(Catalog.class).findFirst().orElseThrow().books().size());
    }
}
