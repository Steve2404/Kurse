package ch18_design.projects.p06_weather.solution;

/**
 * Un DECORATEUR : il ajoute un comportement (reessayer) a n'importe quel WeatherService, sans le modifier
 * et sans heritage. On peut l'empiler avec d'autres decorateurs, dans l'ordre qu'on veut.
 */
public final class RetryingWeather implements WeatherService {

    private final WeatherService inner;
    private final int attempts;

    public RetryingWeather(WeatherService inner, int attempts) {
        if (attempts < 1) {
            throw new IllegalArgumentException("au moins une tentative : " + attempts);
        }
        this.inner = inner;
        this.attempts = attempts;
    }

    // Au plus "attempts" appels ; apres le dernier echec, on relance la DERNIERE exception.
    @Override
    public Forecast forecast(String city) {
        RuntimeException last = null;
        for (int attempt = 0; attempt < attempts; attempt++) {
            try {
                return inner.forecast(city);
            } catch (RuntimeException e) {
                last = e;
            }
        }
        throw last;
    }
}
