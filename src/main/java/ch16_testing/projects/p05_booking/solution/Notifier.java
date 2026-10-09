package ch16_testing.projects.p05_booking.solution;

/** Envoie un message a un utilisateur. En production, un courriel ; dans les tests, un espion qui note tout. */
public interface Notifier {

    void send(String to, String message);
}
