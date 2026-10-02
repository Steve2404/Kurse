package ch5_methods.projects.p05_overload;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON OverloadLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "show : byte int, short int, char int, int int, long long, float double",
            "show : Integer Integer, Short Object, String Object, rien int..., 1,2 int..., int[] int...",
            "box : 5 long, Integer Integer, 5.0 Object, 'c' long",
            "pick : 5 Object, 5L Long, null Long",
            "text : \"a\" String, builder CharSequence, null String, Object \"z\" Object, cast CharSequence",
            "sum : 1,2 int,int, 1,2,3 int... ; add : 1,2 long,long, Integer Integer,Integer ; twice 42 abab",
            "42 true 2.5 \"il dit \\\"oui\\\"\\\\non\"",
            "[1,2,3] [[1,2],[],[3]] [\"a\",\"b\\\"c\"]",
            "type declare Object : \"7\" 7 \"sept\" null",
            "[1,\"deux\",3.0,false,null,[4,5],[\"six\"]]",
            "{\"nom\":\"Ada\",\"age\":36,\"langages\":[\"Java\",\"C\"],\"notes\":[[18,15],[12]],\"adresse\":{\"ville\":\"Paris\"},\"vide\":{}}");
            // EXPECTED-END

    static final List<String> API = List.of(
            "6xstatic String show(", "3xstatic String box(", "2xstatic String pick(", "3xstatic String text(",
            "show(int... ", "pick(Long ", "text(CharSequence ", "Short ",
            "7xstatic String toJson(", "toJson(Object ", "instanceof ", "String... ",
            "Object... ", "re:new \\w+\\(\\)\\.\\w+\\(##methode non static appelee sur new X()",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "OverloadLab", args, EXPECTED, API);
    }
}
