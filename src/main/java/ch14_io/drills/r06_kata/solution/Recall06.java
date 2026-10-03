package ch14_io.drills.r06_kata.solution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * SOLUTION du drill de rappel 6 - kata : flux standard, Console, choisir la bonne classe.
 */
public class Recall06 {

    // Lit des entiers sur un flux d'entree (comme System.in) et rend leur somme.
    static int sum(InputStream in) {
        int total = 0;
        try (Scanner sc = new Scanner(in)) {
            while (sc.hasNextInt()) {
                total += sc.nextInt();
            }
        }
        return total;
    }

    // Compte les voyelles avec un Reader caractere par caractere.
    static int vowels(Reader r) throws IOException {
        int count = 0;
        int c;
        while ((c = r.read()) != -1) {
            if ("aeiouy".indexOf(Character.toLowerCase(c)) >= 0) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("D01 : " + sum(new ByteArrayInputStream("4 8 15 16 23 42".getBytes(StandardCharsets.UTF_8))) + " " + vowels(new StringReader("Entrees Sorties")));

        // Rediriger System.out le temps d'un appel, puis le restaurer.
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        System.out.print("capture");
        System.setOut(original);
        System.out.println("D02 : " + buffer.toString(StandardCharsets.UTF_8));

        // System.console() peut etre null (IDE, sortie redirigee) : TOUJOURS tester.
        Console console = System.console();
        String password = console == null ? "pas de console" : "console presente";
        System.out.println("D03 : " + (password.equals("pas de console") || password.equals("console presente")));

        Path box = Files.createDirectories(Path.of("build/ch14/r06_kata"));
        Path f = Files.writeString(box.resolve("k.txt"), "ligne 1\nligne 2\nligne 3");
        try (var lines = Files.lines(f)) {
            System.out.println("D04 : " + lines.skip(1).findFirst().orElse("") + " " + Files.readString(f).length() + " " + Files.size(f));
        }
    }
}
