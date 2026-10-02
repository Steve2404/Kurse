package ch5_methods.projects.p05_overload.solution;

/**
 * SOLUTION du projet 5 - le laboratoire des surcharges.
 */
public class OverloadLab {

    public static void main(String[] args) {
        byte b = 1;
        short sh = 2;
        char c = 'a';
        float f = 1.5f;
        Short boxedShort = 3;
        System.out.println("show : byte " + Printer.show(b) + ", short " + Printer.show(sh) + ", char " + Printer.show(c) + ", int " + Printer.show(5)
                + ", long " + Printer.show(5L) + ", float " + Printer.show(f));
        System.out.println("show : Integer " + Printer.show(Integer.valueOf(5)) + ", Short " + Printer.show(boxedShort) + ", String " + Printer.show("x")
                + ", rien " + Printer.show() + ", 1,2 " + Printer.show(1, 2) + ", int[] " + Printer.show(new int[] {1}));
        System.out.println("box : 5 " + Printer.box(5) + ", Integer " + Printer.box(Integer.valueOf(5)) + ", 5.0 " + Printer.box(5.0) + ", 'c' "
                + Printer.box('c'));
        System.out.println("pick : 5 " + Printer.pick(5) + ", 5L " + Printer.pick(5L) + ", null " + Printer.pick(null));
        StringBuilder sb = new StringBuilder("y");
        Object hidden = "z";
        System.out.println("text : \"a\" " + Printer.text("a") + ", builder " + Printer.text(sb) + ", null " + Printer.text(null) + ", Object \"z\" "
                + Printer.text(hidden) + ", cast " + Printer.text((CharSequence) "a"));
        System.out.println("sum : 1,2 " + Printer.sum(1, 2) + ", 1,2,3 " + Printer.sum(1, 2, 3) + " ; add : 1,2 " + Printer.add(1, 2) + ", Integer "
                + Printer.add(Integer.valueOf(1), Integer.valueOf(2)) + " ; twice " + Printer.twice(21) + " " + new Printer().twice("ab"));

        System.out.println(Json.toJson(42) + " " + Json.toJson(true) + " " + Json.toJson(2.5) + " " + Json.toJson("il dit \"oui\"\\non"));
        System.out.println(Json.toJson(new int[] {1, 2, 3}) + " " + Json.toJson(new int[][] {{1, 2}, {}, {3}}) + " " + Json.toJson(new String[] {"a", "b\"c"}));
        Object number = 7;
        Object text = "sept";
        System.out.println("type declare Object : " + Json.naive(number) + " " + Json.toJson(number) + " " + Json.toJson(text) + " " + Json.toJson((Object) null));
        System.out.println(Json.array(1, "deux", 3.0, false, null, new int[] {4, 5}, new String[] {"six"}));
        System.out.println(Json.object(Json.field("nom", Json.toJson("Ada")), Json.field("age", Json.toJson(36)),
                Json.field("langages", Json.toJson(new String[] {"Java", "C"})), Json.field("notes", Json.toJson(new int[][] {{18, 15}, {12}})),
                Json.field("adresse", Json.object(Json.field("ville", Json.toJson("Paris")))), Json.field("vide", Json.object())));
    }
}
