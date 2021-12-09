package by.mitso.berezkina.table;

import java.util.List;

import by.mitso.berezkina.domain.Persistent;

public abstract class CrudTableModel<T extends Persistent<?>> extends TableModel<T> {

    private String createAction;
    private String editAction;
    private String deleteAction;

    protected CrudTableModel(String title, List<T> elements) {
        super(title, elements);
    }

    public String getCreateAction() {
        return createAction;
    }

    public void setCreateAction(String createAction) {
        this.createAction = createAction;
    }

    public String getEditAction() {
        return editAction;
    }

    public void setEditAction(String editAction) {
        this.editAction = editAction;
    }

    public String getDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(String deleteAction) {
        this.deleteAction = deleteAction;
    }
}
