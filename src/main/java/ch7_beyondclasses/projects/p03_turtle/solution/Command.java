package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - une interface SCELLEE : seules les classes listees dans permits peuvent l'implementer.
 * Chaque sous-type direct doit etre final (les records le sont), sealed ou non-sealed.
 */
public sealed interface Command permits Move, Turn, Repeat, PenUp, PenDown, Macro {

    // Le nombre de commandes elementaires (Move, Turn, PenUp, PenDown) une fois tout deplie.
    int size();
}
