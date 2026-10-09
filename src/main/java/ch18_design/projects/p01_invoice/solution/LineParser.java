package ch18_design.projects.p01_invoice.solution;

/**
 * Lit une ligne de texte : le SEUL endroit qui connait le format "TYPE;...".
 * Il garde exactement les refus du legacy (meme message), y compris la NumberFormatException
 * qui passe telle quelle : pendant un refactoring, on ne change AUCUN comportement, meme discutable.
 */
public final class LineParser {

    private LineParser() {
    }

    public static InvoiceLine parse(String line) {
        String[] fields = line.split(";");
        return switch (fields[0]) {
            case "PART" -> part(line, fields);
            case "LABOR" -> labor(line, fields);
            case "FEE" -> fee(line, fields);
            default -> throw invalid(line);
        };
    }

    private static InvoiceLine part(String line, String[] fields) {
        requireLength(line, fields, 4);
        int quantity = Integer.parseInt(fields[2]);
        long unit = Long.parseLong(fields[3]);
        if (quantity < 1 || unit < 0) {
            throw invalid(line);
        }
        return new Part(fields[1], quantity, Money.ofCents(unit));
    }

    private static InvoiceLine labor(String line, String[] fields) {
        requireLength(line, fields, 3);
        int minutes = Integer.parseInt(fields[2]);
        if (minutes < 1) {
            throw invalid(line);
        }
        return new Labor(fields[1], minutes);
    }

    private static InvoiceLine fee(String line, String[] fields) {
        requireLength(line, fields, 3);
        long amount = Long.parseLong(fields[2]);
        if (amount < 0) {
            throw invalid(line);
        }
        return new Fee(fields[1], Money.ofCents(amount));
    }

    private static void requireLength(String line, String[] fields, int length) {
        if (fields.length != length) {
            throw invalid(line);
        }
    }

    private static IllegalArgumentException invalid(String line) {
        return new IllegalArgumentException("ligne invalide : " + line);
    }
}
