package ch18_design.drills.r04_wrappers.solution;

import ch18_design.drills.r04_wrappers.Data;

/** L'adaptateur : symbole en minuscules pour le fournisseur, euros arrondis en centimes pour nous. */
public final class ExchangeAdapter implements PriceFeed {

    private final Data.OldExchange exchange;

    public ExchangeAdapter(Data.OldExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public long price(String symbol) {
        return Math.round(exchange.quote(symbol.toLowerCase()) * 100);
    }
}
