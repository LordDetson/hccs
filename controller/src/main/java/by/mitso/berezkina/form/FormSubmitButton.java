package by.mitso.berezkina.form;

import java.util.Objects;
import java.util.Optional;

public class FormSubmitButton {

    private String text;
    private String formAction;

    public FormSubmitButton(String text) {
        this(text, null);
    }

    public FormSubmitButton(String text, String formAction) {
        this.text = text;
        this.formAction = formAction;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        Objects.requireNonNull(text);
        this.text = text;
    }

    public Optional<String> getFormAction() {
        return Optional.ofNullable(formAction);
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        FormSubmitButton that = (FormSubmitButton) o;
        return text.equals(that.text) && Objects.equals(formAction, that.formAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, formAction);
    }

    @Override
    public String toString() {
        return "FormButton{" +
                "text='" + text + '\'' +
                ", formAction='" + formAction + '\'' +
                '}';
    }
}
