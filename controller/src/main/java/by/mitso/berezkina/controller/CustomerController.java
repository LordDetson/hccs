package by.mitso.berezkina.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import by.mitso.berezkina.access.AccessChecker;
import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Customer.CustomerField;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Gender;
import by.mitso.berezkina.factor.CustomerFactory;
import by.mitso.berezkina.factor.Factory;
import by.mitso.berezkina.field.DynamicField;
import by.mitso.berezkina.field.FieldUtil;
import by.mitso.berezkina.field.InputField;
import by.mitso.berezkina.form.FormSubmitButton;
import by.mitso.berezkina.form.InputFormModel;
import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.ColumnList;
import by.mitso.berezkina.table.CrudTableModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CustomerController", urlPatterns = { "/customer/*", "/customers/*" })
public class CustomerController extends BaseController {

    private static final String ADD_CUSTOMER = "/customer/add";
    private static final String GET_CUSTOMERS = "/customers";
    private static final String EDIT_CUSTOMER = "/customer/edit";
    private static final String DELETE_CUSTOMER = "/customer/delete";

    private static final String PLACE_IN_ROOM_PARAMETER = "placeInRoom";

    private static final Field GENDERS_FIELD = new DynamicField(CustomerField.GENDER.getName(), CustomerField.GENDER.getCaption(),
            Set.class, true, null, null);
    private static final Factory<Customer> CUSTOMER_FACTORY = CustomerFactory.getInstance();

    private CrudRepository<Customer, Long> customerRepository;

    @Override
    public void init() throws ServletException {
        customerRepository = new CrudRepositoryImpl<>(getSessionFactory().openSession(), Customer.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isAction(req, ADD_CUSTOMER)) {
            Set<InputField> inputFields = FieldUtil.getCustomerOrderedInputFields();
            InputField gendersField = FieldUtil.convertToInputField(GENDERS_FIELD);
            Set<String> genderNames = new LinkedHashSet<>();
            for(Gender gender : Gender.values()) {
                genderNames.add(gender.getName());
            }
            gendersField.setValues(genderNames);
            inputFields.add(gendersField);
            CustomerField identifierNumberField = CustomerField.IDENTIFIER_NUMBER;
            if(req.getParameterMap().containsKey(identifierNumberField.getName())) {
                inputFields.stream()
                        .filter(inputField -> inputField.getField().getName().equals(identifierNumberField.getName()))
                        .findFirst().ifPresent(inputField -> inputField.setValue(req.getParameter(identifierNumberField.getName())));
            }
            InputFormModel inputFormModel = new InputFormModel(
                    "Форма клиента",
                    "createCustomer",
                    req.getContextPath() + ADD_CUSTOMER,
                    inputFields,
                    "Создать");
            FormSubmitButton placeInRoom = new FormSubmitButton("Поселить", req.getContextPath() + ADD_CUSTOMER + "?" + PLACE_IN_ROOM_PARAMETER);
            inputFormModel.getSubmitButtons().add(placeInRoom);
            forwardStandardForm(req, resp, inputFormModel);
        }
        else if(isAction(req, GET_CUSTOMERS)) {
            CrudTableModel<Customer> tableModel = createCustomerTableModel(req);
            forwardCrudTable(req, resp, tableModel);
        }
        else if(isAction(req, EDIT_CUSTOMER)) {
            Long id = Long.valueOf(req.getParameter(CustomerField.ID.getName()));
            Optional<Customer> customerToEdit = customerRepository.findById(id);
            if(customerToEdit.isPresent()) {
                Set<InputField> inputFields = FieldUtil.getCustomerOrderedInputFields();
                for(InputField inputField : inputFields) {
                    Object value = customerToEdit.get().getFieldValue(inputField.getField());
                    inputField.setValues(Collections.singleton(value.toString()));
                }
                InputField gendersField = FieldUtil.convertToInputField(GENDERS_FIELD);
                Set<String> genderNames = new LinkedHashSet<>();
                for(Gender gender : Gender.values()) {
                    genderNames.add(gender.getName());
                }
                gendersField.setValues(genderNames);
                gendersField.setSelectedValue(customerToEdit.get().getGender().getName());
                inputFields.add(gendersField);
                InputFormModel inputFormModel = new InputFormModel(
                        "Форма клиента",
                        "editCustomer",
                        req.getContextPath() + EDIT_CUSTOMER + "?id=" + id,
                        inputFields,
                        "Обновить");
                forwardStandardForm(req, resp, inputFormModel);
            }
        }
        else if(isAction(req, DELETE_CUSTOMER)) {
            Long id = Long.valueOf(req.getParameter(CustomerField.ID.getName()));
            customerRepository.deleteById(id);
            resp.sendRedirect(req.getContextPath() + GET_CUSTOMERS);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isAction(req, ADD_CUSTOMER)) {
            Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(Customer.getAllFields(), req);
            Customer customer = CUSTOMER_FACTORY.factor(fieldValueMap);
            Customer saved = customerRepository.save(customer);
            if(req.getParameterMap().containsKey(PLACE_IN_ROOM_PARAMETER)) {
                req.getSession().setAttribute(AssignmentController.SELECTED_CUSTOMER_ATTRIBUTE, saved);
                resp.sendRedirect(req.getContextPath() + AssignmentController.SELECT_ROOM);
            }
            else {
                resp.sendRedirect(req.getContextPath() + GET_CUSTOMERS);
            }
        }
        else if(isAction(req, EDIT_CUSTOMER)) {
            Long id = Long.valueOf(req.getParameter(CustomerField.ID.getName()));
            if(customerRepository.existsById(id)) {
                Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(Customer.getAllFields(), req);
                Customer customer = CUSTOMER_FACTORY.factor(fieldValueMap);
                customer.setId(id);
                customerRepository.save(customer);
                resp.sendRedirect(req.getContextPath() + GET_CUSTOMERS);
            }
        }
    }

    private CrudTableModel<Customer> createCustomerTableModel(HttpServletRequest req) {
        List<Customer> customers = new ArrayList<>();
        for(Customer customer : customerRepository.findAll()) {
            customers.add(customer);
        }
        CrudTableModel<Customer> tableModel = new CrudTableModel<>("Таблица клиентов", customers) {

            @Override
            protected ColumnList createColumnList() {
                ColumnList list = new ColumnList();
                list.add(CustomerField.ID).setCaption("#");
                list.add(CustomerField.FULL_NAME);
                list.add(CustomerField.PASSPORT_ID);
                list.add(CustomerField.IDENTIFIER_NUMBER);
                list.add(CustomerField.COUNTRY);
                list.add(CustomerField.NATIONALITY);
                list.add(CustomerField.BIRTHDAY);
                list.add(CustomerField.GENDER);
                return list;
            }

            @Override
            public Object getValueAt(Customer customer, Column column) {
                Field field = column.getField();
                if(field == CustomerField.GENDER) {
                    return customer.getGender().getShortName();
                }
                return super.getValueAt(customer, column);
            }
        };
        tableModel.setCreateAction(req.getContextPath() + ADD_CUSTOMER);
        tableModel.setEditAction(req.getContextPath() + EDIT_CUSTOMER);
        tableModel.setDeleteAction(req.getContextPath() + DELETE_CUSTOMER);
        tableModel.setCanDelete(customer -> AccessChecker.isAdministrator(req));
        return tableModel;
    }
}
