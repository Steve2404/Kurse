package ch6_classdesign.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise12_PolymorphismInPractice.
 */
public class Solution12_PolymorphismInPractice {

    static class Notifier {
        String label = "notifier";

        static String kind() {
            return "notifier";
        }

        String channel() {
            return "base";
        }

        String format(String message) {
            return "[" + channel() + "] " + message;
        }

        String send(String message) {
            return format(message);
        }
    }

    static class EmailNotifier extends Notifier {
        String label = "email";

        static String kind() {
            return "email";
        }

        @Override
        String channel() {
            // Redefinition : send() et format() du parent appelleront CETTE version.
            return "email";
        }
    }

    static class SmsNotifier extends Notifier {
        int maxLength() {
            return 20;
        }

        @Override
        String channel() {
            return "sms";
        }

        @Override
        String format(String message) {
            // On adapte le message, puis on reutilise la mise en forme du parent avec super.
            String cut = message.substring(0, Math.min(message.length(), maxLength()));
            return super.format(cut);
        }
    }

    public static List<String> broadcast(List<Notifier> notifiers, String message) {
        // Le meme appel send() : chaque objet repond avec ses methodes redefinies.
        List<String> results = new ArrayList<>();
        for (Notifier n : notifiers) {
            results.add(n.send(message));
        }
        return results;
    }

    public static SmsNotifier asSmsOrNull(Notifier notifier) {
        // Le pattern matching verifie le type ET cast en une fois : jamais de ClassCastException.
        if (notifier instanceof SmsNotifier sms) {
            return sms;
        }
        return null;
    }

    public static String labelsThroughBothTypes() {
        // Les champs ne sont PAS polymorphes : le type de la variable decide.
        Notifier ref = new EmailNotifier();
        return ref.label + "/" + ((EmailNotifier) ref).label;
    }

    @SuppressWarnings("static")
    public static String kindThroughParentType() {
        // Une methode static cachee est choisie par le type de la variable (Notifier), pas par l'objet.
        Notifier ref = new EmailNotifier();
        return ref.kind();
    }

    public static String badCast() {
        // Le cast compile (SmsNotifier est un Notifier), mais l'objet reel est un EmailNotifier.
        Notifier ref = new EmailNotifier();
        try {
            SmsNotifier sms = (SmsNotifier) ref;
            return "cast reussi : " + sms.maxLength();
        } catch (ClassCastException e) {
            return "ClassCastException";
        }
    }
}
