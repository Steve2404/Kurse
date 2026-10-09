package ch19_final.projects.p04_jobs.solution;

/** Le port d'envoi : rend l'identifiant du message, ou lance GatewayException. */
@FunctionalInterface
public interface SmsGateway {

    String send(String phone, String text);
}
