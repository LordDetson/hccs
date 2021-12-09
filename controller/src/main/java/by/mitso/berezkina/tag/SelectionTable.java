package by.mitso.berezkina.tag;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.domain.Persistent;
import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.SelectionTableModel;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

public class SelectionTable extends TagSupport {

    private SelectionTableModel<Persistent<?>> selectionTableModel;

    public SelectionTableModel<Persistent<?>> getSelectionTableModel() {
        return selectionTableModel;
    }

    public void setSelectionTableModel(SelectionTableModel<Persistent<?>> selectionTableModel) {
        this.selectionTableModel = selectionTableModel;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            List<Column> columns = selectionTableModel.getColumnList().getColumns();
            List<Persistent<?>> rows = selectionTableModel.getElements();
            out.println(String.format("<h3>%s</h3>", selectionTableModel.getTitle()));
            out.println(String.format("<form name=\"%s\" action=\"%s\" method=\"POST\">", selectionTableModel.getName(),
                    selectionTableModel.getSelectionAction()));
            out.println("<button type=\"submit\" class=\"btn btn-primary\">Выбрать</button>");
            out.println("<table class=\"table table-striped\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th scope=\"col\">#</th>");
            for(Column column : columns) {
                out.println(String.format("<th scope=\"col\">%s</th>", StringUtils.capitalize(column.getCaption())));
            }
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            int counter = 0;
            for(Persistent<?> row : rows) {
                String inputId = selectionTableModel.getName() + "_" + counter++;
                out.println("<tr>");
                out.println("<td>");
                out.println("<div class=\"form-check\">");
                out.println(String.format("<input type=\"%s\" id=\"%s\" name=\"selections\" value=\"%s\" class=\"form-check-input\">",
                        selectionTableModel.getType().getName(), inputId, row.getId()));
                out.println("</div>");
                out.println("</td>");
                for(Column column : columns) {
                    out.println(String.format("<td>%s</td>", selectionTableModel.getValueAt(row, column)));
                }
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</form>");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
