package ch11_exceptions.drills;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Les donnees partagees par TOUS les drills du chapitre 11.
 * ========================================================
 *
 * Toujours le meme petit registre : ton cerveau se concentre sur les
 * exceptions, les formats et les Locale, pas sur les donnees.
 *
 *   RAW    : ["12", "x", "-3", "", "7"]     (3 nombres valides, somme 16 ; "x" et "" sont invalides)
 *   WHEN   : 2024-03-07T14:05:09            (un jeudi, 14 h 05 min 09 s)
 *   BUNDLE : "ch11_exceptions.messages"      (les fichiers de src/main/resources/ch11_exceptions :
 *            racine {greeting=Hello, farewell=Goodbye, onlyDefault=OnlyInDefault, invalidRecord=Invalid record: {0}},
 *            fr {greeting=Bonjour}, fr_CA {greeting=Bonjour le Canada})
 *
 * Attention : la Locale par defaut de la machine peut etre n'importe
 * laquelle (de_DE ici). Les drills donnent TOUJOURS une Locale explicite.
 */
public final class Ledger {

    public static final List<String> RAW = List.of("12", "x", "-3", "", "7");

    public static final LocalDateTime WHEN = LocalDateTime.of(2024, 3, 7, 14, 5, 9);

    public static final String BUNDLE = "ch11_exceptions.messages";

    private Ledger() {
    }
}
