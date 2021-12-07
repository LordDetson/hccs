package by.mitso.berezkina.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import by.mitso.berezkina.field.InputField;

public class InputFormModel {

    private String title;
    private String name;
    private String action;
    private InputFormMethod method;
    private Set<InputField> inputFields;
    private List<FormSubmitButton> submitButtons = new ArrayList<>();

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

    public InputFormModel(String name, String action, Set<InputField> inputFields, String submitText) {
        this(null, name, action, inputFields, submitText);
    }

    public InputFormModel(String title, String name, String action, Set<InputField> inputFields, String submitText) {
        this.title = title;
        this.name = name;
        this.action = action;
        this.method = InputFormMethod.POST;
        this.inputFields = inputFields;
        this.submitButtons.add(new FormSubmitButton(submitText));
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

    public List<FormSubmitButton> getSubmitButtons() {
        return submitButtons;
    }

    public void setSubmitButtons(List<FormSubmitButton> submitButtons) {
        this.submitButtons = submitButtons;
    }

    @Override
    public String toString() {
        return "InputForm{" +
                "name='" + name + '\'' +
                '}';
    }
}
