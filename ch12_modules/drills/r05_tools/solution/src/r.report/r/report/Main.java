package r.report;

import java.sql.Date;

/** SOLUTION - le programme du drill 5. */
public class Main {
    public static void main(String[] args) {
        System.out.println("rapport du " + Date.valueOf("2026-10-02") + " dans " + Main.class.getModule().getName());
    }
}
