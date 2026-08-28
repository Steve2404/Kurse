package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

import java.util.Locale;

/**
 * EXERCICE 10 - L'ordre de recherche d'un ResourceBundle (niveau : difficile, style examen OCP)
 * ============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un ResourceBundle, c'est un gros classeur de traductions, RANGE en
 * plusieurs petits fichiers .properties, du PLUS precis au PLUS
 * general (exactement comme des poupees russes a nouveau) :
 *
 *   messages_fr_CA.properties  (francais du CANADA - le plus precis)
 *   messages_fr.properties     (francais en general)
 *   messages.properties        (le fichier "racine", LE PLUS general -
 *                                sert de filet de securite pour TOUTES
 *                                les locales)
 *
 * Regarde les 3 fichiers deja crees a cote de cet exercice, dans
 * src/main/resources/exceptions/ - tu verras que
 * messages_fr.properties et messages_fr_CA.properties ne contiennent
 * QUE la cle "greeting", alors que messages.properties (la racine)
 * contient TOUTES les cles (greeting, farewell, onlyDefault).
 *
 * Quand on demande une Locale precise (par exemple fr_CA), Java
 * cherche d'abord le fichier LE PLUS proche de cette Locale
 * (messages_fr_CA), et s'il n'existe pas, il remonte vers des
 * versions DE PLUS EN PLUS generales (messages_fr, puis messages)
 * JUSQU'A en trouver un qui existe.
 *
 * IMPORTANT : une fois qu'un fichier bundle est TROUVE (par exemple
 * messages_fr_CA), et qu'une CLE PRECISE n'y est pas presente (par
 * exemple "farewell"), Java ne cherche PAS dans un fichier totalement
 * different - il continue SEULEMENT a remonter DANS LA MEME
 * HIERARCHIE (fr_CA -> fr -> racine), jamais ailleurs.
 *
 *
 * ==================================================================
 * TODO : lookup(key, locale)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Charger le bundle avec ResourceBundle.getBundle("ch11_exceptions.messages", locale).
 *   2. Renvoyer bundle.getString(key).
 *
 * -- Essayons a la main --
 *
 * lookup("greeting", Locale fr_CA) -> "Bonjour le Canada" (trouve
 * directement dans messages_fr_CA.properties, LE fichier le plus
 * precis qui existe pour cette Locale).
 *
 * lookup("farewell", Locale fr_CA) -> "Goodbye" (messages_fr_CA n'a
 * PAS cette cle, ni messages_fr : Java continue de remonter DANS LA
 * MEME HIERARCHIE jusqu'a la racine messages.properties, qui l'a).
 *
 * lookup("greeting", Locale allemande) -> "Hello" (aucun fichier
 * messages_de* n'existe du tout : Java tombe directement sur la
 * racine messages.properties, LE filet de securite final).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : ResourceBundle.getBundle() fait deja TOUT le travail de
 * recherche pour toi - le vrai exercice est de bien COMPRENDRE et
 * PREVOIR ce qu'il va trouver, avant de lancer le code.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Le nom de base "ch11_exceptions.messages" correspond au chemin
 *     exceptions/messages*.properties sur le classpath (le point
 *     devient un separateur de dossier).
 *   - bundle.getString(key) lance une MissingResourceException si la
 *     cle n'existe VRAIMENT nulle part dans toute la hierarchie.
 */
public class Exercise10_ResourceBundleSearchOrder {

    public static String lookup(String key, Locale locale) {
        throw new UnsupportedOperationException("TODO : implementer lookup()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("greeting en fr_CA -> le fichier le plus precis (Canada)",
                lookup("greeting", Locale.CANADA_FRENCH).equals("Bonjour le Canada"));

        ExerciseChecker.check("farewell en fr_CA -> absent de fr_CA et fr, trouve dans la racine",
                lookup("farewell", Locale.CANADA_FRENCH).equals("Goodbye"));

        ExerciseChecker.check("greeting en fr -> trouve directement dans messages_fr",
                lookup("greeting", Locale.FRENCH).equals("Bonjour"));

        ExerciseChecker.check("greeting en allemand -> aucun fichier messages_de*, direct a la racine",
                lookup("greeting", Locale.GERMANY).equals("Hello"));

        ExerciseChecker.check("onlyDefault en fr -> absent de fr, trouve dans la racine",
                lookup("onlyDefault", Locale.FRENCH).equals("OnlyInDefault"));

        ExerciseChecker.summary();
    }
}
