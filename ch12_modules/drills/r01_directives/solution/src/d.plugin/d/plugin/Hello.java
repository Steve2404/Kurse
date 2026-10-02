package d.plugin;

import d.base.Greeter;

/** SOLUTION - le fournisseur (public, constructeur public sans argument). */
public class Hello implements Greeter {
    @Override
    public String greet(String name) {
        return "Bonjour " + name;
    }
}
