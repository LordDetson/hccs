package by.mitso.berezkina.tag;

import java.io.IOException;

import by.mitso.berezkina.field.InputField;
import by.mitso.berezkina.form.InputForm;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

public class InputFormTag extends TagSupport {

    private InputForm inputForm;

    public InputForm getInputForm() {
        return inputForm;
    }

    public void setInputForm(InputForm inputForm) {
        this.inputForm = inputForm;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.println(String.format("<h3>%s</h3>", inputForm.getTitle()));
            out.println(String.format("<form name=\"%s\" action=\"%s\" method=\"%s\">", inputForm.getName(), inputForm.getAction(),
                    inputForm.getMethod().getName()));
            int counter = 0;
            for(InputField field : inputForm.getInputFields()) {
                String inputId = field.getName() + "_" + counter;
                out.println("<div class=\"mb-3 row\">");
                out.println(String.format("<label for=\"%s\" class=\"col-sm-3 col-form-label\">%s:</label>", inputId, field.getCaption()));
                out.println("<div class=\"col-sm-9\">");
                out.print(String.format("<input type=\"%s\" id=\"%s\" name=\"%s\" value=\"%s\" class=\"form-control\"", field.getType().getName(), inputId, field.getName(), field.getValue()));
                if(field.isRequired()) {
                    out.print(" required");
                }
                if(counter == 0) {
                    out.print(" autofocus");
                }
                out.println(">");
                out.println("</div>");
                out.println("</div>");
                counter++;
            }
            out.println(String.format("<button type=\"submit\" class=\"btn btn-success\">%s</button>", inputForm.getSubmitText()));
            out.println("</form>");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
