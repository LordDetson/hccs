package by.mitso.berezkina.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import by.mitso.berezkina.form.InputForm;
import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.ColumnList;
import by.mitso.berezkina.table.TableModel;
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

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
            InputForm inputForm = new InputForm(
                    "Форма клиента",
                    "createCustomer",
                    ADD_CUSTOMER,
                    inputFields,
                    "Создать");
            forwardStandardForm(req, resp, inputForm);
        }
        else if(isAction(req, GET_CUSTOMERS)) {
            TableModel<Customer> tableModel = createCustomerTableModel();
            forwardStandardTable(req, resp, tableModel);
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
                InputForm inputForm = new InputForm(
                        "Форма клиента",
                        "editCustomer",
                        EDIT_CUSTOMER + "?id=" + id,
                        inputFields,
                        "Обновить");
                forwardStandardForm(req, resp, inputForm);
            }
        }
        else if(isAction(req, DELETE_CUSTOMER)) {
            Long id = Long.valueOf(req.getParameter(CustomerField.ID.getName()));
            customerRepository.deleteById(id);
            resp.sendRedirect(GET_CUSTOMERS);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isAction(req, ADD_CUSTOMER)) {
            Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(Customer.getAllFields(), req);
            Customer customer = CUSTOMER_FACTORY.factor(fieldValueMap);
            customerRepository.save(customer);
            resp.sendRedirect(GET_CUSTOMERS);
        }
        else if(isAction(req, EDIT_CUSTOMER)) {
            Long id = Long.valueOf(req.getParameter(CustomerField.ID.getName()));
            if(customerRepository.existsById(id)) {
                Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(Customer.getAllFields(), req);
                Customer customer = CUSTOMER_FACTORY.factor(fieldValueMap);
                customer.setId(id);
                customerRepository.save(customer);
                resp.sendRedirect(GET_CUSTOMERS);
            }
        }
    }

    private TableModel<Customer> createCustomerTableModel() {
        List<Customer> customers = new ArrayList<>();
        for(Customer customer : customerRepository.findAll()) {
            customers.add(customer);
        }
        TableModel<Customer> tableModel = new TableModel<>("Таблица клиентов", customers) {

            @Override
            protected ColumnList createColumnList() {
                ColumnList list = new ColumnList();
                list.add(CustomerField.ID).setCaption("#");
                list.add(CustomerField.FIRST_NAME);
                list.add(CustomerField.LAST_NAME);
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
                else if(field == CustomerField.BIRTHDAY) {
                    return customer.getBirthday().format(DATE_TIME_FORMATTER);
                }
                return super.getValueAt(customer, column);
            }
        };
        tableModel.setCreateAction(ADD_CUSTOMER);
        tableModel.setEditAction(EDIT_CUSTOMER);
        tableModel.setDeleteAction(DELETE_CUSTOMER);
        return tableModel;
    }
}
