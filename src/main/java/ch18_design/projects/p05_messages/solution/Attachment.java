package ch18_design.projects.p05_messages.solution;

/** Une piece jointe : un nom et une taille en kilo-octets (au moins 1). */
public record Attachment(String name, int sizeKb) {

    public Attachment {
        if (sizeKb < 1) {
            throw new IllegalArgumentException("taille invalide : " + sizeKb);
        }
    }

    @Override
    public String toString() {
        return name + " (" + sizeKb + " Ko)";
    }
}
