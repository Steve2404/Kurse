package ch18_design.projects.p06_weather.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Le COMPOSITE : un groupe de WeatherService qui se presente comme UN seul. Il demande a toutes ses
 * sources et rend la moyenne de celles qui ont repondu. Un AverageWeather peut contenir un autre AverageWeather.
 */
public final class AverageWeather implements WeatherService {

    private final List<WeatherService> sources;

    public AverageWeather(List<WeatherService> sources) {
        this.sources = List.copyOf(sources);
    }

    @Override
    public Forecast forecast(String city) {
        List<Integer> answers = new ArrayList<>();
        for (WeatherService source : sources) {
            try {
                answers.add(source.forecast(city).celsius());
            } catch (RuntimeException e) {
                // une source en panne ne compte pas dans la moyenne
            }
        }
        if (answers.isEmpty()) {
            throw new IllegalStateException("aucune source pour " + city);
        }
        double average = answers.stream().mapToInt(Integer::intValue).average().orElseThrow();
        return new Forecast(city, (int) Math.round(average), "moyenne(" + answers.size() + ")");
    }
}
