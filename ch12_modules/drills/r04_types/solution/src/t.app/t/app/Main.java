package t.app;

import t.util.Tools;

/** SOLUTION - le programme du drill 4. */
public class Main {
    public static void main(String[] args) {
        Module auto = Tools.class.getModule();
        System.out.println(Tools.shout("auto") + " module " + auto.getName() + " automatique " + auto.getDescriptor().isAutomatic()
                + ", exporte t.util " + auto.isExported("t.util") + ", lit le module sans nom " + auto.canRead(ClassLoader.getSystemClassLoader().getUnnamedModule())
                + ", t.app le lit " + Main.class.getModule().canRead(auto));
    }
}
