package ch18_design.projects.p08_workflow;

/**
 * FOURNI (ne pas modifier) : l'ancienne gestion des commandes de la boutique en ligne.
 * L'etat est une chaine, et chaque methode refait sa propre serie de "if" sur cette chaine.
 * Lance main : compare ce qu'il accepte avec le tableau des transitions du TODO.md.
 */
public final class Data {

    private Data() {
    }

    public static void main(String[] args) {
        LegacyOrder a = new LegacyOrder();
        a.pay();
        a.ship();
        a.cancel();
        System.out.println("payee, expediee, puis annulee : " + a.status + ", rembourse " + a.refunded);

        LegacyOrder b = new LegacyOrder();
        b.pay();
        b.pay();
        System.out.println("payee deux fois : " + b.status + ", paiements " + b.payments);

        LegacyOrder c = new LegacyOrder();
        c.refund();
        System.out.println("remboursee sans avoir ete payee : " + c.status + ", rembourse " + c.refunded);
    }

    public static final class LegacyOrder {
        String status = "NEW";
        int payments;
        long refunded;
        private final long amount = 3400;

        public void pay() {
            if (status.equals("NEW") || status.equals("PAID")) {
                payments++;
                status = "PAID";
            }
        }

        public void ship() {
            if (status.equals("PAID")) {
                status = "SHIPPED";
            }
        }

        public void deliver() {
            if (status.equals("SHIPPED")) {
                status = "DELIVERED";
            }
        }

        public void cancel() {
            if (!status.equals("DELIVERED") && !status.equals("CANCELLED")) {
                if (status.equals("PAID") || status.equals("SHIPPED")) {
                    refunded = amount;
                }
                status = "CANCELLED";
            }
        }

        public void refund() {
            if (!status.equals("CANCELLED")) {
                refunded = amount;
                status = "REFUNDED";
            }
        }
    }
}
