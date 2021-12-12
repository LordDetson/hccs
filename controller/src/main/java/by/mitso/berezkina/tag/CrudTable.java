package by.mitso.berezkina.tag;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.domain.Persistent;
import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.CrudTableModel;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

public class CrudTable extends TagSupport {

    private CrudTableModel<Persistent<?>> crudTableModel;

    public CrudTableModel<Persistent<?>> getCrudTableModel() {
        return crudTableModel;
    }

    public void setCrudTableModel(CrudTableModel<Persistent<?>> crudTableModel) {
        this.crudTableModel = crudTableModel;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            List<Column> columns = crudTableModel.getColumnList().getColumns();
            List<Persistent<?>> rows = crudTableModel.getElements();
            out.println(String.format("<h3>%s</h3>", crudTableModel.getTitle()));
            if(crudTableModel.canCreate()) {
                String createAction = crudTableModel.getCreateAction();
                if(createAction != null) {
                    out.println(String.format("<a href=\"%s\" class=\"btn btn-success\">Создать</a>", createAction));
                }
            }
            out.println("<table class=\"table table-striped\">");
            out.println("<thead>");
            out.println("<tr>");
            for(Column column : columns) {
                out.println(String.format("<th scope=\"col\">%s</th>", StringUtils.capitalize(column.getCaption())));
            }
            String editAction = crudTableModel.getEditAction();
            String deleteAction = crudTableModel.getDeleteAction();
            if((editAction != null || deleteAction != null) && !rows.isEmpty() &&
                    rows.stream().anyMatch(row -> crudTableModel.canEdit(row) || crudTableModel.canDelete(row))) {
                out.println("<th scope=\"col\">Действия</th>");
            }
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            for(Persistent<?> row : rows) {
                out.println("<tr>");
                for(Column column : columns) {
                    Object value = crudTableModel.getValueAt(row, column);
                    out.println(String.format("<td>%s</td>", value != null ? value : ""));
                }
                if((editAction != null || deleteAction != null)) {
                    Serializable id = row.getId();
                    if(id != null) {
                        out.println("<td>");
                        out.println("<div class=\"btn-group\">");
                        if(editAction != null && crudTableModel.canEdit(row)) {
                            out.println(String.format("<a href=\"%s?id=%s\" class=\"btn btn-primary btn-sm\">Редактировать</a>", editAction, id));
                        }
                        if(deleteAction != null && crudTableModel.canDelete(row)) {
                            out.println(String.format("<a href=\"%s?id=%s\" class=\"btn btn-danger btn-sm\">Удалить</a>", deleteAction, id));
                        }
                        out.println("</div>");
                        out.println("</td>");
                    }
                }
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
