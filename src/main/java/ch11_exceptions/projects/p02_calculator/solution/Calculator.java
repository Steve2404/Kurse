package ch11_exceptions.projects.p02_calculator.solution;

import ch11_exceptions.projects.p02_calculator.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 2 - la calculatrice : traduire, chainer, relancer, et les exceptions dans les lambdas.
 */
public class Calculator {

    static int evaluations;
    static final List<String> journal = new ArrayList<>();

    public static long evaluate(String text) throws SyntaxException {
        try {
            return new Parser(text).parseAll();
        } catch (ArithmeticException | IllegalArgumentException e) {
            throw new EvaluationException("evaluation impossible", e);   // traduire en une exception du domaine
        } finally {
            evaluations++;                                                // meme apres un return ou une exception
        }
    }

    // Relance PRECISE : catch (Exception e) puis throw e ; le compilateur sait que seule SyntaxException
    // (verifiee) peut sortir, donc throws SyntaxException suffit.
    static long logged(String text) throws SyntaxException {
        try {
            return evaluate(text);
        } catch (Exception e) {
            journal.add(e.getClass().getSimpleName());
            throw e;
        }
    }

    // Les erreurs prevues deviennent un Optional vide : le stream continue.
    static Optional<Long> tryEvaluate(String text) {
        try {
            return Optional.of(evaluate(text));
        } catch (SyntaxException | EvaluationException e) {
            return Optional.empty();
        }
    }

    // Adapter une fonction qui lance une exception VERIFIEE en Function : on l'enveloppe dans une non verifiee.
    static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R> f) {
        return value -> {
            try {
                return f.apply(value);
            } catch (SyntaxException e) {
                throw new RuntimeException("dans une lambda", e);
            }
        };
    }

    public static void main(String[] args) {
        for (String text : Data.EXPRESSIONS) {
            try {
                System.out.println(text + " = " + logged(text));
            } catch (SyntaxException e) {
                System.out.println(text + " -> syntaxe : " + e.getMessage() + " (position " + e.position() + ")"
                        + (e.getCause() != null ? " <- " + e.getCause().getClass().getSimpleName() : ""));
                System.out.println(" ".repeat(e.position()) + "^");
            } catch (EvaluationException e) {
                System.out.println(text + " -> " + e.getMessage() + " <- " + e.getCause());
            }
        }

        // Une Error peut s'attraper, mais on ne le fait jamais en vrai : ici, seulement pour l'observer.
        String deep = "(".repeat(Data.DEPTH) + "1" + ")".repeat(Data.DEPTH);
        try {
            evaluate(deep);
        } catch (StackOverflowError e) {
            System.out.println("imbrication " + Data.DEPTH + " -> " + e.getClass().getSimpleName() + " (fille de " + e.getClass().getSuperclass().getSimpleName() + ")");
        } catch (SyntaxException e) {
            System.out.println("inattendu : " + e.getMessage());
        }

        List<Long> valid = Arrays.stream(Data.EXPRESSIONS).map(Calculator::tryEvaluate).flatMap(Optional::stream).toList();
        System.out.println("valides " + valid + ", somme " + valid.stream().mapToLong(Long::longValue).sum());

        List<Long> results = new ArrayList<>();
        int before = evaluations;
        try {
            Stream.of(Data.BATCH).map(unchecked(Calculator::evaluate)).forEach(results::add);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SyntaxException s) {
                System.out.println("lot interrompu : " + e.getMessage() + " <- " + s.getMessage() + " en position " + s.position() + ", resultats " + results
                        + ", evaluations " + (evaluations - before));
            }
        }
        System.out.println("evaluations " + evaluations + ", journal " + journal);
    }
}
