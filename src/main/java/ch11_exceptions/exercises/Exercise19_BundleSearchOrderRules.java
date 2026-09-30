package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * EXERCICE 19 - L'ordre de recherche des ResourceBundle, Locale par defaut comprise : ta regle contre le vrai getBundle (niveau : avance)
 * =======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * L'Exercise18 cherchait une cle dans des fichiers qui existaient tous.
 * Ici, on se demande QUEL fichier Java choisit quand certains manquent,
 * et on ajoute la Locale PAR DEFAUT. La regle de l'examen (demande fr_CA,
 * defaut en_US) :
 *
 *   1. messages_fr_CA   2. messages_fr   3. messages_en_US   4. messages_en   5. messages
 *
 * (d'abord la Locale demandee, de la plus precise a la langue seule ;
 * puis la Locale par defaut, pareil ; la racine "messages" en DERNIER.)
 *
 * Deux temps, et c'est LA que tombent les pieges :
 *   - CHOISIR le bundle : le PREMIER de cette liste qui existe.
 *   - CHERCHER une cle : dans le bundle choisi, puis SEULEMENT dans ses
 *     parents : les versions moins precises de SA locale, puis la
 *     racine. Si Java a choisi messages_en_US, il ne regardera JAMAIS
 *     messages_fr, meme si la cle y est : MissingResourceException.
 *
 * main() interroge le VRAI ResourceBundle.getBundle, avec des bundles
 * en memoire (et une Locale par defaut imposee) : c'est le juge.
 *
 * Les locales s'ecrivent en texte : "fr_CA", "fr", "en_US", "de".
 *
 *
 * ==================================================================
 * TODO 1 : candidates(locale)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "fr_CA" -> [messages_fr_CA, messages_fr] ; "de" -> [messages_de]
 *
 * -- Le plan --
 *
 *   1. Toujours "messages_" + locale.
 *   2. S'il y a un pays ('_'), ajouter "messages_" + langue seule.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : searchOrder(requested, defaultLocale)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ("fr_CA", "en_US") -> [messages_fr_CA, messages_fr, messages_en_US, messages_en, messages]
 *   ("en_GB", "en_US") -> [messages_en_GB, messages_en, messages_en_US, messages]   (sans doublon)
 *   ("fr", "fr")       -> [messages_fr, messages]
 *
 * -- Le plan --
 *
 *   1. candidates(requested), puis candidates(defaultLocale), sans doublon.
 *   2. "messages" a la fin.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : candidates (TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : chooseBundle(available, requested, defaultLocale)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Le premier nom de searchOrder qui est dans available.
 *   2. Aucun -> "MissingResourceException".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : searchOrder.
 *
 *
 * ==================================================================
 * TODO 4 : lookup(key, bundles, requested, defaultLocale)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * bundles : nom du bundle -> ses cles et valeurs. Rendre la valeur de
 * key, ou "MissingResourceException".
 *
 * -- Essayons a la main --
 *
 *   bundles {messages: hello=Hello, messages_fr: hello=Bonjour, merci=Merci, messages_en_US: hello=Hi}
 *   lookup("hello", ..., "fr_CA", "en_US") -> Bonjour (bundle choisi : messages_fr)
 *   lookup("merci", ..., "de", "en_US")    -> MissingResourceException (bundle choisi : messages_en_US ; ses parents : messages_en, messages)
 *
 * -- Le plan --
 *
 *   1. choisi = chooseBundle(...) ; introuvable -> l'exception.
 *   2. Chaine = candidates(locale du bundle choisi) + "messages".
 *   3. La premiere de la chaine qui EXISTE et contient key -> sa valeur. Sinon l'exception.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : chooseBundle et candidates. La locale d'un nom de bundle :
 * ce qui suit "messages_" (vide pour la racine).
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - locale.indexOf('_') ; locale.substring(0, i).
 *   - new ArrayList<>(new LinkedHashSet<>(liste)) enleve les doublons en gardant l'ordre.
 */
public class Exercise19_BundleSearchOrderRules {

    public static List<String> candidates(String locale) {
        throw new UnsupportedOperationException("TODO 1 : implementer candidates()");
    }

    public static List<String> searchOrder(String requested, String defaultLocale) {
        throw new UnsupportedOperationException("TODO 2 : implementer searchOrder()");
    }

    public static String chooseBundle(List<String> available, String requested, String defaultLocale) {
        throw new UnsupportedOperationException("TODO 3 : implementer chooseBundle()");
    }

    public static String lookup(String key, Map<String, Map<String, String>> bundles, String requested, String defaultLocale) {
        throw new UnsupportedOperationException("TODO 4 : implementer lookup()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("candidates(fr_CA) / (de)",
                candidates("fr_CA").equals(List.of("messages_fr_CA", "messages_fr")) && candidates("de").equals(List.of("messages_de")));
        ExerciseChecker.check("searchOrder (fr_CA, en_US) / (en_GB, en_US) / (fr, fr)",
                searchOrder("fr_CA", "en_US").equals(List.of("messages_fr_CA", "messages_fr", "messages_en_US", "messages_en", "messages"))
                        && searchOrder("en_GB", "en_US").equals(List.of("messages_en_GB", "messages_en", "messages_en_US", "messages"))
                        && searchOrder("fr", "fr").equals(List.of("messages_fr", "messages")));

        List<Map<String, Map<String, String>>> worlds = List.of(
                Map.of("messages", Map.of("hello", "Hello", "bye", "Bye"),
                        "messages_fr", Map.of("hello", "Bonjour", "merci", "Merci"),
                        "messages_en_US", Map.of("hello", "Hi")),
                Map.of("messages", Map.of("hello", "Hello"),
                        "messages_fr_CA", Map.of("hello", "Allo"),
                        "messages_en", Map.of("bye", "Bye bye")),
                Map.of("messages_fr", Map.of("hello", "Bonjour"),
                        "messages_en_US", Map.of("hello", "Hi", "bye", "See you")),
                Map.of("messages", Map.of("hello", "Hello", "merci", "Thanks")));
        String[][] asks = {{"fr_CA", "en_US"}, {"fr", "en_US"}, {"de", "en_US"}, {"en_GB", "en_US"}, {"fr_CA", "fr_CA"}, {"de_DE", "fr_FR"}};
        List<String> keys = List.of("hello", "bye", "merci");
        int agreeChoose = 0;
        int agreeLookup = 0;
        int total = 0;
        String firstMiss = "";
        for (Map<String, Map<String, String>> world : worlds) {
            for (String[] ask : asks) {
                total++;
                if (chooseBundle(List.copyOf(world.keySet()), ask[0], ask[1]).equals(jvmChoose(world, ask[0], ask[1]))) {
                    agreeChoose++;
                }
                for (String key : keys) {
                    String expected = jvmLookup(world, key, ask[0], ask[1]);
                    String mine = lookup(key, world, ask[0], ask[1]);
                    if (mine.equals(expected)) {
                        agreeLookup++;
                    } else if (firstMiss.isEmpty()) {
                        firstMiss = " ; 1er ecart : " + key + " " + ask[0] + "/" + ask[1] + " dans " + world.keySet()
                                + " -> " + mine + " au lieu de " + expected;
                    }
                }
            }
        }
        ExerciseChecker.check("chooseBundle == getBundle sur " + total + " cas (" + agreeChoose + " d'accord)", agreeChoose == total);
        ExerciseChecker.check("lookup == getBundle sur " + total * 3 + " cas (" + agreeLookup + " d'accord)" + firstMiss,
                agreeLookup == total * 3);

        ExerciseChecker.summary();
    }

    // ---- Le juge : le vrai ResourceBundle.getBundle (ne pas modifier) ----

    static String jvmChoose(Map<String, Map<String, String>> world, String requested, String defaultLocale) {
        try {
            ResourceBundle b = ResourceBundle.getBundle("messages", toLocale(requested), new MapControl(world, toLocale(defaultLocale)));
            String tag = b.getLocale().toString();
            return tag.isEmpty() ? "messages" : "messages_" + tag;
        } catch (MissingResourceException e) {
            return "MissingResourceException";
        }
    }

    static String jvmLookup(Map<String, Map<String, String>> world, String key, String requested, String defaultLocale) {
        try {
            return ResourceBundle.getBundle("messages", toLocale(requested), new MapControl(world, toLocale(defaultLocale))).getString(key);
        } catch (MissingResourceException e) {
            return "MissingResourceException";
        }
    }

    static Locale toLocale(String text) {
        return Locale.forLanguageTag(text.replace('_', '-'));
    }

    static class MapControl extends ResourceBundle.Control {
        private final Map<String, Map<String, String>> world;
        private final Locale defaultLocale;

        MapControl(Map<String, Map<String, String>> world, Locale defaultLocale) {
            this.world = world;
            this.defaultLocale = defaultLocale;
        }

        @Override
        public List<String> getFormats(String baseName) {
            return FORMAT_CLASS;
        }

        @Override
        public Locale getFallbackLocale(String baseName, Locale locale) {
            return locale.equals(defaultLocale) ? null : defaultLocale;
        }

        @Override
        public long getTimeToLive(String baseName, Locale locale) {
            return TTL_DONT_CACHE;
        }

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) {
            Map<String, String> content = world.get(toBundleName(baseName, locale));
            if (content == null) {
                return null;
            }
            return new ResourceBundle() {
                @Override
                protected Object handleGetObject(String key) {
                    return content.get(key);
                }

                @Override
                public Enumeration<String> getKeys() {
                    return Collections.enumeration(content.keySet());
                }

                @Override
                public Locale getLocale() {
                    return locale;
                }
            };
        }
    }
}
