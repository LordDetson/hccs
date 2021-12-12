package by.mitso.berezkina.table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import by.mitso.berezkina.domain.Persistent;

public abstract class TableModel<T extends Persistent<?>> extends AbstractTableModel {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
        return getValueAt(elements.get(rowIndex), column);
    }

    public Object getValueAt(T element, Column column) {
        Object data = column.getData(element);
        if(data instanceof LocalDate) {
            data = ((LocalDate) data).format(DATE_FORMATTER);
        }
        return data;
    }

    public final ColumnList getColumnList() {
        if (columnList == null) {
            columnList = createColumnList();
            if (columnList == null) {
                // so no additional null checks needed
                columnList = new ColumnList();
                assert false : "No columns specified!";
            }
        }

        return columnList;
    }

    protected abstract ColumnList createColumnList();

    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
