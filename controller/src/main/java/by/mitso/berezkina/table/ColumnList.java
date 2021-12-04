package by.mitso.berezkina.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import by.mitso.berezkina.domain.Field;

public class ColumnList implements Iterable<Column> {

    private final List<Column> columns = new ArrayList<>();

    public void add(Column property) {
        assert findColumn(property) == null : "Column name already exists: " + property.getName();
        columns.add(property);
    }

    public Column add(Field field) {
        Column property = new Column(field);
        add(property);
        return property;
    }

    public void addAll(Field... fields) {
        addAll(List.of(fields));
    }

    public void addAll(Collection<? extends Field> fields) {
        for(Field field : fields) {
            add(field);
        }
    }

    public void addAllColumn(Collection<? extends Column> columns) {
        for(Column column : columns) {
            add(column);
        }
    }

    public boolean remove(Column property) {
        return columns.remove(property);
    }

    public boolean remove(Field field) {
        return removeProperty(property -> isSameColumnField(property, field));
    }

    public boolean remove(Predicate<Field> removeFieldPredicate) {
        Predicate<Column> removePropertyPredicate = columnProperty -> removeFieldPredicate.test(columnProperty.getField());
        return removeProperty(removePropertyPredicate);
    }

    private boolean removeProperty(Predicate<Column> removePropertyPredicate) {
        return columns.removeIf(removePropertyPredicate);
    }

    public void clear() {
        columns.clear();
    }

    public void add(int index, Column property) {
        if(findColumn(property) == null) {
            columns.add(index, property);
        }
    }

    public Column add(int index, Field field) {
        Column column = new Column(field);
        add(index, column);
        return column;
    }

    public void addAll(ColumnList list) {
        for(Column prop : list.getColumns()) {
            add(prop);
        }
    }

    public Column findColumn(Column column) {
        if(column == null) {
            return null;
        }
        return findColumnByID(column.getName());
    }

    public Column findColumnByField(Field field) {
        return findColumnByField(columns, field);
    }

    protected Column findColumnByField(List<Column> columns, Field field) {
        if(field == null) {
            return null;
        }

        for(Column column : columns) {
            if(isSameColumnField(column, field)) {
                return column;
            }
        }
        return null;
    }

    private boolean isSameColumnField(Column column, Field field) {
        return column.getField().equals(field) || column.getName().equals(field.getName());
    }

    public Column findColumnByID(String id) {
        return findColumnByID(columns, id);
    }

    protected Column findColumnByID(List<Column> properties, String id) {
        for(Column prop : properties) {
            if(prop.getName().equalsIgnoreCase(id)) {
                return prop;
            }
        }
        return null;
    }

    public Column getColumn(int index) {
        if((index > -1) && (index < columns.size())) {
            return columns.get(index);
        }
        return null;
    }

    public int indexOf(Column column) {
        return columns.indexOf(column);
    }

    public int size() {
        return columns.size();
    }

    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public Iterator<Column> iterator() {
        return columns.iterator();
    }

    public List<Field> toFieldList() {
        List<Field> fields = new ArrayList<>();
        for(Column column : this) {
            fields.add(column.getField());
        }
        return fields;
    }
}
