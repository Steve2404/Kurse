package ch11_exceptions.projects.p04_vm;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier).
 * Chaque programme : "nom|instruction;instruction;...". Une instruction qui finit par ':' est une etiquette.
 */
public final class Data {

    public static final String[] PROGRAMS = {
            "calcul|PUSH 6;PUSH 7;MUL;PRINT;HALT",
            "division rattrapee|TRY err;PUSH 1;PUSH 0;DIV;ENDTRY;PRINT;HALT;err:;PRINT;HALT",
            "throw imbrique|TRY outer;TRY inner;PUSH 5;THROW 7;ENDTRY;inner:;PRINT;THROW 9;outer:;PRINT;HALT",
            "compte a rebours|PUSH 3;loop:;PRINT;PUSH 1;SUB;DUP;JZ end;JMP loop;end:;HALT",
            "pile vide|PUSH 1;ADD;HALT",
            "throw perdu|PUSH 2;THROW 4;HALT",
            "boucle infinie|top:;JMP top",
            "instruction inconnue|PUSH 1;SQUARE;HALT",
            "pile vide rattrapee|PUSH 8;TRY oops;POP;POP;ENDTRY;oops:;PRINT;HALT"};

    /** Le nombre maximal d'instructions executees par programme. */
    public static final int MAX_STEPS = 50;

    private Data() {
    }
}
