package ch18_design.projects.p06_weather.solution;

/** Une prevision : la ville, la temperature en degres Celsius, et d'ou elle vient. */
public record Forecast(String city, int celsius, String source) {
}
