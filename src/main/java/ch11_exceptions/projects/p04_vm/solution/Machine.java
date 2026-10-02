package ch11_exceptions.projects.p04_vm.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SOLUTION - une machine a pile qui implemente ses PROPRES exceptions : TRY empile un gestionnaire
 * (ou aller, et la hauteur de pile a restaurer), ENDTRY le retire ; une erreur DEROULE jusqu'au
 * gestionnaire le plus recent, comme le fait la JVM avec ses blocs catch.
 */
public class Machine {

    record Handler(int target, int depth) {
    }

    private final String[] code;
    private final Map<String, Integer> labels = new HashMap<>();
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Deque<Handler> handlers = new ArrayDeque<>();
    private final List<Integer> printed = new ArrayList<>();
    private int steps;

    public Machine(String program) {
        code = program.split(";");
        for (int i = 0; i < code.length; i++) {
            if (code[i].endsWith(":")) {
                labels.put(code[i].substring(0, code[i].length() - 1), i);
            }
        }
    }

    private int pop(int index) throws StackUnderflowException {
        if (stack.isEmpty()) {
            throw new StackUnderflowException(index);
        }
        return stack.pop();
    }

    private int target(String label) {
        Integer index = labels.get(label);
        if (index == null) {
            throw new IllegalArgumentException("etiquette inconnue : " + label);
        }
        return index;
    }

    // Execute une instruction ; rend l'indice de la suivante (-1 pour HALT).
    private int step(int pc) throws VmException {
        String[] p = code[pc].split(" ");
        switch (p[0]) {
            case "PUSH" -> stack.push(Integer.parseInt(p[1]));
            case "POP" -> pop(pc);
            case "DUP" -> {
                int v = pop(pc);
                stack.push(v);
                stack.push(v);
            }
            case "ADD", "SUB", "MUL", "DIV" -> {
                int b = pop(pc);
                int a = pop(pc);
                stack.push(switch (p[0]) {
                    case "ADD" -> a + b;
                    case "SUB" -> a - b;
                    case "MUL" -> a * b;
                    default -> a / b;                                // b == 0 : ArithmeticException
                });
            }
            case "PRINT" -> printed.add(stack.isEmpty() ? null : stack.peek());
            case "TRY" -> handlers.push(new Handler(target(p[1]), stack.size()));
            case "ENDTRY" -> handlers.pop();
            case "THROW" -> throw new ThrownException(Integer.parseInt(p[1]));
            case "JMP" -> {
                return target(p[1]);
            }
            case "JZ" -> {
                if (pop(pc) == 0) {
                    return target(p[1]);
                }
            }
            case "HALT" -> {
                return -1;
            }
            default -> {
                if (!code[pc].endsWith(":")) {
                    throw new IllegalArgumentException("instruction inconnue : " + p[0]);
                }
            }
        }
        return pc + 1;
    }

    public void run(int maxSteps) throws UncaughtVmException {
        int pc = 0;
        while (pc >= 0 && pc < code.length) {
            if (++steps > maxSteps) {
                throw new StepLimitException(maxSteps);
            }
            try {
                pc = step(pc);
            } catch (VmException | ArithmeticException e) {
                if (handlers.isEmpty()) {
                    throw new UncaughtVmException("erreur non rattrapee", e);
                }
                Handler h = handlers.pop();                         // le gestionnaire le plus RECENT
                while (stack.size() > h.depth()) {
                    stack.pop();                                     // on restaure la pile du TRY
                }
                stack.push(e instanceof VmException vm ? vm.code() : -2);
                pc = h.target();
            }
        }
    }

    public String state() {
        List<Integer> bottomFirst = new ArrayList<>(stack);
        Collections.reverse(bottomFirst);
        return "affiche " + printed + ", pile " + bottomFirst + " (" + steps + " pas)";
    }
}
