package ch6_classdesign.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise15_TemplateMethodReports.
 */
public class Solution15_TemplateMethodReports {

    abstract static class Report {
        private final String title;

        protected Report(String title) {
            this.title = title;
        }

        public final String generate(List<Integer> data) {
            // Le plan est fixe (final) ; chaque etape est une methode que les enfants peuvent remplir.
            return header() + "\n" + body(data) + "\n" + footer();
        }

        protected String header() {
            return "== " + title + " ==";
        }

        protected abstract String body(List<Integer> data);

        protected String footer() {
            return "fin";
        }
    }

    static class CsvReport extends Report {
        CsvReport() {
            super("CSV");
        }

        @Override
        protected String body(List<Integer> data) {
            // Le trou abstract : chaque enfant DOIT le remplir.
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(data.get(i));
            }
            return sb.toString();
        }
    }

    static class SummaryReport extends Report {
        SummaryReport(String title) {
            super(title);
        }

        @Override
        protected String body(List<Integer> data) {
            // Un seul parcours pour min, max et total ; le cas vide d'abord.
            if (data.isEmpty()) {
                return "aucune donnee";
            }
            int min = data.get(0);
            int max = data.get(0);
            int total = 0;
            for (int v : data) {
                min = Math.min(min, v);
                max = Math.max(max, v);
                total += v;
            }
            return "min=" + min + " max=" + max + " total=" + total;
        }

        @Override
        protected String footer() {
            // On garde la version du parent et on la complete.
            return super.footer() + " (resume)";
        }
    }

    public static String longestOutput(List<Report> reports, List<Integer> data) {
        // generate() est la meme pour tous ; les etapes redefinies font la difference.
        String best = "";
        for (Report report : reports) {
            String text = report.generate(data);
            if (text.length() > best.length()) {
                best = text;
            }
        }
        return best;
    }
}
