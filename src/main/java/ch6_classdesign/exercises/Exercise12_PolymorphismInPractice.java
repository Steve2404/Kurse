package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 12 - Le polymorphisme en pratique : des notificateurs, et les 3 choses qui NE sont PAS polymorphes (niveau : avance)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une application envoie des messages par plusieurs canaux. Notifier est
 * le parent ; EmailNotifier et SmsNotifier redefinissent ce qui change.
 * Avec une variable de type Notifier, Java appelle la methode de
 * l'OBJET REEL (redefinition = choix a l'execution).
 *
 * Mais 3 choses sont choisies par le TYPE DE LA VARIABLE, a la compilation :
 *   - les CHAMPS (un champ de meme nom dans l'enfant CACHE celui du parent) ;
 *   - les methodes STATIC (cachage) ;
 *   - la liste des methodes qu'on a le DROIT d'appeler.
 * Et un cast vers le mauvais type compile... puis lance ClassCastException.
 *
 *
 * ==================================================================
 * TODO 1 : EmailNotifier.channel()    et    TODO 2 : SmsNotifier.format(message)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   new EmailNotifier().send("Bonjour")  -> "[email] Bonjour"
 *   new SmsNotifier().send("Rendez-vous demain a la gare centrale")
 *                                        -> "[sms] Rendez-vous demain a" (20 caracteres max)
 *
 * -- Le plan --
 *
 *   1. channel : rendre "email".
 *   2. SmsNotifier.format : couper message a 20 caracteres (si plus long), puis rendre super.format(coupe).
 *      Le canal "sms" vient deja de SmsNotifier.channel(), deja ecrit.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : send (du parent) appelle format, qui appelle channel ; chaque objet utilise SES versions.
 *
 *
 * ==================================================================
 * TODO 3 : broadcast(notifiers, message)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Pour chaque notifier de la liste : ajouter notifier.send(message) au resultat.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : asSmsOrNull(notifier)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut appeler maxLength(), qui n'existe QUE dans SmsNotifier. Avec
 * une variable Notifier, le compilateur refuse. Il faut descendre de
 * type (cast), mais seulement si c'est vraiment un SmsNotifier.
 *
 * -- Le plan --
 *
 *   1. Si notifier instanceof SmsNotifier sms, rendre sms ; sinon null.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : labelsThroughBothTypes()    TODO 6 : kindThroughParentType()    TODO 7 : badCast()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Trois petites experiences, avec Notifier ref = new EmailNotifier() :
 *   5. ref.label + "/" + ((EmailNotifier) ref).label  -> "notifier/email" (le champ suit le type de la variable)
 *   6. ref.kind()  (kind est static, cachee dans EmailNotifier) -> "notifier"
 *   7. (SmsNotifier) ref -> ClassCastException : rendre "ClassCastException" (try/catch)
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
 *   - message.substring(0, Math.min(message.length(), 20)).
 *   - @SuppressWarnings("static") au-dessus de kindThroughParentType() evite l'avertissement de javac -Xlint.
 */
public class Exercise12_PolymorphismInPractice {

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
            throw new UnsupportedOperationException("TODO 1 : implementer channel()");
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
            throw new UnsupportedOperationException("TODO 2 : implementer format()");
        }
    }

    public static List<String> broadcast(List<Notifier> notifiers, String message) {
        throw new UnsupportedOperationException("TODO 3 : implementer broadcast()");
    }

    public static SmsNotifier asSmsOrNull(Notifier notifier) {
        throw new UnsupportedOperationException("TODO 4 : implementer asSmsOrNull()");
    }

    public static String labelsThroughBothTypes() {
        throw new UnsupportedOperationException("TODO 5 : implementer labelsThroughBothTypes()");
    }

    public static String kindThroughParentType() {
        throw new UnsupportedOperationException("TODO 6 : implementer kindThroughParentType()");
    }

    public static String badCast() {
        throw new UnsupportedOperationException("TODO 7 : implementer badCast()");
    }

    public static void main(String[] args) {
        Notifier email = new EmailNotifier();
        Notifier sms = new SmsNotifier();
        ExerciseChecker.check("EmailNotifier : [email] Bonjour", email.send("Bonjour").equals("[email] Bonjour"));
        ExerciseChecker.check("SmsNotifier : coupe a 20 caracteres puis super.format",
                sms.send("Rendez-vous demain a la gare centrale").equals("[sms] Rendez-vous demain a") && sms.send("Court").equals("[sms] Court"));
        List<Notifier> all = new ArrayList<>(List.of(new Notifier(), email, sms));
        ExerciseChecker.check("broadcast : chaque objet utilise SA version",
                broadcast(all, "Hi").equals(List.of("[base] Hi", "[email] Hi", "[sms] Hi")));
        SmsNotifier found = asSmsOrNull(sms);
        ExerciseChecker.check("asSmsOrNull : SmsNotifier accessible (maxLength 20), email -> null",
                found != null && found.maxLength() == 20 && asSmsOrNull(email) == null);
        ExerciseChecker.check("labelsThroughBothTypes() == \"notifier/email\"", labelsThroughBothTypes().equals("notifier/email"));
        ExerciseChecker.check("kindThroughParentType() == \"notifier\"", kindThroughParentType().equals("notifier"));
        ExerciseChecker.check("badCast() == \"ClassCastException\"", badCast().equals("ClassCastException"));

        ExerciseChecker.summary();
    }
}
