package ch18_design.projects.p07_editor.solution;

/** Ce qui vient de changer : "insert", "delete" ou "restore", ou, et quel texte (insere, retire, ou remis). */
public record DocumentEvent(String kind, int position, String text) {
}
