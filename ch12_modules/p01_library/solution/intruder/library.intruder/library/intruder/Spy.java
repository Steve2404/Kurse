package library.intruder;

import library.service.internal.Normalizer;

/**
 * SOLUTION - l'intrus : cette classe NE DOIT PAS compiler (export qualifie).
 */
public class Spy {

    public static void main(String[] args) {
        System.out.println(Normalizer.sortKey("Le Secret"));
    }
}
