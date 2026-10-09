package ch18_design.projects.p06_weather.solution;

/**
 * NOTRE interface : celle que l'application veut, dans sa langue (Celsius, noms de villes).
 * Toutes les pieces du projet l'implementent, et la plupart en ENVELOPPENT une autre : on les emboite.
 */
@FunctionalInterface
public interface WeatherService {

    Forecast forecast(String city);
}
