package by.mitso.berezkina.form;

import java.util.Set;

import by.mitso.berezkina.field.InputField;

public class InputForm {

    private String title;
    private String name;
    private String action;
    private InputFormMethod method;
    private Set<InputField> inputFields;
    private String submitText;

    public enum InputFormMethod {
        GET("get"), POST("post");

        private final String name;

        InputFormMethod(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public InputForm(String title, String name, String action, Set<InputField> inputFields, String submitText) {
        this.title = title;
        this.name = name;
        this.action = action;
        this.method = InputFormMethod.POST;
        this.inputFields = inputFields;
        this.submitText = submitText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public InputFormMethod getMethod() {
        return method;
    }

    public void setMethod(InputFormMethod method) {
        this.method = method;
    }

    public Set<InputField> getInputFields() {
        return inputFields;
    }

    public void setInputFields(Set<InputField> inputFields) {
        this.inputFields = inputFields;
    }

    public String getSubmitText() {
        return submitText;
    }

    public void setSubmitText(String submitText) {
        this.submitText = submitText;
    }

    @Override
    public String toString() {
        return "InputForm{" +
                "name='" + name + '\'' +
                '}';
    }
}
