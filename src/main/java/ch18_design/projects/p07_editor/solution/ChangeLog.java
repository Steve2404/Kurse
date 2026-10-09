package ch18_design.projects.p07_editor.solution;

import java.util.ArrayList;
import java.util.List;

/** Un observateur : il note chaque evenement, par exemple "insert 0 'Bonjour'". */
public final class ChangeLog implements DocumentListener {

    private final List<String> entries = new ArrayList<>();

    @Override
    public void changed(DocumentEvent event) {
        entries.add(event.kind() + " " + event.position() + " '" + event.text() + "'");
    }

    public List<String> entries() {
        return List.copyOf(entries);
    }
}
