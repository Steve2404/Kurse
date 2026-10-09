package ch18_design.projects.p06_weather;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("MeteoAdapter.java", "city.substring(0, 3).toUpperCase()", "city.substring(0, 3)"),
            new Mutant("MeteoAdapter.java", "(int) Math.round((fahrenheit - 32) * 5 / 9)", "(int) ((fahrenheit - 32) * 5 / 9)"),
            new Mutant("CachingWeather.java", "now.isBefore(entry.expires())", "!now.isAfter(entry.expires())"),
            new Mutant("CachingWeather.java", "cache.put(city, new Entry(", "cache.put(\"\", new Entry("),
            new Mutant("CachingWeather.java", "now.plus(ttl)", "now.plus(ttl).plus(ttl)"),
            new Mutant("RetryingWeather.java", "attempt < attempts;", "attempt < attempts - 1;"),
            new Mutant("RetryingWeather.java", "throw last;", "throw new IllegalStateException(\"echec\");"),
            new Mutant("RetryingWeather.java", "if (attempts < 1) {", "if (attempts < 0) {"),
            new Mutant("LoggingWeather.java", "throw e;", "return null;"),
            new Mutant("LoggingWeather.java", "\" -> erreur : \"", "\" -> echec : \""),
            new Mutant("FallbackWeather.java", "failure.addSuppressed(e);", ""),
            new Mutant("AverageWeather.java", "(int) Math.round(average)", "(int) average"),
            new Mutant("AverageWeather.java", "\"moyenne(\" + answers.size()", "\"moyenne(\" + sources.size()"),
            new Mutant("AverageWeather.java", "} catch (RuntimeException e) {", "} catch (IllegalArgumentException e) {"),
            new Mutant("Weather.java", "new LoggingWeather(new CachingWeather(withBackup, clock, TTL), log)", "new CachingWeather(new LoggingWeather(withBackup, log), clock, TTL)"),
            new Mutant("Weather.java", "static final int ATTEMPTS = 3;", "static final int ATTEMPTS = 2;"),
            new Mutant("Weather.java", "List.of(meteo, backup)", "List.of(backup, meteo)"));

    static final List<String> API_CODE = List.of(
            "record Forecast(", "interface WeatherService", "final class MeteoAdapter implements WeatherService",
            "final class CachingWeather implements WeatherService", "final class RetryingWeather implements WeatherService",
            "final class LoggingWeather implements WeatherService", "final class FallbackWeather implements WeatherService",
            "final class AverageWeather implements WeatherService", "final class Weather", "Math.round(", "addSuppressed(",
            "clock.instant()", "!extends##aucun heritage (extends) : on compose", "!instanceof", "!System.out", "!Thread.sleep",
            "max:method=12",
            "in:CachingWeather.java=private final WeatherService inner##le proxy enveloppe un WeatherService",
            "in:RetryingWeather.java=private final WeatherService inner##le decorateur enveloppe un WeatherService",
            "in:LoggingWeather.java=private final WeatherService inner##le decorateur enveloppe un WeatherService",
            "in:AverageWeather.java=List<WeatherService>##le composite contient des WeatherService",
            "in:CachingWeather.java!OldMeteoApi##seul l'adaptateur connait l'API du fournisseur",
            "in:RetryingWeather.java!OldMeteoApi##seul l'adaptateur connait l'API du fournisseur",
            "in:FallbackWeather.java!OldMeteoApi##seul l'adaptateur connait l'API du fournisseur",
            "in:AverageWeather.java!OldMeteoApi##seul l'adaptateur connait l'API du fournisseur");

    static final List<String> API_TESTS = List.of(
            "implements WeatherService", "extends Clock", "Data.OldMeteoApi", "Weather.standard(", "getSuppressed()",
            "assertSame(", "assertThrows(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 15, MUTANTS, API_CODE, API_TESTS);
    }
}
