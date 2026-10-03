package ch14_io.projects.p03_rle.solution;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * SOLUTION - compression RLE (run-length encoding) sur des flux d'OCTETS : chaque serie devient (longueur, octet),
 * la longueur etant limitee a 255 (elle tient dans un octet).
 */
public final class Rle {

    private Rle() {
    }

    // read() rend un octet entre 0 et 255, ou -1 a la FIN du flux (d'ou le type int, et non byte).
    public static void compress(String from, String to) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(from));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(to))) {
            int previous = in.read();
            int count = 0;
            while (previous != -1) {
                int b = in.read();
                count++;
                if (b != previous || count == 255) {
                    out.write(count);
                    out.write(previous);
                    count = 0;
                }
                previous = b;
            }
        }                                                   // close() vide aussi le tampon (flush)
    }

    public static void decompress(String from, String to) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(from));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(to))) {
            int count;
            while ((count = in.read()) != -1) {
                int b = in.read();
                for (int i = 0; i < count; i++) {
                    out.write(b);
                }
            }
        }
    }

    // Adler-32 : deux sommes modulo 65521, lues par blocs avec read(byte[]) (qui rend le nombre d'octets lus).
    public static long adler32(String file) throws IOException {
        long a = 1;
        long b = 0;
        byte[] buffer = new byte[4096];
        try (InputStream in = new FileInputStream(file)) {
            int n;
            while ((n = in.read(buffer)) > 0) {
                for (int i = 0; i < n; i++) {
                    a = (a + (buffer[i] & 0xFF)) % 65521;    // & 0xFF : un byte java est signe (-128..127)
                    b = (b + a) % 65521;
                }
            }
        }
        return b << 16 | a;
    }
}
