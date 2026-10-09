package ch18_design.projects.p06_weather.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Le PROXY : il a la meme interface que le service qu'il remplace, et controle l'acces a lui.
 * Ici, il garde chaque prevision pendant une duree donnee, pour ne pas rappeler le fournisseur (lent, payant).
 */
public final class CachingWeather implements WeatherService {

    private record Entry(Forecast forecast, Instant expires) {
    }

    private final WeatherService inner;
    private final Clock clock;
    private final Duration ttl;
    private final Map<String, Entry> cache = new HashMap<>();

    public CachingWeather(WeatherService inner, Clock clock, Duration ttl) {
        this.inner = inner;
        this.clock = clock;
        this.ttl = ttl;
    }

    // Une entree est valable STRICTEMENT avant son expiration ; une erreur n'est jamais gardee.
    @Override
    public Forecast forecast(String city) {
        Instant now = clock.instant();
        Entry entry = cache.get(city);
        if (entry != null && now.isBefore(entry.expires())) {
            return entry.forecast();
        }
        Forecast fresh = inner.forecast(city);
        cache.put(city, new Entry(fresh, now.plus(ttl)));
        return fresh;
    }
}
