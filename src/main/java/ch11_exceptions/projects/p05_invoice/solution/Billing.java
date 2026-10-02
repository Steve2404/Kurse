package ch11_exceptions.projects.p05_invoice.solution;

import ch11_exceptions.projects.p05_invoice.Data;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * SOLUTION du projet 5 - la facturation : NumberFormat, DecimalFormat, formats compacts, parse.
 */
public class Billing {

    // Les espaces insecables (U+00A0) et fines insecables (U+202F) des locales deviennent '_' pour etre VISIBLES.
    static String visible(String text) {
        return text.replace(' ', '_').replace(' ', '_');
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);                     // la machine peut etre en de_DE : on fixe la locale par defaut

        List<InvoiceLine> lines = new ArrayList<>();
        for (String text : Data.LINES) {
            lines.add(InvoiceLine.parse(text));
        }
        Invoice invoice = new Invoice(lines);
        double total = invoice.total() / 100.0;
        StringBuilder totals = new StringBuilder();
        for (String tag : Data.LOCALES) {
            Locale locale = Locale.forLanguageTag(tag);
            // getCurrencyInstance prend la MONNAIE de la locale : le meme nombre devient $, EUR, CHF, JPY... sans conversion !
            totals.append(' ').append(tag).append('=').append(NumberFormat.getCurrencyInstance(locale).format(total));
        }
        System.out.println("facture : " + invoice.size() + " lignes, total TTC" + visible(totals.toString()));

        NumberFormat percent = NumberFormat.getPercentInstance(Locale.FRANCE);
        NumberFormat precise = NumberFormat.getPercentInstance(Locale.FRANCE);
        precise.setMinimumFractionDigits(1);
        NumberFormat euros = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        StringBuilder vat = new StringBuilder();
        for (Map.Entry<Integer, Long> e : invoice.basesByRate().entrySet()) {
            double rate = e.getKey() / 1000.0;
            vat.append(" | ").append(percent.format(rate)).append(" ou ").append(precise.format(rate)).append(" de ").append(euros.format(e.getValue() / 100.0))
                    .append(" = ").append(euros.format(Invoice.vat(e.getValue(), e.getKey()) / 100.0));
        }
        System.out.println(visible("TVA :" + vat));

        // getIntegerInstance arrondit au PAIR le plus proche (HALF_EVEN) ; setRoundingMode change la regle.
        NumberFormat integer = NumberFormat.getIntegerInstance(Locale.US);
        NumberFormat halfUp = NumberFormat.getIntegerInstance(Locale.US);
        halfUp.setRoundingMode(RoundingMode.HALF_UP);
        NumberFormat two = NumberFormat.getNumberInstance(Locale.US);
        two.setMaximumFractionDigits(2);
        System.out.println("arrondis : " + integer.format(2.5) + " " + integer.format(3.5) + " " + halfUp.format(2.5) + " | " + two.format(1.005) + " "
                + two.format(2.675) + " " + NumberFormat.getInstance(Locale.US).format(1234.56789) + " " + String.format(Locale.GERMANY, "%,.2f", 1234.5));

        DecimalFormatSymbols us = DecimalFormatSymbols.getInstance(Locale.US);
        for (String pattern : Data.PATTERNS) {
            DecimalFormat format = new DecimalFormat(pattern, us);
            StringBuilder row = new StringBuilder();
            for (double v : Data.SAMPLES) {
                row.append(" [").append(format.format(v)).append(']');
            }
            System.out.println("motif " + pattern + " :" + row);
        }

        NumberFormat shortUs = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        NumberFormat longUs = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);
        NumberFormat shortPrecise = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        shortPrecise.setMaximumFractionDigits(1);
        NumberFormat longDe = NumberFormat.getCompactNumberInstance(Locale.GERMANY, NumberFormat.Style.LONG);
        for (long n : Data.BIG) {
            System.out.println(visible("compact " + n + " : " + shortUs.format(n) + " | " + shortPrecise.format(n) + " | " + longUs.format(n) + " | " + longDe.format(n)));
        }

        // parse lit le plus long DEBUT valide ; ParseException (verifiee) si rien n'est lisible.
        for (String input : Data.INPUTS) {
            String[] p = input.split("\\|");
            Locale locale = Locale.forLanguageTag(p[0]);
            NumberFormat parser = p[1].equals("monnaie") ? NumberFormat.getCurrencyInstance(locale) : NumberFormat.getNumberInstance(locale);
            try {
                System.out.println("lu " + p[0] + " " + p[1] + " \"" + p[2] + "\" -> " + parser.parse(p[2]));
            } catch (ParseException e) {
                System.out.println("lu " + p[0] + " " + p[1] + " \"" + p[2] + "\" -> ParseException " + e.getMessage() + " (position " + e.getErrorOffset() + ")");
            }
        }

        long[] shares = Money.split(invoice.total(), Data.WEIGHTS);
        StringBuilder split = new StringBuilder();
        for (long s : shares) {
            split.append(' ').append(euros.format(s / 100.0));
        }
        System.out.println(visible("partage de " + euros.format(invoice.total() / 100.0) + " en " + Arrays.toString(Data.WEIGHTS) + " :" + split + " (somme "
                + euros.format(Arrays.stream(shares).sum() / 100.0) + ")"));

        List<long[]> rows = Money.schedule(Data.LOAN, Data.LOAN_RATE, Data.MONTHS);
        long interests = rows.stream().mapToLong(r -> r[2]).sum();
        for (long[] r : List.of(rows.get(0), rows.get(1), rows.get(rows.size() - 1))) {
            System.out.println(String.format(Locale.FRANCE, "mois %2d : mensualite %,10.2f, interets %,7.2f, capital %,10.2f, reste %,10.2f", r[0], r[1] / 100.0,
                    r[2] / 100.0, r[3] / 100.0, r[4] / 100.0).replace(' ', '_').replace(' ', '_'));
        }
        System.out.println(visible("cout du credit : " + euros.format(interests / 100.0)));
    }
}
