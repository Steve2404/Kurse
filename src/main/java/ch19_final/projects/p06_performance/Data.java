package ch19_final.projects.p06_performance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SplittableRandom;

/**
 * Les donnees FOURNIES du projet 6 (ne pas modifier).
 *
 * LegacyReport fabrique le rapport mensuel de l'atelier a partir du journal des taches (une ligne par
 * evenement : date;personne;colonne;points;titre). Il donne le BON resultat... en plusieurs minutes des que
 * le journal grossit. generate fabrique des journaux realistes (avec des lignes fausses et des doublons).
 */
public final class Data {

    private Data() {
    }

    public static final class LegacyReport {

        private LegacyReport() {
        }

        public static String report(List<String> lines) {
            List<String> seen = new LinkedList<>();
            int invalid = 0;
            int duplicates = 0;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.matches("\\d{4}-\\d{2}-\\d{2};[a-z]+;[^;]+;\\d{1,4};.+")) {
                    invalid++;
                } else if (seen.contains(line)) {
                    duplicates++;
                } else {
                    seen.add(line);
                }
            }
            String out = "lignes : " + lines.size() + " (invalides : " + invalid + ", doublons : " + duplicates + ")\n";
            out = out + "personnes :\n";
            List<String> people = new ArrayList<>();
            for (String line : seen) {
                String who = line.split(";")[1];
                if (!people.contains(who)) {
                    people.add(who);
                }
            }
            people.sort((a, b) -> {
                int pa = points(seen, a);
                int pb = points(seen, b);
                return pa != pb ? pb - pa : a.compareTo(b);
            });
            for (String who : people) {
                out = out + "  " + who + " : " + points(seen, who) + " points, " + count(seen, 1, who) + " taches\n";
            }
            out = out + "colonnes :\n";
            List<String> columns = new ArrayList<>();
            for (String line : seen) {
                String column = line.split(";")[2];
                if (!columns.contains(column)) {
                    columns.add(column);
                }
            }
            columns.sort(null);
            for (String column : columns) {
                out = out + "  " + column + " : " + count(seen, 2, column) + "\n";
            }
            String bestDay = null;
            for (String line : seen) {
                String day = line.split(";")[0];
                if (bestDay == null || count(seen, 0, day) > count(seen, 0, bestDay)
                        || count(seen, 0, day) == count(seen, 0, bestDay) && day.compareTo(bestDay) < 0) {
                    bestDay = day;
                }
            }
            out = out + "jour le plus charge : " + (bestDay == null ? "aucun" : bestDay + " (" + count(seen, 0, bestDay) + " evenements)") + "\n";
            return out;
        }

        private static int points(List<String> seen, String who) {
            int total = 0;
            for (int i = 0; i < seen.size(); i++) {
                String[] fields = seen.get(i).split(";");
                if (fields[1].equals(who)) {
                    total += Integer.parseInt(fields[3]);
                }
            }
            return total;
        }

        private static int count(List<String> seen, int field, String value) {
            int n = 0;
            for (int i = 0; i < seen.size(); i++) {
                if (seen.get(i).split(";")[field].equals(value)) {
                    n++;
                }
            }
            return n;
        }
    }

    private static final String[] PEOPLE = {"ada", "bob", "chloe", "dina", "eli", "farid", "gus", "hana"};
    private static final String[] COLUMNS = {"A faire", "En cours", "Fini", "Bloque"};
    private static final String[] TITLES = {"Pneu", "Freins", "Chaine", "Selle", "Facture; client Dupont", "Cadre"};

    /** Un journal de n lignes, toujours le meme pour la meme graine : environ 2 % de lignes fausses et 3 % de doublons. */
    public static List<String> generate(int n, long seed) {
        SplittableRandom random = new SplittableRandom(seed);
        List<String> lines = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int roll = random.nextInt(100);
            if (roll < 3 && !lines.isEmpty()) {
                lines.add(lines.get(random.nextInt(lines.size())));
            } else if (roll < 5) {
                lines.add(broken(random));
            } else {
                lines.add(String.format("2026-10-%02d;%s;%s;%d;%s %d", 1 + random.nextInt(31), PEOPLE[random.nextInt(PEOPLE.length)],
                        COLUMNS[random.nextInt(COLUMNS.length)], random.nextInt(9), TITLES[random.nextInt(TITLES.length)], i));
            }
        }
        return lines;
    }

    private static String broken(SplittableRandom random) {
        String[] samples = {"2026-10-03;ada;Fini", "2026-1-03;bob;Fini;2;Pneu", "2026-10-03;Bob;Fini;2;Pneu",
                "2026-10-03;bob;Fini;deux;Pneu", "2026-10-03;bob;Fini;2;", "", "2026-10-03;bob;;2;Pneu", "2026-10-03;bob;Fini;12345;Pneu"};
        return samples[random.nextInt(samples.length)];
    }

    public static void main(String[] args) {
        for (int n : new int[]{250, 500, 1000}) {
            List<String> lines = generate(n, 42);
            long start = System.nanoTime();
            String report = LegacyReport.report(lines);
            System.out.println(n + " lignes : " + (System.nanoTime() - start) / 1_000_000 + " ms");
            if (n == 1000) {
                System.out.print(report);
            }
        }
    }
}
