package ch14_io.drills.r04_serialization.solution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * SOLUTION du drill de rappel 4 - la serialisation.
 */
public class Recall04 {

    static byte[] save(Object... objects) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            for (Object o : objects) {
                out.writeObject(o);
            }
        }
        return bytes.toByteArray();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Account a = new Account("ana", 100, "secret");
        Account.bank = "Banque A";
        byte[] data = save(a, "suite", 7);
        Account.bank = "Banque B";
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data))) {
            Account b = (Account) in.readObject();
            System.out.println("D01 : " + b.owner + " " + b.balance + " " + b.pin + " " + Account.bank + " " + (a == b) + " " + in.readObject() + " " + in.readObject());
            String end;
            try {
                in.readObject();
                end = "encore";
            } catch (EOFException e) {
                end = e.getClass().getSimpleName();
            }
            System.out.println("D02 : " + end);
        }

        Base.calls = 0;
        Child c = new Child();
        c.baseValue = 5;
        c.childValue = 9;
        int before = Base.calls;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(save(c)))) {
            Child d = (Child) in.readObject();
            System.out.println("D03 : " + before + " " + Base.calls + " " + d.baseValue + " " + d.childValue + " " + d.initialized);
        }

        String refused;
        try {
            save(new Unsafe());
            refused = "ok";
        } catch (NotSerializableException e) {
            refused = e.getClass().getSimpleName();
        }
        System.out.println("D04 : " + refused);

        Point.built = 0;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(save(new Point(1, 2), List.of(new Point(3, 4)))))) {
            Point p = (Point) in.readObject();
            Object list = in.readObject();
            System.out.println("D05 : " + p + " " + list + " " + Point.built);
        }
    }
}

class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    static String bank;
    final String owner;
    final int balance;
    transient String pin;

    Account(String owner, int balance, String pin) {
        this.owner = owner;
        this.balance = balance;
        this.pin = pin;
    }
}

// Mere NON serialisable : son constructeur sans argument s'execute a la deserialisation.
class Base {
    static int calls;
    int baseValue = 1;

    Base() {
        calls++;
    }
}

class Child extends Base implements Serializable {
    private static final long serialVersionUID = 1L;
    int childValue;
    transient boolean initialized = true;
}

class Unsafe implements Serializable {
    private static final long serialVersionUID = 1L;
    Object notSerializable = new Object();
}

// Un record : deserialise par son constructeur canonique.
record Point(int x, int y) implements Serializable {
    static int built;

    Point {
        built++;
    }
}
