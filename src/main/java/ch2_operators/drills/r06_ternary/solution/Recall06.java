package ch2_operators.drills.r06_ternary.solution;

/**
 * SOLUTION du drill de rappel 6 - ternaire et priorite des operateurs.
 */
public class Recall06 {

    public static void main(String[] args) {
        int age = 17;
        System.out.println("D01 : " + (age >= 18 ? "majeur" : "mineur") + " " + (age < 13 ? "enfant" : age < 18 ? "ado" : "adulte"));
        // Le ternaire promeut ses deux branches vers un type commun : int et double -> double.
        System.out.println("D02 : " + (true ? 1 : 2.0) + " " + (false ? 'a' : 98) + " " + (true ? 'a' : 0));
        int x = 5;
        int y = x > 3 ? x++ : x--;
        System.out.println("D03 : " + y + " " + x);
        System.out.println("D04 : " + "1" + 2 + 3 + " " + (1 + 2 + "3") + " " + ("" + 1 + 2) + " " + (1 + '2'));
        System.out.println("D05 : " + (2 + 3 * 4) + " " + ((2 + 3) * 4) + " " + (-2 * 3 + 4) + " " + (20 / 4 * 2));
        int a = 2;
        int b = a++ + a * 2;          // 2 + 3 * 2
        System.out.println("D06 : " + b + " " + a);
        boolean c = 3 + 4 > 6 && 2 * 2 == 4 || false;
        System.out.println("D07 : " + c + " " + (5 & 3 | 8) + " " + (5 & (3 | 8)));
        int z = 10;
        z += z++ + z--;               // 10 + (10 + 11)
        System.out.println("D08 : " + z + " " + (z > 30 ? z % 7 == 3 ? "trois" : "autre" : "petit"));
    }
}
