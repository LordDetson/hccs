package by.mitso.berezkina.tag;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.TableModel;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

public class TableTag extends TagSupport {

    private TableModel<?> tableModel;

    public TableModel<?> getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel<?> tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            List<Column> columns = tableModel.getColumnList().getColumns();
            List<?> rows = tableModel.getElements();
            out.println("<table class=\"table table-striped\">");
            out.println("<thead>");
            out.println("<tr>");
            for(Column column : columns) {
                out.println(String.format("<th scope=\"col\">%s</th>", StringUtils.capitalize(column.getCaption())));
            }
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            for(Object row : rows) {
                out.println("<tr>");
                for(Column column : columns) {
                    out.println(String.format("<td>%s</td>", tableModel.getValueAt(row, column)));
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
