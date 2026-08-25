package com.example.reflect.tool;

import com.example.model.entities.Person;
import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws Exception {
        Person person = new Person("Steve");
        Field field = Person.class.getDeclaredField("name");
        field.setAccessible(true);
        String value = (String) field.get(person);
        System.out.println("Champ prive lu par reflexion : " + value);
    }
}
