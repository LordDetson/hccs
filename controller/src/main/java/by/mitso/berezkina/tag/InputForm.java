package by.mitso.berezkina.tag;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import by.mitso.berezkina.field.InputField;
import by.mitso.berezkina.field.InputField.InputFieldType;
import by.mitso.berezkina.form.FormSubmitButton;
import by.mitso.berezkina.form.InputFormModel;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

public class InputForm extends TagSupport {

    private InputFormModel inputFormModel;

    public InputFormModel getInputFormModel() {
        return inputFormModel;
    }

    public void setInputFormModel(InputFormModel inputFormModel) {
        this.inputFormModel = inputFormModel;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.println(String.format("<h3>%s</h3>", inputFormModel.getTitle()));
            out.println(String.format("<form name=\"%s\" action=\"%s\" method=\"%s\">", inputFormModel.getName(), inputFormModel.getAction(),
                    inputFormModel.getMethod().getName()));
            Integer counter = 0;
            for(InputField field : inputFormModel.getInputFields()) {
                String inputId = field.getName() + "_" + counter;
                addInputField(out, inputId, field, counter == 0);
                counter++;
            }
            for(FormSubmitButton submitButton: inputFormModel.getSubmitButtons()) {

                out.print("<button type=\"submit\"");
                Optional<String> formAction = submitButton.getFormAction();
                if(formAction.isPresent()) {
                    out.print(String.format(" formaction=\"%s\"", formAction.get()));
                }
                out.println(String.format(" class=\"btn btn-success\">%s</button>", submitButton.getText()));
            }
            out.println("</form>");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    private void addInputField(JspWriter out, String inputId, InputField field, boolean autofocus) throws IOException {
        Set<String> values = field.getValues();
        out.println("<div class=\"mb-3 row\">");
        out.println(String.format("<label for=\"%s\" class=\"col-sm-3 col-form-label\">%s:</label>", inputId, field.getCaption()));
        out.println("<div class=\"col-sm-9\">");
        InputFieldType type = field.getType();
        if(type != InputFieldType.RADIO) {
            String value = values.isEmpty() ? "" : values.iterator().next();
            out.print(String.format("<input type=\"%s\" id=\"%s\" name=\"%s\" value=\"%s\" class=\"form-control\"", field.getType().getName(),
                            inputId, field.getName(), value));
            if(field.isRequired()) {
                out.print(" required");
            }
            if(field.isReadonly()) {
                out.print(" readonly");
            }
            if(autofocus) {
                out.print(" autofocus");
            }
            out.println(">");
        }
        else if(!values.isEmpty()) {
            int counter = 0;
            for(String value : values) {
                String radioId = inputId + "_" + value + "_" + counter;
                out.println("<div class=\"form-check\">");
                out.print(String.format("<input type=\"%s\" id=\"%s\" name=\"%s\" value=\"%s\" class=\"form-check-input\"", field.getType().getName(),
                        radioId, field.getName(), value));
                String selectedValue = field.getSelectedValue();
                if((selectedValue != null && selectedValue.equals(value)) || (selectedValue == null && field.isRequired() && counter == 0)) {
                    out.print(" checked");
                }
                out.println(">");
                out.println(String.format("<label for=\"%s\" class=\"form-check-label\">%s</label>", radioId, value));
                out.println("</div>");
                counter++;
            }
        }
        out.println("</div>");
        out.println("</div>");
    }
}
