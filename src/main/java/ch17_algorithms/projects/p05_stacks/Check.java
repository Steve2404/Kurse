package ch17_algorithms.projects.p05_stacks;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Calculator.java", "        return open.isEmpty();", "        return true;"),
            new Mutant("Calculator.java", "if (open.isEmpty() || !OPENING.get(c).equals(open.pop())) {", "if (open.isEmpty() || open.pop() == null) {"),
            new Mutant("Calculator.java", "long right = stack.pop();\n                long left = stack.pop();", "long left = stack.pop();\n                long right = stack.pop();"),
            new Mutant("Calculator.java", "if (stack.size() != 1) {", "if (stack.isEmpty()) {"),
            new Mutant("Calculator.java", "PRIORITY.get(operators.peek()) >= PRIORITY.get(op)", "PRIORITY.get(operators.peek()) > PRIORITY.get(op)"),
            new Mutant("Calculator.java", "Map.of(\"+\", 1, \"-\", 1, \"*\", 2, \"/\", 2)", "Map.of(\"+\", 1, \"-\", 1, \"*\", 1, \"/\", 1)"),
            new Mutant("Calculator.java", "                if (operators.isEmpty()) {\n                    throw new IllegalArgumentException(\"parentheses desequilibrees\");\n                }\n                operators.pop();",
                    "                if (!operators.isEmpty()) {\n                    operators.pop();\n                }"),
            new Mutant("Monotonic.java", "temps[waiting.peek()] < temps[day]", "temps[waiting.peek()] <= temps[day]"),
            new Mutant("Monotonic.java", "for (int i = 0; i <= heights.length; i++) {", "for (int i = 0; i < heights.length; i++) {"),
            new Mutant("Monotonic.java", "best = Math.max(best, (long) height * (i - left - 1));", "best = Math.max(best, height * (i - left - 1));"),
            new Mutant("MinStack.java", "minimums.push(minimums.isEmpty() ? v : Math.min(v, minimums.peek()));", "if (minimums.isEmpty() || v < minimums.peek()) {\n            minimums.push(v);\n        }"),
            new Mutant("QueueFromStacks.java", "        if (out.isEmpty()) {\n            while (!in.isEmpty()) {", "        {\n            while (!in.isEmpty()) {"),
            new Mutant("QueueFromStacks.java", "return in.size() + out.size();", "return in.size();"));

    static final List<String> API_CODE = List.of(
            "final class Calculator", "static boolean balanced(String s)", "static long evalRpn(String expr)",
            "static List<String> toRpn(String infix)", "static long evaluate(String infix)",
            "final class Monotonic", "static int[] daysUntilWarmer(int[] temps)", "static long largestRectangle(int[] heights)",
            "final class MinStack", "void push(int v)", "int pop()", "int peek()", "int min()",
            "final class QueueFromStacks<T>", "void offer(T value)", "T poll()", "T peek()",
            "Deque<", "ArrayDeque",
            // La vieille classe Stack est deconseillee : Deque la remplace.
            "!java.util.Stack", "!new Stack", "!ScriptEngine");

    static final List<String> API_TESTS = List.of(
            "@Test", "@ParameterizedTest", "assertEquals(", "assertThrows(", "assertTimeoutPreemptively(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 40, MUTANTS, API_CODE, API_TESTS);
    }
}
