package com.example.greeting.api;

public class SimpleGreeter implements Greeter {
    @Override
    public String greet(String name) {
        return "Bonjour, " + name + " !";
    }
}
