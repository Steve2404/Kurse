package ch14_io.projects.p03_rle.solution;

import ch14_io.projects.p03_rle.Data;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Stream;
import java.util.zip.Adler32;

/**
 * SOLUTION du projet 3 - les flux java.io : octets, caracteres, tampons, jeux de caracteres, donnees typees.
 */
public class StreamLab {

    public static void main(String[] args) throws IOException {
        Path sandbox = Path.of(Data.SANDBOX);
        if (Files.exists(sandbox)) {
            try (Stream<Path> all = Files.walk(sandbox)) {
                for (Path p : all.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
        // java.io.File : l'ancienne API ; mkdirs cree aussi les parents.
        File dir = new File(Data.SANDBOX);
        System.out.println("dossier cree " + dir.mkdirs() + ", existe " + dir.exists() + ", isDirectory " + dir.isDirectory() + ", nom " + dir.getName());

        String raw = Data.SANDBOX + "/brut.bin";
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(raw))) {
            for (int i = 0; i < Data.RUNS; i++) {
                for (int k = 0; k < Data.length(i); k++) {
                    out.write(Data.value(i));
                }
            }
        }
        String packed = Data.SANDBOX + "/compresse.rle";
        String back = Data.SANDBOX + "/restaure.bin";
        Rle.compress(raw, packed);
        Rle.decompress(packed, back);
        long rawSize = new File(raw).length();
        long packedSize = new File(packed).length();
        System.out.println("RLE : " + rawSize + " -> " + packedSize + " octets (" + packedSize * 100 / rawSize + " %), restaure identique "
                + (Files.mismatch(Path.of(raw), Path.of(back)) == -1));
        Adler32 reference = new Adler32();
        reference.update(Files.readAllBytes(Path.of(raw)));
        System.out.println("Adler-32 maison " + Long.toHexString(Rle.adler32(raw)) + ", JDK " + Long.toHexString(reference.getValue()) + ", identiques "
                + (Rle.adler32(raw) == reference.getValue()));

        // Caracteres : Writer et Reader. BufferedWriter.newLine, PrintWriter.printf, FileWriter en mode AJOUT.
        String report = Data.SANDBOX + "/rapport.txt";
        try (BufferedWriter w = new BufferedWriter(new FileWriter(report))) {
            w.write("article;quantite;prix");
            w.newLine();
            for (String item : Data.ITEMS) {
                w.write(item);
                w.newLine();
            }
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(report, true))) {      // true : on AJOUTE a la fin
            double total = 0;
            for (String item : Data.ITEMS) {
                String[] p = item.split(";");
                total += Integer.parseInt(p[1]) * Double.parseDouble(p[2]);
            }
            pw.printf(Locale.ROOT, "total;;%.2f%n", total);
        }
        int lines = 0;
        String last = null;
        try (BufferedReader r = new BufferedReader(new FileReader(report))) {
            String line;
            while ((line = r.readLine()) != null) {                 // null a la fin (et pas -1)
                lines++;
                last = line;
            }
        }
        System.out.println("rapport : " + lines + " lignes, derniere \"" + last + "\"");

        // Le meme texte, deux encodages : le nombre d'octets change ; relu avec le mauvais, il est abime.
        String utf8 = Data.SANDBOX + "/utf8.txt";
        String latin1 = Data.SANDBOX + "/latin1.txt";
        try (Writer w = new OutputStreamWriter(new FileOutputStream(utf8), StandardCharsets.UTF_8)) {
            w.write(Data.ACCENTS);
        }
        try (Writer w = new OutputStreamWriter(new FileOutputStream(latin1), StandardCharsets.ISO_8859_1)) {
            w.write(Data.ACCENTS);
        }
        StringBuilder wrong = new StringBuilder();
        try (Reader r = new InputStreamReader(new FileInputStream(utf8), StandardCharsets.ISO_8859_1)) {
            int c;
            while ((c = r.read()) != -1) {
                wrong.append((char) c);
            }
        }
        System.out.println("encodages : " + Data.ACCENTS.length() + " caracteres, UTF-8 " + new File(utf8).length() + " octets, ISO-8859-1 " + new File(latin1).length()
                + " octets ; UTF-8 relu en ISO-8859-1 : " + wrong.length() + " caracteres, identique " + wrong.toString().equals(Data.ACCENTS));

        // mark / reset / skip : relire une partie du flux (si markSupported).
        StringBuilder trace = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new FileReader(report))) {
            trace.append((char) r.read()).append((char) r.read());
            r.mark(100);
            trace.append((char) r.read());
            r.reset();
            trace.append((char) r.read());
            long skipped = r.skip(5);
            trace.append('|').append(skipped).append('|').append((char) r.read());
            System.out.println("mark/reset : markSupported " + r.markSupported() + ", lecture " + trace);
        }

        // Donnees TYPEES : on relit dans le meme ordre ; lire au-dela de la fin leve EOFException.
        String typed = Data.SANDBOX + "/donnees.bin";
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(typed))) {
            out.writeInt(2026);
            out.writeDouble(3.5);
            out.writeUTF("fin");
        }
        try (DataInputStream in = new DataInputStream(new FileInputStream(typed))) {
            String values = in.readInt() + " " + in.readDouble() + " " + in.readUTF();
            String end;
            try {
                in.readInt();
                end = "encore";
            } catch (EOFException e) {
                end = e.getClass().getSimpleName();
            }
            System.out.println("DataStream : " + values + ", " + new File(typed).length() + " octets, puis " + end);
        }
    }
}
