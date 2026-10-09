package ch16_testing.projects.p06_payment.solution;

/** La banque. Peut lancer GatewayTimeoutException si elle ne repond pas. */
public interface PaymentGateway {

    ChargeResult charge(String customer, long cents);
}
