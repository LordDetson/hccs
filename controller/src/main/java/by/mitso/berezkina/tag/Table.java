package by.mitso.berezkina.tag;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.domain.Persistent;
import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.TableModel;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

public class Table extends TagSupport {

    private TableModel<Persistent<?>> tableModel;

    public TableModel<Persistent<?>> getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel<Persistent<?>> tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            List<Column> columns = tableModel.getColumnList().getColumns();
            List<Persistent<?>> rows = tableModel.getElements();
            out.println(String.format("<h3>%s</h3>", tableModel.getTitle()));
            out.println("<table class=\"table table-striped\">");
            out.println("<thead>");
            out.println("<tr>");
            for(Column column : columns) {
                out.println(String.format("<th scope=\"col\">%s</th>", StringUtils.capitalize(column.getCaption())));
            }
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            for(Persistent<?> row : rows) {
                out.println("<tr>");
                for(Column column : columns) {
                    Object value = tableModel.getValueAt(row, column);
                    out.println(String.format("<td>%s</td>", value != null ? value : ""));
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
