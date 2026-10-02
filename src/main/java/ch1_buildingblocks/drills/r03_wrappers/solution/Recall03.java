package ch1_buildingblocks.drills.r03_wrappers.solution;

/**
 * SOLUTION du drill de rappel 3 - classes enveloppes.
 */
public class Recall03 {

    public static void main(String[] args) {
        // parseXxx -> primitif ; valueOf -> objet ; xxxValue() -> de l'objet vers le primitif.
        int a = Integer.parseInt("123");
        Integer b = Integer.valueOf("123");
        System.out.println("D01 : " + a + " " + b + " " + b.intValue());
        System.out.println("D02 : " + Double.parseDouble("2.5") + " " + Double.valueOf("2.5").intValue() + " " + Double.valueOf("-2.9").intValue());
        System.out.println("D03 : " + Integer.valueOf(257).byteValue() + " " + Integer.valueOf(128).byteValue() + " " + Long.valueOf(70000).shortValue());
        System.out.println("D04 : " + Integer.parseInt("777", 8) + " " + Integer.parseInt("1F", 16) + " " + Integer.parseInt("+15"));
        System.out.println("D05 : " + Integer.decode("0x1F") + " " + Integer.decode("#1F") + " " + Integer.decode("017") + " " + Integer.decode("-17"));
        System.out.println("D06 : " + Boolean.parseBoolean("tRuE") + " " + Boolean.parseBoolean("yes") + " " + Boolean.valueOf("false").booleanValue());
        System.out.println("D07 : " + Long.parseLong("123456789012") + " " + Float.parseFloat("0.5") + " " + Short.parseShort("-32768"));
        int maxChar = Character.MAX_VALUE;
        System.out.println("D08 : " + Integer.MIN_VALUE + " " + Double.valueOf("1e-2") + " " + maxChar);
    }
}
