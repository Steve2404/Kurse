package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 15 - Le patron "methode template" : une classe abstraite qui fixe le plan, des enfants qui remplissent les trous (niveau : avance)
 * ========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tous les rapports ont la meme forme : un en-tete, un corps, un pied
 * de page. La classe abstraite Report ecrit CE PLAN une fois pour
 * toutes, dans generate(), marquee final (personne ne peut changer
 * l'ordre). Le corps est abstract : chaque enfant DOIT l'ecrire. L'en-
 * tete et le pied ont une version par defaut que l'enfant PEUT changer.
 *
 * Une classe abstraite peut avoir un constructeur : l'enfant l'appelle
 * avec super(titre), puisqu'on ne peut jamais faire "new Report(...)".
 *
 *
 * ==================================================================
 * TODO 1 : Report.generate(data)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   new CsvReport().generate(List.of(3, 1, 2)) -> "== CSV ==\n3,1,2\nfin"
 *
 * -- Le plan --
 *
 *   1. Rendre header() + "\n" + body(data) + "\n" + footer().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : header, body et footer SONT les boites, remplies par les enfants.
 *
 *
 * ==================================================================
 * TODO 2 : CsvReport.body(data)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Les valeurs separees par des virgules ("" si la liste est vide).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : SummaryReport.body(data)    et    TODO 4 : SummaryReport.footer()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   new SummaryReport("Ventes").generate(List.of(4, 9, 2))
 *     -> "== Ventes ==\nmin=2 max=9 total=15\nfin (resume)"
 *   liste vide -> corps "aucune donnee"
 *
 * -- Le plan --
 *
 *   1. body : liste vide -> "aucune donnee" ; sinon calculer min, max, total.
 *   2. footer : rendre super.footer() + " (resume)" (on garde le pied du parent et on complete).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : longestOutput(reports, data)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une liste de Report (des CsvReport, des SummaryReport...). On genere
 * chacun et on rend le texte le plus long. On ne sait pas qui est qui :
 * generate() appelle les bonnes versions tout seul.
 *
 * -- Le plan --
 *
 *   1. Pour chaque report : text = report.generate(data) ; garder le plus long (le premier en cas d'egalite).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Un StringBuilder pour les virgules, ou String.join(",", ...) apres conversion en String.
 */
public class Exercise15_TemplateMethodReports {

    abstract static class Report {
        private final String title;

        protected Report(String title) {
            this.title = title;
        }

        public final String generate(List<Integer> data) {
            throw new UnsupportedOperationException("TODO 1 : implementer generate()");
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
            throw new UnsupportedOperationException("TODO 2 : implementer CsvReport.body()");
        }
    }

    static class SummaryReport extends Report {
        SummaryReport(String title) {
            super(title);
        }

        @Override
        protected String body(List<Integer> data) {
            throw new UnsupportedOperationException("TODO 3 : implementer SummaryReport.body()");
        }

        @Override
        protected String footer() {
            throw new UnsupportedOperationException("TODO 4 : implementer SummaryReport.footer()");
        }
    }

    public static String longestOutput(List<Report> reports, List<Integer> data) {
        throw new UnsupportedOperationException("TODO 5 : implementer longestOutput()");
    }

    public static void main(String[] args) {
        Report csv = new CsvReport();
        Report summary = new SummaryReport("Ventes");
        ExerciseChecker.check("generate (CSV) : en-tete, corps, pied", csv.generate(List.of(3, 1, 2)).equals("== CSV ==\n3,1,2\nfin"));
        ExerciseChecker.check("CsvReport : liste vide -> corps vide", csv.generate(List.of()).equals("== CSV ==\n\nfin"));
        ExerciseChecker.check("SummaryReport : min, max, total, et footer via super",
                summary.generate(List.of(4, 9, 2)).equals("== Ventes ==\nmin=2 max=9 total=15\nfin (resume)"));
        ExerciseChecker.check("SummaryReport : aucune donnee", summary.generate(List.of()).equals("== Ventes ==\naucune donnee\nfin (resume)"));
        ExerciseChecker.check("longestOutput : le resume est le plus long",
                longestOutput(List.of(csv, summary), List.of(4, 9, 2)).equals(summary.generate(List.of(4, 9, 2))));

        ExerciseChecker.summary();
    }
}
