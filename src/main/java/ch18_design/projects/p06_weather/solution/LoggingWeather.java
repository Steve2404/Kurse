package ch18_design.projects.p06_weather.solution;

import java.util.List;

/** Un decorateur : il note chaque appel (et son resultat) dans un journal, puis rend ou relance tel quel. */
public final class LoggingWeather implements WeatherService {

    private final WeatherService inner;
    private final List<String> log;

    public LoggingWeather(WeatherService inner, List<String> log) {
        this.inner = inner;
        this.log = log;
    }

    @Override
    public Forecast forecast(String city) {
        try {
            Forecast forecast = inner.forecast(city);
            log.add(city + " -> " + forecast.celsius() + " C (" + forecast.source() + ")");
            return forecast;
        } catch (RuntimeException e) {
            log.add(city + " -> erreur : " + e.getMessage());
            throw e;
        }
    }
}
