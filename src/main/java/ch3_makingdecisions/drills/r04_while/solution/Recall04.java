package ch3_makingdecisions.drills.r04_while.solution;

/**
 * SOLUTION du drill de rappel 4 - while et do/while.
 */
public class Recall04 {

    public static void main(String[] args) {
        int n = 5;
        int sum = 0;
        while (n > 0) {
            sum += n;
            n--;
        }
        System.out.println("D01 : " + sum + " " + n);
        int never = 0;
        while (never > 0) {
            never++;
        }
        int once = 0;
        do {
            once++;
        } while (once > 100);
        System.out.println("D02 : " + never + " " + once);
        int value = 1;
        int doublings = 0;
        while (value < 1000) {
            value *= 2;
            doublings++;
        }
        System.out.println("D03 : " + value + " " + doublings);
        int x = 10;
        while (x-- > 7) {
            sum = sum + x;
        }
        System.out.println("D04 : " + x + " " + sum);
        int number = 9045;
        int digits = 0;
        int digitSum = 0;
        do {
            digitSum += number % 10;
            number /= 10;
            digits++;
        } while (number != 0);
        System.out.println("D05 : " + digits + " " + digitSum);
        int zero = 0;
        int count = 0;
        do {
            zero /= 10;
            count++;
        } while (zero != 0);
        System.out.println("D06 : " + count);
        int balance = 1000;
        int years = 0;
        while (balance < 2000) {
            balance += balance * 10 / 100;
            years++;
        }
        System.out.println("D07 : " + years + " " + balance);
        int a = 0;
        int b = 0;
        while (a < 3) {
            a++;
            int c = 0;
            while (c < a) {
                c++;
                b++;
            }
        }
        System.out.println("D08 : " + a + " " + b);
    }
}
