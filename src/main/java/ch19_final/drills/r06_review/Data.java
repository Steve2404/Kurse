package ch19_final.drills.r06_review;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * FOURNI (ne pas modifier) : le code d'un collegue, a relire. Chaque methode "marche en demo" et cache un
 * defaut classique. Ta version corrigee s'appelle Fixes (voir TODO.md) ; ne copie pas : reecris.
 */
public final class Data {

    private Data() {
    }

    public static final class Before {

        private Before() {
        }

        public static double total(List<String> amounts) {
            double total = 0;
            for (String a : amounts) {
                total += Double.parseDouble(a);
            }
            return total;
        }

        public static boolean isPromo(String code) {
            return code == "DOUBLE";
        }

        public static String tier(int points) {
            return points > 1000 ? "GOLD" : points > 300 ? "SILVER" : "BRONZE";
        }

        public static LocalDate lastValidDay(LocalDate earned) {
            return earned.plusDays(364);
        }

        public static List<String> distinctInOrder(List<String> items) {
            List<String> result = new ArrayList<>();
            for (String item : items) {
                if (!result.contains(item)) {
                    result.add(item);
                }
            }
            return result;
        }

        public static String join(List<String> parts) {
            String out = "";
            for (int i = 0; i < parts.size(); i++) {
                out = out + (i == 0 ? "" : ", ") + parts.get(i);
            }
            return out;
        }

        public static int countLines(Reader in) {
            int n = 0;
            try {
                BufferedReader reader = new BufferedReader(in);
                while (reader.readLine() != null) {
                    n++;
                }
            } catch (IOException e) {
                return -1;
            }
            return n;
        }

        public static final class Counter {
            private int value;

            public void increment() {
                value++;
            }

            public int value() {
                return value;
            }
        }
    }
}
