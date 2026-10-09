package ch19_final.projects.p04_jobs.solution;

import ch19_final.projects.p04_jobs.Data;

/**
 * L'ADAPTATEUR vers l'operateur fourni (chapitre 18) : il traduit ses exceptions en GatewayException,
 * en disant pour chacune si reessayer a un sens.
 */
public final class OperatorGateway implements SmsGateway {

    private final Data.Operator operator;

    public OperatorGateway(Data.Operator operator) {
        this.operator = operator;
    }

    @Override
    public String send(String phone, String text) {
        try {
            return operator.deliver(phone, text);
        } catch (IllegalStateException e) {
            throw new GatewayException(e.getMessage(), true);
        } catch (IllegalArgumentException e) {
            throw new GatewayException(e.getMessage(), false);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GatewayException("interrompu", false);
        }
    }
}
