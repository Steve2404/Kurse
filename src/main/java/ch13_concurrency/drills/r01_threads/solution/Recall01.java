package ch13_concurrency.drills.r01_threads.solution;

/**
 * SOLUTION du drill de rappel 1 - creer, demarrer, attendre, interrompre un Thread.
 */
public class Recall01 {

    static String result;

    public static void main(String[] args) throws InterruptedException {
        String[] name = new String[1];
        Thread t1 = new Thread(() -> name[0] = Thread.currentThread().getName(), "t1");
        t1.start();
        t1.join();
        System.out.println("D01 : " + name[0]);

        Thread w = new Worker();
        w.start();
        w.join();
        System.out.println("D02 : " + result + " " + w.getState());

        Runnable who = () -> name[0] = Thread.currentThread().getName();
        who.run();
        String direct = name[0];
        Thread t3 = new Thread(who, "t3");
        t3.start();
        t3.join();
        System.out.println("D03 : " + direct + " " + name[0]);

        Thread sleeper = new Thread(() -> {
            try {
                Thread.sleep(60_000);
            } catch (InterruptedException e) {
                name[0] = "interrompu, drapeau " + Thread.currentThread().isInterrupted();
            }
        });
        Thread.State before = sleeper.getState();
        sleeper.start();
        while (sleeper.getState() != Thread.State.TIMED_WAITING) {
            Thread.sleep(1);
        }
        Thread.State sleeping = sleeper.getState();
        sleeper.join(20);                                       // join avec delai : rend la main meme si le thread vit encore
        boolean stillAlive = sleeper.isAlive();
        sleeper.interrupt();
        sleeper.join();
        System.out.println("D04 : " + before + " " + sleeping + " " + stillAlive + " " + sleeper.getState() + " " + sleeper.isAlive());
        System.out.println("D05 : " + name[0]);

        Thread.currentThread().interrupt();
        boolean once = Thread.interrupted();                    // static : teste ET efface le drapeau du thread courant
        boolean twice = Thread.interrupted();
        System.out.println("D06 : " + once + " " + twice);

        Thread d = new Thread(() -> { });
        d.setDaemon(true);
        d.setPriority(Thread.MAX_PRIORITY);
        d.start();
        d.join();
        String again;
        try {
            d.start();
            again = "ok";
        } catch (IllegalThreadStateException e) {
            again = e.getClass().getSimpleName();
        }
        System.out.println("D07 : " + d.isDaemon() + " " + d.getPriority() + " " + Thread.NORM_PRIORITY + " " + again);

        // wait() rend le verrou et attend un notify ; il faut TENIR le verrou (synchronized) pour appeler wait ou notify.
        Object mailbox = new Object();
        String[] mail = new String[1];
        Thread reader = new Thread(() -> {
            synchronized (mailbox) {
                while (mail[0] == null) {                          // toujours dans une boucle (reveils intempestifs)
                    try {
                        mailbox.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        reader.start();
        while (reader.getState() != Thread.State.WAITING) {
            Thread.sleep(1);
        }
        Thread.State waiting = reader.getState();
        synchronized (mailbox) {
            mail[0] = "courrier";
            mailbox.notifyAll();
        }
        reader.join();
        String noLock;
        try {
            mailbox.notify();                                     // sans synchronized(mailbox) : interdit
            noLock = "ok";
        } catch (IllegalMonitorStateException e) {
            noLock = e.getClass().getSimpleName();
        }
        System.out.println("D08 : " + waiting + " " + reader.getState() + " " + noLock);
    }
}

// Une sous-classe de Thread qui redefinit run().
class Worker extends Thread {
    @Override
    public void run() {
        Recall01.result = "travail fait par " + (getName().startsWith("Thread-") ? "Thread-N" : getName());
    }
}
