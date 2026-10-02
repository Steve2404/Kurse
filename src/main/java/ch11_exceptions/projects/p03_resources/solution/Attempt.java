package ch11_exceptions.projects.p03_resources.solution;

/**
 * SOLUTION - un essai numerote, qui peut echouer avec une exception verifiee.
 */
@FunctionalInterface
public interface Attempt<T> {

    T run(int number) throws ServiceException;
}
