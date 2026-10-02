import c.lib.Calc;

/** SOLUTION - une classe du CLASSPATH (paquet par defaut) qui utilise un module nomme. */
public class Legacy {
    public static void main(String[] args) {
        System.out.println("classpath : somme " + Calc.add(10, 5) + ", module sans nom " + !Legacy.class.getModule().isNamed());
    }
}
