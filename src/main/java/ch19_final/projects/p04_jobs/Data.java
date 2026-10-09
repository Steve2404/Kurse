package ch19_final.projects.p04_jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Les donnees FOURNIES du projet 4 (ne pas modifier).
 *
 * Operator imite l'operateur SMS de l'atelier : un service lent (50 ms par message) et capricieux.
 * Pour que la demo donne toujours le meme resultat, ses caprices dependent du numero, pas du hasard :
 *   - un numero qui finit par 1 est "occupe" une fois, puis passe ; par 2, deux fois ; par 7, toujours ;
 *   - un numero qui ne commence pas par 06 ou 07 est refuse ("numero inconnu") : reessayer ne sert a rien.
 */
public final class Data {

    private Data() {
    }

    /** Les rappels du jour : identifiant de tache, telephone du client, texte. */
    public static final List<String[]> REMINDERS = List.of(
            new String[]{"1", "0612345670", "Votre velo est pret"},
            new String[]{"2", "0612345671", "Votre velo est pret"},
            new String[]{"3", "0712345672", "Devis disponible"},
            new String[]{"4", "0612345673", "Votre velo est pret"},
            new String[]{"5", "0512345674", "Rappel : rendez-vous demain"},
            new String[]{"6", "0612345675", "Devis disponible"},
            new String[]{"7", "0612345677", "Votre velo est pret"},
            new String[]{"8", "0712345678", "Rappel : rendez-vous demain"},
            new String[]{"9", "0612345679", "Votre velo est pret"},
            new String[]{"10", "0612345680", "Devis disponible"},
            new String[]{"11", "0712345681", "Votre velo est pret"},
            new String[]{"12", "0612345682", "Rappel : rendez-vous demain"});

    /** L'operateur SMS : appelle-le depuis TON adaptateur, il ne connait pas tes interfaces. */
    public static final class Operator {

        private final Map<String, AtomicInteger> attempts = new ConcurrentHashMap<>();
        private final AtomicInteger delivered = new AtomicInteger();

        /** Rend l'identifiant du message, ou lance IllegalStateException (occupe) / IllegalArgumentException (inconnu). */
        public String deliver(String phone, String text) throws InterruptedException {
            Thread.sleep(50);
            if (!phone.startsWith("06") && !phone.startsWith("07")) {
                throw new IllegalArgumentException("numero inconnu : " + phone);
            }
            int attempt = attempts.computeIfAbsent(phone, p -> new AtomicInteger()).incrementAndGet();
            char last = phone.charAt(phone.length() - 1);
            int busyTimes = last == '1' ? 1 : last == '2' ? 2 : last == '7' ? Integer.MAX_VALUE : 0;
            if (attempt <= busyTimes) {
                throw new IllegalStateException("operateur occupe");
            }
            return "SMS-" + delivered.incrementAndGet();
        }
    }
}
