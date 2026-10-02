package com.acme.demo;

import com.acme.text.Slugify;

/**
 * SOLUTION - un programme lance depuis le CLASSPATH : il vit dans le module SANS NOM.
 */
public class Demo {

    public static void main(String[] args) {
        Module m = Demo.class.getModule();
        System.out.println("classpath : " + Slugify.slug("Bonjour le Monde") + " ; module nomme " + m.isNamed() + ", nom " + m.getName());
    }
}
