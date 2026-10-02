package ch11_exceptions.drills.r10_kata.solution;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

/**
 * SOLUTION du drill de rappel 10 - kata : MessageFormat, Properties, et un parse sans exception.
 */
public class Recall10 {

    static Optional<Integer> safeParse(String text) {
        try {
            return Optional.of(Integer.parseInt(text.strip()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        // {indice} : les arguments peuvent etre reutilises et dans n'importe quel ordre.
        System.out.println("D01 : " + MessageFormat.format("{1} a {0} ans, {1} !", 7, "Ana") + " | " + MessageFormat.format("{0}{0}", "ha"));
        // Apostrophe : '' pour en ecrire une ; '...' pour du texte litteral (y compris des accolades).
        System.out.println("D02 : " + MessageFormat.format("l''an {0}", 2026) + " | " + MessageFormat.format("l'an {0}", 2026) + " | "
                + MessageFormat.format("'{0}' = {0}", "x"));
        System.out.println("D03 : " + MessageFormat.format("{0,number,#.#} | {0,number,integer} | {1,number,percent}", 3.14159, 0.25) + " | "
                + new MessageFormat("{0,number}", Locale.GERMANY).format(new Object[] {1234.5}) + " | " + MessageFormat.format("{0}", 1234567));
        Properties defaults = new Properties();
        defaults.setProperty("color", "bleu");
        defaults.setProperty("size", "M");
        Properties props = new Properties(defaults);            // getProperty retombe sur les valeurs par defaut...
        props.setProperty("color", "rouge");
        System.out.println("D04 : " + props.getProperty("color") + " " + props.getProperty("size") + " " + props.get("size") + " " + props.getProperty("font", "Arial")
                + " " + props.size() + " " + props.stringPropertyNames().stream().sorted().toList());
        props.put("count", 3);                                   // ...mais get, size et put ignorent les defauts
        System.out.println("D05 : " + props.getProperty("count") + " " + props.get("count") + " " + props.containsKey("count"));
        System.out.println("D06 : " + safeParse(" 42 ") + " " + safeParse("4x2") + " " + safeParse("12").map(n -> n * 2).orElse(-1));
    }
}
