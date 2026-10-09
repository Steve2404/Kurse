package ch18_design.projects.p06_weather.solution;

import java.util.List;

/**
 * Plusieurs sources, essayees DANS L'ORDRE : la premiere qui repond gagne (une chaine de responsabilite).
 * Pour l'appelant, c'est un WeatherService comme un autre.
 */
public final class FallbackWeather implements WeatherService {

    private final List<WeatherService> sources;

    public FallbackWeather(List<WeatherService> sources) {
        this.sources = List.copyOf(sources);
    }

    // Si toutes echouent, on le dit, en gardant chaque cause comme exception "supprimee" (chapitre 11).
    @Override
    public Forecast forecast(String city) {
        IllegalStateException failure = new IllegalStateException("aucune source pour " + city);
        for (WeatherService source : sources) {
            try {
                return source.forecast(city);
            } catch (RuntimeException e) {
                failure.addSuppressed(e);
            }
        }
        throw failure;
    }
}
