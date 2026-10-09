package ch16_testing.projects.p06_payment.solution;

/** La banque n'a pas repondu a temps. */
public class GatewayTimeoutException extends RuntimeException {

    public GatewayTimeoutException(String message) {
        super(message);
    }
}
