package ch18_design.projects.p06_weather.solution;

import ch18_design.projects.p06_weather.Data;

/**
 * L'ADAPTATEUR : il fait parler l'API du fournisseur comme notre interface. C'est le SEUL endroit du
 * projet qui connait OldMeteoApi, les codes de station et les degres Fahrenheit.
 */
public final class MeteoAdapter implements WeatherService {

    private final Data.OldMeteoApi api;

    public MeteoAdapter(Data.OldMeteoApi api) {
        this.api = api;
    }

    @Override
    public Forecast forecast(String city) {
        double fahrenheit = api.tempFahrenheit(stationCode(city));
        return new Forecast(city, toCelsius(fahrenheit), "meteo");
    }

    // "Paris" -> "PAR" : les trois premieres lettres, en majuscules.
    static String stationCode(String city) {
        return city.substring(0, 3).toUpperCase();
    }

    // Arrondi au degre le plus proche (Math.round), pas tronque : 71 F = 21,67 C donne 22.
    static int toCelsius(double fahrenheit) {
        return (int) Math.round((fahrenheit - 32) * 5 / 9);
    }
}
