package ch6_classdesign.drills.r03_initorder.solution;

/**
 * SOLUTION du drill de rappel 3 - l'ordre d'initialisation avec heritage.
 */
public class Recall03 {

    static final StringBuilder LOG = new StringBuilder();

    static String flush() {
        String s = LOG.toString().strip();
        LOG.setLength(0);
        return s;
    }

    public static void main(String[] args) {
        new Child();
        System.out.println("D01 : " + flush());
        new Child();
        System.out.println("D02 : " + flush());
        new Parent();
        System.out.println("D03 : " + flush());
        Child c = new Child();
        String during = flush();
        System.out.println("D04 : " + during.substring(during.indexOf("vu=")) + " apres=" + c.value);
        String constant = Other.CONST;
        String first = flush();
        int counter = Other.counter;
        System.out.println("D05 : [" + first + "] " + constant + " [" + flush() + "] " + counter);
        int inherited = Sub.shared;
        System.out.println("D06 : [" + flush() + "] " + inherited);
    }
}

class Parent {
    static {
        Recall03.LOG.append(" Ps");
    }

    {
        Recall03.LOG.append(" Pi");
    }

    Parent() {
        Recall03.LOG.append(" Pc");
        Recall03.LOG.append(" vu=" + show());   // appel d'une methode redefinie PENDANT la construction du parent
    }

    String show() {
        return "parent";
    }
}

class Child extends Parent {
    int value = 5;

    static {
        Recall03.LOG.append(" Cs");
    }

    {
        Recall03.LOG.append(" Ci");
    }

    Child() {
        Recall03.LOG.append(" Cc");
    }

    @Override
    String show() {
        return String.valueOf(value);           // 0 : le champ de Child n'est pas encore initialise
    }
}

class Other {
    static final String CONST = "K";            // constante de compilation : la lire ne charge pas Other
    static int counter = 7;

    static {
        Recall03.LOG.append(" Os");
    }
}

class Base {
    static int shared = 3;

    static {
        Recall03.LOG.append(" Bs");
    }
}

class Sub extends Base {
    static {
        Recall03.LOG.append(" Ss");             // jamais execute ici : Sub.shared est un champ de Base
    }
}
