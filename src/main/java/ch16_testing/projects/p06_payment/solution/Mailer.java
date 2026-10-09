package ch16_testing.projects.p06_payment.solution;

public interface Mailer {

    void send(String to, String subject, String body);
}
