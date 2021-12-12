package by.mitso.berezkina.table;

import java.util.List;
import java.util.function.Predicate;

import by.mitso.berezkina.domain.Persistent;

public abstract class CrudTableModel<T extends Persistent<?>> extends TableModel<T> {

    private String createAction;
    private String editAction;
    private String deleteAction;

    private boolean canCreate = true;
    private Predicate<T> canEdit = t -> true;
    private Predicate<T> canDelete = t -> true;

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

    public boolean canCreate() {
        return createAction != null && canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean canEdit(T element) {
        return editAction != null && canEdit.test(element);
    }

    public void setCanEdit(Predicate<T> canEdit) {
        this.canEdit = canEdit;
    }

    public boolean canDelete(T element) {
        return deleteAction != null && canDelete.test(element);
    }

    public void setCanDelete(Predicate<T> canDelete) {
        this.canDelete = canDelete;
    }
}
