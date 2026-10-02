package d.friend;

import d.base.hidden.Secret;

/** SOLUTION - d.friend a acces au paquet qualifie, et lit d.base par transitivite (via d.mid). */
public final class Friend {
    private Friend() {
    }

    public static String secret() {
        return "secret " + Secret.code();
    }
}
