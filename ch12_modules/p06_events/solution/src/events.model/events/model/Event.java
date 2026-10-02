package events.model;

/**
 * SOLUTION - un evenement : debut et fin en minutes depuis minuit, et une ville.
 */
public record Event(String id, int start, int end, String city) {
}
