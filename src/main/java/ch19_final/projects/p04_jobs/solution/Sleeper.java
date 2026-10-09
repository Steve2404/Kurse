package ch19_final.projects.p04_jobs.solution;

import java.time.Duration;

/**
 * Attendre est une dependance, comme l'heure (chapitre 18) : les tests injectent un Sleeper qui note les
 * attentes au lieu de dormir. Le programme ne dort jamais "en dur".
 */
@FunctionalInterface
public interface Sleeper {

    Sleeper REAL = duration -> Thread.sleep(duration.toMillis());

    void sleep(Duration duration) throws InterruptedException;
}
