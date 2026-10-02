package ch11_exceptions.projects.p03_resources.solution;

import ch11_exceptions.projects.p03_resources.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SOLUTION du projet 3 - try-with-resources, exceptions supprimees, reessais et disjoncteur.
 */
public class Resources {

    // Le type, le message, puis les exceptions SUPPRIMEES (celles de close() pendant une exception principale).
    static String describe(Throwable e) {
        String text = e.getClass().getSimpleName() + "(" + e.getMessage() + ")";
        Throwable[] suppressed = e.getSuppressed();
        return suppressed.length == 0 ? text : text + " supprimees " + Arrays.stream(suppressed).map(Throwable::getMessage).toList();
    }

    static void runJob(String job, List<String> log) {
        String[] p = job.split(" ");
        // Fermeture dans l'ordre INVERSE de l'ouverture, AVANT le catch et le finally.
        try (Channel first = new Channel(p[0], log); Channel second = new Channel(p[1], log)) {
            first.send("debut");
            if (p[2].equals("fail")) {
                throw new IllegalStateException("travail rate");
            }
            second.send("fin");
        } catch (ResourceException | IllegalStateException e) {
            log.add("attrape " + describe(e));
        } finally {
            log.add("finally");
        }
    }

    // Reessayer : la cause finale est le DERNIER echec ; les echecs precedents sont ajoutes en SUPPRIMES.
    static <T> T retry(int max, Attempt<T> attempt) throws RetryExhaustedException {
        List<ServiceException> previous = new ArrayList<>();
        ServiceException last = null;
        for (int number = 1; number <= max; number++) {
            try {
                return attempt.run(number);
            } catch (ServiceException e) {
                if (last != null) {
                    previous.add(last);
                }
                last = e;
            }
        }
        RetryExhaustedException failure = new RetryExhaustedException("abandon apres " + max + " essais", last);
        previous.forEach(failure::addSuppressed);
        throw failure;
    }

    static Attempt<Integer> scripted(String[] answers) {
        return number -> {
            String answer = answers[number - 1];
            if (answer.startsWith("ok:")) {
                return Integer.parseInt(answer.substring(3));
            }
            throw new ServiceException(answer + " (essai " + number + ")");
        };
    }

    public static void main(String[] args) {
        for (String job : Data.JOBS) {
            List<String> log = new ArrayList<>();
            runJob(job, log);
            System.out.println(job + " : " + String.join(", ", log));
        }

        // Java 9 : une variable effectivement finale declaree AVANT peut servir de ressource.
        List<String> log = new ArrayList<>();
        try {
            Channel shared = new Channel("partage", log);
            try (shared) {
                shared.send("un");
            }
            shared.send("deux");                               // apres le try : la ressource est FERMEE
        } catch (IllegalStateException | ResourceException e) {
            log.add("attrape " + describe(e));
        }
        System.out.println("ressource existante : " + String.join(", ", log));

        Journal journal = new Journal();
        try (journal) {
            journal.close();                                   // ferme a la main, puis try le referme
        }
        System.out.println("Closeable idempotent : " + journal.stats());

        for (String[] answers : List.of(Data.FLAKY, Data.DOWN)) {
            try {
                System.out.println("reessais " + Arrays.toString(answers) + " -> " + retry(Data.MAX_ATTEMPTS, scripted(answers)));
            } catch (RetryExhaustedException e) {
                System.out.println("reessais " + Arrays.toString(answers) + " -> " + e.getMessage() + " <- " + e.getCause().getMessage() + " ; " + describe(e));
            }
        }

        CircuitBreaker breaker = new CircuitBreaker(Data.THRESHOLD, Data.PAUSE);
        StringBuilder trace = new StringBuilder();
        int served = 0;
        int failed = 0;
        int rejected = 0;
        for (String outcome : Data.CALLS.split(" ")) {
            String mark;
            try {
                mark = breaker.call(outcome);
                served++;
            } catch (CircuitOpenException e) {
                mark = "refus";
                rejected++;
            } catch (ServiceException e) {
                mark = "echec";
                failed++;
            }
            trace.append(' ').append(mark).append('/').append(breaker.state());
        }
        System.out.println("disjoncteur :" + trace);
        System.out.println("servis " + served + ", echecs " + failed + ", refus " + rejected + ", etat final " + breaker.state());
    }
}
