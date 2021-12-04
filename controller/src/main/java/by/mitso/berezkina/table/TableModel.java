package by.mitso.berezkina.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import by.mitso.berezkina.domain.Persistent;

public abstract class TableModel<T extends Persistent<?>> extends AbstractTableModel {

    private final String title;
    private final List<T> elements;
    private ColumnList columnList;

    protected TableModel(String title, List<T> elements) {
        this.title = title;
        this.elements = elements;
    }

    public String getTitle() {
        return title;
    }

    public List<T> getElements() {
        return elements;
    }

    @Override
    public int getRowCount() {
        return elements.size();
    }

    @Override
    public int getColumnCount() {
        return columnList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Column column = getColumnList().getColumn(columnIndex);
        return column.getData(elements.get(rowIndex));
    }

    public Object getValueAt(Object element, Column column) {
        return column.getData(element);
    }

    public final ColumnList getColumnList() {
        if (columnList == null) {
            columnList = createColumnPropertyList();
            if (columnList == null) {
                // so no additional null checks needed
                columnList = new ColumnList();
                assert false : "No columns specified!";
            }
        }

        return columnList;
    }

    protected abstract ColumnList createColumnPropertyList();
}
