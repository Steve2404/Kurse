package ch14_io.drills.r03_iostreams.solution;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * SOLUTION du drill de rappel 3 - les flux d'octets et de caracteres de java.io.
 */
public class Recall03 {

    public static void main(String[] args) throws IOException {
        Path box = Files.createDirectories(Path.of("build/ch14/r03_iostreams"));
        String bin = box.resolve("octets.bin").toString();
        try (OutputStream out = new FileOutputStream(bin)) {
            out.write(65);
            out.write(new byte[] {66, 67, 68});
            out.write(-1);                                         // seuls les 8 bits de poids faible : 255
        }
        StringBuilder read = new StringBuilder();
        try (InputStream in = new BufferedInputStream(new FileInputStream(bin))) {
            int b;
            while ((b = in.read()) != -1) {
                read.append(b).append(' ');
            }
        }
        System.out.println("D01 : " + read.toString().strip() + " | " + Files.size(Path.of(bin)));

        String txt = box.resolve("texte.txt").toString();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(txt))) {
            w.write("un");
            w.newLine();
            w.write("deux");
        }
        try (FileWriter w = new FileWriter(txt, true)) {
            w.write(System.lineSeparator() + "trois");
        }
        int lines = 0;
        String last = "";
        try (BufferedReader r = new BufferedReader(new FileReader(txt))) {
            String line;
            while ((line = r.readLine()) != null) {
                lines++;
                last = line;
            }
        }
        System.out.println("D02 : " + lines + " " + last);

        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            pw.print("a");
            pw.println(1);
            pw.printf(Locale.ROOT, "%5.2f|%-4s|%03d", 3.14159, "ok", 7);
            pw.format(Locale.ROOT, "%n%s", true);
        }
        System.out.println("D03 : " + sw.toString().replace(System.lineSeparator(), "/"));

        byte[] utf8 = "é".getBytes(StandardCharsets.UTF_8);
        byte[] latin = "é".getBytes(StandardCharsets.ISO_8859_1);
        System.out.println("D04 : " + utf8.length + " " + latin.length + " " + new String(utf8, StandardCharsets.UTF_8).equals("é") + " "
                + new String(utf8, StandardCharsets.ISO_8859_1).length());

        StringBuilder marks = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new StringReader("ABCDEFG"))) {
            marks.append((char) r.read());
            r.mark(10);
            marks.append((char) r.read()).append((char) r.read());
            r.reset();
            marks.append((char) r.read());
            marks.append(r.skip(2)).append((char) r.read());
        }
        InputStream plain = new ByteArrayInputStream(new byte[] {1, 2});
        System.out.println("D05 : " + marks + " " + plain.markSupported() + " " + new BufferedInputStream(plain).markSupported());

        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        PrintStream console = new PrintStream(capture, true, StandardCharsets.UTF_8);
        console.print("capture");
        console.checkError();
        System.out.println("D06 : " + capture.toString(StandardCharsets.UTF_8) + " " + (System.out instanceof PrintStream) + " " + (System.err != System.out));
    }
}
