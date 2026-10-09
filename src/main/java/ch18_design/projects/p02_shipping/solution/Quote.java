package ch18_design.projects.p02_shipping.solution;

/** Un devis : un transporteur et son prix final. */
public record Quote(String carrier, long priceCents) {
}
