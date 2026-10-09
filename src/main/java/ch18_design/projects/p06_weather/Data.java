package ch18_design.projects.p06_weather;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * FOURNI (ne pas modifier) : la bibliotheque meteo d'un fournisseur exterieur. On ne peut pas la changer.
 * Elle parle en degres Fahrenheit, avec des codes de station a trois lettres, et elle tombe parfois en panne.
 */
public final class Data {

    private Data() {
    }

    public static void main(String[] args) {
        OldMeteoApi api = new OldMeteoApi(OldMeteoApi.READINGS, 2);
        for (int i = 1; i <= 3; i++) {
            try {
                System.out.println("appel " + i + " : PAR = " + api.tempFahrenheit("PAR") + " F");
            } catch (IllegalStateException e) {
                System.out.println("appel " + i + " : " + e.getMessage());
            }
        }
        System.out.println("appels recus : " + api.calls());
    }

    /** L'API du fournisseur. */
    public static final class OldMeteoApi {

        /** Les releves du jour, en degres Fahrenheit. */
        public static final Map<String, Double> READINGS = Map.of("PAR", 64.4, "LYO", 71.0, "BRE", 55.0, "NIC", 11.0);

        private final Map<String, Double> fahrenheit;
        private int failuresLeft;
        private int calls;

        /** failures : le nombre de premiers appels qui echouent (la station est injoignable). */
        public OldMeteoApi(Map<String, Double> fahrenheit, int failures) {
            this.fahrenheit = Map.copyOf(fahrenheit);
            this.failuresLeft = failures;
        }

        public double tempFahrenheit(String stationCode) {
            calls++;
            if (failuresLeft > 0) {
                failuresLeft--;
                throw new IllegalStateException("station injoignable");
            }
            Double value = fahrenheit.get(stationCode);
            if (value == null) {
                throw new NoSuchElementException("station inconnue : " + stationCode);
            }
            return value;
        }

        /** Le nombre d'appels recus, reussis ou non. */
        public int calls() {
            return calls;
        }
    }
}
