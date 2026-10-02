package ch11_exceptions.projects.p06_agenda.solution;

import java.time.LocalDateTime;

/**
 * SOLUTION - un rendez-vous.
 */
public record Event(LocalDateTime when, String title) {
}
