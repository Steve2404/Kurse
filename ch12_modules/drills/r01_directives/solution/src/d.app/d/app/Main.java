package d.app;

import d.base.Greeter;
import d.friend.Friend;
import d.mid.Polite;

import java.util.ServiceLoader;

/** SOLUTION - le programme du drill 1. */
public class Main {
    public static void main(String[] args) {
        Greeter g = ServiceLoader.load(Greeter.class).findFirst().orElseThrow();
        System.out.println(Polite.wrap(g, "Ada") + " " + Friend.secret());
    }
}
