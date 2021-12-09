package by.mitso.berezkina.table;

import java.util.List;

import by.mitso.berezkina.domain.Persistent;

public abstract class SelectionTableModel<T extends Persistent<?>> extends TableModel<T> {

    private final String name;
    private final String selectionAction;
    private final SelectionType type;

    public enum SelectionType {
        CHECKBOX("checkbox"), RADIO("radio");

        private final String name;

        SelectionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public SelectionTableModel(String title, List<T> elements, String name, String selectionAction,
            SelectionType type) {
        super(title, elements);
        this.name = name;
        this.selectionAction = selectionAction;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getSelectionAction() {
        return selectionAction;
    }

    public SelectionType getType() {
        return type;
    }
}
