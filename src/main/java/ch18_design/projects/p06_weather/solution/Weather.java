package ch18_design.projects.p06_weather.solution;

import ch18_design.projects.p06_weather.Data;

import java.time.Clock;
import java.time.Duration;
import java.util.List;

/**
 * La racine de composition : on EMBOITE les pieces comme des poupees russes. L'ordre compte :
 * le journal est dehors (il voit chaque demande), le cache juste dedans, puis le secours, puis les reessais.
 */
public final class Weather {

    static final Duration TTL = Duration.ofMinutes(10);
    static final int ATTEMPTS = 3;

    private Weather() {
    }

    public static WeatherService standard(Data.OldMeteoApi api, WeatherService backup, Clock clock, List<String> log) {
        WeatherService meteo = new RetryingWeather(new MeteoAdapter(api), ATTEMPTS);
        WeatherService withBackup = new FallbackWeather(List.of(meteo, backup));
        return new LoggingWeather(new CachingWeather(withBackup, clock, TTL), log);
    }
}
