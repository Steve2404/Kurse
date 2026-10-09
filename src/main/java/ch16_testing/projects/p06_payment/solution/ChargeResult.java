package ch16_testing.projects.p06_payment.solution;

/** La reponse de la banque : acceptee (avec un numero de transaction) ou refusee (avec une raison). */
public record ChargeResult(boolean approved, String transactionId, String reason) {
}
