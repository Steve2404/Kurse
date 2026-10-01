package com.example.greeting.app;

import com.example.greeting.api.Greeter;
import com.example.greeting.api.SimpleGreeter;

public class Main {
    public static void main(String[] args) {
        Greeter greeter = new SimpleGreeter();
        System.out.println(greeter.greet("Steve"));
    }
}
