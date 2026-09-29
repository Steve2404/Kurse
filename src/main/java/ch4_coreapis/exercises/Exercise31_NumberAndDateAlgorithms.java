package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * EXERCICE 31 - Algorithmique sur les nombres et les dates : premiers, crible, PGCD, chiffres, Fibonacci, jour de la semaine (niveau : avance)
 * ==========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise17_MathReturnTypes.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   Fibonacci : fib(46) = 1836311903 tient dans un int ; fib(47) deborde (Math.addExact le detecte)
 *   Math.floorMod(-7, 3) -> 2 alors que -7 % 3 -> -1
 *   LocalDate.of(2000, 1, 1).getDayOfWeek() -> SATURDAY ; LocalDate.of(1970, 1, 1) -> THURSDAY
 *
 *
 * ==================================================================
 * TODO 1 : isPrime(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un nombre premier n'a que deux diviseurs : 1 et lui-meme. Inutile de
 * tester au-dela de la racine : si n = a x b, l'un des deux est <= racine(n).
 *
 * -- Essayons a la main --
 *
 *   1 -> false ; 2 -> true ; 97 -> true ; 91 = 7 x 13 -> false
 *
 * -- Le plan --
 *
 *   1. n < 2 -> false.
 *   2. Pour i = 2 tant que i * i <= n : si n % i == 0 -> false.
 *   3. true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : primesUpTo(n)    [crible d'Eratosthene]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On ecrit tous les nombres, puis pour chaque premier on raye tous ses
 * multiples. Ce qui n'est pas raye est premier. Un boolean[] sert de
 * "cahier de ratures".
 *
 * -- Essayons a la main --
 *
 *   30 -> {2, 3, 5, 7, 11, 13, 17, 19, 23, 29}
 *
 * -- Le plan --
 *
 *   1. crossed = new boolean[n + 1] (tout a false par defaut).
 *   2. Pour i de 2 tant que i * i <= n : si !crossed[i], rayer i * i, i * i + i, ...
 *   3. Compter les non rayes (>= 2), les copier dans un int[] de la bonne taille.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : gcd(a, b)    et    TODO 4 : lcm(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Euclide : le PGCD de a et b est celui de b et du reste a % b. Quand le
 * reste vaut 0, l'autre nombre est la reponse. Le PPCM se deduit :
 * a / pgcd * b (diviser AVANT de multiplier evite de deborder).
 *
 * -- Essayons a la main --
 *
 *   gcd(84, 36) : (84, 36) -> (36, 12) -> (12, 0) -> 12      lcm(4, 6) = 4 / 2 * 6 = 12
 *
 * -- Le plan --
 *
 *   1. gcd : tant que b != 0 : r = a % b ; a = b ; b = r. Rendre a.
 *   2. lcm : a / gcd(a, b) * b.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : lcm reutilise gcd.
 *
 *
 * ==================================================================
 * TODO 5 : digitSum(n)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   9875 -> 9 + 8 + 7 + 5 = 29      -123 -> 6      0 -> 0
 *
 * -- Le plan --
 *
 *   1. n = Math.abs(n) ; tant que n > 0 : sum += n % 10 ; n /= 10.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : fibonacciOrMinusOne(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * fib(0) = 0, fib(1) = 1, puis chaque nombre est la somme des deux
 * precedents. Les nombres grandissent vite : des que le resultat ne
 * tient plus dans un int, on rend -1. Math.addExact crie au lieu de
 * rendre un nombre faux.
 *
 * -- Essayons a la main --
 *
 *   fib(10) = 55 ; fib(46) = 1836311903 ; fib(47) -> -1
 *
 * -- Le plan --
 *
 *   1. n == 0 -> 0.
 *   2. a = 0 ; b = 1 ; n - 1 fois : next = Math.addExact(a, b) ; a = b ; b = next. Rendre b.
 *   3. catch (ArithmeticException e) -> -1.
 *
 *   Piege : une boucle de n tours qui rend a calcule fib(n + 1) en trop ; pour n = 46,
 *   ce terme de trop deborde alors que fib(46) tient dans un int.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : dayOfWeek(year, month, day)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * SANS java.time : calculer le jour de la semaine (1 = lundi ... 7 =
 * dimanche, comme DayOfWeek.getValue()). Methode de Sakamoto : une
 * table de decalage par mois, et les annees bissextiles comptees avec
 * y / 4 - y / 100 + y / 400. main() compare avec LocalDate pour CHAQUE
 * jour de 1900 a 2100 (plus de 73 000 dates).
 *
 * -- Le plan --
 *
 *   1. t = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4} ; si month < 3, year--.
 *   2. w = (year + year / 4 - year / 100 + year / 400 + t[month - 1] + day) % 7  (0 = dimanche).
 *   3. Rendre w == 0 ? 7 : w.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - i * i <= n evite Math.sqrt (et ses double).
 *   - Arrays.copyOf(tableau, compte) coupe un tableau trop grand.
 */
public class Exercise31_NumberAndDateAlgorithms {

    public static boolean isPrime(int n) {
        throw new UnsupportedOperationException("TODO 1 : implementer isPrime()");
    }

    public static int[] primesUpTo(int n) {
        throw new UnsupportedOperationException("TODO 2 : implementer primesUpTo()");
    }

    public static int gcd(int a, int b) {
        throw new UnsupportedOperationException("TODO 3 : implementer gcd()");
    }

    public static int lcm(int a, int b) {
        throw new UnsupportedOperationException("TODO 4 : implementer lcm()");
    }

    public static int digitSum(int n) {
        throw new UnsupportedOperationException("TODO 5 : implementer digitSum()");
    }

    public static int fibonacciOrMinusOne(int n) {
        throw new UnsupportedOperationException("TODO 6 : implementer fibonacciOrMinusOne()");
    }

    public static int dayOfWeek(int year, int month, int day) {
        throw new UnsupportedOperationException("TODO 7 : implementer dayOfWeek()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("isPrime : 1 non, 2 oui, 97 oui, 91 non", !isPrime(1) && isPrime(2) && isPrime(97) && !isPrime(91));
        boolean sieveOk = Arrays.equals(primesUpTo(30), new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29});
        int[] primes = primesUpTo(1000);
        int count = 0;
        for (int i = 0; i <= 1000; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        ExerciseChecker.check("primesUpTo(30), et le crible est d'accord avec isPrime jusqu'a 1000 (168 premiers)",
                sieveOk && primes.length == count && count == 168);
        ExerciseChecker.check("gcd(84, 36) == 12, gcd(17, 5) == 1", gcd(84, 36) == 12 && gcd(17, 5) == 1);
        ExerciseChecker.check("lcm(4, 6) == 12, lcm(21, 6) == 42", lcm(4, 6) == 12 && lcm(21, 6) == 42);
        ExerciseChecker.check("digitSum : 29, 6, 0", digitSum(9875) == 29 && digitSum(-123) == 6 && digitSum(0) == 0);
        ExerciseChecker.check("fibonacciOrMinusOne : 0, 55, 1836311903, -1",
                fibonacciOrMinusOne(0) == 0 && fibonacciOrMinusOne(10) == 55 && fibonacciOrMinusOne(46) == 1836311903
                        && fibonacciOrMinusOne(47) == -1);

        int wrong = 0;
        for (LocalDate d = LocalDate.of(1900, 1, 1); d.getYear() <= 2100; d = d.plusDays(1)) {
            if (dayOfWeek(d.getYear(), d.getMonthValue(), d.getDayOfMonth()) != d.getDayOfWeek().getValue()) {
                wrong++;
            }
        }
        ExerciseChecker.check("dayOfWeek == LocalDate.getDayOfWeek() pour chaque jour de 1900 a 2100 (erreurs : " + wrong + ")", wrong == 0);

        ExerciseChecker.summary();
    }
}
