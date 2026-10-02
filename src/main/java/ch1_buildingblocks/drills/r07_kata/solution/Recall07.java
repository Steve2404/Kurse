package ch1_buildingblocks.drills.r07_kata.solution;

/**
 * SOLUTION du drill de rappel 7 - kata mixte chronometre.
 */
public class Recall07 {

    static long total;

    public static void main(String... args) {
        int qty = Integer.parseInt(args[0]);
        int price = Integer.decode(args[1]);
        System.out.println("D01 : " + qty * price);
        System.out.println("D02 : " + Integer.toBinaryString(qty) + " " + Integer.toHexString(price));
        var box = new Box(args[2]);
        System.out.println("D03 : " + box.log);
        System.out.println("D04 : " + total + " " + Box.count);
        total = 3_000_000_000L + qty;
        System.out.println("D05 : " + total);
        String card = """
                ** ticket **
                """;
        System.out.print("D06 : " + card);
        final double rate = Double.parseDouble(args[3]);
        System.out.println("D07 : " + rate * 100 + " " + Double.valueOf(args[3]).intValue());
        System.out.println("D08 : " + Boolean.parseBoolean(args[4]) + " " + '\u0041' + 'B');
    }
}

class Box {
    static int count;
    String log = "1";
    String label;

    {
        log = log + "2";
    }

    Box(String label) {
        log = log + "3";
        this.label = label;
        count = count + 1;
        log = log + this.label;
    }
}
