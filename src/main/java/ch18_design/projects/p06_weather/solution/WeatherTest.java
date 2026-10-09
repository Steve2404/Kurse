package ch18_design.projects.p06_weather.solution;

import ch18_design.projects.p06_weather.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Chaque piece se teste seule, avec des doublures ; puis toutes ensemble, emboitees par Weather.standard. */
class WeatherTest {

    /** Une doublure qui joue un scenario : une reponse ou une panne par appel, et compte les appels. */
    static final class ScriptedWeather implements WeatherService {
        private final Deque<Object> script;
        int calls;

        ScriptedWeather(Object... answers) {
            script = new ArrayDeque<>(Arrays.asList(answers));
        }

        @Override
        public Forecast forecast(String city) {
            calls++;
            Object next = script.size() > 1 ? script.poll() : script.peek();
            if (next instanceof RuntimeException e) {
                throw e;
            }
            return new Forecast(city, (Integer) next, "script");
        }
    }

    /** Une horloge qu'on fait avancer a la main : le cache se teste sans attendre dix minutes. */
    static final class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-10-09T08:00:00Z");

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public Instant instant() {
            return now;
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }
    }

    @ParameterizedTest
    @CsvSource({"Paris, 18", "Lyon, 22", "Brest, 13", "Nice, -12", "paris, 18"})
    void adapterConvertsAndRounds(String city, int celsius) {
        MeteoAdapter adapter = new MeteoAdapter(new Data.OldMeteoApi(Data.OldMeteoApi.READINGS, 0));
        assertEquals(new Forecast(city, celsius, "meteo"), adapter.forecast(city));
    }

    @Test
    void adapterLetsTheProviderErrorsThrough() {
        MeteoAdapter adapter = new MeteoAdapter(new Data.OldMeteoApi(Data.OldMeteoApi.READINGS, 0));
        assertEquals("station inconnue : ROM", assertThrows(NoSuchElementException.class, () -> adapter.forecast("Rome")).getMessage());
    }

    @Test
    void cacheKeepsAForecastStrictlyLessThanItsLifetime() {
        MutableClock clock = new MutableClock();
        ScriptedWeather inner = new ScriptedWeather(10, 11, 12);
        CachingWeather cache = new CachingWeather(inner, clock, Duration.ofMinutes(10));
        assertEquals(10, cache.forecast("Paris").celsius());
        clock.advance(Duration.ofMinutes(9).plusSeconds(59));
        assertEquals(10, cache.forecast("Paris").celsius());
        assertEquals(1, inner.calls);
        clock.advance(Duration.ofSeconds(1));
        assertEquals(11, cache.forecast("Paris").celsius());
        assertEquals(2, inner.calls);
    }

    @Test
    void cacheIsPerCityAndNeverKeepsAnError() {
        MutableClock clock = new MutableClock();
        ScriptedWeather inner = new ScriptedWeather(new IllegalStateException("panne"), 10, 20);
        CachingWeather cache = new CachingWeather(inner, clock, Duration.ofMinutes(10));
        assertThrows(IllegalStateException.class, () -> cache.forecast("Paris"));
        assertEquals(10, cache.forecast("Paris").celsius());
        assertEquals(20, cache.forecast("Lyon").celsius());
        assertEquals(10, cache.forecast("Paris").celsius());
        assertEquals(3, inner.calls);
    }

    @Test
    void retrySucceedsWithinItsAttempts() {
        ScriptedWeather inner = new ScriptedWeather(new IllegalStateException("panne 1"), new IllegalStateException("panne 2"), 15);
        assertEquals(15, new RetryingWeather(inner, 3).forecast("Paris").celsius());
        assertEquals(3, inner.calls);
    }

    @Test
    void retryGivesUpWithTheLastError() {
        ScriptedWeather inner = new ScriptedWeather(new IllegalStateException("panne 1"), new IllegalStateException("panne 2"), 15);
        RetryingWeather retry = new RetryingWeather(inner, 2);
        assertEquals("panne 2", assertThrows(IllegalStateException.class, () -> retry.forecast("Paris")).getMessage());
        assertEquals(2, inner.calls);
        assertEquals("au moins une tentative : 0",
                assertThrows(IllegalArgumentException.class, () -> new RetryingWeather(inner, 0)).getMessage());
    }

    @Test
    void loggingNotesAndPassesThrough() {
        IllegalStateException failure = new IllegalStateException("panne");
        List<String> log = new ArrayList<>();
        LoggingWeather logging = new LoggingWeather(new ScriptedWeather(18, failure), log);
        assertEquals(18, logging.forecast("Paris").celsius());
        assertSame(failure, assertThrows(IllegalStateException.class, () -> logging.forecast("Lyon")));
        assertEquals(List.of("Paris -> 18 C (script)", "Lyon -> erreur : panne"), log);
    }

    @Test
    void fallbackTriesInOrderAndStopsAtTheFirstAnswer() {
        ScriptedWeather broken = new ScriptedWeather(new IllegalStateException("panne"));
        ScriptedWeather good = new ScriptedWeather(17);
        ScriptedWeather never = new ScriptedWeather(99);
        assertEquals(17, new FallbackWeather(List.of(broken, good, never)).forecast("Paris").celsius());
        assertEquals(0, never.calls);
    }

    @Test
    void fallbackWithoutAnswerKeepsEveryCause() {
        FallbackWeather fallback = new FallbackWeather(List.of(new ScriptedWeather(new IllegalStateException("panne A")),
                new ScriptedWeather(new NoSuchElementException("panne B"))));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> fallback.forecast("Paris"));
        assertEquals("aucune source pour Paris", e.getMessage());
        assertEquals(List.of("panne A", "panne B"), Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList());
    }

    @Test
    void averageOfTheSourcesThatAnswer() {
        assertEquals(new Forecast("Paris", 19, "moyenne(2)"),
                new AverageWeather(List.of(new ScriptedWeather(18), new ScriptedWeather(19))).forecast("Paris"));
        assertEquals(new Forecast("Paris", 18, "moyenne(1)"),
                new AverageWeather(List.of(new ScriptedWeather(new IllegalStateException("x")), new ScriptedWeather(18))).forecast("Paris"));
        assertEquals("aucune source pour Paris", assertThrows(IllegalStateException.class,
                () -> new AverageWeather(List.of(new ScriptedWeather(new IllegalStateException("x")))).forecast("Paris")).getMessage());
    }

    // Un composite dans un composite : la moyenne de (moyenne de 10 et 20) et de 30.
    @Test
    void compositesNest() {
        WeatherService pair = new AverageWeather(List.of(new ScriptedWeather(10), new ScriptedWeather(20)));
        assertEquals(new Forecast("Paris", 23, "moyenne(2)"),
                new AverageWeather(List.of(pair, new ScriptedWeather(30))).forecast("Paris"));
    }

    @Test
    void standardStackRetriesCachesAndLogsEveryRequest() {
        Data.OldMeteoApi api = new Data.OldMeteoApi(Data.OldMeteoApi.READINGS, 2);
        MutableClock clock = new MutableClock();
        List<String> log = new ArrayList<>();
        WeatherService weather = Weather.standard(api, city -> new Forecast(city, 20, "secours"), clock, log);
        assertEquals(new Forecast("Paris", 18, "meteo"), weather.forecast("Paris"));
        assertEquals(3, api.calls());
        clock.advance(Duration.ofMinutes(5));
        assertEquals(new Forecast("Paris", 18, "meteo"), weather.forecast("Paris"));
        assertEquals(3, api.calls());
        assertEquals(List.of("Paris -> 18 C (meteo)", "Paris -> 18 C (meteo)"), log);
    }

    @Test
    void standardStackFallsBackAfterThreeAttempts() {
        Data.OldMeteoApi api = new Data.OldMeteoApi(Map.of("PAR", 64.4), 3);
        List<String> log = new ArrayList<>();
        WeatherService weather = Weather.standard(api, city -> new Forecast(city, 20, "secours"), new MutableClock(), log);
        assertEquals(new Forecast("Paris", 20, "secours"), weather.forecast("Paris"));
        assertEquals(3, api.calls());
        WeatherService dead = Weather.standard(new Data.OldMeteoApi(Map.of(), 9), city -> {
            throw new IllegalStateException("secours en panne");
        }, new MutableClock(), log);
        assertThrows(IllegalStateException.class, () -> dead.forecast("Lyon"));
        assertEquals(List.of("Paris -> 20 C (secours)", "Lyon -> erreur : aucune source pour Lyon"), log);
    }
}
