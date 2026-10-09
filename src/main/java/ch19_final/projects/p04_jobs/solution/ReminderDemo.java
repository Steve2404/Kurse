package ch19_final.projects.p04_jobs.solution;

import ch19_final.projects.p04_jobs.Data;

import java.time.Duration;
import java.util.List;

/** La demonstration : les 12 rappels du jour, envoyes par 3 fils, avec de vraies attentes. */
public final class ReminderDemo {

    private ReminderDemo() {
    }

    public static void main(String[] args) throws InterruptedException {
        Metrics metrics = new Metrics();
        Retrier retrier = new Retrier(new RetryPolicy(4, Duration.ofMillis(100), 2, Duration.ofSeconds(1)), Sleeper.REAL, metrics);
        ReminderService service = new ReminderService(new OperatorGateway(new Data.Operator()), retrier, 3, 20,
                Duration.ofSeconds(2), metrics);
        List<Notification> reminders = Data.REMINDERS.stream()
                .map(r -> new Notification(Long.parseLong(r[0]), r[1], r[2])).toList();
        long start = System.nanoTime();
        for (SendResult r : service.sendAll(reminders)) {
            System.out.println("tache " + r.taskId() + " : " + r.status() + (r.status().equals("envoye") ? "" : " (" + r.detail() + ")"));
        }
        System.out.println("duree : " + (System.nanoTime() - start) / 1_000_000 + " ms");
        System.out.println(metrics.snapshot());
        System.out.println("jamais commences : " + service.shutdown(Duration.ofSeconds(1)));
    }
}
