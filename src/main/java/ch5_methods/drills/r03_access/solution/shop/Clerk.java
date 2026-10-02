package ch5_methods.drills.r03_access.solution.shop;

/**
 * SOLUTION - un vendeur du MEME paquet : il voit le protected et le package-private.
 */
public class Clerk {

    public static String report(Item item) {
        item.restock(1);
        return item.code + " " + item.stock + " " + Item.label();
    }
}
