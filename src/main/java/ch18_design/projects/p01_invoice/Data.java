package ch18_design.projects.p01_invoice;

import java.util.List;

/**
 * FOURNI (ne pas modifier) : le programme de factures du garage, ecrit il y a dix ans.
 * Il marche : les clients paient ces montants depuis des annees. Sa sortie EST la specification.
 * Mais personne n'ose plus le toucher. Ton travail : le refactorer sans changer un seul caractere
 * de ce qu'il produit (etape 1 a 6), puis lui ajouter une fonctionnalite (etape 7).
 */
public final class Data {

    /** Les lignes de la facture d'exemple. */
    public static final List<String> SAMPLE = List.of(
            "PART;Plaquettes de frein;2;4500",
            "PART;Disque;2;6250",
            "LABOR;Montage freins;100",
            "LABOR;Diagnostic;10");

    private Data() {
    }

    public static void main(String[] args) {
        System.out.println(LegacyGarage.invoice("Dupont", true, SAMPLE));
    }

    /** Le code d'origine. Lis-le en entier avant l'etape 1. */
    public static final class LegacyGarage {

        private LegacyGarage() {
        }

        public static String invoice(String c, boolean f, List<String> l) {
            String s = "FACTURE - Client : " + c + "\n";
            long t = 0;
            long t2 = 0;
            int m = 0;
            for (String x : l) {
                String[] p = x.split(";");
                if (p[0].equals("PART")) {
                    if (p.length != 4) {
                        throw new IllegalArgumentException("ligne invalide : " + x);
                    }
                    int q = Integer.parseInt(p[2]);
                    long u = Long.parseLong(p[3]);
                    if (q < 1 || u < 0) {
                        throw new IllegalArgumentException("ligne invalide : " + x);
                    }
                    t = t + q * u;
                    long a = q * u;
                    s = s + "  " + p[1] + " x" + q + " : " + a / 100 + "," + (a % 100 < 10 ? "0" : "") + a % 100 + "\n";
                } else if (p[0].equals("LABOR")) {
                    if (p.length != 3) {
                        throw new IllegalArgumentException("ligne invalide : " + x);
                    }
                    int mn = Integer.parseInt(p[2]);
                    if (mn < 1) {
                        throw new IllegalArgumentException("ligne invalide : " + x);
                    }
                    int qt = (mn + 14) / 15;
                    m = m + qt * 15;
                    long a = qt * 1800L;
                    t2 = t2 + a;
                    s = s + "  " + p[1] + " (" + qt * 15 / 60 + " h " + (qt * 15 % 60 < 10 ? "0" : "") + qt * 15 % 60 + ") : "
                            + a / 100 + "," + (a % 100 < 10 ? "0" : "") + a % 100 + "\n";
                } else {
                    throw new IllegalArgumentException("ligne invalide : " + x);
                }
            }
            s = s + "Pieces : " + t / 100 + "," + (t % 100 < 10 ? "0" : "") + t % 100 + "\n";
            s = s + "Main-d'oeuvre : " + t2 / 100 + "," + (t2 % 100 < 10 ? "0" : "") + t2 % 100 + "\n";
            long r = 0;
            if (f && t >= 20000) {
                r = (t * 10 + 50) / 100;
                s = s + "Remise fidelite : -" + r / 100 + "," + (r % 100 < 10 ? "0" : "") + r % 100 + "\n";
            }
            long r2 = 0;
            if (m > 240) {
                r2 = (t2 * 5 + 50) / 100;
                s = s + "Remise main-d'oeuvre : -" + r2 / 100 + "," + (r2 % 100 < 10 ? "0" : "") + r2 % 100 + "\n";
            }
            long n = t + t2 - r - r2;
            s = s + "Sous-total HT : " + n / 100 + "," + (n % 100 < 10 ? "0" : "") + n % 100 + "\n";
            long v = (n * 20 + 50) / 100;
            s = s + "TVA 20 % : " + v / 100 + "," + (v % 100 < 10 ? "0" : "") + v % 100 + "\n";
            s = s + "Total TTC : " + (n + v) / 100 + "," + ((n + v) % 100 < 10 ? "0" : "") + (n + v) % 100 + "\n";
            return s;
        }
    }
}
