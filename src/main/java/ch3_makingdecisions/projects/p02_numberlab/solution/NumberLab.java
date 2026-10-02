package ch3_makingdecisions.projects.p02_numberlab.solution;

/**
 * SOLUTION du projet 2 - une conception possible.
 */
public class NumberLab {

    static String primes(int limit) {
        String result = "";
        // Etiquette : continue outer passe directement au candidat SUIVANT, depuis la boucle interieure.
        outer:
        for (int n = 2; n <= limit; n++) {
            for (int d = 2; d * d <= n; d++) {
                if (n % d == 0) {
                    continue outer;
                }
            }
            result = result + " " + n;
        }
        return result;
    }

    static String perfects(int limit) {
        String result = "";
        for (int n = 2; n <= limit; n++) {
            int sum = 1;
            for (int d = 2; d * d <= n; d++) {
                if (n % d == 0) {
                    sum += d;
                    int pair = n / d;
                    if (pair != d) {
                        sum += pair;
                    }
                }
            }
            if (sum == n) {
                result = result + " " + n;
            }
        }
        return result;
    }

    // while : la condition est testee AVANT chaque tour ; pour n = 1, aucun tour.
    static int collatzSteps(long n) {
        int steps = 0;
        while (n != 1) {
            n = n % 2 == 0 ? n / 2 : 3 * n + 1;
            steps++;
        }
        return steps;
    }

    // do/while : le corps s'execute AU MOINS une fois ; ainsi reverse(0) traite bien le chiffre 0.
    static int reverse(int n) {
        int reversed = 0;
        do {
            reversed = reversed * 10 + n % 10;
            n /= 10;
        } while (n > 0);
        return reversed;
    }

    static boolean armstrong(int n) {
        int sum = 0;
        for (int rest = n; rest > 0; rest /= 10) {
            int digit = rest % 10;
            sum += digit * digit * digit;
        }
        return sum == n;
    }

    public static void main(String[] args) {
        int limit = Integer.parseInt(args[0]);
        System.out.println("PREMIERS <= " + limit + " :" + primes(limit));
        System.out.println("PARFAITS <= 10000 :" + perfects(10_000));

        int bestStart = 1;
        int bestSteps = 0;
        for (int start = 1; start < limit; start++) {
            int steps = collatzSteps(start);
            if (steps > bestSteps) {
                bestSteps = steps;
                bestStart = start;
            }
        }
        System.out.println("COLLATZ < " + limit + " : depart " + bestStart + ", " + bestSteps + " etapes");

        String palindromes = "";
        for (int n = 100; n <= 200; n++) {
            if (n == reverse(n)) {
                palindromes = palindromes + " " + n;
            }
        }
        System.out.println("PALINDROMES 100..200 :" + palindromes);

        String armstrongs = "";
        for (int n = 100; n < 1000; n++) {
            if (armstrong(n)) {
                armstrongs = armstrongs + " " + n;
            }
        }
        System.out.println("ARMSTRONG 3 chiffres :" + armstrongs);

        int a = 1071;
        int b = 462;
        int rounds = 0;
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
            rounds++;
        }
        System.out.println("PGCD(1071, 462) = " + a + " en " + rounds + " divisions, PPCM = " + 1071 / a * 462);

        // break etiquete : sort des DEUX boucles des que le couple est trouve.
        int target = 391;
        String pair = "aucun";
        search:
        for (int x = 2; x < target; x++) {
            for (int y = x; y < target; y++) {
                if (x * y == target) {
                    pair = x + " x " + y;
                    break search;
                }
                if (x * y > target) {
                    break;
                }
            }
        }
        System.out.println("FACTEURS de " + target + " : " + pair);

        String fizz = "";
        for (int i = 1; i <= 15; i++) {
            String word = switch ((i % 3 == 0 ? 1 : 0) + (i % 5 == 0 ? 2 : 0)) {
                case 1 -> "Fizz";
                case 2 -> "Buzz";
                case 3 -> "FizzBuzz";
                default -> "" + i;
            };
            fizz = fizz + " " + word;
        }
        System.out.println("FIZZBUZZ :" + fizz);
    }
}
